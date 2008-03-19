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

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.synergetics.testtools.TestCaseAbstract;



public abstract class GenericRendererTestAbstract extends TestCaseAbstract {


    /**
     * Tests the value rendered for a StyleValue is correct.
     */
    protected void checkRenderValue(PropertyRenderer renderer,
                                    StyleValue value,
            String expectedResult) throws Exception {

        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer,
                new CSSStyleSheetRenderer());
        renderer.renderValue(value, context);

        assertEquals(expectedResult, context.getWriter().toString());
    }

    /**
     * Tests the value rendered for a StyleValue is correct.
     */
    protected void checkRender(RendererContext context, PropertyRenderer renderer,
                                    StyleProperty property,
                                    StyleValue value,
            String expectedResult) throws Exception {

        MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        properties.setStyleValue(property, value);
        renderer.render(properties, context);

        assertEquals(expectedResult, context.getWriter().toString());
    }

    /**
     * Tests the value rendered for a set of style properties is correct.
     */
    protected void checkRender(PropertyRenderer renderer,
                               StyleProperties properties,
                               String expectedResult)
            throws Exception {

        StringWriter writer = new StringWriter();
        RendererContext context = new RendererContext(writer,
                new CSSStyleSheetRenderer());

        renderer.render(properties, context);

        assertEquals(expectedResult, ((ReusableStringWriter)context.
                getWriter()).getBuffer().toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 09-Nov-05	10221/1	emma	VBM:2005102606 Forward port: fixing various styling bugs

 09-Nov-05	10201/1	emma	VBM:2005102606 Fixing various styling bugs

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
