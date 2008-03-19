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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.widgets.services;

import java.util.Map;

/**
 * Interface for operations supported by Maps widgets
 */
public interface MapOperation {
    
    // Allowed parameters and values. If they turn out to be Google specific,
    // should be moved to some class in googlemap package, as they are not
    // used outside the googlemap package anyway
    public final static String P_DIR = "d";
    public final static String P_X = "x";
    public final static String P_Y = "y";
    public final static String P_T = "t";

    // offx and offy are used in extended mode
    // to improve quality
    // @todo currently it is not configurable
    // and extended mode is always operational
    public final static String P_OFFX = "offx";
    public final static String P_OFFY = "offy";
    
    public final static String P_MODE = "m";
    public final static String P_ZOOM = "z";
    public final static String P_QUERY = "q";
    
    public final static String MODE_MAP = "map";
    public final static String MODE_PHOTO = "photo";
    
    
    public final static String DIR_IN = "i";
    public final static String DIR_OUT = "o";
    


    /**
     * perform the actual operation and return the result that will be wrapped in
     * map:response and sent to the client-side widget
     *
     * @throws IllegalArgumentException
     */
    String perform(Map parmeters) throws IllegalArgumentException,Exception;
}       
