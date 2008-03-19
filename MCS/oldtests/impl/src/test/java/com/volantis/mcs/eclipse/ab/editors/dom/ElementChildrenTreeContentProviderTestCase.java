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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.input.JDOMFactory;
import org.jdom.input.DefaultJDOMFactory;

/**
 * Test case for ElementChildrenTreeContentProvider.
 */
public class ElementChildrenTreeContentProviderTestCase
        extends TestCaseAbstract {
    /**
     * Test getElements with no skip or stop elements.
     */
    public void testGetChildrenSimple() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent = factory.element("parent");
        Element child1 = factory.element("child1");
        Element child2 = factory.element("child2");
        parent.addContent(child1);
        parent.addContent(child2);

        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider();

        Object [] elements = provider.getChildren(parent);
        assertEquals("There should be two child elements.",
                2, elements.length);

        assertEquals("The first child should be child1",
                child1, elements[0]);

        assertEquals("The second child should be child2",
                child2, elements[1]);
    }

    /**
     * Test getChildren with stop elements.
     */
    public void testGetChildrenStopElements() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child1a);
        parent1.addContent(child2a);

        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        String [] stopElements = { parent1.getName(), parent2.getName() };

        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider(null, stopElements,
                        false, false);

        Object [] elements = provider.getChildren(parent1);
        assertEquals("There should be no child elements for parent1", 0,
                elements.length);

        elements = provider.getChildren(parent2);
        assertEquals("There should be no child elements for parent2", 0,
                elements.length);

        // Make parent1 and parent2 child of some root element to ensure that
        // they do appear in the root's children.
        Element root = factory.element("root");
        root.addContent(parent1);
        root.addContent(parent2);

        elements = provider.getChildren(root);
        assertEquals("There should be two child elements for root.", 2,
                elements.length);

        assertEquals("The first child should be parent1",
                parent1, elements[0]);

        assertEquals("The second child should be parent2",
                parent2, elements[1]);
    }


    /**
     * Test getChildren with skip elements.
     */
    public void testGetChildrenSkipElements() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child1a);
        parent1.addContent(child2a);

        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        Element root = factory.element("root");
        root.addContent(parent1);
        root.addContent(parent2);

        // Test that parent1 is skipped but parent2 is not.
        String [] skipElements = { parent1.getName() };

        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider(skipElements, null,
                        false, false);

        Object [] elements = provider.getChildren(root);
        assertEquals("There should be three child elements.", 3,
                elements.length);


        assertEquals("The first child should be parent2",
                parent2, elements[0]);

        assertEquals("The second child should be child1a",
                child1a, elements[1]);

        assertEquals("The third child should be child2a",
                child2a, elements[2]);

        // Test that both parent1 and parent2 are skipped.
        skipElements = new String[] { parent1.getName(), parent2.getName() };
        provider =
                new ElementChildrenTreeContentProvider(skipElements, null,
                        false, false);
        elements = provider.getChildren(root);

        assertEquals("There should be four child elements. ", 4,
                elements.length);

        assertEquals("The first child should be child1a",
                child1a, elements[0]);

        assertEquals("The second child should be child2a",
                child2a, elements[1]);

        assertEquals("The third child should be child1b",
                child1b, elements[2]);

        assertEquals("The fourth child should be child2b",
                child2b, elements[3]);
    }

    /**
     * Test getChildren() with both stop and skip elements at the same
     * time.
     */
    public void testGetChildrenSkipAndStopElements() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child1a);
        parent1.addContent(child2a);

        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        Element root = factory.element("root");
        root.addContent(parent1);
        root.addContent(parent2);

        Attribute rootAttr1 = factory.attribute("rootAttr1", "value");
        Attribute parentAttr1 = factory.attribute("parentAttr1", "value");

        root.setAttribute(rootAttr1);
        parent1.setAttribute(parentAttr1);

        // Skip both parents.
        String [] skipElements = { parent1.getName(), parent2.getName() };

        // Stop at both parents
        String [] stopElements = { parent1.getName(), parent2.getName() };


        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider(skipElements, stopElements,
                        false, false);

        Object [] elements = provider.getChildren(root);
        assertEquals("There should be no children for root.", 0,
                elements.length);

        provider =
                new ElementChildrenTreeContentProvider(skipElements, stopElements,
                        true, false);
        elements = provider.getChildren(root);
        assertEquals("There should be one child of root.", 1,
                elements.length);
        assertEquals("The child should be rootAttr1", rootAttr1, elements[0]);
    }

    /**
     * Test that getChildren() sorts returned elements and attributes
     * as required i.e. all attributes must come before all elements and
     * both elements and attributes must be ordered alphabetically.
     */
    public void testGetChildrenSorting() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child2a);
        parent1.addContent(child1a);

        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        Element root = factory.element("root");
        root.addContent(parent2);
        root.addContent(parent1);

        Attribute rootAttr1 = factory.attribute("rootAttr1", "value");
        Attribute rootAttr2 = factory.attribute("rootAttr2", "value");
        Attribute parentAttr1 = factory.attribute("parentAttr1", "value");

        root.setAttribute(rootAttr2);
        root.setAttribute(rootAttr1);
        parent1.setAttribute(parentAttr1);

        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider(null, null, true, false);

        Object [] elements = provider.getChildren(root);

        assertEquals("There should be four children for root.", 4,
                elements.length);

        assertEquals("The first element should be rootAttr1", rootAttr1,
                elements[0]);

        assertEquals("The second element should be rootAttr2", rootAttr2,
                elements[1]);

        assertEquals("The third element should be parent1", parent1,
                elements[2]);
    }

    /**
     * Test getElements() when the root element should be displayed.
     */
    public void testGetElementsProvideRoot() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child2a);
        parent1.addContent(child1a);

        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        Element root = factory.element("root");
        root.addContent(parent2);
        root.addContent(parent1);


        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider(null, null, true, true);

        Object [] elements = provider.getElements(root);

        assertEquals("Only the root element should have been provided.",
                1, elements.length);
        assertSame("The first element should be the root element.",
                root, elements[0]);
    }


    /**
     * Test getElements() when the root element should not be displayed.
     */
    public void testGetElementsNoRoot() {
        JDOMFactory factory = new DefaultJDOMFactory();
        Element parent1 = factory.element("parent1");
        Element child1a = factory.element("child1a");
        Element child2a = factory.element("child2a");
        parent1.addContent(child2a);
        parent1.addContent(child1a);

        Element parent2 = factory.element("parent2");
        Element child1b = factory.element("child1b");
        Element child2b = factory.element("child2b");
        parent2.addContent(child1b);
        parent2.addContent(child2b);

        Element root = factory.element("root");
        root.addContent(parent2);
        root.addContent(parent1);

        ElementChildrenTreeContentProvider provider =
                new ElementChildrenTreeContentProvider(null, null, true, false);

        Object [] elements = provider.getElements(root);

        assertEquals("Only parent1 and parent2 should have been provided.",
                2, elements.length);
        assertSame("The first element should be parent2.",
                parent2, elements[0]);
        assertSame("The second element should be parent1.",
                parent1, elements[1]);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Jan-04	2562/1	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 ===========================================================================
*/
