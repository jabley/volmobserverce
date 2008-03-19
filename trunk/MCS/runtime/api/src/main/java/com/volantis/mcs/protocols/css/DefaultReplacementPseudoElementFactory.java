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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ContentKeywords;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of a ReplacementPseudoElementFactory.
 */
public class DefaultReplacementPseudoElementFactory
        implements ReplacementPseudoElementFactory {

    private final DOMProtocol domProtocol;

    /**
     * Maps display property values to elements.
     */
    private static final Map DISPLAY_MAP = new HashMap();

    static {
        DISPLAY_MAP.put(DisplayKeywords.BLOCK, "div");
        DISPLAY_MAP.put(DisplayKeywords.INLINE, "span");
    }

    public DefaultReplacementPseudoElementFactory(DOMProtocol domProtocol) {
        this.domProtocol = domProtocol;
    }

    // javadoc inherited
    public Element createElement(Element element, Styles styles) {

        Element e = getElement(element, styles);
        MutablePropertyValues propValues = styles.getPropertyValues();
        StyleValue content =
                propValues.getComputedValue(StylePropertyDetails.CONTENT);

        if (content != ContentKeywords.NORMAL &&
                content != ContentKeywords.NONE) {
            Inserter inserter = domProtocol.getInserter();
            inserter.insert(e, content);

            // Clear the content property as we have already dealt with it.
            propValues.clearPropertyValue(StylePropertyDetails.CONTENT);
        }

        return e;
    }

    /**
     * If a display style is specified span or div are to be used,
     * otherwise a style emulator is used.
     *
     * @param element the parent element.
     * @param styles  the pseudo element styles.
     * @return the element that effects the pseudo element.
     */
    private Element getElement(Element element, Styles styles) {
        MutablePropertyValues propValues = styles.getPropertyValues();
        Element e = element.getDOMFactory().createElement();
        e.setStyles(styles);

        // Get the computed value, as the properties are from a pseudo element
        // it will not be fully populated so use the defaults if not set.
        StyleValue display = propValues
                .getComputedValue(StylePropertyDetails.DISPLAY);
        if (display == null) {
            display = StylePropertyDetails.DISPLAY.getStandardDetails()
                    .getInitialValue();
        } else {
            // Clear the property value.
            propValues.clearPropertyValue(StylePropertyDetails.DISPLAY);
        }

        String name = (String) DISPLAY_MAP.get(display);
        if (name == null) {
            // displayValue may be a CSS-valid value but only
            // block and inline are supported.
            throw new UnsupportedOperationException
                    (display + " is not supported");
        } else {
            e.setName(name);
        }

        return e;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/5	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 07-Sep-05	9413/3	schaloner	VBM:2005070406 Changed style property iteration to direct access

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 ===========================================================================
*/
