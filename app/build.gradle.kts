plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)

    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.test.logger)
    alias(libs.plugins.kover)

    alias(libs.plugins.paparazzi)
}

android {
    room {
        schemaDirectory("$projectDir/schemas")
    }

    namespace = "com.cheesecomer.rewardstamp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.cheesecomer.rewardstamp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
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

tasks.withType<Test>().configureEach {
    testLogging {
        events("passed", "failed", "skipped")

        showStandardStreams = true
    }
}