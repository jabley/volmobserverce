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
package com.volantis.map.ics.configuration;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for producing ImageProcessor's.
 */
public abstract class ConfigurationParserFactory {

    private static final MetaDefaultFactory instance =
        new MetaDefaultFactory("com.volantis.map.ics.configuration.impl." +
                               "DefaultConfigurationParserFactory",
                               ConfigurationParserFactory.class.getClassLoader());

    /**
     * Creates an image processor with given factories. These factories wil be
     * used for building up image processing machines.
     *
     * @return ImageProcessor - image processor.
     */
    public abstract ConfigurationParser createConfigurationParser();

    /**
     * Gets an instance of ConfigurationParserFactory.
     *
     * @return ConfigurationParserFactory gets an instance of the factory.
     */
    public static ConfigurationParserFactory getInstance() {
        return (ConfigurationParserFactory) instance.getDefaultFactoryInstance();
    }
}
