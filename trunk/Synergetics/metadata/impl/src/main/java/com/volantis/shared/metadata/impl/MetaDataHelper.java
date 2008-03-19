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

package com.volantis.shared.metadata.impl;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.Inhibitor;
import com.volantis.shared.metadata.MetaDataObject;

/**
 * Helper method for {@link MetaDataObject}s.
 */
public class MetaDataHelper {

    /**
     * Returns an immutable version of the specified object, if it is not null.
     * @param object The object.
     * @return An immutable version of the specified object, or null if the specified
     *         object is null.
     */
    public static ImmutableInhibitor getImmutableOrNull(Inhibitor object) {
        return (object == null) ? null : object.createImmutable();
    }

    /**
     * Helper method which determines if two objects are equal. If both object are null
     * then they are said to be equal. Otherwise equality is determines by using the
     * the objects equals method.
     * @param o1 The first object.
     * @param o2 The second object.
     * @return true if both objects are equal, or both objects are null, false otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    /**
     * Helper method to get a hash code for a specified object.
     * @param o The object
     * @return the hash code of the object if it is not null, 0 otherwise.
     */
    public static int hashCode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    /**
     * Helper method to get a hash code for a specified boolean.
     * @param b The boolean
     * @return 1 if the boolean is true, 0 otherwise.
     */
    public static int hashCode(boolean b) {
        return b ? 7 : 3;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6560/7	tom	VBM:2004122401 Changed Javadoc

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 13-Jan-05	6560/3	tom	VBM:2004122401 More Metadata API implementation

 10-Jan-05	6560/1	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
