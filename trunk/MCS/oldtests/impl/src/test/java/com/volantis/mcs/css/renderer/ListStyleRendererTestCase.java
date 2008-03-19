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
 
package com.volantis.mcs.css.renderer;

import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.ListStylePositionKeywords;
import com.volantis.mcs.themes.properties.ListStyleTypeKeywords;
import com.volantis.mcs.utilities.ReusableStringWriter;

/**
 * This class tests ListStyleRendererTestCase
 */
public class ListStyleRendererTestCase
        extends PropertyRendererTestAbstract {

    ListStyleRenderer renderer;
    DefaultCSSVersion cssVersion;

    // javadoc inherited
    public void setUp() throws Exception {
        super.setUp();

        renderer = new ListStyleRenderer();
        // Create a css version with the shortcut the renderer understands
        // removed. This will then be used to ensure that CSS specific rendering
        // works (compared to the default MCS theme rendering).
        cssVersion = new DefaultCSSVersion("test");
        cssVersion.markImmutable();
    }

    // javadoc inherited
    public void tearDown() {
        renderer = null;
        cssVersion = null;
    }

    public void testCSSRender() throws Exception {
        context = new RendererContext(writer, styleSheetRenderer, null,
                cssVersion);
        doRender("");
    }

    public void testThemeRender() throws Exception {
        doRender("");
    }

    public void doRender(String expResult) throws Exception {
        final String uri = "http://www.volantis.com/myimage.jpg";
        StyleValue image = styleValueFactory.getURI(null, uri);
        
        StyleValue type = ListStyleTypeKeywords.UPPER_ALPHA;
        
        StyleValue position = ListStylePositionKeywords.INSIDE;
        
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_IMAGE,
                image);
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_TYPE,
                type);
        properties.setStyleValue(StylePropertyDetails.LIST_STYLE_POSITION,
                position);

        renderer.render(properties, context);

        String expected = null;
        if (context.getCSSVersion() == null) {
            expected =
                    "list-style:" +
                        "url(" + uri + ") " +
                        "upper-alpha " +
                        "inside;";
        } else {
            expected =
                    "list-style-image:url(" + uri + ");" +
                    "list-style-type:upper-alpha;" +
                    "list-style-position:inside;";
        }

        assertEquals("Rendered value is incorrect.", expected,
                ((ReusableStringWriter) context.getWriter()).
                getBuffer().toString());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 22-Aug-03	1176/1	adrian	VBM:2003081811 implemented hashcode in StyleValue classes

 ===========================================================================
*/
