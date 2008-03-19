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
package com.volantis.map.agent;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Indicate that a exception occured when attempting to use the Media Agent
 */
public class MediaAgentException extends Exception {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(MediaAgentException.class);

    /**
     * Accepts a localization key a substitution parameter (or an array of
     * substitution parameters, and a cause exception
     *
     * @param key the message localization key
     * @param param the substitution parameter or array of substitution params
     * @param cause the cause excception
     */
    public MediaAgentException(String key, Object param, Throwable cause) {
        super(EXCEPTION_LOCALIZER.format(key, param), cause);
    }

    /**
     * Accepts a localization key.
     *
     * @param key   the message localization key
     */
    public MediaAgentException(String key) {
        this(key, null, null);
    }

    /**
     * Accepts a localization key a substitution parameter (or an array of
     * substitution parameters.
     *
     * @param key   the message localization key
     * @param param the substitution parameter or array of substitution params
     */ 
    public MediaAgentException(String key, Object param) {
        this(key, param, null);
    }           
}
