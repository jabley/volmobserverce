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
package com.volantis.mcs.wbdom.dissection.io;

import com.volantis.mcs.wbdom.NameVisitor;
import com.volantis.mcs.wbdom.CodedNameProvider;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.LiteralNameProvider;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.Accumulator;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * An implementation of {@link NameVisitor} which calculates the size of  
 * an element or attribute's name information when rendered into a WBXML 
 * output stream.
 */ 
public class WBSAXNameSizer extends AbtractSizer implements NameVisitor {

    /**
     * Construct an instance of this class with the accumlator provided.
     * 
     * @param accumulator size information will be recored in this object.
     */ 
    public WBSAXNameSizer(Accumulator accumulator) {
        super(accumulator);
    }

    // Inherit javadoc.
    public void visitCodeProvider(CodedNameProvider code) 
            throws WBDOMException {
        try {
            // Cost the element name token.
            costToken();
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }

    // Inherit javadoc.
    public void visitLiteralProvider(LiteralNameProvider literal) 
            throws WBDOMException {
        try {
            // Cost the LITERAL* token and the string reference.
            costTokenAndReference(literal.getLiteralName());
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 15-Jul-03	804/2	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/8	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 12-Jun-03	368/2	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
