plugins {
    alias(libs.plugins.flowlink.android.library)
}

android {
    namespace = "FlowLink.notification"
}

dependencies {
    api(projects.domain)
    api(projects.core.presentation)

    implementation(libs.core.ktx)
}