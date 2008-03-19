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
package com.volantis.xml.pipeline.sax.impl.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.PassThroughController;
import com.volantis.xml.namespace.MutableExpandedName;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An <code>XMLProcess</code> that for every
 * {@link com.volantis.xml.pipeline.sax.XMLProcess#startElement} event,
 * inspects the <code>DynamicProcessConfiguratio</code> to see if a
 * <code>DynamicElementRule</code> has been registered. If one has the rule
 * is executed via the {@link DynamicElementRule#endElement} method. When the
 * corresponding {@link com.volantis.xml.pipeline.sax.XMLProcess#endElement}
 * event is recieved the rules {@link DynamicElementRule#endElement} method
 * is invoked.
 */
public class DynamicRuleProcess
        extends XMLProcessImpl
        implements PassThroughController {

    /**
     * Stack used to store away the objects that the
     * {@link com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule#startElement} method returns.
     * This is need as we need to pass the same object into the
     * {@link com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule#endElement} method
     */
    private Stack objects = new Stack();

    /**
     * This is used to lookup elements in the configuration.
     */
    private MutableExpandedName searchName;

    /**
     * A DynamicProcessConfiguration used to perform rule lookups
     */
    private DynamicProcessConfiguration dynamicConfiguration;

    /**
     * A DynamicProcess used when executing rules
     */
    private DynamicProcess dynamicProcess;

    /**
     * Flag that indicates whether this process is in pass through mode.
     */
    private boolean inPassThroughMode;

    /**
     * Initializes an instance of this class with the given parameters
     * @param dynamicProcess a DynamicProcess
     * @param configuration a DynamicProcessConfiguration;
     */
    public DynamicRuleProcess(DynamicProcess dynamicProcess,
                              DynamicProcessConfiguration configuration) {
        this.dynamicProcess = dynamicProcess;
        this.dynamicConfiguration = configuration;
        this.searchName = new MutableExpandedName();
    }

    // javadoc inherited
    public void startPassThrough() {
        inPassThroughMode = true;
    }

    // javadoc inherited
    public void stopPassThrough() {
        inPassThroughMode = false;
    }

    // Javadoc inherited
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        if (inPassThroughMode) {
            // in pass through so we must not process this event, just
            // pass it straight through to the next process
            super.startElement(namespaceURI, localName, qName, atts);
        } else {
            // check to see if a rule has been registered against this element
            DynamicElementRule rule = retrieveRule(namespaceURI, localName);

            // if a rule has been registered then execute it
            if (rule != null) {
                Object object = rule.startElement(dynamicProcess,
                                                  searchName,
                                                  atts);
                // store the returned object away so that we can use it
                // when end element for this rule is invoked
                objects.push(object);
                // Rule was found so we must consume this pipeline event
            } else {
                // no rule found so forward event to next process
                super.startElement(namespaceURI, localName, qName, atts);
            }
        }
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {
        if (inPassThroughMode) {
            // in pass through so we must not process this event, just
            // pass it straight through to the next process
            super.endElement(namespaceURI, localName, qName);
        } else {
            // check to see if a rule has been registered against this element
            DynamicElementRule rule = retrieveRule(namespaceURI, localName);

            // if a rule has been registered then execute it
            if (rule != null) {
                // retrieve the object that this rule returned when its
                // startElement method was invoked
                Object object = objects.pop();
                // invoke the endElement method
                rule.endElement(dynamicProcess, searchName, object);
                // As rule was found so we must consume this pipeline event
            } else {
                // no rule found so forward event to next process
                super.endElement(namespaceURI, localName, qName);
            }
        }
    }

    // javadoc inherited
    public void release() {
        super.release();
        searchName.clear();
    }

    /**
     * For a given namespace URI and element local name, retrieves a
     * <code>DynamicElementRule</code> from the
     * <code>DynamicProcessConfiguration</code>.
     * @param namespaceURI the namespace URI
     * @param localName the elements local name
     * @return A DynamicElementRule or null if one was not registered against
     * the namespace, local name pair.
     */
    protected DynamicElementRule retrieveRule(String namespaceURI,
                                              String localName) {
        // Initialise the search name.
        searchName.setNamespaceURI(namespaceURI);
        searchName.setLocalName(localName);

        // check to see if a rule has been registered against this element
        return dynamicConfiguration.getRule(searchName);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/
