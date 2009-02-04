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

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Attributes of Launch widget element.
 */
public class LaunchAttributes extends WidgetAttributes {

    /**
     * id attribute of parent widget
     */       
    private String widgetId = null;

    /**
     *  Set id attribute from widget. This id is necessary in registerLauncher for client wizard widget   
     * 
     * @param widgetId id attribute from widget:wizard
     */
    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }
    
    /**
     * Get launchable widget id. 
     * 
     * @return id attribute of launchable widget
     */
    public String getWidgetId() {
        return this.widgetId;
    }            
}
