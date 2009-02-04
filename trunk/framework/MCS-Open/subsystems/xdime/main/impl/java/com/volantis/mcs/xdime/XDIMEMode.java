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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime;

/**
 * Type safe enum for XDIME modes
 */
public class XDIMEMode {
    /**
     * Type safe enum for xdime CP mode
     */
    public static final XDIMEMode XDIMECP =
            new XDIMEMode("XDIMECP");

    /**
     * Type safe enum for xdime 2 mode
     */
    public static final XDIMEMode XDIME2 =
            new XDIMEMode("XDIME2");

    /**
     * name of the mode - used for debugging
     */
    private final String myName;

    //constructor
    private XDIMEMode(String name) {
        myName = name;
    }

    /**
     * debug method
     * @return
     */
    public String toString() {
        return myName;
    }
}
