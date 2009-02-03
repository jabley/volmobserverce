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

package com.volantis.map.sti.transcoder;

import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Indicates transcoding failure.
 */
public class TranscoderException extends Exception {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
                TranscoderException.class);

    /**
     * Accepts a localization key, an array of
     * substitution parameters, and a cause exception
     *
     * @param key   the message localization key
     * @param params array of substitution params
     * @param cause the cause excception
     */
    public TranscoderException(
        String key, Object[] params, Throwable cause) {
        super(EXCEPTION_LOCALIZER.format(key, params), cause);
    }

    /**
     * Accepts a localization key, an array of
     * substitution parameters
     *
     * @param key   the message localization key
     * @param params array of substitution params
     */
    public TranscoderException(
        String key, Object[] params) {
        super(EXCEPTION_LOCALIZER.format(key, params));
    }

    /**
     * Constructor just takes a throwable
     * 
     * @param t
     */
    public TranscoderException(Throwable t) {
        super(t);
    }
}
