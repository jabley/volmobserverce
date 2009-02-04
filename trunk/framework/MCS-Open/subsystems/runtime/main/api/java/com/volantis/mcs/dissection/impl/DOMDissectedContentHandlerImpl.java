package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectableElement;
import com.volantis.mcs.dissection.dom.DissectableText;
import com.volantis.mcs.dissection.SharedContentUsages;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.string.StringSegment;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dom.*;

import java.io.Writer;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mrybak
 * Date: Jul 25, 2008
 * Time: 10:40:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DOMDissectedContentHandlerImpl implements DissectedContentHandler {

    private DOMDissectableDocumentImpl document;
    private SharedContentUsages usages;
    private Writer writer;
    private ShardLinkVisitor shardLinkVisitor;
    private ShardLinkDetails shardLinkDetails;

    public DOMDissectedContentHandlerImpl(Writer writer) {
        this.writer = writer;
    }

    public void startDocument(DissectableDocument document, SharedContentUsages usages) throws DissectionException {
        this.document = (DOMDissectableDocumentImpl)document;
        this.usages = usages;
        this.shardLinkVisitor = new ShardLinkVisitor(this.document, this);
        
        XMLDeclaration xmlDeclaration = this.document.getDOMDocument().getDeclaration();
        String declaration = (xmlDeclaration != null) ? xmlDeclaration.toString() : "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

        try {
            writer.write(declaration);
        } catch (IOException e) {
            throw new DissectionException(e);
        }
    }

    public void endDocument() throws DissectionException {
        //do nothing
    }

    public void emptyElement(DissectableElement element) throws DissectionException {
        Element domElement = ((DOMDissectableElementImpl) element).getDOMElement();
        StringBuffer content = new StringBuffer("<");
        serializeAttributes(domElement.getAttributes(), content);
        content.append(domElement.getName()).append("/>");
        try {
            writer.write(content.toString());
        } catch (IOException e) {
            throw new DissectionException(e);
        }
    }

    public void startElement(DissectableElement element) throws DissectionException {
        Element domElement = ((DOMDissectableElementImpl) element).getDOMElement();
        StringBuffer content = new StringBuffer("<");
        content.append(domElement.getName());
        serializeAttributes(domElement.getAttributes(), content);
        content.append(">");
        try {
            writer.write(content.toString());
        } catch (IOException e) {
            throw new DissectionException(e);
        }
    }

    public void endElement(DissectableElement element) throws DissectionException {
        Element domElement = ((DOMDissectableElementImpl) element).getDOMElement();
        StringBuffer content = new StringBuffer("</");
        content.append(domElement.getName()).append(">");
        try {
            writer.write(content.toString());
        } catch (IOException e) {
            throw new DissectionException(e);
        }
    }

    public void shardLink(DissectableElement element, ShardLinkDetails details) throws DissectionException {
        shardLinkDetails = details;
        document.visitChildren(element, shardLinkVisitor);
        shardLinkDetails = null;
    }

    public void text(DissectableText text) throws DissectionException {
        DOMDissectableTextImpl dissText = (DOMDissectableTextImpl)text;
        try {
            writer.write(dissText.getDOMContents(), 0, dissText.getDOMLength());
        } catch (IOException e) {
            throw new DissectionException(e);
        }
    }

    public void text(DissectableText text, StringSegment segment) throws DissectionException {
        DOMDissectableTextImpl dissText = (DOMDissectableTextImpl)text;
        String str;
        try {
            if ((str = segment.getPrefix()) != null) {
                writer.write(str);
            }
            writer.write(dissText.getDOMContents(), segment.getStart(), segment.getEnd() - segment.getStart());
            if ((str = segment.getSuffix()) != null) {
                writer.write(str);
            }
        } catch (IOException e) {
            throw new DissectionException(e);
        }
    }

    private void serializeAttributes(Attribute attribute, StringBuffer buffer) {
        if (shardLinkDetails != null) {
            shardLinkSerializeAttributes(attribute, buffer);
        } else {
            normalSerializeAttributes(attribute, buffer);
        }
    }

    private void normalSerializeAttributes(Attribute attribute, StringBuffer buffer) {
        while (attribute != null) {
                buffer.append(" ");
                buffer.append(attribute.getName());
                buffer.append("=\"");
                buffer.append(attribute.getValue());
                buffer.append("\"");
                attribute = attribute.getNext();
            }
    }

    private void shardLinkSerializeAttributes(Attribute attribute, StringBuffer buffer) {
        // shard link should only contain href="[url]" attribute
        // its value will be caclulated using shardLinkDetails object

        //first, check if href attribute is really, the only one...
        //it should never happen
        if (attribute.getNext() != null) {
            throw new IllegalStateException("Illegal shard link object");
        }
        buffer.append(" ");
        buffer.append(attribute.getName());
        buffer.append("=\"");
        buffer.append(shardLinkDetails.getURL().getExternalForm());
        buffer.append("");
        buffer.append("\"");
    }

    private static class ShardLinkVisitor extends UnsupportedVisitor {

        DOMDissectedContentHandlerImpl handler;

        private ShardLinkVisitor(DissectableDocument document, DOMDissectedContentHandlerImpl handler) {
            super(document);
            this.handler = handler;
        }

        public void visitElement(DissectableElement element) throws DissectionException {
            handler.startElement(element);            
            this.document.visitChildren(element, this);
            handler.endElement(element);
        }

        public void visitText(DissectableText text) throws DissectionException {
            handler.text(text);
        }
    }
}
