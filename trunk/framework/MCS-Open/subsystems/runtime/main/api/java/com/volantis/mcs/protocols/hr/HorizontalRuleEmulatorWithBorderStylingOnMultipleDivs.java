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
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * This class is responsible for emulating the hr element using
 * multiple div elements.
 * <p>
 * The following hr: <p>
 * &lt;hr color="red" height="1" width="100%"
 * style="margin-top:2px; margin-bottom:5px">
 * <p>
 * will be emulated as follows: <p>
 * 
 * &lt;div margin-top="2px"></div> <br>  
 * &lt;div style="border-bottom: 1px solid red; width:100%; margin-top:0px; 
 * margin-bottom:0px></div> <br> 
 * &lt;div margin-bottom="5px"></div> 
 * <p>
 * This style of emulation is required as some devices, such as the 
 * Samsung Z108, do not support the emulation provided by 
 * {@link com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorWithBorderStylingOnDIV}
 * when margins are set to values other than zero. ( The Line created by the 
 * border-bottom styling attribute does not render when margins are set 
 * to a value other than zero).  Hence we need the extra div elements to set
 * the top and bottom margin in order to maintain the rule.
 */
public class HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs
        extends HorizontalRuleEmulatorWithBorderStylingOnDIV {

    public HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs() {
        super(BORDER_BOTTOM_PROPERTY);
    }  
    
     /**
     * Returns the central div used in this emulation.
     * <p>
     * The original interface for this method assumed that the hr element
     * would only ever be emulated using a single element and that this
     * element should be returned to the client.  The reason for this is
     * that if the hr element was being emulated with the hr element
     * itself but with alternative styling, say, then extra processing
     * within the protocol may be required on the hr element.
     * In this case hr emulation is being achieved with three div elements,
     * so what do we return?  In this case any of the div elements may be
     * returned as the intention is to notify the client
     * (one of the Protocol classes) that we are not using the hr element
     * and that no further processing is neccessary.
     *
     * @param domOutputBuffer the buffer to be written to.
     *
     * @return the central div element used in this emalation.
     */
     public Element doEmulation(DOMOutputBuffer domOutputBuffer,
                                  HorizontalRuleAttributes hrAttrs)
             throws ProtocolException {

         // Emulate the margin above the hr
         if (marginTop != null) {
            createDivEmulatingMargin(domOutputBuffer,
                    StylePropertyDetails.MARGIN_TOP,
                    marginTop);
         }

        // Emulate width, height and colour of hr
//        Element innerDiv = domOutputBuffer.addStyledElement("div", hrAttrs);
//        addBorderStyling(innerDiv);
         Element innerDiv = super.doEmulation(domOutputBuffer, hrAttrs);

        // Emulate the margin below the hr
        if (marginBottom != null) {
            createDivEmulatingMargin(domOutputBuffer,
                    StylePropertyDetails.MARGIN_BOTTOM,
                    marginBottom);
        }

        return innerDiv;
    }

    protected void setMargins(Element divHr) {
        // Do not add margins.
    }

    /**
     * Helper method to create a div element that is emulating the
     * margin set on the hr element.
     *
     * @param domOutputBuffer
     * @param property - the margin property to set
     * @param value - the value of the margin property
     */
    private void createDivEmulatingMargin(DOMOutputBuffer domOutputBuffer,
                                          StyleProperty property,
                                          StyleValue value) {

        // Open the div
        Element div = domOutputBuffer.addElement("div");

        // Apply margin styling
        Styles styles;
        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        Styles parentStyles = domOutputBuffer.getCurrentElement().getStyles();
        if (parentStyles != null) {
            styles = stylingFactory.createInheritedStyles(parentStyles,
                    DisplayKeywords.BLOCK);
        } else {
            styles = stylingFactory.createStyles(null);
        }
        div.setStyles(styles);

//            //remove the border styling for the outer divs
//            if (BORDER_BOTTOM_PROPERTY.equals(borderProperty)) {
//                styles.getPropertyValues().setComputedValue(
//                        StylePropertyDetails.BORDER_BOTTOM_COLOR, null);
//                styles.getPropertyValues().setComputedValue(
//                        StylePropertyDetails.BORDER_BOTTOM_STYLE, null);
//                styles.getPropertyValues().setComputedValue(
//                        StylePropertyDetails.BORDER_BOTTOM_WIDTH, null);
//            } else {
//                styles.getPropertyValues().setComputedValue(
//                        StylePropertyDetails.BORDER_TOP_COLOR, null);
//                styles.getPropertyValues().setComputedValue(
//                        StylePropertyDetails.BORDER_TOP_STYLE, null);
//                styles.getPropertyValues().setComputedValue(
//                        StylePropertyDetails.BORDER_TOP_WIDTH, null);
//            }

        MutablePropertyValues propertyValues = styles.getPropertyValues();
        propertyValues.setComputedAndSpecifiedValue(property, value);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Oct-05	9565/11	ibush	VBM:2005081219 Horizontal Rule Emulation

 25-Oct-05	9565/9	ibush	VBM:2005081219 Horizontal Rule Emulation

 25-Oct-05	9565/7	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/3	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/1	ibush	VBM:2005081219 HR Rule Emulation

 ===========================================================================
*/
