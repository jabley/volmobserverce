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
package com.volantis.mcs.css.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.volantis.mcs.css.version.CSSVersion;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.StyleProperty;

public class BorderRadiusHelper {

    /**
     * HashMap maping all individual border-radius properties to type of browser
     */
    static private Map properties = new HashMap();    

    static private List shorthands = new ArrayList();    

    static {       
        properties.put("border-top-left-radius", "css3");
        properties.put("border-top-right-radius", "css3");        
        properties.put("border-bottom-right-radius", "css3");                
        properties.put("border-bottom-left-radius", "css3");
        properties.put("border-radius", "css3");
        
        properties.put("-moz-border-radius-topleft", "moz");        
        properties.put("-moz-border-radius-topright", "moz");
        properties.put("-moz-border-radius-bottomright", "moz");        
        properties.put("-moz-border-radius-bottomleft", "moz");
        properties.put("-moz-border-radius", "moz");        
        
        properties.put("-webkit-border-top-left-radius", "webkit");              
        properties.put("-webkit-border-top-right-radius", "webkit");        
        properties.put("-webkit-border-bottom-right-radius", "webkit");        
        properties.put("-webkit-border-bottom-left-radius", "webkit");
        properties.put("-webkit-border-radius", "webkit");
        
        shorthands.add("border-radius");
        shorthands.add("-moz-border-radius");
        shorthands.add("-webkit-border-radius");        
    }    
        
    /**
     * get browser dependent style property for any corner from cssVersion or null if there is no any
     * @param cssVersion
     * @param propertyName StyleProperty e.g. like mcs-border-top-left-radius
     * @return null if any of border-radius corner property is not supported or browser dependent property name like -moz-border-radius-topleft
     */
    public static String supportCorner(CSSVersion cssVersion, StyleProperty property) {      
        return cssVersion.getPropertyAlias(property);
    }

    /**
     * @return null if any of border-radius shorthand property is not supported or browser dependent property name like -moz-border-radius or -webkit-border-radius
     */
    public static String supportShorthand(CSSVersion cssVersion) {
        Iterator it = shorthands.iterator();
        while (it.hasNext()) {
            String shorthandName = (String) it.next();
            if(cssVersion.supportsShorthand(shorthandName)) {
                return shorthandName;
            }
        }
        return null; 
    }
    
    
    /**
     * Get type of border-radius suported by device 
     * @param propertyName - e.g. -moz-border-radius-topleft
     * @return one of following: moz, css3, webkit
     */
    public static String getBorderRadiusType(String propertyName) {
        return (String) properties.get(propertyName);
    }

    /**
     * Check if emulated mode is supported
     * @return state if emulated mode is supported
     */
    public static boolean isBorderRadiusEmulated(CSSVersion cssVersion) {
               
        ///check support all corners. If none of them is supported return true        
        if(BorderRadiusHelper.supportCorner(cssVersion, StylePropertyDetails.MCS_BORDER_TOP_LEFT_RADIUS) != null)
            return false;             
        if(BorderRadiusHelper.supportCorner(cssVersion, StylePropertyDetails.MCS_BORDER_TOP_RIGHT_RADIUS) != null)
            return false;
        if(BorderRadiusHelper.supportCorner(cssVersion, StylePropertyDetails.MCS_BORDER_BOTTOM_RIGHT_RADIUS) != null)
            return false;
        if(BorderRadiusHelper.supportCorner(cssVersion, StylePropertyDetails.MCS_BORDER_BOTTOM_LEFT_RADIUS) != null)
            return false;
        
        return true;        
    }
}
