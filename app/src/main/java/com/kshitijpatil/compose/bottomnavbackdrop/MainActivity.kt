package com.kshitijpatil.compose.bottomnavbackdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.emptyContent
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ExperimentalSubcomposeLayoutApi
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.ui.tooling.preview.Preview
import com.kshitijpatil.compose.bottomnavbackdrop.components.NavigationRail
import com.kshitijpatil.compose.bottomnavbackdrop.components.NavigationRailItem
import com.kshitijpatil.compose.bottomnavbackdrop.ui.ComposeBottomNavBackdropTheme
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import dev.chrisbanes.accompanist.insets.navigationBarsHeight
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeBottomNavBackdropTheme {
                // A surface container using the 'background' color from the theme
                /*Surface(color = MaterialTheme.colors.background) {
                    HomeScreen()
                }*/
                NavrailScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    BackdropScaffold(
        appBar = {},
        backLayerContent = { BottomNavbar(navController) },
        frontLayerContent = { BottomNavHost(navController) },
        stickyFrontLayer = false
    )
    /*Scaffold(bottomBar = {BottomNavbar(navController)}) {
        BottomNavHost(navController)
    }*/
}

@Composable
fun BottomNavbar(navController: NavHostController) {
    ProvideWindowInsets {
        BottomNavigation(
            Modifier.navigationBarsHeight(additional = 56.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.get(KEY_ROUTE)
            navItems.forEach {
                BottomNavigationItem(
                    icon = { Icon(it.icon) },
                    selected = currentRoute == it.route,
                    label = { Text(it.label) },
                    onClick = {
                        // This is the equivalent to popUpTo the start destination
                        navController.popBackStack(navController.graph.startDestination, false)

                        // This if check gives us a "singleTop" behavior where we do not create a
                        // second instance of the composable if we are already on that destination
                        if (currentRoute != it.route) {
                            navController.navigate(it.route)
                        }
                    },
                    alwaysShowLabels = false,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

@Preview
@Composable
fun NavbarPreview() {
    val navController = rememberNavController()
    ComposeBottomNavBackdropTheme {
        BottomNavbar(navController)
    }
}

@Composable
fun Navrail(navController: NavHostController, modifier: Modifier = Modifier) {
    ProvideWindowInsets {
        NavigationRail(
            modifier.navigationBarsHeight(additional = 56.dp)
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.get(KEY_ROUTE)
            navItems.forEach {
                NavigationRailItem(
                    icon = { Icon(it.icon) },
                    selected = currentRoute == it.route,
                    label = { Text(it.label) },
                    onClick = {
                        // This is the equivalent to popUpTo the start destination
                        navController.popBackStack(navController.graph.startDestination, false)

                        // This if check gives us a "singleTop" behavior where we do not create a
                        // second instance of the composable if we are already on that destination
                        if (currentRoute != it.route) {
                            navController.navigate(it.route)
                        }
                    },
                    alwaysShowLabels = false,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}

@Composable
fun NavrailScreen() {
    val navController = rememberNavController()
    val fillMaxSizeModifier = Modifier.fillMaxSize()
    val fillMaxHeightModifier = Modifier.fillMaxHeight()
    Surface(color = MaterialTheme.colors.background) {
        Box(fillMaxSizeModifier) {
            Row {
                Navrail(
                    navController = navController,
                    fillMaxHeightModifier.align(Alignment.CenterVertically)
                )
                BottomNavHost(navController = navController)
            }
        }
    }
}

@Preview
@Composable
fun NavrailPreview() {
    ComposeBottomNavBackdropTheme {
        NavrailScreen()
    }
}