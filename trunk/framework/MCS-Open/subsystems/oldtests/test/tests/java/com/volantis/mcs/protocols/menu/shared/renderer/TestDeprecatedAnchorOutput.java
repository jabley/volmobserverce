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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.AnchorAttributes;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.policies.variants.text.TextEncoding;

/**
 * A test implementation of {@link DeprecatedAnchorOutput} which writes out
 * the anchor attributes as simply as possible.
 * <p>
 * This also allows retrieval of the last rendered anchor text to allow
 * ease of constructing expected results for test cases which are not testing
 * usage of this interface directly.
 */
public class TestDeprecatedAnchorOutput extends AbstractTestMarkupOutput
        implements DeprecatedAnchorOutput {

    /**
     * The last element created during rendering.
     */
    private Element lastElement;

    // JavaDoc inherited
    public void openAnchor(DOMOutputBuffer dom, AnchorAttributes attributes) {
        Element element = dom.openStyledElement("test-anchor", attributes);

        LinkAssetReference reference = attributes.getHref();
        String value;
        if (reference != null && (value = reference.getURL()) != null) {
            element.setAttribute("href", value);
        }
        TextAssetReference text = attributes.getShortcut();
        if (text != null && (value = text.getText(TextEncoding.PLAIN)) != null) {
            element.setAttribute("shortcut", value);
        }
        if (attributes.getTitle() != null) {
            element.setAttribute("title", attributes.getTitle());
        }
        if (attributes.getSegment() != null) {
            element.setAttribute("target", attributes.getSegment());
        }
        addCoreAttributes(attributes, element);

        lastElement = element;
    }

    // JavaDoc inherited
    public void closeAnchor(DOMOutputBuffer dom, AnchorAttributes attributes) {
        dom.closeElement("test-anchor");
    }

    /**
     * Provides a way to access the last rendered element in string form.
     *
     * @return A string representation of the last anchor to be rendered
     */
    public String getLastRenderedText() {
        return DOMUtilities.toString(lastElement);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Oct-04	5980/1	claire	VBM:2004100104 mergevbm: Segment attribute rendered on menu items

 26-Oct-04	5966/1	claire	VBM:2004100104 Segment attribute rendered on menu items

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 06-May-04	4153/3	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 29-Apr-04	4013/1	pduffin	VBM:2004042210 Restructure menu item renderers

 16-Apr-04	3715/1	claire	VBM:2004040201 Enhanced Menu: WML Menu Item Renderers

 ===========================================================================
*/
