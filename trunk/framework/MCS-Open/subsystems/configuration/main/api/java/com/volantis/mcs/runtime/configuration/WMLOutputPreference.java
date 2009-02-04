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
 * WMLOutputPreference specifies whether WML or WMLC is preferred from the 
 * WML protocol
 */
public class WMLOutputPreference {

    /** WML is preffered */
    public static final WMLOutputPreference WML = 
        new WMLOutputPreference("wml");
    
    /** WMLC is preffered */
    public static final WMLOutputPreference WMLC = 
        new WMLOutputPreference("wmlc");    
    
    /** Preference as a String */
    private final String pref;
    
    /** Create an instance of a preference */
    private WMLOutputPreference(String type ) {
        pref = type;
    }        
    
    /** return String version for printing */
    public String toString() {
        return pref;                       
    }    
    
    /** Return the correct instance from a name */
    public static WMLOutputPreference get( String type ) {
        WMLOutputPreference pref = null;
        String value = type.toLowerCase();
        if (WML.toString().equals(value) ) {
            pref = WML;
        } else if ( WMLC.toString().equals(value) ) {
            pref = WMLC;
        } else {
            throw new IllegalArgumentException(
                    "Invalid WML output preference " + type);
        }
        return pref;
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

 10-Mar-04	3370/1	steve	VBM:2004030901 Application crashes if protocols element is missing

 ===========================================================================
*/
