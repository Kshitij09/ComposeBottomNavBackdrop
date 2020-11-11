package com.kshitijpatil.compose.bottomnavbackdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.kshitijpatil.compose.bottomnavbackdrop.components.*
import com.kshitijpatil.compose.bottomnavbackdrop.ui.ComposeBottomNavBackdropTheme
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

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
    val navController = rememberNavController()
    val selection = remember { mutableStateOf(1) }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    HorizontalBackdropScaffold(
        scaffoldState = scaffoldState,
        navRail = {
            Navrail(navController)
        },
        backLayerContent = {
            /*LazyColumnFor(
                (1..5).toList(),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentSize()
            ) {
                ListItem(
                    Modifier.clickable {
                        selection.value = it
                        scaffoldState.conceal()
                    },
                    text = { Text("Select $it") }
                )
            }*/
            Box(Modifier.fillMaxHeight().preferredWidth(240.dp), alignment = Alignment.Center) {
                Text("Settings Here", style = MaterialTheme.typography.h5)
            }
        },
        frontLayerContent = {
            Box(
                Modifier.fillMaxSize(),
                alignment = Alignment.Center
            ) {
                BottomNavHost(navController)
            }
        },
        stickyFrontLayer = true
    )
}

@Composable
fun Navrail(navController: NavHostController, modifier: Modifier = Modifier) {
    ProvideWindowInsets {
        NavigationRail(modifier = modifier) {
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
                    alwaysShowLabels = false
                )
            }
        }
    }
}