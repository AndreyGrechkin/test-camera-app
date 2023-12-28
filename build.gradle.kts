// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}

plugins {
    id ("com.android.application") version "8.2.0" apply false
    id ("com.android.library") version "8.2.0" apply false
    id ("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id ("io.realm.kotlin") version "1.13.0" apply false
}
