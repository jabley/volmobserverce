************************************************************
** **
** MCS/MPS Installation Instructions for WAS **
** **
************************************************************


This is a pre-release version of the installer used to install a combined
MCS/MPS on to WAS. WebSphere Portal is not required.



Extracting the Multi-Channel Server and Message Preparation Server
installation image
-----------------------------------------------------------------------------------

To extract the Multi-Channel Server and Message Preparation Server
installation image:

1. Log in as the user who installed WebSphere Application Server and cd into

        <was_root>

where <was_root> is the WebSphere Application Server root directory
e.g. /opt/WebSphere/AppServer.

2. Untar the mcsmpsWASInstall.tar archive into the <was_root> directory. This
creates a mps subdirectory.


Configuring a DB2 database for Multi-Channel Server
---------------------------------------------------

You must use DB2 8.1 Universal Database Enterprise Server Edition with Fix
Pack 6a as your database server.
You can install the database server on the system where you will install
Multi-Channel Server and Message Preparation Server or on another system. Refer
to DB2 documentation for detailed planning and installation information.


Perform the following steps on your database server to configure your
Multi-Channel Server DB2 database.


1. Log in as the DB2 instance user. If you are logged in as root, run the
following command to change to the
DB2 instance user:

su - <db2instance_userID>

2. Run the db2start command to ensure that the DB2 server is started.

3. Create a new database. In the following example, /db/mcsdbloc represents
the path of the directory to contain
the new database. The DB2 instance user and group must own mcsdbloc.

db2 create db mcs on /db/mcsdbloc using codeset UTF-8 territory us

In our example, the database name is mcs. If you choose another database name,
use the chosen name instead of mcs in subsequent steps.

4. Update the mcs database configuration using the following command.

db2 update db cfg for mcs using logfilsiz 1000 logprimary 10 logsecond 10

5. Connect to the mcs database using the following command.

db2 connect to mcs user <db2instance_userID> using <db2instance_password>

6. If your database server is on the same server as Multi-Channel Server,
change the working directory to:

<was_root>/mps/mcs/repository/sql/db2

Otherwise, copy the file
<was_root>/mps/mcs/repository/sql/db2/create_vm_tables.sql from the server
where you are installing Multi-Channel Server and Message Preparation Server
to a directory on your database server. Then, make that directory your working
directory.

7. Run the following command to create the Multi-Channel Server tables:

db2 -tf create_vm_tables.sql

8. Run the following command to start the JDBC applet ("net" driver) on port
6789.

db2jstrt 6789

Note: Use port 6789 unless another application (including another database
instance) is already using port 6789.

9. Run the following command to exit the DB2 instance user if you used the su
command in step 1:

exit


Configuring an Oracle database for Multi-Channel Server
-------------------------------------------------------

You must use Oracle Database V9i Release 2 (V9.2.0.5), as your database
server. You can install the database server on the system where you install
Multi-Channel Server and Message Preparation Server or on another system.
Refer to Oracle Database documentation for detailed planning and installation
information.

Perform the following steps on your database server to configure your
Multi-Channel Server Oracle database.


1. Log in to the Oracle user. (An Oracle user is equivalent to a DB2
instance.) If you are logged in as root, enter the following command to change
to the Oracle user:

su - <oracle_user>

2. Use the Oracle database configuration assistant to create a new
Multi-Channel Server database called mcs.
If you choose another database name, use the chosen name instead of mcs in
subsequent steps.
Note: You must create the database as a UTF-8 character set database.

3. Run the command echo $ORACLE_HOME to be sure that the environment variable
is set to the correct value. If it is not set as specified in the Oracle
documentation, set it to conform to the Oracle documentation. Run the
following commands:


ORACLE_HOME=<db_root>
export ORACLE_HOME

where <db_root> is the value specified in the Oracle documentation.

4. Run the command echo $ORACLE_SID to be sure that the environment variable
is set to the correct value. If it is
not, run the following commands:

ORACLE_SID=mcs
export ORACLE_SID

