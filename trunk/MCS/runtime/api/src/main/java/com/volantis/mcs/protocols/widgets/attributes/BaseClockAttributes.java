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
 *  * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.attributes;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds properties specific to all clock rendering widgets:
 * digital-clock, timer, stopwatch.
 */
public class BaseClockAttributes extends WidgetAttributes {
	
    /**
     * List of attributes of elements that are within this element. 
	 */
    private final List contentAttributes = new LinkedList();
    
    /**
     * @return Return list of attributes of elements that are
     * within this element.
     */
    public List getContentAttributes(){
        return contentAttributes;
    }
}
