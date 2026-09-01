package com.castle.FlowLink.presentation.onboarding

import androidx.compose.runtime.Composable
import com.castle.FlowLink.presentation.settings.SettingsViewModel

internal interface OnboardingStep {
    @Composable
    fun Content(viewModel: SettingsViewModel)
}