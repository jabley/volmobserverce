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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.device.impl.integration.xml;

import com.volantis.devrep.device.api.xml.hierarchy.Hierarchy;
import com.volantis.devrep.device.api.xml.hierarchy.HierarchyEntry;
import com.volantis.devrep.device.impl.integration.ResourceJiBXTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jibx.runtime.JiBXException;

import java.io.IOException;
import java.util.Iterator;

public class HierarchyTestCase extends TestCaseAbstract {

    private ResourceJiBXTester tester = new ResourceJiBXTester(
            HierarchyTestCase.class);

    public void testUnmarshall() throws JiBXException, IOException {

        Hierarchy hierarchy = (Hierarchy) tester.unmarshall(Hierarchy.class,
                "HierarchyTest.xml");

        StringBuffer output = new StringBuffer();
        dumpHierarchy(hierarchy, output);
        String actual = output.toString();
        //System.out.println(actual);

        String expected = tester.getResource("HierarchyTest.txt");

        assertEquals("", expected, actual);
    }

    private void dumpHierarchy(Hierarchy hierarchy, StringBuffer buffer) {

        buffer.append("Hierarchy:\n");
        dumpEntry(hierarchy.getRootEntry(), buffer, 0);
    }

    private void dumpEntry(HierarchyEntry entry, StringBuffer buffer,
            int indent) {

        indent(indent, buffer);
        buffer.append("Entry:" + entry.getDeviceName() + "\n");
        int childIndent = indent+1;
        Iterator children = entry.children();
        while (children.hasNext()) {
            HierarchyEntry child = (HierarchyEntry) children.next();
            dumpEntry(child, buffer, childIndent);
        }
    }

    private void indent(int indent, StringBuffer buffer) {

        for (int i=0; i<indent;i++) {
            buffer.append("  ");
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
