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

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.themes.NthChildSelector;
import com.volantis.mcs.themes.PseudoClassTypeEnum;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.PseudoClassSelector;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;

public class DefaultNthChildSelector
        extends DefaultPseudoClassSelector
        implements NthChildSelector {

    private boolean recalculateExpression;

    private String expression;
    private int a;
    private int b;
    private static final char PLUS = '+';
    private static final char MINUS = '-';

    public DefaultNthChildSelector() {
        this(0, 0);
        // This class is annotated by JiBX so that it can be used to unmarshall.
        // The new method added by JiBX uses this constructor (via newInstance)
        // and then sets the expression directly (i.e. not via the accessor
        // method). This is why we have to call update expression directly here,
        // rather than just setting the recalculateExpression flag to true.
        updateExpression();
    }

    public DefaultNthChildSelector(int a, int b) {
        super(PseudoClassTypeEnum.NTH_CHILD);

        this.a = a;
        this.b = b;

        recalculateExpression = true;
    }

    public DefaultNthChildSelector(String expression) {
        super(PseudoClassTypeEnum.NTH_CHILD);

        this.expression = expression;
        calculateCoefficients();
    }

    // Javadoc inherited.
    public int getA() {
        return a;
    }

    // Javadoc inherited.
    public void setA(int a) {
        if (this.a != a) {
            recalculateExpression = true;
            this.a = a;
        }
    }

    // Javadoc inherited.
    public int getB() {
        return b;
    }

    // Javadoc inherited.
    public void setB(int b) {
        if (this.b != b) {
            recalculateExpression = true;
            this.b = b;
        }
    }

    // Javadoc inherited.
    public String getExpression() {
        if (recalculateExpression) {
            updateExpression();
        }
        return expression;
    }

    // Javadoc inherited.
    public void setExpression(String expression) {
        if (this.expression == null
                ? expression != null
                : !this.expression.equals(expression)) {
            this.expression = expression;
            calculateCoefficients();
        }
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // Calling getExpression ensures that if the expression was set to an
        // invalid value, but subsequently the coefficients were set to a valid
        // value, we validate against the recalculated expression, 
        getExpression();
        if (!calculateCoefficients()) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                    context.createMessage("theme-invalid-nth-child-selector",
                            expression));
        }
    }

    /**
     * The expression has changed so use it to calculate the new coefficients.
     *
     * @return true if the expression evaluated to valid coefficients,
     * false otherwise
     */
    private boolean calculateCoefficients() {

        boolean validExpr = true;

        // check that there is an expression
        if (expression == null || expression.length() == 0) {
            validExpr = false;
        } else {
            // Strip out all whitespace.
            StringBuffer buffer = new StringBuffer(expression.length());
            for (int i = 0; i < expression.length(); i += 1) {
                char c = expression.charAt(i);
                if (!Character.isWhitespace(c)) {
                    buffer.append(c);
                }
            }
            String expression = buffer.toString();
            if (expression.equals("odd")) {
                a = 2;
                b = 1;
            } else if (expression.equals("even")) {
                a = 2;
                b = 0;
            } else {
                String aString = null;
                String bString = null;
                // todo handle all the possible values, e.g. odd, even, an-b....

                // check if the first character is an operator
                int startSearch = 0;
                if (expression.charAt(0) == PLUS || expression.charAt(0) == MINUS) {
                    // then don't include that in the operator
                    startSearch = 1;
                }
                int operatorIndex = expression.indexOf(PLUS, startSearch);

                operatorIndex = operatorIndex != -1? operatorIndex:
                        expression.indexOf(MINUS, startSearch);

                if (operatorIndex == -1) {
                    // expression is either "an", or "b".
                    if (expression.endsWith("n")) {
                        // expression is "an" where "a" can be zero or a positive or
                        // negative integer
                        if (expression.equals("n")) {
                            aString = "1";
                        } else if (expression.equals("-n")) {
                            aString = "-1";
                    } else {
                            aString = expression.substring(0,
                                    expression.length() - 1);
                        }
                    } else {
                        // expression is "b" where "b" can be zero or a positive or
                        // negative integer
                        bString = expression;
                    }
                } else {
                    // expression = "an+b"
                    if (expression.startsWith("n")) {
                        aString = "1";
                    } else if (expression.startsWith("-n")) {
                        aString = "-1";
                    } else {
                        aString = expression.substring(0, operatorIndex - 1);
                    }

                    bString = expression.substring(operatorIndex);
                }

                a = 0;
                b = 0;
                if (aString != null) {
                    try {
                        if (aString.charAt(0) == PLUS) {
                            aString = aString.substring(1);
                        }
                        a = Integer.parseInt(aString);
                    } catch (NumberFormatException e) {
                        validExpr = false;
                    }
                }
                if (bString != null) {
                    try {
                        if (bString.charAt(0) == PLUS) {
                            bString = bString.substring(1);
                        }
                        b = Integer.parseInt(bString);
                    } catch (NumberFormatException e) {
                        validExpr = false;
                    }
                }
            }
        }
        return validExpr;
    }

    /**
     * The coefficients have changed so update the expression.
     */
    private void updateExpression() {
        if (a == 2 && b == 0) {
            expression = "even";
        } else if (a == 2 && b == 1) {
            expression = "odd";
        } else if (a == 0) {
            expression = String.valueOf(b);
        } else if (b == 0) {
            expression = String.valueOf(a) + "n";
        } else if (b < 0) {
            expression = String.valueOf(a) + "n" + String.valueOf(b);
        } else {
            expression = String.valueOf(a) + "n+" + String.valueOf(b);
        }
        recalculateExpression = false;
    }

    // Javadoc inherited.
    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    protected PseudoClassSelector copyImpl() {
        return new DefaultNthChildSelector(getExpression());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/2	emma	VBM:2005111705 Interim commit

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/1	pduffin	VBM:2005111410 Added support for copying model objects

 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 ===========================================================================
*/
