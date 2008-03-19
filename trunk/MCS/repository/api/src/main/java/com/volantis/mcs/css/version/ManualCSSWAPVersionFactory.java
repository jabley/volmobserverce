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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.version;

import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;

/**
 * Creates a correct definition of CSS WAP by using the generated CSS WAP
 * version for the info available from the schema and adding any data not
 * available from there manually.
 * <p>
 * Really all this data should come from the schema.
 */
public class ManualCSSWAPVersionFactory implements DefaultCSSVersionFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // Javadoc inherited.
    public DefaultCSSVersion createCSSVersion() {

        DefaultCSSVersion version = new CSSWAPVersionFactory().createCSSVersion();

        // Remove the pseudo class selector ids that we support.
        // I have just used the mandatory and optional values from
        // WAP-239-WCSS-20011026-a.pdf since A913 does not include this
        // information.
        version.addPseudoSelectorId( new String[] {
                "link", "visited", // mandatory
                "active", "focus"  // optional
        });

        // Remove the shorthand properties that the architecture (A913)
        // says are not supported in CSSWAP.
        version.addShorthandProperty(StyleShorthands.BACKGROUND);
        version.addShorthandProperty(StyleShorthands.BORDER);
        version.addShorthandProperty(StyleShorthands.BORDER_BOTTOM);
        version.addShorthandProperty(StyleShorthands.BORDER_COLOR);
        version.addShorthandProperty(StyleShorthands.BORDER_LEFT);
        version.addShorthandProperty(StyleShorthands.BORDER_RIGHT);
        // version.addShorthandProperty(StyleShorthand.BORDER_STYLE);
        version.addShorthandProperty(StyleShorthands.BORDER_TOP);
        //version.addShorthandProperty(StyleShorthand.BORDER_WIDTH);
        version.addShorthandProperty(StyleShorthands.FONT);
        // version.addShorthandProperty(StyleShorthand.LIST_STYLE);
        //version.addShorthandProperty(StyleShorthand.MARGIN);
        //version.addShorthandProperty(StyleShorthand.PADDING);
        //version.addShorthandProperty(StyleShorthands.MARQUEE);

        return version;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
