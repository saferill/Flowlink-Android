plugins {
    alias(libs.plugins.flowlink.android.library)
}

android {
    namespace = "FlowLink.communication"
}

dependencies {
    api(projects.core.common)
    api(projects.domain)
    implementation(libs.activity.compose)
    implementation(libs.android.smsmms)
    implementation(libs.core.ktx)
    implementation(libs.commons.io)
    implementation(libs.commons.lang3)
    implementation(libs.commons.collections4)
}