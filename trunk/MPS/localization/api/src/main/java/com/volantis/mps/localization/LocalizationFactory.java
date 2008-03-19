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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.localization;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.Log4jLogDispatcher;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Used to create instances of localizable aware classes
 */
public class LocalizationFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Used to identify the Volantis product identifier
     */
    private static final String ID = "mps";

    /**
     * Creates a {@link com.volantis.synergetics.log.LogDispatcher} instance.
     *
     * @param clientClass the class of client that requires logging facilities
     * @return A LogDispatcher
     */
    public static LogDispatcher createLogger(Class clientClass) {
        return new Log4jLogDispatcher(clientClass, ID);
    }

    /**
     * Creates a
     * {@link com.volantis.synergetics.localization.ExceptionLocalizer}
     * instance.
     *
     * @param clientClass the class of client that requires exception messages
     * to be localized
     * @return An ExceptionLocalizer instance
     */
    public static ExceptionLocalizer
            createExceptionLocalizer(Class clientClass) {
        return new ExceptionLocalizer(clientClass, ID);
    }

    /**
     * Creates a {@link com.volantis.synergetics.localization.MessageLocalizer}
     * instance.
     *
     * @param clientClass the class of client that requires messages
     *                    to be localized
     * @return An appropriate MessageLocalizer instance
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

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 ===========================================================================
*/
