import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.util.jar.JarFile

plugins {
    java
    id("name.remal.lombok") version "3.1.1"
    idea
}

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("org.jspecify:jspecify:1.0.0")
    implementation("org.jetbrains:annotations:26.0.2-1")

    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation(platform("org.mockito:mockito-bom:5.19.0"))

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.27.5")

    testRuntimeOnly(platform("org.junit:junit-bom:5.13.4"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    enableAssertions = true

    testLogging {
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = TestExceptionFormat.FULL
        stackTraceFilters("GROOVY")
        events("FAILED")
    }
}

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

tasks.withType<Test>().configureEach {
    onlyIf {
        configureAgents(classpath, this)
        return@onlyIf true
    }
}

tasks.withType<JavaExec>().configureEach {
    onlyIf {
        configureAgents(classpath, this)
        return@onlyIf true
    }
}

fun configureAgents(classpath: FileCollection, options: JavaForkOptions) {
    classpath.filter { it.isFile }.forEach { file ->
        if (options.allJvmArgs.contains("-javaagent:${file.path}")
            || options.allJvmArgs.contains("-javaagent:${file.absolutePath}")
        ) {
            return
        }

        JarFile(file).use { jarFile ->
            jarFile.manifest?.mainAttributes?.let { mainAttrs ->
                val isAgent = sequenceOf("Premain-Class", "Agent-Class", "Launcher-Agent-Class")
                    .any { mainAttrs.getValue(it) != null }
                if (isAgent) {
                    options.jvmArgs("-javaagent:${file.absolutePath}")
                }
            }
        }
    }
}

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
