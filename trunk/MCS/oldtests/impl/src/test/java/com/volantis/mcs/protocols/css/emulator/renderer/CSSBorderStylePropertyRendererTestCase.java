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
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link CSSBorderStylePropertyRenderer}.
 */
public class CSSBorderStylePropertyRendererTestCase extends TestCaseAbstract {

    
    public void testRenderWhenBorderStyleIsDotted() {
        StyleKeyword borderStyle = BorderStyleKeywords.DOTTED;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:dotted;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsDashed() {
        StyleKeyword borderStyle = BorderStyleKeywords.DASHED;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:dashed;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsSolid() {
        StyleKeyword borderStyle = BorderStyleKeywords.SOLID;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:solid;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsDouble() {
        StyleKeyword borderStyle = BorderStyleKeywords.DOUBLE;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:double;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsGroove() {
        StyleKeyword borderStyle = BorderStyleKeywords.GROOVE;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:groove;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsRidge() {
        StyleKeyword borderStyle = BorderStyleKeywords.RIDGE;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:ridge;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsInset() {
        StyleKeyword borderStyle = BorderStyleKeywords.INSET;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:inset;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWhenBorderStyleIsOutset() {
        StyleKeyword borderStyle = BorderStyleKeywords.OUTSET;

        CSSBorderStylePropertyRenderer borderStylePropertyRenderer =
                new CSSBorderStylePropertyRenderer("border-top-style");

        String expectedRendering = "border-top-style:outset;";
        String actualRendering = borderStylePropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
