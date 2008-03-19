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

package com.volantis.mcs.themes;


/**
 * Represents a pseudo class selector of the form :nth-child(an+b).
 *
 * @mock.generate base="PseudoClassSelector"
 */
public interface NthChildSelector
    extends PseudoClassSelector {

    /**
     * Get the value of <code>a</code>.
     *
     * @return The value of <code>a</code>.
     */
    public int getA();

    /**
     * Set the value of <code>a</code>.
     *
     * @param a The value of <code>a</code>.
     */
    public void setA(int a);

    /**
     * Get the value of <code>b</code>.
     *
     * @return The value of <code>b</code>.
     */
    public int getB();

    /**
     * Set the value of <code>b</code>.
     *
     * @param b The value of <code>b</code>.
     */
    public void setB(int b);

    /**
     * Set the expression.
     *
     * <p>This may be one of the following forms.</p>
     * <ul>
     * <li><code>an+b</code></li>
     * <li><code>an</code></li>
     * <li><code>b</code></li>
     * <li><code>odd</code></li>
     * <li><code>even</code></li>
     * </ul>
     *
     * @param expression The expression being set.
     */
    public void setExpression(String expression);

    /**
     * Get the expression that was set.
     *
     * @return The expression that was set.
     */
    public String getExpression();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 ===========================================================================
*/
