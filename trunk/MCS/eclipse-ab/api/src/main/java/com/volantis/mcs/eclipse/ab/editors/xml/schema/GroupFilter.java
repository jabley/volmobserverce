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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.xml.schema;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import com.volantis.xml.sax.recorder.SAXRecorder;
import com.volantis.xml.sax.recorder.SAXRecorderFactory;
import com.volantis.xml.sax.recorder.SAXRecording;
import com.volantis.xml.sax.recorder.SAXPlayer;
import com.volantis.mcs.eclipse.ab.editors.xml.schema.recorder.SAXEventRecorder;

/**
 * This filter resolves references to attributeGroup and group XSD elements,
 * substituting the actual content of the referenced attributeGroups and
 * groups.
 */
public class GroupFilter extends XMLFilterImpl {
    /**
     * The XSD markup that represents an attribute
     */
    private static final String ATTRIBUTE_NAME = "attribute";

    /**
     * The XSD markup that represents an attribute group
     */
    private static final String ATTRIBUTE_GROUP_NAME = "attributeGroup";

    /**
     * The XSD markup that represents an element
     */
    private static final String ELEMENT_NAME = "element";

    /**
     * The XSD markup that represents an element group
     */
    private static final String ELEMENT_GROUP_NAME = "group";

    /**
     * The XSD markup that represents a schema
     */
    private static final String SCHEMA_NAME = "schema";

    /**
     * The XSD attribute that indicates a reference rather than definition
     */
    private static final String REF_ATTRIBUTE = "ref";

    /**
     * The XSD attribute that indicates a definition rather than reference
     */
    private static final String NAME_ATTRIBUTE = "name";

    /**
     * The XSD attribute that indicates usage of an attribute (required or
     * optional)
     */
    private static final String USE_ATTRIBUTE = "use";

    /**
     * The most basic XSD attribute type
     */
    private static final String STRING = "string";

    /**
     * Represents an element group, providing reference to other elements.
     */
    private class ElementGroup {
        /**
         * The set of named elements referenced by this group
         *
         * @supplierRole elements
         * @supplierCardinality 0..*
         * @associates String
         */
        private List elements = new ArrayList();

        /**
         * The name of this group
         */
        private String name;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param name the name of the group
         */
        public ElementGroup(String name) {
            this.name = name;
        }

        /**
         * Permits the named element to be added to this element group
         * definition.
         *
         * @param elementName the element to be added to the group
         */
        public void addElement(String elementName) {
            elements.add(elementName);
        }

        /**
         * Permits the named elements identified by another group to be added
         * to this group's definition.
         *
         * @param group the group who's elements are to be added to this group
         */
        public void add(ElementGroup group) {
            elements.addAll(group.elements);
        }

        // javadoc unnecessary
        public String getName() {
            return name;
        }

        // javadoc unnecessary
        public List getElements() {
            return elements;
        }
    }

    /**
     * Represents an element group, providing reference to other elements.
     */
    private class AttributeGroup {
        /**
         * The set of {@link Attribute}s referenced by
         * this group
         *
         * @supplierRole attributes
         * @supplierCardinality 0..*
         * @associates <{Attribute}>
         */
        private List attributes = new ArrayList();

        /**
         * The name of this group
         */
        private String name;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param name the name of the group
         */
        public AttributeGroup(String name) {
            this.name = name;
        }

        /**
         * Permits the given attribute to be added to this attribute group
         * definition.
         *
         * @param attribute the attribute to be added to the group
         */
        public void addAttribute(Attribute attribute) {
            attributes.add(attribute);
        }

        /**
         * Permits the given group's {@link Attribute}s
         * to be added to this group's definition.
         *
         * @param group the group who's attributes are to be added to this
         *              group
         */
        public void add(AttributeGroup group) {
            attributes.addAll(group.attributes);
        }

        // javadoc unnecessary
        public String getName() {
            return name;
        }

        // javadoc unnecessary
        public List getAttributes() {
            return attributes;
        }
    }

    /**
     * Represents an individual attribute.
     */
    private class Attribute {
        /**
         * The attribute's name
         */
        private String name;

        /**
         * The attribute's use (if defined this is required or optional; if
         * not defined the attribute is optional)
         */
        private String use;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param name the attribute name
         * @param use  the attribute's usage
         */
        public Attribute(String name, String use) {
            this.name = name;
            this.use = use;
        }

        // javadoc unnecessary
        public String getName() {
            return name;
        }

        // javadoc unnecessary
        public String getUse() {
            return use;
        }
    }

    /**
     * The defined set of attribute groups, keyed on their names.
     */
    private Map attributeGroups = new HashMap();

    /**
     * When an attribute group definition is being processed this value will
     * be non-null.
     */
    private AttributeGroup currentAttributeGroup;

    /**
     * When an attributeGroup element is being processed this indicates that
     * the element is a reference rather than definition.
     */
    private boolean attributeGroupRef;

    /**
     * The defined set of element groups, keyed on their names.
     */
    private Map elementGroups = new HashMap();

