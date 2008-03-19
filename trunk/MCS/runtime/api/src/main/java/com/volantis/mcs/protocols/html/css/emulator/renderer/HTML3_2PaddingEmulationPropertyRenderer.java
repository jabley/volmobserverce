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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renders the 'padding' style property as stylistic markup for HTML 3.2.
 * <p>
 * This will try and render a callpadding attribute containing a pixel length 
 * onto the containing table of a td. Any other elements will be ignored.
 */ 
public class HTML3_2PaddingEmulationPropertyRenderer 
        implements StyleEmulationPropertyRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(HTML3_2PaddingEmulationPropertyRenderer.class);
    
    /**
     * Renders pixel values for us.
     */ 
    private static final StyleEmulationAttributeValueRenderer PIXELS_RENDERER = 
            new HTML3_2PixelsEmulationAttributeValueRenderer();

    // Javadoc inherited.
    public void apply(Element element, StyleValue value) {
        // If we are attempting to render the style onto a TD ...
        if ("td".equals(element.getName())) {
            // .. then find it's containing TABLE.
            while ((element = element.getParent()) != null) {
                if ("table".equals(element.getName())) {
                    // We have found the containing table.

                    // If we haven't rendered a cellpadding value before ...
                    if (element.getAttributeValue("cellpadding") == null) {
                        // ... then the render the value.
                        String renderedPadding = PIXELS_RENDERER.render(value);

                        if (renderedPadding != null) {
                            element.setAttribute("cellpadding", renderedPadding);
                        }
                    } else {
                        // ... else we ignore the value.
                        // We ignore any TD's which try and set a value after
                        // the first TD does so.
                        if (logger.isDebugEnabled()) {
                            logger.debug("Ignoring css padding '" + value +
                                         "' " +
                                         "on td - table already has cellpadding " +
                                         element.getAttributeValue("cellpadding"));
                        }
                    }
                    // we're done, so may as well finish the loop now.
                    return;
                }
            }
            // If we get here we have not found a containing table.
            // This should probably never happen.
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring css padding '" + value + "' " +
                             "on td - no containing table found");
            }
        } else {
            // then we ignore it.
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring css padding '" + value + "' " +
                             "on " + element.getName() + " - padding only rendered on " +
                             "td's");
            }
        }

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-05	9825/1	pduffin	VBM:2005091502 Corrected device name and made use of new stylistic property.

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Aug-05	9184/3	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 15-Jun-05	8788/1	rgreenall	VBM:2005050501 Merge from 331

 15-Jun-05	8792/1	rgreenall	VBM:2005050501 Style emulation support for <td> element.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 09-Aug-04	5141/1	pcameron	VBM:2004080502 Fixed NullPointerExceptions in renderers and added unit tests

 09-Aug-04	5136/1	pcameron	VBM:2004080502 Fixed NullPointerExceptions in renderers and added unit tests

 20-Jul-04	4897/2	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 ===========================================================================
*/
