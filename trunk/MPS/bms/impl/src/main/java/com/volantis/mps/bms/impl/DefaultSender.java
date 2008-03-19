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
import com.volantis.mps.bms.SMTPAddress;
import com.volantis.mps.bms.Sender;

/**
 * Default implementation
 */
public class DefaultSender implements Sender {

    private SMTPAddress smtpAddress;

    private MSISDN msisdn;

    /**
     * Thing for JiBX.
     */
    public DefaultSender() {
    }

    /**
     * Constructor used by the Factory Method to create a usable instance.
     *
     * @param msisdn      - may be null.
     * @param smtpAddress - may be null.
     */
    public DefaultSender(MSISDN msisdn, SMTPAddress smtpAddress) {
        this.msisdn = msisdn;
        this.smtpAddress = smtpAddress;
    }

    // javadoc inherited
    public SMTPAddress getSMTPAddress() {
        return this.smtpAddress;
    }

    // javadoc inherited
    public MSISDN getMSISDN() {
        return this.msisdn;
    }
}
