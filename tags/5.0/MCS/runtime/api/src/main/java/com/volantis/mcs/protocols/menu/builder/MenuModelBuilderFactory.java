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

package com.volantis.mcs.protocols.menu.builder;

/**
 * An abstract factory used to create menu model builder implementation
 * instances. A default instance of the factory is accessible via {@link
 * #getDefaultInstance}. This is guaranteed to return a useable factory
 * instance.
 *
 * <p>If, for some reason, the default instance must be modified it can be
 * using {@link #setDefaultInstance}.</p>
 */
public abstract class MenuModelBuilderFactory {
    /**
     * The factory creates instances of builder implementations.
     *
     * @link dependency
     */
    /*# MenuModelBuilder lnkMenuModelBuilder; */
    
    /**
     * The default factory instance. This can be changed via the
     * {@link #setDefaultInstance} method.
     * 
     * @supplierRole defaultInstance
     * @supplierCardinality 1 
     */
    private static MenuModelBuilderFactory defaultInstance =
        new com.volantis.mcs.protocols.menu.shared.builder.
            ConcreteMenuModelBuilderFactory();

    /**
     * Permits the default factory instance to be set or reset. The previous
     * default factory is returned.
     *
     * @param defaultInstance
     *         the new default instance. May not be null
     * @return the previous default factory instance. Will not be null
     * @throws IllegalArgumentException if a null factory is supplied
     */
    public static MenuModelBuilderFactory setDefaultInstance(
        MenuModelBuilderFactory defaultInstance)
        throws IllegalArgumentException {
        if (defaultInstance == null) {
            throw new IllegalArgumentException(
                "Builder default factory may not be null");
        }

        MenuModelBuilderFactory old = MenuModelBuilderFactory.defaultInstance;
        MenuModelBuilderFactory.defaultInstance = defaultInstance;

        return old;
    }

    /**
     * Allows the default menu model builder to be obtained.
     */
    public static MenuModelBuilderFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Factory method used to obtain a new menu model builder instance.
     *
     * @return a new menu model builder
     */
    public abstract MenuModelBuilder createBuilder();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Mar-04	3342/1	philws	VBM:2004022707 Implement the Menu Model Builder

 05-Mar-04	3292/1	philws	VBM:2004022703 Added Menu Model Builder API

 ===========================================================================
*/
