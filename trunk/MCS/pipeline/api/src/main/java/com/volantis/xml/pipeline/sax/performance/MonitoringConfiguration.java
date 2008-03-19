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
package com.volantis.xml.pipeline.sax.performance;

import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.synergetics.performance.MonitoringFactory;
import com.volantis.synergetics.performance.MonitoringMetaFactory;
import com.volantis.synergetics.performance.MonitoredApplication;
import com.volantis.synergetics.performance.MonitoredTransaction;

import java.util.Map;
import java.util.HashMap;

/**
 *  This class provides configuration for performance Instrumentation.
 */

public class MonitoringConfiguration implements Configuration {



    /**
     * The default MonitoringFactory to use.
     */
    private static MonitoringFactory monitoringFactory =
            MonitoringMetaFactory.getInstance().getActiveMonitoringFactory();

    /**
     * The default Pipeline Application.
     */
    private static MonitoredApplication monitoredApplication =
            monitoringFactory.createApplication("Pipeline");

    private Map transactions = new HashMap();

    /**
     * Create a {@link MonitoredTransaction} for the named transaction.
     *
     * @param transactionName The name of the Transaction.
     * @return A {@link MonitoredTransaction}
     */
    public synchronized MonitoredTransaction getTransaction(
            String transactionName) {

        MonitoredTransaction monitoredTransaction = null;
        if (transactions.containsKey(transactionName)) {
            monitoredTransaction =
                    (MonitoredTransaction)transactions.get(transactionName);

        } else {
            monitoredTransaction =
                    monitoringFactory.createTransaction(monitoredApplication,
                            transactionName);
            transactions.put(transactionName, monitoredTransaction);
        }
        return monitoredTransaction;
    }

}
