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
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents an opaque value ({@link GlobalToken#OPAQUE}) in a WBSAX event 
 * stream.
 * <p>
 * The content of opaque values are, by their nature, undefined by WBSAX, 
 * apart from the fact that their binary representation is that of a 
 * "B"/"BCPL" string (for the Amiga geeks out there). That is, they have their 
 * length defined as the the first part of their value rather than being null 
 * terminated.
 */ 
public abstract class OpaqueValue implements WBSAXValueVisitor.Acceptor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Returns the content of this opaque value as bytes, including the length.
     * <p>
     * The exact meaning of the binary representation of an opaque value is
     * application defined.
     * 
     * @return the opaque value
     * @throws WBSAXException
     */ 
    public abstract byte[] getBytes() throws WBSAXException;

    /**
     * Returns the content of this opaque value as a String.
     * <p>
     * The exact meaning of the string representation of an opaque value is 
     * application defined.
     * 
     * @return the string value
     * @throws WBSAXException
     */ 
    public abstract String getString() throws WBSAXException;

    // Inherit javadoc.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitOpaque(this);
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

 02-Oct-03	1469/2	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
