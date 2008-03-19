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
package com.volantis.mcs.protocols;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.MCSFragmentListOrientationKeywords;
import com.volantis.styling.Styles;
import com.volantis.styling.values.PropertyValues;

/**
 * The default implementation for fragment link rendering.
 * <p>
 * This renders the fragment link as an "anchor". If the fragment link is
 * part of a list, we will add a separator afterwards, which will be either a 
 * "line break" or a space, depending on the stylistic information provided 
 * for the list.
 * <p>
 * Currently we rely on the Protocol to provide the meaning of "anchor" and 
 * "line break" via the fragment link renderer context proxy. This is known
 * to be less than optimal, but we are hoping that we will be able to clarify 
 * this later, as this is really an experiment in how to start dividing the 
 * protocol behaviour into smaller chunks via delegation.
 */ 
public class DefaultFragmentLinkRenderer extends FragmentLinkRenderer {

    /**
     * Constant default line break attributes (since they never change).
     */
    private static final LineBreakAttributes LINE_BREAK_ATTRIBUTES =
            new LineBreakAttributes();

    public DefaultFragmentLinkRenderer(
            FragmentLinkRendererContext context) {
        super(context);
    }

    // Javadoc inherited.
    public void doFragmentLink(OutputBuffer outputBuffer,
            FraglinkAttributes attributes) throws ProtocolException {

        // Write out an anchor with the fragment link href and content.
        AnchorAttributes a = new AnchorAttributes();
        a.copy(attributes);
        a.setHref(attributes.getHref());
        a.setContent(attributes.getLinkText());
        context.doAnchor(outputBuffer, a);

        // If this fragment link is participating in a fragment list...
        if (attributes.isInList()) {
            // Then we should add a list separator.
            // Note that we are not an offical menu so we can't use the 
            // existing horizontal menu separator style.
            // Yes this is not pretty. See the class comment.

            // List separators must respect the mariner-list-* styles, so
            // before we can add it, we must decide which style they wanted.
            // The default style is vertical (ala real menus).
            boolean horizontal = false;
            Styles styles = attributes.getStyles();
            PropertyValues propertyValues = styles.getPropertyValues();
            StyleValue value = propertyValues.getComputedValue(
                    StylePropertyDetails.MCS_FRAGMENT_LIST_ORIENTATION);
            if (value == MCSFragmentListOrientationKeywords.HORIZONTAL) {
                horizontal = true;
            }

            // So, if they explicitly asked for horizontal alignment
            if (horizontal) {
                // Use a space
                outputBuffer.setElementIsPreFormatted(true);
                outputBuffer.writeText(" ");
            } else {
                // Else, use line break for default/vertical alignment.
                context.doLineBreak(outputBuffer, LINE_BREAK_ATTRIBUTES);
            }
        }
        
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Apr-04	3920/1	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers (review comments)

 17-Sep-03	1412/4	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
