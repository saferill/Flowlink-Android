package com.castle.FlowLink.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.castle.FlowLink.navigation.DeviceRouteScreen
import com.castle.FlowLink.navigation.Graph
import com.castle.FlowLink.navigation.MainRouteScreen
import com.castle.FlowLink.presentation.devices.AddressScreen
import com.castle.FlowLink.presentation.devices.DeviceSettingsScreen
import com.castle.FlowLink.presentation.devices.MediaSessionSettingsScreen

fun NavGraphBuilder.deviceNavGraph(rootNavController: NavHostController) {
    navigation(
        route = Graph.DevicesGraph,
        startDestination = MainRouteScreen.SettingsScreen.route
    ) {
        composable(
            route = "device?deviceId={deviceId}",
            arguments = listOf(
                navArgument("deviceId") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("deviceId")?.let { deviceId ->
                DeviceSettingsScreen(
                    deviceId = deviceId,
                    onNavigateBack = { rootNavController.navigateUp() },
                    onNavigateToAddressScreen = {
                        rootNavController.navigate(DeviceRouteScreen.AddressScreen.route)
                    },
                    onNavigateToMediaSessionSettings = {
                        rootNavController.navigate(DeviceRouteScreen.MediaSessionSettings.route)
                    },
                )
            }
        }

        composable(
            route = DeviceRouteScreen.AddressScreen.route
        ) {
            AddressScreen(
                rootNavController = rootNavController,
                onNavigateBack = { rootNavController.navigateUp() }
            )
        }

        composable(
            route = DeviceRouteScreen.MediaSessionSettings.route
        ) {
            MediaSessionSettingsScreen(
                rootNavController = rootNavController,
                onNavigateBack = { rootNavController.navigateUp() },
            )
        }
    }
}