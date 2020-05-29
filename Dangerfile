github.dismiss_out_of_range_messages

# Ignore inline messages which lay outside a diff's range of PR
github.dismiss_out_of_range_messages

# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

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