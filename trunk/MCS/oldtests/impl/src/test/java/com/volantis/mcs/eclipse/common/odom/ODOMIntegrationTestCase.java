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
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URL;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.jdom.input.JDOMFactory;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Text;
import org.jdom.Namespace;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

/**
 * This integration test case ensures that the JDOM builder will generate
 * ODOM structures and that ODOM structures correctly trigger and propagate
 * change events.
 */
public class ODOMIntegrationTestCase extends TestCaseAbstract {
    /**
     * Class used to track the number of specific types of event notifications
     * received
     */
    class EventCounter implements ODOMChangeListener {
        protected Map count = new HashMap();

        /**
         * Optional command object that will be executed to perform
         * additional testing whenever a change event is received.
         */
        protected EventChangeCommand command;

        /**
         * Constructor
         */
        public EventCounter() {
            this(null);
        }

        /**
         * Constructor
         * @param command This will be executed every time an event is received
         */
        public EventCounter(EventChangeCommand command) {
            this.command = command;
        }

        // javadoc inherited
        public void changed(ODOMObservable node,
                            ODOMChangeEvent event) {
            Integer oldCount = (Integer)count.get(event.getChangeQualifier());
            int newCount = 1;

            if (oldCount != null) {
                newCount += oldCount.intValue();
            }

            count.put(event.getChangeQualifier(),
                      new Integer(newCount));

            if (command != null) {
                // execute any additional test
                command.execute(node, event);
            }
        }

        // javadoc unnecessary
        public int getEventCount(ChangeQualifier q) {
            Integer current = (Integer)count.get(q);
            int result = 0;

            if (current != null) {
                result = current.intValue();
            }

            return result;
        }

        // javadoc unnecessary
        public int getEventCount() {
            Iterator i = count.values().iterator();
            int result = 0;

            while (i.hasNext()) {
                Integer current = (Integer)i.next();

                if (current != null) {
                    result += current.intValue();
                }
            }

            return result;
        }

        /**
         * Checks that the event count is as expected
         * @param expectedEventCount the expected event count.
         */
        public void assertEventCount(int expectedEventCount) {
            assertEquals("event count not as",
                         expectedEventCount,
                         getEventCount());
        }

        public void reset() {
            count.clear();
        }
    }

    /**
     * Command interface that will be executed when the EventCounter receives
     * a change event
     */
    public interface EventChangeCommand {
        public void execute(ODOMObservable node,
                            ODOMChangeEvent event);
    }


    /**
     * A resource path that can be used to access resources in the same package
     * as the test case being executed.
     */
    protected String packagePath;

    /**
     * The ODOM factory to be used.
     */
    protected JDOMFactory factory = new ODOMFactory();

    /**
     * Namespace that some of the tests use.
     */
    protected Namespace ns = Namespace.getNamespace("x", "http://foo.com");

    // javadoc unnecessary
    public ODOMIntegrationTestCase(String name) {
        super(name);

        packagePath = getClass().getPackage().getName().replace('.', '/');
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Simple test that simply verifies that the ODOM factory can be used with
     * a JDOM SAXBuilder to create an ODOM structure.
     */
    public void testConstruction() throws Exception {
        Document doc = createDocumentFromString(
            factory,
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<ea a=\"ea\">" +
                "eatext" +
                "<eb a=\"ba\" b=\"bb\">" +
                    "<ec a=\"ca\" b=\"cb\" c=\"cc\">" +
                        "<![CDATA[ectext]]>" +
                    "</ec>" +
                    "ebtext" +
                "</eb>" +
            "</ea>");

        Element ea = doc.getRootElement();
        Attribute ea_eb_a = ea.getChild("eb").getAttribute("a");
        Text ea_eb_ec_text = (Text)ea.getChild("eb").getChild("ec").
            getContent().get(0);
        Text ea_eb_text = (Text)ea.getChild("eb").getContent().get(1);

        assertSame("Element ea class not as",
                   ODOMElement.class,
                   ea.getClass());

        assertSame("Attribute ea/eb@a class not as",
                   ODOMAttribute.class,
                   ea_eb_a.getClass());

        assertSame("Text ea/eb/ec/text() class not as",
                   ODOMCDATA.class,
                   ea_eb_ec_text.getClass());

        assertSame("Text ea/eb/text() class not as",
                   ODOMText.class,
                   ea_eb_text.getClass());
    }

    /**
     * Creates a simple document that some of the tests use.
     * @return the Document instance
     * @throws Exception if an error occurs
     */
    private Document createSimpleTestDocument() throws Exception {
        return createDocumentFromString(
                factory,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<ea a=\"ea\">" +
                    "<eb/>" +
                "</ea>");

    }

    ////////////////////////////////////////////////////////////////////////////
    // ODOMElement tests
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Tests that the {@link ODOMElement#detach} method
     * fires the change event after the change has been made
     */
    public void testDetach() throws Exception {
        final ODOMElement ea =
                (ODOMElement) createSimpleTestDocument().getRootElement();

        final ODOMElement eb = (ODOMElement) ea.getChild("eb");

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNull("event fired before element was detached",
                                    eb.getParent());
                    }
                });

