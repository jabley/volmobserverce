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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.convertor;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory class to return the image convertor class required to convert an
 * image into a given format.
 */
public abstract class ImageConvertorFactory {

    private static final MetaDefaultFactory instance =
        new MetaDefaultFactory("com.volantis.map.ics.imageprocessor.convertor.impl." +
                               "DefaultImageConvertorFactory",
                               ImageConvertorFactory.class.getClassLoader());

    /**
     * Return an image convertor to convert an image into a given format. Once
     * accessed, the convertor will be stored in a map and the same object will
     * be returned every time from then on. For this reason, all convertors
     * must be stateless so they can be re-used by any number of threads.
     *
     * @param type the target image type.
     * @return a convertor that will achieve the required type.
     */
    public abstract ImageConvertor getImageConvertor(int type);

    /**
     * Gets an instance of ImageConvertorFactory.
     *
     * @return ImageConvertorFactory gets an instance of the factory.
     */
    public static ImageConvertorFactory getInstance() {
        return (ImageConvertorFactory) instance.getDefaultFactoryInstance();
    }
}
