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

package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.StyleInherit;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.InitialValueFinder;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * Implementation of {@link Inheriter}.
 */
public class InheriterImpl
        implements Inheriter, StylePropertyIteratee {

    /**
     * The finder for initial value.
     */
    private final InitialValueFinder initialValueFinder;

    /**
     * THe values being updated.
     */
    private MutablePropertyValues values;

    /**
     * The values for inheritance.
     */
    private StyleValues inheritableValues;

    /**
     * Initialise.
     *
     * @param initialValueFinder The finder for initial value.
     */
    public InheriterImpl(InitialValueFinder initialValueFinder) {
        this.initialValueFinder = initialValueFinder;
    }

    // Javadoc inherited.
    public void inherit(MutablePropertyValues values,
                        StyleValues inheritableValues) {

        StylePropertyDefinitions definitions
                = values.getStylePropertyDefinitions();

        this.values = values;
        this.inheritableValues = inheritableValues;

        definitions.iterateStyleProperties(this);
    }

    // Javadoc inherited.
    public IterationAction next(StyleProperty property) {

        StyleValue specified = values.getSpecifiedValue(property);
        StyleValue computed = null;

        if (specified instanceof StyleInherit) {
            computed = inheritableValues.getStyleValue(property);

        } else if (specified == null) {
            if (property.getStandardDetails().isInherited()) {
                computed = inheritableValues.getStyleValue(property);
            } else {
                computed = getInitial(property);
            }

        } else {
            computed = specified;
        }

        values.setComputedValue(property, computed);

        return IterationAction.CONTINUE;
    }

    /**
     * Get the initial value for a given style property.
     *
     * The initial value may be either a fixed value, or a dependent one, e.g.
     * a computed value. If it is computed, there are translation rules, then
     * we will use both specified and computed source values.
     *
     * @param property to initial value for
     * @return initial style value, may be null
     */
    private StyleValue getInitial(StyleProperty property) {
        return initialValueFinder.getInitialValue(values,
                property.getStandardDetails());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Dec-05	10679/1	ianw	VBM:2005120605 Back out background-color inheritance and make xdimecp body output div

 07-Dec-05	10659/1	ianw	VBM:2005120605 Back out background-color inheritance and make xdimecp body output div

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/5	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10567/1	emma	VBM:2005112901 Forward port of bug fix: problems targetting styles by setting a class on the parent

 02-Dec-05	10544/1	emma	VBM:2005112901 Bug fix: problems targetting styles by setting a class on the parent

 29-Nov-05	10347/11	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/9	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 18-Nov-05	10347/7	pduffin	VBM:2005111405 Stopped copying style values in order to change whether they were explicitly specified or not

 18-Nov-05	10347/5	pduffin	VBM:2005111405 Corrected issue with styling

 18-Nov-05	10347/3	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