    /**
     * When an element group definition is being processed this value will be
     * non-null.
     */
    private ElementGroup currentElementGroup;

    /**
     * When a group element is being processed this indicates that the element
     * is a reference rather than definition.
     */
    private boolean elementGroupRef;

    /**
     * If we need to step through the document to find an element group
     * definition, we record the events to this recorder.
     */
    private SAXEventRecorder recorder;

    /**
     * Flag to indicate whether we've already processed all groups in this
     * document.
     */
    private boolean groupsProcessed = false;

    /**
     * Initializes the new instance using the given parameters.
     */
    public GroupFilter() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param xmlReader the parent XMLReader for this filter
     */
    public GroupFilter(XMLReader xmlReader) {
        super(xmlReader);
    }

    // javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        if (ELEMENT_GROUP_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI) &&
            attributes.getIndex(NAME_ATTRIBUTE) >= 0) {
            processStartElementGroup(attributes, qName);
        } else if (ELEMENT_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI) &&
            (currentElementGroup != null)) {
            if (attributes.getIndex(NAME_ATTRIBUTE) >= 0) {
                throw new IllegalStateException("Found an element definition in a group definition");
            }
            
            currentElementGroup.addElement(attributes.getValue(REF_ATTRIBUTE));
        } else if (recorder != null) {
            recorder.startElement(namespaceURI, localName, qName, attributes);
        } else if (ELEMENT_GROUP_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processStartElementGroup(attributes, qName);
        } else if (ATTRIBUTE_GROUP_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processStartAttributeGroup(attributes, qName);
        } else if (ATTRIBUTE_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI) &&
            (currentAttributeGroup != null)) {
            currentAttributeGroup.addAttribute(
                new Attribute(attributes.getValue(NAME_ATTRIBUTE),
                              attributes.getValue(USE_ATTRIBUTE)));
        } else {
            super.startElement(namespaceURI, localName, qName, attributes);
        }
    }

    /**
     * Helper method that handles an element group (start) event.
     *
     * @param attributes the attributes for the element group
     * @param qName the qualified name from the start element event
     * @throws SAXException if a problem is encountered
     */
    private void processStartElementGroup(Attributes attributes,
                                          String qName) throws SAXException {
        if (attributes.getIndex(NAME_ATTRIBUTE) >= 0) {
            if (currentElementGroup != null) {
                throw new IllegalStateException(
                    "Found nested group definitions");
            }

            currentElementGroup = new ElementGroup(
                attributes.getValue(NAME_ATTRIBUTE));
            elementGroupRef = false;
            elementGroups.put(currentElementGroup.getName(),
                              currentElementGroup);
        } else if (attributes.getIndex(REF_ATTRIBUTE) >= 0) {
            ElementGroup refGroup = (ElementGroup)elementGroups.
                get(attributes.getValue(REF_ATTRIBUTE));

            if (recorder != null) {
                // We're recording - don't try to process this reference yet,
                // but record it for later use.
                recorder.startElement(SchemaConstants.NAMESPACE_URI,
                        ELEMENT_GROUP_NAME, qName, attributes);
                recorder.endElement(SchemaConstants.NAMESPACE_URI,
                        ELEMENT_GROUP_NAME, qName);
            } else if (refGroup == null) {
                // Group has not yet been defined. Quietly ignore this as an
                // attempt to see whether other stuff still works...
                if (currentElementGroup != null) {
                    // We're in an existing element group. The current parser
                    // can't handle this situation.
                    throw new IllegalStateException("Can't handle undefined" +
                            "references in group definition");
                } else {
                    // We've hit an unresolved element group - if we've already
                    // resolved all groups then fail, otherwise start recording
                    // so that we can find a later definition
                    if (groupsProcessed) {
                        throw new IllegalStateException(
                                "Undefined element group reference " +
                                        attributes.getValue(REF_ATTRIBUTE));
                    } else {
                        SAXRecorderFactory recorderFactory =
                                SAXRecorderFactory.getDefaultInstance();
                        recorder = new SAXEventRecorder();
                        recorder.startElement(SchemaConstants.NAMESPACE_URI,
                                ELEMENT_GROUP_NAME, qName, attributes);
                    }
                }
            } else {
                elementGroupRef = true;

                if (currentElementGroup != null) {
                    currentElementGroup.add(refGroup);
                } else {
                    Iterator i = refGroup.getElements().iterator();
                    String type = generateQName(qName, STRING);
                    String elementQName = generateQName(qName, ELEMENT_NAME);

                    while (i.hasNext()) {
                        String elementName = (String)i.next();
                        AttributesImpl attrs = new AttributesImpl();

                        attrs.addAttribute("",
                                           REF_ATTRIBUTE,
                                           REF_ATTRIBUTE,
                                           type,
                                           elementName);

                        super.startElement(SchemaConstants.NAMESPACE_URI,
                                           ELEMENT_NAME,
                                           elementQName,
                                           attrs);

                        super.endElement(SchemaConstants.NAMESPACE_URI,
                                         ELEMENT_NAME,
                                         elementQName);
                    }
                }
            }
        }
    }

    /**
     * Helper method that handles an attribute group (start) event.
     *
     * @param attributes the attributes for the attribute group
     * @param qName the qualified name from the start element event
     * @throws SAXException if a problem is encountered
     */
    private void processStartAttributeGroup(Attributes attributes,
                                            String qName) throws SAXException {
        if (attributes.getIndex(NAME_ATTRIBUTE) >= 0) {
            if (currentAttributeGroup != null) {
                throw new IllegalStateException(
                    "Found nested attributeGroup definitions");
            }

            currentAttributeGroup = new AttributeGroup(
                attributes.getValue(NAME_ATTRIBUTE));
            attributeGroupRef = false;
            attributeGroups.put(currentAttributeGroup.getName(),
                                currentAttributeGroup);
        } else if (attributes.getIndex(REF_ATTRIBUTE) >= 0) {
            AttributeGroup refGroup = (AttributeGroup)attributeGroups.
                get(attributes.getValue(REF_ATTRIBUTE));

            if (refGroup == null) {
                throw new IllegalStateException(
                    "Found a reference to an undefined attributeGroup (" +
                    attributes.getValue(REF_ATTRIBUTE) + ")");
            }

            attributeGroupRef = true;

            if (currentAttributeGroup != null) {
                currentAttributeGroup.add(refGroup);
            } else {
                Iterator i = refGroup.getAttributes().iterator();
                String type = generateQName(qName, STRING);
                String attributeQName = generateQName(qName,
                                                      ATTRIBUTE_NAME);

                while (i.hasNext()) {
                    Attribute attribute = (Attribute)i.next();
                    AttributesImpl attrs = new AttributesImpl();

                    attrs.addAttribute("",
                                       NAME_ATTRIBUTE,
                                       NAME_ATTRIBUTE,
                                       type,
                                       attribute.getName());

                    if (attribute.getUse() != null) {
                        attrs.addAttribute("",
                                           USE_ATTRIBUTE,
                                           USE_ATTRIBUTE,
                                           type,
                                           attribute.getUse());
                    }

                    super.startElement(SchemaConstants.NAMESPACE_URI,
                                       ATTRIBUTE_NAME,
                                       attributeQName,
                                       attrs);

                    super.endElement(SchemaConstants.NAMESPACE_URI,
                                     ATTRIBUTE_NAME,
                                     attributeQName);
                }
            }
        }
    }

    /**
     * Helper method that constructs a qualified name from an existing QName
     * (using its prefix, if available) and the specified local name.
     *
     * @param existingQName an existing QName from which the prefix will be
     *                      obtained
     * @param localName     the local name from which a qualified name is to be
     *                      generated
     * @return a qualified version of the given local name
     */
    private String generateQName(String existingQName, String localName) {
        int index;

        if ((index = existingQName.indexOf(':')) >= 0) {
            StringBuffer sb =
                new StringBuffer(index + 1 + localName.length());

            sb.append(existingQName.substring(0, index + 1)).append(localName);

            localName = sb.toString();
        }
        return localName;
    }

    // javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (SCHEMA_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processEndSchema();
            super.endElement(namespaceURI, localName, qName);
        } else if (ELEMENT_GROUP_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processEndElementGroup();
        } else if (ELEMENT_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI) &&
            (currentElementGroup != null)) {
            // do nothing
        } else if (recorder != null) {
            // We're currently recording, so record
            recorder.endElement(namespaceURI, localName, qName);
        } else if (ATTRIBUTE_GROUP_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processEndAttributeGroup();
        } else if (ATTRIBUTE_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI) &&
            (currentAttributeGroup != null)) {
            // do nothing
        } else if (SCHEMA_NAME.equals(localName) &&
            SchemaConstants.NAMESPACE_URI.equals(namespaceURI)) {
            processEndSchema();
            super.endElement(namespaceURI, localName, qName);
        } else {
            super.endElement(namespaceURI, localName, qName);
        }
    }

    /**
     * Helper method that handles the ending of a schema
     */
    private void processEndSchema() throws SAXException {
        // If we've reached the end of the schema successfully, then no action
        // is required. If we've been recording events to allow groups to be
        // completed, then now is the time to play back those events.
        if (recorder != null) {
            SAXEventRecorder localRecorder = recorder;
            recorder = null;
            groupsProcessed = true;
            localRecorder.playback(this);
        }
    }

    /**
     * Helper method that handles the ending of an element group (definition or
     * reference).
     */
    private void processEndElementGroup() {
        if (!elementGroupRef) {
            currentElementGroup = null;
        }

        elementGroupRef = false;
    }

    /**
     * Helper method that handles the ending of an attribute group (definition
     * or reference).
     */
    private void processEndAttributeGroup() {
        if (!attributeGroupRef) {
            currentAttributeGroup = null;
        }

        attributeGroupRef = false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 04-Jan-04	2309/2	allan	VBM:2003122202 Provide an MCS source editor for multi-page and stand-alone policy editing.

 ===========================================================================
*/
