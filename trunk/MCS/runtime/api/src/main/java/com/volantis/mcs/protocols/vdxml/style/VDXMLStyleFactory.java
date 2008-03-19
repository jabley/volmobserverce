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
package com.volantis.mcs.protocols.vdxml.style;

import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLBackgroundColorProperty;
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLBlinkStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLFontColorProperty;
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLFontSizeProperty;
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLUnderlineStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLBinaryValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLFontSizeValue;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleColorName;
import com.volantis.mcs.themes.StyleColorPercentages;
import com.volantis.mcs.themes.StyleColorRGB;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueVisitorStub;
import com.volantis.mcs.themes.properties.FontSizeKeywords;
import com.volantis.mcs.themes.properties.FontStretchKeywords;
import com.volantis.mcs.themes.properties.MCSTextBlinkKeywords;
import com.volantis.mcs.themes.properties.MCSTextUnderlineStyleKeywords;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * A factory for creating VDXML style properties (and values) from normal
 * style properties (and values).
 * 
 * @todo avoid having the protocol render individual values itself.
 * We should extend the style infrastructure to handle automagic rendering 
 * of style properties for all tags, not just AC and AZ.
 *
 * @todo Remove the collector and character size and integrate them into this.
 */
public class VDXMLStyleFactory {

    /**
     * Visitor for collecting font size style values.
     */ 
    private static final FontSizeCollector FONT_SIZE_COLLECTOR = 
            new FontSizeCollector();
    
    /**
     * Visitor for collecting font stretch values.
     */ 
    private static final FontStretchCollector FONT_STRETCH_COLLECTOR = 
            new FontStretchCollector();
    
    /**
     * Return the VDXML style properties for the normal Styles provided.
     * <p>
     * Currently this method only handles the styles which can be expressed 
     * on an AC or AZ tag, thus most normal style properties are simply thrown 
     * away. 
     * 
     * @param styles the normal styles.
     * @return the calculated VDXML style properties.
     */ 
    public VDXMLStyleProperties getVDXMLStyle(Styles styles) {

        VDXMLStyleProperties result = null;
        if (styles != null) {
            MutablePropertyValues propertyValues = styles.getPropertyValues();
            if (propertyValues != null) {
                // Prepare the underline and blink values by visiting the text
                // decoration value - text decoration can be a bitset which is
                // why we can get 2 output values for 1 input value.
                StyleValue blinkStyle = propertyValues.getComputedValue(
                        StylePropertyDetails.MCS_TEXT_BLINK);
                VDXMLBinaryValue blink = getBinaryValue(
                        blinkStyle, MCSTextBlinkKeywords.BLINK);
                StyleValue underlineStyle = propertyValues.getComputedValue(
                        StylePropertyDetails.MCS_TEXT_UNDERLINE_STYLE);
                VDXMLBinaryValue underline = getBinaryValue(
                        underlineStyle, MCSTextUnderlineStyleKeywords.SOLID);

                //
                // Area Display
                //

                // Background Color
                StyleValue backgroundValue = propertyValues.getComputedValue(
                                        StylePropertyDetails.BACKGROUND_COLOR);
                if (backgroundValue instanceof StyleColor) {
                    VDXMLColorValue backgroundColor = getColor(
                            (StyleColor)backgroundValue);
                    if (backgroundColor != null) {
                        result = allocate(result);
                        result.setBackgroundColor(
                            new VDXMLBackgroundColorProperty(backgroundColor));
                    }
                }

                // Underline
                if (underline != null) {
                    result = allocate(result);
                    result.setUnderline(
                            new VDXMLUnderlineStyleProperty(underline));
                }

                //
                // Character Display
                //

                // Font Size
                VDXMLFontSizeValue fontSize = getCharacterSize(propertyValues);
                if (fontSize != null) {
                    result = allocate(result);
                    result.setFontSize(
                            new VDXMLFontSizeProperty(fontSize));
                }

                // Background Color
                VDXMLColorValue fontColor = getColor(
                        (StyleColor)propertyValues.getComputedValue(
                                StylePropertyDetails.COLOR));
                if (fontColor != null) {
                    result = allocate(result);
                    result.setFontColor(
                            new VDXMLFontColorProperty(fontColor));
                }

                // Blink
                if (blink != null) {
                    result = allocate(result);
                    result.setBlink(new VDXMLBlinkStyleProperty(blink));
                }
            }
        }
        return result;
    }

