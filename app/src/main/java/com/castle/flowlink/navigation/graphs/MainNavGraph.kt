package com.castle.FlowLink.navigation.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.castle.FlowLink.navigation.Graph
import com.castle.FlowLink.navigation.MainRouteScreen
import com.castle.FlowLink.navigation.SettingsRouteScreen
import com.castle.FlowLink.navigation.transitions.NavigationTransitions
import com.castle.FlowLink.presentation.devices.DeviceScreen
import com.castle.FlowLink.presentation.home.HomeScreen
import com.castle.FlowLink.presentation.main.ConnectionViewModel
import com.castle.FlowLink.presentation.settings.update.NewUpdateScreen
import com.castle.FlowLink.presentation.settings.SettingsScreen

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    innerPadding: PaddingValues,
    searchQuery: String,
    connectionViewModel: ConnectionViewModel
) {
    NavHost(
        navController = homeNavController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.HomeScreen.route,
        modifier = Modifier.padding(innerPadding),
        enterTransition = { NavigationTransitions.rootEnterTransition(this) },
        exitTransition = { NavigationTransitions.rootExitTransition(this) },
        popEnterTransition = { NavigationTransitions.rootPopEnterTransition(this) },
        popExitTransition = { NavigationTransitions.rootPopExitTransition(this) }
    ) {
        composable(
            route = MainRouteScreen.HomeScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    MainRouteScreen.DeviceListScreen.route,
                    MainRouteScreen.SettingsScreen.route ->
                        NavigationTransitions.enterTransition(isEnteringFromRight = false)
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    MainRouteScreen.DeviceListScreen.route,
                    MainRouteScreen.SettingsScreen.route ->
                        NavigationTransitions.exitTransition(isExitingToRight = false)
                    else -> null
                }
            }
        ) {
            HomeScreen(rootNavController, connectionViewModel)
        }

        composable(
            route = MainRouteScreen.DeviceListScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    MainRouteScreen.HomeScreen.route ->
                        NavigationTransitions.enterTransition(isEnteringFromRight = true)
                    MainRouteScreen.SettingsScreen.route ->
                        NavigationTransitions.enterTransition(isEnteringFromRight = false)
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    MainRouteScreen.HomeScreen.route ->
                        NavigationTransitions.exitTransition(isExitingToRight = true)
                    MainRouteScreen.SettingsScreen.route ->
                        NavigationTransitions.exitTransition(isExitingToRight = false)
                    else -> null
                }
            }
        ) {
            DeviceScreen(rootNavController, searchQuery, connectionViewModel)
        }

        composable(
            route = MainRouteScreen.SettingsScreen.route,
            enterTransition = {
                when (initialState.destination.route) {
                    MainRouteScreen.HomeScreen.route,
                    MainRouteScreen.DeviceListScreen.route ->
                        NavigationTransitions.enterTransition(isEnteringFromRight = true)
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    MainRouteScreen.HomeScreen.route,
                    MainRouteScreen.DeviceListScreen.route ->
                        NavigationTransitions.exitTransition(isExitingToRight = true)
                    else -> null
                }
            }
        ) {
            SettingsScreen(rootNavController)
        }

        composable(route = SettingsRouteScreen.NewUpdateScreen.route) {
            NewUpdateScreen(rootNavController)
        }
    }
}
