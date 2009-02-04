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

import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * <code>CleanDOMVisitor</code> implementation which will apply a
 * <code>Map</code> of <code>ElementRule</code> implementations to each
 * <code>Element</code> that gets visited.
 */
class ElementRuleVisitor extends WalkingDOMVisitorStub {

    /**
     * Map of <code>ElementRule</code>s keyed by the name of the element
     * that each <code>ElementRule</code> operates on.
     */
    private final Map rules;

    /**
     * Create a new <code>ElementRuleVisitor</code> which will apply the
     * specified rules.
     *
     * @param rules a <code>Map</code> of <code>ElementRule</code>
     *              instances keyed on the element name that each rule
     *              should be applied to.
     */
    public ElementRuleVisitor(Map rules) {
        if (null == rules) {
            throw new IllegalArgumentException("rules cannot be null");
        }
        synchronized(rules) {
            this.rules = new HashMap(rules);
        }
    }

    // Javadoc inherited.
    public void visit(Element element) {
        final ElementRule rule = (ElementRule)
                rules.get(element.getName());

        if (rule != null) {
            rule.apply(element);
        }
    }
}
