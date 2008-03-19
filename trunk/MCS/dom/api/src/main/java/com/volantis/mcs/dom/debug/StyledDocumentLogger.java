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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.mcs.dom.debug;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Logs the output for a styled Document to the log.
 */

public class StyledDocumentLogger {
        private static final LogDispatcher logger =
            LocalizationFactory.createLogger(StyledDocumentLogger.class);

    private static final MutableStylePropertySet INTERESTING;
    static {
        INTERESTING = new MutableStylePropertySetImpl();

        INTERESTING.add(StylePropertyDetails.DISPLAY);
        INTERESTING.add(StylePropertyDetails.COLOR);
        INTERESTING.add(StylePropertyDetails.FONT_SIZE);
        INTERESTING.add(StylePropertyDetails.FONT_FAMILY);
        INTERESTING.add(StylePropertyDetails.FONT_WEIGHT);
        INTERESTING.add(StylePropertyDetails.BACKGROUND_COLOR);
        INTERESTING.add(StylePropertyDetails.BORDER_BOTTOM_COLOR);
        INTERESTING.add(StylePropertyDetails.PADDING_TOP);
        INTERESTING.add(StylePropertyDetails.BACKGROUND_IMAGE);
        INTERESTING.add(StylePropertyDetails.FONT_STYLE);
        INTERESTING.add(StylePropertyDetails.TEXT_ALIGN);
        INTERESTING.add(StylePropertyDetails.VERTICAL_ALIGN);
        INTERESTING.add(StylePropertyDetails.WIDTH);
        INTERESTING.add(StylePropertyDetails.DIRECTION);
    }

    public static void logDocument(Document styledDom) {

        if (logger.isDebugEnabled()) {
            DebugStyledDocument debugStyledDocument =
                    new DebugStyledDocument(INTERESTING);
            String debug = debugStyledDocument.debug(styledDom);
            logger.debug(debug);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10647/1	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 06-Dec-05	10628/2	ibush	VBM:2005113021 Fix Border Bottom Styling by fixing styles merger

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 28-Nov-05	10443/2	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10443/1	ianw	VBM:2005111812 interim commit for IB

 ===========================================================================
*/
