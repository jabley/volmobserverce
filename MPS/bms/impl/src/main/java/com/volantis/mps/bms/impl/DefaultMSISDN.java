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

import com.volantis.mps.bms.MSISDN;
import com.volantis.mps.bms.MalformedAddressException;

public class DefaultMSISDN implements MSISDN {

    private String value;

    public DefaultMSISDN(String msisdn) throws MalformedAddressException {
        setValue(msisdn);
    }

    // javadoc inherited
    public void setValue(String value) throws MalformedAddressException {
        if (null == value) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
    }

    // javadoc inherited
    public String getValue() {
        return value;
    }
}
