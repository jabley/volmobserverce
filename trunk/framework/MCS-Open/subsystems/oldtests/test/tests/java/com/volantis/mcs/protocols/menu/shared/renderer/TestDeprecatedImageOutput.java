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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.ImageAttributes;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.shared.throwable.ExtendedRuntimeException;

/**
 * A test implementation of {@link DeprecatedImageOutput} which writes out
 * the image attributes as simply as possible.
 * <p>
 * This also allows retrieval of the last rendered image text to allow 
 * ease of constructing expected results for test cases which are not testing
 * usage of this interface directly.
 */
public class TestDeprecatedImageOutput extends AbstractTestMarkupOutput 
        implements DeprecatedImageOutput {
        
    /**
     * Factory to use to create DOM objects.
     */
    protected DOMFactory domFactory = DOMFactory.getDefaultInstance();

    /**
     * The root element of the last content we created during rendering.
     */ 
    private Element lastElement;

    // Javadoc inherited.
    public void outputImage(DOMOutputBuffer dom,
            ImageAttributes attributes) {
        Element e = domFactory.createStyledElement(attributes.getStyles());
        e.setName("test-image");
        EventAttributes events = attributes.getEventAttributes(false);
        if (events != null) {
            for (int i=0; i < EventConstants.MAX_EVENTS; i++) {
                ScriptAssetReference event = events.getEvent(i);
                if (event != null) {
                    e.setAttribute("event-" + i, event.getScript());
                }
            }
        }
        if (attributes.getSrc() != null) {
            e.setAttribute("src", attributes.getSrc());
        }
        addCoreAttributes(attributes, e);
        dom.addElement(e);
        
        lastElement = e;
    }

    /**
     * Returns the text of the last rendered image.
     */ 
    public String getLastRenderedText() {
        try {
            return DOMUtilities.toString(lastElement);
        } catch (Exception ex) {
            throw new ExtendedRuntimeException(ex);
        }
    }
        
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/3	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/1	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4153/4	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 26-Apr-04	3920/4	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 15-Apr-04	3645/3	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 15-Apr-04	3645/1	geoff	VBM:2004032904 Enhance Menu Support: Open Wave Menu Renderer

 ===========================================================================
*/
