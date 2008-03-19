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

import com.volantis.mcs.policies.variants.audio.AudioMetaData;
import com.volantis.mcs.policies.variants.chart.ChartMetaData;
import com.volantis.mcs.policies.variants.image.ImageMetaData;
import com.volantis.mcs.policies.variants.script.ScriptMetaData;
import com.volantis.mcs.policies.variants.text.TextMetaData;
import com.volantis.mcs.policies.variants.video.VideoMetaData;


/**
 * Type safe enumeration of the different types of {@link MetaData}.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public final class MetaDataType {

    /**
     * The type of {@link AudioMetaData}.
     */
    public static final MetaDataType AUDIO =
            new MetaDataType("Audio");

    /**
     * The type of {@link ChartMetaData}.
     */
    public static final MetaDataType CHART =
            new MetaDataType("Chart");

    /**
     * The type of {@link VideoMetaData}.
     */
    public static final MetaDataType VIDEO =
            new MetaDataType("Video");

    /**
     * The type of {@link ImageMetaData}.
     */
    public static final MetaDataType IMAGE =
            new MetaDataType("Image");

    /**
     * The type of {@link ScriptMetaData}.
     */
    public static final MetaDataType SCRIPT =
            new MetaDataType("Script");

    /**
     * The type of {@link TextMetaData}.
     */
    public static final MetaDataType TEXT =
            new MetaDataType("Text");

    private final String name;

    private MetaDataType(String name) {
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
