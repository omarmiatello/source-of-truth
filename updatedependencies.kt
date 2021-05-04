#!/usr/bin/env kscript
@file:DependsOn("com.github.omarmiatello.gradle-modules-config:dependencies-update:1.0.4")

/**
 * ### Run script
 * > kscript updatelib.kt
 *
 * ### Edit script
 * This will open IntelliJ IDEA with a minimalistic project containing just your script and a generated gradle.build file
 * > kscript --idea updatelib.kt
 */
import com.github.omarmiatello.gradlemoduleconfig.dependenciesupdate.allDependencies
import com.github.omarmiatello.gradlemoduleconfig.dependenciesupdate.toMap
import com.github.omarmiatello.gradlemoduleconfig.dependenciesupdate.toSimpleDependencies
import java.io.File


fun main() {
    val dependenciesMap = File(".").walk().filter { it.name == "dependencyUpdates" }
        .flatMap { it.listFiles { dir, name -> ".json" in name }!!.toList() }
        .toList()
        .map { it.toSimpleDependencies() }
        .allDependencies()
        .toMap()

    val unused = dependenciesMap.keys.toMutableSet()
    File("buildSrc/src/main/kotlin/Dependencies.kt").also { file ->
        file.writeText(file.readLines().map { line ->
            var newLine = line
            dependenciesMap.forEach { (dependency, version) ->
                val dep = "$dependency:"
                if (dep in line) {
                    unused -= dependency
                    val start = line.indexOf(dep) + dep.length
                    val end = line.indexOf("\"", startIndex = start)
                    newLine = line.replaceRange(start, end, version)
                }
            }
            newLine
        }.joinToString("\n", postfix = "\n"))
    }

    File("buildSrc/dependencies_manually_updated.txt").also { file ->
        if (unused.isEmpty()) {
            file.delete()
        } else {
            file.writeText(
                "THIS FILE IS AUTOGENERATED\n" +
                        "If necessary, these dependencies must be manually updated:\n\n" +
                        dependenciesMap.filterKeys { it in unused }
                            .toList()
                            .joinToString("\n") { (k, v) -> "$k -> $v" })
        }
    }
}