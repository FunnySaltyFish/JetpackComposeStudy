package com.funny.compose.study

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat

class InsetsBasicSample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Update the system bars to be translucent
//            val systemUiController = rememberSystemUiController()
//            val useDarkIcons = MaterialTheme.colors.isLight
//            SideEffect {
//                systemUiController.setSystemBarsColor(Color.Blue, darkIcons = useDarkIcons)
//            }
//
//            JetpackComposeStudyTheme() {
//                // We need to use ProvideWindowInsets to setup the necessary listeners which
//                // power the library
//                ProvideWindowInsets {
//                    InsetsBasics()
//                }
//            }
        }
    }
}

@Composable
internal fun InsetsBasics() {
    Scaffold(
        topBar = {
            // We use TopAppBar from accompanist-insets-ui which allows us to provide
            // content padding matching the system bars insets.
            TopAppBar(
                title = { Text("hhhh") },
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.9f),
//                contentPadding = rememberInsetsPaddingValues(
//                    LocalWindowInsets.current.statusBars,
//                    applyBottom = false,
//                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        bottomBar = {
            // We add a spacer as a bottom bar, which is the same height as
            // the navigation bar
            Spacer(
                Modifier
//                    .navigationBarsHeight()
                    .fillMaxWidth())
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO */ },
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Face icon"
                )
            }
        }
    ) { contentPadding ->
        // We apply the contentPadding passed to us from the Scaffold
        Box(Modifier.padding(contentPadding)) {
            // content
        }
    }
}