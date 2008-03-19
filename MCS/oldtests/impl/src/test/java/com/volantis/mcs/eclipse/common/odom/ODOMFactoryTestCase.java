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
import org.jdom.input.JDOMFactory;
import org.jdom.Namespace;
import org.jdom.Attribute;

/**
 * Test case for {@link ODOMFactory}.
 */
public class ODOMFactoryTestCase extends TestCaseAbstract {
    /**
     * The factory being tested.
     */
    protected JDOMFactory factory;

    /**
     * A usefull Namespace instance for use in various tests
     */
    protected Namespace namespace;

    // javadoc unnecessary
    public ODOMFactoryTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    public void setUp() throws Exception {
        factory = new ODOMFactory();
        namespace =
            Namespace.getNamespace("abc",
                                   "http://www.volantis.com/xmlns/abc");

        super.setUp();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();

        namespace = null;
        factory = null;
    }

    // javadoc unnecessary
    public void testAttribute() throws Exception {
        assertEquals("attribute(name, value, namespace) class not as",
                     ODOMAttribute.class.getName(),
                     factory.attribute("name", "value",
                                       namespace).getClass().getName());

        assertEquals("attribute(name, value, type, namespace) class not as",
                     ODOMAttribute.class.getName(),
                     factory.attribute("name", "value",
                                       Attribute.NMTOKEN_ATTRIBUTE,
                                       namespace).getClass().getName());

        assertEquals("attribute(name, value) class not as",
                     ODOMAttribute.class.getName(),
                     factory.attribute("name", "value").getClass().getName());

        assertEquals("attribute(name, value, type) class not as",
                     ODOMAttribute.class.getName(),
                     factory.attribute("name", "value",
                                       Attribute.NMTOKEN_ATTRIBUTE).getClass().
                     getName());
    }

    // javadoc unnecessary
    public void testText() throws Exception {
        assertEquals("text(text) class not as",
                     ODOMText.class.getName(),
                     factory.text("textual content").getClass().getName());
    }

    // javadoc unnecessary
    public void testElement() throws Exception {
        assertEquals("element(name, namespace) class not as",
                     ODOMElement.class.getName(),
                     factory.element("name", namespace).getClass().getName());

        assertEquals("element(name) class not as",
                     ODOMElement.class.getName(),
                     factory.element("name").getClass().getName());

        assertEquals("element(name, uri) class not as",
                     ODOMElement.class.getName(),
                     factory.element("name",
                                     namespace.getURI()).getClass().getName());

        assertEquals("element(name, prefix, uri) class not as",
                     ODOMElement.class.getName(),
                     factory.element("name",
                                     namespace.getPrefix(),
                                     namespace.getURI()).getClass().getName());
    }

    // javadoc unnecessary
    public void testCdata() throws Exception {
        assertEquals("CDATA(text) class not as",
                     ODOMCDATA.class.getName(),
                     factory.cdata("textual content").getClass().getName());
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

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 07-Nov-03	1813/1	philws	VBM:2003110520 Add ODOMCDATA and provide correct clone feature

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
