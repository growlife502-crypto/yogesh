package com.example

import com.example.data.repository.PreloadedBooks
import com.example.translation.BookTranslationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BookTranslationTest {

    @Test
    fun testIdiomPhraseTranslation() {
        val input = "Once upon a time in a faraway land."
        val translated = BookTranslationEngine.translateSentence(input)
        assertTrue(translated.contains("एक समय की बात है"))
    }

    @Test
    fun testWordLookup() {
        val word = BookTranslationEngine.lookupWord("essential")
        assertNotNull(word)
        assertEquals("Essential", word?.englishWord)
        assertTrue(word?.hindiMeaning?.contains("महत्वपूर्ण") == true || word?.hindiMeaning?.contains("अत्यावश्यक") == true)
    }

    @Test
    fun testPreloadedBooks() {
        val books = PreloadedBooks.getInitialBooks()
        assertTrue(books.isNotEmpty())
        val littlePrince = books.find { it.id == "book_little_prince" }
        assertNotNull(littlePrince)
        assertEquals("The Little Prince", littlePrince?.titleEn)
        assertEquals("छोटा राजकुमार", littlePrince?.titleHi)
    }

    @Test
    fun testChapterParagraphPairing() {
        val chapters = PreloadedBooks.getInitialChapters()
        val firstChapter = chapters.first()
        val pairs = firstChapter.toParagraphPairs()
        assertTrue(pairs.isNotEmpty())
        assertTrue(pairs.first().englishText.isNotBlank())
        assertTrue(pairs.first().hindiText.isNotBlank())
    }
}
