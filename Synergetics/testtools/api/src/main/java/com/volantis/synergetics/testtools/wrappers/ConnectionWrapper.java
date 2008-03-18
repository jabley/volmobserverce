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
 * $Header: /src/voyager/com/volantis/testtools/wrappers/ConnectionWrapper.java,v 1.1 2002/12/19 08:45:53 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Dec-02    Allan           VBM:2002120402 - A wrapper around a
 *                              Connection that is also a
 *                              Connection so we can intercept
 *                              method invocations for test purposes.
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools.wrappers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * A wrapper around a javax.sql.Connection that is also a Connection. This
 * enables interception of method invocations for test purposes.
 *
 * If you need to intercept a method in Statement then implement an
 * InvocationHandler
 */
public class ConnectionWrapper {

    // hide it
    private ConnectionWrapper() {

    }

    /**
     * @param delegate the Connection to delegate to. Must not be null.
     * @return a Statement
     */
    public static Connection createConnectionWrapper(final Connection delegate) {

        if (null == delegate) {
            throw new IllegalArgumentException("Connection must not be null");
        }

        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args)
                throws Throwable {
                return method.invoke(delegate, args);
            }
        };

        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                                                  new Class[]{Connection.class},
                                                  handler);
    }

    /**
     * @param handler the invocation handler to use
     * @return a Connection
     */
    public static Connection createStatementWrapper(InvocationHandler handler) {

        if (null == handler) {
            throw new IllegalArgumentException("Handler must not be null");
        }

        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                                                  new Class[]{Connection.class},
                                                  handler);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jun-03	15/1	allan	VBM:2003060907 Move some more testtools to here from MCS

 ===========================================================================
*/
