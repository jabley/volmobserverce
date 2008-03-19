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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.expression;

import com.volantis.xml.expression.Value;

/**
 * This JavaBean is used to store the state data associated with a PAPI
 * SelectElement during MAML conditional processing. This state data is both
 * queried and updated during the processing of "select", "when" and
 * "otherwise" markup.
 */
public class SelectState {
    /**
     * The precept of the associated "select".
     *
     * @supplierCardinality 1
     * @supplierRole precept
     */
    private Precept precept;

    /**
     * The value of the "select"'s expression.
     *
     * @supplierCardinality 1
     * @supplierRole expression
     */
    private Value expression;

    /**
     * Indicates whether one or more "when" clauses have been executed.
     */
    private boolean matched;

    /**
     * Indicates whether the "otherwise" clause has been executed.
     */
    private boolean otherwiseExecuted;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param precept    the precept for the select. Must be non-null.
     * @param expression the expression for the select. Must be non-null.
     * @throws IllegalArgumentException if the precept or expression given is
     *                                  null
     */
    public SelectState(Precept precept, Value expression) {
        if (precept == null) {
            throw new IllegalArgumentException(
                "A precept must be specified");
        } else if (expression == null) {
            throw new IllegalArgumentException(
                "An expression must be specified");
        }

        this.precept = precept;
        this.expression = expression;
        matched = false;
        otherwiseExecuted = false;
    }

    /**
     * Returns the attribute value.
     *
     * @return the attribute value
     */
    public Precept getPrecept() {
        return precept;
    }

    /**
     * Returns the attribute value.
     *
     * @return the attribute value
     */
    public Value getExpression() {
        return expression;
    }

    /**
     * Returns true if a match has already been made within the scope of the
     * associated "select".
     *
     * @return true if a match has already been made
     */
    public boolean isMatched() {
        return matched;
    }

    /**
     * Returns true if the "otherwise" of the associated "select" has been
     * executed.
     *
     * @return true if otherwise has been executed
     */
    public boolean isOtherwiseExecuted() {
        return otherwiseExecuted;
    }

    /**
     * By invoking this method the caller indicates that a match has been made.
     *
     * @throws IllegalStateException if otherwise has already been executed
     */
    public void setMatched() {
        if (otherwiseExecuted) {
            throw new IllegalStateException(
                "A match should not have been made if otherwise has already " +
                "executed");
        }

        matched = true;
    }

    /**
     * By invoking this method the caller indicates that the "otherwise"
     * statement has been executed.
     *
     * @throws IllegalStateException if a match has already been made
     */
    public void setOtherwiseExecuted() {
        if (matched) {
            throw new IllegalStateException(
                "Otherwise should not have been executed if a match has " +
                "already been made");
        }

        otherwiseExecuted = true;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Aug-03	1019/1	philws	VBM:2003080807 Provide MCS core extensions for handling the select markup element's state

 ===========================================================================
*/
