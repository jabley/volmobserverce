/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server. If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/utilities/Volantis.java,v 1.292 2003/04/28 16:14:55 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history and
 *                              renamed ButtonImageComponent to
 *                              ButtonImageComponent.
 * 29-Jun-01    Paul            VBM:2001062906 - Simplify the use of the
 *                              AppServerInterfaceManager.
 * 29-Jun-01    Paul            VBM:2001062810 - Added VoiceXMLVersion1_0
 *                              protocol and removed HTMLVersion3_2Ext1
 *                              protocol.
 * 23-Jul-01    Paul            VBM:2001070507 - Added WapTV1_WMLVersion1_2
 *                              protocol and some debug logging.
 * 27-Jul-01    Paul            VBM:2001072603 - Added support for modesets.
 * 02-Aug-01    Allan           VBM:2001072604 - Get new config debug
 *                              attribute "comments" and set its value.
 * 03-Aug-01    Payal           VBM:2001071303 -prefix-url for chart-assets
 *                              removed.
 * 03-Aug-01    Doug            VBM:2001072504 Read the 'scripts base-directory'
 *                              value out of the config file and provide getter
 *                              method to return this to client.
 * 08-Aug-01    Payal           VBM:2001071303 - Renamed getImageDirRoot method
 *                              to getPageBase.Removed chartimagesBaseDir.
 *                              Added getChartImagesBase to get value out of
 *                              the config file.
 * 09-Aug-01    Payal           VBM:2001080808 - Renamed getCSSBaseDir method
 *                              to getCSSBase similar change done for modesets
 *                              and scripts as css,modesets,scripts will use
 * 09-Aug-01    Payal           VBM:2001080808 - Renamed getCSSBaseDir method
 *                              to getCSSBase similar change done for modesets
 *                              and scripts as css,modesets,scripts will use
 *                              base attribute from config file.
 * 09-Aug-01    Allan           VBM:2001080805 - Added support for new jdbc
 *                              config properties: keep-connections-alive,
 *                              and connection-pole-interval.
 * 10-Aug-01    Allan           VBM:2001080905 - Made keep-connections-alive
 *                              and connection-poll-interval work as
 *                              optional config entries. Replace pole with
 *                              poll.
 * 10-Aug-01    Doug            VBM:2001062705 Added the HTMLLiberate protocol
 *                              to the protocol list
 * 16-Aug-01    Paul            VBM:2001080805 - Removed confusing log message
 *                              which indicated that connection pooling was
 *                              disabled when it wasn't.
 * 23-Aug-01    Mat             VBM:2001082303 - Make volantisBean non-static
 * 23-Aug-01    Doug            VBM:2001082308 - Read the base attribute of
 *                              the dtd element from the config file. Provided
 *                              getter so clients can access this value.
 * 04-Sep-01    Allan           VBM:2001083101 - Added HTMLNetgem protocol to
 *                              the protocol list.
 * 05-Sep-01    Payal           VBM:2001061505 - Added
 *                              PluginAttributesRepositoryManager.
 * 06-Sep-01    Kula            VBM:2001083116 - WMLPhoneDotCom protocol added
 *                              to support phone.com browsers
 * 10-Sep-01    Allan           VBM:2001083118 - Added marinerAgent
 *                              initialization in the initialize method.
 * 19-Sep-01    Allan           VBM:2001091103 - Removed WMLWideScreen
 *                              protocol.
 * 19-Sep-01    Doug            VBM:2001091701 Added code to read the optional
 *                              page-base attribute out of the config file
 * 21-Sep-01    Doug            VBM:2001090302 - added a LinkRepositoryManager
 * 27-Sep-01    Allan           VBM:2001082106 - Removed call to System.exit()
 *                              inside the initialize() catch block. Also
 *                              log messages inside this catch block.
 * 27-Sep-01    Doug            VBM:2001092002 - optional xml-reader attribute
 *                              is now read in from config file. Added
 *                              getXMLReader method to return a XMLReader
 *                              object, if xml-reader is present in config file
 *                              then instance of this is returned otherwise the
 *                              system default is returned.
 * 09-Oct-01    Payal           VBM:2001090605 - Added HTMLWebTVprotocol to the
 *                              protocol list and removed
 *                              HTMLVersion3_2Transparent protocol from
 *                              protocol list.
 * 10-Oct-01    Allan           VBM:2001100108 - Add licence verification.
 *                              The location of the license file is read
 *                              in from the config file and it is verified
 *                              in a method called initializeAccessors().
 *                              All references to license that could be
 *                              seen in a decompiled version of this class
 *                              are named something not relevant to licenses.
 * 11-Oct-01    Allan           VBM:2001100108 - Move verification to near
 *                              the beginning of bean initialization so that
 *                              if it fails Mariner will break.
 * 12-Oct-01    Doug            VBM:2001101201 - removed the property dtdBase,
 *                              the static String CONFIG_DTD_ELEMENT and the
 *                              method getDTDBase(). Also deleted the code that
 *                              initializes the dtdBase property.
 * 15-Oct-01    Doug            VBM:2001101101 - removed the property cssBase,
 *                              the static String CONFIG_CSS_ELEMENT and the
 *                              method getCSSBase(). Also deleted the code that
 *                              initializes the cssBase property.
 * 15-Oct-01    Paul            VBM:2001101202 - Use getDeviceFallbackChain
 *                              instead of retrieveDevice as it no longer
 *                              resolves all the devices on the fallback chain.
 * 16-Oct-01    Doug            VBM:2001100806 - if xml-reader is not specified
 *                              in the config file we return an instance of
 *                              "com.volantis.mcs.xmlparser.parsers.SAXParser".
 *                              we used to return the System default parser.
 * 16-Oct-01    Doug            VBM:2001100806 - Uncommented the call to
 *                              initializeAccessors(). I had commented this
 *                              call out to test on ATG Dynamo and accidentally
 *                              checked it in during the previous edit - Phew.
 * 17-Oct-01    Allan           VBM:2001101702 - Re-commented the line in
 *                              initialize() that makes HTML4.0 use
 *                              HTMLVersion3_2.
 * 24-Oct-01    Payal           VBM:2001082202 - Added UserInterfaceRepository
 *                              Manager.In order to output to  the page
 *                              the styles that do not have attribute
 *                              cssExclude set to true Added method
 *                              initializeCssExclude which creates a vector
 *                              having property which have cssExclude
 *                              attribute set to true.Added getCssExclude()
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package, Layout
 *                              has been renamed DeviceLayout and Theme has
 *                              been renamed DeviceTheme.
 * 07-Nov-01    Mat             VBM:2001110701 - Pass reference to the bean
 *                              to the AppServerManager, also changed
 *                              getRepositoryPropsFromConfig() to add some
 *                              from the web-application section of the config.
 * 08-Nov-01    Paul            VBM:2001110701 - Modified
 *                              getRepositoryPropsFromConfig to pass the
 *                              AppServerInterfaceManager's DataSource if any
 *                              to the JDBCRepository.createJDBCRepository method
 *                              rather than have it retrieve it itself.
 * 08-Nov-01    Pether          VBM:2001110803 - Added methods to flush the
 *                              different caches.
 * 12-Nov-01    Pether          VBM:2001110803 - Changed name of the methods
 *                              flushAllCache to flushAllCaches and
 *                              flushPolicy to flushPolicyPreferenceCache.
 * 19-Nov-01    Paul            VBM:2001110202 - Removed unused references to
 *                              servlet specific classes.
 * 21-Nov-01    Payal           VBM:2001111202 - Modified method
 *                              initialize to add REVISION_TABLE_NAME constant
 * 22-Nov-01    Paul            VBM:2001110202 - Changed the baseURL to be a
 *                              URL object rather than a string and added
 *                              a count of the number of requests which have
 *                              been made.
 * 26-Nov-01    Doug            VBM:2001112004 - Added the capability to read
 *                              the plugin element info from the config file.
 *                              Added URLRewriterManager member and associated
 *                              getter method.
 * 28-Nov-01    Paul            VBM:2001112202 - Added getAbsoluteURL and
 *                              change package of URLRewriter.
 * 29-Nov-01    Mat             VBM:2001112903 - Implement basic CC/PP header
 *                              usage in device patterns.  Added ProfileString
 *                              to getDevice() to test for an Opt: header
 *                              and use the profile in that if it exists.
 * 03-Dec-01    Doug            VBM:2001112901 - Added and initialized a
 *                              ExternalRepositoryPluginManager. Added a
 *                              flushExternalPluginsCache() method and modified
 *                              flushAllCaches() call this method.
 * 05-Dec-01    Paul            VBM:2001112303 - Modified getDevice (request)
 *                              to ignore user agent if it is not specified
 *                              and to default to PC if the device name cannot
 *                              be found by any means.
 * 03-Jan-02    Payal           VBM:2001110707 - Modified initializeAccessors()
 *                              so that host name check uses equalsIgnoreCase()
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Used manager to retrieve theme
 *                              and removed unused getSupportedUserType method.
 * 09-Jan-02    Paul            VBM:2002010403 - Used UPDATE_TABLE from Export
 *                              instead of the one from VolantisEnvironment.
 * 09-Jan-02    Paul            VBM:2002010403 - Updated to reflect changes in
 *                              other classes and removed dependency on
 *                              HttpServletRequest by using the RequestHeaders
 *                              class instead in the getDevice method.
 * 10-Jan-02    Allan           VBM:2001121703 - Modified initialize() and
 *                              getRepositoryPropsFromConfig() to set up an xml
 *                              repository if the config file specifies this
 *                              type of repository.
 * 11-Jan-02    Paul            VBM:2002010403 - Updated to match changes made
 *                              to VolantisEnvironment.
 * 11-Jan-02    Mat             VBM:2001121403 - Changed getDevice() to look
 *                              for a header ending in -profile instead of
 *                              Opt:
 * 21-Jan-02    Mat             VBM:2002011801 - Added
 *                              generateUniqueRequestString() to provide a
 *                              unique number valid for the request.
 * 24-Jan-02    Doug            VBM:2002011406 - made the baseURL property
 *                              a MarinerURL rather than a java.set.URL.
 *                              Added an AssetURLRewriter property and an
 *                              associated getter method. Renamed
 *                              getAbsoluteURL() to getClientAccessibleURL()
 *                              and it now returns a MarinerURL rather than
 *                              a String.
 * 28-Jan-02    Steve           VBM:2002011412 - Calls DeviceLayoutReplicator
 *                                to fix up layouts for replicates.
 * 01-Feb-02    Doug            VBM:2002011406 - changed import statement for
 *                              DefaultAssetURLRewriter as its' package has
 *                              changed.
 * 11-Feb-02    Adrian          VBM:2002012209 - Modified method
 *                              flushComponentAssetCache to call...
 *                              refreshPluginAttributeCache.
 * 11-Feb-02    Paul            VBM:2001122105 - Added support for scripts
 *                              and made sure that all the base urls ended with
 *                              a /.
 * 13-Feb-02    Paul            VBM:2002021203 - Added PageGenerationCache.
 * 19-Feb-02    Adrian          VBM:2002021909 - Corrected spelling mistake in
 *                              call to setVolantisProtocol in initialise()
 *                              for XHTMLTransitional.
 * 01-Mar-02    Ian              VBM:2002030103 - Ensure profileHeader is
 *                              set to null if not found in the headerIterator.
 * 04-Mar-02    Adrian          VBM:2002021908 - Added class variables for
 *                              dynamicVisualAsset/Component
 *                              RepositoryObjectManagers, and SelectionPolicy.
 *                              Also now instantiate these managers by calling
 *                              RepositoryObjectManager.getInstance().
 *                              Added new method
 *                              getDynamicVisualAssetSelectionPolicy as this
 *                              cannot be returned as an VariantSelectionPolicy
 *                              as it has two retrieveBest methods to account
 *                              for retrieval with or without a specified
 *                              encoding.
 * 06-Mar-02    Allan           VBM:2002030504 - Modified flushDeviceCache() to
 *                              re-initialize the device pattern cache after
 *                              the refresh.
 * 08-Mar-02    Paul            VBM:2002030607 - Changed Hashtables to
 *                              Map/HashMap where possible, removed unused
 *                              deviceThemes, changed cssExclude to be a
 *                              HashSet rather than a Vector as it is much more
 *                              efficient. Added style sheet configuration.
 * 08-Mar-02    Ian             VBM:2002030620 - Windows NT, Windows 2000 and
 *                              Windows XP all grouped as Win32 in
 *                              initializeAccessors.
 * 18-Mar-02    Steve           VBM:2002021119 - Added Basic J-Sky MML protocol
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Import new wml protocols and
 *                              added default configuration for dom pools.
 * 22-Mar-02    Adrian          VBM:2002031503 - Added Selection Policies,
 *                              RepositoryObjectManagers and associated
 *                              retrieve methods for AssetGroup, Audio, Image,
 *                              Link, Text, Chart, RolloverImage, ButtonImage
 * 28-Mar-02    Allan           VBM:2002022007 - Changed all but one log
 *                              call in getDevice() from info to debug.
 * 28-Mar-02    Adrian          VBM:2002031503 - fixed bug where caches were
 *                              not set on the selection policies
 * 02-Apr-02    Mat             VBM:2002022009 - Added support for the IMD
 *                              repository.
 * 02-Apr-02    Adrian          VBM:2002040203 - caches on selection policies
 *                              shared with asset accessors
 * 03-Apr-02    Adrian          VBM:2001102414 - add methods to get the
 *                              RepositoryObjectManager for Audio, Image, Link,
 *                              Text, Chart, DynamicVisual, Script assets and
 *                              components.
 * 05-Apr-02    Mat             VBM:2002022009 - Removed references to
 *                              getUnderLyingConnection() as these are now
 *                              handled in the accessors.
 * 09-Apr-02    Adrian          VBM:2002022001 - prefix URLs fixed to end in
 *                              "/" character if they do not already as
 *                              MarinerURL removes all chars after the last "/"
 * 17-Apr-02    Allan           VBM:2002041705 - Made the css directory, the
 *                              directory specified in the config file +
 *                              "cssFileCache" in initialize().
 * 25-Apr-02    Paul            VBM:2002042202 - Updated as the HTML protocols
 *                              have moved package.
 * 01-May-02    Mat             VBM:2002040814 - Added support for remote
 *                              repositories and disabled the IMD repository
 *                              as no one uses it at the moment and the
 *                              repository model changed.
 * 02-May-02    Steve           VBM:2002040817 - Added cacheRemoteComponent()
 *                              to add a component retrieved from a remote
 *                              repository to the appropriate cache.
 *                              repository model changed.
 * 02-May-02    Allan           VBM:2002040804 - Set deviceTheme to new
 *                              DeviceTheme from the themes package. Tidied up
 *                              some of the import mess left by the previous
 *                              change.Added new getDeviceTheme().
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Paul            VBM:2002042203 - Changed selection policy
 *                              fields to be of type VariantSelectionPolicy.
 * 04-May-02    Ian             VBM:2002040814 - set remoteRepositoryEntitled
 *                              to true for hutch testing.
 * 07-May-02    Steve           VBM:2002040817 - Methods to flush remote caches
 * 08-May-02    Mat             VBM:2002040814 - Added a RepositoryObjectManager
 *                              for asset groups.
 * 13-May-02    Mat             VBM:2002040818 - Added
 *                              getRemotePolicyCacheAttributes() to read the
 *                              remote cache values from the config file.
 * 16-May-02    Mat             VBM:2002040813 - Added theme cache to remote
 *                              managers
 * 15-May-02    Steve           VBM:2002040816 - Read licence to check if
 *                              remote repository and persistant cache use is
 *                              allowed.
 * 17-May-02    Jason           VBM:2002051702 - Modified output of build date
 *                              version, added copyright message to output.
 * 22-May-02    Mat             VBM:2002040826 - Added code to cope with
 *                              remote policy quotas in the remote managers.
 * 23-May-02    Adrian          VBM:2002041503 - Added method
 *                              getPluginAttributes to retrieve PluginAttribute
 *                              values from the repository.
 * 27-May-02    Paul            VBM:2002050301 - Added device layout selection
 *                              policy.
 * 29-May-02    Paul            VBM:2002050301 - Separated out the
 *                              initialisation of the selection policies,
 *                              added support for retrieving branding and non
 *                              branding versions. Removed cssExclude as it
 *                              is no longer used. Fixed minor problem with
 *                              javadoc using @returns instead of @return.
 * 11-Jun-02    Mat             VBM:2002052001 - Re-enabled IMD repository
 *                              and added the SelectionPolicyManagers for
 *                              IMD and Page repositories.
 * 21-Jun-02    Mat             VBM:2002050902 - Changed getPluginAttributes()
 *                              to use the right connection.
 * 19-Jun-02    Adrian          VBM:2002053104 - Added method isLayoutCache..
 *                              ..Enabled to allow stylesheetGenerator to
 *                              easily test if layout stylesheets should be
 *                              cached.
 * 10-Jul-02    Mat             VBM:2002040825 - Added support for the
 *                              shared repository.
 *                              Changed cacheRemoteComponents to get the
 *                              cache from the accessor, also added
 *                              remoteAccessorMap which is populated in
 *                              initializeRemoteManagers.
 * 16-Jul-02    Steve           VBM:2002071501 - Initialise XHTMLBasic MIB 2.0
 *                              protocol and add it to the protocols list.
 * 22-Jul-02    Ian             VBM:2002052804 - Removed all references to old
 *                              style classes.
 * 24-Jul-02    Steve           VBM:2002072301 - Initialise WMLEmptyOK1_3
 *                              protocol and add it to the protocols list.
 * 29-Jul-02    Ian             VBM:2002072603 - Added support for new protocol
 *                              WapTV5_WMLVersion1_3.
 * 02-Aug-02    Adrian          VBM:2002080203 - Initialise XHTMLBasicNetfront3
 *                              protocol and add it to the list of protocols.
 * 06-Aug-02    Allan           VBM:2002080102 - Added initializeLogging() to
 *                              establish what version of log4j is in use and
 *                              modified configureBackwardlyCompatibleLogging()
 *                              to enable compatibility with pre v1.2.5 and
 *                              v1.2.5 and later versions of log4j. Called
 *                              initializeLogging() from initialize() - old
 *                              logging initialization moved out of initialize
 * 08-Aug-02    Allan           VBM:2002080703 - Added HDML_Version3 protocol.
 * 08-Aug-02    Phil W-S        VBM:2002080202 - Initialize the
 *                              XHTMLBasicMIB2_1 protocol and add it to the
 *                              protocols list in the initialize method.
 * 15-Aug-02    Ian             VBM:2002081303 - Added support for
 *                              ConvertibleImageAsset.
 * 03-Sep-02    Chris W         VBM:2002082907 - Added HTMLVersion4_0_IE6
 *                              protocol.
 * 08-Oct-02    Allan           VBM:2002100208 - Added the HTMLParagon
 *                              protocol in initializeProtocols(). Added
 *                              initializeProtocols() and called this in
 *                              initialize().
 * 08-Oct-02    Sumit           VBM:2002091202 - Added RemoteCacehInfo support
 *                              in cacheRemoteComponent
 * 10-Oct-02    Allan           VBM:2002100801 - Added the WMLOpenWave1_3
 *                              protocol in initializeProtocols().
 * 23-Oct-02    Sumit           VBM:2002090206 - Added extra boolean fields for
 *                              allowproxybehaviour, allowremotepolicyserving,
 *                              allowremotecontentserving and the is...Allowed()
 *                              methods. Byte arrays in initialiseAccessors()
 *                              added for verification of these features.
 * 23-Oct-02    Steve           VBM:2002071604 - Added retrieval method for
 *                              the application properties manager.
 * 28-Oct-02    Phil W-S        VBM:2002100906 - Added management of the
 *                              layout device theme cache (initializeManagers
 *                              and flushLayoutCache).
 * 29-Oct-02    Steve           VBM:2002071604 - Added application properties
 *                              repository manager.
 * 04-Nov-02    Mat             VBM:2002110401 - Added ApplicationProperties
 *                              selection policy.
 * 31-Oct-02    Adrian          VBM:2002103004 - updated method
 *                              getRepositoryPropsFromConfig() to get anonymous
 *                              attribute from mariner-config.xml
 * 07-Nov-02    Adrian          VBM:2002103004 - updated method
 *                              getRepositoryPropsFromConfig() to get anonymous
 *                              attribute from mariner-config.xml if using an
 *                              app-server datasource rather than odbc setup
 * 19-Nov-02    Mat             VBM:2002111306 - Log license details to logfile.
 * 08-Nov-02    Steve           VBM:2002071604 - If the user has an XML repository
 *                              but remote repositories are enabled, then the
 *                              JDBC repository must be configured as well.
 * 08-Nov-02    Phil W-S        VBM:2002102306 - Added management of the
 *                              repository object activator factory for
 *                              runtime post-load handling of device themes.
 *                              Updates the initialize method and adds the
 *                              initializeRepositoryObjectActivators method.
 * 11-Nov-02    Chris W         VBM:2002102403 - Added MMS_SMIL_2_0 protocol
 *                              in initializeProtocols()
 * 13-Nov-02    Paul            VBM:2002091806 - Separated the initialize
 *                              method into two. The original one simply
 *                              synchronizes and calls the other
 *                              (initializeInternal). This is to allow the
 *                              Volantis bean to be initialized by another
 *                              class which is already synchronized. Also
 *                              changed the object being synchronized to
 *                              be the ServletContext.
 * 15-Nov-02    Doug            VBM:2002071507 - Changed all
 *                              com.volantis.mcs.xmlparser.parsers.SAXParser
 *                              references to
 *                              com.volantis.xml.xerces.parsers.SAXParser.
 * 19-Nov-02    Phil W-S        VBM:2002111816 - Add initialization of custom
 *                              asset URL rewriter into the initialize method.
 * 22-Nov-02    Paul            VBM:2002091806 - Removed unused references to
 *                              ServletConfig.
 * 15-Oct-02    Chris W         VBM:2002092306 - Added the MHTML protocol
 *                              in initializeProtocols()
 * 11-Nov-02    Geoff           VBM:2002103005 - Added SMS protocol
 *                              in initializeProtocols()
 * 29-Nov-02    Byron           VBM:2002112803 - Connection was erroneously
 *                              kept open. Modified initialize and cleaned up
 *                              imports, etc.
 * 03-Nov-02    Geoff           VBM:2002120306 - Cleanup before some rewriting
 *                              this class - remove masses of unused methods,
 *                              made lots of instance variables local, and
 *                              cleaned up the usual unused imports, casts,
 *                              locals, etc. This means that the following
 *                              config file entries are now ignored:
 *                              <webapp xml-reader=>, <mariner-log>,
 * 03-Nov-02    Geoff           VBM:2002120306 - Whoops - also remove
 *                              accessor for ApplicationPropertiesManager which
 *                              was only called by an unused method of MPC, and
 *                              add back getInstance() which is still used by
 *                              MPS even though it shouldn't really :-).
 * 03-Dec-02    Steve           VBM:2002090210 - Removed RemoteCacheInfo reference
 * 10-Dec-02    Sumit           VBM:2002120207 - Flushed URL cache in
 *                              flushComponentAssetCache()
 * 16-Dec-02    Phil W-S        VBM:2002121001 - Add retrieval and storage of
 *                              the CSS max-age cache header configuration
 *                              value during the configureStyleSheet method.
 * 20-Dec-02    Sumit           VBM:2002120617 - flushDeviceCache() traverses
 *                              the list of connections to get the correct
 *                              local repository connection before re init
 * 07-Jan-03    Geoff           VBM:2003010704 - Reorganised before rewriting
 *                              the config stuff. I tried not to make any
 *                              changes to the code, but I think I got a little
 *                              carried away and made a couple of small changes
 *                              - pageCacheEnabled from String to boolean, and
 *                              addVolantisProtocol() to setVolantisProtocol().
 * 10-Jan-03    Byron           VBM:2003010910 - Modified initializeInternal to
 *                              pass through map of repository configuration
 *                              items to the RemoteRepository.
 * 17-Jan-03    Mat             VBM:2002120904 - Output the build version
 *                              number on startup.
 * 20-Jan-03    Byron           VBM:2003010603 - Modified initializeInternal()
 *                              to remove stack trace output if license check
 *                              fails. Fixed indentation for this method.
 * 27-Jan-03    Byron           VBM:2003010603 - Modified initializeInternal()
 *                              to catch IOException and Throwable only.
 * 05-Feb-03    Byron           VBM:2003013109 - Added pagePackagingMimeEnabled
 *                              CONFIG_PAGE_PACKAGING/MIME and
 *                              and isPagePackagingMimeEnabled. Modified
 *                              initializeInternal to read new configuration
 *                              value.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported changes from Metis for
 *                              MPS.
 * 12-Feb-03    Ian             VBM:2003020607 - Fixed license problem.
 * 13-Feb-03    Byron           VBM:2003021204 - Modified getDevice to use the
 *                              new request headers class.
 * 19-Feb-03    Sumit           VBM:2003020412 Added missing elements to arrays
 *                              in init..RemoteManagers for remote asset groups
 * 20-Feb-03    Ian             VBM:2002122011 - Removed WapTV1_WMLVersion1_2
 *                              protocol.
 * 25-Feb-03    Geoff           VBM:2003021005 - Modify initialiseInternal so
 *                              that we always report errors and license
 *                              failures appropriately.
 * 27-Feb-03    Geoff           VBM:2003010904 - Delete unused page cache code.
 * 10-Mar-03    Steve           VBM:2003021101 - Return configuration entry for
 *                              proxy server cookies.
 * 11-Mar-03    Geoff           VBM:2002112102 - Made constants for tag names
 *                              package protected so that the new Config
 *                              "emulator" can use them, remove unused
 *                              allowProxyMapCookies instance variable, remove
 *                              error handling for Config construction, which
 *                              is not needed since Config is now emulated,
 *                              add new marinerConfig instance variable.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 28-Apr-03    Allan           VBM:2003042802 - Updated to handle
 *                              VoiceXMLVersion1_0 move to voicexml package.
 * 28-Apr-03    Sumit           VBM:2003041502 - Added support for XML pipeline
 *                              VoiceXMLVersion1_0 move to voicexml package.
 * 28-Apr-03    Allan           VBM:2003042802 - Updated to handle
 *                              VoiceXMLVersion1_0 move to voicexml package.
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * 13-May-03    Chris W         VBM:2003041503 - Added jspSupportRequired,
 *                              jspWriteDirect and jspResolveCharacterReferences
 *                              properties and correspoding get methods to
 *                              support character references in JSP pages.
 * 20-May-03    Chris W         VBM:2003040403 - If we support jsps load the
 *                              named character reference entity sets in
 *                              initializeInternal();
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.cache.filter.CacheEntryFilter;
import com.volantis.cache.group.Group;
import com.volantis.cache.Cache;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessor;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocation;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryLocationFactory;
import com.volantis.devrep.repository.api.accessors.DeviceRepositoryAccessorFactory;
import com.volantis.mcs.application.DeviceReader;
import com.volantis.mcs.application.DeviceReaderImpl;
import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.cache.css.CSSCache;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.devrep.repository.api.devices.DevicesHelper;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.devrep.repository.api.devices.logging.UnknownDevicesLogger;
import com.volantis.devrep.repository.api.devices.unknowndevices.EmailNotifierConfig;
import com.volantis.devrep.repository.api.devices.unknowndevices.EmailNotifierThread;
import com.volantis.devrep.repository.api.devices.unknowndevices.EmailNotifierPeriodEnum;
import com.volantis.mcs.http.HttpHeaders;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.marlin.sax.AbstractContentHandlerFactory;
import com.volantis.mcs.marlin.sax.IAPIContentHandlerFactory;
import com.volantis.mcs.marlin.sax.NamespaceSwitchContentHandlerMap;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyType;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.jdbc.JDBCPolicySource;
import com.volantis.mcs.project.xml.XMLPolicySource;
import com.volantis.mcs.protocols.DefaultStyleSheetHandler;
import com.volantis.mcs.repository.AudioRepositoryManager;
import com.volantis.mcs.repository.ChartRepositoryManager;
import com.volantis.mcs.repository.DeprecatedPolicyCacheFlusher;
import com.volantis.mcs.repository.DynamicVisualRepositoryManager;
import com.volantis.mcs.repository.ImageRepositoryManager;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.LocalRepositoryConnection;
import com.volantis.mcs.repository.PolicyCachePreloader;
import com.volantis.mcs.repository.RepositoryConnection;
import com.volantis.mcs.repository.RepositoryConnectionType;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.TextRepositoryManager;
import com.volantis.mcs.repository.jdbc.JDBCRepositoryFactory;
import com.volantis.mcs.repository.xml.XMLRepositoryConfiguration;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;
import com.volantis.mcs.runtime.cache.CacheManager;
import com.volantis.mcs.runtime.configuration.ApplicationPluginConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigContext;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.DevicesConfiguration;
import com.volantis.mcs.runtime.configuration.EmailNotifierConfiguration;
import com.volantis.mcs.runtime.configuration.FileRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfigurationContainer;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryConfiguration;
import com.volantis.mcs.runtime.configuration.JDBCRepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.MCSConfiguration;
import com.volantis.mcs.runtime.configuration.ManagementConfiguration;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.PageTrackingConfiguration;
import com.volantis.mcs.runtime.configuration.PolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.PolicyCaches;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePoliciesConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyCacheConfiguration;
import com.volantis.mcs.runtime.configuration.RemotePolicyQuotaConfiguration;
import com.volantis.mcs.runtime.configuration.RepositoryDeviceConfiguration;
import com.volantis.mcs.runtime.configuration.ServletFilterConfiguration;
import com.volantis.mcs.runtime.configuration.StyleSheetExternalGenerationConfiguration;
import com.volantis.mcs.runtime.configuration.StyleSheetPageLevelGenerationConfiguration;
import com.volantis.mcs.runtime.configuration.StyleSheetsConfig;
import com.volantis.mcs.runtime.configuration.UnknownDevicesLoggingConfiguration;
import com.volantis.mcs.runtime.configuration.WMLOutputPreference;
import com.volantis.mcs.runtime.configuration.MediaAccessProxyConfiguration;
import com.volantis.mcs.runtime.configuration.project.ProjectsConfiguration;
import com.volantis.mcs.runtime.layouts.LayoutActivator;
import com.volantis.mcs.runtime.pipeline.PipelineInitialization;
import com.volantis.mcs.runtime.plugin.markup.MarkupFactory;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginManager;
import com.volantis.mcs.runtime.policies.ActivatedPolicyRetriever;
import com.volantis.mcs.runtime.policies.MultiplexingPolicyActivator;
import com.volantis.mcs.runtime.policies.PolicyActivator;
import com.volantis.mcs.runtime.policies.PolicyFetcher;
import com.volantis.mcs.runtime.policies.PolicyFetcherImpl;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactoryImpl;
import com.volantis.mcs.runtime.policies.RuntimePolicyFactory;
import com.volantis.mcs.runtime.policies.VariablePolicyActivator;
import com.volantis.mcs.runtime.policies.base.BaseURLPolicyActivator;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraints;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.cache.DeprecatedPolicyCacheFlusherImpl;
import com.volantis.mcs.runtime.policies.cache.PolicyCache;
import com.volantis.mcs.runtime.policies.cache.PolicyCacheBuilder;
import com.volantis.mcs.runtime.policies.cache.PolicyCachePartitionConstraints;
import com.volantis.mcs.runtime.policies.cache.PolicyCachePartitionConstraintsImpl;
import com.volantis.mcs.runtime.policies.cache.SeparateCacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.cache.SingleCacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.composite.ButtonImagePolicyActivator;
import com.volantis.mcs.runtime.policies.composite.RolloverImagePolicyActivator;
import com.volantis.mcs.runtime.project.FileProjectLoader;
import com.volantis.mcs.runtime.project.PopulatableProjectManager;
import com.volantis.mcs.runtime.project.ProjectLoader;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.runtime.project.ProjectManagerImpl;
import com.volantis.mcs.runtime.project.RemoteProjectLoader;
import com.volantis.mcs.runtime.project.RuntimeProjectConfiguratorImpl;
import com.volantis.mcs.runtime.project.SelectingProjectLoader;
import com.volantis.mcs.runtime.project.URLProjectLoader;
import com.volantis.mcs.runtime.repository.remote.RemotePolicyPreloader;
import com.volantis.mcs.runtime.repository.remote.xml.RemotePolicyBuildersReader;
import com.volantis.mcs.runtime.repository.remote.xml.RemoteReadersFactory;
import com.volantis.mcs.runtime.repository.remote.xml.RemoteReadersFactoryImpl;
import com.volantis.mcs.runtime.selection.VariantSelectionPolicy;
import com.volantis.mcs.runtime.selection.VariantSelectionPolicyImpl;
import com.volantis.mcs.runtime.themes.ThemeActivator;
import com.volantis.mcs.utilities.GUIDFactoryManager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.VolantisEnvironment;
import com.volantis.mcs.utilities.VolantisVersion;
import com.volantis.synergetics.cornerstone.utilities.extensions.ExtensionFactoryLoader;
import com.volantis.synergetics.cornerstone.utilities.extensions.Extension;
import com.volantis.mcs.utilities.number.LongHelper;
import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.cache.GenericCache;
import com.volantis.synergetics.cache.GenericCacheConfiguration;
import com.volantis.synergetics.cache.GenericCacheFactory;
import com.volantis.synergetics.log.Log4jHelper;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.performance.MonitoredApplication;
import com.volantis.synergetics.performance.MonitoringFactory;
import com.volantis.synergetics.performance.MonitoringMetaFactory;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.cache.CacheProcessConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.url.URLContentCacheConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The Volantis root JavaBean. This bean persists for the entire life of the
 * application. Typically this is the life of the web server in which it is
 * running. The bean holds references and resources that must persist.
 *
 * @mock.generate
 */
