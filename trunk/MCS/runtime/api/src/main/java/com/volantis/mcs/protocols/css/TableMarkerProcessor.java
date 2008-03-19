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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ContentKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.mcs.themes.properties.VerticalAlignKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Marker process that converts the list to a table.
 */
public class TableMarkerProcessor
        extends AbstractMarkerProcessor {

    /**
     * The table element name.
     */
    private static final String TABLE = "table";

    /**
     * The tr element name.
     */
    private static final String TR = "tr";

    /**
     * The td element name.
     */
    private static final String TD = "td";
    private static final StylingFactory stylingFactory =
            StylingFactory.getDefaultInstance();

    // javadoc inherited
    public void process(Element listElement, Inserter inserter) {
        super.process(listElement, inserter);

        // remove the list-style-position style
        removeListStylePosition(listElement);

        listElement.setName(TABLE);
        Styles styles = listElement.getStyles();
        styles.getPropertyValues()
                .clearPropertyValue(StylePropertyDetails.DISPLAY);
    }


    public void visit(Element element) {

        // now inside a supported list: ul, ol, nl, etc
        final Styles styles = element.getStyles();
        if (styles != null) {
            DOMFactory domFactory = element.getDOMFactory();
            Element tr = domFactory.createElement(TR);
            tr.setStyles(styles);

            // Clear the display property as this is not a list-item
            // any more.
            styles.getPropertyValues()
                    .clearPropertyValue(StylePropertyDetails.DISPLAY);

            createMarkerTD(domFactory, tr, styles);

            createContentTD(domFactory, tr, element);


            // Insert the tr before the node it is replacing in order
            // to ensure that it does not get visited itself.
            tr.insertBefore(element);

            // Removed the element.
            element.remove();
        }
    }

    /**
     * Creates the td element containing the marker and inserts it into the
     * table row.
     *
     * @param domFactory the DOM factory of the parent element.
     * @param tr         the table row the td should be placed in.
     * @param styles     the parent styles.
     */
    private void createMarkerTD(
            DOMFactory domFactory,
            Element tr,
            Styles styles) {

        Element markerTd = domFactory.createElement(TD);
        tr.addTail(markerTd);

        Styles markerStyles = styles.removeNestedStyles(PseudoElements.MARKER);
        if (markerStyles == null) {
            markerStyles = stylingFactory.createInheritedStyles(
                    styles, DisplayKeywords.TABLE_CELL);
        }

        markerTd.setStyles(markerStyles);
        MutablePropertyValues propertyValues =
                markerStyles.getPropertyValues();
        StyleValue content = propertyValues.getComputedValue(
                StylePropertyDetails.CONTENT);

        // Make sure that the display is set correctly on the td.
        propertyValues.setComputedValue(StylePropertyDetails.DISPLAY,
                DisplayKeywords.TABLE_CELL);

        if (content != ContentKeywords.NORMAL &&
                content != ContentKeywords.NONE) {

            // Make sure that the marker is at the top of the table cell
            // and aligned to the right unless it has been explicitly
            // specified to be elsewhere.
            overrideVerticalAlign(propertyValues);
            overrideTextAlign(propertyValues);

            Inserter inserter = getInserter();
            inserter.insert(markerTd, content);
            propertyValues.clearPropertyValue(StylePropertyDetails.CONTENT);
        }
    }

    private void overrideTextAlign(MutablePropertyValues propertyValues) {
        propertyValues.overrideUnlessExplicitlySpecified(
                StylePropertyDetails.TEXT_ALIGN, TextAlignKeywords.RIGHT);
    }

    private void overrideVerticalAlign(MutablePropertyValues propertyValues) {
        propertyValues.overrideUnlessExplicitlySpecified(
                StylePropertyDetails.VERTICAL_ALIGN, VerticalAlignKeywords.TOP);
    }

    /**
     * Creates the td element containing the content and inserts it into the
     * table row.
     *
     * @param domFactory the DOM factory of the element element.
     * @param tr         the table row the td should be placed in.
     * @param element     the element element, e.g. the list item.
     */
    private void createContentTD(
            DOMFactory domFactory, Element tr, Element element) {

        Element contentTd = domFactory.createElement(TD);

        // todo don't use this as it does not handle styles from default.css properly.
        Styles styles = stylingFactory.createInheritedStyles(tr.getStyles(),
                DisplayKeywords.TABLE_CELL);
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        // Make sure that the content is at the top of the table cell unless
        // it has been explicitly specified to be elsewhere.
        overrideVerticalAlign(propertyValues);

        contentTd.setStyles(styles);
        tr.addTail(contentTd);

        element.addChildrenToTail(contentTd);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
