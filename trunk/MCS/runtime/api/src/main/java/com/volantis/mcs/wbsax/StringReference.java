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
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents a 
 *                              reference to a string which is stored in the 
 *                              string table, in a WBSAX event stream. 
 * 20-May-03    Geoff           VBM:2003052102 - rename InlineString to 
 *                              WBSAXString.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * Represents a reference to a string which is stored in the string table, in 
 * a WBSAX event stream.
 * <p>
 * This will translate the string data provided into whatever format is 
 * required when used to resolving values, using lazy evaluation as far as is
 * practicable. 
 * <p>
 * Instances of this class must be created using a 
 * {@link StringReferenceFactory}.
 */ 
public class StringReference implements WBSAXValueVisitor.Acceptor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The string table that contains the value referenced by this object.
     */ 
    private StringTable stringTable;

    /**
     * The physical index of the string value in the string table.
     * <p>
     * This is the byte offset of the bytes that make up the string from the 
     * beginning of the string table. 
     * <p>
     * This may be null if this instance was created with a logical index and 
     * conversion has not yet been performed.
     */ 
    private MultiByteInteger physicalIndex;
    
    /**
     * The logical index of the string value in the string table. 
     * <p>
     * This is the index of the string as stored in a string array. 
     * <p>
     * This may be null if this instance was created with a physical index 
     * and conversion has not yet been performed.
     */ 
    private int logicalIndex = -1;
    
    /**
     * Construct an instance of this class with the physical index and 
     * string table provided. 
     * <p>
     * This is package protected as clients will construct instances of this
     * class via a factory.
     * 
     * @param physicalIndex the byte offset of the bytes that make up the 
     * string from the beginning of the string table.
     * @param stringTable the table that the string data is stored in.
     */ 
    StringReference(MultiByteInteger physicalIndex, 
            StringTable stringTable) {
        this.stringTable = stringTable;
        this.physicalIndex = physicalIndex;
    }

    /**
     * Construct an instance of this class with the logical index and 
     * string table provided. 
     * <p>
     * This is package protected as clients will construct instances of this
     * class via a factory.
     * 
     * @param logicalIndex the index of the string as stored in a string 
     *      array.
     * @param stringTable the table that the string data is stored in.
     */ 
    StringReference(int logicalIndex, StringTable stringTable) {
        this.stringTable = stringTable;
        this.logicalIndex = logicalIndex;
    }

    /**
     * Calculates the value which is pointed to by this string reference, and 
     * returns it as a WBSAX string.
     * <p>
     * Note that this will perform translation from whatever format the 
     * string was provided in, if necessary.
     * 
     * @return the inline string containing the string value.
     */ 
    public WBSAXString resolveString() {
        int logicalIndex = resolveLogicalIndex();
        return stringTable.resolveString(logicalIndex);
    }
    
    
    /**
     * Calculates the logical index of an (inline) string in the string table, 
     * and returns it as an integer.
     * <p>
     * Note that this will perform translation from whatever format the 
     * string was provided in, if necessary. In the worst case, this may mean 
     * extraction of an indivudual byte array from a binary string table.
     * <p>
     * Note: this was made public because the dissector assumes that the
     * canonical form of a string reference is a int which is the logical
     * index. I'm not sure this is a good assumption, it may be that it would
     * be better if the dissector could handle string references as Objects
     * instead. For example, we could avoid some object creation in this case.
     *  
     * @return the logical index of the string in the string table.
     */ 
    public int resolveLogicalIndex() {
        if (logicalIndex >= 0) {
            return logicalIndex;
        } else if (physicalIndex != null) {
            logicalIndex = stringTable.resolveLogicalIndex(physicalIndex);
            return logicalIndex;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Calculates the physical index of the string data in the string table 
     * which is pointed to by this string reference, and returns it as a 
     * multi-byte integer.
     * <p>
     * Note that this will perform translation from whatever format the 
     * string was provided in, if necessary.
     *  
     * @return the position of the string in the string table.
     */ 
    public MultiByteInteger resolvePhysicalIndex() throws WBSAXException {
        if (physicalIndex != null) {
            return physicalIndex;
        } else if (logicalIndex >= 0) {
            physicalIndex = stringTable.resolvePhysicalIndex(logicalIndex);
            return physicalIndex;
        } else {
            throw new IllegalStateException();
        }
    }
    
    // Javadoc inherited.
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        visitor.visitReference(this);
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

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 13-Jun-03	372/2	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
