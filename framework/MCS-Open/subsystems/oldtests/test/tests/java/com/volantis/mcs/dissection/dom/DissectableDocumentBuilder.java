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
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import com.volantis.mcs.dom.NodeAnnotation;
import com.volantis.xml.sax.AnnotatedAttributes;

/**
 * Methods needed in order to construct a dissectable document from a text xml
 * document.
 */
public interface DissectableDocumentBuilder
    extends ContentHandler {

    /**
     * Get the DissectableDocument instance that was created by this builder.
     * @return The DissectableDocument instance.
     * @throws SAXException
     */
    public DissectableDocument getDissectableDocument ()
        throws SAXException;

    /**
     * Start an element which has a shared reference for a name.
     * 
     * @param index the index of the string reference to use
     * @param attributes the attributes of the element.
     */ 
    void startCommonElement(int index, AnnotatedAttributes attributes)
            throws SAXException;

    /**
     * Start of an element that has special meaning to the dissector.
     * @param type The ElementType as returned by one of the methods in
     * {@link DissectionElementTypes}.
     * @param annotation The extra information associated with the element.
     */
    public void startSpecialElement(ElementType type,
                                    NodeAnnotation annotation)
        throws SAXException;

    /**
     * End of an element that has special meaning to the dissector.
     */
    public void endSpecialElement()
        throws SAXException;

    /**
     * Add the specified string to the string table and return its index within
     * the string table.
     * <p>
     * This is only called once for each unique string.
     * @param string The string to add to the table.
     * @return The index of the string within the table.
     * @throws SAXException If there was a problem adding the string to the
     * table.
     */
    public int addStringTableEntry(String string)
        throws SAXException;

    /**
     * Add a reference to the specified shared string.
     * @param index The index of the shared string within the string table.
     * @throws SAXException If there was a problem adding the string reference.
     */
    public void addSharedStringReference(int index)
        throws SAXException;

    /**
     * Return an object that will be used to annotate an attribute whose value
     * is the shard link url.
     */
    public Object getShardLinkURLParameter ();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
