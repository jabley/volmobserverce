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

import com.volantis.devrep.device.api.xml.policy.PolicyEntry;
import com.volantis.devrep.device.api.xml.policy.PolicyField;
import com.volantis.devrep.device.api.xml.policy.PolicySet;
import com.volantis.devrep.device.impl.integration.ResourceJiBXTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jibx.runtime.JiBXException;

import java.io.IOException;
import java.util.Iterator;

public class PolicyTestCase extends TestCaseAbstract {

    private ResourceJiBXTester tester = new ResourceJiBXTester(
            PolicyTestCase.class);

    public void testUnmarshall() throws JiBXException, IOException {

        PolicySet policySet = (PolicySet) tester.unmarshall(PolicySet.class,
                "PolicyTest.xml");

        StringBuffer output = new StringBuffer();
        dumpPolicySet(policySet, output);

        Iterator i =policySet.entries();
          while (i.hasNext())
          {
             PolicyEntry test = (PolicyEntry)i.next();
             if ("testinherit".equals(test.getName())) {
                 assertNotNull(test.getInherit());
             }    
          }


        String actual = output.toString();
        //System.out.println(actual);

        String expected = tester.getResource("PolicyTest.txt");

        assertEquals("", expected, actual);
    }

    private void dumpPolicySet(PolicySet policySet, StringBuffer buffer) {

        Iterator policyIterator = policySet.entries();
        while (policyIterator.hasNext()) {
            PolicyEntry policyEntry = (PolicyEntry) policyIterator.next();
            dumpPolicyEntry(policyEntry, buffer);
        }
    }

    private void dumpPolicyEntry(PolicyEntry entry, StringBuffer buffer) {

        buffer.append("Policy: " + entry.getName() + "\n");

        Iterator values = entry.values();
        if (values.hasNext()) {
            dumpValues(buffer, values);
        }

        Iterator fields = entry.fields();
        if (fields.hasNext()) {
            dumpFields(buffer, fields);
        }
    }

    private void dumpFields(StringBuffer buffer, Iterator fields) {
        buffer.append("  Fields:\n");
        while (fields.hasNext()) {
            PolicyField policyField = (PolicyField) fields.next();
            buffer.append("    " + policyField.getName() + " = '" +
                    policyField.getValue() + "',\n");
        }
    }

    private void dumpValues(StringBuffer buffer, Iterator values) {
        buffer.append("  Values:\n");
        while (values.hasNext()) {
            String value = (String) values.next();
            buffer.append("    " + "'" + value + "',\n");
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
