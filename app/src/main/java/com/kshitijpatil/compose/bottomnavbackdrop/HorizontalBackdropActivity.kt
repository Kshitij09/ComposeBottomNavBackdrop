package com.kshitijpatil.compose.bottomnavbackdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import com.kshitijpatil.compose.bottomnavbackdrop.components.BackdropValue
import com.kshitijpatil.compose.bottomnavbackdrop.components.HorizontalBackdropScaffold
import com.kshitijpatil.compose.bottomnavbackdrop.components.rememberBackdropScaffoldState
import com.kshitijpatil.compose.bottomnavbackdrop.ui.ComposeBottomNavBackdropTheme

class HorizontalBackdropActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBottomNavBackdropTheme {
                Surface(color = MaterialTheme.colors.background) {
                    BackdropContent()
                }
            }
        }
    }
}

@Composable
fun Greetings() {
    Text("Hello")
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun BackdropContent() {
    val scope = rememberCoroutineScope()
    val selection = remember { mutableStateOf(1) }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    HorizontalBackdropScaffold(
        scaffoldState = scaffoldState,
        backLayerContent = {
            LazyColumnFor((1..5).toList(), horizontalAlignment = Alignment.CenterHorizontally) {
                ListItem(
                    Modifier.clickable {
                        selection.value = it
                        scaffoldState.conceal()
                    },
                    text = { Text("Select $it") }
                )
            }
        },
        frontLayerContent = {
            Box(
                Modifier.fillMaxSize(),
                alignment = Alignment.Center
            ) {
                Text("Selection: ${selection.value}")
            }
        }
    )
}