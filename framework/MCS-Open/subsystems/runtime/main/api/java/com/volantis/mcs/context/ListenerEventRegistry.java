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
package com.volantis.mcs.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A register of events created from 'listener' elements.
 * <p>
 * The events are registered by the observer id of the element that may
 * reference them.
 * <p>
 * Each observer may have multiple events associated with it.
 *
 * @mock.generate 
 */
public class ListenerEventRegistry {

    /**
     * Map of observer id to list of listeners.
     */
    private Map observerId2ListenerListMap = new HashMap();

    /**
     * Register the supplied listener event with the supplied observer id,
     *
     * @param observerId the observer id to register the event by.
     * @param listenerEvent the listener event to register.
     */
    public void addListenerById(String observerId,
            ListenerEvent listenerEvent) {

        List listenerList = (List) observerId2ListenerListMap.get(observerId);
        if (listenerList == null) {
            listenerList = new ArrayList();
            observerId2ListenerListMap.put(observerId, listenerList);
        }
        listenerList.add(listenerEvent);
    }

    /**
     * Look up a collection of listener events by observer id.
     * <p>
     * Note that once this method is called for a particular observer id, the
     * collection of listener events for that id is marked as used and may not
     * be used again.
     *
     * @param observerId the id of the observer element.
     * @return an iterator over the listeners for this id, or null if there are
     *      no listeners for this id.
     */
    public Iterator getListenersById(String observerId) {

        Iterator listeners = null;
        List listenerList = (List) observerId2ListenerListMap.get(observerId);
        if (listenerList != null) {
            listeners = listenerList.iterator();
            observerId2ListenerListMap.remove(observerId);
        }
        return listeners;
    }

    /**
     * Mark this registry as completed.
     * <p>
     * At this point if the registry has unreferenced listeners, an exception
     * will be thrown, as the page is invalid in this case.
     */
    public void complete() {
        if (!observerId2ListenerListMap.isEmpty()) {
            throw new RuntimeException("Page has unreferenced " +
                    "listeners for observers " +
                    observerId2ListenerListMap.keySet());
        }
    }

}
