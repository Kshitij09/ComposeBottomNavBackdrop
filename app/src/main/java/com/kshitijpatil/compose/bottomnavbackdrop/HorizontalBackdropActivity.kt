package com.kshitijpatil.compose.bottomnavbackdrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import com.kshitijpatil.compose.bottomnavbackdrop.ui.ComposeBottomNavBackdropTheme
import kotlinx.coroutines.launch
import com.kshitijpatil.compose.bottomnavbackdrop.components.HorizontalBackdropScaffold
import com.kshitijpatil.compose.bottomnavbackdrop.components.BackdropValue
import com.kshitijpatil.compose.bottomnavbackdrop.components.rememberBackdropScaffoldState

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
            LazyColumnFor((1..5).toList()) {
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