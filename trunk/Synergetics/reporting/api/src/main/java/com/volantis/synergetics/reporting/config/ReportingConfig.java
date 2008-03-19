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
package com.volantis.synergetics.reporting.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Reporting configuration
 *
 */
public class ReportingConfig {

    /**
     * datasource configuration
     */
    protected List datasourceConfigs;

    /**
     * Report list
     */
    protected List reportList;

    /**
     * Constructor
     */
    public ReportingConfig() {
        reportList = new ArrayList();
    }

    //  javadoc unnecessary
    public void addJdbcDatasource(JNDIDatasource jdbcDatasource) {
        datasourceConfigs.add(jdbcDatasource);
    }

    //  javadoc unnecessary
    public void addPoolDatasource(InternalPoolDatasource poolDatasource) {
        datasourceConfigs.add(poolDatasource);
    }

    //  javadoc unnecessary
    public void addJndiDatasource(JNDIDatasource jndiDatasource) {
        datasourceConfigs.add(jndiDatasource);
    }

    /**
     * Get datasource names iterator
     * @return Iterator
     */
    public Iterator getDatasourceNamesIterator() {
        return datasourceConfigs.iterator();
    }

    /**
     * Get configuration parameters to create datasource
     * as hash map (duplicated omitted)
     * @return map datasourceName -> DatasourceConfiguration
     */
    public Map getDatasourceConfigMap() {
        HashMap ret = new HashMap();
        for(Iterator it = datasourceConfigs.iterator(); it.hasNext();) {
            DatasourceConfiguration config = (DatasourceConfiguration) it.next();
            ret.put(config.getName(), config);
        }
        return ret;
    }

    //  javadoc unnecessary
    public void addReportElement(ReportElement reportElement) {
        reportList.add(reportElement);
    }

    /**
     * Get ReportElement iterator
     * @return Iterator
     */
    public Iterator getReportElementsIterator() {
        return reportList.iterator();
    }

    /**
     * Get ReportElement list
     * @return List
     */
    public List getReportElements() {
        return reportList;
    }

}
