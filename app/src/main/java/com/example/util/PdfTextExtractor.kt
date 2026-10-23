package com.example.util

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.regex.Pattern
import java.util.zip.Inflater
import java.util.zip.InflaterInputStream

object PdfTextExtractor {

    private const val TAG = "PdfTextExtractor"

    data class ExtractionResult(
        val success: Boolean,
        val text: String,
        val pageCountEstimate: Int = 1,
        val fileName: String = "Imported Document"
    )

    fun extractTextFromUri(context: Context, uri: Uri): ExtractionResult {
        return try {
            val contentResolver = context.contentResolver
            val mimeType = contentResolver.getType(uri) ?: ""
            var fileName = "Imported Book"

            // Get display name if possible
            contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    fileName = cursor.getString(nameIndex) ?: fileName
                }
            }

            contentResolver.openInputStream(uri)?.use { inputStream ->
                if (fileName.endsWith(".txt", ignoreCase = true) || mimeType.contains("text")) {
                    val text = inputStream.bufferedReader().use { it.readText() }
                    return ExtractionResult(true, cleanExtractedText(text), 1, fileName)
                } else {
                    // PDF extraction
                    val bytes = inputStream.readBytes()
                    val extracted = extractTextFromPdfBytes(bytes)
                    if (extracted.isNotBlank()) {
                        ExtractionResult(true, cleanExtractedText(extracted), estimatePages(bytes), fileName)
                    } else {
                        // Fallback text if scanned PDF
                        ExtractionResult(
                            false,
                            "Could not extract readable text from this PDF. It may contain scanned images rather than text. Please try a text-based PDF or paste text directly.",
                            1,
                            fileName
                        )
                    }
                }
            } ?: ExtractionResult(false, "Could not open file stream", 1, fileName)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to extract text from URI: ${e.message}", e)
            ExtractionResult(false, "Failed to parse document: ${e.localizedMessage}", 1, "Document")
        }
    }

    private fun extractTextFromPdfBytes(bytes: ByteArray): String {
        val result = StringBuilder()
        val textPattern = Pattern.compile("""\((.*?)\)\s*Tj""", Pattern.DOTALL)
        val arrayTextPattern = Pattern.compile("""\[(.*?)\]\s*TJ""", Pattern.DOTALL)

        try {
            // Find streams in PDF: stream ... endstream
            var index = 0
            while (index < bytes.size) {
                val streamStart = indexOfBytes(bytes, "stream".toByteArray(), index)
                if (streamStart == -1) break

                // Locate actual stream data start after \r?\n
                var dataStart = streamStart + 6
                if (dataStart < bytes.size && bytes[dataStart] == '\r'.code.toByte()) dataStart++
                if (dataStart < bytes.size && bytes[dataStart] == '\n'.code.toByte()) dataStart++

                val streamEnd = indexOfBytes(bytes, "endstream".toByteArray(), dataStart)
                if (streamEnd == -1) break

                val streamLength = streamEnd - dataStart
                if (streamLength > 0) {
                    val streamData = bytes.copyOfRange(dataStart, streamEnd)
                    val decompressed = tryDecompressFlate(streamData) ?: streamData
                    val streamString = String(decompressed, Charsets.ISO_8859_1)

                    // Extract (text) Tj
                    val tjMatcher = textPattern.matcher(streamString)
                    while (tjMatcher.find()) {
                        val raw = tjMatcher.group(1) ?: ""
                        val decoded = decodePdfString(raw)
                        if (decoded.isNotBlank()) {
                            result.append(decoded).append(" ")
                        }
                    }

                    // Extract [(text)-100(more)] TJ
                    val tjArrayMatcher = arrayTextPattern.matcher(streamString)
                    while (tjArrayMatcher.find()) {
                        val rawArray = tjArrayMatcher.group(1) ?: ""
                        val parts = extractStringsFromArray(rawArray)
                        for (part in parts) {
                            val decoded = decodePdfString(part)
                            if (decoded.isNotBlank()) {
                                result.append(decoded).append(" ")
                            }
                        }
                        result.append("\n\n")
                    }
                }
                index = streamEnd + 9
            }
        } catch (e: Exception) {
            Log.w(TAG, "Stream parsing encountered error, falling back: ${e.message}")
        }

        // Fallback: If result is still empty, scan raw PDF text
        if (result.isBlank()) {
            val rawPdf = String(bytes, Charsets.ISO_8859_1)
            val tjMatcher = textPattern.matcher(rawPdf)
            while (tjMatcher.find()) {
                val raw = tjMatcher.group(1) ?: ""
                val decoded = decodePdfString(raw)
                if (decoded.isNotBlank()) {
                    result.append(decoded).append(" ")
                }
            }
        }

        return result.toString().trim()
    }

    private fun tryDecompressFlate(data: ByteArray): ByteArray? {
        return try {
            val inflater = Inflater(false)
            inflater.setInput(data)
            val outputStream = ByteArrayOutputStream(data.size * 2)
            val buffer = ByteArray(4096)
            while (!inflater.finished()) {
                val count = inflater.inflate(buffer)
                if (count == 0 && inflater.needsInput()) break
                outputStream.write(buffer, 0, count)
            }
            inflater.end()
            outputStream.toByteArray()
        } catch (e: Exception) {
            // Not zlib compressed or invalid header
            null
        }
    }

    private fun extractStringsFromArray(arrayContent: String): List<String> {
        val list = mutableListOf<String>()
        val p = Pattern.compile("""\((.*?)\)""")
        val m = p.matcher(arrayContent)
        while (m.find()) {
            m.group(1)?.let { list.add(it) }
        }
        return list
    }

    private fun decodePdfString(raw: String): String {
        return raw.replace("\\n", "\n")
            .replace("\\r", "\r")
            .replace("\\t", "\t")
            .replace("\\(", "(")
            .replace("\\)", ")")
            .replace("\\\\", "\\")
    }

    private fun cleanExtractedText(text: String): String {
        return text.replace(Regex("""\r\n|\r"""), "\n")
            .replace(Regex("""[ \t]+"""), " ")
            .replace(Regex("""\n{3,}"""), "\n\n")
            .trim()
    }

    private fun estimatePages(bytes: ByteArray): Int {
        val content = String(bytes.take(bytes.size.coerceAtMost(100_000)).toByteArray(), Charsets.ISO_8859_1)
        val matches = Regex("""/Type\s*/Page\b""").findAll(content).count()
        return maxOf(1, matches)
    }

    private fun indexOfBytes(source: ByteArray, target: ByteArray, fromIndex: Int): Int {
        if (target.isEmpty() || fromIndex >= source.size) return -1
        for (i in fromIndex..(source.size - target.size)) {
            var found = true
            for (j in target.indices) {
                if (source[i + j] != target[j]) {
                    found = false
                    break
                }
            }
            if (found) return i
        }
        return -1
    }
}
