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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/Utils.java,v 1.3 2003/04/25 23:23:45 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Jan-03    Phil W-S        VBM:2002110402 - Created. Useful methods that
 *                              support test cases for protocols.
 * 25-Apr-03    Allan           VBM:2003041104 - Added some todos.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.trans.TransTable;
import com.volantis.mcs.protocols.trans.TransCell;
import com.volantis.mcs.dom.Element;

import java.util.Set;
import java.util.HashSet;

import junit.framework.Assert;

/**
 * Useful methods that support test cases for protocols.
 * @todo The name of this class seems a bit vague which is one of the reasons
 * why I have just created ProtocolsTestHelper. Some other reasons are
 * described in method todos.
 */
public class Utils {
    /**
     * Checks that the two arrays contain the same attribute names. If they
     * do then null is returned. If they do not, a message is generated and
     * returned detailing the mismatches.
     *
     * @param expected
     * @param actual
     * @return a message detailing mismatches or null if there are none
     * @todo later This method looks rather general. Would it not be better
     * placed somewhere more general?
     */
    static public String findMismatches(String[] expected,
                                        String[] actual) {
        String result = null;
        Set expectedSet = new HashSet(expected.length);
        Set actualSet = new HashSet(actual.length);

        populate(expectedSet, expected);
        populate(actualSet, actual);

        if (!expectedSet.equals(actualSet)) {
            String expectedsMissing = null;
            String actualsMissing = null;

            for (int i = 0;
                 i < expected.length;
                 i++) {
                if (!actualSet.contains(expected[i])) {
                    if (expectedsMissing == null) {
                        expectedsMissing = expected[i];
                    } else {
                        expectedsMissing += ", " + expected[i];
                    }
                }
            }

            for (int i = 0;
                 i < actual.length;
                 i++) {
                if (!expectedSet.contains(actual[i])) {
                    if (actualsMissing == null) {
                        actualsMissing = actual[i];
                    } else {
                        actualsMissing += ", " + actual[i];
                    }
                }
            }

            if (expectedsMissing != null) {
                result = "expected to find \"" + expectedsMissing + "\" but " +
                    "did not";
            }

            if (actualsMissing != null) {
                if (result != null) {
                    result += "; ";
                } else {
                    result = "";
                }

                result += "found unexpected \"" + actualsMissing + "\"";
            }
        }

        return result;
    }

    /**
     * Populates the given set from the given array of values.
     *
     * @param set
     * @param values
     * @todo This method looks even more general. In addition the thing
     * it does could be done by using Set.addAll(Arrays.toList()).
     */
    static public void populate(Set set,
                                String[] values) {
        for (int i = 0;
             i < values.length;
             i++) {
            set.add(values[i]);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
