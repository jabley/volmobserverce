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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.devrep.localization;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.log.Log4jLogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;

/**
 * Used to create instances of localizable aware classes
 */
public class LocalizationFactory {
    /**
     * Used to identify the Volantis product identifier
     */
    private static final String ID = "DeviceRepository";

    /**
     * Creates a {@link LogDispatcher} instance.
     * @param clientClass the class of  client that requires logging facilities
     * @return A LogDispatcher
     */
    public static LogDispatcher createLogger(Class clientClass) {
        return new Log4jLogDispatcher(clientClass, ID);
    }

    /**
     * Creates a {@link ExceptionLocalizer} instance.
     * @param clientClass the class of  client that requires exception messages
     * to be localized
     * @return An ExcpetionLocalizer instance
     */
    public static ExceptionLocalizer
                createExceptionLocalizer(Class clientClass) {
            return new ExceptionLocalizer(clientClass, ID);
    }

    /**
     * Creates a {@link MessageLocalizer} instance.
     * @param clientClass the class of  client that requires exception messages
     * to be localized
     * @return An ExcpetionLocalizer instance
     */
    public static MessageLocalizer createMessageLocalizer(Class clientClass) {
            return MessageLocalizer.getLocalizer(clientClass, ID);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
