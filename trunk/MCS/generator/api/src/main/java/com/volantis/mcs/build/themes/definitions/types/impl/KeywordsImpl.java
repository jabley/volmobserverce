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

package com.volantis.mcs.build.themes.definitions.types.impl;

import com.volantis.mcs.build.themes.definitions.types.Keywords;
import com.volantis.mcs.build.themes.definitions.types.Keyword;
import com.volantis.mcs.build.themes.definitions.types.TypeVisitor;

import java.util.Map;
import java.util.HashMap;

/**
 * A set of keywords.
 */
public class KeywordsImpl
    implements Keywords {

    /**
     * The map from name to Keyword.
     */
    private Map keywords;

    /**
     * Initialise.
     */
    public KeywordsImpl() {
        keywords = new HashMap();
    }

    // Javadoc inherited.
    public void addKeyword(Keyword keyword) {
        keywords.put(keyword.getName(), keyword);
    }

    // Javadoc inherited.
    public Keyword getKeyword(String keywordName) {
        return (Keyword) keywords.get(keywordName);
    }

    // Javadoc inherited.
    public void accept(TypeVisitor visitor, Object obj) {
        visitor.visitKeywords(this, obj);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/1	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
