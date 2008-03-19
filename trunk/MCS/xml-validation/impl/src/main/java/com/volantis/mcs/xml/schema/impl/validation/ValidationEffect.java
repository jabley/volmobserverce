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

package com.volantis.mcs.xml.schema.impl.validation;

public class ValidationEffect {
    public static final ValidationEffect CONSUMED = new ValidationEffect(
            "CONSUMED", true, false);
    public static final ValidationEffect CONSUMED_SATISFIED = new ValidationEffect(
            "CONSUMED_SATISFIED", true, true);
    public static final ValidationEffect WOULD_FAIL = new ValidationEffect(
            "WOULD_FAIL", false, false);
    public static final ValidationEffect WOULD_SATISFY = new ValidationEffect(
            "WOULD_SATISFY", false, true);

    private final String myName; // for debug only
    private final boolean consumed;
    private final boolean satisfied;

    private ValidationEffect(String name, boolean consumed, boolean satisfied) {
        myName = name;
        this.consumed = consumed;
        this.satisfied = satisfied;
    }

    public String toString() {
        return myName;
    }

    public boolean wasToConsume() {
        return consumed;
    }

    public boolean wasOrWouldBeToSatisfyValidator() {
        return satisfied;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
