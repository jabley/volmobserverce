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
 
package com.volantis.mcs.marlin.sax;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.context.ProjectStack;
import com.volantis.mcs.runtime.project.BaseURLTracker;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.synergetics.BooleanWrapper;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.MockFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.LocatorImpl;

/**
 * This class tests NamespaceSwitchContentHandler
 */
public class NamespaceSwitchContentHandlerTestCase
        extends TestCaseAbstract {

    private static final String knownNamespace = "known-namespace";

    /**
     * Test the method setInitialRequestContext(MarinerRequestContext)     
     */ 
    public void testSetInitialRequestContext() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final TestMarinerRequestContext context =
                new TestMarinerRequestContext();


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final MarinerPageContextMock pageContextMock =
                new MarinerPageContextMock("pageContextMock",
                        expectations);

        final ProjectManagerMock projectManagerMock =
                new ProjectManagerMock("projectManagerMock", expectations);

        ProjectStack projectStack = new ProjectStack();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pageContextMock.expects.getRequestURL(false).returns(null);
        pageContextMock.fuzzy.setBaseURLProvider(
                mockFactory.expectsInstanceOf(BaseURLTracker.class));
        pageContextMock.expects.getProjectManager()
                .returns(projectManagerMock).any();
        pageContextMock.expects.getProjectStack().returns(projectStack).any();

        // =====================================================================
        //   Test Expectations
        // ====================================================================

        ContextInternals.setMarinerPageContext(context, pageContextMock);
        
        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from MarlinContentHandler interface
            public void setInitialRequestContext(
                    MarinerRequestContext requestContext) {
                calledMethod.setValue(true);
                assertSame("Unexpected value for request context.",
                        context, requestContext);
            }
        };        
        
        NamespaceSwitchContentHandler handler = 
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.setInitialRequestContext(context);
        
        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method setDocumentLocator     
     */ 
    public void testSetDocumentLocator() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final LocatorImpl locatorImpl = new LocatorImpl();
        
        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void setDocumentLocator(Locator locator) {
                calledMethod.setValue(true);
                assertSame("Unexpected value for Locator.", 
                        locatorImpl, locator);
                this.locator = locator;
            }
        };
        
        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.setDocumentLocator(locatorImpl);
        
        assertTrue("Expected method to be invoked.", calledMethod.getValue());
        assertSame("Unexpected value for Locator.", 
                locatorImpl, handler.getDocumentLocator());
    }

    /**
     * Test the method startDocument.     
     */ 
    public void testStartDocument() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        
        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void startDocument() throws SAXException {
                calledMethod.setValue(true);
            }
        };
        
        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.startDocument();
        
        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method endDocument     
     */ 
    public void testEndDocument() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);

        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void endDocument() throws SAXException {
                calledMethod.setValue(true);
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.endDocument();

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method startPrefixMapping     
     */ 
    public void testStartPrefixMapping() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final String prefixParam = "prefix";
        final String uriParam = "uri";

        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void startPrefixMapping(String s, String s1) 
                    throws SAXException {
                calledMethod.setValue(true);
                assertEquals("Unexpected parameter value", prefixParam, s);
                assertEquals("Unexpected parameter value", uriParam, s1);
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.startPrefixMapping(prefixParam, uriParam);

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method endPrefix mapping.     
     */ 
    public void testEndPrefixMapping() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final String prefixParam = "prefix";        

        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void endPrefixMapping(String s)
                    throws SAXException {
                calledMethod.setValue(true);
                assertEquals("Unexpected parameter value", prefixParam, s);                
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.endPrefixMapping(prefixParam);

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method characters.    
     */ 
    public void testCharacters() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final char[] charParam = new char[] {'x', 'y', 'z'}; 
        final int startParam = 37;
        final int lenParam = 13;
        
        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void characters(char[] chars, int i, int i1) 
                    throws SAXException {
                calledMethod.setValue(true);
                assertEquals("Unexpected param value.", charParam, chars);
                assertEquals("Unexpected param value.", startParam, i);
                assertEquals("Unexpected param value.", lenParam, i1);
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.characters(charParam, startParam, lenParam);

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method characters.    
     */ 
    public void testIgnorableWhitespace() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final char[] charParam = new char[]{' ', ' ', ' '};
        final int startParam = 39;
        final int lenParam = 17;

        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void ignorableWhitespace(char[] chars, int i, int i1) 
                    throws SAXException {
                calledMethod.setValue(true);
                assertEquals("Unexpected param value.", charParam, chars);
                assertEquals("Unexpected param value.", startParam, i);
                assertEquals("Unexpected param value.", lenParam, i1);
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.ignorableWhitespace(charParam, startParam, lenParam);

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method processingInstruction     
     */ 
    public void testProcessingInstruction() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final String targetParam = "target";
        final String dataParam = "data";

        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void processingInstruction(String s, String s1) 
                    throws SAXException {
                calledMethod.setValue(true);
                assertEquals("Unexpected parameter value", targetParam, s);
                assertEquals("Unexpected parameter value", dataParam, s1);
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.processingInstruction(targetParam, dataParam);

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Test the method skippedEntity     
     */ 
    public void testSkippedEntity() throws Exception {
        final BooleanWrapper calledMethod = new BooleanWrapper(false);
        final String nameParam = "name";

        AbstractMarlinContentHandler defaultHandler = new TestHandler() {
            // Javadoc inherited from ContentHandler interface
            public void skippedEntity(String s) throws SAXException {
                calledMethod.setValue(true);
                assertEquals("Unexpected parameter value", nameParam, s);
            }
        };

        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        handler.skippedEntity(nameParam);

        assertTrue("Expected method to be invoked.", calledMethod.getValue());
    }

    /**
     * Verify that specifying no namespace will result in the default handler
     * being used to process the content. This is for backwards compatibility.
     */
    public void testGetHandlerWithNullNamespace() throws SAXException {
        TestHandler defaultHandler = new TestHandler();
        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        MCSInternalContentHandler result = handler.getHandler(null);
        assertEquals("The default handler should be used if a null namespace " +
                "is specified", defaultHandler, result);

        result = handler.getHandler("");
        assertEquals("The default handler should be used if an empty string " +
                "is specified for the namespace ", defaultHandler, result);        
    }

    /**
     * Verify that an exception is thrown if an unrecognised namespace is
     * encountered.
     */
    public void testGetHandlerWithUnknownNamespace() {
        NamespaceSwitchContentHandlerMap map =
                NamespaceSwitchContentHandlerMap.getInstance();
        final String unknownNamespace = "unknown-namespace";
        assertNull(map.getContentHandlerFactory(unknownNamespace));

        TestHandler defaultHandler = new TestHandler();
        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        try {
            handler.getHandler(unknownNamespace);
            fail("Calling #getHandler with an unknown namespace should " +
                    "result in an exception being thrown");
        } catch (SAXException e) {
            // do nothing, this is the correct behaviour
        }
    }

    /**
     * Verify that specifying a namespace with a registered content handler
     * will result in that content handler being used to process the content.
     */
    public void testGetHandlerWithKnownNamespace() throws SAXException {

        NamespaceSwitchContentHandlerMap map =
                NamespaceSwitchContentHandlerMap.getInstance();
        assertNull(map.getContentHandlerFactory(knownNamespace));

        final TestHandler knownNamespaceHandler = new TestHandler();
        TestHandlerFactory knownNamespaceFactory = new TestHandlerFactory() {
            public MCSInternalContentHandler createContentHandler(
                MarinerRequestContext requestContext) {
                knownNamespaceHandler.setInitialRequestContext(requestContext);
                return knownNamespaceHandler;
            }
            public String[] getHandledNamespaces() {
                return new String[]{knownNamespace};
            }
        };
        map.addContentHandler(knownNamespace, knownNamespaceFactory);
        assertEquals(knownNamespaceFactory,
                map.getContentHandlerFactory(knownNamespace));

        TestHandler defaultHandler = new TestHandler();
        NamespaceSwitchContentHandler handler =
                new NamespaceSwitchContentHandler(defaultHandler);
        MCSInternalContentHandler result = handler.getHandler(knownNamespace);
        assertEquals("The handler that was registered against this namespace" +
                "is not the one that was returned",
                knownNamespaceHandler, result);
    }

    /**
     * Test class which follows the pattern of the actual implementing classes.
     */
    public static class TestHandlerFactory
            implements AbstractContentHandlerFactory {

        public MCSInternalContentHandler createContentHandler(
                MarinerRequestContext requestContext) {
            MCSInternalContentHandler handler = new TestGetHandlerContentHandler();
            handler.setInitialRequestContext(requestContext);
            return handler;
        }
        public String[] getHandledNamespaces() {
            return new String[]{knownNamespace};
        }
    }

    /**
     * Used to check that getHandler method behaves as expected.
     */ 
    public static class TestGetHandlerContentHandler extends TestHandler {
        
        /**
         * The MarinerRequestContext that we are expecting to be passed into
         * the setInitialRequestContext method.
         */ 
        public MarinerRequestContext context;
        
        /**
         * The Locator we are expecting to be passed into the 
         * setDocumentLocator method.
         */ 
        public Locator locator;

        /**
         * Flag to tell us if setInitialRequestContext has been invoked.
         */ 
        public boolean setRequestInvoked = false;
        
        /**
         * Flag to tell us if setDocumentLocator has been invoked.
         */ 
        public boolean setLocatorInvoked = false;
        
        /**
         * Create a new instance of TestGetHandlerContentHandler
         */
        public TestGetHandlerContentHandler() {            
        }
        
        // Javadoc inherited from MarlinContentHandler interface
        public void setInitialRequestContext(
                MarinerRequestContext requestContext) {
            context = requestContext;
            setRequestInvoked = true;
        }

        // Javadoc inherited from ContentHandler interface
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
            setLocatorInvoked = true;
        }
    }

    /**
     * A test version of AbstractMarlinContentHandler.
     */
    public static class TestHandler extends
            AbstractMarlinContentHandler {

        /**
         * The MarinerRequestContext
         */
        protected MarinerRequestContext initialContext;

        /**
         * The document Locator
         */
        protected Locator locator;

        public TestHandler() {
            super(null);
        }

        // Javadoc inherited from AbstractMarlinContentHandler
        public MarinerRequestContext getCurrentRequestContext() {
            return initialContext;
        }

        // Javadoc inherited from AbstractMarlinContentHandler
        public Locator getDocumentLocator() {
            return locator;
        }

        // Javadoc inherited from MarlinContentHandler interface
        public void setInitialRequestContext(MarinerRequestContext requestContext) {
            initialContext = requestContext;
        }

        // Javadoc inherited from ContentHandler interface
        public void setDocumentLocator(Locator locator) {
            this.locator = locator;
        }

        // Javadoc inherited from ContentHandler interface
        public void startDocument() throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void endDocument() throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void startPrefixMapping(String s, String s1) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void endPrefixMapping(String s) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void endElement(String s, String s1, String s2) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void characters(char[] chars, int i, int i1) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void processingInstruction(String s, String s1) throws SAXException {
        }

        // Javadoc inherited from ContentHandler interface
        public void skippedEntity(String s) throws SAXException {
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 05-Sep-05	9391/6	emma	VBM:2005082604 Handling empty string namespaces

 02-Sep-05	9391/4	emma	VBM:2005082604 Supermerge required

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Feb-04	2802/1	ianw	VBM:2004011921 Added mechanism to enable AppServer interfaces to configure NamespaceSwitchContentHandler

 27-Aug-03	1253/1	doug	VBM:2003082202 Restructured MarlinContentHandler class hierarchy

 13-Aug-03	1048/1	doug	VBM:2003070904 Modified MarlinContentHandlers so that they can control the flow of pipeline SAX events

 22-Jul-03	833/1	adrian	VBM:2003071902 added marlin support for invocation elements

 ===========================================================================
*/
