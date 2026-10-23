package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ParallelReadingRow
import com.example.ui.components.ReaderSettingsSheet
import com.example.ui.components.VoiceControlBar
import com.example.ui.components.WordLookupSheet
import com.example.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    viewModel: BookViewModel,
    onBackToLibrary: () -> Unit
) {
    BackHandler {
        viewModel.stopAudio()
        onBackToLibrary()
    }

    val currentBook by viewModel.currentBook.collectAsState()
    val chapters by viewModel.bookChapters.collectAsState()
    val currentChapterIndex by viewModel.currentChapterIndex.collectAsState()
    val currentChapter by viewModel.currentChapter.collectAsState()
    val paragraphPairs by viewModel.paragraphPairs.collectAsState()
    val settings by viewModel.readerSettings.collectAsState()

    val inspectedWord by viewModel.inspectedWord.collectAsState()
    val isWordLookupOpen by viewModel.isWordLookupSheetOpen.collectAsState()
    val isSettingsOpen by viewModel.isSettingsSheetOpen.collectAsState()

    val isSpeaking by viewModel.ttsManager.isSpeaking.collectAsState()
    val currentPlayingIndex by viewModel.ttsManager.currentParagraphIndex.collectAsState()
    val isVoiceBarVisible by viewModel.isVoiceBarVisible.collectAsState()
    val isContinuousReading by viewModel.isContinuousReading.collectAsState()
    val activeVoiceLang by viewModel.activeVoiceLanguage.collectAsState()

    var chapterMenuExpanded by remember { mutableStateOf(false) }

    val wordSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val settingsSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val theme = settings.theme

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { chapterMenuExpanded = true }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentBook?.titleEn ?: "Book Reader",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select Chapter",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = currentChapter?.titleHi ?: currentChapter?.titleEn ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Chapter Selection Dropdown
                        DropdownMenu(
                            expanded = chapterMenuExpanded,
                            onDismissRequest = { chapterMenuExpanded = false }
                        ) {
                            chapters.forEachIndexed { index, ch ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = ch.titleEn,
                                                fontWeight = if (index == currentChapterIndex) FontWeight.Bold else FontWeight.Normal
                                            )
                                            Text(
                                                text = ch.titleHi,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            )
                                        }
                                    },
                                    onClick = {
                                        viewModel.selectChapter(index)
                                        chapterMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.stopAudio()
                            onBackToLibrary()
                        },
                        modifier = Modifier.testTag("reader_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Library"
                        )
                    }
                },
                actions = {
                    // Voice Text-Reader Bar Toggle
                    IconButton(
                        onClick = {
                            if (!isVoiceBarVisible) {
                                viewModel.toggleVoiceBar(true)
                                if (!isSpeaking) {
                                    viewModel.readCurrentChapterFromStart(activeVoiceLang)
                                }
                            } else {
                                viewModel.toggleVoiceBar(false)
                            }
                        },
                        modifier = Modifier.testTag("voice_read_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isSpeaking) Icons.Default.GraphicEq else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Voice Option Text Read",
                            tint = if (isSpeaking || isVoiceBarVisible) MaterialTheme.colorScheme.primary else theme.textColor
                        )
                    }

                    // Reader Settings Button
                    IconButton(
                        onClick = { viewModel.toggleSettingsSheet(true) },
                        modifier = Modifier.testTag("reader_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Reader Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = theme.backgroundColor,
                    titleContentColor = theme.textColor,
                    navigationIconContentColor = theme.textColor,
                    actionIconContentColor = theme.textColor
                )
            )
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Animated Voice Control Bar
                AnimatedVisibility(
                    visible = isVoiceBarVisible,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    VoiceControlBar(
                        isSpeaking = isSpeaking,
                        currentLanguage = activeVoiceLang,
                        currentParagraphIndex = currentPlayingIndex,
                        speechSpeed = settings.speechRate,
                        isContinuous = isContinuousReading,
                        onTogglePlayStop = { viewModel.togglePlayStopVoice() },
                        onLanguageChange = { lang ->
                            viewModel.setVoiceLanguage(lang)
                            if (isSpeaking) {
                                viewModel.togglePlayStopVoice()
                                viewModel.togglePlayStopVoice()
                            }
                        },
                        onSpeedChange = { speed -> viewModel.setSpeechSpeed(speed) },
                        onToggleContinuous = { viewModel.toggleContinuousReading() },
                        onClose = { viewModel.toggleVoiceBar(false) }
                    )
                }

                // Chapter Navigation & Progress Bar
                Surface(
                    color = theme.backgroundColor,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.previousChapter() },
                            enabled = currentChapterIndex > 0,
                            modifier = Modifier.testTag("prev_chapter_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Prev")
                        }

                        Text(
                            text = "Chapter ${currentChapterIndex + 1} of ${chapters.size.coerceAtLeast(1)}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = theme.textColor.copy(alpha = 0.8f)
                            )
                        )

                        Button(
                            onClick = { viewModel.nextChapter() },
                            enabled = currentChapterIndex < chapters.size - 1,
                            modifier = Modifier.testTag("next_chapter_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Next")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(theme.backgroundColor)
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Chapter Header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentChapter?.titleEn ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                color = theme.textColor
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = currentChapter?.titleHi ?: "",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = theme.hindiColor
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(2.dp)
                                .clip(RoundedCornerShape(1.dp))
                                .background(theme.hindiColor.copy(alpha = 0.4f))
                        )
                    }
                }

                // Paragraph Pairs
                items(paragraphPairs, key = { it.id }) { pair ->
                    val isPlaying = isSpeaking && currentPlayingIndex == pair.index

                    ParallelReadingRow(
                        pair = pair,
                        settings = settings,
                        isCurrentlyPlaying = isPlaying,
                        onWordClick = { word, contextEn ->
                            viewModel.inspectWord(word, contextEn)
                        },
                        onPlayAudio = { p, lang ->
                            viewModel.playParagraph(p, lang)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // Word Lookup Sheet
            if (isWordLookupOpen && inspectedWord != null) {
                WordLookupSheet(
                    word = inspectedWord,
                    sheetState = wordSheetState,
                    onDismiss = { viewModel.closeWordLookup() },
                    onSaveToVocab = { viewModel.saveInspectedWord() },
                    onPronounce = { text, lang ->
                        viewModel.ttsManager.speak(text, lang)
                    }
                )
            }

            // Reader Settings Sheet
            if (isSettingsOpen) {
                ReaderSettingsSheet(
                    settings = settings,
                    sheetState = settingsSheetState,
                    onDismiss = { viewModel.toggleSettingsSheet(false) },
                    onThemeSelect = { viewModel.setReaderTheme(it) },
                    onLayoutSelect = { viewModel.setLayoutMode(it) },
                    onFontSizeChange = { viewModel.setFontSize(it) },
                    onSpeechSpeedChange = { viewModel.setSpeechSpeed(it) }
                )
            }
        }
    }
}
