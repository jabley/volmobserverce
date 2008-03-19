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

package com.volantis.xml.sax.recorder;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Creates instances of objects relating to recording and playing back a stream
 * of SAX events.
 */
public abstract class SAXRecorderFactory {

    /**
     * The default instance.
     */
    private static final SAXRecorderFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = SAXRecorderFactory.class.getClassLoader();
            Class implClass = loader.loadClass("com.volantis.xml.sax.recorder.impl.SAXRecorderFactoryImpl");
            defaultInstance = (SAXRecorderFactory) implClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static SAXRecorderFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Create a {@link SAXRecorderConfiguration} instance.
     *
     * @return A newly created {@link SAXRecorderConfiguration} instance.
     */
    public abstract SAXRecorderConfiguration createSAXRecorderConfiguration();

    /**
     * Create a {@link SAXRecorder} instance.
     *
     * @param configuration The configuration for the recorder, may not be null
     * and must not be changed after calling this method.
     *
     * @return The newly created {@link SAXRecorder} instance.
     */
    public abstract SAXRecorder createSAXRecorder(SAXRecorderConfiguration configuration);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Apr-05	1262/1	pduffin	VBM:2005041105 Added support for preparsing the pipeline template

 ===========================================================================
*/
