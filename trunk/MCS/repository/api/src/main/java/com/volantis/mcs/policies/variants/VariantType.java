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
package com.volantis.mcs.policies.variants;

/**
 * Type safe enumeration of the different variant types.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @since 3.5.1
 */
public final class VariantType {

    /**
     * Type of an audio {@link Variant}.
     */
    public static final VariantType AUDIO = new VariantType("Audio");

    /**
     * Type of a chart {@link Variant}.
     */
    public static final VariantType CHART = new VariantType("Chart");

    /**
     * Type of a video {@link Variant}.
     */
    public static final VariantType VIDEO = new VariantType("Video");

    /**
     * Type of an image {@link Variant}.
     */
    public static final VariantType IMAGE = new VariantType("Image");

    /**
     * Type of a layout {@link Variant}.
     */
    public static final VariantType LAYOUT = new VariantType("Layout");

    /**
     * Type of a link {@link Variant}.
     */
    public static final VariantType LINK = new VariantType("Link");

    /**
     * Type of a null {@link Variant}.
     */
    public static final VariantType NULL = new VariantType("Null");

    /**
     * Type of a script {@link Variant}.
     */
    public static final VariantType SCRIPT = new VariantType("Script");

    /**
     * Type of a text {@link Variant}.
     */
    public static final VariantType TEXT = new VariantType("Text");

    /**
     * Type of a theme {@link Variant}.
     */
    public static final VariantType THEME = new VariantType("Theme");

    private final String name;

    private VariantType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
