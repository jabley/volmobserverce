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
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.Iterator;

/**
 * Class defining how an expression maps to an attribute. The expression is
 * intended to a single attribute to be generated from multiple CSS properties.
 *
 * Instances of this class are immutable but it performs no locking around the
 * {@link #apply(com.volantis.mcs.dom.Element)} method, so it cannot guarantee
 * thread-safety. Clients requiring that sort of guarantee should perform their
 * own locking around that method.
 */
public class CSSRemappingRule implements ElementRule {

    /**
     * The name of the attribute to create.
     */
    private final String attName;

    /**
     * The name of the CSS Property to use to create the attribute value. This
     * field is a convenience based on the currently limited grammar that is
     * supported, and would probably be removed once more complex grammar
     * support is added.
     */
    private final String cssProperty;

    /**
     * Create a new CSSRemappingRule instance, using the specified attribute
     * name and <code>expression</code>.
     *
     * @param attName
     * @param expression
     * @throws InvalidExpressionException if the expression was not
     * syntactically correct
     */
    public CSSRemappingRule(String attName, String expression)
            throws InvalidExpressionException {
        this.attName = attName;

        // Validate the expression is syntactically correct. We are doing this
        // eagerly here, but it could be done lazily in the execute method.
        // Doing it in an eager fashion means that the cssProperty field can be
        // final and thus the class is immutable.
        this.cssProperty = getCSSProperty(expression);
    }

    /**
     * Return the CSS property from expression after parsing it.
     *
     * @param expression
     * @return the CSS String property
     * @throws InvalidExpressionException if the expression was not
     * syntactically correct
     */
    private String getCSSProperty(String expression)
            throws InvalidExpressionException {
        return new ExpressionParser(expression).getCSSProperty();
    }

    // javadoc inherited
    public void apply(Element element) {

        // @todo can throw a RuntimeException subclass if the expression is
        // invalid, during lazy expression evaluation.

        if (!passesPrerequisites(element)) {

            // Do we alter an existing one? No for now. Choosing this option
            // means that the execute method is idempotent, which could be a
            // good property to have.
            return;
        }

        String value = evaluateExpression(element);

        // @todo should we add the attribute if the value is ""?
        element.setAttribute(attName, value);
    }

    /**
     * Return true if the element is suitable for having this rule applied to
     * it, otherwise false.
     *
     * @param element
     * @return true or false
     */
    private boolean passesPrerequisites(Element element) {
        return !hasAttributeAlready(element);
    }

    /**
     * Return true if the element already has an attribute with a name matching
     * the <code>attName</code> field, otherwise false.
     *
     * @param element
     * @return true if the element has the attribute.
     */
    private boolean hasAttributeAlready(Element element) {
        return null != element.getAttributeValue(attName);
    }


    /**
     * Return a <code>String</code> returned via evaluating the expression.
     *
     * @param element
     * @return a String value - not null.
     */
    private String evaluateExpression(Element element) {

        final MutablePropertyValues propertyValues = element.getStyles().
                getPropertyValues();

        StyleProperty styleProperty = findStyleProperty(propertyValues);

        if (styleProperty != null) {
            StyleValue styleValue = propertyValues.getComputedValue(styleProperty);
            if (styleValue != null) {
                return styleValue.getStandardCSS();
            }
        }

        return "";
    }

    /**
     * Return the <code>StyleProperty</code> matching the
     * <code>cssProperty</code> field, or <code>null</code> if there is no such
     * match.
     *
     * @param propertyValues
     * @return a <code>StyleProperty</code> instance or <code>null</code>.
     */
    private StyleProperty findStyleProperty(
            final MutablePropertyValues propertyValues) {

        for (Iterator it = propertyValues.stylePropertyIterator();
             it.hasNext();) {
            StyleProperty styleProperty = (StyleProperty) it.next();

            if (cssProperty.equals(styleProperty.getName())) {
                return styleProperty;
            }
        }

        return null;
    }

    // javadoc inherited
    public boolean equals(Object obj) {
        if (!(obj instanceof CSSRemappingRule)) {
            return false;
        }

        CSSRemappingRule other = (CSSRemappingRule) obj;

        return attName.equals(other.attName)
                && cssProperty.equals(other.cssProperty);
    }

    // javadoc inherited
    public int hashCode() {
        return 37 * attName.hashCode() + cssProperty.hashCode();
    }

    // javadoc inherited
    public String toString() {
        return "[cssRemappingRule: attribute=" + attName
                + ", cssProperty=" + cssProperty +
                "]";
    }
}
