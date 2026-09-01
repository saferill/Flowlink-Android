plugins {
    alias(libs.plugins.flowlink.android.library)
}

android {
    namespace = "FlowLink.common"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
        )
    }
}


dependencies {

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
}
