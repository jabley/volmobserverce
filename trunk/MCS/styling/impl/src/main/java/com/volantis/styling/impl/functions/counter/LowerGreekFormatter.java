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

package com.volantis.styling.impl.functions.counter;



/**
 * Lower greek formatter.
 */
public class LowerGreekFormatter
        extends AbstractAlphabeticFormatter {

    /**
     * Array of lower greek letters.
     */
    private static final String[] LOWER_GREEK = new String[]{
        "\u03B1", // lower alpha
        "\u03B2", // lower beta
        "\u03B3", // lower gamma
        "\u03B4", // lower delta
        "\u03B5", // lower epsilon
        "\u03B6", // lower zeta
        "\u03B7", // lower eta
        "\u03B8", // lower theta
        "\u03B9", // lower iota
        "\u03BA", // lower kappa
        "\u03BB", // lower lambda
        "\u03BC", // lower mu
        "\u03BD", // lower nu
        "\u03BE", // lower xi
        "\u03BF", // lower omicron
        "\u03C0", // lower pi
        "\u03C1", // lower rho
        "\u03C2", // lower final
        "\u03C3", // lower sigma
        "\u03C4", // lower tau
        "\u03C5", // lower upsilon
        "\u03C6", // lower phi
        "\u03C7", // lower chi
        "\u03C8", // lower psi
        "\u03C9", // lower omega
    };

    /**
     * Initialise.
     */
    public LowerGreekFormatter() {
        super(LOWER_GREEK);
    }
}
