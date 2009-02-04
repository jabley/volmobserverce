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
 * 29-May-03    Mat             VBM:2003042911 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.dissection;

import com.volantis.mcs.dissection.dom.ElementType;
import com.volantis.mcs.wbsax.OpaqueElementStart;
import com.volantis.mcs.dom.NodeAnnotation;

/**
 * An implementation of WBSAX's {@link OpaqueElementStart} which is used to 
 * pass the special dissection elements across the WBSAX event boundary when 
 * serialising the MCS DOM to create a WBDOM.
 */
public class SpecialOpaqueElementStart implements OpaqueElementStart {
    
    /** The element type */
    private ElementType type;
    
    /** The annotation for the element type */
    private NodeAnnotation annotation;

    /**
     * Constructor for the opaque element
     * 
     * @param type The element type 
     * @param annotation The annotations.
     */
    public SpecialOpaqueElementStart(ElementType type,
            NodeAnnotation annotation) {
        this.type = type;
        this.annotation = annotation;
    }

    // Javadoc inherited
    public byte[] getBytes() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Getter for the element name
     * @return The element name
     */
    public String getName() {
        return type.getDescription();
    }

    /**
     * Getter for the element type
     * @return The element type.
     */
    public ElementType getType() {
        return type;
    }

    /**
     * Getter for the annotation
     * @return The annotation.
     */
    public NodeAnnotation getAnnotation() {
        return annotation;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/6	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
