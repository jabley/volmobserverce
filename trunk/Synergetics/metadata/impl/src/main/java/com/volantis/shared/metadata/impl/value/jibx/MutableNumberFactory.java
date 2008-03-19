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
package com.volantis.shared.metadata.impl.value.jibx;

import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * Factory to create mutable numbers from java.lang.Number objects.
 */
public class MutableNumberFactory {

    /**
     * Creates a new MutableNumber from the specified number. The class of the
     * created mutable number depends on the class of the specified number and
     * the value of the specified number.
     *
     * @param number the number to convert into a mutable number
     * @return the mutable number with the value of the specified number
     */
    public static MutableNumber createMutableNumber(final Number number) {
        final MutableNumber result;
        if (number instanceof Byte) {
            result = new MutableByte(number.byteValue());
        } else if (number instanceof Short) {
            result = new MutableShort(number.shortValue());
        } else if (number instanceof Integer) {
            result = new MutableInteger(number.intValue());
        } else if (number instanceof Long) {
            result = new MutableLong(number.longValue());
        } else if (number instanceof BigInteger) {
            result = new MutableBigInteger((BigInteger) number);
        } else if (number instanceof BigDecimal) {
            result = new MutableBigDecimal((BigDecimal) number);
        } else if (number instanceof Double) {
            result = new MutableDouble(number.doubleValue());
        } else if (number instanceof Float) {
            result = new MutableFloat(number.floatValue());
        } else {
            throw new IllegalStateException(
                "Invalid number type: " + number.getClass());
        }
        return result;
    }
}
