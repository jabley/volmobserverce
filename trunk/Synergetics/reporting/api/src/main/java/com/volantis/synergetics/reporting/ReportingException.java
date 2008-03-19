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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.MessageLocalizer;

/**
 * A localized exeption thrown by the reporting package.
 * 
 * @volantis-api-include-in PublicAPI
 */
public class ReportingException extends Exception {

    /**
     * Localize messages
     */
    private static final MessageLocalizer MESSAGE_LOCALIZER =
        LocalizationFactory.createMessageLocalizer(ReportingException.class);


    /**
     * Create a new ReportingException using the specified message key and
     * parameter
     *
     * @param key   the message key
     * @param param the substitution parameter for the message
     */
    public ReportingException(String key, Object param) {
        this(key, new Object[]{param});
    }

    /**
     * Create a new ReportingException using the specified message key and
     * parameters
     *
     * @param key    the message key
     * @param params the substitution parameters for the message
     */
    public ReportingException(String key, Object[] params) {
        super(MESSAGE_LOCALIZER.format(key, params));
    }
}
