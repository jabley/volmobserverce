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

package com.volantis.mcs.dissection;

import com.volantis.synergetics.ObjectHelper;

/**
 * This defines the identity of a dissectable area.
 */
public final class DissectableAreaIdentity {

    private String inclusionPath;

    private String name;

    public DissectableAreaIdentity(String inclusionPath, String name) {
        this.inclusionPath = inclusionPath;

        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        this.name = name;
    }

    public String getInclusionPath() {
        return inclusionPath;
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DissectableAreaIdentity)) {
            return false;
        }

        final DissectableAreaIdentity identity = (DissectableAreaIdentity) o;

        return (!ObjectHelper.equals(inclusionPath, identity.inclusionPath)
            && name.equals(identity.name));
    }

    public int hashCode() {
        int result;
        result = (inclusionPath != null ? inclusionPath.hashCode() : 0);
        result = 29 * result + name.hashCode();
        return result;
    }
    
    // Javadoc inherited
    public String toString() {
        return "[DissectableAreaIdentity:inclusionPath=" + inclusionPath +
                ",name=" + name + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Apr-04	4048/1	geoff	VBM:2004042606 Enhance Menu Support: WML Dissection

 ===========================================================================
*/
