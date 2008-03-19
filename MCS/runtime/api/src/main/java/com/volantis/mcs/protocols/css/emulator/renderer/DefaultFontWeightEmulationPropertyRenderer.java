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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.FontWeightKeywords;

/**
 * Renders the 'font-weight' style property as stylistic markup.
 * <p>
 * This renders either a 'b' element (for 'bold', '900' - '700') or an
 * ANTI-B element (for '600' - '100').
 * <p>
 * Nesting of b and anti-b elements will be dealt with by a subsequent
 * DOM transformer.
 */
public class DefaultFontWeightEmulationPropertyRenderer
        extends StyleEmulationElementRenderer {

    // Javadoc inherited.
    public String getElementName(StyleValue value) {

        String result = null;
        if (value == FontWeightKeywords.BOLD ||
                value == FontWeightKeywords.WEIGHT__900 ||
                value == FontWeightKeywords.WEIGHT__800 ||
                value == FontWeightKeywords.WEIGHT__700) {
            result = "b";
        } else if (value == FontWeightKeywords.WEIGHT__600 ||
                value == FontWeightKeywords.WEIGHT__500 ||
                value == FontWeightKeywords.WEIGHT__400 ||
                value == FontWeightKeywords.WEIGHT__300 ||
                value == FontWeightKeywords.WEIGHT__200 ||
                value == FontWeightKeywords.WEIGHT__100) {
            result = StyleEmulationVisitor.ANTI_BOLD_ELEMENT;
        }
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 17-Nov-05	10356/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 15-Nov-05	10333/1	geoff	VBM:2005110120 MCS35: WML not rendering mode and align in dissecting panes

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
