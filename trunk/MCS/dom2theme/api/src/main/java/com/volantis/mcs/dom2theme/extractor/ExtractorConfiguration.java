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

package com.volantis.mcs.dom2theme.extractor;

import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Contains configuration needed by the CSS extractor.
 */
public interface ExtractorConfiguration {

    /**
     * The details of the properties supported by the target device.
     *
     * @return The details of the properties supported by the target device.
     */
    PropertyDetailsSet getPropertyDetailsSet();

    /**
     * The set of shorthands supported by the target device.
     *
     * @return The set of shorthands supported by the target device.
     */
    ShorthandSet getSupportedShorthands();

    /**
     * A compiled representation of the style sheet used by the device.
     *
     * @return A compiled representation of the style sheet used by the device.
     */
    CompiledStyleSheet getDeviceStyleSheet();
}
