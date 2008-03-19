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


package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.css.renderer.GenericRendererTestAbstract;
import com.volantis.mcs.css.renderer.PropertyRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.version.DefaultCSSVersion;
import com.volantis.mcs.protocols.ValidationHelper;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.protocols.wml.WMLValidationHelper;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.MCSInputFormatKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.io.StringWriter;
import java.io.Writer;


/**
 * Test the RuntimeWapInputFormatRenderer.
 */
public class RuntimeWapInputFormatRendererTestCase extends
        GenericRendererTestAbstract {

    protected Writer writer;

    protected RendererContext context;

    protected void setUp() throws Exception {
        writer = new StringWriter();
        DefaultCSSVersion cssVersion = new DefaultCSSVersion("WapCSS");
        cssVersion.markImmutable();
        
        context = new RuntimeRendererContext(writer,
                new RuntimeCSSStyleSheetRenderer(),
                new RuntimeRendererProtocolConfiguration() {
                    public ValidationHelper getValidationHelper() {
                        return new WMLValidationHelper();
                    }

                    public Inserter getInserter() {
                        return null;
                    }

                }, cssVersion);

    }

    /**
     * Ensure we render the WML validation format.
     *
     * @throws Exception
     */
    public void testRender() throws Exception {
        PropertyRenderer renderer = new RuntimeWapInputFormatRenderer();
        StyleProperty property = StylePropertyDetails.MCS_INPUT_FORMAT;
        String expected = "-wap-input-format:\"N*\";";
        StyleValue value =
            StyleValueFactory.getDefaultInstance().getString(null, "N:N*");


        checkRender(context, renderer, property, value, expected);

    }

    /**
     * Ensure nothing is rendered if NONE keyword is specified.
     *
     * @throws Exception
     */
    public void testRenderNoneKeyword() throws Exception {
        PropertyRenderer renderer = new RuntimeWapInputFormatRenderer();
        StyleProperty property = StylePropertyDetails.MCS_INPUT_FORMAT;
        String expected = "";
        StyleValue value = MCSInputFormatKeywords.NONE;


        checkRender(context, renderer, property, value, expected);

    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Allowed keyword mapper used by renderer to be overridden by CSSVersion, created keyword mapper for vertical-align

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 ===========================================================================
*/
