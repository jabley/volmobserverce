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

import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.dom.WalkingDOMVisitor;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.WalkingDOMVisitorBasedTransformer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p><code>DOMTransformer</code> implementation that uses the
 * {@link com.volantis.mcs.css.version.CSSVersion#getRemappableElements()} to
 * create a series of <code>ElementRule</code>s that can be applied to each
 * <code>Element</code>. An <code>ElementRuleVisitor</code> is used to process
 * the <code>ElementRule</code>s that are relevant.</p>
 *
 * <p>The rationale for this class is to handle devices that have device
 * policies which equate to expressions that cause attributes to be generated
 * on elements rather than solely relying on CSS styling to work properly on a
 * given device.</p>
 */
public class CSSRemappingTransformer
        extends WalkingDOMVisitorBasedTransformer {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(CSSRemappingTransformer.class);

    // javadoc inherited
    protected WalkingDOMVisitor getWalkingDOMVisitor(DOMProtocol protocol) {

        final ProtocolConfiguration configuration =
                protocol.getProtocolConfiguration();

        CSSVersion cssVersion = null;

        if (null != configuration) {
            cssVersion = configuration.getCssVersion();
        }

        final Map rules = buildRules(cssVersion);

        return new ElementRuleVisitor(rules);
    }

    /**
     * Return a <code>Map</code> of <code>ElementRuleContainer</code>s keyed by
     * the name of the Element that each ElementRule should be applied to.
     *
     * @param cssVersion the <code>CSSVersion</code> - may be null.
     * @return a <code>Map</code> - non-null.
     */
    private Map buildRules(CSSVersion cssVersion) {

        if (null == cssVersion) {

            // Return an empty item.
            return Collections.EMPTY_MAP;
        }

        Map rules = new HashMap();

        Map remappableElements = cssVersion.getRemappableElements();

        for (Iterator elements = remappableElements.keySet().iterator();
             elements.hasNext();) {
            String element = (String) elements.next();
            Collection rulesForElement = buildRulesForElement(
                    (Map) remappableElements.get(element), element);
            rules.put(element, new ElementRuleContainer(element, rulesForElement));
        }

        return rules;
    }

    /**
     * Return a Collection of CSSRemappingRule instances based on the supplied
     * Map of device policy expression keyed by attribute name.
     *
     * @param expressions   a <code>Map</code> of device policy expressions.
     * @param element       name of the element for which remapping rules will
     *                      be generated
     * @return a <code>Collection</code> - non-null.
     */
    protected Collection buildRulesForElement(final Map expressions,
                                              String element) {
        Collection rules = new ArrayList();

        for (Iterator iterator = expressions.keySet().iterator();
             iterator.hasNext();) {
            final String attribute = (String) iterator.next();
            final String expression = (String) expressions.get(attribute);
            // Only attempt to create a rule if there is actually an expression.
            if (expression != null && expression.length() > 0) {
                try {
                    rules.add(new CSSRemappingRule(attribute, expression));
                } catch (InvalidExpressionException e) {
                    // Log the problem but continue.
                    LOGGER.warn("invalid-css-remapping-expression",
                            new Object[]{expression, element, attribute}, e);
                }
            }
        }
        return rules;
    }

}
