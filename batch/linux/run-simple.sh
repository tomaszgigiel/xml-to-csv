# http://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
DIR_PROJECT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"/.. #
# http://stackoverflow.com/questions/11420221/sbt-run-from-outside-the-project-directory
(cd $DIR_PROJECT; lein run-main-xml-to-csv -i "./src/test/resources/sample-simple.xml" -o "$HOME/_delete_content/sample-simple.csv"; cd -) #
