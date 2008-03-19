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

import com.volantis.mcs.runtime.OutputBufferResolver;

/**
 * Default implementation.
 */
public class DefaultMenuBufferLocatorFactory
        implements MenuBufferLocatorFactory {

    /**
     * The object for resolving formats to
     * {@link com.volantis.mcs.protocols.OutputBuffer}.
     */
    private final OutputBufferResolver bufferResolver;

    /**
     * Initialise.
     *
     */
    public DefaultMenuBufferLocatorFactory(
            OutputBufferResolver bufferResolver) {

        this.bufferResolver = bufferResolver;
    }

    // Javadoc inherited.
    public MenuBufferLocator createMenuBufferLocator(MenuBufferFactory factory) {
        return new DefaultMenuBufferLocator(bufferResolver, factory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Mar-05	7022/2	geoff	VBM:2005021711 R821: Branding using Projects: Prerequisites: Fix menu expression resolution

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 ===========================================================================
*/
