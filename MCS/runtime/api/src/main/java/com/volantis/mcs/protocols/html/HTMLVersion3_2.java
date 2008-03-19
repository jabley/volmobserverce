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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLVersion3_2.java,v 1.24 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062805 - Added this change history and
 *                              added support for aggregation as originally
 *                              added to HTMLVersion3_2Ext1 under bug report
 *                              VBM:2001030602.
 * 09-Jul-01    Paul            VBM:2001062810 - Fixed the code so that it
 *                              only gets values out of attributes once and
 *                              uses append for each separate part of the
 *                              output string instead of inline string
 *                              concatenation. Also made use of the new
 *                              getStyle method in VolantisProtocol to
 *                              simplify the code.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by renaming all
 *                              the parameters to attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Modified to make it compatible
 *                              with some minor changes in some *Attributes
 *                              classes.
 * 06-Aug-01    Kula            openCanvas and closeCanvas Removed to use its
 *                              parent method
 * 20-Aug-01    Doug            VBM:2001081616 fixed problem with font tags.
 *                              Signature to VolantisProtocol.closeFontTag
 *                              changed to fix problem were a close font tag
 *                              could be output with no corresponding open.
 *                              Changed all calls to closeFontTag to reflect
 *                              new signature.
 * 23-Aug-01    Allan           VBM:2001082305 - Set supportsTitleOnInput and
 *                              supportsTitleOnOption to false in constructor.
 *                              Added an appendCoreAttributes that takes
 *                              a boolean "title" and will only include title
 *                              in the core attributes if this param is true.
 *                              Added doInput() since openInput() is in
 *                              here and duplicated in XHTMLFull.java apart
 *                              from the event attribute and the unused
 *                              selected attribute - too close to
 *                              2.2.0 release to do a more elegant fix.
 * 31-Aug-01    Allan           VBM:2001083102 - Modified openBody() to
 *                              use retrieveImageAssetURLAsString() instead of
 *                              retrieveAbsoluteURLAsString().
 * 03-Sep-01    Allan           VBM:2001083103 - Modified openBody() to
 *                              only output a background image if the url
 *                              is not null.
 * 04-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 05-Sep-01    Kula            oVBM:2001090401 - openTableRow(),
 *                              openTableDataCell(), and openTableHeaderCell()
 *                              methods are modified to write the emulated
 *                              style attributes.
 * 10-Sep-01    Kula            VBM:2001090608 - the closeSample method is
 *                              modified to output </samp> rather than
 *                              </sample>.
 * 10-Sep-01    Kula            oVBM:2001090401 - javadoc comments added to
 *                              TableRow, TableDataCell,and TableHeaderCell
 *                              open and close methods.nowrap attribute added
 *                              to openTableDataCell and openTableHeaderCell
 *                              methods.
 * 21-Sep-01    Doug            VBM:2001090302 - Use getLinkFromReference to get
 *                              links for those attributes whose value could
 *                              be a LinkComponentName.
 * 01-Oct-01    Doug            VBM:2001092501 - now use the MarinerPageContext
 *                              method getBackgroundImageURLAsString to
 *                              calculate the background-image url.
 * 04-Oct-01    Doug            VBM:2001100201 - Modified the methods
 *                              opentInput and do Input to check the
 *                              supportsAccessKeyAttribute flag before writing
 *                              out a accesskey attribute
 * 11-Oct-01    Allan           VBM:2001090401 - Replaced TableDataCell and
 *                              TableHeaderCell Attributes with
 *                              TableCellAttributes. Reverted back to
 *                              including stylistic attributes for these
 *                              tags and others removed earlier on the tag
 *                              overriding the theme.
 * 29-Oct-01    Paul            VBM:2001102901 - Removed unused import.
 * 19-Nov-01    Pether          VBM:2001103001 - In openTable()
 *                              get the align attribute from table-align
 *                              instead of text-align.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 22-Jan-02    Doug            VBM:2002011003 - Removed the methods openTitle
 *                              and closeTitle as doTitle is used instead.
 *                              Fixed a problem in doTitle were the font
 *                              tag was not being closed.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 01-Mar-02    Payal           VBM:2002020506 - Modified doMMFlash() to
 *                              output l,r,t,b instead of top, left, right,
 *                              bottom for align and similarly for salign if
 *                              align and salign are set in themes.
 * 08-Mar-02    Paul            VBM:2002030607 - Stopped calling toString on
 *                              StringOutputBuffers.
 * 08-Mar-02    Adrian          VBM:2002030705 - Added openSpan and closeSpan
 *                              to append a font tag with the style attributes
 *                              from the span, as span is not a valid tag in
 *                              this protocol.
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 18-Mar-02    Mat             VBM:2002031802 - Changed addBackground() to
 *                              check for a null background image.  This can
 *                              occur if the one specified in the theme does
 *                              not exist.
 * 28-Mar-02    Allan           VBM:2002022007 - Updated constructor for
 *                              change of quoteTable to an array.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 10-May-02    Adrian          VBM:2002040808 - call separate methods to get
 *                              ordered and unordered list styles.
 * 16-May-02    Adrian          VBM:2002040808 - updated open font to get the
 *                              first font name from font family.
 * 21-May-02    Byron           VBM:2002042602 - Modified doMMFlash() to check
 *                              to see if the device supports dynamic visuals
 *                              before generating html for it.
 * 22-May-02    Byron           VBM:2002042602 - Removed changes made on
 *                              21-May-02 and version log output in constructor
 * 23-May-02    Adrian          VBM:2002041503 - Removed method doMMFlash as
 *                              attributes from themes are now added in
 *                              MMFlashElement.
 * 05-Jun-02    Adrian          VBM:2002021103 - Open KEEPTOGETHER_ELEMENT in
 *                              method openTableBody , and close in method
 *                              closeTableBody
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 14-Jun-02    Byron           VBM:2002052707 - Removed ' mapping to [SPACE]
 *                              (HTML3.2 does not support &quot;). Added mapping
 *                              from " to &#34; so that quote encoding
 *                              is supported correctly in HTML 3.2
 * 11-Jun-02    Steve           VBM:2002040807 - Added mariner to protocol rule
 *                              mappings in the initialise method
 * 21-Jun-02    Adrian          VBM:2002041702 - updated open/closeFont methods
 *                              to output i, u, b, and strike tags where
 *                              appropriate values have been set in the theme.
 * 26-Jul-02    Steve           VBM:2002040807 - moved the protocol element
 *                              mapping initialisation into the class
 *                              constructor and out of initialise() so that
 *                              it only gets called once.
 * 20-Aug-02    Adrian          VBM:2002081316 - Updated doImage to call
 *                              super.doImage if image source is null.
 * 14-Nov-02    Adrian          VBM:2002111109 - updated open/closeFont to
 *                              output a "strong" tag where fontWeight is
 *                              "bolder"
 * 14-Nov-02    Adrian          VBM:2002111109 - Removed System.err.println
 *                              from change described above.
 * 19-Nov-02    Adrian          VBM:2002111109 - update open/closeFont to
 *                              output only bold tag if Style.hasBoldFontWeight
 *                              returns true.
 * 16-Dec-02    Adrian          VBM:2002100203 - Added more comprehensive
 *                              javadoc to constructor where elementMappings
 *                              field is populated.
 * 10-Jan-03    Phil W-S        VBM:2002110402 - Add in usage of the required
 *                              optimizingTransformer in the constructor.
 * 16-Jan-03    Sumit           VBM:2003011602 - addHorizontalRuleAttributes
 *                              adds HR attributes using HTML3.2 style class
 * 20-Jan-03    Geoff           VBM:2003011616 - set
 *                              supportsAccessKeyAttribute to false since 3.2
 *                              does not support the accesskey attribute,
 *                              usual IDEA cleanup.
 * 12-Mar-03    Sumit           VBM:2003022008 - added addLinkAttributes to
 *                              add alink, vlink and link attributes to body
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;


