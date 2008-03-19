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

package com.volantis.mcs.themes;

import com.volantis.mcs.themes.constraints.Constraint;

/**
 * A selector that matches an attribute name and optional value.
 *
 * <p>Selects an element based on the value of its attributes. Examples of
 * attribute selectors in CSS would include:
 * <ul>
 * <li><code>[href]</code></li>
 * <li><code>[align=right]</code></li>
 * <li><code>[lang|=fr]</code></li>
 * </ul>
 * </p>
 *
 * @mock.generate base="Selector"
 */
public interface AttributeSelector extends Selector, Namespaced {

    /**
     * Gets the name of the attribute for which the test is taking place.
     *
     * @return The name of the attribute for which the test is taking place
     */
    public String getName();

    /**
     * Sets the name of the attribute for which the test is taking place.
     *
     * @param newName The name of the attribute for which the test is taking
     *                place
     */
    public void setName(String newName);

    /**
     * Retrieve the {@link Constraint} which should be used to
     * determine if this selector should match.
     *
     * @return Constraint which is used to determine what type of
     * comparison should take place
     */
    Constraint getConstraint();

    /**
     * Set the {@link com.volantis.mcs.themes.constraints.Constraint} which should be used to determine
     * if this selector should match.
     *
     * @param constraint    which is used to determine what type of comparison
     *                      should take place
     */
    void setConstraint(Constraint constraint);

    /**
     * Convenience method to allow existing legacy code to access the action
     * associated with this selector in the old way. Will create a new
     * {@link com.volantis.mcs.themes.constraints.Constraint} (using the given
     * action to determine the type of constraint to create) and set it as
     * this attributes selector's constraint. If the attribute selector action
     * enumeration value is incorrectly mapped to a {@link Constraint} class,
     * this may fail to create the constraint (this should never happen). In
     * this case an error will be logged.
     *
     * @param action    AttributeSelectorActionEnum corresponding to the action
     *                  for this attribute
     * @param value     The required value to set on the new {@link Constraint}
     */
    void setConstraint(AttributeSelectorActionEnum action, String value);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/1	pduffin	VBM:2005083007 Fixed issue with build

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
