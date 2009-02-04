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
import com.volantis.mcs.protocols.FragmentLinkRendererContext;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;

/**
 * {@link FragmentLinkRenderer} for rendering numeric shortcut style
 * fragment links on generic WML devices.
 * <p/>
 * Since generic WML phones don't support automagic accesskey generation ala
 * OpenWave phones, we emulate the Openwave behaviour for generic WML.
 * <p/>
 * NOTE: there is no rendering of stylistic emulation markup here. This is
 * because we only write 'a' tags which cannot contain the stylistic tags.
 *
 * @see OpenWaveNumericShortcutFragmentLinkRenderer
 */
public final class WMLNumericShortcutFragmentLinkRenderer
        extends FragmentLinkRenderer {

    /**
     * Flag that indicates whether elements that have values auto assigned for
     * accesskey attributes should have there content prefixed with the
     * accesskey value.
     */
    private final boolean prefixAccesskeyContent;

    /**
     * Construct an instance of this class.
     *
     * @param context
     * @param prefixAccesskeyContent
     */
    public WMLNumericShortcutFragmentLinkRenderer(
            FragmentLinkRendererContext context,
            boolean prefixAccesskeyContent) {
        super(context);
        this.prefixAccesskeyContent = prefixAccesskeyContent;
    }

    // Inherit Javadoc.
    public void doFragmentLink(
            OutputBuffer outputBuffer, FraglinkAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom = (DOMOutputBuffer) outputBuffer;
        WMLFragmentLinkRendererContext ctx =
                (WMLFragmentLinkRendererContext) context;

        // Open the annotation element.
        // @todo 2005060816 annotate child with style information if it's not inherited from the parent
        Element annotator = dom.openStyledElement(
                AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT, attributes);

        // Open the anchor element.
        Element anchor = dom.openElement("a");

        // Copy attributes into the anchor element.
        ctx.addTitleAttribute(anchor, attributes);
        anchor.setAttribute("href", attributes.getHref().getURL());
        // Add the dummy accesskey attribute as well.
        anchor.setAttribute("accesskey",
                            AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_STRING);

        // Add the dummy access key prefix to the text if necessary.
        if (prefixAccesskeyContent) {
            dom.appendEncoded(
                AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_STRING + " ");
        }

        // Write out the menu text as the content of the link.
        OutputBuffer text = attributes.getLinkText();
        dom.addOutputBuffer((DOMOutputBuffer) text);

        // Close the anchor element.
        dom.closeElement(anchor);
        
        // Close the annotation element.
        dom.closeElement(annotator);
        
        // Add BR to force hardcoded vertical alignment.
        // This is compatible with actual Openwave numeric shortcut rendering 
        // which is always vertical.
        // NOTE: This means that the mariner-fragment-list-orientation style
        // is ignored.
        dom.addStyledElement("br", attributes);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 06-Oct-03	1469/3	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 ===========================================================================
*/
