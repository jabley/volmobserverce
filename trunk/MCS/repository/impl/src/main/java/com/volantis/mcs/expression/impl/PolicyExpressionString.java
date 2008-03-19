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

package com.volantis.mcs.expression.impl;

import com.volantis.mcs.expression.PolicyExpression;

/**
 * Represents a policy expression as a string.
 */
public class PolicyExpressionString
        implements PolicyExpression {

    /**
     * The string representation of the expression.
     */
    private String expression;

    /**
     * Package private constructor for use by JiBX.
     */
    PolicyExpressionString() {
    }

    /**
     * Initialise.
     *
     * @param expression The expression as a string.
     */
    public PolicyExpressionString(String expression) {
        this.expression = expression;
    }

    // Javadoc inherited.
    public String getAsString() {
        return expression;
    }

    // Javadoc inherited.
    public boolean equals(Object obj) {
        if (!(obj instanceof PolicyExpressionString)) {
            return false;
        }

        PolicyExpressionString other = (PolicyExpressionString) obj;

        return expression.equals(other.expression);
    }

    // Javadoc inherited.
    public int hashCode() {
        return expression.hashCode(); 
    }

    // Javadoc inherited.
    public String toString() {
        return super.toString() + " {" + expression + "}";
    }
}
