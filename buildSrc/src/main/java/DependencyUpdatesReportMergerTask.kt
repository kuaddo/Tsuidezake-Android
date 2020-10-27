import com.google.gson.Gson
import com.google.gson.JsonObject
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths

open class DependencyUpdatesReportMergerTask : DefaultTask() {
    @get:Input
    var projectRootDirPath: String = ""

    @TaskAction
    fun createMergedReport() {
        val gson = Gson()
        val dependencyMap = File(projectRootDirPath).walk()
            .filter { it.endsWith(File("dependencyUpdates", "report.json")) }
            .flatMap { path ->
                val moduleName = getModuleName(path.toString())
                gson.fromJson(path.readText(), JsonObject::class.java)
                    .getAsJsonObject("outdated")
                    .getAsJsonArray("dependencies")
                    .map { gson.fromJson(it, Dependency::class.java) to moduleName }
                    .asSequence()
            }
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
            .entries
            .groupBy(
                keySelector = {
                    if (it.key.projectUrl == null) it.key.group
                    else "[${it.key.group}](${it.key.projectUrl})"
                },
                valueTransform = { (key, value) -> DependencyOutputItem.from(key, value) }
            )

        val outputFile = Paths.get(
            projectRootDirPath,
            "app",
            "build",
            "dependencyUpdates",
            "merged_report.md"
        ).toFile()
        outputFile.delete()
        writeTo(outputFile, dependencyMap)
    }

    private fun writeTo(outputFile: File, dependencyMap: Map<String, List<DependencyOutputItem>>) {
        FileOutputStream(outputFile).bufferedWriter().use { writer ->
            if (dependencyMap.isNotEmpty()) {
                writer.append("# Library update information")
                writer.newLine()
            }

            dependencyMap.forEach { (groupInfo, outputItems) ->
                writer.append("## $groupInfo")
                writer.newLine()
                outputItems.forEach { outputItem ->
                    writer.append("\t$outputItem")
                    writer.newLine()
                }
            }
        }
    }

    private fun getModuleName(path: String) = path
        .substringAfter(projectRootDirPath + File.separator)
        .substringBefore("${File.separator}build${File.separator}")
}

data class Dependency(
    val group: String,
    val name: String,
    val version: String,
    val available: Available,
    val projectUrl: String?
)

data class Available(
    val milestone: String,
    val release: String?,
    val integration: String?
)

data class DependencyOutputItem(
    val name: String,
    val versionInfo: String,
    val moduleInfo: String
) {
    override fun toString(): String = "$name [$versionInfo] ($moduleInfo)"

    companion object {
        fun from(dependency: Dependency, modules: List<String>) = DependencyOutputItem(
            name = dependency.name,
            versionInfo = "${dependency.version} -> ${dependency.available.milestone}",
            moduleInfo = modules.joinToString(separator = ", ")
        )
    }
}
