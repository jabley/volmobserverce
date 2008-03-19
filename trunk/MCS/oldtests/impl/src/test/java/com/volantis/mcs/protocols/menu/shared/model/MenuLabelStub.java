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
package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.FormatReference;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.MenuIcon;
import com.volantis.mcs.protocols.menu.model.MenuLabel;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.shared.AbstractTestStub;

/**
 * Stub version of {@link MenuLabel} for testing. 
 */ 
public class MenuLabelStub extends AbstractTestStub implements MenuLabel {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    public MenuLabelStub() {
    }

    public MenuLabelStub(String id) {
        super(id);
    }

    // Javadoc inherited.
    public MenuIcon getIcon() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public MenuText getText() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public ElementDetails getElementDetails() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public ScriptAssetReference getEventHandler(EventType type) {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public FormatReference getPane() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public String getTitle() {
        throw new UnsupportedOperationException();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
