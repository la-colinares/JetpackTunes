package com.lacolinares.jetpacktunes.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.lacolinares.jetpacktunes.view.theme.JetpackTunesTheme
import com.lacolinares.jetpacktunes.view.ui.home.HomeScreen
import com.lacolinares.jetpacktunes.view.ui.home.NavGraphs
import com.lacolinares.jetpacktunes.view.ui.home.destinations.HomeScreenDestination
import com.lacolinares.jetpacktunes.viewmodel.MainViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            JetpackTunesTheme {
                JetpackTunesApp(this, viewModel)
            }
        }
    }
}

@Composable
fun JetpackTunesApp(activity: MainActivity, viewModel: MainViewModel) {
    val navController = rememberNavController()

    BackHandler {
        val currentDestination: String = navController.currentDestination?.route ?: NavGraphs.root.startRoute.route
        if (currentDestination == HomeScreenDestination.route){
            activity.finish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            navController = navController,
        ){
            composable(HomeScreenDestination){
                HomeScreen(navigator = destinationsNavigator, viewModel = viewModel)
            }
        }
    }
}