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
 * Concrete implementation of {@link StylingStrategy} which does not style
 * the element.
 */
public class UnstyledStrategy implements StylingStrategy {

    // Javadoc inherited.
    public void startElement(
            StylingEngine engine, String namespace, String localName,
            Attributes attributes) {
    }

    // Javadoc inherited.
    public Styles getStyles(StylingEngine engine) {
        return null;
    }

    // Javadoc inherited.
    public void endElement(
            StylingEngine engine, String namespace, String localName) {
    }

    public static final UnstyledStrategy STRATEGY = new UnstyledStrategy();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/3	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
