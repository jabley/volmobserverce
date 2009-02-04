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
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbsax.WBSAXValueVisitor;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.Extension;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.OpaqueValue;
import com.volantis.mcs.wbsax.EntityCode;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class to buffer "proper" WBSAX values (that is, classes which implement 
 * {@link com.volantis.mcs.wbsax.WBSAXValueVisitor.Acceptor}.
 * <p>
 * This class is used within the WBDOM for storing both attribute and content
 * values (although attribute values are in a subclass, 
 * {@link WBSAXAttributeValueBuffer}. 
 * <p>
 * This also doubles as a sort of Factory/Adaptor as it provides append 
 * methods which take in "simple" WBSAX value objects which do not implement 
 * the acceptor interface and wraps them in objects which do.
 * <p> 
 * That is, it works around the fact that not all WBSAX data events have a 
 * single event object which implements the accept interface. If WBSAX is 
 * improved in this regard in future, then this class would have a lesser role.
 * 
 * @see WBDOMText
 * @see WBDOMAttribute 
 */ 
public class WBSAXValueBuffer implements WBSAXValueVisitor.Acceptor {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private List contents;
    
    public WBSAXValueBuffer() {
        contents = new ArrayList();
    }

    protected void add(WBSAXValueVisitor.Acceptor acceptor) {
        contents.add(acceptor);
    }
    
    public int length() {
        return contents.size();
    }
    
    /**
     * Append an inline string to the contents.
     * @param string the string to append
     */
    public void append(WBSAXString string) throws WBDOMException {
        add(string);
    }

    public void append(StringReference reference) throws WBDOMException {
        add(reference);
    }

    public void append(final Extension extension) {
        add(new WBSAXValueVisitor.Acceptor() {
            public void accept(WBSAXValueVisitor visitor) 
                    throws WBSAXException {
                visitor.visitExtension(extension);
            }
        });
    }

    public void append(final Extension extension, final WBSAXString string) 
            throws WBDOMException {
        add(new WBSAXValueVisitor.Acceptor() {
            public void accept(WBSAXValueVisitor visitor) 
                    throws WBSAXException {
                visitor.visitExtensionString(extension, string);
            }
        });
    }

    public void append(OpaqueValue part) {
        add(part);
    }
    
    public void append(final Extension extension, final StringReference 
            reference) {
        add(new WBSAXValueVisitor.Acceptor() {
            public void accept(WBSAXValueVisitor visitor) 
                    throws WBSAXException {
                visitor.visitExtensionReference(extension, reference);
            }
        });
    }

    public void append(EntityCode entity) {
        add(entity);
    }
    
    public interface InternalIterator {
        void next(WBSAXValueVisitor.Acceptor value) throws WBDOMException;
    }
    
    public void forEachWBSAXValueAcceptor(InternalIterator iterator) 
            throws WBDOMException {
        Iterator itr = contents.iterator();
        while (itr.hasNext()) {
            WBSAXValueVisitor.Acceptor value = 
                    (WBSAXValueVisitor.Acceptor) itr.next();
            iterator.next(value);
        }
    }
    
    // NOTE: internal iterator and "proxy" accept are sort of competing here.
    // Maybe we should sort out which is canonical and ditch the other? 
    // We could have an VisitorWBSAXValueIterator implementation of 
    // InternalIterator ala the other VisitorXxxxIterator classes. The accept 
    // method of WBDOMAttribute makes this a bit tricker, since we need to be 
    // able to iterator over attribute values without necessarily creating
    // the value buffer for memory reasons...
    
    //
    // Visitor methods.
    //
    
    public void accept(WBSAXValueVisitor visitor) throws WBSAXException {
        java.util.Iterator contentIterator = contents.iterator();
        while (contentIterator.hasNext()) {
            WBSAXValueVisitor.Acceptor content = 
                    (WBSAXValueVisitor.Acceptor) contentIterator.next();
            content.accept(visitor);
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

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 27-Aug-03	1286/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Proteus)

 26-Aug-03	1284/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Mimas)

 26-Aug-03	1278/1	geoff	VBM:2003082101 Clean up wbdom.dissection (merge from Metis)

 26-Aug-03	1238/1	geoff	VBM:2003082101 Clean up wbdom.dissection

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 ===========================================================================
*/
