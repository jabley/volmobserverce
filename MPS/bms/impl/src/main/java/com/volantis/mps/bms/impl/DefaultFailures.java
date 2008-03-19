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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.bms.impl;

import com.volantis.mps.bms.Failures;
import com.volantis.mps.bms.Recipient;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation of Failures.
 */
public class DefaultFailures implements Failures {

    private Collection failures = new ArrayList();

    // javadoc inherited
    public Recipient[] getRecipients() {
        return (Recipient[]) failures.toArray(
                new Recipient[failures.size()]);
    }

    // javadoc inherited
    public void add(Recipient failure) {
        if (null == failure) {
            throw new IllegalArgumentException("failure cannot be null");
        }

        failures.add(failure);
    }

    // javadoc inherited
    public boolean isEmpty() {
        return failures.isEmpty();
    }
}
