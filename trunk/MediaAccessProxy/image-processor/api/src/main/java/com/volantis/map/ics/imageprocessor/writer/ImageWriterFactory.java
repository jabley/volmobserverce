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

package com.volantis.map.ics.imageprocessor.writer;

import com.volantis.synergetics.factory.MetaDefaultFactory;
import com.volantis.map.common.param.Parameters;

/**
 * Factory for ImageWriter objects.
 */
public abstract class ImageWriterFactory {

    private static final MetaDefaultFactory instance =
        new MetaDefaultFactory("com.volantis.map.ics.imageprocessor.writer.impl." +
                               "ImageWriterFactoryImpl",
                               ImageWriterFactory.class.getClassLoader());

    /**
     * Retrieves appropriate writer for pointed image conversion rule.
     *
     * @param imageRule - image rule.
     * @return ImageWriter appropriate image writer.
     *
     * @throws ImageWriterFactoryException - if there is impossible to retrieve
     *                                     appropriate writer.
     */
    public abstract ImageWriter getWriter(String imageRule, Parameters params)
        throws ImageWriterFactoryException;

    /**
     * Gets an instance of ImageWriterFactory.
     *
     * @return instance of the factory.
     */
    public static ImageWriterFactory getInstance() {
        return (ImageWriterFactory) instance.getDefaultFactoryInstance();
    }
}
