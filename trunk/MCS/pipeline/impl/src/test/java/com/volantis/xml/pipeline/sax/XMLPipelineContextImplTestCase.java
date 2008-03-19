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
package com.volantis.xml.pipeline.sax;

import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.recovery.RecoverableTransactionStack;
import com.volantis.shared.servlet.ServletEnvironmentInteractionImpl;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.config.SimpleXMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import junitx.util.PrivateAccessor;
import org.xml.sax.Locator;
import org.xml.sax.helpers.LocatorImpl;

import java.net.URL;
import java.util.Iterator;

/**
 * Test Case for the XMLPipelineContextImpl class
 */
public class XMLPipelineContextImplTestCase extends TestCaseAbstract {

    /**
     * An instance of the class that is being tested
     */
    private XMLPipelineContext context;

    /**
     * Factory for creating pipeline based objects
     */
    private InternalXMLPipelineFactory pipelineFactory;

    /**
     * The pipeline configuration
     */
    private XMLPipelineConfiguration pipelineConfiguration;

    /**
     * A root environment interaction
     */
    private EnvironmentInteraction environmentInteraction;

    /**
     * The internal base URI stack that the context manages 
     */
    private RecoverableTransactionStack internalURIStack;

    /**
     * The internal locator stack that the context manages 
     */
    private RecoverableTransactionStack internalLocatorStack;

    /**
     * The ComplexPropertyContainer member that XMLPipelineContextImpl
     * uses to manage properties
     */
//    private RecoverableComplexPropertyContainer internalProperties;

    /**
     * The Internal stack member that the XMLPipelineContextImpl use to 
     * manage arbitary objects. 
     */
//    private RecoverableResourceOwnerStack internalObjects;

    /**
     * Creates a new XMLPipelineContextImplTestCase
     * @param name the name of the test
     */
    public XMLPipelineContextImplTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        pipelineFactory = new TestPipelineFactory();
        pipelineConfiguration = new SimpleXMLPipelineConfiguration();
        environmentInteraction =
                new ServletEnvironmentInteractionImpl(null, null, null,
                                                      null, null);
        context = pipelineFactory.createPipelineContext(
                pipelineConfiguration, environmentInteraction);

        internalURIStack = (RecoverableTransactionStack) 
                PrivateAccessor.getField(context, "baseURIs");
        
        internalLocatorStack = (RecoverableTransactionStack) 
                PrivateAccessor.getField(context, "locators");
        
//        internalProperties = (RecoverableComplexPropertyContainer)
//                PrivateAccessor.getField(context, "properties");
//
//        internalObjects = (RecoverableResourceOwnerStack)
//                PrivateAccessor.getField(context, "objects");
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the getPipelineFactory method
     * @throws Exception if an error occurs
     */
    public void testGetPipelineFactory() throws Exception {
        assertEquals("getPipelineFactory should return the factory passed " +
                     "in on construction",
                     pipelineFactory,
                     context.getPipelineFactory());
    }

    /**
     * Tests the getPipelineConfiguration method
     * @throws Exception if an error occurs
     */
    public void testGetPipelineConfiguration() throws Exception {
        assertEquals("getPipelineConfiguration should return the " +
                     "configurationpassed in on construction",
                     pipelineConfiguration,
                     context.getPipelineConfiguration());
    }

    /**
     * Tests the pushLocator method
     * @throws Exception if an error occurs
     */
    public void testPushLocator() throws Exception {
        Locator l1 = createLocator();
        Locator l2 = createLocator();

        context.pushLocator(l1);
        context.pushLocator(l2);
        
        // ensure that l2 is at the top of the stack         
        assertEquals("pushLocator should add Locators to the top of the stack",
                     1, internalLocatorStack.search(l2));
        
        // ensure that l1 is second in the of the stack         
        assertEquals("l1 should be second from top in the stack",
                     2, internalLocatorStack.search(l1));

    }

    /**
     * Tests the popLocator method
     * @throws Exception if an error occurs
     */
    public void testPopLocator() throws Exception {
        Locator l1 = createLocator();
        Locator l2 = createLocator();

        internalLocatorStack.push(l1);
        internalLocatorStack.push(l2);
        
        // ensure items are popped off in the correct order
        assertEquals("popLocator should pop top item",
                     l2, context.popLocator());

        assertEquals("popLocator should pop l1 item",
                     l1, context.popLocator());
    }

