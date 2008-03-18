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
package com.volantis.synergetics.factory;

import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

/**
 * A meta factory used to create Default Factories. Default factory
 * implementations must have a public no-arg constructor. <p/> This class uses
 * reflection to load the specified default factory. It was created to
 * centralise the functionality needed by factories thoughout volantis
 * products. <p/> An example of the use of this class is given in the test
 * cases.
 *
 * @see AbstractExampleMapFactory
 * @see DefaultExampleMapFactory
 * @see MetaDefaultFactoryTestCase
 */
public class MetaDefaultFactory {

    /**
     * Used for localizing messages
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(MetaDefaultFactory.class);

    /**
     * The default factory created using reflection
     */
    private final Object defaultFactoryInstance;

    /**
     * The exception that may have been thrown during construction
     */
    private final UndeclaredThrowableException initialisationException;

    /**
     * Construct the MetaDefaultFactory.
     *
     * @param factoryClassName the full class name of the default factory
     *                         implementation this instance of the meta factory
     *                         should produce
     * @param classLoader      the class loader to use when loading the default
     *                         factory implementation
     */
    public MetaDefaultFactory(String factoryClassName,
                              ClassLoader classLoader) {

        Throwable exception = null;
        Object factoryInstance = null;

        try {
            Class implClass =
                classLoader.loadClass(factoryClassName);
            factoryInstance = implClass.newInstance();
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (InstantiationException e) {
            exception = e;
        } catch (IllegalAccessException e) {
            exception = e;
        }

        this.defaultFactoryInstance = factoryInstance;
        if (exception != null) {
            initialisationException = new UndeclaredThrowableException(
                exception,
                EXCEPTION_LOCALIZER.format("failed-to-load-class",
                                           factoryClassName));
        } else {
            initialisationException = null;
        }
    }

    /**
     * @return the an instance of the default factory this MetaDefaultFactory
     *         has been told to create.
     *
     * @throws UndeclaredThrowableException if an error occured while
     *                                      attempting to create the default
     *                                      factory
     */
    public Object getDefaultFactoryInstance() {
        if (initialisationException != null) {
            throw initialisationException;
        }
        return defaultFactoryInstance;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	487/3	matthew	VBM:2005062701 Create a DefaultFactory factory

 27-Jun-05	487/1	matthew	VBM:2005062701 Create a DefaultFactory factory

 ===========================================================================
*/
