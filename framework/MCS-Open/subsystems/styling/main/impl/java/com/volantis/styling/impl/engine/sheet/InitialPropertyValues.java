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
package com.volantis.styling.impl.engine.sheet;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.impl.values.MutablePropertyValuesImpl;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.Iterator;

/**
 * A set of initial property values, from a {@link StylePropertyDefinitions}.
 */
public class InitialPropertyValues
        implements ImmutablePropertyValues {

    /**
     * The style property definitions which supply the initial values.
     */
    private final StylePropertyDefinitions definitions;

    private final String standardCSS;

    /**
     * Initialise.
     *
     * @param definitions the style property definitions which supply the
     *      initial values.
     */
    public InitialPropertyValues(StylePropertyDefinitions definitions) {
        this.definitions = definitions;

        standardCSS = new MutablePropertyValuesImpl(this).getStandardCSS();
    }

    // Javadoc inherited.
    public ImmutablePropertyValues createImmutablePropertyValues() {
        return this;
    }

    // Javadoc inherited.
    public MutablePropertyValues createMutablePropertyValues() {
        return new MutablePropertyValuesImpl(this);
    }

    // Javadoc inherited.
    public StyleValue getComputedValue(StyleProperty property) {
        return property.getStandardDetails().getInitialValue();
    }

    // Javadoc inherited.
    public StyleValue getStyleValue(StyleProperty property) {
        return getComputedValue(property);
    }

    // Javadoc inherited.
    public StylePropertyDefinitions getStylePropertyDefinitions() {
        return definitions;
    }

    // Javadoc inherited.
    public IterationAction iterateStyleProperties(StylePropertyIteratee iteratee) {
        return definitions.iterateStyleProperties(iteratee);
    }

    // Javadoc inherited.
    public Iterator stylePropertyIterator() {
        return definitions.stylePropertyIterator();
    }

    // Javadoc inherited.
    public boolean isEmpty() {
        return false;
    }

    public StyleValue getSpecifiedValue(StyleProperty property) {
        return null;
    }

    public boolean wasExplicitlySpecified(StyleProperty property) {
        return false;
    }

    // Javadoc inherited.
    public boolean hasExplicitlySpecified() {
        return false;
    }

    public String getStandardCSS() {
        return standardCSS;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10730/1	emma	VBM:2005112516 Forward port: Vertical alignment style ignored on panes

 08-Dec-05	10716/1	emma	VBM:2005112516 Vertical alignment style ignored on panes

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 ===========================================================================
*/
