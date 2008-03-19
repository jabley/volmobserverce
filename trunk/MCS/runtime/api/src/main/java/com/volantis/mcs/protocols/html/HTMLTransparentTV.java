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
 * $Header: /src/voyager/com/volantis/mcs/protocols/html/HTMLTransparentTV.java,v 1.7 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Jul-01    Paul            VBM:2001062810 - Added this header and fixed
 *                              the code so that it only gets values out of
 *                              attributes once and uses append for each
 *                              separate part of the output string instead of
 *                              inline string concatenation. Also made use of
 *                              the new getStyle method in VolantisProtocol to
 *                              simplify the code.
 * 23-Jul-01    Paul            VBM:2001070507 - Simplified by not creating
 *                              StringBuffers when returning a fixed string
 *                              and by renaming all the parameters to
 *                              attributes. Also removed the openHead and
 *                              closeHead as they are unnecessary.
 * 03-Aug-01    Kula            VBM:2001080102 Height property added to table
 * 10-Aug-01    Allan           VBM:2001062705 Moved openBody, openInput and
 *                              openTextArea to HTMLLiberate.java.
 * 14-Aug-01    Payal           VBM:2001080803 - Height property added to Grid
 *                              changed openGridPreamble() so that it uses height
 *                              attribute.
 * 05-Sep-01    Kula            VBM:2001090401 - openTableRow(),
 *                              openTableDataCell(), and openTableHeaderCell()
 *                              methods are modified to write the emulated
 *                              style attributes.
 * 10-Sep-01    Kula            VBM:2001090401 - javadoc comments added to
 *                              TableRow, TableDataCell,and TableHeaderCell
 *                              open  methods. nowrap attribute added to
 *                              openTableDataCell and openTableHeaderCell
 *                              methods
 * 04-Oct-01    Doug            VBM:2001100201 - Added constructor so that the
 *                              supportsAccessKeyAttribute property could be
 *                              set to false. This protocol does not support
 *                              the accesskey attribute on any tags.
 * 09-Oct-01    Payal           VBM:2001090605 Renamed
 *                              HTMLVersion3_2Transparent
 *                              to abstract class HTMLTransparentTV.
 * 10-Oct-01    Payal           VBM:2001090605 - javadoc for the class.Changed
 *                              HTMLTransparentTV: constructor to protected.
 * 10-Oct-01    Allan           VBM:2001092806 - Added addBackground() methods
 *                              that will add a background attribute to
 *                              the outStr StringBuffer it is called with.
 *                              Modified openTable(), openTableHeader(), and
 *                              openTableDataCell() to call addBackground if
 *                              background is supported on that tag. Added
 *                              flags for: supportsBackgroundInTable,
 *                              supportsBackgroundInTD, supportsBackgroundInTH,
 *                              and supportsBackgroundInBody and set defaults.
 * 11-Oct-01    Allan           VBM:2001090401 - TableDataCell and
 *                              TableHeaderCell type methods changed to use
 *                              TableCellAttributes.
 * 26-Oct-01    Doug            VBM:2001092806 - Added addBackground() methods
 *                              that will add a background attribute to
 *                              the outStr StringBuffer it is called with.
 *                              Modified openTable(), openTableHeader(), and
 *                              openTableDataCell() to call addBackground if
 *                              background is supported on that tag. These had
 *                              been added before but were subsequently lost.
 *                              Also checked supportsBackgroundInBody before
 *                              writting out a backround url in the methods
 *                              openPanePreamble, openGridPreamble,
 *                              openColumnIteratorPanePreamble and
 *                              openRowIteratorPanePreamble .
 * 31-Oct-01    Doug            VBM:2001092806 moved the  protected member
 *                              supportsBackgroundInTable to VolantisProtocol.
 *                              Modified openPanePreamble() to write a
 *                              background attribute in the td tag if required.
 *                              Modified the methods openPanePreamble(),
 *                              openColumnIteratorPanePreamble,
 *                              openRowIteratorPanePreamble(), and
 *                              openGridPreamble() to support the background
 *                              attribute in td tags.
 *                              Added the methods closeGridPostamble(),
 *                              closeColumnIteratorPanePostamble() and
 *                              closeRowIteratorPanePostamble() to support
 *                              the background attribute in td tags.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 31-Jan-02    Paul            VBM:2001122105 - Restructured to reflect
 *                              changes to protocols.
 * 06-Feb-02    Paul            VBM:2001122103 - Added missing call to
 *                              super.addPaneCellAttributes and remove
 *                              overridden appendOpenPane method.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed calls to getPageHead
 *                              method in MarinerPageContext as it is now
 *                              accessible to subclasses of StringProtocol
 *                              through a protected field.
 * 28-Feb-02    Paul            VBM:2002022804 - Made methods consistent with
 *                              other StringProtocol based classes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Moved from protocols and
 *                              made it generate a DOM.
 * 26-Apr-02    Paul            VBM:2002042205 - Moved supportsBackgroundInTD
 *                              and supportsBackgroundInTH to HTMLRoot and
 *                              removed the code which added the background
 *                              attribute to the pane as that is now handled
 *                              in HTMLRoot.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 27-Aug-02    Adrian          VBM:2001090607 - Removed default bgColor of
 *                              "transparent" from methods...
 *                              openColumnIteratorPane, openRowIteratorPane,
 *                              addTableAttributes, addTableCellAttributes,
 *                              openGrid.  Removed method addPaneCellAttributes
 *                              and addTableRowAttributes.
 * 11-Sep-02    Steve           VBM:2002040809 - Use the style returned from
 *                              getPaneStyle to resolve theme/layout pane style
 *                              attributes.
 * 20-Jan-03    Geoff           VBM:2003011616 - Removed redundant
 *                              supportsAccessKeyAttribute setting which was
 *                              just setting it to the same as the superclass,
 *                              usual IDEA cleanup.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.ColumnIteratorPaneAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.GridAttributes;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.RowIteratorPaneAttributes;
