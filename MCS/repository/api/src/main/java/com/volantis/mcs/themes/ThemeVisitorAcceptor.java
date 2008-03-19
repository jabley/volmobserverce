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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes;

/**
 * Interface defining the accept method(s) which need to be implemented
 * by all objects in a Theme tree, Theme, DeviceTheme, Selector ...
 *
 */
public interface ThemeVisitorAcceptor {

    /**
     * Accept the given visitor and call the visit() method on the visitor.
     * In addition this method may call accept on any child objects.
     *
     * @param visitor ThemeVisitor to call.
     */
    void accept(ThemeVisitor visitor);
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-May-05	8329/1	pabbott	VBM:2005051901 New vistitor pattern for Themes

 ===========================================================================
*/
