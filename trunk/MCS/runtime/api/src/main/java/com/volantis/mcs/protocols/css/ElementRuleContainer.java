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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Composite class to hold a Collection of ElementRule instances to be appplied
 * to a given element.
 *
 * This class is thread-safe and immutable.
 */
public class ElementRuleContainer implements ElementRule {

    private final String elementName;

    /**
     * Collection of CSSRemappingRule instances to be applied.
     */
    private final Collection rules;

    /**
     * Create a new ElementRuleContainer that will act on the specified named element,
     * applying the specified rules.
     *
     * @param elementName the element name that this collection of rules will
     *                    apply to.
     * @param rules       a <code>Collection</code> of
     *                    <code>CSSRemappingRule</code> instances.
     */
    public ElementRuleContainer(String elementName, Collection rules) {

        if (null == elementName) {
            throw new IllegalArgumentException("elementName cannot be null");
        }

        if (null == rules) {
            throw new IllegalArgumentException("rules cannot be null");
        }

        this.elementName = elementName;

        // Copy the collection so that internal data structures aren't left
        // exposed.
        synchronized (rules) {
            this.rules = new ArrayList(rules);
        }
    }

    /**
     * Apply the <code>CSSRemappingRule</code> instances to the specified
     * <code>Element</code>.
     *
     * @param element the context <code>Element</code>.
     */
    public void apply(Element element) {

        // Don't act on elements that this rule shouldn't be applied to. This
        // could be omitted on the understanding that the client will use this
        // object properly, but leaving the check in for now.
        if (!elementName.equals(element.getName())) {
            return;
        }

        for (Iterator iterator = rules.iterator(); iterator.hasNext();) {
            ElementRule rule = (ElementRule) iterator.next();
            rule.apply(element);
        }
    }

    // javadoc inherited
    public String toString() {
        return "[elementRuleContainer: elementName=" + elementName
                + ", rules=" + rules
                + "]";
    }
}
