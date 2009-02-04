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
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.annotation.TextAnnotation;
import com.volantis.mcs.dissection.links.ShardLinkDetails;

/**
 * Defines events for streaming the contents of a DissectedDocument.
 * <p>
 * Equivalent to the SAX {@link org.xml.sax.ContentHandler}.
 * <p>
 * Most implementations of this will need to save away a reference to the
 * {@link DissectableDocument} passed in to the {@link #startDocument} method
 * in order to access the contents of the document nodes.
 */
public interface DissectedContentHandler {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Start of the document.
     * <p>
     * This method is called before any other methods on this interface.
     * @param document The underlying dissectable document.
     * @param usages The set of shared content that is used within the document.
     * @throws DissectionException If there was an error handling this event.
     */
    void startDocument(DissectableDocument document,
            SharedContentUsages usages)
            throws DissectionException;

    /**
     * End of the document.
     * <p>
     * This method is the last one called.
     * @throws DissectionException If there was an error handling this event.
     */
    void endDocument()
            throws DissectionException;

    /**
     * Empty element
     * <p>
     * This is called for an element that has no children as it may be
     * necessary to treat them differently to elements that have children.
     * Without this it would be necessary for implementations to buffer up
     * an element start event until they could tell whether it had some content
     * or not. This would be awkward to do and is unnecessary as the dissected
     * document already knows whether an element is empty or not.
     * @param element The empty element.
     * @throws DissectionException If there was an error handling this event.
     */
    void emptyElement(DissectableElement element)
            throws DissectionException;

    /**
     * Start a non empty element.
     * @param element The element.
     * @throws DissectionException If there was an error handling this event.
     */
    void startElement(DissectableElement element)
            throws DissectionException;

    /**
     * End a non empty element.
     * @param element The element.
     * @throws DissectionException If there was an error handling this event.
     */
    void endElement(DissectableElement element)
            throws DissectionException;

    /**
     * A shard link element.
     * <p>
     * A shard link element contains information that will allow the reader of
     * a dissected page to navigate between the different shards in a
     * dissectable area. The URL that is asso
     * <p>
     * There are a number of aspects of the shard link element contents that
     * are dependent on information provided by the dissected document. The most
     * important (and currently the only one that is used) is the fragmentation
     * specifier.
     * <p>
     * The fragmentation specifier encapsulates all the information needed to
     * select the correct shards in the page that is generated when the link is
     * used by the reader. The shard link contents must be updated with this
     * information otherwise the output document will be invalid.
     * @param element The shard link element.
     * @param details The information necessary to update the shard link and
     * customise it for the selected shard.
     */
    void shardLink(DissectableElement element, ShardLinkDetails details)
            throws DissectionException;

    void text(DissectableText text)
            throws DissectionException;

    void text(DissectableText text, StringSegment segment)
            throws DissectionException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Jun-03	521/3	geoff	VBM:2003061005 mimas version of original metis changes

 24-Jun-03	365/1	geoff	VBM:2003061005 first go at long string dissection; still needs cleanup and more testing.

 ===========================================================================
*/
