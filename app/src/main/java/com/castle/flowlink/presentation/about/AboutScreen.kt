package com.castle.FlowLink.presentation.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.castle.FlowLink.BuildConfig
import com.castle.FlowLink.presentation.about.components.LinkIcon
import com.castle.FlowLink.presentation.settings.components.LogoHeader
import com.castle.FlowLink.presentation.settings.components.TextPreferenceWidget
import FlowLink.common.R
import FlowLink.network.util.NetworkHelper
import FlowLink.presentation.icons.CustomIcons
import FlowLink.presentation.icons.Discord
import FlowLink.presentation.icons.Github

@Composable
fun AboutScreen(rootNavController: NavController, modifier: Modifier = Modifier) {
    val uriHandler = LocalUriHandler.current
    val ipAddresses = remember { NetworkHelper.getAllDeviceIpAddresses() }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                title = { Text(stringResource(id = R.string.about)) },
                navigationIcon = {
                    IconButton(
                        onClick = { rootNavController.navigateUp() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
            )
        }
    ) { contentPadding ->
        LazyColumn(contentPadding = contentPadding) {
            item {
                LogoHeader()
            }

            item {
                TextPreferenceWidget(
                    title = "Developer",
                    subtitle = "saferill",
                    onPreferenceClick = {
                        // Developer info
                    },
                )
            }

            item {
                TextPreferenceWidget(
                    title = stringResource(R.string.version),
                    subtitle = BuildConfig.VERSION_NAME,
                    onPreferenceClick = {
                        // Version
                    },
                )
            }

            if (ipAddresses.isNotEmpty()) {
                item {
                    TextPreferenceWidget(
                        title = "Alamat IP HP (Tailscale / Wi-Fi)",
                        subtitle = ipAddresses.joinToString("\n"),
                        onPreferenceClick = {
                            // Copy to clipboard
                        }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    LinkIcon(
                        label = "GitHub",
                        icon = CustomIcons.Github,
                        url = "https://github.com/safe_rill",
                    )
                }
            }
        }
    }
}