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

package com.volantis.styling.impl.engine.inferring;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.NestedStylesIteratee;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.impl.counter.CounterEngine;
import com.volantis.styling.impl.engine.Inheriter;
import com.volantis.styling.impl.engine.InheriterImpl;
import com.volantis.styling.impl.engine.PropertyValueTranslator;
import com.volantis.styling.impl.expressions.PropertyValuesEvaluator;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.InitialValueFinder;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.Iterator;

/**
 * Infers all the values for those properties not explicitly specified.
 */
public class Inferrer
        implements NestedStylesIteratee {

    /**
     * Performs inheritance.
     */
    private final Inheriter inheriter;

    /**
     * Keeps track of the counters.
     */
    private final CounterEngine counterEngine;

    /**
     * Evaluates property values that contain expressions.
     */
    private final PropertyValuesEvaluator propertyValuesEvaluator;

    /**
     * Translates conditional values.
     */
    private final PropertyValueTranslator propertyValueTranslator;

    /**
     * The set of inheritable values.
     */
    private StyleValues inheritableValues;

    /**
     * Initialise.
     *
     * @param counterEngine           The counter engine.
     * @param propertyValuesEvaluator The property values evaluator.
     */
    public Inferrer(
            CounterEngine counterEngine,
            PropertyValuesEvaluator propertyValuesEvaluator) {

        this.counterEngine = counterEngine;
        this.propertyValuesEvaluator = propertyValuesEvaluator;
        inheriter = new InheriterImpl(new InitialValueFinder());

        propertyValueTranslator = new PropertyValueTranslator();
    }

    /**
     * Infer the values in styles that have not been explicitly set.
     *
     * @param styles      The styles whose values need inferring.
     * @param inheritable The inheritable values.
     * @return The new set of inheritable values.
     */
    public StyleValues infer(Styles styles, StyleValues inheritable) {

        MutablePropertyValues values = styles.getPropertyValues();

        inheriter.inherit(values, inheritable);

        // Update the counters based on the styles.
        counterEngine.startElement(styles);

        // Evaluate any compiled values in the styles.
        propertyValuesEvaluator.evaluate(values);

        // Create a copy of the evaluated properties in case we need to inherit
        // from them.
        inheritableValues = values.createImmutablePropertyValues();

        styles.iterate(this);

        propertyValueTranslator.translate(values);

        return inheritableValues;
    }

    // Javadoc inherited.
    public IterationAction next(NestedStyles nestedStyles) {

        MutablePropertyValues values = nestedStyles.getPropertyValues();

        PseudoStyleEntity entity = nestedStyles.getPseudoStyleEntity();
        if (entity instanceof PseudoElement) {
            // Pseudo elements inherit from their superior parent just as
            // elements inherit from their parent.
            inheriter.inherit(values, inheritableValues);
        } else {
            // Make sure that all the computed properties for pseudo classes
            // are set.
            // todo Optimise this when PropertyValues are made lazy.
            Iterator i = values.stylePropertyIterator();
            while (i.hasNext()) {
                StyleProperty property = (StyleProperty) i.next();
                StyleValue specified = values.getSpecifiedValue(property);
                if (specified != null) {
                    values.setComputedValue(property, specified);
                }
            }
        }

        // Evaluate any compiled values in the styles.
        propertyValuesEvaluator.evaluate(values);

        if (nestedStyles.hasNestedStyles()) {

            // Save away the existing inheritable values so they can be
            // restored later.
            StyleValues oldInheritableValues = inheritableValues;

            // Create an immutable set of properties from which any nested
            // pseudo elements will inherit.
            inheritableValues = values.createImmutablePropertyValues();

            // Iterate over the nested styles.
            nestedStyles.iterate(this);

            // Restore the previous inheritable values.
            inheritableValues = oldInheritableValues;
        }

        propertyValueTranslator.translate(values);

        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 ===========================================================================
*/
