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
 * 15-May-03    Geoff           VBM:2003042904 - Created; an enumeration to 
 *                              represent an extension's numeric id in a WBSAX 
 *                              event stream. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * An enumeration to represent an extension's numeric id in a WBSAX event 
 * stream.
 * <p>
 * This is implemented according to the "Typesafe Enum" pattern. 
 */ 
public class Extension {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The numeric id of the extension, can be 0, 1, or 2. 
     */ 
    private int id;

    /**
     * Constructs an instance of this class using the id provided.
     * <p>
     * Private to prevent others constructing any instances.
     * 
     * @param id the numeric id of the extension (0, 1 or 2).
     */ 
    private Extension(int id) {
        this.id = id;
    }

    /**
     * Extension id '0'.
     */ 
    public static Extension ZERO = new Extension(0);
    
    /**
     * Extension id '1'.
     */ 
    public static Extension ONE = new Extension(1);
    
    /**
     * Extension id '2'.
     */ 
    public static Extension TWO = new Extension(2);

    /**
     * Returns the integer value of the numeric id of this extension. Useful 
     * for using with switch(). 
     * 
     * @return the integer value of the numeric id of this extension.
     */ 
    public int intValue() {
        return id;
    }

    // Inherit Javadoc.
    public String toString() {
        return "[Extension:id=" + id + "]";
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
