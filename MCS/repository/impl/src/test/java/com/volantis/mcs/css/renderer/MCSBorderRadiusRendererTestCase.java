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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.renderer;

import java.io.IOException;

import com.volantis.mcs.css.version.CSSPropertyMock;
import com.volantis.mcs.css.version.CSSVersionMock;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.properties.StyleProperty;

/**
 * Test case for Border Radius renderer
 */
public class MCSBorderRadiusRendererTestCase
        extends PropertyRendererTestAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    protected CSSVersionMock cssVersionMock;
    
    protected void setUpRendererContext() {
        cssVersionMock = new CSSVersionMock("cssVersion", expectations);
        
        // Checks which are always made even if the styles are not actually present
        // in the processed stylesheet. 
        cssVersionMock.expects.getProperty(StylePropertyDetails.VERTICAL_ALIGN).returns(null);
        cssVersionMock.expects.supportsShorthand(StyleShorthands.LIST_STYLE).returns(false);
        
        context = new RendererContext(writer, styleSheetRenderer, null, cssVersionMock);        
    }
    
    /**
     * Test translation of single property for WebKit-based browsers
     */
    public void testSinglePropertyForWebKit() throws IOException {

        StylePair value = STYLE_VALUE_FACTORY.getPair(
            STYLE_VALUE_FACTORY.getLength(null, 10.0, LengthUnit.PX),
            STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX)); 
        
        setupPropertyAndAlias(
                StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS,
                value, "-webkit-border-top-right-radius");

        assertEquals("-webkit-border-top-right-radius:10px 5px", renderStyles());        
    }

    /**
     * Test translation of single property for Gecko-based browsers
     */
    public void testSinglePropertyForMozilla() throws IOException {

        StylePair value = STYLE_VALUE_FACTORY.getPair(
            STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX),
            STYLE_VALUE_FACTORY.getLength(null, 10.0, LengthUnit.PX)); 
        
        setupPropertyAndAlias(
                StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS,
                value, "-moz-border-radius-topleft");


        assertEquals("-moz-border-radius-topleft:5px", renderStyles());
    }

    /**
     * Test translation of single property for browsers
     * supporting broder-radius from CSS3
     */
    public void testSinglePropertyForCSS3() throws IOException {

        StylePair value = STYLE_VALUE_FACTORY.getPair(
            STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX),
            STYLE_VALUE_FACTORY.getLength(null, 6.0, LengthUnit.PX));
        
        setupPropertyAndAlias(
                StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS,
                value, "border-bottom-left-radius");

        assertEquals("border-bottom-left-radius:5px 6px", renderStyles());
    }


    /**
     * Test translation of multiple properties for Gecko-based browsers
     */
    public void testMultiplePropertiesForMozilla() throws IOException {

        StylePair value = STYLE_VALUE_FACTORY.getPair(
            STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX),
            STYLE_VALUE_FACTORY.getLength(null, 10.0, LengthUnit.PX));
        
        setupPropertyAndAlias(
                StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS,
                value, "-moz-border-radius-topleft");
        setupPropertyAndAlias(
                StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS,
                value, "-moz-border-radius-topright");
        setupPropertyAndAlias(
                StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS,
                value, "-moz-border-radius-bottomright");


        assertEquals("-moz-border-radius-topleft:5px;" +
                     "-moz-border-radius-topright:5px;" +
                     "-moz-border-radius-bottomright:5px", renderStyles());
    }

    /**
     * Test translation of shorthand property for WebKit-based browsers
     */
    public void testShorthandForWebKit() throws IOException {

        ShorthandValue value = new ShorthandValue(
            StyleShorthands.MCS_BORDER_RADIUS,
            new StyleValue[] {
                STYLE_VALUE_FACTORY.getPair(
                    STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX),
                    STYLE_VALUE_FACTORY.getLength(null, 10.0, LengthUnit.PX))
            },
            Priority.NORMAL);
        
        properties.setShorthandValue(value);
        cssVersionMock.expects.supportsShorthand("border-radius")
            .returns(false);
        cssVersionMock.expects.supportsShorthand("-moz-border-radius")
            .returns(false);
        cssVersionMock.expects.supportsShorthand("-webkit-border-radius")
            .returns(true);
        
        assertEquals("-webkit-border-radius:5px 10px", renderStyles());
    }

    /**
     * Test translation of shorthand property for Gecko-based browsers
     */
    public void testShorthandForMozilla() throws IOException {

        ShorthandValue value = new ShorthandValue(
            StyleShorthands.MCS_BORDER_RADIUS,
            new StyleValue[] {
                STYLE_VALUE_FACTORY.getPair(
                    STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX),
                    STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX))
            },
            Priority.NORMAL);
        
        properties.setShorthandValue(value);
        cssVersionMock.expects.supportsShorthand("border-radius")
            .returns(false);
        cssVersionMock.expects.supportsShorthand("-moz-border-radius")
            .returns(true);
        
        assertEquals("-moz-border-radius:5px", renderStyles());
    }

    /**
     * Test translation of shorthand property for browsers
     * supporting border-radius from CSS3
     */
    public void testShorthandForCSS3() throws IOException {

        ShorthandValue value = new ShorthandValue(
            StyleShorthands.MCS_BORDER_RADIUS,
            new StyleValue[] {
                STYLE_VALUE_FACTORY.getPair(
                    STYLE_VALUE_FACTORY.getLength(null, 10.0, LengthUnit.PX),
                    STYLE_VALUE_FACTORY.getLength(null, 5.0, LengthUnit.PX))
            },
            Priority.NORMAL);
        
        properties.setShorthandValue(value);
        cssVersionMock.expects.supportsShorthand("border-radius")
            .returns(true);
        
        assertEquals("border-radius:10px 5px", renderStyles());
    }

    protected void setupPropertyAndAlias(StyleProperty property, StyleValue value, String alias) {
        
        CSSPropertyMock cssPropertyMock = new CSSPropertyMock("cssPropertyMock", expectations);         
        properties.setStyleValue(property, value);
        
        cssVersionMock
            .expects.getProperty(property)
            .returns(cssPropertyMock);
        
        cssVersionMock
            .expects.getPropertyAlias(property)
            .returns(alias);        
    }
    
    protected String renderStyles() throws IOException {        
        styleSheetRenderer.renderStyleProperties(properties, context);
        context.flushStyleSheet();
        String result = writer.getBuffer().toString();        
        System.out.println(result);
        return result;
    }
}
