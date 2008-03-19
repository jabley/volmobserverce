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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.marshaller;

import com.volantis.synergetics.reporting.config.DatasourceConfiguration;

/**
 * Test element for default marshaller tests. 
 */
public class TestDefaultMarshallerElement {
    
    /**
     * Datasource.
     */    
    private DatasourceConfiguration datasource;

    public TestDefaultMarshallerElement() {
        
    }
    
    /**
     * Gets datasource.
     * 
     * @return datasource
     */    
    public DatasourceConfiguration getDatasource() {
        return datasource;
    }

    /**
     * Sets datasource.
     * 
     * @param datasource datasource
     */    
    public void setDatasource(DatasourceConfiguration datasource) {
        this.datasource = datasource;
    }
}
