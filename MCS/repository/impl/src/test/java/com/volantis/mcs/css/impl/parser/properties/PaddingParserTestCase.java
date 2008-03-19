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

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Test the parser for padding.
 */
public class PaddingParserTestCase
        extends GenericAllEdgeTestCaseAbstract {

    /**
     * Initialise.
     */
    public PaddingParserTestCase() {
        super("padding",
              new String[] {"1px", "2cm", "3in", "4pt"},
              new StyleValue[] {
                  STYLE_VALUE_FACTORY.getLength(null, 1, LengthUnit.PX),
                  STYLE_VALUE_FACTORY.getLength(null, 2, LengthUnit.CM),
                  STYLE_VALUE_FACTORY.getLength(null, 3, LengthUnit.IN),
                  STYLE_VALUE_FACTORY.getLength(null, 4, LengthUnit.PT),
              },
              PropertyGroups.PADDING_PROPERTIES);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
