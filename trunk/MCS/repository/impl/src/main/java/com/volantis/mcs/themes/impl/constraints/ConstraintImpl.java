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
package com.volantis.mcs.themes.impl.constraints;

import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.jibx.JiBXBase;
import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.mcs.themes.constraints.Set;

/**
 * Defines a constraint against which a value can be evaluated to determine if
 * the constraint is satisfied.
 *
 * @todo Treat set differently to others, don't force it to have a value.
 */
public abstract class ConstraintImpl extends JiBXBase implements Constraint {

    /**
     * The value to compare against.
     */
    protected String value;

    /**
     * No-arg constructor required by JiBX.
     */
    public ConstraintImpl() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // Requirement 1: If this constraint should have a value, then it does.
        if (value == null && requiresValue()) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                    context.createMessage("theme-attribute-selector-no-value"));
        }

        // Requirement 2: If this constraint shouldn't have a value,
        // then it doesn't.
        if (value != null && !requiresValue()) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                    context.createMessage("theme-attribute-selector-value"));
        }
    }

    // Javadoc inherited.
    public abstract String toString();

    public boolean requiresValue() {
        return !(this instanceof Set);
    }

    void jibxPostSet() {
        // Normalize the value until the bindings have been changed to treat
        // Set/Exists as special.
        if ("".equals(value)) {
            value = null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 ===========================================================================
*/
