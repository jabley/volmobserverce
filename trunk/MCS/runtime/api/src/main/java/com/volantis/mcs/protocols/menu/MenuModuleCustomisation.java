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
 * Encapsulates those customisations that affect the menu module.
 *
 * @mock.generate
 */
public interface MenuModuleCustomisation {

    /**
     * Specifies whether the protocol supports accesskey attributes.
     * 
     * @return True if the protocol supports accesskey attributes.
     */
    public boolean supportsAccessKeyAttribute();
    
    /**
     * Determines whether the device will automatically display the access key
     * associated with a link.
     *
     * <p>This will be checked if the author has requested that the access key
     * should be displayed to the user. If this is true then no additional
     * effort is required, otherwise the protocol renderers will need to add
     * the access key to the content.</p>
     *
     * @return True if the device will automatically display the access key
     * associated with a link, and false if it will not.
     */
    public boolean automaticallyDisplaysAccessKey();

    /**
     * @return  Returns true if the device supports stylesheets of one form or
     * another.
     */
    public boolean supportsStyleSheets();

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
