package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Book
import com.example.data.model.Chapter
import com.example.data.model.VocabularyWord

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val titleEn: String,
    val titleHi: String,
    val author: String,
    val descriptionEn: String,
    val descriptionHi: String,
    val category: String,
    val coverColorHex: Long,
    val totalChapters: Int,
    val currentChapterIndex: Int,
    val progressPercent: Int,
    val lastReadTimestamp: Long,
    val isCustom: Boolean
) {
    fun toDomain(): Book = Book(
        id = id,
        titleEn = titleEn,
        titleHi = titleHi,
        author = author,
        descriptionEn = descriptionEn,
        descriptionHi = descriptionHi,
        category = category,
        coverColorHex = coverColorHex,
        totalChapters = totalChapters,
        currentChapterIndex = currentChapterIndex,
        progressPercent = progressPercent,
        lastReadTimestamp = lastReadTimestamp,
        isCustom = isCustom
    )

    companion object {
        fun fromDomain(book: Book): BookEntity = BookEntity(
            id = book.id,
            titleEn = book.titleEn,
            titleHi = book.titleHi,
            author = book.author,
            descriptionEn = book.descriptionEn,
            descriptionHi = book.descriptionHi,
            category = book.category,
            coverColorHex = book.coverColorHex,
            totalChapters = book.totalChapters,
            currentChapterIndex = book.currentChapterIndex,
            progressPercent = book.progressPercent,
            lastReadTimestamp = book.lastReadTimestamp,
            isCustom = book.isCustom
        )
    }
}

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey val id: String,
    val bookId: String,
    val chapterIndex: Int,
    val titleEn: String,
    val titleHi: String,
    val contentEn: String,
    val contentHi: String,
    val isCompleted: Boolean = false
) {
    fun toDomain(): Chapter = Chapter(
        id = id,
        bookId = bookId,
        chapterIndex = chapterIndex,
        titleEn = titleEn,
        titleHi = titleHi,
        contentEn = contentEn,
        contentHi = contentHi,
        isCompleted = isCompleted
    )

    companion object {
        fun fromDomain(chapter: Chapter): ChapterEntity = ChapterEntity(
            id = chapter.id,
            bookId = chapter.bookId,
            chapterIndex = chapter.chapterIndex,
            titleEn = chapter.titleEn,
            titleHi = chapter.titleHi,
            contentEn = chapter.contentEn,
            contentHi = chapter.contentHi,
            isCompleted = chapter.isCompleted
        )
    }
}

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val englishWord: String,
    val hindiMeaning: String,
    val transliteration: String,
    val partOfSpeech: String,
    val exampleEn: String,
    val exampleHi: String,
    val sourceBook: String,
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toDomain(): VocabularyWord = VocabularyWord(
        id = id,
        englishWord = englishWord,
        hindiMeaning = hindiMeaning,
        transliteration = transliteration,
        partOfSpeech = partOfSpeech,
        exampleEn = exampleEn,
        exampleHi = exampleHi,
        sourceBook = sourceBook,
        isFavorite = isFavorite,
        timestamp = timestamp
    )

    companion object {
        fun fromDomain(word: VocabularyWord): VocabularyEntity = VocabularyEntity(
            id = word.id,
            englishWord = word.englishWord,
            hindiMeaning = word.hindiMeaning,
            transliteration = word.transliteration,
            partOfSpeech = word.partOfSpeech,
            exampleEn = word.exampleEn,
            exampleHi = word.exampleHi,
            sourceBook = word.sourceBook,
            isFavorite = word.isFavorite,
            timestamp = word.timestamp
        )
    }
}
