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
 * Container for the protocols configuration read from the mariner
 * configuration file.
 *
 * @mock.generate 
 */
public class ProtocolsConfiguration {

    /**
     * Output WML or WMLC
     */
    private WMLOutputPreference wmlPreferredOutputFormat;    
    
    

    /**
     * Return the setting for the preferred-output-format attribute as a String. 
     * This is required by digester which will not function without it. Other
     * code should use getPreferredOutputFormat() instead as it returns the 
     * setting as an object.
     * @return the setting for the preferred-output-format attribute
     * @deprecated use getPreferredOutputFormat instead
     */
    public String getWmlPreferredOutputFormat() {
        if (wmlPreferredOutputFormat != null) {
            return wmlPreferredOutputFormat.toString();
        } else {
            return null;
        }
    }

    /**
     * Return the setting for the preferred-output-format attribute as an enum
     * object 
     * @return the setting for the preferred-output-format attribute
     */
    public WMLOutputPreference getPreferredOutputFormat() {
        return wmlPreferredOutputFormat;
    }

    /**
     * Set the value of the preferred-output-format attribute
     * @param string the attribute value "wml" or "wmlc"
     */
    public void setWmlPreferredOutputFormat(String string) {
        wmlPreferredOutputFormat = WMLOutputPreference.get(string);
    }

    /**
     * Set the value of the preferred-output-format attribute
     * @param pref the preffered WML output mode
     */
    public void setPreferredOutputFormat(WMLOutputPreference pref) {
        wmlPreferredOutputFormat = pref;
    }    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 12-Mar-04	3370/5	steve	VBM:2004030901 Null exception if protocols element is missing in config

 11-Mar-04	3370/3	steve	VBM:2004030901 Null exception if protocols element is missing in config

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 02-Mar-04	2736/2	steve	VBM:2003121104 Patched from Proteus2 and merged with MCS

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 ===========================================================================
*/
