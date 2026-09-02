<div align="center">

<img src="./banner.jpg" alt="FlowLink Banner" width="100%" style="border-radius: 16px; margin-bottom: 20px; box-shadow: 0 10px 30px rgba(0,0,0,0.5);"/>

# ⚡ FlowLink Android

### *The Seamless, Privacy-Focused Bridge Between Android & Windows PC*

[![GitHub Desktop Repo](https://img.shields.io/badge/Desktop_Companion-FlowLink_Desktop-00d2ff?style=for-the-badge&logo=windows&logoColor=white)](https://github.com/saferill/Flowlink-Desktop)
[![Android](https://img.shields.io/badge/Android-8.0%2B-10b981?style=for-the-badge&logo=android&logoColor=white)](https://github.com/saferill/Flowlink-Android)
[![Tailscale](https://img.shields.io/badge/Tailscale-VPN_Mesh-9d4edd?style=for-the-badge&logo=tailscale&logoColor=white)](https://tailscale.com)
[![License](https://img.shields.io/badge/License-GPL_v3-f59e0b?style=for-the-badge)](LICENSE)

<br/>

**FlowLink Android** connects your smartphone and Windows PC into a unified ecosystem. Enjoy **zero-latency real-time clipboard sync**, **encrypted peer-to-peer file transfers**, **remote PC power controls**, and **seamless Tailscale mesh discovery**.

</div>

---

## 📸 Tampilan Aplikasi (App Screenshots)

<div align="center">

| Beranda & Remote Control | Manajemen Perangkat | Pengaturan Aplikasi |
| :---: | :---: | :---: |
| <img src="./screenshot_home.jpg" width="240" style="border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.2);"/> | <img src="./screenshot_devices.jpg" width="240" style="border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.2);"/> | <img src="./screenshot_settings.jpg" width="240" style="border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.2);"/> |

<br/>

### 💻 Pasangan Windows Desktop
<img src="./screenshot_desktop.png" width="85%" style="border-radius: 12px; box-shadow: 0 6px 20px rgba(0,0,0,0.3); margin-top: 8px;"/>

</div>

---

## 📥 Download FlowLink (Both Devices Required)

> 💡 **Penting**: Untuk menghubungkan perangkat, pasang FlowLink di smartphone Android **DAN** di laptop/PC Windows kamu.

| Platform | Download Link | Deskripsi / Format |
| :--- | :--- | :--- |
| 📱 **Android** | 📦 [**Download FlowLink APK (v1.0.0)**](https://github.com/saferill/Flowlink-Android/releases/latest) <br> *(Pengajuan IzzyOnDroid / F-Droid sedang dalam peninjauan)* | File APK resmi (Android 8.0+) |
| 💻 **Windows PC** | 📥 [**FlowLink Desktop Setup (.exe)**](https://github.com/saferill/Flowlink-Desktop/releases/latest) <br><br> 🗜️ [**FlowLink Desktop Portable (.zip)**](https://github.com/saferill/Flowlink-Desktop/releases/latest) | Installer resmi dengan shortcut Start Menu & Desktop, atau versi Portable tanpa instalasi (Windows 10/11) |

---

## 🔗 Repositori Resmi
* 📱 **Android App Repo**: [saferill/Flowlink-Android](https://github.com/saferill/Flowlink-Android)
* 💻 **Windows Desktop Repo**: [saferill/Flowlink-Desktop](https://github.com/saferill/Flowlink-Desktop)

---

## 🌟 Architecture Overview

<div align="center">
  <img src="./architecture.gif" alt="FlowLink Live Architecture Motion Flow" width="100%" style="border-radius: 16px; margin-bottom: 16px; box-shadow: 0 10px 30px rgba(0,0,0,0.5);"/>
</div>

Open the interactive simulation in your browser:  
👉 **[`architecture.html`](./architecture.html)** *(Interactive motion flow, live packet simulator, and real-time signal waveforms)*

---

## 🚀 Fitur Unggulan

### 1. ⚡ Ultra Low-Latency Adaptive File Transfer
- **Micro-Burst Streaming (< 1 MB)**: Foto, dokumen kecil, dan catatan terkirim secara instan (**< 10–30 ms**).
- **High-Speed Large File Transfer (> 100 MB)**: Otomatis meningkatkan buffer ke **4 MB mega-chunks** dengan socket window 2 MB untuk memaksimalkan kecepatan transfer Wi-Fi lokal.

### 2. 📋 Real-Time Clipboard Synchronization
- Copy teks atau link di Android ➔ langsung bisa di-paste di Windows PC (dan sebaliknya).
- Proteksi loop cerdas (*smart hash debounce*) mencegah terjadinya perulangan pengiriman clipboard.

### 3. 🛡️ Privasi Aman & Tanpa Izin Berlebih
- Bebas dari izin overlay agresif ataupun akses pembacaan SMS sensitif, menjamin **100% aman untuk aplikasi perbankan (m-Banking)**.
- 100% Bebas Iklan (*No Ads*) dan Bebas Pelacak (*No Analytics / Tracking*).

### 4. 🌐 Tailscale Mesh Discovery
- Terhubung otomatis saat berada di satu jaringan Wi-Fi lokal, atau di mana pun di seluruh dunia menggunakan jaringan VPN Tailscale terenkripsi.

---

## 🛠️ Build dari Source Code

```bash
# Clone repository
git clone https://github.com/saferill/Flowlink-Android.git
cd Flowlink-Android

# Build Release APK
./gradlew assembleRelease
```

---

## 📜 Lisensi
Proyek ini dilisensikan di bawah lisensi **[GNU General Public License v3.0 (GPL-3.0)](LICENSE)**.
Dikembangkan dan dirawat secara aktif oleh **[saferill](https://github.com/saferill)**.
