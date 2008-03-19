#!/bin/sh
REPOSITORY_HOME=@WPS-INSTALL-LOCATION@/mcs/repository
CLASSPATH=$CLASSPATH:$REPOSITORY_HOME/lib/jdbc2_0-stdext.jar:$REPOSITORY_HOME/lib/volantis-jdom.jar:$REPOSITORY_HOME/lib/log4j.jar:$REPOSITORY_HOME/lib/volantis-mcs-cli.jar:$REPOSITORY_HOME/lib/volantis-mcs-common.jar:$REPOSITORY_HOME/lib/volantis-synergetics.jar:$REPOSITORY_HOME/lib/volantis-xercesImpl.jar:$REPOSITORY_HOME/lib/volantis-xmlParserAPIs.jar:$REPOSITORY_HOME/lib/jakarta-regexp-1.3.jar:$REPOSITORY_HOME/lib/volantis-xml-apis.jar:$REPOSITORY_HOME/lib/mcs-api.jar



DEFAULT_ARGS="-vendor @ODBC-VENDOR@ -host @ODBC-HOST@ -port @ODBC-PORT@ -source @ODBC-SOURCE@ -user @ODBC-USER@ -password @ODBC-PASSWORD@ -updateall -enableundo"

export CLASSPATH

if [ "$JAVA_HOME" = "" ]
then
	echo "JAVA_HOME must be set" 
	exit 1
fi


$JAVA_HOME/bin/java com.volantis.mcs.cli.MarinerImport $DEFAULT_ARGS "$@" 


#
# ===========================================================================
# Change History
# ===========================================================================
# $Log$

# 11-Jan-05	6413/1	pcameron	VBM:2004120702 Some fixes for WEMP from alanw

# 20-Dec-04	6522/3	pcameron	VBM:2004122004 Fixed path in ImportPolicies scripts for was

# 20-Dec-04	6522/1	pcameron	VBM:2004122004 New packagers for wemp

# 20-Dec-04	6496/1	ianw	VBM:2004121502 interim commit for Peter

# ===========================================================================
#
