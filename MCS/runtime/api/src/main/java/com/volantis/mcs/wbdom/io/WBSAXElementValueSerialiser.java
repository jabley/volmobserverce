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
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbdom.io;

import com.volantis.mcs.wbsax.EntityCode;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.ReferenceResolver;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.WBSAXValueVisitor;

public class WBSAXElementValueSerialiser extends AbstractWBSAXSerialiser 
        implements WBSAXValueVisitor {
        
    /**
     * Construct an instance of this class, with the handler and resolver
     * provided.
     * 
     * @param handler the handler to serialise to.
     * @param resolver the reference resolver to use.
     */ 
    public WBSAXElementValueSerialiser(WBSAXContentHandler handler, 
            ReferenceResolver resolver) {
        super(handler, resolver);
    }

    public void visitString(WBSAXString string) throws WBSAXException {
        handler.addContentValue(string);
    }

    public void visitReference(StringReference reference) 
            throws WBSAXException {
        handler.addContentValue(resolver.resolve(reference));
    }

    public void visitEntity(EntityCode entity) throws WBSAXException {
        handler.addContentValueEntity(entity);
    }

    public void visitExtension(Extension extension) 
            throws WBSAXException {
        handler.addContentValueExtension(extension);
    }

    public void visitExtensionString(Extension extension, 
            WBSAXString string) throws WBSAXException {
        handler.addContentValueExtension(extension, string);
    }

    public void visitExtensionReference(Extension extension, 
            StringReference reference) throws WBSAXException {
        handler.addContentValueExtension(extension, 
                resolver.resolve(reference));
    }

    public void visitOpaque(OpaqueValue opaque) throws WBSAXException {
        handler.addContentValueOpaque(opaque);
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

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 ===========================================================================
*/
