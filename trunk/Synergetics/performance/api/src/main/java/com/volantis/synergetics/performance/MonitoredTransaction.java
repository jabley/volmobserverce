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
package com.volantis.synergetics.performance;

/**
 * This interface provides a wrapper for the underlying performance monitoring
 * engines representation of a Transaction.
 */

public interface MonitoredTransaction {

    /**
     * Indicates successful completion of the transaction.
     */
    public static int SUCCESSFUL = 0;

    /**
     * Indicates a failed transaction.
     */
    public static int FAILED = 1;


    /**
     * Indicates the start of the transaction causing response time measurment
     * recording to be started.
     */
    public void start();


    /**
     * Indicates the end of the transaction, causing response time measurment
     * to be stopped and recorded.
     *
     * @param successful Indicates the status of the transaction may be either
     *                   {@link #SUCCESSFUL} or {@link #FAILED}
     */
    public void stop(int successful);

    /**
     * Indicates the end of the transaction, causing response time measurment
     * to be stopped and recorded.
     *
     * @param successful Indicates the status of the transaction may be either
     *                   {@link #SUCCESSFUL} or {@link #FAILED}.
     * @param diagnostic Diagnostic message.
     */
    public void stop(int successful, String diagnostic);
}