import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.protocols.BodyAttributes;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.ListItemAttributes;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.MenuAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.SpanAttributes;
import com.volantis.mcs.protocols.TableBodyAttributes;
import com.volantis.mcs.protocols.TableFooterAttributes;
import com.volantis.mcs.protocols.TableHeaderAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.html.css.emulator.styles.HTML3_2LetterSpacingRenderer;
import com.volantis.mcs.protocols.html.css.emulator.styles.HTML3_2LineHeightRenderer;
import com.volantis.mcs.protocols.html.css.emulator.styles.HTML3_2ListStyleTypeRenderer;
import com.volantis.mcs.protocols.html.css.emulator.styles.HTML3_2OrderedListStyleTypeRenderer;
import com.volantis.mcs.protocols.html.css.emulator.styles.HTML3_2UnorderedListStyleTypeRenderer;
import com.volantis.mcs.protocols.html.htmlversion3_2.HTML3_2UnabridgedTransformer;
import com.volantis.mcs.protocols.styles.PropertyRenderer;
import com.volantis.mcs.protocols.trans.CompoundTransformer;
import com.volantis.mcs.protocols.trans.TableCellAligningTransformer;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;

/**
 * This is a sub-class of the HTMLRoot protocol class to provide the precise
 * definition of the HTMLVersion3.2 protocol. Very little here is different
 * from the HTMLRoot class definition, so most things are referenced to the
 * superclass.
 */
