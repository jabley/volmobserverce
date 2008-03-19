@echo off

rem Assumed that this is run from the bin directory and that there is
rem a ..\lib directory with the jar files in.

set TOOLS_LIB=..\lib

java -jar %TOOLS_LIB%\mcs-device-repository-cli.jar %*

