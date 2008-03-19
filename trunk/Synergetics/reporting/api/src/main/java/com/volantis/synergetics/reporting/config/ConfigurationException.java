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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.reporting.config;

/**
 * Configuration exception
 */
public class ConfigurationException extends Exception {
    /**
     * Serialization
     */
    private static final long serialVersionUID = 8145268621747253593L;

    /**
     * Initializes the new instance.
     */
    public ConfigurationException() {
        super();
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param cause the cause exception. May be null
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the (already localized) message for the exception. May be
     *                null
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param message the (already localized) message for the exception. May be
     *                null
     * @param cause   the cause exception. May be null
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