5. If your database server is on the same server as WebSphere Application
Server, change the working directory to:

<was_root>/mps/mcs/repository/sql/oracle

Otherwise, copy the file
<was_root>/mps/mcs/repository/sql/oracle/create_vm_tables.sql from the server
where you are installing Multi-Channel Server and Message Preparation Server
to a directory on your database server.
Then, make that directory your working directory.


6. Go through the following command sequence to log in to SQL*Plus, the Oracle
SQL command line interface (CLI).
Enter the user ID and password of the Oracle system administrator.

sqlplus
Enter user-name: <sysadmin_userID>
Enter password: <sysadmin_password>

7. Create an Oracle database user ID and password for the Multi-Channel Server
database. At the SQL> prompt,
run the following command.

create user <mcs_userID> identified by <mcs_user_password>;

where <mcs_userID> is the user ID for the database user, and
<mcs_user_password> is the password for the
database user.

8. Grant connect and resource privileges to the database user you just
created. At the SQL> prompt, run the
following command.

grant connect, resource to <mcs_userID>;

9. Connect to the Multi-Channel Server database using the database user you
just created. At the SQL> prompt, go
through the following command/response sequence.

connect
Enter user-name: <mcs_userID>
Enter password: <mcs_user_password>
Connected.

10. Create the Multi-Channel Server tables. At the SQL> prompt, run the
following command.

start create_vm_tables.sql

11. Exit SQL*Plus. At the SQL> prompt, run the following command.

exit

12. Run the following command to exit the Oracle user if you used the su
command in Step 1::

exit

13. Determine the Oracle port number and ensure that the port is open.

By default, Oracle listens on port 1521. If you are not sure what port number
is being used, check the port number in the $ORACLE_HOME/network/admin/listener.ora
and $ORACLE_HOME/network/admin/tnsnames.ora files.



Installing Multi-Channel Server and Message Preparation Server
--------------------------------------------------------------

Perform the following procedure on the server where you are installing
Multi-Channel Server and
Message Preparation Server.


1. Log in to the server as the user who installed WebSphere Application
Server.


2. Ensure that the database server and the database listener are running.

a) If you are using a remote DB2 database server and the DB2 client software
is not installed on the server where you are installing Multi-Channel Server
and Message Preparation Server, copy the following files from your database
server to a directory on your application server:

<db_root>/<db2instance>/sqllib/java/db2java.zip
<db_root>/<db2instance>/sqllib/java/db2jcc.jar

where <db_root> is the home directory selected during DB2 installation and
<db2instance> is the user ID of the DB2 instance user.

Later in this procedure you will add db2java.zip and db2jcc.jar to the Java
Virtual Machine (JVM) classpath and to the CLASSPATH environment variable. At
that time, specify the location of the files that you just copied onto your
server.

b) If you are using a remote Oracle database, you must copy the ojdbc14.jar
file from the remote Oracle server to the server where you are installing
Multi-Channel Server and Message Preparation Server. The typical
location of this file on the database server is the <oracle_root>/jdbc/lib
directory where <oracle_root> is the directory where Oracle has been installed.

Later in this procedure, you will add ojdbc14.jar to the Java Virtual Machine
(JVM) classpath and to CLASSPATH environment variable. Specify the location of
the file that you just copied onto your server.


3. Ensure that the application server that Multi-Channel Server and Message
Preparation Server will be installed on is running.

4. Add the JDBC driver jars to the Java Virtual Machine
(JVM) classpath of the application server that Multi-Channel Server and Message
Preparation Server will be installed on.
Log in to the WebSphere Application Server admin console to update the JVM
classpath as follows.

a. Select Servers icon: forward arrow Application Servers.
b. Click the server link for the application server that Multi-Channel Server
   and Message Preparation Server will be installed on.
c. Under Additional Properties, click the Process Definition link.
d. Under Additional Properties, click the Java Virtual Machine link .
e. In the Classpath field, add entries for your JDBC driver.

Example entries follow for DB2:


<db_root>/<db2instance>/sqllib/java/db2java.zip
<db_root>/<db2instance>/sqllib/java/db2jcc.jar

