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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.agent;

import com.volantis.map.common.param.MutableParameters;

import java.lang.reflect.UndeclaredThrowableException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.HashMap;

/**
 * Factory class used to obtain MediaAgent instances
 *
 * <p> <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong> </p>
 */
public abstract class MediaAgentFactory {
    private static final String MEDIA_AGENT_FACTORY_CLASS =
        "com.volantis.map.agent.impl.DefaultMediaAgentFactory";

    private static final Map PREFIX_TO_FACTORY;

    static {
        PREFIX_TO_FACTORY = new HashMap();
    }

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    private static MediaAgentFactory createInstance(final String urlPrefix) {
        try {
            final ClassLoader loader = MediaAgentFactory.class.getClassLoader();
            final Class implClass = loader.loadClass(MEDIA_AGENT_FACTORY_CLASS);
            final Constructor constructor =
                implClass.getConstructor(new Class[]{String.class});
            return (MediaAgentFactory)
                constructor.newInstance(new Object[]{urlPrefix});
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        } catch (NoSuchMethodException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InvocationTargetException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * @deprecated Use {@link #getInstance(String)} instead.
     */
    public static synchronized MediaAgentFactory getInstance() {
        return getInstance("REMOVE_ME");
    }

    /**
     * Get the default instance of this factory.
     *
     * @return The default instance of this factory.
     */
    public static synchronized MediaAgentFactory getInstance(String urlPrefix) {
        if (!urlPrefix.endsWith("/")) {
            urlPrefix += "/";
        }
        MediaAgentFactory factory =
            (MediaAgentFactory) PREFIX_TO_FACTORY.get(urlPrefix);
        if (factory == null) {
            factory  = createInstance(urlPrefix);
            PREFIX_TO_FACTORY.put(urlPrefix, factory);
        }
        return factory;
    }

    /**
     * Get a MediaAgent instance.
     */
    public abstract MediaAgent getMediaAgent();
}
