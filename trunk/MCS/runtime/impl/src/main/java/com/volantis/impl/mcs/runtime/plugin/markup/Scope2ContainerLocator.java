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

package com.volantis.impl.mcs.runtime.plugin.markup;

/**
 * Get the container locator for a specified scope.
 *
 * <p>This is provide to allow tests to override the default behaviour of
 * {@link MarkupPluginManagerImpl} to prevent them from having to
 * initialise all the context objects.</p>
 */
public interface Scope2ContainerLocator {

    public static final Scope2ContainerLocator DEFAULT_INSTANCE
            = new Scope2ContainerLocator() {
                public ContainerLocator getLocator(MarkupPluginScope scope) {
                    return scope.getLocator();
                }
            };

    /**
     * Get the container locator to use for the specified scope.
     *
     * @param scope The scope, may not be null.
     * @return The container locator.
     */
    public ContainerLocator getLocator(MarkupPluginScope scope);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
