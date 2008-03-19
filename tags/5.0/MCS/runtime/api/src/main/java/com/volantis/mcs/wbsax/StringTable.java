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
 * 18-May-03    Geoff           VBM:2003042904 - Created; represents the 
 *                              string table in a WBSAX event stream. 
 * 20-May-03    Geoff           VBM:2003052102 - Extract codec stuff and move 
 *                              into a new class Codec, rename InlineString to 
 *                              WBSAXString.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.util.ArrayList;

/**
 * Represents the string table in a WBSAX event stream.
 * <p>
 * The string table stores a set of strings who's individual values and/or 
 * offsets may be evaluated indirectly using {@link StringReference}s, or 
 * who's summed binary content may be accessed directly. 
 * <p>
 * The string table is generally a pain, because it's position in the WBSAX 
 * stream before the body of the document means that either the input side or 
 * the output side of WBSAX needs to go to the effort of "caching" the string 
 * table information.
 * <p>
 * Most modification of the string table is done through the 
 * {@link StringReferenceFactory}, which allows it's client to create string
 * references and adds the strings to the string table at the same time. 
 * As a result, the methods of this class that are public are mostly the ones 
 * for inspecting the state of the string table, rather than modifying it.
 * <p>
 * The string table operates in two modes; incomplete and complete. If the 
 * string table is incomplete, then strings may be added to it, but the
 * final binary representation of the string table may not be obtained. 
 * Conversely, if the string table is complete, then strings may not be added
 * to it, but the final binary representation of the string table may be 
 * obtained. 
 * <p>
 * The string table <b>must</b> be marked complete by the input side of WBSAX 
 * at some stage during a WBSAX "run". It may be marked at one of two points; 
 * either before {@link WBSAXContentHandler#startDocument} is called, or 
 * before {@link WBSAXContentHandler#endDocument} is called.
 * <p>
 * In the former case, it means that the "caching" of the string table is the 
 * responsibility of the input side, as all the strings in the string table 
 * must be created before start document is called, and the output side can 
 * get the binary representation of the string table at the point it may need 
 * to write it.
 * <p>
 * In the latter case, it means that the "caching" of the string table is the
 * responsibility of the output side, as the strings in the string table are
 * created at the point they are needed, and the binary representation of the 
 * string table is not available until all the content events have been fired.
 */ 
public class StringTable {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The string table content as a list of Strings.
     * <p>
     * This may be null if this instance was created with an array of bytes 
     * and conversion has not yet been performed.
     */ 
    private ArrayList stringList;
    
    /**
     * The string table content as a array of bytes.
     * <p>
     * This may be null if this instance was created with indivudual Strings
     * and/or byte arrays and conversion has not yet been performed.
     */ 
    private byte[] table;
    
    /**
     * A list of matching offsets for the strings in the string list.
     * <p>
     * The <b>first</b> entry is the start position of the <b>second</b> 
     * string in the list! This means that the final entry is the current size 
     * of the byte array required to store the string list. 
     */ 
    private ArrayList offsetList;

    /**
     * The flag that indicates if this table is complete or not.
     */ 
    private boolean complete;

    /**
     * The physical length of the string table, in bytes. This is derived from 
     * the table, once it is complete, and then cached here.
     */ 
    private MultiByteInteger length;

    
    public StringTable() {
        
    }
    
    //
    // Input methods - to add content to the string table.
    //

    /**
     * Set the contents of the string table, as an array of bytes.
     * <p>
     * This must not include the encoded length of the string table. 
     * <p>
     * This marks the table as complete.
     * 
     * @param bytes the byte content of the complete string table.
     */ 
    public void setContent(byte [] bytes) {
        throw new UnsupportedOperationException(
                "binary input not implemented");
        // we could use the code below, but then only getContent would work
        // in general we need an expandTable() method and calls to it in
        // various places before we can deal with binary content.
//        if (complete) {
//            throw new IllegalStateException(
//                    "Attempt to add reference to completed table");
//        }
//        table = bytes;
//        complete = true;
    }

