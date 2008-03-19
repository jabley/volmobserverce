@echo off

rem Assumed that this is run from the bin directory and that there is
rem a ..\lib directory with the jar files in.

set TOOLS_LIB=..\lib

java -jar %TOOLS_LIB%\mcs-device-update-client-cli.jar %*

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 20-Dec-04	6522/1	pcameron	VBM:2004122004 New packagers for wemp

REM 20-Dec-04	6496/1	ianw	VBM:2004121502 interim commit for Peter

REM ===========================================================================
REM
