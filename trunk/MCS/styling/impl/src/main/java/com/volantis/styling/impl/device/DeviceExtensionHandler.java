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

package com.volantis.styling.impl.device;

import com.volantis.mcs.css.parser.ExtensionHandler;
import com.volantis.mcs.themes.ExtensionPriority;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.device.DeviceValues;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Supports the extensions to the CSS syntax needed to represent information
 * for the CSS extractor in dom2theme.
 */
public class DeviceExtensionHandler
        implements ExtensionHandler {

    /**
     * The default instance.
     */
    private static final ExtensionHandler DEFAULT_INSTANCE =
            new DeviceExtensionHandler();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static ExtensionHandler getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     *  A map from token to extension priority.
     */
    private static final Map TOKEN_2_PRIORITY;

    /**
     * The descriptions of the tokens used for the priorities.
     */
    private static final List PRIORITY_TOKENS;

    /**
     *  A map from token to extension style value.
     */
    private static final Map TOKEN_2_VALUE;

    /**
     * The descriptions of the tokens used for the values.
     */
    private static final List VALUE_TOKENS;

    static {
        Map map;

        map = new HashMap();
        map.put("<default>", DeviceValues.DEFAULT);
        map.put("<not-set>", DeviceValues.NOT_SET);
        map.put("<unknown>", DeviceValues.UNKNOWN);
        TOKEN_2_VALUE = map;

        VALUE_TOKENS = Arrays.asList(new String[] {
            "\"<default>\"",
            "\"<not-set>\"",
            "\"<unknown>\"",
        });

        map = new HashMap();
        map.put("-medium", ExtensionPriority.MEDIUM);
        TOKEN_2_PRIORITY = map;

        PRIORITY_TOKENS = Arrays.asList(new String[] {
            "\"-medium\"",
        });
    }

    // Javadoc inherited.
    public StyleValue customValue(String token) {
        return (StyleValue) TOKEN_2_VALUE.get(token);
    }

    // Javadoc inherited.
    public Priority customPriority(String token) {
        return (Priority) TOKEN_2_PRIORITY.get(token);
    }

    // Javadoc inherited.
    public Collection getCustomPriorities() {
        return PRIORITY_TOKENS;
    }

    // Javadoc inherited.
    public Collection getCustomValues() {
        return VALUE_TOKENS;
    }
}
