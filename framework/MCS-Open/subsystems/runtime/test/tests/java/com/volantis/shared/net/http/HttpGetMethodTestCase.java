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

package com.volantis.shared.net.http;

import com.volantis.shared.system.SystemClockMock;
import com.volantis.shared.time.Period;
import com.volantis.shared.time.Time;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.InterruptedIOException;

/**
 * Test cases for {@link HttpGetMethod}.
 */
public class HttpGetMethodTestCase
        extends TestCaseAbstract {

    /**
     * Ensure that it times out if executing the method causes an exception
     * after the connection timeout has passed.
     */
    public void testTimeOut() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final SystemClockMock clockMock =
                new SystemClockMock("clockMock", expectations);

        final MethodExecuterMock methodExecuterMock =
                new MethodExecuterMock("methodExecuterMock", expectations);

        GetMethod getMethod = new GetMethod();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        clockMock.expects.getCurrentTime().returns(Time.inMilliSeconds((1000)));
        methodExecuterMock.expects.execute(getMethod)
                .fails(new InterruptedIOException("timed out"));

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        HttpGetMethod method = new HttpGetMethodImpl(clockMock,
                methodExecuterMock, getMethod,
                "http://www.volantis.com:8080/",
                Period.inSeconds(100));

        HttpStatusCode statusCode = method.execute();
        assertEquals(HttpStatusCode.RESPONSE_TIMED_OUT, statusCode);
    }

}
