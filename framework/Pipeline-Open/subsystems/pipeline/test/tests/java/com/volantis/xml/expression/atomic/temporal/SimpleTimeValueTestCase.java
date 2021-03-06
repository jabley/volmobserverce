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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.xml.expression.ExpressionFactory;

import java.util.Calendar;

/**
 * Test the Simple implementation of the TimeValue
 */
public class SimpleTimeValueTestCase extends TimeValueTestAbstract {

    /**
     * The factory to use
     */
    private ExpressionFactory factory = ExpressionFactory.getDefaultInstance();

    // javadoc inherited
    public TimeValue getTimeValue(String value) {
        return factory.createTimeValue(value);
    }

    // javadoc inherited
    public TimeValue getTimeValue(Calendar value) {
        return factory.createTimeValue(value);
    }
}
