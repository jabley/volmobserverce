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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/css/emulator/styles/WapTV5_WMLVersion1_3Style.java,v 1.15 2003/03/19 09:52:58 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Jul-02    Ian             VBM:2002072603 - Created from
 *                              WapTV1_WMLVersion1_2Style.java.
 * 06-Aug-02    Ian             VBM:2002080603 - Support new WapTV stylistic
 *                              properties.
 * 08-Aug-02    Ian             VBM:2002080603 - Updated Javadoc.
 * 13-Aug-02    Paul            VBM:2002080603 - Check for null in
 *                              addPixelLength
 * 11-Sep-02    Ian             VBM:2002090602 - Removed references to
 *                              MarinerRowGap.
 * 16-Sep-02    Ian             VBM:2002091006 - Added support for
 *                              marinerFocus.
 * 02-Dec-02    Sumit           VBM:2002101505 - addFontFamily does not add a 
 *                              default font of helvetica
 * 18-Dec-02    Adrian          VBM:2002110106 - updated addPosition check that
 *                              the StyleValue is not invalid instead of is 
 *                              invalid 
 * 20-Dec-02    Sumit           VBM:2002101505 - addFontFamily signature now 
 *                              takes param to add default font of helvetica
 * 16-Jan-03    Adrian          VBM:2002110505 - Updated addBackgroundPosition
 *                              and addPosition to use the new method 
 *                              DOMProtocolStyle.doAddLengthPair(). 
 * 24-Feb-03    Ian             VBM:2002072414 - Refactored methods to use
 *                              addPixelLength.
 * 18-Mar-03    Byron           VBM:2003031105 - Added getPixelLength() and
 *                              modifed several addXX(...) methods to use new
 *                              method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml.css.emulator.styles;

import com.volantis.mcs.css.mappers.FontFamilyKeywordMapper;
import com.volantis.mcs.css.mappers.MarinerImageRepeatCountKeywordMapper;
import com.volantis.mcs.css.mappers.PositionKeywordMapper;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.EmulatorRendererContext;
import com.volantis.mcs.protocols.vdxml.StylePropertiesConvertor;
import com.volantis.mcs.protocols.wml.WapTV5_WMLVersion1_3;
import com.volantis.mcs.runtime.css.emulator.mappers.waptv5.FontSizeKeywordMapper;
import com.volantis.mcs.runtime.css.emulator.mappers.waptv5.MarinerFocusKeywordMapper;
import com.volantis.mcs.runtime.css.emulator.mappers.waptv5.TextAlignKeywordMapper;
import com.volantis.mcs.runtime.css.emulator.mappers.waptv5.WapTV5_WMLVersion1_3KeywordMapperFactory;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.mappers.KeywordMapper;
import com.volantis.mcs.themes.properties.MCSFocusKeywords;
import com.volantis.mcs.themes.properties.PositionKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import com.volantis.styling.values.PropertyValues;

import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * The Style class for the WapTV5_WMLVersion1_3 protocol.
 *
 * @deprecated use {@link StyleProperties} for checking style properties and
 *             {@link com.volantis.mcs.protocols.css.emulator.StyleEmulationRenderer}
 *             for rendering emulation markup for style properties.
 */
public final class WapTV5_WMLVersion1_3Style {

    /**
     * Valid font sizes.
     */
    private static final String[] szStr =
            {"6", "8", "10", "12", "14", "16", "18"};

    /**
     * Percentage representation of valid font sizes
     */
    private static final int[] percent = {50, 50, 75, 100, 125, 150, 200};

    /**
     * em representation of valid font sizes
     */
    private static final double[] em =
            {0.25, 0.50, 0.75, 1.00, 1.25, 1.50, 1.75};
    /**
     * The StyleProperties which make up our source of stylistic information
     */
    private final StyleProperties styles;
    /**
     * The RenderContext which will process the style values
     */
    private final EmulatorRendererContext context;


    /**
     * Constructor for this style.
     */
    public WapTV5_WMLVersion1_3Style(
            PropertyValues styles,
            WapTV5_WMLVersion1_3 protocol) {

        this.styles = StylePropertiesConvertor.wrap(styles);

        context = protocol.getEmulatorRendererContext();
        context.setKeywordMapperFactory(
                WapTV5_WMLVersion1_3KeywordMapperFactory.getSingleton());
    }

