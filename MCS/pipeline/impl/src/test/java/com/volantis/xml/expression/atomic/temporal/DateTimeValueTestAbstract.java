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
package com.volantis.xml.expression.atomic.temporal;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Base for {@link com.volantis.xml.expression.atomic.temporal.DateTimeValue} test cases.
 */
public abstract class DateTimeValueTestAbstract extends TestCaseAbstract {

    /**
     * Specialized test cases should implement this method to return
     * an instance of the DateTimeValue for given {@link String} to be tested.
     *
     * @param value the value that will be used to initialize the DateTimeValue
     * @return a DateTimeValue
     */
    public abstract DateTimeValue getDateTimeValue(String value);
    
    /**
     * Specialized test cases should implement this method to return
     * an instance of the DateTimeValue for given {@link Calendar} to be
     * tested.
     *
     * @param value the value that will be used to initialize the DateTimeValue
     * @return a DateTimeValue
     */
    public abstract DateTimeValue getDateTimeValue(Calendar value);

    /**
     * Ensure initialization works correctly
     *
     * @throws Exception
     */
    public void testInitialization() throws Exception {
        String date = "2006-02-06T12:26:03.7Z";
        DateTimeValue value = getDateTimeValue(date);
        performTest(value);
        
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.YEAR, 2006);
        // adjust the month number as the Calendar class requires
        calendar.set(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 26);
        calendar.set(Calendar.SECOND, 3);
        calendar.set(Calendar.MILLISECOND, 700);
        value = getDateTimeValue(calendar);
        performTest(value);
    }

    /**
     * Performs test on given {@link DateTimeValue}.
     * 
     * @param value the value to be tested
     */
    private void performTest(DateTimeValue value) {
        assertEquals(2006, value.getYear());
        assertEquals(02, value.getMonth());
        assertEquals(06, value.getDay());
        assertEquals(12, value.getHours());
        assertEquals(26, value.getMinutes());
        // test for equality with a error margin
        assertEquals(03, value.getSeconds());
        assertEquals(700, value.getMilliseconds());
        assertEquals(TimeZone.getTimeZone("UTC"), value.getTimeZone());
    }
}

