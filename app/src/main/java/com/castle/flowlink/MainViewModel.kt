package com.castle.FlowLink

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.castle.FlowLink.navigation.Graph
import com.castle.FlowLink.navigation.OnboardingRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import FlowLink.domain.model.PendingDeviceApproval
import FlowLink.domain.interfaces.DeviceManager
import FlowLink.domain.interfaces.NetworkManager
import FlowLink.domain.interfaces.PreferencesRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val deviceManager: DeviceManager,
    private val networkManager: NetworkManager,
    private val preferencesRepository: PreferencesRepository,
): ViewModel() {
    
    val startDestination: String = runBlocking(Dispatchers.IO) {
        val hasCompletedOnboarding = preferencesRepository.readAppEntry()
        if (hasCompletedOnboarding) {
            Graph.MainScreenGraph
        } else {
            OnboardingRoute.OnboardingScreen.route
        }
    }
    
    val pendingDeviceApproval: StateFlow<PendingDeviceApproval?> = deviceManager.pendingDeviceApproval

    fun approveDevice(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.approveDeviceConnection(deviceId)
        }
    }

    fun rejectDevice(deviceId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            networkManager.rejectDeviceConnection(deviceId)
        }
    }
}