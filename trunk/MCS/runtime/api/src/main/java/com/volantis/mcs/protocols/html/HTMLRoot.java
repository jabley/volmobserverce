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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLRoot.java,v 1.11 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header and fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by not creating
 *                              StringBuffers when returning a fixed string
 *                              and also by renaming all the *Attributes
 *                              parameters to attributes.
 * 10-Aug-01    Allan           VBM:2001081004 - Added versions of open
 *                              form that will provide a target segment
 *                              if the device supports aggregation and
 *                              segment is set in the attributes.
 * 16-Aug-01    Payal           VBM:2001081501 Added methods openGridPreamble,
 *                              closeGridPostamble, openPanePreamble
 *                              closePanePostamble,
 *                              openColumnIteratorPanePreamble,
 *                              closeColumnIteratorPanePostamble,
 *                              openGridChildPreamble, closeGridChildPostamble
 *                              to allow height attribute to be added to table
 *                              type tags.
 * 20-Aug-01    Allan           VBM:2001081614 - Modified openStyle() to
 *                              set type to text/css.
 * 30-Aug-01    Allan           VBM:2001072509 - Make paneNeedsTableWrapper()
 *                              return false if the pane is a top level pane.
 * 31-Aug-01    Allan           VBM:2001083121 - Set width to 100% in
 *                              openColumnIteratorPanePreamble(). Add
 *                              openRowIteratorPaneElementPreamble() that
 *                              uses a height attribute if available.
 * 04-Sep-01    Paul            VBM:2001081707 - Use getTextFromReference to get
 *                              the text in the correct encoding for those
 *                              attributes whose value could be a
 *                              TextComponentName.
 * 21-Sep-01    Doug            VBM:2001090302 - Use getLinkFromReference to get
 *                              links for those attributes whose value could
 *                              be a LinkComponentName.
 * 29-Oct-01    Paul            VBM:2001102901 - Use new getPreambleBuffer
 *                              method to get the Form's preamble buffer.
 * 31-Oct-01    Doug            VBM:2001092806 tested against the property
 *                              supportsBackgroundInTable in the methods
 *                              openColumnIteratorPanePreamble(),
 *                              openPanePreamble() and openGridPreamble()
 *                              to determine if the protocol supports the
 *                              background attribute for a table tag.
 * 15-Jan-02    Allan           VBM:2002011101 - Modified openForm() to output
 *                              the name attribute if specified.
 * 22-Jan-02    Doug            VBM:2002011003 - Many XHTML protocol methods
 *                              were writting out attributes that resulted in
 *                              invalid XHTML. Moved this functionality to this
 *                              class as the output is valid HTML.
 * 31-Jan-02    Steve           VBM:2002010803 - Bugs found in all the add
 *                              multimedia attribute methods.. They were
 *                              re-calling themselves rather than the
 *                              superclass methods. This leads to a
 *                              Stackoverflow.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 08-Mar-02    Paul            VBM:2002030607 - Stopped calling toString on
 *                              StringOutputBuffers.
 * 11-Mar-02    Doug            VBM:2002011003 - Added a doTitle method.
 *                              Modified the constructor to set the
 *                              altTextForImgRequired property to false.
 * 12-Mar-02    Adrian          VBM:2002021910 - override writeScriptBody to
 *                              call writeDirect as script content is CDATA in
 *                              HTML
 * 13-Mar-02    Paul            VBM:2002030104 - Removed classic form methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 04-Apr-02    Adrian          VBM:2001122105 - Added calls to add the general
 *                              event attributes in add media attribute methods
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 26-Apr-02    Paul            VBM:2002042205 - Fixed release so it does not
 *                              try and release resources which were not
 *                              initialised due to an exception. Also made
 * 26-Apr-02    Paul            VBM:2002042205 - Moved supportsBackgroundInTD
 *                              and supportsBackgroundInTH from the
 *                              HTMLTransparentTV protocol and also moved
 *                              supportsBackgroundInTable from VolantisProtocol
 *                              as they are all needed here. Modified the
 *                              way in which panes are rendered to match the
 *                              changes made in XHTMLFull.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 23-May-02    Paul            VBM:2002042202 - Use XMLOutputter instead of
 *                              DOMOutputter and override getScriptBodyWriter.
 * 29-May-02    Steve           VBM:2002040809 - Override layout style for
 *                              pane if the pane has an associated style class.
 * 18-Jun-02    Steve           VBM:2002040807 - Add core attributes to
 *                              embedded
 *                              elements for CSS transformations
 * 10-Jul-02    Steve           VBM:2002040807 - Removed calls to the four
 *                              param version of addCoreAttributes as the logic
 *                              is now handled by the three parameter version
 *                              as the mariner element name is stored in the
 *                              attributes of the element being rendered.
 * 11-Sep-02    Steve           VBM:2002040809 - Use the style returned from
 *                              getPaneStyle to resolve theme/layout pane style
 *                              attributes.
 * 29-Jan-03    Doug            VBM:2003012713 - Modified the constructor to
 *                              call setOptGroupDepth() with a value of zero.
 * 29-Jan-03    Byron           VBM:2003012803 - Modified constructor to set
 *                              protocolConfiguration value and any static
 *                              variables dependent on it.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * 23-May-03    Mat             VBM:2003042907 - Renamed getXMLOutputter() to
 *                              getDocumentOutputter()
 * 28-May-03    Mat             VBM:2003042911 - Changed to use outputter instead
 *                              of outputterWriter
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.CanvasAttributes;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.GridChildAttributes;
import com.volantis.mcs.protocols.PaneAttributes;
import com.volantis.mcs.protocols.PaneRendering;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.styling.Styles;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Class: HTMLRoot, the base HTML sub-class
 *
 * This sub-class of the root protocol class is itself a root for different
 * versions of HTML. The methods here are generalised HTML 4.0 tags. For
 * each version that sub-classes this, only a few methods will need to be
 * overridden to customise support. Most will work "as is".
 *
 */
