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
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLReverseVideoStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.properties.VDXMLUnderlineStyleProperty;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLBinaryValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLFontSizeValue;

/**
 * A collection of style properties for VDXML.
 * <p>
 * Currently this contains all the (useful) VDXML styles which can be 
 * expressed via the AZ and AC tags, apart from AZ MAsquage (hidden characters) 
 * which is not supported in MCS (because it's not part of CSS). 
 * <p>
 * NOTE: We could fairly easily extend the use of these style properties to be 
 * useful for all the VDXML tags rather than just AZ and AC since a lot of
 * the individual properties are applicable to things like TEXTXE, etc. This
 * would prevent us having to render things like background color twice in
 * the protocol, but there just isn't enough time to do this at the moment, 
 * especially given the lack of unit/integration tests.
 * <p>
 * NOTE: It would be nice to generalise this class to avoid all the cut and 
 * paste code below, but with only six properties and no hope of any more, it
 * is just not quite worth it at the moment.
 */ 
public class VDXMLStyleProperties {

    /**
     * The default values for style properties to be used when starting a
     * new display context.
     */ 
    public static final VDXMLStyleProperties DEFAULTS;
    static {
        DEFAULTS = new VDXMLStyleProperties();
        DEFAULTS.setFontColor(
                new VDXMLFontColorProperty(VDXMLColorValue.WHITE));
        DEFAULTS.setBackgroundColor(
                new VDXMLBackgroundColorProperty(VDXMLColorValue.BLACK));
        DEFAULTS.setBlink(
                new VDXMLBlinkStyleProperty(VDXMLBinaryValue.FALSE));
        DEFAULTS.setUnderline(
                new VDXMLUnderlineStyleProperty(VDXMLBinaryValue.FALSE));
        DEFAULTS.setFontSize(
                new VDXMLFontSizeProperty(VDXMLFontSizeValue.NORMAL));
        DEFAULTS.setReverseVideo(
                new VDXMLReverseVideoStyleProperty(VDXMLBinaryValue.FALSE));
    }

    /**
     * Default constructor.
     */ 
    public VDXMLStyleProperties() {
    }

    /**
     * Copy constructor.
     * 
     * @param copy the style properties to copy.
     */
    private VDXMLStyleProperties(VDXMLStyleProperties copy) {
        
        this.backgroundColor = copy.backgroundColor;
        this.underline = copy.underline;
        this.fontSize = copy.fontSize;
        this.fontColor = copy.fontColor;
        this.blink = copy.blink;
        this.reverseVideo = copy.reverseVideo;
    }
    
    // Area Display
    
    private VDXMLBackgroundColorProperty backgroundColor;
    
    private VDXMLUnderlineStyleProperty underline;
    
    // Character Display
    
    private VDXMLFontSizeProperty fontSize;
    
    private VDXMLFontColorProperty fontColor;

    private VDXMLBlinkStyleProperty blink;
    
    private VDXMLReverseVideoStyleProperty reverseVideo;
    
    // Area Display

