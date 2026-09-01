plugins {
    alias(libs.plugins.flowlink.android.library)
}

android {
    namespace = "FlowLink.clipboard"
}

dependencies {
    api(projects.domain)
    implementation(projects.core.database)
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.bundles.ktor)
}