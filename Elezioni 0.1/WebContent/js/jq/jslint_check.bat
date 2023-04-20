@echo off
echo Begin jslint check for *.js
java -jar "C:\Program Files (x86)\jslint4java-2.0.3\jslint4java-2.0.3.jar" *.js
echo Press ENTER to close window
pause > nul