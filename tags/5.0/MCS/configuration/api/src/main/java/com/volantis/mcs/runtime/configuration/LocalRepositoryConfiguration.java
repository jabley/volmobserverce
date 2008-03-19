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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/LocalRepositoryConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about the local repository. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 * Holds configuration information about the local repository. 
 */ 
public class LocalRepositoryConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    private JDBCRepositoryConfiguration jdbcConfiguration;
    
    private XMLRepositoryConfiguration xmlRepository;

    public void setXmlRepositoryConfiguration(XMLRepositoryConfiguration xmlRepository) {
        this.xmlRepository = xmlRepository;
    }
    
    public XMLRepositoryConfiguration getXmlRepository() {
        return xmlRepository;
    }
    /**
     * Set the configuration as a JDBCDriverConfiguration object.
     *
     * @param config the configuration as a JDBCDriverConfiguration object.
     */
    public void setJDBCRepositoryConfiguration(JDBCRepositoryConfiguration jdbcConfiguration) {
        this.jdbcConfiguration = jdbcConfiguration;
    }
    
    public JDBCRepositoryConfiguration getJDBCRepositoryConfiguration() {
        return jdbcConfiguration;
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

 09-Mar-04	2867/4	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 ===========================================================================
*/
