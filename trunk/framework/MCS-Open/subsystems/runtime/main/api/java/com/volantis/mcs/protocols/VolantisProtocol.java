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
 * $Header: /src/voyager/com/volantis/mcs/protocols/VolantisProtocol.java,v 1.176 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header, fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation. Also added the
 *                              getStyle helper method which retrieves a style
 *                              using information stored in a VolantisAttribute
 *                              object, and default implementations of the
 *                              extended function form methods.
 * 23-Jul-01    Paul            VBM:2001070507 - Added support for protocol
 *                              specific extensions to PageHead, removed
 *                              top level do and pre process flags, simplified
 *                              by renaming all *Attributes parameters to
 *                              attributes, added doAnchor, open/closeLayout,
 *                              methods and improved the menu support.
 * 24-Jul-01    Paul            VBM:2001071103 - Added doForm method which
 *                              calls openForm, iterates through the fields
 *                              doing them and then calls closeForm. Made
 *                              openForm and closeForm protected.
 * 26-Jul-01    Paul            VBM:2001072301 - Added openFormFormat and
 *                              closeFormFormat methods.
 * 02-Aug-01    Allan           VBM:2001072604 - Added doComment() to both
 *                              simplify comments and make comments work
 *                              with the debug config attribute "comments"
 *                              that enable or disable comments when
 *                              specified using doComment. Also fixed
 *                              bugs in doMeta where the output of the
 *                              content string was in the wrong place and
 *                              http-equip had the wrong name.
 * 03-Aug-01    Doug            VBM:2001072504 Implemented Client-Side
 *                              Validation for extended forms.
 * 20-Aug-01    Doug            VBM:2001081616 fixed problem with font tags. A
 *                              call to createFontTag would open a font tag
 *                              only if certain conditions were met. The
 *                              corresponding closeFontTag method always
 *                              closed a font tag even if one had never been
 *                              opened in the first place. closeFontTag now
 *                              uses same logic as createFontTag to determine
 *                              whether to actually write a close font tag.
 * 23-Aug-01    Allan           VBM:2001082305 - Added supportsTitleOnInput
 *                              and supportsTitleOnOption boolean instance
 *                              variables with default values of true.
 * 31-Aug-01    Payal           VBM:2001080607 - Modified writePageHead
 *                              method so that it outputs head tag to the page
 *                              even if title or theme is not there
 *                              (it is empty).
 * 04-Sep-01    Paul            VBM:2001081707 - Added getTextFromReference method
 *                              and use it to get the text in the correct
 *                              encoding for those attributes whose value
 *                              could be a TextComponentName.
 * 14-Sep-01    Paul            VBM:2001083114 - Added method to initialise the
 *                              protocol which is called after the context has
 *                              been initialised.
 * 20-Sep-01    Paul            VBM:2001091202 - Added support for implicit
 *                              values.
 * 21-Sep-01    Doug            VBM:2001090302 - Changed doAnchor signature to
 *                              accept the href as a parameter (not allowed
 *                              to modify attributes). Relocated
 *                              encodeSegmentURL from VolantisTag.java. Added
 *                              getLinkFromReference methods to get links for
 *                              those attributes whose value could be a
 *                              LinkComponentName.
 * 24-Sep-01    Doug            VBM:2001092402 removed bug in getTextFromReference
 *                              which I inadvertently added in previous edit.
 * 29-Sep-01    Allan           VBM:2001090609 - Modified doMenu() do expect
 *                              orientation to come from the theme only.
 * 01-Oct-01    Allan           VBM:2001083120 - Modified doMeta() to call
 *                              addMetaAttributes(). Added addMetaAttributes()
 *                              for protocol specific meta tag attributes.
 *                              Removed dir, lang and scheme from doMeta()
 *                              since these are protocol specific.
 * 04-Oct-01    Doug            VBM:2001100201 - Added protected property
 *                              supportsAccessKeyAttribute so that subclasses
 *                              can define whether they support access keys
 *                              via the setting of an  "accesskey"  attribute
 *                              on selected tags. The default for this property
 *                              is false.
 * 08-Oct-01    Paul            VBM:2001082109 - Added new getStyle method
 *                              which allows the caller to override the tag
 *                              name in the attributes class.
 * 09-Oct-01    Allan           VBM:2001061505 - Removed doActiveSky().
 * 09-Oct-01    Doug            VBM:2001092802 - Modified the writePageHead
 *                              method so that the contents of the script
 *                              buffer are written out even if the head buffer
 *                              is empty.
 * 11-Oct-01    Allan           VBM:2001090401 - TableDataCell and
 *                              TableHeaderCell type methods changed to use
 *                              TableCellAttributes.
 * 15-Oct-01    Paul            VBM:2001101207 - Add enteredAnchorBody and
 *                              exitedAnchorBody methods.
 * 29-Oct-01    Paul            VBM:2001102901 - Theme has been renamed
 *                              DeviceTheme and added some new methods
 *                              to retrieve context information from Formats.
 * 31-Oct-01    Doug            VBM:2001092806 Moved supportsBackgroundInTable
 *                              from HTMLTransparentTV to this protocol.
 * 02-Nov-01    Paul            VBM:2001102403 - Replaced inDynamoGear flag
 *                              with a writeHead flag which does the same but
 *                              is not ATG specific.
 * 14-Nov-01    Paul            VBM:2001111402 - Added write... methods which
 *                              wrap the existing ... methods for PAPI. These
 *                              methods write the output from the existing
 *                              methods directly to the current output buffer.
 *                              Eventually the existing methods will be
 *                              replaced by the new methods.
 * 21-Nov-01    Payal           VBM:2001111902 - Modified doComment(). Added
 *                              flag which when set to true makes comments work
 *                              with the debug config attribute "comments"
 *                              that enable or disable comments when specified
 *                              using doComment and  when set to false always
 *                              writes the comments to page.
 * 22-Nov-01    Paul            VBM:2001110202 - Added more attributes to the
 *                              dissectableContents element which is written
 *                              in the text.
 * 26-Nov-01    Doug            VBM:2001112004 - added a quoteValue method.
 *                              this method is used to escape certain
 *                              characters in attribute name and values.
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes and removed dependency of this
 *                              class on HttpServletRequest.
 * 30-Nov-01    Paul            VBM:2001112909 - Added support for horizontal
 *                              rule.
 * 07-Dec-01    Paul            VBM:2001120704 - Renamed write...Emphasize
 *                              methods to write...Emphasis methods.
 * 19-Dec-01    Paul            VBM:2001120506 - Added write.. methods for
 *                              script and noscript.
 * 21-Dec01     Adrian          VBM:2001121304 - modified doAnchor() to handle
 *                              cases where <vt:a> tags contain no href="[opt]"
 *                              attribute.  Now output anchor without href.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Added method to write style
 *                              sheet.
 * 08-Jan-02    Adrian          VBM:2001121304 - correction to doAnchor()
 *                              method.  Now outputs when fallbackText == null.
 * 18-Jan-02	  Steve			      VBM:2002011102 - Added doDivideHint()
 * 18-Jan-02    Adrian          VBM:2001121003 - Added write methods for PAPI
 *                              Elements, such as writeWindowsAudio.
 * 18-Jan-02	  Steve	          VBM:2002011102 - Added doDivideHint()
 * 22-Jan-02    Doug            VBM:2002011003 - Removed openTitle and
 *                              closeTitle as doTitle is used instead.
 *                              Modified writeStyleSheet() so that a style
 *                              sheet is only generated if the
 *                              styleSheetRenderer is not null.
 * 22-Jan-02    Mat             VBM:2002011410 - Added openInclusion()
 *                              Also changed writeStyleSheet() to create a link
 *                              to an external style sheet if one is present in
 *                              the theme attributes.
 * 22-Jan-02	  Steve           VBM:2002011102 - Added writeDivideHint()
 * 22-Jan-02	  Steve           VBM:2002011102 - Added writeDivideHint()
 * 28-Jan-02    Steve           VBM:2002011412 - Support for replicas
 * 29-Jan-02    Adrian          VBM:2001121003 - javadoc for write...() methods
 *                              such as writeMMFlash.
 * 31-Jan-02    Paul            VBM:2001122105 - Deprecated all methods which
 *                              returned a String, replaces them with write
 *                              methods which automatically write the output
 *                              to the correct place. Moved a lot of the String
 *                              based helper code to StringProtocol so this
 *                              class is no relatively independent of the
 *                              output format.
 * 12-Feb-02    Paul            VBM:2002021201 - Added release method which
 *                              is used to free protocol resources.
 * 13-Feb-02    Paul            VBM:2002021203 - Added getContentExtension
 *                              method.
 * 15-Feb-02    Paul            VBM:2002021203 -  Replaced literal url
 *                              parameter names with constants from
 *                              URLConstants, also renamed server side include
 *                              methods to content component include.
 * 14-Feb-02    Allan           VBM:2002021303 - Changed writePageComment()
 *                              value param to overideConfig & javadoc'd.
 * 19-Feb-02    Paul            VBM:2001100102 - Added code to initialise the
 *                              form specifier attribute, added a method to
 *                              resolve the form action and also added methods
 *                              to retrieve protocol specific FieldHandlers.
 * 20-Feb-02    Steve           VBM:2001101803 - Adds fragmentation buttons
 *                              to forms if the form is fragmented.
 * 21-Feb-02    Steve           VBM:2001101803 - Added checkFragmentedPane()
 *                              which determines whether or not a pane is
 *                              inside a form fragment, and if it is, will it
 *                              be displayed.
 * 22-Feb-02    Steve           VBM:2001101803 - Added removeFormParameters()
 *                              This is a fairly sledgehammer way of gtetting
 *                              rid of the form parameters from a MarinerURL
 *                              to send to the form fragmetation servlet.
 *                              It stores parameters we are interested in such
 *                              as vfrag, portlet, etc..Nukes all the
 *                              parameters.. then puts the ones we are
 *                              interested in back again. This needs to be
 *                              re-done properly...but it works.
 * 28-Feb-02    Paul            VBM:2002022804 - Removed most of the old
 *                              deprecated methods, also moved the form
 *                              fragmentation helper methods into
 *                              StringProtocol as they are specifically String
 *                              based.
 * 01-Mar-02    Mat             VBM:2002021203 - Added writeSSIInclude(),
 *                              writeSSIConfig() and writeWSDirective()
 * 04-Mar-02    Paul            VBM:2001101803 - Moved the code which resolves
 *                              the form action url depending on whether the
 *                              form is fragmented or not into the
 *                              resolveFormAction method and removed the
 *                              duplicated code from WMLRoot and XHTMLBasic.
 *                              Changed all the getFieldHandler methods
 *                              to take the FieldType instead of the
 *                              attributes. Added updateSelectedOptions to
 *                              properly support multiple select fields in a
 *                              fragmented form.
 * 08-Mar-02    Paul            VBM:2002030607 - Added supportsInlineStyles
 *                              and supportsExternalStyleSheets flags to
 *                              control the style sheet generation. Added
 *                              a CanvasAttributes attribute to allow them to
 *                              be accessed from anywhere in the protocol.
 *                              Added a method to get the style sheet renderer.
 *                              Added openInclusionPage and closeInclusionPage
 *                              methods.
 * 11-Mar-02    Doug            VBM:2002011003 - Added a pageType property.
 *                              Added the page type constants CANVAS_PAGE &
 *                              MONTAGE_PAGE. Added the methods setPageType()
 *                              and isPageType().
 * 12-Mar-02    Adrian          VBM:2002021910 - Added method quoteScriptValue
 *                              to perform entity replacement on Script values.
 *                              Also abstract method writeScriptBody as each
 *                              protocol may require different processing of
 *                              script asset content.
 * 12-Mar-02    Paul            VBM:2002021201 - Added initialiseCanvas method
 *                              which does any initialisation which depends
 *                              on properties initialised in the
 *                              MarinerPageContext's initialiseCanvas method.
 * 13-Mar-02    Paul            VBM:2002031301 - Renamed get/endContentBuffer
 *                              to get/endCurrentBuffer respectively.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 21-Mar-03    Paul            VBM:2002032105 - Moved the removeFormParameters
 *                              method from StringProtocol, added the
 *                              writeOpen/CloseElement custom markup methods
 *                              and removed the deprecated methods which are
 *                              no longer used.
 * 22-Feb-02    Paul            VBM:2002021802 - Added some methods and flags
 *                              for doing dissection and removed the last
 *                              deprecated methods.
 * 28-Mar-02    Allan           VBM:2002022007 - generateContentTree() now
 *                              takes an Object. generateContents() returns an
 *                              Object. Re-wrote quoteTextString() and
 *                              quoteTable is now an array not a HashMap. Also
 *                              added a new char[] version of quoteTextString()
 *                              and char[] versions of writeDirect() and
 *                              writeEncoded(). Added experimental quoted
 *                              text caching.
 * 02-Apr-02    Allan           VBM:2002022007 - Fixed bug in quoteTextString()
 *                              to cache the protocol using the class instead
 *                              of this - since there is no equals method for
 *                              protocols.
 * 03-Apr-02    Allan           VBM:2002022007 - Apply previous fix to the
 *                              char[] version of quoteTextString().
 * 09-Apr-02    Allan           VBM:2002040912 - Added a ; char to every
 *                              encoding in quoteTable. Rewrote
 *                              encodeCharacter() to use quoteTable. Modified
 *                              both quoteTextString() methods to use
 *                              encodeCharacter.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Fixed problem with
 *                              encodeSegmentURL method, removed writePageHead
 *                              and writeComment as they are no longer needed
 *                              at this level and also changed the form format
 *                              methods to take a set of attributes instead of
 *                              a Form.
 * 26-Apr-02    Paul            VBM:2002042205 - Fixed release so it does not
 *                              try and release resources which were not
 *                              initialised due to an exception and moved
 *                              supportsBackgroundInTable to a HTMLRoot as it
 *                              is specific to that protocol and extensions.
 * 31-Apr-02    Paul            VBM:2002042205 - Moved the writeStyleSheet code
 *                              from StringProtocol as it is common across
 *                              both String and DOM based protocols.
 * 01-May-02    Allan           VBM:2002040804 - StyleSheetRenderer and
 *                              DeviceTheme set to new renderers and themes
 *                              versions respectively. Updated
 *                              writeStyleSheet().
 * 02-May-02    Allan           VBM:2002040804 - Added link type to style sheet
 *                              link in writeStyleSheet().
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Allan           VBM:2002040804 - Added check to writeStyleSheet
 *                              method to return if there are no rules to
 *                              render.
 * 03-May-02    Paul            VBM:2002042203 - Removed the preprocess flag
 *                              and added maxPageSize.
 * 07-May-02    Adrian          VBM:2002042302 - Removed method getContent..
 *                              ..Extension() as the cached file extension is
 *                              defined in the device policies.
 * 16-May-02    Adrian          VBM:2002040808 - added method getStyle(String..
 *                              .. ElementName, String className, String id)
 *                              as it is required in ChartElement.
 * 20-May-02    Mat             VBM:2002040813 - Changed writeStyleSheet() to
 *                              call StyleSheetGenerator.getThemeURL() to
 *                              create the URL for the style sheet.
 * 23-May-02    Paul            VBM:2002042202 - Removed the writeDirect,
 *                              writeEncoded, getDirectWriter and
 *                              getEncodingWriter methods.
 * 23-May-02    Paul            VBM:2002042202 - Removed quoteScriptValue
 *                              method.
 * 14-Jun-02    Byron           VBM:2002052707 - Removed % mapping from the
 *                              quote table in constructor.
 * 11-Jun-02    Steve           VBM:2002040807 - Added a base
 *                              ProtocolElementMapping class which stores the
 *                              mappings between mariner and protocol elements
 *                              for any derived protocol. The mappings should
 *                              be generated in the initialise method of the
 *                              derived protocol.
 * 19-Jun-02    Adrian          VBM:2002053104 - Added methods getInclusionTag,
 *                              getInclusionStyleElement, getInclusionStyleID,
 *                              getInclusionStyleClass, getLayoutDeviceTheme..
 *                              ..Factory.  Also updated method writeStyleSheet
 *                              to cope with DeviceLayout stylesheets and
 *                              inline imports of inclusion page stylesheets.
 * 28-Jun-02    Paul            VBM:2002051302 - Added EmulatorRendererContext
 *                              to prevent having to create one for each
 *                              property.
 * 08-Jul-02    Steve           VBM:2002062401 - doContent() method which is
 *                              called by the xfcontent tag. This simply calls
 *                              a derived writeContent() method and releases
 *                              the output buffer that the tags content was
 *                              written to.
 * 10-Jul-02    Steve           VBM:2002040807 - Call style sheet converter
 *                              if there is a device theme to render.
 * 15-Jul-02    Byron           VBM:2002052707 - Removed $ entry in quote table
 * 15-Jul-02    Adrian          VBM:2002053104 - Added member variable flag
 *                              supportsMultipleSelectorClasses which is set on
 *                              initialisation.  Set to true if the current
 *                              device allows multiple classnames in class
 *                              attributes.
 * 16-Jul-02    Steve           VBM:2002040807 - supportsMultipleSelectorClasses
 *                              should not be set if the style class support
 *                              is set to ATTRIBUTE. Added
 *                              supportsMultipleAttributeClasses which is set
 *                              is the support is ATTRIBUTE. If the support is
 *                              SELECTOR then both multiple style classes and
 *                              multiple attributes are set.
 * 19-Jul-02    Adrian          VBM:2002071712 -updated method writeStyleSheet
 *                              use a local variable for DeviceTheme as a new
 *                              DeviceTheme is now created by RuleManipulator.
 *                              Previously the cached DeviceTheme was being
 *                              modified causing css emulation to fail.
 * 26-Jul-02    Steve           VBM:2002062401 - doContent() and
 *                              writeContent() have shiny new javadoc.
 * 26-Jul-02    Steve           VBM:2002040807 - pass the inverse of
 *                              isInclusion() to the style sheet xformer
 *                              to determine whether or not the canvas is
 *                              the root canvas or not. If the theme we
 *                              are converting has its own external stylesheet
 *                              then no conversion is done.
 * 29-Jul-02    Sumit           VBM:2002072506 - Support for pseudo classes
 * 31-Jul-02    Paul            VBM:2002073008 - Added extra methods to support
 *                              packages, and added some logging to
 *                              writeStyleSheet to make it easy to track down
 *                              a bug.
 * 01-Aug-02    Sumit           VBM:2002073109 - Modified updateSelectedOptions
 *                              to handle list of options and optgroups
 * 06-Aug-02    Paul            VBM:2002073008 - Removed support for package
 *                              element.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added a doTimer method for
 *                              <timer> support
 * 06-Aug-02    Paul            VBM:2002080509 - Stopped passing
 *                              VolantisAttribute to class in common package.
 * 09-Aug-02    Sumit           VBM:2002080732 - Removed the duplicate code
 *                              that renders inline stylesheets incorrectly
 * 09-Sep-02    Mat             VBM:2002090502 - Added ContentFieldType and the
 *                              fieldHandler for it.
 * 10-Sep-02    Steve           VBM:2002040809 - Added getPaneStyle() to return
 *                              a pane style from either the pane attributes
 *                              or the theme.
 * 10-Oct-02    Adrian          VBM:2002100404 - rewrote writeStyleSheet and
 *                              Changed writeLink and writeOpen/CloseStyle to
 *                              public from protected.
 * 14-Oct-02    Byron           VBM:2002091904 - Added getProtocolStyleSheet()
 *                              and modified writeStyleSheet to output the link
 *                              for the protocolStyleSheet only once for
 *                              external, inline and @import links.
 * 08-Nov-02    Byron           VBM:2002110516 - Modified writeStyleSheet()
 *                              to used new method addDefaultCssCanditate().
 *                              Removed redundant imports and fixed javadocs.
 * 11-Nov-02    Chris W         VBM:2002102403 - Added writeOpenSlide(),
 *                              writeCloseSlide() and calculatePaneHeight(),
 *                              tweaked calculateWidth to produce sensible
 *                              defaults for MMS SMIL protocol.
 * 18-Nov-02    Geoff           VBM:2002111504 - Refactored to use new fallback
 *                              methods in the page context.
 * 20-Nov-02    Geoff           VBM:2002111504 - Refactored code to get
 *                              fallback text of particular encoding to
 *                              MarinerPageContext.
 * 20-Nov-02    Geoff           VBM:2002111504 - remove unused methods
 *                              (generateShare, encodeAndAppend,
 *                              writeShardLink, generateContents,
 *                              generateContentTree), cleaned up some unused
 *                              imports, local vars and casts.
 * 22-Nov-02    Geoff           VBM:2002111504 - Refactored code to do alt text
 *                              fallbacks from Element classes to here; added
 *                              writeAltText and doAltText
 * 14-Nov-02    Geoff           VBM:2002103005 - moved impl. of writeLayout up
 *                              from child classes since all their impls were
 *                              identical.
 * 22-Nov-02    Geoff           VBM:2002103005 - small fix to renderAltText.
 * 01-Dec-02    Phil W-S        VBM:2002112901 - Updated writeStyleSheet to
 *                              construct the candidates using the updated
 *                              constructor method signatures.
 * 01-Dec-02    Phil W-S        VBM:2002112701 - Add the RENDER_ constants,
 *                              the deviceStylesheetPreference,
 *                              layoutStylesheetPreference and
 *                              themeStylesheetPreference members and updated
 *                              writeStyleSheet to use these members and the
 *                              new renderMode method to output the various
 *                              stylesheets as either external or internal
 *                              ("inline") stylesheets, with internal
 *                              stylesheets rendered explicitly or as
 *                              @import statements (depending on a device
 *                              policy value). NB: There are issues with the
 *                              original code this was based on. See the todos
 *                              in the method.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 16-Dec-02    Adrian          VBM:2002100203 - Renamed member field
 *                              supportsMultipleStyleClasses to
 *                              supportsMultipleAttributeClasses and added more
 *                              comprehensive javadoc to elementMappings field
 * 08-Jan-03    Phil W-S        VBM:2003010906 - Add accessors for
 *                              supportsMultipleAttributeClasses and
 *                              supportsMultipleSelectorClasses.
 * 20-Jan-03    Doug            VBM:2002120213 - Added the
 *                              getSelectionRenderer() method.
 * 16-Jan-03    Mat             VBM:2002112603 - Added writeAudio
 * 29-Jan-03    Byron           VBM:2003012803 - Added protocolConfiguration
 *                              member variable and added getter for it.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Add packagePage,
 *                              writeCanvasContent, writeMontageContent.
 *                              Implement closeCanvasPage and closeMontagePage.
 *                              Implement PackageBodySource.
 * 13-Feb-03    Byron           VBM:2003021309 - Added getPackagingType method.
 * 03-Mar-03    Byron           VBM:2003022813 - Modified getInitialValue to
 *                              call getTextFromReference.
 * 05-Mar-03    Chris W         VBM:2003030523 - Modified setMultipleSelected
 *                              to compare the option attribute's value with
 *                              the vth value in the initialValues array.
 * 06-Mar-03    Sumit           VBM:2003022605 - Added new methods for opening
 *                              and closing spatial format iterators and their
 *                              rows and children
 * 14-Mar-03    Chris W         VBM:2003030403 - Added supportsNativeMarkup to
 *                              indicate if the protocol supports the maml
 *                              nativemarkup tag.
 * 14-Mar-03    Chris W         VBM:2003030403 - writeOpenNativeMarkup and
 *                              writeCloseNativeMarkup no longer throw
 *                              IOExceptions.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 21-Mar-03    Sumit           VBM:2003022828 - isFormatStrValid sets the
 *                              isEmptyOk if pattern starts with lowercase
 *                              letters eg m:XX###
 * 21-Mar-03    Phil W-S        VBM:2003031910 - Converted the RENDER_
 *                              constants into a type-safe enumeration,
 *                              StylesheetRenderMode, renamed members
 *                              *StylesheetReference to
 *                              protocol*StylesheetPreference and moved
 *                              initialization of these into the constructor,
 *                              changed the type for these members and the
 *                              return type of renderMode to the new
 *                              enumeration. Added the
 *                              preferredLocationFor*Stylesheet* members,
 *                              initialized these in initialise. Changed the
 *                              signature of renderMode to take both device and
 *                              protocol scope preferences and updated it to
 *                              use both (device preferences taking
 *                              precedence). Changed the calls to renderMode in
 *                              writeStyleSheet to pass the required
 *                              preferredLocationFor*Stylesheet* members.
 * 29-Mar-03    Phil W-S        VBM:2002111502 - Add writeOpenPhoneNumber and
 *                              writeClosePhoneNumber. Added the
 *                              supportsDiallingLinks, diallingLinkInfo
 *                              and diallingLinkInfoType members and
 *                              initialization of these in initialize. Added
 *                              the DiallingLinkInfoType nested class.
 * 14-Apr-03    Steve           VBM:2003041501 - Create and return a native
 *                              writer object used to output native markup to
 *                              the device.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary, catch
 *                              FormatVisitorException and rethrow
 *                              ProtocolException in writeLayout().
 * 17-Apr-03    Geoff           VBM:2003040305 - Refactor getScriptFromObject
 *                              method away to Script class.
 * 22-Apr-03    Allan           VBM:2003041710 - Added skipElementBody property
 *                              with public getter and protected setter.
 * 24-Apr-03    Chris W         VBM:2003030404 - Added supportsNativeMarkup to
 *                              indicate if the protocol supports the maml
 *                              nativemarkup tag. Added writeOpenNativeMarkup
 *                              and writeCloseNativeMarkup as well.
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * 17-Apr-03    Geoff           VBM:2003040305 - Refactor getScriptFromObject
 *                              method away to Script class.
 * 25-Apr-03    Steve           VBM:2003041606 - The encoding writer is now an
 *                              OutputBufferWriter
 * 01-May-03    Mat             VBM:2003042912 - Added NBSP literal
 * 02-May-03    Byron           VBM:2003042208 - Added writeInitialFocus(..).
 *                              and writeCloseNativeMarkup as well.
 * 07-May-03    Allan           VBM:2003050704 - Caches are now in the
 *                              synergetics package.
 * 08-May-03    Steve           VBM:2003042914 - Update signature of write to
 *                              take a PackageBodyOutput class instead of a Writer.
 *                              The PackageBodyOutput will return the required
 *                              Writer object
 * 21-May-03    Byron           VBM:2003042208 - Added writeInitialFocus(..).
 *                              Merged with main stream.
 * 28-May-03    Steve           VBM:2003042206 - Patch 2003041501 from Metis
 * 30-May-03    Mat             VBM:2003042911 - Changed writeCanvasContent()
 *                              and writeMontageContent() to accept a
 *                              PackageBodyOutput instead of a Writer.
 * 03-Jun-03    Allan           VBM:2003060301 - quoteTextString() modified to
 *                              use a LeastUsedStrategy to be compatible with
 *                              Synergetics.
 * 02-Jun-03    Steve           VBM:2003042906 - directWriter only needs to
 *                              be a ProtocolWriter as this extends
 *                              OuputBufferWriter. This seems to fix the
 *                              whitespace problem for WML but I am not 100%
 *                              sure why.....
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.CacheScopeConstant;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.css.reference.CssCandidateAdapter;
import com.volantis.mcs.protocols.css.reference.CssReference;
import com.volantis.mcs.protocols.css.renderer.RuntimeRendererProtocolConfiguration;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.forms.AbstractFormFragment;
import com.volantis.mcs.protocols.forms.ActionFieldType;
import com.volantis.mcs.protocols.forms.BooleanFieldType;
import com.volantis.mcs.protocols.forms.ContentFieldType;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.protocols.forms.FieldHandler;
import com.volantis.mcs.protocols.forms.FieldType;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.protocols.forms.FormDescriptor;
import com.volantis.mcs.protocols.forms.FormFragmentData;
import com.volantis.mcs.protocols.forms.ImplicitFieldType;
import com.volantis.mcs.protocols.forms.Link;
import com.volantis.mcs.protocols.forms.MultipleSelectFieldType;
import com.volantis.mcs.protocols.forms.MultipleValueFieldHandler;
import com.volantis.mcs.protocols.forms.SessionFormData;
import com.volantis.mcs.protocols.forms.SingleSelectFieldType;
import com.volantis.mcs.protocols.forms.SingleValueFieldHandler;
import com.volantis.mcs.protocols.forms.TextInputFieldType;
import com.volantis.mcs.protocols.forms.UploadFieldType;
import com.volantis.mcs.protocols.gallery.GalleryModule;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.menu.shared.MenuModuleCustomisationImpl;
import com.volantis.mcs.protocols.renderer.RendererContext;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.shared.RendererContextImpl;
import com.volantis.mcs.protocols.renderer.shared.layouts.FormatRendererContextImpl;
import com.volantis.mcs.protocols.response.attributes.ResponseBodyAttributes;
import com.volantis.mcs.protocols.ticker.TickerModule;
import com.volantis.mcs.protocols.ticker.response.TickerResponseModule;
import com.volantis.mcs.protocols.widgets.WidgetModule;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.PageURLDetailsFactory;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.URLConstants;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.mcs.runtime.packagers.PackageBodySource;
import com.volantis.mcs.runtime.packagers.Packager;
import com.volantis.mcs.runtime.packagers.PackagingException;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSLinkStyleKeywords;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.shared.content.ContentStyle;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class: VolantisProtocol, the protocol superclass
 *
 * This is the protocols SuperClass and should be extended by any of protocols
 * that need to definitions. Each of the major protocols should extend this
 * class, and within each major protocol sub-class, further version specific
 * classes should be sub-classed to tailor the specific output requirements of
 * a particular version of the protocol in use.
 *
 * @mock.generate
 */
