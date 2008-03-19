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

package com.volantis.shared.net.http.client;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * A factory for {@link HttpClient} related classes.
 */
public abstract class HttpClientFactory {

    /**
     * Obtain a reference to the default factory implementation.
     */
    protected static final MetaDefaultFactory metaDefaultFactory;

    static {
        // Determine whether we are running on JDK 1.3 or JDK 1.4 and later so
        // that we can create an appropriate instance of the HttpClient.
        // There are lots of differences that could be used but the main one
        // that affects the choice is the presence or absence of the new I/O
        // socket channels.
        Version version = Version.JDK13;
        try {
            Class.forName("java.nio.channels.SocketChannel");
            version = Version.JDK14;
        } catch (ClassNotFoundException e) {
            // Ignore the class not found exception.
        }

        String factoryClassName = version.getFactoryClassName();

        metaDefaultFactory =
                new MetaDefaultFactory(factoryClassName,
                        HttpClientFactory.class.getClassLoader());
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static HttpClientFactory getDefaultInstance() {
        return (HttpClientFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * Create a {@link HttpClientBuilder}.
     *
     * @return The newly created {@link HttpClientBuilder}.
     */
    public abstract HttpClientBuilder createClientBuilder();

    /**
     * Enumeration of the different JDK versions that determine the actual
     * factory to use.
     */
    static class Version {

        /**
         * The JDK is version 1.3.
         */
        public static final Version JDK13 = new Version("JDK13",
                "com.volantis.shared.net.impl.http.client.jdk13.JDK13HttpClientFactory");

        /**
         * The JDK is version 1.4 or above.
         */
        public static final Version JDK14 = new Version("JDK14",
                "com.volantis.shared.net.impl.http.client.jdk14.JDK14HttpClientFactory");

        /**
         * The name of the enumeration.
         */
        private final String name;

        /**
         * The name of the factory class to use for this version of the JDK.
         */
        private final String factoryClassName;

        /**
         * Initialise.
         *
         * @param name             The name of the enumeration.
         * @param factoryClassName The name of the factory class.
         */
        private Version(
                String name,
                String factoryClassName) {
            this.name = name;
            this.factoryClassName = factoryClassName;
        }

        // Javadoc inherited.
        public String toString() {
            return name;
        }

        /**
         * Get the name of the factory class.
         *
         * @return The name of the factory class.
         */
        public String getFactoryClassName() {
            return factoryClassName;
        }
    }

}