public class HTMLVersion3_2
    extends HTMLRoot {

    /**
     * The main transformer for this protocol. This could be a compound
     * transformer (StyleInversionTransformer and UnabridgedDOMTransformer).
     */
    private DOMTransformer transformer;

    private PropertyRenderer lineHeightRenderer;

    private PropertyRenderer letterSpacingRenderer;

    private PropertyRenderer listStyleTypeRenderer;

    private PropertyRenderer orderedListStyleTypeRenderer;

    private PropertyRenderer unorderedlistStyleTypeRenderer;

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    public HTMLVersion3_2(ProtocolSupportFactory supportFactory,
                          ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        quoteTable['\"'] = "&#34;";

        // Define the default accesskey attribute support for this protocol
        // family
        supportsAccessKeyAttribute = false;

        supportsTitleOnInput = false;
        supportsTitleOnOption = false;

        supportsExternalStyleSheets = false;
        supportsInlineStyles = false;
    }

    public void initialise() {
        super.initialise();        

        // HTML v3.2 doesn't support CSS.
        supportsCSS = false;

        this.transformer = createDOMTransformer();
    }

    // Javadoc inherited.
    protected void createStyleEmulationRenderer() {
        super.createStyleEmulationRenderer();

        // Finally tell the renderer that these elements should not
        // have style emulation applied to them.
        styleEmulationRenderer.exclude("html");
        styleEmulationRenderer.exclude("head");
        styleEmulationRenderer.exclude("title");
    }

    private DOMTransformer createDOMTransformer() {

        DOMTransformer transformer = null;

        // This protocol requires different optimization rules to be applied
        // than those used by the base class
        optimizingTransformer =
                new HTML3_2UnabridgedTransformer(protocolConfiguration);


        if (supportsFormatOptimization) {
            transformer = optimizingTransformer;
        }

        // If the device does not support align attribute of td, but does
        // support an alternative, then add a transformer to translate.
        if (!supportsTableCellAlign && supportsCenterElement) {
            TableCellAligningTransformer tableCellAlignTransformer =
                    new TableCellAligningTransformer();
            if (transformer != null) {
                transformer = new CompoundTransformer(transformer,
                        tableCellAlignTransformer);
            } else {
                transformer = tableCellAlignTransformer;
            }
        }
        return transformer;
    }

    // Javadoc inherited.
    protected void initialiseStyleHandlers() {
        super.initialiseStyleHandlers();

        lineHeightRenderer = new HTML3_2LineHeightRenderer();

        letterSpacingRenderer = new HTML3_2LetterSpacingRenderer();

        listStyleTypeRenderer = new HTML3_2ListStyleTypeRenderer();
        unorderedlistStyleTypeRenderer = new HTML3_2UnorderedListStyleTypeRenderer();
        orderedListStyleTypeRenderer = new HTML3_2OrderedListStyleTypeRenderer();
    }

    // javadoc inherited.
    protected DOMTransformer getDOMTransformer() {
        return transformer;
    }

    // ========================================================================
    //   General helper methods
    // ========================================================================

    /**
     * Override this to prevent class and id attributes being written out as
     * HTML 3.2 does not support them.
     */
    protected void addCoreAttributes(Element element,
                                     MCSAttributes attributes) {

        addCoreAttributes(element, attributes, true);
    }

    /**
     * Override this to prevent class and id attributes being written out as
     * HTML 3.2 does not support them.
     */
    protected void addCoreAttributes(Element element,
                                     MCSAttributes attributes,
                                     boolean title) {

        String value;

        if (title && (value = attributes.getTitle()) != null) {
            element.setAttribute("title", value);
        }
    }

    // ========================================================================
    //   Page element methods
    // ========================================================================

    public String defaultMimeType() {
        return "text/html";
    }

    // Javadoc inherited.
    protected void addBodyAttributes(Element element,
                                     BodyAttributes attributes)
        throws ProtocolException {

        // Add the super class attributes.
        super.addBodyAttributes(element, attributes);

        Styles styles = attributes.getStyles();

        // Check for the theme attributes
        addBackgroundAttribute(element, styles);
        addLinkAttributes(element, attributes);
    }

    /**
     * @param element
     */
    private void addLinkAttributes(Element element, MCSAttributes attributes) {


        Styles styles = attributes.getStyles();

        addStatefulColorAttribute(
                element, styles, StatefulPseudoClasses.ACTIVE, "alink");
        addStatefulColorAttribute(
                element, styles, StatefulPseudoClasses.LINK, "link");
        addStatefulColorAttribute(
                element, styles, StatefulPseudoClasses.VISITED, "vlink");
    }

    /**
     * Add color attribute from styles associated with the specified stateful
     * pseudo class.
     *
     * @param element The element.
     * @param styles The parent styles.
     * @param statefulPseudoClass The stateful pseudo class.
     * @param attributeName The attribute name to set.
     */
    private void addStatefulColorAttribute(
            Element element, Styles styles,
            final StatefulPseudoClass statefulPseudoClass,
            final String attributeName) {
        Styles specialStyles;
        String value;
        specialStyles = styles.findNestedStyles(statefulPseudoClass);
        if (specialStyles != null &&
                (value = colorHandler.getAsString(specialStyles)) != null) {
            element.setAttribute(attributeName, value);
        }
    }

    // Javadoc inherited.
    protected void openInclusion(DOMOutputBuffer dom,
            CanvasAttributes attributes) {

        Styles styles = attributes.getStyles();
        if (backgroundColorHandler.isSignificant(styles)) {
            // If there is a background colour, use <table><tr><td> to open
            // the inclusion so that a bgcolor attribute can be set
            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            dom.openStyledElement ("table", attributes,
                    DisplayKeywords.TABLE);
            dom.openElement("tr");
            dom.openElement("td");
        } else {
            // Otherwise do what the super class is doing
            super.openInclusion(dom, attributes);
        }

    }

    // Javadoc inherited.
    protected void closeInclusion(DOMOutputBuffer dom,
            CanvasAttributes attributes) {

        Styles styles = attributes.getStyles();
        if (backgroundColorHandler.isSignificant(styles)) {
            // If there is a background colour, use </td></tr></table> to
            // close the inclusion that was opened because of a bgcolor
            // attribute
            dom.closeElement("td");
            dom.closeElement("tr");
            dom.closeElement("table");
        } else {
            // Otherwise do what the super class is doing
            super.closeInclusion(dom, attributes);
        }
    }

    // Javadoc inherited from super class.
    protected void doProtocolString(Document document) {
        DocType docType = domFactory.createDocType(
                "HTML", "-//W3C//DTD HTML 3.2 Final//EN", null, null,
                MarkupFamily.SGML);
        document.setDocType(docType);
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    // javadoc inherited
    protected void openDissectingPane(DOMOutputBuffer dom,
                                      DissectingPaneAttributes attributes) {

        super.openDissectingPane(dom, attributes);

        // Disection does not appear to be supported in this protocol
        // so add a style marker
        openStyleMarker(dom, attributes);
    }

    // javadoc inherited
    protected void closeDissectingPane(DOMOutputBuffer dom,
                                       DissectingPaneAttributes attributes) {

        closeStyleMarker(dom);

        super.closeDissectingPane(dom, attributes);
    }

    // Javadoc inherited.
    protected void openForm(DOMOutputBuffer dom,
                            FormAttributes attributes) {

        super.openForm(dom, attributes);

        // Super method does not create an element
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited.
    protected void closeForm(DOMOutputBuffer dom,
                             FormAttributes attributes) {

        closeStyleMarker(dom);

        super.closeForm(dom, attributes);
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================


    // ========================================================================
    //   Block element methods.
    // ========================================================================


    // ========================================================================
    //   List element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addListItemAttributes (Element element,
                                          ListItemAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addListItemAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;

        if ((value = listStyleTypeRenderer.getAsString(styles)) != null) {
            element.setAttribute ("type", value);
        }
    }

    // Javadoc inherited.
    protected void addOrderedListAttributes(Element element,
                                            OrderedListAttributes attributes)
        throws ProtocolException {

        Styles styles = attributes.getStyles();
        String value;

        // Initialise some attributes from the style if they do not already
        // have values.
        if (attributes.getType() == null
                && (value = orderedListStyleTypeRenderer
                .getAsString(styles)) != null) {

            attributes.setType(value);
        }

        // Add the super class's attributes.
        super.addOrderedListAttributes(element, attributes);
    }


    // Javadoc inherited from super class.
    protected
    void addUnorderedListAttributes(Element element,
                                    UnorderedListAttributes attributes)
        throws ProtocolException {

        Styles styles = attributes.getStyles();
        String value;

        // Initialise some attributes from the style if they do not already
        // have values.
        if (attributes.getType() == null
                && (value = unorderedlistStyleTypeRenderer
                .getAsString(styles)) != null) {

            attributes.setType(value);
        }

        // Add the super class's attributes.
        super.addUnorderedListAttributes(element, attributes);
    }


    // ========================================================================
    //   Table element methods.
    // ========================================================================


    // Javadoc inherited.
    protected void openTableBody(DOMOutputBuffer dom,
                                 TableBodyAttributes attributes) {

        if ("true".equals(attributes.getKeepTogether())) {
            dom.openElement(KEEPTOGETHER_ELEMENT);
        }

        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited.
    protected void closeTableBody(DOMOutputBuffer dom,
                                  TableBodyAttributes attributes) {

        closeStyleMarker(dom);

        if ("true".equals(attributes.getKeepTogether())) {
            dom.closeElement(KEEPTOGETHER_ELEMENT);
        }
    }

    protected void openTableFooter(DOMOutputBuffer dom,
                                   TableFooterAttributes attributes) {

        openStyleMarker(dom, attributes);

    }

    protected void closeTableFooter(DOMOutputBuffer dom,
                                    TableFooterAttributes attributes) {

        closeStyleMarker(dom);

    }

    protected void openTableHeader(DOMOutputBuffer dom,
                                   TableHeaderAttributes attributes) {

        openStyleMarker(dom, attributes);
    }

    protected void closeTableHeader(DOMOutputBuffer dom,
                                    TableHeaderAttributes attributes) {

        closeStyleMarker(dom);

    }

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    // Javadoc inherited.
    public void openSpan(DOMOutputBuffer dom,
                         SpanAttributes attributes) {
        openStyleMarker(dom, attributes);
    }

    // Javadoc inherited.
    public void closeSpan(DOMOutputBuffer dom,
                          SpanAttributes attributes) {
        closeStyleMarker(dom);
    }

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    /**
     * Override this method to add extra attributes to the image.
     */
    protected void addImageAttributes(Element element,
                                      ImageAttributes attributes)
        throws ProtocolException {

        Styles styles = attributes.getStyles();
        String value;

        if (null != styles) {
            // Initialise some attributes from the style if they do not already
            // have values.
            if (attributes.getHSpace() == null
                    && (value = lineHeightRenderer.getAsString(styles)) != null) {
                attributes.setHSpace(value);
            }
    
            if (attributes.getVSpace() == null
                    && (value = letterSpacingRenderer.getAsString(styles)) != null) {
                attributes.setVSpace(value);
            }
        }
        // Add the super class's attributes.
        super.addImageAttributes(element, attributes);

    }

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    // Javadoc inherited.
    protected void openMenu(DOMOutputBuffer dom, MenuAttributes attributes) {

        openStyleMarker(dom, attributes);

    }

    // Javadoc inherited.
    protected void closeMenu(DOMOutputBuffer dom, MenuAttributes attributes) {

        closeStyleMarker(dom);

    }

    // ========================================================================
    //   Script element methods.
    // ========================================================================


    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/1	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/2	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes, also removed getColor() from Style

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/8	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9348/5	gkoch	VBM:2005081805 TableCellAttributes.noWrap property is stored in styles + inlined getters

 22-Aug-05	9184/9	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 22-Aug-05	9184/7	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 23-Jun-05	8833/4	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 24-May-05	8462/1	philws	VBM:2005052310 Port device access key override from 3.3

 24-May-05	8430/1	philws	VBM:2005052310 Allow the device to override a protocol's access key support

 19-May-05	8335/1	philws	VBM:2005051705 Port Palm WCA style emulation from 3.3

 19-May-05	8305/1	philws	VBM:2005051705 Provide style emulation rendering for HTML Palm WCA version 1.1

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6076/6	tom	VBM:2004101509 Modified protocols to get their styles from MCSAttributes

 22-Nov-04	6183/4	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 02-Nov-04	6068/4	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 01-Nov-04	6064/1	claire	VBM:2004102801 mergevbm: Handling background colour for HTML 3.2 portlets

 01-Nov-04	6014/3	claire	VBM:2004102801 Handling background colour for HTML 3.2 portlets

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 13-Aug-04	5220/1	pcameron	VBM:2004081303 Fixed HTML3.2 inclusions with a hack

 13-Aug-04	5204/2	pcameron	VBM:2004081303 Fixed HTML3.2 inclusions with a hack

 20-Jul-04	4897/2	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/4	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 14-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/6	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/4	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 27-May-04	4589/22	steve	VBM:2004051102 Output text colour in body tag

 27-May-04	4589/20	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4589/2	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4570/2	steve	VBM:2004051102 Output text colour in body tag

 06-May-04	4174/1	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/1	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 26-May-04	4570/4	steve	VBM:2004051102 Output text colour in body tag

 26-May-04	4570/2	steve	VBM:2004051102 Output text colour in body tag

 25-Feb-04	3179/3	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 25-Feb-04	3179/3	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 24-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 12-Feb-04	2958/1	philws	VBM:2004012715 Add protocol.content.type device policy

 09-Dec-03	2180/1	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2162/2	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2176/4	philws	VBM:2003120505 Handle dissecting pane font colour style

 09-Dec-03	2176/2	philws	VBM:2003120505 Allow HTML 3.2 to render pane font colour style

 09-Dec-03	2174/3	philws	VBM:2003120505 Handle dissecting pane font colour style

 09-Dec-03	2174/1	philws	VBM:2003120505 Allow HTML 3.2 to render pane font colour style

 24-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 09-Dec-03	2162/2	mat	VBM:2003120504 Add style attributes to format cells

 09-Dec-03	2174/3	philws	VBM:2003120505 Handle dissecting pane font colour style

 09-Dec-03	2174/1	philws	VBM:2003120505 Allow HTML 3.2 to render pane font colour style

 17-Aug-03	1052/2	allan	VBM:2003073101 Support styles on menu and menuitems

 ===========================================================================
*/
