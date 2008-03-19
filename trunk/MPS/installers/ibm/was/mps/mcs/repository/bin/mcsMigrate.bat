@echo off

rem Assumed that this is run from the bin directory and that there is
rem a ..\lib directory with the jar files in.
rem The jdbc drivers require to be in the users CLASSPATH

set TOOLS_LIB=..\lib
set TOOLS_JAR_PATH=%TOOLS_LIB%\volantis-jdom.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\log4j.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\mcs-api.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-xercesImpl.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-xml-apis.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-xmlParserAPIs.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-xalan.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\jdbc2_0-stdext.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-synergetics.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-mcs-migrate-proteus.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\jakarta-regexp-1.3.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-mcs-migrate-client.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-mcs-common.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-mcs-core.jar
set TOOLS_JAR_PATH=%TOOLS_JAR_PATH%;%TOOLS_LIB%\volantis-mcs-eclipse-common.jar

java -classpath %TOOLS_JAR_PATH%;%CLASSPATH% com.volantis.mcs.cli.migrate.MarinerMigrate %*

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 22-Dec-04	6496/1	ianw	VBM:2004121502 Added mcs-api to CLI and implement JAvadoc of Public API

REM 20-Dec-04	6522/1	pcameron	VBM:2004122004 New packagers for wemp

REM 20-Dec-04	6496/1	ianw	VBM:2004121502 interim commit for Peter

REM ===========================================================================
REM
