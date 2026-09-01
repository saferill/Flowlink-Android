plugins {
    alias(libs.plugins.flowlink.android.library)
}

android {
    namespace = "FlowLink.projection"
}

dependencies {
    api(projects.core.common)
    api(projects.core.presentation)
    api(projects.feature.notification)
    api(projects.domain)

    implementation(libs.core.ktx)
    implementation(libs.androidx.media)
    implementation(libs.androidx.media3.session)
}