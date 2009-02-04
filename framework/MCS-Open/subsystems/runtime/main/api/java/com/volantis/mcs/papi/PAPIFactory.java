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


package com.volantis.mcs.papi;


import java.lang.reflect.UndeclaredThrowableException;

/**
 * This factory provides a @link PAPIElementFactory for the given element type e.g. "a", "big" etc.
 */
public abstract class PAPIFactory {

    /**
     * The default single instance of this factory
     */
    private static PAPIFactory defaultInstance;

    /**
     * Instantiate the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = PAPIFactory.class.getClassLoader();
            Class implClass = null;
            PAPIFactory instance = null;

            try {
                implClass = loader.loadClass(
                        "com.volantis.mcs.papi.impl.DefaultPAPIFactory");
                instance = (PAPIFactory) implClass.newInstance();
            } catch (ClassNotFoundException e) {
            }

            defaultInstance = instance;
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the single instance of the DefaultPAPIFactory
     * @return The default instance of the DefaultPAPIFactory;
     */
    public static PAPIFactory getDefaultInstance() {
        if (defaultInstance == null) {
            throw new UndeclaredThrowableException(new ClassNotFoundException(
                    "com.volantis.mcs.papi.impl.DefaultPAPIFactory"));
        }

        return defaultInstance;
    }

    /**
     * Get the PAPIElementFactory for the given PAPI Element
     * @param element The name of the PAPIElement
     * @return The Factory responsible for creating implementations of the PAPI Element
     */
    public abstract PAPIElementFactory getPAPIElementFactory(String element);

    /**
     * Get the PAPIElementFactory for PickleBlockElementImpl
     * @return The Factory responsible for creating implementations of the PickleBlockElementImpl
     */
    public abstract PAPIElementFactory getPickleBlockElementFactory();

    /**
     * Get the PAPIElementFactory for PickleHeadElementImpl
     * @return The Factory responsible for creating implementations of the PickleHeadElementImpl
     */
    public abstract PAPIElementFactory getPickleHeadElementFactory();

    /**
     * Get the PAPIElementFactory for PickleInlineElementImpl
     * @return The Factory responsible for creating implementations of the PickleInlineElementImpl
     */
    public abstract PAPIElementFactory getPickleInlineElementFactory();

    /**
     * Get the PAPIElementFactory for PickleNativeElementImpl
     * @return The Factory responsible for creating implementations of the PickleNativeElementImpl
     */
    public abstract PAPIElementFactory getPickleNativeElementFactory();

}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 18-May-05	8196/3	ianw	VBM:2005051203 Fixed up javadoc

 18-May-05	8196/1	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 ===========================================================================
*/
