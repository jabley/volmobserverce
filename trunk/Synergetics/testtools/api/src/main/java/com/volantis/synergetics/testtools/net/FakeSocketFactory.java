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

import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.SocketImplFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link java.net.SocketImplFactory} that allows the user to register the
 * response that a Socket will provide.
 */
public class FakeSocketFactory implements SocketImplFactory {

    /**
     * Will hold the registered responses - keyed on a port number.
     */
    private static Map responses = new HashMap();

    /**
     * Will store the data sent to the socket keyed on a port number.
     */
    private static Map requests = new HashMap();

    /**
     * Convience method that allows registers this Factory with the {@link
     * java.net.Socket} class. If the Socket class as already had a Factory
     * registered against this factory will not be registered.
     *
     * @return true if and only if this class was successfully registered.
     */
    public static boolean registerForClientSockets() {
        boolean success = true;
        try {
            Socket.setSocketImplFactory(new FakeSocketFactory());
        } catch (IOException e) {
            // You can only ever set the SocketImplFactory for the socket
            // class once. If we catch this exception we have to assume that
            // it has occurred because the factory has already been set.
            success = false;
        }
        return success;
    }

    /**
     * Register a response for a given port. Any connection requested on this
     * port will return the registered response via the {@link
     * java.net.SocketImpl#getInputStream} method.
     *
     * @param responseHeaders the response that is to be registered
     * @param port            the port to register against
     */
    public static void putResponse(String[] responseHeaders, int port) {
        responses.put(new Integer(port), responseHeaders);
    }

    /**
     * Removes a registered response for the given port
     *
     * @param port the port that the response to be removed is registered
     *             against
     */
    private static void removeResponse(int port) {
        responses.remove(new Integer(port));
    }

    /**
     * Returns a response for a given port
     *
     * @param port the port that the response to be returned is registered
     *             against
     * @return the String[] array response
     */
    private static String[] getResponse(int port) {
        return (String[]) responses.get(new Integer(port));
    }

    /**
     * Removes a registered response for the given port
     *
     * @param port the port that the response to be removed is registered
     *             against
     */
    private static void removeRequest(int port) {
        requests.remove(new Integer(port));
    }

    /**
     * Releases any cached response/requests registered against the given port
     *
     * @param port the port number whose resource are to be released
     */
    public static void release(int port) {
        removeRequest(port);
        removeResponse(port);
    }

    /**
     * Returns a response for a given port
     *
     * @param port the port that the response to be returned is registered
     *             against
     * @return the String[] array response
     */
    public static byte[] getRequest(int port) {
        return ((ByteArrayOutputStream)
            requests.get(new Integer(port))).toByteArray();
    }

    /**
     * Returns an InputStream that allows access to the response that is
     * registered against the given port.
     *
     * @param port the port
     * @return an InputStream or null if no response is registered against the
     *         specified port.
     */
    private InputStream lookupCachedResponse(int port) {
        String[] response = getResponse(port);
        InputStream responseStream = null;
        if (response != null) {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < response.length; i++) {
                buffer.append(response[i]);
                buffer.append("\r\n");
            }
            responseStream = new ByteArrayInputStream(
                buffer.toString().getBytes());
        }
        return responseStream;
    }

    // javadoc inherited
    public SocketImpl createSocketImpl() {

        try {
            // get the Class for the PlainSocketImpl class
            final Class delegatClass =
                Class.forName("java.net.PlainSocketImpl");
            // obtain the default constructor for this class
            Constructor constructor =
                delegatClass.getDeclaredConstructor(new Class[]{});
            // it will be protected so ensure we can access it
            constructor.setAccessible(true);
            // create an instance of the PlainSocketImpl class
            SocketImpl impl =
                (SocketImpl) constructor.newInstance(new Object[]{});

            // return a SocketImp that wraps a PlainSocketImpl
            return new SocketImplWrapper(impl) {

                private int port = -1;

                private InputStream cachedResponse = null;

                private ByteArrayOutputStream request = null;

                // javadoc inherited
                protected void accept(SocketImpl s) throws IOException {
                    if (cachedResponse == null) {
                        super.accept(s);
                    }
                }

                // javadoc inherited
                protected int available() throws IOException {
                    if (cachedResponse == null) {
                        return super.available();
                    } else {
                        return cachedResponse.available();
                    }
                }

                // javadoc inherited
                protected void bind(InetAddress host, int port)

                    throws IOException {
                    this.port = port;
                    cachedResponse = lookupCachedResponse(port);
                    if (cachedResponse == null) {
                        super.bind(host, port);
                    }
                }

                // javadoc inherited
                protected void close() throws IOException {
                    if (cachedResponse == null) {
                        super.close();
                    }
                }

                // javadoc inherited
                protected void connect(InetAddress address, int port)
                    throws IOException {
                    this.port = port;
                    cachedResponse = lookupCachedResponse(port);
                    if (cachedResponse == null) {
                        super.connect(address, port);
                    }
                }

                // javadoc inherited
                protected void connect(String host, int port)
                    throws IOException {
                    this.port = port;
                    cachedResponse = lookupCachedResponse(port);
                    if (cachedResponse == null) {
                        super.connect(host, port);
                    }
                }
                
                // javadoc inherited
                protected void connect(SocketAddress address, int timeout)
                    throws IOException {
                    final InetSocketAddress endpoint = 
                            (InetSocketAddress) address;
                    this.port = endpoint.getPort();
                    cachedResponse = lookupCachedResponse(port);
                    if (cachedResponse == null) {
                        super.connect(address, timeout);
                    }
                }

                // javadoc inherited
                protected void create(boolean stream) throws IOException {
                    super.create(stream);
                }

                // javadoc inherited
                protected InputStream getInputStream() throws IOException {
                    return (cachedResponse != null)
                        ? cachedResponse : super.getInputStream();
                }

                // javadoc inherited
                public Object getOption(int optID) throws SocketException {
                    return super.getOption(optID);
                }

                // javadoc inherited
                protected OutputStream getOutputStream() throws IOException {
                    OutputStream stream = null;
                    if (cachedResponse != null) {
                        request = new ByteArrayOutputStream();
                        stream = request;
                        if (request != null && port != -1) {
                            requests.put(new Integer(port), request);
                        }
                    } else {
                        stream = super.getOutputStream();
                    }
                    return stream;
                }

                // javadoc inherited
                protected void listen(int backlog) throws IOException {
                    super.listen(backlog);
                }

                // javadoc inherited
                public void setOption(int optID, Object value)
                    throws SocketException {
                    super.setOption(optID, value);
                }
            };

        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (NoSuchMethodException e) {
            throw new UndeclaredThrowableException(e);
        } catch (SecurityException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InvocationTargetException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jul-04	274/3	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 19-Jul-04	781/3	adrianj	VBM:2004071302 Moved FakeSocketFactory and SocketImplWrapper from Pipeline to Synergetics

 16-Jul-04	767/1	claire	VBM:2004070101 Provide Jigsaw implemention of PluggableHTTPManager

 09-Jul-04	769/1	doug	VBM:2004070502 Improved integration tests for the Web Driver

 ===========================================================================
*/
