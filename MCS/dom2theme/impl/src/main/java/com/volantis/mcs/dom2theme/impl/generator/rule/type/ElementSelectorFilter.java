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

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.shared.iteration.IterationAction;

/**
 * Selects the style properties contained within a list of output styled
 * elements which "match" a selector sequence.
 * <p>
 * Only the type selector affects matching, the pseudo classes and pseudo
 * elements select the set of properties to compare.
 */
public class ElementSelectorFilter
        implements OutputStyledElementIteratee {

    private final TypeSelectorSequence selectorSequence;

    private final MutableStylePropertiesIteratee filtered;

    public ElementSelectorFilter(TypeSelectorSequence selectorSequence,
            MutableStylePropertiesIteratee filtered) {

        this.selectorSequence = selectorSequence;
        this.filtered = filtered;
    }

    public void filter(OutputStyledElementList elements) {

        elements.iterate(this);
    }

    public IterationAction next(OutputStyledElement element) {

        // Only the type selector affects matching, the pseudo classes and
        // pseudo elements select the set of properties to compare.

        IterationAction action = IterationAction.CONTINUE;

        // If the type selector matches ...
        String type = selectorSequence.getType();
        if (type == null || type.equals(element.getName())) {

            // Then extract the appropriate properties from the styles.

            MutableStyleProperties properties = null;
            PseudoStylePath pseudoPath = selectorSequence.getPath();
            // If the element has styles
            OutputStyles styles = element.getStyles();
            if (styles != null) {
                // Then we might have some style properties.
                properties = styles.getPathProperties(pseudoPath);
            }
            // else, no styles. This means the properties are empty.

            action = filtered.next(properties);

            // the filtered may remove property values, so we need to check
            // to see if the containing objects are still required.
            if (properties != null && properties.isEmpty()) {
                // if the properties is now empty, remove it.
                styles.removePathProperties(pseudoPath);
                if (styles.isEmpty()) {
                    element.clearStyles();
                }
            }
        }

        return action;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Jul-05	8668/3	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
