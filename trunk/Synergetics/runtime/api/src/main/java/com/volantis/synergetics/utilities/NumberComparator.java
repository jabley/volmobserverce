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
package com.volantis.synergetics.utilities;

import java.util.Comparator;
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * Comparator that works on numbers.
 */
public class NumberComparator implements Comparator {

    public int compare(final Object o1, final Object o2) {
        if (!(o1 instanceof Number) || !(o2 instanceof Number)) {
            throw new ClassCastException("Numbers are expected");
        }
        final Number first = (Number) o1;
        final Number second = (Number) o2;
        int result;
        if (first.getClass().equals(second.getClass())) {
            // every known implementation of Number class implements Comparable
            result = ((Comparable) first).compareTo(second);
        } else if (first instanceof BigInteger) {
            final BigDecimal bigDecimal = new BigDecimal((BigInteger) first);
            if (second instanceof BigDecimal) {
                result = bigDecimal.compareTo((BigDecimal)second);
            } else {
                result = bigDecimal.compareTo(
                    new BigDecimal(second.doubleValue()));
            }
        } else if (first instanceof BigDecimal) {
            final BigDecimal bigDecimal = (BigDecimal) first;
            if (second instanceof BigInteger) {
                result = bigDecimal.compareTo(
                    new BigDecimal((BigInteger) second));
            } else {
                result = bigDecimal.compareTo(
                    new BigDecimal(second.doubleValue()));
            }
        } else {
            final double firstAsDouble = first.doubleValue();
            if (second instanceof BigDecimal) {
                result = new BigDecimal(firstAsDouble).compareTo(
                    (BigDecimal) second);
            } else if (second instanceof BigInteger) {
                result = new BigDecimal(firstAsDouble).compareTo(
                    new BigDecimal((BigInteger) second));
            } else {
                result = Double.compare(
                    firstAsDouble, second.doubleValue());
            }
        }
        return result;
    }
}
