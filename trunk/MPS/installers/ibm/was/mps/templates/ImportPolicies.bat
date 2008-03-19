@ECHO OFF
SET REPOSITORY_HOME=@WPS-INSTALL-LOCATION@\mcs\repository
SET OLDCLASSPATH=%CLASSPATH%
SET CP=%CLASSPATH%;%REPOSITORY_HOME%\lib\jdbc2_0-stdext.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-jdom.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\log4j.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-mcs-cli.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-mcs-common.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-synergetics.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-xercesImpl.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-xmlParserAPIs.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\jakarta-regexp-1.3.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\volantis-xml-apis.jar
SET CP=%CP%;%REPOSITORY_HOME%\lib\mcs-api.jar

SET CLASSPATH=%CP%

SET DEFAULT_ARGS=-vendor @ODBC-VENDOR@ -host @ODBC-HOST@ -port @ODBC-PORT@ -source @ODBC-SOURCE@ -user @ODBC-USER@ -password @ODBC-PASSWORD@ -updateall -enableundo

"%JAVA_HOME%\bin\java" com.volantis.mcs.cli.MarinerImport %DEFAULT_ARGS% %* 
SET CLASSPATH=%OLDCLASSPATH%

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 11-Jan-05	6413/1	pcameron	VBM:2004120702 Some fixes for WEMP from alanw

REM 20-Dec-04	6522/3	pcameron	VBM:2004122004 Fixed path in ImportPolicies scripts for was

REM 20-Dec-04	6522/1	pcameron	VBM:2004122004 New packagers for wemp

REM 20-Dec-04	6496/1	ianw	VBM:2004121502 interim commit for Peter

REM ===========================================================================
REM
