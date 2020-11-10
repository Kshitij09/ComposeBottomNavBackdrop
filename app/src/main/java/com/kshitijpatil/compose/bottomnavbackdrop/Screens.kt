package com.kshitijpatil.compose.bottomnavbackdrop

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
    Box(
        Modifier.fillMaxSize(),
        alignment = Alignment.Center
    ) {
        Text("Messages Content", style = MaterialTheme.typography.h3)
    }
}