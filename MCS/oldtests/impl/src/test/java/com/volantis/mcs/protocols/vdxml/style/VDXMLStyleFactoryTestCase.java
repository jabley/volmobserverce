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

import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLFontSizeValue;
import com.volantis.mcs.themes.StyleColor;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class VDXMLStyleFactoryTestCase extends TestCaseAbstract {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    VDXMLStyleFactory factory = new VDXMLStyleFactory();

    public void testColorRGB() {
        
        assertEquals("", VDXMLColorValue.WHITE, 
            factory.getColor(STYLE_VALUE_FACTORY.getColorByRGB(null, 0xFFFFFF)));
        assertEquals("", VDXMLColorValue.RED, 
            factory.getColor(STYLE_VALUE_FACTORY.getColorByRGB(null, 0xFF0000)));
        assertEquals("", VDXMLColorValue.BLACK, 
            factory.getColor(STYLE_VALUE_FACTORY.getColorByRGB(null, 0x000000)));
    }

    public void testColorPercentage() {
        
        assertEquals("", VDXMLColorValue.WHITE, factory.getColor(
            STYLE_VALUE_FACTORY.getColorByPercentages(null, 100, 100, 100)));
        assertEquals("", VDXMLColorValue.LIME, factory.getColor(
            STYLE_VALUE_FACTORY.getColorByPercentages(null, 0, 100, 0)));
        assertEquals("", VDXMLColorValue.BLACK, factory.getColor(
            STYLE_VALUE_FACTORY.getColorByPercentages(null, 0, 0, 0)));

    }
    
    public void testColorKeyword() {
        
        StyleColor color;
        color = StyleColorNames.WHITE;
        assertEquals("", VDXMLColorValue.WHITE, factory.getColor(color));
        color = StyleColorNames.BLUE;
        assertEquals("", VDXMLColorValue.BLUE, factory.getColor(color));
        color = StyleColorNames.BLACK;
        assertEquals("", VDXMLColorValue.BLACK, factory.getColor(color));
    }

    /**
     * This does not work properly if the properties are fully populated.
     *
     * todo fix this up. 
     */
    public void notestCharacterSizeNone() {

        checkCharacterSize(null, StylesBuilder.getInitialValueStyles());
    }
    
    public void testCharacterSizeNormal() {
        
        checkCharacterSize(VDXMLFontSizeValue.NORMAL,
                StylesBuilder.getCompleteStyles(
                        "font-size: medium; font-stretch: normal"));

        checkCharacterSize(VDXMLFontSizeValue.NORMAL, 
                StylesBuilder.getCompleteStyles(
                        "font-size: medium; font-stretch: condensed"));
    }
    
    public void testCharacterSizeDoubleWidth() {

        checkCharacterSize(VDXMLFontSizeValue.DOUBLE_WIDTH, 
                StylesBuilder.getCompleteStyles(
                        "font-stretch: expanded"));

        checkCharacterSize(VDXMLFontSizeValue.DOUBLE_WIDTH, 
                StylesBuilder.getCompleteStyles(
                        "font-size: medium; font-stretch: expanded"));
    }
    
    public void testCharacterSizeDoubleHeight() {
        
        checkCharacterSize(VDXMLFontSizeValue.DOUBLE_HEIGHT, 
                StylesBuilder.getCompleteStyles(
                        "font-size: large; font-stretch: condensed"));

    } 
    
    public void testCharacterSizeDoubleWidthAndHeight() {
        
        checkCharacterSize(VDXMLFontSizeValue.DOUBLE_HEIGHT_AND_WIDTH, 
                StylesBuilder.getCompleteStyles(
                        "font-size: large"));

        checkCharacterSize(VDXMLFontSizeValue.DOUBLE_HEIGHT_AND_WIDTH, 
                StylesBuilder.getCompleteStyles(
                        "font-size: large; font-stretch: normal"));

        checkCharacterSize(VDXMLFontSizeValue.DOUBLE_HEIGHT_AND_WIDTH, 
                StylesBuilder.getCompleteStyles(
                        "font-size: large; font-stretch: expanded"));
    }

    private void checkCharacterSize(VDXMLFontSizeValue expectedCharSize,
                                    Styles styles) {

        VDXMLFontSizeValue actualCharSize = factory.getCharacterSize(
                styles.getPropertyValues());
        assertEquals("", expectedCharSize, actualCharSize);
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

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 15-Sep-05	9512/1	pduffin	VBM:2005091408 Committing changes to JIBX to use new schema

 22-Jul-05	8859/1	emma	VBM:2005062006 Modify transformers to take account of Styles when flattening/optimizing tables

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/1	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 02-Jun-04	4575/8	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 28-May-04	4575/5	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
