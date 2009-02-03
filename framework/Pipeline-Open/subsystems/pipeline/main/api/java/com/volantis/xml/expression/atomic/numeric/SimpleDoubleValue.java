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
package com.volantis.xml.expression.atomic.numeric;

import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.atomic.AtomicSequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.StringValueHelper;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * This is a simple, generic implementation of the DoubleValue interface.
 *
 * @see com.volantis.xml.expression.ExpressionFactory
 */
public final class SimpleDoubleValue extends AtomicSequence
        implements DoubleValue {
    /**
     * The value wrappered by this class.
     */
    private double value;

    /**
     * Initializes the new instance with the given parameters.
     *
     * @param factory the factory by which the value was created
     * @param value   the value to be wrappered
     */
    public SimpleDoubleValue(ExpressionFactory factory, double value) {
        super(factory);

        this.value = value;
    }

    // javadoc inherited
    public double asJavaDouble() {
        return value;
    }

    // javadoc inherited
    public StringValue stringValue() {
        return factory.createStringValue(xpathString());
    }

    /**
     * Get the XPath representation of the double.
     *
     * <p>XPath represents a number as follows:
     * <ul>
     * <li>NaN is converted to the string NaN</li>
     * <li>positive zero is converted to the string 0</li>
     * <li>negative zero is converted to the string 0</li>
     * <li>positive infinity is converted to the string Infinity</li>
     * <li>negative infinity is converted to the string -Infinity</li>
     * <li>if the number is an integer, the number is represented in decimal
     * form as a Number with no decimal point and no leading zeros, preceded
     * by a minus sign (-) if the number is negative</li>
     * <li>otherwise, the number is represented in decimal form as a Number
     * including a decimal point with at least one digit before the decimal
     * point and at least one digit after the decimal point, preceded by a
     * minus sign (-) if the number is negative; there must be no leading
     * zeros before the decimal point apart possibly from the one required
     * digit immediately before the decimal point; beyond the one required
     * digit after the decimal point there must be as many, but only as many,
     * more digits as are needed to uniquely distinguish the number from all
     * other IEEE 754 numeric values.</li>
     * </ol>
     *
     * @return The XPath representation of the double.
     */
    private String xpathString() {
        if (value != value) {
            // NaN cannot be compared with any value, even itself.
            return "NaN";
        } else if (value == -0D) {
            return "0";
        } else if (value == Double.POSITIVE_INFINITY) {
            return "Infinity";
        } else if (value == Double.NEGATIVE_INFINITY) {
            return "-Infinity";
        } else {
            NumberFormat format = new DecimalFormat(
                    "0",
                    StringValueHelper.DECIMAL_FORMAT_SYMBOLS);
            format.setMaximumFractionDigits(Integer.MAX_VALUE);
            return format.format(value);
        }
    }

    // javadoc inherited
    public void streamContents(ContentHandler contentHandler)
            throws SAXException {
        String string = xpathString();

        contentHandler.characters(string.toCharArray(), 0, string.length());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/1	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
