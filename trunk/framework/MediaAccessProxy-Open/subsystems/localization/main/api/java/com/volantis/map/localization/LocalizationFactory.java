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
package com.volantis.map.localization;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.log.Log4jLogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Used to create instances of localizable aware classes
 */
public class LocalizationFactory {
    /**
     * Used to identify the Volantis product identifier
     */
    private static final String ID = "map";

    /**
     * Creates a {@link com.volantis.synergetics.log.LogDispatcher} instance.
     * @param clientClass the class of  client that requires logging facilities
     * @return A LogDispatcher
     */
    public static LogDispatcher createLogger(Class clientClass) {
        return new Log4jLogDispatcher(clientClass, ID);
    }

    /**
     * Creates a {@link com.volantis.synergetics.localization.ExceptionLocalizer} instance.
     * @param clientClass the class of  client that requires exception messages
     * to be localized
     * @return An ExcpetionLocalizer instance
     */
    public static ExceptionLocalizer
                createExceptionLocalizer(Class clientClass) {
            return new ExceptionLocalizer(clientClass, ID);
    }

}
