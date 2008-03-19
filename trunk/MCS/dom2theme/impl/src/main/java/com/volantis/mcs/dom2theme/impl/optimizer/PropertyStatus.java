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

package com.volantis.mcs.dom2theme.impl.optimizer;

/**
 * A type safe enumeration of the  different ways that a device will treat a
 * specific property value.
 */
public class PropertyStatus {

    /**
     * The property can be cleared, e.g. because it is the same as the known
     * value of the device.
     */
    public static final PropertyStatus CLEARABLE = new PropertyStatus(
            "CLEARABLE", false, false);

    /**
     * The property is required if rendered as a shorthand but not if
     * rendered as an individual property.
     */
    public static final PropertyStatus REQUIRED_FOR_SHORTHAND =
            new PropertyStatus("REQUIRED FOR SHORTHAND", true, false);

    /**
     * The property is required if rendered as an individual property but not
     * if rendered as a shorthand.
     */
    public static final PropertyStatus REQUIRED_FOR_INDIVIDUAL =
            new PropertyStatus("REQUIRED FOR INDIVIDUAL", false, true);

    /**
     * The property is required because clearing it would result in a change to
     * the style of the page.
     */
    public static final PropertyStatus REQUIRED = new PropertyStatus(
            "REQUIRED", true, true);

    /**
     * The name of the constant.
     */
    private final String name;

    /**
     * True if the property is required for the shorthand and false otherwise.
     */
    private final boolean requiredForShorthand;

    /**
     * True if the property is required for individual use, false otherwise.
     */
    private final boolean requiredForIndividual;

    /**
     * Initialise.
     *
     * @param name                  The name of the constant.
     * @param requiredForShorthand  Indicates whether the property is required
     *                              for use within a shorthand.
     * @param requiredForIndividual Indicates whether the property is required
     *                              for use as an individual property.
     */
    private PropertyStatus(
            String name,
            boolean requiredForShorthand,
            boolean requiredForIndividual) {

        this.name = name;
        this.requiredForShorthand = requiredForShorthand;
        this.requiredForIndividual = requiredForIndividual;
    }

    // Javadoc inherited.
    public String toString() {
        return name;
    }

    /**
     * Check if the property is required for a shorthand.
     *
     * @return True if it is, false otherwise.
     */
    public boolean isRequiredForShorthand() {
        return requiredForShorthand;
    }

    /**
     * Check if the property is required for an individual property.
     *
     * @return True if it is, false otherwise.
     */
    public boolean isRequiredForIndividual() {
        return requiredForIndividual;
    }
}
