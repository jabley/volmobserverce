/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/context/MarinerPageContext.java,v 1.157 2003/04/25 10:26:08 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history and
 *                              renamed CanvasHead to PageHead.
 * 28-Jun-01    Paul            VBM:2001062805 - Added
 *                              getBooleanDevicePolicyValue method.
 * 29-Jun-01    Paul            VBM:2001062906 - Add support for literal
 *                              text assets, corrected the name of the
 *                              parameters to retrieveAsset methods and fixed
 *                              a problem with the initialisation of the
 *                              volantis bean.
 * 09-Jul-01    Paul            VBM:2001062810 - Added helper method
 *                              getAssetFromImageComponent.
 * 23-Jul-01    Paul            VBM:2001070507 - Added helper methods
 *                              getAssetsFromImageComponents and
 *                              retrieveRolloverImageComponent, also fixed
 *                              a problem where dissecting was not being
 *                              reset in release.
 * 30-Jul-01    Paul            VBM:2001071609 - Stopped trimming input to
 *                              writeToPage methods as it is unnecessary and
 *                              a waste of time and space. Added an extra
 *                              writeToPage method which takes an extra
 *                              flag which controls whether it is going to
 *                              trim or not.
 * 05-Aug-01    Kula            VBM:2001080605 - getCurrentBandWidthAsBAUD()
 *                              method added
 * 15-Aug-01    Kula            VBM:2001072701 - usage of DeviceImage removed
 * 15-Aug-01    Paul            VBM:2001081507 - Fixed regression in
 *                              retrieveBestTextAsset, it was calling
 *                              retrieveTextAsset in the TextRepositoryManager
 *                              rather than retrieveBestTextAsset.
 * 16-Aug-01    Paul            VBM:2001081601 - Created a MarinerFacilities
 *                              object when initialising and released it when
 *                              cleaning up.
 * 20-Aug-01    Allan           VBM:2001081614 - Removed 2001072701 changes
 *                              since removal of create.jsp has made these
 *                              obselete.
 * 03-Sep-01    Allan           VBM:2001083103 - Modified all retrieve...URL...
 *                              type methods that were not checking if
 *                              the Asset they were given as a param was null
 *                              to ensure that they do this check. Also,
 *                              replaced the throw of IllegalArgumentException
 *                              in retrieveAbsoluteURLAsString() with
 *                              logging a Warning and returning null.
 * 13-Sep-01    Allan           VBM:2001091301 - Modified  writeToPage(String)
 *                              and writeToPage(StringBuffer) to use
 *                              VolantisEnvironment.debugEnabled() to
 *                              determine whether or not to output a newline.
 * 14-Sep-01    Kula            VBM:2001091005 - the getAssetFormImageComponent
 *                              method removed. This method wes called by
 *                              XHTMLFull class. The doActionInput method in
 *                              XHTMLFull mofified to get the asset.
 * 14-Sep-01    Paul            VBM:2001083114 - Added call to initialise the
 *                              protocol after the context had been
 *                              initialised.
 * 19-Sep-01    Doug            VBM:2001091701 Added method
 *                              getAbsolutePageBaseURL()
 * 21-Sep-01    Doug            VBM:2001090302 Added support for LinkAssets,
 *                              removed the baseURL property that is set from
 *                              the CanvasTag or MontageTag.
 * 01-Oct-01    Doug            VBM:2001092501 Added method
 *                              retrieveBackgroundImageURLAsString to retrieve
 *                              a suitable background image URL  from a mariner
 *                              Component. If the component type is 'Dynamic
 *                              Visual' then we retrieve the most suitable
 *                              Dynamic Visual asset. If no Asset is available
 *                              for the device then the image fallback
 *                              component is used to try to locate an image
 *                              asset. If the component type was 'Image' then
 *                              the URL is obtained from an Image component.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package, Layout
 *                              has been renamed DeviceLayout, Theme has
 *                              been renamed DeviceTheme and the FormatInstance
 *                              management code has been simplified, the array
 *                              of FormatContexts is populated during the
 *                              initialisation of this object, rather than as
 *                              needed.
 * 31-Oct-01    Paul            VBM:2001102608 - Stored reference to this
 *                              object in a ThreadLocal object rather than in
 *                              the PageContext as that is the only way to
 *                              make sure that tags in included pages can get
 *                              access to it. Also added a stack of tags which
 *                              allows tags in included pages to get access to
 *                              their parent tag which is needed in order to
 *                              support inclusion of pages which consist only
 *                              of inline tags, or form fields, or menu
 *                              options.
 * 02-Nov-01    Paul            VBM:2001102403 - Moved all the fields specific
 *                              to the layout being generated into the
 *                              DeviceLayoutContext, added a stack of
 *                              RegionContexts, and added a canvas type.
 * 07-Nov-01    Mat             VBM:2001110701 - The getVolantisBean() method
 *                              in AppServerInterfaceManager no longer
 *                              needs the pageContext.
 * 08-Nov-01    Mat             VBM:2001110802 - Get the connection from the
 *                              enclosing PageContext if there is one.
 * 14-Nov-01    Paul            VBM:2001111402 - Added a stack of panes,
 *                              a stack of output buffers and the writeRawText
 *                              and writeEncodedText methods for PAPI.
 * 19-Nov-01    Paul            VBM:2001110202 - Moved some of the JSP and
 *                              servlet specific code out of here and into
 *                              the JspPAPIPageContext. Split the
 *                              initialisation into page and canvas specific
 *                              parts to allow some of the information such
 *                              as device policies to be accessed before the
 *                              canvas is created. Also,
 * 22-Nov-01    Paul            VBM:2001110202 - Made rollover menus work
 *                              inside regions by making sure that they
 *                              did not have the same names and used the URL
 *                              class to resolve a relative path into an
 *                              absolute path.
 * 26-Nov-01    Doug            VBM:2001112004 - Modified the initialisePage
 *                              method to initialise a PAPIURL object that
 *                              stores away the request URL after it has been
 *                              mapped to a mariner url via the URLRewriter
 *                              interface. Replaced the method
 *                              getMutableRequestURL with getPAPIRequestURL
 * 27-Nov-01    Pether          VBM:2001112101 - Moved the methods
 *                              handleMarinerExpression() and
 *                              getComponentNameObject() to here from
 *                              VolantisTag and changed the type to public
 *                              static since they will be used to consistent
 *                              handle expressions.
 * 27-Nov-01    Paul            VBM:2001112601 - Restructured the properties
 *                              to make it obvious which properties were
 *                              related to the page, canvas and session. This
 *                              makes it much easier to see whether all of the
 *                              resources referenced by those properties
 *                              have been released. As part of this the
 *                              release method was split into three new
 *                              methods releasePage, releaseCanvas and
 *                              releaseSession and also the initialisedPage
 *                              and initialisedCanvas flags were added to
 *                              prevent the object from being initialised or
 *                              cleaned up multiple times.
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi and integration classes. Also made this
 *                              class independent of any JSP specific classes
 *                              by having the request and response and writer
 *                              passed in directly.
 * 29-Nov-01    Paul            VBM:2001112906 - Renamed the method
 *                              getMarinerRequestContext to getRequestContext
 *                              for consistency and also added an
 *                              initialiseResponse method which will do
 *                              environment specific initialisation of the
 *                              response.
 * 29-Nov-01    Mat             VBM:2001112913 - Added getAncestorRelationship
 * 30-Nov-01    Paul            VBM:2001112909 - Added stack of PAPIElements.
 * 03-Dec-01    Doug            VBM:2001112901 - Modified the metdhods
 *                              retrieveAbsoluteURLAsString and
 *                              getLinkFromLinkAsset so that any
 *                              specified external repository plugin is called.
 *                              Added getAssetGroup helper method. Simplified
 *                              getPrefixURL(). Added the new methods
 *                              getURLFromExternalPlugin() and
 *                              getValueFromExternalPlugin(). Modified the
 *                              method getContentsFromVariant to use if necessary
 *                              the method getValueFromExternalPlugin to
 *                              retrive the value.
 * 04-Dec-01    Mat             VBM:2001112913 - Added some javadoc for
 *                              getAncestorRelationship
 * 14-Dec-01    Paul            VBM:2001121405 - Enforced inheritance of theme.
 * 19-Dec-01    Paul            VBM:2001120506 - Added sendRedirect, getRegion
 *                              and getSegment methods.
 * 21-Dec-01    Paul            VBM:2001121702 - Removed references to jsp
 *                              specific classes and distinguished between an
 *                              enclosing page context and an including page
 *                              context.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Passed MarinerRequestContext
 *                              into the UserFactory.createUser method instead
 *                              of the HttpServletRequest.
 * 09-Jan-02    Paul            VBM:2002010403 - Updated to reflect changes in
 *                              other classes. Also removed last dependency on
 *                              servlet environment by using the RequestHeaders
 *                              class.
 * 10-Jan-02    Adrian          VBM:2001122803 - Added
 *                              retrieveDynamicVisualComponent method for use
 *                              by DynamicVisualElement classes.
 * 18-Jan-02    Adrian          VBM:2001121003 - Added methods:
 *                              getEnvironmentContext, retrieveAudioComponent,
 *                              getChartImagesBase, retrieveBestChartAsset,
 *                              retrieveChartComponent.  All added for new
 *                              PAPI elements.
 * 21-Jan-02    Mat             VBM:2002011801 - Added generateJavaScriptID(),
 *                              generateStyleID() and generateWMLActionID() to
 *                              generate page unique ID's.
 * 24-Jan-02    Doug            VBM:2002011406 - removed the methods
 *                              getPrefixURL(), buildAbsoluteURL(),
 *                              getURLFromExternalPlugin()
 *                              added the method computeURL()
 *                              which resolves an assets URL be calling the
 *                              DefaultURLRewriter. Asset URL's are now cached
 *                              as MarinerURL objects rather than strings.
 *                              retrieveAbsouluteURLAsString() can now return a
 *                              relative URL. Modified the methods
 *                              retrieveImageAssetURLAsString() and
 *                              retrieveLinkAssetURLAsString() to call
 *                              retrieveAbsouluteURLAsString().
 * 30-Jan-02    Mat             VBM:2002011410 - Changed to accept theme names
 *                              on included pages.  Theme will inherit if no
 *                              theme name found.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed unused caching code,
 *                              changed writer to be a simple Writer and
 *                              made getCurrentStringBuffer public.
 * 01-Feb-02    Doug            VBM:2002011406 - deprecated the method
 *                              retrieveAbsoluteURLAsString() as we can no
 *                              longer guarantee that an absolute URL is
 *                              returned. Added the method computeURLAsString()
 *                              to replace retrieveAbsoluteURLAsString().
 * 08-Feb-02    Adrian          VBM:2002020603 - pageHead is now set to be the
 *                              page head of the includingPageContext if this
 *                              this is an includedPageContext.  The release
 *                              method for pageHead is only called if this is
 *                              is the includingPageContext - i.e. is the real
 *                              owner of the PageHead.
 * 12-Feb-02    Steve           VBM:2001101803 - Added form fragment support.
 *                              This is basically the same as for normal frags
 *                              except that forms are kept in a hash table where
 *                              there is a list of states per form. The name
 *                              of the form is the key into the hash table.
 * 12-Feb-02    Mat             VBM:2002021203 - Changed getInclusionPath() to
 *                              check for a vportlet parameter if the
 *                              inclusionPath from the layout context is null
 * 12-Feb-02    Paul            VBM:2002021201 - Removed page head, it is
 *                              string protocol specific and therefore has
 *                              been moved to the protocols package and renamed
 *                              StringPageHead.
 * 13-Feb-02    Paul            VBM:2002021203 - Moved fragmentation state
 *                              cache from session into PageGenerationCache.
 *                              Also, added the ability to generate portlets
 *                              out of context.
 * 14-Feb-02    Steve           VBM:2001101803 - Redid the previous changes
 *                              under this VBM due to Pauls changes for
 *                              2002021203... The joy of concurrent changes.
 * 15-Feb-02    Paul            VBM:2002021203 - Added isRootCanvas method and
 *                              replaced literal url parameter names with
 *                              constants from URLConstants.
 * 19-Feb-02    Paul            VBM:2001100102 - Added support for hiding
 *                              differences between the way protocols handle
 *                              form fields. Also removed the
 *                              retrieveLinkAssetURLAsString method and
 *                              replaced any calls to it to computeURLAsString.
 * 21-Feb-02    Steve           VBM:2001101803 - Added form fragmentation reset
 *                              state to denote whether the current fragment is
 *                              being reset or not.
 * 22-Feb-02    Steve           VBM:2001101803 - Fixed bug in
 *                              updateFormFragmentationState() that stopped
 *                              non-fragmented forms working.
 * 22-Feb-02    Adrian          VBM:2002021906 - Added methods...
 *                              writeScriptText() getScriptStringBuffer() and
 *                              getCurrentParentElement().  These ensure that
 *                              a script asset is written to the current
 *                              stringbuffer if the script element has a parent
 *                              block element, otherwise it is written to the
 *                              DeviceLayoutContext preamble buffer.
 * 25-Feb-02    Paul            VBM:2002022204 - Removed responseWriter from
 *                              initialisePage. The initialiseResponse now
 *                              gets it and this is only called if we have
 *                              something to write to the page.
 * 25-Feb-02    Adrian          VBM:2002021906 - Removed methods...
 *                              writeScriptText() getScriptStringBuffer() and
 *                              getCurrentParentElement().
 *                              Method getCurrentStringBuffer now returns null
 *                              if the current pane is null.  StringProtocol
 *                              checks for this null and if found writes to the
 *                              DeviceLayoutContext preambleBuffer.
 * 28-Feb-02    Paul            VBM:2002022804 - Fixed minor problem with
 *                              form fragmentation work, made pushing and
 *                              popping a pane push and pop its OutputBuffer.
 *                              Popping a pane also calls endContentBuffer to
 *                              support iterator panes. Also removed the
 *                              writeToPage and writeRawText, writeEncodedText
 *                              as this is now supported through the protocol.
 * 04-Mar-02    Adrian          VBM:2002021908 - modified retrieveDynamicVisual
 *                              methods to use the new
 *                              DynamicVisualAssetSelectionPolicy as a result
 *                              of the changes to auto generate the dynamic
 *                              visual accessors.
 * 04-Mar-02    Paul            VBM:2001101803 - Fixed some problems with
 *                              updateFormParameters as it was not correctly
 *                              copying the values from the session into the
 *                              pureRequestURL for fragmented forms.
 * 08-Mar-02    Paul            VBM:2002030607 - Added extra logging for
 *                              themes.
 * 12-Mar-02    Paul            VBM:2002021201 - Called the protocol method
 *                              initialiseCanvas at the end of the
 *                              initialiseCanvas method to allow it to do any
 *                              initialisation which may depend on the
 *                              properties initialise in that method.
 * 13-Mar-02    Paul            VBM:2002031301 - Change the pushPane, popPane
 *                              to push and pop the output buffer associated
 *                              with the pane and not the current output
 *                              buffer which may change if the pane is an
 *                              iterator pane. Also modifed the
 *                              getCurrentOutputBuffer method to return the
 *                              current buffer.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 21-Mar-02    Allan           VBM:2002032001 - Added isErrorCanvas property
 *                              so we can establish is the page (canvas) is
 *                              an error page from the MarinerPageContext.
 * 22-Feb-02    Paul            VBM:2002021802 - Always allocate a new
 *                              DeviceLayoutContext from the pool.
 * 22-Mar-02    Mat             VBM:2002022009 - Seeing as everyone else is
 *                              adding stacks, I thought I'd add an
 *                              IMDAPIElement stack.
 * 22-Mar-02    Adrian          VBM:2002031503 - Modified retrieveBest...
 *                              AssetGroup, Audio, Image, Link, Text, Chart,
 *                              RolloverImage, ButtonImage ...Asset methods
 *                              to use ObjectSelectionPolicies.
 * 22-Mar-02    Steve           VBM:2002031801 Added the following methods
 *                              for layout substitution.
 *                              void initialiseSubstituteLayout() Called by
 *                              the layout element when it starts
 *                              void substituteLayout() called by the layout
 *                              element when it ends.
 *                              void substituteFormat() called by the
 *                              substituteformat element to substitute formats
 * 25-Mar-02    Allan           VBM:2002022007 - Added a LocalRSBPool and
 *                              allocateRSB() and releaseRSB() methods. Also,
 *                              call release() on the localRSBPool in
 *                              releasePage().
 * 02-Apr-02    Steve           VBM:2001101803 - Fixed UpdateFormParameters
 *                              to only update the parameters if they are named
 * 03-Apr-02    Adrian          VBM:2001102414 - Added branding support.  New
 *                              cache to map from jsp name identities to brand
 *                              identities. Modified all retrieve asset &
 *                              component methods to use identities instead of
 *                              String names. Deprecated previous versions of
 *                              these methods for backward compatability.
 * 22-Apr-02    Paul            VBM:2002041901 - Removed the deprecated method
 *                              retrieveAbsoluteURLAsString and used the
 *                              computeURLAsString method instead.
 * 26-Apr-02    Paul            VBM:2002042205 - Added pushDeviceLayoutContext
 *                              method.
 * 01-May-02    Mat             VBM:2002040814 - Added code to support
 *                              remote repositories
 * 02-May-02    Steve           VBM:2002040817 - Added retrieveRemotePolicies()
 *                              to allow remote policy preloading through
 *                              a servlet or JSP.
 * 02-May-02    Allan           VBM:2002040804 - Replaced old DeviceTheme with
 *                              the new DeviceTheme.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Paul            VBM:2002042203 - Removed maxPageSize, that
 *                              value is now protocol dependent.
 * 07-May-02    Steve           VBM:2002040817 - Added flushRemotePolicies()
 *                              and flushAllRemotePolicies() to flush remote
 *                              caches.
 * 07-May-02    Adrian          VBM:2002042302 - Updated method getAncestor..
 *                              ..Relationship to return MarinerRequestContext.
 *                              IS_DEVICE if the device name matches the name
 *                              of the current device
 * 08-May-02    Allan           VBM:2002050402 - Removed extraenous catch block
 *                              from initialiseCanvas().
 * 08-May-02    Mat             VBM:2002040814 - Changed getAssetGroup() to
 *                              use a RepositoryObjectManager
 * 07-May-02    Jason           VBM:2002050802 - Modified initialiseSession()
 *                              so that if device or user are null, they will be
 *                              repopulated asif this was a new session. This
 *                              is to take into account session migrated over
 *                              by clusters.
 * 13-May-02    Doug            VBM:2002040803 - removed the method
 *                              getFakeDeviceTheme().
 * 22-May-02    Mat             VBM:2002040826 - Change retrieveRemotePolicies()
 *                              to log a message and stack trace if there was
 *                              a problem retrieving the remote policies.
 * 27-May-02    Paul            VBM:2002050301 - Added getDeviceLayout method
 *                              to retrieve a DeviceLayout by name for the
 *                              current device.
 * 29-May-02    Paul            VBM:2002050301 - Stopped the retrieveBest...
 *                              methods from doing the branding as this is now
 *                              done by the selection policies.
 * 19-Jun-02    Adrian          VBM:2002053104 - Added methods getStyleClass..
 *                              ..Name getShortInclusionPath and makeShort..
 *                              ..InclusionPath.
 * 16-Jul-02    Adrian          VBM:2002071108 - Do not set brand name in
 *                              initialiseCanvas when the brandName is an
 *                              empty string.
 * 22-Jul-02    Ian             VBM:2002052804 - Removed all references to old
 *                              styles classes.
 * 26-Jul-02    Allan           VBM:2002072508 - Replaced calls to asset.
 *                              getAssetGroup() with getAssetGroupName(asset).
 *                              Replaced calls to asset.getValue() with
 *                              getValue(asset). Added getValue(), modified
 *                              getAssetGroup() to work with SubstantiveAssets.
 *                              Added getAssetGroupName().
 * 31-Jul-02    Paul            VBM:2002073008 - Added support for package.
 * 06-Aug-02    Paul            VBM:2002073008 - Removed support for package.
 * 09-Sep-02    Mat             VBM:2002040825 - Changed arguments for
 *                              cacheRemoteComponent in retrieveRemotePolicies
 *                              to reflect changes in Volantis.java
 * 09-Sep-02    Ian             VBM:2002081307 - Added getTranscodingRule
 *                              method.
 * 01-Oct-02    Steve           VBM:2002071604 - Added ApplicationProperties
 *                              retrieval methods
 * 02-Oct-02    Ian             VBM:2002092507 - Changed getAssetGroup from
 *                              protected to public.
 * 07-Oct-02    Allan           VBM:2002100202 - Corrected javadoc in
 *                              getTextFromReference() to refer to a
 *                              TextComponentIdentity instead of a
 *                              TextComponentName.
 * 08-Oct-02    Sumit           VBM:2002091202 - retrieveRemotePolicies()
 *                              changed to use RemoteCacheEntry object
 * 23-Oct-02    Steve           VBM:2002071604 - Modified ApplicationProperties
 *                              retrieval methods for the auto generated
 *                              accessors and fixed up loads of javadoc.
 * 04-Nov-02    Mat             VBM:2002110401 - Changed the
 *                              retrieveBestApplicationProperties() to use
 *                              ApplicationPropertiesSelectionPolicy.
 * 30-Oct-02    Phil W-S        VBM:2002100906 - Updated context to utilize
 *                              the device theme context's LayoutDeviceTheme
 *                              for style class name generation, if the
 *                              protocol uses layout device themes. Changed
 *                              getStyleClassName.
 * 08-Nov-02    Phil W-S        VBM:2002102306 - Updated initialiseCanvas to
 *                              populate the device theme into the device
 *                              layout context. Allows access to the stack
 *                              of device themes (the current theme and through
 *                              the including device layout contexts the
 *                              "ancestor" themes.
 * 25-11-02     Sumit           VBM:2002111103 - push/popPane now supports
 *                              FormatInstanceReferences
 * 18-Nov-02    Geoff           VBM:2002111504 - Refactored the way fallbacks
 *                              are accessed; removed
 *                              getFallbackTextAssetForLinkComponent and added
 *                              getFallbackText[Asset]ForComponentId and
 *                              getFallbackImage[Url|Asset]ForComponentId.
 *                              Also, generally cleaned up; removed many unused
 *                              deprecated methods, unused variables and fixed
 *                              javadoc as well.
 * 20-Nov-02    Geoff           VBM:2002111504 - Refactored code to get
 *                              fallback text of particular encoding from
 *                              VolantisProtocol into new overload of
 *                              getFallbackTextForComponentId, added logging.
 * 20-Nov-02    Geoff           VBM:2002111504 - Remove an unused method I
 *                              forgot. Doh.
 * 03-Nov-02    Geoff           VBM:2002120306 - Cleanup before rewriting
 *                              Volantis.java - removed unused methods
 *                              getApplicationServerType, getConfig that called
 *                              Volantis, and removed other methods
 *                              getXxxRepositoryManager and flushXxxPolicy
 *                              that were only called by MarinerPageContext
 *                              and called Volantis - now MRC calls it direct.
 * 04-Dec-02    Steve           VBM:2002071604 - Got rid of Mat's logging message
 *                              which causes a null pointer exception if the
 *                              application properties being searched for do not
 *                              exist.
 * 03-Nov-02    Geoff           VBM:2002120306 - Whoops, also remove the
 *                              unused retrieveApplicationProperties method -
 *                              should be using the getBestXxx variant anyway,
 *                              and remove unused imports I forgot b4 too.
 * 06-Dec-02    Allan           VBM:2002110102 - DeviceLayout has had all its
 *                              FormatNamespace methods replaced by a single
 *                              add, remove, retrieve method. This class
 *                              has been modified accordingly. Modified
 *                              setCurrentFragment() to update the
 *                              FormatScope. Used formatScope instead of
 *                              deviceLayout when retrieving Formats. Added
 *                              getFormatScope().
 * 18-Dec-02    Phil W-S        VBM:2002121601 - Added javadoc for
 *                              getDeviceLayout.
 * 30-Dec-02    Byron           VBM:2002071015 - Modified LocalRSBPool import
 *                              statement to reflect updated package structure.
 * 09-Jan-02    Sumit           VBM:2002112201 - initialiseCanvas() and
 *                              substituteLayout() create a new DeviceLayoutCxt
 *                              instead of allocating from pool
 * 14-Jan-03    Mat             VBM:2002112212 - Added retrieveBestAudioAsset
 *                              which doesn't take an encoding.  Used when
 *                              searching for a DeviceAudioAsset.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported changes from Metis to
 *                              move session management into the environment.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Remove the outputWriter and
 *                              responseWriter members and usages
 *                              (releaseCanvas and initialiseResponse
 *                              affected). Remove the setOutputWriter and
 *                              getOutputWriter methods. Make getAbsoluteURL
 *                              public and change exception thrown (affects
 *                              getContentsFromVariant and
 *                              getContentsOfScriptAsset).
 * 18-Feb-03    Geoff           VBM:2003021903 - Fix NPE in getDeviceLayout().
 * 24-Feb-03    Ian             VBM:2003020607 - Fixed problem in computeURL.
 * 24-Feb-02    Ian             VBM:2003021904 - Added code in InitializePage to
 *                              set sessionDevice if not set.
 * 05-Mar-03    Chris W         VBM:2003022706 - updateFormFragmentationState
 *                              now looks in the current fragment's format
 *                              scope as well as the device layout's format
 *                              scope for the form fragment. Direct reference
 *                              to fields have been changed to calls to getXXX
 *                              methods as well.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 17-Apr-03    Geoff           VBM:2003040305 - Refactor two overloads of
 *                              getContentsOfScriptAsset away to Script class,
 *                              refactor getTextFromURL away to
 *                              UrlAssetTextRetriever class, modify
 *                              getContentsFromVariant to use Retrievers, make
 *                              getValueFromExternalPlugin public so the
 *                              Retreivers can see it.
 * 22-Apr-03    Allan           VBM:2003041710 - Added isMarinerExpression().
 *                              Called isMarinerExpression() from
 *                              handleMarinerExpression().
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import com.volantis.charset.Encoding;
import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.MediaAgentFactory;
import com.volantis.map.agent.RequestFactory;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.expression.SelectState;
import com.volantis.mcs.imdapi.AbstractIMDAPIElement;
import com.volantis.mcs.integration.AssetTranscoderContext;
import com.volantis.mcs.integration.ImageURLModifier;
import com.volantis.mcs.integration.ImageURLModifierDetails;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.integration.TranscodingException;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.integration.iapi.IAPIElement;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.FormatScope;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.menu.MenuRendererSelectorLocator;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.implementation.AssetResolver;
import com.volantis.mcs.protocols.css.DefaultStylePropertyResolver;
import com.volantis.mcs.protocols.css.StylePropertyResolver;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.SessionFormData;
import com.volantis.mcs.protocols.forms.XFormEmulatingPageContext;
import com.volantis.mcs.protocols.layouts.AbstractPaneInstance;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilderFactory;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.FormatLocator;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.OutputBufferResolver;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.ScriptLibraryManager;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.scriptlibrarymanager.RequiredScriptModules;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.plugin.markup.MarkupFactory;
import com.volantis.mcs.runtime.plugin.markup.MarkupPluginContainer;
import com.volantis.mcs.runtime.policies.PolicyFetcher;
import com.volantis.mcs.runtime.policies.PolicyReferenceBrander;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolverImpl;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.policies.SelectedVariant;
import com.volantis.mcs.runtime.policies.SelectionContext;
import com.volantis.mcs.runtime.policies.SelectionContextImpl;
import com.volantis.mcs.runtime.policies.expression.PolicyExpressionParserImpl;
import com.volantis.mcs.runtime.policies.theme.RuntimeDeviceTheme;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.runtime.repository.imd.IMDPolicyFetcher;
import com.volantis.mcs.runtime.repository.remote.RemoteRepositoryHelper;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.mcs.runtime.styling.StylingFunctions;
import com.volantis.mcs.utilities.LocalRSBPool;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.PolicyException;
import com.volantis.mcs.utilities.PreservedArea;
import com.volantis.mcs.utilities.StringConvertor;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.InlineStyleSheetCompilerFactory;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.expressions.EvaluationContext;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.path.Path;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;
import com.volantis.xml.expression.ExpressionContext;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;

/**
 * Provides methods and instance variables that are applicable to the
 * page associated with the current request. Every requested page will
 * generate a new MarinerPageContext object.
 * <p/>
 * <p/>
 * The top level (first) canvas with the request will initialise the top level
 * stacks and global objects such as the styling engine within the
 * MarinerPageContext.
 * </p>
 * <p>An included canvas will push its status onto the various stacks held
 * within the MarinerPageContext and remove those from the stack on completion
 * of that canvases rendering.
 * <p/>
 * <p/>
 * If this page is being rendered inside a region then the DeviceLayoutContext
 * is added to the RegionInstance of the including region instead of being
 * released here.
 * </p><p>
 * An outOfContextInclusion is a page which was created in either a servlet, or
 * a JSP page but which was not initialised by a canvas and so limits the
 * information which can be inherited
 * </p>
 * <p/>
 * <h2>Refactoring</h2>
 * <p/>
 * This class has grown quite substantially since it was first created and
 * has reached the point where it is becoming quite unmanageable. It has 2
 * distinct but related uses. As the internal request context, and the canvas
 * context. It would be better if this was split into a couple of different
 * related classes each of which is geared towards the different use made of
 * it. This would have significant savings in terms of wasted space.
 * <p/>
 * Obviously refactoring can introduce bugs so it must be done with great care
 * and even greater understanding of the uses made of it as it will have a wide
 * impact on the code.
 *
 * @mock.generate
 */
public class MarinerPageContext implements
        OutputBufferResolver,
        FormatLocator,
        OutputBufferStack,
        XFormEmulatingPageContext,
        CurrentProjectProvider, 
        BrandNameProvider,
        BaseURLProvider {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MarinerPageContext.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    MarinerPageContext.class);

    /**
     * The styling factory to use.
     */
    private final StylingFactory stylingFactory;

    // --------------------------------------------------------------------------
    // Page resources.
    // ===============
    //   These resources are either initialised directly in initialisePage, or
    //   are only dependent on other page resources. Either way they are all
    //   released in releasePage.
    // --------------------------------------------------------------------------

    /**
     * A pool of ReusableStringBuffers for the page.
     */
    private LocalRSBPool localRSBPool;

    /**
     * Flag which indicates that initialisePage completed successfully and
     * releasePage has not yet has been called.
     */
    private boolean initialisedPage;

    /**
     * A counter which when combined with {@link #requestID} produces a unique
     * identifier across all pages.
     */
    private int counter;

    /**
     * A Map which is used to associate information with an id specified on an
     * element.
     * <p/>
     * <p/>
     * </p>
     */
    private Map idMap;

    /**
     * Used to differentiate between a null value for an id, and an id which
     * has never been set.
     */
    private static Object idMapNullValue = new Object();

    /**
     * The current inclusion state of the page;
     */
    private boolean inclusionState;


    /**
     * The cache of information which is needed when generating pages.
     */
    private PageGenerationCache pageGenerationCache;

    /**
     * The protocol which the current device supports.
     */
    private VolantisProtocol protocol;

    /**
     * The MarinerURL which contains the pure request parameters.
     * <p/>
     * By pure we mean that the url does not contain any of the parameters that
     * we use to control the page generation process and also has removed any
     * protocol differences.
     * </p>
     */
    private MarinerURL pureRequestURL;

    /**
     * The file part of the requestURL.
     */
    private String relativeRequestURL;


    /**
     * The stack of MarinerRequestContexts.
     */
    private Stack requestContextStack;

    /**
     * The Application context for this page
     */
    private ApplicationContext applicationContext;

    /**
     * An ID which is unique across all the requests which have ever been
     * processed, allowing for wrapping of course.
     */
    private String requestID = null;

    /**
     * The URL which was (or could be) used to initiate this request.
     */
    private MarinerURL requestURL;

    /**
     * The URL of the top level page.
     */
    private MarinerURL rootPageURL;

    /**
     * The Volantis bean, there is only ever one of these created and it never
     * changes so we do not need to reset it.
     */
    private Volantis volantisBean;


    private StringBuffer WMLActionID;

    /**
     * FCUiniqueID is generated for ensuring that each FrameworkClient
     * related item without explicity spiecified ID will get unique one. 
     */
    private StringBuffer FCUniqueID;

    /**
     * The name of the character set to be used for output.
     * <p/>
     * There are various ways to communicate this to the output device,
     * for example in a HTTP or MIME ContentType header, in an xml declaration,
     * or in a HTML META tag.
     * <p/>
     * This value is used to derive the value of {@link #charsetEncoding}.
     */
    private String charsetName;

    /**
     * The charset Encoding derived from {@link #charsetName}.
     */
    private Encoding charsetEncoding;

    /**
     * The {@link com.volantis.styling.engine.StylingEngine} instance for this
     * session.
     */
    private StylingEngine stylingEngine;

    // --------------------------------------------------------------------------
    // Session resources.
    // =================
    //   These resources are either initialised directly in initialiseSession, or
    //   are dependent on other session resources. Either way they are all
    //   released in releaseSession.
    // --------------------------------------------------------------------------

    // --------------------------------------------------------------------------
    // Canvas resources.
    // =================
    //   These resources are either initialised directly in initialiseCanvas, or
    //   are dependent on other canvas resources. Either way they are all
    //   released in releaseCanvas.
    // --------------------------------------------------------------------------

    /**
     * Flag which indicates that initialiseCanvas completed successfully and
     * releaseCanvas has not yet been called.
     */
    private boolean initialisedCanvas;

    /**
     * The type of the canvas.
     */
    private Stack canvasTypeStack;

    /**
     * Whether or not the canvas is for an error.
     */
    private boolean isErrorCanvas;

    /**
     * A boolean flag denoting whether the canvas has seen any children yet.
     */
    private boolean canvasHasChildren;

    /**
     * The canvas brand name
     */
    private Stack brandNameStack;

    /**
     * The object which contains all the context specific information about the
     * device layout.
     */
    private Stack deviceLayoutContextStack;

    /**
     * The index which can be used to access the current fragmentation state
     * in the session.
     */
    private int fragmentationIndex = -1;

    /**
     * The fragmentation state for the current page and its included pages.
     */
    private FragmentationState fragmentationState;

    /**
     * Hashmap of form fragmentation states
     */
    private HashMap formFragmentationStates;

    /**
     * A flag which indicates whether the current form fragment is being reset
     */
    private boolean formFragmentResetState = false;

    /**
     * PAPI: The stack of output buffers.
     */
    private Stack outputBufferStack;

    /**
     * The id of the root tag in the page.
     */
    private String pageTagId;

    /**
     * PAPI: The stack of PAPIElements.
     */
    private Stack papiElementStack;

    /**
     * IMDAPI: The stack of IMDAPI elements.
     */
    private Stack imdapiElementStack;

    /**
     * IAPI: The stack of {@link IAPIElement}s
     */
    private Stack iapiElementStack;

    /**
     * MCSI: The stack of MCSIElements
     */
    private Stack mcsiElementStack;

    /**
     * The stack of Container Instances.
     * <p/>
     * A container context is created and pushed onto/popped off the stack for
     * each container element in the input PAPI/XDIME. We need a stack as containers
     * can be nested. The current container instance is thus always on the top of
     * the stack.
     */
    private Stack containerInstanceStack;

    /**
     * A builder used to support the construction of enhanced menus.  This has
     * it's own initialise and release methods.  Releasing a canvas should
     * always release any menu building resources, but initialise should not
     * be called by default.
     */
    private MenuModelBuilder menuBuilder;

    /**
     * A count of the number of levels of nested XDIME CP elements that have
     * been entered. This is needed in order to prevent XDIME from being used
     * inside XDIME CP.
     */
    private int xdimecpCount;

    private static final String WMLACTION_ID_PREFIX = "VC_";

    /** prefix for FrameworkClient ids */ 
    private static final String FRAMEWORK_CLIENT_PREFIX = "FC_";

    /**
     * The {@link MarkupPluginContainer} to which we will delegate our
     * {@link MarkupPluginContainer} methods.  If we have an enclosing page
     * context then this should be the handler of that context (and we won't
     * call release on it) otherwise it should be initialized to a default
     * {@link MarkupPluginContainer}
     */
    protected MarkupPluginContainer markupPluginContainer;

    /**
     * This is the stack of SelectState items.
     */
    private Stack selectStateStack;

    /**
     * Resolves style properties using the expression context associated with
     * this page context.
     */
    private StylePropertyResolver styleResolver;

    /**
     * The object to use to find appropriate format references.
     */
    private FormatReferenceFinder formatReferenceFinder;

    private AssetResolver assetResolver;

    private SelectionContext selectionContext;

    private PolicyFetcher policyFetcher;

    private final DevicePolicyAccessor devicePolicyAccessor;

    private PolicyReferenceResolver policyReferenceResolver;

    /**
    /**
     * Map from element ids to meta objects stored for that id. Null key means
     * that the value applies for the whole page.
     */
    private final Map metaDataMap;

    /**
     * The object that provides access to the current base URL.
     * 
     * <p>This is only valid for use when processing the input elements. It is
     * not valid for use after processing the elements, e.g. when rendering
     * the layouts.</p>
     */ 
    private BaseURLProvider baseURLProvider;

    /**
     * The processing phase.
     */
    private ProcessingPhase processingPhase = ProcessingPhase.PHASE1;
    private PolicyReferenceFactory referenceFactory;

    /**
     * The stack of projects.
     */
    private ProjectStack projectStack;

    /**
     * The registry of handler scripts. Used by the XDIME 2 'handler' element
     * to register its scripts and the 'listener' element to look them up again.
     */
    private HandlerScriptRegistry handlerScriptRegistry =
            new HandlerScriptRegistry();

    /**
     * The registry of listener events. Used by the XDIME 2 'listener' element
     * to register its events and the various other XDIME 2 elements to look up
     * events.
     */
    private ListenerEventRegistry listenerEventRegistry =
            new ListenerEventRegistry();

    /**
     * ScriptLibraryManager object that collects script libraries.
     */
    private ScriptLibraryManager scriptLibraryManager;

    /**
     * RequestedScriptModules object that collects script libraries.
     */
    private RequiredScriptModules requiredScriptModules;

    /**
     * The fetcher used when supporting IMDAPI elements.
     */
    private IMDPolicyFetcher imdPolicyFetcher;

    /**
     * The URI rewriter.
     */
    private PageURIRewriterImpl uriRewriter;

    /**
     * Resolver for transcodable URLs.
     */
    private TranscodableUrlResolver transcodableUrlResolver;

    /**
     * An instance of MediaAgent, instantiated on first access.
     */
    private MediaAgent mediaAgent = null;

    /**
     * Externally accessible name of local host.
     */
    private String externalHostName;

    /**
     * Create a new <code>MarinerPageContext</code>.
     */
    public MarinerPageContext() {
        this(null);
    }

    public MarinerPageContext(StylingFactory stylingFactory) {
        this.stylingFactory = stylingFactory;
        formFragmentationStates = new HashMap();

        devicePolicyAccessor = new ExternalDevicePolicyAccessor(this);

        metaDataMap = new HashMap();
    }

    /**
     * Perform the page specific initialisation.
     *
     * @param volantisBean            The VolantisBean.
     * @param requestContext          The <code>MarinerRequestContext</code> object for
     *                                the page being generated.
     * @param enclosingRequestContext The <code>MarinerRequestContext</code>
     *                                object for the page which included this page, or null.
     * @param marinerRequestURL       The <code>MarinerURL</code> which contains the
     *                                URL which could have been used to generate this request.
     * @param requestHeaders          The <CODE>RequestHeaders</CODE> to send with any requests
     * @throws RepositoryException Occurs if any called methods throw this exception
     */
    public void initialisePage(
            Volantis volantisBean,
            MarinerRequestContext requestContext,
            MarinerRequestContext enclosingRequestContext,
            MarinerURL marinerRequestURL,
            RequestHeaders requestHeaders)
            throws RepositoryException {

        // Make sure that we don't try to initialise the page resources
        // more than once.
        if (initialisedPage) {
            throw new IllegalStateException
                    ("initialisePage has already been called");
        }

        try {

            MarkupFactory markupFactory = MarkupFactory.getDefaultInstance();
            markupPluginContainer =
                    markupFactory.createMarkupPluginContainer();

            if (localRSBPool == null) {
                localRSBPool = new LocalRSBPool(25);
            }

            // Save a reference to the bean.
            if (logger.isDebugEnabled()) {
                logger.debug("Volantis bean is " + volantisBean);
            }
            this.volantisBean = volantisBean;
            this.pageGenerationCache = volantisBean.getPageGenerationCache();

            // Create the project stack and initialise it with the default
            // project.
            projectStack = new ProjectStack();
            projectStack.pushProject(volantisBean.getDefaultProject());

            // Save a reference to the MarinerRequestContext.
            pushRequestContext(requestContext);

            EnvironmentContext environmentContext =
                    requestContext.getEnvironmentContext();

            // Initialise the MarinerSessionContext.
            //initialiseSession ();
            applicationContext =
                    ContextInternals.getApplicationContext(requestContext);
            // If we don't have a session context device yet then get it from the
            // applicationContext.

            MarinerSessionContext sessionContext = environmentContext.getSessionContext();
            InternalDevice device = sessionContext.getDevice();
            if (device == null) {
                device = applicationContext.getDevice();
                sessionContext.setDevice(device);
            }

            protocol = applicationContext.getProtocol();

            // The protocol needs to know what the context is.
            protocol.setMarinerPageContext(this);

            URLRewriter urlRewriter = volantisBean.getURLRewriter();

            requestURL = urlRewriter.mapToMarinerURL(requestContext,
                                                     marinerRequestURL);

            // The request URL must not be modified.
            requestURL.makeReadOnly();

            // Create a pure version of the request url which has all the extra
            // parameters that we use for page generation removed.
            pureRequestURL = new MarinerURL(requestURL);
            pureRequestURL.removeParameter(
                    URLConstants.FRAGMENTATION_PARAMETER);
            pureRequestURL.removeParameter(URLConstants.SEGMENTATION_PARAMETER);
            pureRequestURL.removeParameter(URLConstants.FORM_PARAMETER);

            // If the pure request has a vform attribute then make sure that the
            // values of the form parameters are consistent.
            updateFormParameters();

            // Make sure that the pure request url is not modified.
            pureRequestURL.makeReadOnly();

            // a URL file is the URL path and query string
            relativeRequestURL = requestURL.getFile();

            initialiseTopLevelCanvas();

            PolicyFetcher policyFetcher =
                    volantisBean.getPolicyFetcher();

            // If support for inline meta data has been configured then wrap
            // the fetcher in a special one that will search for policies in
            // the page first.
            if (volantisBean.isIMDRepositoryEnabled()) {
                imdPolicyFetcher = new IMDPolicyFetcher(policyFetcher,
                        volantisBean.getPolicyActivator(), this);
                policyFetcher = imdPolicyFetcher;
            }

            SelectionContextImpl selectionContext = new SelectionContextImpl();
            selectionContext.setDevice(device);
            selectionContext.setPolicyFetcher(policyFetcher);
            this.policyFetcher = policyFetcher;
            this.selectionContext = selectionContext;

            referenceFactory = volantisBean.getPolicyReferenceFactory();
            
            uriRewriter = new PageURIRewriterImpl();

            assetResolver = new AssetResolverImpl(this, selectionContext,
                    volantisBean.getVariantSelectionPolicy(),
                    applicationContext.getPageURLRewriter(),
                    referenceFactory);

            // Create the object responsible for resolving expressions to
            // policy references.
            ExpressionContext expressionContext =
                    MCSExpressionHelper.getExpressionContext(requestContext);
            PolicyExpressionParserImpl expressionParser =
                    new PolicyExpressionParserImpl(this, this, this);

            PolicyReferenceBrander brander = new PolicyReferenceBrander(
                    referenceFactory);

            policyReferenceResolver =
                    new PolicyReferenceResolverImpl(expressionContext,
                            assetResolver, referenceFactory,
                            expressionParser, brander);

            // Initialise the protocol.
            // Note: must be initialized after assetResolver and
            // policyReferenceResolver as at least some protocols use those variables
            // during their initialization
            protocol.initialise();

            try {
                externalHostName = InetAddress.getLocalHost().getCanonicalHostName();
            } catch (UnknownHostException e) {
                externalHostName = null;
                logger.warn("cannot-get-canonical-host-name", e);
            }

            // This must be the last thing which is done in this catch block.
            initialisedPage = true;
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
            throw e;
        } catch (RuntimeException e) {
            logger.error("unexpected-exception", e);
            throw e;
        } catch (Throwable e) {
            // Cannot rethrow throwables, so this may hide problems (programmatically).
            logger.error("unexpected-exception", e);
        } finally {
            if (!initialisedPage) {
                // The only way we could have reached here is if there was an
                // exception in the body of the catch which caused the initialised
                // flag to not be set.

                logger.error("mpc-exception");

                // Clean up the page related resources which may already have been
                // allocated by this method.
                releasePage();

                // Clean up everything else.
                release();
            }
        }
    }

    /**
     * Release any resources which were allocated by the initialisePage method.
     * <p/>
     * This method has to free the resources in an order determined by the
     * dependencies between the different resources. A resource must only
     * be released once all resources which depend on it have also been
     * released.
     * </p>
     */
    private void releasePage() {

        localRSBPool.release();
        markupPluginContainer.releasePlugins();
    }

    /**
     * Allocate a ReusableStringBuffer for use during this page.
     *
     * @return a ResuableStringBuffer for use during this page
     */
    public ReusableStringBuffer allocateRSB() {
        return localRSBPool.allocateObject();
    }

    /**
     * Release a ReusableStringBuffer from use.
     *
     * @param rsb the ReusableStringBuffer to release
     */
    public void releaseRSB(ReusableStringBuffer rsb) {
        localRSBPool.releaseObject(rsb);
    }

    /**
     * Find of if the Canvas for this context is an error canvas.
     *
     * @return true if the canvas for this context is an error canvas;
     *         otherwise false.
     */
    public boolean isErrorCanvas() {
        return isErrorCanvas;
    }


    /**
     * Initialise the MarinerPageContext object for a canvas.
     *
     * @param brandName       The name of the brand for this page
     * @param inclusion       Specifies whether this page should be treated as an
     *                        inclusion.
     * @param errorPage       True if this is an error page and false otherwise.
     * @param id              The id of the page.
     * @param themeStyleSheets The compiled style sheets of the themes used in
     *                        this page.
     * @param layoutName      The name of the layout used in this page.
     * @throws LayoutException     If the named layout does not exist
     * @throws PolicyException     If a policy does not exist
     * @throws RepositoryException If an error occurs while accessing the repository
     */
    public void initialise(
            boolean inclusion,
            boolean errorPage,
            String id,
            String brandName,
            CompiledStyleSheetCollection themeStyleSheets,
            String layoutName)
            throws LayoutException,
            PolicyException,
            RepositoryException {
        // Make sure that the page resources have been initialised.
        if (!initialisedPage) {
            throw new IllegalStateException("Must call initialisePage first");
        }
        inclusionState = inclusion;

        try {
            // If this is an error page then remove all parameters from the
            // request url. The request url is currently read only so we need to
            // make a copy in order to modify it. We should probably move the
            // initialisation of the request url into here but only after we have
            // checked that it is ok.
            if (errorPage) {

                // The best way to ignore parameters is to clear them from the request
                // URL that way we can never use them. If we need to we can save the
                // parameters away so that they can be retrieved later. For now we just
                // remove the parameters that affect the way we generate the page.
                // This does mean that we cannot have a fragmented or montage error
                // page.
                requestURL = new MarinerURL(requestURL);
                requestURL.removeParameter(
                        URLConstants.FRAGMENTATION_PARAMETER);
                requestURL.removeParameter(URLConstants.SEGMENTATION_PARAMETER);
                requestURL.removeParameter(URLConstants.FORM_PARAMETER);
                requestURL.makeReadOnly();
                this.isErrorCanvas = errorPage;
            }

            this.pageTagId = id;

            // Make sure that the protocol knows whether or not this canvas is an
            // inclusion.
            protocol.setInclusion(inclusion);


            // Get the layout.
            if (layoutName != null) {
                RuntimeDeviceLayout deviceLayout =
                        getDeviceLayout(layoutName);
                if (deviceLayout == null) {
                    throw new LayoutException(EXCEPTION_LOCALIZER.format(
                            "no-suitable-device-layout",
                            new Object[]{layoutName,
                                         getDeviceName()}));
                }

                DeviceLayoutContext deviceLayoutContext =
                        new DeviceLayoutContext();
                deviceLayoutContext.setMarinerPageContext(this);

//                formatRendererContext.pushDeviceLayoutContext(
//                        deviceLayoutContext);

                if (inclusionState) {
                    DeviceLayoutContext idlc
                            = getDeviceLayoutContext();
                    deviceLayoutContext.setIncludingDeviceLayoutContext(idlc);
                    boolean inRegion
                            = (getEnclosingRegionInstance() != null);
                    deviceLayoutContext.setInRegion(inRegion);
                }

                deviceLayoutContext.setMarinerPageContext(this);
                deviceLayoutContext.setDeviceLayout(deviceLayout);
                deviceLayoutContext.initialise();
                pushDeviceLayoutContext(deviceLayoutContext);
            }

            getDeviceLayoutContext().setThemeStyleSheets(
                    themeStyleSheets);
            protocol.initialiseCanvas();

            // These locators can (and should be) overridden to provide access to
            // real markup generators and renderers.

            // No children have been seen for the canvas yet
            canvasHasChildren = false;

            // This must be the last thing which is done in this catch block.
            initialisedCanvas = true;
        } catch (LayoutException e) {
            logger.error("unexpected-exception", e);
            throw e;
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
            throw e;
        }
//        } catch (Throwable e) {
//            logger.error("unexpected-exception", e);
//            // None of the methods in the try body throw a PolicyException but
//            // this method declares that it does throw a PolicyException. Removing
//            // the throws will probably break other classes that expect it hence
//            // the need for the following condition.
//            // todo later investigate if a throws PolicyException clause is valid.
//            if (e instanceof PolicyException) {
//                throw (PolicyException) e;
//            }
//        }


    }

    public boolean initialisedCanvas() {
        return initialisedCanvas;
    }

    /**
     * Initialise resources that are shared across canvases.
     */
    private void initialiseTopLevelCanvas() {

        counter = 0;
        requestID = null;

        if (canvasTypeStack == null) {
            canvasTypeStack = new Stack();
        }
        if (deviceLayoutContextStack == null) {
            deviceLayoutContextStack = new Stack();
        }

        // Create a stack for papi elements
        if (papiElementStack == null) {
            papiElementStack = new Stack();
        }

        // Create a stack for output buffers
        if (outputBufferStack == null) {
            outputBufferStack = new Stack();
        }

        // Create a stack for the container instances.
        if (containerInstanceStack == null) {
            containerInstanceStack = new Stack();
        }

        // Create a stack for the imdapi elements.
        if (imdapiElementStack == null) {
            imdapiElementStack = new Stack();
        }

        if (iapiElementStack == null) {
            iapiElementStack = new Stack();
        }

        if (mcsiElementStack == null) {
            mcsiElementStack = new Stack();
        }

        if (selectStateStack == null) {
            selectStateStack = new Stack();
        }

        if (brandNameStack == null) {
            brandNameStack = new Stack();
        }


    }

    /**
     * Release any resources which were allocated by the initialiseCanvas method.
     * <p/>
     * This method has to free the resources in an order determined by the
     * dependencies between the different resources. A resource must only
     * be released once all resources which depend on it have also been
     * released.
     * </p>
     *
     * @todo later should really check that the InternalDevice stored in the Session
     * matches the request.
     */
    private void releaseCanvas() {

        // ------------------------------------------------------------------------
        // Free the rest of the canvas related resources.
        // ------------------------------------------------------------------------

        if (peekCanvasType() != null) {
            popCanvasType();
        }
    }


    /**
     * Release any resources and reset the state.
     */
    public void release() {
        
        // Release any canvas related resources next but only if initialiseCanvas
        // completed successfully.
        if (initialisedCanvas) {
            if (logger.isDebugEnabled()) {
                logger.debug("Releasing canvas resources");
            }
            releaseCanvas();
        }

        // Always pop the brand as it will always get
        // set on an initialise canvas
        if (brandNameStack != null) {
            if (!brandNameStack.isEmpty()) {
                brandNameStack.pop();
            }
        }

        // Release any page related resources but only if initialisePage
        // completed successfully.
        if (initialisedPage) {
            if (logger.isDebugEnabled()) {
                logger.debug("Releasing page resources");
            }
            releasePage();
        }
    }

    /**
     * Return the environment context
     *
     * @return The environment context
     */
    public EnvironmentContext getEnvironmentContext() {
        return getRequestContext().getEnvironmentContext();
    }

    /**
     * Get the enclosing Region instance from the top of the stack.
     *
     * @return The first Region instance found searching through the stack.
     */
    public RegionInstance getEnclosingRegionInstance() {
        return (RegionInstance) findContainerInstance(RegionInstance.class);
    }

    private ContainerInstance findContainerInstance(Class instanceClass) {

        if (containerInstanceStack != null) {

            for (int i = containerInstanceStack.size() - 1; i >= 0; i--) {
                ContainerInstance instance = (ContainerInstance)
                        containerInstanceStack.get(i);
                if (instanceClass.isInstance(instance)) {
                    return instance;
                }
            }
        }

        return null;

    }

    /**
     * Push the specified Container instance onto the top of the stack.
     *
     * @param instance The instance of the container to push onto the stack.
     */
    public void pushContainerInstance(ContainerInstance instance) {

        if (containerInstanceStack == null) {
            containerInstanceStack = new Stack();
        }
        containerInstanceStack.push(instance);
        if (logger.isDebugEnabled()) {
            logger.debug("CONTAINER INSTANCE STACK: Pushed " + instance);
        }

        pushOutputBuffer(instance.getCurrentBuffer());
    }

    /**
     * Pop the current Container instance from the top of the stack.
     *
     * @param expectedInstance The instance of the container which is expected to
     *                         be popped. If this is not null and not equal to the ContainerInstance
     *                         on the top of the stack then throw an IllegalStateException.
     */
    public void popContainerInstance(ContainerInstance expectedInstance) {

        ContainerInstance instance = (ContainerInstance) containerInstanceStack.pop();
        if (expectedInstance != null && expectedInstance != instance) {
            throw new IllegalStateException("CONTAINER INSTANCE STACK:"
                                            + " Expected " + expectedInstance
                                            + " popped " + instance);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("CONTAINER INSTANCE STACK: Popped " + instance);
        }

        popOutputBuffer(instance.getCurrentBuffer());

        // End the current content buffer.
        instance.endCurrentBuffer();
    }

    /**
     * Get the current Container instance from the top of the stack.
     *
     * @return The Container instance which is on the top of the stack.
     */
    public ContainerInstance getCurrentContainerInstance() {

        ContainerInstance instance;
        if (containerInstanceStack == null || containerInstanceStack.isEmpty()) {
            instance = null;
        } else {
            instance = (ContainerInstance) containerInstanceStack.peek();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("CONTAINER INSTANCE STACK: Current " + instance);
        }

        return instance;
    }

    /**
     * Make a string which uniquely identifies an inclusion.
     * <p/>
     * The string consists of the including pages inclusion path, the name and
     * n dimensional index of the region and the index of the inclusion within
     * the region.
     * </p>
     *
     * @param inclusionPath  The including pages region path.
     * @param regionInstance The region instance.
     * @param index          The index of the inclusion within the region.
     * @return The inclusion identity
     */
    public String makeInclusionPath(
            String inclusionPath,
            RegionInstance regionInstance,
            int index) {

        // Get the name of the enclosing region.
        String regionName = regionInstance.getRegion().getName();

        // The region name will be null if it is an anonymous region.
        if (regionName == null) {
            regionName = "<anon>";
        }
        int[] regionIndicies = regionInstance.getIndex().getIndicies();

        int bufferLength = inclusionPath == null ? 0 :
                inclusionPath.length() + 1;
        bufferLength += regionName.length() + 1 + 2; // index probably < 100
        bufferLength += regionIndicies.length * (1 + 2);

        StringBuffer path = new StringBuffer(bufferLength);
        if (inclusionPath != null) {
            path.append(inclusionPath).append('.');
        }

        // Add the name of the enclosing region to the path followed by the
        // index of this path within that region.
        path.append(regionName);
        for (int i = 0; i < regionIndicies.length; i++) {
            path.append(".");
            path.append(regionIndicies[i]);
        }
        path.append('@').append(index);

        // Get the String out of the StringBuffer.
        return path.toString();
    }

    /**
     * Get the inclusion path of this page.
     *
     * @return The inclusion path of this page.
     */
    public String getInclusionPath() {
        return getDeviceLayoutContext().getInclusionPath();
    }

    /**
     * Make a short string which uniquely identifies an inclusion.
     * <p/>
     * The string consists of the including pages short inclusion path, the
     * instance id and n dimensional index of the region and the index of
     * the inclusion within the region.
     * </p>
     *
     * @param shortInclusionPath The path to the inclusion
     * @param regionInstance     The region instance.
     * @param index              The index of the inclusion within the region.
     * @return The inclusion identity
     */
    private String makeShortInclusionPath(
            String shortInclusionPath,
            RegionInstance regionInstance,
            int index) {

        int formatInstance = regionInstance.getFormat().getInstance();
        int[] regionIndicies = regionInstance.getIndex().getIndicies();

        StringBuffer path = new StringBuffer();

        if (shortInclusionPath != null) {
            path.append(shortInclusionPath).append('-');
        }

        // Add the instance id of the enclosing region to the path followed by
        // the index of this path within that region.
        path.append(formatInstance);
        for (int i = 0; i < regionIndicies.length; i++) {
            path.append("x"); // ugly but valid in CSS name
            path.append(regionIndicies[i]);
        }
        path.append('-').append(index);

        return path.toString();
    }

    /**
     * Get the inclusion path of this page.
     *
     * @return The inclusion path of this page.
     */
    public String getShortInclusionPath() {
        return getDeviceLayoutContext().getShortInclusionPath();
    }

    /**
     * Push the current CanvasType onto the stack.
     *
     * @param canvasType The type of canvas e.g. "portal".
     */
    public void pushCanvasType(String canvasType) {
        if (canvasTypeStack == null) {
            canvasTypeStack = new Stack();
        }
        canvasTypeStack.push(canvasType);
    }

    /**
     * Retrieve and remove the current CanvasType from the stack.
     *
     * @return The CanvasType.
     */
    public String popCanvasType() {
        return (String) canvasTypeStack.pop();
    }

    /**
     * Retrieve but do not remove the current CanvasType from the stack.
     * @return  The CanvasType.
     */
    public String peekCanvasType() {
        String value = null;
        if (canvasTypeStack != null) {
            if (!canvasTypeStack.isEmpty()) {
                value = (String) canvasTypeStack.peek();
            }
        }
        return value;
    }


    /**
     * Record whether the canvas has seen any of it's children
     *
     * @param seen boolean flag
     */
    public void setCanvasHasChildren(boolean seen) {
        this.canvasHasChildren = seen;
    }

    /**
     * Return whether the canvas has seen any children
     *
     * @return boolean flag
     */
    public boolean isCanvasFirstChild() {
        return canvasHasChildren ? false : true;
    }

    /**
     * Return true if the current canvas is the root canvas.
     *
     * @return True if the current canvas is the root canvas and false otherwise.
     */
    public boolean isRootCanvas() {
        return "main".equals(peekCanvasType())
                || "portal".equals(peekCanvasType());
    }

    /**
     * Set the brand name for the current context.  If this is an included page
     * and the brand name is null then set the brand name to the brand name
     * of the including page.
     *
     * @param brandName The name of the brand for this page
     * @param inclusion true if this is an included page
     */
    public void setBrandName(String brandName, boolean inclusion) {
        if (brandNameStack == null) {
            brandNameStack = new Stack();
        }

        if (brandName != null) {
            brandName = brandName.trim();
            if ("".equals(brandName)) {
                brandNameStack.push(null);
            } else if (brandName.endsWith("/")) {
                brandNameStack.push(brandName);
            } else {
                brandNameStack.push(brandName + "/");
            }
        } else if (inclusion) {
                brandNameStack.push(getCurrentBrandName());
            }

    }    

    public String getCurrentBrandName() {
        String value = null;
        if (brandNameStack != null) {
            if (!brandNameStack.isEmpty()) {
                value = (String) brandNameStack.peek();
            }
        }
        return value;
    }

    /**
     * Create the branded version of a name.
     *
     * @param name The name to brand
     * @return The branded name
     */
    public String getBrandedName(String name) {
        return getBrandedName(name, getCurrentBrandName());
    }

    /**
     * Create the branded version of a name.
     *
     * @param name  The name to brand.  May not be null.
     * @param brand The name of the brand
     * @return The branded name
     */
    public static String getBrandedName(String name, String brand) {

        String brandedName;

        if ((name == null) ||
                "".equals(name)) {
            logger.warn("asset-null-when-branding");
            brandedName = null;
        } else if (RemoteRepositoryHelper.isRemoteName(name)) {
            // Remote names cannot be branded.
            brandedName = name;
        } else if (name.charAt(0) == '^') {
            brandedName = name.substring(1);
        } else if (brand != null) {
            if (brand.endsWith("/") && name.startsWith("/")) {
                // Double slashes are no good for accessing the repository and
                // finding a match.  XML works as the file handling in Java
                // ignores them (at least currently!) but it seriously upsets
                // the JDBC retrieval.
                brandedName = brand + name.substring(1);
            } else {
                brandedName = brand + name;
            }
        } else {
            brandedName = name;
        }
        return brandedName;
    }

    /**
     * Get the MarinerRequestContext.
     *
     * @return The MarinerRequestContext.
     */
    public MarinerRequestContext getRequestContext() {
        MarinerRequestContext value = null;
        if (requestContextStack != null) {
            if (!requestContextStack.isEmpty()) {
                value = (MarinerRequestContext) requestContextStack.peek();
            }
        }
        return value;
    }

    /**
     * Push a new {@link MarinerRequestContext} on to the stack.
     *
     * @param requestContext The new MarinerRequestContext.
     */
    public void pushRequestContext(MarinerRequestContext requestContext) {
        if (requestContextStack == null) {
            requestContextStack = new Stack();
        }
       requestContextStack.push(requestContext);
    }

    /**
     * Get the MarinerRequestContext and remove it from the stack.
     *
     * @return The MarinerRequestContext.
     */
    public MarinerRequestContext popRequestContext() {
        return (MarinerRequestContext)requestContextStack.pop();
    }


    /**
     * Perform environment specific initialisation of the response.
     * <p/>
     * This must only be called by the top level page.
     * </p>
     *
     * @throws MarinerContextException If the response writer has already been
     * accessed.
     */
    public void initialiseResponse()
            throws MarinerContextException {

        getEnvironmentContext().initialiseResponse();
    }

    /**
     * Perform environment specific redirection.
     * <p/>
     * This must only be called by the top level page.
     * </p>
     *
     * @param url The MarinerURL to which the client should be redirected.
     * @throws IOException If redirection fails
     */
    public void sendRedirect(MarinerURL url)
            throws IOException {

        getEnvironmentContext().sendRedirect(url);
    }

    /**
     * Get the current session context.
     *
     * @return The current session context.
     */
    public MarinerSessionContext getSessionContext() {
        return getEnvironmentContext().getCurrentSessionContext();
    }


    /**
     * Set the current fragment.
     *
     * @param fragment The current fragment.
     */
    public void setCurrentFragment(Fragment fragment) {
        getDeviceLayoutContext().setCurrentFragment(fragment);
    }

    /**
     * Get the current fragment.
     *
     * @return The current fragment.
     */
    public Fragment getCurrentFragment() {
        return getDeviceLayoutContext().getCurrentFragment();
    }

    /**
     * Get the current <code>FragmentationState</code>.
     *
     * @return The current <code>FragmentationState</code>.
     */
    public FragmentationState getFragmentationState() {
        return fragmentationState;
    }

    /**
     * Get the index associated with the current <code>FragmentationState</code>.
     *
     * @return The index associated with the current
     *         <code>FragmentationState</code> state.
     */
    public int getFragmentationIndex() {
        return fragmentationIndex;
    }

    /**
     * Resets the fragmentation index to -1 and the state to null.
     */
    public void resetFragmentationIndex() {
        fragmentationIndex = -1;
        fragmentationState = null;
    }

    /**
     * Get the FormatScope initializing it first if necessary.
     *
     * @return An initialized FormatScope.
     */
    private FormatScope getFormatScope() {
        final Fragment currentFragment =
            getDeviceLayoutContext().getCurrentFragment();
        if (currentFragment != null) {
            final FormatScope fragmentScope = currentFragment.getFormatScope();
            if (fragmentScope != null) {
                return fragmentScope;
            }
        }
        return getDeviceLayout().getFormatScope();
    }

    /**
     * Update the FragmentationState based on the request parameters.
     *
     * @throws LayoutException If the layout does not contain fragments
     */
    public void updateFragmentationState()
            throws LayoutException {

        // The root page url is based off of the current request url.
        rootPageURL = getRequestURL(true);
        rootPageURL.removeParameter(URLConstants.FRAGMENTATION_PARAMETER);

        // The root page URL must not be modified.
        if (requestURL != null) {
            requestURL.makeReadOnly();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Root page URL is " + rootPageURL);
        }

        // Get the enclosing region context.
        String inclusionPath = null;
        RegionInstance includingRegionInstance = null;
        if (inclusionState) {
            includingRegionInstance
                    = getEnclosingRegionInstance();
        }

        // Only the top level page needs to change the fragmentation.
        if (!inclusionState) {
            String value = requestURL.getParameterValue
                    (URLConstants.FRAGMENTATION_PARAMETER);
            if (value == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("No fragmentation state.");
                }
            } else {
                fragmentationState =
                        pageGenerationCache.getFragmentationState(value);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Enclosing page has updated fragmentation");
            }
            fragmentationState = getFragmentationState();
        }

        // Get the index of the fragmentation state.
        if (fragmentationState == null) {
            fragmentationIndex = -1;
        } else {
            fragmentationIndex
                    =
                    pageGenerationCache.getFragmentationIndex(
                            fragmentationState);
        }

        if (includingRegionInstance != null) {
            initialiseDeviceLayoutContext(includingRegionInstance);
        }

        initialiseCurrentFragment();
    }

    /**
     * Initialise the current device layout context.
     *
     * @param includingRegionInstance The including region instance.
     */
    public void initialiseDeviceLayoutContext(
            RegionInstance includingRegionInstance) {

        String inclusionPath;
        String shortInclusionPath;
            // Get the index of this page within the enclosing region context.
            int index = includingRegionInstance.getRegionContentCount();

            // Get the including page's inclusion path, if any.
        inclusionPath = getIncludingInclusionPath();

            // Make an inclusion path for the current page.
            inclusionPath = makeInclusionPath(inclusionPath,
                                              includingRegionInstance,
                                              index);

        shortInclusionPath = getIncludingShortInclusionPath();

            shortInclusionPath = makeShortInclusionPath(shortInclusionPath,
                                                        includingRegionInstance,
                                                        index);

        getDeviceLayoutContext().setInclusionPath(inclusionPath);

        getDeviceLayoutContext().setShortInclusionPath(shortInclusionPath);
    }

    /**
     * Get the inclusion path from the including device layout context.
     *
     * @return The inclusion path from the including device layout context, or
     * null.
     */
    private String getIncludingInclusionPath() {
        DeviceLayoutContext including = getIncludingDeviceLayoutContext();
        if (including == null) {
            return null;
            } else {
            return including.getInclusionPath();
            }
            }

    /**
     * Get the including device layout context.
     *
     * @return The including device layout context.
     */
    private DeviceLayoutContext getIncludingDeviceLayoutContext() {
        DeviceLayoutContext current = getDeviceLayoutContext();
        DeviceLayoutContext including =
                current.getIncludingDeviceLayoutContext();
        return including;
        }

    /**
     * Get the short inclusion path from the including device layout context.
     *
     * @return The short inclusion path from the including device layout
     * context, or null.
     */
    private String getIncludingShortInclusionPath() {
        DeviceLayoutContext including = getIncludingDeviceLayoutContext();
        if (including == null) {
            return null;
        } else {
            return including.getShortInclusionPath();
        }
        }

    /**
     * Initialise the current fragment in the current device layout context.
     *
     * <p>This requires that the inclusion path has been initialised in the
     * current device layout context.</p>
     *
     * @throws LayoutException
     * @see #initialiseDeviceLayoutContext(RegionInstance)
     */
    public void initialiseCurrentFragment()
            throws LayoutException {

        String inclusionPath = getInclusionPath();

        // Get the page identifier, this is the String which we use to
        // check whether the page being included in a region is the same as the
        // page which was in the region when the fragmentation occurred. For
        // now we use the layout name as we know that if the layouts are the
        // same name then the fragmentation name will still be valid.
        String pageIdentifier = getDeviceLayout().getName();

        if (logger.isDebugEnabled()) {
            if (requestURL != null) {
                logger.debug("Updating fragment for page identifier "
                             + pageIdentifier
                             + " with parameters "
                             + requestURL.getQuery());

                logger.debug("Fragmentation state for " + inclusionPath
                             + "(" + pageIdentifier + ") "
                             + " parameters " + requestURL.getQuery()
                             + " is " + fragmentationState);
            } else {
                logger.debug("Updating fragment for page identifier "
                             + pageIdentifier);

                logger.debug("Fragmentation state for " + inclusionPath
                             + "(" + pageIdentifier + ") "
                             + " is " + fragmentationState);
            }
        }

        // Get the fragment for the current region and page identifier.
        String fragmentName = null;
        if (fragmentationState != null) {
            fragmentName = fragmentationState.getFragmentName(inclusionPath,
                                                              pageIdentifier);
        }

        if (fragmentName == null) {
            fragmentName = getDeviceLayout().getDefaultFragmentName();
            if (logger.isDebugEnabled()) {
                logger.debug("Using default fragment of "
                             + fragmentName + " instead");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Fragment name is " + fragmentName);

            // If the fragment name was set then find the fragment in the
            // device layout and store it in the context. If the fragment
            // was set but could not be found then throw an exception.

        }

        Fragment fragment = null;
        if (fragmentName != null) {
            fragment =
                    (Fragment) getDeviceLayout().retrieveFormat(fragmentName,
                                                                FormatType.FRAGMENT);
            if (fragment == null) {
                throw new LayoutException(EXCEPTION_LOCALIZER.format(
                        "fragment-missing", fragmentName));
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Current fragment in region " + inclusionPath
                         + " is " + fragmentName);
        }

        setCurrentFragment(fragment);
    }

    /**
     * Set the current form fragment.
     *
     * @param fragment The current fragment.
     */
    private void setCurrentFormFragment(FormFragment fragment) {
        getDeviceLayoutContext().setCurrentFormFragment(fragment);
    }

    /**
     * Get the current form fragment.
     *
     * @return The current fragment.
     */
    public FormFragment getCurrentFormFragment() {
        return getDeviceLayoutContext().getCurrentFormFragment();
    }

    /**
     * Get whether or not the current fragment is being reset.
     *
     * @return <CODE>true</CODE> if the current fragment is being reset, otherwise <CODE>false</CODE>
     */
    public boolean getFormFragmentResetState() {
        return formFragmentResetState;
    }

    /**
     * Get whether or not the current fragment is being reset.
     */
    public void clearFormFragmentResetState() {
        formFragmentResetState = false;
    }


    /**
     * Get the index associated with the current <code>FormFragmentationState</code>.
     *
     * @param formName The name of the fragmented form
     * @return The index associated with the current
     *         <code>FormFragmentationState</code> state.
     */
    public int getFormFragmentationIndex(String formName) {
        FragmentationState state = (FragmentationState) formFragmentationStates.get(
                formName);
        if (state == null) {
            return -1;
        }

        return pageGenerationCache.getFormFragmentationIndex(formName, state);
    }

    /**
     * Update the FormFragmentationState based on the request parameters.
     *
     * @param form The fragmented form
     * @throws LayoutException If an error occurs while updating the
     * fragmentation state
     */
    public void updateFormFragmentationState(Form form) throws LayoutException {

        String formFragmentName = updateFormFragmentationState(form.getName());

        if (formFragmentName == null) {
            FormFragment defFrag = form.getDefaultFormFragment();
            if (defFrag == null) {
                formFragmentName = null;
            } else {
                formFragmentName = defFrag.getName();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Using default Form fragment of " +
                        formFragmentName + " instead");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Form Fragment name is " + formFragmentName);

            // If the fragment name was set then find the fragment in the
            // device layout and store it in the context. If the fragment
            // was set but could not be found then throw an exception.
        }

        FormFragment fragment = null;
        if (formFragmentName != null) {
            fragment = (FormFragment) getDeviceLayout().
                    retrieveFormat(formFragmentName,
                                   FormatType.FORM_FRAGMENT);
            if (fragment == null) {
                // The device layout format scope only contains top level
                // fragmented forms and fragments. Perhaps, the form fragment
                // we are looking for is contained in a fragment inside the
                // device layout format.
                fragment = (FormFragment) getCurrentFragment().retrieveFormat(
                        formFragmentName, FormatType.FORM_FRAGMENT);
                if (fragment == null) {
                    throw new LayoutException(EXCEPTION_LOCALIZER.format(
                            "form-fragment-missing", formFragmentName));
                }
            }
        }

        setCurrentFormFragment(fragment);
    }

    /**
     * Update the FormFragmentationState based on the request parameters, and
     * return the current form fragment name.
     *
     * @param formName  of the form whose fragmentation state should be updated
     * @return String current form fragment name. May be null.
     */
    public String updateFormFragmentationState(String formName) {

        FragmentationState state = null;
        getPageGenerationCache().createFormFragmentationStates(formName);

        MarinerURL localRequestURL = getRequestURL(false);
        String value = localRequestURL.getParameterValue("vffrag");

        if (value == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No form fragmentation state.");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting form fragmentation states for value " +
                        value);
            }
            state = pageGenerationCache.getFormFragmentationState(formName,
                    value);
            formFragmentationStates.put(formName, state);
        }

        // Get the page identifier, this is the String which we use to
        // check whether the page being included in a region is the same as the
        // page which was in the region when the fragmentation occurred. For
        // now we use the layout name as we know that if the layouts are the
        // same name then the fragmentation name will still be valid.
        String pageIdentifier = getDeviceLayout().getName();

        if (logger.isDebugEnabled()) {
            logger.debug("Updating form fragment for page identifier "
                         + pageIdentifier + " with parameters "
                         + localRequestURL.getQuery());
        }

        String inclusionPath = getInclusionPath();
        if (logger.isDebugEnabled()) {
            logger.debug("FormFragmentation state for " + inclusionPath
                         + "(" + pageIdentifier + ") "
                         + " parameters " +
                         localRequestURL.getQuery() +
                         " is " +
                         state);

            // Get the fragment for the current region and page identifier.

        }
        String formFragmentName = null;
        if (state != null) {
            formFragmentName = state.getFragmentName(inclusionPath, pageIdentifier);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Current form fragment in region " +
                    getInclusionPath() + " is " + formFragmentName);
        }

        String isReset = getRequestURL(false).getParameterValue("vreset");

        if (logger.isDebugEnabled()) {
            logger.debug("Reset state is " + isReset);
        }

        if (isReset != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Setting form fragmentation reset state.");
            }
            formFragmentResetState = true;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Clearing form fragmentation reset state.");
            }
            formFragmentResetState = false;
        }

        return formFragmentName;
    }

    /**
     * Provide access to the manager which keeps track of all the current
     * fragmented form data in this session.
     *
     * @return FormDataManager which keeps track of all the current fragmented
     * form data in this sessions
     */
    public FormDataManager getFormDataManager() {
        return getSessionContext().getFormDataManager();
    }

    /**
     * Update the form parameters.
     * <p/>
     * This method is responsible for making sure that the parameters which
     * are made available to the submit page are correctly set.
     * </p>
     */
    private void updateFormParameters() {
        String value;

        String formSpecifier = requestURL.getParameterValue(
                URLConstants.FORM_PARAMETER);
        if (formSpecifier == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No form.");
            }
            return;
        }

        FormDescriptor formDescriptor = getFormDataManager().
                getSessionFormData(formSpecifier).getFormDescriptor();

        if (formDescriptor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No form descriptor for " + formSpecifier);
            }
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Form descriptor is " + formDescriptor);
        }

        // Return if we are still being fragmented
        value = requestURL.getParameterValue
                (URLConstants.FORM_FRAGMENTATION_PARAMETER);
        if (value != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("No form fragmentation parameter");
            }
            return;
        }

        // Return if we have a next form fragment value
        value = requestURL.getParameterValue(URLConstants.NEXT_FORM_FRAGMENT);
        if (value != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Selecting next form fragment");
            }
            return;
        }

        // Return if we have a previous form fragment value
        value = requestURL.getParameterValue(URLConstants.PREV_FORM_FRAGMENT);
        if (value != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Selecting previous form fragment");
            }
            return;
        }

        // Return if we have a reset form fragment value
        value = requestURL.getParameterValue(URLConstants.RESET_FORM_FRAGMENT);
        if (value != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Resetting form fragment");
            }
            return;
        }

        List fields = formDescriptor.getFields();
        int count = fields.size();

        // This must be a normal submit so extract all the values for the form
        // and put them in the request URL. Look in the session first to see
        // whether it contains any values from previously fragmented forms.
        MarinerURL url = getFormDataManager().
                getSessionFormData(formSpecifier).getAsMarinerURL();

        if (url == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("URL not in session, assuming that this form " +
                        "was never fragmented");
            }

            // Update the form parameters. This involves iterating through all the
            // fields removing any protocol specific parameters from the request url
            // and adding them to the pure url. Currently the
            for (int i = 0; i < count; i += 1) {
                FieldDescriptor field = (FieldDescriptor) fields.get(i);
                FieldType type = field.getType();
                FieldHandler handler = type.getFieldHandler(protocol);
                if (logger.isDebugEnabled()) {
                    logger.debug("Updating parameter(s) for " + field);
                }
                handler.updateParameterValue(this, field, pureRequestURL,
                                             pureRequestURL);
            }

            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Session data is " + url.getExternalForm());
        }

        // Add the parameters from the session into the pure request url.
        Map parameterMap = pureRequestURL.getParameterMap();
        for (int i = 0; i < count; i += 1) {
            FieldDescriptor field = (FieldDescriptor) fields.get(i);
            if (logger.isDebugEnabled()) {
                logger.debug("Copying parameter(s) for " + field);
            }
            String name = field.getName();
            if (name != null) {
                String[] sessionValues = url.getParameterValues(name);
                // Don't overwrite values from the current request that haven't yet been stored in
                // the session. so:
                // - don't add empty parameters
                // - don't add parameters if request already contains parameter of given name
                if (sessionValues != null && !parameterMap.containsKey(name)) {
                    pureRequestURL.setParameterValues(name, sessionValues);
                }
            }
        }
    }

    /**
     * IMDAPI: Push the specified element onto the top of the stack.
     *
     * @param element The element to push onto the stack
     */
    public void pushIMDAPIElement(AbstractIMDAPIElement element) {
        imdapiElementStack.push(element);
        if (logger.isDebugEnabled()) {
            logger.debug("IMDAPI stack: pushed " + element);
        }
    }

    /**
     * IMDAPI: Pop the IMDAPI element from the top of the stack.
     *
     * @return The IMDAPI element from the top of the element stack
     */
    public AbstractIMDAPIElement popIMDAPIElement() {
        AbstractIMDAPIElement e = (AbstractIMDAPIElement)
                imdapiElementStack.pop();

        /*
        if(!(e instanceof expectedElement)) {
          throw new IllegalStateException ("IMDAPIElement STACK:"
                                         + " Expected " + expectedElement
                                         + " popped " + e);
        }
        */
        return e;
    }

    /**
     * IMDAPI: Return the IMDAPI from the top of the stack without
     * removing it from the stack.
     *
     * @return The IMDAPI element from the top of the element stack
     */
    public AbstractIMDAPIElement getIMDAPIElement() {
        AbstractIMDAPIElement e = (AbstractIMDAPIElement)
                imdapiElementStack.peek();

        /*
        if(!(e instanceof expectedElement)) {
          throw new IllegalStateException ("IMDAPIElement STACK:"
                                         + " Expected " + expectedElement
                                         + " got " + e);
        }
        */
        return e;
    }

    /**
     * IAPI: Push the specified IAPIElement onto the top of the stack.
     *
     * @param element The IAPIElement to push onto the stack.
     */
    public void pushIAPIElement(IAPIElement element) {
        iapiElementStack.push(element);
        if (logger.isDebugEnabled()) {
            logger.debug("IAPI stack: pushed " + element);
        }
    }

    /**
     * IAPI: Pop the IAPIElement from the top of the stack.
     *
     * @return The IAPIElement from the top of the stack
     */
    public IAPIElement popIAPIElement() {
        IAPIElement element = (IAPIElement) iapiElementStack.pop();
        if (logger.isDebugEnabled()) {
            logger.debug("IAPI stack: popped " + element);
        }
        return element;
    }

    /**
     * IAPI: Return the IAPIElement from the top of the stack without
     * removing it from the stack.
     *
     * @return The IAPI element from the top of the stack
     */
    public IAPIElement peekIAPIElement() {
        IAPIElement element = (IAPIElement) iapiElementStack.peek();
        if (logger.isDebugEnabled()) {
            logger.debug("IAPI stack: peeked " + element);
        }
        return element;
    }

    /**
     * MCSI: Push the specified MCSIElement onto the top of the stack.
     *
     * @param element The MCSIElement to push onto the stack.
     */
    public void pushMCSIElement(PAPIElement element) {
        mcsiElementStack.push(element);
        if (logger.isDebugEnabled()) {
            logger.debug("MCSI stack: pushed " + element);
        }
    }

    /**
     * MCSI: Pop the MCSIElement from the top of the stack.
     *
     * @return The MCSIElement from the top of the stack
     */
    public PAPIElement popMCSIElement() {
        PAPIElement element = (PAPIElement) mcsiElementStack.pop();
        if (logger.isDebugEnabled()) {
            logger.debug("MCSI stack: popped " + element);
        }
        return element;
    }

    /**
     * MCSI: Return the MCSIElement from the top of the stack without
     * removing it from the stack.
     *
     * @return The MCSI element from the top of the stack
     */
    public PAPIElement peekMCSIElement() {
        PAPIElement element = (PAPIElement) mcsiElementStack.peek();
        if (logger.isDebugEnabled()) {
            logger.debug("MCSI stack: peeked " + element);
        }
        return element;
    }

    /**
     * PAPI: Get the current pane from the top of the stack.
     *
     * @return The Pane which is on the top of the stack.
     * @todo later refactor this to getCurrentPaneContext for consistency
     */
    public Pane getCurrentPane() {
        Pane pane = null;
        AbstractPaneInstance instance;

        instance = (AbstractPaneInstance)
                findContainerInstance(AbstractPaneInstance.class);

        if (logger.isDebugEnabled()) {
            logger.debug("PANE INSTANCE STACK: Enclosing " + instance);
        }

        if (instance != null) {
            pane = (Pane) instance.getFormat();
        }
        return pane;
    }

    // javadoc inherited
    public OutputBuffer resolvePaneOutputBuffer(
            FormatReference paneFormatReference) {
        OutputBuffer buffer = null;

        if (paneFormatReference != null) {
            Pane pane = getPane(paneFormatReference.getStem());

            if (pane != null) {
                AbstractPaneInstance paneInstance =
                        (AbstractPaneInstance) getFormatInstance(pane,
                                                                 paneFormatReference.getIndex());

                // This will create the pane's output buffer if one doesn't
                // already exist
                buffer = paneInstance.getCurrentBuffer();
            }
        } else {
            // A null format reference indicates that the current pane's output
            // buffer is required
            buffer = getCurrentOutputBuffer();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("OutputBuffer for " + paneFormatReference + " is " +
                         buffer);
        }

        return buffer;
    }

    /**
     * PAPI: Push the specified PAPIElement onto the top of the stack.
     *
     * @param element The PAPI element to place on the stack
     */
    public void pushElement(PAPIElement element) {
        // We have seen a child element
        setCanvasHasChildren(true);

        papiElementStack.push(element);
        if (logger.isDebugEnabled()) {
            logger.debug("PAPI ELEMENT STACK: Pushed " + element);
        }
    }

    /**
     * PAPI: Pop the current papiElement from the top of the stack.
     *
     * @param expectedElement The PAPIElement which is expected to be
     *                        popped. If this is not null and not equal to the papiElement on the top
     *                        of the stack then throw an IllegalStateException.
     */
    public void popElement(PAPIElement expectedElement) {

        PAPIElement element = (PAPIElement) papiElementStack.pop();
        if (expectedElement != null && expectedElement != element) {
            throw new IllegalStateException("PAPIELEMENT STACK:"
                                            + " Expected " + expectedElement
                                            + " popped " + element);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("PAPI ELEMENT STACK: Popped " + element);
        }
    }

    /**
     * PAPI: Get the current PAPIElement from the top of the stack.
     *
     * @return The PAPIElement which is on the top of the stack.
     */
    public PAPIElement getCurrentElement() {
        PAPIElement element;

        if (papiElementStack == null || papiElementStack.isEmpty()) {
            element = null;
        } else {
            element = (PAPIElement) papiElementStack.peek();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("PAPI ELEMENT STACK: Current " + element);
        }

        return element;
    }

    /**
     * Get the specified format, by name and namespace.
     *
     * @param name      the name of the format to retrieve
     * @param namespace the namespace of format to retreive
     * @return the format.
     */
    public Format getFormat(String name, FormatNamespace namespace) {
        Format format = null;
        FormatScope formatScope = getFormatScope();
        if (formatScope != null) {
            format = formatScope.retrieveFormat(name, namespace);
        }

        return format;
    }

    /**
     * PAPI: Get the specified pane.
     *
     * @param paneName The name of the pane.
     * @return The Pane which was found or null
     */
    public Pane getPane(String paneName) {
        // Do not check for '.' characters in pane names anymore as they are
        // now valid. So are '-' and '_'
        return (Pane) getFormat(paneName, FormatNamespace.PANE);
    }

    /**
     * PAPI: Get the specified form.
     *
     * @param formName The name of the form.
     * @return The Form which was found.
     */
    public Form getForm(String formName) {
        return (Form) getFormat(formName, FormatNamespace.FORM);
    }

    /**
     * PAPI: Get the specified region.
     *
     * @param regionName The name of the region.
     * @return The Region which was found.
     */
    public Region getRegion(String regionName) {
        return (Region) getFormat(regionName, FormatNamespace.REGION);
    }

    /**
     * PAPI: Get the specified segment.
     *
     * @param segmentName The name of the segment.
     * @return The Segment which was found.
     */
    public Segment getSegment(String segmentName) {
        return (Segment) getFormat(segmentName, FormatNamespace.SEGMENT);
    }

    // Javadoc inherited.
    public void pushOutputBuffer(OutputBuffer outputBuffer) {
        outputBufferStack.push(outputBuffer);
        if (logger.isDebugEnabled()) {
            logger.debug("OUTPUT BUFFER STACK: Pushed " + outputBuffer);
        }
    }

    // Javadoc inherited.
    public void popOutputBuffer(OutputBuffer expectedOutputBuffer) {

        OutputBuffer outputBuffer = (OutputBuffer) outputBufferStack.pop();
        if (expectedOutputBuffer != null &&
                expectedOutputBuffer != outputBuffer) {
            throw new IllegalStateException("OUTPUT BUFFER STACK:"
                                            + " Expected " +
                                            expectedOutputBuffer
                                            + " popped " + outputBuffer);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("OUTPUT BUFFER STACK: Popped " + outputBuffer);
        }
    }

    // Javadoc inherited.
    public OutputBuffer getCurrentOutputBuffer() {

        OutputBuffer currentOutputBuffer;
        OutputBuffer topOutputBuffer;

        if (outputBufferStack == null || outputBufferStack.isEmpty()) {
            topOutputBuffer = null;
            currentOutputBuffer = null;
        } else {
            topOutputBuffer = (OutputBuffer) outputBufferStack.peek();

            // The OutputBuffer on the stack could be a simple one, or a
            // compound one. If it is a compound one then we need to return
            // its current buffer.
            currentOutputBuffer = topOutputBuffer.getCurrentBuffer();
        }

        if (logger.isDebugEnabled()) {
            if (currentOutputBuffer == topOutputBuffer) {
                logger.debug(
                        "OUTPUT BUFFER STACK: Current " + currentOutputBuffer);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("OUTPUT BUFFER STACK: Top " + topOutputBuffer
                                 + " Current " + currentOutputBuffer);
                }
            }
        }

        return currentOutputBuffer;
    }

    /**
     * Get the device that is being written to
     *
     * @return The current output device
     */
    public InternalDevice getDevice() {
        return getEnvironmentContext().getSessionDevice();
    }


    /**
     * Get the name of the current output device
     *
     * @return The name of the output device
     */
    public String getDeviceName() {
        return getDevice().getName();
    }

    /**
     * Return the value of a policy for the current device
     *
     * @param policyName The name of the policy to retrieve
     * @return The value of the policy
     */
    public String getDevicePolicyValue(String policyName) {
        return getDevice().getPolicyValue(policyName);
    }

    /**
     * Return the appropriate Transcoding rule for the device rendering
     * parameters.
     *
     * @param renderMode   The rendering mode COLOR or MONOCHROME.
     * @param encodingType The image encoding type.
     * @param pixelDepth   The device pixel depth.
     * @return The rule name or null.
     */
    public static String getTranscodingRule(
            int renderMode, int encodingType,
            int pixelDepth) {

        StringBuffer rule = new StringBuffer();
        switch (renderMode) {
            case ImageAsset.COLOR:
                rule.append('c');
                switch (encodingType) {
                    case ImageAsset.JPEG:
                        rule.append("jpeg");
                        //
                        // All JPEG images should be rendered as 24 bit.
                        //
                        pixelDepth = 24;
                        break;
                    case ImageAsset.PNG:
                        rule.append("png");
                        //
                        // All PNG images should be rendered as 24 bit on devices that
                        // support more than 8 bit.
                        //
                        if (pixelDepth > 8) {
                            pixelDepth = 24;
                        } else {
                            pixelDepth = 8;
                        }
                        break;
                    case ImageAsset.GIF:
                        rule.append("gif");
                        //
                        // All GIF images should be rendered as 8 bit.
                        //
                        pixelDepth = 8;
                        break;
                    case ImageAsset.WBMP:
                        // shouldn't depends on rendermode for WBMP so
                        // replace prefix from color to gray in order to return correct rule
                        // policy name (gwbmp1rule)
                        rule.replace(0,1,"g");
                        rule.append("wbmp");
                        //
                        // All wbmp images should be rendered as 1 bit.
                        //
                        pixelDepth = 1;
                        break;                    
                    default:
                        //
                        // We can not get a valid rule name so lets return.
                        //
                        return null;
                }
                break;
            case ImageAsset.MONOCHROME:
                rule.append('g');
                switch (encodingType) {
                    case ImageAsset.JPEG:
                        rule.append("jpeg");
                        //
                        // All JPEG images should be rendered as 8 bit.
                        //
                        pixelDepth = 8;
                        break;
                    case ImageAsset.PNG:
                        rule.append("png");
                        //
                        // All PNG images should be rendered as 16 bit on devices
                        // that support more than 8 bit.
                        //
                        if (pixelDepth > 8) {
                            pixelDepth = 16;
                        }
                        break;
                    case ImageAsset.GIF:
                        rule.append("gif");
                        //
                        // All GIF images should be rendered as 8 bit on devices
                        // that support more than 8 bit.
                        //
                        if (pixelDepth > 8) {
                            pixelDepth = 8;
                        }
                        break;
                    case ImageAsset.WBMP:
                        rule.append("wbmp");
                        //
                        // All wbmp images should be rendered as 1 bit.
                        //
                        pixelDepth = 1;
                        break;
                    default:
                        //
                        // We can not get a valid rule name so lets return.
                        //
                        return null;
                }
                break;
            default:
                //
                // We can not get a valid rule name so lets return.
                //
                return null;
        }
        //
        // We need to normalise some pixelDepths
        //
        switch (pixelDepth) {
            case 3:
                --pixelDepth;
                break;
            case 5:
                --pixelDepth;
                break;
            case 6:
                pixelDepth = pixelDepth - 2;
                break;
            case 7:
                pixelDepth = pixelDepth - 3;
                break;
            default:
        }
        rule.append(pixelDepth);
        rule.append("rule");
        return rule.toString();
    }

    /**
     * Return the value of a boolean policy for the current device
     *
     * @param policyName The name of the policy to retrieve
     * @return the boolean value of the policy
     */
    public boolean getBooleanDevicePolicyValue(String policyName) {
        return "true".equalsIgnoreCase(getDevicePolicyValue(policyName));

    }

    /**
     * Retrieve the Layout with the specified name for the current
     * device.
     *
     * @param name The name of the Layout.
     * @return The layout for this device
     * @throws RepositoryException If the device layout does not exist
     */
    public RuntimeDeviceLayout getDeviceLayout(String name)
        throws RepositoryException {

        PolicyReferenceResolver resolver = getPolicyReferenceResolver();

        RuntimePolicyReference reference =
            resolver.resolveUnquotedPolicyExpression(name,
                PolicyType.LAYOUT);

        SelectedVariant selected = assetResolver.selectBestVariant(
            reference, null);
        RuntimeDeviceLayout deviceLayout = null;

        if (selected != null) {
            deviceLayout = (RuntimeDeviceLayout)selected.getOldObject();
        }

        if (deviceLayout == null) {
            logger.warn("xdime-could-not-find-layout=", name);
        }

        return deviceLayout;
    }

    public SelectionContext getSelectionContext() {
        return selectionContext;
    }

    /**
     * Return the context's layout.
     * <p/>
     * <p><b>NOTE:</b> this method should not be used within an inclusion (i.e.
     * inside a region/portlet).
     * Use {@link #getDeviceLayoutContext} to get the current device layout
     * context then call {@link DeviceLayoutContext#getDeviceLayout} instead.</p>
     *
     * @return the device layout
     */
    public RuntimeDeviceLayout getDeviceLayout() {
        return getDeviceLayoutContext().getDeviceLayout();
    }

    /**
     * Get all the information needed to render a Layout.
     *
     * @return The DeviceLayoutContext.
     */
    public DeviceLayoutContext getDeviceLayoutContext() {
        DeviceLayoutContext deviceLayoutContext = null;
        if (deviceLayoutContextStack != null) {
            if (!deviceLayoutContextStack.isEmpty()) {
                deviceLayoutContext =
                        (DeviceLayoutContext) deviceLayoutContextStack.peek();
            }
        }
        return deviceLayoutContext;
    }

    /**
     * Set the context for the device layout
     *
     * @param deviceLayoutContext The layout context
     */
    public
    void pushDeviceLayoutContext(DeviceLayoutContext deviceLayoutContext) {
        if (deviceLayoutContextStack == null) {
            deviceLayoutContextStack = new Stack();
        }
        deviceLayoutContextStack.push(deviceLayoutContext);
    }

    /**
     * Retrieve and remove the DeviceLayoutContext from the stack.
     *
     * @return The DeviceLayoutContext
     */
    public DeviceLayoutContext popDeviceLayoutContext() {
        return (DeviceLayoutContext) deviceLayoutContextStack.pop();
    }


    /**
     * Return a <code>MarinerURL</code> object which represents the URL which
     * a client could have used to generate the current request.
     * <p/>
     * This URL is absolute and has undergone any URL rewriting that is
     * needed.
     * </p>
     *
     * @param clone If true then a copy of the MarinerURL is returned, otherwise
     *              a reference to the stored <code>MarinerURL</code> is returned.
     * @return A <code>MarinerURL</code> which could have been used to generate
     *         the current request.
     */
    public MarinerURL getRequestURL(boolean clone) {
        if (clone) {
            return new MarinerURL(requestURL);
        } else {
            return requestURL;
        }
    }

    /**
     * Return a <code>MarinerURL</code> object which contains only those
     * parameters which are of interest to the user and which also has had
     * any form parameters fixed up.
     *
     * @return The <code>MarinerURL</code>.
     */
    public MarinerURL getPureRequestURL() {
        return pureRequestURL;
    }

    /**
     * Return the url associated with the top level page.
     * <p/>
     * In a portal environment multiple pages are combined together into one
     * page. If a portlet is being generated out of context then it needs to
     * know what its root page URL is.
     * </p>
     *
     * @param clone If true then a copy of the MarinerURL is returned, otherwise
     *              a reference to the stored <code>MarinerURL</code> is returned.
     * @return The current MarinerURL
     */
    public MarinerURL getRootPageURL(boolean clone) {
        if (clone) {
            return new MarinerURL(rootPageURL);
        } else {
            return rootPageURL;
        }
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
     * Get the Tag ID for this page
     *
     * @return the page Tag ID
     */
    public String getPageTagId() {
        return pageTagId;
    }

    /**
     * Get the protocol that will generate this page
     *
     * @return The page generation protocol
     */
    public VolantisProtocol getProtocol() {
        return protocol;
    }

    /**
     * Get the relative request URL that caused this page to be generated
     *
     * @return The Relative Request URL
     */
    public String getRelativeRequestURL() {
        return relativeRequestURL;
    }

    /**
     * Get the reference to the controlling bean
     *
     * @return The current volantis bean
     */
    public com.volantis.mcs.runtime.Volantis getVolantisBean() {
        return volantisBean;
    }

    /**
     * Returns a number which is unique within the body of the page.
     *
     * @return A number unique within the page.
     */
    private int incrementCounter() {
        counter++;
        return counter;
    }

    /**
     * Get a number which can be used as a suffix to the current unique request
     * ID from the bean.
     *
     * @return A string which is unique within the request.
     */
    private int getUniqueId() {
        return incrementCounter();
    }

    /**
     * Put a value in the id map.
     *
     * @param id    The id, may not be null.
     * @param value The value.
     */
    public void putIdValue(String id, Object value) {
        if (id == null) {
            throw new IllegalArgumentException("null id is not valid");
        }

        if (idMap == null) {
            idMap = new HashMap();
        }

        if (value == null) {
            value = idMapNullValue;
        }

        value = idMap.put(id, value);
        if (value != null) {
            throw new IllegalArgumentException
                    ("Id " + id +
                     " is not unique, map already contains " +
                     value);
        }
    }

    /**
     * Put a value from the id map.
     *
     * @param id The id, may not be null.
     * @return The value.
     */
    public Object getIdValue(String id) {
        if (id == null) {
            throw new IllegalArgumentException("null id is not valid");
        }

        if (idMap == null) {
            return null;
        }

        Object value = idMap.get(id);
        if (value == idMapNullValue) {
            value = null;
        }

        return value;
    }

    /**
     * Get the FormatInstance for the specified format instance.
     *
     * @param format The object whose FormatInstance is required.
     * @param index  The index which identifies the instance. This should be
     *               NDimensionalIndex.ZERO_DIMENSIONS if the format does not
     *               understand format iteration.
     * @return The FormatInstance which was allocated for the specified format.
     */
    public FormatInstance getFormatInstance(
            Format format,
            NDimensionalIndex index) {

        return getDeviceLayoutContext().getFormatInstance(format, index);
    }

    /**
     * Get the URL rewriter for the current session
     *
     * @return The URL rewriter for the session
     * @see EnvironmentContext#getSessionURLRewriter
     */
    public URLRewriter getSessionURLRewriter() {
        return getEnvironmentContext().getSessionURLRewriter();
    }

    /**
     * Construct and return a url based on the device's preferred image type,
     * rules and the current transcoder plugin in use.
     *
     * @param srcUrl the url before any additional parameters.
     * @param preservedArea optional protected area of the image asset.
     * @return a new url containing any additional parameters as required.
     * @throws RepositoryException if a problem occurs building the URL
     */
    protected String constructImageURL(final String srcUrl,
                                       PreservedArea preservedArea) 
            throws RepositoryException {
        PluggableAssetTranscoder transcoder =
                getVolantisBean().getAssetTranscoder();
        String newURL = srcUrl;

        InternalDevice device = getDevice();

        TranscodingRule transcodingRule = getTranscodingRule(device);

        if (transcodingRule != null) {

            String ruleName = transcodingRule.getName();

            // We got a rule so let's generate the requirNameed URL
            String ruleValue = device.getPolicyValue(ruleName);
            StringBuffer value;
            int width;
            int maxSize;

            // Determine the width to be requested
            if (getCurrentPane() == null) {
                // If we are not in a pane, use the device width
                width = device.getPixelsX();
            } else {
                width = getProtocol().calculatePaneWidth(getCurrentPane());
            }

            // Determine the maxSize to be requested
            try {
                maxSize = device.getIntegerPolicyValue("maximagesize", -1);
                // parse out the values of image maxsize that have been entered
                // by the user
                MarinerURL murl = new MarinerURL(srcUrl);
                String[] paramValues =
                    murl.getParameterValues(transcoder.getMaxImageSizeParameter());

                int min = Integer.MAX_VALUE;
                if (null != paramValues) {
                    // always iterate over the parameter values even if there
                    // is only one as we need to set up "min"
                    if (paramValues.length > 0) {
                        // remove the parameters as they will be added back later
                        murl.removeParameter(
                            transcoder.getMaxImageSizeParameter());
                        // reassign the newURL variable with the removed
                        newURL = murl.getExternalForm();
                        for (int i=0; i< paramValues.length; i++) {
                            int val = Integer.parseInt(paramValues[i]);
                            if (val < min) {
                                min = val;
                            }
                        }
                    }
                    // if min is valid (>-1) and is less then a valid maxSize
                    // then use its value otherwise use the value in the
                    // device repository
                    if (min >= 0 && min < Integer.MAX_VALUE &&
                        maxSize >=0 && min < maxSize) {
                        maxSize = min;
                    }
                }
            } catch (NumberFormatException e) {
                logger.warn("maximagesize-not-valid",
                            new Object[]{
                                device.getPolicyValue("maximagesize")}, e);
                maxSize = -1;
            }

            try {
                AssetTranscoderContext ctx = new AssetTranscoderContext(
                        newURL,
                        ruleValue,
                        width,
                        maxSize,
                        getRequestContext(),
                        preservedArea);

                value = new StringBuffer(
                        transcoder.constructImageURL(ctx));
            } catch (TranscodingException e) {
                throw new RepositoryException(e);
            }

            // Allow the ImageURLModifier (if there is one) in the
            // ApplicationContext to modify the image url.
            ImageURLModifier ium = applicationContext.getImageURLModifier();
            if (ium != null) {
                ImageURLModifierDetails details =
                        new ImageURLModifierDetails();
                details.setMaxImageSize(device.getPolicyValue("maxmmsize"));
                details.setEncoding(transcodingRule.getEncoding());

                ium.modifyImageURL(value, details);
            }

            newURL = value.toString();
        }

        return newURL;
    }

    public static TranscodingRule getTranscodingRule(InternalDevice device) {

        int preferredImageType = device.getPreferredImageType();
        int renderMode = device.getDeprecatedRenderMode();
        int pixelDepth = device.getPixelDepth();

        TranscodingRule transcodingRule = null;

        String rule = null;
        int actualEncoding = ImageAsset.INVALID_ENCODING;

        // Lets try and get a rule for the preferred image type
        if (device.supportsImageEncoding(preferredImageType)) {
            rule = getTranscodingRule(renderMode,
                    preferredImageType, pixelDepth);
            if (rule != null) {
                actualEncoding = preferredImageType;
            }
        }

        // If a rule can not be found for the preferred image type or the device
        // does not support the preferred image type (unlikely but possible) then
        // lets try and get a rule for a supported encoding.
        if (rule == null) {
            int supportedEncodings[] = device.getSupportedImageEncodingArray();
            int index = 0;

            while ((rule == null) &&
                    (index < supportedEncodings.length)) {
                // Only process those encodings we haven't already checked
                int supportedEncoding = supportedEncodings[index];
                if (supportedEncoding != preferredImageType) {
                    rule = getTranscodingRule(renderMode,
                            supportedEncoding, pixelDepth);
                    if (rule != null) {
                        actualEncoding = preferredImageType;
                    }
                }
                index++;
            }
        }

        if (rule != null) {
            transcodingRule = new TranscodingRule(rule, actualEncoding);
        }
        return transcodingRule;
    }

    /**
     * Returns list of encodings supported by given device except prefered image type,
     * list as string with encoding separated by comma
     * @return
     */
    public String getSupportedImages() {

        InternalDevice device = getDevice();

        int preferredImageType = device.getPreferredImageType();
        StringBuffer supportedImages = new StringBuffer();

        int supportedEncodings[] = device.getSupportedImageEncodingArray();

        int index = 0;
        while (index < supportedEncodings.length) {
            int supportedEncoding = supportedEncodings[index];
            if (supportedEncoding != preferredImageType) {
               if(supportedImages.length() > 0) {
                    supportedImages.append(",");                   
               }
               supportedImages.append(ImageAsset.encodingName(supportedEncoding));
            }
            index++;
        }
        return supportedImages.toString();
    }



    /**
     * Return a <code>URL</code> based on the MarinerURL.
     * <p/>
     * If the MarinerURL is absolute then we don't have to do anything to it,
     * otherwise, if it has a document relative path we resolve it relative to
     * the current request's url and make it absolute by assuming a protocol of
     * http.
     * </p>
     *
     * @param url The MarinerURL which may be document relative, host relative,
     *            or absolute.
     * @return A URL which can be used to access the resource.
     */
    public URL getAbsoluteURL(MarinerURL url) throws MalformedURLException {
        return getAbsoluteURL(url, false);
    }

    /**
     * Return a <code>URL</code> based on the MarinerURL.
     * <p/>
     * If the MarinerURL is absolute then we don't have to do anything to it,
     * otherwise, if it has a document relative path we resolve it relative to
     * the current request's url and make it absolute by assuming a protocol of
     * http.
     * </p>
     *
     * @param url The MarinerURL which may be document relative, host relative,
     *            or absolute.
     * @param externallyVisible if true and the specified URL is relative then
     * extra care is take to make sure that the URL is externally visible (if
     * possible - i.e. localhost and 127.0.0.1 will be replaced with the
     * external host name) 
     * @return A URL which can be used to access the resource.
     */
    public URL getAbsoluteURL(MarinerURL url, boolean externallyVisible)
            throws MalformedURLException {

        // If the url is relative, i.e. has no protocol set then we need to
        // do something to make it value.
        if (url.isRelative()) {
            int pathType = url.getPathType();

            if (pathType == MarinerURL.DOCUMENT_RELATIVE_PATH) {
                // We need to resolve this relative to the current document.
                url = new MarinerURL(requestURL, url);
                //I don't know reason why query string was removed here - commented out
                //url.setQuery(null);
            }

            // Resolve it relative to the internal url.
            url = new MarinerURL(getVolantisBean().getInternalURL(), url);
            if (externallyVisible) {
                final String host = url.getHost().trim().toLowerCase();
                if (("localhost".equals(host) || "127.0.0.1".equals(host))
                        && externalHostName != null) {
                    if (url.isReadOnly()) {
                        url = new MarinerURL(url);
                    }
                    url.setHost(externalHostName);
                }
            }
        }

        try {
            return new URL(url.getExternalForm());
        } catch (MalformedURLException e) {
            logger.error("unexpected-exception", e);
            throw e;
        }
    }

    /**
     * Retrieve the URL of the specified <code>ImageAsset</code>
     *
     * @param asset The <code>ImageAsset</code> from which to retrieve the URL
     * @return An array of URL(s) for the <code>ImageAsset</code>
     */
    public String[] retrieveImageAssetURLAsString(ImageAsset asset) {

        if (asset == null) {
            logger.warn("asset-null-no-url");
            return null;
        }
        String[] url = null;
        if (asset.isSequence()) {
            ImageAsset clonedAsset = (ImageAsset) asset.clone();
            String originalvalue = clonedAsset.getValue();
            int sequenceSize = clonedAsset.getSequenceSize();
            url = new String[sequenceSize];

            for (int i = 0; i < sequenceSize; i++) {
                // alter the value of the url to take into account the sequence number
                String sequencedURL = getSequencedImageAssetURL(
                        originalvalue, i);
                clonedAsset.setValue(sequencedURL);
                url[i] = assetResolver.computeURLAsString(clonedAsset);
            }
        } else {
            url = new String[1];
            url[0] = assetResolver.computeURLAsString(asset);
        }
        return url;
    }

    /**
     * Helper method which takes an <code>ImmageAsset</code> url, and appends the sequence
     * value onto the image name.
     *
     * @param url The url containing the image name
     * @return The url with the image name postfixed with the sequence number
     */
    private String getSequencedImageAssetURL(String url, int sequenceNumber) {
        int openBrace = url.indexOf('{');
        int closeBrace = url.indexOf('}');
        String beginning = url.substring(0, openBrace);
        String end = url.substring(closeBrace + 1, url.length());
        String number = StringConvertor.valueOf(sequenceNumber);
        StringBuffer sb = new StringBuffer(beginning.length()
                                           + number.length()
                                           + end.length());
        sb.append(beginning);
        sb.append(sequenceNumber);
        sb.append(end);
        return sb.toString();
    }

    /**
     * Get the base URL for ChartImages
     *
     * @return A <code>String</code> representation of the ChartImage base URL
     */
    public String getChartImagesBase() {
        return volantisBean.getChartImagesBase();
    }

    /**
     * This is used to retrieve the actual bandwidth if available.
     * If the actual bandwidth is not available, the value is retrieved
     * from the repository entry for the device. If there is no value
     * available, then the value 0 is returned to indicate that the
     * bandwith is unknown
     * Currently the value 0 is returned
     *
     * @return int bandwith
     */
    public int getCurrentBandWidthAsBAUD() {
        return 0;
    }

    /**
     * Method to return the absolute URL to the page base
     * For example this method would return
     * "http:localhost:8080/abc/" for the URL
     * "http:localhost:8080/abc/xyz.jsp"
     *
     * @return the URL to the page base
     */
    public String getAbsolutePageBaseURL() {
        // base URL should look like "http//hostname:port"
        // this should always be present
        String baseURL = volantisBean.getBaseURL().getExternalForm();
        // page-base is location of jsp pages
        String pageBase = volantisBean.getPageBase();
        StringBuffer url = new StringBuffer();
        if (baseURL == null) {
            logger.warn("base-url-null");
            return null;
        }
        url.append(baseURL);
        if (!baseURL.endsWith("/")) {
            url.append("/");
        }
        if (pageBase != null) {
            pageBase = pageBase.trim();
            if (pageBase.length() > 0) {
                if (pageBase.startsWith("/")) {
                    pageBase = pageBase.substring(1);
                }
                url.append(pageBase);
                if (!pageBase.endsWith("/")) {
                    url.append("/");
                }
            }
        }
        return url.toString();
    }

    /**
     * Get the relationship between deviceName and the
     * current device.
     *
     * @param deviceName The device to check.
     * @return The relationship with the current device.
     */
    public int getAncestorRelationship(String deviceName) {

        InternalDevice device = getDevice();

        if (device.getName().equals(deviceName)) {
            return MarinerRequestContext.IS_DEVICE;
        }

        device = device.getFallbackDevice();

        while (device != null) {
            if (device.getName().equals(deviceName)) {
                return MarinerRequestContext.IS_ANCESTOR;
            }
            device = device.getFallbackDevice();
        }

        return MarinerRequestContext.IS_UNRELATED;
    }

    /**
     * Generate an unique ID prefix for use in FrameworkClient client-side.
     */
    protected StringBuffer getFCIDPrefixBuffer() {
        if(FCUniqueID == null){
            FCUniqueID  = new StringBuffer(FRAMEWORK_CLIENT_PREFIX+
                     getUniqueRequestID()+"_");
        }
        return FCUniqueID;
    }
    
    /**
     * Generate an unique ID for use in FrameworkClient client-side
     */
    public String generateUniqueFCID() {
        return addUniqueID(getFCIDPrefixBuffer());
    }
    
    /**
     * Generates an ID with given suffix, for use in FrameworkClient client-side.
     */
    public String generateFCID(String suffix) {
        // The dollar sign inserted between prefix and suffix
        // prevents ID clash with the generateUniqueFCID() method.
        return getFCIDPrefixBuffer() + "$" + suffix;
    }
    
    /**
     * Returns true, if specified ID is (or could be) generated Framework Client ID.
     */
    public boolean isFCID(String id) {
        return id.startsWith(FRAMEWORK_CLIENT_PREFIX);
    }
    
   
    /**
     * Generate an ID suitable for use with WMLActions.  This ID will consist
     * of the hex representation of the request number from the bean with a
     * defined prefix.  An extra number is added to the end of the string to
     * give uniqueness in the current page.
     *
     * @return An ID unique to this page.
     */
    public String generateWMLActionID() {

        if (WMLActionID == null) {
            WMLActionID = new StringBuffer(WMLACTION_ID_PREFIX +
                                           getUniqueRequestID() + "_");
        }
        return addUniqueID(WMLActionID);
    }

    /**
     * Get the request number from the bean as a hex string.
     * This is only retrieved once per page.
     *
     * @return The request number.
     */
    private String getUniqueRequestID() {
        if (requestID == null) {
            requestID =
                    volantisBean.generateUniqueRequestString();
        }
        return requestID;
    }

    /**
     * Add a number to the string to make it unique for this page.
     *
     * @return A unique ID
     */
    private String addUniqueID(StringBuffer id) {
        return id.toString() + getUniqueId();
    }

    /**
     * Get the container of markup plugins.
     *
     * @return The
     */
    public MarkupPluginContainer getMarkupPluginContainer() {
        return markupPluginContainer;
    }

    /**
     * Get the name of the character set to be used when generating the output.
     * <p/>
     * This value will also be set in the <code>charset</code> property
     * of the <code>ContentType</code> header of the HTTP response.
     * <p/>
     * NOTE: This method is not currently supported in a JSP environment.
     *
     * @return The name of the character set or null if the
     *         property has not been set.
     */
    public String getCharsetName() {
        return charsetName;
    }

    /**
     * Set the name of the character set to be used when generating the output.
     * <p/>
     * This value will also be set in the <code>charset</code> property
     * of the <code>ContentType</code> header of the HTTP response.
     * <p/>
     * Note that if this character set is not found in our configuration, and
     * thus there is no MIBEnum value, then WMLC rendering will be disabled
     * for this request.
     * <p/>
     * NOTE: This method is not currently supported in a JSP environment.
     *
     * @param charsetName The name of the charset to use.
     * @throws IllegalArgumentException if the charset was not known to
     *                                  the underlying platform.
     */
    public void setCharsetName(String charsetName)
            throws IllegalArgumentException {
        // Find the Encoding for the proposed character encoding.
        Encoding encoding = applicationContext.getEncodingManager().
                getEncoding(charsetName);
        if (encoding != null) {
            this.charsetEncoding = encoding;
        } else {
            throw new IllegalArgumentException("Charset " + charsetName +
                                               " is not supported by the Java VM");
        }

        // Save the original charset name away as well, since this may be an
        // alias for the canonical charset name which is recorded in the
        // Encoding.
        this.charsetName = charsetName;
        if (logger.isDebugEnabled()) {
            logger.debug("Set charset encoding to " + charsetEncoding);
        }
    }

    /**
     * Get the Encoding found for the charset name provided when
     * {@link #setCharsetName} was called, or null if it has not been called.
     *
     * @return the Encoding found, or null.
     */
    public Encoding getCharsetEncoding() {
        return charsetEncoding;
    }

    /**
     * Get the current {@link SelectState} instance.
     *
     * @return the current SelectState instance
     * @throws EmptyStackException if there is none
     */
    public SelectState peekSelectState() {
        return (SelectState) selectStateStack.peek();
    }

    /**
     * Pushes the given {@link SelectState} instance onto the context.
     *
     * @param selectState the SelectState instance to be added
     */
    public void pushSelectState(SelectState selectState) {
        selectStateStack.push(selectState);
    }

    /**
     * Pops and returns the current {@link SelectState} instance out of the
     * context.
     *
     * @return the current SelectState instance
     * @throws EmptyStackException if there is none
     */
    public SelectState popSelectState() {
        return (SelectState) selectStateStack.pop();
    }

    /**
     * Gets the current menu builder in this context.  If there is not one
     * currently initialised then it is created.
     *
     * @return The menu builder instance available in this context.
     */
    public MenuModelBuilder getMenuBuilder() {
        if (menuBuilder == null) {
            menuBuilder = MenuModelBuilderFactory.
                    getDefaultInstance().createBuilder();

        }
        return menuBuilder;
    }

    /**
     * Returns the current menu renderer locator object.  This provides a
     * means for code using this method to obtain which renderer to use.
     *
     * @return The menu renderer locator object
     */
    public MenuRendererSelectorLocator getRendererLocator() {
        return MenuRendererSelectorLocator.getDefaultInstance();
    }

    /**
     * Return the style property resolver associated with this context.
     *
     * @return the style property resolver associated with this context.
     */
    public StylePropertyResolver getStylePropertyResolver() {
        if (styleResolver == null) {
            styleResolver = new DefaultStylePropertyResolver(
                    getPolicyReferenceResolver(), getTranscodableUrlResolver());
        }
        // And then return it.
        return styleResolver;
    }

    /**
     * Get the stack of output buffers.
     *
     * @return The stack of output buffers.
     */
    public OutputBufferStack getOutputBufferStack() {
        return this;
    }

    /**
     * Returns the StylingEngine instance for this context, and if none exists
     * creates one. However, if this MarinerPageContext is included in another,
     * and the parent's styling engine is non null, then the parent's styling
     * engine will be used.
     *
     * @return StylingEngine instance for this context
     */
    public StylingEngine getStylingEngine() {

        if (stylingEngine == null) {

            // todo Remove this hack to obtain the styling factory lazily when this class is refactored properly.
            StylingFactory stylingFactory = getStylingFactoryLazily();

            // create styling engine
            //provide the styling engine with an InlineStyleSheetCompilerFactory
            //so that it can compile any style attributes.
            InlineStyleSheetCompilerFactory styleSheetCompilerFactory =
                    getInlineStyleSheetCompilerFactory();

            stylingEngine = stylingFactory.createStylingEngine(
                    styleSheetCompilerFactory);

            // Initialise the evaluation context.
            EvaluationContext context = stylingEngine.getEvaluationContext();
            context.setProperty(FormatReferenceFinder.class,
                                getFormatReferenceFinder());

            // Add the system wide default style sheet.
            if (volantisBean != null) {
                stylingEngine.pushStyleSheet(
                        volantisBean.getDefaultStyleSheet());
            }

            // Add the protocol specific style sheet.
            if (protocol != null &&
                    protocol.getCompiledDefaultStyleSheet() != null) {
                // merge the default style sheet and the current one
                stylingEngine.pushStyleSheet(
                        protocol.getCompiledDefaultStyleSheet());
            }
        }
        return stylingEngine;
    }

    /**
     * Get an InlineStyleSheetCompilerFactory.
     *
     * @return The InlineStyleSheetCompilerFactory.
     */
    protected InlineStyleSheetCompilerFactory
            getInlineStyleSheetCompilerFactory() {

        // This has to be here rather than created statically as otherwise
        // it causes loads of unit tests to fail.
        return new InlineStyleSheetCompilerFactory(
                StylingFunctions.getResolver());
    }

    /**
     * Get the styling engine lazily.
     * <p/>
     * <p>Moved this out in order to try and stop a test from failing.</p>
     *
     * @return The styling engine.
     */
    private StylingFactory getStylingFactoryLazily() {
        StylingFactory stylingFactory = this.stylingFactory;
        if (stylingFactory == null) {
            stylingFactory = StylingFactory.getDefaultInstance();
        }
        return stylingFactory;
    }

    /**
     * Get the component identity resolver.
     *
     * @return The component identity resolver.
     */
    public PolicyReferenceResolver getPolicyReferenceResolver() {
        return policyReferenceResolver;
    }

    public RuntimeProject getCurrentProject() {
        return getProjectStack().getCurrentProject();
    }

    /**
     * Retrieve the compiled style sheet from the theme name provided.
     *
     * @param themeName the name of the theme, may not be null.
     * @return the compiled style sheet for the theme if it exists, otherwise
     *         null.
     */
    public CompiledStyleSheet retrieveThemeStyleSheet(String themeName) {
        CompiledStyleSheet styleSheet = null;

        if (themeName == null) {
            throw new IllegalArgumentException("themeName cannot be null");
        }

        try {
            RuntimeDeviceTheme theme = retrieveBestDeviceTheme(themeName);
            if (theme != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Retrieved " + theme + " for theme " +
                            themeName + " for device " +
                            getEnvironmentContext().
                                    getSessionDevice().getName());
                }
                styleSheet = theme.getCompiledStyleSheet();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to retrieve DeviceTheme " +
                            themeName + " for device " +
                            getEnvironmentContext().
                                    getSessionDevice().getName());
                }
            }
        } catch (RepositoryException e) {
            logger.error("repository-exception", e);
        }

        return styleSheet;
    }

    private RuntimeDeviceTheme retrieveBestDeviceTheme(String themeName)
        throws RepositoryException {

        PolicyReferenceResolver resolver = getPolicyReferenceResolver();

        RuntimePolicyReference reference =
            resolver.resolveUnquotedPolicyExpression(themeName,
                PolicyType.THEME);

        SelectedVariant selected = assetResolver.selectBestVariant(
            reference, null);

        RuntimeDeviceTheme runtimeDeviceTheme = null;

        if (selected != null) {
            runtimeDeviceTheme = (RuntimeDeviceTheme)selected.getOldObject();
        }

        return runtimeDeviceTheme;
    }

    /**
     * Indicates that this canvas is an out of context inclusion.
     *
     * @return true if this is an out of context inclusion.
     */
    public boolean isOutOfContextInclusion() {
        return !(inclusionState && ("portal".equals(
                canvasTypeStack.firstElement()) ||
                "main".equals(canvasTypeStack.firstElement())));
    }

    public AssetResolver getAssetResolver() {
        return assetResolver;
    }

    /**
     * Returns the {@link TranscodableUrlResolver} that can be used to resolve
     * transcodable URLs.
     */
    public TranscodableUrlResolver getTranscodableUrlResolver() {
        final MarinerRequestContext requestContext = getRequestContext();
        if (transcodableUrlResolver == null ||
            transcodableUrlResolver.getRequestContext() != requestContext) {

            transcodableUrlResolver =
                new TranscodableUrlResolver(requestContext);
        }
        return transcodableUrlResolver;
    }

    //========================================================================
    //    Methods required to implement XFormEmulatingPageContext
    //========================================================================

    // Javadoc inherited.
    public EmulatedXFormDescriptor getEmulatedXFormDescriptor(
            String containingFormName) {
        // Get any form data stored in the session for this form.
        SessionFormData sessionFormData = getFormDataManager().
                getSessionFormDataByName(containingFormName);

        EmulatedXFormDescriptor xfd = null;
        if (sessionFormData != null) {
            FormDescriptor fd = sessionFormData.getFormDescriptor();
            if (fd instanceof EmulatedXFormDescriptor) {
                xfd = (EmulatedXFormDescriptor)fd;
            } else {
                throw new IllegalArgumentException("XDIME2 form descriptors " +
                        "must be EmulatedXFormDescriptor instances");
            }
        }
        return xfd;
    }

    public FormatReferenceFinder getFormatReferenceFinder() {
        if (formatReferenceFinder == null) {
            formatReferenceFinder = new FormatReferenceFinderImpl(this);
        }
        return formatReferenceFinder;
    }

    public void enteringXDIMECPElement() {
        xdimecpCount += 1;
    }

    public boolean insideXDIMECPElement() {
        return xdimecpCount > 0;
    }

    public void exitingXDIMECPElement() {
        xdimecpCount -= 1;
    }

    public PolicyFetcher getPolicyFetcher() {
        return policyFetcher;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public DevicePolicyAccessor getDevicePolicyAccessor() {
        return devicePolicyAccessor;
    }

    /**
     * Returns the meta data that holds the element scope meta informations for
     * the element with the specified ID.
     *
     * @param id the ID of the element, must not be null
     * @return the meta data, never returns null
     */
    public MetaData getElementMetaData(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Element ID cannot be null");
        }
        return getMetaData(id);
    }

    /**
     * Returns the meta data that holds the page scope meta informations.
     *
     * @return the meta data, never returns null
     */
    public MetaData getPageMetaData() {
        return getMetaData(null);
    }

    /**
     * Returns the meta data stored for the element with the given id (if id is
     * not null) or for the page (if id is null).
     *
     * <p>Creates an empty meta data object and registers it, if no meta data
     * have existed for the given id.</p>
     *
     * @param id the id of the element or null for the page meta data
     * @return the meta data object, never returns null
     */
    private MetaData getMetaData(final String id) {
        MetaData metaData = (MetaData) metaDataMap.get(id);
        if (metaData == null) {
            metaData = new MetaData();
            metaDataMap.put(id, metaData);
        }
        return metaData;
    }
    
    public void setBaseURLProvider(
            BaseURLProvider baseURLProvider) {
        this.baseURLProvider = baseURLProvider;
    }

    // Javadoc inherited.
    public MarinerURL getBaseURL() {
        // The base URL is only valid during PHASE1 as it is associated with
        // the elements which are processed in PHASE1.
        if (processingPhase != ProcessingPhase.PHASE1) {
            throw new IllegalStateException(
                    "Base URL is not valid during phase 2");
        }

        if (baseURLProvider == null) {
            return requestURL;
        } else {
            return baseURLProvider.getBaseURL();
        }
    }

    /**
     * Called after processing all the markup but before rendering the layout.
     *
     * <p>This allows code to be sensitive to the phase that they are used
     * in, which can be used to prevent subtle yet nasty errors. This is a bit
     * of a hack as the code should really be split into two separate and
     * distinct phases.</p>
     */
    public void endPhase1BeginPhase2() {
        if (processingPhase != ProcessingPhase.PHASE1) {
            throw new IllegalStateException("Expected to be in " +
                    ProcessingPhase.PHASE1 + " but was in " + processingPhase);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Moving from " + ProcessingPhase.PHASE1 + " to " +
                    ProcessingPhase.PHASE2);
        }

        processingPhase = ProcessingPhase.PHASE2;
    }

    /**
     * Get the processing phase.
     *
     * @return The processing phase.
     */
    public ProcessingPhase getProcessingPhase() {
        return processingPhase;
    }

    public PolicyReferenceFactory getPolicyReferenceFactory() {
        return referenceFactory;
    }

    public ProjectManager getProjectManager() {
        return volantisBean.getProjectManager();
    }

    public ProjectStack getProjectStack() {

        // The project stack is only valid during PHASE1 as it is associated
        // with the elements which are processed in PHASE1.
        if (processingPhase != ProcessingPhase.PHASE1) {
            throw new IllegalStateException(
                    "Project stack is not valid during phase 2");
        }

        return projectStack;
    }

    /**
     * @see #listenerEventRegistry
     */
    public ListenerEventRegistry getListenerEventRegistry() {
        return listenerEventRegistry;
        }

    /**
     * @see #handlerScriptRegistry
     */
    public HandlerScriptRegistry getHandlerScriptRegistry() {
        return handlerScriptRegistry;
    }

    /**
     * Returns the ScriptLibraryManager object associated with this page.
     *
     * @return the script library manager.
     */
    public ScriptLibraryManager getScriptLibraryManager() {
        if (scriptLibraryManager == null) {
            scriptLibraryManager = new ScriptLibraryManager(getProtocol());
        }
        return scriptLibraryManager;
    }

    /**
     * Returns the RequiredScriptModules object associated with this page.
     *
     * @return the requested script modules.
     */
    public RequiredScriptModules getRequiredScriptModules() {
        if (requiredScriptModules == null) {
            requiredScriptModules = new RequiredScriptModules(this);
        }
        return requiredScriptModules;
    }

    /**
     * Get the {@link PolicyFetcher} for the page.
     *
     * @return The {@link IMDPolicyFetcher}.
     * @throws IllegalStateException if support for inline meta data has not
     *                               been configured.
     */
    public IMDPolicyFetcher getPagePolicyFetcher() {
        if (imdPolicyFetcher == null) {
            throw new IllegalStateException("Inline meta data not supported");
        }
        return imdPolicyFetcher;
    }
    
    /**
     * Returns the page URI rewriter, which will rewrite specified URI through
     * all currently pushed rewriters, in the order from the least to the most
     * recently pushed one.
     * 
     * @return The page URI rewriter.
     */
    public PageURIRewriter getPageURIRewriter() {
        return uriRewriter;
    }
    
    /**
     * Pushes the specified URI rewriter onto the stack.
     * 
     * @param rewriter The rewriter to push.
     */
    public void pushURIRewriter(PageURIRewriter rewriter) {
        uriRewriter.pushRewriter(rewriter);
    }

    /**
     * Pops a URI rewriter from the stack.
     * 
     * @param expectedRewriter The expected URI rewriter to pop.
     * @throws EmptyStackException if the stack is empty.
     * @throws IllegalStateException if the expectedRewriter is not null,
     * and is not the one on the top of the stack. 
     */
    public void popURIRewriter(PageURIRewriter expectedRewriter) {
        uriRewriter.popRewriter(expectedRewriter);
    }

    /**
     * Resolve the specified absolute, or relative url against the current
     * page location as seen by the client. (The client does not necessarily
     * use the same host and prefixes as are seen by the servlet container due
     * to proxying/loadbalancing)
     *
     * @param url the absolute or relative url to resolve.
     * @return the resolved absolute url as the client should see it. Note
     * that absolute URLs passed to this metho are returned unchanged.
     */
    public MarinerURL resolveURLRelativeToClient(MarinerURL url) {
        MarinerURL result = url; // if absolute then pass through
        Path path = Path.parse(getRequestURL(false).getPath());
        if (url.isRelative()) {
            // if the url is relative then resolve the paths
            // we only care if it has more then one fragment (i.e. its a
            // page request) so trim the requested page and resolve
            // relative
            if (path.getNumberOfFragments() > 1) {
                path = new Path(path, 0, path.getNumberOfFragments()-1);
                // resolve the url to request url after url is truncated.
                path = path.resolve(url.getPath());
                result = getRequestURL(true);
                result.setPath(path.asString());
            }
            // get the client accessible URL
            result = getVolantisBean().getClientAccessibleURL(result);   
        }
        return result;
    }
    
    /**
     * Returns an instance of MediaAgent used within this context. If it was not
     * created yet, it returns null, unless create flag is true; in that case it
     * creates new instance and returns it.
     * 
     * @return An instance of MediaAgent
     */
    public MediaAgent getMediaAgent(boolean create) {
        if (mediaAgent == null && create) {
            final String urlPrefix = volantisBean.getMapUrlPrefix();
            if (urlPrefix == null) {
                throw new IllegalStateException(
                    EXCEPTION_LOCALIZER.format("missing-map-url-prefix"));
            } else {
                final MediaAgentFactory factory =
                    MediaAgentFactory.getInstance(urlPrefix);
                mediaAgent = factory.getMediaAgent();
            }
        }
        
        return mediaAgent;
    }
    
    /**
     * Returns the RequestFactory producing MediaAgent requests.
     * 
     * Note: This method is there to make MarinerPageContext more testable. 
     * 
     * @return The request factory.
     */
    public RequestFactory getMediaAgentRequestFactory() {
        return RequestFactory.getDefaultInstance();
    }
}

