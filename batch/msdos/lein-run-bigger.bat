cd %~dp0/../..
md "%HOMEPATH%/_delete_content/"
start lein run-main-xml-to-csv -i "%HOMEPATH%/_delete_content/bigger.xml" -o "%HOMEPATH%/_delete_content/bigger.csv"
pause
