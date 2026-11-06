import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)

    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.kover)

    alias(libs.plugins.paparazzi)
}

// リリース署名の設定。ファイルがなければ release は未署名でビルドされる
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties =
    Properties().apply {
        if (keystorePropertiesFile.exists()) {
            keystorePropertiesFile.inputStream().use { load(it) }
        }
    }

android {
    room {
        schemaDirectory("$projectDir/schemas")
    }

    namespace = "com.cheesecomer.rewardstamp"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.cheesecomer.rewardstamp"
        minSdk = 26
        targetSdk = 36
        versionCode = 4
        versionName = "0.9.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.androidx.test.core)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.navigation.testing)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.truth)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.konfetti.compose)
    implementation(libs.androidx.datastore.preferences)
    ksp(libs.androidx.room.compiler)
    ktlintRuleset(libs.ktlint.compose.rules)
}

ktlint {
    android.set(true)
    outputColorName.set("RED")

    filter {
        exclude("**/*.gradle.kts")
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    autoCorrect = true

    config.setFrom("$rootDir/detekt.yml")
}
kover {
    currentProject {
        // Merges coverage of the debug unit tests and the screenshot tests run on release
        createVariant("all") {
            add("debug", "release")
        }
    }
    reports {
        verify {
            rule {
                minBound(95)
            }
        }
        filters {
            excludes {
                annotatedBy(
                    "com.cheesecomer.rewardstamp.annotation.ExcludeFromCoverage",
                )
                classes(
                    "*AppDatabase_Impl*",
                    "*_Impl*",
                    "*Dao_Impl*",
                    "*.BuildConfig",
                    "*.R",
                    "*.R$*",
                    "*.Manifest",
                    "*.Manifest$*",
                    "*ComposableSingletons*",
                    "**/*Preview*.*",
                )
            }
        }
    }
}

// Paparazzi (layoutlib) and Robolectric load conflicting native libraries, so they cannot share a JVM.
// Screenshot tests run only in the release unit test task, and all other tests in the debug one.
val screenshotTestPattern = "*ScreenshotTest"

tasks.withType<Test>().configureEach {
    if (name == "testReleaseUnitTest") {
        filter.includeTestsMatching(screenshotTestPattern)
    } else {
        filter.excludeTestsMatching(screenshotTestPattern)
    }
    // Robolectric accesses FileDescriptor internals through jdk.internal.access.SharedSecrets
    jvmArgs("--add-exports=java.base/jdk.internal.access=ALL-UNNAMED")

    testLogging {
        events("passed", "failed", "skipped")

        showStandardStreams = true
    }
}

tasks.register("screenshotTest") {
    group = "verification"
    description = "Runs Paparazzi screenshot tests."
    dependsOn("testReleaseUnitTest")
}