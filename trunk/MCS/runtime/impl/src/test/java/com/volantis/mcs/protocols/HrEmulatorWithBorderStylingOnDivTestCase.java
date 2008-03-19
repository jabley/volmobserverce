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
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorWithBorderStylingOnDIV;

import java.io.IOException;

/**
 * Test horizontal rule emulation using styling on divs
 */
public class HrEmulatorWithBorderStylingOnDivTestCase
        extends HrEmulationTestAbstract {

    /**
     * Test the rendering of a hr when the emulation is
     * DIV_WITH_BORDER_BOTTOM_STYLING
     * @throws ProtocolException
     * @throws java.io.IOException
     */
    public void testDivWithBorderBottomStyling() throws ProtocolException,
            IOException {

        Element element;

        String noStyle_Expected =
                "<div style='border-bottom-color: black; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        String widthStyle_Expected =
                "<div style='border-bottom-color: black; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 50%'/>";

        String heightStyle_Expected =
                "<div style='border-bottom-color: black; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 5px; "+
                "height: 5px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        String colorStyle_Expected =
                "<div style='color: red; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        String textAlignStyle_Expected =
                "<div style='border-bottom-color: black; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "width: 100%'/>";

        String marginTopAlignStyle_Expected =
                "<div style='border-bottom-color: black; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 1px; "+
                "margin-bottom: 2px; margin-top: 8px; "+
                "text-align: center; width: 100%'/>";

        String marginBottomAlignStyle_Expected =
                "<div style='border-bottom-color: black; "+
                "border-bottom-style: solid; "+
                "border-bottom-width: 1px; "+
                "margin-bottom: 8px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        HorizontalRuleEmulator emulator =
                new HorizontalRuleEmulatorWithBorderStylingOnDIV(
                        HorizontalRuleEmulatorWithBorderStylingOnDIV
                        .BORDER_BOTTOM_PROPERTY
                );

        /**
         * Test no style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                            createHorizontalRuleAttributes(null));

        assertEquals("Incorrect Hr Emulation",noStyle_Expected,
                helper.render(element));

        /**
         * Test width style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("width: 50%"));

        assertEquals("Incorrect Hr Emulation",widthStyle_Expected,
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

        /**
         * Test text align style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("text-align: left"));

        assertEquals("Incorrect Hr Emulation",textAlignStyle_Expected,
                helper.render(element));

        /**
         * Test margin top style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("margin-top: 8px"));

        assertEquals("Incorrect Hr Emulation",marginTopAlignStyle_Expected,
                helper.render(element));

        /**
         * Test margin bottom style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("margin-bottom: 8px"));

        assertEquals("Incorrect Hr Emulation",marginBottomAlignStyle_Expected,
                helper.render(element));
    }

    /**
     * Test the rendering of a hr when the emulation is
     * DIV_WITH_BORDER_TOP_STYLING
     * @throws ProtocolException
     * @throws IOException
     */
    public void testDivWithBorderTopStyling() throws ProtocolException,
                                                        IOException {

        Element element;

        String noStyle_Expected =
                "<div style='border-top-color: black; "+
                "border-top-style: solid; "+
                "border-top-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        String widthStyle_Expected =
                "<div style='border-top-color: black; "+
                "border-top-style: solid; "+
                "border-top-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 50%'/>";

        String heightStyle_Expected =
                "<div style='border-top-color: black; "+
                "border-top-style: solid; "+
                "border-top-width: 5px; "+
                "height: 5px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        String colorStyle_Expected =
                "<div style='color: red; "+
                "border-top-style: solid; "+
                "border-top-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        String textAlignStyle_Expected =
                "<div style='border-top-color: black; "+
                "border-top-style: solid; "+
                "border-top-width: 1px; "+
                "margin-bottom: 2px; margin-top: 2px; "+
                "width: 100%'/>";

        String marginTopAlignStyle_Expected =
                "<div style='border-top-color: black; "+
                "border-top-style: solid; "+
                "border-top-width: 1px; "+
                "margin-bottom: 2px; margin-top: 8px; "+
                "text-align: center; width: 100%'/>";

        String marginBottomAlignStyle_Expected =
                "<div style='border-top-color: black; "+
                "border-top-style: solid; "+
                "border-top-width: 1px; "+
                "margin-bottom: 8px; margin-top: 2px; "+
                "text-align: center; width: 100%'/>";

        HorizontalRuleEmulator emulator =
                new HorizontalRuleEmulatorWithBorderStylingOnDIV(
                        HorizontalRuleEmulatorWithBorderStylingOnDIV
                        .BORDER_TOP_PROPERTY
                );

        /**
         * Test no style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                            createHorizontalRuleAttributes(null));

        assertEquals("Incorrect Hr Emulation",noStyle_Expected,
                helper.render(element));

        /**
         * Test width style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("width: 50%"));

        assertEquals("Incorrect Hr Emulation",widthStyle_Expected,
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

        /**
         * Test text align style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("text-align: left"));

        assertEquals("Incorrect Hr Emulation",textAlignStyle_Expected,
                helper.render(element));

        /**
         * Test margin top style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("margin-top: 8px"));

        assertEquals("Incorrect Hr Emulation",marginTopAlignStyle_Expected,
                helper.render(element));

        /**
         * Test margin bottom style applied
         */
        element = emulator.emulateHorizontalRule(new DOMOutputBuffer(),
                                    createHorizontalRuleAttributes("margin-bottom: 8px"));

        assertEquals("Incorrect Hr Emulation",marginBottomAlignStyle_Expected,
                helper.render(element));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 31-Oct-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 ===========================================================================
*/
