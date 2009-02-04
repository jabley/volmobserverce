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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/XHTMLTransitional.java,v 1.10 2003/04/17 10:21:07 geoff Exp $
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
 *                              inline string concatenation.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by not creating
 *                              StringBuffers when returning a fixed string
 *                              and also by renaming all the *Attributes
 *                              parameters to attributes.
 * 26-Jul-01    Paul            VBM:2001071707 - Modified to make it compatible
 *                              with some minor changes in some *Attributes
 *                              classes.
 * 30-Oct-01    Paul            VBM:2001102902 - Renamed openUnderline to
 *                              openUnderLine.
 * 30-Oct-01    Paul            VBM:2001102901 - Removed unused import.
 * 22-Jan-02    Doug            VBM:2002011003 - Numerous changes to ensure
 *                              that valid XHTML (transitional) is generated.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 07-Feb-02    Adrian          VBM:2001101002 - override addPaneCellAttributes
 *                              to output width attribute for <td> tag.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 06-Mar-02    Adrian          VBM:2002020736 - removed addPaneCellAttributes.
 *                              Width attribute now appended in XHTMLFull.
 *                              Also moved width from addTableCellAttributes to
 *                              XHTMLFull.addTableCellAttributes.
 * 06-Mar-02    Adrian          VBM:2002021101 - Moved methods...
 *                              doRolloverImage addRolloverImageAttributes to
 *                              XHTMLFull
 * 11-Mar-02    Paul            VBM:2001122105 - Added general event attributes
 *                              to blockquote and underline.
 * 11-Mar-02    Doug            VBM:2002011003 - Modified the doProtocolString
 *                              method so that it references the
 *                              xhtml1-frameset.dtd if a montage page is being
 *                              processed and the xhtml1-transitional.dtd
 *                              otherwise. Moved all the montage methods from
 *                              XHTMLFull to this protocol. Moved all code
 *                              that writes out a width attributes on td
 *                              elements from XHTMLBasic to this protocol.
 * 13-Mar-02    Allan           VBM:2002031302 - Removed addPaneCellAttributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 26-Apr-02    Paul            VBM:2002042205 - Updated pane rendering code
 *                              to match changes made in XHTMLFull.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 20-May-02    Paul            VBM:2001122105 - Moved addBlockQuoteAttributes
 *                              method to XHTMLFull as it belongs there.
 * 23-May-02    Steve           VBM:2002040809 - Check pane for a styleClass
 *                              attribute. If the pane has a style class and
 *                              the style class has a background colour defined
 *                              then this value overrides the theme if the
 *                              device does not support style sheets.
 * 09-Aug-02    Adrian          VBM:2002080901 - modified method
 *                              addPaneTableOrCellAttributes to call getStyle
 *                              with "pane" rather than element.getName() as
 *                              the element is protocol specific and will never
 *                              be "pane" so will not match any rules which
 *                              specify the element selector "pane".
 * 14-Aug-02    Allan           VBM:2002081302 - Modified method
 *                              addPaneTableOrCellAttributes to check that
 *                              the result returned from getStyle is not null
 *                              before trying to use it.
 * 11-Sep-02    Steve           VBM:2002040809 - Call getPaneStyle() method
 *                              to get table attributes from theme or layout
 *                              for panes and derived panes
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Add in usage of the required
 *                              optimizingTransformer in the constructor.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.DocType;
import com.volantis.mcs.dom.MarkupFamily;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DivAttributes;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.MontageAttributes;
import com.volantis.mcs.protocols.OrderedListAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.SegmentAttributes;
import com.volantis.mcs.protocols.SegmentGridAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.protocols.TableRowAttributes;
import com.volantis.mcs.protocols.UnderlineAttributes;
import com.volantis.mcs.protocols.UnorderedListAttributes;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.html.xhtmltransitional.XHTMLTransitionalUnabridgedTransformer;
import com.volantis.mcs.protocols.styles.KeywordValueHandler;
import com.volantis.mcs.protocols.styles.PositivePixelLengthHandler;
import com.volantis.mcs.protocols.styles.PropertyHandler;
import com.volantis.mcs.protocols.styles.ValueHandlerToPropertyAdapter;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.mappers.DefaultKeywordMapper;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;

