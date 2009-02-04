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

/**
 * Provide a bean implementation of the NamedDataSource configuration.
 */
public class AnonymousDataSourceConfiguration 
    implements AnonymousDataSource {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2004. ";

   /**
    * The name of a user that can connect to the database. This is optional.
    */
   private String user;

   /**
    * The password that the user needs to specify to access the database. This
    * is optional.
    */
   private String password;

    /**
     * Store the actual configuration as an object. This may be one of two
     * types, JDBCDriverConfiguration MCSDatabaseConfiguration.
     */
    private AnonymousDataSource configuration;

    /**
     * Return the user for the <code>DataSource</code>.
     *
     * @return      the user for the <code>DataSource</code>.
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the user for the <code>DataSource</code>.
     *
     * @param user for the <code>DataSource</code>.
     */

    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the actual configuration as an object.
     *
     * @return      the actual configuration as an object
     */
    public AnonymousDataSource getDataSourceConfiguration() {
        return configuration;
    }

    /**
     * Set the configuration as a AnonymouseDataSourceConfiguration object.
     *
     * @param config the configuration as a AnonymousDataSourceConfiguration object.
     */
    public void setDataSourceConfiguration(AnonymousDataSource config) {
        configuration = config;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String string) {
        password = string;
    }

    /**
     * Useful string representation of this object - used in test cases and
     * debug output (when necessary).
     */
    public String toString() {
        return super.toString() + " [user='" + user + "', password='" +
            password + "', config='" + configuration + "']";
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