    /**
     * Return the pixel length for the given style value. Note that the result
     * is only returned if the StyleValue is a LENGTH and its units are PIXELS.
     *
     * @param styleValue the style value object.
     * @return the pixel length of the style length object, or 0 if
     *         not a style length with pixels object.
     */
    private int getPixelLength(StyleValue styleValue) {
        int value = 0;
        if (styleValue instanceof StyleLength) {
            StyleLength styleLength = (StyleLength) styleValue;
            if (styleLength.getUnit() == LengthUnit.PX) {
                value = styleLength.pixels();
            }
        }
        return value;
    }

    /**
     * Create the hpad attribute from the left and right padding values;
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addHorizontalPadding(Element element, String attribute) {
        int leftPadding = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.PADDING_LEFT));
        int rightPadding = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.PADDING_RIGHT));

        int hpad = (leftPadding + rightPadding) / 2;

        if (hpad != 0) {
            element.setAttribute(attribute, String.valueOf(hpad));
        }
    }

    /**
     * Create the vpad attribute from the top and bottom padding values
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addVerticalPadding(Element element, String attribute) {

        int topPadding = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.PADDING_TOP));
        int bottomPadding = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.PADDING_BOTTOM));

        int vpad = (topPadding + bottomPadding) / 2;

        if (vpad != 0) {
            element.setAttribute(attribute, String.valueOf(vpad));
        }
    }

    /**
     * Create the hspace attribute from the left and right margin values
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addHorizontalSpace(Element element, String attribute) {

        int leftMargin = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.MARGIN_LEFT));
        int rightMargin = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.MARGIN_RIGHT));

        int hspace = (leftMargin + rightMargin) / 2;

        if (hspace != 0) {
            element.setAttribute(attribute, String.valueOf(hspace));
        }
    }

    /**
     * Create the vspace attribute from the top and bottom margin values
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addVerticalSpace(Element element, String attribute) {

        int topMargin = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.MARGIN_TOP));
        int bottomMargin = getPixelLength(styles.getStyleValue(
                StylePropertyDetails.MARGIN_BOTTOM));

        int vspace = (topMargin + bottomMargin) / 2;

        if (vspace != 0) {
            element.setAttribute(attribute, String.valueOf(vspace));
        }
    }

    protected void addPixelLength(
            Element element, String attribute,
            StyleValue value) {
        if (value instanceof StyleLength) {
            StyleLength length = (StyleLength) value;
            if (length.getUnit() == LengthUnit.PX) {
                element.setAttribute(attribute,
                        String.valueOf(length.pixels()));
            }
        }
    }

    /**
     * add the value of the marinerLineGap property.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addMarinerCornerRadius(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_CORNER_RADIUS);
        addPixelLength(element, attribute, value);
    }

    /**
     * add the value of the marinerLineGap property.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addMarinerLineGap(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_LINE_GAP);
        addPixelLength(element, attribute, value);
    }

    /**
     * add the value of the marinerParagraphGap property.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addMarinerParagraphGap(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_PARAGRAPH_GAP);
        addPixelLength(element, attribute, value);
    }

    /**
     * get the value of the borderHorizontalSpacing property.
     */
    public void addBorderHorizontalSpacing(Element element, String attribute) {
        StyleValue pair = styles.getStyleValue(
                StylePropertyDetails.BORDER_SPACING);
        if (pair != null && pair instanceof StylePair) {
            StyleValue value = ((StylePair) pair).getFirst();
            addPixelLength(element, attribute, value);
        }
    }

    /**
     * get the value of the borderVerticalSpacing property.
     */
    public void addBorderVerticalSpacing(Element element, String attribute) {
        StyleValue pair = styles.getStyleValue(
                StylePropertyDetails.BORDER_SPACING);
        if (pair != null && pair instanceof StylePair) {
            StyleValue value = ((StylePair) pair).getSecond();
            addPixelLength(element, attribute, value);
        }
    }