        eb.addChangeListener(ec_1);

        // detach the element
        eb.detach();

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#setText} method
     * fires the change event after the change has been made
     */
    public void testSetText() throws Exception {
        final ODOMElement eb =
                (ODOMElement) createSimpleTestDocument().getRootElement().
                    getChild("eb");

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        if (node == eb) {
                            assertEquals("event fired before element updated",
                                         "hello",
                                          eb.getText());
                        }
                    }
                });

        eb.addChangeListener(ec_1);

        // set the text
        eb.setText("hello");

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#addContent} (Text) method
     * fires the change event after the change has been made
     */
    public void testStringAddContent() throws Exception {
        final ODOMElement ea =
                (ODOMElement) createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertEquals("event fired before element was updated",
                                     "hello",
                                      ea.getText());
                    }
                });

        ea.addChangeListener(ec_1);

        // add the hello element
        ea.addContent("hello");

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }


    /**
     * Tests that the {@link ODOMElement#addContent} (Element) method
     * fires the change event after the change has been made
     */
    public void testElementAddContent() throws Exception {
        final ODOMElement ea =
                (ODOMElement) createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNotNull("event fired before element was updated",
                                      ea.getChild("hello"));
                    }
                });

        ea.addChangeListener(ec_1);

        // add the child element.
        ea.addContent(factory.element("hello"));

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#addContent} (Text) method
     * fires the change event after the change has been made
     */
    public void testTextAddContent() throws Exception {
        final ODOMElement ea =
                (ODOMElement) createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertEquals("event fired before element was updated",
                                     "hello",
                                      ea.getText());
                    }
                });

        ea.addChangeListener(ec_1);

        // add the text node
        ea.addContent(factory.text("hello"));

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#removeChild} (String) method
     * fires the change event after the change has been made
     */
    public void testStringRemoveChild() throws Exception {
        final ODOMElement ea =
                (ODOMElement) createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNull("event fired before element was updated",
                                    ea.getChild("eb"));
                    }
                });

        ea.addChangeListener(ec_1);

        // remove the child element.
        ea.removeChild("eb");

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#removeChild} (String, Namespace)
     * method fires the change event after the change has been made
     */
    public void testNamespaceRemoveChild() throws Exception {
        Document doc = createDocumentFromString(
                    factory,
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<ea a=\"ea\">" +
                        "<x:eb xmlns:x=\"http://foo.com\"/>" +
                    "</ea>");

        final ODOMElement ea = (ODOMElement) doc.getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNull("event fired before element was updated",
                                    ea.getChild("eb", ns));
                    }
                });

        ea.addChangeListener(ec_1);

        // remove the child element.
        ea.removeChild("eb", ns);

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#removeChildren} (String)
     * method fires the change event after the change has been made
     */
    public void testRemoveChildren() throws Exception {
        Document doc = createDocumentFromString(
                    factory,
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<ea a=\"ea\">" +
                        "<eb/>" +
                        "<eb/>" +
                    "</ea>");

        final ODOMElement ea = (ODOMElement) doc.getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    int count = 0;
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        if (++count == 2) {
                            // both should have been in
                            assertNull("event fired before element was updated",
                                        ea.getChild("eb"));
                        }
                    }
                });

        ea.addChangeListener(ec_1);

        // remove the child elements.
        ea.removeChildren("eb");

        // check that an event was actually fired
        ec_1.assertEventCount(2);
    }


    /**
     * Tests that the {@link ODOMElement#removeChildren} (String, Namespace)
     * method fires the change event after the change has been made
     */
    public void testNamespaceRemoveChildren() throws Exception {
        Document doc = createDocumentFromString(
                    factory,
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<ea a=\"ea\">" +
                        "<x:eb xmlns:x=\"http://foo.com\"/>" +
                        "<x:eb xmlns:x=\"http://foo.com\"/>" +
                    "</ea>");

        final ODOMElement ea = (ODOMElement) doc.getRootElement();
        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    int count = 0;
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        if (count == 2) {
                            assertNull("event fired before element was updated",
                                        ea.getChild("eb", ns));
                        }
                    }
                });

        ea.addChangeListener(ec_1);

        // remove the child elements
        ea.removeChildren("eb", ns);

        // check that an event was actually fired
        ec_1.assertEventCount(2);
    }

    /**
     * Tests that the {@link ODOMElement#setAttributes} (String, Namespace)
     * method fires the change event after the change has been made
     */
    public void testSetAttributes() throws Exception {

        final ODOMElement eb = (ODOMElement)createSimpleTestDocument().
                getRootElement().getChild("eb");

        Attribute a1 = factory.attribute("a1", "v1");
        Attribute a2 = factory.attribute("a2", "v2");
        List attributes = new ArrayList(2);
        attributes.add(a1);
        attributes.add(a2);

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    int count = 0;
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNotNull("a1 attribute not set",
                                      eb.getAttribute("a1"));
                        if (++count == 2) {
                            assertNotNull("a2 attribute not set",
                                          eb.getAttribute("a2"));
                        }
                    }
                });

        eb.addChangeListener(ec_1);

        // set the attributes.
        eb.setAttributes(attributes);

        // check that an event was actually fired
        ec_1.assertEventCount(2);
    }

    /**
     * Tests that the {@link ODOMElement#setAttribute (Attribute)}
     * method fires the change event after the call has been made
     * to an element not already containing the attribute
     */
    public void testSetAttributeAttribute() throws Exception {

        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();

        Attribute a1 = factory.attribute("a1", "v1");

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNotNull("a1 attribute not set",
                                      ea.getAttribute("a1"));
                        assertEquals("wrong event change qualifier",
                                     ChangeQualifier.HIERARCHY,
                                     event.getChangeQualifier());
                    }
                });

        ea.addChangeListener(ec_1);

        // set the attribute.
        ea.setAttribute(a1);

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#setAttribute (String, String)}
     * method fires a hierarchy event after a call has been made to an
     * element not already containing the attribute
     */
    public void testSetAttributeStringString1() throws Exception {

        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNotNull("a1 attribute not set",
                                      ea.getAttribute("a1"));
                        assertEquals("wrong event change qualifier",
                                     ChangeQualifier.HIERARCHY,
                                     event.getChangeQualifier());
                    }
                });

        ea.addChangeListener(ec_1);

        // set the attribute.
        ea.setAttribute("a1", "v1");

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#setAttribute (String, String)}
     * method fires a change event after a call has been made to an
     * element already containing the attribute (but with a different value)
     */
    public void testSetAttributeStringString2() throws Exception {

        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();
                
        // Do this before the change listener is added
        ea.setAttribute("a1", "valueToBeReplaced");

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNotNull("a1 attribute not set",
                                      ea.getAttribute("a1"));
                        assertEquals("wrong event change qualifier",
                                     ChangeQualifier.ATTRIBUTE_VALUE,
                                     event.getChangeQualifier());
                    }
                });

        ea.addChangeListener(ec_1);

        // set the attribute.
        ea.setAttribute("a1", "v1");

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#setAttribute (String, String)}
     * method fires NO event after a call has been made to an element
     * already containing the attribute with the SAME value
     */
    public void testSetAttributeStringString3() throws Exception {

        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();
                
        // Do this before the change listener is added: same value as below
        ea.setAttribute("a1", "v1");

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        fail("Should be no events");
                    }
                });

        ea.addChangeListener(ec_1);

        // set the attribute.
        ea.setAttribute("a1", "v1");

        // check that an event was NOT fired
        ec_1.assertEventCount(0);
    }

    /**
     * Tests that the {@link ODOMElement#removeAttribute} (String)
     * method fires the change event after the change has been made
     */
    public void testStringRemoveAttribute() throws Exception {

        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNull("a attribute not removed",
                                    ea.getAttribute("a"));
                    }
                });

        ea.addChangeListener(ec_1);

        // remove the attribute.
        ea.removeAttribute("a");

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    /**
     * Tests that the {@link ODOMElement#removeAttribute} (Attribute)
     * method fires the change event after the change has been made
     */
    public void testRemoveAttribute() throws Exception {

        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNull("a attribute not removed",
                                    ea.getAttribute("a"));
                    }
                });

        ea.addChangeListener(ec_1);

        Attribute a = ea.getAttribute("a");
        assertNotNull("Attribute 'a' does not exist", a);

        // remove the attribute.
        ea.removeAttribute(ea.getAttribute("a"));

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // ODOMText tests
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Tests that the {@link ODOMText#detach} method
     * fires the change event after the change has been made
     */
    public void testTextDetach() throws Exception {

        Document doc = createDocumentFromString(
                factory,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<ea a=\"ea\">" +
                    "Hello" +
                "</ea>");

        final ODOMElement ea = (ODOMElement)doc.getRootElement();

        List children = ea.getContent();
        assertEquals("Should have one child text node", 1, children.size());

        final ODOMText text = (ODOMText) children.get(0);

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {

                        assertNull("event fired before text was detached",
                                    text.getParent());
                    }
                });

        text.addChangeListener(ec_1);

        // detach the text node
        text.detach();

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // ODOMCDATA tests
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Tests that the {@link ODOMCDATA#detach} method
     * fires the change event after the change has been made
     */
    public void testCDATADetach() throws Exception {

        Document doc = createDocumentFromString(
                factory,
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<ea a=\"ea\">" +
                    "<![CDATA[ectext]]>" +
                "</ea>");

        final ODOMElement ea = (ODOMElement)doc.getRootElement();

        List children = ea.getContent();
        assertEquals("Should have one child CDATA node", 1, children.size());

        final ODOMCDATA cdata = (ODOMCDATA) children.get(0);

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {
                        assertNull("event fired before CDATA was detached",
                                    cdata.getParent());
                    }
                });

        cdata.addChangeListener(ec_1);

        // detach the cdata node
        cdata.detach();

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    ////////////////////////////////////////////////////////////////////////////
    // ODOMAttribute tests
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Tests that the {@link ODOMAttribute#detach} method
     * fires the change event after the change has been made
     */
    public void testAttributeDetach() throws Exception {
        final ODOMElement ea =
                (ODOMElement)createSimpleTestDocument().getRootElement();

        final ODOMAttribute a =(ODOMAttribute) ea.getAttribute("a");

        EventCounter ec_1 = new EventCounter(
                new EventChangeCommand() {
                    public void execute(ODOMObservable node,
                                        ODOMChangeEvent event) {

                        assertNull("event fired before attribute was detached",
                                   a.getParent());
                    }
                });

        a.addChangeListener(ec_1);

        // detach the attribute.
        a.detach();

        // check that an event was actually fired
        ec_1.assertEventCount(1);
    }

    public void testHierarchicalEventNotification() throws Exception {
        Document doc = createDocumentFromString(
            factory,
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<ea a=\"ea\">" +
                "eatext" +
                "<eb a=\"ba\" b=\"bb\">" +
                    "<ec a=\"ca\" b=\"cb\" c=\"cc\">" +
                        "ectext" +
                    "</ec>" +
                    "ebtext" +
                "</eb>" +
            "</ea>");

        ODOMElement ea = (ODOMElement)doc.getRootElement();
        ODOMElement ea_eb_ec = (ODOMElement)ea.getChild("eb").getChild("ec");
        ODOMAttribute ea_eb_ec_c = (ODOMAttribute)ea_eb_ec.getAttribute("c");

        EventCounter ea_el = new EventCounter();
        EventCounter ea_eb_ec_el = new EventCounter();
        EventCounter ea_eb_ec_c_el = new EventCounter();

        ea.addChangeListener(ea_el);
        ea_eb_ec.addChangeListener(ea_eb_ec_el, ChangeQualifier.NAME);
        ea_eb_ec_c.addChangeListener(ea_eb_ec_c_el);

        // This should propagate just one event
        ea_eb_ec_c.setValue("New Value");

        assertEquals("1. ea event count not as",
                     1,
                     ea_el.getEventCount());

        assertEquals("1. ea/eb/ec event count not as",
                     0,
                     ea_eb_ec_el.getEventCount());

        assertEquals("1. ea/eb/ec/@c event count not as",
                     1,
                     ea_eb_ec_c_el.getEventCount());

        // This should propagate just one event
        ea_eb_ec_c.setName("newc");

        assertEquals("2. ea event count not as",
                     2,
                     ea_el.getEventCount());

        assertEquals("2. ea/eb/ec event count not as",
                     1,
                     ea_eb_ec_el.getEventCount());

        assertEquals("2. ea/eb/ec/@c event count not as",
                     2,
                     ea_eb_ec_c_el.getEventCount());

        // This will remove the old text and add the new, thus generating
        // two events
        ea_eb_ec.setText("New text");

        assertEquals("3. ea event count not as",
                     4,
                     ea_el.getEventCount());

        assertEquals("3. ea/eb/ec event count not as",
                     1,
                     ea_eb_ec_el.getEventCount());

        assertEquals("3. ea/eb/ec/@c event count not as",
                     2,
                     ea_eb_ec_c_el.getEventCount());

        // Only one event is expected as this overloading just changes the
        // value for an existing attribute
        ea_eb_ec.setAttribute("newc", "Another Value");

        assertEquals("4. ea event count not as",
                     5,
                     ea_el.getEventCount());

        assertEquals("4. ea/eb/ec event count not as",
                     1,
                     ea_eb_ec_el.getEventCount());

        assertEquals("4. ea/eb/ec/@c event count not as",
                     3,
                     ea_eb_ec_c_el.getEventCount());
    }


    public void testCloning() throws Exception {
        Document doc = createDocumentFromString(
            factory,
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<ea a=\"ea\">" +
                "eatext" +
                "<eb a=\"ba\" b=\"bb\">" +
                    "<ec a=\"ca\" b=\"cb\" c=\"cc\">" +
                        "<![CDATA[ectext]]>" +
                    "</ec>" +
                    "ebtext" +
                "</eb>" +
            "</ea>");

        ODOMElement ea = (ODOMElement)doc.getRootElement();
        ODOMElement ea_eb_ec = (ODOMElement)ea.getChild("eb").getChild("ec");
        ODOMAttribute ea_eb_ec_c = (ODOMAttribute)ea_eb_ec.getAttribute("c");
        EventCounter ea_eb_ec_el = new EventCounter();
        ODOMElement ea_clone;
        ODOMElement ea_eb_ec_clone;

        ea_eb_ec.addChangeListener(ea_eb_ec_el);

        ea_clone = (ODOMElement)ea.clone();
        ea_eb_ec_clone = (ODOMElement)ea_clone.getChild("eb").getChild("ec");

        // Only the change to the original ea/eb/ec/@c should trigger an event
        // to the registered change listener
        ea_eb_ec_c.setValue("new value");
        ea_eb_ec_clone.setName("eclone");

        assertEquals("ea/eb/ec event count not as",
                     1,
                     ea_eb_ec_el.getEventCount());

        // Now make sure that the clone changes only affected the clone
        // instance and not the original
        assertEquals("ea/eb/ec name not as",
                     "ec",
                     ea_eb_ec.getName());

        assertEquals("cloned ea/eb/eclone/@c value not as",
                     "cc",
                     ea_eb_ec_clone.getAttributeValue("c"));

        // And vice versa
        assertEquals("ea/eb/ec/@c value not as",
                     "new value",
                     ea_eb_ec_c.getValue());
    }

    /**
     * This "template" integration test is provided for validation testing and
     * doesn't do much yet.
     */
    public void testInvalidButWellformedDocument() throws Exception {
        URL url = getClass().getResource("ODOMIntegrationTestCase.dtd");

        String input =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<!DOCTYPE root SYSTEM \"" + url.toExternalForm() + "\">" +
            "<root>" +
                "<head>" +
                    "<p>Not allowed here</p>" +
                "</head>" +
                "<body>" +
                    "<p colour=\"invalid\">Hello World</p>" +
                "</body>" +
            "</root>";

        SAXBuilder builder = new SAXBuilder();
        Document doc;

        // This error handler quietly consumes all errors
        ErrorHandler errorHandler = new ErrorHandler() {
            public void warning(SAXParseException e) throws SAXException {
                System.out.println("Warning reported: " + e);
                e.printStackTrace(System.out);
            }

            public void error(SAXParseException e) throws SAXException {
                System.out.println("Error reported: " + e);
                e.printStackTrace(System.out);
            }

            public void fatalError(SAXParseException e) throws SAXException {
                System.out.println("Fatal Error reported: " + e);
                e.printStackTrace(System.out);
            }
        };

        builder.setFactory(factory);
        builder.setErrorHandler(errorHandler);
        builder.setValidation(true);

        doc = builder.build(new StringReader(input));

        assertTrue("Document not returned",
                   doc != null);
    }

    /**
     * Uses a JDOM SAXBuilder to create a JDOM Document, using the given
     * factory, from a given XML string.
     *
     * @param factory the JDOM factory used to create the JDOM Document
     * @param input   the source XML
     * @return a JDOM Document
     */
    protected Document createDocumentFromString(JDOMFactory factory,
                                                String input)
        throws Exception {
        SAXBuilder builder = new SAXBuilder();

        builder.setFactory(factory);

        return builder.build(new StringReader(input));
    }

    /**
     * Uses a JDOM SAXBuilder to create a JDOM Document, using the given
     * factory, from an XML resource in the test case's package with the given
     * (packageless) resource name.
     *
     * @param factory   the JDOM factory used to create the JDOM Document
     * @param inputName the resource file name (without any package)
     * @return a JDOM Document
     */
    protected Document createDocumentFromResource(JDOMFactory factory,
                                                  String inputName)
        throws Exception {
        URL inputURL = getResourceURL(packagePath + '/' + inputName);

        SAXBuilder builder = new SAXBuilder();

        builder.setFactory(factory);

        return builder.build(inputURL);
    }

    /**
     * Returns a URL to a given named resource.
     *
     * @param resource the resource to look for.
     * @return the URL to the resource
     */
    protected URL getResourceURL(String resource) {
        ClassLoader cl;
        URL url = null;

        // Use the context class loader first if any.
        cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            url = cl.getResource(resource);
        }

        // If that did not work then use the current class's class loader.
        if (url == null) {
            cl = getClass().getClassLoader();
            url = cl.getResource(resource);
        }

        // If that did not work then use the system class loader.
        if (url == null) {
            cl = ClassLoader.getSystemClassLoader();
            url = cl.getResource(resource);
        }

        return url;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 15-Dec-03	2223/3	richardc	VBM:2003121203 Fixed assert args order and test method name conventions

 15-Dec-03	2223/1	richardc	VBM:2003121203 setAttribute(String,String) checks for existence of attribute first

 15-Dec-03	2160/4	doug	VBM:2003120702 Modified ODOMObservables so that they can validate themesevles.

 13-Dec-03	2198/1	doug	VBM:2003121003 Ensured ODOMObservables generate notification after the change has occurred

 07-Nov-03	1813/1	philws	VBM:2003110520 Add ODOMCDATA and provide correct clone feature

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
