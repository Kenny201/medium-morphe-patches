rootProject.name = "morphe-patches-template"

pluginManagement {
    includeBuild("../morphe-patches-gradle-plugin")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

includeBuild("../morphe-patcher")

plugins {
    id("app.morphe.patches") version "2.1.0"
}