public class Volantis {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(Volantis.class);

    /**
     * The default location of the log4j configuration file for MCS.
     */
    private static final String DEFAULT_LOG4J_LOCATION =
            "/WEB-INF/mcs-log4j.xml";

    /**
     * The location of the predefined log4j configuration file resource for
     * MCS. This is used if a location was not specified in the web.xml, and
     * if the default location does not exist. The predefined location is the
     * last resort fallback.
     */
    private static final String PREDEFINED_LOG4J_LOCATION =
            "com/volantis/mcs/logging/mcs-log4j-predefined.xml";

    /**
     * 0 integer value as an object.
     */
    private static final Integer INTEGER_ZERO = new Integer(0);

    /**
     * The maximum integer value as an object.
     */
    private static final Integer INTEGER_MAX_VALUE =
            new Integer(Integer.MAX_VALUE);

    /**
     * The default size of the remote group if it has not been explicitly
     * configured.
     */
    private static final int DEFAULT_REMOTE_GROUP_SIZE = 1000;

    //
    // The keys for our config information
    //
    private static final String CONFIG_ELEMENT = "mcs-config";
    static final String CONFIG_PAGEMESSAGES_ELEMENT =
            CONFIG_ELEMENT + ".page-messages";
    static final String CONFIG_DEBUG_ELEMENT =
            CONFIG_ELEMENT + ".debug";
    static final String CONFIG_PROTOCOLS_ELEMENT =
            CONFIG_ELEMENT + ".protocols";
    static final String CONFIG_AGENT_ELEMENT =
            CONFIG_ELEMENT + ".mcs-agent";
    static final String CONFIG_SCRIPTS_ELEMENT =
            CONFIG_ELEMENT + ".scripts";
    static final String CONFIG_MODESETS_ELEMENT =
            CONFIG_ELEMENT + ".modesets";
    static final String CONFIG_CHARTIMAGES_ELEMENT =
            CONFIG_ELEMENT + ".chartimages";
    static final String CONFIG_WEBAPP_ELEMENT =
            CONFIG_ELEMENT + ".web-application";
    static final String PLUGINS_ELEMENT =
            CONFIG_ELEMENT + ".plugins";
    private static final String CONFIG_PAGE_PACKAGING =
            CONFIG_ELEMENT + ".page-packaging";
    static final String CONFIG_PAGE_PACKAGING_MIME =
            CONFIG_PAGE_PACKAGING + ".mime-packaging";
    private static final String CONFIG_STYLE_SHEET_ELEMENT =
            CONFIG_ELEMENT + ".style-sheets";
    private static final String CONFIG_EXTERNAL_STYLE_SHEET_ELEMENT =
            CONFIG_STYLE_SHEET_ELEMENT + ".external-generation";
    private static final String CONFIG_PAGE_LEVEL_STYLE_SHEET_ELEMENT =
            CONFIG_STYLE_SHEET_ELEMENT + ".page-level-generation";
    private static final String CONFIG_SESSION_PROXY_ELEMENT =
            CONFIG_ELEMENT + ".session-proxy";
    static final String CONFIG_MAP_COOKIES_ELEMENT =
            CONFIG_SESSION_PROXY_ELEMENT + ".map-cookies";

