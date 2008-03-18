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
package com.volantis.synergetics.localization;

/**
 * Class that provides localized messages for exceptions
 */
public class ExceptionLocalizer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * A MessageLocalizer that will be used to provide the localized messages
     * for exceptions
     */
    private MessageLocalizer messageLocalizer;

    /**
     * Creates an ExceptionLocalizer that can be used to obtain localized
     * messages for exception
     *
     * @param clientClass        the <code>Class</code> of the client that
     *                           requires exception localization facilities
     * @param productIndentifier an identifier used to identify the Volantis
     *                           product that requires exception messages to be
     *                           localized
     */
    public ExceptionLocalizer(Class clientClass, String productIndentifier) {
        this.messageLocalizer = MessageLocalizer.getLocalizer(clientClass,
                                                              productIndentifier);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle.
     *
     * @param key the mesage key
     * @return the localized message
     */
    public String format(String key) {
        return messageLocalizer.format(Category.EXCEPTION, key);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArg
     * parameter will be substituted into the message at it's single
     * substitution point.
     *
     * @param key             the message key
     * @param substitutionArg the single substitution argument
     * @return the localized formatted message
     */
    public String format(String key, Object substitutionArg) {

        return messageLocalizer.format(Category.EXCEPTION, key,
                                       substitutionArg);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArgs
     * will be substituted into the message at it's substitution points.
     *
     * @param key              the mesage key
     * @param substitutionArgs the substitution arguments
     * @return the localized formatted message
     */
    public String format(String key, Object[] substitutionArgs) {

        return messageLocalizer.format(Category.EXCEPTION, key,
                                       substitutionArgs);
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	343/7	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/5	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/3	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
