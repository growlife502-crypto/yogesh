package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TtsManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentParagraphIndex = MutableStateFlow<Int?>(null)
    val currentParagraphIndex: StateFlow<Int?> = _currentParagraphIndex.asStateFlow()

    private val _currentLanguage = MutableStateFlow("hi") // "hi" or "en"
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _currentPlayingText = MutableStateFlow("")
    val currentPlayingText: StateFlow<String> = _currentPlayingText.asStateFlow()

    private var speechSpeed = 1.0f
    private var speechPitch = 1.0f

    var onParagraphCompleted: ((Int) -> Unit)? = null

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                    utteranceId?.toIntOrNull()?.let {
                        _currentParagraphIndex.value = it
                    }
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    val finishedIndex = utteranceId?.toIntOrNull()
                    _currentParagraphIndex.value = null
                    finishedIndex?.let { idx ->
                        onParagraphCompleted?.invoke(idx)
                    }
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    _currentParagraphIndex.value = null
                }
            })
        } else {
            Log.e("TtsManager", "TTS initialization failed")
        }
    }

    fun setSpeechSpeed(speed: Float) {
        speechSpeed = speed
        tts?.setSpeechRate(speed)
    }

    fun setPitch(pitch: Float) {
        speechPitch = pitch
        tts?.setPitch(pitch)
    }

    fun speak(text: String, lang: String = "hi", paragraphIndex: Int = -1) {
        if (!isInitialized || tts == null || text.isBlank()) return

        _currentLanguage.value = lang
        _currentPlayingText.value = text

        val locale = if (lang == "hi") {
            Locale.Builder().setLanguage("hi").setRegion("IN").build()
        } else {
            Locale.US
        }

        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // Fallback to US English if Indian Hindi voice data is missing on device
            tts?.setLanguage(Locale.US)
        }

        tts?.setSpeechRate(speechSpeed)
        tts?.setPitch(speechPitch)

        val utteranceId = if (paragraphIndex >= 0) paragraphIndex.toString() else "single_utterance"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _currentParagraphIndex.value = null
        _currentPlayingText.value = ""
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
