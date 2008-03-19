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

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulator;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule;

import java.io.IOException;

/**
 * Test horizontal rule emulation using styling on the hr
 */
public class HrEmulatorWithBorderStylingOnHrTestCase
        extends HrEmulationTestAbstract {

    /**
     * Test the rendering of a hr when the emulation is
     * HR_WITH_BORDER_STYLING
     * @throws ProtocolException
     * @throws java.io.IOException
     */
    public void testHrWithBorderStyling() throws ProtocolException,
            IOException {

        Element element;

        String noStyle_Expected =
                "<hr style='border-top-color: black; " +
                "border-top-style: solid; "+
                "border-top-width: 1px'/>";

        String heightStyle_Expected =
        "<hr style='border-top-color: black; " +
                "border-top-style: solid; " +
                "border-top-width: 5px; " +
                "height: 5px'/>";

        String colorStyle_Expected =
        "<hr style='color: red; " +
                "border-top-style: solid; " +
                "border-top-width: 1px'/>";

        HorizontalRuleEmulator emulator =
                new HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule();

        /**
         * Test no style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                            createHorizontalRuleAttributes(null));

        assertEquals("Incorrect Hr Emulation",noStyle_Expected,
                helper.render(element));

        /**
         * Test height style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("height: 5px"));

        assertEquals("Incorrect Hr Emulation",heightStyle_Expected,
                helper.render(element));

        /**
         * Test colour style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("color: red"));

        assertEquals("Incorrect Hr Emulation",colorStyle_Expected,
                helper.render(element));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10812/1	pduffin	VBM:2005121322 Committing changes ported forward from 3.5

 13-Dec-05	10808/1	pduffin	VBM:2005121322 Fixed horizontal rule emulation for SonyEricsson-P900

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/4	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10567/1	emma	VBM:2005112901 Forward port of bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 31-Oct-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
