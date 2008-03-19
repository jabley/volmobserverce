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

package com.volantis.mcs.dom2theme.impl.optimizer.border;

import com.volantis.mcs.dom2theme.impl.optimizer.BasicShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.EdgeCharacteristicAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerChecker;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.AbstractCompositeAnalyzer;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.values.LengthUnit;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.mcs.themes.values.StyleColorNames;
import com.volantis.styling.properties.MutableStylePropertySet;

/**
 * Analyzes all the border properties to see what the best (smallest size of
 * CSS) way is to represent them.
 */
public class BorderAnalyzer
        extends AbstractCompositeAnalyzer {

    /**
     * The index of color related information within the
     * {@link #CHARACTERISTIC_SHORTHANDS} and {@link #ANY_REQUIRED_VALUES}.
     */
    private static final int COLOR = 0;

    /**
     * The index of style related information within the
     * {@link #CHARACTERISTIC_SHORTHANDS} and {@link #ANY_REQUIRED_VALUES}.
     */
    private static final int STYLE = 1;

    /**
     * The index of width related information within the
     * {@link #CHARACTERISTIC_SHORTHANDS} and {@link #ANY_REQUIRED_VALUES}.
     */
    private static final int WIDTH = 2;

    /**
     * The number of characteristics.
     */
    public static final int CHARACTERISTIC_COUNT = 3;

    /**
     * The values to use if a required property has a value of ANY.
     *
     * <p>These are used in place of the ANY as that is not valid CSS and
     * therefore cannot be written out.</p>
     */
    private static final StyleValue[] ANY_REQUIRED_VALUES;

    /**
     * The shorthands related to characteristics of the border,
     * i.e. color, width and style.
     */
    private static final StyleShorthand[] CHARACTERISTIC_SHORTHANDS;

    /**
     * The shorthands related to edges of the border, i.e. top, bottom, left
     * and right.
     */
    private static final StyleShorthand[] EDGE_SHORTHANDS;

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    /**
     * Initialise static fields.
     */
    static {
        StyleShorthand[] shorthands;

        shorthands = new StyleShorthand[CHARACTERISTIC_COUNT];
        shorthands[COLOR] = StyleShorthands.BORDER_COLOR;
        shorthands[STYLE] = StyleShorthands.BORDER_STYLE;
        shorthands[WIDTH] = StyleShorthands.BORDER_WIDTH;
        CHARACTERISTIC_SHORTHANDS = shorthands;

        shorthands = new StyleShorthand[PropertyGroups.EDGE_COUNT];
        shorthands[PropertyGroups.TOP] = StyleShorthands.BORDER_TOP;
        shorthands[PropertyGroups.RIGHT] = StyleShorthands.BORDER_RIGHT;
        shorthands[PropertyGroups.BOTTOM] = StyleShorthands.BORDER_BOTTOM;
        shorthands[PropertyGroups.LEFT] = StyleShorthands.BORDER_LEFT;
        EDGE_SHORTHANDS = shorthands;

        StyleValue[] values;

        // Calculate the concrete values to be used when we find ANY values
        // that are required.
        //
        // NOTE: for border shorthands we need explicit concrete values because
        // the different shorthand forms have positional values. For example,
        // if we have
        //
        // border-top-color:white
        // border-left-color:(ANY)
        // border-right-color:(ANY)
        // border-bottom-color:black
        //
        // then
        //
        // border-color: white red black
        //
        // is used because ANY can be any value and there is no useful initial
        // value that is applicable so we choose red to in order to allow us to
        // use the shorter border-color form (note that ANY values never have
        // any effect on the client even if they are rendered, since in order
        // to have an ANY value here the related border value must include
        // -style:none or -width:0).
        values = new StyleValue[CHARACTERISTIC_COUNT];
        values[COLOR] = StyleColorNames.RED;
        values[STYLE] = BorderStyleKeywords.NONE;
        values[WIDTH] = STYLE_VALUE_FACTORY.getLength(null, 0, LengthUnit.PX);
        ANY_REQUIRED_VALUES = values;
    }

    /**
     * The analyzers for the characteristic shorthands.
     */
    private final EdgeCharacteristicAnalyzer[] characteristicAnalyzers;

    /**
     * The analyzers for the edge shorthands.
     */
    private final ShorthandAnalyzer[] edgeAnalyzers;

    /**
     * The array of characteristic values that will be used if the border
     * shorthand can be used.
     */
    private final StyleValue[] characteristics;

    /**
     * The count of the number of values in the {@link #characteristics} array.
     */
    private int characteristicCount;

    /**
     * Initialise.
     *
     * @param checker             The object to use to check the status of the
     *                            properties.
     * @param supportedShorthands The set of supported shorthands.
     */
    public BorderAnalyzer(
            PropertyClearerChecker checker,
            ShorthandSet supportedShorthands) {

        super(PropertyGroups.BORDER_PROPERTIES);

        int length;

        length = CHARACTERISTIC_SHORTHANDS.length;
        characteristicAnalyzers = new EdgeCharacteristicAnalyzer[length];
        for (int i = 0; i < length; i++) {
            StyleShorthand shorthand = CHARACTERISTIC_SHORTHANDS[i];
            characteristicAnalyzers[i] = new EdgeCharacteristicAnalyzer(
                    shorthand, checker, ANY_REQUIRED_VALUES[i],
                    supportedShorthands);
        }

        length = EDGE_SHORTHANDS.length;
        edgeAnalyzers = new ShorthandAnalyzer[length];
        for (int i = 0; i < length; i++) {
            StyleShorthand shorthand = EDGE_SHORTHANDS[i];
            edgeAnalyzers[i] = new BasicShorthandAnalyzer(shorthand, checker,
                    supportedShorthands);
        }

        characteristics = new StyleValue[CHARACTERISTIC_COUNT];
    }

    protected void analyzeImpl() {

        analyze(characteristicAnalyzers);
        analyze(edgeAnalyzers);

        // The single border property can only be used if colors, styles and
        // widths are either not required at all, or only require a single
        // value each.

        // Check all the characteristics.
        boolean canUseSingleBorder = true;

        characteristicCount = 0;
        for (int i = 0;
             i < characteristicAnalyzers.length && canUseSingleBorder; i++) {

            EdgeCharacteristicAnalyzer analyser = characteristicAnalyzers[i];
            StyleValue value = analyser.getCommonValue();
            if (value == null && !analyser.allClearable()) {
                canUseSingleBorder = false;
            } else {

                if (value != null) {
                    characteristics[characteristicCount] = value;
                    characteristicCount += 1;
                }
            }
        }

        if (!canUseSingleBorder) {
            characteristicCount = -1;
        }

        // Update the required sets.
        updateRequiredSets(characteristicAnalyzers);
    }

    // Javadoc inherited.
    public boolean allClearable() {
        return characteristicCount == 0;
    }

    // Javadoc inherited.
    public boolean canUseShorthand() {
        return characteristicCount > 0;
    }

    // Javadoc inherited.
    public int getShorthandCost(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        int cost = StyleShorthands.BORDER.getName().length();
        cost += 1; // The ':'.
        int separatorCost = 0;

        StyleValue[] values = characteristics;
        for (int i = 0; i < characteristicCount; i++) {
            StyleValue value = values[i];

            if (value != null) {
                cost += separatorCost;
                cost += value.getStandardCost();
                separatorCost = 1;
            }
        }

        return cost;
    }

    // Javadoc inherited.
    public void updateShorthand(
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        ShorthandValue shorthandValue = createShorthandValue();
        outputValues.setShorthandValue(shorthandValue);
    }

    /**
     * Create the single border shorthand value.
     *
     * <p>The returned style will have the highest priority of all the
     * individual properties.</p>
     *
     * @return The shorthand value.
     * @throws IllegalStateException If the shorthand is not usable.
     */
    private ShorthandValue createShorthandValue() {
        if (characteristicCount < 1) {
            throw new IllegalStateException("Shorthand is not usable");
        }

        StyleValue[] values = new StyleValue[characteristicCount];
        System.arraycopy(characteristics, 0, values, 0, characteristicCount);

        return new ShorthandValue(StyleShorthands.BORDER, values,
                shorthandPriority);
    }

    // Javadoc inherited.
    public int getIndividualCost(MutableStylePropertySet required) {
        
        int cost = 0;
        for (int c = 0; c < CHARACTERISTIC_COUNT; c += 1) {
            ShorthandAnalyzer analyzer = characteristicAnalyzers[c];
            cost += analyzer.getIndividualCost(required);
        }
        return cost;
    }

    public void copyIndividualValues(
            MutableStyleProperties properties,
            MutableStylePropertySet required) {

        for (int c = 0; c < CHARACTERISTIC_COUNT; c += 1) {
            ShorthandAnalyzer analyzer = characteristicAnalyzers[c];
            analyzer.copyIndividualValues(properties, required);
        }
    }

    /**
     * Get the analyzer for the specified characteristic.
     *
     * @param characteristic The index of the characteristics, must be less then
     *                       {@link #CHARACTERISTIC_COUNT}.
     * @return The analyzer.
     */
    public ShorthandAnalyzer getCharacteristicAnalyzer(int characteristic) {
        return characteristicAnalyzers[characteristic];
    }

    /**
     * Get the analyzer for the specified edge.
     *
     * @param edge The index of the edge, must be less then
     *             {@link PropertyGroups.EDGE_COUNT}.
     * @return The analyzer.
     */
    public ShorthandAnalyzer getEdgeAnalyzer(int edge) {
        return edgeAnalyzers[edge];
    }

    public Priority getShorthandPriority() {
        return shorthandPriority;
    }
}
