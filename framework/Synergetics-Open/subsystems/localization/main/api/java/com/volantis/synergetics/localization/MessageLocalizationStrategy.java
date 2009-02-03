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
package com.volantis.synergetics.localization;


/**
 * Interface that allows a message localization strategy to be defined
 */
public interface MessageLocalizationStrategy {

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle
     *
     * @param key      the mesage key
     * @param category the message Category
     * @return the localized message
     */
    String formatMessage(String key, Category category);

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArg
     * parameter will be substituted into the message at it's single
     * substitution point
     *
     * @param key             the mesage key
     * @param category        the message Category
     * @param substitutionArg the single substitution argument
     * @return the localized formatted message
     */
    String formatMessage(String key,
                         Category category,
                         Object substitutionArg);

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArgs
     * will be substituted into the message at it's substitution points.
     *
     * @param key              the mesage key
     * @param substitutionArgs the substitution arguments
     * @param category         the message Category
     * @return the localized formatted message
     */
    String formatMessage(String key,
                         Category category,
                         Object[] substitutionArgs);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