import com.volantis.mcs.protocols.TableAttributes;
import com.volantis.mcs.protocols.TableCellAttributes;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;

/**
 *
 * This is a sub-class of the HTMLRoot protocol class to provide the precise
 * definition of the HTMLTransparentTV protocol. Very little here is different
 * from the HTMLRoot class definition, so most things are referenced to the
 * superclass.
 *
 */
public abstract class HTMLTransparentTV
    extends HTMLVersion3_2 {

    /**
     * Constructor for HTMLTransparentTV.
     */
    protected HTMLTransparentTV(ProtocolSupportFactory supportFactory,
                          ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    // ========================================================================
    //   General helper methods
    // ========================================================================

    // ========================================================================
    //   Page element methods
    // ========================================================================

    // ========================================================================
    //   Dissection methods
    // ========================================================================

    // ========================================================================
    //   Layout / format methods
    // ========================================================================

    /**
     * Override this method to set default background colour and to wrap the
     * contents of the column iterator pane in another table if necessary.
     */
    protected
        void openColumnIteratorPane (DOMOutputBuffer dom,
                                     ColumnIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        super.openColumnIteratorPane (dom, attributes);

        // If a background image is set and it is not supported on the table
        // element but is supported on the td element then add a td on which
        // the background image is set and then add a nested table to contain
        // the contents of the iterator pane.
        if (!supportsBackgroundInTable && supportsBackgroundInTD
            && (value = backgroundComponentHandler.getAsString(styles)) != null) {

            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element element = dom.openStyledElement ("td", attributes,
                    DisplayKeywords.TABLE_CELL);
            element.setAttribute ("background", value);

            element = dom.openElement ("table");
            element.setAttribute ("cellpadding", "0");
            element.setAttribute ("cellspacing", "0");
            element.setAttribute ("width", "100%");

            dom.openElement ("tr");
        }
    }

    /**
     * Override close the wrapping table if necessary.
     */
    protected
        void closeColumnIteratorPane (DOMOutputBuffer dom,
                                      ColumnIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();

        // If we wrapped the contents in an extra table in order to add the
        // background image then we need to close the table as well.
        if (!supportsBackgroundInTable && supportsBackgroundInTD
            && (backgroundComponentHandler.isSignificant(styles))) {

            dom.closeElement ("tr");
            dom.closeElement ("table");
            dom.closeElement ("td");
        }

        super.closeColumnIteratorPane (dom, attributes);
    }

    /**
     * Override this method to set default background colour and to wrap the
     * contents of the row iterator pane in another table if necessary.
     */
    protected
        void openRowIteratorPane (DOMOutputBuffer dom,
                                  RowIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        super.openRowIteratorPane (dom, attributes);

        // If a background image is set and it is not supported on the table
        // element but is supported on the td element then add a td on which
        // the background image is set and then add a nested table to contain
        // the contents of the iterator pane.
        if (!supportsBackgroundInTable && supportsBackgroundInTD
            && (value = backgroundComponentHandler.getAsString(styles)) != null) {

            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element element = dom.openStyledElement ("tr", attributes,
                    DisplayKeywords.TABLE_ROW);

            element = dom.openElement ("td");
            element.setAttribute ("background", value);

            element = dom.openElement ("table");
            element.setAttribute ("cellpadding", "0");
            element.setAttribute ("cellspacing", "0");
            element.setAttribute ("width", "100%");
        }
    }

    /**
     * Override close the wrapping table if necessary.
     */
    protected
        void closeRowIteratorPane (DOMOutputBuffer dom,
                                   RowIteratorPaneAttributes attributes) {

        Styles styles = attributes.getStyles();

        // If we wrapped the contents in an extra table in order to add the
        // background image then we need to close the table as well.
        if (!supportsBackgroundInTable && supportsBackgroundInTD
            && backgroundComponentHandler.isSignificant(styles)) {

            dom.closeElement ("table");
            dom.closeElement ("td");
            dom.closeElement ("tr");
        }

        super.closeRowIteratorPane (dom, attributes);
    }

    /**
     * Override this method to set default background colour and to wrap the
     * contents of the grid in another table if necessary.
     */
    protected void openGrid (DOMOutputBuffer dom,
                             GridAttributes attributes) {

        Styles styles = attributes.getStyles();
        String value;

        super.openGrid (dom, attributes);

        // If a background image is set and it is not supported on the table
        // element but is supported on the td element then add a td on which
        // the background image is set and then add a nested table to contain
        // the contents of the iterator pane.
        if (!supportsBackgroundInTable && supportsBackgroundInTD
            && (value = backgroundComponentHandler.getAsString(styles)) != null) {

            // @todo 2005060816 annotate child with style information if it's not inherited from the parent
            Element element = dom.openStyledElement ("tr", attributes,
                    DisplayKeywords.TABLE_ROW);

            element = dom.openElement ("td");
            element.setAttribute ("background", value);

            element = dom.openElement ("table");
            element.setAttribute ("cellpadding", "0");
            element.setAttribute ("cellspacing", "0");
            element.setAttribute ("width", "100%");
        }
    }

    /**
     * Override close the wrapping table if necessary.
     */
    protected void closeGrid (DOMOutputBuffer dom,
                              GridAttributes attributes) {

        Styles styles = attributes.getStyles();

        // If we wrapped the contents in an extra table in order to add the
        // background image then we need to close the table as well.
        if (!supportsBackgroundInTable && supportsBackgroundInTD
            && backgroundComponentHandler.isSignificant(styles)) {

            dom.closeElement ("table");
            dom.closeElement ("td");
            dom.closeElement ("tr");
        }

        super.closeGrid (dom, attributes);
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

    /**
     * Override this method to add extra attributes to the table.
     */
    protected void addTableAttributes (Element element,
                                       TableAttributes attributes)
            throws ProtocolException {

        Styles styles = attributes.getStyles();

        // Add the super class attributes.
        super.addTableAttributes (element, attributes);

        if (supportsBackgroundInTable) {
          addBackgroundAttribute (element, styles);
        }
    }

    /**
     * Override this method to set extra attributes on the table cell.
     */
    protected void addTableCellAttributes (Element element,
                                           TableCellAttributes attributes)
            throws ProtocolException {

        Styles styles = attributes.getStyles();

        // Add the super class attributes.
        super.addTableCellAttributes(element, attributes);

        // Add background images if they are supported and needed.
        String cellType = attributes.getTagName();
        boolean supportsBackground = false;
        if ("td".equals(cellType)) {
            supportsBackground = supportsBackgroundInTD;
        } else if ("th".equals(cellType)) {
            supportsBackground = supportsBackgroundInTH;
        }

        if (supportsBackground) {
            addBackgroundAttribute(element, styles);
        }
    }

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
    //   Classic form element methods.
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

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 25-Jun-04	4720/2	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