where <db_root> is the home directory selected during DB2 installation and
<db2instance> is the user ID of the DB2 instance user.

Note: If DB2 is not installed on the server, specify the path to db2java.zip
and db2jcc.jar files that you copied from the database server to your
application server.


Example entry follows for Oracle:

<db_root>/jdbc/lib/ojdbc14.jar

where <db_root> is the value of the ORACLE_HOME environment variable set in
"Configuring an Oracle database for Multi-Channel Server" in this topic.

Note: If Oracle is not installed on your server, specify the path to
ojdbc14.jar that you copied from the database server to your application server.

f. Click Apply.

g. Save the configuration changes.

5. Run the following command to ensure that the WebSphere environment is set
up:

. <was_root>/bin/setupCmdLine.sh

Note: You must type a space between the "." and the rest of the command.

6. Add the JDBC driver to your current CLASSPATH environment variable, as
illustrated in the following examples:

DB2 example:

CLASSPATH=<db_root>/<db2instance>/sqllib/java/db2java.zip:<db_root>/<db2instance>/sqllib/java/db2jcc.jar:$CLASSPATH
export CLASSPATH

Note: For DB2, <db_root> is the home directory selected during DB2
installation and <db2instance> is the DB2 instance user. If DB2 is not installed
on your server, specify the path to db2java.zip and db2jcc.jar files that you
copied from the database server to your application server.

Oracle example:

CLASSPATH=<db_root>/jdbc/lib/ojdbc14.jar:$CLASSPATH
export CLASSPATH

Note: For Oracle, <db_root> is the value of the ORACLE_HOME environment
variable. If Oracle is not installed on your server, specify the path to
ojdbc14.jar that you copied from the database server to your application server.

7. cd <was_root>/mps/install

8. Run the mcsmpsInstaller script to install Multi-Channel Server and Message
Preparation Server and one or more Message Preparation Server channels.


You must specify the following parameters with the mcsmpsInstaller command:

-DServerName=<server name>
-DHostName=<hostname>
-DHostPort=<host port>
-DWebApp=<existing web app>

Optionally if WAS Security is enabled:
-DWasUserid=<was user>
-DWasPassword=<was password>
[ -DManagedNode=<managed> ]


where

<server name> is the name of the application server (e.g. server1) that
Multi-Channel Server and Message Preparation Server are being installed on.

<hostname> is the fully qualified hostname of the hostname that Multi-Channel
Server and Message Preparation Server are being installed on.

<host port> is the port number associated with the application server that
Multi-Channel Server and Message Preparation Server are being installed on.

<existing web app> is the name of the enterprise application that
Multi-Channel Server and Message Preparation Server are being installed on.
(This enterprise application must already exist.) If the enterprise application
name contains a space, use double quotes around the application name
e.g. -DWebApp="my application name".

<was user> is the name of the WAS user

<was password> is the password of the WAS user

<managed> is true if the node is a managed node; false (or not specified)
otherwise



If Multi-Channel Server and Message Preparation Server should use a database
repository, you must also specify the following parameters:

-DDBVendor=<db vendor>
-DDBHost=<db host>
-DDBPort=<db port>
-DDBInstance=<db instance>
-DDBUser=<db user>
-DDBPass=<db password>
-DDBProject=<db project>

where

<db vendor> is either db2 or oracle.

<db host> is the fully qualified host name of the database server.

<db port> is the database port number for the Multi-Channel Server database.

If DB2 is being used, it is the port number used by the JDBC applet ("net")
driver on the database server. Use port 6789 unless another application
(including another database instance) is already using port 6789.

If Oracle is being used, it is the port defined when Oracle is installed. The
default Oracle port number is 1521.

<db instance> is the name of the Multi-Channel Server database when DB2 is
being used. When Oracle is being used, it is the Oracle SID of the
Multi-Channel Server database.

<db user> is the user ID of the DB2 instance user when DB2 is being used. When
Oracle is being used, it is the Multi-Channel Server database user name.