public abstract class HTMLRoot
    extends XHTMLTransitional {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
                LocalizationFactory.createLogger(HTMLRoot.class);

    /**
     * Flag for supporting the background attribute in table tags.
     */
    protected boolean supportsBackgroundInTable = true;

    /**
     * Flag for supporting the background attribute in td tags.
     */
    protected boolean supportsBackgroundInTD = false;

    /**
     * Flag for supporting the background attribute in th tags.
     */
    protected boolean supportsBackgroundInTH = false;

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     * objects.
     * @param configuration The protocol specific configuration.
     */
    protected HTMLRoot(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {

        super(supportFactory, configuration);

        altTextForImgRequired = false;
        xmlNamespace = null;

        // by default most HTML protocols do not support the optgroup element
        setOptGroupDepth(0);
    }

    // ========================================================================
    //   General helper methods
    // ========================================================================

    // ========================================================================
    //   Page element methods
    // ========================================================================

    protected abstract void doProtocolString (Document document);

    /**
     * Append the title markup to the specified StringOutputBuffer.
     * @param dom The DOMOutputBuffer to append to.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doTitle (DOMOutputBuffer dom,
                            CanvasAttributes attributes) {

        String value;

        if ((value = attributes.getPageTitle ()) != null) {
            dom.openElement ("title");
            dom.appendEncoded (value);
            dom.closeElement ("title");
        }
    }

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    // Javadoc inherited from super class.
    protected void addColumnIteratorPaneAttributes (Element element,
                                                    ColumnIteratorPaneAttributes attributes) {

        // add the super classes attributes first
        super.addColumnIteratorPaneAttributes (element, attributes);

        Styles styles = attributes.getStyles();
        String value;

        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute ("height", value);
        }
        if (supportsBackgroundInTable) {
            addBackgroundAttribute(element, styles);
        }
    }

    protected void addBackgroundAttribute(
            Element element, Styles styles) {
        String value;
        value = backgroundComponentHandler.getAsString(styles);
        if (value != null) {
            element.setAttribute("background", value);
        }
    }

    // Javadoc inherited from super class.
    protected void addGridAttributes (Element element,
                                      GridAttributes attributes) {

        // add the super classes attributes first
        super.addGridAttributes (element, attributes);

        Styles styles = attributes.getStyles();

        String value;
        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute ("height", value);
        }
        if (supportsBackgroundInTable) {
            addBackgroundAttribute(element, styles);
        }
    }

    // Javadoc inherited from super class.
    protected void addGridChildAttributes (Element element,
                                           GridChildAttributes attributes) {

        // add the super classes attributes first
        super.addGridChildAttributes (element, attributes);

        Styles styles = attributes.getStyles();

        // Only add the height attribute to the cells in the first column.
        int column = attributes.getColumn ();
        String value;
        if (column == 0) {
            if ((value = heightHandler.getAsString(styles)) != null) {
                element.setAttribute("height", value);
            }
        }
    }

    /**
     * This method is overridden to add checks for following attributes:
     *
     * <ul>
     *
     * <li>height (should USE_TABLE or USE_ENCLOSING_TABLE_CELL)</li>
     *
     * <li>background image (should USE_TABLE or USE_ENCLOSING_TABLE_CELL)</li>
     *
     * </ul>
     */
    protected PaneRendering checkPaneCellAttributes(
            Element element,
            PaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        // Call the super class method first.
        PaneRendering rendering = super.checkPaneCellAttributes(
                element, attributes);

        if (rendering == PaneRendering.USE_TABLE) {
            return rendering;
        }

        // Check the height.
        if ((value = heightChecker.getAsString(styles)) != null) {

            if (logger.isDebugEnabled()) {
                logger.debug("Height set to " + value);
            }

            if ((element == null) ||
                    (element.getAttributeValue("height") != null)) {
                return PaneRendering.USE_TABLE;
            }

            // We will at least need to use a cell.
            rendering = PaneRendering.USE_ENCLOSING_TABLE_CELL;
        }

        // Check the background image but only if this protocol actually
        // supports them.
        if (supportsBackgroundInTD || supportsBackgroundInTable) {
            if (backgroundComponentHandler.isSignificant(styles)) {

                if (logger.isDebugEnabled()) {
                    logger.debug("Background image set to " + value);
                }

                // If this protocol only supports background on a table then
                // we need to generate a table.
                if (element == null
                        || (!supportsBackgroundInTD &&
                        supportsBackgroundInTable)
                        || element.getAttributeValue("background") != null) {
                    return PaneRendering.USE_TABLE;
                }
                
                // We will at least need to use a cell.
                rendering = PaneRendering.USE_ENCLOSING_TABLE_CELL;
            }
        }

        return rendering;
    }

    /**
     * Augments the superclass to additionally handle the following attributes:
     *
     * <ul>
     *
     * <li>background</li>
     *
     * </ul>
     */
    protected void addPaneTableAttributes(Element element,
                                          PaneAttributes attributes) {
        // Add the super classes attributes first
        super.addPaneTableAttributes(element, attributes);

        Styles styles = attributes.getStyles();

        // If background images are only supported on tables then add it here.
        if (supportsBackgroundInTable && !supportsBackgroundInTD) {
            addBackgroundAttribute(element, styles);
        }
    }

    /**
     * Augments the superclass to additionally handle the following
     * attributes:
     *
     * <ul>
     *
     * <li>height</li>
     *
     * <li>background</li>
     *
     * </ul>
     */
    protected void addPaneTableOrCellAttributes(Element element,
                                                PaneAttributes attributes,
                                                boolean table) {
        // Add the super classes attributes first
        super.addPaneTableOrCellAttributes(element, attributes, table);

        Styles styles = attributes.getStyles();

        // If background images are supported on either tables, or cells then
        // add it here.
        if (supportsBackgroundInTable || supportsBackgroundInTD) {
            addBackgroundAttribute(element, styles);
        }
    }

    /**
     * Augments the superclass to additionally handle the following
     * attributes:
     *
     * <ul>
     *
     * <li>background</li>
     *
     * </ul>
     */
    protected void addPaneCellAttributes(Element element,
                                         Styles styles) {
        // Add the super classes attributes first
        super.addPaneCellAttributes(element, styles);

        // Check if we have a style class
        if (!supportsBackgroundInTable && supportsBackgroundInTD) {
            addBackgroundAttribute(element, styles);
        }

        String value = null;
        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute("height", value);
        }        
    }

    // javadoc inherited
    protected void createEnclosingElement(DOMOutputBuffer dom,
                                          PaneAttributes attributes)
            throws ProtocolException {
        // We think we need an enclosing element (i.e. a div); we can't use any
        // enclosing TD but don't think we need a table. However, this doesn't
        // account for the case when a width is defined in the theme (and not
        // in the layout). Thus, check to see if we actually need to
        // render a table instead.
        Styles styles = attributes.getStyles();
        boolean createTable = false;

        createTable = widthHandler.isSignificant(styles);

        if (createTable) {
            // We are forced to use a table. Set the rendering to USE_TABLE
            // so that the closePane does not attempt to close a div element
            PaneInstance paneInstance = (PaneInstance)
                    context.getDeviceLayoutContext().getCurrentFormatInstance(
                            attributes.getPane());
            paneInstance.setRendering(PaneRendering.USE_TABLE);

            openPaneTable(dom, attributes);
        } else {
            super.createEnclosingElement(dom, attributes);
        }
    }

    // Javadoc inherited from super class.
    protected void addRowIteratorPaneAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {
        // add the super classes attributes first
        super.addRowIteratorPaneAttributes(element, attributes);

        Styles styles = attributes.getStyles();

        if (supportsBackgroundInTable) {
            addBackgroundAttribute(element, styles);
        }
    }

    // Javadoc inherited from super class.
    protected void addRowIteratorPaneCellAttributes(
            Element element,
            RowIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();

        // add the super classes attributes first
        super.addRowIteratorPaneCellAttributes(element, attributes);

        String value;
        if ((value = heightHandler.getAsString(styles)) != null) {
            element.setAttribute("height", value);
        }
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

    // ========================================================================
    //   Table element methods.
    // ========================================================================

    // ========================================================================
    //   Inline element methods.
    // ========================================================================

    // ========================================================================
    //   Special element methods.
    // ========================================================================

    // ========================================================================
    //   Menu element methods.
    // ========================================================================

    // ========================================================================
    //   Script element methods.
    // ========================================================================

    // ========================================================================
    //   Extended function form element methods.
    // ======================================================================
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-05	9708/4	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 08-Aug-05	9205/1	rgreenall	VBM:2005062107 Forward port of VBM:2005062107

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 28-Oct-05	10020/1	geoff	VBM:2005102406 Height attributes incorrectly rendered

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 22-Apr-05	7820/1	philws	VBM:2005040411 Port submit button fix from 3.3

 22-Apr-05	7812/1	philws	VBM:2005040411 Ensure submit action is retained if action style is image but no image can be found

 22-Apr-05	7791/1	philws	VBM:2005040113 Port openPane changes from 3.3

 22-Apr-05	7746/1	philws	VBM:2005040113 Correct pane rendering where width and/or alignment are specified

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 04-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull - moved PaneRendering

 03-Nov-04	5871/1	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 01-Jun-04	4616/2	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 01-Jun-04	4614/1	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 05-Mar-04	3339/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3337/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 05-Mar-04	3323/1	geoff	VBM:2003052104 Invalid usage of DOMOutputBuffer.appendLiteral()

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 12-Jun-03	381/1	mat	VBM:2003061101 Better debugging for WMLRoot

 05-Jun-03	285/6	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
