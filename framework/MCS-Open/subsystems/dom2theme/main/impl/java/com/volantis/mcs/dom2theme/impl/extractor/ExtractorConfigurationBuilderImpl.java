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
import com.volantis.mcs.dom2theme.extractor.ExtractorConfigurationBuilder;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Implementation of {@link ExtractorConfigurationBuilder}.
 */
public class ExtractorConfigurationBuilderImpl
        implements ExtractorConfigurationBuilder {

    /**
     * The details of the properties supported by the device.
     */
    private PropertyDetailsSet detailsSet;

    /**
     * The set of supported shorthands.
     */
    private ShorthandSet supportedShorthands;

    /**
     * The compiled device style sheet.
     */
    private CompiledStyleSheet deviceStyleSheet;

    /**
     * Get the details of the properties supported by the device.
     *
     * @return The details of the properties supported by the device.
     */
    public PropertyDetailsSet getDetailsSet() {
        return detailsSet;
    }

    // Javadoc inherited.
    public void setDetailsSet(PropertyDetailsSet detailsSet) {
        this.detailsSet = detailsSet;
    }

    /**
     * Get the set of supported shorthands.
     *
     * @return The set of supported shorthands.
     */
    public ShorthandSet getSupportedShorthands() {
        return supportedShorthands;
    }

    // Javadoc inherited.
    public void setSupportedShorthands(ShorthandSet shorthands) {
        this.supportedShorthands = shorthands;
    }

    /**
     * Get the compiled device style sheet.
     *
     * @return The compiled device style sheet.
     */
    public CompiledStyleSheet getDeviceStyleSheet() {
        return deviceStyleSheet;
    }

    // Javadoc inherited.
    public void setDeviceStyleSheet(CompiledStyleSheet deviceStyleSheet) {
        this.deviceStyleSheet = deviceStyleSheet;
    }

    // Javadoc inherited.
    public ExtractorConfiguration buildConfiguration() {
        if (detailsSet == null) {
            throw new IllegalStateException("detailsSet cannot be null");
        }
        if (deviceStyleSheet == null) {
            throw new IllegalStateException("deviceStyleSheet cannot be null");
        }
        if (supportedShorthands == null) {
            throw new IllegalStateException(
                    "supportedShorthands cannot be null");
        }

        return new ExtractorConfigurationImpl(this);
    }
}
