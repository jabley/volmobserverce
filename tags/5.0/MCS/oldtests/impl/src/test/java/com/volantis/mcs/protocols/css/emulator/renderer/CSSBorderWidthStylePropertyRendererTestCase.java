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
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderWidthKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link CSSBorderWidthPropertyRenderer}
 */
public class CSSBorderWidthStylePropertyRendererTestCase
        extends TestCaseAbstract {

    public void testRenderWithWidthSpecifiedUsingPixels() {

        StyleValue borderWidthStyle = StyleValueFactory.getDefaultInstance()
                .getLength(null, 15, LengthUnit.PX);

        CSSBorderWidthPropertyRenderer borderPropertyRenderer =
                new CSSBorderWidthPropertyRenderer("border-top-width");

        String expectedRendering = "border-top-width:15px;";
        String actualRendering = borderPropertyRenderer.render(borderWidthStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWithWidthSpecifiedUsingPercentage() {
        StylePercentage percentageWidth =
            StyleValueFactory.getDefaultInstance().getPercentage(null, 10);

        CSSBorderWidthPropertyRenderer borderPropertyRenderer =
                new CSSBorderWidthPropertyRenderer("border-top-width");

        String expectedRendering = "border-top-width:10%;";
        String actualRendering = borderPropertyRenderer.render(percentageWidth);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWithWidthSpecifiedUsingThinKeyword() {
        StyleKeyword borderStyle = BorderWidthKeywords.THIN;

        CSSBorderWidthPropertyRenderer borderPropertyRenderer =
                new CSSBorderWidthPropertyRenderer("border-top-width");

        String expectedRendering = "border-top-width:thin;";
        String actualRendering = borderPropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWithWidthSpecifiedUsingMediumKeyword() {
        StyleKeyword borderStyle = BorderWidthKeywords.MEDIUM;

        CSSBorderWidthPropertyRenderer borderPropertyRenderer =
                new CSSBorderWidthPropertyRenderer("border-top-width");

        String expectedRendering = "border-top-width:medium;";
        String actualRendering = borderPropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }

    public void testRenderWithWidthSpecifiedUsingThickKeyword() {
        StyleKeyword borderStyle = BorderWidthKeywords.THICK;

        CSSBorderWidthPropertyRenderer borderPropertyRenderer =
                new CSSBorderWidthPropertyRenderer("border-top-width");

        String expectedRendering = "border-top-width:thick;";
        String actualRendering = borderPropertyRenderer.render(borderStyle);

        assertEquals(expectedRendering, actualRendering);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 22-Nov-05	10381/2	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 ===========================================================================
*/
