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

package com.volantis.styling.impl.state;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * todo Document this.
 */
public abstract class StateFactory {

    /**
     * The default instance.
     */
    private static final StateFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = StateFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.styling.impl.state.impl.StateFactoryImpl");
            defaultInstance = (StateFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static StateFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Create a new {@link MutableStateRegistry} instance.
     *
     * @return A newly create {@link MutableStateRegistry} instance.
     */
    public abstract MutableStateRegistry createStateRegistry();

    /**
     * Create a container that can hold state for the specified registry.
     *
     * @param registry The registry that defines the set of valid
     * {@link StateIdentifier}s that can be used to retrieve {@link State}
     * objects.
     *
     * @return A newly created {@link StateContainer} instance.
     */
    public abstract StateContainer createStateContainer(
            ImmutableStateRegistry registry);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