    private VDXMLStyleProperties allocate(VDXMLStyleProperties input) {
        
        if (input == null) {
            input = new VDXMLStyleProperties();
        }
        return input;
    }
    
    /**
     * Return the VDXML character size for the supplied style property values.
     * This value is used for rendering the TC attribute.
     * <p>
     * This will look into the style property values and calculate the value
     * based on the values of font size and stretch.
     * 
     * @param propertyValues the generic style property values.
     * @return the VDXML specific character size value to use, or null if one
     *      could not be found.
     */ 
    public VDXMLFontSizeValue getCharacterSize(
            MutablePropertyValues propertyValues) {
        if (propertyValues == null) {
            return null;
        }
        
        CharacterSizeBuilder builder = new CharacterSizeBuilder();
        StyleValue fontSize = propertyValues.getComputedValue(
                StylePropertyDetails.FONT_SIZE);
        if (fontSize != null) {
            fontSize.visit(FONT_SIZE_COLLECTOR, builder);
        }
        StyleValue fontStretch = propertyValues.getComputedValue(
                StylePropertyDetails.FONT_STRETCH);
        if (fontStretch != null) {
            fontStretch.visit(FONT_STRETCH_COLLECTOR, builder);
        }
        return builder.getCharacterSize();
    }

    /**
     * Return the VDXML color to use for a StyleColor value, or null if the 
     * color value is not recognised.
     * <p>
     * NOTE: As per the architecture, this method currently does not return a 
     * value unless there is an <strong>exact</strong> match on the name or 
     * (equivalent) rbg value. That is, we do no interpolation.
     *  
     * @param color the generic style color value. 
     * @return the VDXML specific color value to use, or null if one could not
     *  be found.
     */ 
    public VDXMLColorValue getColor(StyleColor color) {
        if (color == null) {
            return null;
        }

        if (color instanceof StyleColorName) {
            if (color == StyleColorNames.BLACK) {
                return VDXMLColorValue.BLACK;
            } else if (color == StyleColorNames.RED) {
                return VDXMLColorValue.RED;
            } else if (color == StyleColorNames.LIME) {
                return VDXMLColorValue.LIME;
            } else if (color == StyleColorNames.YELLOW) {
                return VDXMLColorValue.YELLOW;
            } else if (color == StyleColorNames.BLUE) {
                return VDXMLColorValue.BLUE;
            } else if (color == StyleColorNames.FUCHSIA) {
                return VDXMLColorValue.FUSCHIA;
            } else if (color == StyleColorNames.AQUA) {
                return VDXMLColorValue.AQUA;
            } else if (color == StyleColorNames.WHITE) {
                return VDXMLColorValue.WHITE;
            } else {
                return null;
            }
        } else if (color instanceof StyleColorPercentages) {
            StyleColorPercentages colorPercentages = (StyleColorPercentages) color;
            // Basics of this ripped off from EmulatorStyleColorRenderer
            // Might be worth factoring the percent to byte conversion out.
            int red = convertPercentageToByte(colorPercentages.getRed());
            int green = convertPercentageToByte(colorPercentages.getGreen());
            int blue = convertPercentageToByte(colorPercentages.getBlue());
            int percentRgb =
                    (red & 0x0000ff) << 16 |
                    (green & 0x0000ff) << 8 |
                    (blue & 0x0000ff);
            return getColorForRGB(percentRgb);
        } else if (color instanceof StyleColorRGB) {
            StyleColorRGB colorRGB = (StyleColorRGB) color;
            int rgb = colorRGB.getRGB();
            return getColorForRGB(rgb);
        }

        return null;
    }

