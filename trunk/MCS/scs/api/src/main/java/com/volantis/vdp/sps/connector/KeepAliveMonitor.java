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
package com.volantis.vdp.sps.connector;

import com.volantis.vdp.cli.SecurePublisher;
import com.volantis.vdp.cli.SecurePublisher;

import java.util.Date;

/**
 * 
 * This class monitoring connection and if connection
 * is not active in long time then reconnect to SCP
 * User: rstroz01
 * Date: 2006-01-10
 * Time: 14:15:27
 */
public class KeepAliveMonitor implements Runnable {
    private static boolean run = false;
    private static int TIMEOUT = 600000;

    private static int TIMEOUT_MONIT = 30000;

    private static Date lastMonit = new Date();
    private static long averageTimeout = 0;
    private SecurePublisher securePublisher;

    public KeepAliveMonitor(SecurePublisher securePublisher) {
        this.securePublisher = securePublisher;
    }

    /**
     * Method receive Kepp Alive monits
     */
    public synchronized static void monit() {
        Date monit = new Date();
        if (averageTimeout == 0) {
            averageTimeout = monit.getTime() - lastMonit.getTime();
        } else {
            averageTimeout = (averageTimeout +
                    (monit.getTime() - lastMonit.getTime())) / 2;
        }
        lastMonit = monit;
    }

    /**
     * Method running Secure Publisher Server monitor
     */
    public void run() {
        if (!run) {
            run = true;
            while (true) {
                Date monitor = new Date();
                while ((monitor.getTime() - lastMonit.getTime()) < TIMEOUT) {
                    try {
                        Thread.sleep(TIMEOUT_MONIT);
                    } catch (InterruptedException ex) {

                    }
                    monitor = new Date();
                }

                while (!restartServer()) {
                    try {
                        Thread.sleep(TIMEOUT_MONIT * 4);
                    } catch (InterruptedException ex) {

                    }
                }
            }
        }
    }

    // Restart Secure Publisher Server
    private boolean restartServer() {
        return securePublisher.startServer();
    }

    ;
}
