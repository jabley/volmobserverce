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
package com.volantis.mcs.dom2theme.impl.model;

import com.volantis.mcs.dom.Element;

/**
 * A styled element which has been adapted for use during the "output" phase.
 * <p>
 * This exists primarily to allow us to associate the similarly adapted
 * {@link OutputStyles} with it's owning element.
 * <p>
 * NOTE: since styles on an output element will generally be quite sparse
 * (unlike styles on normal elements), we allow styles to be null.
 *
 * @mock.generate
 */
public class OutputStyledElement {

    /**
     * The real element which backs this class.
     */
    private Element element;

    /**
     * The adapted styles.
     */
    private OutputStyles styles;

    /**
     * Initialise.
     *
     * @param element the real element which backs this class, may not be null.
     * @param styles the adapted styles, may be null.
     */
    public OutputStyledElement(Element element, OutputStyles styles) {

        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }

        this.element = element;
        this.styles = styles;
    }

    /**
     * Get the name of the element.
     *
     * @return the name of the element.
     */
    public String getName() {

        return element.getName();
    }

    /**
     * Set the CSS class of the element.
     *
     * @param className the name of the class.
     */
    public void setClass(String className) {

        element.setAttribute("class", className);
    }

    public Element getElement() {
        return element;
    }

    /**
     * Get the styles associated with this element.
     *
     * @return the styles associated with this element, or null if there are
     *      none.
     */
    public OutputStyles getStyles() {

        return styles;
    }

    /**
     * Clear any styles associated with this element.
     * <p>
     * Once this has been called, {@link #getStyles()} will return null.
     */
    public void clearStyles() {

        styles = null;
    }

    // javadoc inherited
    public String toString() {
        return "[" + getClass().getName()
                + ": element=" + element.toString()
                + ", styles=" + styles
                + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 18-Jul-05	8668/6	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
