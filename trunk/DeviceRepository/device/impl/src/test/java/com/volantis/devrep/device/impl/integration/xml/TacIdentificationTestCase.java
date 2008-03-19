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

import com.volantis.devrep.device.api.xml.tacidentification.TacIdentification;
import com.volantis.devrep.device.api.xml.tacidentification.TacIdentificationEntry;
import com.volantis.devrep.device.impl.integration.ResourceJiBXTester;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jibx.runtime.JiBXException;

import java.io.IOException;
import java.util.Iterator;

public class TacIdentificationTestCase extends TestCaseAbstract {

    private ResourceJiBXTester tester = new ResourceJiBXTester(
            PolicyTestCase.class);

    public void testUnmarshall() throws JiBXException, IOException {
        
        TacIdentification tacIdentification =  (TacIdentification) tester.unmarshall(
                TacIdentification.class, "TacIdentificationTest.xml");

        StringBuffer output = new StringBuffer();
        dumpTacIdentification(tacIdentification, output);
        String actual = output.toString();
        //System.out.println(actual);

        String expected = tester.getResource("TacIdentificationTest.txt");

        assertEquals("", expected, actual);
    }

    private void dumpTacIdentification(TacIdentification tacIdentification,
            StringBuffer buffer) {

        Iterator entries = tacIdentification.entries();
        while (entries.hasNext()) {
            TacIdentificationEntry entry = (TacIdentificationEntry) entries.next();
            dumpTacEntry(entry, buffer);
        }
    }

    private void dumpTacEntry(TacIdentificationEntry entry,
            StringBuffer buffer) {

        buffer.append("Tac Identification Entry: " + entry.getDeviceName() + "\n");
        Iterator numbers = entry.numbers();
        while (numbers.hasNext()) {
            String number = (String) numbers.next();
            buffer.append("  Number: " + number + "\n");
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