    /**
     * Returns a reference to a string that was previously added to the string 
     * table, or null if the string is not present in the string table.
     * <p>
     * This is package protected as it is designed to be called by the 
     * {@link StringReferenceFactory#getReference}.
     *  
     * @param string the string to search for.
     * @return a string reference to the string matched or null if not found. 
     */ 
    StringReference getReference(WBSAXString string) {
        if (stringList != null) {
            /* @todo Calling indexOf will be slow if we have a large number of 
             * Strings in the string table. At the time of writing the only
             * string in the string table will be a jsessionid
             */        
            int pos = stringList.indexOf(string);
            if (pos != -1)  {
                return new StringReference(pos, this);
            }        
        }
        
        return null;
    }

    /**
     * Create a string reference from an inline string.
     * <p>
     * This is package protected as it is designed to be called by the 
     * {@link StringReferenceFactory#createReference}.
     * 
     * @param string the inline string to create a reference to.
     * @return the created string reference.
     */ 
    StringReference createReference(WBSAXString string) {
        if (complete) {
            throw new IllegalStateException(
                    "Attempt to add reference to completed table");
        }
        complete = false;
        
        StringReference toReturn = null;
        if (stringList != null) {
            toReturn = getReference(string);
        } else {
            this.stringList = new ArrayList();
        }
        
        if (toReturn == null)  {
            stringList.add(string);
            // Create string reference with a logical offset.
            return new StringReference(stringList.size() - 1, this);
        } else {
            return toReturn;
        }        
    }

    /**
     * Create a string reference from a byte offset of a string in the binary 
     * representation of the string table.
     * <p>
     * This is package protected as it is designed to be called by the 
     * {@link StringReferenceFactory#createReference}.
     * 
     * @param physicalIndex the byte offset of a string in the binary 
     *      representation of the string table to create a reference to.
     * @return the created string reference.
     */ 
    StringReference createReference(MultiByteInteger physicalIndex) {
        return new StringReference(physicalIndex, this); 
    }
    
    //
    // Output methods - to retrieve content from the string table.
    //

    /**
     * Return the contents of the string table, as an array of bytes. 
     * <p>
     * This does not include the encoded length of the string table. 
     * 
     * @return the content as a byte array
     */ 
    public byte [] getContent() throws WBSAXException {
        // Make sure the physical representation of the table is available.
        compressTable();
        // And return it.
        return table;
    }

    /**
     * Calculates the logical index of an (inline) string in the string table, 
     * and returns it as an integer.
     * <p>
     * This is package protected as it is designed to be called by 
     * {@link StringReference#resolveLogicalIndex}.
     *  
     * @return the logical index of the string in the string table.
     */ 
    int resolveLogicalIndex(MultiByteInteger physicalIndex) {
        // Convert physical to logical
        throw new UnsupportedOperationException(
                "binary input not implemented");
    }
    
    /**
     * Calculates the inline string which is present at the logical index
     * provided.
     * <p>
     * This is package protected as it is designed to be called by 
     * {@link StringReference#resolveString}.
     * 
     * @return the inline string containing the string value.
     */ 
    WBSAXString resolveString(int logicalIndex) {
        // NOTE: conversion would be required here if the string table data
        // was provided via setContent().

        // Return the logical representation of the string.
        return (WBSAXString) stringList.get(logicalIndex);
    }
    
    /**
     * Calculates the physical index of the string data in the string table
     * using the logical index of the string provided, and returns it as a 
     * multi-byte integer.
     * <p>
     * This is package protected as it is designed to be called by 
     * {@link StringReference#resolvePhysicalIndex}.
     * <p>
     * Note that this will perform translation from whatever format the 
     * string was provided in, if necessary.
     *  
     * @return the position of the string in the string table.
     */ 
    MultiByteInteger resolvePhysicalIndex(int logicalOffset) 
            throws WBSAXException {
        // NOTE: conversion would be required here if the string table data
        // was provided via setContent().

        if (stringList == null) {
            throw new IllegalStateException("Attempt to resolve logical " + 
                    "offset " + logicalOffset + " into a physical offset " +
                    "when the string table is empty.");
        }
            
        // First make sure the offsetList is up to date.
        int stringSize = stringList.size();
        // Create the offsets list if this was the first call.
        if (offsetList == null) {
            offsetList = new ArrayList(stringSize);
        }
        // Loop over the part of the strings list that we haven't already
        // converted, calculating the offset that each string would
        // start at in the string table. This will have the side effect
        // of resolving the bytes of each String if not already present.
        int offsetSize = offsetList.size();
        int offset = getPhysicalIndex(offsetSize);
        for (int i = offsetSize; i < stringSize; i++) {
            WBSAXString inlineString = (WBSAXString) stringList.get(i);
            offset += inlineString.getBytes().length;
            offsetList.add(new Integer(offset));
        }
        
        // Then use the offsetList to calculate the byte offset for this 
        // logical offset.
        // TODO: implement factory/flyweights for multi byte integers.
        return new MultiByteInteger(getPhysicalIndex(logicalOffset));
    }

