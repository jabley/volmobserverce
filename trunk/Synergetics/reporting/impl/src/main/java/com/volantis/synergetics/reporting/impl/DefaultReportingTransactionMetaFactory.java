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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl;

import com.volantis.synergetics.reporting.ReportingException;
import com.volantis.synergetics.reporting.ReportingTransactionFactory;
import com.volantis.synergetics.reporting.ReportingTransactionMetaFactory;

import java.util.Map;

/**
 * The default implementation of the ReportingTransactionMetaFactory. This
 * creates a new ReportingTransactionFactory based on the Configuration passed
 * to the first call of {@link #getReportingTransactionFactory}. This factory
 * will be returned for all subsequent calls to {@link
 * #getReportingTransactionFactory}
 */
public class DefaultReportingTransactionMetaFactory extends
    ReportingTransactionMetaFactory {

    /**
     * The object used to lock access to the stored reporting transaction
     * factory
     */
    private final Object lock = new Object();

    /**
     * The transaction factory to return
     */
    private ReportingTransactionFactory result;

    /**
     * Create a ReportingTransactionFactory based on the configuration passed
     * in to the first call of this method. Subsequent calls return the same
     * factory.
     *
     * @param config the servlet factory used to create an instance of the
     *               factory. This may not be null on the first call to this
     *               meta factory but can be on subsequent calls.
     * @return a ReportingTransactionFactory configured with
     *
     * @throws IllegalStateException
     */
    public ReportingTransactionFactory
        getReportingTransactionFactory(Map config)
        throws IllegalStateException, ReportingException {
        synchronized (lock) {
            if (result == null) {
                result = new DefaultReportingTransactionFactory(config);
            }
        }
        return result;
    }
}
