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
package com.volantis.mcs.protocols.capability;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.styling.properties.StyleProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides supporting operations of a device for CSS properties.
 * Will be extended if needed.
 */
public class DeviceCSSCapability {

    /**
     * the collection of supported keywords per style property value.
     * If value for a key is missing it is assumed keyword is fully 
     * supported.
     */
    private final Map supportedKeywords = new HashMap();

    /**
     * add a supported style keyword for a style property
     * @param styleProperty property
     * @param styleKeyword keyword
     * @param supportLevel support level
     */
    void addSupportedStyleKeyword(StyleProperty styleProperty,
                                  StyleKeyword styleKeyword, 
                                  CapabilitySupportLevel supportLevel) {
        if (styleProperty != null && styleKeyword != null) {
            getOrCreateStyleKeywords(styleProperty).put(
                    styleKeyword, supportLevel);
        }
    }

    private Map getStyleKeywords(StyleProperty styleProperty) {
        return (Map) supportedKeywords.get(styleProperty);
    }

    private Map getOrCreateStyleKeywords(StyleProperty styleProperty) {
        Map keywords = getStyleKeywords(styleProperty);
        if (keywords == null) {
            supportedKeywords.put(styleProperty, keywords = new HashMap());
        }
        return keywords;
    }

    /**
     * Get the support level for a given keyword of StyleProperty 
     * for this element by a device
     * @param styleProperty property
     * @param styleKeyword keyword
     * @return support level for a keyword
     */
    public CapabilitySupportLevel getKeywordSupportType(
            StyleProperty styleProperty, StyleKeyword styleKeyword) {
        CapabilitySupportLevel supportType = CapabilitySupportLevel.FULL;
        Map keywords = getStyleKeywords(styleProperty);
        if (keywords != null) {
            CapabilitySupportLevel specificSupportType =
                    (CapabilitySupportLevel) keywords.get(styleKeyword);
            if (specificSupportType != null) {
                supportType = specificSupportType;
            }
        }
        return supportType;
    }
}
