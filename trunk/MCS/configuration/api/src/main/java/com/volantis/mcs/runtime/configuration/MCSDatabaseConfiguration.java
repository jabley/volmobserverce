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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration;

/**
 * Provide a bean implementation for the MarinerDatabase configuration.
 */
public class MCSDatabaseConfiguration implements AnonymousDataSource {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * The vendor of the database. This also identifies the driver to use to
     * access the database.
     */
    private String vendor;

    /**
     * The source of the database.
     */
    private String source;

    /**
     * The database host.
     */
    private String host;

    /**
     * The database port.
     */
    private Integer port;

    /**
     * Return the database host
     *
     * @return      the database host
     */
    public String getHost() {
        return host;
    }

    /**
     * Set the database host
     *
     * @param host the database host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Return the database port.
     *
     * @return      the database port.
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Set the database port.
     *
     * @param port the database port.
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * The source of the database.
     *
     * @return      the source of the database.
     */
    public String getSource() {
        return source;
    }

    /**
     * Set the source of the database.
     *
     * @param source the source of the database.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Return the vendor of the database. This also identifies the driver to
     * use to access the database.
     *
     * @return      the vendor of the database. This also identifies the driver
     *              to use to access the database.
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Set the vendor of the database. This also identifies the driver to use
     * to access the database.
     *
     * @param vendor the vendor of the database. This also identifies the
     *               driver to use to access the database.
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    /**
     * Useful string representation of this object - used in test cases and
     * debug output (when necessary).
     */
    public String toString() {
        return super.toString() + " [vendor='" + vendor + "', host='" +
            host + "', source='" + source + ", port=" + port + "']";
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

 13-Jun-03	316/5	byron	VBM:2003060403 Addressed some rework issues

 12-Jun-03	316/3	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
