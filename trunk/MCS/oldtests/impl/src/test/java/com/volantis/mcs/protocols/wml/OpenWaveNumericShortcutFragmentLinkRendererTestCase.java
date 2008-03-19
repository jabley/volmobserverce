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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.StyledDOMTester;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.styling.StylingFactory;

/**
 * A test case for the OpenWave "numeric-shortcut" fragment link renderer.
 */ 
public class OpenWaveNumericShortcutFragmentLinkRendererTestCase 
        extends TestCaseAbstract {

    /**
     * Test the main doFragmentLink method. This should generate a single
     * select option with the fraglink attributes contained in it.
     */ 
    public void testDoFragmentLink() throws Exception {
        // Implement any methods of the context we need. 
        // This means that this test is independent of the Protocol.
        WMLFragmentLinkRendererContext context = 
                new WMLFragmentLinkRendererContext(null) {
                    public void addTitleAttribute(Element element,
                            MCSAttributes attributes) {
                        element.setAttribute("title", "x");
                    }
                };
        OpenWaveNumericShortcutFragmentLinkRenderer renderer = 
                new OpenWaveNumericShortcutFragmentLinkRenderer(context);
        
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        String href = "href";
        String text = "text";
        FraglinkAttributes attrs = new FraglinkAttributes();
        attrs.setHref(href);
        final DOMOutputBuffer outputBuffer = new DOMOutputBuffer();
        outputBuffer.writeText(text);
        attrs.setLinkText(outputBuffer);
        final StylingFactory factory = StylingFactory.getDefaultInstance();
        attrs.setStyles(factory.createStyles(factory.createPropertyValues(
                StylePropertyDetails.getDefinitions())));
        renderer.doFragmentLink(buffer, attrs);

        StyledDOMTester tester = new StyledDOMTester();

        String expected =
                "<BLOCK style='white-space: nowrap'>" +
                  "<select title=\"x\">" + 
                    "<option onpick=\"" + href + "\" title=\"x\">" + 
                      text + 
                    "</option>" + 
                  "</select>" +
                "</BLOCK>";
        String actual = tester.render(buffer.getRoot());
        assertEquals("Openwave Fragment Link not rendering correctly", 
                tester.normalize(expected), actual);
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 25-Sep-03	1412/5	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
