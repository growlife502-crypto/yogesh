package com.example.data.repository

import com.example.data.local.BookDao
import com.example.data.local.BookEntity
import com.example.data.local.ChapterEntity
import com.example.data.local.VocabularyDao
import com.example.data.local.VocabularyEntity
import com.example.data.model.Book
import com.example.data.model.Chapter
import com.example.data.model.VocabularyWord
import com.example.translation.BookTranslationEngine
import com.example.translation.TranslationStyle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class BookRepository(
    private val bookDao: BookDao,
    private val vocabularyDao: VocabularyDao
) {
    val allBooks: Flow<List<Book>> = bookDao.getAllBooks().map { list ->
        list.map { it.toDomain() }
    }

    val allVocabulary: Flow<List<VocabularyWord>> = vocabularyDao.getAllVocabulary().map { list ->
        list.map { it.toDomain() }
    }

    val favoriteVocabulary: Flow<List<VocabularyWord>> = vocabularyDao.getFavoriteVocabulary().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val existing = bookDao.getBookById("book_little_prince")
        if (existing == null) {
            val initialBooks = PreloadedBooks.getInitialBooks().map { BookEntity.fromDomain(it) }
            bookDao.insertBooks(initialBooks)

            val initialChapters = PreloadedBooks.getInitialChapters().map { ChapterEntity.fromDomain(it) }
            bookDao.insertChapters(initialChapters)

            val initialVocab = PreloadedBooks.getInitialVocabulary().map { VocabularyEntity.fromDomain(it) }
            vocabularyDao.insertWords(initialVocab)
        }
    }

    suspend fun getBook(bookId: String): Book? = withContext(Dispatchers.IO) {
        bookDao.getBookById(bookId)?.toDomain()
    }

    fun getChapters(bookId: String): Flow<List<Chapter>> {
        return bookDao.getChaptersForBook(bookId).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getChapter(chapterId: String): Chapter? = withContext(Dispatchers.IO) {
        bookDao.getChapterById(chapterId)?.toDomain()
    }

    suspend fun updateReadingProgress(bookId: String, chapterIndex: Int, progress: Int) = withContext(Dispatchers.IO) {
        bookDao.updateReadingProgress(bookId, chapterIndex, progress, System.currentTimeMillis())
    }

    suspend fun saveCustomBookWithTranslation(
        titleEn: String,
        author: String,
        englishText: String,
        style: TranslationStyle = TranslationStyle.LITERARY
    ): Book = withContext(Dispatchers.IO) {
        val bookId = "custom_${UUID.randomUUID().toString().take(8)}"
        val titleHi = BookTranslationEngine.translateSentence(titleEn, style).ifBlank { titleEn }

        // Split text into chapters if delimited, or create Chapter 1
        val rawChapters = if (englishText.contains("Chapter ", ignoreCase = true) || englishText.contains("अध्याय ")) {
            englishText.split(Regex("""(?i)(?=Chapter \d+|अध्याय \d+)""")).filter { it.isNotBlank() }
        } else {
            listOf(englishText)
        }

        val chaptersList = mutableListOf<ChapterEntity>()

        rawChapters.forEachIndexed { idx, chText ->
            val chTitleEn = "Chapter ${idx + 1}"
            val chTitleHi = "अध्याय ${idx + 1}"
            val translatedHi = BookTranslationEngine.translateParagraph(chText, style)

            val chapterEntity = ChapterEntity(
                id = "${bookId}_ch$idx",
                bookId = bookId,
                chapterIndex = idx,
                titleEn = chTitleEn,
                titleHi = chTitleHi,
                contentEn = chText.trim(),
                contentHi = translatedHi.trim(),
                isCompleted = false
            )
            chaptersList.add(chapterEntity)
        }

        val bookEntity = BookEntity(
            id = bookId,
            titleEn = titleEn,
            titleHi = titleHi,
            author = author.ifBlank { "Unknown Author" },
            descriptionEn = "User translated book with ${chaptersList.size} chapter(s).",
            descriptionHi = "उपयोगकर्ता द्वारा अनूदित पुस्तक (${chaptersList.size} अध्याय)।",
            category = "My Translations",
            coverColorHex = 0xFF8A3B14,
            totalChapters = chaptersList.size,
            currentChapterIndex = 0,
            progressPercent = 0,
            lastReadTimestamp = System.currentTimeMillis(),
            isCustom = true
        )

        bookDao.insertBook(bookEntity)
        bookDao.insertChapters(chaptersList)

        bookEntity.toDomain()
    }

    suspend fun deleteBook(bookId: String) = withContext(Dispatchers.IO) {
        bookDao.deleteChaptersForBook(bookId)
        bookDao.deleteBook(bookId)
    }

    suspend fun addVocabularyWord(word: VocabularyWord): Long = withContext(Dispatchers.IO) {
        vocabularyDao.insertWord(VocabularyEntity.fromDomain(word))
    }

    suspend fun toggleFavoriteWord(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        vocabularyDao.updateFavorite(id, isFavorite)
    }

    suspend fun deleteVocabularyWord(id: Long) = withContext(Dispatchers.IO) {
        vocabularyDao.deleteWord(id)
    }
}
