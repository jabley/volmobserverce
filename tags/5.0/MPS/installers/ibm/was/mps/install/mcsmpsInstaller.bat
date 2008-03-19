@ECHO OFF
ECHO > "%WAS_HOME%\mps\install\jacl.properties"
cd "%WAS_HOME%\mps\install"
"%WAS_HOME%\bin\ws_ant"  -f "%WAS_HOME%\mps\install\MCSMPSInstaller.xml" -Dwas.platform.script.ext=.bat -DNodeName=%WAS_NODE% %*

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 07-Jan-05	6613/1	ianw	VBM:2004121401 Fixed WasHome in WAS installer and various quote issues

REM 20-Dec-04	6522/1	pcameron	VBM:2004122004 New packagers for wemp

REM ===========================================================================
REM
