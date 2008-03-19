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
package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.xml.pipeline.sax.XMLProcessImplTestCase;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLProcessTestable;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import org.xml.sax.helpers.NamespaceSupport;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;
import org.xml.sax.Locator;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * Test case for the {@link com.volantis.xml.pipeline.sax.impl.dynamic.ContextManagerProcess}
 */
public class ContextManagerProcessTestCase extends XMLProcessImplTestCase {

    /**
     * Creates a new <code>ContextManagerProcessTestCase</code> instance
     * @param name the name of the test
     */
    public ContextManagerProcessTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected XMLProcess createTestableProcess() {
        ContextManagerProcess process = new ContextManagerProcess();
        initializeProcess(process);
        return process;
    }

        // javadoc inherited
    public void testStartElement() throws Exception {
        // create an instance of the class that is to be tested
        XMLProcess process = createTestableProcess();
        
        // create a "base" URI
        URL baseURI = null;
        try {
            baseURI = new URL("http://foo.com");
        } catch (MalformedURLException e) {
            fail("Could not construct a java.net.URL instance");
        }

        String localName = "localName";
        String namespaceURI = "";
        String qName = localName;

        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(NamespaceSupport.XMLNS,
                          "base",
                          "xml:base",
                          "String",
                          baseURI.toExternalForm());
        
        // set the next process
        XMLProcessTestable next = new XMLProcessTestable();
        process.setNextProcess(next);
        
        // invoke the method that is being tested
        process.startElement(namespaceURI, localName, qName, atts);
        
        // get hold of the current base uri from the pipeline context
        URL currentBase =
                process.getPipeline().getPipelineContext().getCurrentBaseURI();
        
        // ensure that the XMLPipelineContext is updated with the new base
        assertEquals(
                "XMLPipelineContext should have had its base uri updated",
                baseURI,
                currentBase);

        AttributesImpl cloneAttrs = new AttributesImpl(atts);
                int index = cloneAttrs.
                        getIndex(NamespaceSupport.XMLNS, "base");
                cloneAttrs.removeAttribute(index);

        // ensure that the event is forwarded to the next process
        next.assertStartElementInvoked(namespaceURI, localName, qName, cloneAttrs);
    }

    // javadoc inherited
    public void testEndElement() throws Exception {
        // create an instance of the class that is to be tested
        XMLProcess process = createTestableProcess();
                
        // create a "base" URI
        URL baseURI = null;
        try {
            baseURI = new URL("http://foo.com");
        } catch (MalformedURLException e) {
            fail("Could not construct a java.net.URL instance");
        }
        
        // ensure the pipeline context has a current base uri
        XMLPipelineContext context
                = process.getPipeline().getPipelineContext();
        context.pushBaseURI(baseURI.toExternalForm());

        String localName = "localName";
        String namespaceURI = "";
        String qName = localName;                
        
        // set the next process
        XMLProcessTestable next = new XMLProcessTestable();
        process.setNextProcess(next);
        
        // invoke the method that is being tested 
        process.endElement(namespaceURI, localName, qName);
        
        // retrieve the current base
        URL base =
                process.getPipeline().getPipelineContext().getCurrentBaseURI();
        // ensure that the base uri is null
        assertNull("XMLPipelineContext should have had its base uri popped",
                   base);
        
        // ensure that the event is forwarded to the next process
        next.assertEndElementInvoked(namespaceURI, localName, qName);
    }

    // javadoc inherited
    public void testSetDocumentLocator() throws Exception {
        // create an instance of the class that is to be tested
        XMLProcess process = createTestableProcess();
        
        // create a locator
        LocatorImpl locator = new LocatorImpl();
        
        // create a "base" URI
        URL baseURI = null;
        try {
            baseURI = new URL("http://foo.com");
        } catch (MalformedURLException e) {
            fail("Could not construct a java.net.URL instance");
        }
        
        // update the locator with the location
        locator.setSystemId(baseURI.toExternalForm());
        
        // set the next process
        XMLProcessTestable next = new XMLProcessTestable();
        process.setNextProcess(next);
        
        // invoke the method that is being tested 
        process.setDocumentLocator(locator);        
        process.startDocument();
        
        // get hold of the current base uri from the pipeline context
        URL currentBase =
                process.getPipeline().getPipelineContext().getCurrentBaseURI();
        
        // ensure that the XMLPipelineContext is updated with the new base
        assertEquals(
                "XMLPipelineContext should have had its base uri updated",
                baseURI,
                currentBase);
        
        // get hold of the current locator from the pipeline context
        Locator currentLocator =
                process.getPipeline().getPipelineContext().getCurrentLocator();
        
        // ensure that the XMLPipelineContexts stack of locators is updated 
        assertSame(
                "XMLPipelineContext should have the current locator updated",
                locator,
                currentLocator);
        
        // ensure that the event is forwarded to the next process
        next.assertSetDocumentLocatorNotInvoked();
    }

