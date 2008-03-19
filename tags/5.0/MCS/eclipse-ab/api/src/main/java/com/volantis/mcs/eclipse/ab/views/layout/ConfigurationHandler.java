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

package com.volantis.mcs.eclipse.ab.views.layout;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

import java.util.List;

/**
 * The configuration handler that will react to SAX events by recognizing
 * certain element's and constructing and populating the appropriate object
 * instance.
 */
class ConfigurationHandler implements ContentHandler {

    /**
     * Store the current attribute name so we can use it to add selection
     * items to the appropriate details object.
     */
    private String currentAttributeName = null;

    /**
     * Store the current section so we can access the appropriate details.
     */
    private SectionDetails currentSection = null;

    /**
     * Store the current details so we add the appropriate attributes to it.
     */
    private FormatAttributesViewDetails currentDetails;

    /**
     * Store a list of sections.
     */
    private List sections;

    /**
     * Construct the handler with a list that will be populated with section
     * information.
     *
     * @param sections an empty list that will be populated with section
     *                 information.
     */
    public ConfigurationHandler(List sections) {
        this.sections = sections;
    }

    // javadoc inherited.
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes attributes) throws SAXException {
        String name = localName;
        if ("section".equals(name)) { //$NON-NLS-1$

            currentSection = new SectionDetails(attributes.getValue("titleKey")); //$NON-NLS-1$
            sections.add(currentSection);

        } else if ("attribute".equals(name)) { //$NON-NLS-1$
            if (currentDetails == null) {
                currentDetails = new FormatAttributesViewDetails();
            }
            currentAttributeName = attributes.getValue("name"); //$NON-NLS-1$
            currentDetails.addAttributes(currentAttributeName,
                    attributes.getValue("type"), //$NON-NLS-1$
                    attributes.getValue("attributeType"), //$NON-NLS-1$
                    attributes.getValue("supplementary")); //$NON-NLS-1$
            currentSection.setDetails(currentDetails);

        } else if ("selection".equals(name)) { //$NON-NLS-1$
            currentSection.getDetails().associateSelectionValue(
                    currentAttributeName, attributes.getValue("value")); //$NON-NLS-1$
        }
    }

    // javadoc inherited
    public void startPrefixMapping(String s, String s1) throws SAXException {
    }

    // javadoc inherited
    public void characters(char[] chars, int i, int i1) throws SAXException {
    }

    // javadoc inherited
    public void endDocument() throws SAXException {
    }

    // javadoc inherited.
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String name = localName;
        if ("section".equals(name)) { //$NON-NLS-1$
            currentSection = null;
            currentDetails = null;
            currentAttributeName = null;
        } else if ("attribute".equals(name)) { //$NON-NLS-1$
            currentAttributeName = null;
        }
    }

    // javadoc inherited
    public void endPrefixMapping(String s) throws SAXException {
    }

    // javadoc inherited
    public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {
    }

    // javadoc inherited
    public void processingInstruction(String s, String s1) throws SAXException {
    }

    // javadoc inherited
    public void setDocumentLocator(Locator locator) {
    }

    // javadoc inherited
    public void skippedEntity(String s) throws SAXException {
    }

    // javadoc inherited
    public void startDocument() throws SAXException {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 13-Jan-04	2483/3	byron	VBM:2003121504 Corrected javadoc and updated xml and xsd file (unique validation and removed namespace declaration) and test cases

 13-Jan-04	2483/1	byron	VBM:2003121504 Eclipse PM Layout Editor: Format Attributes View: XML Config

 ===========================================================================
*/
