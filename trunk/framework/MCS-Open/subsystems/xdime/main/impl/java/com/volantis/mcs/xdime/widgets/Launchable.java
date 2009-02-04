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

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.LaunchAttributes;

/**
 * Interface for elements that can have widget:launch elements 
 * as their children. Allows the launch element to propagate 
 * its attributes to the ancestor which is Launchable. 
 * 
 * @param attrs attributes of launch element
 */
public interface Launchable {
    public void addLaunch(LaunchAttributes attrs);
        
    /**
     * Get id attribute from launchable element eg from widget:wizard. 
     * 
     * @return id element 
     */
    public String getWidgetId();
}
