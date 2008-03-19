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
package com.volantis.mcs.xdime.xforms.model;

/**
 * Type safe enumeration which defines the valid output states of an
 * XFormModel. It is either before any controls have been written out,
 * during, or after.
 */
public class ModelOutputState {

    /**
     * Initialize a new instance. Private so that this class can act as a
     * typesafe enumeration.
     */
    private ModelOutputState() {}

    public static final ModelOutputState BEFORE =
            new ModelOutputState();
    public static final ModelOutputState DURING =
            new ModelOutputState();
    public static final ModelOutputState AFTER =
            new ModelOutputState();

   /**
     * Returns the next output state to the one supplied. The progression
     * is BEFORE->DURING->AFTER.
     *
     * @return ModelOutputState the next state in the sequence
     */
    public final ModelOutputState getNextState() {

        ModelOutputState newState = this;
        if (this.equals(ModelOutputState.BEFORE)) {
            newState = ModelOutputState.DURING;
        } else if (this.equals(ModelOutputState.DURING)) {
            newState = ModelOutputState.AFTER;
        }
        return newState;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-05	9637/3	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
