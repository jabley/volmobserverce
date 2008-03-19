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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.testtools.net;

import junitx.util.PrivateAccessor;

import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

/**
 * A {@link java.net.SocketImpl} implementation that delegates to another
 * <code>SocketImpl</code> instance
 */
public class SocketImplWrapper extends SocketImpl {

    /**
     * The <code>SocketImpl</code> instance that will be delegated to.
     */
    private SocketImpl delegate;

    /**
     * Creates a new <code>SocketImplWrapper</code> instance
     *
     * @param delegate the <code>SocketImplWrapper</code> that will be
     *                 delegated to.
     */
    public SocketImplWrapper(SocketImpl delegate) {
        this.delegate = delegate;
    }

    // javadoc inherited
    protected void accept(SocketImpl s) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "accept",
                                   new Class[]{SocketImpl.class},
                                   new Object[]{s});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected int available() throws IOException {
        try {
            Integer i = (Integer) PrivateAccessor.invoke(delegate,
                                                         "available",
                                                         new Class[]{},
                                                         new Object[]{});
            return i.intValue();
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected void bind(InetAddress host, int port) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "bind",
                                   new Class[]{InetAddress.class, int.class},
                                   new Object[]{host, new Integer(port)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected void close() throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "close",
                                   new Class[]{},
                                   new Object[]{});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected void connect(InetAddress address, int port) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "connect",
                                   new Class[]{InetAddress.class, int.class},
                                   new Object[]{address, new Integer(port)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected void connect(String host, int port) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "connect",
                                   new Class[]{String.class, int.class},
                                   new Object[]{host, new Integer(port)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }


    // javadoc inherited
    protected void connect(SocketAddress address,
                           int timeout) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "connect",
                                   new Class[]{SocketAddress.class, int.class},
                                   new Object[]{address, new Integer(timeout)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected void create(boolean stream) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "create",
                                   new Class[]{boolean.class},
                                   new Object[]{new Boolean(stream)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected InputStream getInputStream() throws IOException {
        try {
            return (InputStream) PrivateAccessor.invoke(delegate,
                                                        "getInputStream",
                                                        new Class[]{},
                                                        new Object[]{});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected OutputStream getOutputStream() throws IOException {
        try {
            return (OutputStream) PrivateAccessor.invoke(delegate,
                                                         "getOutputStream",
                                                         new Class[]{},
                                                         new Object[]{});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    protected void listen(int backlog) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "listen",
                                   new Class[]{int.class},
                                   new Object[]{new Integer(backlog)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    public Object getOption(int optID) throws SocketException {
        try {
            return PrivateAccessor.invoke(delegate,
                                          "getOption",
                                          new Class[]{int.class},
                                          new Object[]{new Integer(optID)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    // javadoc inherited
    public void setOption(int optID, Object value) throws SocketException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "setOption",
                                   new Class[]{int.class, Object.class},
                                   new Object[]{new Integer(optID), value});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }


    // javadoc inherited
    protected void sendUrgentData(int data) throws IOException {
        try {
            PrivateAccessor.invoke(delegate,
                                   "sendUrgentData",
                                   new Class[]{int.class},
                                   new Object[]{new Integer(data)});
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Nov-04	343/1	doug	VBM:2004111702 Refactored Logging framework

 20-Jul-04	274/3	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 19-Jul-04	781/4	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 19-Jul-04	769/2	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 09-Jul-04	769/1	doug	VBM:2004070502 Improved integration tests for the Web Driver

 ===========================================================================
*/
