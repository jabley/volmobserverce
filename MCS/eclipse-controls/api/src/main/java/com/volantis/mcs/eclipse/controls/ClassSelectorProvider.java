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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.controls.events.StateChangeListener;
import com.volantis.mcs.themes.ClassSelector;
import com.volantis.mcs.themes.StyleSheetFactory;

/**
 * A {@link ValidatedObjectControl} implementation for creating class
 * selectors.
 *
 * <p>Because all class selectors are created equally (as new class selectors
 * with an empty string for their class) this implementation has no GUI of its
 * own, is always valid, and generates a class selector with an empty string
 * for its class each time {@link ValidatedObjectControl#getValue} is
 * called.</p>
 */
public class ClassSelectorProvider implements NonVisualValidatedObjectControl {
    /**
     * The default OK validation status
     */
    private ValidationStatus VALIDATE_OK =
            new ValidationStatus(ValidationStatus.OK, "");

    /**
     * The factory to use for creating new theme model objects.
     */
    private static final StyleSheetFactory MODEL_FACTORY =
            StyleSheetFactory.getDefaultInstance();

    // Javadoc inherited
    public Object getValue() {
        ClassSelector selector = MODEL_FACTORY.createClassSelector("");
        return selector;
    }

    /**
     * Given that the class selector provider has no configurable values, there
     * is nothing to do when it displayed value is 'set' to a given value.
     *
     * @param newValue The value to set on the validated object control
     */
    public void setValue(Object newValue) {
    }

    // Javadoc inherited
    public ValidationStatus validate() {
        return VALIDATE_OK;
    }

    // Javadoc inherited
    public boolean canProvideObject() {
        return true;
    }

    //javadoc inherited
    public void addStateChangeListener(StateChangeListener listener) {
        //not required as there are no configurable values - state
        //will not change
    }

    //javadoc inherited
    public void removeStateChangeListener(StateChangeListener listener) {
        //not required as there are no configurable values - state
        //will not change
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10708/1	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 08-Dec-05	10666/2	ibush	VBM:2005120209 Disable new style wizard add button if all fields are empty

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