    private VDXMLColorValue getColorForRGB(int rgb) {
        switch (rgb) {
            case 0x000000: 
                return VDXMLColorValue.BLACK;
            case 0xFF0000: 
                return VDXMLColorValue.RED;
            case 0x00FF00: 
                return VDXMLColorValue.LIME;
            case 0xFFFF00: 
                return VDXMLColorValue.YELLOW;
            case 0x0000FF: 
                return VDXMLColorValue.BLUE;
            case 0xFF00FF: 
                return VDXMLColorValue.FUSCHIA;
            case 0x00FFFF: 
                return VDXMLColorValue.AQUA;
            case 0xFFFFFF: 
                return VDXMLColorValue.WHITE;
            default: 
                return null;
        }
    }

    /**
     * Convert a (double) percentage value to an (integer) 8 bit value. 
     * 
     * @param percentage the precentage value (0.0 - 100.0) to convert.
     * @return the 8 bit value (0 - 255).
     */ 
    private int convertPercentageToByte(double percentage) {
        double bitsAsDouble = percentage * 2.55;
        int rgb = round (bitsAsDouble);
        return rgb;
    }

    /**
     * Round the double value to the "nearest neighbour" int.
     * 
     * @param dbl the double to round.
     * @return the int value
     */
    private int round (double dbl) {
      int i = (int)dbl;
      double d = dbl - i;
      if (d > 0.5) {
        i = i + 1;
      }
      return i;
    }

    private VDXMLBinaryValue getBinaryValue(
            StyleValue styleValue, StyleKeyword keyword) {

        VDXMLBinaryValue binary;
        if (styleValue == keyword) {
            binary = VDXMLBinaryValue.TRUE;
        } else {
            binary = VDXMLBinaryValue.FALSE;
        }

        return binary;
    }

    /**
     * A simple class to build VDXML font size value.
     * <p>
     * VDXML font size is a combination of the normal font size and font 
     * stretch values. 
     */ 
    private static class CharacterSizeBuilder {
        
        private StyleValue size;
        
        private StyleValue stretch;

        public void setSize(StyleValue size) {
            this.size = size;
        }

        public void setStretch(StyleValue stretch) {
            this.stretch = stretch;
        }

        public VDXMLFontSizeValue getCharacterSize() {
            
            if (size == null && stretch == null) {
                return null;
            }

            if (size == FontSizeKeywords.LARGE) {
                if (stretch == FontStretchKeywords.CONDENSED) {
                    return VDXMLFontSizeValue.DOUBLE_HEIGHT;
                } else if (stretch == FontStretchKeywords.EXPANDED) {
                    return VDXMLFontSizeValue.DOUBLE_HEIGHT_AND_WIDTH;
                } else {
                    return VDXMLFontSizeValue.DOUBLE_HEIGHT_AND_WIDTH;
                }
            } else {
                if (stretch == FontStretchKeywords.CONDENSED) {
                    return VDXMLFontSizeValue.NORMAL;
                } else if (stretch == FontStretchKeywords.EXPANDED) {
                    return VDXMLFontSizeValue.DOUBLE_WIDTH;
                } else {
                    return VDXMLFontSizeValue.NORMAL;
                }
            }
        }
    }

    /**
     * Visitor to add the font size value the character size builder.
     */ 
    private static class FontSizeCollector 
            extends StyleValueVisitorStub {
        
        public void visit(StyleKeyword value, Object object) {
            
            CharacterSizeBuilder tc = (CharacterSizeBuilder) object;
            if (value == FontSizeKeywords.MEDIUM ||
                    value == FontSizeKeywords.LARGE) {
                tc.setSize(value);
            }
        }
    }

    /**
     * Visitor to add the font stretch value the character size builder.
     */
    private static class FontStretchCollector 
            extends StyleValueVisitorStub {
        
        public void visit(StyleKeyword value, Object object) {
            
            CharacterSizeBuilder tc = (CharacterSizeBuilder) object;
            if (value == FontStretchKeywords.NORMAL ||
                    value == FontStretchKeywords.CONDENSED ||
                    value == FontStretchKeywords.EXPANDED) {
                tc.setStretch(value);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/2	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 06-May-05	8090/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 06-May-05	8048/1	emma	VBM:2005050411 Fixing broken css underline emulation for WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/13	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 02-Jun-04	4575/10	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 01-Jun-04	4495/1	claire	VBM:2004051807 Horizontal menus and line breaks

 28-May-04	4575/8	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4575/5	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
