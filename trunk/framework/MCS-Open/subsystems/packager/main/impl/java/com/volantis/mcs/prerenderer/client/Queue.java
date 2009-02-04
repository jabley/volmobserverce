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

package com.volantis.mcs.prerenderer.client;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Implementation of simple queue collection with synchronized method pop 
 */

public class Queue extends LinkedList{

    // create Queue
    Queue(Map map) {
        Iterator it = map.keySet().iterator();
        while(it.hasNext()) {
            String remote = (String) it.next();
            add(new Pair(remote, (String) map.get(remote)));    
        }
    }

    /**
     * Return and remove first element in queue
     * @return pair with remote and local URL to prerendered page
     */
    public synchronized Pair pop() {        
        Pair p = null;  
        if(size() > 0) {
            p = (Pair) getFirst();
            removeFirst();
        }    
        return p;        
    }    
}
