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

import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.metadata.MetaData;
import com.volantis.mcs.policies.variants.metadata.PixelDimensionsMetaData;

/**
 * The {@link MetaData} for {@link VariantType#IMAGE} variants.
 *
 * <p>This type of {@link MetaData} is usable with {@link VariantType#IMAGE}
 * variants.</p>
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
 * <tr id="imageEncoding">
 * <td align="right" valign="top" width="1%"><b>image&nbsp;encoding</b></td>
 * <td>the encoding of the associated image resource.</td>
 * </tr>
 *
 * <tr id="pixelDepth">
 * <td align="right" valign="top" width="1%"><b>pixel&nbsp;depth</b></td>
 * <td>the pixel depth of the image resource.</td>
 * </tr>
 *
 * <tr id="rendering">
 * <td align="right" valign="top" width="1%"><b>rendering</b></td>
 * <td>the rendering of the image, may not be null and defaults to
 * {@link ImageRendering#COLOR}.</td>
 * </tr>
 *
 * <tr id="conversionMode">
 * <td align="right" valign="top" width="1%"><b>conversion&nbsp;mode</b></td>
 * <td>the conversion mode for the image, may not be null and defaults to
 * {@link ImageConversionMode#NEVER_CONVERT}.</td>
 * </tr>
 *
 * <tr id="preserveLeft">
 * <td align="right" valign="top" width="1%"><b>preserve left</b></td>
 * <td>the left boundary that should be preserved if this image is
 * converted. The default value is -1.</td>
 * </tr>
 *
 * <tr id="preserveRight">
 * <td align="right" valign="top" width="1%"><b>preserve right</b></td>
 * <td>the right boundary that should be preserved if this image is
 * converted. The default value is -1.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @mock.generate
 * @see ImageMetaDataBuilder
 * @since 3.5.1
 */
public interface ImageMetaData
        extends PixelDimensionsMetaData {

    /**
     * Get a new builder instance for {@link ImageMetaData}.
     *
     * <p>The returned builder has been initialised with the values of this
     * object and will return this object from its
     * {@link ImageMetaDataBuilder#getImageMetaData()} until its state is
     * changed.</p>
     *
     * @return A new builder instance.
     */
    ImageMetaDataBuilder getImageMetaDataBuilder();

    /**
     * Getter for the <a href="#imageEncoding">image encoding</a> property.
     *
     * @return Value of the <a href="#imageEncoding">image encoding</a>
     *         property.
     */
    ImageEncoding getImageEncoding();

    /**
     * Getter for the <a href="#pixelDepth">pixel depth</a> property.
     *
     * @return Value of the <a href="#pixelDepth">pixel depth</a>
     *         property.
     */
    int getPixelDepth();

    /**
     * Getter for the <a href="#rendering">rendering</a> property.
     * @return Value of the <a href="#rendering">rendering</a>
     * property.
     */
    ImageRendering getRendering();

    /**
     * Getter for the <a href="#conversionMode">conversion mode</a> property.
     * @return Value of the <a href="#conversionMode">conversion mode</a>
     * property.
     */
    ImageConversionMode getConversionMode();

    /**
     * Getter for the <a href="#preserveLeft">preserve left</a> property.
     * @return Value of the <a href="#preserveLeft">preserve left</a>
     * property.
     */
    int getPreserveLeft();

    /**
     * Getter for the <a href="#preserveRight">preserve right</a> property.
     * @return Value of the <a href="#preserveRight">preserve right</a>
     * property.
     */
    int getPreserveRight();
}