    /**
     * Returns the physical index of a string stored in the logical table.
     * <p>
     * Nicely handles the fact that the offsetList stores indexes offset by
     * one.
     * 
     * @param logicalIndex the logical index of a string
     * @return the physical index of the string in the binary representation 
     *      of the string table.
     */ 
    private int getPhysicalIndex(int logicalIndex) {
        // NOTE: conversion would be required here if the string table data
        // was provided via setContent().

        if (logicalIndex == 0) {
            return 0;
        } else {
            return ((Integer)offsetList.get(logicalIndex-1)).intValue();
        }
    }
    
    /**
     * Compress the logical representation of the table into a physical
     * representation of the table, if there was one.
     */ 
    private void compressTable() throws WBSAXException {
        if (!complete) {
            throw new IllegalStateException(
                    "Attempt to generate physical representation of " + 
                    "incomplete string table");
        }

        // If there is nothing to compress, or we already did the compression
        if (stringList == null || table != null) {
            // Then there is nothing to do, so just return.
            return;
        }
        
        // Loop over the strings list, calculating the length in bytes.
        // This will force the strings to be translated to bytes if they
        // weren't already.
        int length = 0;
        for (int i = 0; i < stringList.size(); i++) {
            WBSAXString inlineString = (WBSAXString) stringList.get(i);
            length += inlineString.getBytes().length;
        }

        // Create the compressed table with the length calculated.
        // In future we will need to expand it dynamically. 
        table = new byte[length];

        // Loop over the expanded table, transferring the byte content of each
        // string into the compressed table.
        int position = 0;
        for (int i = 0; i < stringList.size(); i++) {
            WBSAXString inlineString = (WBSAXString) stringList.get(i);
            byte[] bytes = inlineString.getBytes();
            System.arraycopy(bytes, 0, table, position, bytes.length);
            position += bytes.length;
        }
    }

    //
    // Control methods
    //

    /**
     * Marks this string table complete.
     * <p>
     * This is called implicitly by {@link #setContent}.
     */ 
    public void markComplete() {
        complete = true;
    }

    /**
     * Returns true if this string table has been marked complete.
     * 
     * @return true if this string table has been marked complete.
     */ 
    public boolean isComplete() {
        return complete;
    }

    /**
     * Returns the logical size of the string table, i.e. the number of 
     * strings in the table.
     * 
     * @return the size of the string table
     */ 
    public int size() {
        if (!complete) {
            throw new IllegalStateException(
                    "Attempt to get size of incomplete string table");
        }

        // NOTE: conversion would be required here if the string table data
        // was provided via setContent().

        if (stringList == null) {
            return 0;
        } else {
            return stringList.size();
        }
    }

    /**
     * Returns the physical size of the string table, i.e. the number of 
     * bytes in the table, as a multi byte integer.
     * 
     * @return a MultiByteInteger
     */ 
    public MultiByteInteger length() throws WBSAXException {
        if (length == null) {
            byte[] content = getContent();
            int intLength;
            if (content == null || content.length == 0) {
                intLength = 0;
            } else {
                intLength = content.length;
            }
            length = new MultiByteInteger(intLength);
        }
        
        return length;
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

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/4	geoff	VBM:2003070404 clean up WBSAX

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 13-Jun-03	372/3	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
