package com.volantis.mcs.dissection.impl;

import com.volantis.mcs.dissection.dom.*;
import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.links.ShardLinkDetails;
import com.volantis.mcs.dissection.string.DissectableString;
import com.volantis.mcs.dom.*;
import com.volantis.mcs.protocols.ProtocolConfiguration;

import java.util.Map;
import java.util.HashMap;

/**
 * Default implementation of {@link DissectableDocument}
 * Wrapps DOM document object, provides access to DissectableNodes hierarchy.
 * 
 */
public class DOMDissectableDocumentImpl implements DissectableDocument {

    /**
     Non-changeable document object being  wrapped by this class 
     */
    private final Document document;

    private DissectableElement rootElement;

    private DocumentAnnotation docAnnotation;

    private ShardLinkCalculator shardLinkCalculator;

    private ProtocolConfiguration configuration;

    /**
     * Contains relation between Nodes and DissectableNodes
     * The opposite relation (DissectableNode -> Node) is done via
     * {@link DOMDissectableElementImpl#getDOMElement()} and {@link DOMDissectableTextImpl#getDOMText()}
     */
    private Map nodesMap;

    /**
     * Constructor.
     *
     * @param document DOM object to be wrapped
     * @param configuration ProtocolConfiguration
     */
    public DOMDissectableDocumentImpl(Document document, ProtocolConfiguration configuration) {
        this.configuration = configuration;
        this.document = document;
        this.nodesMap = new HashMap();
        this.rootElement = (DOMDissectableElementImpl)getDissectableNode(this.document.getRootElement());
        shardLinkCalculator = new ShardLinkCalculator(this);
    }

    /**
     * Provides access to the original wrapped document
     * @return
     */
    public Document getDOMDocument() {
        return document;
    }

    // =========================================================================
    //   Document Methods
    // =========================================================================
    
    public DissectableElement getRootElement() {
        return rootElement;
    }

    // =========================================================================
    //   Visit Methods
    // =========================================================================

    public void visitDocument(DocumentVisitor visitor) throws DissectionException {
        visitor.visitDocument(this);
    }

    public void visitNode(DissectableNode node, DocumentVisitor visitor) throws DissectionException {
        ((DOMDissectableNodeAbstract)node).accept(visitor);        
    }

    public void visitChildren(DissectableElement element, DocumentVisitor visitor) throws DissectionException {
        DOMDissectableElementImpl dissElement = ((DOMDissectableElementImpl)element);
        dissElement.visitChildren(visitor);
    }

    // =========================================================================
    //   Annotation Methods
    // =========================================================================

    public void setAnnotation(DocumentAnnotation annotation) {
        this.docAnnotation = annotation;
    }

    public DocumentAnnotation getAnnotation() {
        return this.docAnnotation;
    }

    public void setAnnotation(DissectableNode node, NodeAnnotation annotation) {
        ((DOMDissectableNodeAbstract)node).setNodeAnnotation(annotation);
    }

    public NodeAnnotation getAnnotation(DissectableNode node) {
        return ((DOMDissectableNodeAbstract)node).getNodeAnnotation();
    }

    // =========================================================================
    //   DOM tree methods
    // =========================================================================
    
    public boolean isElementAtomic(DissectableElement element) {
        return ((DOMDissectableElementImpl)element).isAtomic();
    }

    public boolean isElementEmpty(DissectableElement element) throws DissectionException {
        return ((DOMDissectableElementImpl)element).getDOMElement().isEmpty();
    }

    public ElementType getElementType(DissectableElement element) throws DissectionException {
        //TODO never used. Consider removing it from the interface.
        throw new RuntimeException("Not implemented");
    }

    public DissectableString getDissectableString(DissectableText text) throws DissectionException {
        return ((DOMDissectableTextImpl)text).getDissectableString();
    }

    public DissectableIterator childrenIterator(DissectableElement element, DissectableIterator iterator, int start) {
        return new DOMDissectableIteratorImpl(element, start);
    }

    public DissectableIterator childrenIterator(DissectableElement element, DissectableIterator iterator) {
         return childrenIterator(element, iterator,  0);
    }

    public int getSharedContentCount() {
        //TODO to be implemented - for now, we always return 0
        return 0;
    }

