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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.dom.impl.DocumentImpl;

/**
 * Test cases for {@link Document}.
 */
public class DocumentTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that getRootElement works.
     */
    public void testGetRootElement() throws Exception {

        DOMFactory factory = DOMFactory.getDefaultInstance();
        Document document = factory.createDocument();

        Element root = factory.createElement("root");
        document.addNode(root);

        assertSame(root, document.getRootElement());

        Text text = factory.createText("   ");
        text.insertBefore(root);
        assertSame(root, document.getRootElement());
    }
}
