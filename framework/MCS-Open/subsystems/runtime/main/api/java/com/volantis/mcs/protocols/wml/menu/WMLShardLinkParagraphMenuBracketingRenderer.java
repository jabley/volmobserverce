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
package com.volantis.mcs.protocols.wml.menu;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.Menu;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuBracketingRenderer;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.wml.WMLConstants;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;

/**
 * A rather bogus menu bracketing renderer that renders a &lt;p mode=wrap&gt;,
 * specifically for WML shard link menus.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because there is no style associated with the shard links as a whole.
 * Also implementing it would be a pain until we use a protocol callback
 * such as DeprecatedDivOutput to render the p.
 *
 * @todo use DeprecatedDivOutput to render the block rather than hardcoding?
 */
public class WMLShardLinkParagraphMenuBracketingRenderer
        implements MenuBracketingRenderer {

    /**
     * Start a p mode=nowap element.
     */
    // Other javadoc inherited.
    public void open(OutputBuffer buffer, Menu menu)
            throws RendererException {

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        // todo: only set whitespace if it was not specified by user
        // currently we cannot tell, fix when VBM:2005092701 is fixed
        Styles styles = menu.getElementDetails().getStyles();
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.WHITE_SPACE,
                WhiteSpaceKeywords.NOWRAP);
        dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, styles);
    }

    /**
     * End the p element.
     */
    // Other javadoc inherited.
    public void close(OutputBuffer buffer, Menu menu)
            throws RendererException {

        DOMOutputBuffer dom = (DOMOutputBuffer) buffer;

        dom.closeElement(WMLConstants.BLOCH_ELEMENT);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9600/3	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
