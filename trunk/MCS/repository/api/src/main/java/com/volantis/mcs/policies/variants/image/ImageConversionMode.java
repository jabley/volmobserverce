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

package com.volantis.mcs.policies.variants.image;

/**
 * Type safe enumeration of the different conversion modes that images may use.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see ImageMetaData
 * @since 3.5.1
 */
public final class ImageConversionMode {

    /**
     * Indicates that the image resource is never passed to an image conversion
     * server before it is used.
     */
    public static final ImageConversionMode NEVER_CONVERT =
            new ImageConversionMode("NEVER_CONVERT");

    /**
     * Indicates that the image resource is always passed to an image
     * conversion server before it is used.
     */
    public static final ImageConversionMode ALWAYS_CONVERT =
            new ImageConversionMode("ALWAYS_CONVERT");

    private final String name; // for debug only

    private ImageConversionMode(String name) {
        this.name = name;
    }

    /**
     * Overridden to return the name.
     *
     * <p>The returned value is only supplied for debug purposes and cannot be
     * relied upon.</p>
     *
     * @return The name of the vendor.
     */
    public String toString() {
        return name;
    }
}
