
set JDBC_DRIVER=\path\to\jdbcdriver.jar

set CLASSPATH=..\lib\mcs.cli-api-@MCS_VERSION@.jar;..\lib\jakarta-regexp-1.4.jar;..\lib\jdo2-api-2.0.jar;..\lib\jibx-run-1.1.5.jar;..\lib\log4j-1.2.8.jar;..\lib\volantis-jdom-0.9.jar;..\lib\volantis-xerces-2.6.2.jar;..\lib\xpp3-1.0.jar;..\lib\mcs.localization-api-@MCS_VERSION@.jar;..\lib\synergetics.localization-api-@SYNERGETICS_VERSION@.jar;..\lib\synergetics.repository-api-@SYNERGETICS_VERSION@.jar;..\lib\synergetics.repository-impl-@SYNERGETICS_VERSION@.jar;..\lib\device-repository.api-api-@DEVICE_REPOSITORY_VERSION@.jar;..\lib\device-repository.repository-api-@DEVICE_REPOSITORY_VERSION@.jar;..\lib\device-repository.repository-impl-@DEVICE_REPOSITORY_VERSION@.jar;..\lib\mcs.repository-api-@MCS_VERSION@.jar;..\lib\mcs.repository-impl-@MCS_VERSION@.jar;..\lib\synergetics.runtime-api-@SYNERGETICS_VERSION@.jar;..\lib\synergetics.cornerstone-api-@SYNERGETICS_VERSION@.jar;..\lib\mcs.model-api-@MCS_VERSION@.jar;..\lib\mcs.cornerstone-api-@MCS_VERSION@.jar;..\lib\device-repository.localization-api-@DEVICE_REPOSITORY_VERSION@.jar;..\lib\device-repository.device-api-@DEVICE_REPOSITORY_VERSION@.jar;..\lib\synergetics.metadata-api-@SYNERGETICS_VERSION@.jar;..\lib\synergetics.metadata-impl-@SYNERGETICS_VERSION@.jar;..\lib\mcs.model-impl-@MCS_VERSION@.jar;%JDBC_DRIVER%

java com.volantis.mcs.cli.MarinerExport %*

