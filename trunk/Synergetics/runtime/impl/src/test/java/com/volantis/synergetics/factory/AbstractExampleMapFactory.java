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
package com.volantis.synergetics.factory;

import java.util.Map;

/**
 * Define the abstract factory.
 */
public abstract class AbstractExampleMapFactory {

    /**
     * Set up the meta default factory instance
     */
    private static MetaDefaultFactory metaDefaultFactory = new MetaDefaultFactory(
        "com.volantis.synergetics.factory.DefaultExampleMapFactory",
        AbstractExampleMapFactory.class.getClassLoader());

    /**
     * @return the default instance of the ExampleMapFactory
     */
    public static AbstractExampleMapFactory getDefaultInstance() {
        return (AbstractExampleMapFactory) metaDefaultFactory.getDefaultFactoryInstance();
    }

    /**
     * The example factory is used to create instances of Map
     *
     * @return an instance of the Map interface
     */
    public abstract Map createMap();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	487/1	matthew	VBM:2005062701 Create a DefaultFactory factory

 ===========================================================================
*/
