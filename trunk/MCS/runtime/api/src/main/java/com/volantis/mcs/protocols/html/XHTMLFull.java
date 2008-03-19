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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLFull.java,v 1.37 2003/04/23 09:44:19 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header, fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation. Also added check
 *                              to determine whether a pane needs a table
 *                              wrapper around it, implemented the extended
 *                              function form methods and made use of the new
 *                              getStyle method in VolantisProtocol to simplify
 *                              the code.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by not creating
 *                              StringBuffers when returning a fixed string
 *                              and also by renaming all the *Attributes
 *                              parameters to attributes, reimplemented menus,
 *                              added a helper method appendCoreAttributes
 *                              which appends the core HTML attributes to a
 *                              buffer and implemented textarea support for
 *                              xftextinput.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width and
 *                              height unit format attributes.
 * 24-Jul-01    Paul            VBM:2001071103 - Made openForm and closeForm
 *                              protected.
 * 26-Jul-01    Paul            VBM:2001071707 - Modified to make it compatible
 *                              with some minor changes in some *Attributes
 *                              classes.
 * 26-Jul-01    Paul            VBM:2001071705 - Wrote the value associated
 *                              with the vt:textarea tag into the body of the
 *                              <textarea> tag.
 * 30-Jul-01    Paul            VBM:2001071609 - Removed a couple of
 *                              unnecessary white space characters from the
 *                              output and fixed a minor problem with <title>.
 * 01-Aug-01    Paul            VBM:2001072506 - Removed setting of layoutName
 *                              and themeName on the <html> tag.
 * 03-Aug-01    Kula            VBM:2001080102 Height property added to panes.
 * 03-Aug-01    Allan           VBM:2001080102 Height made to work on
 *                              GridChildPreamble - a pixel value is always
 *                              assumed for this.
 * 03-Aug-01    Doug            VBM:2001072504 Implemented Client-Side
 *                              Validation for extend forms
 * 06-Aug-01    Allan           VBM:2001080610 Changed openGridChildPreamble
 *                              to set column to the column instead of the
 *                              row.
 * 09-Aug-01    Payal           VBM:2001080808 - Renamed getScriptsBaseDir
 *                              method to getScriptsBase.
 * 10-Aug-01    Doug            VBM:2001062705 Added method addXFormAttributes
 *                              so that subclasses can provide additional
 *                              attributes to xf form tag. This was
 *                              required by the protocol HTMLLiberate so that
 *                              the name attribute could be set for a form
 * 14-Aug-01    Payal           VBM:2001080803 - Height property added to Grid
 *                              changed openGridPreamble() so that it uses the
 *                              height attribute.
 * 16-Aug-01    Payal           VBM:2001081501 Height attribute removed from
 *                              table type tags.Height property removed from
 *                              openGridPreamble(), openPanePreamble(),
 *                              openColumnIteratorPanePreamble(),
 *                              openGridChildPreamble() and moved it to
 *                              HTMLRoot.
 * 21-Aug-01    Kula            VBM:2001062202 in doRolloverImage asset is
 *                              retrieved using correct image order.
 * 23-Aug-01    Allan           VBM:2001082305 - Added appendCoreAttributes()
 *                              with a boolean title param that will only
 *                              include title if this param is true. Modified
 *                              all calls to appendCoreAttributes within
 *                              option and input tags to use this new
 *                              method with supportsTitleOnInput and
 *                              supportsTitleOnOption where required.
 *                              Modified doBooleanInput() so as not to
 *                              output the title attribute twice.
 * 30-Aug-01    Allan           VBM:2001072509 - Make paneNeedsTableWrapper()
 *                              return false if the pane is a top level pane.
 * 31-Aug-01    Allan           VBM:2001083121 - Set width to 100% in
 *                              openColumnIteratorPanePreamble(). Modify
 *                              openColumnIteratorPaneElementPreamble() to
 *                              use a width attribute if available.
 * 04-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 05-Sep-01    Kula            oVBM:2001090401- theme related attributes align
 *                              ,bgcolor,colspan,height,nowrap,rowspan,
 *                              valign and width are removed from
 *                              openTableRow(), openTableDataCell() and
 *                              openTableHeaderHeaderCell() methods.
 * 10-Sep-01    Kula            oVBM:2001090401 - javadoc comments added to
 *                              TableRow, TableDataCell,and TableHeaderCell
 *                              open and close methods. nowrap attribute
 *                              added to openTableDataCell and
 *                              openTableHeaderCell
 * 10-Sep-01    Doug            VBM:2001090701 Fixed iterator looping bug in
 *                              doValidation method
 * 10-Sep-01    Doug            VBM:2001091007 Ensured that getTextFromReference
 *                              is used to extract the caption attribute from
 *                              text input fields in method
 *                              writeJavaScriptValidation.
 * 14-Sep-01    Kula            VBM2001091005 - the doActionInput method
 *                              used to call the getAssetImageComponent method
 *                              from MarinerPageContext class. Now it is
 *                              modified to get the asset internally.
 * 14-Sep-01    Paul            VBM:2001091302 - Made the select option's
 *                              caption default to the option's value if the
 *                              caption is null.
 * 14-Sep-01    Paul            VBM:2001091405 - Added support for name
 *                              attribute on xfaction.
 * 20-Sep-01    Paul            VBM:2001091202 - Added support for implicit
 *                              values.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 21-Sep-01    Doug            VBM:2001090302 - Use getLinkFromReference to get
 *                              links for those attributes whose value could
 *                              be a LinkComponentName. Added href parameter
 *                              to methods that need a resolved link (ie one
 *                              that might have originated from a LinkAsset
 *                              Added method menuItemAddedOK that subclasses
 *                              can override to perform additional processing
 *                              after a menu item hase been written out.
 * 01-Oct-01    Allan           VBM:2001083120 - Added addMetaAttributes().
 * 02-Oct-01    Doug            VBM:2001092805 - Form validation error message
 *                              is constructed using the form fileds errmsg
 *                              attribute. If this attribute is not set then
 *                              attempt to generate sensible alternative.
 * 03-Oct-01    Allan           VBM:2001100204 - Call endOutput() for the
 *                              caption and entry panes in doTextInput(),
 *                              doBooleanInput(), doSelectInput() and
 *                              doActionInput(). Removed some separator
 *                              comments.
 * 04-Oct-01    Doug            VBM:2001100201 - Modified constructor to set
 *                              the supportsAccessKeyAttribute property to
 *                              true. Modified all the methods that write out
 *                              an "accesskey" attribute to check the
 *                              supportsAccessKeyAttribute flag to determine
 *                              wether to actually write out the attribute.
 * 07-Oct-01    Allan           VBM:2001100805 - Modified doTextInput(),
 *                              doBooleanInput(), doSelectInput() and
 *                              doActionInput() to check that caption pane is
 *                              not null before called endOutput.
 * 11-Oct-01    Allan           VBM:2001090401 - Replaced TableDataCell and
 *                              TableHeaderCell Attributes with
 *                              TableCellAttributes. Reverted back to
 *                              including stylistic attributes for these
 *                              tags and others removed earlier on the tag
 *                              overriding the theme.
 * 19-Oct-01    Pether          VBM:2001100302 - Now getting style information
 *                              for orientation in doSelectInput to determine
 *                              which separator to use.
 * 22-Oct-01    Pether          VBM:2001100302 - Removed some comments and a
 *                              unecessary check for null"
 * 22-Oct-01    Pether          VBM:2001041201 - Now set the attribute class
 *                              in the openObject method and doRealMedia one.
 * 23-Oct-01    Pether          VBM:2001041201 - Now added the class and id
 *                              attribute in the following methods
 *                              "openObject", "doMMFlash",
 *                              "doRealMedia", "doWindowsMedia" and
 *                              "doQuickTime".
 * 29-Oct-01    Paul            VBM:2001102901 - Modified to use new methods
 *                              in VolantisProtocol to retrieve context
 *                              information from Formats.
 * 31-Oct-01    Doug            VBM:2001092806 Modified the methods
 *                              openPanePreamble(), openGridPreamble(),
 *                              openColumnIteratorPanePreamble() and
 *                              openRowIteratorPanePreamble(),
 *                              to check supportsBackgroundInTable to
 *                              determine if we need to right out a background
 *                              attribute for the table tag.
 * 19-Nov-01    Pether          VBM:2001103001 - get table-align style in
 *                              openTable() to set the align attribute.
 * 20-Nov-01    Pether          VBM:2001111602 - Changed the doSelectInput()
 *                              method to add the accesskey attribute to the
 *                              select tag.
 * 22-Nov-01    Paul            VBM:2001110202 - Called getUniqueId instead
 *                              of getCount to allow us to create more
 *                              complicated ids rather than use a single
 *                              number.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 08-Jan-02    Doug            VBM:2001121005 - Added the method
 *                              createUniqueRuntimeStyle so that an existing
 *                              style can be cloned, assigned a unique id and
 *                              added to the deviceTheme. Changed the XF Form
 *                              methods to call createUniqueRuntimeStyle.
 * 09-Jan-02    Adrian          VBM:2001121901 - doSelectInput modified to
 *                              stop output of < br/> tag before first <imput>
 *                              tag.
 * 21-Jan-02    Mat             VBM:2002011801 - Called generateJavaScriptID()
 *                              on doRolloverImage() to get a unqiue ID for the
 *                              menuItem.  Also changed to use
 *                              generateStyleID() in createUniqueRuntimeStyle()
 * 22-Jan-02    Doug            VBM:2002011003 - Numerous changes to ensure
 *                              that valid XHTML Strict is generated by this
 *                              protocol. XHTMLBasic now extends
 *                              XHTMLBasic rather than VolantisProtocol.
 *                              Moved much of the functionality to the
 *                              XHTMLBasic and XHTMLTransitional protocols.
 * 30-Jan-02    Mat             VBM:2002011410 - Added openInclusion()
 * 30-Jan-02    Mat             VBM:2002011410 - Fixed problem with previous
 *                              check in.  openInclusion would try to
 *                              render a null styleSheet
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 07-Feb-02    Adrian          VBM:2001101002 - override addPaneCellAttributes
 *                              to output align and valign attributes for
 *                              <td> tag.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed calls to getPageHead
 *                              method in MarinerPageContext as it is now
 *                              accessible to subclasses of StringProtocol
 *                              through a protected field.
 * 15-Feb-02    Paul            VBM:2002021203 - Modified open / close
 *                              inclusion to only generate a div tag if it is
 *                              not the root canvas in a page.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 06-Mar-02    Adrian          VBM:2002020736 - moved width attribute for all
 *                              table tags to XHTMLBasic.  Moved all width attr
 *                              for td tags from XHTMLBasic to this class.
 *                              Moved all valign and align for td tag to
 *                              XHTMLBasic.
 * 06-Mar-02    Adrian          VBM:2002021102 - modified doProtocolString() to
 *                              output frameset DTD if the device supports
 *                              aggregation, as the user could potential create
 *                              output not supported by the strict DTD
 * 06-Mar-02    Adrian          VBM:2002021905 - Moved methods...
 *                              openTableHeader, addTableHeaderAttributes,
 *                              closeTableHeader, openTableBody, closeTableBody
 *                              addTableBodyAttributes, openTableFooter,
 *                              closeTableFooter, addTableFooterAttributes from
 *                              XHTMLBasic as these tags are not supported in
 *                              XHTMLBasic
 * 06-Mar-02    Adrian          VBM:2002021101 - Moved methods...
 *                              doRolloverImage addRolloverImageAttributes
 *                              from XHTMLTransitional as rollover images are
 *                              supported from this protocol level.
 * 08-Mar-02    Paul            VBM:2002030607 - Changed rendering of inclusion
 *                              styles to send them to an extra style buffer
 *                              associated with the current device layout
 *                              context, rather than to the preamble buffer.
 *                              Changed the menu code to generate valid inline
 *                              styles.
 * 11-Mar-02    Paul            VBM:2001122105 - Made sure that the different
 *                              menu items have script attributes written out
 *                              which for rollover images can include an
 *                              internally generated script.
 * 11-Mar-02    Doug            VBM:2002011003 - Modified the doProtocolString
 *                              method so that it allways references the
 *                              xhtml1-strict.dtd.
 * 11-Mar-02    Adrian          VBM:2002021910 - modify addEventAttribute to
 *                              call quoteScriptValue with the script asset
 *                              value as (at least) quotes must be replaced by
 *                              entities to ensure the attribute is valid.
 * 11-Mar-02    Ian             VBM:2001122103 - Do not put events out on non
 *                              JavaScript enabled devices.
 * 13-Mar-02    Paul            VBM:2002030104 - Made addSelectMenuAttributes
 *                              use XFSelectAttributes instead of
 *                              SelectAttributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Removed dissecting code as
 *                              no string protocols can actually dissect now.
 *                              Also fixed up the ssi methods and removed the
 *                              last deprecated methods.
 * 04-Apr-02    Adrian          VBM:2001122105 - Added method
 *                              addObjectAttributes to add the general event
 *                              attributes to the object tag.
 * 12-Apr-02    Adrian          VBM:2002041106 - moved width attribute for all
 *                              table tags from XHTMLBasic.
 * 16-Apr-02    Allan           VBM:2002041601 - Fixed bug to mask out
 *                              the remaining event attributes using the
 *                              correct constants in doRolloverImage().
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 26-Apr-02    Paul            VBM:2002042205 - Changed pane rendering code
 *                              to only use a table for a pane when absolutely
 *                              necessary, also integrated the XHTMLBasic code
 *                              which rendered row iterator panes.
 * 28-Apr-02    Allan           VBM:2002040804 - StyleSheetRenderer set to new
 *                              css2 renderer. openInclusion() updated to use
 *                              new css renderer.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Paul            VBM:2002042203 - Removed the preprocess flag.
 * 20-May-02    Paul            VBM:2002042202 - Fixed problem with the
 *                              doHorizontalRule method pushing an element but
 *                              not popping it.
 * 20-May-02    Paul            VBM:2001122105 - Added general events to
 *                              address, block quote (moved from
 *                              XHTMLTransitional), div and xfaction, added
 *                              field events to xfaction.
 * 23-May-02    Steve           VBM:2002040809 - Check pane for a styleClass
 *                              attribute. If one is present and the device
 *                              supports style sheets then a class attribute
 *                              is added to the table surrounding the pane.
 * 23-May-02    Paul            VBM:2002042202 - Removed call to
 *                              quoteScriptValue as the attributes are encoded
 *                              properly by the XMLOutputter.
 * 29-May-02    Steve           VBM:2002040809 - Override layout style for
 *                              pane table cells if the pane has an associated
 *                              style class.
 * 05-Jun-02    Adrian          VBM:2002021103 - Open KEEPTOGETHER_ELEMENT in
 *                              methods openTableBody, and close in method
 *                              closeTableBody.
 * 06-Jun-02    Byron           VBM:2002051303 - Modified openInclusion to
 *                              insert an inlined style sheet according to
 *                              the rules defined by this task's description.
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 14-Jun-02    Steve           VBM:2002040807 - Pass the protocol element
 *                              mapping to the CSS renderer context so that the
 *                              stylesheet rules can be transformed.
 * 19-Jun-02    Adrian          VBM:2002053104 - moved logic from openInclusion
 *                              to VolantisProtocol.writeStyleSheet for writing
 *                              of inline css imports.
 * 21-Jun-02    Paul            VBM:2002061202 - Overrode getDOMTransformer and
 *                              getDissector to make sure that they return
 *                              nothing.
 * 27-Jun-02    Byron           VBM:2002062501 - Set supportsDissectingPanes
 *                              to false in constructor
 * 01-Jul-02    Sumit           VBM:2002061004 - Fixed the form validation by
 *                              appending the name of the form to the function
 * 10-Jul-02    Steve           VBM:2002040807 - Removed calls to the four
 *                              param version of addCoreAttributes as the logic
 *                              is now handled by the three parameter version as
 *                              the mariner element name is stored in the attributes
 *                              of the element being rendered.
 *
 * 10-Jul-02    Allan           VBM:2002052302 - Modified addGridAttributes(),
 *                              checkPaneCellAttributes(),
 *                              addPaneTableOrCellAttributes and
 *                              addRowIteratorPaneAttributes to not output
 *                              a width attribute if the value is set to 0.
 * 10-Jul-02    Steve           VBM:2002040807 - Add div around pane output
 *                              if the pane has a style class so the attributes
 *                              affect the whole cell.
 * 15-Jul-02    Adrian          VBM:2002053104 - Move methods openInclusion and
 *                              closeInclusion up to XHTMLBasic.
 * 23-Jul-02    Phil W-S        VBM:2002071706 - Fix
 *                              addPaneTableOrCellAttributes to output the
 *                              width attribute at the correct time (non-zero
 *                              pixels units, non-zero and non-100 percent
 *                              units and 100% units outside tables - the
 *                              latter was already coded so has been left in).
 * 24-Jul-02    Phil W-S        VBM:2002071704 - Fix addPaneTableAttributes to
 *                              output the border, cellspacing and cellpadding
 *                              attributes using a device default if they have
 *                              not been given otherwise (border is only
 *                              output as a device default if the device is
 *                              a Netscape4 browser).
 * 25-Jul-02    Phil W-S        VBM:2002072410 - Updated
 *                              checkPaneTableAttributes to also indicate that
 *                              a table will be needed if the width of a pane
 *                              is set to anything but 100%. This ensures
 *                              compatibility with the Deimos build but changes
 *                              the table generation rules defined by
 *                              2002042205.
 * 26-Jul-02    Steve           VBM:2002040807 - moved the protocol element
 *                              mapping initialisation into the class
 *                              constructor and out of initialise() so that
 *                              it only gets called once.
 * 11-Sep-02    Steve           VBM:2002040809 - Call getPaneStyle() method
 *                              to get table attributes from theme or layout
 *                              for panes and derived panes
 * 18-Nov-02    Geoff           VBM:2002111504 - Avoid deprecated methods of
 *                              page context, clean up imports, remove unused
 *                              locals.
 * 12-Dec-02    Phil W-S        VBM:2002110516 - Added overriding versions of
 *                              openGrid and closeGrid to maintain the current
 *                              functionality.
 * 16-Dec-02    Adrian          VBM:2002100203 - Added more comprehensive
 *                              javadoc to constructor where elementMappings
 *                              field is populated.
 * 08-Jan-03    Phil W-S        VBM:2002110402 - Add support for the Grid and
 *                              Pane optimization level attributes. This adds
 *                              the supportsFormatOptimization flag, the
 *                              optimizingTransformer association and the
 *                              addOptimizeAttribute method. Also affects the
 *                              constructor, getDOMTransformer,
 *                              openPaneTable, openGrid and
 *                              openRowIteratorPane methods. Removed redundant
 *                              import statements.
 * 20-Jan-03    Geoff           VBM:2003011616 - Removed redundant
 *                              supportsAccessKeyAttribute and
 *                              supportsExternalStyleSheets settings which were
 *                              just setting it to the same as the superclass.
 * 29-Jan-03    Doug            VBM:2003012713 - Modified the constructor to
 *                              call setOptGroupDepth() with a value of one.
 * 20-Mar-03    Sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 21-Mar-03    Sumit           VBM:2003022828 - writeJavaScript...() adds a
 *                              test for empty fields if the isEmptyOk is set
 * 09-Apr-03    Phil W-S        VBM:2002111502 - Augment
 *                              addPhoneNumberAttributes in the same way that
 *                              addAnchorAttributes is but for only those
 *                              attributes that are relevant to phone number
 *                              links.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * 17-Apr-03    Geoff           VBM:2003040305 - Modified addEventAttribute to
 *                              use new Script object.
 * 09-May-03    Byron           VBM:2003042205 - Added
 *                              openSpatialFormatIterator which calls
 *                              openSpatialFormatIteratorOriginal.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.OptimizationLevelAttribute;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.AddressAttributes;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.BigAttributes;
import com.volantis.mcs.protocols.BlockQuoteAttributes;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.BoldAttributes;
import com.volantis.mcs.protocols.CiteAttributes;
import com.volantis.mcs.protocols.CodeAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DefaultScriptHandler;
import com.volantis.mcs.protocols.DefinitionDataAttributes;
import com.volantis.mcs.protocols.DefinitionListAttributes;
import com.volantis.mcs.protocols.DefinitionTermAttributes;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.EmphasisAttributes;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.FormatAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.HeadingAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ItalicAttributes;
import com.volantis.mcs.protocols.KeyboardAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.mcs.protocols.MonospaceFontAttributes;
import com.volantis.mcs.protocols.NoScriptAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.ParagraphAttributes;
import com.volantis.mcs.protocols.PhoneNumberAttributes;
import com.volantis.mcs.protocols.PreAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.SampleAttributes;
import com.volantis.mcs.protocols.Script;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SmallAttributes;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;
import com.volantis.mcs.protocols.StrongAttributes;
import com.volantis.mcs.protocols.StyleAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableFooterAttributes;
import com.volantis.mcs.protocols.TableHeaderAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.XFActionAttributes;
import com.volantis.mcs.protocols.XFBooleanAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFSelectAttributes;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.html.menu.DeprecatedEventAttributeUpdater;
import com.volantis.mcs.protocols.html.menu.XHTMLFullMenuModuleRendererFactory;
import com.volantis.mcs.protocols.html.xhtmlfull.CellPaddingAnalyser;
import com.volantis.mcs.protocols.html.xhtmlfull.TextInputFormatScriptGenerator;
import com.volantis.mcs.protocols.html.xhtmlfull.XHTMLFullUnabridgedTransformer;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.menu.MenuModule;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.menu.shared.DefaultMenuModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.styles.DefaultingPropertyHandler;
import com.volantis.mcs.protocols.styles.EdgePropertyHandler;
import com.volantis.mcs.protocols.styles.NoopPropertyUpdater;
import com.volantis.mcs.protocols.styles.PositivePixelLengthHandler;
import com.volantis.mcs.protocols.styles.PropertyClearer;
import com.volantis.mcs.protocols.styles.PropertyHandler;
import com.volantis.mcs.protocols.styles.ValueHandler;
import com.volantis.mcs.protocols.trans.OptimizationConstants;
import com.volantis.mcs.protocols.trans.UnabridgedTransformer;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.log.LogDispatcher;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Iterator;


/**
 * <p>XHTMLFull, the base HTML sub-class</p>
 *
 * <p>This sub-class of the root protocol class is itself a root for different
 * versions of HTML. The methods here are XHTML 1.0 Full tags. For each version
 * that sub-classes this, only a few methods will need to be overridden to
 * customise support. Most will work "as is".</p>
 *
 * <h2>Events and scripting</h2>
 *
 * <p>This protocol is the first one in the hierarchy to support scripts and
 * events so this is where the support is added. </p>
 */
public class XHTMLFull
    extends XHTMLBasic implements DeprecatedEventAttributeUpdater {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(XHTMLFull.class);

    /**
     * This flag specifies whether the current protocol supports scripts.
     */
    protected boolean supportsScripts;

    /**
     * This flag indicates whether a protocol supports the Pane and Grid
     * layout optimization functionality. Can be reset by specializations
     * if this functionality is inappropriate.
     */
    protected boolean supportsFormatOptimization;

    private final int supportedPageEvents;
    private final int supportedFocusEvents;
    private final int supportedFormEvents;
    private final int supportedFieldEvents;

    /**
     * Specializations of this protocol can control the specific
     * UnabridgedTransformer used.
     */
    protected UnabridgedTransformer optimizingTransformer;

    private PropertyHandler paddingHandler;

    /**
     * The object to use to check to see whether a pane needs a table or not.
     *
     * <p>Instances of this are stateful so must be initialised in the
     * initialise method, not in the constructor.</p>
     */
    private XHTMLFullPaneTableChecker paneTableChecker;

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     * objects.
     * @param configuration The protocol specific configuration.
     */
    public XHTMLFull(ProtocolSupportFactory supportFactory,
                     ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        // This protocol supports scripts.
        supportsScripts = true;

        // This protocol supports format optimization
        supportsFormatOptimization = true;

        optimizingTransformer =
                new XHTMLFullUnabridgedTransformer(protocolConfiguration);

        // This protocol supports events.

        supportedPageEvents = EventConstants.PAGE_EVENTS_MASK;
        supportedFocusEvents = EventConstants.FOCUS_EVENTS_MASK;
        supportedFormEvents = EventConstants.FORM_EVENTS_MASK;
        supportedFieldEvents = EventConstants.FIELD_EVENTS_MASK;

        // This protocol supports inline styles.
        supportsInlineStyles = true;

        supportsDissectingPanes = false;

        // We like hr elements

        /**
         * XHTMLFull does not need to use div for menus because it does support
         * span.
         */
        usesDivForMenus = false;

        // This is set because some HTML devices do not support targetting
        // of id's properly; but they usually support targetting of names.
        enableNameIdentification = true;

        // set the max optgroup depth to 1
        setOptGroupDepth(1);

        // Set the protocol default for nested table support (back) to true.
        // This may be updated by the initialise method depending on the
        // value in the device database.
        supportsNestedTables = true;

    }

    public void initialise () {
        super.initialise ();

        // Set the default script handler to use.
        scriptHandler = DefaultScriptHandler.getSingleton ();
    }

    // Javadoc inherited.
    protected void initialiseStyleHandlers() {
        super.initialiseStyleHandlers();

        // Initialise the style value handlers.
        paneTableChecker = new XHTMLFullPaneTableChecker();
        paneTableChecker.setWidthChecker(widthHandler);

        ValueHandler valueHandler = new PositivePixelLengthHandler();
        PropertyHandler propertyHandler = new EdgePropertyHandler(
                PropertyGroups.PADDING_PROPERTIES, valueHandler,
                NoopPropertyUpdater.getDefaultInstance());
        paddingHandler = new DefaultingPropertyHandler(
                propertyHandler, "0");
    }

    // ========================================================================
    //   General helper methods.
    // ========================================================================

    /**
     * Add an event attribute to the StringOutputBuffer.
     * @param element The Element to modify.
     * @param attributes The attributes object.
     * @param eventAttributeNames An array of the names of the event
     * attributes.
     * @param supportedEventsMask A mask of the events which are supported
     * on the current element.
     * @param eventIndex The index of the event.
     * @param internalScript Any internal script that we add.
     */
    protected void addEventAttribute(Element element,
                                     MCSAttributes attributes, String[] eventAttributeNames,
                                     int supportedEventsMask, int eventIndex, String internalScript)
            throws ProtocolException {

        if (!protocolConfiguration.supportsEvents()) {
            return;
        }

        EventAttributes eventAttributes
            = attributes.getEventAttributes (false);
        if (eventAttributes == null) {
            return;
        }

        int bit = (1 << eventIndex);

        // If the attribute name is not specified then ignore it.
        String attributeName = eventAttributeNames [eventIndex];
        if (attributeName == null) {
            return;
        }

        // If the supportedEvents mask doesn't have the bit associated
        // with the current event set then ignore it.
        if ((supportedEventsMask & bit) != bit) {
            return;
        }

        ScriptAssetReference userScript = eventAttributes.getEvent (eventIndex);

        addEventAttribute (element, attributeName, userScript, internalScript);
    }

    /**
     * Add field event attributes.
     * @param element The Element to modify.
     * @param attributes The attributes object which contains the events.
     */
    private void addFieldEventAttributes (Element element,
                                  MCSAttributes attributes) throws ProtocolException {

        // All fields support focus events.
        addEventAttributes (element, attributes, eventAttributeNames,
                            supportedFieldEvents | supportedFocusEvents);
    }

    /**
     * Add focus event attributes.
     * @param element The Element to modify.
     * @param attributes The attributes object which contains the events.
     */
    private void addFocusEventAttributes (Element element,
                                  MCSAttributes attributes) throws ProtocolException {

        addEventAttributes (element, attributes, eventAttributeNames,
                            supportedFocusEvents);
    }

    protected
    void addFormEventAttributes (Element element,
                                 MCSAttributes attributes) throws ProtocolException {

        addEventAttributes (element, attributes, eventAttributeNames,
                            supportedFormEvents);
    }

    /**
     * Add page event attributes.
     * @param element The Element to modify.
     * @param attributes The attributes object which contains the events.
     */
    private void addPageEventAttributes (Element element,
                                 MCSAttributes attributes) throws ProtocolException {

        addEventAttributes (element, attributes, eventAttributeNames,
                            supportedPageEvents);
    }

    // ========================================================================
    //   Page element methods
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addBodyAttributes (Element element,
                                      BodyAttributes attributes) throws ProtocolException {

        super.addBodyAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes.getCanvasAttributes ());

        // Add the page event attributes.
        addPageEventAttributes (element, attributes.getCanvasAttributes ());
    }

    // Javadoc inherited from super class.
    protected void openStyle (DOMOutputBuffer dom,
                              StyleAttributes attributes) {

        performDefaultOpenStyle(dom, attributes, true);
    }

    // Javadoc inherited from super class.
    protected void closeStyle (DOMOutputBuffer dom,
                               StyleAttributes attributes) {
        
        performDefaultCloseStyle(dom, true);
    }

    // Javadoc inherited from super class.
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "html", "-//W3C//DTD XHTML 1.0 Strict//EN",
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", null,
                MarkupFamily.XML);
        document.setDocType(docType);
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    /**
     * Override this method as the optimizingTransformer should be returned
     * if this protocol supports format optimization, otherwise return null.
     */
    protected DOMTransformer getDOMTransformer () {
        DOMTransformer result = null;

        if (supportsFormatOptimization) {
            result = optimizingTransformer;
        }

        return result;
    }

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    /**
     * Allows the optimization attribute to be added to a table element if
     * needed. The attribute is not added if the protocol doesn't support
     * format optimization. If optimization is supported the default value will
     * never be added. Either way, a null value is never used to generate an
     * attribute.
     *
     * @param table the DOM table element to be updated
     * @param attributes the optimization choice selected by the layout designer
     */
    protected void addOptimizeAttribute(Element table,
                                        OptimizationLevelAttribute attributes) {

        if ("table".equals(table.getName())) {
            String result = getOptimizeAttributeValue(attributes);
            if (result != null) {
                table.setAttribute(OptimizationConstants.OPTIMIZATION_ATTRIBUTE,
                        result);
            }
        }
    }

    private String getOptimizeAttributeValue(
            OptimizationLevelAttribute attributes) {

        String result = null;
        if (supportsFormatOptimization) {
            String choice = attributes.getOptimizationLevel();
            if (choice != null) {
                if (FormatConstants.OPTIMIZATION_LEVEL_VALUE_ALWAYS.
                        equals(choice)) {
                    result = OptimizationConstants.OPTIMIZE_ALWAYS;

                } else if (FormatConstants.OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT.
                        equals(choice)) {
                    result = OptimizationConstants.OPTIMIZE_LITTLE_IMPACT;

                } else if (FormatConstants.OPTIMIZATION_LEVEL_VALUE_NEVER.
                        equals(choice)) {
                    // Specifically do nothing. This is the default for this
                    // protocol set

                } else {
                    logger.warn("unknown-optimization-level",
                            new Object[]{choice});

                }
            }
        }
        return result;
    }

    /**
     * Add extra attributes to the column iterator pane markup.
     * Note that this includes the propagation of optimization attributes.
     * If this method is overridden, then the overriding method must either
     * call its superclass' version of the method or explicitly add the
     * optimization attribute as below, or no table optimization will take
     * place.
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addColumnIteratorPaneAttributes (Element element,
                                                    ColumnIteratorPaneAttributes attributes) {

        // add the super classes attributes first
        super.addColumnIteratorPaneAttributes (element, attributes);

        element.setAttribute ("width", "100%");

        if (attributes.getPane() != null) {
            addOptimizeAttribute(element, attributes.getPane());
        }

        addPaneTableAttributes(element, attributes);
    }

    /**
     * Add extra attributes to the grid markup.
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addGridAttributes (Element element,
                                      GridAttributes attributes) {

        // add the super classes attributes first
        super.addGridAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;
        PropertyHandler widthHandler;

        widthHandler = getCorrectWidthHandler(element);
        // Render the various attributes into a StringBuffer
        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute ("width", value);
        }

        addTableAttributes(element, attributes);
    }


    // Javadoc inherited from super class.
    protected void openGrid(DOMOutputBuffer dom,
                            GridAttributes attributes) {

        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);

        addOptimizeAttribute(element, (OptimizationLevelAttribute)
                attributes.getFormat());

        // allow subclasses to specify additional attributes
        addGridAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeGrid(DOMOutputBuffer dom,
                             GridAttributes attributes) {

        Element element = dom.closeElement("table");
        postTableConstruction(element);
    }

    /**
     * <p>Check how to render the pane.</p>
     *
     * <p>A pane needs a table if any of the following are true.</p>
     *
     * <ul>
     *
     * <li>Its border width is not 0.</li>
     *
     * <li>Its cell spacing is not 0.</li>
     *
     * <li>Its cell padding is not 0.</li>
     *
     * <li>Its parent element is not a "td" and any of the following are true.
     *
     * <ul>
     *
     * <li>Background colour is set.</li>
     *
     * <li>Background component is set and resolves to a url.</li>
     *
     * <li>Width is set.</li>
     *
     * <li>Height is set.</li>
     *
     * </ul>
     *
     * </li>
     *
     * <li>Its parent element is a "td" which already has attributes set which
     * could clash with those which need to be set for the pane. </li>
     *
     * </ul>
     *
     * If the pane does not need a table then it may need to use the enclosing
     * table cell element if any. </p>
     *
     * @return One of USE_TABLE, USE_ENCLOSING_TABLE_CELL,
     *         USE_NEW_ENCLOSING_TAG or DO_NOTHING.
     */
    protected PaneRendering checkPaneRendering(DOMOutputBuffer dom,
                                               PaneAttributes attributes) {

        if (checkPaneTableAttributes(attributes)) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Pane needs to be rendered as a table as it has " +
                        "attributes which can only be added to a table");
            }

            return PaneRendering.USE_TABLE;
        }

        // Check to see whether the enclosing element is a "td" because if
        // it is we could add the pane attributes to it (assuming that they
        // do not clash with existing attributes) instead of creating a new
        // table.
        Element element = dom.getCurrentElement();

        if (!"td".equals(element.getName())) {
            element = null;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Enclosing element is " + element);
        }

        // If the enclosing element is a "td" then we need to check for each
        // attribute that has been set whether the associated attribute has
        // been set on the "td" or not. If it has then we have to generate a
        // table.

        // If the enclosing element is not a "td" then if any of the attributes
        // have been set we need to generate a table.

        PaneRendering rendering = checkPaneCellAttributes(element, attributes);
        if (logger.isDebugEnabled()) {
            String name = attributes.getPane().getName();
            if (rendering == PaneRendering.USE_TABLE) {
                logger.debug("Pane " + name + " needs to be rendered as a"
                             + " table as it has attributes which need"
                             + " to be added to a cell but either there"
                             + " is no enclosing cell, or the enclosing"
                             + " cell already has the attributes set");
            } else if (rendering == PaneRendering.USE_ENCLOSING_TABLE_CELL) {
                logger.debug("Pane " + name + " can use the enclosing"
                             + " table cell as the attributes which are"
                             + " set do not clash with the existing"
                             + " attributes on the cell");
            } else if (rendering == PaneRendering.DO_NOTHING) {
                logger.debug("Pane " + name + " does not need any mark up");

            } else if (rendering == PaneRendering.CREATE_ENCLOSING_ELEMENT) {
                logger.debug("Pane '" + name + "' cannot use the enclosing " +
                             "table cell. A new enclosing element will be " +
                             "created");

            } else {
                logger.debug("Pane " + name + " has unknown rendering "
                             + rendering);
            }
        }

        return rendering;
    }

    /**
     * Determine whether we support either external or inline stylesheets.
     * @return true if we support either external or inline stylesheets.
     */
    protected boolean supportsStyleSheets() {
        return supportsExternalStyleSheets || supportsInlineStyles;
    }

    // Javadoc inherited from super class.
    protected void openPane(DOMOutputBuffer dom,
                            PaneAttributes attributes) {

        PaneRendering rendering = checkPaneRendering(dom, attributes);

        // Store it away in the PaneInstance for later use.
        PaneInstance paneInstance = (PaneInstance)
                context.getDeviceLayoutContext().getCurrentFormatInstance(
                        attributes.getPane());
        paneInstance.setRendering (rendering);

        if (rendering == PaneRendering.USE_TABLE) {
            openPaneTable(dom, attributes);

        } else if (rendering == PaneRendering.USE_ENCLOSING_TABLE_CELL) {
            useEnclosingTableCell(dom, attributes);

        } else if (rendering == PaneRendering.CREATE_ENCLOSING_ELEMENT) {
            try {
                createEnclosingElement(dom, attributes);
            } catch (ProtocolException e) {
                throw new UndeclaredThrowableException(e);
            }
        } else if (rendering == PaneRendering.DO_NOTHING) {
            // We can really do nothing here as there is no additional markup
            // required (assuming our checkPaneRendering was accurate!)
        } else {
            throw new IllegalStateException("Unknown rendering " + rendering);
        }
    }

    /**
     * Use a new enclosing element to render attributes and/or styles.
     *
     * @param dom        the dom output buffer to render into.
     * @param attributes the pane attributes.
     */
    protected void createEnclosingElement(DOMOutputBuffer dom,
                                          PaneAttributes attributes)
            throws ProtocolException {

        Styles styles = attributes.getStyles();

        // If the valign attribute is non-null we are forced to use a table
        // rather than a div element.
        if (verticalAlignHandler.isSignificant(styles)) {
            // We are forced to use a table. Set the rendering to USE_TABLE
            // so that the closePane does not attempt to close a div element
            PaneInstance paneInstance = (PaneInstance)
                    context.getDeviceLayoutContext().getCurrentFormatInstance(
                            attributes.getPane());
            paneInstance.setRendering(PaneRendering.USE_TABLE);

            openPaneTable(dom, attributes);
        } else {
            // Open a new tag and add the style class to this element.
            openDiv(dom, createDivAttributes(attributes));
        }
    }

    /**
     * Use a new enclosing element to render attributes and/or styles.
     *
     * @param dom        the dom output buffer to render into.
     * @param attributes the pane attributes.
     */
    private void closeCreatedEnclosingElement(DOMOutputBuffer dom,
                                                PaneAttributes attributes) {
        closeDiv(dom, createDivAttributes(attributes));
    }

    /**
     * Create div attributes from the pane attributes.
     *
     * @param attributes the pane attributes.
     * @return newly created div attributes from the pane attributes.
     */
    private DivAttributes createDivAttributes(PaneAttributes attributes) {
        DivAttributes divAttributes = new DivAttributes();
        divAttributes.copy(attributes);
        return divAttributes;
    }

    /**
     * Use the enclosing table cell to render attributes and/or styles.
     *
     * @param dom        the dom output buffer to render into.
     * @param attributes the pane attributes.
     */
    protected void useEnclosingTableCell(DOMOutputBuffer dom,
                                         PaneAttributes attributes) {
        Element element = dom.getCurrentElement();

        Styles styles = attributes.getStyles();

        addCoreAttributes(element, attributes);

        // Add those attributes which can be added to either a table or
        // a table cell.
        addPaneTableOrCellAttributes(element, attributes, false);

        // Add the attributes which can only be added to a table cell.
        addPaneCellAttributes(element, styles);

        // preserve any Styles information on the pane element
        if (styles != null) {
            StylesMerger merger = StylingFactory.getDefaultInstance().
                    getStylesMerger();
            final Styles mergedStyles = merger.merge(
                    styles, element.getStyles());
            // restore the table-cell display style
            mergedStyles.getPropertyValues().setComputedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.TABLE_CELL);
            mergedStyles.getPropertyValues().setSpecifiedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.TABLE_CELL);
            element.setStyles(mergedStyles);
        }
    }

    // javadoc inherited
    protected void closePane(DOMOutputBuffer dom,
                             PaneAttributes attributes) {
        // Get the rendering from the PaneContext.
        PaneInstance paneInstance = (PaneInstance)
                context.getDeviceLayoutContext().getCurrentFormatInstance(
                        attributes.getPane());
        PaneRendering rendering = paneInstance.getRendering();

        if (rendering == PaneRendering.USE_TABLE) {
            closePaneTable(dom, attributes);
        } else if (rendering == PaneRendering.USE_ENCLOSING_TABLE_CELL) {
        } else if (rendering == PaneRendering.CREATE_ENCLOSING_ELEMENT) {
            closeCreatedEnclosingElement(dom, attributes);
        } else if (rendering == PaneRendering.DO_NOTHING) {
            // We can really do nothing here, see {@link #openPane}
        } else {
            throw new IllegalStateException("Unknown rendering " + rendering);
        }
    }

    /**
     * Opens a table to render the pane. If the pane has a style class and the
     * protocol supports stylesheets then a div element is used to deal with
     * the stylesheet. If a div is used, it is given the core attributes.
     * Otherwise the table is given the core attributes.
     * @param dom the DOMOutputBuffer in use
     * @param attributes the pane's attributes
     */
    protected void openPaneTable(DOMOutputBuffer dom,
                                 PaneAttributes attributes) {

        Styles tableStyles = attributes.getStyles();

        // A pane is normally treated as a CSS block so now we have to turn
        // it into a table.
        Element element = dom.openStyledElement("table", tableStyles,
                DisplayKeywords.TABLE);

        addOptimizeAttribute(element, attributes.getPane());
        addPaneTableAttributes(element, attributes);

        StylingFactory factory = StylingFactory.getDefaultInstance();
        Styles rowStyles = factory.createInheritedStyles(tableStyles,
                DisplayKeywords.TABLE_ROW);
        dom.openStyledElement("tr", rowStyles);

        Styles cellStyles = factory.createInheritedStyles(rowStyles,
                DisplayKeywords.TABLE_CELL);

        // Move the padding style from the table to the cell. This is required
        // because we render cell padding as a cellpadding attribute on the
        // table and a padding CSS style. The cellpadding attribute is equivalent
        // to the padding style on the cell - applying the style to the table
        // results in the padding being set twice (once between the table and the
        // cell, once between the cell and its content).
        //
        // Note that we must do this after opening the table since the padding
        // information is required in order to specify the cellpadding attribute.
        moveProperty(tableStyles, cellStyles, StylePropertyDetails.PADDING_BOTTOM);
        moveProperty(tableStyles, cellStyles, StylePropertyDetails.PADDING_TOP);
        moveProperty(tableStyles, cellStyles, StylePropertyDetails.PADDING_LEFT);
        moveProperty(tableStyles, cellStyles, StylePropertyDetails.PADDING_RIGHT);

        Element cell = dom.openStyledElement("td", cellStyles);

        // Add the styles from the pane styles which were added to the table.
        // todo: This will cause the text-align style on the table to be
        // todo: ignored when generating the CSS (but will still be used for
        // todo: detecting inherited values. This should use the cell styles
        // todo: and copy the styles needed down from the table.
        addPaneCellAttributes(cell, tableStyles);

        // todo XDIME-CP check to see whether the tableStyles associated cannot be added to the td, if not then add the div, otherwise do not.
        if (supportsStyleSheets()) {
            Styles divStyles = factory.createInheritedStyles(cellStyles,
                    DisplayKeywords.BLOCK);
            Element div = dom.openStyledElement("div", divStyles);
            addCoreAttributes(div, attributes);
        } else {
            addCoreAttributes(element, attributes);
        }
    }

    /**
     * Move a style property from one {@link Styles} to another if it exists on
     * the first. If it exists on both, the destination property will be
     * overwritten.
     *
     * @param source The source for the property
     * @param destination The destination for the property
     * @param property The style property to move
     */
    private void moveProperty(Styles source, Styles destination, StyleProperty property) {
        MutablePropertyValues sourceValues = source.getPropertyValues();
        StyleValue specifiedValue = sourceValues.getSpecifiedValue(property);
        StyleValue computedValue = sourceValues.getComputedValue(property);
        MutablePropertyValues dstValues = destination.getPropertyValues();
        if (specifiedValue != null) {
            dstValues.setSpecifiedValue(property, specifiedValue);
        }
        if (computedValue != null) {
            dstValues.setComputedValue(property, computedValue);
        }

        // todo: should probably set this to the inferred value, i.e. the
        // todo: inherited or initial value.
        sourceValues.clearPropertyValue(property);
    }

    /**
     * Closes the table which renders the pane.
     * @param dom the DOMOutputBuffer in use
     * @param attributes the pane's attributes
     */
    private void closePaneTable(DOMOutputBuffer dom,
                                  PaneAttributes attributes) {
        if (supportsStyleSheets()) {
            dom.closeElement("div");
        }

        dom.closeElement("td");
        dom.closeElement("tr");
        dom.closeElement("table");
    }

    /**
     * Check the pane's layout and theme attributes to see whether a table must
     * be used to render the pane. A table is used if any of the following are
     * true:
     *
     * <ul>
     *
     * <li>the width is specified by the pane and is not 0 and is not 100%</li>
     *
     * <li>a non-zero borderwidth is specified by the pane or its theme</li>
     *
     * <li>a non-zero cell padding is specified by the pane or its theme</li>
     *
     * <li>a non-zero cell spacing is specified by the pane or its theme</li>
     *
     * </ul>
     *
     * @return true if a table must be used; false otherwise.
     */
    private boolean checkPaneTableAttributes(PaneAttributes attributes) {
        return paneTableChecker.checkTable(attributes.getStyles());
    }

    /**
     * Add attributes to the table created for the pane.
     *
     * @param element    The Element to modify.
     * @param attributes the attributes
     */
    protected void addPaneTableAttributes(Element element,
                                          PaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        // Ensure that we add the attributes that apply to both tables and
        // cells
        addPaneTableOrCellAttributes(element, attributes, true);

        // Padding on the pane is equivalent to cell padding on the table.
        if ((value = paddingHandler.getAsString(styles)) != null) {
            element.setAttribute("cellpadding", value);
        }

    // Add the common table attributes from layout and theme
        addTableAttributes(element, attributes);
    }

    /**
     * Adds the specific table attributes given in the parameters, overriding
     * these values from the associated style properties if applicable.
     *
     * @param element the element to which the attributes are to be added
     * @param attributes the attributes used to obtain the style properties
     */
    protected void addTableAttributes(
            Element element,
            FormatAttributes attributes) {

        Styles styles = attributes.getStyles();

        final String deviceDefault = "0";
        String value;

        // Only add the border attribute to the table if the device does
        // not allow this effect to be achieved using CSS.
        // @todo should this check should be performed before adding attributes for other properties that can be rendered using CSS? (i.e. bgcolor)
        if (!supportsCSS) {
            value = borderHandler.getAsString(styles);
            if (value != null) {
                element.setAttribute("border", value);
            }
        }

        // Cell padding has to be done after the table and all its children
        // have been created.

        value = borderSpacingHandler.getAsString(styles);
        if (value == null) {
            value = deviceDefault;
        }
        if (value != null) {
            element.setAttribute("cellspacing", value);
        }
    }

    protected void postTableConstruction(Element element) {
        super.postTableConstruction(element);

        CellPaddingAnalyser analyser = new CellPaddingAnalyser();
        String value = analyser.calculateCellPadding(
                element, PropertyClearer.getDefaultInstance());
        if (value != null) {
            element.setAttribute("cellpadding", value);
        }
    }

    /**
     * <p>Check the pane's attributes to see whether they can use the td
     * element, or we must use a table, or a new element or do not need to use
     * anything.</p>
     *
     * <pre>
     * If the the width attribute value is null, or '0' or set to 100% then:
     *      DO_NOTHING
     * otherwise
     *      if the enclosing element is null or has a width set already then
     *          USE_TABLE
     *      otherwise
     *          USE_ENCLOSING_CELL
     *
     * If the rendering isn't USE_TABLE and
     *      the enclosing cell and attribute have style class values and
     *      the style classes do not match and
     *      the attribute's style class doesn't contain the element's style
     * class then:
     *      If multiple style class attribute are supported
     *          USE_ENCLOSING_TABLE_CELL (requires a merge of style classes)
     *      otherwise
     *          CREATE_ENCLOSING_ELEMENT
     *
     * If the rendering is DO_NOTHING and
     *      the attributes have a style class defined or
     *      the attributes have a alignment and that value isn't the same
     *      as the containing markup's alignment, if any, then:
     *          CREATE_ENCLOSING_ELEMENT
     * </pre>
     *
     * @param element    the parent element (may be null).
     * @param attributes the pane attributes.
     * @return One of the {@link com.volantis.mcs.protocols.PaneRendering}
     *         enumeration instances.
     */
    protected PaneRendering checkPaneCellAttributes(
            Element element,
            PaneAttributes attributes) {

        Styles styles = attributes.getStyles();

        // Assume that we don't need to do anything to render this pane.
        PaneRendering rendering = PaneRendering.DO_NOTHING;
        boolean width = widthHandler.isSignificant(styles);
        if (width) {
            if (logger.isDebugEnabled()) {
                logger.debug("Width set to " + width);
            }

            if ((element == null) ||
                    element.getAttributeValue("width") != null) {
                rendering = PaneRendering.USE_TABLE;
            } else {
                rendering = PaneRendering.USE_ENCLOSING_TABLE_CELL;
            }
        }

        // If the rendering is not set to USE_TABLE and styles have been
        // specified on the pane then we should use either
        // CREATE_ENCLOSING_ELEMENT or USE_ENCLOSING_TABLE_CELL,
        // depending on whether the specified styles can be rendered on the
        // enclosing table cell.
        if (rendering != PaneRendering.USE_TABLE) {

            // if there are any explicitly set styles on the pane then we
            // should not DO_NOTHING because that would result in these styles
            // being lost.
            PropertyValues propertyValues = attributes.getStyles().
                    getPropertyValues();
            if (propertyValues != null &&
                    propertyValues.hasExplicitlySpecified()) {
                rendering = PaneRendering.USE_ENCLOSING_TABLE_CELL;
            }

            rendering = checkPaneRendering(attributes, element, rendering);
        }

        return rendering;
    }

    /**
     * Returns the given rendering if no special handling is required or {@link
     * PaneRendering#CREATE_ENCLOSING_ELEMENT} if the given element cannot be
     * used or is null and styling exists that means that some form of markup
     * must be generated.
     *
     * @param attributes       the attributes of the pane to be rendered
     * @param element          the enclosing table cell or null if there is
     *                         none
     * @param defaultRendering the value to be returned if no special handling
     *                         is required
     * @return a pane rendering choice based on the pane's attributes and
     *         styling and the optional enclosing table cell
     */
    private PaneRendering checkPaneRendering(
            final PaneAttributes attributes,
            final Element element,
            final PaneRendering defaultRendering) {

        Styles styles = attributes.getStyles();

        final String hAlign = horizontalAlignChecker.getAsString(styles);
        PaneRendering rendering = defaultRendering;

        // Check to see if we have an alignment
        if (hAlign != null) {
            if (element != null) {
                final String elementHAlign = element.getAttributeValue("align");

                if ((elementHAlign != null) && !elementHAlign.equals(hAlign)) {
                    // There is an alignment that isn't catered
                    // for by the enclosing table cell so we need
                    // to render an enclosing element to handle it
                    rendering = PaneRendering.CREATE_ENCLOSING_ELEMENT;
                }
            } else {
                // There is an alignment required so we need to at
                // least generate an enclosing element. Note that
                // we could potentially suppress this element if
                // the alignment is the default value, but won't
                // in case alignment is to be inherited from other
                // containing markup
                rendering = PaneRendering.CREATE_ENCLOSING_ELEMENT;
            }
        }

        return rendering;
    }

    /**
     * Add attributes to the table or cell created for the pane. If a table is
     * used for the pane then these will be added to it, otherwise they will
     * be added to the enclosing table cell.
     * @param element The Element to modify.
     * @param attributes the attributes
     * @param table True if the element is a table and false if it is a cell.
     */
    protected void addPaneTableOrCellAttributes(Element element,
                                                PaneAttributes attributes,
                                                boolean table) {
        addPaneTableOrCellWidth(element, attributes, table);

    }

    /**
     * Add width to the table or cell representing the pane. If a table is
     * used for the pane then this attribute will be added to it, otherwise it
     * will be added to the enclosing table cell.
     *
     * <p><strong>Note:</strong> a width of 100% will not be added to the table
     * cell as a table cell will automatically be 100% of the width of the
     * table if the table only has 1 column or will be a width appropriate to
     * the number of columns and the table content.</p>
     *
     * @param element the Element to modify
     * @param attributes the attributes
     * @param table True if the element is a table and false if it is a cell.
     */
    protected void addPaneTableOrCellWidth(Element element,
                                           PaneAttributes attributes,
                                           boolean table) {

        Styles styles = attributes.getStyles();

        // Decide if we should remove the width: value from the styles after
        // rendering the width onto the element. Usually we do but if we are
        // rendering a pane table which will always be optimised then we need
        // to keep the style value as we know that the HTML will be thown away.
        // See 2005110903/2005113024.
        //
        // Default to removing the width: after creating the HTML version.
        PropertyHandler widthRenderer = widthHandler;
        // If we are rendering a pane table..
        if (table) {
            // and if we are always going to optimise this pane table
            String optimize = getOptimizeAttributeValue(attributes.getPane());
            if (OptimizationConstants.OPTIMIZE_ALWAYS.equals(optimize)) {
                // Then do not remove the width: after creating the HTML
                // version.
                // todo: better: maybe we do not need to bother rendering width
                // into the html at all since it will be discarded anyway?
                widthRenderer = tableWidthChecker;
            } else {
                widthRenderer = tableWidthHandler;
            }
        }

        // TODO: should this significance check be done automagically by
        // the handler? (and by extension, all handlers?)
        // TODO: investigate to see if we need render table and non-table
        // widths differently as was done in pre 3.5 versions. This may require
        // refactoring the significance checking out of PropertyHandler into
        // a higher level object so that we can define a different significance
        // for both pane tables and cells.
        // NOTE: significant check must be done before getAsString because the
        // latter is destructive.
        final boolean significant = widthRenderer.isSignificant(styles);
        String value;
        if ((value = widthRenderer.getAsString(styles)) != null) {
            if (significant) {
                element.setAttribute("width", value);
            } else {
                // throw away the value otherwise it stays in the theme
                // and will be used by CSS aware browsers to override
                // any value we already rendered on the element
            }
        }
    }

    /**
     * Add attributes to the cell created for the pane.
     *
     * @param element The Element to modify.
     * @param styles  the styles.
     */
    protected void addPaneCellAttributes(Element element, Styles styles) {

        String value;

        // Now add the specific attributes for a table cell
        if ((value = horizontalAlignHandler.getAsString(styles)) != null) {
            element.setAttribute ("align", value);
        }

        if ((value = verticalAlignHandler.getAsString(styles)) != null) {
            element.setAttribute ("valign", value);
        }
    }

    /**
     * Method to allow subclasses to specify additional attributes for
     * a row iterator pane
     * Note that this includes the propagation of optimization attributes.
     * If this method is overridden, then the overriding method must either
     * call its superclass' version of the method or explicitly add the
     * optimization attribute as below, or no table optimization will take
     * place.
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addRowIteratorPaneAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;
        PropertyHandler widthHandler = getCorrectWidthHandler(element);

        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute("width", value);
        }

        if (attributes.getPane() != null) {
            addOptimizeAttribute(element, attributes.getPane());
        }

        addPaneTableAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void openRowIteratorPane(DOMOutputBuffer dom,
                                       RowIteratorPaneAttributes attributes) {

        Element element = dom.openStyledElement("table", attributes,
                DisplayKeywords.TABLE);

        addCoreAttributes(element, attributes);

        // allow subclasses to add additional attributes
        addRowIteratorPaneAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeRowIteratorPane(DOMOutputBuffer dom,
                                        RowIteratorPaneAttributes attributes) {

        Element element = dom.closeElement("table");
        postTableConstruction(element);
    }

    /**
     * Method to allow subclasses to specify additional attributes for
     * a row iterator pane cell
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addRowIteratorPaneCellAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {

        Styles rowStyles = attributes.getStyles();

        // Create a table row with appropriate display
        dom.openStyledElement("tr", rowStyles, DisplayKeywords.TABLE_ROW);

        StylingFactory factory = StylingFactory.getDefaultInstance();
        Styles cellStyles = factory.createInheritedStyles(rowStyles,
                DisplayKeywords.TABLE_CELL);
        Element element = dom.openStyledElement("td", cellStyles);

        addRowIteratorPaneCellAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeRowIteratorPaneElement(
            DOMOutputBuffer dom,
            RowIteratorPaneAttributes attributes) {
        dom.closeElement("td");
        dom.closeElement("tr");
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    protected void addAnchorAttributes (Element element,
                                        AnchorAttributes attributes) throws ProtocolException {

        // call super method first
        super.addAnchorAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the focus event attributes.
        addFocusEventAttributes (element, attributes);
    }

    protected void addPhoneNumberAttributes(Element element,
                                            PhoneNumberAttributes attributes)
            throws ProtocolException {
        super.addPhoneNumberAttributes(element, attributes);

        addGeneralEventAttributes(element, attributes);

        addFocusEventAttributes(element, attributes);
    }

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addAddressAttributes (Element element,
                                         AddressAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addAddressAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addBlockQuoteAttributes (Element element,
                                            BlockQuoteAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addBlockQuoteAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addDivAttributes (Element element,
                                     DivAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addDivAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addHeadingAttributes(Element element,
                                        HeadingAttributes attributes, int level)
            throws ProtocolException {

        super.addHeadingAttributes (element, attributes, level);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void addHorizontalRuleAttributes (Element element,
                                                HorizontalRuleAttributes attributes) throws ProtocolException {
    }

    // Javadoc inherited from super class.
    protected void addParagraphAttributes (Element element,
                                           ParagraphAttributes attributes) throws ProtocolException {

        super.addParagraphAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addPreAttributes (Element element,
                                     PreAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addPreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // ========================================================================
    //   List element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addDefinitionListAttributes (Element element,
                                                DefinitionListAttributes attributes) throws ProtocolException {

        super.addDefinitionListAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addDefinitionTermAttributes (Element element,
                                                DefinitionTermAttributes attributes) throws ProtocolException {

        super.addDefinitionTermAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addDefinitionDataAttributes (Element element,
                                                DefinitionDataAttributes attributes) throws ProtocolException {

        super.addDefinitionDataAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected
    void addOrderedListAttributes (Element element,
                                   OrderedListAttributes attributes) throws ProtocolException {

        super.addOrderedListAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected
    void addUnorderedListAttributes (Element element,
                                     UnorderedListAttributes attributes) throws ProtocolException {

        super.addUnorderedListAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addListItemAttributes (Element element,
                                          ListItemAttributes attributes) throws ProtocolException {

        super.addListItemAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addTableAttributes (Element element,
                                       TableAttributes attributes)
            throws ProtocolException {

        super.addTableAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addTableBodyAttributes (Element element,
                                           TableBodyAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openTableBody (DOMOutputBuffer dom,
                                  TableBodyAttributes attributes)
            throws ProtocolException {

        if ("true".equals(attributes.getKeepTogether())) {
          dom.openElement(KEEPTOGETHER_ELEMENT);
        }

        Element element = dom.openStyledElement ("tbody", attributes,
                DisplayKeywords.TABLE_ROW_GROUP);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addTableBodyAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableBody (DOMOutputBuffer dom,
                                   TableBodyAttributes attributes) {
        dom.closeElement ("tbody");
        if ("true".equals(attributes.getKeepTogether())) {
          dom.closeElement(KEEPTOGETHER_ELEMENT);
        }
    }

    // Javadoc inherited from super class.
    protected void addTableCellAttributes (Element element,
                                           TableCellAttributes attributes)
            throws ProtocolException {

        super.addTableCellAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addTableFooterAttributes (Element element,
                                   TableFooterAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openTableFooter (DOMOutputBuffer dom,
                                    TableFooterAttributes attributes)
            throws ProtocolException {
        Element element = dom.openStyledElement ("tfoot", attributes,
                DisplayKeywords.TABLE_FOOTER_GROUP);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addTableFooterAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableFooter (DOMOutputBuffer dom,
                                     TableFooterAttributes attributes) {
        dom.closeElement ("tfoot");
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addTableHeaderAttributes (Element element,
                                   TableHeaderAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openTableHeader (DOMOutputBuffer dom,
                                    TableHeaderAttributes attributes)
            throws ProtocolException {
        Element element = dom.openStyledElement ("thead", attributes,
                DisplayKeywords.TABLE_HEADER_GROUP);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addTableHeaderAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeTableHeader (DOMOutputBuffer dom,
                                     TableHeaderAttributes attributes) {
        dom.closeElement ("thead");
    }

    // Javadoc inherited from super class.
    protected void addTableRowAttributes (Element element,
                                          TableRowAttributes attributes)
            throws ProtocolException {

        super.addTableRowAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }


    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addBigAttributes (Element element,
                                     BigAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openBig (DOMOutputBuffer dom,
                            BigAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("big", attributes);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addBigAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeBig (DOMOutputBuffer dom,
                             BigAttributes attributes) {

        dom.closeElement ("big");
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addBoldAttributes (Element element,
                                      BoldAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openBold (DOMOutputBuffer dom,
                             BoldAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("b", attributes);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addBoldAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeBold (DOMOutputBuffer dom,
                              BoldAttributes attributes) {
        dom.closeElement ("b");
    }

    // Javadoc inherited from super class.
    protected void addCiteAttributes (Element element,
                                      CiteAttributes attributes)
            throws ProtocolException {

        super.addCiteAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addCodeAttributes (Element element,
                                      CodeAttributes attributes)
            throws ProtocolException {

        super.addCodeAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addEmphasisAttributes (Element element,
                                          EmphasisAttributes attributes)
            throws ProtocolException {

        super.addEmphasisAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addItalicAttributes (Element element,
                                        ItalicAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openItalic (DOMOutputBuffer dom,
                               ItalicAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("i", attributes);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addItalicAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeItalic (DOMOutputBuffer dom,
                                ItalicAttributes attributes) {
        dom.closeElement ("i");
    }

    // Javadoc inherited from super class.
    protected void addKeyboardAttributes (Element element,
                                          KeyboardAttributes attributes)
            throws ProtocolException {

        super.addKeyboardAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addMonospaceFontAttributes (Element element,
                                     MonospaceFontAttributes attributes) {
    }


    /**
     * Open a menu.
     * @param dom The DOMOutputBuffer to write to.
     * @param attributes The MenuAttributes for the menu.
     */
    protected void openMenu(DOMOutputBuffer dom, MenuAttributes attributes) {
        Element element = dom.openStyledElement("span", attributes);
        addCoreAttributes(element, attributes);
    }

    /**
     * Close a menu.
     * @param dom The DOMOutputBuffer to write to.
     * @param attributes The MenuAttributes for the menu.
     */
    protected void closeMenu(DOMOutputBuffer dom, MenuAttributes attributes) {
        dom.closeElement("span");
    }

    // Javadoc inherited from super class.
    protected
    void openMonospaceFont (DOMOutputBuffer dom,
                            MonospaceFontAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("tt", attributes);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addMonospaceFontAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected
    void closeMonospaceFont (DOMOutputBuffer dom,
                             MonospaceFontAttributes attributes) {
        dom.closeElement ("tt");
    }

    // Javadoc inherited from super class.
    protected void addSampleAttributes (Element element,
                                        SampleAttributes attributes)
            throws ProtocolException {

        super.addSampleAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addSmallAttributes (Element element,
                                       SmallAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openSmall (DOMOutputBuffer dom,
                              SmallAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("small", attributes);
        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addSmallAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeSmall (DOMOutputBuffer dom,
                               SmallAttributes attributes) {
        dom.closeElement ("small");
    }

    // Javadoc inherited from super class.
    protected void addSpanAttributes (Element element,
                                      SpanAttributes attributes)
            throws ProtocolException {

        super.addSpanAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addStrongAttributes (Element element,
                                        StrongAttributes attributes)
            throws ProtocolException {

        super.addStrongAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    /**
     * Method to allow subclasses to specify additional attributes for
     * the image element
     * @param element The Element to modify.
     * @param attributes the attributes
     */
    protected void addImageAttributes (Element element,
                                       ImageAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addImageAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited
    protected void addMetaAttributes (Element element,
                                      MetaAttributes attributes) {
        // call super method first
        super.addMetaAttributes (element, attributes);

        if (attributes != null) {
            String value;
            if ((value = attributes.getLang ()) != null) {
                element.setAttribute ("lang", value);
            }
        }
    }

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addMenuItemAttributes (Element element,
                                          MenuItem attributes,
                                          int type) throws ProtocolException {

        super.addMenuItemAttributes (element, attributes, type);

        // It would be good to always add the general events here but
        // unfortunately this method is called by doMenuItemAnchor which
        // is called before doRolloverImage method has had a chance to
        // do any event processing. In that case the doRolloverImage method
        // is responsible for adding the event attributes.
        if (type != ROLLOVER_IMAGE_MENU_ITEM) {
            // Add the general event attributes.
            addGeneralEventAttributes (element, attributes);
        }
    }

    protected boolean doRolloverText (DOMOutputBuffer dom,
                                      MenuItem item,
                                      String resolvedHref)
            throws ProtocolException {

        String onColor = item.getOnColor ();
        String offColor = item.getOffColor ();
        if (onColor == null || offColor == null) {
            return false;
        }

        // Write out stylesheet info if device supports it.
        if (supportsCSS) {
            openStyle(dom, null);
            dom.appendEncoded(".roll { color:")
                    .appendEncoded(offColor)
                    .appendEncoded("; }\n")
                    .appendEncoded(".roll:hover { color:")
                    .appendEncoded(onColor)
                    .appendEncoded("; }\n");
            closeStyle(dom, null);
        }

        // Add the standard anchor stuff to the buffer.
        Element element = openMenuItemAnchor (dom, item, resolvedHref,
                                              ROLLOVER_TEXT_MENU_ITEM);

        if (supportsCSS) {
            element.setAttribute ("class", "roll");
        }

        dom.appendEncoded (item.getText ());

        closeMenuItemAnchor (dom, item, resolvedHref, ROLLOVER_TEXT_MENU_ITEM);

        return true;
    }

    /**
     * Write out a rollover image
     * @param dom The DOMOutputBuffer to use.
     * @param item the MenuItem
     * @param resolvedHref the anchor
     * @return true if item is written out
     */
    protected boolean doRolloverImage(DOMOutputBuffer dom, MenuItem item,
                                      String resolvedHref) throws ProtocolException {

        // No longer supported.
        throw new UnsupportedOperationException();
    }

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    /**
     * Does the protocol support JavaScript?
     *
     * @return Whether protocol supports JavaScript?
     */
    public boolean supportsJavaScript() {
        return protocolConfiguration.supportsJavaScript();
    }

    // Javadoc inherited from super class.
    protected void openNoScript (DOMOutputBuffer dom,
                                 NoScriptAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("noscript", attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeNoScript (DOMOutputBuffer dom,
                                  NoScriptAttributes attributes) {

        dom.closeElement ("noscript");
    }

    protected void addScriptAttributes (Element element,
                                        ScriptAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openScript (DOMOutputBuffer dom,
                               ScriptAttributes attributes) {

        String value;

        Element element = dom.openStyledElement ("script", attributes);

        if ((value = attributes.getCharSet()) != null) {
            element.setAttribute ("charset", value);
        }
        if ((value = attributes.getDefer()) != null) {
            element.setAttribute ("defer", value);
        }
        if ((value = attributes.getType()) != null) {
            element.setAttribute ("type", value);
        }

        // Allow subclasses to add extra attributes to script.
        addScriptAttributes (element, attributes);
        final ScriptAssetReference scriptReference = attributes.getScriptReference();
        if (scriptReference != null) {
            if (scriptReference.isURL()) {
                final String url = scriptReference.getURL();
                element.setAttribute("src", url);
            } else {
                // must be a literal script. sigh!
                // TODO: make an enum for script asset type!
                final String literalScript = scriptReference.getScript();
                dom.appendEncoded(literalScript);
            }
        }
    }

    // Javadoc inherited from super class.
    protected void closeScript (DOMOutputBuffer dom,
                                ScriptAttributes attributes) {
        dom.closeElement ("script");
    }

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    // ==========================================================================
    //   Custom markup methods
    // ==========================================================================

    //==========================================================================
    // Spatial Format Iterator methods
    //==========================================================================
    protected void openSpatialFormatIterator(
            DOMOutputBuffer dom,
            SpatialFormatIteratorAttributes attributes) {
        openSpatialFormatIteratorOriginal(dom, attributes);
    }

    protected void addXFFormAttributes (Element element,
                                        XFFormAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addXFFormAttributes (element, attributes);

        if (doValidation(attributes)) {
            String formName = attributes.getFormData().getName();
            element.setAttribute ("onSubmit", "return validateForm"
                                    +formName+"(this);");
        }

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form event attributes.
        addFormEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addActionAttributes (Element element,
                                        XFActionAttributes attributes) throws ProtocolException {

        super.addActionAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addBooleanAttributes (Element element,
                                         XFBooleanAttributes attributes) throws ProtocolException {

        super.addBooleanAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addSelectMenuAttributes (Element element,
                                            XFSelectAttributes attributes) throws ProtocolException {

        super.addSelectMenuAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void addTextAreaAttributes (Element element,
                                          XFTextInputAttributes attributes) throws ProtocolException {

        super.addTextAreaAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    /**
     * Add extra attributes to the text input tag.
     * @param element the Element to modify.
     * @param attributes <code>XFTextInputAttributes</code> for the text
     * input form field.
     */
    protected void addTextInputAttributes (Element element,
                                           XFTextInputAttributes attributes) throws ProtocolException {

        // Add the super classes attributes first.
        super.addTextInputAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        // Add the form field attributes.
        addFieldEventAttributes (element, attributes);
    }

    private boolean doValidation(XFFormAttributes attributes) {
        // if device does not support JavaScript return
        if(!protocolConfiguration.supportsJavaScript()) {
            return false;
        }

        // Create a script generator.
        TextInputFormatScriptGenerator generator =
                new TextInputFormatScriptGenerator(
                        getExtractorContext().getAssetResolver(), 
                        getTextInputFormatParser());

        StringBuffer sb = new StringBuffer();
        Object obj  = null;
        XFTextInputAttributes attribute = null;
        Iterator i = attributes.getFields().iterator();
        while(i.hasNext()) {
            obj = i.next();
            if(obj instanceof XFTextInputAttributes) {
                attribute = (XFTextInputAttributes)obj;
                generator.writeJavaScriptValidation(attribute, sb);
            }
        }
        if(sb.length() > 0) {
            DOMOutputBuffer dom = getHeadBuffer ();

            // Calculate the url to the validate script. This could be done
            // once for the whole application.
            String validateURL
                = "/"+context.getVolantisBean().getPageBase()+"/"+ 
                context.getVolantisBean().getScriptsBase()+"/validate.js";

            Element element = dom.addStyledElement ("script", attributes);
            element.setAttribute ("language", "JavaScript");
            element.setAttribute ("src", validateURL);

            dom = getScriptBuffer ();
            String suffix = attributes.getFormData().getName();
            dom.appendEncoded("function validateForm").appendEncoded(suffix)
                    .appendEncoded("(form) {" +
                            "var errMsg = \"\";")
                    .appendEncoded(sb.toString())
                    .appendEncoded("if(errMsg != \"\"){" +
                            "alert(\"Form Validation Error\\n\\n\" + errMsg);" +
                            "return false;" +
                            "}" +
                            "return true" +
                            "}");

            return true;
        }
        return false;
    }

    //  Javadoc inherited from super class
    public String defaultMimeType() {
        return "text/html";
    }

    // Javadoc inherited.
    public void mergeEventAttribute(MCSAttributes attributes, int eventIndex,
                                    String internalScript) throws RendererException {

        try {
            if (!protocolConfiguration.supportsEvents()) {
                return;
            }

            // If the attribute name is not specified we can't render this.
            String attributeName = eventAttributeNames [eventIndex];
            if (attributeName == null) {
                throw new IllegalStateException("attempt to render custom " +
                        "event attribute which is not supported for this " +
                        "protocol");
            }

            String script = null;

            EventAttributes eventAttributes
                = attributes.getEventAttributes (true);

            ScriptAssetReference userScript = eventAttributes.getEvent (eventIndex);

            if (userScript != null) {
                Script scriptObject = Script.createScript(userScript);
                if (logger.isDebugEnabled ()) {
                    logger.debug ("User script for " + attributeName
                                  + " is " + scriptObject);
                }
                if (scriptObject != null) {
                    script = scriptObject.stringValue();
                }
            }

            if (internalScript != null) {
                if (logger.isDebugEnabled ()) {
                    logger.debug ("Internal script for " + attributeName
                                  + " is " + internalScript);
                }
                if (script == null) {
                    script = internalScript;
                } else {
                    script = scriptHandler.joinScripts (this, script,
                                                        internalScript);

                    if (logger.isDebugEnabled ()) {
                        logger.debug ("Joined script " + attributeName
                                      + " is " + script);
                    }
                }
            }
            if (script != null) {
                eventAttributes.setEvent(eventIndex, script);
            }

        } catch (ProtocolException e) {
            throw new RendererException(e);
        }

    }

    //========================================================================
    // MenuModule related implementation.
    //========================================================================

    // Other javadoc inherited.
    /**
     * Creates a default menu module for this protocol.
     */
    // Other javadoc inherited.
    protected MenuModule createMenuModule(
            MenuModuleRendererFactoryFilter metaFactory) {

        MenuModuleRendererFactory rendererFactory =
                new XHTMLFullMenuModuleRendererFactory(getRendererContext(),
                        getDeprecatedOutputLocator(),
                        getMenuModuleCustomisation());

        if (metaFactory != null) {
            rendererFactory = metaFactory.decorate(rendererFactory);
        }

        return new DefaultMenuModule(getRendererContext(),
                rendererFactory);
    }

    //========================================================================
    // DeprecatedOutputLocator interface implementation.
    //========================================================================

    public DeprecatedEventAttributeUpdater getEventAttributeUpdater() {
        return this;
    }

    // javadoc inherited
    public Element createScriptElement(final ScriptAttributes attributes) {
        final Element scriptElement =
            getDOMFactory().createElement("script");

        final String charSet = attributes.getCharSet();
        if (charSet != null) {
            scriptElement.setAttribute("charset", charSet);
        }
        final String language = attributes.getLanguage();
        if (language != null) {
            scriptElement.setAttribute("language", language);
        }

        final ScriptAssetReference scriptReference =
            attributes.getScriptReference();
        if (scriptReference.isURL()) {
            scriptElement.setAttribute("src", scriptReference.getURL());
        } else {
            scriptElement.addText(scriptReference.getScript());
        }
        return scriptElement;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10730/1	emma	VBM:2005112516 Forward port: Vertical alignment style ignored on panes

 08-Dec-05	10716/1	emma	VBM:2005112516 Vertical alignment style ignored on panes

 29-Nov-05	10036/4	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 31-Oct-05	10036/1	geoff	VBM:2005102407 Pane width attributes rendered incorrectly

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 11-Nov-05	10293/1	emma	VBM:2005110906 Forward port: Bug fix: partial box border from theme being rendered as complete box

 11-Nov-05	10284/2	emma	VBM:2005110906 Bug fix: partial box border from theme being rendered as complete box

 01-Nov-05	9565/2	ibush	VBM:2005081219 Horizontal Rule Emulation

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 22-Sep-05	9540/4	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 21-Sep-05	9128/4	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 14-Sep-05	9472/4	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 23-Jun-05	8833/4	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/2	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/1	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 31-May-05	8591/2	philws	VBM:2005052311 Provide improved layout and theme based table attribute handling

 24-May-05	8491/1	tom	VBM:2005052311 Supermerge to MCS Mainline

 05-May-05	8072/1	philws	VBM:2005010510 Port pane rendering fixes from 3.3

 05-May-05	8038/1	philws	VBM:2005010510 Allow panes with style classes to use the enclosing table cell if possible

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 11-Mar-05	7308/2	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 22-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 05-Nov-04	6112/7	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 04-Nov-04	5871/8	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 04-Nov-04	5871/6	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - rework issues

 03-Nov-04	5871/4	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 09-Sep-04	4839/59	pcameron	VBM:2004062801 div tag is now used again in a pane's table if stylesheets are in use

 20-Jul-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 29-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 02-Jul-04	4803/3	adrianj	VBM:2003041504 Improved javadoc for adding column/row iterator pane attributes

 02-Jul-04	4803/1	adrianj	VBM:2003041504 Fixed optimization attribute propagation for column iterator panes

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 14-May-04	4318/5	pduffin	VBM:2004051207 Integrated separators into menu rendering

 14-May-04	4315/5	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 10-May-04	4257/1	geoff	VBM:2004051002 Enhance Menu Support: Integration Bugs: NPE in getPageConnection

 06-May-04	4174/3	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Apr-04	3920/4	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 23-Mar-04	3362/2	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 15-Mar-04	3403/3	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 05-Mar-04	3339/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3337/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3323/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 25-Feb-04	3179/3	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 24-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 16-Feb-04	2789/5	tony	VBM:2004012601 add localised logging and exception services

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Feb-04	2966/2	ianw	VBM:2004011923 Added mcsi:policy function

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 14-Nov-03	1861/1	mat	VBM:2003110602 Add correct mimetype to descendants of XHTMLBasic

 21-Aug-03	1240/1	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from mimas to proteus

 21-Aug-03	1219/5	chrisw	VBM:2003070811 Ported emulation of CSS2 border-spacing from metis to mimas

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
