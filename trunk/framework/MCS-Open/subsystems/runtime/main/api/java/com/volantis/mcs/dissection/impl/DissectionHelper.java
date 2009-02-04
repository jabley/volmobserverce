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
 * 28-May-03    Paul            VBM:2003052901 - Created
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.DissectableAreaIdentity;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.shared.SharedContentUsagesImpl;
import com.volantis.synergetics.ObjectHelper;

import java.util.Comparator;

public class DissectionHelper {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public static final Comparator DISSECTABLE_AREA_IDENTITY_COMPARATOR
        = new DissectableAreaIdentityComparator();

    /**
     * Create a SharedContentUsages object that is suitable for tracking
     * usages of the shared content with the document.
     * @param document The document whose shared content will be tracked by
     * the returned SharedContentUsages.
     * @return A new SharedContentUsages, or null if the document does not
     * contain any shared content.
     */
    public static SharedContentUsages
        createSharedContentUsages(DissectableDocument document) {

        int count = document.getSharedContentCount();
        if (count == 0) {
            return null;
        }

        return new SharedContentUsagesImpl(count);
    }

    public static class DissectableAreaIdentityComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            DissectableAreaIdentity i1 = (DissectableAreaIdentity) o1;
            DissectableAreaIdentity i2 = (DissectableAreaIdentity) o2;

            int result;

            if ((result = i1.getName().compareTo(i2.getName())) != 0) {
                return result;
            }

            return ObjectHelper.compare(i1.getInclusionPath(),
                                        i2.getInclusionPath());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
