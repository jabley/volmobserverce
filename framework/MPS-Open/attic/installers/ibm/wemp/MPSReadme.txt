************************************************************
** **
** MPS Installation Instructions for WebSphere Portal **
** **
************************************************************


This is a pre-release version of the installer used to install MPS on top of an
existing MCS installation in WebSphere Portal.

Installing Message Preparation Server
--------------------------------------



1. If you plan to use the SMS channel adapter provided with Message
Preparation Server, you must obtain the Logica
SMPP smpp.jar file from http://opensmpp.logica.com/introhtml/menu.htm
and copy it to a directory on your portal server.


2. Add the channel adapters jars to the Java Virtual Machine
(JVM) classpath of the WebSphere_Portal application server by
logging in to the WebSphere Application
Server admin console and updating the JVM classpath as follows.

a. Select Servers icon: forward arrow Application Servers.
b. Click the server link for the WebSphere_Portal application server
c. Under Additional Properties, click the Process Definition link.
d. Under Additional Properties, click the Java Virtual Machine link .
e. If you are planning to use the SMS channel adapter provided with Message
Preparation Server, you should add an entry to the Classpath field
that specifies the location of the smpp.jar file.
g. Click Apply.
h. Save the configuration changes.

The details for the MMS channel adapter jars are specified later in this
procedure.


3. Log in as the user who installed WebSphere Portal


4. Change your working directory to the <wp_root> directory


5. Extract the MPSWPInstallPackage.tar file into the <wp_root> directory using
the following command:

tar -xvf <mnt>/inst.images/MPSWPInstallPackage.tar

where <mnt> is the mount point of the CD-ROM drive or the directory where you extracted the software image.

6. Run the following command to ensure that the WebSphere environment is set up:

. <was_root>/bin/setupCmdLine.sh

Note: You must type a space between the "." and the rest of the command.

7. Ensure the WebSphere_Portal application server is running.


8. Change to the <wp_root>/config directory and run the following command:

./WPSconfig.sh mps-deploy-mps


This command has the following sets of optional parameters:

a. Security parameters

For security reasons, if you do not want to store the WebSphere Portal
admin password and WebSphere Application Server admin password in the
wpconfig.properties file, you can specify the passwords using the parameters
shown below:


-DPortalAdminPwd=<WP_admin_password_value>
-DWasPassword=<WAS_admin_password_value>

where

<WP_admin_password_value> is the value of the WebSphere Portal admin password
<WAS_admin_password_value> is the value of the WebSphere Application Server
admin password if WebSphere Application Server security is enabled


b. SMTP parameters


If you plan to use the SMTP channel adapter provided with Message Preparation
Server, you should also specify the following parameters:


-DSMTPHost=<SMTP host>
-DSMTPAuth=<SMTP auth value>
-DSMTPUser=<SMTP user>
-DSMTPPass=<SMTP password>

where

<SMTP host> is the SMTP relay host through which all messages are sent.
<SMTP auth value> is true or false and specifies whether or not the SMTP relay
host requires equests to be authenticated.
<SMTP user> is the username for authenticating with the SMTP relay host. If
<SMTP auth value> is false, specify none for the username.
<SMTP password> is the password for authenticating with the SMTP relay host.
If <SMTP auth value> is false, specify none for the password.



c. SMS parameters

If you plan to use the SMS channel adapter provided with Message Preparation
Server, you should also specify the following parameters:

-DSMSCIp=<SMSC IP address>
-DSMSCPort=<SMSC port>
-DSMSCUser=<SMSC user>
-DSMSCPass=<SMSC password>
-DSMSCBind=<SMSC bind type>
-DSMSCMulti=<SMSC multi-setting>

where

<SMSC IP address> is the IP address of the SMSC
<SMSC port> is the port number that the SMSC is listening on.
<SMSC user> is the username that is used to authenticate with the SMSC.
<SMSC password> is the password that is used to authenticate with the SMSC.
<SMSC bind type> is either async or sync and specifies if PDU responses are
handeled in a synchronous or asynchronous manner. The default value is async
if the -DSMSCBind parameter is not specified.
<SMSC multi-setting> is either yes or no and specifies if a message is sent to
multiple recipients as a single interaction with the SMSC or not. The SMSC
must support receving multiple recipients in a single interaction if "yes" is
specified. The default value is no if the -DSMSCMulti parameter is not
specified.



d. MMS parameters

If you plan to use the MMS channel adapter provided with Message Preparation
Server, you should also specify the following parameters:


-DMMSCUrl=<MMSC URL>
-DMMSCCode=<MMSC country code>

where

<MMSC URL> is the URL of the MMSC
<MMSC country code> is the default country code prefix for recipients without
fully qualified MSISDN numbers.



