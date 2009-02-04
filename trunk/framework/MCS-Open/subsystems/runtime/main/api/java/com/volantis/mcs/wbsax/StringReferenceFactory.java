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
 * 18-May-03    Geoff           VBM:2003042904 - Created; a string factory for 
 *                              "serialising" clients. 
 * 20-May-03    Geoff           VBM:2003052102 - Add codec to constructor, 
 *                              rename InlineString to WBSAXString, add 
 *                              various char[] overloads.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * A factory which can take the various forms of string data (byte[], char[], 
 * String), create instances of {@link StringReference} for them, and add
 * them as instances of {@link WBSAXString} into a {@link StringTable}.
 * <p>
 * This forms a fairly thin layer on top of {@link StringTable}, which 
 * implements the creation of the references from {@link WBSAXString}s and 
 * the addition of {@link WBSAXString}s to its table data simutaneously, and
 * {@link StringFactory}, which translates between {@link WBSAXString} and the
 * various forms of string data. 
 * <p>
 * It might be nice to split this in half, one for logical and one for 
 * physical access. That is, one suitable for clients creating references from
 * high level data such as a DOM, and one suitable for clients creating
 * references from low level data such as a WBXML byte stream. This would make
 * it easier for the client to understand which methods were suitable given
 * the use they were making of the factory.
 */ 
public class StringReferenceFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The factory used by this factory to create WBSAX strings from the 
     * various forms of string data to be referenced.
     */ 
    private StringFactory strings;
    
    /**
     * The string table used by this factory to create and manage string 
     * references.
     */ 
    protected StringTable table;

    /**
     * Construct an instance of this class, using the string table provided.
     * 
     * @param table the string table to use for creating references, 
     *      may be null if you promise never to attempt to create one :-).
     * @param strings the factory to use for creating strings.
     */ 
    public StringReferenceFactory(StringTable table, StringFactory strings) {
        this.table = table;
        this.strings = strings;
    }

    /**
     * Returns the string table that this instance is using.
     */
    StringTable getStringTable() {
        return table;
    }

    /**
     * Returns the string factory that this instance is using.
     */ 
    public StringFactory getStringFactory() {
        return strings;
    }
    
    
    //
    // Logical Creation.
    //
    
    /**
     * Create a string reference from a String, and add it to the string table.
     * 
     * @param string the character content of the string. 
     * @return the string reference created. 
     */ 
    public StringReference createReference(String string) {
        return table.createReference(strings.create(string));
    }

    /**
     * Create a string reference from a char array, and add it to the string 
     * table.
     * 
     * @param chars the character content of the string. 
     * @return the string reference created. 
     */ 
    public StringReference createReference(char[] chars) {
        return table.createReference(strings.create(chars));
    }

    /**
     * Create an string reference from a portion of a char array, and add it 
     * to the string table.
     * 
     * @param chars the character content of the string. 
     * @param offset the offset of the string data within chars. 
     * @param length the length of the string data within chars. 
     * @return the string reference created. 
     */ 
    public StringReference createReference(char[] chars, int offset, 
            int length) {
        return table.createReference(strings.create(chars, offset, length));
    }

    /**
     * Create a string reference from a WBSAX String, and add it to the string 
     * table.
     * 
     * @param string the character content of the string. 
     * @return the string reference created. 
     */ 
    public StringReference createReference(WBSAXString string) {
        return table.createReference(string);
    }

    /**
     * Create a string reference from an array of bytes, and add it to the 
     * string table.
     * 
     * @param bytes the byte content of the string, must include the 
     *      termination character.
     * @return a StringReference
     */ 
    public StringReference createReference(byte[] bytes) {
        return table.createReference(strings.createFromBytes(bytes));
    }
    
    
    //
    // Logical retrieval.
    //

    /**
     * Returns a reference to a string that was previously added to the string 
     * table, or null if the string is not present in the string table.
     * 
     * @param string the string to search for.
     * @return a string reference to the string matched or null if not found. 
     */ 
    public StringReference getReference(String string) {
        return table.getReference(strings.create(string));
    }
    
    /**
     * Returns a reference to a string that was previously added to the string 
     * table, given the logical offset of the string in the table.
     * <p>
     * <b>NOTE:</b>
     * <p>
     * This is currently required only by the dissector. I am not convinced it 
     * should be used, but it was too late to change the dissector by the time 
     * I realised that it was required. 
     * <p>
     * The dissector only needs this because it assumes string reference's 
     * canonical form is an int. WBSAX does not assume this, as it is not type 
     * safe. Requiring this to be the case will slow things down (and result 
     * in more garbage creation as we translate back and forward from int to 
     * string references. A better designed dissector would remove this 
     * limitation by allowing string references to be Objects rather than
     * ints. 
     * <p>
     * In the meantime, avoid using this method where possible.
     *  
     * @param index the logical index of a string in the table.
     * @return a reference to the string at the index provided.
     */ 
    public StringReference createReference(int index) {
        return new StringReference(index, table);
    }

    
    //
    // Physical Creation.
    //

    /**
     * Create a string reference from a multi-byte integer.
     * 
     * @param integer the multi byte integer which represents the offset
     *      of a string in the string table. 
     * @return the string reference created. 
     */ 
    public StringReference createReference(MultiByteInteger integer) {
        return table.createReference(integer);
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

 14-Jul-03	790/2	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
