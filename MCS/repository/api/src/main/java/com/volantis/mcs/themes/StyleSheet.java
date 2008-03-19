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

import java.util.List;

/**
 * A list of rules which make up a complete style sheet.
 *
 * @mock.generate
 */
public interface StyleSheet
        extends BaseStyleSheet {

    /**
     * Return the list of rules.
     *
     * @return The list of rules.
     */
    List getRules();

    /**
     * Add a <code>Rule</code> to the list of rules
     *
     * @param rule The <code>Rule</code> to add to the list of rules.
     * @throws IllegalStateException can not change an immutable DeviceTheme.
     */
    void addRule(Rule rule);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Aug-05	9187/1	tom	VBM:2005080509 release of 2005080509 [extract WML default styles into a CSS file]

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
