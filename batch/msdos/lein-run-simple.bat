cd %~dp0/../..
md "%HOMEPATH%/_delete_content/"
start lein run-main-xml-to-csv -i "./src/test/resources/sample-simple.xml" -o "%HOMEPATH%/_delete_content/sample-simple.csv"
pause
