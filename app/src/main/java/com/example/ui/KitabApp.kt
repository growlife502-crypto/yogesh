package com.example.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.ReaderScreen
import com.example.ui.screens.TranslateScreen
import com.example.ui.screens.VocabularyScreen
import com.example.viewmodel.BookViewModel

enum class AppDestination(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    LIBRARY("Library", Icons.Default.AutoStories),
    TRANSLATE("Translate", Icons.Default.Translate),
    VOCABULARY("Vocabulary", Icons.Default.Bookmark)
}

@Composable
fun KitabApp(
    viewModel: BookViewModel = viewModel()
) {
    var currentDestination by remember { mutableStateOf(AppDestination.LIBRARY) }
    var isReadingBook by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val allBooks by viewModel.allBooks.collectAsState()

    if (isReadingBook) {
        ReaderScreen(
            viewModel = viewModel,
            onBackToLibrary = { isReadingBook = false }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .testTag("bottom_nav_bar")
                ) {
                    AppDestination.entries.forEach { dest ->
                        val isSelected = currentDestination == dest
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentDestination = dest },
                            icon = { Icon(dest.icon, contentDescription = dest.label) },
                            label = { Text(dest.label) },
                            modifier = Modifier.testTag("nav_${dest.name.lowercase()}"),
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentDestination) {
                    AppDestination.LIBRARY -> {
                        LibraryScreen(
                            books = allBooks,
                            onBookClick = { book ->
                                viewModel.openBook(book)
                                isReadingBook = true
                            },
                            onNavigateToTranslate = {
                                currentDestination = AppDestination.TRANSLATE
                            },
                            onUploadPdfSelected = { uri ->
                                viewModel.handlePdfUpload(context, uri) {
                                    currentDestination = AppDestination.TRANSLATE
                                }
                                currentDestination = AppDestination.TRANSLATE
                            },
                            onDeleteBook = { bookId ->
                                viewModel.deleteBook(bookId)
                            }
                        )
                    }

                    AppDestination.TRANSLATE -> {
                        TranslateScreen(
                            viewModel = viewModel,
                            onBookSaved = { savedBook ->
                                viewModel.openBook(savedBook)
                                isReadingBook = true
                            }
                        )
                    }

                    AppDestination.VOCABULARY -> {
                        VocabularyScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
