import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.jetbrains.kotlin.android) apply false
//    id("com.google.devtools.ksp") version "1.8.21-1.0.11" apply false
//}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("androidx.room") version "2.6.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false // Hypothetical compatible version
}



