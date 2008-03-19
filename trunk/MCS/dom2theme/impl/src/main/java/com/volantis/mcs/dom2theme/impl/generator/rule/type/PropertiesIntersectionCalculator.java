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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.ShorthandValueIteratee;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.values.ShorthandValue;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.properties.StyleProperty;

public class PropertiesIntersectionCalculator
        implements MutableStylePropertiesIteratee, PropertyValueIteratee, ShorthandValueIteratee {

    private MutableStyleProperties subset;
    private MutableStyleProperties properties;
    private List notCommonProperties;

    public PropertiesIntersectionCalculator() {
    }

    public StyleProperties intersection(OutputStyledElementList elements,
            TypeSelectorSequence sequence) {

        subset = null;
        notCommonProperties = null;

        ElementSelectorFilter elementSelectorFilter =
                new ElementSelectorFilter(sequence, this);
        
        elementSelectorFilter.filter(elements);

        return subset;
    }
    
    public IterationAction next(final MutableStyleProperties properties) {

        IterationAction action = IterationAction.CONTINUE;
        this.properties = properties;
        if (properties != null) {
            if (subset == null) {
                // this is the first properties encountered.
                // so initialise the subset by optimistically moving all of
                // these into the subset. We will reduce them as we go.
                subset = ThemeFactory.getDefaultInstance().
                    createMutableStyleProperties(properties);
                notCommonProperties = new ArrayList();
            } else {
                // This is not the first properties encountered.
                // So search the properties for each of the subset properties.
                action = subset.iteratePropertyValues(this);
                
                for (Iterator i = notCommonProperties.iterator(); i.hasNext();) {
                    subset.clearPropertyValue((StyleProperty)i.next());
                }
                notCommonProperties.clear();
                
                if (action == IterationAction.CONTINUE) {
                    action = subset.iterateShorthandValues(this);
                }
                if (subset.isEmpty()) {
                    subset = null;
                }
            }
        } else {
            // no values in this properties, so there are no common properties
            // overall.
            subset = null;
        }

        if (subset == null) {
            action = IterationAction.BREAK;
        }

        return action;
    }

    public IterationAction next(PropertyValue subsetValue) {

        StyleProperty property = subsetValue.getProperty();
        PropertyValue propertyValue = properties.getPropertyValue(property);
        if (subsetValue.equals(propertyValue)) {
            // the property is in both sets with the same
            // value. In this case, leave the subset
            // as it is.
        } else {
            // property is either not in both sets or has
            // different value. In either case, it is
            // not common so remove this property's value
            // from the common set.
            notCommonProperties.add(property);
        }
        return IterationAction.CONTINUE;
    }

    public IterationAction next(ShorthandValue subsetValue) {

        StyleShorthand shorthand = subsetValue.getShorthand();
        ShorthandValue shorthandValue = properties.getShorthandValue(shorthand);
        if (subsetValue.equals(shorthandValue)) {
            // the shorthand is in both sets with the same
            // value. In this case, leave the subset
            // as it is.
        } else {
            // shorthand is either not in both sets or has
            // different value. In either case, it is
            // not common so remove this shorthand's value
            // from the common set.
            subset.clearShorthandValue(shorthand);
        }
        return IterationAction.CONTINUE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 18-Jul-05	8668/10	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