    /**
     * Factory to create {@link DeviceRepositoryLocation} objects.
     */
    private static final DeviceRepositoryLocationFactory REPOSITORY_LOCATION_FACTORY =
        DeviceRepositoryLocationFactory.getDefaultInstance();

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY =
        InternalDeviceFactory.getDefaultInstance();

    /** The full class name of the professional update service */
    private static final String PROF_UPDATE_SERVICE_CLASS =
        "com.volantis.mcs.professional.runtime.ProfessionalUpdateService";

    private boolean IMDRepositoryEnabled;

    private MonitoringFactory monitoringFactory;

    private MonitoredApplication monitoredApplication;

    /**
     * Old config object which is emulated for backwards compatibility.
     *
     * @deprecated DO NOT USE THIS FOR NEW CODE. See {@link #marinerConfig}.
     */
    private Config config = null;

    /**
     * Root of the mariner configuration object heirarchy.
     * <p>
     * Only this class should have access to this root object. Other classes
     * should only be passed the subtree that they are interested in.
     */
    private MarinerConfiguration marinerConfig;

    /**
     * The object that contains a reference to {@link #marinerConfig} for data
     * values but contains various pieces of logic surrounding the use of that
     * data.
     */
    private MCSConfiguration mcsConfiguration;

    private String vtPageHeadingMsg = "No message";
    private String scriptsBase;
    private String modeSetsBase;
    private String chartimagesBase;

    private String pageBase;

    // Page packaging
    private boolean pagePackagingMimeEnabled = false;

    /** The base url */
    private MarinerURL baseURL;

    /** The url we use to access resources internally. */
    private MarinerURL internalURL;

    //
    // RepositoryManagers.
    //
    // NOTE: RepositoryManagers are fast becoming out of date and
    // should eventually be deprecated in favour of the
    // RepositoryObjectManager classes.
    //

    /**
     * Audio repository manager.
     */
    private AudioRepositoryManager audioRepositoryManager;

    private DeviceRepositoryAccessor deviceRepositoryAccessor;

    /**
     * Text repository manager.
     */
    private TextRepositoryManager textRepositoryManager;

    /**
     * Chart repository manager.
     */
    private ChartRepositoryManager chartRepositoryManager;

    /**
     * DynamicVisual repository manager.
     */
    private DynamicVisualRepositoryManager dynamicVisualRepositoryManager;

    /**
     * Image repository manager.
     */
    private ImageRepositoryManager imageRepositoryManager;

    /**
     * The cache for policies.
     */
    private PolicyCache policyCache;

    //
    // RepositoryObjectManagers
    //
    // These are only needed here for assets that don't have a selection
    // policy.
    //

    /** Flag which is set if persistant cache use is enabled */
    private final boolean allowPersistentCacheUse = true;


    private static AppServerInterfaceManager appServerInterfaceManager;

    /** Used to perform url rewriting */
    private URLRewriterManager urlRewriterManager;

    /**
     * URL rewriter for form, fragment and shard links.
     */
    private PageURLRewriter layoutURLRewriter;

    /**
     * URL rewriter for all links that are neither asset urls nor
     * framment and shard links.
     */
    private RuntimePageURLRewriter pageURLRewriter;

    /** Used to perform asset url rewriting */
    private AssetURLRewriter assetURLRewriter;

    /** Used to perform asset transcoder URL generation */
    private PluggableAssetTranscoder assetTranscoder;

    /**
     * A count of the number of requests which have been processed. It is used
     * to generate unique ids within each page processed for the request.
     */
    private int requestNumber;

    /** Dummy object for locking when incrementing the requestNumber */
    private final Object requestNumberLock = new Object();

    /** The cache of information which is needed when generating pages. */
    private PageGenerationCache pageGenerationCache;

    /** The configuration of style sheet support. */
    private StyleSheetConfiguration styleSheetConfiguration;

    /** The cofiguration for protocols */
    private ProtocolsConfiguration protocolsConfiguration;

    private boolean initializationComplete;

    /**
     * Instance of the PipelineInitialization class
     */
    private PipelineInitialization pipelineInitialization;

    private NamespaceSwitchContentHandlerMap namespaceSwitchContentHandlerMap;

    /**
     * The MarkupPluginManager
     */
    private MarkupPluginManager markupPluginManager;

    /**
     * The container of {@link MarkupPlugin} with application scope.
     */
    private MarkupPluginContainer markupPluginContainer;

    /**
     * The PageTrackerFactory to use.
     */
    private PageTrackerFactory pageTrackerFactory = null;

    /**
     * The servlet filter configuration.
     */
    private ServletFilterConfiguration servletFilterConfiguration;

    /**
     * Used for flushing the rendered page cache the the MCSFilter may use.
     */
    private CacheManager renderedPageCacheManager;


    /**
     * The cache for CSS page level objects
     */
    private com.volantis.mcs.cache.Cache cssCache;

    /**
     * Common Namespace definitions. These initialise the Namespace Map and
     * should not be removed even if they appear to be unused in this class.
     */
    private final static Namespace IM = new Namespace("im") {};

    /**
     * The default StyleSheet
     */
    public CompiledStyleSheet defaultStyleSheet;

    /**
     * The repository within which the device information resides.
     */
    private LocalRepository deviceRepository;

    private VariantSelectionPolicy selectionPolicy;
    private LocalRepository jdbcRepository;
    private LocalRepository xmlRepository;

    private Map dynamicProjectCache;

    private ProjectManager projectManager;

    private PolicyReferenceFactory referenceFactory;

    private PolicyActivator policyActivator;

    private RemotePolicyPreloader remotePolicyPreloader;
    private PolicyFetcher policyFetcher;

    /**
     * Logger for unknown/abstract devices.
     */
    private UnknownDevicesLogger unknownDevicesLogger;

    /**
     * The unique identifier for the current MCS instance.
     */
    private String instanceIdentifier;
    private String mapUrlPrefix;

    /**
     * Constructor
     */
    public Volantis() {

        //logger.debug ("Volantis bean is being constructed");
    }


    // ========================================================================
    //  Initialisation Methods that do not use the Config object
    // ========================================================================

    /**
     * Initialise the selection policies.
     */
    private void initializeSelectionPolicies() {
        selectionPolicy = new VariantSelectionPolicyImpl();
    }

    /**
     * Initialize the repository object activator mechanism. This requires
     * the creation, initialization and installation of a repository object
     * activator factory. This initialization must be performed before
     * repository initialization because accessors are at liberty to
     * obtain and cache the list of activators for the repository object
     * class(es) that they manage (for optimal performance).
     * @param referenceFactory
     */
    private PolicyActivator initializeRepositoryObjectActivators(
            final PolicyReferenceFactory referenceFactory) {

        MultiplexingPolicyActivator policyActivator =
                new MultiplexingPolicyActivator();

        PolicyActivator variablePolicyActivator =
                new VariablePolicyActivator(referenceFactory);
        for (Iterator i = VariablePolicyType.getVariablePolicyTypes().iterator();
             i.hasNext();) {
            PolicyType policyType = (PolicyType) i.next();

            PolicyActivator activator;
            if (policyType == PolicyType.THEME) {
                activator = new ThemeActivator(referenceFactory);
            } else if (policyType == PolicyType.LAYOUT) {
                activator = new LayoutActivator(referenceFactory);
            } else {
                activator = variablePolicyActivator;
            }
            policyActivator.addActivator(policyType, activator);
        }

        policyActivator.addActivator(PolicyType.BUTTON_IMAGE,
                new ButtonImagePolicyActivator(referenceFactory));

        policyActivator.addActivator(PolicyType.ROLLOVER_IMAGE,
                new RolloverImagePolicyActivator(referenceFactory));

        policyActivator.addActivator(PolicyType.BASE_URL,
                new BaseURLPolicyActivator(referenceFactory));

        return policyActivator;
    }

    // ========================================================================
    //  Initialisation Methods that use the Config object
    // ========================================================================

