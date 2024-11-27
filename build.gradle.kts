// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
 //   id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false
}

