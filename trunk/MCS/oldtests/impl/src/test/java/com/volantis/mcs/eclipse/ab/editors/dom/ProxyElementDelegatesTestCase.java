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

package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Document;

/**
 * Test the ProxyElementDelegates object.
 */
public class ProxyElementDelegatesTestCase
        extends TestCaseAbstract {

    /**
     * Mock implementation of the ProxyDetails interface.
     */
    private MockProxyElementDetails proxyElementDetails =
            new MockProxyElementDetails();

    /**
     * The delegates object.
     */
    private ProxyElementDelegates delegates;

    /**
     * Mock attributes change listener.
     */
    // TODO RESTORE
//    private MockAttributesChangeListener listener = new MockAttributesChangeListener();

    /**
     * Constants
     */
    private static final String COMPANY = "company";
    private static final String DIRECTOR_AGE = "directorAge";
    private static final String DIRECTOR = "director";
    private static final String STAFF = "staff";
    private static final String DIRECTOR_NAME = "directorName";


    // javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        // TODO RESTORE
//        delegates = new ProxyElementDelegates(new ODOMFactory(),
//                proxyElementDetails, listener);
    }

    /**
     * Test Constructor
     */
    public void testProxyElementDelegates() throws Exception {
        // TODO RESTORE
//        assertNotNull(PrivateAccessor.getField(delegates, "sourceTargetPairs"));
//
//        ProxyElementDetails details = (ProxyElementDetails) PrivateAccessor.
//                getField(delegates, "proxyElementDetails");
//        assertTrue("Details should match", details == proxyElementDetails);

    }

    /**
     * Helper method to create a simple company document.
     */
    private Document createCompanyDocument() {
        String xml =
            "<root id=\"root\">" +
                "<company companyName=\"volantis\">" +
                    "<staff staffCount=\"5\">" +
                        "<director directorName=\"bert\" directorAge=\"46\">" +
                        "</director>" +
                    "</staff>" +
                "</company>" +
                "<company companyName=\"eds\">" +
                    "<staff staffCount=\"5000\">" +
                        "<director directorName=\"chuck\" directorAge=\"46\">" +
                        "</director>" +
                    "</staff>" +
                "</company>" +
            "</root>";

        // TODO RESTORE
        //return createDocument(xml);
        return null;
    }

    /**
     * Helper method to create a simple cd collection document.
     */
    private Document createCDCatalogDocument() {
        String xml =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<catalog>" +
                "  <cd country=\"USA\">" +
                "    <title attribute=\"value\">Empire Burlesque</title>" +
                "    <artist>Bob Dylan</artist>" +
                "    <price>10.90</price>" +
                "  </cd>" +
                "  <cd country=\"UK\">" +
                "    <title>Hide your heart</title>" +
                "    <artist>Bonnie Tyler</artist>" +
                "    <price>9.90</price>" +
                "  </cd>" +
                "  <cd country=\"USA\">" +
                "    <title>Greatest Hits</title> " +
                "    <artist>Dolly Parton</artist>" +
                "    <price>9.90</price>" +
                "  </cd>" +
                "  <cd country=\"Australia\">" +
                "    <title>Down Under with Bruce</title> " +
                "    <artist>Rolf Harris</artist>" +
                "    <price></price>" +
                "  </cd>" +
                "</catalog>";

        // Translate the cd collection into an xml string.
        // TODO RESTORE
        //return createDocument(xml);
        return null;
    }

    /**
     * Test the propogation of updates on an attribute.
     */
    public void testApplySelectionUpdateAttribute() throws Exception {

// TODO RESTORE!
//        Document doc = createCompanyDocument();
//        doApplySelectionTest(doc, new XPath("staff/director"), "", "46", 1);
//
//        XPath selector = new XPath("company/staff/director");
//        final ODOMObservable context = (ODOMObservable)doc.getRootElement();
//
//        // Test what happens if the first director changes his age.
//        List directors = selector.selectNodes(context);
//        ODOMAttribute attr;
//        attr = (ODOMAttribute) ((Element) directors.get(0)).getAttribute(DIRECTOR_AGE);
//        attr.setValue("101");
//        assertEquals("Value should match", "",
//                listener.proxy.getAttributeValue(DIRECTOR_AGE));
//
//        // Test what happens if the second director changes his age to the
//        // same as the first director (should be aggregated again).
//        selector = new XPath("company/staff/director[2]");
//        attr = (ODOMAttribute)((Element) directors.get(1)).getAttribute(DIRECTOR_AGE);
//        attr.setValue("101");
//        assertEquals("Value should match", "101",
//                listener.proxy.getAttributeValue(DIRECTOR_AGE));
    }