e. WAP Push parameters

If you plan to use the WAP push channel adapter provided with Message
Preparation Server, you should also specify the following parameter:


-DWAPPUrl=<WAP push gateway URL>
-DWAPPCode=<WAP push country code>
-DWAPPStoreUrl=<MSS server URL>


where


<WAP push gateway URL> is the URL of the WAP push gateway.

<WAP push country code> is the default country code prefix for recipients
without fully qualified MSISDN numbers.

<MSS server URL> is the URL of the MSS servlet. If the MSS servlet is being
installed with WebSphere Portal, the URL would be
http://<wp-hostname>:<wp_portnum>/<wp_context_root>/mss


For example, the following command configures Message Preparation Server to use
all four channel adapters:

./WPSconfig.sh mps-deploy-mps
-DSMTPHost=myrelay.com
-DSMTPAuth=false
-DSMTPUser=none
-DSMTPPass=none
-DSMSCIp=127.0.0.1
-DSMSCPort=8888
-DSMSCUser=myname
-DSMSCPass=mypassword
-DSMSCBind=async
-DSMSCMulti=no
-DMMSCUrl=http://mymmsc.com
-DMMSCCode=+44
-DWAPPUrl=http://mywappushgateway.com
-DWAPPCode=+44
-DWAPPStoreUrl=http://mywphost:9081/wps/mss


Note: This is one command, so enter it all on the same command line.

If you need to add or modify channel adapter configuration parameters after
installing Message Preparation Server, you can update the channel elements
in the mcs-config.xml file.

9. If you plan to use the MMS channel adapter provided with Message
Preparation Server, you must obtain the Nokia MMSC nokia_mmsdriver-1.5.jar file
from http://www.forum.nokia.com/main/0,,034-741,00.html. The library file is
contained within the Nokia Mobile Server Services SDK 1.5 which is
distributed as a Windows binary file. The jar file can be obtained as follows.

a. Download the NMSS API and Library package from the specified Nokia URL
b. Install as directed in the Nokia install instructions.
c. Copy the mmsdriver.jar from <nokia_root>/Server_SDKs/Libraries_1_5/lib/
   to <wemp-root>/shared/mps
d. Copy any supporting jars not provided by MCS/MPS from
   <nokia_root>/Server_SDKs/Libraries_1_5/lib/ext/ to <wemp-root>/shared/mps
e. Rename the mmsdriver.jar file to nokia_mmsdriver-1.5.jar


10. Update the libraries.xml file that corresponds to the node on which MPS is installed.

a. Open the file <was_root>/config/cells/<cell_name>/nodes/<node_name>/libraries.xml
b. Navigate to the library definition named MPSlib
c. Add the following entry for the Nokia MMS driver

<classPath>${WPS_HOME}/shared/mps/nokia_mmsdriver-1.5.jar</classPath>

d. For an entry for each of the Nokia dependencies in the following format

<classPath>${WPS_HOME}/shared/mps/<depends>.jar</classPath>


11. Restart the WebSphere_Portal application server using the following commands:

<was_root>/bin/stopServer.sh WebSphere_Portal
<was_root>/bin/startServer.sh WebSphere_Portal


Installing a standalone MSS
---------------------------

A standalone MCS/MSS can be configured to run on a server other than that
running MPS (Note a copy of MSS will always be deployed on to the MPS server).
This can be installed using the following command:-


1. To install the MSS servlet on the WebSphere_Portal application server,
run the following command:

./WPSconfig.sh mps-deploy-mss


Notes:
a) If the MSS servlet will be installed separately from the WebSphere_Portal
application server, refer to the install instructions for installing MCS
and MPS directly on WebSphere Application Server.


2. Restart the WebSphere_Portal application server using the following commands:

<was_root>/bin/stopServer.sh WebSphere_Portal
<was_root>/bin/startServer.sh WebSphere_Portal


Uninstalling Message Preparation Server
---------------------------------------

To uninstall Message Preparation Server, perform the following steps:

1. Log in as the user who installed WebSphere Portal

2. Change your working directory to the <wp_root>/config directory


3. Run the following command to ensure that the WebSphere environment is set up:

. <was_root>/bin/setupCmdLine.sh

Note: You must type a space between the "." and the rest of the command.

4. Ensure the WebSphere_Portal application server is running.


5. Change to the <wp_root>/config directory and run the following command:

./WPSconfig.sh mps-uninstall-mps


6. Restart the WebSphere_Portal application server using the following commands:

<was_root>/bin/stopServer.sh WebSphere_Portal
<was_root>/bin/startServer.sh WebSphere_Portal