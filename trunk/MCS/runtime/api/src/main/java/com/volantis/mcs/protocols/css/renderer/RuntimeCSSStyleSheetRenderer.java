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
 * $Header: /src/voyager/com/volantis/mcs/protocols/css/renderer/css2/RuntimeCSSStyleSheetRenderer.java,v 1.5 2003/03/13 12:27:00 payal Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 01-May-02    Allan           VBM:2002040804 - Created. A runtime version of
 *                              CSSStyleSheetRenderer.
 * 25-Jun-02    Steve           VBM:2002040807 - Now derives from
 *                              AbstractRuntimeCSS2StyleSheetRenderer to generate
 *                              transformed stylesheets through the 
 *                              renderDeviceTheme() method.
 * 10-Jul-02    Steve           VBM:2002040807 - Reverted to CSSStyleSheetRenderer
 *                              as a base class as stylesheets are now transformed 
 *                              in the style sheet generator and not the renderer.
 * 13-Mar-03    Payal           VBM:2003030710 - Added getKeywordMapperFactory()
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.css.renderer;

import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.CounterIncrementRenderer;
import com.volantis.mcs.css.renderer.CounterResetRenderer;
import com.volantis.mcs.css.renderer.PropertyRenderer;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.themes.mappers.KeywordMapperFactory;

/**
 * The runtime version of CSSStyleSheetRenderer. This version uses runtime
 * versions of the property renderers where these are available.
 */
public final class RuntimeCSSStyleSheetRenderer
       extends CSSStyleSheetRenderer {

    /**
     * The singleton instance of this StyleSheetRenderer.
     */
    private static final StyleSheetRenderer singleton =
        new RuntimeCSSStyleSheetRenderer();

    // javadoc inherited
    protected CounterIncrementRenderer getCounterIncrementRenderer() {
        // No point rendering this at runtime since we don't have a content
        // property yet.
        return null;
    }

    // javadoc inherited
    protected CounterResetRenderer getCounterResetRenderer() {
        // No point rendering this at runtime since we don't have a content
        // property yet.
        return null;
    }

    // javadoc inherited
    protected PropertyRenderer getWapInputFormatRenderer() {
        return new RuntimeWapInputFormatRenderer();

    }
    /**
     * Get the singleton instance of this StyleSheetRenderer.
     * @return the singleton RuntimeCSSStyleSheetRenderer
     */
    public static StyleSheetRenderer getSingleton() {
        return singleton;
    }
    
    // javadoc inherited
    public KeywordMapperFactory getKeywordMapperFactory() {
        return RuntimeKeywordMapperFactory.getSingleton();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9324/1	ianw	VBM:2005080202 Move validation for WapCSS into styling

 29-Jul-05	9114/1	geoff	VBM:2005072120 XDIMECP: Implement CSS Counters

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/4	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