    public void addDocumentOverhead(Accumulator accumulator) throws DissectionException {
        int size = 0;
        DocType docType = this.document.getDocType();
        if (docType != null) {size += docType.getAsString().length();}

        Node node = this.document.getContents();
        //if there are some comments before or after root, add their length
        do {
            if (node instanceof Comment) {
                size += ((Comment)node).getLength();
            }
        } while (!accumulator.isCalculationFinished() && (node = node.getNext()) != null);

        accumulator.add(size);
    }

    public void addElementOverhead(DissectableElement element, Accumulator accumulator) throws DissectionException {
        Element domElement = ((DOMDissectableElementImpl) element).getDOMElement();

        // Add the cost of the < and the >
        accumulator.add(2);

        // Add the cost of the element name.
        //todo support encodings?
        accumulator.add(domElement.getName().length());

        Attribute attribute = domElement.getAttributes();
        
        while (attribute != null) {
            if (accumulator.isCalculationFinished()) {
                return;
            }
            // Add the cost of the space before the attribute, the equals sign
            // and the two double quote characters.
            accumulator.add(4);
            accumulator.add(attribute.getName().length());
            accumulator.add(attribute.getValue().length());

            attribute = attribute.getNext();
        }

        // Add overhead of the close element tag
        //todo support empty: <tag/> elements
        accumulator.add(3);
        accumulator.add(domElement.getName().length());
    }

    public void addShardLinkCost(DissectableElement element, Accumulator accumulator, ShardLinkDetails details) throws DissectionException {
        shardLinkCalculator.addCost(element, accumulator);
    }

    public void addTextCost(DissectableText text, Accumulator accumulator) throws DissectionException {
        accumulator.add(((DOMDissectableTextImpl)text).getDOMLength());
    }
    
    // =========================================================================
    //   Debug Methods
    // =========================================================================
    public String getElementDescription(DissectableElement element) throws DissectionException {
        return ((DOMDissectableElementImpl)element).getDOMName();
    }

    public String getEmptyElementDescription(DissectableElement element) throws DissectionException {
        return "<" + ((DOMDissectableElementImpl)element).getDOMName() + "/>";
    }

    public String getOpenElementDescription(DissectableElement element) throws DissectionException {
        Element domElement = ((DOMDissectableElementImpl)element).getDOMElement();
        StringBuffer sb = new StringBuffer("<");
        sb.append(domElement.getName());
        Attribute attribute = domElement.getAttributes();
        while (null != attribute) {
            sb.append(" ").
               append(attribute.getName()).
               append("=\"").
               append(attribute.getValue()).
               append("\"");
            attribute = attribute.getNext();
        }
        sb.append(">");
        return sb.toString();
    }

    public String getCloseElementDescription(DissectableElement element) throws DissectionException {
        return "</" + ((DOMDissectableElementImpl)element).getDOMName() + ">";
    }

    public String getSharedContentDescription(int index) throws DissectionException {
        return "Shared content description of " + index;
    }

    public String getTextDescription(DissectableText text) throws DissectionException {
       return new String(((DOMDissectableTextImpl)text).getDOMContents());
    }
    // =========================================================================
    // non-interface protected methods
    // =========================================================================
    DOMDissectableNodeAbstract getDissectableNode(Node node) {
        DOMDissectableNodeAbstract dissNode = null;
        
        if (node != null) {
            //first, check if there is such node already created
            dissNode = (DOMDissectableNodeAbstract)nodesMap.get(node);

            //if there is not, initialize one, and put in into map
            if (dissNode == null) {
                if (node instanceof Element) {
                    dissNode = new DOMDissectableElementImpl(
                            (Element)node,
                            this,
                            configuration.isElementAtomic(((Element)node).getName()));
                } else if (node instanceof Text) {
                    dissNode = new DOMDissectableTextImpl((Text)node, this);
                } else {//todo: other types? comment ?
                    throw new RuntimeException("Unknown node type+ " + node.getClass());
                }
                nodesMap.put(node, dissNode);
            }
        }
        return dissNode;
    }

    private class ShardLinkCalculator
        extends UnsupportedVisitor {

        private Accumulator accumulator;

        public ShardLinkCalculator(DissectableDocument document) {
            super(document);
        }

        public void addCost(DissectableElement element,
                            Accumulator accumulator)
            throws DissectionException {

            this.accumulator = accumulator;

            this.document.visitChildren(element, this);
        }

        public void visitElement(DissectableElement element)
            throws DissectionException {

            addElementOverhead(element, accumulator);

            this.document.visitChildren(element, this);
        }

        public void visitText(DissectableText text)
            throws DissectionException {

            addTextCost(text, accumulator);
        }
    }
}
