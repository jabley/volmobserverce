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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DataSourcesConfiguration {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
       	= "(c) Volantis Systems Ltd 2004. ";
     

    private List namedDataSources = new ArrayList();
    /**
    * 
    */
    public DataSourcesConfiguration() {

    }

    /**
     * Add the <code>NamedDataSourceConfiguration</code> object to the list of
     * DataSource objects. The pooled flag is set here.
     *
     * @param config the <code>NamedDataSourceConfiguration</code> object.
     */
    public void addNamedDataSource(NamedDataSourceConfiguration config) {
        namedDataSources.add(config);
    }
    
    public List getNamedDataSources() {
           return namedDataSources;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
