package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CleanWhiteBg
import com.example.ui.theme.CleanWhiteHindiText
import com.example.ui.theme.CleanWhiteText
import com.example.ui.theme.CreamPaperBg
import com.example.ui.theme.CreamPaperHindiText
import com.example.ui.theme.CreamPaperText
import com.example.ui.theme.NightBg
import com.example.ui.theme.NightHindiText
import com.example.ui.theme.NightText
import com.example.ui.theme.SepiaBg
import com.example.ui.theme.SepiaHindiText
import com.example.ui.theme.SepiaText

enum class ReaderTheme(
    val displayName: String,
    val hindiName: String,
    val backgroundColor: Color,
    val textColor: Color,
    val hindiColor: Color
) {
    CREAM("Paper Cream", "कागज़", CreamPaperBg, CreamPaperText, CreamPaperHindiText),
    SEPIA("Warm Sepia", "सेपिया", SepiaBg, SepiaText, SepiaHindiText),
    NIGHT("Midnight Dark", "रात्रि", NightBg, NightText, NightHindiText),
    LIGHT("Clean Light", "श्वेत", CleanWhiteBg, CleanWhiteText, CleanWhiteHindiText)
}

enum class ReadingLayoutMode(
    val title: String,
    val hindiTitle: String,
    val description: String
) {
    INTERLEAVED("Bilingual Parallel", "द्विभाषी समानांतर", "English paragraph with Hindi translation below"),
    SIDE_BY_SIDE("Side-by-Side Split", "आस-पास विभाजन", "English on left, Hindi on right"),
    HINDI_ONLY("Hindi Translation", "केवल हिन्दी", "Pure Hindi translation reading view"),
    ENGLISH_ONLY("English Original", "केवल अंग्रेज़ी", "Original English text only")
}

data class ReaderSettings(
    val theme: ReaderTheme = ReaderTheme.CREAM,
    val layoutMode: ReadingLayoutMode = ReadingLayoutMode.INTERLEAVED,
    val fontSizeSp: Float = 17f,
    val hindiFontScale: Float = 1.05f, // Devanagari often benefits from slightly larger font
    val lineSpacingMultiplier: Float = 1.5f,
    val speechRate: Float = 1.0f,
    val autoScrollWithAudio: Boolean = true
)
