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
 * A visitor for visiting the different subtypes of {@link WBDOMElement} and 
 * {@link WBDOMAttribute}, in order to access their name information.
 * <p>
 * Name information may, in general, can be stored as a code or a "literal" 
 * (string reference). Both elements and attributes work this way, but the 
 * coded ones are a little bit more complex than the literal ones as each
 * code set is defined in a separate subclass of 
 * {@link com.volantis.mcs.wbsax.SingleByteInteger}. Thus, unfortunately, 
 * coded names are a somewhat "leaky abstraction" and we have to cast down to 
 * the required type for coded names to extract the name information from the 
 * code.
 * 
 * @see CodedNameElement
 * @see LiteralNameElement 
 * @see CodedStartAttribute
 * @see LiteralNameAttribute
 */ 
public interface NameVisitor {
    
    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * An "accept" interface for objects which may be visited by this visitor
     * (see GOF book).
     * <p>
     * The various element and attribute subtypes (and any proxies for them) 
     * must implement this interface so that they may be visited by a 
     * {@link NameVisitor}.
     */ 
    public interface Acceptor {
        void accept(NameVisitor visitor) throws WBDOMException;
    }
    
    /**
     * Visit a provider of coded name information.
     * 
     * @param code
     * @throws WBDOMException
     */ 
    void visitCodeProvider(CodedNameProvider code) throws WBDOMException;
    
    /**
     * Visit a provider of literal name information.
     *  
     * @param literal
     * @throws WBDOMException
     */ 
    void visitLiteralProvider(LiteralNameProvider literal) throws WBDOMException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/2	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
