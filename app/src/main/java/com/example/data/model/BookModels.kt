package com.example.data.model

data class Book(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val author: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val category: String,
    val coverColorHex: Long = 0xFF8A3B14,
    val totalChapters: Int = 1,
    val currentChapterIndex: Int = 0,
    val progressPercent: Int = 0,
    val lastReadTimestamp: Long = System.currentTimeMillis(),
    val isCustom: Boolean = false
)

data class Chapter(
    val id: String,
    val bookId: String,
    val chapterIndex: Int,
    val titleEn: String,
    val titleHi: String,
    val contentEn: String,
    val contentHi: String,
    val isCompleted: Boolean = false
) {
    fun toParagraphPairs(): List<ParagraphPair> {
        val parasEn = contentEn.split("\n\n").map { it.trim() }.filter { it.isNotEmpty() }
        val parasHi = contentHi.split("\n\n").map { it.trim() }.filter { it.isNotEmpty() }

        val size = maxOf(parasEn.size, parasHi.size)
        return (0 until size).map { i ->
            ParagraphPair(
                id = "${id}_p$i",
                index = i,
                englishText = parasEn.getOrElse(i) { "" },
                hindiText = parasHi.getOrElse(i) { "" }
            )
        }
    }
}

data class ParagraphPair(
    val id: String,
    val index: Int,
    val englishText: String,
    val hindiText: String
)

data class VocabularyWord(
    val id: Long = 0,
    val englishWord: String,
    val hindiMeaning: String,
    val transliteration: String = "",
    val partOfSpeech: String = "Noun",
    val exampleEn: String = "",
    val exampleHi: String = "",
    val sourceBook: String = "",
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