/*
===========================================================================
Change History
===========================================================================
$Log$

05-Dec-05   10527/3 pduffin VBM:2005112927 Fixed markers, before, after, hr, using images in content

05-Dec-05   10512/1 pduffin VBM:2005112927 Fixed markers, before, after, hr, using images in content

29-Nov-05   10505/13    pduffin VBM:2005111405 Committing transactions from MCS 3.5.0 (8)

29-Nov-05   10347/7 pduffin VBM:2005111405 More conflicts

29-Nov-05   10505/9 pduffin VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

29-Nov-05   10347/4 pduffin VBM:2005111405 Massive changes for performance

29-Nov-05   10505/3 pduffin VBM:2005111405 Committing transactions from MCS 3.5.0 (5)

18-Nov-05   10347/1 pduffin VBM:2005111405 Made session context create its contents lazily and optimised PseudoStylePath

29-Nov-05   10484/1 ianw    VBM:2005112312 Fixed pseudoElements in GUI and JIBX

28-Nov-05   10394/7 ibush   VBM:2005111812 Styling Fixes for Orange Test Page

25-Nov-05   10394/5 ibush   VBM:2005111812 interim commit for Willobs

22-Nov-05   10394/3 ibush   VBM:2005111812 Fix Canvas Branding

22-Nov-05   10394/1 ibush   VBM:2005111812 Fix Canvas Branding

15-Nov-05   10278/5 ianw    VBM:2005110425 Fixed up formating/comments

14-Nov-05   10278/3 ianw    VBM:2005110425 Fix the releasing of DeviceLayoutContext's

11-Nov-05   10253/1 emma    VBM:2005110902 Fixing two layout rendering bugs

07-Nov-05   10170/1 ianw    VBM:2005102504 port forward web clipping

04-Nov-05   9999/2  pszul   VBM:2005102504 preserver area implemented in ConvertibleImageAsset

28-Nov-05   10394/7 ibush   VBM:2005111812 Styling Fixes for Orange Test Page

25-Nov-05   10394/5 ibush   VBM:2005111812 interim commit for Willobs

22-Nov-05   10394/3 ibush   VBM:2005111812 Fix Canvas Branding

22-Nov-05   10394/1 ibush   VBM:2005111812 Fix Canvas Branding

15-Nov-05   10278/5 ianw    VBM:2005110425 Fixed up formating/comments

14-Nov-05   10278/3 ianw    VBM:2005110425 Fix the releasing of DeviceLayoutContext's

11-Nov-05   10253/1 emma    VBM:2005110902 Fixing two layout rendering bugs

07-Nov-05   10170/1 ianw    VBM:2005102504 port forward web clipping

04-Nov-05   9999/2  pszul   VBM:2005102504 preserver area implemented in ConvertibleImageAsset

15-Nov-05   10278/5 ianw    VBM:2005110425 Fixed up formating/comments

14-Nov-05   10278/3 ianw    VBM:2005110425 Fix the releasing of DeviceLayoutContext's

11-Nov-05   10253/1 emma    VBM:2005110902 Fixing two layout rendering bugs

07-Nov-05   10170/1 ianw    VBM:2005102504 port forward web clipping

04-Nov-05   9999/2  pszul   VBM:2005102504 preserver area implemented in ConvertibleImageAsset

29-Nov-05   10504/1 ianw    VBM:2005112312 Fixed pseudoElements in GUI and JIBX

29-Nov-05   10484/1 ianw    VBM:2005112312 Fixed pseudoElements in GUI and JIBX

28-Nov-05   10467/1 ibush   VBM:2005111812 Styling Fixes for Orange Test Page

28-Nov-05   10394/7 ibush   VBM:2005111812 Styling Fixes for Orange Test Page

25-Nov-05   10394/5 ibush   VBM:2005111812 interim commit for Willobs

22-Nov-05   10394/3 ibush   VBM:2005111812 Fix Canvas Branding

22-Nov-05   10394/1 ibush   VBM:2005111812 Fix Canvas Branding

15-Nov-05   10326/13    ianw    VBM:2005110425 Fixed up formating/comments

15-Nov-05   10326/1 ianw    VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

14-Nov-05   10278/3 ianw    VBM:2005110425 Fix the releasing of DeviceLayoutContext's

11-Nov-05   10282/1 emma    VBM:2005110902 Forward port: fixing two layout rendering bugs

11-Nov-05   10253/1 emma    VBM:2005110902 Fixing two layout rendering bugs

07-Nov-05   10168/1 ianw    VBM:2005102504 port forward web clipping

15-Nov-05   10278/5 ianw    VBM:2005110425 Fixed up formating/comments

14-Nov-05   10278/3 ianw    VBM:2005110425 Fix the releasing of DeviceLayoutContext's

11-Nov-05   10253/1 emma    VBM:2005110902 Fixing two layout rendering bugs

11-Nov-05   10253/1 emma    VBM:2005110902 Fixing two layout rendering bugs

07-Nov-05   10170/1 ianw    VBM:2005102504 port forward web clipping

07-Nov-05   10170/1 ianw    VBM:2005102504 port forward web clipping

04-Nov-05   9999/2  pszul   VBM:2005102504 preserver area implemented in ConvertibleImageAsset

04-Nov-05   9999/2  pszul   VBM:2005102504 preserver area implemented in ConvertibleImageAsset

10-Oct-05   9673/5  pduffin VBM:2005092906 Improved validation and fixed layout formatting

03-Oct-05   9673/3  pduffin VBM:2005092906 Added support for targeting content at layout using styles

30-Sep-05   9637/3  emma    VBM:2005092807 XForms in XDIME-CP (without tests)

30-Sep-05   9637/1  emma    VBM:2005092807 XForms in XDIME-CP (without tests)

12-Sep-05   9372/11 ianw    VBM:2005082221 Updated Javadoc

12-Sep-05   9372/9  ianw    VBM:2005082221 Updated JAvadoc

12-Sep-05   9372/7  ianw    VBM:2005082221 Allow only one instance of MarinerPageContext for a page

08-Sep-05   9447/3  ibush   VBM:2005090604 Create default style sheet and load in style engine

06-Sep-05   9447/1  ibush   VBM:2005090604 Create default style sheet and load in style engine

01-Sep-05   9375/2  geoff   VBM:2005082301 XDIMECP: clean up protocol creation

22-Aug-05   9298/5  geoff   VBM:2005080402 Style portlets and inclusions correctly.

18-Aug-05   9007/1  pduffin VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

11-Aug-05   9187/7  tom VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file] - third attempt

11-Aug-05   9187/5  tom VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file] - second attempt

10-Aug-05   9187/3  tom VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file]

04-Aug-05   9151/1  pduffin VBM:2005080205 Removing a lot of unnecessary styling code

01-Aug-05   9110/3  pduffin VBM:2005072107 First stab at integrating new themes stuff together

22-Jul-05   9110/1  pduffin VBM:2005072107 First stab at integrating new themes stuff together

15-Jul-05   9073/1  pduffin VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

12-Jul-05   8862/10 pduffin VBM:2005062108 Updated based on review comments

12-Jul-05   8862/8  pduffin VBM:2005062108 Refactored layout rendering to make it more testable.

11-Jul-05   8862/6  pduffin VBM:2005062108 Refactored layout rendering to make it more testable.

07-Jul-05   8967/1  pduffin VBM:2005070702 Refactored resolving of expressions into component identities

27-Jun-05   8878/4  emma    VBM:2005062306 rework

24-Jun-05   8878/2  emma    VBM:2005062306 Building calls to the styling engine into the framework and fixing NPE

23-Jun-05   8833/4  pduffin VBM:2005042901 Addressing review comments

21-Jun-05   8833/1  pduffin VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

20-Jun-05   8483/2  emma    VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

25-May-05   7890/5  pduffin VBM:2005042705 Committing prior to supermerge

24-May-05   7890/2  pduffin VBM:2005042705 Committing extensive restructuring changes

03-May-05   7963/1  pduffin VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

28-Apr-05   7922/2  pduffin VBM:2005042801 Removed User and UserFactory classes

28-Apr-05   7914/1  pduffin VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

27-Apr-05   7896/1  pduffin VBM:2005042709 Removing PolicyPreference and all related classes

24-May-05   8123/5  ianw    VBM:2005050906 Fix merge conflicts

11-May-05   8123/2  ianw    VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

18-May-05   8279/1  matthew VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

19-Apr-05   7738/1  philws  VBM:2004102604 Port RepositoryException localization from 3.3

19-Apr-05   7720/1  philws  VBM:2004102604 Localize RepositoryException messages

04-Apr-05   7459/5  tom VBM:2005032101 Added SmartClientSkin protocol

04-Apr-05   7459/2  tom VBM:2005032101 Added SmartClientSkin protocol

04-Apr-05   7539/3  geoff   VBM:2005040106 MPS ignores audio elements when MMS is generated

04-Apr-05   7539/1  geoff   VBM:2005040106 MPS ignores audio elements when MMS is generated

01-Apr-05   7535/1  geoff   VBM:2005040106 MPS ignores audio elements when MMS is generated

30-Mar-05   7511/5  emma    VBM:2005032204 Merge from 3.3.0 - Changing how *FormatBuilder classes (generated and hand-written) translate attribute names

11-Mar-05   7308/4  tom VBM:2005030702 Added XHTMLSmartClient and support for image sequences

11-Mar-05   7308/2  tom VBM:2005030702 Added XHTMLSmartClient and support for image sequences

10-Mar-05   6852/1  geoff   VBM:2005020206 R821: Branding using Projects (Umbrella)

09-Mar-05   7022/2  geoff   VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

17-Feb-05   6957/8  geoff   VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

17-Feb-05   6957/6  geoff   VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

11-Feb-05   6931/4  geoff   VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

09-Feb-05   6914/2  geoff   VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

25-Jan-05   6712/3  pduffin VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

29-Mar-05   7484/1  emma    VBM:2005032204 Changing how *FormatBuilder classes (generated and hand-written) translate attribute names

24-Jan-05   6760/1  tom VBM:2005011709 Fix branding identity handling and add WebSphere portal filter errors to include output of received content

17-Jan-05   6693/7  allan   VBM:2005011403 Remove MPS specific image url parameters

17-Jan-05   6693/5  allan   VBM:2005011403 Remove MPS specific image url parameters

17-Jan-05   6693/1  allan   VBM:2005011403 Remove MPS specific image url parameters

23-Dec-04   6518/3  tom VBM:2004122001 Added remote repository pre loading and cache fulshing API's to MarinerApplication

09-Dec-04   6417/1  philws  VBM:2004120703 Committing tidy up

08-Dec-04   6416/4  ianw    VBM:2004120703 New Build

08-Dec-04   6232/6  doug    VBM:2004111702 refactored logging framework

29-Nov-04   6332/1  doug    VBM:2004112913 Refactored logging framework

29-Nov-04   6232/4  doug    VBM:2004111702 Refactored Logging framework

23-Nov-04   6282/1  claire  VBM:2004112301 Ensure no null pointer on null device layout context/theme

19-Nov-04   6253/1  claire  VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

19-Nov-04   6236/3  claire  VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

12-Nov-04   6188/1  claire  VBM:2004110406 mergevbm: Ensure branded name doesn't contain slashes from brand and component name

11-Nov-04   6185/4  claire  VBM:2004110406 Ensure branded name doesn't contain slashes from brand and component name

05-Nov-04   6112/4  byron   VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

05-Nov-04   6112/2  byron   VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

02-Nov-04   5882/2  ianw    VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

01-Nov-04   6068/1  tom VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

21-Oct-04   5895/1  geoff   VBM:2004101503 chartimage path rendered icnorrectly when deployed as ROOT webapp

21-Oct-04   5884/1  geoff   VBM:2004101503 chartimage path rendered icnorrectly when deployed as ROOT webapp

15-Oct-04   5831/1  geoff   VBM:2004101502 Audio and Dynamic Visual Components do not work

12-Oct-04   5763/1  geoff   VBM:2004100105 MCS: internal-generation cache has issues with themes in a Portlet

02-Jul-04   4713/12 geoff   VBM:2004061004 Support iterated Regions (review comments)

29-Jun-04   4713/8  geoff   VBM:2004061004 Support iterated Regions (make format contexts per format instance)

21-Jun-04   4713/4  geoff   VBM:2004061004 Support iterated Regions (commit prototype for safety)

01-Jul-04   4778/3  allan   VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

14-Jun-04   4704/3  geoff   VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

14-Jun-04   4698/1  geoff   VBM:2003061912 RegionContent should not store a MarinerPageContext (rework fixes)

14-May-04   4318/1  pduffin VBM:2004051207 Integrated separators into menu rendering

12-May-04   4279/1  pduffin VBM:2004051104 Major refactoring to simplify extending the infrastructure

10-May-04   4257/2  geoff   VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection

21-Apr-04   3681/6  philws  VBM:2004033104 Menu Separator Renderer and Menu Buffer Management updates and initial DefaultMenuRenderer implementation

16-Apr-04   3884/6  claire  VBM:2004040712 Fix merge/overlap problems

16-Apr-04   3884/3  claire  VBM:2004040712 Tidied up and supermerged AssetReferenceException

15-Apr-04   3884/1  claire  VBM:2004040712 Added AssetReferenceException

16-Apr-04   3272/5  philws  VBM:2004021117 Fix supermerge issues

16-Apr-04   3362/4  steve   VBM:2003082208 supermerged

07-Apr-04   3735/1  geoff   VBM:2004033102 Enhance Menu Support: Address some issues with asset references

26-Mar-04   3500/2  claire  VBM:2004031806 Initial implementation of abstract component image references

25-Mar-04   3272/2  philws  VBM:2004021117 Fix merge issues

23-Mar-04   3362/1  steve   VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

23-Mar-04   3555/1  allan   VBM:2004032205 Patch performance fixes from MCS 3.0GA

23-Mar-04   3512/3  allan   VBM:2004032205 MCS performance enhancements.

22-Mar-04   3512/1  allan   VBM:2004032205 MCS performance enhancements.

19-Mar-04   3478/1  claire  VBM:2004031805 Removed MenuMarkupGenerator and associated code

19-Mar-04   3412/7  claire  VBM:2004031201 Improving PAPI and new menus

18-Mar-04   3412/4  claire  VBM:2004031201 Early implementation of new menus in PAPI

27-Feb-04   3215/3  steve   VBM:2004021911 Patch from Proteus2 and fixes for RemoteProject

24-Feb-04   3165/1  steve   VBM:2004021911 Allow retry on missing remote components

25-Feb-04   3113/4  steve   VBM:2004013005 supermerged

19-Feb-04   2789/16 tony    VBM:2004012601 rework changes

19-Feb-04   2789/14 tony    VBM:2004012601 refactored localised logging to synergetics

18-Feb-04   3113/1  steve   VBM:2004013005 Montage segment error. Patched from proteus2.Next

18-Feb-04   3104/1  steve   VBM:2004013005 Montage segment failures

18-Feb-04   2789/12 tony    VBM:2004012601 localisation services update

18-Feb-04   2789/9  tony    VBM:2004012601 update localisation services

18-Feb-04   3090/5  ianw    VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

18-Feb-04   3090/3  ianw    VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

18-Feb-04   3090/1  ianw    VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

16-Feb-04   2966/6  ianw    VBM:2004011923 Fixed namespace issues

16-Feb-04   2789/5  tony    VBM:2004012601 add localised logging and exception services

15-Feb-04   3034/1  mat VBM:2004011923 Added debugging and made exceptions more friendly

14-Feb-04   2966/4  ianw    VBM:2004011923 Fixed problem with mcsi handling of themes

13-Feb-04   2966/2  ianw    VBM:2004011923 Added mcsi:policy function

12-Feb-04   2789/2  tony    VBM:2004012601 Localised logging (and exceptions)

11-Feb-04   2761/3  mat VBM:2004011910 Add Project repository

06-Feb-04   2828/5  ianw    VBM:2004011922 corrected logging issues

04-Feb-04   2828/2  ianw    VBM:2004011922 Added MCSI content handler

05-Feb-04   2694/3  mat VBM:2004011917 Rework for finding repositories

26-Jan-04   2694/1  mat VBM:2004011917 Improve the way repository connections are located

08-Jan-04   2461/1  steve   VBM:2003121701 Patch pane name changes from Proteus2

07-Jan-04   2389/4  steve   VBM:2003121701 Enhanced pane referencing

06-Jan-04   2389/2  steve   VBM:2003121701 Pre-test save

05-Dec-03   2075/1  mat VBM:2003120106 Rename Device and add a public Device Interface

02-Nov-03   1765/1  allan   VBM:2003090207 Patched from Proteus2.

02-Nov-03   1756/5  allan   VBM:2003090207 Ensure page and canvas initialization failures are logged.

02-Nov-03   1756/3  allan   VBM:2003090207 Ensure page and canvas initialization failures are logged.

02-Nov-03   1756/1  allan   VBM:2003090207 Ensure page and canvas initialization failures are logged.

24-Oct-03   1623/3  steve   VBM:2003102006 Update test case to use regions

22-Oct-03   1623/1  steve   VBM:2003102006 Undo 2003090501

22-Oct-03   1620/1  steve   VBM:2003102006 Backout changes for 2003090501

22-Oct-03   1616/1  steve   VBM:2003102006 Undo 2003090501

06-Oct-03   1473/4  steve   VBM:2003092403 Merged with latest version

29-Sep-03   1473/1  steve   VBM:2003092403 Patch styleclass fixes from 2003090501

26-Sep-03   1454/2  philws  VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

11-Aug-03   1019/4  philws  VBM:2003080807 Ensure that the selectStateStack is cleared in releaseCanvas

11-Aug-03   1019/2  philws  VBM:2003080807 Provide MCS core extensions for handling the select markup element's state

28-Jul-03   755/7   adrian  VBM:2003022801 fixed merge problems with iapi implementation

18-Jul-03   812/1   adrian  VBM:2003071609 Added canvas and session level scopes for markup plugins

16-Jul-03   757/1   adrian  VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

25-Jul-03   860/3   geoff   VBM:2003071405 merge from metis again

25-Jul-03   860/1   geoff   VBM:2003071405 merge from mimas

25-Jul-03   858/1   geoff   VBM:2003071405 merge from metis; fix dissection test case sizes

25-Jun-03   549/1   steve   VBM:2003061806 URL Generation when not in a pane

19-Jun-03   407/1   steve   VBM:2002121215 Flow elements and PCData in regions

===========================================================================
*/
