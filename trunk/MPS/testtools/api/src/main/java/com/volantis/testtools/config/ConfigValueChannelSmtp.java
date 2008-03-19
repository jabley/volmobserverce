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
 * $Header: /src/mps/com/volantis/testtools/config/ConfigValueChannelSmtp.java,v 1.1 2003/03/20 10:15:37 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-03    Geoff           VBM:2003032001 - Created; a simple ValueObject
 *                              for use with ConfigValueMps to configure MPS
 *                              SMTP channels.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.config;

/**
 * A simple ValueObject for use with {@link ConfigValueMps} to configure MPS
 * SMTP channels.
 */ 
public class ConfigValueChannelSmtp extends ConfigValueChannel {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public String hostName;

    public Boolean authorisationEnabled;

    public String userName;

    public String password;
    
}