    /**
     * add the value of the textAlign property.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addTextAlign(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.TEXT_ALIGN);
        if (value != null) {
            context.setKeywordMapper(getTextAlignKeywordMapper());
            ReusableStringBuffer buffer = context.getRenderValue(value);
            if (buffer != null) {
                String attrValue = buffer.toString();
                if ("c".equals(attrValue) || "r".equals(attrValue)) {
                    element.setAttribute("align", attrValue);
                }
            }
        }
    }

    /**
     * Get the vspace attribute for body tag in paragraph
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addVspace(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_PARAGRAPH_GAP);
        if (value instanceof StyleLength) {
            StyleLength length = (StyleLength) value;
            if (length.getUnit() == LengthUnit.PX) {
                int result = (length.pixels() + 1) / 2;
                element.setAttribute(attribute, String.valueOf(result));
            }
        }
    }

    /**
     * add the font size to the specified element.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addFontSize(Element element, String attribute) {
        StyleValue styleValue = styles.getStyleValue(
                StylePropertyDetails.FONT_SIZE);
        if (styleValue != null) {
            if (styleValue.getStyleValueType() == StyleValueType.KEYWORD) {
                context.setKeywordMapper(getFontSizeKeywordMapper());
            }
            String value = context.getRenderValue(styleValue).toString();

            if (value != null) {
                for (int i = 0; i < szStr.length; i++) {
                    if (szStr[i].equals(value)) {
                        element.setAttribute(attribute, value);
                        return;
                    }
                }
            }

            String number;
            if (value.endsWith("%")) {
                number = value.substring(0, value.length() - 1);
                double fpc = Double.parseDouble(number);
                for (int i = percent.length - 1; i >= 0; i--) {
                    if (fpc >= percent[i]) {
                        element.setAttribute(attribute, szStr[i]);
                        return;
                    }
                }
            }

            if (value.endsWith("px")) {
                number = value.substring(0, value.length() - 2);
                int fpx = Integer.parseInt(number);

                if (fpx < 12) {
                    element.setAttribute(attribute, szStr[2]);
                    return;
                } else if (fpx > 12) {
                    element.setAttribute(attribute, szStr[5]);
                    return;
                } else if (fpx == 12) {
                    element.setAttribute(attribute, szStr[3]);
                    return;
                }
            }

            if (value.endsWith("em")) {
                number = value.substring(0, value.length() - 2);
                double fem = Double.parseDouble(number);

                for (int i = em.length - 1; i >= 0; i--) {
                    if (fem >= em[i]) {
                        element.setAttribute(attribute, szStr[i]);
                        return;
                    }
                }
            }
        }
    }

    /**
     * get the value of the fontFamily property.
     *
     * @return the value of the fontFamily property.
     */
    private String getFontFamily() {
        StyleList listValue = (StyleList) styles
                .getStyleValue(StylePropertyDetails.FONT_FAMILY);
        if (listValue != null && listValue.getList() != null) {
            Iterator i = listValue.getList().iterator();
            ReusableStringBuffer buffer = new ReusableStringBuffer();
            while (i.hasNext()) {
                StyleValue value = (StyleValue) i.next();
                context.setKeywordMapper(getFontFamilyKeywordMapper());
                if (buffer.length() == 0) {
                    buffer.append(context.getRenderValue(value));
                } else {
                    buffer.append(", ").append(context.getRenderValue(value));
                }
            }
            return (buffer.length() == 0 ? null : buffer.toString());
        }
        return null;
    }

    /**
     * add the fontFamily size to the specified element. If there isn't a
     * font available set it to the protocol default of helvetica.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addFontFamily(
            Element element, String attribute, boolean addDefault) {
        String fontFamily = getFontFamily();
        if (fontFamily != null) {
            StringTokenizer st = new StringTokenizer(fontFamily, ",");
            String fontName = st.nextToken();
            element.setAttribute(attribute, fontName);
        } else {
            if (addDefault) {
                element.setAttribute(attribute, "helvetica");
            }
        }
    }

    /**
     * add the background position to the specified element.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addBackgroundPosition(Element element, String attribute) {
        StyleValue pair = styles.getStyleValue(
                StylePropertyDetails.BACKGROUND_POSITION);

        if (pair != null && pair instanceof StylePair) {
            StyleValue first = ((StylePair) pair).getFirst();
            StyleValue second = ((StylePair) pair).getSecond();
            doAddLengthPair(first, second, element, attribute,
                    LengthUnit.PX);
        }
    }

    /**
     * add the background position to the specified element.
     *
     * @param element   The DOM Element to add the style value
     * @param attribute The name to give the attribute in which the style value
     *                  will be stored.
     */
    public void addPosition(Element element, String attribute) {
        StyleValue position = styles.getStyleValue(
                StylePropertyDetails.POSITION);
        if (position == PositionKeywords.ABSOLUTE) {

            context.setKeywordMapper(getPositionEdgeKeywordMapper());
            StyleValue top = styles.getStyleValue(StylePropertyDetails.TOP);
            StyleValue left = styles.getStyleValue(StylePropertyDetails.LEFT);
            doAddLengthPair(left, top, element, attribute,
                    LengthUnit.PX);
        }
    }

