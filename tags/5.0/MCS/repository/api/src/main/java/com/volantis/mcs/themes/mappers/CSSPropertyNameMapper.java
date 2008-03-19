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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.themes.mappers;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.properties.DisplayKeywords;
import com.volantis.styling.properties.StyleProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for mapping from internal MCS property/shorthand names to
 * external CSS property/shorthand names.
 *
 * NB: If this becomes something that varies with CSSVersion it should
 * probably be generated from data in the same way. However at present
 * that would probably be overkill.
 */
public class CSSPropertyNameMapper {

    /**
     * Map of internal mcs property names to those used externally.
     */
    private static Map internal2ExternalProperties;

    /**
     * Map of internal mcs shorthand names to those used externally.
     */
    private static Map internal2ExternalShorthands;

    /**
     * Map of internal mcs keyword names to those used externally.
     */
    private static Map internal2ExternalKeywords;

    /**
     * Singleton instance of this mapper.
     */
    private static CSSPropertyNameMapper singleton;

    static {
        internal2ExternalProperties = new HashMap();
        internal2ExternalShorthands = new HashMap();
        internal2ExternalKeywords = new HashMap();

        internal2ExternalShorthands.put(StyleShorthands.MARQUEE.getName(),
                "-wap-marquee");

        internal2ExternalKeywords.put(DisplayKeywords.MCS_MARQUEE.getName(),
                "-wap-marquee");

        internal2ExternalProperties.put(
                StylePropertyDetails.MCS_MARQUEE_DIRECTION.getName(),
                "-wap-marquee-dir");
        internal2ExternalProperties.put(
                StylePropertyDetails.MCS_MARQUEE_REPETITION.getName(),
                "-wap-marquee-loop");
        internal2ExternalProperties.put(
                StylePropertyDetails.MCS_MARQUEE_SPEED.getName(),
                "-wap-marquee-speed");
        internal2ExternalProperties.put(
                StylePropertyDetails.MCS_MARQUEE_STYLE.getName(),
                "-wap-marquee-style");

        internal2ExternalProperties.put(
                StylePropertyDetails.MCS_INPUT_FORMAT.getName(),
                "-wap-input-format");
        
        singleton = new CSSPropertyNameMapper();
    }

    /**
     * Private constructor to enforce singleton pattern.
     */
    private CSSPropertyNameMapper() {
    }

    /**
     * Access the only instance of this mapper.
     * @return CSSPropertyNameMapper the single instance of this mapper
     */
    public static CSSPropertyNameMapper getDefaultInstance() {
        return singleton;
    }

    /**
     * Return the external string which is the external css representation of
     * this style property. May be the same as the internal representation if
     * no mapping is defined.
     *
     * @param property whose external string css representation to return
     * @return external css representation of this property
     */
    public String getExternalString(StyleProperty property) {
        return getExternalString(property.getName(), internal2ExternalProperties);
    }

    /**
     * Return the external string which is the external css representation of
     * this style shorthand. May be the same as the internal representation if
     * no mapping is defined.
     *
     * @param shorthand whose external string css representation to return
     * @return external css representation of this shorthand
     */
    public String getExternalString(StyleShorthand shorthand) {
        return getExternalString(shorthand.getName(), internal2ExternalShorthands);
    }

    /**
     * Return the external string which is the external css representation of
     * the {@link StyleKeyword} given. May be the same as the internal
     * representation if no mapping is defined.
     *
     * @param keyword   whose external string css representation should be
     *                  returned
     * @return external css representation of this style keyword
     */
    public String getExternalString(StyleKeyword keyword) {
        return getExternalString(keyword.getName(), internal2ExternalKeywords);
    }

    /**
     * Return the external string which is the external css representation of
     * the style property with the given name. May be the same as the internal
     * representation if no mapping is defined.
     *
     * @param propertyName whose external string css representation to return
     * @return external css representation of this property
     */
    public String getExternalPropertyName(String propertyName) {
        return getExternalString(propertyName, internal2ExternalProperties);
    }

    /**
     * Return the external string which is the external css representation of
     * the style property identified by the name given. May be the same as the
     * internal representation if no mapping is defined.
     *
     * @param internalName internal name of the style property whose external
     * string css representation should be returned
     * @return external css representation of this property
     */
    private String getExternalString(String internalName, Map map) {
        final String externalName = (String) map.get(internalName);
        return externalName != null? externalName: internalName;
    }
}
