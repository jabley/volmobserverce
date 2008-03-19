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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import com.volantis.shared.system.SystemClock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;

import junit.framework.TestCase;

public class PipelineCacheStateTestCase extends TestCase {

    public void testLimitedTimeToLive() throws Exception {
        final SystemClock clock = SystemClock.getDefaultInstance();
        final Period period = CacheControlRule.calculateTimeToLive("2");    // 2 seconds

        final PipelineCacheState pcs = new PipelineCacheState(
            clock.getCurrentTime().addPeriod(period));

        assertFalse("Hasn't expired yet", pcs.hasExpired(clock));

        assertTrue("Has expired when we tweak the system time",
                pcs.hasExpired(new SystemClock() {

            // javadoc inherited
            public Time getCurrentTime() {
                // return now + 3 seconds
                return Time.inMilliSeconds(
                        clock.getCurrentTime().inMillis() + 3000);
            }
        }));
    }

    public void testNeverExpires()  throws Exception {
        final SystemClock clock = SystemClock.getDefaultInstance();

        PipelineCacheState pcs = new PipelineCacheState(Time.NEVER);

        assertFalse("Hasn't expired yet", pcs.hasExpired(clock));

        assertFalse("Hasn't expired when we tweak the system time",
                pcs.hasExpired(new SystemClock() {

            // javadoc inherited
            public Time getCurrentTime() {
                // return now + 60 seconds
                return Time.inMilliSeconds(
                        clock.getCurrentTime().inMillis() + 60 * 1000);
            }
        }));

    }
}
