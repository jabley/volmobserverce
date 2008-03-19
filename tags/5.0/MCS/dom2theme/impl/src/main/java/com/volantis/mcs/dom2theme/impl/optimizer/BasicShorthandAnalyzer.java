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

import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.PropertyValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Analyses a set of properties that are handled by a shorthand to see whether
 * they can be represented using a shorthand, or individual properties.
 */
public class BasicShorthandAnalyzer
        extends AbstractShorthandAnalyzer {

    /**
     * A map from a {@link StyleProperty} to a {@link StyleValue} that should
     * be used if the property is required but currently has a value of
     * {@link OptimizerHelper#ANY}.
     */
    private static final Map INITIAL_VALUE_OVERRIDES;
    static {
        Map map = new HashMap();
        map.put(StylePropertyDetails.BORDER_LEFT_COLOR, StyleColorNames.RED);
        map.put(StylePropertyDetails.BORDER_RIGHT_COLOR, StyleColorNames.RED);
        map.put(StylePropertyDetails.BORDER_TOP_COLOR, StyleColorNames.RED);
        map.put(StylePropertyDetails.BORDER_BOTTOM_COLOR, StyleColorNames.RED);
        INITIAL_VALUE_OVERRIDES = map;
    }

    /**
     * The group of {@link StyleProperty}s that make up the shorthand being
     * analysed.
     */
    protected final StyleProperty[] group;

    /**
     * The property status checker.
     */
    private final PropertyClearerChecker checker;

    /**
     * The shorthand being analysed.
     */
    protected final StyleShorthand shorthand;

    /**
     * Indicates whether the shorthand is supported.
     *
     * <p>Even if the shorthand is not supported this still needs to perform
     * some analysis as otherwise it will
     */
    private final boolean shorthandSupported;

    /**
     * A count of the number of properties used by this shorthand.
     */
    private final int count;

    /**
     * A bit mask that selects all properties.
     */
    private int all;

    /**
     * A bit set indicating which properties need to have an important priority.
     */
    private int important;

    /**
     * A count of the number of properties that have been set to their initial
     * value.
     */
    private int initialCount;

    /**
     * A bit set indicating which properties are set.
     */
    private int set;

    /**
     * A bit set indicating the properties required for shorthand.
     */
    protected int requiredForShorthand;

    /**
     * A bit set indicating the individual properties required.
     */
    private int requiredForIndividual;

    /**
     * The set of properties that have been analysed.
     */
    private StyleValues inputValues;

    /**
     * The priority for the resulting shorthand, this is the highest priority
     * needed to ensure that the individual properties have an effect on the
     * target device.
     */
    private Priority shorthandPriority;

    /**
     * Indicates whether the shorthand can be used.
     */
    private boolean canUseShorthand;

    /**
     * Initialise.
     *
     * @param shorthand The shorthand to analyse.
     * @param checker Checks status of values to see how they will be treated on
     * the device.
     * @param supportedShorthands The set of supported shorthands.
     */
    public BasicShorthandAnalyzer(StyleShorthand shorthand,
            PropertyClearerChecker checker, ShorthandSet supportedShorthands) {
        this(shorthand, shorthand.getStandardProperties(), checker,
                supportedShorthands);
    }

    /**
     * Initialise.
     *
     * @param shorthand The shorthand to analyse.
     * @param checker Checks status of values to see how they will be treated on
     * the device.
     * @param supportedShorthands The set of supported shorthands.
     */
    public BasicShorthandAnalyzer(
            StyleShorthand shorthand, StyleProperty[] group,
            PropertyClearerChecker checker, ShorthandSet supportedShorthands) {
        super(shorthand.getStandardProperties());

        if (shorthand == null) {
            throw new IllegalArgumentException("group cannot be null");
        }
        if (checker == null) {
            throw new IllegalArgumentException("checker cannot be null");
        }

        this.shorthand = shorthand;
        this.shorthandSupported = supportedShorthands.contains(shorthand);
        this.group = group;
        this.checker = checker;
        this.count = group.length;
    }

    // Javadoc inherited.
    public void analyze(
            TargetEntity target, PropertyValues inputValues,
            DeviceValues deviceValues) {

        this.inputValues = inputValues;
        important = 0;
        set = 0;
        initialCount = 0;
        all = 0;
        requiredForShorthand = 0;
        requiredForIndividual = 0;

        for (int i = 0; i < count; i++) {
            StyleProperty property = group[i];
            int bit = 1 << i;

            all |= bit;

            StyleValue styleValue = inputValues.getStyleValue(property);
            if (styleValue == null) {
                // The property was not set so there is nothing to do, this
                // will probably mean that the shorthand cannot be used.
            } else {

                StyleValue deviceValue = deviceValues.getStyleValue(property);

                // The property is set.
                set |= bit;

                // Check the status of the value to see what can be done
                // with it.
                PropertyStatus status = checker.checkStatus(property,
                        styleValue, StatusUsage.SHORTHAND,
                        inputValues, deviceValue);

                if (status.isRequiredForShorthand()) {
                    // The property is required for shorthands as it is not
                    // set to its initial value.
                    requiredForShorthand |= bit;
                } else {
                    // The property is not inherited and has the same value
                    // as the value that the device would use if it was not
                    // specified in the output so this can be cleared.
                    initialCount += 1;
                }

                if (status.isRequiredForIndividual()) {
                    // The property is required for individual properties.
                    requiredForIndividual |= bit;
                }

                Priority devicePriority = deviceValues.getPriority(property);
                if (devicePriority.isGreaterThan(Priority.NORMAL)) {
                    important |= bit;
                }
            }
        }

        // If the styles are for a pseudo class then all the properties that
        // are set must be set by the resulting CSS that is generated. A
        // shorthand will by definition set all the properties but all the
        // individual properties must be written out so make sure that all the
        // properties that have been set are required.
        if (target == TargetEntity.PSEUDO_CLASS) {
            requiredForIndividual = set;
        }

        // The shorthand priority is important if any of the properties are
        // important, otherwise it is normal.
        shorthandPriority = (important == 0) ? Priority.NORMAL :
                Priority.IMPORTANT;

        // A shorthand can only be used if all the following conditions hold:
        // 1) it is supported.
        // 2) all the properties are set.
        // 3) at least one of the properties is required.
        //
        // If these conditions are not met then individual properties have to
        // be used.
        canUseShorthand = shorthandSupported && determineIfCanUseShorthand();
    }

    /**
     * Determine if the shorthand can be used.
     *
     * @return True if all the properties are set and at least one property is
     * required for the shorthand.
     */
    protected boolean determineIfCanUseShorthand() {
        return (allSet() && requiredForShorthand != 0);
    }

    /**
     * Get the priority that must be used for the shorthand.
     *
     * <p>This is the highest priority needed by any of the required
     * properties.</p>
     *
     * @return The priority for the shorthand.
     */
    public Priority getShorthandPriority() {
        return shorthandPriority;
    }

    /**
     * Checks to see whether all the properties have been set.
     *
     * @return True if all the properties have been set, false otherwise.
     */
    private boolean allSet() {
        return set == all;
    }

    /**
     * Get the count of the properties that match their initial value.
     *
     * <p>This should only be used internally within this class and its test
     * case and in fact the latter should be moved into the same package as
     * this so that this can be marked as package private.</p>
     *
     * @return The count of the properties that match their initial value.
     */
    public int getInitialCount() {
        return initialCount;
    }

    /**
     * Indicates whether the property at the specified index within the group
     * is important.
     *
     * @param index The index of the property within the group.
     * @return True if the property is important, false otherwise.
     */
    private boolean isImportant(int index) {
        return (important & (1 << index)) != 0;
    }

    // Javadoc inherited.
    public boolean canUseShorthand() {
        return canUseShorthand;
    }

    // Javadoc inherited.
    public boolean allClearable() {
        return initialCount == count;
    }

    // Javadoc inherited.
    public int getIndividualCost(MutableStylePropertySet required) {
        int cost = 0;

        for (int index = 0; index < group.length; index++) {
            StyleProperty property = group[index];
            if (required.contains(property)) {
                // Calculate the cost of the property and it's concrete value.
                StyleValue value = inputValues.getStyleValue(property);
                value = getConcreteValue(index, value);
                cost += property.getName().length();
                cost += 1; // The ':'.
                cost += value.getStandardCost();
                if (isImportant(index)) {
                    cost += Priority.IMPORTANT.getStandardCost();
                }
            }
        }
        return cost;
    }

    // Javadoc inherited.
    public void copyIndividualValues(
            MutableStyleProperties outputValues,
            MutableStylePropertySet required) {

        for (int index = 0; index < group.length; index++) {
            StyleProperty property = group[index];
            if (required.contains(property)) {
                copyValue(property, outputValues, index);
            }
        }
    }


    /**
     * Update the individual properties as the shorthand cannot be used.
     *
     * <p>Clear all those property that are set to #ANY.</p>
     * <p>Update the priority of any properties that need to be important.</p>
     *
     * @param outputValues The properties to update.
     */
    public void updateProperties(MutableStyleProperties outputValues) {
        for (int index = 0; index < group.length; index++) {
            StyleProperty property = group[index];
            int bit = 1 << index;
            if ((requiredForIndividual & bit) != 0) {
                copyValue(property, outputValues, index);
            }
        }
    }

    /**
     * Copy the value from the input to the output making important if
     * necessary.
     *
     * @param property     The property to copy.
     * @param outputValues The output values.
     * @param index        The index of the property, used to determine whether
     *                     it is important.
     */
    private void copyValue(
            StyleProperty property, MutableStyleProperties outputValues,
            int index) {
        StyleValue styleValue = inputValues.getStyleValue(property);
        if (styleValue != null) {
            int bit = 1 << index;
            boolean important = (this.important & bit) != 0;
            outputValues.setPropertyValue(
                ThemeFactory.getDefaultInstance().createPropertyValue(property,
                    getConcreteValue(index, styleValue),
                    important ? Priority.IMPORTANT : Priority.NORMAL));
        }
    }

    // Javadoc inherited.
    public void addRequired(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        for (int index = 0; index < group.length; index++) {
            StyleProperty property = group[index];
            int bit = 1 << index;
            if ((this.requiredForIndividual & bit) != 0) {
                requiredForIndividual.add(property);
            }
            if ((this.requiredForShorthand & bit) != 0) {
                requiredForShorthand.add(property);
            }
        }
    }

    /**
     * Create the shorthand value.
     * <p/>
     * <p>This should only ever be called if the shorthand can be used.</p>
     *
     * @return The newly create shorthand value.
     * @param values
     * @param priority
     */
    protected ShorthandValue createShorthandValue(
            StyleValue[] values, Priority priority) {
        return new ShorthandValue(shorthand, values, priority);
    }

    /**
     * Get the values that are used by the shorthand.
     * <p/>
     * <p>This should only ever be called if the shorthand can be used.</p>
     *
     * @return The values that are used by the shorthand.
     * @param requiredForIndividual
     * @param requiredForShorthand
     */
    protected StyleValue[] getShorthandValues(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        // If any of them are set to their initial value then ignore them.
        int count = group.length;
        StyleValue[] values = new StyleValue[count];
        for (int index = 0; index < group.length; index++) {
            StyleProperty property = group[index];
            if (isRequiredShorthandProperty(index)) {
                final StyleValue concreteValue = getConcreteValue(index,
                        inputValues.getStyleValue(property));
                values[index] = concreteValue;
            }

            requiredForShorthand.remove(property);
            requiredForIndividual.remove(property);
        }
        return values;
    }

    /**
     * Check to see whether a property is required for shorthand usage.
     *
     * @param index The index of the property.
     * @return True if it is required, false otherwise.
     */
    private boolean isRequiredShorthandProperty(int index) {
        int bit = 1 << index;
        return (requiredForShorthand & bit) != 0;
    }

    // Javadoc inherited.
    public int getShorthandCost(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        int cost = 0;

        cost += shorthand.getName().length();
        cost += 1; // The ':'.

        cost += getShorthandValuesCost(requiredForIndividual,
                requiredForShorthand);

        cost += 1; // The ';'.

        return cost;
    }

    /**
     * Get the cost (in characters) of the values when used in a shorthand.
     *
     * @return The cost in characters of the shorthand.
     * @param requiredForIndividual
     * @param requiredForShorthand
     */
    protected int getShorthandValuesCost(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {
        int cost = 0;
        // If any of them are set to their initial value then ignore them.
        int separatorCost = 0;
        for (int index = 0; index < group.length; index++) {
            // Only values that can not be treated as initial values affect the
            // cost of the shorthand.
            StyleProperty property = group[index];
            if (isRequiredShorthandProperty(index)) {
                StyleValue value = getConcreteValue(index,
                        inputValues.getStyleValue(property));
                if (value != null) {
                    cost += separatorCost;
                    cost += value.getStandardCost();
                    separatorCost = 1; // The ' '.
                }
            }

            requiredForIndividual.remove(property);
            requiredForShorthand.remove(property);
        }

        cost += shorthandPriority.getStandardCost();

        return cost;
    }

    // Javadoc inherited.
    public void updateShorthand(
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        StyleValue[] values = getShorthandValues(requiredForIndividual,
                requiredForShorthand);
        Priority priority = getShorthandPriority();
        ShorthandValue shorthandValue = createShorthandValue(values, priority);

        outputValues.setShorthandValue(shorthandValue);
    }

    /**
     * Calculate and return the concrete value to use for the property's
     * logical value.
     * <p/>
     * The logical value may be a normal value or the special value {@link
     * BasicShorthandAnalyzer.ANY}. This value matches any value and when being
     * costed it must be translated to a normal value obtained from {@link
     * BasicShorthandAnalyzer#getAnyConcreteValue}.
     *
     * @param index the property index
     * @param logicalValue the logical property index value, may include
     * instances of {@link OptimizerHelper#ANY}.
     * @return the concrete property value, may not include instances of
     *         {@link OptimizerHelper#ANY}. Note this may be null if property
     *         is not required
     */
    protected StyleValue getConcreteValue(int index, StyleValue logicalValue) {

        StyleValue concreteValue;

        if (logicalValue == null) {
            throw new IllegalArgumentException("logicalValue cannot be null");
        }

        // If this value is the special logical value which matches any value...
        if (logicalValue == OptimizerHelper.ANY) {
            // Then we need to translate it into a normal value.
            concreteValue = getRequiredInitialValue(index);
        } else {
            // If not, the concrete value is the normal value.
            concreteValue = logicalValue;
        }

        return concreteValue;

    }
    
    /**
     * Get a required initial value for the property value.
     *
     * <p>A value is required so if there is no override and the initial value
     * is null then fail.</p>
     *
     * @param index The index of the property within the group.
     * @return The initial value, will not be null.
     */
    protected StyleValue getRequiredInitialValue(int index) {
        StyleValue result;

        // Then we definitely can't use null or it would upset the
        // styling, so we instead use the initial value of the property.
        final StyleProperty property = group[index];
        result = (StyleValue) INITIAL_VALUE_OVERRIDES.get(property);
        if (result == null) {
            result = property.getStandardDetails().getInitialValue();
            // property is required so it must not be null
            if (result == null) {
                throw new IllegalStateException(
                        "initialValue must not be null");
            }
        }

        return result;
    }
}
