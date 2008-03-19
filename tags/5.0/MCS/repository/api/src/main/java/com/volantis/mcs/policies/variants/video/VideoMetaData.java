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
package com.volantis.mcs.policies.variants.video;

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaData;

/**
 * The {@link MetaData} for {@link VariantType#VIDEO} variants.
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
 * <tr id="videoEncoding">
 * <td align="right" valign="top" width="1%"><b>video&nbsp;encoding</b></td>
 * <td>the encoding of the associated video resource.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see VideoMetaDataBuilder
 * @since 3.5.1
 */
public interface VideoMetaData
        extends PixelDimensionsMetaData {

    /**
     * Get a new builder instance for {@link VideoMetaData}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link VideoMetaDataBuilder#getVideoMetaData()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    VideoMetaDataBuilder getVideoMetaDataBuilder();

    /**
     * Getter for the <a href="#videoEncoding">video encoding</a> property.
     *
     * @return Value of the <a href="#videoEncoding">video encoding</a>
     *         property.
     */
    VideoEncoding getVideoEncoding();
}
