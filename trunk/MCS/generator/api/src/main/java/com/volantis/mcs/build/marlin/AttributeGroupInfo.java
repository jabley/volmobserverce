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
 * $Header: /src/voyager/com/volantis/mcs/build/marlin/AttributeGroupInfo.java,v 1.1 2002/05/16 08:42:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Changed PAPI references to API
 *                              so that other generators (eg IMDAPI) can use
 *                              the same names.
 * 16-May-02    Paul            VBM:2002032501 - Moved from the
 *                              com.volantis.mcs.build package.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.marlin;

import com.volantis.mcs.build.parser.AttributeGroupDefinition;
import com.volantis.mcs.build.parser.AttributesStructure;
import com.volantis.mcs.build.parser.Scope;

/**
 * This class adds extra information to the
 * <code>AttributeGroupDefinition</code> which is needed to automatically
 * generate code.
 */
public class AttributeGroupInfo
        extends AttributeGroupDefinition {

    /**
     * Create a new <code>AttributeGroupInfo</code>.
     *
     * @param scope The scope within which this object belongs.
     * @param name  The name of the attribute group.
     */
    public AttributeGroupInfo(Scope scope, String name) {
        super(name, scope);
    }

    /**
     * Get the extended attributes structure.
     *
     * @return The extended attributes structure.
     */
    public AttributesStructureInfo getAttributesStructureInfo() {
        return (AttributesStructureInfo) getAttributesStructure();
    }

    // Javadoc inherited from super class.
    protected AttributesStructure createAttributesStructure() {
        AttributesStructureInfo info = new AttributesStructureInfo(this);
        info.setAbstractAPIAttributesClass(true);
        return info;
    }

    /**
     * Set the name of the API attribute group class.
     *
     * @param apiElementClass The name of the API attribute group class.
     */
    public void setAPIElementClass(String apiElementClass) {
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
