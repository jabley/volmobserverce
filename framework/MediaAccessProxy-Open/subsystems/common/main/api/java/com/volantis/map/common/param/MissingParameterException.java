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
package com.volantis.map.common.param;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.map.localization.LocalizationFactory;

/**
 * Thrown to indicate that the parameter that was requested is missing
 */
public class MissingParameterException extends Exception {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(MissingParameterException.class);

    // javadoc inherited
    public MissingParameterException() {
        super();
    }

    // javadoc inherited
    public MissingParameterException(Throwable cause) {
        super(cause);
    }

    // javadoc inherited
    public MissingParameterException(String key, String param, Throwable cause) {
        super(MissingParameterException.EXCEPTION_LOCALIZER.format(key, param), cause);
    }

    // javadoc inherited
    public MissingParameterException(String key, String[] params, Throwable cause) {
        super(MissingParameterException.EXCEPTION_LOCALIZER.format(key, params), cause);
    }

    // javadoc inherited
    public MissingParameterException(String key, Throwable cause) {
        super(MissingParameterException.EXCEPTION_LOCALIZER.format(key, null), cause);
    }

    // javadoc inherited
    public MissingParameterException(String key) {
        super(MissingParameterException.EXCEPTION_LOCALIZER.format(key));
    }

    // javadoc inherited
    public MissingParameterException(String key, String param) {
        this(key, param, null);
    }

    // javadoc inherited
    public MissingParameterException(String key, String params[]) {
        this(key, params, null);
    }

}
