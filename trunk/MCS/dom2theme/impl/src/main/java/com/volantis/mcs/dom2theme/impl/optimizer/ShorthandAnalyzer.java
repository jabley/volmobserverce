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

package com.volantis.mcs.dom2theme.impl.optimizer;

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.values.PropertyValues;

/**
 * Analyzer for properties relating to a shorthand.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 */
public interface ShorthandAnalyzer {

    /**
     * Analyse the properties for the shorthand.
     *
     * <p>This method must be called before any other method on this class.</p>
     *
     * @param target
     * @param inputValues  The set of properties that contain values to
     * @param deviceValues
     */
    void analyze(
            TargetEntity target, PropertyValues inputValues,
            DeviceValues deviceValues);

    /**
     * Indicates whether the properties associated with this are all clearable.
     *
     * @return True if the properties are all clearable, false otherwise.
     */
    boolean allClearable();

    /**
     * Get the cost of the shorthand, clearing any properties set by the
     * shorthand from the set of required properties for individual and
     * shorthands.
     *
     * <p>If the shorthand cannot be used then this returns 0.</p>
     *
     * @return The cost of the shorthand, or 0 if it could not be used.
     */
    int getShorthandCost(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand);

    /**
     * Indicates whether the shorthand can be used.
     *
     * @return True if the shorthand can be used false otherwise.
     */
    boolean canUseShorthand();

    /**
     * Update the shorthand in the properties.
     * <p/>
     * <p>If the shorthand property cannot be used then this does nothing.
     * Otherwise it updates the contained properties by adding the shorthand and
     * clearing out all the associated properties.</p>
     *
     * @param outputValues
     */
    void updateShorthand(MutableStyleProperties outputValues);

    /**
     * Update the shorthand in the properties.
     * <p/>
     * <p>If the shorthand property cannot be used then this does nothing.
     * Otherwise it updates the contained properties by adding the shorthand
     * and clearing out all the associated properties.</p>
     *
     * @param outputValues
     * @param requiredForIndividual
     * @param requiredForShorthand
     */
    void updateShorthand(
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand);

    /**
     * Add those properties that are required for individual properties to the
     * set.
     *
     * @param requiredForIndividual The set of required properties.
     * @param requiredForShorthand
     */
    void addRequired(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand);

    /**
     * Get the priority that should be used for the shorthand.
     *
     * <p>This will be the highest priority of all required properties in that
     * shorthand.</p>
     *
     * @return The priority of the shorthand.
     */
    Priority getShorthandPriority();

    /**
     * Get the cost of writing out the required properties as individual
     * properties.
     *
     * @param required The set of required properties.
     * @return The cost (in characters) of writing out the properties,
     *         including separators.
     */
    int getIndividualCost(MutableStylePropertySet required);

    /**
     * Clear all those property values that this analyser has determined are
     * not required.
     *
     * @param outputValues The properties to update.
     * @param required
     */
    void copyIndividualValues(
            MutableStyleProperties outputValues,
            MutableStylePropertySet required);

    /**
     * Remove the properties managed by this analyzer from the set.
     *
     * @param individualProperties The set of individual properties, i.e. those
     *                             that cannot be part of a shorthand.
     */
    void removeProperties(MutableStylePropertySet individualProperties);
}
