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
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-May-03    Allan           VBM:2003051303 - Created. A stub for
 *                              java.sql.Connection.
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools.stubs;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;


/**
 * This has been rewritten as a Dynamic Proxy due to incompatible changes in
 * the Connection interface between JDK versions that would stop us compiling
 * against jdk1.5 and jdk 1.6 at the same time.
 */
public class ConnectionStub {

    // hide it
    private ConnectionStub() {

    }

    public static final Connection createConnectionStub(InvocationHandler handler) {

        if (null == handler) {
            handler = new InvocationHandler() {
                public Object invoke(Object proxy, Method method,
                                     Object[] args)
                    throws Throwable {

                    if (Integer.TYPE == method.getReturnType() || Integer.class == method.getReturnType()) {
                        return new Integer(0);
                    } else if (Boolean.TYPE == method.getReturnType() ||
                        Boolean.class == method.getReturnType()) {
                        return Boolean.FALSE;
                    }


                    return null;
                }
            };
        }

        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),
                                                   new Class[]{
                                                       Connection.class}, handler);

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
