package com.castle.FlowLink.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.castle.FlowLink.navigation.Graph
import com.castle.FlowLink.navigation.MainRouteScreen
import com.castle.FlowLink.navigation.SettingsRouteScreen
import com.castle.FlowLink.presentation.about.AboutScreen
import com.castle.FlowLink.presentation.network.TrustedNetworkScreen
import com.castle.FlowLink.presentation.permission.PermissionScreen
import com.castle.FlowLink.presentation.settings.update.NewUpdateScreen

fun NavGraphBuilder.settingsNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.SettingsGraph,
        startDestination = MainRouteScreen.SettingsScreen.route
    ) {
        composable(route = SettingsRouteScreen.NetworkScreen.route) {
            TrustedNetworkScreen(rootNavController)
        }
        composable(route = SettingsRouteScreen.AboutScreen.route) {
            AboutScreen(rootNavController)
        }
        composable(route = SettingsRouteScreen.PermissionScreen.route) {
            PermissionScreen(rootNavController,)
        }
        composable(route = SettingsRouteScreen.NewUpdateScreen.route) {
            NewUpdateScreen(rootNavController)
        }
    }
}
