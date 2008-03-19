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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.widgets.attributes.MapLocationMarkerAttributes;

public class MapLocationMarkerContextHandler {

    /**
     * List of mapLocationMarkers of elements that are within 
     * map-location-markers element
     */
    private List markers = new LinkedList();
    
    public void addMapLocatorMarkerId(String id) throws ProtocolException {     
        markers.add(id);
    }
    
    /**
     * Adds Map Location Markers ids
     * @param contentAttributes List of MapLocationMarkerAttributes
     * @throws ProtocolException
     */
    public void mergeMapLocationMarkerAttributes(
            List mapLocationMarkerAttributes) throws ProtocolException {
        Iterator i = mapLocationMarkerAttributes.iterator();
        while (i.hasNext()){
            MapLocationMarkerAttributes m = (MapLocationMarkerAttributes)i.next();
            addMapLocatorMarkerId(m.getId());
        }
    }
    
    public List getMarkersList() {
        return markers;
    }
}
