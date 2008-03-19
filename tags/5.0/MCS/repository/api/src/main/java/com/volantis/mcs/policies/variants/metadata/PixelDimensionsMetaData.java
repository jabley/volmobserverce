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
 * Meta data for content which has dimensions (width and height) in pixels.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="width">
 * <td align="right" valign="top" width="1%"><b>width</b></td>
 * <td>the width of the resource in pixels.</td>
 * </tr>
 *
 * <tr id="height">
 * <td align="right" valign="top" width="1%"><b>height</b></td>
 * <td>the height of the resource in pixels.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see PixelDimensionsMetaDataBuilder
 * @since 3.5.1
 */
public interface PixelDimensionsMetaData
        extends MetaData {

    /**
     * Getter for the <a href="#width">width</a> property.
     *
     * @return Value of the <a href="#width">width</a>
     *         property.
     */
    int getWidth();

    /**
     * Getter for the <a href="#height">height</a> property.
     *
     * @return Value of the <a href="#height">height</a>
     *         property.
     */
    int getHeight();

}
