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
package com.volantis.mcs.policies.variants.metadata;

/**
 * Builder of {@link PixelDimensionsMetaData} instances.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see PixelDimensionsMetaData
 * @since 3.5.1
 */
public interface PixelDimensionsMetaDataBuilder
        extends MetaDataBuilder {

    /**
     * Getter for the <a href="PixelDimensionsMetaData.html#width">width</a>
     * property.
     *
     * @return Value of the
     *         <a href="PixelDimensionsMetaData.html#width">width</a> property.
     */
    int getWidth();

    /**
     * Setter for the <a href="PixelDimensionsMetaData.html#width">width</a>
     * property.
     *
     * @param width New value of the
     *              <a href="PixelDimensionsMetaData.html#width">width</a>
     *              property.
     */
    void setWidth(int width);

    /**
     * Getter for the <a href="PixelDimensionsMetaData.html#height">height</a>
     * property.
     *
     * @return Value of the
     *         <a href="PixelDimensionsMetaData.html#height">height</a>
     *         property.
     */
    int getHeight();

    /**
     * Setter for the <a href="PixelDimensionsMetaData.html#height">height</a>
     * property.
     *
     * @param height New value of the
     *               <a href="PixelDimensionsMetaData.html#height">height</a>
     *               property.
     */
    void setHeight(int height);
}
