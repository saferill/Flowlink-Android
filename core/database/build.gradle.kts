plugins {
    alias(libs.plugins.flowlink.android.library)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "FlowLink.database"
}

dependencies {
    api(projects.domain)
    api(projects.core.common)

    // Room components
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(libs.kotlinx.serialization.json)

    ksp(libs.androidx.room.compiler)
    androidTestImplementation(libs.androidx.room.testing)
}