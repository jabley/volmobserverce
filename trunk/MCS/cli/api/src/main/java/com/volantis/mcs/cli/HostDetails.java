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
 * $Header: /src/voyager/com/volantis/mcs/cli/HostDetails.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 16-Oct-2001  Allan           VBM:2001100903 - Created.
 * 31-Jan-2002  Payal           VBM:2002013008 - Replaced it in cli package 
 *                              from utilities package.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.cli;


import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class to show the host details necessary to create a license for
 * this machine.
 */
public class HostDetails {
    
    /**
     * Main method. Output the relevant System properties to stdout.
     */
    public static void main(String [] args) {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            System.out.println("Hostname:\t" + hostname);
            System.out.println("Machine OS:\t" +System.getProperty("os.name"));
        }
        catch(UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