    /**
     * Tests the popLocator method when no Locators have been pushed
     * @throws Exception if an error occurs
     */
    public void testPopLocatorNoLocatorss() throws Exception {
        IllegalStateException ise = null;
        try {
            context.popLocator();
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull("popLocator should throw IllegalStateException if no " +
                      "Locator has been pushed", ise);
    }

    /**
     * Tests the getCurrentLocator method
     * @throws Exception if an error occurs 
     */
    public void testGetCurrentLocator() throws Exception {
        URL url = new URL("http://www.bbc.co.uk");
        internalURIStack.push(url);

        assertEquals("getCurrentBaseURI should return URL at top of stack",
                     url, context.getCurrentBaseURI());
    }

    /**
     * Tests the getCurrentBaseURI method returns null when no
     * URI has been pushed
     * @throws Exception if an error occurs 
     */
    public void testGetCurrentBaseURINoURIPushed() throws Exception {
        assertNull("getCurrentBaseURI should return null if no base " +
                   "has been pushed", context.getCurrentBaseURI());
    }

    /**
     * Tests the pushBaseURI method
     * @throws Exception if an error occurs
     */
    public void testPushBaseURI() throws Exception {
        String url1 = "http://www.bbc.co.uk";
        String url2 = "http://www.itn.co.uk";

        context.pushBaseURI(url1);
        context.pushBaseURI(null);
        context.pushBaseURI(url2);
        

        // ensure that url2 is at the top of the stack         
        assertEquals("pushBaseURI should add urls to the top of the stack",
                     url2,
                     ((URL) internalURIStack.get(2)).toExternalForm());
        
        // ensure that url1 is second in the of the stack         
        assertEquals("pushBaseURI url1 should be second from top in the stack",
                     url1,
                     ((URL) internalURIStack.get(1)).toExternalForm());
        
        // ensure that url1 is second in the of the stack         
        assertEquals("pushBaseURI url1 should be at bottom of the stack",
                     url1,
                     ((URL) internalURIStack.get(0)).toExternalForm());
    }

    /**
     * Tests the popBaseURI method
     * @throws Exception if an error occurs
     */
    public void testPopBaseURI() throws Exception {
        URL url1 = new URL("http://www.bbc.co.uk");
        URL url2 = new URL("http://www.itn.co.uk");

        internalURIStack.push(url1);
        internalURIStack.push(url2);
        
        // ensure items are popped off in the correct order
        assertEquals("popBaseURI should pop top item",
                     url2, context.popBaseURI());

        assertEquals("popBaseURI should pop ur1 item",
                     url1, context.popBaseURI());
    }

    /**
     * Tests the popBaseURI method when no URIs have been pushed
     * @throws Exception if an error occurs
     */
    public void testPopBaseURINoURIs() throws Exception {
        IllegalStateException ise = null;
        try {
            context.popBaseURI();
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull("popBaseURI should throw IllegalStateException if no " +
                      "URI has been pushed", ise);
    }

    /**
     * Tests the getCurrentBaseURI method
     * @throws Exception if an error occurs 
     */
    public void testGetCurrentBaseURI() throws Exception {
        Locator l1 = createLocator();
        internalLocatorStack.push(l1);

        assertEquals("getCurrentLocator should return Locator at top of stack",
                     l1, context.getCurrentLocator());
    }

    /**
     * Tests the getCurrentLocator method returns null when no
     * URI has been pushed
     * @throws Exception if an error occurs 
     */
    public void testGetCurrentLocatorNoLocatorPushed() throws Exception {
        assertNull("getCurrentLocator should return null if no base " +
                   "has been pushed", context.getCurrentLocator());
    }

    /**
     * Tests the getLocators method.
     * @throws Exception if an error occurs
     */
    public void testGetLocators() throws Exception {
        // create some locators
        Locator l1 = createLocator();
        Locator l2 = createLocator();
        // add the locators to the context
        context.pushLocator(l1);
        context.pushLocator(l2);
        // call getLocators to obtain an iterator
        Iterator i = context.getLocators();
        // check that we can iterate l2
        assertTrue("hasNext should return true when 2 items in stack",
                   i.hasNext());
        assertEquals("Locator l1 should be returned", l1, i.next());
        // check that we can iterate l1
        assertTrue("hasNext should return true when 1 items in stack",
                   i.hasNext());
        assertEquals("Locator l2 should be returned", l2, i.next());
        // ensure no more items
        assertFalse("hasNext should return false when nothing to iterate",
                    i.hasNext());

    }

    /**
     * Ensures that you cannot remove locators via the iterator returned 
     * from the getLocators method
     * @throws Exception if an error occurs
     */
    public void testGetLocatorsRemove() throws Exception {
        // enusre we have a locator to iterate
        context.pushLocator(createLocator());
        Iterator iter = context.getLocators();
        UnsupportedOperationException uoe = null;
        try {
            iter.next();
            iter.remove();
        } catch (UnsupportedOperationException e) {
            uoe = e;
        }
        assertNotNull("Iterator.remove should throw an " + "" +
                      "UnsupportedOperationException", uoe);
    }

    /**
     * Tests the pushObject method
     * @throws Exception if an error occurs
     */
    public void testObjectStack() throws Exception {
        // create a object to push
        Object o = new Object();
        // push the object onto the stack
        context.pushObject(o, false);

        Object popped = context.popObject();
        assertSame(o, popped);

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ResourceOwnerMock resourceOwnerMock1 =
                new ResourceOwnerMock("resourceOwnerMock1", expectations);

        final ResourceOwnerMock resourceOwnerMock2 =
                new ResourceOwnerMock("resourceOwnerMock2", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        resourceOwnerMock1.expects.release();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // create an object
        context.pushObject(resourceOwnerMock1, true);

        assertSame(resourceOwnerMock1, context.getCurrentObject());

        context.pushObject(resourceOwnerMock2, false);

        // ensure pop pops the correct object off the stack
        assertSame("Wrong object popped", resourceOwnerMock2, context.popObject());

        // ensure pop pops the correct object off the stack
        assertSame("Correct Object was not popped", resourceOwnerMock1,
                     context.popObject());
    }

    /**
     * Tests the findObject method
     * @throws Exception if an error occurs
     */
    public void testFindObject() throws Exception {
        // create some objects
        Integer i = new Integer(1);
        Boolean b = new Boolean(false);
        Integer j = new Integer(3);
        // push these objects onto the stack
        context.pushObject(i, false);
        context.pushObject(b, false);
        context.pushObject(j, false);
        // ensure that findObject returns the expected object
        assertEquals("Wrong Integer object found",
                     j, context.findObject(Integer.class));
        assertEquals("Wrong Boolean object found",
                     b, context.findObject(Boolean.class));
        assertNull("Should not be able to find a Long object",
                   context.findObject(Long.class));

    }

    /**
     * Tests the setProperty method
     * @throws Exception if an error occures
     */
    public void testPropertyContainer() throws Exception {
        // create a key and value
        Object key = new Object();
        Object value = new Object();
        // call setProperty to add the property
        context.setProperty(key, value, false);

        // call getProperty ensuring the correct value is returned
        assertSame("getProperty should return the correct property",
                     value, context.getProperty(key));
    }

    /**
     * Tests the removeProperty method
     * @throws Exception if an error occures
     */
    public void testRemoveProperty() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ResourceOwnerMock resourceOwnerMock =
                new ResourceOwnerMock("resourceOwnerMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        resourceOwnerMock.expects.release();

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // create a key and value
        Object key = new Object();
        // add the property directly to the internal container
        context.setProperty(key, resourceOwnerMock, true);
        // call remove and ensure the correct property was removed
        assertEquals("removeProperty should remove the correct property",
                     resourceOwnerMock, context.removeProperty(key));
        // ensure the property was actually removed.
        assertNull("removeProperty should remove the property from the " +
                   "container", context.getProperty(key));

        expectations.verify();

        context.setProperty(key, resourceOwnerMock, false);

        // call remove and ensure the correct property was removed
        assertEquals("removeProperty should remove the correct property",
                resourceOwnerMock, context.removeProperty(key));
        // ensure the property was actually removed.
        assertNull("removeProperty should remove the property from the " +
                "container", context.getProperty(key));
    }

    /**
     * Factory method for creating locator objects
     * @return a Locator instance
     */
    protected Locator createLocator() {
        return new LocatorImpl();
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/4	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 28-Jul-03	232/1	doug	VBM:2003071804 Refactored XMLPipelineContext to reflect new public API

 ===========================================================================
*/
