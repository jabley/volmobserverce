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

package com.volantis.mcs.build.themes.definitions.values.impl;

import com.volantis.mcs.build.themes.AbstractThemeClassGenerator;
import com.volantis.mcs.build.themes.definitions.types.Keyword;
import com.volantis.mcs.build.themes.definitions.values.KeywordReference;

import java.io.PrintStream;

/**
 * An implementation of a KeywordReference.
 */
public class KeywordReferenceImpl
    implements KeywordReference {

    /**
     * The keyword that is referenced from this object.
     */
    private Keyword keyword;

    // Javadoc inherited.
    public void setKeyword(Keyword keyword) {
        this.keyword = keyword;
    }

    // Javadoc inherited.
    public void writeConstructCode(String indent, PrintStream out) {

        String name = keyword.getName();

        out.print(indent);
        out.print("StyleKeywords." + AbstractThemeClassGenerator.getConstant(name));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Aug-04	5139/1	geoff	VBM:2004080311 Implement Null Assets: ObjectSelectionPolicys

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
