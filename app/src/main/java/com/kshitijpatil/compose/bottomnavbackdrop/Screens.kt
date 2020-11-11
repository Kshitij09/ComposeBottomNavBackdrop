package com.kshitijpatil.compose.bottomnavbackdrop

import android.content.Intent
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp

@Composable
fun DashBoardScreen() {
    Box(
        Modifier.fillMaxSize(),
        alignment = Alignment.Center
    ) {
        Text("Dashboard Content", style = MaterialTheme.typography.h3)
    }
}

@Composable
fun SearchScreen() {
    Box(
        Modifier.fillMaxSize(),
        alignment = Alignment.Center
    ) {
        Text("Search Content", style = MaterialTheme.typography.h3)
    }
}

@Composable
fun SettingsScreen() {
    Box(
        Modifier.fillMaxSize(),
        alignment = Alignment.Center
    ) {
        Text("Settings Content", style = MaterialTheme.typography.h3)
    }
}

@Composable
fun MessagesScreen() {
    val context = ContextAmbient.current
    Box(
        Modifier.fillMaxSize(),
        alignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Messages Content", style = MaterialTheme.typography.h3)
            Button(onClick = {
                with(context) {
                    startActivity(Intent(this,BackdropScaffoldActivity::class.java))
                }
            }) {
                Text("BackdropScaffold")
            }

            Button(onClick = {
                with(context) {
                    startActivity(Intent(this,HorizontalBackdropActivity::class.java))
                }
            }) {
                Text("HorizontalBackdropScaffold")
            }
        }
    }
}