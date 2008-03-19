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
package com.volantis.mcs.context;

import com.volantis.shared.system.SystemClock;
import com.volantis.shared.system.SystemClockMock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

/**
 * Test case for ResponseCachingDirectives.
 */
public class ResponseCachingDirectivesTestCase extends TestCaseAbstract {

    public void testEnabled() {
        final ResponseCachingDirectives rcd =
            new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        assertFalse(rcd.isEnabled());
        rcd.enable();
        assertTrue(rcd.isEnabled());
        rcd.disable();
        assertFalse(rcd.isEnabled());
        rcd.enable();
        assertFalse(rcd.isEnabled());
    }

    public void testPriorityMaxAge() {
        final SystemClockMock clockMock =
            new SystemClockMock("clockMock", expectations);
        clockMock.expects.getCurrentTime().returns(
            Time.inMilliSeconds(System.currentTimeMillis())).any();
        final ResponseCachingDirectives rcd =
            new ResponseCachingDirectives(clockMock);

        assertNull(rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(100),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Period.inSeconds(100), rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(200),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Period.inSeconds(100), rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(50),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Period.inSeconds(50), rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(150),
            ResponseCachingDirectives.PRIORITY_HIGH);
        assertEquals(Period.inSeconds(150), rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(50),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Period.inSeconds(150), rcd.getTimeToLive());
    }

    public void testPriorityExpires() {
        final SystemClockMock clockMock =
            new SystemClockMock("clockMock", expectations);
        final long baseTime = System.currentTimeMillis();
        clockMock.expects.getCurrentTime().returns(
            Time.inMilliSeconds(baseTime)).any();
        final ResponseCachingDirectives rcd =
            new ResponseCachingDirectives(clockMock);

        assertNull(rcd.getExpires());

        rcd.setExpires(Time.inMilliSeconds(baseTime + 1000),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(baseTime + 1000), rcd.getExpires());

        rcd.setExpires(Time.inMilliSeconds(baseTime + 2000),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(baseTime + 1000), rcd.getExpires());

        rcd.setExpires(Time.inMilliSeconds(baseTime + 500),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(baseTime + 500), rcd.getExpires());

        rcd.setExpires(Time.inMilliSeconds(baseTime + 1500),
            ResponseCachingDirectives.PRIORITY_HIGH);
        assertEquals(Time.inMilliSeconds(baseTime + 1500), rcd.getExpires());

        rcd.setExpires(Time.inMilliSeconds(baseTime + 1000),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(baseTime + 1500), rcd.getExpires());
    }

    public void testMaxAge() {
        final SystemClockMock clockMock =
            new SystemClockMock("clockMock", expectations);
        final Time[] time =
            new Time[]{Time.inMilliSeconds(System.currentTimeMillis())};
        clockMock.expects.getCurrentTime().does(new MethodAction(){
                public Object perform(final MethodActionEvent event) {
                    return time[0];
                }
            }).any();
        final ResponseCachingDirectives rcd =
            new ResponseCachingDirectives(clockMock);

        assertNull(rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(100),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Period.inSeconds(100), rcd.getTimeToLive());

        time[0] = time[0].addPeriod(Period.inSeconds(10));
        assertEquals(Period.inSeconds(90), rcd.getTimeToLive());

        time[0] = time[0].addPeriod(Period.inSeconds(-20));
        assertEquals(Period.inSeconds(110), rcd.getTimeToLive());
    }

    public void testExpires() {
        final SystemClockMock clockMock =
            new SystemClockMock("clockMock", expectations);
        final Time[] time =
            new Time[]{Time.inMilliSeconds(System.currentTimeMillis())};
        clockMock.expects.getCurrentTime().does(new MethodAction(){
                public Object perform(final MethodActionEvent event) {
                    return time[0];
                }
            }).any();
        final ResponseCachingDirectives rcd =
            new ResponseCachingDirectives(clockMock);

        assertNull(rcd.getExpires());

        final Time expires = time[0].addPeriod(Period.inSeconds(50));
        rcd.setExpires(expires, ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(expires, rcd.getExpires());

        time[0] = time[0].addPeriod(Period.inSeconds(10));
        assertEquals(expires, rcd.getExpires());

        time[0] = time[0].addPeriod(Period.inSeconds(-20));
        assertEquals(expires, rcd.getExpires());
    }

    public void testMaxAgeAndExpires() {
        final SystemClockMock clockMock =
            new SystemClockMock("clockMock", expectations);
        final long time = System.currentTimeMillis();
        clockMock.expects.getCurrentTime().returns(
            Time.inMilliSeconds(time)).any();
        final ResponseCachingDirectives rcd =
            new ResponseCachingDirectives(clockMock);

        assertNull(rcd.getExpires());
        assertNull(rcd.getTimeToLive());

        rcd.setExpires(Time.inMilliSeconds(time + 50000),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(time + 50000), rcd.getExpires());
        assertEquals(Period.inSeconds(50), rcd.getTimeToLive());

        rcd.setMaxAge(Period.inSeconds(40),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(time + 40000), rcd.getExpires());
        assertEquals(Period.inSeconds(40), rcd.getTimeToLive());

        rcd.setExpires(Time.inMilliSeconds(time + 30000),
            ResponseCachingDirectives.PRIORITY_NORMAL);
        assertEquals(Time.inMilliSeconds(time + 30000), rcd.getExpires());
        assertEquals(Period.inSeconds(30), rcd.getTimeToLive());
    }
}
