plugins {
    kotlin("multiplatform") version "1.7.21"
}

group = "org.d7z"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") { // NL
            cinterops { // NL

                val gtk4 by creating {
                    defFile(project.file("src/nativeInterop/cinterop/libgtk4.def"))
                    packageName("gtk4")
                    compilerOpts("-I/path")
                    includeDirs.allHeaders("path")
                }
            } // NL
        }
        binaries {
            executable {
                val sysRoot = "/"
                val libGcc = (
                    File("/lib/gcc/x86_64-pc-linux-gnu/").listFiles()
                        ?: throw RuntimeException("gcc not found")
                    ).first().absolutePath
                val overriddenProperties = "targetSysRoot.linux_x64=$sysRoot;libGcc.linux_x64=$libGcc"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xoverride-konan-properties=$overriddenProperties"
                )
                entryPoint = "main"
            }
        }
    }
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}
