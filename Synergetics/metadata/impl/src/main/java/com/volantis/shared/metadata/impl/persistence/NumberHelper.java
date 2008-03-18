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
package com.volantis.shared.metadata.impl.persistence;

import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * A helper class for handling Number objects
 */
public class NumberHelper {

    private NumberHelper() {
        // hide it
    }

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(NumberHelper.class);


    // Javadoc inherited.
    public static String getAsString(Number number) {
        char prefix;
        if (number instanceof Double) {
            prefix = 'D';
        } else if (number instanceof Float) {
            prefix = 'F';
        } else if (number instanceof Integer) {
            prefix = 'I';
        } else if (number instanceof Long) {
            prefix = 'L';
        } else if (number instanceof Byte) {
            prefix = 'B';
        } else if (number instanceof Short) {
            prefix = 'S';
        } else if (number instanceof BigInteger) {
            prefix = 'K';
        } else if (number instanceof BigDecimal) {
            prefix = 'P';
        } else {
            throw new IllegalStateException(EXCEPTION_LOCALIZER.format(
                "unknown-number", number.getClass().getName()));
        }
        return prefix + number.toString();
    }

    /**
     * Set the value of the Number from a string
     *
     * @param value the value of the number to set
     */
    public static Number setFromString(String value) {
        String number = value.substring(1);
        Number result;
        char type = value.charAt(0);
        switch (type) {
            case'D':
                result = new Double(number);
                break;
            case'F':
                result = new Float(number);
                break;
            case'I':
                result = new Integer(number);
                break;
            case'L':
                result = new Long(number);
                break;
            case'B':
                result = new Byte(number);
                break;
            case'S':
                result = new Short(number);
                break;
            case'K':
                result = new BigInteger(number);
                break;
            case'P':
                result = new BigDecimal(number);
                break;
            default:
                throw new IllegalStateException(EXCEPTION_LOCALIZER.format(
                    "unknown-number", "" + type));
        }
        return result;
    }


}
