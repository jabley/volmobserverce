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

package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetSource;
import com.volantis.styling.debug.DebugStyles;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StylePropertySet;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;

public abstract class FormatRuleBuilderTestAbstract
        extends TestCaseAbstract {

    protected static final StylePropertySet FORMAT_STYLING_PROPERTIES;
    protected static final StylePropertySet COLUMN_STYLING_PROPERTIES;
    protected static final StylePropertySet ROW_STYLING_PROPERTIES;
    protected static final StylePropertySet CELL_STYLING_PROPERTIES;

    static {
        MutableStylePropertySet properties = new MutableStylePropertySetImpl();
        properties.add(StylePropertyDetails.BACKGROUND_COLOR);
        properties.add(StylePropertyDetails.BACKGROUND_IMAGE);
        properties.add(StylePropertyDetails.WIDTH);
        properties.add(StylePropertyDetails.HEIGHT);
        properties.add(StylePropertyDetails.TEXT_ALIGN);
        properties.add(StylePropertyDetails.VERTICAL_ALIGN);
        properties.add(StylePropertyDetails.BORDER_BOTTOM_STYLE);
        properties.add(StylePropertyDetails.BORDER_BOTTOM_WIDTH);
        properties.add(StylePropertyDetails.BORDER_TOP_STYLE);
        properties.add(StylePropertyDetails.BORDER_TOP_WIDTH);
        properties.add(StylePropertyDetails.BORDER_LEFT_STYLE);
        properties.add(StylePropertyDetails.BORDER_LEFT_WIDTH);
        properties.add(StylePropertyDetails.BORDER_RIGHT_STYLE);
        properties.add(StylePropertyDetails.BORDER_RIGHT_WIDTH);
        properties.add(StylePropertyDetails.BORDER_SPACING);
        properties.add(StylePropertyDetails.PADDING_BOTTOM);
        properties.add(StylePropertyDetails.PADDING_TOP);
        properties.add(StylePropertyDetails.PADDING_LEFT);
        properties.add(StylePropertyDetails.PADDING_RIGHT);
        FORMAT_STYLING_PROPERTIES = properties;

        properties = new MutableStylePropertySetImpl();
        properties.add(StylePropertyDetails.WIDTH);
        COLUMN_STYLING_PROPERTIES = properties;

        properties = new MutableStylePropertySetImpl();
        properties.add(StylePropertyDetails.HEIGHT);
        properties.add(StylePropertyDetails.TEXT_ALIGN);
        properties.add(StylePropertyDetails.VERTICAL_ALIGN);
        ROW_STYLING_PROPERTIES = properties;

        properties = new MutableStylePropertySetImpl();
        properties.add(StylePropertyDetails.TEXT_ALIGN);
        properties.add(StylePropertyDetails.VERTICAL_ALIGN);
        properties.add(StylePropertyDetails.PADDING_BOTTOM);
        properties.add(StylePropertyDetails.PADDING_TOP);
        properties.add(StylePropertyDetails.PADDING_LEFT);
        properties.add(StylePropertyDetails.PADDING_RIGHT);
        CELL_STYLING_PROPERTIES = properties;
    }

    protected CanvasLayout canvasLayout;
    private DebugStyles debugStyles;

    protected void setUp() throws Exception {
        super.setUp();

        canvasLayout = new CanvasLayout();

        debugStyles = new DebugStyles(FORMAT_STYLING_PROPERTIES, false);
    }

    public String getStylesAsString(Styles styles) {
        return debugStyles.output(styles, "");
    }

    public String getStylesAsString(Styles styles, StylePropertySet interesting) {
        return new DebugStyles(interesting, false).output(styles, "");
    }

    protected FormatStylingEngine createFormatStylingEngine(
            Layout layout) throws IOException {

        LayoutStyleSheetBuilder builder = new LayoutStyleSheetBuilderImpl();
        StyleSheet styleSheet = builder.build(layout);

//        String css = StyleSheetTester.renderStyleSheet(styleSheet);
//        System.out.println("CSS\n" + css);

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();

        CompilerConfiguration configuration =
                stylingFactory.createCompilerConfiguration();
        configuration.setSource(StyleSheetSource.LAYOUT);

        StyleSheetCompiler compiler = stylingFactory.createStyleSheetCompiler(
                configuration);
        CompiledStyleSheet compiledStyleSheet = compiler.compileStyleSheet(
                styleSheet);

        StylingEngine stylingEngine = stylingFactory.createStylingEngine();

        stylingEngine.pushStyleSheet(compiledStyleSheet);

        FormatStylingEngine formatStylingEngine =
                new FormatStylingEngineImpl(stylingEngine);
        return formatStylingEngine;
    }

    public void assertEquals(String message, String expected, Styles actualStyles) {

        Styles expectedStyles = StylesBuilder.getStyles(expected);
        expected = getStylesAsString(expectedStyles);
        String actual = getStylesAsString(actualStyles);

        assertEquals(message, expected, actual);
    }

    public void assertEquals(String message, String expected,
                             Styles actualStyles, StylePropertySet interesting) {

        Styles expectedStyles = StylesBuilder.getStyles(expected);
        expected = getStylesAsString(expectedStyles, interesting);
        String actual = getStylesAsString(actualStyles, interesting);

        assertEquals(message, expected, actual);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 02-Sep-05	9407/1	pduffin	VBM:2005083007 Committing resolved conflicts

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 ===========================================================================
*/