<db password> is the DB2 instance password for the Multi-Channel Server
database when DB2 is being used. When Oracle is being used, it is the
Multi-Channel Server database user password

<db project> is the default project name. If you are using the Multi-Channel
Server database that is also being used with WebSphere Everyplace Mobile
Portal with WebSphere Portal, specify mobile-portal as the project name.
Otherwise, you can choose your own project name.


Note: If Multi-Channel Server and Message Preparation Server should use a XML
repository, you should not include any of the database repository parameters
listed above.


If you plan to use the SMTP channel adapter provided with Message Preparation
Server, you should also specify the following parameters:


-DSMTPHost=<SMTP host>
-DSMTPAuth=<SMTP auth value>
-DSMTPUser=<SMTP user>
-DSMTPPass=<SMTP password>

where

<SMTP host> is the SMTP relay host through which all messages are sent.
<SMTP auth value> is true or false and specifies whether or not the SMTP relay
host requires requests to be authenticated.
<SMTP user> is the username for authenticating with the SMTP relay host. If
<SMTP auth value> is false, specify none for the username.
<SMTP password> is the password for authenticating with the SMTP relay host.
If <SMTP auth value> is false, specify none for the password.

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




If you plan to use the MMS channel adapter provided with Message Preparation
Server, you should also specify the following parameters:


-DMMSCUrl=<MMSC URL>
-DMMSCCode=<MMSC country code>

where

<MMSC URL> is the URL of the MMSC
<MMSC country code> is the default country code prefix for recipients without
fully qualified MSISDN numbers.


If you plan to use the WAP push channel adapter provided with Message
Preparation Server, you should also specify
the following parameter:


-DWAPPUrl=<WAP push gateway URL>
-DWAPPCode=<WAP push country code>
-DWAPPStoreUrl=<WAP push URL of MSS servlet>

where


<WAP push gateway URL> is the URL of the WAP push gateway.
<WAP push country code> is the default country code prefix for recipients
without fully qualified MSISDN numbers.
<WAP push URL of MSS servlet> is the fully qualified URL of the MSS servlet


For example, the following command configures Multi-Channel Server and Message
Preparation Server to use
a DB2 database repository and all four channel adapters:

./mcsmpsInstaller -DServerName=server1
-DHostName=myhost.com
-DHostPort=9080
-DWebApp=myapplication
-DDBVendor=db2
-DDBHost=mydatabase.server.com
-DDBPort=6789
-DDBInstance=mcs
-DDBUser=db2inst1
-DDBPass=ibmdb2
-DDBProject=mps-project
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
-DWAPPStoreUrl=http://mymssserver:9080/mss


Note: This is one command, so enter it all on the same command line.

If you need to add or modify channel adapter configuration parameters after
installing Message Preparation Server, you can update the channel elements in
the mcs-config.xml file. See "Updating mcs-config.xml" for more details.

9. If you plan to use the SMS channel adapter provided with Message
Preparation Server, you must download the Logica SMPP smpp.jar file from
http://opensmpp.logica.com/introhtml/menu.htm and copy it to 
<was_root>/installedApps/<cell-name>/<enterprise application name>/mcs.war/WEB-INF/lib/.

10. If you plan to use the MMS channel adapter provided with Message
Preparation Server, you must obtain the Nokia MMSC nokia_mmsdriver-1.5.jar file
from http://www.forum.nokia.com/main/0,,034-741,00.html. The library file is
contained within the Nokia Mobile Server Services SDK 1.5 which is
distributed as a Windows binary file. The jar file can be obtained as follows.

a. Download the NMSS API and Library package from the specified Nokia URL
b. Install as directed in the Nokia install instructions.
c. Copy the mmsdriver.jar from 
   <nokia_root>/Server_SDKs/Libraries_1_5/lib/ to 
   <was_root>/installedApps/<cell-name>/<enterprise application name>/mcs.war/WEB-INF/lib/
d. Copy any supporting jars not provided by MCS/MPS from
   <nokia_root>/Server_SDKs/Libraries_1_5/lib/ext/ to 
   <was_root>/installedApps/<cell-name>/<enterprise application name>/mcs.war/WEB-INF/lib/
