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
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Used to collect the schema's definition of elements and permitted attributes
 * in such a way as to populate data storage that can be used to perform
 * content assist functionality.
 *
 * <p>Note that this only handles a single namespace.</p>
 */
public class SchemaConfigurator extends DefaultHandler {
    private final static String ELEMENT = "element"; //$NON-NLS-1$
    private final static String ATTRIBUTE = "attribute"; //$NON-NLS-1$
    private final static String SCHEMA = "schema"; //$NON-NLS-1$
    private final static String TARGET_NAMESPACE_URI = "targetNamespace"; //$NON-NLS-1$
    private final static String REF = "ref"; //$NON-NLS-1$
    private final static String NAME = "name"; //$NON-NLS-1$
    private final static String USE = "use"; //$NON-NLS-1$

    /**
     * The schema definition being set up by this configurator
     */
    private SchemaDefinition schemaDefinition;

    /**
     * The current element definition being set up by this configurator
     */
    private ElementDefinition elementDefinition;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param schemaDefinition the object within which the definition is to be
     *                         configured
     */
    public SchemaConfigurator(SchemaDefinition schemaDefinition) {
        this.schemaDefinition = schemaDefinition;
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        if (SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            if (SCHEMA.equals(localName)) {
                processStartSchema(attributes);
            } else if (ELEMENT.equals(localName)) {
                processStartElement(attributes);
            } else if (ATTRIBUTE.equals(localName)) {
                processStartAttribute(attributes);
            } else {
                throw new SAXException(
                    "Found an unexpected markup element \"" + localName + //$NON-NLS-1$
                    "\" in namespace \"" + namespaceURI + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else {
            throw new SAXException(
                "Found an unexpected markup element \"" + localName + //$NON-NLS-1$
                "\" in namespace \"" + namespaceURI + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            if (SCHEMA.equals(localName)) {
                processEndSchema();
            } else if (ELEMENT.equals(localName)) {
                processEndElement();
            } else if (ATTRIBUTE.equals(localName)) {
                processEndAttribute();
            } else {
                throw new SAXException(
                    "Found an unexpected markup element \"" + localName + //$NON-NLS-1$
                    "\" in namespace \"" + namespaceURI + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else {
            throw new SAXException(
                "Found an unexpected markup element \"" + localName + //$NON-NLS-1$
                "\" in namespace \"" + namespaceURI + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * Helper method used to process SAX start events for schema.
     *
     * @param attributes the attributes associated with the schema
     */
    private void processStartSchema(Attributes attributes) {
        schemaDefinition.setNamespaceURI(
            attributes.getValue(TARGET_NAMESPACE_URI));
    }

    /**
     * Helper method used to process SAX end events for schema.
     */
    private void processEndSchema() {
    }

    /**
     * Helper method used to process SAX start events for elements.
     *
     * @param attributes the attributes associated with the element
     */
    private void processStartElement(Attributes attributes) {
        if (attributes.getIndex(REF) >= 0) {
            // Add this reference as a sub-element of the current element
            // definition
            elementDefinition.addSubElement(attributes.getValue(REF));
        } else {
            // This is a new element definition
            elementDefinition = schemaDefinition.addElement(
                attributes.getValue(NAME));
        }
    }

    /**
     * Helper method used to process SAX end events for elements.
     */
    private void processEndElement() {
    }

    /**
     * Helper method used to process SAX start events for attributes.
     *
     * @param attributes the attributes associated with the element
     */
    private void processStartAttribute(Attributes attributes) {
        // Add the attribute to the current element definition
        elementDefinition.addAttribute(attributes.getValue(NAME),
                                       attributes.getValue(USE));
    }

    /**
     * Helper method used to process SAX start events for attributes.
     */
    private void processEndAttribute() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Mar-05	7457/1	philws	VBM:2005030811 Allow the MCS Source editor to work again on existing LPDM file types

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 04-Jan-04	2309/1	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
