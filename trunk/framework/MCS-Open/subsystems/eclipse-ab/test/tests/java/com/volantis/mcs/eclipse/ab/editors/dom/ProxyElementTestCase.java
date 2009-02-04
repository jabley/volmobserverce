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

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.IllegalNameException;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * Test the ProxyElement class.
 */
public class ProxyElementTestCase extends TestCaseAbstract {

    /**
     * A valid <code>ProxyElement</code> instance.
     */
    private ProxyElement proxy;
    private MockProxyElementDetails details;
    private final ODOMFactory odomFactory = new ODOMFactory();

    // javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();
        details = new MockProxyElementDetails();
        proxy = new ProxyElement(details, odomFactory);
    }

    /**
     * Test the constructor.
     */
    public void testProxyElement() throws Exception {
        try {
            new ProxyElement(null, null);
            fail("Expected an NullPointerException.");
        } catch (NullPointerException e) {
        }
        try {
            new ProxyElement(null, odomFactory);
            fail("Expected an NullPointerException.");
        } catch (NullPointerException e) {
        }
        try {
            new ProxyElement(details, null);
            fail("Expected an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test addContent method(s).
     */
    public void testProxyElementAddContent() throws Exception {
        try {
            proxy.addContent(new Text(""));
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
        proxy.addContent(new ProxyText());
        try {
            proxy.addContent(new Element("element"));
            fail("Expected an IllegalStateException");
        } catch (IllegalStateException e) {
        }
        ProxyElement element = new ProxyElement(details, odomFactory);
        proxy.addContent(element);
        try {
            proxy.addContent(new ProcessingInstruction("a", ""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            proxy.addContent(new EntityRef("a"));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            proxy.addContent(new Comment(""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test removeContent method(s).
     */
    public void testRemoveContent() throws Exception {
        try {
            proxy.removeContent(new Text(""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
//        try {
            proxy.removeContent(new Element(("a")));
//            fail("Expected an UnsupportedOperationException");
//        } catch (UnsupportedOperationException e) {
//        }
        try {
            proxy.removeContent(new ProcessingInstruction("a", ""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            proxy.removeContent(new EntityRef("a"));
            proxy.removeContent(new Comment(""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            proxy.removeContent(new Comment(""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test setAttributes method(s).
     */
    public void testSetAttributes() throws Exception {
        try {
            proxy.setAttributes(new ArrayList());
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test setAttribute method(s).
     */
    public void testSetAttribute() throws Exception {
        try {
            proxy.setAttribute("attribute", "", null);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }

        try {
            // The proxy should reject this call because the given attribute is
            // not an EXTANT one
            proxy.setAttribute(new Attribute("a", ""));
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }

        try {
            // The proxy should reject this call because the given attribute is
            // not an EXTANT one
            proxy.setAttribute("attribute", "");
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test setAttribute method(s).
     */
    public void setSetAttributeValid() throws Exception {
        List list = new ArrayList(1);
        final ODOMElement element = (ODOMElement) odomFactory.element("test");
        element.setAttribute("attribute", "value");
        list.add(element);
        proxy.selectionChanged(new ODOMElementSelectionEvent(list));
        proxy.setAttribute("attribute", "");
    }

    /**
     * Test getAttributes method(s).
     */
    public void testGetAttributes() throws Exception {
        proxy.getAttributes();
    }

    /**
     * Test getAttribute method(s).
     */
    public void testGetAttribute() throws Exception {
        proxy.getAttribute("duff", Namespace.getNamespace("test",
                "uri:test"));

        assertNull("Should not have the duff attribute",
                proxy.getAttribute("duff"));

        List list = new ArrayList(1);
        final ODOMElement element = (ODOMElement) odomFactory.element("test");
        element.setAttribute("attribute", "value");
        list.add(element);

        details.setProxiedElements(list.iterator());

        proxy.selectionChanged(new ODOMElementSelectionEvent(list));

        Attribute attribute = proxy.getAttribute("attribute");

        assertNotNull("Attribute should exist",
                attribute);

        assertEquals("Attribute value not as",
                "value",
                proxy.getAttributeValue("attribute"));

        assertEquals("Attribute value not as",
                "value",
                attribute.getValue());

        attribute.setValue("test");

        assertEquals("Attribute value not as",
                "test",
                proxy.getAttributeValue("attribute"));
    }

    /**
     * Test the setName method
     */
    public void testSetName() throws Exception {
        try {
            proxy.setName("");
        } catch (IllegalNameException e) {
        }
        proxy.setName("testName");
    }

    /**
     * Test setAttribute method(s).
     */
    public void testSelectionChanged() throws Exception {
        try {
            proxy.selectionChanged(null);
            fail("Expected a IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
        }

        ODOMElementSelectionEvent event = new ODOMElementSelectionEvent(
                new ArrayList());
        // This is properly tested in ProxyElementDelegates test case.
        proxy.selectionChanged(event);
    }

    /**
     * Test setParent method(s).
     */
    public void testSetParent() throws Exception {
        // calculatePath method should be called (no way of easily checking
        // this - the method is private).
        proxy.setParent(null);
    }

    /**
     * Test setText method(s).
     */
    public void testSetText() throws Exception {
        proxy.addContent(new ProxyText());
        proxy.setText(null);
        proxy.setText("");
        proxy.setText("test");
    }

    /**
     * Test setContent method(s).
     */
    public void testSetContent() throws Exception {
        try {
            proxy.setContent(new ArrayList());
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test setRemoveChild method(s).
     */
    public void testRemoveChild() throws Exception {
        proxy.removeChild("");
        proxy.removeChild("", null);
    }

    /**
     * Test setRemoveChildren method(s).
     */
    public void testRemoveChildren() throws Exception {
        proxy.removeChildren("");
        proxy.removeChildren("", null);
    }

    /**
     * Test setRemoveAttribute method(s).
     */
    public void testRemoveAttribute() throws Exception {
        try {
            proxy.removeAttribute("");
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            proxy.removeAttribute("", null);
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
        try {
            proxy.removeAttribute(new Attribute("a", ""));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Test setChildren method(s).
     */
    public void testSetChildren() throws Exception {
        try {
            proxy.setChildren(new ArrayList());
            fail("setChildren is not expected to work for proxy elements");
        } catch (UnsupportedOperationException uoe) {
            // Expected exception
        }
    }

    /**
     * Test setNamespace method(s).
     */
    public void testSetNullNamespace() throws Exception {
        // Null namespaces are allowed
        proxy.setNamespace(null);
    }

    /**
     * Test setNamespace method(s).
     */
    public void testSetNonNullNamespace() throws Exception {
        try {
            proxy.setNamespace(Namespace.getNamespace("fruit"));
            fail("Expected an UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
        }
    }

    /**
     * Tests getting text of a proxied element.
     *
     * @throws Exception
     */
    public void testGetText() throws Exception {
        final String sampleText="quickbrownfox";
        // assertNull("Text should be null by default", proxy.getText());

        List list = new ArrayList(1);
        final ODOMElement element = (ODOMElement) odomFactory.element("test");
        element.setText(sampleText);
        list.add(element);

        proxy.addContent(new ProxyText());
        details.setProxiedElements(list.iterator());

        proxy.selectionChanged(new ODOMElementSelectionEvent(list));

        String text = proxy.getText();
        assertNotNull("Text should exist", text);

        assertEquals("Text value incorrect", sampleText, text);

        // Check that the proxy is updated correctly
        final String otherSample = "lazydog";
        element.setText(otherSample);

        text = proxy.getText();
        assertNotNull("Text should exist", text);

        assertEquals("Text value incorrect", otherSample, text);

        // Check that aggregation works correctly
        final ODOMElement otherElement =
                (ODOMElement) odomFactory.element("test");
        otherElement.setText(otherSample);

        list = new ArrayList(2);
        list.add(element);
        list.add(otherElement);

        proxy.selectionChanged(new ODOMElementSelectionEvent(list));

        // Identical text values should be passed through
        text = proxy.getText();
        assertNotNull("Text should exist", text);

        assertEquals("Text value incorrect", otherSample, text);

        // Different text values should aggregate as an empty string
        element.setText(sampleText);

        text = proxy.getText();
        assertNotNull("Text should exist", text);

        assertEquals("Text value incorrect", "", text);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Oct-05	9734/1	adrianj	VBM:2005100510 Allow text in ProxyElements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 15-Dec-03	2160/3	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 10-Dec-03	1968/5	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/3	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	2041/2	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 27-Nov-03	1968/1	richardc	VBM:2003111502 First draft for unit test

 12-Nov-03	1837/1	steve	VBM:2003110407 ProxyElement implementation

 ===========================================================================
*/
