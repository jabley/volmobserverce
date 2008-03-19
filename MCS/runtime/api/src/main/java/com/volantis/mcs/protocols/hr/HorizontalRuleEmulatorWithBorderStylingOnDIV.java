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
package com.volantis.mcs.protocols.hr;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BackgroundColorKeywords;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * This class is responsible for emulating a horizontal rule by applying
 * border styling to the div element.
 * <p>
 * i.e <br/>
 * border-bottom: 1px solid black; <br/>
 * width: 100%;<br/>
 * margin-bottom: 2px;<br/>
 * margin-top: 2px
 * </p>
 * <p>
 * Border margins are required or hr otherwise the following will
 * render without any space in between. <br/>
 * &lt;hr/> <br/>
 * &lt;hr> <br/>
 * &lt;hr>
 * </p>
 * <p>
 * This is required as some browsers do not honour standard styling on the
 * hr element, e.g: width, height etc, but do support border styling on the
 * div, which gives the same result when rendered.
 * </p>
 */
public class HorizontalRuleEmulatorWithBorderStylingOnDIV
        extends DefaultHorizontalRuleEmulator {

    /**
     * Constant for the CSS border-bottom property.
     */
    public static final String BORDER_BOTTOM_PROPERTY = "border-bottom: ";

    /**
     * Constant for the CSS border-top property.
     */
    public static final String BORDER_TOP_PROPERTY = "border-top: ";

    private static final StyleLength DEFAULT_VERTICAL_SPACING =
        StyleValueFactory.getDefaultInstance().getLength(null, 2, LengthUnit.PX);

    /**
     * Property to indicate which border the styling should be applied to
     */
    private final String borderProperty;

    /**
     * size of the top margin on the div
     */
    protected StyleValue marginTop;
    /**
     * size of the bottom margin on the div
     */
    protected StyleValue marginBottom;


    /**
     * Constructor
     * @param borderProperty
     */
    public HorizontalRuleEmulatorWithBorderStylingOnDIV(String borderProperty) {
        if (borderProperty != null) {
            this.borderProperty = borderProperty;
        } else {
            this.borderProperty = BORDER_BOTTOM_PROPERTY;
        }
    }

    //javadoc inherited
    protected Element doEmulation(DOMOutputBuffer domOutputBuffer,
                                         HorizontalRuleAttributes hrAttrs)
            throws ProtocolException {

        Element divHr = domOutputBuffer.addStyledElement ("div", hrAttrs);

        addBorderStyling(divHr);
        setMargins(divHr);

        return divHr;
    }


    /**
     * Add the additional markup required to emulate the hr element using the
     * div
     * @param divHr
     */
    private void addBorderStyling(Element divHr) {

        Styles styles = divHr.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        // Handle border-bottom style attribute
        if (BORDER_BOTTOM_PROPERTY.equals(borderProperty)) {
            propertyValues.setComputedValue(
                    StylePropertyDetails.BORDER_BOTTOM_WIDTH, height);
            propertyValues.setComputedValue(
                    StylePropertyDetails.BORDER_BOTTOM_STYLE,
                    BorderStyleKeywords.SOLID);
            propertyValues.setComputedValue(
                    StylePropertyDetails.BORDER_BOTTOM_COLOR, color);
        } else {
            propertyValues.setComputedValue(
                    StylePropertyDetails.BORDER_TOP_WIDTH, height);
            propertyValues.setComputedValue(
                    StylePropertyDetails.BORDER_TOP_STYLE,
                    BorderStyleKeywords.SOLID);
            propertyValues.setComputedValue(
                    StylePropertyDetails.BORDER_TOP_COLOR, color);
        }

        // Handle width attribute
        propertyValues.setComputedValue(StylePropertyDetails.WIDTH, width);

        // Handle text-align style attribute
        propertyValues.setComputedValue(StylePropertyDetails.TEXT_ALIGN, align);

        // Clear Background color as it should not effect styling, but it does
        // on a LG U8150.
        propertyValues.setComputedValue(StylePropertyDetails.BACKGROUND_COLOR,
                BackgroundColorKeywords.TRANSPARENT);

    }

    /**
     * append the margin default values if they have not already been set
     * @param divHr Emulation Element
     */
    protected void setMargins(Element divHr) {
        Styles styles = divHr.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();

        // Handle margin-top and margin-bottom
        propertyValues.setComputedValue(StylePropertyDetails.MARGIN_BOTTOM,
                marginBottom);

        propertyValues.setComputedValue(StylePropertyDetails.MARGIN_TOP,
                marginTop);
    }

    //javadoc inherited
    protected void initialiseState(HorizontalRuleAttributes hrAttrs) {
        super.initialiseState(hrAttrs);

        MutablePropertyValues propertyValues =
                hrAttrs.getStyles().getPropertyValues();

        if (propertyValues.wasExplicitlySpecified(StylePropertyDetails.MARGIN_TOP)) {
            marginTop = propertyValues.getComputedValue(
                    StylePropertyDetails.MARGIN_TOP);
        } else {
            marginTop = DEFAULT_VERTICAL_SPACING;
        }

        if (propertyValues.wasExplicitlySpecified(StylePropertyDetails.MARGIN_BOTTOM)) {
            marginBottom = propertyValues.getComputedValue(
                    StylePropertyDetails.MARGIN_BOTTOM);
        } else {
            marginBottom = DEFAULT_VERTICAL_SPACING;
        }
    }

    /**
     * Method intended for testing purposes only. Gets the border property
     * @return
     */
    public String getBorderProperty() {
        return this.borderProperty;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 25-Oct-05	9565/11	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/5	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 ===========================================================================
*/
