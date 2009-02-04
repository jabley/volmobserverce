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

import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.OutputValues;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.NestedStylesIteratee;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoElements;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.styling.StatefulPseudoClassSetImpl;
import com.volantis.styling.StatefulPseudoClasses;
import com.volantis.styling.Styles;
import com.volantis.styling.device.DeviceStyles;
import com.volantis.styling.device.DeviceValues;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Optimize a {@link Styles}.
 */
public class StylesOptimizer
        implements NestedStylesIteratee {

    /**
     * The set of valis pseudo classes.
     */
    private static final StatefulPseudoClassSet VALID_PSEUDO_CLASSES;

    /**
     * The set of valid pseudo elements.
     */
    private static final Set VALID_PSEUDO_ELEMENTS;

    static {
        StatefulPseudoClassSet valid = new StatefulPseudoClassSetImpl();
        valid = valid.add(StatefulPseudoClasses.ACTIVE);
        valid = valid.add(StatefulPseudoClasses.FOCUS);
        valid = valid.add(StatefulPseudoClasses.HOVER);
        valid = valid.add(StatefulPseudoClasses.LINK);
        valid = valid.add(StatefulPseudoClasses.VISITED);
        VALID_PSEUDO_CLASSES = valid;

        Set set = new HashSet();
        set.add(PseudoElements.FIRST_LETTER);
        set.add(PseudoElements.FIRST_LINE);
        VALID_PSEUDO_ELEMENTS = set;
    }

    /**
     * The optimizer for the properties.
     */
    private final InputPropertiesOptimizer propertiesOptimizer;

    /**
     * The list of {@link OutputValues} that has being constructed.
     */
    private final List flattened;

    /**
     * The name of the element being optimized.
     */
    private String elementName;

    /**
     * The parent values.
     */
    private StyleValues parentValues;

    /**
     * The output styles that are being constructed, this is created lazily
     * when it is needed.
     */
    private OutputStyles outputStyles;

    /**
     * The path from the root of the {@link Styles} of the styles currently
     * being processed.
     */
    private PseudoStylePath pseudoStylePath;

    /**
     * The styles that the device will use, this is a shadow of the element
     * styles.
     */
    private DeviceStyles deviceStyles;

    /**
     * Initialise.
     *
     * @param propertiesOptimizer The properties optimizer.
     */
    public StylesOptimizer(InputPropertiesOptimizer propertiesOptimizer) {
        this.propertiesOptimizer = propertiesOptimizer;
        flattened = new ArrayList();
    }

    /**
     * Calculates the output styles.
     *
     * @param name         The element name.
     * @param inputStyles  The input styles.
     * @param parentValues The parent styles.
     * @param deviceStyles The device styles.
     * @return The {@link OutputStyles}, or null if no styles are needed.
     */
    public OutputStyles calculateOutputStyles(
            String name, Styles inputStyles, StyleValues parentValues,
            DeviceStyles deviceStyles) {

        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (inputStyles == null) {
            throw new IllegalArgumentException("inputStyles cannot be null");
        }
        if (parentValues == null) {
            throw new IllegalArgumentException("parentValues cannot be null");
        }

        this.elementName = name;
        this.parentValues = parentValues;
        this.deviceStyles = deviceStyles;
        this.outputStyles = null;

        pseudoStylePath = PseudoStylePath.EMPTY_PATH;
        flattened.clear();

        optimizeStyles(inputStyles);

        for (int i = 0; i < flattened.size(); i++) {
            OutputValues values = (OutputValues) flattened.get(i);
            if (values != null) {
                if (outputStyles == null) {
                    outputStyles = new OutputStyles();
                }
                outputStyles.addPathProperties(values.getPath(),
                        values.getProperties());
            }
        }

        return outputStyles;
    }

    /**
     * Optimize the styles.
     *
     * @param styles The styles to optimize.
     */
    private void optimizeStyles(Styles styles) {

        // Makes space in the list for the output values that may be added by
        // this method. This must be done in order to preserve order which is
        // important for styling.
        int index = flattened.size();
        flattened.add(null);

        MutablePropertyValues inputValues = styles.findPropertyValues();

        styles.iterate(this);

        DeviceValues deviceValues = deviceStyles.getValues();

        MutableStyleProperties outputProperties =
                propertiesOptimizer.calculateOutputProperties(elementName,
                        pseudoStylePath, inputValues, parentValues,
                        deviceValues);

        if (outputProperties != null) {
            OutputValues outputValues = null;

            outputValues = new OutputValues(pseudoStylePath, outputProperties);
            flattened.set(index, outputValues);
        }
    }

    // Javadoc inherited.
    public IterationAction next(NestedStyles nestedStyles) {

        // Save away the current values of the path, parent value and device
        // styles as they may change during the body of this but need
        // resetting afterwards.
        PseudoStylePath oldPath = pseudoStylePath;
        StyleValues oldParentValues = parentValues;
        DeviceStyles oldDeviceStyles = deviceStyles;

        PseudoStyleEntity entity = nestedStyles.getPseudoStyleEntity();
        boolean valid = false;
        if (entity instanceof PseudoElement) {
            PseudoElement pseudoElement = (PseudoElement) entity;

            if (VALID_PSEUDO_ELEMENTS.contains(pseudoElement)) {
                pseudoStylePath =
                        pseudoStylePath.addPseudoElement(pseudoElement);
                parentValues =
                        nestedStyles.getContainer().findPropertyValues();
                deviceStyles = deviceStyles.getNestedStyles(pseudoElement);
                valid = true;
            }

        } else if (entity instanceof StatefulPseudoClassSet) {
            StatefulPseudoClassSet pseudoClassSet =
                    (StatefulPseudoClassSet) entity;
            if (pseudoClassSet.isSubSetOf(VALID_PSEUDO_CLASSES)) {
                pseudoStylePath =
                        pseudoStylePath.addPseudoClassSet(pseudoClassSet);
                deviceStyles = deviceStyles.getMatchingStyles(pseudoClassSet);
                valid = true;
            }
        } else {
            throw new IllegalStateException("Unknwon pseudo entity " + entity);
        }

        if (valid) {
            optimizeStyles(nestedStyles);
        }

        // Reset the paths.
        pseudoStylePath = oldPath;
        parentValues = oldParentValues;
        deviceStyles = oldDeviceStyles;

        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10370/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10370/2	geoff	VBM:2005111405 MCS stability. Requesting pages over a 48 hour period lead to Errors

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 18-Jul-05	8668/4	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
