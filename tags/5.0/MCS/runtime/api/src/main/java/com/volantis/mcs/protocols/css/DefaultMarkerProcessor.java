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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesMerger;
import com.volantis.styling.StylingFactory;

/**
 * Marker process that changes only the style of the marker, not the content
 * or layout requirements.
 */
public class DefaultMarkerProcessor extends AbstractMarkerProcessor {

    /**
     * The name of the span element.
     */
    private static final String SPAN = "span";


    public void visit(Element element) {

        // now inside a supported list: ul, ol, nl, etc
        final Styles styles = element.getStyles();
        if (styles != null) {
            Styles markerStyles =
                    styles.removeNestedStyles(PseudoElements.MARKER);
            if (markerStyles != null) {
                separateMarkerAndContent(element, markerStyles);
            }
        }
    }

    /**
     * Sets the marker styles as the styles of the list item.  A span is added
     * to list item, whose existing children are put into the span.  The span's
     * styles are taken from the non-marker styles of the list item and the list
     * declaration.
     *
     * @param element the list element.
     * @param markerStyles the marker styles.
     */
    private void separateMarkerAndContent(Element element,
                                          Styles markerStyles) {

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        StylesMerger stylesMerger = stylingFactory.getStylesMerger();

        Styles listItemStyles = element.getStyles();

        // Styles for the content are the same as the original styles for the
        // list item.
        Styles contentStyles = listItemStyles;

        // Styles for the list item are calculated by overriding the styles
        // for the list item with the styles from the marker. This will
        // modify and return the marker styles.
        listItemStyles = stylesMerger.merge(markerStyles, listItemStyles);

        // Update the list item styles associated with the element.
        element.setStyles(listItemStyles);

        DOMFactory domFactory = element.getDOMFactory();
        // add the children of the list item to the span, and the span to
        // the list item

        // Make sure that the content properties do not have a
        // display: list-item
        contentStyles.getPropertyValues().setComputedValue(
                StylePropertyDetails.DISPLAY, DisplayKeywords.INLINE);
        Element contents = domFactory.createElement(SPAN);
        contents.setStyles(contentStyles);

        // Add the list item children to the contents element.
        element.addChildrenToTail(contents);

        // Add the contents element to the list item.
        element.addTail(contents);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
