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
import java.io.Writer;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test the rendering of WapInputRequired.
 */
public class WapInputRequiredTestCase
        extends GenericRendererTestAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    protected Writer writer;
    protected RendererContext context;

    protected void setUp() throws Exception {
        writer = new StringWriter();
        context = new RendererContext(writer, new CSSStyleSheetRenderer());

    }

    /**
     * Ensure we render false for input required.
     *
     * @throws Exception
     */
    public void testRenderInputNotRequired() throws Exception {
        PropertyRenderer renderer = new WapInputRequiredRenderer();
        StyleProperty property = StylePropertyDetails.MCS_INPUT_FORMAT;
        String expected = "-wap-input-required:false;";
        StyleValue value = STYLE_VALUE_FACTORY.getString(null, "n:N*");


        checkRender(context, renderer, property, value, expected);

    }

    /**
     * Ensure we render true for input required.
     *
     * @throws Exception
     */
    public void testRenderInputRequired() throws Exception {
        PropertyRenderer renderer = new WapInputRequiredRenderer();
        StyleProperty property = StylePropertyDetails.MCS_INPUT_FORMAT;
        String expected = "-wap-input-required:true;";
        StyleValue value = STYLE_VALUE_FACTORY.getString(null, "N:N*");


        checkRender(context, renderer, property, value, expected);

    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
