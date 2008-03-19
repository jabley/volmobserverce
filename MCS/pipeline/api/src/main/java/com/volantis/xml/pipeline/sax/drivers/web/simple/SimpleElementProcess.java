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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.drivers.web.simple;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A process that collects the content of simple elements and sets them as
 * properties on an object.
 *
 * todo: This should be replaced with rules, it is only here to try and behave as closely as possible with the old code.
 */
public class SimpleElementProcess
        extends XMLProcessImpl {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(SimpleElementProcess.class);

    /**
     * The finder for a {@link Setter}.
     */
    private final SetterFinder setterFinder;

    /**
     * The object upon which the property
     */
    private final Object object;

    /**
     * The contents of the element.
     */
    private final StringBuffer contents;

    private final ExpandedName elementName;
    /**
     * The default namespace for elements processed by
     * this, only required to support elements that are
     * incorrectly unqualified.
     */
    private final String defaultNamespace;

    /**
     * The element that is being processed.
     */
    private String currentQName;

    /**
     * The current setter to use, set in startElement, used and cleared in
     * endElement.
     */
    private Setter setter;

    /**
     * Indicates whether during processing an unqualified element was found
     * for which there was no setter but which when it was checked in the
     * default namespace did match.
     */
    private boolean foundUnqualifiedElementThatMatched;

    /**
     * Initialise.
     *
     * @param elementName
     * @param setters          The setters to use.
     * @param object           The object to update.
     * @param defaultNamespace The default namespace for elements processed by
     *                         this, only required to support elements that are
     *                         incorrectly unqualified.
     */
    public SimpleElementProcess(
            ExpandedName elementName, Setters setters, Object object,
            String defaultNamespace) {

        this.elementName = elementName;
        this.defaultNamespace = defaultNamespace;
        setterFinder = new SetterFinder(setters);
        this.object = object;
        contents = new StringBuffer();
    }

    // Javadoc inherited
    public void startElement(
            String namespaceURI, String localName, String qName,
            Attributes atts) throws SAXException {

        if (setter != null) {
            error(new XMLProcessingException(
                    "Nested elements not allowed inside simple elements, " +
                    "found " + qName + " inside " + currentQName,
                    getPipelineContext().getCurrentLocator()));
        }

        setter = getSetter(namespaceURI, localName);
        currentQName = qName;

        contents.setLength(0);
    }

    /**
     * Get the setter for the specified element.
     * @param namespaceURI The namespace URI of the element.
     * @param localName The local name of the element.
     * @return The setter for the element.
     * @throws SAXException If the setter could not be found.
     */
    private Setter getSetter(String namespaceURI, String localName)
            throws SAXException {
        Setter setter = setterFinder.findElementSetter(namespaceURI, localName);
        if (setter == null) {
            // If the namespace is not specified then look for a setter in
            // the default namespace.
            if (namespaceURI.equals("")) {
                setter = setterFinder.findElementSetter(
                        defaultNamespace, localName);
                if (setter == null) {
                    error(new XMLProcessingException("Invalid element '" +
                            "{" + namespaceURI + "}" + localName + "'",
                            getPipelineContext().getCurrentLocator()));
                } else {
                    foundUnqualifiedElementThatMatched = true;
                }
            }
        }

        return setter;
    }

    // Javadoc inherited
    public void characters(char ch[], int start, int length)
            throws SAXException {
        contents.append(ch, start, length);
    }

    // Javadoc inherited
    public void endElement(
            String namespaceURI, String localName, String qName)
            throws SAXException {

        if (!getPipelineContext().inErrorRecoveryMode()) {
            // Get the contents as a string.
            String string = contents.toString();

            setter.setPropertyAsString(object, string);
        }

        setter = null;
        currentQName = null;
    }

    /**
     * Get the object that was being initialised.
     *
     * @return The object being initialised.
     */
    public Object getObject() {

        if (foundUnqualifiedElementThatMatched) {
            if (logger.isErrorEnabled()) {
                logger.error("children-must-be-qualified",
                        new Object[]{
                            elementName,
                            defaultNamespace
                        });
            }
        }

        return object;
    }
}
