# 🗺️ FlowLink Future Updates & Roadmap

Dokumen ini berisi rencana pengembangan dan optimalisasi fitur **FlowLink** ke depannya untuk meningkatkan pengalaman pengguna menjadi lebih premium, stabil, dan terintegrasi.

---

## 🚀 Daftar Rencana Fitur & Optimalisasi

### 1. 🖥️ Integrasi Jendela Screen Mirroring (Embed scrcpy)
Menyematkan tampilan layar HP (`scrcpy`) secara langsung di dalam salah satu grid atau tab jendela FlowLink, alih-alih membuka jendela cmd/eksternal terpisah.
*   **Target Dampak**: Sangat Tinggi (Meningkatkan estetika aplikasi agar terasa terpadu/bukan sekadar launcher).
*   **Detail Teknis**: Menggunakan Win32 API Interop (`SetParent`) untuk menangkap handle jendela (HWND) dari proses scrcpy yang diluncurkan dan memasukkannya ke elemen UI XAML.

### 2. ⚡ Visual Drag-and-Drop Overlay
Menampilkan overlay visual yang interaktif (efek gelap transparan, bingkai putus-putus, berkas ikon melayang, dan tulisan instruksi) ketika pengguna menyeret file dari Windows Explorer ke atas jendela FlowLink.
*   **Target Dampak**: Tinggi (Membuat fitur pengiriman file cepat lewat drag-and-drop menjadi mudah ditemukan dan digunakan).
*   **Detail Teknis**: Memanfaatkan event `DragEnter`, `DragLeave`, dan penataan visual state pada `Grid` utama di `MainPage.xaml`.

### 3. 🔄 Koneksi Ulang Otomatis Pintar (Smart Auto-Reconnect)
Mengembangkan sistem deteksi latar belakang untuk melacak perubahan alamat IP perangkat Android (karena DHCP Wi-Fi) dan menyambungkannya kembali secara otomatis tanpa perlu intervensi pengguna.
*   **Target Dampak**: Tinggi (Meningkatkan kenyamanan pengguna secara drastis saat komputer baru menyala atau saat berpindah jaringan Wi-Fi).
*   **Detail Teknis**: Melakukan pemindaian jaringan lokal berkala, caching riwayat IP, dan pemanfaatan mDNS secara lebih agresif.

### 4. 📞 Panggilan Telepon Penuh via Bluetooth (Hands-Free calling)
Menambahkan dukungan panggilan telepon langsung lewat aplikasi di PC dengan memanfaatkan koneksi Bluetooth Hands-Free Profile (HFP).
*   **Target Dampak**: Sedang-Tinggi (Membuat alur komunikasi lebih lengkap sehingga pengguna dapat menerima suara panggilan dan berbicara langsung melalui PC).
*   **Detail Teknis**: Integrasi dengan Windows Bluetooth APIs dan audio router ke perangkat audio PC.

---

> **Rekomendasi Rencana Kerja**:
> Mulai dengan **No. 2 (Drag-and-Drop Overlay)** karena merupakan perbaikan cepat (quick win), dilanjutkan dengan **No. 1 (Embed scrcpy)** untuk memberikan peningkatan estetika terbesar pada aplikasi.
 