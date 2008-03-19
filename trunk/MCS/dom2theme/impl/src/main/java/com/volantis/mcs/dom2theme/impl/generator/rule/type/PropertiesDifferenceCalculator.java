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
package com.volantis.mcs.dom2theme.impl.generator.rule.type;

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.ShorthandValueIteratee;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;

public class PropertiesDifferenceCalculator
        implements MutableStylePropertiesIteratee, PropertyValueIteratee, ShorthandValueIteratee {

    private StyleProperties commonProperties;
    private MutableStyleProperties properties;

    public PropertiesDifferenceCalculator() {
    }

    public void difference(OutputStyledElementList typedElementSubset,
            TypeSelectorSequence selectorSequence,
            StyleProperties commonProperties) {

        this.commonProperties = commonProperties;

        ElementSelectorFilter elementSelectorFilter =
                new ElementSelectorFilter(selectorSequence, this);

        elementSelectorFilter.filter(typedElementSubset);
    }

    public IterationAction next(final MutableStyleProperties properties) {

        this.properties = properties;
        IterationAction action = commonProperties.iteratePropertyValues(this);
        if (action == IterationAction.CONTINUE) {
            action = commonProperties.iterateShorthandValues(this);
        }
        return action;
    }

    public IterationAction next(PropertyValue propertyValue) {
        StyleProperty property = propertyValue.getProperty();
        // remove the common property from the properties passed in.
        properties.clearPropertyValue(property);
        return IterationAction.CONTINUE;
    }

    public IterationAction next(ShorthandValue shorthandValue) {
        StyleShorthand shorthand = shorthandValue.getShorthand();
        // remove the common shorthand from the properties passed in.
        properties.clearShorthandValue(shorthand);
        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Jul-05	8668/12	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
