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

package com.volantis.mcs.runtime.plugin.markup;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.integration.MarkupPlugin;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfigurationContainer;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Factory to use to create {@link MarkupPlugin} related objects.
 *
 * <p>This is needed in order to separate the implementation from the API.
 * Although these classes are not really part of the API of this subsystem they
 * have to be treated as such. This is because although they are only
 * referenced by the implementation it has not been separated from the API.</p>
 *
 * @mock.generate
 */
public abstract class MarkupFactory {

    /**
     * The default instance.
     */
    private static final MarkupFactory defaultInstance;

    /**
     * The exception thrown during initialisation.
     */
    private static final Throwable initialisationException;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class. Defer any
     * exceptions until the default instance is actually requested.
     */
    static {
        Throwable exception = null;
        MarkupFactory instance = null;
        try {
            ClassLoader loader = MarkupFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.impl.mcs.runtime.plugin.markup.MarkupFactoryImpl");
            instance = (MarkupFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (InstantiationException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        }

        defaultInstance = instance;
        initialisationException = exception;
    }

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static MarkupFactory getDefaultInstance() {
        if (initialisationException != null) {
            throw new UndeclaredThrowableException(initialisationException);
        }

        return defaultInstance;
    }

    /**
     * Create a {@link MarkupPluginManager}.
     *
     * @param plugins The set of plugins.
     *
     * @return A new {@link MarkupPluginManager}.
     */
    public abstract MarkupPluginManager createMarkupPluginManager(
            IntegrationPluginConfigurationContainer plugins,
            MarkupPluginContainer applicationContainer,
            MarinerApplication application);

    /**
     * Create a {@link MarkupPluginContainer}.
     * @return A new {@link MarkupPluginContainer}.
     */
    public abstract MarkupPluginContainer createMarkupPluginContainer();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 25-Jan-05	6712/5	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
