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
package com.volantis.mcs.protocols.menu;

/**
 * An interface to allow pluggable decoration of menu module renderer 
 * factories.
 * <p>
 * This was required initially to allow us to decorate these factories for 
 * creating shard link versions during the construction of a shard link 
 * specific instance of a menu module. 
 */ 
public interface MenuModuleRendererFactoryFilter {

    /**
     * Decorate the factory provided as necessary.
     * 
     * @param rendererFactory the factory to decorate.
     * @return a decorated version of the original factory, may be the same 
     *      instance if no decoration is required.
     */ 
    MenuModuleRendererFactory decorate(
            MenuModuleRendererFactory rendererFactory);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 ===========================================================================
*/
