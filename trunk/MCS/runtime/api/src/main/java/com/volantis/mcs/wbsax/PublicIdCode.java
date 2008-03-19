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
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents a public 
 *                              identifier in a WBSAX event stream.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a DTD public identifier in a WBSAX event stream.
 * <p>
 * This is used in the {@link WBSAXContentHandler#startDocument} events. 
 */ 
public class PublicIdCode extends MultiByteInteger {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * A public id value which is defined to be invalid by the specification.
     */ 
    private static final int INVALID_INT = 0x00;

    /**
     * The name of the public id.
     */ 
    private String name;
    
    /**
     * The URL to the "canonical" dtd associated with this public id.
     */ 
    private String dtd;
    
    /**
     * Construct an instance of this class, using the coded value, name and 
     * dtd url provided. 
     * <p>
     * This is package protected as clients will construct instances of this
     * class via a factory.
     *  
     * @param value the coded value for the public id.
     * @param name the name of the public id.
     * @param dtd the canonical url for this public id. 
     */ 
    public PublicIdCode(int value, String name, String dtd) {
        if (value == INVALID_INT) {
            throw new IllegalArgumentException("Public Id " + value + 
                    " is invalid (reserved)" );
        }
        setInteger(value);
        this.name = name;
        this.dtd = dtd;
    }

    /**
     * Returns the name of this public id. 
     * <p>
     * Will return null if this is the "unknown" code.
     * @return the name
     */  
    public String getName() {
        return name;
    }

    /**
     * Return the canonical URL for the DTD associated with this public id.
     *  
     * Will return null if this is the "unknown" code.
     * @return the dtd file name
     */ 
    public String getDtd() {
        return dtd;
    }

    // Inherit javadoc.
    public String toString() {
        return "[PublicIdCode:name=" + name + "," + super.toString() + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
