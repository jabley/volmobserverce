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

/**
 * Represents an XPath 2.0 double value.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation in user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface DoubleValue extends NumericValue {

    /**
     * The NOT_A_NUMBER value
     */
    public static final DoubleValue NOT_A_NUMBER =
            new SimpleDoubleValue(ExpressionFactory.getDefaultInstance(),
                                  Double.NaN);

    /**
     * The positive infinity value
     */
    public static final DoubleValue POSITIVE_INFINITY =
            new SimpleDoubleValue(ExpressionFactory.getDefaultInstance(),
                                  Double.POSITIVE_INFINITY);

    /**
     * The negative infinity value
     */
    public static final DoubleValue NEGATIVE_INFINITY =
            new SimpleDoubleValue(ExpressionFactory.getDefaultInstance(),
                                  Double.NEGATIVE_INFINITY);

    /**
     * Get the value of this object as a Java double.
     *
     * @return The value of this object as a Java double.
     */
    public double asJavaDouble();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 ===========================================================================
*/
