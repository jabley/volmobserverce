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
package com.volantis.mcs.protocols.wml.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSImageSavingKeywords;

/**
 * Renderer that will write a special value NO_SAVE into the alt attribute of
 * an img element if the mcs-image-saving property is set to disallow it.
 */
public class WMLImageAltEmulationPropertyRenderer
        implements StyleEmulationPropertyRenderer {
    
    /**
     * If the element is an image then the current alt text should be set to
     * "no_save". This is to correct a problem with WML highlighting images
     * when the no_save is not specified
     *
     * @param element
     * @param value
     */
    public void apply(Element element, StyleValue value) {
        if ("img".equals(element.getName()) &&
                value == MCSImageSavingKeywords.DISALLOW) {                       
            element.setAttribute("alt", "NO_SAVE");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10039/4	ibush	VBM:2005103106 Remove title attribute from image renderer

 31-Oct-05	10039/1	ibush	VBM:2005103106 Remove title attribute from image renderer

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 04-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 ===========================================================================
*/