    /**
     * Initialize the Volantis bean.
     * <p>
     * This MUST be called while synchronized on the ConfigContext. It is
     * public so it can be called from the MarinerServletApplication.
     *
     * @param pathURLMapper
     * @param configContext the current ConfigContext
     * @param marinerApplication the MarinerApplication that this
     * Volantis bean belongs to. Cannot be null.
     * @throws IllegalArgumentException if marinerApplication is null.
     * @todo later stop using deprecated Config
     */
    public void initializeInternal(
            ExternalPathToInternalURLMapper pathURLMapper,
            ConfigContext configContext,
            MarinerApplication marinerApplication) {
        try {
            // If the initialization of this object has been completed
            // then return without doing anything.
            if (initializationComplete) {
                return;
            }

            if (marinerApplication == null) {
                throw new IllegalArgumentException("Cannot be null: " +
                        "marinerApplication");
            }

            Log4jHelper.initializeLogging(
                    configContext.getLog4jLocation(),
                    configContext.getConfigurationResolver(
                            "mcs.log4j.config.file"),
                    DEFAULT_LOG4J_LOCATION,
                    PREDEFINED_LOG4J_LOCATION);


            if (logger.isDebugEnabled()) {
                logger.debug("Initializing " + this);
            }

            // Old config object which is emulated for backwards compatibility.
            // DO NOT USE THIS FOR NEW CODE.
            config = new Config(configContext);

            // New config object.
            // USE THIS FOR NEW CODE.
            marinerConfig = config.getMarinerConfiguration();

            // Log the standard header.
            logger.logStandardHeader("log-header");

            if (logger.isDebugEnabled()){
                logger.debug("Initializing " + this);
            }

            // This is definitely the first time through. (I think !?!).

            // This outputs to System.out which may be collected or redirected
            // to a destination dependent on the app or web server that MCS
            // is executing within
            VolantisVersion.displayVersion();

            // check for updates
            UpdateService updateService = (UpdateService)
                ExtensionFactoryLoader.createExtensionFactory(
                    PROF_UPDATE_SERVICE_CLASS, null);
            if (updateService == null) {
                updateService = new DefaultUpdateService();
            }
            updateService.checkForUpdates();    

            String logPageOutput = config.getAttributeValue(
                    CONFIG_DEBUG_ELEMENT,
                    "logPageOutput");
            VolantisEnvironment.setLogPageOutput("true".equals(
                    logPageOutput));

            // Decide if comments are enabled
            String comments = config.getAttributeValue(CONFIG_DEBUG_ELEMENT,
                    "comments");
            VolantisEnvironment.setCommentsEnabled("true".equals(comments));

            if (logger.isDebugEnabled()){
                logger.debug("Volantis root bean being initialized" +
                             " - new version (" + this + ")");
            }

            // This outputs the version information to the log.
            VolantisVersion.logVersion(logger);

            MonitoringMetaFactory monitoringMetaFactory =
                    MonitoringMetaFactory.getInstance();

            monitoringFactory =
                    monitoringMetaFactory.getActiveMonitoringFactory();

            monitoredApplication = monitoringFactory.createApplication("MCS");


            if (logger.isDebugEnabled()) {

                if (!allowPersistentCacheUse) {
                    logger.debug("Persistent cache use disabled.");
                }
            }

            pagePackagingMimeEnabled = config.getBooleanAttributeValue(
                    CONFIG_PAGE_PACKAGING_MIME, "enabled");

            // Configure the application server interface manager.
            appServerInterfaceManager = new AppServerInterfaceManager(
                    config, this);

            // Configure the HTTP Proxy
            if (marinerConfig.getHTTPProxyConfiguration() != null) {
                System.setProperty("http.proxySet", "true");
                System.setProperty("http.proxyHost",
                        marinerConfig.getHTTPProxyConfiguration().getHost());
                System.setProperty("http.proxyPort",
                        marinerConfig.getHTTPProxyConfiguration().getPort());
            }

            // Initialize the various URL rewriters
            initializeURLRewriters(marinerApplication);

            // Configure the asset transcoder URL generator
            String assetTranscoderConfig =
                config.getAttributeValue(PLUGINS_ELEMENT,
                                         "asset-transcoder");

            assetTranscoder =
                new PluggableAssetTranscoderManager(assetTranscoderConfig);


            LocalRepository xmlRepository = null;
            LocalRepository jdbcRepository = null;

            // Resolve all datasources
            if (logger.isDebugEnabled()) {
                logger.debug("Resolving DataSources");
            }

            mcsConfiguration = new MCSConfiguration(marinerConfig);
            mcsConfiguration.resolveDataSources();

            // Determine whether the default project should preload its
            // policies.
            ProjectsConfiguration projects = marinerConfig.getProjects();

            RepositoryConnectionType deviceConnectionType =
                    getDRepositoryConnectionType();

            // Configure an XML repository if one is configured or required
            // by the device repository; if not required this will be ignored.
            if (marinerConfig.getLocalRepository().
                    getXmlRepository() != null ||
                    (deviceConnectionType ==
                     RepositoryConnectionType.XML_REPOSITORY_CONNECTION)) {

                XMLRepositoryFactory factory =
                        XMLRepositoryFactory.getDefaultInstance();

                // todo: later: refactor this config object using factory pattern
                XMLRepositoryConfiguration xmlRepositoryConfiguration =
                        (XMLRepositoryConfiguration)
                        factory.createXMLRepositoryConfiguration();

                xmlRepository =
                        factory.createXMLRepository(xmlRepositoryConfiguration);
            }

            // Create a JDBC repository if one has been configured.

            // todo Should we try and get the data source from the
            // todo AppServerInterfaceManager like we used to.
            DataSource dataSource = getLocalRepositoryJDBCDataSource();

            if (dataSource != null ||
                    (deviceConnectionType ==
                     RepositoryConnectionType.JDBC_REPOSITORY_CONNECTION)) {

                JDBCRepositoryFactory factory =
                        JDBCRepositoryFactory.getDefaultInstance();

                // todo: later: refactor this config object using factory pattern
                com.volantis.mcs.repository.jdbc.JDBCRepositoryConfiguration configuration =
                        factory.createJDBCRepositoryConfiguration();
                configuration.setDataSource(dataSource);
                configuration.setReleaseConnectionsImmediately(true);
                configuration.setShortNames(getJDBCRepositoryUsesShortNames());
                configuration.setAnonymous(true);

                jdbcRepository = (LocalRepository)
                        factory.createJDBCRepository(configuration);
            } else {
                // Until datasource configuration is implemented, log a warning
                // about not being able to create a JDBC repository.
                logger.warn("local-repository-type-unknown");
            }

            // Determine the repository to use to access the device.
            DeviceRepositoryLocation deviceRepositoryLocation =
                    getDeviceRepositoryLocation(configContext);
            if (deviceConnectionType == RepositoryConnectionType.JDBC_REPOSITORY_CONNECTION) {
                deviceRepository = jdbcRepository;
            } else if (deviceConnectionType == RepositoryConnectionType.XML_REPOSITORY_CONNECTION) {
                deviceRepository = xmlRepository;
            } else {
                throw new IllegalStateException("Unknown device repository type " + deviceConnectionType);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Creating remote repository");
            }

            boolean IMDRepositoryEnabled =
                    marinerConfig.getImdRepositoryEnabled() == Boolean.TRUE;

            if (IMDRepositoryEnabled) {
                logger.info("using-imd-repository");
            }

            CacheControlConstraints defaultRemoteCacheControlConstraints =
                    createRemoteCacheControlConstraints();

            // Create the policy cache.
            PolicyCache policyCache = createPolicyCache(
                    defaultRemoteCacheControlConstraints);

            RemoteReadersFactory remoteReadersFactory =
                    createRemoteReadersFactory();

            // Create the ProjectManager.
            ProjectLoader projectLoader = createProjectLoader();

            RepositoryContainer repositoryContainer =
                    new RepositoryContainer(jdbcRepository, xmlRepository);

            // Create the remote project specific constraints.
            CacheControlConstraintsMap defaultRemoteCacheControlConstraintsMap
                    = createRemoteCacheControlConstraintsMap(
                            defaultRemoteCacheControlConstraints);

            // Create the local project specific constraints.
            CacheControlConstraintsMap defaultLocalCacheControlConstraintsMap
                    = createLocalCacheControlConstraintsMap();

            PopulatableProjectManager projectManager =
                    new ProjectManagerImpl(projectLoader,
                            new RuntimeProjectConfiguratorImpl(
                                    defaultLocalCacheControlConstraintsMap,
                                    defaultRemoteCacheControlConstraintsMap,
                                    pathURLMapper, policyCache),
                            repositoryContainer,
                            remoteReadersFactory);

            // Create the activator.
            PolicyReferenceFactory referenceFactory =
                    new PolicyReferenceFactoryImpl(projectManager);

            PolicyActivator policyActivator =
                    initializeRepositoryObjectActivators(referenceFactory);

            // Create the retriever to use to retrieve activated policies from
            // any source.
            RuntimePolicyFactory runtimePolicyFactory =
                    RuntimePolicyFactory.getDefaultInstance();

            ActivatedPolicyRetriever activatedPolicyRetriever =
                    runtimePolicyFactory.createCachingRetriever(
                            policyActivator, projectManager, policyCache);

            policyFetcher = new PolicyFetcherImpl(activatedPolicyRetriever);

            // Initialize the projects

            projectManager.createPredefinedProjects(projects, configContext,
                    defaultLocalCacheControlConstraintsMap,
                    policyCache.getLocalDefaultGroup());
            RuntimeProject defaultProject = projectManager.getDefaultProject();

            // Determine the repository within which the default project
            // resides.
            LocalRepository defaultRepository;

            PolicySource defaultPolicySource = defaultProject.getPolicySource();
            if (defaultPolicySource instanceof JDBCPolicySource) {
                defaultRepository = jdbcRepository;
            } else if (defaultPolicySource instanceof XMLPolicySource) {
                defaultRepository = xmlRepository;
            } else {
                throw new IllegalStateException(
                        "Unknown policy source for the default project: " +
                        defaultPolicySource);
            }

            // Initialize repository managers;
            initializeDeprecatedManagers(defaultRepository, defaultProject,
                    policyCache);

            initializeDeviceManager(deviceRepository, deviceRepositoryLocation);

            // If we were asked to preload the default project policies...
            if (defaultProject.isPreloaded()) {
                // ... then preload the policy caches, syncronously.
                // In future we may wish to do this asyncronously.
                // NOTE: this will only load policies from the default
                // project! The accessor API does not currently allow us
                // to iterate over non-default projects.
                PolicyCachePreloader preloader =
                        new PolicyCachePreloader(defaultRepository,
                                defaultProject,
                                deviceRepositoryAccessor);

                preloader.run();
            }

            RemotePolicyBuildersReader policyBuildersReader =
                    remoteReadersFactory.createPolicyBuildersReader(
                            projectManager);

            this.remotePolicyPreloader = new RemotePolicyPreloader(
                    policyBuildersReader, policyActivator, policyCache);
            this.jdbcRepository = jdbcRepository;
            this.xmlRepository = xmlRepository;
            this.policyCache = policyCache;

            // ================================================================
            //   Markup Plugins
            // ================================================================

            // Create a markup plugin manager.
            MarkupFactory markupFactory = MarkupFactory.getDefaultInstance();
            IntegrationPluginConfigurationContainer markupPluginConfigurations
                    = marinerConfig.getMarkupPlugins();

            markupPluginContainer
                    = markupFactory.createMarkupPluginContainer();

            markupPluginManager
                    = markupFactory.createMarkupPluginManager(
                            markupPluginConfigurations, markupPluginContainer,
                            marinerApplication);

            // ================================================================
            //   Selection Methods
            // ================================================================

            // Initialize the selection policies after the managers as
            // it depends on the initializeLocalCaches method to set the
            // caches to use.
            initializeSelectionPolicies();

            // Style sheet configuration.
            configureStyleSheet(marinerConfig.getStylesheetConfiguration());

            vtPageHeadingMsg = config.getAttributeValue(
                    CONFIG_PAGEMESSAGES_ELEMENT,
                    "heading");
            scriptsBase = config.getAttributeValue(CONFIG_SCRIPTS_ELEMENT,
                    "base");

            if (scriptsBase == null) {
                logger.warn("config-file-scripts-base-not-found");

                scriptsBase = "scripts/";
            }

            modeSetsBase = config.getAttributeValue(CONFIG_MODESETS_ELEMENT,
                    "base");

            if (modeSetsBase == null) {
                logger.warn("config-file-mode-sets-base-not-found");

                modeSetsBase = "modesets/";
            }

            chartimagesBase = config.getAttributeValue(
                    CONFIG_CHARTIMAGES_ELEMENT, "base");

            if (chartimagesBase == null) {
                logger.warn("config-file-chart-images-base-not-found");
                chartimagesBase = "chartimages/";
            }

            // Set up base prefix url
            String value = config.getAttributeValue(CONFIG_WEBAPP_ELEMENT,
                    "base-url");

            if (value == null) {
                logger.warn("base-url-missing");
            } else {
                baseURL = new MarinerURL(value);
            }

            // Set up internal prefix url
            value = config.getAttributeValue(CONFIG_WEBAPP_ELEMENT,
                    "internal-url");

            if (value == null) {
                logger.warn("config-file-internal-url-not-found");
            } else {
                internalURL = new MarinerURL(value);
            }

            // Create the PageGenerationCache.
            pageGenerationCache = new PageGenerationCache();

            // Set up the page base
            pageBase = config.getAttributeValue(CONFIG_WEBAPP_ELEMENT,
                    "page-base");

            if (pageBase == null) {

                // page-base might not exist in config.
                // If not found CanvasTag or MontageTag will set it
                logger.warn("config-file-page-base-not-found");
            }

            // create the pipelineInitialization instance
            pipelineInitialization =
                    new PipelineInitialization(marinerConfig,
                                               mcsConfiguration,
                                               assetTranscoder);

            // initilalise the Management functionality (PageTracker)
            initializePageTracker();

            servletFilterConfiguration =
                    marinerConfig.getServletFilterConfiguration();

            // ================================================================
            //   Create Custom Plugins
            // ================================================================

            // Create the custom plugins here so that they will have access
            // to an initialised {@link MarinerApplication}.

            // Make sure that any application scope plugins have been created
            // and initialised.
            markupPluginManager.createApplicationPlugins();

            this.policyActivator = policyActivator;
            this.projectManager = projectManager;
            this.referenceFactory = referenceFactory;
            this.IMDRepositoryEnabled = IMDRepositoryEnabled;

            // Compile the default style sheet.
            InputStream cssInputStream =
                    Volantis.class.getResourceAsStream("default.css");
            defaultStyleSheet = DefaultStyleSheetHandler.compileStyleSheet(
                    cssInputStream);

            // Initialise this last as it breaks in some tests which causes
            // all sorts of problems.
            initializeNamespaceSwitchContentHandlerMap();

            // start the e-mail sending process for abstract and unknown devices
            final UnknownDevicesLoggingConfiguration unknownDevicesLogging =
                getUnknownDevicesConfiguration();
            if (unknownDevicesLogging != null) {
                // resolve the file name to an absolute path
                final String fileName = unknownDevicesLogging.getFileName();
                if (fileName != null && fileName.length() > 0) {
                    final File file =
                        configContext.getConfigRelativeFile(fileName, false);
                    unknownDevicesLogging.setFileName(file.getAbsolutePath());
                    initializeUnknownDevicesLogger();
                    final EmailNotifierConfiguration emailNotifierConfiguration =
                        unknownDevicesLogging.getEmailNotifier();
                    if (emailNotifierConfiguration != null &&
                            emailNotifierConfiguration.isEnabled()) {
                        final EmailNotifierConfig config =
                            createEmailNotifierConfig(emailNotifierConfiguration);
                        final EmailNotifierThread thread =
                                new EmailNotifierThread(config,
                                        getUnknownDevicesLogger());
                        thread.setDaemon(true);
                        thread.start();
                    }
                }
            }

            // generate a unique identifier
            final long guidFactoryId = LongHelper.hashCode(
                pathURLMapper.mapExternalPathToInternalURL(".").toExternalForm());
            instanceIdentifier = GUIDFactoryManager.getDefaultInstance().
                createGuidFactory(guidFactoryId).generateGuid();

            final MediaAccessProxyConfiguration mapConfiguration =
                marinerConfig.getMediaAccessProxyConfiguration();
            if (mapConfiguration != null) {
                mapUrlPrefix = mapConfiguration.getUrlPrefix();
            } else {
                mapUrlPrefix = null;
            }

            // The last thing we do is mark this object as having been
            // completely initialized. Nothing must come after this.
            initializationComplete = true;

            if (logger.isDebugEnabled()){
                logger.debug("Initialization of " + this + " has completed");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            // We have had a non-license related initialisation failure.
            // logger message and stack trace.
            logger.fatal("volantis-bean-init-failure", this, e);
        }
    }

    /**
     * Returns a logger to be used for unknown/abstract devices.
     *
     * <p>May return null.</p>
     *
     * @return the logger or null.
     */
    public UnknownDevicesLogger getUnknownDevicesLogger() {
        return unknownDevicesLogger;
    }

    /**
     * Initializes the logger for unknown/abstract devices.
     *
     * <p>Sets it to null, if the log file name is empty string.</p>
     */
    private void initializeUnknownDevicesLogger() {
        final UnknownDevicesLoggingConfiguration config =
            getUnknownDevicesConfiguration();
        UnknownDevicesLogger devicesLogger = null;
        if (config != null) {
            final String fileName = config.getFileName();
            if (fileName.length() != 0) {
                final File logFile = new File(fileName);
                devicesLogger = UnknownDevicesLogger.getLogger(logFile);
            }
        }
        unknownDevicesLogger = devicesLogger;
    }

    /**
     * Returns the configuration object to be used for logging unknown/abstract
     * devices.
     *
     * If the configuration file doesn't contain this information, returns the
     * default values.
     *
     * @return the configuration object.
     */
    private UnknownDevicesLoggingConfiguration getUnknownDevicesConfiguration() {
        final DevicesConfiguration devicesConfiguration =
            getDevicesConfiguration();

        UnknownDevicesLoggingConfiguration unknownDevicesConfig =
            devicesConfiguration.getUnknownDevicesLogging();
        return unknownDevicesConfig;
    }

    /**
     * Transforms the e-mail configuration object into its inner form.
     *
     * @param sourceConfiguration the source to convert
     * @return the converted e-mail configuration
     */
    private EmailNotifierConfig createEmailNotifierConfig(
            final EmailNotifierConfiguration sourceConfiguration) {
        final EmailNotifierConfig config = new EmailNotifierConfig();
        boolean enabled = sourceConfiguration.isEnabled();
        config.setEnabled(enabled);
        if (enabled) {
            final String smtpHost = sourceConfiguration.getSmtpHost();
            if (smtpHost != null) {
                config.setSmtpHost(smtpHost);
            }
            final int smtpPort = sourceConfiguration.getSmtpPort();
            if (smtpPort != 0) {
                config.setSmtpPort(smtpPort);
            }
            final String smtpUserName = sourceConfiguration.getSmtpUserName();
            if (smtpUserName != null) {
                config.setSmtpUserName(smtpUserName);
            }
            final String smtpPassword = sourceConfiguration.getSmtpPassword();
            if (smtpPassword != null) {
                config.setSmtpPassword(smtpPassword);
            }
            final String fromAddress = sourceConfiguration.getFromAddress();
            if (fromAddress != null) {
                config.setFromAddress(fromAddress);
            }
            final String fromName = sourceConfiguration.getFromName();
            if (fromName != null) {
                config.setFromName(fromName);
            }
            final String toAddress = sourceConfiguration.getToAddress();
            if (toAddress != null) {
                config.setToAddress(toAddress);
            }
            final String toName = sourceConfiguration.getToName();
            if (toName != null) {
                config.setToName(toName);
            }
            final String subject = sourceConfiguration.getSubject();
            if (subject != null) {
                config.setSubject(subject);
            }
            final String period = sourceConfiguration.getPeriod();
            if (period != null) {
                config.setPeriod(EmailNotifierPeriodEnum.literal(period));
            }
        }
        return config;
    }

    private RemoteReadersFactory createRemoteReadersFactory() {
        RemotePoliciesConfiguration remotePolicies =
                marinerConfig.getRemotePolicies();
        int connectionTimeout = remotePolicies.getRealConnectionTimeout();

        Period timeout = Period.treatZeroAsIndefinitely(connectionTimeout);

        RemoteReadersFactory remoteReadersFactory =
                new RemoteReadersFactoryImpl(timeout,
                        SystemClock.getDefaultInstance());
        return remoteReadersFactory;
    }

    private ProjectLoader createProjectLoader() {
        ProjectLoader urlProjectLoader = new URLProjectLoader();
        ProjectLoader fileProjectLoader = new FileProjectLoader();
        ProjectLoader remoteProjectLoader = new RemoteProjectLoader();
        ProjectLoader projectLoader =
                new SelectingProjectLoader(urlProjectLoader,
                        fileProjectLoader,
                        remoteProjectLoader);
        return projectLoader;
    }

    /**
     * Initialise the NamespaceSwitchContentHandlerMap.
     * <p/>
     * Pipeline initialization must have occurred for namespace resolution to
     * work correctly.
     *
     * @throws ClassNotFoundException if there is a problem loading the handler
     * factory classes
     * @throws IllegalAccessException if there is a problem loading the handler
     * factory classes
     * @throws InstantiationException if there is a problem loading the handler
     * factory classes
     */
    protected void initializeNamespaceSwitchContentHandlerMap()
            throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {

        namespaceSwitchContentHandlerMap =
                NamespaceSwitchContentHandlerMap.getInstance();

        // initialize the "im" namespace content handler.
        namespaceSwitchContentHandlerMap.addContentHandler(IM.getURI(),
                new IAPIContentHandlerFactory());

        // Getting the XDIME content handler factory by reflection is messy,
        // but without refactoring Volantis and/or the runtime subsystem this
        // is the clearest solution.
        // @todo refactor to avoid this
        ClassLoader classLoader = this.getClass().getClassLoader();
        Class implClass = classLoader.loadClass(
                "com.volantis.mcs.xdime.XDIMEContentHandlerFactory");
        AbstractContentHandlerFactory factory =
                (AbstractContentHandlerFactory) implClass.newInstance();

        String [] supportedNamespaces = factory.getHandledNamespaces();
        for (int i = 0; i < supportedNamespaces.length; i++) {
            String namespace = supportedNamespaces[i];

            namespaceSwitchContentHandlerMap.addContentHandler(
                    namespace, factory);
        }
    }

    /**
     * Returns true if we are using a local JDBC repository with short table
     * and colum names enabled.
     */
    public boolean getJDBCRepositoryUsesShortNames() {

        boolean usesShortNames = false;
        if (marinerConfig.getLocalRepository() != null &&
                marinerConfig.getLocalRepository().getJDBCRepositoryConfiguration() != null) {

            JDBCRepositoryConfiguration jdbcRepositoryConfiguration =
                    marinerConfig.getLocalRepository().
                            getJDBCRepositoryConfiguration();
            if (jdbcRepositoryConfiguration.getUseShortNames() != null) {

                usesShortNames = jdbcRepositoryConfiguration.getUseShortNames().
                        booleanValue();
            }
        }
        return usesShortNames;
    }

    /**
     * Initilaise PageTracker infrastructure.
     */
    private void initializePageTracker() {
        ManagementConfiguration managementConfig =
                marinerConfig.getManagementConfiguration();
        if (managementConfig != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Initialising Management System.");
            }
            PageTrackingConfiguration pageTrackerConfig =
                    managementConfig.getPageTrackingConfiguration();
            if (pageTrackerConfig != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Initialising Page Tracking.");
                }
                pageTrackerFactory = appServerInterfaceManager
                        .getPageTrackerFactory();
            }
            if(logger.isDebugEnabled()) {
                logger.debug("Management System intialised.");
            }
        }
    }

    /**
     * Initialize the url rewriters associated with the Volantis bean.
     * @param marinerApplication the current MarinerApplication. Must not
     * be null.
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException if marinerApplication is null.
     */
    private void initializeURLRewriters(MarinerApplication marinerApplication)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException {

        if (marinerApplication==null) {
            throw new IllegalArgumentException("Cannot be null: " +
                    "marinerApplication");
        }

        // Configure the url rewriter manager
        urlRewriterManager = new URLRewriterManager(marinerConfig.
                getUrlRewriterPluginClass());

        pageURLRewriter = new RuntimePageURLRewriter(marinerConfig.
                getPageURLRewriterPluginClass(), marinerApplication);

        // Configure the layout url rewriter.
        layoutURLRewriter = new PageURLRewriter() {
            public MarinerURL rewriteURL(MarinerRequestContext context,
                                         MarinerURL url,
                                         PageURLDetails details) {

                // First rewrite the url with the urlRewriter.
                MarinerURL rewrittenURL =
                        urlRewriterManager.mapToExternalURL(context, url);

                // Then rewrite with the pageURLRewriter
                return pageURLRewriter.rewriteURL(context, rewrittenURL,
                        details);
            }
        };

        // Configure the asset URL rewriter
        String assetURLRewriterPluginClass =
                marinerConfig.getAssetURLRewriterPluginClass();

        if ((assetURLRewriterPluginClass == null) ||
                "".equals(assetURLRewriterPluginClass)) {
            // Optimize invocation if there is no custom rewriter
            // configuration value
            assetURLRewriter =
                    new DefaultAssetURLRewriter();
        } else {
            assetURLRewriter =
                    new AssetURLRewriterManager(assetURLRewriterPluginClass);
        }
    }

    /**
     * Initializes the styleSheetConfiguration member using the given
     * configuration data.
     *
     * @param config the configuration data as retrieved from the config XML.
     */
    private void configureStyleSheet(StyleSheetsConfig config) {

        styleSheetConfiguration = new StyleSheetConfiguration();

        boolean supportsExternal = true;
        StyleSheetExternalGenerationConfiguration externalConfig = null;
        StyleSheetPageLevelGenerationConfiguration pageLevelConfig = null;

        if (config != null) {
            // Get the internal and external config information
            externalConfig = config.getExternalCacheConfiguration();
            pageLevelConfig = config.getPageLevelCacheConfiguration();
        }

        // Handle the external generation caching
        if (externalConfig == null) {

            if (logger.isDebugEnabled()) {
                logger.debug(
                        "No " + CONFIG_EXTERNAL_STYLE_SHEET_ELEMENT +
                        " element in the configuration file");
            }

            // If the element does not exist (or has no attributes which
            // is not valid according to the dtd) then we cannot support
            // generation of external style sheets.
            supportsExternal = false;

            // Set the preferred location to be internal.
            styleSheetConfiguration.setPreferredLocation(
                    StyleSheetConfiguration.INLINE);
        } else {

            // Set the base url which may not have been set.
            String value = externalConfig.getBaseUrl();
            if (value != null) {
                MarinerURL baseURL = new MarinerURL(value);
                baseURL.makeReadOnly();
                styleSheetConfiguration.setBaseURL(baseURL);
            }

            // Set the preferred location to be external.
            styleSheetConfiguration.setPreferredLocation(
                    StyleSheetConfiguration.EXTERNAL);

            // Set the css session type.
            styleSheetConfiguration.setCssSessionType(
                    externalConfig.getCssSessionType());
        }

        styleSheetConfiguration.setSupportsExternal(supportsExternal);

        // Handle the page level generation caching
        int pageLevelMaxAge = 60000;
        if (pageLevelConfig == null) {

            if (logger.isDebugEnabled()) {
                logger.debug(
                        "No " + CONFIG_PAGE_LEVEL_STYLE_SHEET_ELEMENT +
                        " element in the configuration file");
            }

        } else {
            String maxAgeString = pageLevelConfig.getMaxAge();
            if (maxAgeString != null) {
                if ("unlimited".equals(maxAgeString)) {
                    pageLevelMaxAge = -1;
                } else {
                    try {
                        Integer maxAgeInteger = new Integer(maxAgeString);
                        pageLevelMaxAge = maxAgeInteger.intValue();

                        if (pageLevelMaxAge < 0) {
                            logger.warn("config-file-max-age-sign-error");
                        }
                    } catch (NumberFormatException e) {
                        logger.warn("config-file-max-age-conversion-error", e);
                    }
                }
            }
        }

        styleSheetConfiguration.setPageLevelMaxAge(pageLevelMaxAge);

        if (logger.isDebugEnabled()) {
            logger.debug(styleSheetConfiguration);
        }

    }

    /**
     * Get the type of the Device Repository Connection.
     *
     * @return The {@link RepositoryConnectionType of the device repository}
     *
     */
    private RepositoryConnectionType getDRepositoryConnectionType() {

        DevicesConfiguration devices = marinerConfig.getDevices();

        RepositoryDeviceConfiguration srdc =
                devices.getStandardDeviceRepository();

        RepositoryConnectionType deviceConnectionType = null;

        if (srdc != null) {
            if (srdc instanceof JDBCRepositoryDeviceConfiguration) {
                deviceConnectionType =
                        RepositoryConnectionType.JDBC_REPOSITORY_CONNECTION;

            } else if (srdc instanceof FileRepositoryDeviceConfiguration) {
                deviceConnectionType =
                        RepositoryConnectionType.XML_REPOSITORY_CONNECTION;
            }
        }
        return deviceConnectionType;
    }

    /**
     * Extract the device repository related information from the config
     * objects and stick them into the properties provided.
     *
     * @param configContext Used to resolve relative paths
     * @throws ConfigurationException if there was a problem configuring the
     *                                repository
     */
    DeviceRepositoryLocation getDeviceRepositoryLocation(
            ConfigContext configContext)
            throws ConfigurationException {
        DevicesConfiguration devices = marinerConfig.getDevices();
        RepositoryDeviceConfiguration srdc =
                devices.getStandardDeviceRepository();
        if (srdc != null) {
            if (srdc instanceof JDBCRepositoryDeviceConfiguration) {
                // JDBC devices - this object should probably be passed into
                // the JDBC repository / JDBC device accessor directly.

                JDBCRepositoryDeviceConfiguration jsrdc =
                        (JDBCRepositoryDeviceConfiguration) srdc;
                String deviceProject = jsrdc.getProject();
                if (deviceProject != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Using standard jdbc device " +
                                "repository project: " + deviceProject);
                    }

                    return REPOSITORY_LOCATION_FACTORY.createDeviceRepositoryLocation(deviceProject);
                } else {
                    throw new IllegalStateException("The standard jdbc " +
                            "device repository project is missing");
                }
            } else if (srdc instanceof FileRepositoryDeviceConfiguration) {
                // File devices - this object should probably be passed into
                // the XML repository / XML device accessor directly.

                FileRepositoryDeviceConfiguration fsrdc =
                        (FileRepositoryDeviceConfiguration) srdc;
                String deviceLocation = fsrdc.getLocation();
                if (deviceLocation != null) {
                    File deviceFile = configContext.getConfigRelativeFile(
                        deviceLocation, true);
                    if (deviceFile != null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Using standard file device " +
                                    "repository location: " + deviceLocation);
                        }
                        return REPOSITORY_LOCATION_FACTORY.createDeviceRepositoryLocation(
                                deviceFile.getAbsolutePath());
                    } else {
                        throw new IllegalStateException("The standard file " +
                                "device repository file is missing");
                    }
                } else {
                    throw new IllegalStateException("The standard file " +
                            "device repository location is missing");
                }
            } else {
                // This should never happen.
                throw new IllegalStateException();
            }
        }

        return null;
    }

    /**
     * Get the {@link MarkupPluginManager}
     * @return the {@link MarkupPluginManager}
     */
    public MarkupPluginManager getMarkupPluginManager() {
        return markupPluginManager;
    }

    /**
     * Get the container of application scope {@link MarkupPlugin}s.
     *
     * @return The container of application scope {@link MarkupPlugin}s.
     */
    public MarkupPluginContainer getMarkupPluginContainer() {
        return markupPluginContainer;
    }

    /**
     * Get the default style sheet
     * @return Compiled style sheet
     */
    public CompiledStyleSheet getDefaultStyleSheet() {
        return defaultStyleSheet;
    }

    /**
     * Create the default local cache control constraints map.
     *
     * <p>If a cache has been configured for the policy type then allow caching
     * and use the timeout from that, otherwise disallow caching. If a timeout
     * was not specified then policies never expire locally.</p>
     *
     * @return A map of constraints one for each policy type.
     */
    private CacheControlConstraintsMap createLocalCacheControlConstraintsMap() {

        SeparateCacheControlConstraintsMap map = new SeparateCacheControlConstraintsMap();

        // Now create the set of defaults for the different policy types.
        Collection policyTypes = PolicyType.getPolicyTypes();
        for (Iterator i = policyTypes.iterator(); i.hasNext();) {
            PolicyType policyType = (PolicyType) i.next();
            PolicyCacheConfiguration configuration =
                    getPolicyCacheConfiguration(policyType);
            Boolean allowCacheThisPolicy;
            Integer timeout;
            if (configuration == null) {
                // Policies are not to be cached so do not create a group
                // for them.
                allowCacheThisPolicy = Boolean.FALSE;
                timeout = INTEGER_MAX_VALUE;
            } else {

                allowCacheThisPolicy = Boolean.TRUE;
                timeout = configuration.getTimeout();
                if (timeout == null || timeout.intValue() == -1) {
                    timeout = INTEGER_MAX_VALUE;
                }
            }

            CacheControlConstraints constraints = createLocalCacheConstraints(
                    allowCacheThisPolicy, timeout);

            map.addConstraints(policyType, constraints);
        }

        return map;
    }

    private CacheControlConstraints createLocalCacheConstraints(
            Boolean allowCacheThisPolicy, Integer timeout) {
        RemotePolicyCacheConfiguration cacheConfiguration =
                new RemotePolicyCacheConfiguration();

        cacheConfiguration.setAllowCacheThisPolicy(allowCacheThisPolicy);

        // Allow retries as previously we did retry when there was an
        // error simply because we did not cache the fact that there was an
        // error. Retain during retry, retry immediately, and allow any
        // number of retries.
        cacheConfiguration.setAllowRetryFailedRetrieval(Boolean.TRUE);
        cacheConfiguration.setMinRetryInterval(INTEGER_ZERO);
        cacheConfiguration.setMaxRetryMaxCount(INTEGER_MAX_VALUE);
        cacheConfiguration.setAllowRetainDuringRetry(Boolean.TRUE);

        cacheConfiguration.setDefaultRetryFailedRetrieval(Boolean.TRUE);
        cacheConfiguration.setDefaultRetryInterval(INTEGER_ZERO);
        cacheConfiguration.setDefaultRetryMaxCount(INTEGER_MAX_VALUE);
        cacheConfiguration.setDefaultRetainDuringRetry(Boolean.TRUE);

        cacheConfiguration.setDefaultTimeToLive(timeout);
        CacheControlConstraints constraints = new CacheControlConstraints(
                cacheConfiguration);
        return constraints;
    }

    /**
     * Create the default remote cache control constraints map.
     *
     * <p>The constraints are created from the configuration and are the same
     * for all policy types.</p>
     *
     * @param constraints The constraints to wrap..
     * @return A map of constraints that are the same for all policy types.
     */
    private CacheControlConstraintsMap createRemoteCacheControlConstraintsMap(
            final CacheControlConstraints constraints) {

        return new SingleCacheControlConstraintsMap(constraints);
    }

    /**
     * Create the default remote cache control constraints.
     *
     * <p>The constraints are created from the configuration and are the same
     * for all policy types.</p>
     *
     * @return The constraints.
     */
    private CacheControlConstraints createRemoteCacheControlConstraints() {
        RemotePoliciesConfiguration remotePolicies =
                marinerConfig.getRemotePolicies();
        RemotePolicyCacheConfiguration policyCacheConfiguration =
                remotePolicies.getPolicyCache();

        if (policyCacheConfiguration == null) {
            policyCacheConfiguration = new RemotePolicyCacheConfiguration();
        }

        // Treat a maxTimeToLive of 0 as unlimited.
        Integer maxTimeToLive = policyCacheConfiguration.getMaxTimeToLive();
        if (maxTimeToLive != null && maxTimeToLive.intValue() == 0) {
            policyCacheConfiguration.setMaxTimeToLive(INTEGER_MAX_VALUE);
        }

        CacheControlConstraints constraints = new CacheControlConstraints(
                policyCacheConfiguration);
        return constraints;
    }

    /**
     * Create the cache for policies.
     *
     * <p>The returned cache is used for all policies, both local and
     * remote.</p>
     *
     * @return A cache for policies.
     * @param remoteConstraints
     */
    private PolicyCache createPolicyCache(
            CacheControlConstraints remoteConstraints) {

        // Calculate a total size for the cache equal to the sum of the sizes
        // for each policy.
        int localSize = 0;
        Collection policyTypes = PolicyType.getPolicyTypes();
        for (Iterator i = policyTypes.iterator();
             i.hasNext() && localSize < Integer.MAX_VALUE;) {

            PolicyType policyType = (PolicyType) i.next();
            PolicyCacheConfiguration groupConfiguration =
                    getPolicyCacheConfiguration(policyType);
            if (groupConfiguration != null) {
                Integer integer = groupConfiguration.getMaxEntries();
                if (integer != null) {
                    if (integer.intValue() != -1) {
                        localSize += integer.intValue();
                    } else {
                        localSize = Integer.MAX_VALUE;
                    }
                } else {
                    localSize = Integer.MAX_VALUE;
                }
            }
        }

        // If the local size is still 0 then set it to one so that the cache
        // can be built.
        if (localSize == 0) {
            localSize = 1;
        }

        RemotePoliciesConfiguration remotePolicies =
                marinerConfig.getRemotePolicies();
        RemotePolicyCacheConfiguration policyCacheConfiguration =
                remotePolicies.getPolicyCache();
        if (policyCacheConfiguration == null) {
            policyCacheConfiguration = new RemotePolicyCacheConfiguration();
        }

        int remoteSize;
        remoteSize = getInteger(policyCacheConfiguration.getMaxCacheSize(),
                DEFAULT_REMOTE_GROUP_SIZE);

        PolicyCacheBuilder builder = new PolicyCacheBuilder(
                localSize, remoteSize);

        CacheControlConstraints localConstraints = createLocalCacheConstraints(
                Boolean.TRUE, null);

        PolicyCachePartitionConstraints localPartitionConstraints =
                createPartitionConstraints(localSize, localConstraints);

        builder.setLocalPartitionConstraints(localPartitionConstraints);

        PolicyCachePartitionConstraints remotePartitionConstraints =
                createPartitionConstraints(remoteSize, remoteConstraints);

        builder.setRemotePartitionConstraints(remotePartitionConstraints);

        // Now build the policy specific groups to the local group.
        for (Iterator i = policyTypes.iterator(); i.hasNext();) {

            PolicyType policyType = (PolicyType) i.next();
            PolicyCacheConfiguration groupConfiguration =
                    getPolicyCacheConfiguration(policyType);
            int maxCount;
            if (groupConfiguration == null) {
                // Allow some entries in the group to ensure that when a policy
                // is marked as uncacheable that it is preserved.
                maxCount = 10;
            } else {
                maxCount = getInteger(groupConfiguration.getMaxEntries(),
                        Integer.MAX_VALUE);
                if (maxCount == -1) {
                    maxCount = Integer.MAX_VALUE;
                }
            }

            builder.addDefaultLocalPolicySpecificGroup(
                    policyType, maxCount);
        }

        Iterator i = remotePolicies.getQuotaIterator();
        while (i.hasNext()) {
            RemotePolicyQuotaConfiguration quota =
                    (RemotePolicyQuotaConfiguration) i.next();
            int percentage = quota.getPercentage().intValue();
            String url = quota.getUrl();
            int share = (remoteSize * percentage) / 100;

            builder.addDefaultRemotePathSpecificGroup(url, share);
        }

        return builder.getPolicyCache();
    }

    private PolicyCachePartitionConstraints createPartitionConstraints(
            int totalSize, CacheControlConstraints cacheControlConstraints) {

        // The maximum partition size of a partition is 80% of the total
        // size of the partition, or a minimum of 1.
        int maxPartitionSize = Math.max(totalSize * 8 / 10, 1);

        // The default partition size of a partition is 50% of the total
        // size of the partition, or a minimum of 1.
        int defaultPartitionSize = Math.max(totalSize / 2, 1);

        PolicyCachePartitionConstraints partitionConstraints
                = new PolicyCachePartitionConstraintsImpl(maxPartitionSize,
                        defaultPartitionSize, cacheControlConstraints);
        return partitionConstraints;
    }

    /**
     * Get the int value of the supplied Integer, or the default value if the
     * Integer is null.
     *
     * @param integer      The Integer whose value is requested, may be null.
     * @param defaultValue The default value to use if the Integer is null.
     * @return The int value of the supplied Integer, or the default value.
     */
    private int getInteger(Integer integer, final int defaultValue) {
        return integer == null ? defaultValue : integer.intValue();
    }

    PolicyCacheConfiguration getPolicyCacheConfiguration(PolicyType policyType) {
        PolicyCaches policies = marinerConfig.getPolicies();
        PolicyCacheConfiguration cacheConfiguration =
                policies.getPolicyCache(policyType);
        return cacheConfiguration;
    }

    private GenericCache createDeviceCache(
            PolicyCacheConfiguration cacheConfiguration) {

        GenericCache cache = null;
        if (cacheConfiguration != null) {
            GenericCacheConfiguration genericConfiguration =
                    new GenericCacheConfiguration();

            String strategy = cacheConfiguration.getStrategy();
            if (strategy != null) {
                genericConfiguration.setStrategy(strategy);
            }

            Integer maxEntries = cacheConfiguration.getMaxEntries();
            if (maxEntries != null) {
                genericConfiguration.setMaxEntries(maxEntries.intValue());
            }

            Integer timeout = cacheConfiguration.getTimeout();
            if (timeout != null) {
                genericConfiguration.setTimeout(timeout.intValue());
            }

            cache = GenericCacheFactory.createCache(null,
                    genericConfiguration);
        }

        return cache;
    }

    /**
     * Initialise the managers for the repository.
     *
     * @param repository The repository
     * @param project
     * @param policyCache
     */
    private void initializeDeprecatedManagers(
            LocalRepository repository, Project project,
            PolicyCache policyCache) {

        // Create a flusher for the managers to use to flush the caches.
        DeprecatedPolicyCacheFlusher policyCacheFlusher =
                new DeprecatedPolicyCacheFlusherImpl(policyCache);

        // --------------------------------------------------------------------
        // Audio Repository Manager
        // --------------------------------------------------------------------
        audioRepositoryManager = new AudioRepositoryManager(
                repository, project, policyCacheFlusher);

        // --------------------------------------------------------------------
        // DynamicVisual Repository Manager
        // --------------------------------------------------------------------
        dynamicVisualRepositoryManager = new DynamicVisualRepositoryManager(
                repository, project, policyCacheFlusher);

        // --------------------------------------------------------------------
        // Image Repository Manager
        // --------------------------------------------------------------------
        imageRepositoryManager = new ImageRepositoryManager(
                repository, project, policyCacheFlusher);

        // --------------------------------------------------------------------
        // Text Repository Manager
        // --------------------------------------------------------------------
        textRepositoryManager = new TextRepositoryManager(
                repository, project, policyCacheFlusher);

        // --------------------------------------------------------------------
        // Chart Repository Manager
        // --------------------------------------------------------------------
        chartRepositoryManager = new ChartRepositoryManager(
                repository, project, policyCacheFlusher);
    }

    private void initializeDeviceManager(LocalRepository repository,
                                         DeviceRepositoryLocation location) {

        GenericCache cache;
        // --------------------------------------------------------------------
        // Device Repository Manager
        // --------------------------------------------------------------------

        final DeviceRepositoryAccessorFactory accessorFactory =
            DeviceRepositoryAccessorFactory.getDefaultInstance();
        deviceRepositoryAccessor =
            accessorFactory.createDeviceRepositoryAccessor(
                repository, location, null);
        cache = createDeviceCache(marinerConfig.getPolicies().getDeviceCache());
        deviceRepositoryAccessor.setDeviceCache(cache);

        // we always cache device patterns
        loadDevicePatterns();
    }

    // ========================================================================
    //  Unclassified Accessors
    // ========================================================================

    /**
     * Get the scripts base.
     *
     * @return scriptsBase property.
     */
    public String getScriptsBase() {

        return scriptsBase;
    }

    /**
     * Get the mode sets base.
     *
     * @return modeSetsBase property.
     */
    public String getModeSetsBase() {

        return modeSetsBase;
    }

    /**
     * Get the Chart Images base.
     *
     * @return chartimagesBase property.
     */
    public String getChartImagesBase() {

        return chartimagesBase;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MarinerURL getBaseURL() {

        return baseURL;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public MarinerURL getInternalURL() {

        return internalURL;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPageBase() {

        return (this.pageBase);
    }

    /**
     * Get the PageGenerationCache.
     *
     * @return The PageGenerationCache.
     */
    public PageGenerationCache getPageGenerationCache() {

        return pageGenerationCache;
    }

    /**
     * Get the StyleSheetConfiguration.
     *
     * @return The StyleSheetConfiguration.
     */
    public StyleSheetConfiguration getStyleSheetConfiguration() {

        return styleSheetConfiguration;
    }

    /**
     * Return a URLRewriter
     *
     * @return the URLRewriter. This should never be null.
     */
    public URLRewriter getURLRewriter() {
        return urlRewriterManager;
    }

    /**
     * Return the general RuntimePageURLRewriter.
     *
     * @return the pageURLRewriter. This should never be null.
     */
    public RuntimePageURLRewriter getPageURLRewriter() {
        return pageURLRewriter;
    }

    /**
     * Return the PageURLRewriter for use with layout links (i.e. form,
     * fragment and shard links).
     *
     * @return the layoutURLRewriter. This should never be null.
     */
    public PageURLRewriter getLayoutURLRewriter() {
        return layoutURLRewriter;
    }

    /**
     * Return the AssetURLRewriter
     *
     * @return the AssetURLRewriter
     */
    public AssetURLRewriter getAssetURLRewriter() {

        return assetURLRewriter;
    }

    /**
     * Return the PluggableAssetTranscoder
     *
     * @return the PluggableAssetTranscoder
     */
    public PluggableAssetTranscoder getAssetTranscoder() {
        return assetTranscoder;
    }

    /**
     * Return the page message supplied in the XML file
     *
     * @return DOCUMENT ME!
     */
    public String getPageHeadingMsg() {

        return (vtPageHeadingMsg);
    }


    // ========================================================================
    //  Accessors for Licensing flags
    // ========================================================================

    // ========================================================================
    // Proxy server flags
    // ========================================================================

    /** Returns whether or not the proxy server is allowed to handle cookies
     * on behalf of the client.
     * @return boolean flag read from the configuration file.
     */
    public boolean isCookieMappingEnabled() {
        return config.getBooleanAttributeValue(
                                    CONFIG_MAP_COOKIES_ELEMENT, "enabled" );
    }

    // ========================================================================
    //  Accessors for Repository Managers
    //
    //  These methods are really only to allow MarinerPageContext to provide
    //  these to the public API - they are not to be used internally.
    // ========================================================================

    /**
     * Get the <code>AudioRepositoryManager</code>
     *
     * @return <code>AudioRepositoryManager</code>
     */
    public AudioRepositoryManager getAudioRepositoryManager() {

        return audioRepositoryManager;
    }

    /**
     * Get the <code>ImageRepositoryManager</code>
     *
     * @return <code>ImageRepositoryManager</code>
     */
    public ImageRepositoryManager getImageRepositoryManager() {

        return imageRepositoryManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DynamicVisualRepositoryManager getDynamicVisualRepositoryManager() {

        return dynamicVisualRepositoryManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TextRepositoryManager getTextRepositoryManager() {

        return textRepositoryManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChartRepositoryManager getChartRepositoryManager() {

        return chartRepositoryManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DeviceRepositoryAccessor getDeviceRepositoryAccessor() {
        return deviceRepositoryAccessor;
    }


    // ========================================================================
    //  Accessors for Selection Policies
    //
    //  Use these in preference to using RepositoryObjectManagers direct!
    // ========================================================================

    public VariantSelectionPolicy getVariantSelectionPolicy() {
        return selectionPolicy;
    }

    // ========================================================================
    //  Methods to deal with caching
    // ========================================================================
    /**
     * Return true if the page packaging mime value is enabled, false
     * otherwise.
     *
     * @return      true if the page packaging mime value is enabled, false
     *              otherwise.
     */
    public boolean isPagePackagingMimeEnabled() {
        return pagePackagingMimeEnabled;
    }

    /**
     * A method to flush all the cached content in Mariner.
     */
    public void flushAllCaches() {
        policyCache.flushAll();

        flushDeviceCache();

        flushAllPipelineCaches();
    }

    /**
     * Flush the specified policy types from the local cache.
     *
     * @param policyType The type of policies to flush, may be null in which
     *                   case all policies of any type are flushed.
     */
    public void flushLocalPolicyCache(PolicyType policyType) {
        flushLocalPolicyCaches(policyType);
    }

    /**
     * Flush the policies of the specified type from the local cache.
     *
     * @param policyType The type of policies to flush, may be null in which
     *                   case all policies of any type are flushed.
     */
    private void flushLocalPolicyCaches(PolicyType policyType) {
        Group group = policyCache.getLocalGroup();
        CacheEntryFilter filter = policyCache.getFilter(policyType);
        group.flush(filter);
    }

    /**
     * Flush all local policies.
     */
    public void flushLocalPolicyCaches() {
        flushLocalPolicyCaches(null);
    }

    /**
     * Flushes the page cache that the MCSFilter may be using
     */
    public void flushRenderedPageCache() {
        if (renderedPageCacheManager != null) {
            renderedPageCacheManager.flushCache();
        }
    }

    /**
     * A method to flush all the device cached content in Mariner.
     *
     */
    public void flushDeviceCache() {
        deviceRepositoryAccessor.refreshDeviceCache();
        loadDevicePatterns();
    }

    /**
     * A method to flush all the pipeline caches.
     */
    public void flushAllPipelineCaches() {
        // flush named caches
        CacheProcessConfiguration cache = 
            retrievePipelineCacheProcessConfiguration();
        cache.flushAllCaches();


        // flush URLContentCache
        URLContentCacheConfiguration contentCacheConfig = retrieveURLContentCacheConfiguration();
        Cache contentCache = contentCacheConfig.getCache();

        if (contentCache != null) {
            contentCache.getRootGroup().flush(null);
        }
    }

    /**
     * Return the Cache stored in the XMLPipelineConfiguration, via the
     * URLContentCacheConfiguration object.
     *
     * @return the Cache.
     */
    private URLContentCacheConfiguration retrieveURLContentCacheConfiguration() {
        final XMLPipelineConfiguration config =
                pipelineInitialization.getPipelineConfiguration();
        final URLContentCacheConfiguration cpc = (URLContentCacheConfiguration)
                config.retrieveConfiguration(URLContentCacheConfiguration.class);
        return cpc;
    }

    /**
     * A method to flush a specific pipeline cache.
     *
     * @param name the name of the cache to flush
     * @return false if pipeline cache with the specified name was not found
     */
    public boolean flushPipelineCache(final String name) {
        CacheProcessConfiguration config =
            retrievePipelineCacheProcessConfiguration();

        boolean result = false;
        try {
            config.getCache(name).getRootGroup().flush(null);
            result = true;
        } catch (RuntimeException e) {
            logger.error(e);

        }
        return result;
    }

    /**
     * Return the Cache stored in the XMLPipelineConfiguration, via the
     * CacheProcessConfiguration object.
     *
     * @return the Cache.
     */
    private CacheProcessConfiguration retrievePipelineCacheProcessConfiguration() {
        final XMLPipelineConfiguration config =
                pipelineInitialization.getPipelineConfiguration();
        final CacheProcessConfiguration cpc = (CacheProcessConfiguration)
                config.retrieveConfiguration(CacheProcessConfiguration.class);
        return cpc;
    }

    /**
     * A method clear the device patterns and re initialise them
     *
     */
    private void loadDevicePatterns() {

        try {
            RepositoryConnection connection = null;
            try {
                connection = getDeviceConnection();
                deviceRepositoryAccessor.initializeDevicePatternCache(connection);
            } finally {
                freeConnection(connection);
            }
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);

            // We should throw an exception since we are broken but this
            // method does not declare that it does throw an exception, so
            // to avoid changing the public api, lets throw a RuntimeException.
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Sets the {@link CacheManager} that will be used to manage the MCSFilters page cache.
     * @param renderedPageCacheManager
     */
    public void setRenderedPageCacheManager(CacheManager renderedPageCacheManager) {
        this.renderedPageCacheManager = renderedPageCacheManager;
    }

    /**
     * A method to flush all the component and asset cached content in Mariner.
     */
    public void flushComponentAssetCache() {
        flushLocalPolicyCache(PolicyType.AUDIO);
        flushLocalPolicyCache(PolicyType.CHART);
        flushLocalPolicyCache(PolicyType.IMAGE);
        flushLocalPolicyCache(PolicyType.LINK);
        flushLocalPolicyCache(PolicyType.SCRIPT);
        flushLocalPolicyCache(PolicyType.TEXT);
        flushLocalPolicyCache(PolicyType.VIDEO);
    }


    // ========================================================================
    //  Methods to deal with remote caching
    // ========================================================================

    public RemotePolicyPreloader getRemotePolicyPreloader() {
        return remotePolicyPreloader;
    }

    /**
     * Flushes the remote cache.
     *
     * @param path      with which to flush the cache
     */
    public void flushRemoteCache(String path) {

        Group group = policyCache.getRemotePartitionGroup(path);
        if (group == null) {
            // No group exists so maybe the path relates to a specific policy
            // so try and flush it.
            policyCache.flushRemotePolicy(path);
        } else {
            group.flush(null);
        }
    }

    // ========================================================================
    //  Methods to deal with database connections
    // ========================================================================

    public LocalRepository getDeviceRepository() {
        return deviceRepository;
    }

    public LocalRepositoryConnection getDeviceConnection()
            throws RepositoryException {

        return (LocalRepositoryConnection) deviceRepository.connect();
    }

    /**
     * Free a database connection
     *
     * @param connection DOCUMENT ME!
     * @throws RepositoryException DOCUMENT ME!
     */
    public void freeConnection(RepositoryConnection connection)
                        throws RepositoryException {
        connection.disconnect();
    }


    // ========================================================================
    //  Methods to deal with MarinerPageContexts
    // ========================================================================

    /**
     * @return a new page context
     */
    public MarinerPageContext createMarinerPageContext() {
        // todo Should pass information that MarinerPageContext needs in here, rather than have it come back and ask for it later.
        MarinerPageContext marinerPageContext = new MarinerPageContext();
        return marinerPageContext;
    }

    // ========================================================================
    //  Methods to deal with unique request numbering
    // ========================================================================

    /**
     * Get the count of the number of requests which have been processed.
     *
     * <p>
     * Calling this method increases the count of the number of requests which
     * have been processed.
     * </p>
     *
     * @return The count of the number of requests which have been processed,
     *         including the one which is currently being processed.
     */
    public String generateUniqueRequestString() {

        int id;

        synchronized (requestNumberLock) {
            id = ++requestNumber;
        }

        return Integer.toHexString(id).toUpperCase();
    }


    // ========================================================================
    //  Methods that really ought to be somewhere else!
    // ========================================================================

    /**
     * Get the requested device from the DeviceRepositoryManager.
     *
     * @param connection RepositoryConnection to get the device with
     * @param deviceName String name of the device to get
     * @return requested InternalDevice
     * @throws com.volantis.mcs.repository.RepositoryException if the named device cannot be found
     */
    public InternalDevice getDevice(
            LocalRepositoryConnection connection,
            String deviceName)
            throws RepositoryException {

        return INTERNAL_DEVICE_FACTORY.createInternalDevice(
            deviceRepositoryAccessor.getDeviceFallbackChain(connection,
                deviceName));
    }

    /**
     * Get the device name using the headers in the servlet request.
     *
     * @param connection Connection to the respository
     * @param requestHeaders The servlet request
     * @return The device object
     * @throws RepositoryException A problem in the repository
     */
    public InternalDevice getDevice(LocalRepositoryConnection connection,
                                    RequestHeaders requestHeaders)
            throws RepositoryException {

        HttpHeaders headers = null;

        // todo this is nasty and may be avoided if the ServletRequestHeaders is moved to an appropriate package, or something else
        if (requestHeaders instanceof ServletRequestHeaders) {
            headers = ((ServletRequestHeaders)requestHeaders).getHttpHeaders();
        }

        final UnknownDevicesLogger logger = getUnknownDevicesLogger();

        final String defaultDeviceName = getDevicesConfiguration().getDefaultDeviceName();        

        // null value of defaultDeviceName is OK, it means that
        // we let DeviceRepository subsystem use its own default
        return INTERNAL_DEVICE_FACTORY.createInternalDevice(
            DevicesHelper.getDevice(connection, headers,
                deviceRepositoryAccessor, logger, defaultDeviceName).getDevice());
    }

    /**
     * Retrieve the config class for the bean
     *
     * @return DOCUMENT ME!
     * @todo only used by the app server stuff so we can remove l8r
     */
    public Config getConfig() {

        return config;
    }

    /**
     *  Return application configuration object for the
     *  external application
     *  @return The Application Configuration
     */
    public ApplicationPluginConfiguration
            getApplicationPluginConfiguration(String name) {
        return marinerConfig.getApplicationPlugin(name);
    }

    /**
     * Given a  url return a client accessible url
     *
     * @param url The url.
     * @return An absolute url.
     * @todo only used in one place, this ought to be inlined
     */
    public MarinerURL getClientAccessibleURL(MarinerURL url) {

        if (baseURL != null) {

            return new MarinerURL(baseURL, url);
        }

        return url;
    }

    /**
     * Provides the JDBC DataSource for the local repository if one exists.
     * @return The JDBC DataSource;
     */
    public DataSource getLocalRepositoryJDBCDataSource() {
        DataSource source = null;
        if (mcsConfiguration != null) {
            source = mcsConfiguration.getLocalRepositoryJDBCDataSource();
        }
        return source;
    }


    /**
     * Return the current protocol configuration settings.
     * @return ProtocolsConfiguration object hold protocol specific settings
     * from the mariner configuration file.
     */
    public ProtocolsConfiguration getProtocolsConfiguration() {
        if (protocolsConfiguration == null) {
            protocolsConfiguration = marinerConfig.getProtocols();
            if (protocolsConfiguration == null) {
                protocolsConfiguration = new ProtocolsConfiguration();
                protocolsConfiguration.setPreferredOutputFormat(
                        WMLOutputPreference.WMLC);
            }
        }
        return protocolsConfiguration;
    }

    public DevicesConfiguration getDevicesConfiguration() {
        return marinerConfig.getDevices();
    }

    /**
     * Set a protocols configuration. This is not called by MCS code
     * but is useful for test cases when a known configuration is required to
     * be returned.
     */
    public void setProtocolsConfiguration(ProtocolsConfiguration config) {
        protocolsConfiguration = config;
    }

    /**
     * Return the instance of the Volantis bean.
     * <p>
     * NOTE: this method is not used for MCS, was only added back for MPS,
     * and should not be used for anything else.
     *
     * @return The current instance of the Volantis bean, or null if it has not
     *         yet been configured.
     * @todo this method ought to be removed once we clarify how the new
     *      MarinerApplication stuff really needs to work.
     */
    public static Volantis getInstance() {

        if (appServerInterfaceManager != null) {
            return appServerInterfaceManager.getVolantisBean();
        }

        return null;
    }

    /**
     * Return an instance of the PipelineInitialization class.
     * @return a PipelineInitialization instance.
     */
    public PipelineInitialization getPipelineInitialization() {
        return pipelineInitialization;
    }

    /**
     * Retrieves the default project for the current runtime.
     *
     * @return The project in use
     */
    public RuntimeProject getDefaultProject() {
        return projectManager.getDefaultProject();
    }

    /**
     * Retrieves the PredefinedProject information for a given named project.
     * @param name The name of the project to locate.
     * @return Project information for the given project or null if it does
     * not exist.
     */
    public RuntimeProject getPredefinedProject(String name) {
        // get(key) returns null if (a) key is not in the map and
        // (b) if key points to a null object.  In either case we
        // want to return null hence no checking for this.
        return projectManager.getPredefinedProject(name);
    }

    /**
     * Returns the PageTrackerFactory to use.
     * @return The PageTrackerFactory to use or null if PageTracking is
     * not enabled.
     */
    public PageTrackerFactory getPageTrackerFactory() {
       return this.pageTrackerFactory;
    }

    /**
     * Get the servlet filter configuration.
     * @return the filter configuration
     */
    public ServletFilterConfiguration getServletFilterConfiguration() {
        return servletFilterConfiguration;
    }

    /**
     * Retrieve and initialise if necassary the page level CSS cache.
     * @return The CSS cache
     */
    public com.volantis.mcs.cache.Cache getCSSCache() {
        if (cssCache == null) {
            // Set the default value of 2 minutes if nothing is set via config.
            long maxAge = 1200000;
            if (getStyleSheetConfiguration() != null) {
                    maxAge = getStyleSheetConfiguration()
                    .getPageLevelMaxAge();
            }
            cssCache = new CSSCache(maxAge);
        }
        return cssCache;
    }



    /**
     * Shut this volantis instance down.
     * <p>
     * This was added to allow tests to shut down volantis cleanly, but it
     * may also be useful if the runtime supports shutting down cleanly in
     * future.
     *
     * @throws RepositoryException if there was a problem shutting down.
     */
    public void shutdown() throws RepositoryException {

        // Shutdown the repositories we are using.
        if (xmlRepository != null) {
            xmlRepository.terminate();
        }
        // Cause the JDBC repository to close down it's contained connection
        // pool.
        if (jdbcRepository != null) {
            jdbcRepository.terminate();
        }

        // Avoid keeping a static reference to ourselves now that we are shut
        // down. This means that the next Volantis instance that is obtained
        // will be a new one.
        if (appServerInterfaceManager != null) {
            appServerInterfaceManager.setVolantisBean(null);
        }
    }

    /**
     * Get the default Monitoring Factory for MCS.
     *
     * @return The default {@link MonitoringFactory}.
     */
    public MonitoringFactory getDefaultMonitoringFactory() {
        return monitoringFactory;
    }

    /**
     * Get the default Monitored Application for MCS.
     *
     * @return The default {@link MonitoredApplication}
     */
    public MonitoredApplication getDefaultMonitoredApplication() {
        return monitoredApplication;
    }

    public LocalRepository getJDBCRepository() {
        return jdbcRepository;
    }

    /**
     * Get the project manager.
     *
     * @return The project manager.
     */
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    /**
     * Get the policy reference factory.
     *
     * @return The reference factory.
     */
    public PolicyReferenceFactory getPolicyReferenceFactory() {
        return referenceFactory;
    }

    public synchronized RuntimeProject getDynamicProject(DynamicProjectKey key) {
        if (dynamicProjectCache == null) {
            dynamicProjectCache = new HashMap();
        }

        // Potential memory leak as we never remove projects but this is
        // something that we should be removing soon anyway.
        RuntimeProject project = (RuntimeProject) dynamicProjectCache.get(key);
        if (project == null) {
            project = key.getProject();
            dynamicProjectCache.put(key, project);
        }

        return project;
    }

    public DeviceReader getDeviceReader() {
        // todo this should probably return the same instance each time.
        return new DeviceReaderImpl(this);
    }

    /**
     * Get the policy activator to use.
     *
     * @return The policy activator.
     */
    public PolicyActivator getPolicyActivator() {
        return policyActivator;
    }

    public boolean isIMDRepositoryEnabled() {
        return IMDRepositoryEnabled;
    }

    public PolicyFetcher getPolicyFetcher() {
        return policyFetcher;
    }

    /**
     * Returns the unique identifier for the current MCS instance.
     * @return the MCS instance identifier
     */
    public String getMCSInstanceIdentifier() {
        return instanceIdentifier;
    }

    /**
     * Returns the global value for the URL prefix to be used to access
     * resources through the Media Access Proxy.
     *
     * <p>A unique identifier is needed to be appended to this URL prefix to
     * access the actual resource.</p>
     *
     * @return the URL prefix or null
     */
    public String getMapUrlPrefix() {
        return mapUrlPrefix;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	9789/10	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 03-Nov-05	9789/7	emma	VBM:2005101113 Supermerge: Migrate JDBC Accessors to use chunked accessors

 23-Oct-05	9789/4	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 18-Oct-05	9789/2	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 10-Oct-05	9727/1	ianw	VBM:2005100506 Fixed up remote repositories layout issues

 13-Sep-05	9415/7	emma	VBM:2005072710 Making unbranded policy name function a static final

 09-Sep-05	9415/4	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 12-Sep-05	9372/2	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 08-Sep-05	9447/6	ibush	VBM:2005090604 Create default style sheet and load in style engine

 06-Sep-05	9447/2	ibush	VBM:2005090604 Create default style sheet and load in style engine

 06-Sep-05	9407/4	pduffin	VBM:2005083007 Fixed conflict

 02-Sep-05	9408/3	pabbott	VBM:2005083007 Move over to using JiBX accessor

 02-Sep-05	9407/1	pduffin	VBM:2005083007 Committing resolved conflicts

 02-Sep-05	9391/4	emma	VBM:2005082604 Supermerge required

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 31-Aug-05	9391/2	emma	VBM:2005082604 Integrate the new XDIMEContentHandler and refactor NamespaceSwitchContentHandler (& Map) as required

 22-Aug-05	9298/3	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 10-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 09-Aug-05	9153/3	ianw	VBM:2005072216 Fixed rework issues

 18-Jul-05	9029/2	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 13-Jul-05	9033/1	allan	VBM:2005071312 Move IOUtils.java that is in cornerstone into Synergetics

 12-Jul-05	8862/5	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 01-Jul-05	8616/6	ianw	VBM:2005060103 New page level CSS servlet

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 02-Jun-05	8005/5	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 31-May-05	8036/2	geoff	VBM:2005050505 XDIMECP: Migration Framework

 25-May-05	7890/11	pduffin	VBM:2005042705 Committing supermerge changes

 25-May-05	7890/8	pduffin	VBM:2005042705 Committing prior to supermerge

 24-May-05	7890/5	pduffin	VBM:2005042705 Committing extensive restructuring changes

 05-May-05	7890/2	pduffin	VBM:2005042705 Committing results of supermerge

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 28-Apr-05	7922/2	pduffin	VBM:2005042801 Removed User and UserFactory classes

 28-Apr-05	7914/2	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 27-Apr-05	7896/1	pduffin	VBM:2005042709 Removing PolicyPreference and all related classes

 25-May-05	8517/2	pduffin	VBM:2005052404 Commiting changes from supermerge

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 27-May-05	8556/1	rgreenall	VBM:2005050503 Merge from 323

 20-May-05	7762/3	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 19-May-05	8158/1	emma	VBM:2005041508 Merge from 330: Moving MarinerAgent management from Volantis to a servlet

 04-May-05	7759/9	pcameron	VBM:2005040505 Fixes to logging

 26-Apr-05	7759/7	pcameron	VBM:2005040505 Logging initialisation changed

 26-Apr-05	7759/4	pcameron	VBM:2005040505 Logging initialisation changed

 26-Apr-05	7852/1	allan	VBM:2005042603 Put CLDC and MIDP versions into package jad

 05-Apr-05	7459/9	tom	VBM:2005032101 Added SmartClientSkin protocol

 05-Apr-05	7459/6	tom	VBM:2005032101 Added SmartClientSkin protocol

 04-Apr-05	7459/4	tom	VBM:2005032101 Added SmartClientSkin protocol

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 05-Apr-05	7513/2	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 01-Apr-05	6798/9	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 17-Mar-05	7401/3	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 17-Mar-05	7401/1	emma	VBM:2005020303 Allow log files to be specified as relative to the log4j config file

 13-Mar-05	6842/5	emma	VBM:2005020302 Make all file/resource references in config files relative to that file

 11-Mar-05	6842/2	emma	VBM:2005020302 Making file references in config files relative to those files

 21-Feb-05	6986/8	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/13	emma	VBM:2005021411 Modifications after review

 18-Feb-05	6974/11	emma	VBM:2005021411 Changing exception message to be more specific

 18-Feb-05	6974/9	emma	VBM:2005021411 Making the device repository and xml policies locations relative to mcs-config.xml

 17-Feb-05	6974/7	emma	VBM:2005021411 Reworking tests to use testtools TempFileManager mechanism

 15-Feb-05	6974/5	emma	VBM:2005021411 Modifications after review

 15-Feb-05	6974/3	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 11-Mar-05	7308/2	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 08-Feb-05	6919/2	pduffin	VBM:2004122401 Fixed super merge issues

 01-Feb-05	6712/5	pduffin	VBM:2005011713 Fix minor problem with Im/MutableMetaDataObject

 25-Jan-05	6712/3	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 02-Feb-05	6828/1	matthew	VBM:2005012601 Allow new Cache mechanism to work with MCS (not optimally though)

 31-Jan-05	6818/1	tom	VBM:2005011004 Moved -remote repository use disabled- message to display after assignment

 11-Jan-05	6635/4	philws	VBM:2004121005 Fix build information messages to allow localization

 11-Jan-05	6413/1	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 07-Jan-05	6609/1	matthew	VBM:2005010404 Remove MarinerPageContext pooling from Volantis bean

 23-Dec-04	6518/4	tom	VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

 20-Dec-04	6529/1	matthew	VBM:2004121702 Allow MarinerAgent to flush the device pattern caches

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/5	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 08-Nov-04	6027/6	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 29-Oct-04	6027/3	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/4	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Nov-04	6109/2	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names

 15-Oct-04	5794/1	geoff	VBM:2004100801 MCS Import slow

 06-Oct-04	5710/2	geoff	VBM:2004052005 Short column name support

 23-Sep-04	5599/2	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 22-Sep-04	5604/2	pcameron	VBM:2004092101 Added asset group flushing to the Volantis bean

 22-Sep-04	5588/4	pcameron	VBM:2004092101 Added asset group flushing to the Volantis bean

 21-Sep-04	5567/1	allan	VBM:2004092010 Handle multi-valued device policy selection.

 09-Sep-04	5466/1	claire	VBM:2004090905 New Build Mechanism: Refactor business logic out of MarinerConfiguration

 06-Sep-04	5361/8	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices which adds cellspacing=0 to all tables

 01-Sep-04	5331/1	pcameron	VBM:2004082517 New subprotocol for SE P800/P900 devices to add cellspacing=0 to all tables

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 02-Aug-04	5050/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4940/1	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 27-Jul-04	4937/5	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - rework issues

 27-Jul-04	4937/3	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers - rework issues

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 12-Jul-04	4707/3	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 01-Jul-04	4702/10	matthew	VBM:2004061402 supermerge errors

 01-Jul-04	4702/7	matthew	VBM:2004061402 merge problems

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 29-Jun-04	4733/9	allan	VBM:2004062105 Merge issues.

 29-Jun-04	4733/6	allan	VBM:2004062105 Rework issues.

 28-Jun-04	4733/4	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 29-Jun-04	4726/8	claire	VBM:2004060803 Moved all stylesheet initialisation to use MarinerConfig not Config

 28-Jun-04	4726/6	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 24-Jun-04	4737/4	allan	VBM:2004062202 Fixed merge conflicts in log.

 24-Jun-04	4737/1	allan	VBM:2004062202 Restrict volantis initialization.

 09-Jun-04	4619/4	ianw	VBM:2004060111 Fudge config file builder to fix MPS problems

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 25-May-04	4507/1	geoff	VBM:2004051809 pre populate policy caches

 20-May-04	4480/4	geoff	VBM:2004051809 pre populate policy caches (use modified caches)

 19-May-04	4480/2	geoff	VBM:2004051809 pre populate policy caches

 14-May-04	4346/1	mat	VBM:2004051111 Delete external themes on theme cache flush

 04-May-04	4023/2	ianw	VBM:2004032302 Added support for short length tables

 04-May-04	4111/4	ianw	VBM:2004042908 Fixed always returning null in getLocalRepository

 30-Apr-04	4111/1	ianw	VBM:2004042908 Added new Public API to get a local JDBC Repository

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 21-Apr-04	3973/1	steve	VBM:2004042002 Encode #nbsp;

 16-Apr-04	3362/7	steve	VBM:2003082208 supermerged

 23-Mar-04	3362/4	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 23-Mar-04	3362/2	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 07-Apr-04	3771/1	steve	VBM:2004040613 Null pointer check

 26-Mar-04	3596/1	geoff	VBM:2004032507 De-brand the build statements in the log

 25-Mar-04	3386/4	steve	VBM:2004030901 Supermerged and merged back with Proteus

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 12-Mar-04	3370/3	steve	VBM:2004030901 Null exception if protocols element is missing in config

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 16-Mar-04	2867/13	ianw	VBM:2004012603 Fixed some rework issues with datasource rationalisation

 09-Mar-04	2867/9	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 15-Mar-04	2736/7	steve	VBM:2003121104 Supermerged

 02-Mar-04	2736/4	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 11-Mar-04	3376/4	adrian	VBM:2004030908 Rework to fix javadoc duplication

 11-Mar-04	3376/1	adrian	VBM:2004030908 Implemented a fix to release DB connections immediately after use at runtime

 11-Mar-04	3391/1	geoff	VBM:2004031002 webapp datasource-vendor, anonymous etc ignored when using server datasource

 04-Mar-04	3204/11	doug	VBM:2004022410 fixed merge issue

 03-Mar-04	3200/4	allan	VBM:2004022410 Destination field and validation fixes.

 02-Mar-04	3200/1	allan	VBM:2004022410 Added file copying utils.

 03-Mar-04	3277/2	claire	VBM:2004021606 Added devices to configuration and cli options

 02-Mar-04	3215/3	steve	VBM:2004021911 Rework Issues

 27-Feb-04	3215/1	steve	VBM:2004021911 Patch from Proteus2 and fixes for RemoteProject

 24-Feb-04	3136/4	philws	VBM:2004021908 Introduce new runtime device repository usage

 19-Feb-04	3124/4	geoff	VBM:2004021901 Use local repository type to decide whether to initialise JDBC repository

 19-Feb-04	3124/1	geoff	VBM:2004021901 Use local-repository-type to decide whether to create JDBC repository

 19-Feb-04	2789/11	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/9	tony	VBM:2004012601 update localisation services

 16-Feb-04	2789/7	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/4	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	3025/3	mat	VBM:2004021304 Changes to make import work

 13-Feb-04	3025/1	mat	VBM:2004021304 Changes to make import work

 12-Feb-04	2982/3	mat	VBM:2004021203 Changed create use both repositories

 12-Feb-04	2982/1	mat	VBM:2004021203 Changed create use both repositories

 11-Feb-04	2761/12	mat	VBM:2004011910 Add Project repository

 10-Feb-04	2931/1	claire	VBM:2004021008 Added named projects from the config

 09-Feb-04	2846/4	claire	VBM:2004011915 Refactoring URL handling

 05-Feb-04	2846/1	claire	VBM:2004011915 Asset URL computation based on base and prefix

 05-Feb-04	2694/11	mat	VBM:2004011917 Rework for finding repositories

 26-Jan-04	2694/4	mat	VBM:2004011917 Improve the way repository connections are located
 03-Feb-04	2767/3	claire	VBM:2004012701 Adding project handling code

 03-Feb-04	2626/4	mat	VBM:2004011507 After merge

 23-Jan-04	2736/1	steve	VBM:2003121104 Configurable WMLC and dollar encoding

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 15-Jan-04	2626/1	mat	VBM:2004011507 Separate out the refreshDevicePatternCache from refreshDeviceCache

 15-Jan-04	2608/1	mat	VBM:2004011507 Proteus2

 02-Feb-04	2802/4	ianw	VBM:2004011921 Added mechanism to enable AppServer interfaces to configure NamespaceSwitchContentHandler

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 21-Jan-04	2659/2	allan	VBM:2003112801 RuleSection basics (read only)

 13-Jan-04	2573/1	andy	VBM:2003121907 renamed file variables to directory

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 29-Oct-03	1704/1	geoff	VBM:2003102906 HTMLVersion4_0_IE protocol is throwing an Inetrnal error under Tomcat

 15-Oct-03	1565/1	pcameron	VBM:2003100703 Debranded the STDOUT startup messages in the Volantis bean

 15-Oct-03	1517/17	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1517/12	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 29-Sep-03	1454/4	philws	VBM:2003092401 Fix supermerge issues

 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 12-Sep-03	1295/11	geoff	VBM:2003082109 Build all jars and run the junit testsuite with IBM JDK 1.4.1 (fix up comments again)

 12-Sep-03	1295/9	geoff	VBM:2003082109 Build all jars and run the junit testsuite with IBM JDK 1.4.1 (build script changes)

 02-Sep-03	1295/2	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 02-Sep-03	1290/8	steve	VBM:2003082105 One return point from getSecondaryDevice

 02-Sep-03	1290/5	steve	VBM:2003082105 Secondary ID Header implementation
 02-Sep-03	1305/2	adrian	VBM:2003082108 added new openwave6 xhtml protocol

 21-Aug-03	1240/1	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03	1219/19	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 20-Aug-03	1190/1	adrian	VBM:2003081903 updated redistribution versions of mariner-config files

 08-Aug-03	970/7	geoff	VBM:2003071607 fix supermerge again - argh

 08-Aug-03	970/4	geoff	VBM:2003071607 fix supermerge

 06-Aug-03	970/1	geoff	VBM:2003071607 merged from metis

 06-Aug-03	967/1	geoff	VBM:2003071607 merge from metis, mostly manual

 06-Aug-03	951/1	geoff	VBM:2003071607 fix up the agent and manager

 08-Aug-03	906/8	chrisw	VBM:2003072905 Put the backslashs back into Volantis.java

 07-Aug-03	906/6	chrisw	VBM:2003072905 Public API changed for transform configuration

 07-Aug-03	906/3	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	906/1	chrisw	VBM:2003072905 implemented compilable attribute on transform

 05-Aug-03	921/2	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 10-Jul-03	761/1	adrian	VBM:2003070801 Added integration test to Volantis testcase to test markup plugin configuration

 30-Jun-03	625/1	byron	VBM:2003022823 Support web service integration within a JSP page

 30-Jun-03	492/6	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector - part 2

 25-Jun-03	492/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 30-Jun-03	569/5	philws	VBM:2003062604 Fix merge problems

 30-Jun-03	569/2	philws	VBM:2003062604 Add XHTML Mobile Profile protocol

 30-Jun-03	552/6	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 30-Jun-03	552/3	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 25-Jun-03	544/2	geoff	VBM:2003061007 Allow JSPs to create binary output

 24-Jun-03	516/1	mat	VBM:2003061604 Display patchlevel with the build version

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 16-Jun-03	366/4	doug	VBM:2003041502 Integration with pipeline JSPs

 13-Jun-03	316/8	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/4	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/

