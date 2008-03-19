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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.directory.monitor;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Indicates that a problem occured when attempting to register a directory for
 * monitoring.
 *
 * <p>
 *  This class produces localized exceptions
 * </p>
 */
public class RegistrationException extends Exception {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory
            .createExceptionLocalizer(RegistrationException.class);

    /**
     * Allows a cause to be set
     * 
     * @param cause
     */
    public RegistrationException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct with a message key and substitution parameters
     *
     * @param key
     * @param param
     */
    public RegistrationException(String key, Object param) {
        this(key, param, null);
    }

    /**
     * Construct with a message key, substitution parameters and a cause
     *
     * @param key
     * @param param
     * @param cause
     */
    public RegistrationException(String key, Object param, Throwable cause) {
        super(EXCEPTION_LOCALIZER.format(key, param), cause);
    }
}
