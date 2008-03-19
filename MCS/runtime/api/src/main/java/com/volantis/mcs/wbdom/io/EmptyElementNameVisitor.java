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
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbdom.NameVisitor;
import com.volantis.mcs.wbdom.CodedNameProvider;
import com.volantis.mcs.wbdom.WBDOMException;
import com.volantis.mcs.wbdom.LiteralNameProvider;
import com.volantis.mcs.wbdom.EmptyElementType;
import com.volantis.mcs.wbdom.io.EmptyElementConfiguration;
import com.volantis.mcs.wbsax.WBSAXException;

/**
 * An implementation of {@link com.volantis.mcs.wbdom.NameVisitor} which 
 * extracts the empty element type of an element from it's name information. 
 */ 
public class EmptyElementNameVisitor implements NameVisitor {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The configuration of empty element types, contains info on which 
     * elements have which empty types.
     */ 
    private EmptyElementConfiguration emptyConf;
    
    /**
     * The empty element type to be calculated by the visitation.
     */ 
    private EmptyElementType emptyElementType;

    /**
     * Construct an instance of this class, with the configuration provided.
     * 
     * @param emptyConf provides info on which elements have which empty 
     *      element types.
     */ 
    public EmptyElementNameVisitor(EmptyElementConfiguration emptyConf) {
        this.emptyConf = emptyConf;
    }

    // Javadoc inherited.
    public void visitCodeProvider(CodedNameProvider code) throws WBDOMException {
        emptyElementType = emptyConf.getEmptyElementType(
                code.getCodedName().getInteger());
    }

    // Javadoc inherited.
    public void visitLiteralProvider(LiteralNameProvider literal) 
            throws WBDOMException {
        try {
            emptyElementType = emptyConf.getEmptyElementType(
                    literal.getLiteralName().resolveString().getString());
        } catch (WBSAXException e) {
            throw new WBDOMException(e);
        }
    }

    /**
     * Get the empty element type which was calculated by the visitation.
     * 
     * @return the empty element type.
     */ 
    public EmptyElementType getEmptyElementType() {
        return emptyElementType;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/6	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/3	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
