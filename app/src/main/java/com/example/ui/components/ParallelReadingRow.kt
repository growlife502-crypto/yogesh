package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ParagraphPair
import com.example.data.model.ReaderSettings
import com.example.data.model.ReadingLayoutMode

@Composable
fun ParallelReadingRow(
    pair: ParagraphPair,
    settings: ReaderSettings,
    isCurrentlyPlaying: Boolean,
    onWordClick: (word: String, contextEn: String) -> Unit,
    onPlayAudio: (pair: ParagraphPair, lang: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = settings.theme
    val baseFontSize = settings.fontSizeSp.sp
    val hindiFontSize = (settings.fontSizeSp * settings.hindiFontScale).sp

    val activeBgColor by animateColorAsState(
        targetValue = if (isCurrentlyPlaying) theme.hindiColor.copy(alpha = 0.12f) else Color.Transparent,
        label = "active_bg"
    )

    Box(
        modifier = modifier
            .testTag("reading_row_${pair.index}")
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(activeBgColor)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            when (settings.layoutMode) {
                ReadingLayoutMode.INTERLEAVED -> {
                    // English paragraph
                    InteractiveTextParagraph(
                        text = pair.englishText,
                        textColor = theme.textColor,
                        fontSize = baseFontSize,
                        fontFamily = FontFamily.Serif,
                        isHindi = false,
                        onWordClick = { word -> onWordClick(word, pair.englishText) }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Hindi Translation Block with accent bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(theme.hindiColor.copy(alpha = 0.06f))
                            .padding(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        // Devanagari accent pill
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(theme.hindiColor)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            InteractiveTextParagraph(
                                text = pair.hindiText,
                                textColor = theme.hindiColor,
                                fontSize = hindiFontSize,
                                fontFamily = FontFamily.Default,
                                isHindi = true,
                                onWordClick = { word -> onWordClick(word, pair.englishText) }
                            )
                        }

                        // TTS Voice Action
                        IconButton(
                            onClick = { onPlayAudio(pair, "hi") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen in Hindi",
                                tint = if (isCurrentlyPlaying) theme.hindiColor else theme.textColor.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                ReadingLayoutMode.SIDE_BY_SIDE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // English column
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(theme.textColor.copy(alpha = 0.03f))
                                .padding(8.dp)
                        ) {
                            InteractiveTextParagraph(
                                text = pair.englishText,
                                textColor = theme.textColor,
                                fontSize = (settings.fontSizeSp * 0.95f).sp,
                                fontFamily = FontFamily.Serif,
                                isHindi = false,
                                onWordClick = { word -> onWordClick(word, pair.englishText) }
                            )
                        }

                        // Hindi column
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(theme.hindiColor.copy(alpha = 0.05f))
                                .padding(8.dp)
                        ) {
                            InteractiveTextParagraph(
                                text = pair.hindiText,
                                textColor = theme.hindiColor,
                                fontSize = (settings.fontSizeSp * 0.95f * settings.hindiFontScale).sp,
                                fontFamily = FontFamily.Default,
                                isHindi = true,
                                onWordClick = { word -> onWordClick(word, pair.englishText) }
                            )
                        }
                    }
                }

                ReadingLayoutMode.HINDI_ONLY -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            InteractiveTextParagraph(
                                text = pair.hindiText,
                                textColor = theme.hindiColor,
                                fontSize = hindiFontSize,
                                fontFamily = FontFamily.Default,
                                isHindi = true,
                                onWordClick = { word -> onWordClick(word, pair.englishText) }
                            )
                        }

                        IconButton(
                            onClick = { onPlayAudio(pair, "hi") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen in Hindi",
                                tint = if (isCurrentlyPlaying) theme.hindiColor else theme.textColor.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                ReadingLayoutMode.ENGLISH_ONLY -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            InteractiveTextParagraph(
                                text = pair.englishText,
                                textColor = theme.textColor,
                                fontSize = baseFontSize,
                                fontFamily = FontFamily.Serif,
                                isHindi = false,
                                onWordClick = { word -> onWordClick(word, pair.englishText) }
                            )
                        }

                        IconButton(
                            onClick = { onPlayAudio(pair, "en") },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen in English",
                                tint = if (isCurrentlyPlaying) theme.textColor else theme.textColor.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InteractiveTextParagraph(
    text: String,
    textColor: Color,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontFamily: FontFamily,
    isHindi: Boolean,
    onWordClick: (String) -> Unit
) {
    val annotated = buildAnnotatedString {
        append(text)
    }

    // We make the text clickable so user can tap to inspect words
    ClickableText(
        text = annotated,
        style = TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontFamily = fontFamily,
            lineHeight = (fontSize.value * 1.55f).sp,
            letterSpacing = if (isHindi) 0.1.sp else 0.2.sp
        ),
        onClick = { offset ->
            // Extract word at tapped offset
            val word = extractWordAt(text, offset)
            if (word.isNotBlank()) {
                onWordClick(word)
            }
        }
    )
}

private fun extractWordAt(text: String, offset: Int): String {
    if (offset !in text.indices) return ""

    var start = offset
    while (start > 0 && !text[start - 1].isWhitespace() && text[start - 1] !in ",.!?;:\"'()[]{}«»—–") {
        start--
    }

    var end = offset
    while (end < text.length && !text[end].isWhitespace() && text[end] !in ",.!?;:\"'()[]{}«»—–") {
        end++
    }

    return text.substring(start, end).trim()
}
