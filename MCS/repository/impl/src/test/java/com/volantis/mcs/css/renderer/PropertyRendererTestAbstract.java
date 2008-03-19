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

import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.utilities.ReusableStringWriter;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Base class for property renderer test cases.
 */
public abstract class PropertyRendererTestAbstract
        extends TestCaseAbstract {

    /**
     * The object to use to create style values.
     */
    protected StyleValueFactory styleValueFactory;
    protected MutableStyleProperties properties;
    protected StyleSheetRenderer styleSheetRenderer;
    protected StringWriter writer;
    protected RendererContext context;

    // Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        styleValueFactory = StyleValueFactory.getDefaultInstance();
        properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();
        styleSheetRenderer = CSSStyleSheetRenderer.getSingleton();
        writer = new StringWriter();   
        // deletgated to a separare method so it can be easilly overriden
        // without duplicating the rest of setUp() code
        setUpRendererContext();
    }

    protected String render(PropertyRenderer renderer,
                            StyleProperties properties)
            throws IOException {

        renderer.render(properties, context);

        return ((ReusableStringWriter) context.getWriter())
                .getBuffer().toString();
    }
    
    protected void setUpRendererContext() {
        context = new RendererContext(writer, styleSheetRenderer);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Sep-05	9496/3	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 ===========================================================================
*/
