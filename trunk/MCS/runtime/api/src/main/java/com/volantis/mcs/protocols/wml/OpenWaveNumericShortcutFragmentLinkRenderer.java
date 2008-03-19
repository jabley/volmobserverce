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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.WhiteSpaceKeywords;
import com.volantis.styling.Styles;

/**
 * An OpenWave specific fragment link renderer which renders "numeric
 * shortcut" style fragment links.
 * <p/>
 * NOTE: given that these fragment links are usually part of a list, it would
 * be nicer (and more space efficient) to render them as a multiple select
 * list rather than individual single select lists.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because select/option can only contain PCDATA, so even if we rendered it
 * it would be invalid and (almost always) removed by the transformer. The
 * only time it would not be removed would be if it was intercepted by the
 * "emulate emphasis" processing in the transformer and translated into just
 * text before and after (eg "[" & "]") - and it's not worth implementing just
 * for this edge condition.
 */
class OpenWaveNumericShortcutFragmentLinkRenderer
        extends FragmentLinkRenderer {

    public OpenWaveNumericShortcutFragmentLinkRenderer(
            WMLFragmentLinkRendererContext context) {

        super(context);

    }

    // Javadoc inherited.
    public void doFragmentLink(
            OutputBuffer outputBuffer,
            FraglinkAttributes attributes) throws ProtocolException {

        DOMOutputBuffer dom = (DOMOutputBuffer) outputBuffer;
        WMLFragmentLinkRendererContext ctx =
                (WMLFragmentLinkRendererContext) context;
            
        // Add a p with nowrap.
        // This is recommended by openwave style guidelines for "menus".
        // NOTE: this code is mostly duplicated by other openwave renderers
        // that need to generate openwave style "menus" for other purposes.

        // todo: only set whitespace if it was not specified by user
        // currently we cannot tell, fix when VBM:2005092701 is fixed
        Styles styles = attributes.getStyles();
        styles.getPropertyValues().setComputedValue(
                StylePropertyDetails.WHITE_SPACE,
                WhiteSpaceKeywords.NOWRAP);
        Element p = dom.openStyledElement(WMLConstants.BLOCH_ELEMENT, styles);

        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element select = dom.openElement("select");
        ctx.addTitleAttribute(select, attributes);

        Element option = dom.openElement("option");
        ctx.addTitleAttribute(option, attributes);

        LinkAssetReference reference = attributes.getHref();
        if (reference != null) {
            option.setAttribute("onpick", reference.getURL());
        }
        dom.addOutputBuffer((DOMOutputBuffer) attributes.getLinkText());

        dom.closeElement(option);

        dom.closeElement(select);

        dom.closeElement(p);

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Sep-05	9600/2	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 25-Sep-03	1412/6	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 17-Sep-03	1412/2	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
