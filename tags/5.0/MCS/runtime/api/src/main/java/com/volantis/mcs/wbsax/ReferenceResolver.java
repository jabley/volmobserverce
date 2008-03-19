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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

/**
 * An interface which controls the way that string references are resolved 
 * during filtering (e.g. with {@link WBSAXFilterHandler}.
 * <p>
 * Currently this only supports resolving string references to themselves
 * or to a new string reference in the output string table. It may be nice to
 * support resolving string references to inline strings as well, but the
 * design for that would be a lot more complex.
 */ 
public interface ReferenceResolver {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Resolve the input string table, returning the string table to use
     * for output.
     * <p>
     * This method must be called exactly once, in the 
     * {@link WBSAXContentHandler#startDocument} event, before any calls are 
     * made to {@link #resolve(StringReference)}. 
     * 
     * @return the resolved string table, which may or may not be the same as
     *      the string table supplied.  
     */ 
    StringTable resolve(StringTable table);

    /**
     * Resolve a reference, adding it to the output string table if neccessary.
     * 
     * @param reference the reference to resolve.
     * @return the resolved reference, which may or may not be the same as the
     *      reference supplied.
     */ 
    StringReference resolve(StringReference reference);

    /**
     * Mark the underlying string table as completed, if the reference
     * resolver created one.
     * <p>
     * This method must be called exactly once, in the 
     * {@link WBSAXContentHandler#endDocument} event, after any calls are 
     * made to {@link #resolve(StringReference)}. 
     */
    void markComplete();
    
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

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
