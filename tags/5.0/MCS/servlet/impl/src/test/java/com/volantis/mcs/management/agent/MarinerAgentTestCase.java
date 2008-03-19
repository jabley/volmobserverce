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
package com.volantis.mcs.management.agent;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.utilities.JDKProperties;

/**
 * This class is responsible for testing the behaviour of {@link MarinerAgent}.
 */
public class MarinerAgentTestCase extends TestCaseAbstract {

    private static final String PASSWORD = "password";

    /**
     * This test case will use any free port
     */
    private static final int ANY_FREE_PORT = 0;

    /**
     * Constant representing 1 second
     */
    private static final int ONE_SECOND = 1000;

    /**
     * Used to obtain information about the JDK being used.
     */
    private JDKProperties jdkProperties = new JDKProperties();

    /**
     * The maximum time that we will wait for the Agent to start or stop.
     */
    private static final int MAX_TIME_TO_WAIT_IN_SECONDS = 30;

    public void testStopAgentWhenInBlockingState() throws Exception {

        Volantis volantis = new Volantis();

        MarinerAgent agent = new MarinerAgent(PASSWORD, null,
                ANY_FREE_PORT, volantis);

        boolean shouldRunTest = true;

        // Is a 1.3 JDK being used?
        if (jdkProperties.getJavaSpecificationVersion().startsWith("1.3")) {

            shouldRunTest = jdkProperties.isIBM1_3JDKBuild20041210orLater();
        }

        if (shouldRunTest) {
            agent.start();

            // Give the run() method time to setup the server socket.
            int timeSpentWaiting = 0;
            while(!agent.isServiceRunning() &&
                    timeSpentWaiting < MAX_TIME_TO_WAIT_IN_SECONDS) {

                Thread.sleep(ONE_SECOND);
                timeSpentWaiting++;
            }

            agent.stopAgent();

            timeSpentWaiting = 0;
            // Give the agent time to stop.
            while (agent.isAlive() &&
                    timeSpentWaiting < MAX_TIME_TO_WAIT_IN_SECONDS) {

                Thread.sleep(ONE_SECOND);
                timeSpentWaiting++;
            }
            assertFalse("Mariner agent should have stopped.", agent.isAlive());
       } else {
            System.out.println("testStopAgentWhenInBlockingState has not " +
                               "been executed as the JDK version 1.3 being " +
                               "used contains a big which results " +
                               "in this test failing.");
       }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Sep-05	9514/1	pcameron	VBM:2005071113 Added host parameter to MarinerAgent

 12-Sep-05	9420/3	pcameron	VBM:2005071113 Added host parameter to MarinerAgent

 22-Jun-05	8866/1	rgreenall	VBM:2005062004 Merge from 323: Gaurding MarinerAgent against test case failure when using JDK with bug 4386498.

 20-Jun-05	8840/1	rgreenall	VBM:2005061401 Merge from 323: BindException exception thrown when redeploying on WebSphere.

 17-Jun-05	8804/3	rgreenall	VBM:2005061401 Fix does not work on Weblogic using Sun's 1.3 JDK due to a bug in ServerSocket. Log warning when Sun's 1.3 JDK is used.

 16-Jun-05	8804/1	rgreenall	VBM:2005061401 BindException exception thrown when redeploying on WebSphere

 ===========================================================================
*/

