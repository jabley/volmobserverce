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
package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.hr.HorizontalRuleEmulator;
import com.volantis.mcs.protocols.hr.TableAttrHREmulator;

import java.io.IOException;

/**
 * Test horizontal rule emulation using tables
 */
public class TableAttrEmulatorTestCase
    extends HrEmulationTestAbstract {

        /**
     * Test the rendering of a hr when the emulation is
     * TableAttrHrEmulator
     * @throws ProtocolException
     * @throws java.io.IOException
     */
    public void testTableEmulation() throws ProtocolException, IOException {

        String noStyle_Expected =
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" " +
            "cellspacing=\"0\" height=\"1\" width=\"100%\">" +
            "<tr><td bgcolor=\"black\" bordercolor=\"black\"/></tr></table>";


        String widthStyle_Expected =
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" " +
                "cellspacing=\"0\" height=\"1\" width=\"50%\">" +
            "<tr><td bgcolor=\"black\" bordercolor=\"black\"/></tr></table>";

        String heightStyle_Expected =
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" " +
                "cellspacing=\"0\" height=\"5\" width=\"100%\">" +
            "<tr><td bgcolor=\"black\" bordercolor=\"black\"/></tr></table>";

        String colorStyle_Expected =
            "<table align=\"center\" border=\"0\" cellpadding=\"0\" " +
                "cellspacing=\"0\" height=\"1\" width=\"100%\">" +
            "<tr><td bgcolor=\"red\" bordercolor=\"red\"/></tr></table>";

        HorizontalRuleEmulator emulator = new TableAttrHREmulator();

        /**
         * Test no style applied
         */
        DOMOutputBuffer domOutputBuffer = new DOMOutputBuffer() ;
        emulator.emulateHorizontalRule(
                domOutputBuffer, createHorizontalRuleAttributes(null));

        assertEquals("Incorrect Hr Emulation",noStyle_Expected,
                helper.render(domOutputBuffer.getRoot()));

        /**
         * Test width style applied
         */
        domOutputBuffer = new DOMOutputBuffer() ;
        emulator.emulateHorizontalRule(
                domOutputBuffer, createHorizontalRuleAttributes("width: 50%"));


        assertEquals("Incorrect Hr Emulation",widthStyle_Expected,
                helper.render(domOutputBuffer.getRoot()));

        /**
         * Test height style applied
         */
        domOutputBuffer = new DOMOutputBuffer() ;
        emulator.emulateHorizontalRule(domOutputBuffer,
                                    createHorizontalRuleAttributes("height: 5px"));


        assertEquals("Incorrect Hr Emulation",heightStyle_Expected,
                helper.render(domOutputBuffer.getRoot()));

        /**
         * Test colour style applied
         */
        domOutputBuffer = new DOMOutputBuffer() ;
        emulator.emulateHorizontalRule(domOutputBuffer,
                                    createHorizontalRuleAttributes("color: red"));


        assertEquals("Incorrect Hr Emulation",colorStyle_Expected,
                helper.render(domOutputBuffer.getRoot()));

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 31-Oct-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 31-Oct-05	9565/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
