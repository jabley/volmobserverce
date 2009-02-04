package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.dom.DissectableIterator;
import com.volantis.mcs.dissection.dom.DissectableNode;

/**
 * Created by IntelliJ IDEA.
 * User: mrybak
 * Date: Aug 18, 2008
 * Time: 10:15:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class DOMDissectableIteratorImpl implements DissectableIterator {

    private DOMDissectableNodeAbstract current; 

    public DOMDissectableIteratorImpl(DissectableNode first) {
        this.current = (DOMDissectableNodeAbstract)first;
    }

    public DOMDissectableIteratorImpl(DissectableNode parent, int start) {
        DOMDissectableDocumentImpl document = ((DOMDissectableElementImpl)parent).document;
        this.current = document.getDissectableNode(((DOMDissectableElementImpl)parent).getDOMHead()); 
        for (int i = 0; hasNext() && i < start; i++ ) {
            next();
        }
    }

    public boolean hasNext() {
        return current != null;
    }

    public DissectableNode next() {
        DOMDissectableNodeAbstract next = current;
        current = current.getNext();
        return next;
    }
}
