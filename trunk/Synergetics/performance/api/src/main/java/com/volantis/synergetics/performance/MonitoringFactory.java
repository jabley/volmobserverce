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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. 
 * ---------------------------------------------------------------------------
 */
package com.volantis.synergetics.performance;


/**
 * This interface provides a wrapper for the underlying performance monitoring
 * engines factory methods for creating an Application  and a Transaction
 */

public interface MonitoringFactory {


    /**
     * Create a {@link MonitoredApplication} with the given application name.
     *
     * @param appName The name of the application.
     * @return A new {@link MonitoredApplication}
     */
    public abstract MonitoredApplication createApplication(String appName);

    /**
     * Create a {@link MonitoredTransaction} with the given Transaction name.
     *
     * @param monitoredApplication The {@link MonitoredApplication} that this
     *                             transaction belongs to.
     * @param transactionName      The name of the transaction.
     * @return A new {@link MonitoredTransaction}
     */
    public abstract MonitoredTransaction createTransaction(
        MonitoredApplication monitoredApplication,
        String transactionName);


}
