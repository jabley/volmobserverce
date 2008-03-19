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

import java.io.StringWriter;
import java.io.IOException;

import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSyntaxes;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.css.version.CSSVersionMock;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

public class StyleColorRendererTestCase extends MockTestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private static final StyleColorRGB VALID_TRIPLET =
        (StyleColorRGB) STYLE_VALUE_FACTORY.getColorByRGB(null, 0xffffff);
    private static final StyleColorRGB INVALID_TRIPLET =
        (StyleColorRGB) STYLE_VALUE_FACTORY.getColorByRGB(null, 0xfeffff);

    private static final String TRIPLET = "#fff";
    private static final String NOT_TRIPLET = "#ffffff";
    private static final String NOT_TRIPLET2 = "#feffff";

    private CSSVersionMock cssVersion;
    private StringWriter writer;
    private StyleColorRenderer renderer;
    RendererContext context;

    protected void setUp() throws Exception {
        super.setUp();
        cssVersion = new CSSVersionMock(expectations);

        writer = new StringWriter();
        renderer = StyleColorRenderer.getSingleton();
    }

    /**
     * Make sure that the orange keyword gets written out as RGB value.
     */
    public void testOrange() throws Exception {

        context = new RendererContext(writer,
                CSSStyleSheetRenderer.getSingleton());

        renderer.renderName(StyleColorNames.ORANGE, context);
        context.flushStyleSheet();

        String result = writer.getBuffer().toString();
        assertEquals("Orange RGB", "#ffa500", result);
    }

    /**
     * Verify that if the RGB color value cannot be represented as a valid
     * triplet value, then it is not rendered as a triplet (regardless of
     * whether the device supports triplets correctly).
     */
    public void testRenderRGBForInvalidTripletValueWhenTripletsSupported() throws IOException {
        // Set expectations.
        cssVersion.expects.getProperty(StylePropertyDetails.VERTICAL_ALIGN).
                returns(null);
        cssVersion.expects.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS).
                returns(true);

        context = new RendererContext(writer, null, null, cssVersion);
        renderer.renderRGB(INVALID_TRIPLET, context);
        context.flushStyleSheet();
        assertEquals(NOT_TRIPLET2, writer.toString());
    }

    /**
     * Verify that if the RGB color value cannot be represented as a valid
     * triplet value, then it is not rendered as a triplet (regardless of
     * whether the device supports triplets correctly).
     */
    public void testRenderRGBForInvalidTripletValueWhenTripletsNotSupported()
            throws IOException {
        // Set expectations.
        cssVersion.expects.getProperty(StylePropertyDetails.VERTICAL_ALIGN).
                returns(null);
        cssVersion.expects.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS).
                returns(false);

        context = new RendererContext(writer, null, null, cssVersion);
        renderer.renderRGB(INVALID_TRIPLET, context);
        context.flushStyleSheet();
        assertEquals(NOT_TRIPLET2, writer.toString());
    }

    /**
     * Verify that if the RGB color value can be represented as a valid
     * triplet value, then it is rendered as a triplet if the device can
     * support it.
     */
    public void testRenderRGBForValidTripletValueWhenTripletsSupported()
            throws IOException {
        // Set expectations.
        cssVersion.expects.getProperty(StylePropertyDetails.VERTICAL_ALIGN).
                returns(null);
        cssVersion.expects.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS).
                returns(true);

        context = new RendererContext(writer, null, null, cssVersion);

        renderer.renderRGB(VALID_TRIPLET, context);
        context.flushStyleSheet();
        assertEquals(TRIPLET, writer.toString());
    }

    /**
     * Verify that if even the RGB color value can be represented as a valid
     * triplet value, it is NOT rendered as a triplet if the device cannot
     * support it.
     */
    public void testRenderRGBForValidTripletValueWhenTripletsNotSupported()
            throws IOException {
        // Set expectations.
        cssVersion.expects.getProperty(StylePropertyDetails.VERTICAL_ALIGN).
                returns(null);
        cssVersion.expects.supportsSyntax(StyleSyntaxes.COLOR_TRIPLETS).
                returns(false);

        context = new RendererContext(writer, null, null, cssVersion);

        renderer.renderRGB(VALID_TRIPLET, context);
        context.flushStyleSheet();
        assertEquals(NOT_TRIPLET, writer.toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10818/1	pduffin	VBM:2005121401 Added color orange, refactored NamedColor and StyleColorName to remove duplication of data

 ===========================================================================
*/
