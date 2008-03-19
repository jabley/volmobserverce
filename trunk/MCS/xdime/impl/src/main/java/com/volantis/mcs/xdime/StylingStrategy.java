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
package com.volantis.mcs.xdime;

import com.volantis.styling.Styles;
import com.volantis.styling.engine.Attributes;
import com.volantis.styling.engine.StylingEngine;

/**
 * Interface which defines how the styling should be used.
 */
public interface StylingStrategy {
    /**
     * A new element has been encountered that needs styling.
     *
     * @param namespace The namespace of the element, may not be null.
     * @param localName The local name of the element.
     * @param attributes The attributes of the element.
     */
    void startElement(StylingEngine engine,
                      String namespace, String localName,
                      Attributes attributes);

    /**
     * Get the styles associated with the element that has just been styled.
     *
     * @return The styles associated with the element that has just been
     * styled.
     */
    Styles getStyles(StylingEngine engine);

    /**
     * An element has been finished with.
     *
     * @param namespace The namespace of the element, may not be null.
     * @param localName The local name of the element, may not be null.
     */
    void endElement(StylingEngine engine, String namespace, String localName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
