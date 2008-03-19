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
package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Marker process that converts the list to a block and inline elements.
 */
public class BlockInlineMarkerProcessor
        extends AbstractMarkerProcessor {

    /**
     * The div element's name.
     */
    private static final String DIV = "div";

    /**
     * The span element's name.
     */
    private static final String SPAN = "span";

    private static final StyleLength DEFAULT_PADDING__RIGHT =
            StyleValueFactory.getDefaultInstance().getLength(
                null, 3, LengthUnit.PX);

    /**
     * Creates the element containing the marker.
     *
     * @param domFactory the DOM factory of the parent element.
     * @param markerStyles     the parent markerStyles.
     * @return the marker element.
     */
    private Element createMarkerElement(DOMFactory domFactory, Styles markerStyles) {

        Element markerSpan = domFactory.createElement(SPAN);
        markerSpan.setStyles(markerStyles);

        MutablePropertyValues propertyValues =
                markerStyles.getPropertyValues();
        StyleValue content =
                propertyValues.getComputedValue(StylePropertyDetails.CONTENT);
        if (content != null) {
            Inserter inserter = getInserter();
            inserter.insert(markerSpan, content);

            propertyValues.clearPropertyValue(StylePropertyDetails.CONTENT);

            // As some content has been added to the marker make sure that
            // if the user has not explicitly specified any padding on the
            // right that we add some to make it readable.
            propertyValues.overrideUnlessExplicitlySpecified(
                    StylePropertyDetails.PADDING_RIGHT, DEFAULT_PADDING__RIGHT);

        } // otherwise don't use a marker

        return markerSpan;
    }

    // javadoc inherited
    public void process(Element listElement, Inserter inserter) {
        super.process(listElement, inserter);

        listElement.setName(DIV);

        // remove the list-style-position style
        removeListStylePosition(listElement);
    }


    public void visit(Element element) {

        // This element should be a <li>.

        // now inside a supported list: ul, ol, nl, etc
        final Styles styles = element.getStyles();
        if (styles != null) {
            DOMFactory domFactory = element.getDOMFactory();
            Element div = domFactory.createElement(DIV);
            div.setStyles(styles);

            // Clear the display property as this is not a list-item
            // any more.
            styles.getPropertyValues()
                    .clearPropertyValue(StylePropertyDetails.DISPLAY);

            Styles markerStyles =
                    styles.removeNestedStyles(PseudoElements.MARKER);
            if (markerStyles != null) {
                Element marker =
                        createMarkerElement(domFactory, markerStyles);
                div.addTail(marker);
            }

            // Insert the div before the node it is replacing in order
            // to ensure that it does not get visited itself.
            div.insertBefore(element);

            // Removed the element and add all its children to the
            // div.
            element.remove();
            element.addChildrenToTail(div);
        }
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
