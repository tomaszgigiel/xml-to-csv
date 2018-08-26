cd %~dp0/../..
md "%HOMEPATH%/_delete_content/"
start lein run-main-xml-to-csv -i "./src/test/resources/sample-trapeze.xml" -o "%HOMEPATH%/_delete_content/sample-trapeze.csv"
pause
