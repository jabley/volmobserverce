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

import com.volantis.devrep.device.api.xml.identification.HeaderPattern;
import com.volantis.devrep.device.api.xml.identification.Identification;
import com.volantis.devrep.device.api.xml.identification.IdentificationEntry;
import com.volantis.devrep.device.api.xml.identification.UserAgentPattern;
import com.volantis.devrep.device.impl.integration.ResourceJiBXTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jibx.runtime.JiBXException;

import java.io.IOException;
import java.util.Iterator;

public class IdentificationTestCase extends TestCaseAbstract {

    private ResourceJiBXTester tester = new ResourceJiBXTester(
            PolicyTestCase.class);

    public void testUnmarshall() throws JiBXException, IOException {
        
        Identification identification =  (Identification) tester.unmarshall(
                Identification.class, "IdentificationTest.xml");

        StringBuffer output = new StringBuffer();
        dumpIdentification(identification, output);
        String actual = output.toString();
        //System.out.println(actual);

        String expected = tester.getResource("IdentificationTest.txt");

        assertEquals("", expected, actual);
    }

    private void dumpIdentification(Identification identification,
            StringBuffer buffer) {

        Iterator entries = identification.entries();
        while (entries.hasNext()) {
            IdentificationEntry entry = (IdentificationEntry) entries.next();
            dumpEntry(entry, buffer);
        }
    }

    private void dumpEntry(IdentificationEntry entry,
            StringBuffer buffer) {
        buffer.append("Identification Entry: " + entry.getDeviceName() + "\n");

        Iterator userAgentPatterns = entry.userAgentPatterns();
        while (userAgentPatterns.hasNext()) {
            UserAgentPattern userAgentPattern = (UserAgentPattern) userAgentPatterns.next();
            buffer.append("  UserAgentPattern:\n");
            buffer.append("    regexp=" + userAgentPattern.getRegularExpression() + "\n");
        }

        Iterator headerPatterns = entry.headerPatterns();
        while (headerPatterns.hasNext()) {
            HeaderPattern headerPattern = (HeaderPattern) headerPatterns.next();
            buffer.append("  HeaderPattern:\n");
            buffer.append("    name=" + headerPattern.getName() + "\n");
            buffer.append("    baseDevice=" + headerPattern.getBaseDevice() + "\n");
            buffer.append("    regexp=" + headerPattern.getRegularExpression() + "\n");
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
