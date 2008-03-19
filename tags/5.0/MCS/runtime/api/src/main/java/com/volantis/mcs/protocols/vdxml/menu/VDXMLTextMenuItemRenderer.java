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
package com.volantis.mcs.protocols.vdxml.menu;

import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.shared.renderer.AbstractStyledTextMenuItemRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedSpanOutput;

/**
 * Renders the text of a VDXML menu item.
 * <p>
 * This is styled as per usual, and then rendered with the shortcut name before
 * the menu text so that the user knows which shortcut to enter to trigger each
 * externally rendered menu item.
 *
 * @see VDXMLMenuItemRendererFactory
 */
public class VDXMLTextMenuItemRenderer
        extends AbstractStyledTextMenuItemRenderer {

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param spanOutput the outputter for span markup required when style is
     */
    public VDXMLTextMenuItemRenderer(DeprecatedSpanOutput spanOutput) {

        super(spanOutput);
    }

    // Javadoc inherited.
    protected void writeMenuTextToBuffer(OutputBuffer output, MenuItem item) {

        final DOMOutputBuffer domOutput = (DOMOutputBuffer) output;

        // Render the shortcut text
        TextAssetReference shortcut = item.getShortcut();
        final String shortcutText = (shortcut == null ? null :
                shortcut.getText(TextEncoding.PLAIN));
        domOutput.appendEncoded(shortcutText);
        // Then a space
        domOutput.appendEncoded(" ");
        // And finally the text of the menu itself.
        final OutputBuffer text = item.getLabel().getText().getText();
        domOutput.transferContentsFrom(text);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/3	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 ===========================================================================
*/
