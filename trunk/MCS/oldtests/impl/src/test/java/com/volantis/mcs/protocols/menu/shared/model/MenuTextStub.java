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

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuText;
import com.volantis.mcs.protocols.menu.shared.AbstractTestStub;

/**
 * Stub version of {@link MenuText} for testing. 
 */ 
public class MenuTextStub extends AbstractTestStub implements MenuText {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    public MenuTextStub() {
    }

    public MenuTextStub(String id) {
        super(id);
    }

    // Javadoc inherited.
    public OutputBuffer getText() {
        throw new UnsupportedOperationException();
    }

    // Javadoc inherited.
    public ElementDetails getElementDetails() {
        throw new UnsupportedOperationException();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9609/1	ibush	VBM:2005082215 Move on/off color values for menu items

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Apr-04	3920/2	geoff	VBM:2004041910 Enhance Menu Support: Renderers: HTML Menu Item Renderers

 ===========================================================================
*/
