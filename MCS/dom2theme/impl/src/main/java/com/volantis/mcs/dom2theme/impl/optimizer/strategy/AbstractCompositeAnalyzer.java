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

package com.volantis.mcs.dom2theme.impl.optimizer.strategy;

import com.volantis.mcs.dom2theme.impl.optimizer.AbstractShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.TargetEntity;
import com.volantis.mcs.themes.Priority;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.MutableStylePropertySetImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * The base for those analyzers that consist of a number of other analyzers.
 */
public abstract class AbstractCompositeAnalyzer
        extends AbstractShorthandAnalyzer {

    /**
     * The set of properties required for shorthand usage.
     */
    private final MutableStylePropertySet requiredForShorthand;

    /**
     * The set of properties required for individual usage.
     */
    private final MutableStylePropertySet requiredForIndividual;

    /**
     * The type of entity for which the properties belong.
     */
    private TargetEntity target;

    /**
     * The input values.
     */
    protected PropertyValues inputValues;

    /**
     * The priority that should be used by the single shorthand.
     *
     * <p>This is the greatest priority out of all the individual
     * properties.</p>
     */
    protected Priority shorthandPriority;

    /**
     * The device values.
     */
    private DeviceValues deviceValues;

    /**
     * Initialise.
     * @param properties
     */
    protected AbstractCompositeAnalyzer(StyleProperty[] properties) {
        super(properties);

        requiredForShorthand = new MutableStylePropertySetImpl();
        requiredForIndividual = new MutableStylePropertySetImpl();
    }

    // Javadoc inherited.
    public final void analyze(
            TargetEntity target, PropertyValues inputValues,
            DeviceValues deviceValues) {

        this.target = target;
        this.inputValues = inputValues;
        this.deviceValues = deviceValues;

        this.shorthandPriority = Priority.NORMAL;
        analyzeImpl();
    }

    /**
     * Analyze the properties.
     */
    protected abstract void analyzeImpl();

    /**
     * Invoke the set of analyzers.
     *
     * @param analyzers The analyzers to invoke.
     */
    protected void analyze(ShorthandAnalyzer[] analyzers) {

        for (int i = 0; i < analyzers.length; i++) {
            ShorthandAnalyzer analyzer = analyzers[i];
            analyzer.analyze(target, inputValues, deviceValues);

            Priority priority = analyzer.getShorthandPriority();
            if (priority.isGreaterThan(shorthandPriority)) {
                shorthandPriority = priority;
            }
        }
    }

    /**
     * Add the set of required properties to the supplied set.
     *
     * @param requiredForIndividual The set of required properties for
     *                              individual usage.
     * @param requiredForShorthand  The set of required properties for
     *                              shorthand usage.
     */
    public void addRequired(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {
        requiredForIndividual.add(this.requiredForIndividual);
        requiredForShorthand.add(this.requiredForShorthand);
    }

    /**
     * Update the required sets of properties from the composed analyzers.
     *
     * @param analyzers The analyzers that must update the properties.
     */
    protected void updateRequiredSets(ShorthandAnalyzer[] analyzers) {
        requiredForShorthand.clear();
        requiredForIndividual.clear();
        for (int i = 0; i < analyzers.length; i++) {
            ShorthandAnalyzer analyzer = analyzers[i];
            analyzer.addRequired(requiredForIndividual, requiredForShorthand);
        }
    }
}
