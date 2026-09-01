---
name: project-scope-guard
description: Memastikan setiap perubahan kode hanya dilakukan pada project yang diminta user (Desktop atau Android), tidak boleh menyentuh project lain tanpa instruksi eksplisit.
---

# Project Scope Guard — FlowLink

## Aturan Utama

Workspace ini terdiri dari dua project terpisah:

| Project | Lokasi |
|---------|--------|
| **Desktop (Windows)** | `c:\Users\Hype AMD\Downloads\FlowLink\FlowLink Desktop` |
| **Android** | `c:\Users\Hype AMD\Downloads\FlowLink\FlowLink Android` |

## Perilaku yang Harus Dipatuhi

1. **Jika user menyebut "desktop", "Windows", atau topik terkait WinUI/C#/installer** → hanya kerjakan file di `FlowLink Desktop`.

2. **Jika user menyebut "android", "APK", "Kotlin", atau topik terkait Android Studio** → hanya kerjakan file di `FlowLink Android`.

3. **Jika perintah tidak jelas menyebut yang mana** → **WAJIB tanya dulu** sebelum mengerjakan. Contoh: *"Ini untuk Desktop atau Android?"*

4. **Dilarang keras** melakukan perubahan ke project yang tidak disebutkan, bahkan jika perubahan terlihat "aman" atau "relevan".

## Konteks Teknis

- **Desktop**: WinUI 3 / Uno Platform, C#, .NET 9, build via `dotnet publish`, installer via Inno Setup (`flowlink-installer.iss`)
- **Android**: Kotlin, Android Studio, output APK (`FlowLink-Release.apk`)
- Keduanya **tidak saling bergantung** di level source code — komunikasi hanya via jaringan (socket/mDNS)
