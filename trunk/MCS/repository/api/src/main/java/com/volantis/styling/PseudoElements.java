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

package com.volantis.styling;

/**
 * Represents the pseudo elements that can be used within the styling engine.
 *
 * <p>This really belongs in MCS somewhere.</p>
 */
public class PseudoElements implements PseudoElement {

    public static final PseudoElement FIRST_LINE = new PseudoElements(":first-line");

    public static final PseudoElement FIRST_LETTER = new PseudoElements(":first-letter");

    public static final PseudoElement BEFORE = new PseudoElements("::before");

    public static final PseudoElement AFTER = new PseudoElements("::after");

    public static final PseudoElement MCS_SHORTCUT = new PseudoElements(":mcs-shortcut");

    public static final PseudoElement MARKER = new PseudoElements("::marker");

    public static final PseudoElement MCS_NEXT = new PseudoElements("::mcs-next");

    public static final PseudoElement MCS_PREVIOUS = new PseudoElements("::mcs-previous");

    public static final PseudoElement MCS_RESET = new PseudoElements("::mcs-reset");
    
    public static final PseudoElement MCS_CANCEL = new PseudoElements("::mcs-cancel");
    
    public static final PseudoElement MCS_COMPLETE = new PseudoElements("::mcs-complete");
    
    public static final PseudoElement MCS_LABEL = new PseudoElements("::mcs-label");

    public static final PseudoElement MCS_ITEM = new PseudoElements("::mcs-item");

    public static final PseudoElement MCS_BETWEEN = new PseudoElements("::mcs-between");

    private final String cssRepresentation;

    protected PseudoElements(String cssRepresentation) {
        this.cssRepresentation = cssRepresentation;
    }

    // Javadoc inherited.
    public String getCSSRepresentation() {
        return cssRepresentation;
    }

    // Javadoc inherited.
    public void accept(PseudoStyleEntityVisitor visitor) {
        visitor.visit(this);
    }

    // Javadoc inherited.
    public String toString() {
        return getCSSRepresentation();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Oct-05	9440/2	schaloner	VBM:2005070711 Added marker pseudo-element support

 25-Aug-05	9377/1	schaloner	VBM:2005071102 Migrated mcs-shortcut-after to mcs-shortcut and after

 09-Aug-05	9195/2	emma	VBM:2005080510 Refactoring to create StyledDOMTester

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
