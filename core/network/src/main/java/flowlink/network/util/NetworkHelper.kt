package FlowLink.network.util

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Collections

object NetworkHelper {
    val localAddress: String?
        get() = getDeviceIpAddress()?.hostAddress

    // rmnet is related to cellular connections or USB tethering mechanisms.
    // See: https://android.googlesource.com/kernel/msm/+/android-msm-flo-3.4-kitkat-mr1/Documentation/usb/gadget_rmnet.txt
    fun getDeviceIpAddress(): InetAddress? {
        var fallbackIp: InetAddress? = null
        var tailscaleIp: InetAddress? = null
        var wifiIp: InetAddress? = null
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                if (!networkInterface.isUp || networkInterface.isLoopback) continue
                if (networkInterface.displayName.contains("rmnet", ignoreCase = true)) continue

                for (inetAddress in Collections.list(networkInterface.inetAddresses)) {
                    if (inetAddress.isLoopbackAddress) continue
                    if (inetAddress is Inet4Address) {
                        val host = inetAddress.hostAddress ?: ""
                        if (isTailscaleAddress(host)) {
                            tailscaleIp = inetAddress
                        } else if (networkInterface.name.startsWith("wlan")) {
                            wifiIp = inetAddress
                        } else if (fallbackIp == null) {
                            fallbackIp = inetAddress
                        }
                    }
                }
            }
        } catch (_: SocketException) {
        }

        return tailscaleIp ?: wifiIp ?: fallbackIp
    }

    fun getAllDeviceIpAddresses(): List<String> {
        val list = mutableListOf<String>()
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                if (!networkInterface.isUp || networkInterface.isLoopback) continue
                if (networkInterface.displayName.contains("rmnet", ignoreCase = true)) continue

                val isTailscale = networkInterface.name.contains("tailscale", ignoreCase = true) ||
                                  networkInterface.displayName.contains("tailscale", ignoreCase = true) ||
                                  networkInterface.name.startsWith("tun")

                for (inetAddress in Collections.list(networkInterface.inetAddresses)) {
                    if (inetAddress.isLoopbackAddress) continue
                    if (inetAddress is Inet4Address) {
                        val hostAddr = inetAddress.hostAddress ?: continue
                        val isTailscaleIp = isTailscaleAddress(hostAddr)
                        val label = when {
                            isTailscale || isTailscaleIp -> "Tailscale"
                            networkInterface.name.startsWith("wlan") -> "Wi-Fi"
                            networkInterface.name.startsWith("eth") -> "Ethernet"
                            else -> networkInterface.displayName
                        }
                        list.add("$label: $hostAddr")
                    }
                }
            }
        } catch (_: SocketException) {}
        return list
    }

    fun getAllLocalIpAddresses(): List<String> {
        val list = mutableListOf<String>()
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (networkInterface in interfaces) {
                if (!networkInterface.isUp || networkInterface.isLoopback) continue
                if (networkInterface.displayName.contains("rmnet", ignoreCase = true)) continue

                for (inetAddress in Collections.list(networkInterface.inetAddresses)) {
                    if (inetAddress.isLoopbackAddress) continue
                    if (inetAddress is Inet4Address) {
                        inetAddress.hostAddress?.let { list.add(it) }
                    }
                }
            }
        } catch (_: Exception) {}
        return list.distinct()
    }

    fun isTailscaleAddress(ip: String): Boolean {
        return try {
            val parts = ip.split(".").map { it.toInt() }
            if (parts.size == 4) {
                parts[0] == 100 && parts[1] in 64..127
            } else false
        } catch (_: Exception) {
            false
        }
    }
}
