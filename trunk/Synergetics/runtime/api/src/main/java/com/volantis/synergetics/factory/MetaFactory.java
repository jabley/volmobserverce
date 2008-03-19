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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.factory;

import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.lang.reflect.Constructor;

/**
 * <p>Utility class for encapsulating the process of creating classes
 * reflectively.</p>
 *
 * @volantis-api-include-in InternalAPI
 */
public class MetaFactory {

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(MetaFactory.class);

    private final Constructor constructor;

    private final UndeclaredThrowableException initializationException;

    /**
     * Create a new MetaFactory object which will try to locate a public
     * default constructor on the specified class.
     *
     * @param className   the class name to try to instantiate
     * @param classLoader the classloader to use
     */
    public MetaFactory(String className, ClassLoader classLoader) {
        this(className, classLoader, new Class[0]);
    }

    /**
     * Create a new MetaFactory object which willl try to locate a public
     * constructor with the specifed signature on the specifed class.
     *
     * @param className   the class name to try to instantiate
     * @param classLoader the classloader to use
     * @param parameters  Class array representing the constructor parameters
     */
    public MetaFactory(String className, ClassLoader classLoader,
                       Class[] parameters) {
        Throwable exception = null;
        Constructor constructorInstance = null;

        try {
            Class implClass =
                classLoader.loadClass(className);
            constructorInstance = implClass.getConstructor(parameters);
        } catch (ClassNotFoundException e) {
            exception = e;
        } catch (NoSuchMethodException e) {
            exception = e;
        }
        this.constructor = constructorInstance;
        if (null != exception) {
            this.initializationException = new UndeclaredThrowableException(
                exception,
                EXCEPTION_LOCALIZER.format("failed-to-load-class", className));
        } else {
            initializationException = null;
        }
    }

    /**
     * Shortcut utility method for creating instances of the class using the
     * default constructor. <strong>This method should only be called if the
     * constructor {@link #MetaFactory(String, ClassLoader)}  was used to
     * create this instance of a MetaFactory.</strong>
     *
     * @return an Object instance of the specified class, using the default
     *         constructor.
     *
     * @throws UndeclaredThrowableException if there was a problem
     */
    public Object createInstance() {
        return createInstance(new Object[0]);
    }

    /**
     * Create an instance of the class using the specified parameters to the
     * constructor
     *
     * @param arguments an ordered arrray of parameters to the constructor,
     *                  matching the Class array used to instantiate this
     *                  instance of MetaFactory.
     * @return an Object instance of the specified class
     *
     * @throws UndeclaredThrowableException if there was a problem
     */
    public Object createInstance(Object[] arguments) {
        if (null != this.initializationException) {
            throw this.initializationException;
        }

        try {
            return this.constructor.newInstance(arguments);
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e,
                                                   EXCEPTION_LOCALIZER.format(
                                                       "failed-to-create-class",
                                                       constructor.getName()));
        }

    }
}