//  TODO RESTORE!
//    /**
//     * Test the propogation of deletes on an attribute.
//     */
//    public void testApplySelectionDeleteAttribute() throws Exception {
//        Document doc = createCompanyDocument();
//        int calls = 0;
//        doApplySelectionTest(doc, new XPath("staff/director"), "", "46",
//                ++calls);
//
//        delegates.propagateSetAttribute("directorName", "");
//        doApplySelectionTest(doc, new XPath("staff/director"), null, "46",
//                ++calls);
//
//        // TODO Re-instate this after 2003120301 has been fixed
// ////       delegates.propagateSetAttribute("directorAge", "");
// ////       doApplySelectionTest(doc, new XPath("staff/director"), null, null,
// ////               ++calls);
//
//        // TODO Re-instate this after 2003120301 has been fixed
// ////       delegates.propagateSetAttribute("value", "ddd");
// ////       doApplySelectionTest(doc, new XPath("staff/director"), null, null,
// ////               ++calls);
//
//        // Attempt to add an attribute and value that does not exist on the
//        // proxied element.
//        // TODO Re-instate this after 2003120301 has been fixed
//////       delegates.propagateSetAttribute("value", "ddd");
// ////       assertEquals("No attribute should exist on proxied Element",
// ////               0, listener.proxy.getAttributes().size());
//
//        showJDOM(listener.proxy);
//    }
//
//    /**
//     * Test the propogation of deletes on an attribute where parent items
//     * are deleted too.
//     */
//    public void testApplySelectionDeleteAttributeDeleteParents() throws Exception {
//        String xml =
//            "<root id=\"root\">" +
//                "<company>" + // should NOT be deleted.
//                    "<staff>" + // should be deleted.
//                        "<director directorName=\"bert\">" + // should be deleted.
//                        "</director>" +
//                    "</staff>" +
//                "</company>" +
//                "<company companyName=\"eds\">" + // should NOT be deleted.
//                "</company>" +
//            "</root>";
//        Document doc = createDocument(xml);
//
//        int calls = 0;
//        // TODO Re-instate this after 2003120301 has been fixed
// ////       doApplySelectionTest(doc, new XPath("staff/director"), "bert", null,
// ////               ++calls);
//
//        assertEquals("Root child count should match", 2,
//                doc.getRootElement().getChildren().size());
//
//        // The deletion of the directorName attribute should cause the
//        // deletion of all parents (excluding the context).
//        delegates.propagateSetAttribute("directorName", "");
//        // TODO Re-instate this after 2003120301 has been fixed
//////        doApplySelectionTest(doc, new XPath("staff/director"), null, null,
//////                ++calls);
//
//        showJDOM(doc);
//
//        final List children = doc.getRootElement().getChildren();
//        assertEquals("Root child count should match", 2, children.size());
//        assertNull("No attribute value expected",
//                ((Element)children.get(0)).getAttributeValue("companyName"));
//
//        assertEquals("Attribute name should match", "eds",
//                ((Element)children.get(1)).getAttributeValue("companyName"));
//    }
//
//    /**
//     * Test the attribute addition with namespaces.
//     */
//    public void testAttributeAdditionWithNamespaces() throws Exception {
//        String prefix = "test";
//        String uri = "http://www.volantis.com/testnamespace";
//        String ns = prefix + ":";
//        String xml =
//            "<root xmlns:" + prefix + "=\"" + uri + "\" " +
//                    "id=\"root\">" +
//                "<" + ns + "company>" +
//                    "<" + ns + "staff>" +
//                        "<" + ns + "director directorName=\"bert\">" +
//                        "</" + ns + "director>" +
//                    "</" + ns + "staff>" +
//                "</" + ns + "company>" +
//                "<" + ns + "company companyName=\"eds\">" +
//                "</" + ns + "company>" +
//            "</root>";
//        Document doc = createDocument(xml);
//
//        Namespace namespaces[] = {
//            Namespace.getNamespace(prefix, uri)
//        };
//
//        final XPath xpath = new XPath(ns + "staff/" + ns + "director", namespaces);
//        final Element child = doc.getRootElement().getChild("company", namespaces[0]);
//        List elements = xpath.selectNodes((ODOMObservable) child);
//        int calls = 0;
//
//        // TODO Re-instate this after 2003120301 has been fixed
//////        doApplySelectionTest(doc, xpath, "bert", null, ++calls);
//
//        showJDOM(doc);
//    }
//
//    /**
//     * Test the applying of a selection.
//     */
//    public void testApplySelectionMultipleCalls() throws Exception {
//
//        assertEquals("Proxy element set count should match.",
//                proxyElementDetails.getSetProxiedElementsCallCount(), 0);
//
//        doApplySelectionTest(createCompanyDocument(),
//                new XPath("staff/director"), "", "46", 1);
//
//        // Deliberately perform the applySelection again.
//        doApplySelectionTest(createCompanyDocument(),
//                new XPath("staff/director"), "", "46", 2);
//    }
//
//
//    /**
//     * Helper method to perform the test.
//     */
//    private void doApplySelectionTest(Document doc,
//                                      XPath xpath,
//                                      String expectedDirectorName,
//                                      String expectedAge,
//                                      int setProxiedCallCount) throws Exception {
//        showJDOM(doc);
//
//        List elements = doc.getRootElement().getChildren();
//        ODOMElementSelectionEvent event = new ODOMElementSelectionEvent(elements);
//        delegates.applySelection(event, xpath);
//
//        // Aggregrated value attribute 'directorName' should match.
//        assertEquals("Attribute value match", expectedDirectorName,
//                listener.proxy.getAttributeValue(DIRECTOR_NAME));
//
//        // Aggregrated value attribute 'directorAge' should match.
//        assertEquals("Attribute value match", expectedAge,
//                listener.proxy.getAttributeValue(DIRECTOR_AGE));
//
//        assertEquals("Proxy element set count should match.",
//                proxyElementDetails.getSetProxiedElementsCallCount(),
//                setProxiedCallCount);
//
//        listener.dump();
//    }
//
//    /**
//     * Test apply selection when no values found.
//     */
//    public void testApplySelectionNoValuesToChange() throws Exception {
//
//        doApplySelectionTest(createCompanyDocument(), new XPath("dummy/path"),
//                null, null, 1);
//    }
//
//    /**
//     * Test the event registration and de-registration. In particular, ensure
//     * that:
//     * <ol>
//     * <li>If a listener is registered then deregistrations does break the
//     * coupling and no events permeate through (garbage must be eligible for
//     * removal).</li>
//     * <li></li>
//     * </ol>
//     */
//    public void testEventRegistration() throws Exception {
//        // Translate the cd collection into an xml string.
//        Document doc = createCDCatalogDocument();
//        showJDOM(doc);
//
//        ODOMElement odomElements[] = {
//            (ODOMElement)doc.getRootElement().getChild("cd")
//        };
//
//        // Simulate a selection event change.
//        ODOMElementSelectionEvent event = new ODOMElementSelectionEvent(odomElements);
//        XPath sourceToTargetXPath = new XPath("title");
//        delegates.applySelection(event, sourceToTargetXPath);
//
//        showJDOM(listener.proxy);
//    }
//
//    /**
//     * Create a JDOM document from the input xml string.
//     * @param xml the input xml string.
//     * @return the newly created JDOM document.
//     */
//    private Document createDocument(String xml) {
//        JDOMFactory factory = new ODOMFactory();
//
//        SAXBuilder builder = new SAXBuilder();
//        builder.setFactory(factory);
//
//        Document document = null;
//        try {
//            document = builder.build(new StringReader(xml));
//        } catch (JDOMException e) {
//            e.printStackTrace();
//            fail("Could not create document with input: " + xml);
//        } catch (IOException e) {
//            e.printStackTrace();
//            fail("Could not create document with input: " + xml);
//        }
//        return document;
//    }
//
//    /**
//     * Test the applySelection method with dud paramaters.
//     */
//    public void testApplySelectionDudParameters() throws Exception {
//        try {
//            delegates.applySelection(null, null);
//            fail("Expected a IllegalArgumentException.");
//        } catch (IllegalArgumentException e) {
//        }
//
//        try {
//            delegates.applySelection(null, new XPath("test"));
//            fail("Expected a IllegalArgumentException.");
//        } catch (IllegalArgumentException e) {
//        }
//
//        ODOMElement original = (ODOMElement)listener.proxy.clone();
//        List elements = new ArrayList();
//        ODOMElementSelectionEvent event = new ODOMElementSelectionEvent(elements);
//        delegates.applySelection(event, null);
//
//        assertEquals("Original and new proxy name should match",
//                original.getName(), listener.proxy.getName());
//
//        assertEquals("Original and new proxy children should match",
//                original.getChildren(), listener.proxy.getChildren());
//
//        assertEquals("Original and new proxy namespace should match",
//                original.getNamespace(), listener.proxy.getNamespace());
//
//        assertEquals("Original and new proxy attributes should match",
//                original.getAttributes(), listener.proxy.getAttributes());
//    }
//
//    /**
//     * Test the propagateSetAttribute method with dud values.
//     */
//    public void testPropagateSetAttributeDudValues() throws Exception {
//        try {
//            delegates.propagateSetAttribute(null, null);
//            fail("Expected a IllegalArgumentException.");
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            delegates.propagateSetAttribute("", null);
//            fail("Expected a IllegalArgumentException.");
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            delegates.propagateSetAttribute(null, "");
//            fail("Expected a IllegalArgumentException.");
//        } catch (IllegalArgumentException e) {
//        }
//        try {
//            delegates.propagateSetAttribute("", "");
//            fail("Expected a IllegalArgumentException.");
//        } catch (IllegalArgumentException e) {
//        }
//    }
//
//    /**
//     * Show the XML element tree structure starting with the root Element passed
//     * in.
//     */
//    private void showJDOM(Element root) throws IOException {
//        XMLOutputter outputter = new XMLOutputter();
//        outputter.setTextTrim(true);
//        outputter.setNewlines(true);
//        StringWriter writer = new StringWriter();
//        outputter.output(root, writer);
//        System.out.println("/= START =\\");
//        System.out.println(writer.toString());
//        System.out.println("\\= END =/");
//        System.out.println("");
//    }
//
//    /**
//     * Show the XML document.
//     */
//    private void showJDOM(Document document) throws IOException {
//        showJDOM(document.getRootElement());
//    }
//
//    /**
//     * Test the getAggregate value method.
//     */
//    public void testGetAggregateValue() throws Exception {
//        Class args[] = { String.class };
//        String params[] = { null };
//        final String methodName = "getAggregateValue";
//        try {
//            String result = (String)PrivateAccessor.invoke(
//                    delegates, methodName, args, params);
//            assertNotNull("Result shouldn't be null", result);
//            assertEquals("Result should match", "", result);
//
//            PrivateAccessor.getField(delegates, "sourceTargetPairs");
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//            fail("Unable to invoke method: " + methodName);
//        }
//    }
//
//    /**
//     * Test the setting of the attributes name.
//     */
//    public void testPropagateSetAttributes() throws Exception {
//        final Document doc = createCompanyDocument();
//        doApplySelectionTest(doc, new XPath("staff/director"), "", "46", 1);
//
//        Boolean ignoreChangeEvents;
//        final String fieldName = "ignoreChangeEvents";
//        try {
//            ignoreChangeEvents = (Boolean) PrivateAccessor.getField(
//                    delegates, fieldName);
//
//            assertTrue("Ignore change events should be false",
//                    !ignoreChangeEvents.booleanValue());
//
//            delegates.propagateSetAttribute("", "");
//
//            fail("Expected an IllegalArgumentException");
//        } catch (IllegalArgumentException e) {
//            ignoreChangeEvents = (Boolean) PrivateAccessor.getField(
//                    delegates, fieldName);
//            assertTrue("Ignore change events should be false",
//                    !ignoreChangeEvents.booleanValue());
//        }
//    }
//
//    /**
//     * Test the setting of the element name.
//     */
//    public void testPropagateSetName() throws Exception {
//        final Document doc = createCompanyDocument();
//        doApplySelectionTest(doc, new XPath("staff/director"), "", "46", 1);
//
//        doTestElementRename("newname", doc);
//        doTestElementRename("anotherName", doc);
//        final String fieldName = "ignoreChangeEvents";
//        Boolean ignoreChangeEvents;
//        try {
//
//            ignoreChangeEvents = (Boolean) PrivateAccessor.getField(
//                    delegates, fieldName);
//            assertTrue("Ignore change events should be false",
//                    !ignoreChangeEvents.booleanValue());
//
//            doTestElementRename("Invalid Name", doc);
//            fail("Expected an IllegalNameException");
//
//        } catch (IllegalNameException e) {
//            ignoreChangeEvents = (Boolean) PrivateAccessor.getField(
//                    delegates, fieldName);
//            assertTrue("Ignore change events should be false",
//                    !ignoreChangeEvents.booleanValue());
//        }
//    }
//
//    /**
//     * Helper to test the renaming of elements.
//     */
//    private void doTestElementRename(final String name, final Document doc)
//            throws IOException, XPathException {
//
//        delegates.propagateSetName(name);
//        showJDOM(doc);
//
//        List elementsRenamed = new XPath("company/staff/" + name).selectNodes(
//                (ODOMObservable)doc.getRootElement());
//        assertEquals("List size should match", 2, elementsRenamed.size());
//
//        for (int i = 0; i < elementsRenamed.size(); i++) {
//            Element element = (Element) elementsRenamed.get(i);
//            assertEquals("Name should match", name, element.getName());
//        }
//    }
//
//    /**
//     * Mock attributes change listener.
//     */
//    private class MockAttributesChangeListener
//            implements ProxyElementDelegates.AggregationListener {
//
//        private final ODOMElement proxy = new ODOMElement("proxy");
//
//        // javadoc inherited
//        public void newAttribute(String attribName, String attribValue)
//                throws IllegalStateException {
//            System.out.println("Received new Attribute: '" + attribName +
//                    "' with value '" + attribValue + "'");
//            if (proxy.getAttribute(attribName) != null) {
//                throw new IllegalArgumentException("Already had " + attribName);
//            }
//            proxy.setAttribute(attribName, attribValue);
//        }
//
//        // javadoc inherited
//        public void updatedAttributeValue(String attribName, String attribValue)
//                throws IllegalStateException {
//
//            System.out.println("Received updateValue: '" + attribName +
//                    "' with value '" + attribValue + "'");
//            final Attribute attribute = proxy.getAttribute(attribName);
//            if (attribute == null) {
//                throw new IllegalArgumentException("Missing " + attribName);
//            }
//            attribute.setValue(attribValue);
//        }
//
//        // javadoc inherited
//        public void deletedAttribute(String attribName)
//                throws IllegalStateException {
//
//            System.out.println("Received deleteAttribute: '" + attribName + "'");
//
//            if (proxy.getAttribute(attribName) == null) {
//                throw new IllegalArgumentException("Missing " + attribName);
//            }
//            proxy.removeAttribute(attribName);
//        }
//
//        // javadoc inherited
//        public void updatedElementNamespace(Namespace namespace) {
//            // TODO
//        }
//
//        public void updatedElementName(String elementName) {
//            // TODO
//        }
//
//        // javadoc inherited
//        public void dump() {
//            Iterator attribsIter = proxy.getAttributes().iterator();
//            while (attribsIter.hasNext()) {
//                Attribute attribute = (Attribute) attribsIter.next();
//                System.out.println(attribute.getName() + " = " + attribute.getValue());
//            }
//        }
//
//    }
//
//    /**
//     * Mock ProxyElementDelegates class.
//     */
//    private class MockProxyElementDelegates extends ProxyElementDelegates {
//        public MockProxyElementDelegates(ODOMFactory odomFactory,
//                                         ProxyElementDetails proxyElementDetails,
//                                         ProxyElementDelegates.AggregationListener aggregationListener) {
//            super(odomFactory, proxyElementDetails, aggregationListener);
//        }
//
////        protected ProxyElementDelegates.SourceTargetPairs getSourceTargetPairs() {
////
////        }
////
////        protected void add(Element sourceElement1, Element sourceElement2) {
////            getSourceTargetPairs().add(sourceElement1, sourceElement2);
////        }
//    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Dec-03	1968/5	richardc	VBM:2003111502 Second semi-tested draft for code release

 10-Dec-03	1968/3	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/1	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	2041/1	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 ===========================================================================
*/
