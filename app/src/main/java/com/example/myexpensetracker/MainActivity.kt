package com.example.myexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.pages.AppNavigation
import com.example.myexpensetracker.ui.theme.MyExpenseTrackerTheme

//@MainActivity.AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //annotation class AndroidEntryPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyExpenseTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {

            var selectedBottomBar: NavigationItem by remember {
                mutableStateOf(NavigationItem.HomePage)
            }

            MyBottomAppBar(selectedBottomBar, onBottomBarSelect = {
                selectedBottomBar = it
                navController.navigate(it.route)
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addTransaction") },
                containerColor = colorResource(id = R.color.blue)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            AppNavigation(navController, Modifier)
        }
    }
}

sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object HomePage : NavigationItem("home", Icons.Default.Home, "Home")
    object Transactions : NavigationItem("transactions", Icons.AutoMirrored.Filled.List, "Transactions")
    object Statistics : NavigationItem("stats", Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Statistics")
    object Profile : NavigationItem("profile", Icons.Default.AccountCircle, "Profile")
}

@Composable
fun MyBottomAppBar(selectedBottomBar: NavigationItem, onBottomBarSelect: (NavigationItem) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = NavigationItem.HomePage.icon,
                    contentDescription = NavigationItem.HomePage.title,
                    tint = if (selectedBottomBar == NavigationItem.HomePage) {
                        Color.Black
                    } else {
                        colorResource(id = R.color.TextGreyColor)
                    }
                )
            },
            selected = selectedBottomBar == NavigationItem.HomePage,
            onClick = { onBottomBarSelect(NavigationItem.HomePage) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = NavigationItem.Transactions.icon,
                    contentDescription = NavigationItem.Transactions.title,
                    tint = if (selectedBottomBar == NavigationItem.Transactions) {
                        Color.Black
                    } else {
                        colorResource(id = R.color.TextGreyColor)
                    }
                )
            },
            selected = selectedBottomBar == NavigationItem.Transactions,
            onClick = { onBottomBarSelect(NavigationItem.Transactions) }
        )

        // Spacer for the FAB
        Spacer(Modifier.weight(1f, true))

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = NavigationItem.Statistics.icon,
                    contentDescription = NavigationItem.Statistics.title,
                    tint = if (selectedBottomBar == NavigationItem.Statistics) {
                        Color.Black
                    } else {
                        colorResource(id = R.color.TextGreyColor)
                    }
                )
            },
            selected = selectedBottomBar == NavigationItem.Statistics,
            onClick = { onBottomBarSelect(NavigationItem.Statistics) }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = NavigationItem.Profile.icon,
                    contentDescription = NavigationItem.Profile.title,
                    tint = if (selectedBottomBar == NavigationItem.Profile) {
                        Color.Black
                    } else {
                        colorResource(id = R.color.TextGreyColor)
                    }
                )
            },
            selected = selectedBottomBar == NavigationItem.Profile,
            onClick = { onBottomBarSelect(NavigationItem.Profile) }
        )
    }
}


