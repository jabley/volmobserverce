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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created.
 * 02-Jun-03    Geoff           VBM:2003042906 - Make normal and pathological
 *                              URLs use the same form.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection;

import com.volantis.mcs.dissection.impl.DissectionHelper;
import com.volantis.mcs.utilities.MarinerURL;

import java.util.Map;
import java.util.TreeMap;

public class MyDissectionURLManager
    implements DissectionURLManager {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private Map map;

    private int count;

    private static class PairOfInts {
        public final int next;
        public final int previous;

        public PairOfInts(int next, int previous) {
            this.next = next;
            this.previous = previous;
        }
    }

    public MyDissectionURLManager() {
        map = new TreeMap(DissectionHelper.DISSECTABLE_AREA_IDENTITY_COMPARATOR);
    }

    public void addChangeIndexes(DissectableAreaIdentity identity,
                                 int nextChangeIndex,
                                 int previousChangeIndex) {
        map.put(identity, new PairOfInts(nextChangeIndex,
                                         previousChangeIndex));
    }

    public int getNextChangeIndex(DissectableAreaIdentity identity) {
        PairOfInts p = (PairOfInts) map.get(identity);
        if (p == null) {
            return count++;
        } else {
            return p.next;
        }
    }

    public int getPreviousChangeIndex(DissectableAreaIdentity identity) {
        PairOfInts p = (PairOfInts) map.get(identity);
        if (p == null) {
            return count++;
        } else {
            return p.previous;
        }
    }

    public MarinerURL makeURL(DissectionContext dissectionContext,
                              MarinerURL documentURL, int changeIndex) {
        if (documentURL.isReadOnly()) {
            documentURL = new MarinerURL(documentURL);
        }

        documentURL.setParameterValue("vfrag", Integer.toString(changeIndex));

        return documentURL;
    }

    public MarinerURL makePathologicalURL(DissectionContext dissectionContext,
                                          MarinerURL documentURL)
        throws DissectionException {

        if (documentURL.isReadOnly()) {
            documentURL = new MarinerURL(documentURL);
        }

        documentURL.setParameterValue("vfrag", "999");

        return documentURL;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 ===========================================================================
*/
