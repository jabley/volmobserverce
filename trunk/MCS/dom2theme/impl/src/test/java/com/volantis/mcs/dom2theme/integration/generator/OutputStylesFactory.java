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
package com.volantis.mcs.dom2theme.integration.generator;

import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.optimizer.InputPropertiesOptimizer;
import com.volantis.mcs.dom2theme.impl.optimizer.StylesOptimizer;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

public class OutputStylesFactory {

    private static final DeviceStyles DEVICE_STYLES = new DeviceStyles() {
        public DeviceValues getValues() {
            return new DeviceValues() {
                public Priority getPriority(StyleProperty property) {
                    return Priority.NORMAL;
                }

                public void appendStandardCSS(StringBuffer buffer) {
                }

                public StyleValue getStyleValue(StyleProperty property) {
                    return DEFAULT;
                }
            };
        }

        public String getStandardCSS() {
            return null;
        }

        public DeviceStyles getNestedStyles(PseudoStyleEntity entity) {
            return this;
        }

        public DeviceStyles getMatchingStyles(
                StatefulPseudoClassSet pseudoClasses) {
            return this;
        }
    };

    private final StylesOptimizer stylesOptimizer;
    private final StyleValues parentValues;

    public OutputStylesFactory() {
        stylesOptimizer = new StylesOptimizer(new InputPropertiesOptimizer() {
            public MutableStyleProperties calculateOutputProperties(
                    String elementName, PseudoStylePath pseudoPath,
                    MutablePropertyValues inputValues,
                    StyleValues parentValues, DeviceValues deviceValues) {

                return createPropertiesFromValues(inputValues);
            }
        });

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        parentValues = stylingFactory.createPropertyValues();
    }

    public OutputStyles create(String name, Styles styles) {

        if (styles == null) {
            return null;
        }

        OutputStyles outputStyles = stylesOptimizer.calculateOutputStyles(
                name, styles, parentValues, DEVICE_STYLES);

        return outputStyles;
    }

    // todo: make into factory class for testing and sharing?
    private MutableStyleProperties createPropertiesFromValues(
            final PropertyValues values) {

        // todo: don't create unless necessary. then no need for empty check above?
        final MutableStyleProperties properties =
            ThemeFactory.getDefaultInstance().createMutableStyleProperties();

        // todo: better to use values.iterateComputed() and StyleValueIteratee?
        values.iterateStyleProperties(new StylePropertyIteratee() {
            public IterationAction next(StyleProperty property) {
                StyleValue value = values.getComputedValue(property);
                properties.setStyleValue(property, value);
                return IterationAction.CONTINUE;
            }
        });

        return properties;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05	9286/1	geoff	VBM:2005072208 Normalizing of inferrable properties does not work properly.

 18-Jul-05	8668/12	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
