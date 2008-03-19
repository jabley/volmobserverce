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
package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Converts a W3C DOM (or DOM fragment) to SAX events.
 */
public class DOMToSAX {
    /**
     * The prefix for XML namespaces.
     */
    private static final String NAMESPACE_PREFIX = "xmlns:";

    /**
     * The root node for which we'll be generating SAX events.
     */
    private Node rootNode;

    /**
     * The content handler to which SAX events should be output.
     */
    private ContentHandler contentHandler;

    /**
     * The lexical handler to which SAX events should be output.
     */
    private LexicalHandler lexicalHandler;

    /**
     * A map of prefixes to namespace URIs.
     */
    private Map mappedPrefixes;

    /**
     * Create a new DOM to SAX converter with the specified root node.
     *
     * @param root The root node from which to convert. Should be a document or
     *             element node.
     */
    public DOMToSAX(Node root) {
        rootNode = root;
    }

    /**
     * Navigate the DOM, converting to SAX events.
     */
    public void parse() throws SAXException {
        parseNode(rootNode);
    }

    public void setContentHandler(ContentHandler handler) {
        contentHandler = handler;
        if (handler instanceof LexicalHandler) {
            lexicalHandler = (LexicalHandler) handler;
        }
    }

    private void parseNode(Node node) throws SAXException {
        switch (node.getNodeType()) {
            case Node.ELEMENT_NODE:
                parseElement(node);
                break;
            case Node.DOCUMENT_NODE:
                parseDocument(node);
                break;
            case Node.TEXT_NODE:
                parseText(node);
                break;
            case Node.CDATA_SECTION_NODE:
                parseCDATA(node);
                break;
            case Node.COMMENT_NODE:
                parseComment(node);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                parseProcessingInstruction(node);
                break;
            default:
                // All other node types we ignore - they will be handled by the
                // above node types or not at all.
        }
    }

    private void parseElement(Node node) throws SAXException {
        NamedNodeMap nodeAttributes = node.getAttributes();
        List nodePrefixes = null;
        // Start prefix mapping for all new namespace prefixes.
        for (int i = 0; i < nodeAttributes.getLength(); i++) {
            Node attribute = nodeAttributes.item(i);
            String attributeName = attribute.getNodeName();
            if (isNamespaceAttribute(attributeName)) {
                String mapping = getNameWithoutPrefix(attributeName);
                // Attribute declaration - attempt to pass this through if we
                // aren't already mapping this prefix.
                boolean mapped =
                        startPrefixMapping(mapping, attribute.getNodeValue());
                if (mapped) {
                    if (nodePrefixes == null) {
                        nodePrefixes = new ArrayList();
                    }
                    nodePrefixes.add(mapping);
                }
            }
        }

        // Generate attributes
        AttributesImpl attributes = new AttributesImpl();
        for (int i = 0; i < nodeAttributes.getLength(); i++) {
            Node attribute = nodeAttributes.item(i);
            String attributeName = attribute.getNodeName();
            if (!isNamespaceAttribute(attributeName)) {
                attributes.addAttribute(attribute.getNamespaceURI(),
                        attribute.getLocalName(), attributeName, "CDATA",
                        attribute.getNodeValue());
            }
        }

        // Output the element
        contentHandler.startElement(node.getNamespaceURI(), node.getLocalName(),
                node.getNodeName(), attributes);
        Node child = node.getFirstChild();
        while (child != null) {
            parseNode(child);
            child = child.getNextSibling();
        }
        contentHandler.endElement(node.getNamespaceURI(), node.getLocalName(),
                node.getNodeName());

        // End any prefix mappings that were started at this node
        if (nodePrefixes != null) {
            Iterator it = nodePrefixes.iterator();
            while (it.hasNext()) {
                String endPrefix = it.next().toString();
                endPrefixMapping(endPrefix);
            }
        }
    }

    private boolean startPrefixMapping(String prefix, String namespace)
            throws SAXException {
        boolean mapped = false;
        if (mappedPrefixes == null) {
            mappedPrefixes = new HashMap();
        }
        Stack namespaces = (Stack) mappedPrefixes.get(prefix);
        if (namespaces == null) {
            // Nothing is mapped to this prefix yet - start the mapping
            namespaces = new Stack();
            namespaces.push(namespace);
            contentHandler.startPrefixMapping(prefix, namespace);
            mapped = true;
        } else {
            if (namespaces.isEmpty() ||
                    !namespace.equals(namespaces.peek().toString())) {
                // The prefix is mapped to a different namespace or nothing is
                // mapped to it - start the mapping
                namespaces.push(namespace);
                contentHandler.startPrefixMapping(prefix, namespace);
                mapped = true;
            }
        }
        return mapped;
    }

    private void endPrefixMapping(String prefix) throws SAXException {
        Stack namespaces = (Stack) mappedPrefixes.get(prefix);
        contentHandler.endPrefixMapping(prefix);
        namespaces.pop();
    }

    private String getNameWithoutPrefix(String name) {
        int lastColon = name.lastIndexOf(':');
        return lastColon == -1 ? name : name.substring(lastColon + 1);
    }

    private boolean isNamespaceAttribute(String attributeQName) {
        return attributeQName.startsWith(NAMESPACE_PREFIX);
    }

    private void parseDocument(Node node) throws SAXException {
        contentHandler.startDocument();
        Node child = node.getFirstChild();
        while (child != null) {
            parseNode(child);
            child = child.getNextSibling();
        }
        contentHandler.endDocument();
    }

    private void parseText(Node node) throws SAXException {
        String text = node.getNodeValue();
        contentHandler.characters(text.toCharArray(), 0, text.length());
    }

    private void parseCDATA(Node node) throws SAXException {
        String characters = node.getNodeValue();
        if (lexicalHandler != null) {
            lexicalHandler.startCDATA();
        }
        contentHandler.characters(characters.toCharArray(), 0,
                characters.length());
        if (lexicalHandler != null) {
            lexicalHandler.endCDATA();
        }
    }

    private void parseComment(Node node) throws SAXException {
        if (lexicalHandler != null) {
            String comment = node.getNodeValue();
            lexicalHandler.comment(comment.toCharArray(), 0, comment.length());
        }
    }

    private void parseProcessingInstruction(Node node) throws SAXException {
        contentHandler.processingInstruction(node.getNodeName(),
                node.getNodeValue());
    }
}
