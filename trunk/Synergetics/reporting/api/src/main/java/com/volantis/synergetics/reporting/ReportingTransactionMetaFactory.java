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
package com.volantis.synergetics.reporting;

import com.volantis.synergetics.factory.MetaDefaultFactory;

import java.util.Map;

/**
 * This is used to provide a ReportingTransactionMetaFactory instance from
 * which ReporintFactories can be obtained.
 * 
 * @volantis-api-include-in PublicAPI
 *
 * @deprecated Use OSGi {@link ReportService} instead
 */
public abstract class ReportingTransactionMetaFactory {

    /**
     * The meta factory for creating default factories
     */
    private static MetaDefaultFactory factoryInstance =
        new MetaDefaultFactory("com.volantis.synergetics.reporting.impl."
                               + "DefaultReportingTransactionMetaFactory",
                               ReportingTransactionMetaFactory.class.getClassLoader());

    /**
     * Returns an instance of the default ReportingTransactionFactory
     *
     * @return an instance of the default ReportingTransactionFactory
     */
    public static ReportingTransactionMetaFactory getDefaultInstance() {
        return (ReportingTransactionMetaFactory)
            factoryInstance.getDefaultFactoryInstance();
    }

    /**
     * Create an ReportingTransactionFactory instance. The instance created
     * depends on the initialization parameters of the servlet configuration
     * object provided.
     *
     * @param config the configuration from used to determine which Reporting
     *               Configuration to instantiate.
     */
    public abstract ReportingTransactionFactory
        getReportingTransactionFactory(Map config)
        throws IllegalStateException, ReportingException;

}


