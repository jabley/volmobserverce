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
package com.volantis.mcs.css.renderer;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.themes.StylePercentage;
import com.volantis.mcs.themes.StyleValueFactory;

import java.io.StringWriter;

public class StylePercentageRendererTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    public StylePercentageRendererTestCase(String name) {
        super(name);
    }

    /**
     * This method will test that double values with trailing zeros are
     * converted to integers, and that all other double values and integers
     * are unchanged.
     */
    public void testRender() throws Exception {
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 100.0), "100%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 100.05), "100.05%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 100.50), "100.5%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 100), "100%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 50.0), "50%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 50.05), "50.05%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 50.50), "50.5%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 50), "50%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 33.33), "33.33%");
        doRendererTest(STYLE_VALUE_FACTORY.getPercentage(null, 66.66), "66.66%");
    }

    /**
     * Tests the value rendered for a StyleLength is correct.
     */
    private void doRendererTest(StylePercentage value, String expectedResult)
            throws Exception {
        StyleSheetRenderer styleSheetRenderer = new CSSStyleSheetRenderer();
        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer, styleSheetRenderer);
        StylePercentageRenderer renderer = new StylePercentageRenderer();

        renderer.render(value, context);
        assertEquals(expectedResult, context.getWriter().toString());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Apr-05	7568/1	emma	VBM:2005033015 Removing unnecessary trailing zeros

 12-Apr-05	7568/1	emma	VBM:2005033015 Removing unnecessary trailing zeros

 06-Apr-05	7562/3	emma	VBM:2005033015 Removing unnecessary trailing zeros and adding unit test

 06-Apr-05	7562/1	emma	VBM:2005033015 Removing unnecessary trailing zeros and adding unit test

 ===========================================================================
*/
