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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.model.validation;

/**
 * An internationalized message.
 *
 * @mock.generate 
 */
public interface I18NMessage {

    /**
     * The language independent message key.
     *
     * <p>This may be used to determine how this message may be processed.</p>
     *
     * @return The language independent message key.
     */
    String getKey();

    /**
     * Get the number of arguments used to create the i18n message.
     *
     * @return The number of arguments.
     */
    int getArgumentCount();

    /**
     * Get an argument.
     *
     * @param argument The index of the argument.
     * @return The value of the argument.
     * @throws IndexOutOfBoundsException if the argument is greater than or
     *                                   equal to {@link #getArgumentCount}.
     */
    Object getArgument(int argument);

    /**
     * Get the internationalized message.
     *
     * @return The i18n message.
     */
    String getMessage();

    /**
     * Get the message as a string.
     *
     * <p>If the message could be internationalized then return the
     * internationalized message, otherwise return the message key and the
     * arguments.</p>
     *
     * @return A string representation of the message.
     */
    String toString();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9992/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 26-Oct-05	9961/3	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 25-Oct-05	9961/1	pduffin	VBM:2005101811 Added diagnostic support and some commands

 ===========================================================================
*/
