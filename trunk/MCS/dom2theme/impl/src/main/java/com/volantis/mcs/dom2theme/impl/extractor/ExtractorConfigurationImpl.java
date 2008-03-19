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

package com.volantis.mcs.dom2theme.impl.extractor;

import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Implementation of {@link ExtractorConfiguration}.
 */
public class ExtractorConfigurationImpl
        implements ExtractorConfiguration {

    /**
     * The details of the properties supported by the device.
     */
    private final PropertyDetailsSet detailsSet;

    /**
     * The set of supported shorthands.
     */
    private final ShorthandSet supportedShorthands;

    /**
     * The compiled device style sheet.
     */
    private final CompiledStyleSheet deviceStyleSheet;

    /**
     * Initialise.
     *
     * @param builder The builder.
     */
    public ExtractorConfigurationImpl(
            ExtractorConfigurationBuilderImpl builder) {
        detailsSet = builder.getDetailsSet();
        supportedShorthands = builder.getSupportedShorthands();
        deviceStyleSheet = builder.getDeviceStyleSheet();
    }

    // Javadoc inherited.
    public PropertyDetailsSet getPropertyDetailsSet() {
        return detailsSet;
    }

    // Javadoc inherited.
    public ShorthandSet getSupportedShorthands() {
        return supportedShorthands;
    }

    // Javadoc inherited.
    public CompiledStyleSheet getDeviceStyleSheet() {
        return deviceStyleSheet;
    }
}
