<div align="center">

# 📱 FlowLink Android
### *The Ultimate Low-Latency Bridge Between Android & Windows Desktop*

[![GitHub Desktop Repo](https://img.shields.io/badge/Desktop_Repo-FlowLink_Desktop-00d2ff?style=for-the-badge&logo=windows&logoColor=white)](https://github.com/saferill/Flowlink-Desktop)
[![Android](https://img.shields.io/badge/Android-10%2B-10b981?style=for-the-badge&logo=android&logoColor=white)](https://github.com/saferill/Flowlink-Android)
[![Tailscale](https://img.shields.io/badge/Tailscale-VPN_Mesh-9d4edd?style=for-the-badge&logo=tailscale&logoColor=white)](https://tailscale.com)
[![License](https://img.shields.io/badge/License-GPL_v3-f59e0b?style=for-the-badge)](LICENSE)

<br/>

**FlowLink Android** enables real-time device continuity between Android smartphones and Windows PCs with **zero-latency TCP streaming (`NoDelay`)**, **Tailscale mesh tunneling**, **ultra-fast adaptive file transfers**, and **instant PC system power controls**.

</div>

---

## 🌟 Interactive Live Architecture

Open the interactive simulation in your browser:
👉 **[`architecture.html`](./architecture.html)** *(Interactive motion flow, live packet simulator, and real-time signal waveforms)*

```mermaid
flowchart LR
    subgraph ANDROID["📱 FlowLink Android (Kotlin / Compose)"]
        direction TB
        A1["🚀 Network Discovery (mDNS & Tailscale CGNAT 100.64.0.0/10)"]
        A2["⚡ Adaptive Buffer Stream (64KB Photo ➔ 4MB 4K Video)"]
        A3["🔒 Device Control Card (Lock, Close All Apps, Shutdown)"]
        A4["📋 Clipboard & Notification Mirroring (E2EE)"]
    end

    subgraph MESH["🌐 Encrypted WireGuard / TLS 1.3 Mesh"]
        direction TB
        M1["TCP NoDelay (Nagle Disabled)"]
        M2["2MB Socket Windows"]
        M3["Zero-Latency Stream Pipeline"]
    end

    subgraph DESKTOP["💻 FlowLink Desktop (WinUI 3 / .NET 9)"]
        direction TB
        D1["🔄 Silent System Tray Autostart (Boot / Restart)"]
        D2["⏱️ Adaptive 3s Fast Polling Discovery Loop"]
        D3["🖱️ Explorer Context Menu ('Send to Phone')"]
        D4["⚡ Native Win32 API Execution (0ms Response)"]
    end

    ANDROID <===>|"Encrypted Sockets (Port 5149–5169)"| MESH
    MESH <===>|"High-Throughput IO"| DESKTOP
```

---

## 🚀 Key Innovations & Supercharged Features

### 1. ⚡ Ultra Low-Latency Adaptive File Transfer
- **Micro-Burst Streaming (< 1 MB)**: Photos, icons, and small docs use a **64 KB buffer** for near-instant transfer in **< 10–30 milliseconds**.
- **Wire-Speed 4K Video Streaming (> 100 MB)**: Automatically scales up to **4 MB mega-chunks** over a **2 MB socket window** to saturate Gigabit Wi-Fi / LAN bandwidth.

### 2. 🎮 Instant PC System Power Controls
Execute Windows commands straight from Android:
- **🔒 Lock Screen (`lock`)**: Native Win32 `LockWorkStation()` locks laptop in **0 ms**.
- **🛑 Close All Apps (`close_all`)**: Gracefully closes all open apps, browser tabs, and windows via Win32 `WM_CLOSE`.
- **⚡ Shutdown (`shutdown`)**: Fast automated shutdown.
- **🔄 Restart (`restart`)** & **💤 Hibernate (`hibernate`)**.

### 3. 🌐 Tailscale Mesh Auto-Discovery & Zero-Click Connection
- Automatically detects Tailscale CGNAT IP addresses (`100.64.0.0/10`) and VPN interfaces.
- Desktop polls every **3 seconds** upon boot to find and connect to your phone immediately.

### 4. 📋 Two-Way Clipboard & Notification Mirror
- Real-time text & image clipboard sync.
- Reply to WhatsApp, Telegram, and SMS notifications directly from Desktop.

---

## 🛠️ Architecture Highlights

| Layer | Implementation | Purpose |
| :--- | :--- | :--- |
| **UI** | Jetpack Compose + Material 3 | Modern, responsive interface |
| **Networking** | Ktor Sockets + TLS 1.3 | End-to-end encrypted packet stream |
| **Discovery** | UDP Multicast (5149) + Tailscale API | Zero-configuration local & remote pairing |
| **Transfer** | Adaptive Buffer Coroutines | Millisecond file & photo transfers |
| **Storage** | SFTP Server (Apache SSHD) | Mount Android Storage as Network Drive |

---

## 📲 Quick Setup

1. **Install Android APK**: Download the latest release.
2. **Grant Permissions**: Notification Access, Storage, and Accessibility (for instant clipboard sync).
3. **Connect**:
   - **Local Wi-Fi**: Open app, tap **Auto Connect** or scan QR Code.
   - **Remote / Mobile Data**: Turn on **Tailscale** on both phone and laptop. They will link automatically within 3 seconds!

---

<div align="center">
  <sub>Built with ❤️ for seamless cross-platform productivity.</sub>
</div>
