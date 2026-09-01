package com.castle.FlowLink.navigation.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.castle.FlowLink.navigation.Graph
import com.castle.FlowLink.navigation.OnboardingRoute
import com.castle.FlowLink.navigation.SyncRoute
import com.castle.FlowLink.navigation.transitions.NavigationTransitions
import com.castle.FlowLink.presentation.main.MainScreen
import com.castle.FlowLink.presentation.onboarding.OnboardingScreen
import com.castle.FlowLink.presentation.sync.QrCodeScanner
import com.castle.FlowLink.presentation.sync.SyncScreen

@Composable
fun RootNavGraph(startDestination: String) {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        route = Graph.RootGraph,
        startDestination = startDestination,
        enterTransition = { NavigationTransitions.rootEnterTransition(this) },
        exitTransition = { NavigationTransitions.rootExitTransition(this) },
        popEnterTransition = { NavigationTransitions.rootPopEnterTransition(this) },
        popExitTransition = { NavigationTransitions.rootPopExitTransition(this) }
    ) {
        composable(route = OnboardingRoute.OnboardingScreen.route) {
            OnboardingScreen(
                onComplete = {
                    // Navigate to main screen and clear the back stack
                    rootNavController.navigate(Graph.MainScreenGraph) {
                        popUpTo(Graph.RootGraph) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = SyncRoute.SyncScreen.route) {
            SyncScreen(rootNavController = rootNavController)
        }
        composable(route = SyncRoute.QrCodeScannerScreen.route) {
            QrCodeScanner(rootNavController = rootNavController)
        }
        composable(route = Graph.MainScreenGraph) {
            MainScreen(rootNavController = rootNavController)
        }
        deviceNavGraph(rootNavController)
        settingsNavGraph(rootNavController)
    }
}

