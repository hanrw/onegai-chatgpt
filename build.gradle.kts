import Google.Ar.Sceneform.plugin
import com.linecorp.support.project.multi.recipe.configure
import com.linecorp.support.project.multi.recipe.matcher.ProjectMatchers.Companion.byLabel
import com.linecorp.support.project.multi.recipe.matcher.ProjectMatchers.Companion.byTypePrefix
import com.linecorp.support.project.multi.recipe.matcher.ProjectMatchers.Companion.byTypeSuffix
import org.springframework.boot.gradle.tasks.bundling.BootJar
import org.springframework.boot.gradle.tasks.run.BootRun

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.org.jetbrains.kotlin.plugin.spring) // org.jetbrains.kotlin:kotlin-allopen
    alias(libs.plugins.org.jetbrains.kotlin.jvm)
    alias(libs.plugins.org.springframework.boot) apply false
    alias(libs.plugins.io.spring.dependency.management)
    alias(libs.plugins.org.jetbrains.kotlinx.kover)
    alias(libs.plugins.com.linecorp.build.recipe.plugin)
    alias(libs.plugins.org.jlleitschuh.gradle.ktlint)
    alias(libs.plugins.org.jetbrains.plugin.serialization)
    id("com.google.osdetector") version "1.7.3"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kover {
    engine.set(kotlinx.kover.api.DefaultJacocoEngine)
    engine.set(kotlinx.kover.api.JacocoEngine("0.8.8"))
    htmlReport {
        reportDir.set(layout.buildDirectory.dir("reports/kover/html"))
    }
    xmlReport {
        reportFile.set(layout.buildDirectory.file("reports/kover/reports.xml"))
    }
}

allprojects {
    group = "com.snacks.onegai"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven(url = "https://repo.spring.io/snapshot")
        maven(url = "https://repo.spring.io/milestone")
    }

    tasks {
        test {
            useJUnitPlatform()
            extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
                isEnabled = true
                reportFile.set(file("$buildDir/kover/test.exec"))
            }
            finalizedBy(koverReport)
        }
    }
}

subprojects {
    // Prevent the build of empty project by build-recipe-plugin
    if (!file("gradle.properties").exists()) {
        ext["type"] = null
    }
    apply {
        plugin("java-library")
        plugin(rootProject.libs.plugins.org.jetbrains.kotlin.plugin.spring.get().pluginId)
    }

    tasks {
        test {
            useJUnitPlatform()
        }
    }

    dependencies {
        testRuntimeOnly(rootProject.libs.junit.jupiter.engine)
        testImplementation(rootProject.libs.junit.jupiter.api)
        testImplementation(rootProject.libs.mockito.kotlin)
        testImplementation(rootProject.libs.mockito.junit.jupiter)
        testImplementation(rootProject.libs.assertj.core)
    }
}

configure(byTypePrefix("kotlin")) {
    apply {
        plugin(rootProject.libs.plugins.org.jetbrains.kotlin.jvm.get().pluginId)
        plugin(rootProject.libs.plugins.org.jlleitschuh.gradle.ktlint.get().pluginId)
    }
    dependencies {
        implementation(rootProject.libs.ktor.serialization.kotlinx.json)
    }

    tasks {
        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
            }
        }

        ktlint {
            version.set("0.48.1")
            verbose.set(true)
            outputToConsole.set(true)
            coloredOutput.set(true)
            reporters {
                reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
            }
            filter {
                include("**/kotlin/**")
            }
        }
    }

    dependencies {
        implementation(rootProject.libs.kotlin.reflect)
        implementation(rootProject.libs.kotlin.stdlib.jdk8)
        testImplementation(rootProject.libs.mockito.kotlin)
    }
}

configure(byLabel("spring-boot-webflux")) {
    apply {
        plugin("com.google.osdetector")
    }

    dependencies {
        // Unable to load io.netty.resolver.dns.macos.MacOSDnsServerAddressStreamProvider
        if (osdetector.arch.equals("aarch_64")) {
            implementation("io.netty:netty-resolver-dns-native-macos:4.1.79.Final:osx-aarch_64")
        }
        implementation(rootProject.libs.kotlinx.coroutines.reactor)
        implementation(rootProject.libs.spring.boot.starter.webflux)
        // OpenAPI
        implementation(rootProject.libs.springdoc.openapi.starter.webflux.ui)
        // OpenAPI Unable to create a Configuration, because no Jakarta Bean Validation provider could be found. Add a provider like Hibernate Validator (RI) to your classpath.
        implementation(rootProject.libs.spring.boot.starter.validation)

        // Testing
        testImplementation(rootProject.libs.reactor.test)
    }
}

configure(byTypeSuffix("boot-application")) {
    apply {
        plugin("java-library")
        plugin(rootProject.libs.plugins.org.jetbrains.kotlin.plugin.spring.get().pluginId)
        plugin(rootProject.libs.plugins.org.jetbrains.kotlin.jvm.get().pluginId)
        plugin(rootProject.libs.plugins.org.springframework.boot.get().pluginId)
        plugin(rootProject.libs.plugins.io.spring.dependency.management.get().pluginId)
    }

    tasks {
        withType<BootJar> {
            enabled = true
        }

        withType<BootRun> {
            enabled = true
            environment["SPRING_PROFILES_ACTIVE"] = environment["SPRING_PROFILES_ACTIVE"] ?: "local"
        }
    }

    dependencies {
        // Testing
        testImplementation(rootProject.rootProject.libs.spring.boot.starter.test)
    }
}
