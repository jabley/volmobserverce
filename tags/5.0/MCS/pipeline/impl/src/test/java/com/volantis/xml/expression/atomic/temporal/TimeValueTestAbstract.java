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
 * Base for {@link DateTimeValue} test cases.
 */
public abstract class TimeValueTestAbstract extends TestCaseAbstract {

    /**
     * Specialized test cases should implement this method to return
     * an instance of the TimeValue for given {@link String} to be tested.
     *
     * @param value the value that will be used to initialize the TimeValue
     * @return a TimeValue
     */
    public abstract TimeValue getTimeValue(String value);
    
    /**
     * Specialized test cases should implement this method to return
     * an instance of the TimeValue for given {@link Calendar} to be tested.
     *
     * @param value the value that will be used to initialize the TimeValue
     * @return a TimeValue
     */
    public abstract TimeValue getTimeValue(Calendar value);

    /**
     * Ensure initialization works correctly
     *
     * @throws Exception
     */
    public void testInitialization() throws Exception {

        {
            TimeValue v = getTimeValue("12:26:03.7Z");
            doTestTime(v, 12, 26, 3, 700, true,
                    TimeZone.getTimeZone("UTC"), "12:26:03.7Z");
        }
        {
            TimeValue v = getTimeValue("23:56:32");
            doTestTime(v, 23, 56, 32, 00, false, null, "23:56:32.0");
        }
        {
            TimeValue v = getTimeValue("23:56:32+12:00");
            doTestTime(v, 23, 56, 32, 00, true,
                    TimeZone.getTimeZone("GMT+12:00"), "23:56:32.0+12:00");
        }
        {
            TimeValue v = getTimeValue("23:56:32.8599");
            // the 8599 gets rounded up to 860
            doTestTime(v, 23, 56, 32, 860, false,
                       null, "23:56:32.86");
        }
        {
            TimeValue v = getTimeValue("23:56:32.099");
            doTestTime(v, 23, 56, 32, 99, false,
                       null, "23:56:32.099");
        }
        {
            Calendar calendar = Calendar.getInstance(
                    TimeZone.getTimeZone("UTC"));
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 56);
            calendar.set(Calendar.SECOND, 32);
            calendar.set(Calendar.MILLISECOND, 860);
            TimeValue value = getTimeValue(calendar);
            doTestTime(value, 23, 56, 32, 860, true,
                    TimeZone.getTimeZone("UTC"), "23:56:32.86Z");
        }

    }

    private void doTestTime(TimeValue t, int hours, int minutes,
                            int seconds, int millis, boolean isTimeZoned,
                            TimeZone tz, String expectedString) {
        assertEquals(hours, t.getHours());
        assertEquals(minutes, t.getMinutes());
        assertEquals(seconds, t.getSeconds());
        assertEquals(millis, t.getMilliseconds());
        if (isTimeZoned) {
            assertTrue(t.isTimezoned());
            assertEquals(tz, t.getTimeZone());
        } else {
            assertNull(t.getTimeZone());
        }
        assertEquals(expectedString, t.toString());
    }

}
