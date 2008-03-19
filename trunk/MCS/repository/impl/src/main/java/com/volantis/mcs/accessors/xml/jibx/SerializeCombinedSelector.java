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
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.themes.CombinatorEnum;

import java.util.HashMap;

/**
 * Serialize/Deserialize methods for CombindSelector combinator attribute.
 */
public class SerializeCombinedSelector {

    private static HashMap enumToTextMapping = new HashMap();
    private static HashMap textToEnumMapping = new HashMap();

    static {
        enumToTextMapping.put(CombinatorEnum.CHILD, "child");
        textToEnumMapping.put("child", CombinatorEnum.CHILD);

        enumToTextMapping.put(CombinatorEnum.DESCENDANT, "descendant");
        textToEnumMapping.put("descendant", CombinatorEnum.DESCENDANT);

        enumToTextMapping.put(CombinatorEnum.DIRECT_ADJACENT,
                "direct-adjacent");
        textToEnumMapping.put("direct-adjacent",
                CombinatorEnum.DIRECT_ADJACENT);

        enumToTextMapping.put(CombinatorEnum.INDIRECT_ADJACENT,
                "indirect-adjacent");
        textToEnumMapping.put("indirect-adjacent",
                CombinatorEnum.INDIRECT_ADJACENT);


    }

    /**
     * Helper method for converting the combinator value read from an XML file
     * into an enum suitable for storing in a CombinedSelector class.
     *
     * @param text Text to be converted, null input will return null output
     * @return combinator value as an CombinatorEnum or null if not
     *         recognised.
     */
    public static CombinatorEnum combinatorTextToEnum(String text) {

        if (text == null) {
            return null;
        }

        String upText = text.toLowerCase();

        return (CombinatorEnum) textToEnumMapping.get(upText);

    }

    /**
     * Heler method for converting the combinator attribute of a
     * CombinedSelector object to a string for writing out to an XML file.
     *
     * @param combinatorEnum
     * @return String representation of combinator value or null if not found.
     */
    public static String combinatorEnumToText(CombinatorEnum combinatorEnum) {

        if (combinatorEnum == null) {
            return null;
        }

        return (String) enumToTextMapping.get(combinatorEnum);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 29-Jun-05	8552/1	pabbott	VBM:2005051902 JIBX Theme accessors

 ===========================================================================
*/
