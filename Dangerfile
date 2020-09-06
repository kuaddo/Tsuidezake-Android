# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Gradle Versions Plugin
hasUpdate = false
Dir["**/build/dependencyUpdates/report.txt"].each do |file|
    fileContent = File.open(file, "rb").readlines
    toRemove = "The following dependencies have later milestone versions:\n"
    index = fileContent.index(toRemove)
    if index
        hasUpdate = true
        moduleInfo = fileContent[2]
        updateInfo = fileContent.slice(index..-1).join()
        warn(moduleInfo + "\n" + updateInfo)
    end
end
if !hasUpdate
    message("All libraries are up to date.")
end

# ktlint
checkstyle_format.base_path = Dir.pwd
Dir["**/build/reports/ktlint/ktlint*.xml"].each do |file|
  checkstyle_format.report file
end

# test
Dir["**/build/test-results/**/*.xml"].each do |file|
  junit.parse(file)
  junit.show_skipped_tests = true
  junit.report
end