public abstract class VolantisProtocol
    implements DevicePolicyConstants, PackageBodySource,
    LayoutModule, RuntimeRendererProtocolConfiguration {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(VolantisProtocol.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(VolantisProtocol.class);

    private ProtocolIndependentObjectRenderer objectRenderer;    

    /**
     * Writes an action button to link to another fragment in the form and uses
     * the base-url defined in the config file to make the URL.
     *
     * @param dom               to which to add any generated markup
     * @param attributes        Must contain a non null {@link AbstractForm}
     * @param link              for which to generate a link
     * @todo later XDIME-CP style forms properly
     */
    protected void doFormLink(OutputBuffer dom, XFFormAttributes attributes,
            Link link) throws ProtocolException {

        AbstractForm form = attributes.getFormData();
        String formName = form.getName();
        AbstractFormFragment fragment = link.getFormFragment();
        String fragmentName = fragment.getName();

        MarinerURL marinerURL = context.getRequestURL(true);
        if (!marinerURL.isRelative()) {
            marinerURL = new MarinerURL(marinerURL.getPath());
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Getting form fragmentation states for form "
                         + formName);
        }

        // Get the key to the current fragmentation state.
        int currentKey = context.getFormFragmentationIndex(formName);

        if (logger.isDebugEnabled()) {
            logger.debug("Generating link to enclosed form fragment");
        }

        // Determine the change to apply to the fragmentation state.
        String inclusionPath = getInclusionPath(fragment);
        FragmentationState.Change change =
                new FragmentationState.FragmentChange(inclusionPath,
                fragmentName, false, false);

        if (logger.isDebugEnabled()) {
            logger.debug("Change to be applied is " + change);
        }

        PageGenerationCache pgCache = context.getPageGenerationCache();

        int changeIndex = pgCache.getFormFragmentationStateChangeIndex(
                formName, change);

        String requestValue = PageGenerationCache.
                makeFragmentChangeSpecifier(currentKey, changeIndex);

        marinerURL = removeFormParameters(marinerURL);
        if (logger.isDebugEnabled()) {
            logger.debug("URL before is " + marinerURL.getExternalForm());
        }

        if (requestValue != null) {
            marinerURL.setParameterValue
                (URLConstants.FORM_FRAGMENTATION_PARAMETER, requestValue);
        }

        // Get the URLRewriter to use to encode session information in the
        // URL and use it.
        MarinerRequestContext requestContext = context.getRequestContext();
        URLRewriter sessionURLRewriter = context.getSessionURLRewriter();
        marinerURL = sessionURLRewriter.mapToExternalURL(requestContext,
                                                         marinerURL);

        // perform any URL rewriting that may be required by an external plugin
        PageURLRewriter urlRewriter = context.getVolantisBean().
            getLayoutURLRewriter();
        MarinerURL externalURL = urlRewriter.rewriteURL(
            context.getRequestContext(),
            marinerURL,
            PageURLDetailsFactory.createPageURLDetails(PageURLType.FORM));

        String absoluteLink = externalURL.getExternalForm();
        if (logger.isDebugEnabled()) {
            logger.debug("URL after Rewriting is " + absoluteLink);
        }

        final FormDescriptor fd = attributes.getFormDescriptor();
        SessionFormData formData = context.getFormDataManager().getSessionFormData(fd);
        final String linkName = link.getLinkName();
        formData.setFieldValue(linkName, absoluteLink);

        if (logger.isDebugEnabled()) {
            logger.debug("Setting field " + linkName + "=" + absoluteLink +
                    " for form name=" + formName);
        }

        // Create an action button for this link
        XFActionAttributes action = createActionForLink(attributes, link);

        doFormFragmentButton(dom, action);
    }

    /**
     * Determine the correct inclusion path - the mechanism varies between
     * XDIME1 and XDIME2 form fragmentation.
     *
     * @param formFragment  whose inclusion path to find
     * @return String inclusion path for this fragment
     */
    private String getInclusionPath(AbstractFormFragment formFragment) {
        if (formFragment instanceof FormFragmentData) {
            return ((FormFragmentData)formFragment).getInclusionPath();
        } else {
            DeviceLayoutContext deviceContext = context.getDeviceLayoutContext();
            return deviceContext.getInclusionPath();
        }
    }

    /**
     * Generate {@link XFActionAttributes} instance using the link information
     * given. This should be used for styling an action input.
     *
     * @param attributes    provide additional information about the link
     * @param link          for which to generate XFActionAttributes
     * @return XFActionAttributes describing the link
     */
    private XFActionAttributes createActionForLink(XFFormAttributes attributes,
            Link link) {
        // Create an action button for this link
        XFActionAttributes action = new XFActionAttributes();

        // If the link styles are null (as they will be if this comes from an
        // XDIME1 form) then fall back to the element's styles.
        Styles linkStyles = link.getLinkStyles();
        if (linkStyles == null) {
            linkStyles = attributes.getStyles();
        }
        action.setStyles(linkStyles.copy());

        action.setName(link.getLinkName());
        action.setFormData(attributes.getFormData());
        action.setFormAttributes(attributes);

        if (context.getPageTagId() != null) {
            action.setId(context.getPageTagId());
        }

        // The link text could be null if it should come from the CONTENT style
        // property (see DefaultContentInserter).
        final String linkText = link.getLinkText();
        if (linkText != null) {
            PolicyReferenceResolver resolver =
                    context.getPolicyReferenceResolver();
            TextAssetReference linkTextReference =
                    resolver.resolveQuotedTextExpression(linkText);
            action.setCaption(linkTextReference);
        }

        // An action button is always a submit.
        action.setType("submit");

        // Fake an entry container instance for the action. The action is not
        // being written to the entry container but needs one in order for WML
        // protocols to correctly generate the action.
        action.setEntryContainerInstance(context.getCurrentContainerInstance());
        return action;
    }

    protected void doFormFragmentButton(
            OutputBuffer buffer, XFActionAttributes action)
            throws ProtocolException {
    }

    /**
     * Prepare Object markup and write suitable markup open,
     * object XDIE element might be written out as anchor,image,object or text
     * and there is complicated logic behind so implementation is moved to separate class
     * {@link ProtocolIndependentObjectRenderer}
     * @param attributes objectElement attributes
     * @throws ProtocolException
     */
    public void writeOpenObject(ObjectAttribute attributes) throws ProtocolException {
        if(objectRenderer == null){
            objectRenderer = new ProtocolIndependentObjectRenderer(this);
        }
        objectRenderer.writeOpenObject(attributes);
    }

    /**
     * write suitable close markup for object element
     * @param attributes objectElement attributes
     * @throws ProtocolException
     */
    public void writeCloseObject(ObjectAttribute attributes) throws ProtocolException {
        objectRenderer.writeCloseObject(attributes);
    }

    /**
     * Writes the opening of the object markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    protected void directWriteOpenObject(ObjectAttribute attributes)
            throws ProtocolException {
    }

    /**
     * Writes the closure of the object markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    protected void directWriteCloseObject(ObjectAttribute attributes)
            throws ProtocolException {
    }    
    

    /**
     * This typesafe enumeration provides symbol resolution for internal
     * purposes only (the static get method can be used with an internal string
     * version of the enumeration names to retrieve the required instance, or
     * null if the string is not recognised).
     */
    protected static final class DiallingLinkInfoType {
        /**
         * The set of all literals. Keyed on the internal string version of the
         * enumeration, mapping to the DiallingLinkInfoType equivalent. <p/>
         * NB: This static member *must* appear before the enumeration literals
         * for this to work. If it does not, the access of this variable within
         * the literal construction (within this class's constructor) will find
         * this variable to be null (i.e. it won't have been initialized yet).
         * The Java Language Spec second edition, section 8.7, specifically
         * states that initialization is performed in "textual order".
         */
        private static final HashMap entries = new HashMap();

        /**
         * An enumeration literal indicating that dialling links require no
         * dialling link info.
         */
        public static final DiallingLinkInfoType NONE =
            new DiallingLinkInfoType(DevicePolicyConstants.
                                     LINK_DIALLING_INFO_TYPE_NONE);

        /**
         * An enumeration literal indicating that dialling links require a
         * dialling link prefix in the associated dialling link info.
         */
        public static final DiallingLinkInfoType PREFIX =
            new DiallingLinkInfoType(DevicePolicyConstants.
                                     LINK_DIALLING_INFO_TYPE_PREFIX);

        private final String name;

        private DiallingLinkInfoType(String name) {
            this.name = name;

            entries.put(name, this);
        }

        /**
         * Returns the internal name for the enumeration literal. This must not
         * be used for presentation purposes.
         *
         * @return internal name for the enumeration literal
         */
        public String toString() {
            return name;
        }

        /**
         * Retrieves the enumeration literal that is equivalent to the given
         * internal name, or null if the name is not recognized.
         *
         * @param name the internal name to be looked up
         * @return the equivalent enumeration literal or null if the name is
         *         not recognized
         */
        public static DiallingLinkInfoType get(String name) {
            return (DiallingLinkInfoType)entries.get(name);
        }
    }

    /**
     * Typesafe enumeration for the stylesheet rendering mode requests. It
     * provides symbol resolution for internal purposes only (the static get
     * method can be used with an internal string version of the enumeration
     * names to retrieve the required instance, or null if the string is not
     * recognised).
     */
    protected static class StylesheetRenderMode {
        /**
         * The set of all literals. Keyed on the internal string version of the
         * enumeration, mapping to the StylesheetRenderMode equivalent. <p/>
         * NB: This static member *must* appear before the enumeration literals
         * for this to work. If it does not, the access of this variable within
         * the literal construction (within this class's constructor) will find
         * this variable to be null (i.e. it won't have been initialized yet).
         * The Java Language Spec second edition, section 8.7, specifically
         * states that initialization is performed in "textual order".
         */
        private static final HashMap entries = new HashMap();

        /**
         * An enumeration literal indicating that the default rendering should
         * be applied
         */
        public static final StylesheetRenderMode DEFAULT =
            new StylesheetRenderMode(STYLESHEET_RENDER_DEFAULT);

        /**
         * An enumeration literal requesting that internal rendering should be
         * applied
         */
        public static final StylesheetRenderMode INTERNAL =
            new StylesheetRenderMode(STYLESHEET_RENDER_INTERNAL);

        /**
         * An enumeration literal requesting that external rendering should be
         * applied
         */
        public static final StylesheetRenderMode EXTERNAL =
            new StylesheetRenderMode(STYLESHEET_RENDER_EXTERNAL);

        /**
         * An enumeration literal requesting that internal import rendering
         * should be applied. This enumeration value is for internal use in the
         * {@link VolantisProtocol#writeStyleSheet} method only and is not
         * valid as a stylesheet rendering mode preference (device or
         * protocol)
         */
        public static final StylesheetRenderMode IMPORT =
            new StylesheetRenderMode("import");

        /**
         * The internal string version of the enumeration.
         */
        private final String name;

        /**
         * Private to ensure that all literals must be defined within this
         * class.
         *
         * @param name the internal string version of the enumeration
         */
        private StylesheetRenderMode(String name) {
            this.name = name;

            entries.put(name, this);
        }

        /**
         * Returns the internal name for the enumeration literal. This must not
         * be used for presentation purposes.
         *
         * @return internal name for the enumeration literal
         */
        public String toString() {
            return name;
        }

        /**
         * Retrieves the enumeration literal that is equivalent to the given
         * internal name, or null if the name is not recognized.
         *
         * @param name the internal name to be looked up
         * @return the equivalent enumeration literal or null if the name is
         *         not recognized
         */
        public static StylesheetRenderMode get(String name) {
            return (StylesheetRenderMode)entries.get(name);
        }
    }

    /**
     * Specifies that the page being processed is from a canvas layout
     */
    protected final static int CANVAS_PAGE = 0;

    /**
     * Specifies that the page being processed is from a canvas layout
     */
    protected final static int MONTAGE_PAGE = 1;

    private boolean skipElementBody = false;

    /**
     * This flag indicates whether a protocol supports alignment
     * of table cells
     */
    protected boolean supportsTableCellAlign = true;

    /**
     * This flag indicates whether a protocol supports center element
     */
    protected boolean supportsCenterElement = true;

    /**
     * NOTE: Child classes that override this method must reset skipElementBody
     * to false before returning the result - see design for details. Child
     * classes must override this method if they wish to change the value of
     * skipElementBody.
     *
     * @return skipElementBody.
     */
    public boolean skipElementBody() {
        return skipElementBody;
    }

    /**
     * Set skipElementBody.
     *
     * @param value
     */
    protected void setSkipElementBody(boolean value) {
        skipElementBody = value;
    }

    /**
     * Each device can specify the preferred rendering mechanism for the theme
     * stylesheet(s), if the page warrants one or more. If this value is set to
     * DEFAULT, the location reference will be determined by {@link
     * #protocolThemeStylesheetPreference}. This value is determined from the
     * {@link DevicePolicyConstants#STYLESHEET_LOCATION_THEME} device policy
     * value.
     */
    protected StylesheetRenderMode preferredLocationForThemeStylesheets;

    /**
     * Each protocol can specify the preferred rendering mechanism for the
     * theme stylesheet(s), if the current page warrants one or more.
     */
    protected StylesheetRenderMode protocolThemeStylesheetPreference;

    /**
     * The StyleSheetRenderer.
     */
    protected StyleSheetRenderer styleSheetRenderer;

    /**
     * The ScriptHandler.
     */
    protected ScriptHandler scriptHandler;

    /**
     * The MarinerPageContext associated with the page being generated.
     */
    protected MarinerPageContext context;

    /**
     * Flag which controls whether or not the head is written.
     */
    protected boolean writeHead;

    /**
     * Indicates whether this page needs dissecting.
     */
    private boolean dissecting = false;

    /**
     * Indicates whether it is possible to dissect this page.
     */
    private boolean dissectionNeeded = false;

    /**
     * Indicates whether this page is an inclusion.
     */
    protected boolean inclusion = false;

    /**
     * Specifies whether the protocol supports accesskey attributes.
     */
    protected boolean supportsAccessKeyAttribute = false;

    /**
     * Indicates whether the protocol will automatically display a shortcut
     * prefix for elements with a shortcut (aka access key). Is expected to be
     * set in specialist protocol {@link #initialise} methods (since the value
     * is to be derived from device policy values and therefore varies on a
     * device rather than protocol basis).
     */
    protected boolean supportsAutomaticShortcutPrefixDisplay = false;

    /**
     * Flag which specifies whether the protocol supports inline styles or
     * not.
     */
    protected boolean supportsInlineStyles = false;

    /**
     * Flag which specifies whether the protocol supports external style sheets
     * or not.
     */
    protected boolean supportsExternalStyleSheets = false;

    /**
     * Flag to indicate that menus are rendered using a div tag and therefore
     * these menu div tags need to have style property associated with them to
     * ensure that the div does not cause a line break to happen i.e.
     * display:inline. This style property is generated internally at runtime
     * should this flag be set to true.
     */
    protected boolean usesDivForMenus = false;

    /**
     * Indicates whether or not the current device supports dialling links. If
     * this is false, the {@link #diallingLinkInfoType} and {@link
     * #diallingLinkInfo} values are not relevant.
     */
    protected boolean supportsDiallingLinks = false;

    /**
     * Indicates, if {@link #supportsDiallingLinks} is <code>true</code>, the
     * type of the {@link #diallingLinkInfo} property.
     */
    protected DiallingLinkInfoType diallingLinkInfoType = null;

    /**
     * Provides, if {@link #supportsDiallingLinks} is <code>true</code>, the
     * associated dialling link information. This must be interpreted in
     * conjunction with {@link #diallingLinkInfoType}.
     */
    protected String diallingLinkInfo = null;

    /**
     * Flag which specifies if the protocol supports targetting of fragment
     * link lists into specific panes, rather than just appending them after
     * the content of their fragment. This affects the order of rendering of
     * the fragment content versus the linklist. Initially only used by VDXML.
     */
    protected boolean supportsFragmentLinkListTargetting = false;

    /**
     * The PageHead.
     */
    protected PageHead pageHead;

    /**
     * The CanvasAttributes.
     */
    protected CanvasAttributes canvasAttributes;

    /**
     * VolantisProtocol constructor
     */
    protected final String[] quoteTable = new String[255];

    /**
     * The <code>Writer</code> to use to write normal text to the current
     * output buffer.
     */
    protected OutputBufferWriter contentWriter;


    /**
     * The <code>Writer</code> to use to write native markup text to the
     * current output buffer.
     */
    protected Writer nativeWriter;

    /**
     * Used to indentify the type of page being processed (canvas, montage
     * etc.)
     */
    private int pageType;

    /**
     * The maximum page size.
     */
    protected int maxPageSize;

    /**
     * The character encoder to use to encode output in this protocol.
     */
    protected CharacterEncoder characterEncoder;

    /**
     * The unicode representation for &nbsp; which should be used as we don't
     * want to output literal characters for things like WMLC
     */
    public static final String NBSP = "\u00a0";

    /**
     * This protocols configuration.
     */
    private final ProtocolConfiguration protocolConfiguration;

    /**
     * Flag which specifies whether the protocol supports fragmentation or not.
     * <p> We default this to false for safety.
     */
    protected boolean supportsFragmentation = false;


    /**
     * The "default" fragment link renderer for this protocol. <p> This will
     * never be set if fragmentation is not supported.
     */
    private FragmentLinkRenderer fragmentLinkRenderer;

    /**
     * The fragment link renderer for use with those fragment links which have
     * a mariner-link-style value of numeric-shortcut. <p> This will never be
     * set if fragmentation is not supported.
     */
    private FragmentLinkRenderer numericShortcutFragmentLinkRenderer;

    /**
     * The fragment link renderer context for this protocol. <p> There should
     * be a single context for all fragment link renderers that a protocol
     * uses, since that context is just a proxy back onto the protocol itself.
     * <p> This will never be set if fragmentation is not supported.
     */
    private FragmentLinkRendererContext fragmentLinkRendererContext;

    /**
     * The part of the protocol that supports menus.
     */
    private MenuModule menuModule;

    /**
     * Contextual information for menus.
     */
    protected RendererContext rendererContext;

    /**
     * Customisation information for the menu module.
     */
    private MenuModuleCustomisation menuModuleCustomisation;

    /**
     * Compiled style sheet with the default values.
     */
//    @todo Currently only the WMLRoot protocol supports this style of storing
//    default values, but more expected. If other protocols are converted to
//    this style then code from WMLRoot need to be pulled up.
    protected CompiledStyleSheet compiledDefaultStyleSheet;

    /**
     * A reference to the CSS.
     */
    protected CssReference cssReference;

    /**
     * This flag indicates whether a protocol supports nested tables. This is
     * then used during table optimisation to decide if tables should be
     * optimised away, irrespective of any optimisation setting in the layout.
     */
    protected boolean supportsNestedTables;

    /**
     * The style of output this protocol will generate, either TEXT or BINARY.
     * <p>
     * Initially at least most are text, WMLC is binary.
     */
    private ContentStyle outputStyle;

    /**
     * An instance of the Ticker Response Module - extension
     * responsible for processing TickerResponse markup.
     * Instantiated on first access. 
     */
    private TickerResponseModule tickerResponseModule;

    /*
    * Initialise.
    *
    * @param protocolConfiguration The protocol specific configuration, may
    * not be null.
    */
    protected VolantisProtocol(ProtocolConfiguration protocolConfiguration) {

        this.protocolConfiguration = protocolConfiguration;

        this.protocolThemeStylesheetPreference = StylesheetRenderMode.DEFAULT;

        quoteTable['<'] = "&lt;";
        quoteTable['>'] = "&gt;";
        quoteTable['\"'] = "&quot;";
        quoteTable['&'] = "&amp;";
        quoteTable['@'] = "&#64;";
        quoteTable[163] = "&#163;";
        quoteTable[172] = "&not;";

        writeHead = true;

        characterEncoder = new ProtocolCharacterEncoder(this);

        // By default we assume that most protocols support nested tables.
        // This may be updated by subclass constructors to provide a
        // different default value for that protocol and is also potentially
        // updated by the initialise method depending on what it set in the
        // device database.
        supportsNestedTables = true;

    }

    /**
     * Returns the value of the usesDivForMenus flag.
     *
     * @return usesDivForMenus.
     */
    public boolean usesDivForMenus() {
        return usesDivForMenus;
    }

    /**
     * Initialise the protocol. This is called after the context and theme are
     * set and after the context has been fully initialised.
     */
    public void initialise() {

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising page resources associated with "
                         + this);
        }

        // Default maximum page size is -1.
        maxPageSize = -1;

        initialiseDiallingLink();

        // Determine the stylesheet's location preferences
        if ((preferredLocationForThemeStylesheets =
            StylesheetRenderMode.get(context.getDevicePolicyValue(
                DevicePolicyConstants.STYLESHEET_LOCATION_THEME))) ==
            null) {
            preferredLocationForThemeStylesheets =
                StylesheetRenderMode.DEFAULT;
        }

        initialiseAccessKeySupport();

        // Handle device override of the default value
        final String propertyValue = context.getDevicePolicyValue(
                DevicePolicyConstants.NESTED_TABLE_SUPPORT);

        if ((propertyValue != null) &&
            !DevicePolicyConstants.NESTED_TABLE_SUPPORT_DEFAULT.equalsIgnoreCase(propertyValue)) {
            // The device policy value is different to the default nested tables
            // support for the protocol, so override the protocol accordingly
            supportsNestedTables =  Boolean.valueOf(propertyValue).booleanValue();

            if (logger.isDebugEnabled()) {
                logger.debug("Feature "+ DevicePolicyConstants.NESTED_TABLE_SUPPORT+" support explicitly set to " +
                             propertyValue + " by the device");
            }
        }


        if (logger.isDebugEnabled()) {
            logger.debug("Initialising (VolantisProtocol) " + this);
        }
    }

    /**
     * Initialise dialling link support.
     *
     * <p>This has been moved out of the main initialise method in order to
     * allow it to be tested.</p>
     */
    protected void initialiseDiallingLink() {
        // Determine whether the device supports dialling links and, if so,
        // set up the other dialling link information
        supportsDiallingLinks = context.
            getBooleanDevicePolicyValue(
                DevicePolicyConstants.SUPPORTS_LINK_DIALLING);

        if (supportsDiallingLinks) {
            diallingLinkInfoType =
                DiallingLinkInfoType.get(context.getDevicePolicyValue(DevicePolicyConstants.
                                                                      LINK_DIALLING_INFO_TYPE));

            if (diallingLinkInfoType == null) {
                diallingLinkInfoType = DiallingLinkInfoType.NONE;
            } else {
                diallingLinkInfo =
                    context.getDevicePolicyValue(DevicePolicyConstants.
                                                 LINK_DIALLING_INFO);
            }
        }
    }

    /**
     * Initialise access key support.
     *
     * <p>This has been moved out of the main initialise method in order to
     * allow it to be tested.</p>
     */
    protected void initialiseAccessKeySupport() {
        // Handle device override of the accesskey support
        String accesskeySupport = context.getDevicePolicyValue(
            DevicePolicyConstants.ACCESSKEY_SUPPORTED);

        if ((accesskeySupport != null) &&
            !DevicePolicyConstants.ACCESSKEY_SUPPORT_DEFAULT.
            equalsIgnoreCase(accesskeySupport)) {
            // The device policy value is different to the default accesskey
            // support for the protocol, so override the protocol accordingly
            supportsAccessKeyAttribute = Boolean.valueOf(accesskeySupport).
                booleanValue();

            if (logger.isDebugEnabled()) {
                logger.debug("Accesskey support explicitly set to " +
                             supportsAccessKeyAttribute + " by the device");
            }
        }
    }

    /**
     * Get this protcol's configuration
     *
     * @return this protocol's configuration (which may be null).
     */
    public ProtocolConfiguration getProtocolConfiguration() {
        return protocolConfiguration;
    }

    /**
     * This method is called at the end of the MarinerPageContext's
     * initialiseCanvas method which is called during the processing of a
     * canvas or montage element. <p> Code which needs access to parts of the
     * MarinerPageContext which are initialised in the initialiseCanvas method
     * should be added here rather than in the initialise method. </p>
     */
    public void initialiseCanvas() {

        setAlignTableCellSupport();

        setCenterElementSupport();

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising canvas resources associated with "
                         + this);
        }

        createWriters();

        // If this is an included page we must get the page header of
        // of our including page so that our header information, such as
        // validation JavaScript is written out.
        if (pageHead == null) {
            initialisePageHead();
        }
    }

    /**
     * Initialise {@link #contentWriter} and {@link #nativeWriter}
     * appropriately for this protocol.
     */
    protected void createWriters() {

        // Create a writer for writing normal protocol text into the protocol's
        // current output buffer.
        contentWriter = new ProtocolWriter(this);

        // By default, the native writer is identical to the normal writer.
        // Subclasses may deal with native writers differently.
        nativeWriter = contentWriter;
    }

    /**
     * Allocate and initialise a new page head.
     */
    protected void initialisePageHead() {
        pageHead = new PageHead();
        pageHead.setOutputBufferFactory(getOutputBufferFactory());
    }

    /**
     * Set the value of the dissecting property.
     *
     * @param dissecting The new value of the dissecting property.
     */
    public void setDissecting(boolean dissecting) {
        this.dissecting = dissecting;
    }

    /**
     * Get the value of the dissecting property.
     *
     * @return The value of the dissecting property.
     */
    public boolean isDissecting() {
        return dissecting;
    }

    /**
     * Set the value of the dissection possible property.
     *
     * @param dissectionNeeded The new value of the dissection possible
     *                         property.
     */
    public void setDissectionNeeded(boolean dissectionNeeded) {
        this.dissectionNeeded = dissectionNeeded;
    }

    /**
     * Get the value of the dissection possible property.
     *
     * @return The value of the dissection possible property.
     */
    public boolean isDissectionNeeded() {
        return dissectionNeeded;
    }

    /**
     * Set the value of the canvas attributes property.
     *
     * @param canvasAttributes The new value of the canvas attributes
     *                         property.
     */
    public void setCanvasAttributes(CanvasAttributes canvasAttributes) {
        this.canvasAttributes = canvasAttributes;
    }

    /**
     * Get the value of the canvas attributes property.
     *
     * @return The value of the canvas attributes property.
     */
    public CanvasAttributes getCanvasAttributes() {
        return canvasAttributes;
    }

    public void setInclusion(boolean b) {
        inclusion = b;
    }

    /**
     * Get the value of the context property.
     *
     * @return The value of the context property.
     */
    public MarinerPageContext getMarinerPageContext() {
        return context;
    }

    public void setMarinerPageContext(MarinerPageContext context) {
        this.context = context;
    }

    public void setWriteHead(boolean b) {
        writeHead = b;
    }

    public boolean getWriteHead() {
        return writeHead;
    }

    public abstract OutputBufferFactory getOutputBufferFactory();

    public PageHead getPageHead() {
        if (pageHead == null) {
            initialisePageHead();
        }
        return pageHead;
    }


    /**
     * Get widget module for current protocol
     * Default implementation returns null, as by default
     * ClientFramework is not supported
     * @return WidgetModule
     */
    public WidgetModule getWidgetModule(){
        return null;
    }
    
    /**
     * Get ticker module for current protocol
     * Default implementation returns null, as by default
     * Ticker Client is not supported
     * @return TickerModule
     */
    public TickerModule getTickerModule(){
        return null;
    }
    
    /**
     * Get gallery module for current protocol
     * Default implementation returns null, as by default
     * Gallery is not supported
     * @return GalleryModule
     */
    public GalleryModule getGalleryModule(){
        return null;
    }
    
    /**
     * Returns Ticker Response Module for current protocol.
     * 
     * @return TickerResponseModule
     * @throws ProtocolException
     */
    public TickerResponseModule getTickerResponseModule() {
        if (tickerResponseModule == null) {
            tickerResponseModule = createTickerResponseModule();
        }

        return tickerResponseModule;
    }

    /**
     * Creates an instance of TickerResponseModule, for use with this protocol.
     * This method is invoked once, during protocol initialization.
     * 
     * @return An instance of TickerResponseModule.
     * @throws ProtocolException
     */
    protected TickerResponseModule createTickerResponseModule() {
        try {
            // Because VolantisTickerResponseModule is defined in 'impl',
            // it needs to instantiated using reflection.
            return (TickerResponseModule) Class.forName(
                    "com.volantis.mcs.protocols.ticker.response.VolantisTickerResponseModule")
                    .getConstructor(new Class[] {VolantisProtocol.class})
                    .newInstance(new Object[] {this});
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate VolantisTickerResponseModule", e);
        }
    }

    /**
     * Writes the elements needed for a client to perform a timed refresh of
     * its page.
     *
     * @param tri the refresh info. Must not be null?
     * @throws ProtocolException if error occurs
     */
    public void writeTimedRefresh(TimedRefreshInfo tri) 
            throws ProtocolException {
    }

    /**
     * set the page type
     *
     * @param pageType the type of page being processed
     */
    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    /**
     * Is the page of a given type
     *
     * @param pageType the page type
     * @return a true if page is of the given type
     */
    public boolean isPageType(int pageType) {
        return (this.pageType == pageType);
    }

    public StyleSheetRenderer getStyleSheetRenderer() {
        return styleSheetRenderer;
    }

    /**
     * The compiled style sheet containing the default styles. May return
     * <code>null</code> indicating that no default styles were set for the
     * given protocol.
     *
     * @return the compiled style sheet or <code>null</code>
     */
    public CompiledStyleSheet getCompiledDefaultStyleSheet() {
        return compiledDefaultStyleSheet;
    }

    /**
     * @see #outputStyle
     */
    public ContentStyle getOutputStyle() {
        if (outputStyle == null) {
            // Calculate the output style lazily.
            //
            // This is required for WMLRoot so it can get access to the charset
            // which is not currently set until after protocol initialisation.
            outputStyle = calculateOutputStyle();
        }
        return outputStyle;
    }

    /**
     * This method will be called to calculate the output style lazily.
     * <p>
     * This is guaranteed to be called at most once.
     *
     * @return the calculated output style.
     */
    protected ContentStyle calculateOutputStyle() {
        return ContentStyle.TEXT;
    }


    // ========================================================================
    //   General helper methods
    // ========================================================================

    /**
     * Helper method to get the text from the reference in the specified encoding.
     * If the reference is not a TextComponentName then the result is the value
     * returned from the reference's toString method, otherwise the text asset for
     * the current device is retrieved. If the asset could not be found, or the
     * asset's encoding does not match the required encoding then it returns
     * null, otherwise it returns the text associated with the asset.
     *
     * @param reference   The reference from which the text is to be retrieved.
     * @param encoding The required encoding of the retrieved text.
     * @return The text associated with the matching text asset, or null.
     */
    protected String getTextFromReference(TextAssetReference reference,
                                       TextEncoding encoding) {
        return reference == null ? null : reference.getText(encoding);
    }

    /**
     * Get plain text from the reference.
     *
     * @param reference   The reference from which the text is to be retrieved.
     * @return The text associated with the matching text asset, or null.
     */
    protected String getPlainText(TextAssetReference reference) {
        return reference == null ? null : reference.getText(TextEncoding.PLAIN);
    }

    /**
     * Get the link associated with the object. If is is a LinkComponentName
     * object then the value is the text value retrieved from the link
     * component, otherwise the value is the result of calling the toString
     * method on the object.
     *
     * @param object the object that contains the link info
     * @return the Link as a String
     */
    protected String getLinkFromReference(LinkAssetReference object) {
        return getRewrittenLinkFromObject(object, false);
    }

    /**
     * Get the link associated with the reference.
     *
     * @param reference           the object that contains the link info
     * @param encodeSegmentURL if true encode the link with default segment
     *                         info.
     * @return the Link as a String
     */
    public String getRewrittenLinkFromObject(
            LinkAssetReference reference,
            boolean encodeSegmentURL) {
        String link = null;

        if (reference != null) {
            link = reference.getURL();

            if (link != null && encodeSegmentURL) {
                link = encodeSegmentURL(link);
            }
        }

        return link;
    }

    /**
     * If the Object paramemter is an intsance of LinkComponentName then
     * retrieve the text from any fallback text component that might be
     * specified via the link component
     *
     * @param object the object the specifies the link. Is either an instance
     *               of a LinkComponentName  or String
     * @return a <code>String</code> value
     */
    protected String getTextFallbackFromLink(LinkAssetReference object) {
        String text = null;
        // if object must be a LinkComponentName
        if (object != null) {
            TextAssetReference textReference = object.getTextFallback();
            if (textReference != null) {
                text = textReference.getText(TextEncoding.PLAIN);
            }
        }
        return text;
    }

    /**
     * Encode the query string a URL with the information to the default
     * segment
     *
     * @param url the url to be encoded
     * @return the encoded url
     */
    protected String encodeSegmentURL(String url) {

        String value = null;
        value = context.getDevice().getPolicyValue("aggregation");

        boolean supportsAggregation = (value != null
            && value.equalsIgnoreCase("true"));

        // If aggregation is not supported then we need to add the default
        // segment URL to the url.
        MarinerURL requestURL = context.getRequestURL(false);
        if (!supportsAggregation) {
            String defaultSegmentURL = requestURL.getParameterValue
                (URLConstants.SEGMENTATION_PARAMETER);

            // If no default segment url is specified then assume that the
            // current page is the default segment.
            if (defaultSegmentURL == null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("No default segment url specified,"
                                 + " defaulting to current");
                }
                defaultSegmentURL = requestURL.getExternalForm();
            }

            MarinerURL encodedURL = new MarinerURL(url);
            encodedURL.setParameterValue(URLConstants.SEGMENTATION_PARAMETER,
                                         defaultSegmentURL);
            url = encodedURL.getExternalForm();
        }

        return url;
    }

    /**
     * Return the appropriate fragment link renderer, given the attributes
     * provided, or null if fragmentation is not supported for this protocol.
     * <p> Often protocols only have one renderer, but we pass in the
     * attributes for those few that do wish to be a bit more tricky. <p> The
     * renderer will be created using lazy evaluation at this point if it does
     * not already exist.
     *
     * @param attrs the attributes of the fragment link to be rendered
     * @return the appropriate fragment link renderer.
     * @see #createFragmentLinkRenderer
     */
    protected FragmentLinkRenderer getFragmentLinkRenderer(
        FraglinkAttributes attrs) {

        if (!supportsFragmentation) {
            // Not supported, so just return null.
            return null;
        }

        FragmentLinkRenderer renderer = null;

        // If the style for this fragment has a "mariner link" style value
        // of "numeric shortcut"...
        Styles styles = attrs.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_LINK_STYLE);
        if (value == MCSLinkStyleKeywords.NUMERIC_SHORTCUT) {
            // Then use the numeric-shortcut fragment link renderer.
            if (logger.isDebugEnabled()) {
                logger.debug("Using numeric-shortcut fragment link rendering");
            }
            // Lazily create the numeric-shortcut renderer if necessary.
            if (numericShortcutFragmentLinkRenderer == null) {
                numericShortcutFragmentLinkRenderer =
                    createNumericShortcutFragmentLinkRenderer();
            }
            // Return the numeric-shortcut renderer.
            renderer = numericShortcutFragmentLinkRenderer;
        }

        // If we couldn't find a alternative renderer...
        if (renderer == null) {
            // Fall back to the normal fragment link renderer.
            if (logger.isDebugEnabled()) {
                logger.debug("Using standard fragment link rendering");
            }
            // Lazily create the default renderer if necessary.
            if (fragmentLinkRenderer == null) {
                fragmentLinkRenderer = createFragmentLinkRenderer();
            }
            // Return the default renderer.
            renderer = fragmentLinkRenderer;
        }

        return renderer;

    }

    /**
     * Factory Method to create an instance of the "default" fragment link
     * renderer for this protocol.
     *
     * @return the newly created fragment link renderer.
     * @see #getFragmentLinkRenderer
     */
    private FragmentLinkRenderer createFragmentLinkRenderer() {

        if (!supportsFragmentation) {
            throw new IllegalStateException("Cannot create renderer " +
                                            "if fragmentation is not supported");
        }

        return new DefaultFragmentLinkRenderer(
            getFragmentLinkRendererContext());
    }

    /**
     * Factory Method to create an instance of the numeric-shortcut fragment
     * link renderer for this protocol.
     *
     * @return the newly created fragment link renderer, or null if this
     *         protocol does not support the numeric-shortcut style of
     *         rendering for fragment links.
     * @see #getFragmentLinkRenderer
     */
    protected FragmentLinkRenderer createNumericShortcutFragmentLinkRenderer() {

        if (!supportsFragmentation) {
            throw new IllegalStateException("Cannot create renderer " +
                                            "if fragmentation is not supported");
        }

        return null;
    }

    /**
     * Return the fragment link renderer context for this protocol, or null if
     * fragmentation is not supported for this protocol. <p> This context will
     * be created using lazy evaluation at this point if it does not already
     * exist.
     *
     * @return the fragment link renderer context.
     * @see #createFragmentLinkRendererContext
     */
    protected FragmentLinkRendererContext getFragmentLinkRendererContext() {

        if (supportsFragmentation) {
            if (fragmentLinkRendererContext == null) {
                fragmentLinkRendererContext =
                    createFragmentLinkRendererContext();
            }
            return fragmentLinkRendererContext;
        } else {
            // Not supported, so just return null.
            return null;
        }

    }

    /**
     * Factory Method to create an instance of the fragment link renderer
     * context for this protocol. <p> Protocols which support fragmentation
     * must override this method to provide an appropriate context.
     *
     * @return the fragment link renderer context created.
     * @see #getFragmentLinkRenderer
     */
    protected FragmentLinkRendererContext
        createFragmentLinkRendererContext() {

        throw new IllegalStateException("must be overridden");

    }


    // ========================================================================
    //   Page element methods
    // ========================================================================


    /**
     * Open the canvas page
     */
    public abstract void openCanvasPage(CanvasAttributes attributes)
        throws IOException, ProtocolException;


    /**
     * Open AJAXResponse page - without HTML, HEAD and BODY elements
     * @param attributes
     * @throws IOException
     * @throws ProtocolException
     */
    public void openAJAXResponsePage(ResponseBodyAttributes attributes)
        throws IOException, ProtocolException{

    }



    /**
     * Close the canvas page.
     */
    public void closeCanvasPage(CanvasAttributes attributes)
        throws IOException,
        ProtocolException {
        packagePage(attributes);
    }


    /**
     * Close the canvas page.
     */
    public void closeAJAXResponsePage(ResponseBodyAttributes attributes)
        throws IOException,
        ProtocolException {
        packagePage(attributes);
    }

    /**
     * Open the inclusion page.
     */
    public abstract void openInclusionPage(CanvasAttributes attributes)
        throws IOException, ProtocolException;

    /**
     * Close the inclusion page.
     */
    public abstract void closeInclusionPage(CanvasAttributes attributes)
        throws IOException, ProtocolException;

    /**
     * Open the montage page.
     */
    public abstract void openMontagePage(MontageAttributes attributes)
        throws IOException;

    /**
     * Close the montage page.
     */
    public void closeMontagePage(MontageAttributes attributes)
        throws IOException, ProtocolException {
        packagePage(attributes);
    }

    /**
     * Close the canvas page and generate the page content, writing it to the
     * given writer.
     *
     * @param output     the output to which the content is to be written
     * @param attributes the Canvas attributes
     * @throws IOException       if a problem is encountered generating the
     *                           page
     * @throws ProtocolException if a protocol-specific problemn is
     *                           encountered
     */
    protected abstract void writeCanvasContent(PackageBodyOutput output,
                                               CanvasAttributes attributes)
        throws IOException, ProtocolException;

    /**
     * Close the montage page and generate the page content, writing it to the
     * given writer.
     *
     * @param output     the output to which the content is to be written
     * @param attributes the Montage attributes
     * @throws IOException       if a problem is encountered generating the
     *                           page
     * @throws ProtocolException if a protocol-specific problemn is
     *                           encountered
     */
    protected abstract void writeMontageContent(PackageBodyOutput output,
                                                MontageAttributes attributes)
        throws IOException, ProtocolException;



    /**
     * Close the AJAX response generate the page content, writing it to the
     * given writer.
     *
     * @param output     the output to which the content is to be written
     * @param attributes the Montage attributes
     * @throws IOException       if a problem is encountered generating the
     *                           page
     * @throws ProtocolException if a protocol-specific problemn is
     *                           encountered
     */
    protected void writeAJAXResponseContent(PackageBodyOutput output,
                ResponseBodyAttributes attributes)
        throws IOException, ProtocolException{
    }


    /**
     * Returns the mime type that should be used for this protocol, accounting
     * for any device-specific mime type requirements.
     *
     * @return the protocol's device-specific mime type
     */
    public String mimeType() {
        String mimeType = context.getDevicePolicyValue(
            DevicePolicyConstants.PROTOCOL_MIME_TYPE);

        if ((mimeType == null) || ("".equals(mimeType))) {
            mimeType = defaultMimeType();
        } else if (mimeType.indexOf('/') == -1) {
            logger.warn("policy-value-invalid",
                        new Object[]{DevicePolicyConstants.PROTOCOL_MIME_TYPE,
                                     mimeType, context.getDeviceName()});
            mimeType = defaultMimeType();
        } else {
            return mimeType;
        }

        if (mimeType != null && mimeType.length() > 0 &&
            mimeType.indexOf('/') == -1) {
            throw new IllegalStateException("Default mime type is not " +
                                            "valid as it does not contain a '/' character.");
        }

        if (mimeType == null) {
            throw new IllegalStateException("Default mime type is null. " +
                                            "Should contain a valid mime type or an empty string.");
        }

        return mimeType;
    }

    /**
     * Returns the default mime type associated with this protocol. For
     * concrete classes that do not have an appropriate mime type to associate
     * with them, an empty String should be returned. Malformed or null return
     * values from this method will lead to an IllegalStateException being
     * thrown when the mime type is requested.
     *
     * @return the default mime type associated with this protocol
     */
    public abstract String defaultMimeType();

    /**
     * If this protocol has a specific value defined for packaging mime type,
     * then return it otherwise return null.
     *
     * @return the packaging mime type for a particular protocol.
     */
    public String getPackagingType() {
        return null;
    }

    public void writeLayout(DeviceLayoutContext deviceLayoutContext)
        throws IOException, ProtocolException {

        DeviceLayoutRenderer renderer = DeviceLayoutRenderer.getSingleton();

        MarinerPageContext pageContext = getMarinerPageContext();

        FormatRendererContext formatRendererContext =
                new FormatRendererContextImpl(
                        pageContext, StylingFactory.getDefaultInstance());

        try {
            renderer.renderLayout(deviceLayoutContext, formatRendererContext);
        } catch (RendererException e) {
            throw new ProtocolException(
                exceptionLocalizer.format("layout-rendering-error"),
                e);
        }
    }

    /**
     * Write out the style sheet or a link to a style sheet to the head
     * buffer.
     *
     * @throws IOException if there is a problem writing to the buffer.
     * @todo later generate import URL for layout stylesheets
     * @todo later should all direct calls to writeCss be explicitly wrapped
     * within a style element?
     * @todo later should there be tests for out == null for import writeCss?
     */
    public void writeStyleSheet()
        throws IOException {
        // All the actual candidate generation and/or writeCss calls are
        // based on the original re-factored code. There are definitely
        // issues with some of the processing, as identified by the todos.
        if (styleSheetRenderer == null) {
            // Since there is no style sheet renderer do nothing
            if (logger.isDebugEnabled()) {
                logger.debug("Cannot write style sheets as there " +
                             "is no renderer");
            }
        } else {
            // Determine the rendering styles that are allowed
            StyleSheetConfiguration configuration =
                context.getVolantisBean().getStyleSheetConfiguration();

            final boolean externalable =
                (configuration.getSupportsExternal() &&
                supportsExternalStyleSheets);

            final boolean internalable = supportsInlineStyles;

            // Only proceed if it is actually possible to render stylesheets
            // in one or other way.
            if (externalable || internalable) {

                // Determine the specified stylesheet render mode.
                StylesheetRenderMode specifiedMode = getSpecifiedRenderMode(
                        preferredLocationForThemeStylesheets,
                        protocolThemeStylesheetPreference,
                        configuration.getPreferredLocation());

                // Determine the stylesheet renderMode mode
                StylesheetRenderMode renderMode = determineActualRenderMode(
                        specifiedMode, externalable, internalable);

                if (logger.isDebugEnabled()) {
                    logger.debug("Stylesheet to be rendered as " +
                            renderMode.toString() + " stylesheet");
                }

                // As part of seting the way in which the stylesheets will be
                // rendered the cache scope variable will also be set on the
                // request here. This is because the cache scope value is
                // dependant on whether the stylesheet is rendered internally
                // or not. This is a hack to improve the page caching
                // mechanisum.
                // page caching can be used iff the style sheet is rendered
                // internally.
                boolean canCachePage = false;

                // New theme code.
                if (StylesheetRenderMode.INTERNAL == renderMode) {
                    cssReference = createInternalCSSReference();
                    canCachePage = true;
                } else if (StylesheetRenderMode.EXTERNAL == renderMode) {
                    cssReference = createExternalCSSReference();
                } else {
                    throw new IllegalStateException("Unexpected rendering" + 
                            "mode encountered");
                }

                // Add a reference to the head.
                if (cssReference != null) {
                    getPageHead().addURLCssCandidate(
                        new CssCandidateAdapter(cssReference));
                }

                setPageCacheAttribute(canCachePage);
            }
        }
    }

    /**
     * Determines how a given preference should be rendered given the device
     * capabilities and the specified rendering mode.
     *
     * @param specifiedMode         the specified stylesheet rendering mode -
     *                              either {@link StylesheetRenderMode#EXTERNAL}
     *                              or {@link StylesheetRenderMode#INTERNAL}
     * @param supportsExternal      true if the stylesheet can be rendered as
     *                              an external stylesheet
     * @param supportsInternal      true if the stylesheet can be rendered
     *                              internally (inline)
     * @return one of StylesheetRenderMode.EXTERNAL,
     * StylesheetRenderMode.INTERNAL
     */
    protected StylesheetRenderMode determineActualRenderMode(
            StylesheetRenderMode specifiedMode,
            final boolean supportsExternal,
            final boolean supportsInternal) {

        // Verify that this agrees with the device's capabilities e.g. if
        // internal has been specified, but the device only supports external,
        // then use external rather than doing something that won't work.
        if (specifiedMode == StylesheetRenderMode.INTERNAL) {
            if (supportsInternal) {
                // Leave the result as-is.
            } else if (supportsExternal) {
                // fall back to using external stylesheets
                specifiedMode = StylesheetRenderMode.EXTERNAL;
                if (logger.isDebugEnabled()) {
                    logger.debug("Falling back to using INTERNAL stylesheet " +
                            "rendering because the specified mode is not " +
                            "supported.");
                }
            } else {
                throw new IllegalStateException("No rendering options are " +
                        "enabled");
            }
        } else if (specifiedMode == StylesheetRenderMode.EXTERNAL) {
            if (supportsExternal) {
                // Leave the result as-is
            } else if (supportsInternal) {
                // fall back to using inline stylesheets
                specifiedMode = StylesheetRenderMode.INTERNAL;
                if (logger.isDebugEnabled()) {
                    logger.debug("Falling back to using EXTERNAL stylesheet" + 
                            "rendering because the specified mode is not " +
                            "supported.");
                }
            } else {
                throw new IllegalStateException("No rendering options are " +
                        "enabled");
            }
        } else {
            throw new IllegalStateException("Unexpected rendering mode " +
                    "encountered");
        }

        return specifiedMode;
    }

    /**
     * Determines how stylesheets should be rendered. It will be either:
     * <ol>
     * <li>the value specified in the device repository</li>
     * <li>the value hard coded in the protocol</li>
     * <li>the value specified in the MCS configuration file.</li>
     * </ol>
     * falling back in that order if the values are not set or default.
     *
     * @param devicePreference      From the device repository, can be one of
     *                              StylesheetRenderMode.DEFAULT,
     *                              StylesheetRenderMode.EXTERNAL,
     *                              StylesheetRenderMode.INTERNAL
     * @param protocolPreference    Hard coded into the protocol, can be one of
     *                              StylesheetRenderMode.DEFAULT,
     *                              StylesheetRenderMode.EXTERNAL,
     *                              StylesheetRenderMode.INTERNAL
     * @param globalPreference  From the mcs configuration file, can be one
     *                              of StyleSheetConfiguration.EXTERNAL,
     *                              StyleSheetConfiguration.INLINE
     * @return one of StylesheetRenderMode.EXTERNAL,
     * StylesheetRenderMode.INTERNAL, StylesheetRenderMode.IMPORT
     */
    protected StylesheetRenderMode getSpecifiedRenderMode(
            final StylesheetRenderMode devicePreference,
            final StylesheetRenderMode protocolPreference,
            final int globalPreference) {

        StylesheetRenderMode result = devicePreference;

        // If the device did not specify a non default value then use the value
        // hard coded in the protocol.
        if (result == StylesheetRenderMode.DEFAULT) {
            result = protocolPreference;
        }

        // If the protocol didn't specify a value then fall back to use the
        // global preference (specified in the MCS config file).
        if (result == StylesheetRenderMode.DEFAULT) {
            if (globalPreference == StyleSheetConfiguration.EXTERNAL) {
                result = StylesheetRenderMode.EXTERNAL;
            } else if (globalPreference ==
                    StyleSheetConfiguration.INLINE) {
                result = StylesheetRenderMode.INTERNAL;
            } else {
                logger.warn("default-rendering-mode-unknown");
            }
        }

        // If no global preference was specified in the mcs config file, then
        // the style sheet configuration will fall back to specify inline
        // rendering, so we don't need to do it explicitly here.

        return result;
    }

    protected CssReference createExternalCSSReference() {
        return null;
    }

    protected CssReference createInternalCSSReference() {
        return null;
    }

    /**
     * Set the an attribute on the request to show whether the page can should
     * be cached
     * @param canCache - true iff the css reference is internal only
     */
    private void setPageCacheAttribute(boolean canCache) {
        EnvironmentContext environmentContext =
                getMarinerPageContext().getEnvironmentContext();

        if (canCache) {
            environmentContext.setAttribute(
                    CacheScopeConstant.CACHE_SCOPE_ATTRIBUTE,
                    CacheScopeConstant.CAN_CACHE_PAGE);
        } else {
            environmentContext.setAttribute(
                    CacheScopeConstant.CACHE_SCOPE_ATTRIBUTE,
                    CacheScopeConstant.CAN_NOT_CACHE_PAGE);
        }
    }

    /**
     * Write the open body markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenBody(BodyAttributes attributes)
        throws IOException, ProtocolException {
    }

    /**
     * Write the close body markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseBody(BodyAttributes attributes)
        throws IOException {
    }

    /**
     * Write the open canvas markup directly to the initial page head buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenCanvas(CanvasAttributes attributes)
        throws IOException, ProtocolException {
    }

    /**
     * Write the close canvas markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseCanvas(CanvasAttributes attributes)
        throws IOException {
    }

    /**
     * Write the open inclusion markup to the device layout context.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenInclusion(CanvasAttributes attributes)
        throws IOException, ProtocolException {
    }

    /**
     * Write the close inclusion markup to the device layout context.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseInclusion(CanvasAttributes attributes)
        throws IOException, ProtocolException {
    }

    public void beginNestedInclusion() {
    }

    public void endNestedInclusion() {
    }

    /**
     * Write the standard volantis header to the page.
     */
    public void writeInitialHeader() {
    }

    /**
     * Write the open montage markup directly to the initial page header
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenMontage(MontageAttributes attributes)
        throws IOException {
    }

    /**
     * Write the close montage markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseMontage(MontageAttributes attributes)
        throws IOException {
    }

    public void writeProtocolString() {
    }

    public void writeProtocolString(Document document) {
    }

    public void writeOpenStyle(OutputBuffer out,
                               StyleAttributes attributes) {
    }

    public void writeCloseStyle(OutputBuffer out,
                                StyleAttributes attributes) {
    }

    public void writeOpenSlide(SlideAttributes attributes) {
    }

    public void writeCloseSlide(SlideAttributes attributes) {
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    /**
     * Get the maximum page size, this is specified in the device context but
     * it is different for the different protocol types.
     */
    public int getMaxPageSize() {
        return maxPageSize;
    }

    /**
     * Encode the character according to the rules for the protocol.
     *
     * @param c The character to encode.
     * @return A replacement string if the character is special, or null if it
     *         is not.
     */
    public String encodeCharacter(int c) {

        // Map from the character to some replacement text.
        if (c < quoteTable.length && quoteTable[c] != null) {
            return quoteTable[c];
        } else {
            return null;
        }
    }

    /**
     * Return the character encoder used to encode the output of this
     * protocol.
     */
    public CharacterEncoder getCharacterEncoder() {
        return characterEncoder;
    }

    // ========================================================================
    //   Text methods
    // ========================================================================

    public OutputBufferWriter getDirectWriter () {
        throw new UnsupportedOperationException();
    }

    public OutputBufferWriter getContentWriter() {
        return contentWriter;
    }

    public Writer getNativeWriter() {
        return nativeWriter;
    }

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    public void writeOpenSpatialFormatIterator(
        SpatialFormatIteratorAttributes attributes) {
    }

    public void writeOpenSpatialFormatIteratorRow(
            SpatialFormatIteratorAttributes attributes) {
    }

    public void writeOpenSpatialFormatIteratorChild(
            SpatialFormatIteratorAttributes attributes) {
    }

    public void writeCloseSpatialFormatIterator(
            SpatialFormatIteratorAttributes attributes) {
    }

    public void writeCloseSpatialFormatIteratorRow(
            SpatialFormatIteratorAttributes attributes) {
    }

    public void writeCloseSpatialFormatIteratorChild(
            SpatialFormatIteratorAttributes attributes) {
    }

    public
    void writeOpenColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
        throws IOException {
    }

    public
    void writeCloseColumnIteratorPane(ColumnIteratorPaneAttributes attributes)
        throws IOException {
    }

    public void writeOpenColumnIteratorPaneElement(
        ColumnIteratorPaneAttributes attributes)
        throws IOException {
    }

    public void writeCloseColumnIteratorPaneElement(
        ColumnIteratorPaneAttributes attributes)
        throws IOException {
    }

    public void writeColumnIteratorPaneElementContents(OutputBuffer buffer)
        throws IOException {
    }

    public
    void writeOpenDissectingPane(DissectingPaneAttributes attributes)
        throws IOException {
    }

    public
    void writeCloseDissectingPane(DissectingPaneAttributes attributes)
        throws IOException, ProtocolException {
    }

    public void writeOpenForm(FormAttributes attributes)
        throws IOException {
    }

    public void writeCloseForm(FormAttributes attributes)
        throws IOException {
    }

    public void writeFormPreamble(OutputBuffer buffer)
        throws IOException {
    }

    public void writeFormPostamble(OutputBuffer buffer)
        throws IOException {
    }

    public void writeOpenGrid(GridAttributes attributes) {
    }

    public void writeCloseGrid(GridAttributes attributes) {
    }

    public void writeOpenGridChild(GridChildAttributes attributes) {
    }

    public void writeCloseGridChild(GridChildAttributes attributes) {
    }

    public void writeOpenGridRow(GridRowAttributes attributes) {
    }

    public void writeCloseGridRow(GridRowAttributes attributes) {
    }

    public void writeOpenLayout(LayoutAttributes attributes)
        throws IOException {
    }

    public void writeCloseLayout(LayoutAttributes attributes)
        throws IOException {
    }

    public void writeOpenPane(PaneAttributes attributes)
        throws IOException {
    }

    public void writeClosePane(PaneAttributes attributes)
        throws IOException {
    }

    public void writePaneContents(OutputBuffer buffer)
        throws IOException {
    }

    public
    void writeOpenRowIteratorPane(RowIteratorPaneAttributes attributes)
        throws IOException {
    }

    public
    void writeCloseRowIteratorPane(RowIteratorPaneAttributes attributes)
        throws IOException {
    }

    public
    void writeOpenRowIteratorPaneElement(RowIteratorPaneAttributes attributes)
        throws IOException {
    }

    public void writeCloseRowIteratorPaneElement(
        RowIteratorPaneAttributes attributes)
        throws IOException {
    }

    public void writeRowIteratorPaneElementContents(OutputBuffer buffer)
        throws IOException {
    }

    public void writeOpenSegment(SegmentAttributes attributes)
        throws IOException {
    }

    public void writeCloseSegment(SegmentAttributes attributes)
        throws IOException {
    }

    public void writeOpenSegmentGrid(SegmentGridAttributes attributes)
        throws IOException {
    }

    public void writeCloseSegmentGrid(SegmentGridAttributes attributes)
        throws IOException {
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    /**
     * Write the open anchor markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenAnchor(AnchorAttributes attributes) {
    }

    /**
     * Write the close anchor markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseAnchor(AnchorAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the default segment link markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeDefaultSegmentLink(AnchorAttributes attributes)
        throws IOException, ProtocolException {
    }

    public boolean getSupportsFragmentLinkListTargetting() {
        return supportsFragmentLinkListTargetting;
    }

    /**
     * Write the fragment link markup directly to the page.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeFragmentLink(FraglinkAttributes attributes)
        throws IOException, ProtocolException {
    }

    // ========================================================================
    //   Dialling methods.
    // ========================================================================

    /**
     * Write the open phone number link markup directly to the current output
     * buffer.
     *
     * @param attributes the attributes to use when generating the markup
     */
    public void writeOpenPhoneNumber(PhoneNumberAttributes attributes) {
    }

    /**
     * Write the close phone number link markup directly to the current output
     * buffer.
     *
     * @param attributes the attributes to use when generating the markup
     */
    public void writeClosePhoneNumber(PhoneNumberAttributes attributes)
        throws ProtocolException {
    }

    /**
     * The attributes are updated to ensure that the fullNumber attribute is
     * stored as the display string (i.e. type String, rather than as a display
     * string <strong>or</string> a mariner expression i.e. a text asset) and
     * the qualified full number is determined and stored in the attributes.
     * This supporting method is here to allow all specialist protocols to
     * utilize it as needed.
     *
     * @param attributes the attributes from which the full number is obtained
     *                   and to which the full number is updated and the
     *                   qualified full number is written
     */
    protected void resolvePhoneNumberAttributes(
        PhoneNumberAttributes attributes) {
        String fullNumber;
        String qualifiedFullNumber = null;

        // Resolve any Mariner expressions in the full number specified in
        // the inbound attributes
        fullNumber = getPlainText(attributes.getFullNumber());

        // Ensure that the resolved value is stored back in the attributes
        // so that it can be used in creating default content if need be
        attributes.setDefaultContents(fullNumber);

        // Determine the qualifiedFullNumber if possible
        qualifiedFullNumber = resolveQualifiedFullNumber(fullNumber);

        // Store the qualified value for use when generating the markup
        attributes.setQualifiedFullNumber(qualifiedFullNumber);
    }

    /**
     * The given full number is converted into a qualified full number (i.e.
     * the value that must be used as the dialling number identifier) using the
     * {@link #supportsDiallingLinks}, {@link #diallingLinkInfoType} and {@link
     * #diallingLinkInfo} properties. If the full number is null or empty, null
     * is returned. This supporting method is here to allow all specialist
     * protocols to utilize it as needed.
     *
     * @param fullNumber the full number from the phone number input or null
     * @return the qualifiedFullNumber or null
     */
    protected String resolveQualifiedFullNumber(String fullNumber) {
        String qualifiedFullNumber = null;

        // Only determine a qualified value if the device supports dialling
        // links and a full number has been specified
        if (supportsDiallingLinks &&
            (fullNumber != null) &&
            (!"".equals(fullNumber))) {
            if ((diallingLinkInfoType == null) ||
                (diallingLinkInfoType == DiallingLinkInfoType.NONE)) {
                qualifiedFullNumber = fullNumber;
            } else if (diallingLinkInfoType == DiallingLinkInfoType.PREFIX) {
                if (diallingLinkInfo != null) {
                    StringBuffer qfn =
                        new StringBuffer(diallingLinkInfo.length() +
                                         fullNumber.length());
                    qfn.append(diallingLinkInfo).append(fullNumber);
                    qualifiedFullNumber = qfn.toString();
                } else {
                    qualifiedFullNumber = fullNumber;
                }
            }
        }

        return qualifiedFullNumber;
    }

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    /**
     * Write the open address markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenAddress(AddressAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close address markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseAddress(AddressAttributes attributes) {
    }

    /**
     * Write the open block quote markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenBlockQuote(BlockQuoteAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close block quote markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseBlockQuote(BlockQuoteAttributes attributes) {
    }

    /**
     * Write the open div markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenDiv(DivAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close div markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseDiv(DivAttributes attributes) {
    }

    /**
     * Write the open heading1 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenHeading1(HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close heading1 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseHeading1(HeadingAttributes attributes) {
    }

    /**
     * Write the open heading2 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenHeading2(HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close heading2 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseHeading2(HeadingAttributes attributes) {
    }

    /**
     * Write the open heading3 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenHeading3(HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close heading3 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseHeading3(HeadingAttributes attributes) {
    }

    /**
     * Write the open heading4 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenHeading4(HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close heading4 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseHeading4(HeadingAttributes attributes) {
    }

    /**
     * Write the open heading5 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenHeading5(HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close heading5 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseHeading5(HeadingAttributes attributes) {
    }

    /**
     * Write the open heading6 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenHeading6(HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close heading6 markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseHeading6(HeadingAttributes attributes) {
    }

    /**
     * Write the horizontal rule markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public
    void writeHorizontalRule(HorizontalRuleAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the open paragraph markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenParagraph(ParagraphAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close paragraph markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseParagraph(ParagraphAttributes attributes) {
    }

    /**
     * Write the open pre markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenPre(PreAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close pre markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeClosePre(PreAttributes attributes) {
    }

    // ========================================================================
    //   List element methods.
    // ========================================================================

    /**
     * Write the open definition data markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenDefinitionData(DefinitionDataAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close definition data markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseDefinitionData(DefinitionDataAttributes attributes) {
    }

    /**
     * Write the open definition list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenDefinitionList(DefinitionListAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close definition list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public
    void writeCloseDefinitionList(DefinitionListAttributes attributes) {
    }

    /**
     * Write the open definition term markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenDefinitionTerm(DefinitionTermAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close definition term markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseDefinitionTerm(DefinitionTermAttributes attributes) {
    }

    /**
     * Write the open list item markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenListItem(ListItemAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close list item markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseListItem(ListItemAttributes attributes) {
    }

    /**
     * Write the open ordered list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenOrderedList(OrderedListAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close ordered list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseOrderedList(OrderedListAttributes attributes) {
    }

    /**
     * Write the open unordered list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenUnorderedList(UnorderedListAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close unordered list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseUnorderedList(UnorderedListAttributes attributes) {
    }


    // ========================================================================
    //   Table element methods.
    // ========================================================================

    /**
     * Write the open table markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTable(TableAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTable(TableAttributes attributes) {
    }

    /**
     * Write the open table body markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTableBody(TableBodyAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table body markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableBody(TableBodyAttributes attributes) {
    }

    /**
     * Write the open table data cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTableDataCell(TableCellAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table data cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableDataCell(TableCellAttributes attributes) {
    }

    /**
     * Write the open table footer markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTableFooter(TableFooterAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table footer markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableFooter(TableFooterAttributes attributes) {
    }

    /**
     * Write the open table header markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTableHeader(TableHeaderAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table header markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableHeader(TableHeaderAttributes attributes) {
    }

    /**
     * Write the open table caption markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */

    public void writeOpenTableCaption(CaptionAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table caption markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableCaption(CaptionAttributes attributes) {
    }

    /**
     * Write the open table header cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTableHeaderCell(TableCellAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table header cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableHeaderCell(TableCellAttributes attributes) {
    }

    /**
     * Write the open table row markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenTableRow(TableRowAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close table row markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseTableRow(TableRowAttributes attributes) {
    }

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    /**
     * Write the open big markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenBig(BigAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close big markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseBig(BigAttributes attributes) {
    }

    /**
     * Write the open bold markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenBold(BoldAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close bold markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseBold(BoldAttributes attributes) {
    }

    /**
     * Write the open cite markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenCite(CiteAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close cite markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseCite(CiteAttributes attributes) {
    }

    /**
     * Write the open code markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenCode(CodeAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close code markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseCode(CodeAttributes attributes) {
    }

    /**
     * Write the open emphasis markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenEmphasis(EmphasisAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close emphasis markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseEmphasis(EmphasisAttributes attributes) {
    }

    /**
     * Write the open italic markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenItalic(ItalicAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close italic markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseItalic(ItalicAttributes attributes) {
    }

    /**
     * Write the open keyboard markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenKeyboard(KeyboardAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close keyboard markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseKeyboard(KeyboardAttributes attributes) {
    }

    /**
     * Write the line break markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeLineBreak(LineBreakAttributes attributes) {
    }

    /**
     * Write the open monospace font markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenMonospaceFont(MonospaceFontAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close monospace font markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseMonospaceFont(MonospaceFontAttributes attributes) {
    }

    /**
     * Write the open sample markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenSample(SampleAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close sample markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseSample(SampleAttributes attributes) {
    }

    /**
     * Write the open small markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenSmall(SmallAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close small markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseSmall(SmallAttributes attributes) {
    }

    /**
     * Write the open span markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenSpan(SpanAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close span markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseSpan(SpanAttributes attributes) {
    }

    /**
     * Write the open strong markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenStrong(StrongAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close strong markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseStrong(StrongAttributes attributes) {
    }

    /**
     * Write the open subscript markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenSubscript(SubscriptAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close subscript markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseSubscript(SubscriptAttributes attributes) {
    }

    /**
     * Write the open superscript markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenSuperscript(SuperscriptAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close superscript markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseSuperscript(SuperscriptAttributes attributes) {
    }

    /**
     * Write the open underline markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenUnderline(UnderlineAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close underline markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseUnderline(UnderlineAttributes attributes) {
    }

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    /**
     * Write the divide hint markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeDivideHint(DivideHintAttributes attributes) {
    }

    /**
     * Write the image markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeImage(ImageAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the meta markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeMeta(MetaAttributes attributes) {
    }

    
    /**
     * Write the audio markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeAudio(AudioAttributes attributes) {
    }


    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    public void doMenu(MenuAttributes attributes) throws ProtocolException {
    }

    // ========================================================================
    //   Script element methods.
    // ========================================================================
    
    /**
     * Does the protocol support Script?
     *
     * @return Whether protocol supports Script?  Default is null because
     *         absence of a Device DB setting we need to follow existing
     *         default behaviour
     */
    public String supportsScriptType() {
        return null;
    }    

    /**
     * Does the protocol support JavaScript?
     *
     * @return Whether protocol supports JavaScript?  Default is false
     */
    public boolean supportsJavaScript() {
        return false;
    }

    /**
     * Write the open no script markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenNoScript(NoScriptAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the close no script markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseNoScript(NoScriptAttributes attributes) {
    }

    /**
     * Write the open script markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenScript(ScriptAttributes attributes) {
    }

    /**
     * Write the close script markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseScript(ScriptAttributes attributes) {
    }

    public void doTimer(TimerAttributes attributes) {
    }


    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    // Unlike other protocol methods the form methods do not rely on the tag
    // to decide where to put the text. This is because the structure of forms;
    // where the different parts go; is highly dependent on the protocol.
    //
    // These methods are supplied with all the information that they need to
    // allow them to control where the different parts of a form are put which
    // means that the tags are very simple.

    public void doForm(XFFormAttributes attributes)
        throws ProtocolException {

        List fields = attributes.getFields();
        int count = fields.size();

        // Getting the form specifier will create it and add it to the
        // attributes if it's not already there.
        getFormSpecifier(attributes);

        openForm(attributes);

        for (int i = 0; i < count; i += 1) {
            XFFormFieldAttributes fieldAttributes
                = (XFFormFieldAttributes)fields.get(i);
            FieldDescriptor fieldDescriptor
                = fieldAttributes.getFieldDescriptor();
            FieldType fieldType = fieldDescriptor.getType();

            fieldType.doField(this, fieldAttributes);

            // Make sure that we make sure that we close any iterator pane
            // buffers.

            // If the caption container instance was specified then end the
            // buffer.
            ContainerInstance captionContainerInstance =
                    fieldAttributes.getCaptionContainerInstance();
            if (captionContainerInstance != null) {
                endCurrentBuffer(captionContainerInstance);
            }

            // If the entry container is not the same container as the caption
            // container then end the buffer.
            ContainerInstance entryContainerInstance =
                    fieldAttributes.getEntryContainerInstance();
            if (entryContainerInstance != null &&
                    entryContainerInstance != captionContainerInstance) {
                endCurrentBuffer(entryContainerInstance);
            }
        }

        closeForm(attributes);
    }

    /**
     * Retrieve the String form specifier from the session context using the
     * form descriptor stored in the {@link XFFormAttributes}. This will add
     * the form descriptor to the cache and generate a new specifier (and add
     * it to the supplied attributes) if it is not already present.
     *
     * @param attributes    form attributes which describe the form for which
     *                      to generate a specifier
     * @return String form specifier
     */
    protected String getFormSpecifier(XFFormAttributes attributes) {

        // Get the form data manager.
        FormDataManager manager = context.getFormDataManager();

        String formSpecifier = attributes.getFormSpecifier();
        FormDescriptor formDescriptor = attributes.getFormDescriptor();

        if (formSpecifier == null) {
            // This will add the form descriptor to the cache and create the a
            // new form specifier for it if none exists, or return an existing
            // specifier if the descriptor is already in the cache.
            formSpecifier = manager.getFormSpecifier(formDescriptor);
            attributes.setFormSpecifier(formSpecifier);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Form specifier for " + formDescriptor
                         + " is " + formSpecifier);
        }

        return formSpecifier;
    }

    protected void openForm(XFFormAttributes attributes)
        throws ProtocolException {
    }

    protected void closeForm(XFFormAttributes attributes)
        throws ProtocolException {
    }

    /**                                            
     * Overrides the default value of table cell alignment support
     * if overridden by the device
     */
    private void setAlignTableCellSupport() {
        // Handle device override of the support for align attribute
        // of table cells
        final String alignTableSetSupport = context.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_TD_SUPPORTS_ALIGN_ATTRIBUTE);

        if ((alignTableSetSupport != null) &&
                !DevicePolicyConstants.X_ELEMENT_TD_SUPPORTS_ALIGN_ATTRIBUTE_DEFAULT.
                equalsIgnoreCase(alignTableSetSupport)) {
            // The device policy value is different than the default,
            // so override the protocol accordingly
            supportsTableCellAlign =
                    Boolean.valueOf(alignTableSetSupport).booleanValue();

            if (logger.isDebugEnabled()) {
                logger.debug("Support for align attribute of table cells " +
                        "explicitly set to " + supportsTableCellAlign + " by " +
                        "the device");
            }
        }
    }

    /**
     * Overrides the default value of support for center element
     * if overridden by the device
     */
    private void setCenterElementSupport() {
        // Handle device override of the support for <center> element
        final String centerElementSupport = context.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_CENTER_SUPPORTED);

        if ((centerElementSupport != null) &&
                !DevicePolicyConstants.X_ELEMENT_CENTER_SUPPORTED_DEFAULT.
                equalsIgnoreCase(centerElementSupport)) {
            // The device policy value is different than the default,
            // so override the protocol accordingly
            supportsCenterElement =
                    Boolean.valueOf(centerElementSupport).booleanValue();

            if (logger.isDebugEnabled()) {
                logger.debug("Support for the center element explicitly " +
                        "set to " + supportsCenterElement + " by the device");
            }
        }
    }

    /**
     * Called by the xfcontent tag to write content to a pane within a form.
     * The output buffer for the pane has already been opened by the PAPI
     * element and is passed in the attributes. This method calls a derived
     * method to write the contents of the buffer then releases the buffer.
     */

    public void doContent(XFContentAttributes attributes)
            throws ProtocolException {
        OutputBuffer buffer = attributes.getOutputBuffer();

        if (buffer != null) {
            writeContent(attributes);
        }
    }

    /**
     * Called by doContent() to write the contents of an xfcontent tag. This
     * method should be overridden by any protocol that supports this. The
     * attributes contain the pane to write the content to and an OutputBuffer
     * holding the content that is to be written.
     */
    protected void writeContent(XFContentAttributes attributes)
            throws ProtocolException {
    }


    /**
     * Use the session and layout URL rewriters to rewrite the given form url.
     *
     * @param url the MarinerURL to rewrite.
     * @return the rewritten MarinerURL.
     */
    protected MarinerURL rewriteFormURL(MarinerURL url) {
        // Get the URLRewriter to use to encode session information in the
        // URL and use it.
        MarinerRequestContext requestContext = context.getRequestContext();
        URLRewriter sessionURLRewriter = context.getSessionURLRewriter();

        MarinerURL sessionURL = sessionURLRewriter.mapToExternalURL
            (requestContext, url);

        // perform any URL rewriting that may be required by an external
        // plugin
        PageURLRewriter urlRewriter
            = context.getVolantisBean().getLayoutURLRewriter();

        return urlRewriter.rewriteURL
            (requestContext, sessionURL,
             PageURLDetailsFactory.
             createPageURLDetails(PageURLType.FORM));
    }

    /**
     * Removes all form related parameters from a mariner URL
     */
    protected MarinerURL removeFormParameters(MarinerURL url) {
        String vfrag = url.getParameterValue
            (URLConstants.FRAGMENTATION_PARAMETER);
        String defseg = url.getParameterValue
            (URLConstants.SEGMENTATION_PARAMETER);

        url.removeAllParameters();

        if (vfrag != null) {
            url.addParameterValue(URLConstants.FRAGMENTATION_PARAMETER,
                                  vfrag);
        }

        if (defseg != null) {
            url.addParameterValue(URLConstants.SEGMENTATION_PARAMETER,
                                  defseg);
        }

        return url;
    }

    /**
     * The action is intercepted and re-routed to
     * the fragmentation servlet.
     *
     * @param attributes the XFFormAttributes describing the attributes of the
     *                   form.
     * @return the form action url resolved. Will never return null, but may
     * return an empty string if the action could not be resolved.
     */
    protected String resolveFormAction(XFFormAttributes attributes) {

        String resolvedFormAction = null;
        if (attributes != null) {
            String resolvedLink = getLinkFromReference(attributes.getAction());
            AbstractForm form = attributes.getFormData();
            if (form != null ) {
                // Get the context path URL which is environment dependent.
                EnvironmentContext envContext = context.getEnvironmentContext();
                MarinerURL contextPathURL = null;
                if (envContext != null) {
                    contextPathURL = envContext.getContextPathURL();
                }

                if (logger.isDebugEnabled()) {
                    logger.debug("Context path is "
                                 + contextPathURL.getExternalForm());
                }
                MarinerURL fragmentor = new MarinerURL(contextPathURL,
                                                       "MarinerFFP");

                MarinerURL externalURL = rewriteFormURL(fragmentor);

                resolvedFormAction = externalURL.getExternalForm();

                // The form action URL will now point to the form fragmentation
                // servlet, so we store the actual form URL in the session for
                // use later.
                storeFragmentedFormURL(resolvedLink, getFormSpecifier(attributes));
            } else {
                MarinerURL url = new MarinerURL(resolvedLink);
                resolvedFormAction = rewriteFormURL(url).getExternalForm();
            }
        }

        if (resolvedFormAction == null) {
            resolvedFormAction = "";
        }
        return resolvedFormAction;
    }

    /**
     * Transform the specified form URL so that it is relative to the servlet
     * context and store it in the {@link FormDataManager}. 
     * <p/>
     * The URL will be used by the FormFragmentationServlet to forward
     * the form data (both that stored in the session and from this request) to
     * the submission URL. It should be relative to the servlet context and 
     * start with a '/' because that is the form required by 
     * RequestDispatcher#forward.
     * <p/>
     * Our architecture requires that the original fragmented form submission 
     * URL be either host or page relative. However, absolute and invalid URLs
     * will just be stored unchanged with a warning; the former because it is a
     * valid form url (we just can't handle it in a fragmented form) and we
     * need to be backwards compatible.
     *
     * @param linkToTransform   link which may need modification to make it
     *                          relative to the servlet context
     * @param formSpecifier     string specifier which uniquely identifies
     *                          a form in a session context
     */
    protected void storeFragmentedFormURL(String linkToTransform,
                                          String formSpecifier) {
        if (logger.isDebugEnabled()) {
            logger.debug("Fragmented form submission URL to transform is: "
                    + linkToTransform);
        }

        MarinerURL urlToTransform = new MarinerURL(linkToTransform);
        String transformedLink = null;

        // Get the context path URL which is environment dependent.
        // For some tests in oldtests enviromentContext is null so only for these old tests
        // are added checking conditions 
        String contextPath = null;
        EnvironmentContext envContext = context.getEnvironmentContext();
        if(envContext == null) {
            logger.warn("EnvironmentContext object is NULL");
            return;
        }
        MarinerURL contextPathURL = envContext.getContextPathURL();
        if (contextPathURL != null) {
            contextPath = contextPathURL.getExternalForm();
        } else {
            logger.warn("EnvironmentContext#getContextPathURL object is NULL");            
            return;
        }

        if (urlToTransform.containsDocumentRelativePath()) {
            // Resolve the relative path against the request URL.
            final MarinerURL requestURL =
                    getMarinerPageContext().getRequestURL(true);

            MarinerURL resolvedURL = new MarinerURL(requestURL, urlToTransform);
            String fullPath = resolvedURL.getExternalForm();
            // And remove it.
            final int contextPos = fullPath.indexOf(contextPath);
            if (contextPos >= 0) {
                final int start = contextPos+contextPath.length()-1;
                transformedLink = fullPath.substring(start);
                if (logger.isDebugEnabled()) {
                    logger.debug("Fragmented form submission URL was " +
                            "document relative");
                }
            } else {
                throw new IllegalArgumentException( linkToTransform +
                    " is an invalid fragmented form submission URL");
            }
        } else if (urlToTransform.containsHostRelativePath()) {
            // Remove the context path from the host relative URL.
            if (linkToTransform.startsWith(contextPath)) {
                transformedLink =
                        linkToTransform.substring(contextPath.length() -1);
                if (logger.isDebugEnabled()) {
                    logger.debug("Fragmented form submission URL was host " +
                            "relative");
                }
            } else {
                // The URL is already relative to the servlet context, which 
                // is actually an invalid form submission URL. However, 
                // because we actually want a context relative URL, and we 
                // need to be backwards compatible, we'll just log it and continue.                
                logger.warn("form-fragmentation-invalid-form-url", linkToTransform);
                transformedLink = linkToTransform;
            }
        } else if (urlToTransform.isAbsolute() ||
                urlToTransform.containsFullyQualifiedPath()) {
            // Absolute URLs will fail when we submit the form because we can't
            // pass it to a request dispatcher.
            logger.warn("form-fragmentation-invalid-form-url", linkToTransform);
            // However because it could be a valid form URL, and we need to be
            // backwards compatible, we'll attempt to make it relative to our
            // servlet context, log a warning and continue.
            String relativeURL = urlToTransform.getPath();
            
            if (relativeURL != null && relativeURL.equals(contextPath.substring(0, contextPath.length() - 1))) {
                relativeURL = relativeURL + "/";
            }
            
            if (relativeURL != null && relativeURL.startsWith(contextPath)) {
                transformedLink =
                        relativeURL.substring(contextPath.length() -1);
            } else {
                transformedLink = linkToTransform;
            }
        } else {
            throw new IllegalArgumentException( linkToTransform +
                    " is an invalid fragmented form submission URL");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Setting vaction attribute for form specifier: "
                         + formSpecifier + " to :" + transformedLink);
        }
        
        // Set the field value in the session
        FormDataManager dataManager = context.getFormDataManager();
        if (dataManager != null) {
            SessionFormData sessionData = dataManager.getSessionFormData(formSpecifier);
            sessionData.setFieldValue(URLConstants.ACTION_FORM_FRAGMENT,
                    transformedLink);
        }
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(ContentFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(TextInputFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    public void doTextInput(XFTextInputAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(UploadFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    public void doUpload(XFUploadAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(BooleanFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    public void doBooleanInput(XFBooleanAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(SingleSelectFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(MultipleSelectFieldType type) {
        return MultipleValueFieldHandler.getSingleton();
    }

    public void doSelectInput(XFSelectAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(ActionFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    public void doActionInput(XFActionAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Return the <code>FieldHandler</code> object to associate with the form
     * field of the specified type.
     *
     * @param type The type of the field which is used to decide which
     *             <code>FieldHandler</code> object to return.
     * @return The instance of <code>FieldHandler</code> to associated with the
     *         field.
     */
    public FieldHandler getFieldHandler(ImplicitFieldType type) {
        return SingleValueFieldHandler.getSingleton();
    }

    /**
     * Add an implicit value to the form.
     *
     * @param attributes The attributes to use when generating the mark up.
     */
    public void doImplicitValue(XFImplicitAttributes attributes) {
    }

    // Extended Form helper methods

    protected void endCurrentBuffer(ContainerInstance containerInstance) {
        containerInstance.endCurrentBuffer();
    }

    /**
     * Get the initial value associated with the field. <p> If the form is
     * fragmented then this value may come from the information stored in the
     * session, otherwise it is the value which was set in the attributes.
     * </p>
     *
     * @param attributes The attributes of the field whose initial value is
     *                   required.
     * @return The initial value of the field.
     */
    protected String getInitialValue(XFFormFieldAttributes attributes) {

        String initialValue = null;
        if (!context.getFormFragmentResetState()) {

            XFFormAttributes formAttributes = attributes.getFormAttributes();
            if (formAttributes != null) {
                // Retrieve the form data from the session context.
                String formSpecifier = getFormSpecifier(formAttributes);
                SessionFormData sessionData =
                        context.getFormDataManager().getSessionFormData(formSpecifier);
                initialValue = sessionData.getFieldValue(attributes.getName());
            }
        }

        if (initialValue == null) {
            initialValue = getPlainText(attributes.getInitial());
        }
        return initialValue;
    }

    /**
     * Modify the SelectOption objects which belong to the specified select
     * element to make sure that they reflect the current state of the form.
     *
     * @param attributes The select element attributes.
     */
    protected void updateSelectedOptions(XFSelectAttributes attributes) {
        List options = attributes.getOptions();

        if (attributes.isMultiple()) {
            // This is a multiple select field and if the form is fragmented
            // and not being reset then we need to update the selected
            // flags in the options.
            XFFormAttributes formAttributes = attributes.getFormAttributes();
            if (formAttributes != null) {
                AbstractForm form = attributes.getFormData();

                // If the form is not being fragmented, or it is being
                // reset then do nothing as the default settings of the
                // options are correct.
                if (!form.isFragmented()
                    && context.getFormFragmentResetState()) {
                    return;
                }

                // Get the current values stored in the session.
                String formSpecifier = getFormSpecifier(formAttributes);
                SessionFormData sessionData =
                        context.getFormDataManager().getSessionFormData(formSpecifier);
                MarinerURL url = sessionData.getAsMarinerURL();
                String[] initialValues;
                if (url == null) {
                    initialValues = null;
                } else {
                    initialValues = url.getParameterValues(attributes.getName());
                }
                if (initialValues == null) {
                    // At the moment we cannot distinguish between when
                    // the user has purposely selected nothing, and when
                    // the field has not yet been edited by the user as
                    // they both return a null value.

                    // For now we assume that a null value means that the
                    // field has not yet been edited so we should use the
                    // default settings.
                    return;
                }
                setSelectedOptions(options, Arrays.asList(initialValues));
            }
             // NB: If we're emulating xform elements (rather than simply
             // processing xfform markup) then the XFormAttributes will be null
             // and the form won't be fragmented. The multiple select options
             // will already have been initialised by
             // AbstractXFSelectElementImpl#callCloseOnProtocol.
        } else {
            // Get the initial value associated with this field.
            List initialValue = new ArrayList(1);
            initialValue.add(getInitialValue(attributes));
            setSelectedOptions(options, initialValue);
        }
    }

    /**
     * Iterate through all the options setting
     * the selected flag for the option whose value matches the initial value
     * and clearing the selected flag for all others.
     *
     * @param options is a list of SelectOption and SelectOptionGroups
     * @param initialValues initial selected values, may be empty
     */

    protected void setSelectedOptions(List options, List initialValues) {
        for (int i = 0; i < options.size(); i += 1) {
            Option option = (Option)options.get(i);
            option.selectedValues(initialValues);
        }
    }

    public static String[] parseCommaSeparatedList(String list) {
        StringTokenizer tokenizer = new StringTokenizer(list, ",");
        String[] elements = new String[tokenizer.countTokens()];
        int e = 0;
        while (tokenizer.hasMoreTokens()) {
            elements[e] = tokenizer.nextToken();
            e += 1;
        }

        return elements;
    }

    // ========================================================================
    //   Custom markup methods
    // ========================================================================

    /**
     * Write the markup to open an arbitrary element with arbitrary attributes
     * to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeOpenElement(CustomMarkupAttributes attributes) {
    }

    /**
     * Write the markup to close an arbitrary element with arbitrary attributes
     * to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public void writeCloseElement(CustomMarkupAttributes attributes) {
    }

    /**
     * Returns false to indicate that by default protocols do not support the
     * maml nativemarkup tag
     */
    public boolean supportsNativeMarkup() {
        return false;
    }

    /**
     * Write the markup to open an arbitrary element with arbitrary attributes
     * to an output buffer specified by the attributes passed in.
     *
     * @param attributes Attributes that determine the output buffer to use
     */
    public void writeOpenNativeMarkup(NativeMarkupAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Write the markup to close an arbitrary element with arbitrary attributes
     * to an output buffer specified by the attributes passed in.
     *
     * @param attributes Attributes that determine the output buffer to use
     */
    public void writeCloseNativeMarkup(NativeMarkupAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Determine if the nativemarkup element should be processed if it is not
     * an expression
     *
     * @return true if the element can process the nativemarkup element when
     * not an expression
     */
    public boolean doProcessNativeMarkupWithoutExpression() {
        return false;
    }

    // ========================================================================
    //   Fallback methods
    // ========================================================================

    /**
     * Write out the appropriate fallback text for an element. If the
     * attributes alt text supplied is not null, it writes that (handling any
     * possible text component reference), otherwise it tries to write fallback
     * text for the component of the id supplied in the attributes.
     *
     * @param attributes attributes to be used for writing
     * @return true if we managed to write one, false if there was none
     */
    public boolean writeAltText(AltTextAttributes attributes)
        throws ProtocolException {
        // Default to alt text
        TextAssetReference textReference = attributes.getAltText();
        String text = getPlainText(textReference);

        if (text != null) {
            renderAltText(text, attributes);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Writes out the text supplied as alt / fallback text. This is useful for
     * subclasses to override to change the way alt text is rendered.
     *
     * @param altText    the text to render
     * @param attributes attributes containing the style info with which to
     *                   render.
     */
    protected void renderAltText(String altText,
                                 MCSAttributes attributes)
        throws ProtocolException {
        // Copy the tag attrs into something we can use with P
        ParagraphAttributes paragraphAttrs = new ParagraphAttributes();
        paragraphAttrs.copy(attributes);
        // Write out a P tag with the fallback text in it, using the
        // styles of the original PAPI tag.
        writeOpenParagraph(paragraphAttrs);
        Writer writer = getContentWriter();
        try {
            writer.write(altText);
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }
        writeCloseParagraph(paragraphAttrs);
    }

    // @todo we should really have matching writeAltImage methods here since
    // we have recently invented FallsBackToText and FallsBackToMediImage for
    // components but it's a bridge too far at the moment.

    /**
     * Make the head buffer the current output buffer.
     */
    public void pushHeadBuffer() {
        OutputBuffer head = getPageHead().getHead();
        context.pushOutputBuffer(head);
    }

    /**
     * Remove the head buffer from the stack of output buffers.
     */
    public void popHeadBuffer() {
        OutputBuffer head = getPageHead().getHead();
        context.popOutputBuffer(head);
    }

    /**
     * Calculate the width of a pane.
     *
     * @param pane The pane.
     * @return width in pixels as an int.
     */
    public int calculatePaneWidth(Pane pane) {
        return (int)calculateWidth(pane);
    }

    /**
     * Calculate the width of a pane.
     *
     * @param pane The pane.
     * @return width in pixels as a float.
     */
    protected float calculateWidth(Pane pane) {
        float width = Float.parseFloat(pane.getWidth());
        if (FormatConstants.WIDTH_UNITS_VALUE_PERCENT.
            equals(pane.getWidthUnits())) {
            if (pane.getParent() != null) {
                width = calculateWidth(pane.getParent(), width);
            } else {
                width = (width / 100) * context.getDevice().getPixelsX();
            }
        }
        return width;
    }

    /**
     * Calculate the width of a pane wxpressed as a percentage of a formats
     * width.
     *
     * @param format The parent format.
     * @param width  The percentage width of the pane
     * @return the width in pixels.
     */
    protected float calculateWidth(Format format, float width) {
        String formatWidth = format.getWidth();
        float formatWidthValue;

        // If the user hasn't specified a width for this format then if the
        // format is a grid, evenly space out the width of the columns,
        // else assume it's 100%. (By default the width units will be percentages
        // if a width hasn't been specified.)
        if (formatWidth == null) {
            FormatType type = format.getFormatType();
            if (FormatType.GRID == type) {
                int columns = format.getColumns();
                formatWidthValue = 100 / columns;
            } else {
                formatWidthValue = 100;
            }
        } else {
            formatWidthValue = Float.parseFloat(formatWidth);
        }

        width = (width / 100) * formatWidthValue;
        if (FormatConstants.WIDTH_UNITS_VALUE_PERCENT.
            equals(format.getWidthUnits())) {
            if (format.getParent() != null) {
                width = calculateWidth(format.getParent(), width);
            } else {
                width = (width / 100) * context.getDevice().getPixelsX();
            }
        }
        return width;
    }

    /**
     * Calculate the height of a pane. If the height is not specified it is
     * assumed to be the height of the device.
     *
     * @param pane The pane.
     * @return height in pixels as an int.
     */
    public int calculatePaneHeight(Pane pane) {
        String height = pane.getHeight();
        if (height == null) {
            return context.getDevice().getPixelsY();
        } else {
            return Integer.parseInt(height);
        }
    }

    /**
     * Package the page content. This should be called for a top-level canvas
     * or for a montage, which should be identified by passing in <p/> The page
     * buffer should be written via the packager (accessed from the application
     * context).
     *
     * @param bodyContext
     */
    private void packagePage(Object bodyContext)
        throws IOException, ProtocolException {
        MarinerRequestContext requestContext = context.getRequestContext();
        ApplicationContext ac =
            ContextInternals.getApplicationContext(requestContext);
        Packager packager = ac.getPackager();

        if (packager != null) {
            try {
                packager.createPackage(requestContext, this, bodyContext);
            } catch (PackagingException e) {
                Throwable cause = e.getCause();

                if (cause instanceof IOException) {
                    throw (IOException)cause;
                } else if (cause instanceof ProtocolException) {
                    throw (ProtocolException)cause;
                } else {
                    if (cause != null) {
                        logger.warn("packaging-problem", cause);
                    } else {
                        logger.warn("packaging-problem", e);
                    }
                    new ProtocolException(exceptionLocalizer.format(
                        "unexpected-exception"),
                                          e);
                }
            }
        } else {
            new ProtocolException(
                exceptionLocalizer.format("packager-missing"));
        }
    }

    public void write(PackageBodyOutput writer,
                      MarinerRequestContext context,
                      Object bodyContext)
        throws PackagingException {
        try {
            if (bodyContext instanceof MontageAttributes) {
                writeMontageContent(writer, (MontageAttributes)bodyContext);
            } else if (bodyContext instanceof CanvasAttributes) {
                writeCanvasContent(writer, (CanvasAttributes)bodyContext);
            } else if (bodyContext instanceof ResponseBodyAttributes) {
                writeAJAXResponseContent(writer, (ResponseBodyAttributes)bodyContext);
            }
        } catch (IOException e) {
            throw new PackagingException(e);
        } catch (ProtocolException e) {
            throw new PackagingException(e);
        }
    }

    public String getBodyType(MarinerRequestContext context) {
        return mimeType();
    }

    /**
     * Write the initialFocus for the stored element (if necessary)
     *
     * @param tabindex the tabindex (shouldn't be null).
     */
    public void writeInitialFocus(String tabindex) {
    }

    /**
     * Get the renderer context.
     *
     * @return The renderer context.
     */
    public RendererContext getRendererContext() {
        if (rendererContext == null) {
            MarinerPageContext marinerPageContext = getMarinerPageContext();
            // NOTE: any methods of MPC that are visible via this context
            // (e.g. in AssetResolver) must (currently) NOT be dependent on
            // having the correct nested instance of MPC.
            // This is because we cache the entire menu module which uses this
            // class in the protocol.
            rendererContext =
                    new RendererContextImpl(marinerPageContext.getAssetResolver(),
                            marinerPageContext,
                            getOutputBufferFactory(),
                            marinerPageContext.getStylePropertyResolver());
        }
        return rendererContext;
    }

    /**
     * Get the menu module for this protocol.
     */
    public MenuModule getMenuModule() {

        if (menuModule == null) {
            menuModule = createMenuModule(null);
        }
        return menuModule;
    }

    /**
     * Create the normal menu module for this protocol.
     *
     * @param factoryFilter
     * @return The menu module.
     */
    protected MenuModule createMenuModule(
        MenuModuleRendererFactoryFilter factoryFilter) {

        return MenuModule.UNSUPPORTED;
    }

    /**
     * Get the menu module customisation for this protocol, creating one if
     * necessary.
     *
     * @return The customisation instance, may not be null.
     */
    protected MenuModuleCustomisation getMenuModuleCustomisation() {
        if (menuModuleCustomisation == null) {
            menuModuleCustomisation = createMenuModuleCustomisation();
        }
        return menuModuleCustomisation;
    }

    /**
     * Create the menu module customisation. This method calls
     * provideMenuModelCustomisation to obtain the customisation. If that
     * method returns null then this method creates a MenuModuleCustomisationImpl
     * with a default configuration.
     *
     * @return The customisation instance, is never null.
     */
    private MenuModuleCustomisation createMenuModuleCustomisation() {
        MenuModuleCustomisation customisation =
            provideMenuModelCustomisation();

        if (customisation == null) {
            // informed guess about style sheet support.
            boolean supportsStyleSheets = supportsExternalStyleSheets ||
                supportsInlineStyles;

            customisation = new MenuModuleCustomisationImpl(
                supportsAccessKeyAttribute,
                supportsAutomaticShortcutPrefixDisplay,
                supportsStyleSheets);
        }
        return customisation;
    }

    /**
     * Override this mehtod to provide customized specialised implementations
     * of the MenuModuleCustomisation interface.
     *
     * @return an implementation of the MenuMouleCustomisation interface or
     *         null if the default implementation is sufficient. This
     *         implementation always returns null.
     */
    protected MenuModuleCustomisation provideMenuModelCustomisation() {
        return null;
    }

    /**
     * Returns whether or not this protocol supports nested tables.
     *
     * @return true if nested tables are supported; false otherwise
     */
    public boolean supportsNestedTables() {
        return supportsNestedTables;
    }


    /**
     * Returns true if the device being targeted honours spacing for
     * inline styling open elements.
     *
     * @return true if spacing is honoured; otherwise false.
     */
    public boolean deviceHonoursSpacingForInlineStylingOpenElements() {
        String inlineStyleOpenFix = context.getDevicePolicyValue(
                FIX_FOR_OPEN_INLINE_STYLING_ELEMENTS);
        String inlineStyleCloseFix = context.getDevicePolicyValue(
                FIX_FOR_CLOSING_INLINE_STYLING_ELEMENTS);
        String inlineLinkOpenFix = context.getDevicePolicyValue(
                FIX_FOR_OPEN_ANCHOR_ELEMENT);
        String inlineLinkCloseFix = context.getDevicePolicyValue(
                FIX_FOR_CLOSING_ANCHOR_ELEMENT);


        boolean deviceHonoursSpacing = false;


        if ((inlineStyleOpenFix == null || DevicePolicyConstants.
                NO_WHITESPACE_FIXING.equals(inlineStyleOpenFix)) &&
            (inlineStyleCloseFix == null || DevicePolicyConstants.
                NO_WHITESPACE_FIXING.equals(inlineStyleCloseFix)) &&
            (inlineLinkOpenFix == null || DevicePolicyConstants.
                NO_WHITESPACE_FIXING.equals(inlineLinkOpenFix)) &&
            (inlineLinkCloseFix == null || DevicePolicyConstants.
                NO_WHITESPACE_FIXING.equals(inlineStyleOpenFix))) {

            // No fix has been specified in the repository so the
            // device must honour spacing.
            deviceHonoursSpacing = true;
        }
        return deviceHonoursSpacing;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 07-Dec-05	10321/4	emma	VBM:2005103109 Supermerge required

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 06-Dec-05	10638/1	emma	VBM:2005120505 Forward port: Generated XHTML was invalid - had no head tag but had head content

 06-Dec-05	10623/1	emma	VBM:2005120505 Generated XHTML was invalid: missing head tag but head content

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 29-Nov-05	10478/3	pduffin	VBM:2005070711 Fixed merge conflicts

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 10-Oct-05	9637/6	emma	VBM:2005092807 Adding tests for XForms emulation
 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 29-Nov-05	10471/1	ibush	VBM:2005112303 Change selection priority of internable and externable

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 29-Nov-05	10488/1	ibush	VBM:2005112303 Change selection priority of internable and externable

 29-Nov-05	10478/3	pduffin	VBM:2005070711 Fixed merge conflicts

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 10-Oct-05	9637/6	emma	VBM:2005092807 Adding tests for XForms emulation
 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 10-Oct-05	9637/6	emma	VBM:2005092807 Adding tests for XForms emulation

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/4	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/2	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9540/4	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 21-Sep-05	9128/8	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/4	pabbott	VBM:2005071114 Add XHTML 2 elements

 19-Sep-05	9472/5	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 12-Sep-05	9372/4	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 02-Sep-05	9408/4	pabbott	VBM:2005083007 Move over to using JiBX accessor

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/6	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/3	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/9	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9287/1	gkoch	VBM:2005080509 vbm2005080509 applied review comments

 10-Aug-05	9187/2	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file]

 09-Aug-05	9151/6	pduffin	VBM:2005080205 Recommitted after super merge

 05-Aug-05	9151/4	pduffin	VBM:2005080205 Recommitted after super merge

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 04-Aug-05	8990/15	pcameron	VBM:2005052606 Fixed merge conflicts

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 23-Jun-05	8833/6	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/2	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 23-Jun-05	8483/4	emma	VBM:2005052410 Modifications after review

 20-Jun-05	8483/2	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 24-May-05	8123/5	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 05-Apr-05	7459/4	tom	VBM:2005032101 Added SmartClientSkin protocol

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 05-Apr-05	7513/4	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 05-Apr-05	7513/2	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 09-Mar-05	7022/4	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 17-Feb-05	6957/4	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 17-Feb-05	6957/1	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 18-Feb-05	7037/2	pcameron	VBM:2005021704 Width units default to percent if not present

 16-Feb-05	6129/13	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/11	matthew	VBM:2004102019 yet another supermerge

 28-Jan-05	6129/9	matthew	VBM:2004102019 supermerge required

 27-Jan-05	6129/6	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/3	matthew	VBM:2004102019 Enable shortcut menu link rendering

 10-Dec-04	6391/2	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/8	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/2	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 26-Nov-04	6076/8	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	5733/15	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/13	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Nov-04	5733/11	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/8	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/1	claire	VBM:2004092801 Encoding of style classes names for inclusions

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 02-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 01-Jul-04	4778/3	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 01-Jul-04	4775/6	claire	VBM:2004062911 Tidied testcase for caching up

 01-Jul-04	4775/3	claire	VBM:2004062911 Caching of inline stylesheets internally

 30-Jun-04	4781/4	adrianj	VBM:2002111405 Created SMS test case and added check for null/empty mime types in protocols

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 14-May-04	4315/8	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/5	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/2	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 06-May-04	4174/2	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 27-Apr-04	4061/3	pduffin	VBM:2004042705 Added new OutputBuffer factory class

 27-Apr-04	4061/1	pduffin	VBM:2004042705 Added new OutputBuffer factory class

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 22-Mar-04	3512/3	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 25-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/4	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 12-Feb-04	2958/2	philws	VBM:2004012715 Add protocol.content.type device policy

 22-Jan-04	2718/1	steve	VBM:2003121103 Remove apostrophe encoding from HTML

 19-Dec-03	2275/1	steve	VBM:2003121601 Dollar encoding in WAP TV - Merged from Proteus2

 19-Dec-03	2263/1	steve	VBM:2003121601 Dollar encoding on WAPTV

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 06-Nov-03	1760/6	philws	VBM:2003031710 Fix unknown component NPE for AltText

 03-Nov-03	1760/4	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03	1751/1	philws	VBM:2003031710 Permit image alt text to be component reference

 02-Nov-03	1763/1	allan	VBM:2003032007 Patched from Proteus2.

 02-Nov-03	1754/1	allan	VBM:2003032007 Correct packaging type to multipart/mixed.

 29-Sep-03	1412/10	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (review feedback)

 25-Sep-03	1412/8	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (move some code about)

 25-Sep-03	1412/6	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 17-Sep-03	1412/4	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 18-Aug-03	1052/3	allan	VBM:2003073101 Fixed quoteTable ? entries.

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 06-Jun-03	52/9	mat	VBM:2003042912 Remove domOutputBuffer.appendLiterals

 05-Jun-03	285/6	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
