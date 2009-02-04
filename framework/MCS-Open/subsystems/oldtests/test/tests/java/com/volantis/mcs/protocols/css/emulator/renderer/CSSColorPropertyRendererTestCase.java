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

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link CSSColorPropertyRenderer}.
 */
public class CSSColorPropertyRendererTestCase extends TestCaseAbstract {

    /**
     * Constant for the hexidecimal value for the color orange.
     */
    private static final int COLOR_ORANGE = 0xffa500;

    /**
     * Tests that the color style value is rendered correctly when
     * it is specified as a name, e.g red.
     */ 
    public void testRenderWhenColorRepresentedAsName() {

        String expectedRendering = "border-top-color:red;";

        testRender(StyleColorNames.RED, "border-top-color", expectedRendering);
    }

    /**
     * Tests that the color style value is rendered correctly
     * when it is specified as a hexadecimal value.
     */
    public void testRenderWhenColorRepresentedUsingRBGValues() {

        StyleValue color = StyleValueFactory.getDefaultInstance().
                getColorByRGB(null, COLOR_ORANGE);
        
        String expectedRendering = "border-top-color:#ffa500;";
        testRender(color, "border-top-color", expectedRendering);
    }

    private void testRender(StyleValue styleToBeRendered,
                              String cssPropertyName,
                              String expectedRendering) {

        // Configure the renderer.
        CSSColorPropertyRenderer colorPropertyRenderer =
                new CSSColorPropertyRenderer(cssPropertyName);
        // Render the style.
        String actualRendering = colorPropertyRenderer.render(styleToBeRendered);
        // Check the output.
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
