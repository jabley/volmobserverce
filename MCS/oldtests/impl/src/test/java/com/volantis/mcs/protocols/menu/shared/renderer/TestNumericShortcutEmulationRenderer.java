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

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;

/**
 * A test implemententation of {@link NumericShortcutEmulationRenderer} which 
 * just provides some fixed text values for testing.
 */ 
public class TestNumericShortcutEmulationRenderer 
        implements NumericShortcutEmulationRenderer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // Javadoc inherited.
    public void start(DOMOutputBuffer buffer) {
        buffer.writeText("[emul-start]");
    }

    // Javadoc inherited.
    public void end(DOMOutputBuffer buffer) {
        buffer.writeText("[emul-end]");
    }

    // Javadoc inherited.
    public TextAssetReference getShortcut() {
        return new LiteralTextAssetReference("[emul-access-key]");
    }

    // Javadoc inherited.
    public void outputPrefix(DOMOutputBuffer buffer) {
        buffer.writeText("[emul-prefix]");
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/4	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 06-May-04	4153/2	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 ===========================================================================
*/