    /**
     * add the value of the marinerFocus property.
     */
    public void addMarinerFocus(
            Element element,
            String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_FOCUS);
        if (value == MCSFocusKeywords.IGNORE) {
            context.setKeywordMapper(getMarinerFocusKeywordMapper());
            context.renderValue(value, element, attribute);
        }
    }

    //===========================================================================
    // KEYWORD MAPPERS
    //===========================================================================

    /**
     * Get the FontSizeKeywordMapper.
     */
    private KeywordMapper getFontSizeKeywordMapper() {
        return FontSizeKeywordMapper.getSingleton();
    }

    /**
     * Get the MArinerFocusKeywordMapper.
     */
    private KeywordMapper getMarinerFocusKeywordMapper() {
        return MarinerFocusKeywordMapper.getSingleton();
    }

    /**
     * Get the TextAlignKeywordMapper.
     */
    private KeywordMapper getTextAlignKeywordMapper() {
        return TextAlignKeywordMapper.getSingleton();
    }

    /**
     * get the value of the fontFamily property.
     */
    public void addFontFamily(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.FONT_FAMILY);
        if (value != null) {
            context.setKeywordMapper(getFontFamilyKeywordMapper());
            context.renderValue(value, element, attribute);
        }
    }

    /**
     * get the value of the marinerCaretColor property.
     */
    public void addMarinerCaretColor(Element element, String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_CARET_COLOR);
        if (value != null) {
            context.renderValue(value, element, attribute);
        }
    }

    /**
     * add the value of the marinerImageRepeatCount property.
     */
    public void addMarinerImageRepeatCount(
            Element element,
            String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_IMAGE_REPEAT_COUNT);
        if (value != null) {
            context.setKeywordMapper(getMarinerImageRepeatCountKeywordMapper());
            context.renderValue(value, element, attribute);
        }
    }

    /**
     * add the value of the marinerImageFrameInterval property.
     */
    public void addMarinerImageFrameInterval(
            Element element,
            String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_IMAGE_FRAME_INTERVAL);
        if (value != null) {
            context.renderValue(value, element, attribute);
        }
    }

    /**
     * add the value of the marinerImageInitialFrame property.
     */
    public void addMarinerImageInitialFrame(
            Element element,
            String attribute) {
        StyleValue value = styles.getStyleValue(
                StylePropertyDetails.MCS_IMAGE_INITIAL_FRAME);
        if (value != null) {
            context.renderValue(value, element, attribute);
        }
    }

    /**
     * Utility method to add two style values to an element in the format
     * myelement myattribute="value1, value2".
     * The StyleValues must both be StyleLengths and be of a specified unit.
     * The unit suffix will be removed from the output.
     *
     * @param value1         The first StyleValue to add to the attribute.
     * @param value2         The second StyleValue to add to the attribute.
     * @param element        The element to which the new attribute will be added.
     * @param attribute      The attribute to which the values will be added.
     * @param unitConstraint The unit to which the StyleValues must conform.
     */
    private void doAddLengthPair(
            StyleValue value1, StyleValue value2,
            Element element, String attribute,
            LengthUnit unitConstraint) {
        if (value1 != null && value1 instanceof StyleLength &&
                value2 != null && value2 instanceof StyleLength) {

            StyleLength length1 = (StyleLength) value1;
            StyleLength length2 = (StyleLength) value2;

            if (length1.getUnit() == unitConstraint &&
                    length2.getUnit() == unitConstraint) {
                int unitLength = unitConstraint.toString().length();
                StringBuffer buffer = new StringBuffer();

                String value = context.getRenderValue(length1).toString();
                String number = value.substring(0, value.length() - unitLength);
                double doubleVal = Double.parseDouble(number);

                buffer.append((int) doubleVal);
                buffer.append(',');

                value = context.getRenderValue(length2).toString();
                number = value.substring(0, value.length() - unitLength);
                doubleVal = Double.parseDouble(number);

                buffer.append((int) doubleVal);
                element.setAttribute(attribute, buffer.toString());
            }
        }
    }

    /**
     * Get the FontFamilyKeywordMapper.
     */
    private KeywordMapper getFontFamilyKeywordMapper() {
        return FontFamilyKeywordMapper.getSingleton();
    }

    /**
     * Get the MarinerImageRepeatCountKeywordMapper.
     */
    private KeywordMapper getMarinerImageRepeatCountKeywordMapper() {
        return MarinerImageRepeatCountKeywordMapper.getSingleton();
    }

    /**
     * Get the PositionEdgeKeywordMapper.
     */
    private KeywordMapper getPositionEdgeKeywordMapper() {
        return PositionKeywordMapper.getSingleton();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 20-Jul-04	4897/3	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
