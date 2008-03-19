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
package com.volantis.mcs.protocols.href;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;

/**
 * Rule for handling hrefs in XHTML 2 elements.
 */
public abstract class HrefRule {

    final String HREF = "href";

    /**
     * The transform method which processes the element.
     *
     * @param element element to process
     */
    abstract void transform(Element element);

    /**
     * Add styles to the new element that are inherited from the old
     * element.
     *
     * @param element source element for the styles
     * @param newElement target element which will recive the inherited
     *                   styles
     */
    protected void addInheritedStyles(Element element, Element newElement) {
        final StylingFactory stylingFactory =
            StylingFactory.getDefaultInstance();
        Styles newStyles =
            stylingFactory.createInheritedStyles(element.getStyles(), 
                    DisplayKeywords.INLINE);

        newElement.setStyles(newStyles);
    }

    /**
     * Move the href attribute from one element to the other.
     *
     * @param element source element for the href attribute
     * @param newElement target element
     */
    protected void moveTheHref(Element element, Element newElement) {

        String href = element.getAttributeValue(HREF);
        element.removeAttribute(HREF);

        newElement.setAttribute(HREF, href);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9128/3	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 ===========================================================================
*/
