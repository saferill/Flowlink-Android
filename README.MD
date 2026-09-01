<p align="center">
  <img alt="FlowLink Android Hero" src="./fastlane/metadata/android/en-US/images/featureGraphic.png" />
</p>

<h1 align="center">FlowLink — Android</h1>

<p align="center">
  <em>Jembatan antara Android-mu dan Windows PC</em>
</p>

<p align="center">
  <a href="https://github.com/PLACEHOLDER/FlowLink"><img src="https://img.shields.io/badge/desktop-repo-blue?logo=github" alt="Desktop Repo" /></a>
  <a href="https://discord.gg/MuvMqv4MES"><img src="https://img.shields.io/discord/1310140719138340925?label=Discord&color=7289da" alt="Discord" /></a>
  <img src="https://img.shields.io/badge/platform-Android%2010%2B-brightgreen?logo=android" alt="Android 10+" />
  <img src="https://img.shields.io/badge/language-Kotlin-orange?logo=kotlin" alt="Kotlin" />
</p>

---

## 🗺️ Arsitektur Sistem

```mermaid
graph TB
    subgraph ANDROID["📱 Android — FlowLink Android"]
        direction TB
        UI_A["Jetpack Compose UI"]
        CONN["Connection Manager\n(TCP Client)"]
        MDNS_A["mDNS Discovery"]
        CLIP_A["Clipboard Monitor\n(Accessibility Service)"]
        NOTIF_A["Notification Listener\nService"]
        FILE_A["File Transfer Handler"]
        MEDIA_A["Media Session\n(MediaController)"]
        SFTP["SFTP Server\n(Storage Access)"]
        SMS["SMS / Calls Handler"]

        UI_A --> CONN
        CONN --> CLIP_A
        CONN --> NOTIF_A
        CONN --> FILE_A
        CONN --> MEDIA_A
        CONN --> SFTP
        CONN --> SMS
    end

    subgraph WINDOWS["🖥️ Windows PC — FlowLink Desktop"]
        WAPP["FlowLink Desktop\n(WinUI 3 / .NET 9)"]
    end

    CONN <-->|"Wi-Fi / LAN\nTCP Port 5149–5169\nTLS Encrypted"| WAPP
    MDNS_A <-.->|"Auto Discovery"| WAPP
```

---

## 🔄 Alur Permission & Koneksi

```mermaid
flowchart TD
    START(["🚀 Buka FlowLink Android"])
    PERM["Halaman Onboarding\n— Grant Permissions"]

    subgraph PERMISSIONS["Permissions yang Diperlukan"]
        P1["🔔 Notification Access"]
        P2["♿ Accessibility Service\n(Clipboard Auto-sync)"]
        P3["📁 Storage Permission"]
        P4["📞 Phone/SMS Permission"]
    end

    SCAN["Scan / Input IP Desktop"]
    CONNECT["Inisiasi Koneksi TCP"]
    VERIFY["🔑 Verifikasi Kode Pairing"]
    DONE(["✅ Terhubung!\nSemua fitur aktif"])

    START --> PERM
    PERM --> PERMISSIONS
    PERMISSIONS --> SCAN
    SCAN --> CONNECT
    CONNECT --> VERIFY
    VERIFY -->|Kode cocok| DONE
    VERIFY -->|Kode tidak cocok| CONNECT
```

---

## ✨ Fitur

| Fitur | Deskripsi | Permission |
|-------|-----------|-----------|
| 📋 **Clipboard Sharing** | Sinkronisasi clipboard dua arah (teks & gambar) | Accessibility Service |
| 🔔 **Notification Mirror** | Kirim notifikasi Android ke Windows | Notification Listener |
| 📁 **File Sharing** | Kirim & terima file via share sheet atau drag-drop | Storage |
| 🗂️ **Storage Integration** | Expose storage Android ke Windows Explorer via SFTP | Storage (Android 11+) |
| 🎵 **Media Control** | Remote kontrol playback & volume dari PC | — |
| 📺 **Screen Mirroring** | Mirror layar Android ke PC via Scrcpy (USB/WiFi) | ADB |
| 💬 **SMS Texting** | Baca & balas SMS dari Windows | SMS + Contacts |
| 📞 **Call Management** | Terima & kelola panggilan masuk dari PC | Phone |

---

## 🏗️ Struktur Proyek

```
FlowLink Android/
├── app/                    # Entry point & DI setup
├── core/
│   ├── network/            # TCP Client, mDNS, TLS
│   ├── data/               # Repository & DataStore
│   └── common/             # Shared utilities
├── domain/                 # Use cases & interfaces
├── feature/
│   ├── connection/         # UI pairing & koneksi
│   ├── clipboard/          # Clipboard sync
│   ├── notifications/      # Notification listener
│   ├── filetransfer/       # File send/receive
│   └── media/              # Media session control
├── data/
│   ├── sftp/               # SFTP server (storage)
│   └── sms/                # SMS & Calls handler
└── build-logic/            # Gradle convention plugins
```

---

## 🚀 Instalasi

### Dari APK (Langsung)

Download **`FlowLink-Release.apk`** dari [Releases](https://github.com/PLACEHOLDER/FlowLink-Android/releases/latest), lalu:

```
⚠️ Catatan sideload:
Setelah install, buka App Info → "Allow restricted settings"
Ini diperlukan agar Notification & Accessibility permission bisa diaktifkan.
```

### Dari Store

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.castle.FlowLink)
[![Get it on IzzyOnDroid](https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png)](https://apt.izzysoft.de/fdroid/index/apk/com.castle.FlowLink)

---

## ⚡ Quick Start

```mermaid
flowchart LR
    S1["1️⃣ Install APK\ndi Android"]
    S2["2️⃣ Grant semua\npermission"]
    S3["3️⃣ Sambungkan ke\nWi-Fi yang sama\ndengan PC"]
    S4["4️⃣ Tap 'Connect'\natau masukkan IP PC"]
    S5["5️⃣ Cocokkan kode\nverifikasi di PC"]
    S6["6️⃣ ✅ Terhubung!"]

    S1 --> S2 --> S3 --> S4 --> S5 --> S6
```

---

## ⚙️ Build dari Source

```bash
# Clone repo
git clone https://github.com/PLACEHOLDER/FlowLink-Android.git

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

**Persyaratan:** Android Studio · JDK 17+ · Android SDK 35+

---

## 🌍 Limitasi

### Notification Sync (Android 15+)
Notifikasi sensitif tidak bisa dibaca karena kebijakan Android. Fix via ADB:

```sh
adb shell appops set com.castle.FlowLink RECEIVE_SENSITIVE_NOTIFICATIONS allow
```

### Storage Integration
- Memerlukan **Android 11** atau lebih tinggi
- **⚠️ PERINGATAN:** Jangan arahkan remote storage ke folder yang sudah ada isinya — konten akan terhapus!

---

## 🤝 Kontribusi

Bug report & fitur request → buka issue di [repo Desktop](https://github.com/PLACEHOLDER/FlowLink/issues) kecuali masalah khusus Android.

Pull request sangat diterima!

- **Discord:** [discord.gg/MuvMqv4MES](https://discord.gg/MuvMqv4MES)

---

## 🙏 Terima Kasih

- **Clipboard via Accessibility:** [XClipper](https://github.com/KaustubhPatange/XClipper) oleh [@KaustubhPatange](https://github.com/KaustubhPatange)
- **SFTP Server:** [KDE Connect Android](https://github.com/KDE/kdeconnect-android)

---

<p align="center">
  <img src="./.github/readme-images/Screenshot.png" alt="FlowLink Android Screenshot" />
</p>
