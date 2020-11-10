package com.kshitijpatil.compose.bottomnavbackdrop

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import java.util.*

sealed class BottomNavScreen(val label: String, val icon: VectorAsset) {
    object Dashboard: BottomNavScreen("Dashboard", Icons.Filled.Home)
    object Search: BottomNavScreen("Search", Icons.Filled.Search)
    object Settings: BottomNavScreen("Settings", Icons.Filled.Settings)
    object Messages: BottomNavScreen("Messages", Icons.Filled.Email)

    val route: String
        get() = this.label
            .replace(" ", "")
            .toLowerCase(Locale.ROOT)
}

val navItems: List<BottomNavScreen>
    get() = listOf(
        BottomNavScreen.Dashboard,
        BottomNavScreen.Search,
        BottomNavScreen.Settings,
        BottomNavScreen.Messages
    )

@Composable
fun BottomNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Dashboard.route
    ) {
        bottomNavComposable(BottomNavScreen.Dashboard) { DashBoardScreen() }
        bottomNavComposable(BottomNavScreen.Search) { SearchScreen() }
        bottomNavComposable(BottomNavScreen.Settings) { SettingsScreen() }
        bottomNavComposable(BottomNavScreen.Messages) { MessagesScreen() }
    }
}

private fun NavGraphBuilder.bottomNavComposable(
    screen: BottomNavScreen,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    return composable(screen.route,content=content)
}

