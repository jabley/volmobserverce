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

import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.wml.AccesskeyConstants;

/**
 * This provides a default implementation of numeric shortcut emulation.  It
 * can be used in any situation where numeric shortcuts are required but that
 * inbuilt protocol support is not required.
 */
public class DefaultNumericShortcutEmulationRenderer
        implements NumericShortcutEmulationRenderer {

    /**
     * A reference to the customisation that this emulation renderer is
     * operating against.
     */
    private final MenuModuleCustomisation customisation;
    private static final LiteralTextAssetReference SHORTCUT = new LiteralTextAssetReference(AccesskeyConstants.DUMMY_ACCESSKEY_VALUE_STRING);

    /**
     * Initialise a new instance of this class using the given parameter.
     *
     * @param customisation The device for this emulation.  May not be null.
     */
    public DefaultNumericShortcutEmulationRenderer(
            MenuModuleCustomisation customisation) {
        
        if (customisation == null) {
            throw new IllegalArgumentException("customisation cannot be null");
        }
        this.customisation = customisation;
    }

    // JavaDoc inherited
    public void start(DOMOutputBuffer buffer) {
        buffer.openElement(AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT);
    }

    // JavaDoc inherited
    public void end(DOMOutputBuffer buffer) {
        buffer.closeElement(AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT);
    }

    // JavaDoc inherited
    public TextAssetReference getShortcut() {
        return SHORTCUT;
    }

    // JavaDoc inherited
    public void outputPrefix(DOMOutputBuffer buffer) {
        boolean isAccesskeyPrefixKeyNeeded
                = !customisation.automaticallyDisplaysAccessKey();

        // Add the access key prefix to the text if necessary.
        if (isAccesskeyPrefixKeyNeeded) {
            buffer.appendEncoded(getShortcut().getText(TextEncoding.PLAIN) +
                    " ");
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7243/5	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/3	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/4	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 05-May-04	4124/3	claire	VBM:2004042805 Refining menu renderer selectors

 30-Apr-04	4124/1	claire	VBM:2004042805 Openwave and WML menu renderer selectors

 27-Apr-04	4025/5	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/3	claire	VBM:2004042302 Refining numeric emulation interface

 27-Apr-04	4025/1	claire	VBM:2004042302 Enhance Menu Support: Numeric shortcut rendering and and emulation

 ===========================================================================
*/
