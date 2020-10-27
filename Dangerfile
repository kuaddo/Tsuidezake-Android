# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Gradle Versions Plugin
if File.exist?("app/build/dependencyUpdates/merged_report.md")
  File.open("app/build/dependencyUpdates/merged_report.md", "r") do |f|
    contents = f.read()
    if contents.empty?
        message("All libraries are up to date.")
    else
        markdown(contents)
    end
  end
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
