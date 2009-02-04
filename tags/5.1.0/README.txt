Building instructions.

1. System requirements
1.1 Building Volantis Mobility Server requires Java 2 Standard Edition Development Kit version 5.
1.2. The only supported operating system is Linux.

2. Package contents
Package consists of 3 parts: 
2.1. Build bootstrap
2.2. Framework sources 
2.3. Ivy repository

3. Building steps
3.1. Place Ivy repository ('repository' folder in the tarball ) in /srv/repository
3.2. Put 'build' script on your PATH environment variable
3.3. Set and export JAVA_HOME environment variable pointing to location of your JDK
3.4. Set and export GBUILD_LOCATION environmennt variable pointing to Bootstrap folder
3.5. Build and publish artifacts to Ivy repository, using following command:
build publish-local

Products are dependant on each other, so order of building is important. Products should be built in following order:
TestTools-Open
Synergetics-Open  
Pipeline-Open  
DeviceRepository-Open 
MediaAccessProxy-Open 
MCS-Open  
MPS-Open  

3.6 Build installers using following command:
build installer

Installers can be built for following products:
MediaAccessProxy-Open 
MCS-Open  
MPS-Open
