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

package com.volantis.mcs.protocols.menu.shared;

import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;

/**
 * Implementation.
 *
 * <p>Boolean values are represented as a Boolean object rather than a
 * primitive boolean type as they can be in three states not two. The three
 * states are unitialised represented as null, true represented as Boolean.TRUE
 * and false represented as Boolean.FALSE.</p>
 *
 */
public final class MenuModuleCustomisationImpl
        implements MenuModuleCustomisation {

    /**
     * Specifies whether the protocol supports accesskey attributes.
     */
    private final boolean supportsAccessKeyAttribute;

    /**
     * The cached value of the device policy:
     * DevicePolicyConstants.SUPPORTS_(X)_ACCESSKEY_AUTOMAGIC_NUMBER_DISPLAY
     */
    private final boolean automaticallyDisplaysAccessKey;

    /**
     * Specifies whether the protocol supports style sheets.
     */
    private final boolean supportsStyleSheets;

    /**
     * Initialise.
     *
     * @param supportsAccessKeyAttribute Specifies whether the protocol
     *      supports accesskey attributes.
     * @param automaticallyDisplaysAccessKey true if the device supports the
     * automatic display of short cut access keys.
     */
    public MenuModuleCustomisationImpl(boolean supportsAccessKeyAttribute,
            boolean automaticallyDisplaysAccessKey,
            boolean supportsStyleSheets) {
        this.supportsAccessKeyAttribute = supportsAccessKeyAttribute;
        this.automaticallyDisplaysAccessKey = automaticallyDisplaysAccessKey;
        this.supportsStyleSheets = supportsStyleSheets;
    }

    // Javadoc inherited.
    public boolean supportsAccessKeyAttribute() {
        return supportsAccessKeyAttribute;
    }

    // Javadoc inherited.
    public boolean automaticallyDisplaysAccessKey() {
        return automaticallyDisplaysAccessKey;
    }

    public boolean supportsStyleSheets() {
        return supportsStyleSheets;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Feb-05	6129/6	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/4	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/2	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4440/1	geoff	VBM:2004051703 Enhanced Menus: WML11 doesn't remove accesskey annotations

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
