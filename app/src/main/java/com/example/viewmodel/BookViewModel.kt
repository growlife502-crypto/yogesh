package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.TtsManager
import com.example.data.local.AppDatabase
import com.example.data.model.Book
import com.example.data.model.Chapter
import com.example.data.model.ParagraphPair
import com.example.data.model.ReaderSettings
import com.example.data.model.ReaderTheme
import com.example.data.model.ReadingLayoutMode
import com.example.data.model.VocabularyWord
import com.example.data.repository.BookRepository
import com.example.translation.BookTranslationEngine
import com.example.translation.TranslationStyle
import com.example.util.PdfTextExtractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class TranslationScreenState(
    val inputText: String = "",
    val bookTitle: String = "",
    val author: String = "",
    val translationStyle: TranslationStyle = TranslationStyle.LITERARY,
    val isTranslating: Boolean = false,
    val previewPairs: List<ParagraphPair> = emptyList(),
    val savedSuccessMessage: String? = null,
    val isUploadingPdf: Boolean = false,
    val uploadStatusMessage: String? = null
)

class BookViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = BookRepository(database.bookDao(), database.vocabularyDao())
    val ttsManager = TtsManager(application)

    val allBooks: StateFlow<List<Book>> = repository.allBooks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allVocabulary: StateFlow<List<VocabularyWord>> = repository.allVocabulary
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Reader State
    private val _currentBook = MutableStateFlow<Book?>(null)
    val currentBook: StateFlow<Book?> = _currentBook.asStateFlow()

    private val _bookChapters = MutableStateFlow<List<Chapter>>(emptyList())
    val bookChapters: StateFlow<List<Chapter>> = _bookChapters.asStateFlow()

    private val _currentChapterIndex = MutableStateFlow(0)
    val currentChapterIndex: StateFlow<Int> = _currentChapterIndex.asStateFlow()

    private val _currentChapter = MutableStateFlow<Chapter?>(null)
    val currentChapter: StateFlow<Chapter?> = _currentChapter.asStateFlow()

    private val _paragraphPairs = MutableStateFlow<List<ParagraphPair>>(emptyList())
    val paragraphPairs: StateFlow<List<ParagraphPair>> = _paragraphPairs.asStateFlow()

    // Reader UI Preferences
    private val _readerSettings = MutableStateFlow(ReaderSettings())
    val readerSettings: StateFlow<ReaderSettings> = _readerSettings.asStateFlow()

    // Word Inspector State
    private val _inspectedWord = MutableStateFlow<VocabularyWord?>(null)
    val inspectedWord: StateFlow<VocabularyWord?> = _inspectedWord.asStateFlow()

    private val _isWordLookupSheetOpen = MutableStateFlow(false)
    val isWordLookupSheetOpen: StateFlow<Boolean> = _isWordLookupSheetOpen.asStateFlow()

    private val _isSettingsSheetOpen = MutableStateFlow(false)
    val isSettingsSheetOpen: StateFlow<Boolean> = _isSettingsSheetOpen.asStateFlow()

    // Voice Option Reader State
    private val _isVoiceBarVisible = MutableStateFlow(false)
    val isVoiceBarVisible: StateFlow<Boolean> = _isVoiceBarVisible.asStateFlow()

    private val _isContinuousReading = MutableStateFlow(true)
    val isContinuousReading: StateFlow<Boolean> = _isContinuousReading.asStateFlow()

    private val _activeVoiceLanguage = MutableStateFlow("hi") // "hi" or "en"
    val activeVoiceLanguage: StateFlow<String> = _activeVoiceLanguage.asStateFlow()

    // Translator Screen State
    private val _translateState = MutableStateFlow(TranslationScreenState())
    val translateState: StateFlow<TranslationScreenState> = _translateState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }

        // Configure continuous auto-advancing voice narration
        ttsManager.onParagraphCompleted = { completedIdx ->
            if (_isContinuousReading.value) {
                val pairs = _paragraphPairs.value
                val nextIdx = completedIdx + 1
                if (nextIdx in pairs.indices) {
                    val nextPair = pairs[nextIdx]
                    val lang = _activeVoiceLanguage.value
                    val text = if (lang == "hi") nextPair.hindiText else nextPair.englishText
                    if (text.isNotBlank()) {
                        ttsManager.speak(text, lang, nextIdx)
                    }
                } else {
                    _isVoiceBarVisible.value = false
                }
            }
        }
    }

    fun openBook(book: Book, chapterIndex: Int? = null) {
        _currentBook.value = book
        val targetIndex = chapterIndex ?: book.currentChapterIndex
        _currentChapterIndex.value = targetIndex

        viewModelScope.launch {
            repository.getChapters(book.id).collect { chapters ->
                _bookChapters.value = chapters
                if (chapters.isNotEmpty()) {
                    val safeIndex = targetIndex.coerceIn(0, chapters.size - 1)
                    val ch = chapters[safeIndex]
                    _currentChapter.value = ch
                    _paragraphPairs.value = ch.toParagraphPairs()
                }
            }
        }
    }

    fun selectChapter(index: Int) {
        val chapters = _bookChapters.value
        if (index in chapters.indices) {
            _currentChapterIndex.value = index
            val ch = chapters[index]
            _currentChapter.value = ch
            _paragraphPairs.value = ch.toParagraphPairs()
            ttsManager.stop()

            _currentBook.value?.let { book ->
                val progress = ((index + 1) * 100) / chapters.size
                viewModelScope.launch {
                    repository.updateReadingProgress(book.id, index, progress)
                }
            }
        }
    }

    fun nextChapter() {
        selectChapter(_currentChapterIndex.value + 1)
    }

    fun previousChapter() {
        selectChapter(_currentChapterIndex.value - 1)
    }

    // Reader Preferences updates
    fun setReaderTheme(theme: ReaderTheme) {
        _readerSettings.value = _readerSettings.value.copy(theme = theme)
    }

    fun setLayoutMode(mode: ReadingLayoutMode) {
        _readerSettings.value = _readerSettings.value.copy(layoutMode = mode)
    }

    fun setFontSize(sizeSp: Float) {
        _readerSettings.value = _readerSettings.value.copy(fontSizeSp = sizeSp.coerceIn(13f, 26f))
    }

    fun setSpeechSpeed(speed: Float) {
        _readerSettings.value = _readerSettings.value.copy(speechRate = speed)
        ttsManager.setSpeechSpeed(speed)
    }

    fun toggleSettingsSheet(open: Boolean) {
        _isSettingsSheetOpen.value = open
    }

    // Voice Narration & Text Read Controls
    fun toggleVoiceBar(visible: Boolean) {
        _isVoiceBarVisible.value = visible
    }

    fun toggleContinuousReading() {
        _isContinuousReading.value = !_isContinuousReading.value
    }

    fun setVoiceLanguage(lang: String) {
        _activeVoiceLanguage.value = lang
    }

    fun playParagraph(pair: ParagraphPair, lang: String = "hi") {
        _activeVoiceLanguage.value = lang
        _isVoiceBarVisible.value = true
        val text = if (lang == "hi") pair.hindiText else pair.englishText
        if (text.isNotBlank()) {
            ttsManager.speak(text, lang, pair.index)
        }
    }

    fun readCurrentChapterFromStart(lang: String = "hi") {
        val pairs = _paragraphPairs.value
        if (pairs.isNotEmpty()) {
            _activeVoiceLanguage.value = lang
            _isVoiceBarVisible.value = true
            playParagraph(pairs.first(), lang)
        }
    }

    fun togglePlayStopVoice() {
        if (ttsManager.isSpeaking.value) {
            ttsManager.stop()
        } else {
            val pairs = _paragraphPairs.value
            val targetIdx = ttsManager.currentParagraphIndex.value ?: 0
            if (pairs.isNotEmpty()) {
                val safeIdx = targetIdx.coerceIn(0, pairs.size - 1)
                playParagraph(pairs[safeIdx], _activeVoiceLanguage.value)
            }
        }
    }

    fun stopAudio() {
        ttsManager.stop()
        _isVoiceBarVisible.value = false
    }

    // Word Inspector
    fun inspectWord(wordText: String, contextEn: String = "") {
        val found = BookTranslationEngine.lookupWord(wordText) ?: VocabularyWord(
            englishWord = wordText.replace(Regex("""[^\w]"""), "").replaceFirstChar { it.uppercase() },
            hindiMeaning = BookTranslationEngine.translateSentence(wordText),
            transliteration = "",
            partOfSpeech = "Word",
            exampleEn = contextEn,
            sourceBook = _currentBook.value?.titleEn ?: "Book Reader"
        )
        _inspectedWord.value = found
        _isWordLookupSheetOpen.value = true
    }

    fun closeWordLookup() {
        _isWordLookupSheetOpen.value = false
    }

    fun saveInspectedWord() {
        val word = _inspectedWord.value ?: return
        viewModelScope.launch {
            repository.addVocabularyWord(word)
            _isWordLookupSheetOpen.value = false
        }
    }

    fun toggleFavoriteVocab(word: VocabularyWord) {
        viewModelScope.launch {
            repository.toggleFavoriteWord(word.id, !word.isFavorite)
        }
    }

    fun deleteVocab(wordId: Long) {
        viewModelScope.launch {
            repository.deleteVocabularyWord(wordId)
        }
    }

    // PDF / Document Upload & Text Extraction
    fun handlePdfUpload(context: Context, uri: Uri, onNavigateToTranslate: () -> Unit) {
        _translateState.value = _translateState.value.copy(
            isUploadingPdf = true,
            uploadStatusMessage = "Extracting text from PDF..."
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = PdfTextExtractor.extractTextFromUri(context, uri)
            withContext(Dispatchers.Main) {
                if (result.success && result.text.isNotBlank()) {
                    val cleanTitle = result.fileName.removeSuffix(".pdf").removeSuffix(".PDF")
                        .replace("_", " ").replace("-", " ")

                    _translateState.value = _translateState.value.copy(
                        inputText = result.text,
                        bookTitle = cleanTitle,
                        isUploadingPdf = false,
                        uploadStatusMessage = "PDF extracted successfully (${result.text.length} chars)!"
                    )
                    translateCurrentInput()
                    onNavigateToTranslate()
                } else {
                    _translateState.value = _translateState.value.copy(
                        isUploadingPdf = false,
                        uploadStatusMessage = result.text
                    )
                }
            }
        }
    }

    // Voice Dictation / Speech-to-Text Input
    fun onSpeechRecognized(spokenText: String) {
        val current = _translateState.value.inputText
        val updated = if (current.isBlank()) spokenText else "$current\n$spokenText"
        updateTranslateInput(updated)
        translateCurrentInput()
    }

    // Custom Translation Screen Actions
    fun updateTranslateInput(text: String) {
        _translateState.value = _translateState.value.copy(inputText = text, savedSuccessMessage = null)
    }

    fun updateTranslateTitle(title: String) {
        _translateState.value = _translateState.value.copy(bookTitle = title)
    }

    fun updateTranslateAuthor(author: String) {
        _translateState.value = _translateState.value.copy(author = author)
    }

    fun setTranslateStyle(style: TranslationStyle) {
        _translateState.value = _translateState.value.copy(translationStyle = style)
        if (_translateState.value.inputText.isNotBlank()) {
            translateCurrentInput()
        }
    }

    fun translateCurrentInput() {
        val text = _translateState.value.inputText
        if (text.isBlank()) return

        _translateState.value = _translateState.value.copy(isTranslating = true)
        viewModelScope.launch {
            val pairs = BookTranslationEngine.translateBookText(text, _translateState.value.translationStyle)
            _translateState.value = _translateState.value.copy(
                previewPairs = pairs,
                isTranslating = false
            )
        }
    }

    fun saveTranslatedBook(onSuccess: (Book) -> Unit) {
        val state = _translateState.value
        if (state.inputText.isBlank()) return

        viewModelScope.launch {
            val title = if (state.bookTitle.isNotBlank()) state.bookTitle else "Translated Book"
            val newBook = repository.saveCustomBookWithTranslation(
                titleEn = title,
                author = state.author.ifBlank { "Unknown" },
                englishText = state.inputText,
                style = state.translationStyle
            )
            _translateState.value = _translateState.value.copy(
                savedSuccessMessage = "Book '${newBook.titleEn}' saved to your Library!"
            )
            onSuccess(newBook)
        }
    }

    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            repository.deleteBook(bookId)
            if (_currentBook.value?.id == bookId) {
                _currentBook.value = null
                _currentChapter.value = null
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
