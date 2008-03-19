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

import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

/**
 * Performs additional analysis specific to those properties that specify styles
 * for edges of boxes.
 */
public class EdgeCharacteristicAnalyzer
        extends BasicShorthandAnalyzer {

    /**
     * The values for the edges.
     */
    private final StyleValue[] edges;

    /**
     * The count of the number of actual values in the above array.
     */
    private int edgeCount;

    /**
     * The concrete value to use when we encounter the special
     * {@link OptimizerHelper#ANY} style value which matches any value and it
     * is required in the output.
     */
    private final StyleValue anyValue;

    /**
     * Initialise.
     *
     * @param shorthand           The shorthand being analysed.
     * @param checker             The checker.
     * @param anyValue            The value to use for required ANY values.
     * @param supportedShorthands The set of supported shorthands.
     */
    public EdgeCharacteristicAnalyzer(
            StyleShorthand shorthand,
            PropertyClearerChecker checker,
            StyleValue anyValue,
            ShorthandSet supportedShorthands) {

        super(shorthand, checker, supportedShorthands);
        if (anyValue == null) {
            throw new IllegalArgumentException("anyValue must not be null");
        }
        this.edges = new StyleValue[group.length];
        this.anyValue = anyValue;
    }

    // Javadoc inherited.
    public void analyze(
            TargetEntity target, PropertyValues inputValues,
            DeviceValues deviceValues) {

        super.analyze(target, inputValues, deviceValues);
        if (canUseShorthand()) {

            // All the priorities are equal and all the properties are set
            // so it is possible that the values on all edges may be equal.


            // Now check to see how many properties are needed.
            StyleValue top = getEdgeValue(inputValues, PropertyGroups.TOP);
            StyleValue right = getEdgeValue(inputValues, PropertyGroups.RIGHT);
            StyleValue bottom = getEdgeValue(inputValues,
                    PropertyGroups.BOTTOM);
            StyleValue left = getEdgeValue(inputValues, PropertyGroups.LEFT);

            // If there is only one value, it applies to all sides. If there are
            // two values, the top and bottom borders are set to the first value
            // and the right and left are set to the second. If there are three
            // values, the top is set to the first value, the left and right are
            // set to the second, and the bottom is set to the third. If there are
            // four values, they apply to the top, right, bottom, and left,
            // respectively.

            StyleValue commonTopBottom = commonValue(top, bottom);
            StyleValue commonRightLeft = commonValue(left, right);

            if (commonRightLeft != null) {
                if (commonTopBottom != null) {
                    StyleValue commonTopRight =
                            commonValue(commonTopBottom, commonRightLeft);
                    if (commonTopRight != null) {
                        // All the values are the same so only write out one value.
                        edgeCount = 1;
                        edges[0] = getConcreteValue(PropertyGroups.TOP,
                                commonTopRight);
                    } else {
                        // Top and bottom are equal and left and right are equal
                        // so write out two values.
                        edgeCount = 2;
                        edges[0] = getConcreteValue(PropertyGroups.TOP,
                                commonTopBottom);
                        edges[1] = getConcreteValue(PropertyGroups.RIGHT,
                                commonRightLeft);
                    }
                } else {
                    // Top and bottom are different but left and right are equal
                    // so right out three values.
                    edgeCount = 3;
                    edges[0] = getConcreteValue(PropertyGroups.TOP,
                            top);
                    edges[1] = getConcreteValue(PropertyGroups.RIGHT,
                            commonRightLeft);
                    edges[2] = getConcreteValue(PropertyGroups.BOTTOM,
                            bottom);
                }
            } else {
                // Left and right are different so need to write out all four
                // values.
                edgeCount = 4;
                edges[0] = getConcreteValue(PropertyGroups.TOP, top);
                edges[1] = getConcreteValue(PropertyGroups.RIGHT, right);
                edges[2] = getConcreteValue(PropertyGroups.BOTTOM, bottom);
                edges[3] = getConcreteValue(PropertyGroups.LEFT, left);
            }
        } else {
            // The shorthand is not usable.
            edgeCount = -1;
        }
    }


    /**
     * Gets ANY concrete value of the property, which is anyValue.
     *
     * <p>We can never clear values just because they are equal to initial
     * values, because the ordering is important.</p>
     */
    protected StyleValue getRequiredInitialValue(int edge) {
        return anyValue;
    }

    /**
     * Get the common value between the two values.
     *
     * <p>If either of them are ANY then the other is returned, otherwise the
     * first is returned if they are equal, otherwise they are different and
     * so null is returned.</p>
     *
     * @param v1 One value.
     * @param v2 Another value.
     * @return The common value, or null if they are different.
     */
    private StyleValue commonValue(StyleValue v1, StyleValue v2) {
        if (v1 == OptimizerHelper.ANY) {
            return v2;
        } else if (v2 == OptimizerHelper.ANY) {
            return v1;
        } else if (v1.equals(v2)) {
            return v1;
        } else {
            return null;
        }
    }

    /**
     * Get the value of the specified edge.
     *
     * @param properties The properties to read.
     * @param edge       The edge whose value is required.
     * @return The style value.
     */
    private StyleValue getEdgeValue(StyleValues properties, int edge) {
        return properties.getStyleValue(group[edge]);
    }

    /**
     * Get the common value across all the edges.
     *
     * @return The common value, or null if there is no single value common
     *         across them all.
     */
    public StyleValue getCommonValue() {
        if (edgeCount == 1) {
            return edges[0];
        } else {
            return null;
        }
    }

    // Javadoc inherited.
    protected StyleValue[] getShorthandValues(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        StyleValue[] values = new StyleValue[edgeCount];
        System.arraycopy(edges, 0, values, 0, edgeCount);

        clearProperties(requiredForIndividual, requiredForShorthand);

        return values;
    }

    // Javadoc inherited.
    protected int getShorthandValuesCost(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        int cost = 0;
        int separatorCost = 0;
        for (int i = 0; i < edgeCount; i++) {
            StyleValue edge = edges[i];
            cost += separatorCost;
            cost += edge.getStandardCost();
            separatorCost = 1; // The ' '.
        }

        clearProperties(requiredForIndividual, requiredForShorthand);

        return cost;
    }

    /**
     * Clear the properties from the required sets.
     *
     * @param requiredForIndividual The set of required properties for
     *                              individual usage.
     * @param requiredForShorthand  The set of required properties for
     */
    private void clearProperties(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {
        for (int i = 0; i < group.length; i++) {
            StyleProperty property = group[i];
            requiredForShorthand.remove(property);
            requiredForIndividual.remove(property);
        }
    }
}