    // javadoc inherited
    public void testEndDocument() throws Exception {
        // create an instance of the class that is to be tested
        XMLProcess process = createTestableProcess();
        
        // create a locator
        LocatorImpl locator = new LocatorImpl();
        
        // create a "base" URI
        URL baseURI = null;
        try {
            baseURI = new URL("http://foo.com");
        } catch (MalformedURLException e) {
            fail("Could not construct a java.net.URL instance");
        }
        
        // update the locator with the location
        locator.setSystemId(baseURI.toExternalForm());
        
        // set the next process
        XMLProcessTestable next = new XMLProcessTestable();
        process.setNextProcess(next);
                
        // call setDocumentLocator()
        process.setDocumentLocator(locator);
        process.startDocument();
        
        // get hold of the pipeline context
        XMLPipelineContext context
                = process.getPipeline().getPipelineContext();
        
        // ensure that the XMLPipelineContext has a current locator and
        // base uri
        assertNotNull(
                "XMLPipelineContext should have a non null current locator",
                context.getCurrentLocator());

        assertNotNull(
                "XMLPipelineContext should have a non null current base URI",
                context.getCurrentBaseURI());
        
        // invoke the method that is being tested
        process.endDocument();
        
        // ensure that the pipeline context has had both the current locator
        // and base uri popped off.
        assertNull("endDocument should pop the current locator",
                   context.getCurrentLocator());

        assertNull("endDocument should pop the current base uri",
                   context.getCurrentBaseURI());
        
        // ensure that the event is not forwarded to the next process
        next.assertEndDocumentNotInvoked();
    }

    //javadoc inherited
    public void testStartPrefixMapping() throws Exception {
        // create an instance of the class that is to be tested
        XMLProcess process = createTestableProcess();

        String prefix = "prefix";
        String namespace = "namespace";

        // set the next process
        XMLProcessTestable next = new XMLProcessTestable();
        process.setNextProcess(next);
        
        // invoke the method that is being tested
        process.startPrefixMapping(prefix, namespace);

        // get hold of the pipeline context
        XMLPipelineContext context
                = process.getPipeline().getPipelineContext();
        
        // ensure that the pipeline contexts NamespacePrefixTracker has
        // been updated        
        NamespacePrefixTracker tracker = context.getNamespacePrefixTracker();

        assertEquals("NamespacePrefixTracker was not updated with the " +
                     "startPrefixMapping event",
                     namespace,
                     tracker.getNamespaceURI(prefix));       
                
        // ensure the event was forwarded to the next process
        next.assertStartPrefixMappingInvoked(prefix, namespace);
    }

    // javadoc inherited
    public void testEndPrefixMapping() throws Exception {
        // create an instance of the class that is to be tested
        XMLProcess process = createTestableProcess();

        String prefix = "prefix";
        String namespace = "namespace";

        // set the next process
        XMLProcessTestable next = new XMLProcessTestable();
        process.setNextProcess(next);

        // get hold of the pipeline context
        XMLPipelineContext context
                = process.getPipeline().getPipelineContext();
        
        // Update pipeline contexts NamespacePrefixTracker with a prefix
        // and namespace pair
        NamespacePrefixTracker tracker = context.getNamespacePrefixTracker();
        tracker.startPrefixMapping(prefix, namespace);

        assertEquals("NamespacePrefixTracker was not updated with the " +
                     "startPrefixMapping event",
                     namespace,
                     tracker.getNamespaceURI(prefix));       
                      
        // invoke the method that is being tested
        process.endPrefixMapping(prefix);
        
        // ensure that the NamespacePrefixTracker has been updated
        assertNull("NamespacePrefixTracker was not updated with the " +
                   "endPrefixMapping event",                    
                     tracker.getNamespaceURI(prefix));
        
        // ensure the event was forwarded to the next process
        next.assertEndPrefixMappingInvoked(prefix);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 21-Nov-03	470/1	steve	VBM:2003112005 Locator stacking fix

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 ===========================================================================
*/
