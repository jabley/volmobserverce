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

/**
 * This interface defines the methods that are needed to access the dissector's
 * DOM.
 */
public interface DocumentVisitor {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Called when visiting the document.
     * @param document The document that is being visited.
     */
    public void visitDocument(DissectableDocument document)
        throws DissectionException;

    /**
     * Called when visiting an element node.
     * @param element The element node.
     */
    public void visitElement(DissectableElement element)
        throws DissectionException;

    /**
     * Called when visiting an text node.
     * @param text The text node.
     */
    public void visitText(DissectableText text)
        throws DissectionException;

    /**
     * Called when visiting a shard link element.
     * @param element The element node.
     */
    public void visitShardLink(DissectableElement element)
        throws DissectionException;

    /**
     * Called when visiting a shard link group element.
     * @param element The element node.
     */
    public void visitShardLinkGroup(DissectableElement element)
        throws DissectionException;

    /**
     * Called when visiting a shard link conditional element.
     * @param element The element node.
     */
    public void visitShardLinkConditional(DissectableElement element)
        throws DissectionException;

    /**
     * Called when visiting a dissectable area element.
     * @param element The element node.
     */
    public void visitDissectableArea(DissectableElement element)
        throws DissectionException;

    /**
     * Called when visiting a keep together element.
     * @param element The element node.
     */
    public void visitKeepTogether(DissectableElement element)
        throws DissectionException;
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	363/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
