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
package com.volantis.mcs.eclipse.ab.actions.layout;

import java.util.Arrays;

import org.jdom.Document;
import org.jdom.Element;


/**
 * testcase for DeleteActionCommand limited to jdom sorting
 * (sensitive to jdom version)
 */
public class DeleteActionTestCase extends LayoutActionCommandTestAbstract {

    private Element rootElement;

    private Element child1;

    private Element child2;

    private Element child11;


    //javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        rootElement = new Element("root");
        Document jdomDoc = new Document(rootElement);
        child1 = new Element("child1");
        rootElement.addContent(child1);
        child2 = new Element("child2");
        rootElement.addContent(child2);
        child11 = new Element("child11");
        child1.addContent(child11);
    }


    /**
     * this may fail if we upgrade JDOM when the B9 behavior will be fixed
     */
    public void testJDOMisAncestorDoesTheOppositeOfWhatItSays()
            throws Exception {
        assertTrue("expected behavior of JDOM b9 Element.isAncestor()",
                child1.isAncestor(rootElement));

        assertFalse("expected behavior of JDOM b9 Element.isAncestor()",
                rootElement.isAncestor(child1));
    }


    /**
     * test for {@link DeleteActionCommand#sortChildrenFirst(org.jdom.Element[])}
     */
    public void testJDOMSortChildrenfirstTestCase() throws Exception {
        Element[] inputElements = {rootElement, child11, child2, child1};
        Element[] expectedSortedElements = {child11,
                                            child2,
                                            child1,
                                            rootElement};

        DeleteActionCommand.sortChildrenFirst(inputElements);

        assertTrue("sorted arrays",
                Arrays.equals(expectedSortedElements, inputElements));
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4470/3	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 11-Feb-04	2939/1	eduardo	VBM:2004020506 ODOM DeleteActionCommand changed to be undo/redo friendly

 ===========================================================================
*/
