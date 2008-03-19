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
package com.volantis.mcs.wbsax;

/**
 * A visitor for WBSAX value objects.
 * <p>
 * This is useful for defining visitors which may visit over any type of 
 * WBSAX value object; the initial example was while serialising a set of
 * value objects held in the WBDOM.
 * <p>
 * This is kind of dodgy since extensions values are composed of an 
 * {@link Extension} and an optional {@link WBSAXString} or 
 * {@link StringReference}, and thus there is no place to define the accept
 * method for these values. In this case the client must currently define
 * their own {@link Acceptor} for these values.
 * <p>
 * In future we may consider changing the design to have separate individual
 * objects for each extension value defined within WBSAX but that was 
 * considered to be too big a change to be done at this point. 
 */ 
public interface WBSAXValueVisitor {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * An "accept" interface for WBSAX values (see GOF book).
     * <p>
     * WBSAX value objects (and proxies for them) must implement this 
     * interface so that they may be visited by a {@link WBSAXValueVisitor}.
     */ 
    public interface Acceptor {
        void accept(WBSAXValueVisitor visitor) throws WBSAXException;
    }

    void visitString(WBSAXString string) throws WBSAXException;
    
    void visitReference(StringReference reference) throws WBSAXException;
    
    void visitEntity(EntityCode entity) throws WBSAXException;
    
    void visitExtension(Extension extension) throws WBSAXException;
    
    void visitExtensionString(Extension extension, WBSAXString string)
            throws WBSAXException;

    void visitExtensionReference(Extension extension, StringReference string)
            throws WBSAXException;
    
    void visitOpaque(OpaqueValue opaque) throws WBSAXException;
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

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 ===========================================================================
*/
