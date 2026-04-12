plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) version "2.0.21" apply false
    alias(libs.plugins.kotlin.compose) version "2.0.21" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    // THÊM: Plugin KSP phiên bản tương ứng với Kotlin 2.0.21
    id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
}