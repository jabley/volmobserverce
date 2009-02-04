package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.dom.DocumentVisitor;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.dom.Element;

/**
 * This class encapsulates DOM Text for purposes of dissection.
 */
public class DOMDissectableTextImpl extends DOMDissectableNodeAbstract implements DissectableText {

    private final Text text;

    private final DissectableStringImpl dissectableString;

    private final DOMDissectableDocumentImpl document;

    public DOMDissectableTextImpl(Text text, DOMDissectableDocumentImpl document) {
        this.text = text;
        this.document = document;
        this.dissectableString = new DissectableStringImpl(this); 
    }

    public Text getDOMText() {
        return this.text;
    }

    public DissectableString getDissectableString() {
        return dissectableString;
    }

    /**
     * It is only used during debugging    
     */
    public String toString() {
        return new String(text.getContents(),0 ,20);
    }

    protected void accept(DocumentVisitor visitor) throws DissectionException {
        visitor.visitText(this);
    }

    protected DOMDissectableNodeAbstract getNext() {
        return document.getDissectableNode(text.getNext());
    }

    // =========================================================================
    // Delegate methods
    // =========================================================================
    protected char[] getDOMContents(){
        return this.text.getContents();
    }
    protected int getDOMLength(){
        return this.text.getLength();
    }
}