e. Rename the mmsdriver.jar file to nokia_mmsdriver-1.5.jar

11. Run the following command to import the default device policies:-

UNIX platform: ./mcsmpsInstaller mps-import-devices -DDBProject=<db project>

Windows Platform: mcsmpsInstaller.bat mps-import-devices -DDBProject=<db project>

The results of the import operation are displayed by the task.
Verify that the import was successful before continuing.

12. Restart the application server that you installed Multi-Channel Server and
Message Preparation Server on.


Installing a standalone MSS
---------------------------

A standalone MCS/MSS can be configured to run on a server other than that
running MPS (Note a copy of MSS will always be deployed on to the MPS server).
This can be installed using the following command:-


./mcsmpsInstaller mps-deploy-mss -DServerName=server1
-DHostName=myhost.com
-DHostPort=9080
-DWebApp=myapplication
-DDBVendor=db2
-DDBHost=mydatabase.server.com
-DDBPort=6789
-DDBInstance=mcs
-DDBUser=db2inst1
-DDBPass=ibmdb2
-DDBProject=mps-project

The DB parameters should match those specified on the MPS server.


Updating mcs-config.xml
-----------------------

To update the mcs-config.xml file, perform the following procedure on the
server where Multi-Channel Server and Message Preparation Server are installed:


1. Log in to the server as the user who installed WebSphere Application
Server.

2. Copy the mcs-config.xml file from the
<was_root>/installedApps/<cell_name>/<enterprise-app-name>.ear/mcs.war/WEB-INF
directory to the <was_root>/mps/mcs directory where <enterprise-app-name> is
the name of the enterprise application that Multi-Channel Server and Message
Preparation Server are installed with.

3. Edit the mcs-config.xml file appropriately.


4. Ensure that the application server that Multi-Channel Server and Message
Preparation Server are installed
on is running.

5. Run the following command to ensure that the WebSphere environment is set
up:

. <was_root>/bin/setupCmdLine.sh

Note: You must type a space between the "." and the rest of the command.


6. cd to the <was_root>/mps/install directory and run the following command:

./mcsmpsInstaller mps-update-config -DServerName=<server name>
-DWebapp=<existing webapp>
[ -DWasUserid=<was user> ]
[ -DWasPassword=<was password> ]
[ -DManagedNode=<managed> ]

where <existing webapp> is the name of the enterprise application that
Multi-Channel Server and Message Preparation server are installed with.


7. Restart the application server that Multi-Channel Server and Message
Preparation Server are installed on.


Uninstalling Multi-Channel Server and Message Preparation Server
----------------------------------------------------------------

To uninstall Multi-Channel Server and Message Preparation Server, perform
the following procedure on the server where they are installed:


1. Log in to the server as the user who installed WebSphere Application
Server.

2. Ensure that the application server that Multi-Channel Server and Message
Preparation Server are installed on is running.

3. Run the following command to ensure that the WebSphere environment is set
up:

. <was_root>/bin/setupCmdLine.sh

Note: You must type a space between the "." and the rest of the command.


4. cd to the <was_root>/mps/install directory and run the following command:


./mcsmpsInstaller mps-uninstall-mcsmps
-DServerName=<server name>
-DHostName=<hostname>
-DWebApp=<existing webapp>
[ -DWasUserid=<was user> ]
[ -DWasPassword=<was password> ]
[ -DManagedNode=<managed> ]

where
<server name> is the name of the application server (e.g. server1) that
Multi-Channel Server and Message Preparation Server
are installed on.
<hostname> is the fully qualified hostname of the server that Multi-Channel
Server and Message Preparation Server
are installed on.
<webapp> is the name of the enterprise application that Multi-Channel Server
and Message Preparation Server
are installed on.


Note: This is one command, so enter it all on the same command line.


5. Remove the <was_root>/mps directory.

6. Restart the application server that Multi-Channel Server and Message
Preparation Server
were installed on.
