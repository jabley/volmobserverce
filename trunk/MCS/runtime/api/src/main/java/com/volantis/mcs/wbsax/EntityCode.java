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
 * 15-May-03    Geoff           VBM:2003042904 - Created; represents a Unicode 
 *                              character entity in a WBSAX event stream.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a Unicode character entity ({@link GlobalToken#ENTITY}) in a 
 * WBSAX event stream.
 */ 
public class EntityCode extends MultiByteInteger 
        implements WBSAXValueVisitor.Acceptor {

    /**
     * WBXML supports UCS4, but our unicode support is currently pretty lax.
     * At best, we support what can fit into a single Java char. So values 
     * larger than 16 bits are rejected here. If we ever support UCS4/UTF16 
     * properly (which means utilising multi-char "characters" within Java), 
     * then we can relax this.
     */ 
    private static final int NUMBER_OF_UNICODE_CHARACTERS = 65536;
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Create an instance of this class, using the Unicode character entity 
     * value provided.
     *  
     * @param value a Unicode character entity value.
     */ 
    public EntityCode(int value) {
        setMaximum(NUMBER_OF_UNICODE_CHARACTERS); 
        setInteger(value);
    }

    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitEntity(this);
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
