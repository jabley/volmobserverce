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

package com.volantis.mcs.protocols.menu;

/**
 * Test implementation of the customisation.
 */
public class TestMenuModuleCustomisation
        implements MenuModuleCustomisation {

    /**
     * See {@link #supportsAccessKeyAttribute} for a description of this
     * property.
     */
    private boolean supportsAccessKeyAttribute = false;
    
    /**
     * See {@link #automaticallyDisplaysAccessKey} for a description of this
     * property.
     */
    private boolean automaticallyDisplaysAccessKey = false;

    /**
     * See {@link #supportsStyleSheets} for a description of this property.
     */
    private boolean supportsStyleSheets = false;

    /**
     * Set a customisation property.
     *
     * <p>See {@link #supportsAccessKeyAttribute} for a description of the
     * property.</p>
     *
     * @param value The new value of the property.
     */
    public void setSupportsAccessKeyAttribute(boolean value) {
        this.supportsAccessKeyAttribute = value;
    }

    // Javadoc inherited.
    public boolean supportsAccessKeyAttribute() {
        return supportsAccessKeyAttribute;
    }

    /**
     * Set a customisation property.
     *
     * <p>See {@link #automaticallyDisplaysAccessKey} for a description of the
     * property.</p>
     *
     * @param value The new value of the property.
     */
    public void setAutomaticallyDisplaysAccessKey(boolean value) {
        this.automaticallyDisplaysAccessKey = value;
    }

    // Javadoc inherited.
    public boolean automaticallyDisplaysAccessKey() {
        return automaticallyDisplaysAccessKey;
    }

    /**
     * Used to indicate that this MenuModule supports Style Sheets.
     * @param supportsStyleSheets rue if this MenuModuleCustomisation supports
     * style sheets, false otherwise.
     */
    public void setSupportsStyleSheets(boolean supportsStyleSheets) {
        this.supportsStyleSheets = supportsStyleSheets;
    }

    /**
     * @return true if this MenuModuleCustomisation supports style sheets, false
     * otherwise.
     */
    public boolean supportsStyleSheets() {
        return supportsStyleSheets;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
