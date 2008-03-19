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

package com.volantis.mcs.dom2theme.impl.optimizer.font;

import com.volantis.mcs.dom2theme.impl.optimizer.BasicShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.PropertyClearerChecker;
import com.volantis.mcs.dom2theme.impl.optimizer.ShorthandAnalyzer;
import com.volantis.mcs.dom2theme.impl.optimizer.strategy.AbstractCompositeAnalyzer;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleShorthands;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.font.UnknownFontValue;
import com.volantis.mcs.themes.values.FontShorthandValue;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.styling.properties.MutableStylePropertySet;
import com.volantis.styling.properties.StyleProperty;

/**
 * Analyzer for font shorthand related properties.
 */
public class FontAnalyzer
        extends AbstractCompositeAnalyzer {

    /**
     * The bit for the font-family properties within the analyzer bits fields.
     */
    public static final int FAMILY_BIT = 1 << FontShorthandValue.FAMILY_INDEX;

    /**
     * The bit for the font-family properties within the analyzer bits fields.
     */
    public static final int SIZE_BIT = 1 << FontShorthandValue.SIZE_INDEX;

    /**
     * The analyzer for the mcs-system-font property.
     */
    private final ShorthandAnalyzer systemAnalyzer;

    /**
     * The analyzer for the standard font properties.
     */
    private final ShorthandAnalyzer standardAnalyzer;

    /**
     * The array of the above two analyzers
     */
    private final ShorthandAnalyzer[] allAnalyzers;

    /**
     * Indicates whether the system font is required.
     */
    private boolean systemFontRequired;

    /**
     * Indicates whether the shorthand can be used.
     */
    private boolean canUseShorthand;

    /**
     * Initialise.
     *
     * @param checker             The checker.
     * @param supportedShorthands The set of supported shorthands.
     */
    public FontAnalyzer(
            PropertyClearerChecker checker, ShorthandSet supportedShorthands) {
        super(PropertyGroups.FONT_PROPERTIES);

        systemAnalyzer = new BasicShorthandAnalyzer(StyleShorthands.FONT,
                new StyleProperty[]{StylePropertyDetails.MCS_SYSTEM_FONT},
                checker, supportedShorthands);
        standardAnalyzer = new FontShorthandAnalyzer(checker,
                supportedShorthands);

        allAnalyzers = new ShorthandAnalyzer[]{
            systemAnalyzer, standardAnalyzer
        };
    }

    // Javadoc inherited.
    protected void analyzeImpl() {

        analyze(allAnalyzers);

        if (systemAnalyzer.canUseShorthand()) {
            systemFontRequired = true;
            canUseShorthand = true;
        } else {
            systemFontRequired = false;
            canUseShorthand = standardAnalyzer.canUseShorthand();
        }

        // Update the required sets.
        updateRequiredSets(allAnalyzers);
    }

    // Javadoc inherited.
    public boolean allClearable() {
        return systemAnalyzer.allClearable() &&
                standardAnalyzer.allClearable();
    }

    // Javadoc inherited.
    public boolean canUseShorthand() {
        return canUseShorthand;
    }

    // Javadoc inherited.
    public void updateShorthand(
            MutableStyleProperties outputValues,
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {

        if (systemFontRequired) {
            StyleValue systemFont = inputValues.getStyleValue(
                    StylePropertyDetails.MCS_SYSTEM_FONT);

            StyleValue[] values = new StyleValue[FontShorthandValue.PROPERTIES.length];
            for (int i = 0; i < FontShorthandValue.PROPERTIES.length; i++) {
                StyleProperty property = FontShorthandValue.PROPERTIES[i];
                StyleValue value = inputValues.getStyleValue(property);
                if (value != UnknownFontValue.INSTANCE && value != null) {
                    values[i] = value;
                }
            }

            ShorthandValue shorthandValue = new FontShorthandValue(
                    StyleShorthands.FONT, systemFont, values,
                    shorthandPriority);
            outputValues.setShorthandValue(shorthandValue);
        } else {
            standardAnalyzer.updateShorthand(outputValues,
                    requiredForIndividual, requiredForShorthand);
        }
    }

    // Javadoc inherited.
    public int getIndividualCost(MutableStylePropertySet required) {
        return standardAnalyzer.getIndividualCost(required);
    }

    // Javadoc inherited.
    public void copyIndividualValues(
            MutableStyleProperties properties,
            MutableStylePropertySet required) {
        standardAnalyzer.copyIndividualValues(properties, required);
    }

    // Javadoc inherited.
    public int getShorthandCost(
            MutableStylePropertySet requiredForIndividual,
            MutableStylePropertySet requiredForShorthand) {
        int cost = 0;
        if (systemFontRequired) {
            StyleValue systemFont = inputValues.getStyleValue(
                    StylePropertyDetails.MCS_SYSTEM_FONT);
            cost += StyleShorthands.FONT.getName().length() + 1 +
                    systemFont.getStandardCost();

            for (int i = 0; i < FontShorthandValue.PROPERTIES.length; i++) {
                StyleProperty property = FontShorthandValue.PROPERTIES[i];
                StyleValue value = inputValues.getStyleValue(property);
                if (value != UnknownFontValue.INSTANCE && value != null) {
                    cost += 1 + property.getName().length() + 1 +
                            value.getStandardCost();
                }
            }
        } else {
            cost = standardAnalyzer.getShorthandCost(requiredForIndividual,
                    requiredForShorthand);
        }
        return cost;
    }

    // Javadoc inherited.
    public Priority getShorthandPriority() {
        return shorthandPriority;
    }

    /**
     * Indicates whether the system font is required.
     *
     * @return True if it is, false otherwise.
     */
    public boolean isSystemFontRequired() {
        return systemFontRequired;
    }
}
