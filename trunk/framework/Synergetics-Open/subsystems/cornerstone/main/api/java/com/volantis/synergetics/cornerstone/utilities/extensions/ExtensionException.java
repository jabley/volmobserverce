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
 * Copyright Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.cornerstone.utilities.extensions;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * This exception is thrown to indicate that a problem occurred loading the
 * extension factory. This exception localizes messages passed in by thier key
 *
 * @volantis-api-include-in InternalAPI
 */
public class ExtensionException extends Exception {

    /**
     * Used for localizing messages
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(ExtensionException.class);

    // Javadoc unnecessary
    public ExtensionException() {
        super();
    }

    /**
     * Create an exception with the message specified by the supplied key
     *
     * @param key the key for the message to use
     */
    public ExtensionException(String key) {
        super(EXCEPTION_LOCALIZER.format(key));
    }

    /**
     * Create an exception with the message specified by the supplied key.
     *
     * @param key the key for the message to use
     * @param cause the exception that caused this exception to be thrown
     */
    public ExtensionException(String key, Object params, Throwable cause) {
        super(EXCEPTION_LOCALIZER.format(key, params), cause);
    }

    // Javadoc unnecessary
    public ExtensionException(String key, Throwable cause) {
        super(EXCEPTION_LOCALIZER.format(key), cause);
    }

    // Javadoc unnecessary
    public ExtensionException(Throwable cause) {
        super(cause);
    }
}
