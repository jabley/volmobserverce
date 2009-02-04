package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.dom.DissectableNode;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dom.NodeAnnotation;

/**
 * This class implements behaviour shared by {@link DOMDissectableElementImpl} and {@link DOMDissectableTextImpl}
 * It should be only used by {@link DOMDissectableDocumentImpl}
 */
public abstract class DOMDissectableNodeAbstract implements DissectableNode {
    protected NodeAnnotation nodeAnnotation;

    protected NodeAnnotation getNodeAnnotation() {
        return nodeAnnotation;
    }

    protected void setNodeAnnotation(NodeAnnotation nodeAnnotation) {
        this.nodeAnnotation = nodeAnnotation;
    }

    protected abstract void accept(DocumentVisitor visitor) throws DissectionException;
    
    protected abstract DOMDissectableNodeAbstract getNext();

    public abstract String toString();
}