    public VDXMLBackgroundColorProperty getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(VDXMLBackgroundColorProperty backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public VDXMLUnderlineStyleProperty getUnderline() {
        return underline;
    }

    public void setUnderline(VDXMLUnderlineStyleProperty underline) {
        this.underline = underline;
    }
    
    // Character Display

    public VDXMLFontSizeProperty getFontSize() {
        return fontSize;
    }

    public void setFontSize(VDXMLFontSizeProperty fontSize) {
        this.fontSize = fontSize;
    }

    public VDXMLFontColorProperty getFontColor() {
        return fontColor;
    }

    public void setFontColor(VDXMLFontColorProperty fontColor) {
        this.fontColor = fontColor;
    }

    public VDXMLBlinkStyleProperty getBlink() {
        return blink;
    }

    public void setBlink(VDXMLBlinkStyleProperty blink) {
        this.blink = blink;
    }

    public VDXMLReverseVideoStyleProperty getReverseVideo() {
        return reverseVideo;
    }

    public void setReverseVideo(VDXMLReverseVideoStyleProperty reverseVideo) {
        this.reverseVideo = reverseVideo;
    }

    /**
     * Creates and returns a new style properties which contains the property
     * values of this style properties, overriding any property values with 
     * the property values provided in the parameter.
     * 
     * @param properties the properties to merge with.
     * @return a new style properties containing the merged properties. 
     */ 
    public VDXMLStyleProperties merge(VDXMLStyleProperties properties) {
        VDXMLStyleProperties result = new VDXMLStyleProperties(this);
        if (properties.backgroundColor != null) {
            result.backgroundColor = properties.backgroundColor;
        }
        if (properties.underline != null) {
            result.underline = properties.underline;
        }
        if (properties.fontSize != null) {
            result.fontSize = properties.fontSize;
        }
        if (properties.fontColor != null) {
            result.fontColor = properties.fontColor;
        }
        if (properties.blink != null) {
            result.blink = properties.blink;
        }
        if (properties.reverseVideo != null) {
            result.reverseVideo = properties.reverseVideo;
        }
        return result;
    }

    /**
     * Creates and returns a new style properties which contains the property
     * values of this style properties, removing any property values which are 
     * present in this style property and the one provided, and are identical.
     * 
     * @param properties the properties to filter with.
     * @return a new style properties containing the filtered properties. 
     */ 
    public VDXMLStyleProperties removeDuplicate(
            VDXMLStyleProperties properties) {
        
        VDXMLStyleProperties result = new VDXMLStyleProperties(this);
        if (backgroundColor != null && properties.backgroundColor != null &&
                backgroundColor.getValue() ==
                    properties.backgroundColor.getValue()) {
            result.backgroundColor = null;
        }
        if (underline != null && properties.underline != null &&
                underline.getValue() == properties.underline.getValue()) {
            result.underline = null;
        }
        if (fontSize != null && properties.fontSize != null &&
                fontSize.getValue() == properties.fontSize.getValue()) {
            result.fontSize = null;
        }
        if (fontColor != null && properties.fontColor != null &&
                fontColor.getValue() == properties.fontColor.getValue()) {
            result.fontColor = null;
        }
        if (blink != null && properties.blink != null &&
                blink.getValue() == properties.blink.getValue()) {
            result.blink = null;
        }
        if (reverseVideo != null && properties.reverseVideo != null &&
                reverseVideo.getValue() ==
                properties.reverseVideo.getValue()) {
            result.reverseVideo = null;
        }
        return result;
    }
    
    /**
     * Creates and returns a new style properties which contains the property 
     * values of this style properties, removing any property values which are 
     * not present in the properties provided.
     * 
     * @param properties the properties to filter with.
     * @return a new style properties containing the filtered properties. 
     */ 
    public VDXMLStyleProperties removeMissing(
            VDXMLStyleProperties properties) {
        
        VDXMLStyleProperties result = new VDXMLStyleProperties(this);
        if (properties.backgroundColor == null) {
            result.backgroundColor = null;
        }
        if (properties.underline == null) {
            result.underline = null;
        }
        if (properties.fontSize == null) {
            result.fontSize = null;
        }
        if (properties.fontColor == null) {
            result.fontColor = null;
        }
        if (properties.blink == null) {
            result.blink = null;
        }
        if (properties.reverseVideo == null) {
            result.reverseVideo = null;
        }
        return result;
    }
    
    /**
     * Creates and returns a new style properties which contains just the 
     * boolean property values of this style properties.
     * 
     * @return a new style properties containing boolean properties. 
     */ 
    public VDXMLStyleProperties booleanOnly() {
        
        VDXMLStyleProperties result = new VDXMLStyleProperties();
        result.underline = underline;
        result.blink = blink;
        result.reverseVideo = reverseVideo;
        return result;
    }
    
    /**
     * Creates and returns a new style properties which contains just the 
     * non-boolean property values of this style properties.
     * 
     * @return a new style properties containing non-boolean properties. 
     */ 
    public VDXMLStyleProperties normalOnly() {
        
        VDXMLStyleProperties result = new VDXMLStyleProperties();
        result.backgroundColor = backgroundColor;
        result.fontSize = fontSize;
        result.fontColor = fontColor;
        return result;
    }

    /**
     * Returns true if the style properties provided has the font color and 
     * background color properties inverted compared to this style properties.
     * <p>
     * When this is true, it should be possible to use the synthetic reverse 
     * video property to represent this case more succinctly than an explicit
     * foreground and backgound color change. 
     * 
     * @param style
     * @return true if font and backgound colors are inverted.
     */ 
    public boolean isReverseVideo(VDXMLStyleProperties style) {
        
        return (fontColor != null && backgroundColor != null &&
                style.fontColor != null && style.backgroundColor != null && 
                fontColor.getValue() == style.backgroundColor.getValue() &&
                backgroundColor.getValue() == style.fontColor.getValue());        
    }
    
    /**
     * Returns true if this style properties has none of it's properties set.
     */ 
    public boolean isEmpty() {
        
        return (backgroundColor == null &&
                underline == null &&
                fontSize == null &&
                fontColor == null &&
                blink == null &&
                reverseVideo == null);
    }

    // Javadoc inherited.
    public String toString() {
        
        String value = "";
        if (backgroundColor != null) value += backgroundColor.toString();
        if (underline != null) value += underline.toString();
        if (fontSize != null) value += fontSize.toString();
        if (fontColor != null) value += fontColor.toString();
        if (blink != null) value += blink.toString();
        if (reverseVideo != null) value += reverseVideo.toString();
        return value;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 08-Jun-04	4575/14	geoff	VBM:2004051807 Minitel VDXML protocol support (javadoc and make rendering clearer)

 04-Jun-04	4575/12	geoff	VBM:2004051807 Minitel VDXML protocol support

 03-Jun-04	4575/10	geoff	VBM:2004051807 Minitel VDXML protocol support (checkin before fragmentation)

 02-Jun-04	4575/7	geoff	VBM:2004051807 Minitel VDXML protocol support (reverse video, tests and some cleanup)

 28-May-04	4575/5	geoff	VBM:2004051807 Minitel VDXML protocol support (working inline)

 28-May-04	4575/3	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 27-May-04	4575/1	geoff	VBM:2004051807 Minitel VDXML protocol support

 ===========================================================================
*/