/**
 *
 * This is a sub-class of the XHTMLFull protocol class to provide the precise
 * definition of the XHTML Transitional protocol.
 *
 * XHTML supports style sheets, so no themes processing is performed here.
 */
public class XHTMLTransitional
    extends XHTMLFull {

    private PropertyHandler horizontalRuleAlignHandler;
    private PropertyHandler horizontalRuleShadeHandler;
    private PropertyHandler horizontalRuleSizeHandler;
    private PropertyHandler noWrapHandler;

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     * objects.
     * @param configuration The protocol specific configuration.
     */
    public XHTMLTransitional(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        // This protocol requires different optimization rules to be applied
        // than those used by the base class
        optimizingTransformer = new XHTMLTransitionalUnabridgedTransformer(
                protocolConfiguration);
    }

    // Javadoc inherited.
    protected void initialiseStyleHandlers() {
        super.initialiseStyleHandlers();

        // center and justify are both treated as center which is the default.
        DefaultKeywordMapper alignmentMapper = new DefaultKeywordMapper();
        alignmentMapper.addMapping(TextAlignKeywords.LEFT, "left");
        alignmentMapper.addMapping(TextAlignKeywords.RIGHT, "right");

        horizontalRuleAlignHandler = new ValueHandlerToPropertyAdapter(StylePropertyDetails.TEXT_ALIGN,
                new KeywordValueHandler(alignmentMapper));

        DefaultKeywordMapper shadeMapper = new DefaultKeywordMapper();
        shadeMapper.addMapping(BorderStyleKeywords.SOLID, "true");

        horizontalRuleShadeHandler = new ValueHandlerToPropertyAdapter(StylePropertyDetails.BORDER_TOP_STYLE,
                new KeywordValueHandler(shadeMapper));

        horizontalRuleSizeHandler = new ValueHandlerToPropertyAdapter(StylePropertyDetails.HEIGHT,
                new PositivePixelLengthHandler());

        // noWrap property
        // noWrapMapper maps "noWrap" to the NOWRAP value and will null to other
        // values
        final DefaultKeywordMapper noWrapMapper = new DefaultKeywordMapper();
        noWrapMapper.addMapping(WhiteSpaceKeywords.NOWRAP, "noWrap");

        noWrapHandler = new ValueHandlerToPropertyAdapter(StylePropertyDetails.WHITE_SPACE,
                new KeywordValueHandler(noWrapMapper));
    }

    // Javadoc inherited from super class.
    protected void doProtocolString (Document document) {
        String publicId;
        String systemId;
        // if the page being generated is a montage then we must write out
        // the XHTML-frameset protocol string.
        if (isPageType(MONTAGE_PAGE)) {
            publicId = "-//W3C//DTD XHTML 1.0 Frameset//EN";
            systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd";
        } else {
            publicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
            systemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        }
        DocType docType = domFactory.createDocType(
                "html", publicId, systemId,
                null, MarkupFamily.XML);
        document.setDocType(docType);
    }

    // ========================================================================
    //   Page element methods
    // ========================================================================

    // Javadoc inherited from super class.
    protected void openMontage (DOMOutputBuffer dom,
                                MontageAttributes attributes) {
        dom.openElement ("html");
    }

    // Javadoc inherited from super class.
    protected void closeMontage (DOMOutputBuffer dom,
                                 MontageAttributes attributes) {
        dom.closeElement ("html");
    }

    // Javadoc inherited from super class.
    protected void openSegment (DOMOutputBuffer dom,
                                SegmentAttributes attributes) {

        String value;
        //boolean bvalue;
        int ivalue;

        Element element = dom.openStyledElement ("frame", attributes);
        addCoreAttributes (element, attributes);

        element.setAttribute ("frameborder",
                              attributes.isFrameBorder () ? "1" : "0");
        element.setAttribute ("marginheight",
                              String.valueOf (attributes.getMarginHeight ()));
        element.setAttribute ("marginwidth",
                              String.valueOf (attributes.getMarginWidth ()));

        if ((value = attributes.getLongDesc ()) != null) {
            element.setAttribute ("longdesc", value);
        }

        if ((value = attributes.getBorderColor()) != null) {
            element.setAttribute ("bordercolor", value);
        }

        if ((value = attributes.getName()) != null) {
            element.setAttribute ("name", value);
        }

        if (!attributes.isResize()) {
            element.setAttribute ("noresize", "noresize");
        }

        ivalue = attributes.getScrolling();
        if (ivalue != SegmentAttributes.SCROLLING_AUTOMATIC) {
            if (ivalue == SegmentAttributes.SCROLLING_NO) {
                element.setAttribute ("scrolling", "no");
            }
            else if (ivalue == SegmentAttributes.SCROLLING_YES) {
                element.setAttribute ("scrolling", "yes");
            }
        }

        // src could be a link component
        LinkAssetReference reference = attributes.getSrc();
        if (reference != null) {
            element.setAttribute("src", reference.getURL());
        }
    }

    // Javadoc inherited from super class.
    protected void closeSegment (DOMOutputBuffer dom,
                                 SegmentAttributes attributes) {
        dom.closeElement ("frame");
    }


    protected
        void addSegmentGridAttributes (Element element,
                                       SegmentGridAttributes attributes) {
    }


    /**
     * Helper method to set the "rows" or "cols" attribute correctly
     * @param element The element on which the attribute will be set
     * @param attributes The collection of attributes
     */
    private void addRowColAttributes(Element element,
                                    SegmentGridAttributes attributes) {

        String[] units;
        int[] values;
        String suffix = null;

        // short loop to perform operation on "rows" (0) then on "cols" (1)
        for (int attribIndex = 0; attribIndex < 2; attribIndex++) {

            if (attribIndex==0) {
                values = attributes.getRowHeights();
                units = attributes.getRowHeightUnits();
            } else {
                values = attributes.getColumnWidths();
                units = attributes.getColumnWidthUnits();
            }

            StringBuffer buffer = null;

            if (values != null && values.length != 0) {
                buffer = new StringBuffer();

                String separator = "";
                for (int i = 0; i < values.length; i++) {
                    // only build string if attribute is valid (-1 indicates
                    // its not there)
                    if (values[i] >= 0) {
                        if ((units != null) &&
                                (units[i].equals((attribIndex==0) ?
                                SegmentGridAttributes.HEIGHT_UNITS_VALUE_PERCENT : //rows
                                SegmentGridAttributes.WIDTH_UNITS_VALUE_PERCENT))) { //cols
                            suffix = "%";
                        } else {
                            suffix = "";
                        }
                        buffer.append(separator).append(values[i]).append(suffix);
                    } else {
                        buffer.append(separator).append("*");
                    }
                    separator = ",";
                }

                //don't write attribute if no value is set
                if (buffer.length() != 0) {
                    element.setAttribute((attribIndex==0)?"rows":"cols",
                            buffer.toString());
                }
            }
        }
    }

    /**
     * Allow subclasses to add extra attributes to a segment grid.
     * @param  dom The DOMOutputBuffer to add the markup to.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSegmentGrid(DOMOutputBuffer dom,
                                   SegmentGridAttributes attributes) {

        String value;

        Element element = dom.openStyledElement("frameset", attributes);
        addCoreAttributes(element, attributes);

        // process the row and column attributes
        addRowColAttributes(element, attributes);

        if ((value = attributes.getBorderColor()) != null) {
            element.setAttribute("bordercolor", value);
        }

        element.setAttribute("border",
                String.valueOf(attributes.getBorderWidth()));

        element.setAttribute("frameborder",
                attributes.isFrameBorder() ? "yes" : "no");
        element.setAttribute("framespacing",
                String.valueOf(attributes.getFrameSpacing()));

        addSegmentGridAttributes(element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeSegmentGrid (DOMOutputBuffer dom,
                                     SegmentGridAttributes attributes) {
        dom.closeElement ("frameset");
    }

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    // ========================================================================
    //   Layout / format methods
    // ========================================================================


    // Javadoc inherited from super class.
    protected void addColumnIteratorPaneAttributes (Element element,
                                    ColumnIteratorPaneAttributes attributes) {

        // add the super classes attributes first
        super.addColumnIteratorPaneAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value = backgroundColorHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("bgcolor", value);
        }
    }

    // Javadoc inherited from super class.
    protected void addColumnIteratorPaneElementAttributes (Element element,
                                                           ColumnIteratorPaneAttributes attributes) {

        // add super class attributes first
        super.addColumnIteratorPaneElementAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;
        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute ("width", value);
        }
    }

    // Javadoc inherited from super class.
    protected
        void addGridAttributes (Element element,
                                GridAttributes attributes) {

        // add the super classes attributes first
        super.addGridAttributes (element, attributes);

        Styles styles = attributes.getStyles();

        String value = backgroundColorHandler.getAsString(styles);
        if( value != null ) {
            element.setAttribute ("bgcolor", value);
        }
    }

    // Javadoc inherited from superclass
    protected void addGridChildAttributes (Element element,
                                           GridChildAttributes attributes) {

        super.addGridChildAttributes (element, attributes);

        Styles styles = attributes.getStyles();

        // Only add the width attribute to the cells in the first row.
        String value;
        int row = attributes.getRow ();
        if (row == 0) {
            if ((value = widthHandler.getAsString(styles)) != null) {
                element.setAttribute("width", value);
            }
        }
    }

    /**
     * Augments the superclass to check for background colour.
     */
    protected PaneRendering checkPaneCellAttributes(
            Element element,
            PaneAttributes attributes) {

        // Call the super class method first.
        PaneRendering rendering = super.checkPaneCellAttributes(element,
                                                                attributes);

        if (rendering == PaneRendering.USE_TABLE) {
            return rendering;
        }

        Styles styles = attributes.getStyles();
        if (backgroundColorHandler.isSignificant(styles)) {

            if ((element == null) ||
                    (element.getAttributeValue("bgcolor") != null)) {
                return PaneRendering.USE_TABLE;
            }

            // We will at least need to use a cell.
            rendering = PaneRendering.USE_ENCLOSING_TABLE_CELL;
        }

        return rendering;
    }

    protected void addPaneTableOrCellAttributes (Element element,
                                                 PaneAttributes attributes,
                                                 boolean table) {

        // Add the super classes attributes first
        super.addPaneTableOrCellAttributes (element, attributes, table);

        Styles styles = attributes.getStyles();
        String value = backgroundColorHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("bgcolor", value);
        }
    }

    // Javadoc inherited from super class.
    protected void addRowIteratorPaneAttributes (Element element,
                                                 RowIteratorPaneAttributes attributes) {

        // add the super classes attributes first
        super.addRowIteratorPaneAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value = backgroundColorHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("bgcolor", value);
        }
    }

    // ========================================================================
    //   Navigation methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addAnchorAttributes (Element element,
                                        AnchorAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addAnchorAttributes (element, attributes);

        String value;

        boolean supportsAggregation
            = context.getBooleanDevicePolicyValue ("aggregation");

        if (supportsAggregation
            && (value = attributes.getSegment()) != null) {
            element.setAttribute ("target", value);
        }
    }

    // ========================================================================
    //   Block element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addDivAttributes (Element element,
                                     DivAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addDivAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;
        if ((value = horizontalAlignHandler.getAsString(styles)) != null) {
            element.setAttribute ("align", value);
        }
    }

    protected void addHorizontalRuleAttributes (Element element,
              HorizontalRuleAttributes attributes) throws ProtocolException {
        // Add the super classes attributes first.
        super.addHorizontalRuleAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;

        if ((value = horizontalRuleAlignHandler.getAsString(styles)) != null) {
            element.setAttribute ("align", value);
        }
        if ((value = horizontalRuleShadeHandler.getAsString(styles)) != null) {
            element.setAttribute ("noshade", value);
        }
        if ((value = horizontalRuleSizeHandler.getAsString(styles)) != null) {
            element.setAttribute ("size", value);
        }
        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute ("width", value);
        }
    }

    // ========================================================================
    //   List element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected
        void addOrderedListAttributes (Element element,
                                       OrderedListAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addOrderedListAttributes (element, attributes);

        String value;
        if ((value = attributes.getType ()) != null) {
            element.setAttribute ("type", value);
        }
        if ((value = attributes.getStart ()) != null) {
            element.setAttribute ("start", value);
        }
    }

    // Javadoc inherited from super class.
    protected
        void addUnorderedListAttributes (Element element,
                                         UnorderedListAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addUnorderedListAttributes (element, attributes);

        String value;
        if ((value = attributes.getType ()) != null) {
            element.setAttribute ("type", value);
        }
    }

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    /**
     * Override this method to add extra attributes to the unordered list.
     */
    protected void addTableAttributes (Element element,
                                       TableAttributes attributes)
            throws ProtocolException {

        // add super classes attributes first
        super.addTableAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;
        if ((value = horizontalAlignHandler.getAsString(styles)) != null) {
            element.setAttribute("align", value);
        }
    }

    /**
     * Override this method to add extra attributes to the table row.
     */
    protected void addTableRowAttributes (Element element,
                                          TableRowAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addTableRowAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;

        value = backgroundColorHandler.getAsString(styles);
        if( value != null) {
            element.setAttribute ("bgcolor", value);
        }
    }

    /**
     * Add deprecated stylistic attrubutes that remain on the tag of
     * table cell type tags.
     * @param element the Element
     * @param attributes <code>TableCellAttributes</code> to add to the tag
     */
    protected void addTableCellAttributes (Element element,
                                          TableCellAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addTableCellAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;

        if ((value = widthHandler.getAsString(styles)) != null) {
            element.setAttribute("width", value);
        }
        if ((value = backgroundColorHandler.getAsString(styles)) != null) {
            element.setAttribute("bgcolor", value);
        }
        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute("height", value);
        }
        if ((value = noWrapHandler.getAsString(styles)) != null) {
            element.setAttribute("nowrap", value);
        }
    }

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    /**
     * Allow subclasses to add extra attributes.
     * @param element The Element to modify.
     * @param attributes The attributes to use when generating the markup.
     */
    private void addUnderlineAttributes (Element element,
                                           UnderlineAttributes attributes) {
    }

    // Javadoc inherited from super class.
    protected void openUnderline (DOMOutputBuffer dom,
                                  UnderlineAttributes attributes)
            throws ProtocolException {

        Element element = dom.openStyledElement ("u", attributes);

        addCoreAttributes (element, attributes);

        // Add the general event attributes.
        addGeneralEventAttributes (element, attributes);

        addUnderlineAttributes (element, attributes);
    }

    // Javadoc inherited from super class.
    protected void closeUnderline (DOMOutputBuffer dom,
                                   UnderlineAttributes attributes) {
        dom.closeElement ("u");
    }

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addImageAttributes (Element element,
                                       ImageAttributes attributes)
            throws ProtocolException {

        // Add the super classes attributes first.
        super.addImageAttributes (element, attributes);

        String value;

        if ((value = attributes.getAlign ()) != null) {
            element.setAttribute ("align", value);
        }
        if ((value = attributes.getBorder ()) != null) {
            element.setAttribute ("border", value);
        } else {
            element.setAttribute ("border", "0");
        }
        if ((value = attributes.getHSpace ()) != null) {
            element.setAttribute ("hspace", value);
        }
        if ((value = attributes.getVSpace ()) != null) {
            element.setAttribute ("vspace", value);
        }
    }

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addScriptAttributes (Element element,
                                        ScriptAttributes attributes) {

        // Add the super classes attributes first.
        super.addScriptAttributes (element, attributes);

        String value;

        if ((value = attributes.getLanguage()) != null) {
            element.setAttribute ("language", value);
        }
    }

    // ========================================================================
    //   Extended function form element methods.
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addXFFormAttributes (Element element,
            XFFormAttributes attributes) throws ProtocolException {
        // allow super class to added extra attributers first
        super.addXFFormAttributes (element, attributes);

        // XHTMLTransitional allows a form to be named
        String value;
        if ((value = attributes.getName()) != null) {
            element.setAttribute ("name", value);
        }

        // Add a target for the anchor if aggregation is supported and a
        // segment is set.
        boolean supportsAggregation
            = context.getBooleanDevicePolicyValue ("aggregation");

        if (supportsAggregation
            && (value = attributes.getSegment()) != null){
            element.setAttribute ("target", value);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Sep-05	9540/1	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9348/3	gkoch	VBM:2005081805 TableCellAttributes.noWrap property is stored in styles + inlined getters

 19-Aug-05	9289/4	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 24-May-05	8491/1	tom	VBM:2005052311 Supermerge to MCS Mainline

 24-May-05	8489/1	tom	VBM:2005052311 Added width to tables and font colour to <a>

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 04-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 03-Nov-04	5871/1	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 17-May-04	4029/4	steve	VBM:2004042003 Supermerged again - Hurry up Mat :)

 26-Apr-04	4029/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 23-Apr-04	4020/1	steve	VBM:2004042003 support hr element in netfront3 and MIB

 05-May-04	4157/5	matthew	VBM:2003030319 added test implementation to HDML_Version3TestCase.testOpenSegmentGrid, changed signature of XHTMLTransitional.addRowColAttributes

 05-May-04	4157/3	matthew	VBM:2003030319 change the way default values of rowHeight and columnHeight attributes are handled (insert a bin build-ab.xml build-admin.xml build-charset.xml build-clean_war.xml build-cli.xml build-code-generators.xml build-common.xml build-controls.xml build-core.xml build-deploy.xml build-docs.xml build-dynamo.xml build-eclipse-common.xml build-eclipse-updateclient.xml build-eclipse.xml build-examples.xml build-external-plugins.xml build-i18n.xml build-librarian-generator.xml build-librarian.xml build-migrate30.xml build-properties.xml build-release.xml build-samples.xml build-servlet.xml build-targets.xml build-testsuite.xml build-tests.xml build-testtools.xml build-tomcat.xml build-tt_gui.xml build-tt_war.xml build-uaprof.xml build-ucp.xml build-update-client-cli.xml build-update-deploy.xml build-update.xml build-validation.xml build-version.properties build-vignette.xml build-weblogic.xml build-websphere.xml build.xml client.cer client.keystore com db doc jar javadoc key librarian-lookup-table.xml librarian.xml mcs.ipr mcs.iws product.key product.lkd redist report.txt Test testdata tests testsuite volantis webapp rather then -1)

 05-May-04	4157/1	matthew	VBM:2003030319 change the way default values of rowHeight and columnHeight attributes are handled (insert a bin build build-ab.xml build-admin.xml build-charset.xml build-clean_war.xml build-cli.xml build-code-generators.xml build-common.xml build-controls.xml build-core.xml build-deploy.xml build-docs.xml build-dynamo.xml build-eclipse-common.xml build-eclipse-updateclient.xml build-eclipse.xml build-examples.xml build-external-plugins.xml build-i18n.xml build-librarian-generator.xml build-librarian.xml build-migrate30.xml build-properties.xml build-release.xml build-samples.xml build-servlet.xml build-targets.xml build-testsuite.xml build-tests.xml build-testtools.xml build-tomcat.xml build-tt_gui.xml build-tt_war.xml build-uaprof.xml build-ucp.xml build-update-client-cli.xml build-update-deploy.xml build-update.xml build-validation.xml build-version.properties build-vignette.xml build-weblogic.xml build-websphere.xml build.xml client.cer client.keystore com db doc jar javadoc key librarian-lookup-table.xml librarian.xml mcs.ipr mcs.iws product.key product.lkd redist report.txt Test testdata tests testsuite volantis webapp rather then -1)

 15-Mar-04	3422/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 12-Mar-04	3403/1	geoff	VBM:2004030907 name attribute not rendered on a tag

 ===========================================================================
*/
