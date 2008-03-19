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

/**
 * SQL handler configuration parameters
 *
 */
public class SqlHandler {

    /**
     * SQL table name
     */
    private String tableName;

    /**
     * datasource name
     */
    private String datasourceName;

    /**
     * Column mappings
     */
    private ArrayList columnMappingList = new ArrayList();

    //  javadoc unnecessary
    public String getTableName() {
        return this.tableName;
    }

    //  javadoc unnecessary
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    //  javadoc unnecessary
    public void addColumnMapping(ColumnMapping columnMapping) {
        columnMappingList.add(columnMapping);
    }

    //  javadoc unnecessary
    public ColumnMapping getColumnMapping(int index) {
        return (ColumnMapping) columnMappingList.get(index);
    }

    //  javadoc unnecessary
    public int sizeColumnMappingList() {
        return columnMappingList.size();
    }

    //  javadoc unnecessary
    public String getDatasourceName() {
        return this.datasourceName;
    }

    //  javadoc unnecessary
    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
    }

}
