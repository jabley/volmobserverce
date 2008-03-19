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
package com.volantis.mcs.protocols.vdxml;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.FraglinkAttributes;
import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.protocols.FragmentLinkRendererContext;
import com.volantis.mcs.protocols.LineBreakAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSFragmentListOrientationKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * An implementation of fragment link rendering for Minitel/VDXML.
 * <p>
 * Fragment links in VDXML are basically as standard as can be apart from the
 * nastiness forced by the fact that VDXML links are implemented so unusually,
 * whereby the text of the link is rendered in once place and the URL is
 * rendered in another.
 * <p>
 * See the architecture paper for a description of how fragmentation is handled
 * for Minitel/VDXML.
 *
 * @todo update VDXML styling to use the style container rather than StyleProperties
 */
public class VDXMLFragmentLinkRenderer extends FragmentLinkRenderer {

    /**
     * Construct an instance of this class, with the context provided.
     *
     * @param context
     */
    public VDXMLFragmentLinkRenderer(FragmentLinkRendererContext context) {
        super(context);
    }

    // Inherit Javadoc.
    public void doFragmentLink(OutputBuffer outputBuffer,
            FraglinkAttributes attributes) throws ProtocolException {

        final VDXMLFragmentLinkRendererContext ctx =
                (VDXMLFragmentLinkRendererContext) context;
        final Styles styles = attributes.getStyles();
        final String fragmentName = attributes.getName();

        // Create the RACCOURCI that this fragment link uses to "link" out
        // of this page to this fragment.
        ctx.outputExternalLink(fragmentName, attributes.getHref().getURL());

        // If this fragment link is part of a fragment peer/parent link list,
        if (attributes.isInList()) {
            // Then render the fragment link as an entry in the link list.
            // This means rendering the text of the link into the special
            // fragment links pane.

            // First render the text of the link into the special links pane.
            // Get the special buffer to put the fragment link text into.
            DOMOutputBuffer linksDom = ctx.getFragmentLinksBuffer(fragmentName);
            // Dump in the link text. This tells the user which RACCOURCI
            // shortcut to enter.
            linksDom.addOutputBuffer((DOMOutputBuffer) attributes.getLinkText());

            // Then add in the list separator.
            // Note that this is a bit daft and adds a separator after the
            // last entry. Fixing this would take a lot of refactoring...
            // The default style is vertical (ala menus).
            boolean horizontal = false;
            if (styles != null && styles.getPropertyValues() != null) {
                StyleValue value = styles.getPropertyValues().
                        getComputedValue(StylePropertyDetails.
                        MCS_FRAGMENT_LIST_ORIENTATION);
                if (value == MCSFragmentListOrientationKeywords.HORIZONTAL) {
                    horizontal = true;
                }
            }
            // So, if they explicitly asked for horizontal alignment
            if (horizontal) {
                // Use a space
                linksDom.setElementIsPreFormatted(true);
                linksDom.writeText(" ");
            } else {
                // Else, use line break for default/vertical alignment.
                ctx.doLineBreak(linksDom, new LineBreakAttributes());
            }
        } else {
            // Then render the fragment link as a child link.
            // This means rendering the text of the link inside a "fake" pane
            // element, which is required to provide geometry information.

            // Open our fake pane.
            final DOMOutputBuffer dom = (DOMOutputBuffer) outputBuffer;
            Element element = dom.openElement(
                    VDXMLConstants.PSEUDO_PANE_ELEMENT);
            MutablePropertyValues propertyValues = null;
            if (styles != null) {
                propertyValues = styles.getPropertyValues();
            }
            ctx.addFakePaneAttributes(element, "synthetic fragment pane",
                    propertyValues);
            // Dump in the link text. This tells the user which RACCOURCI
            // shortcut to enter.
            dom.addOutputBuffer((DOMOutputBuffer) attributes.getLinkText());
            // Close our fake pane.
            dom.closeElement(element);
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

 27-Sep-05	9487/2	pduffin	VBM:2005091203 Committing new CSS Parser

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 30-Jun-05	8893/2	emma	VBM:2005062406 Annotate DOM elements generated from VDXML with styles

 09-Jun-05	8665/4	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 24-Sep-04	5613/2	geoff	VBM:2004092215 Port VDXML to MCS: update fragment link support

 ===========================================================================
*/
