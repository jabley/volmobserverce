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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.framework.boot;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

/**
 * An InvocationHandler for a service proxy that switch the context class
 * loader before calling a service method and restores it afterwards.
 */
public class ContextSwitchingProxyHandler
        implements InvocationHandler {

    /**
     * The service being proxied.
     */
    private final Object service;

    /**
     * The context class loader.
     */
    private final ClassLoader contextClassLoader;

    /**
     * Initialise.
     *
     * @param service            The service to proxy.
     * @param contextClassLoader The context class loader.
     */
    public ContextSwitchingProxyHandler(
            Object service, ClassLoader contextClassLoader) {
        this.service = service;
        this.contextClassLoader = contextClassLoader;
    }

    // Javadoc inherited.
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {

        // Make sure that the return type for the service method is primitive
        // or an array of primitives. Interfaces and classes are not supported
        // because they could have methods that require context switching and
        // at the moment that is not supported.
//        Class returnType = method.getReturnType();
//        checkType(method, returnType);

        Thread thread = Thread.currentThread();
        ClassLoader oldContextClassLoader = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(contextClassLoader);
            Object returnObject = method.invoke(service, args);

            return returnObject;
        } catch (InvocationTargetException e) {
            // Extract the cause from the InvocationTargetException and throw
            // that as then it will be handled properly by the proxy rather
            // than causing an UndeclaredThrowableException to be thrown.
            throw e.getCause();
        } finally {
            thread.setContextClassLoader(oldContextClassLoader);
        }
    }

    /**
     * Check the type for the specified method to ensure that it is a primitive
     * type.
     *
     * @param method The method, for error reporting.
     * @param type   The type.
     */
    private void checkType(Method method, Class type) {
        if (type.isPrimitive()) {
            // Nothing to do.
        } else if (type.isArray()) {
            checkType(method, type.getComponentType());
        } else {
            throw new IllegalStateException("Cannot invoke service method " +
                    method + " as its return type is not primitive, " +
                    "or an array of primitives");
        }
    }
}
