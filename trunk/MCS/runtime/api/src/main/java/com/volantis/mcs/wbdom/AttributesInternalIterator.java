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
package com.volantis.mcs.wbdom;

/**
 * An Internal Iterator interface for the instances of {@link WBDOMAttribute} 
 * belonging to an instance of {@link WBDOMElement}.
 * <p>
 * Internal Iterators <a href="http://www.c2.com/cgi/wiki?InternalIterator">
 * are defined here</a>
 * 
 * @see WBDOMElement#forEachAttribute
 */ 
public interface AttributesInternalIterator {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * This method will be called once, before any attributes are processed.
     * <p>
     * It will be called even if there are zero attributes in the element.
     * 
     * @throws WBDOMException
     */ 
    void before() throws WBDOMException;
    
    /**
     * This method will be called once for each attribute of the element.
     * 
     * @param attribute the attribute to process.
     * @throws WBDOMException if there was a problem processing the attribute.
     */ 
    void next(WBDOMAttribute attribute) throws WBDOMException;
    
    /**
     * This method will be called once, after any attributes are processed.
     * <p>
     * It will be called even if there are zero attributes in the element.
     * 
     * @throws WBDOMException
     */ 
    void after() throws WBDOMException;
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
