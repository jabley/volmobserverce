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
package com.volantis.devrep.device.api.xml.hierarchy;

import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Hierarchy {

    private HierarchyEntry rootEntry;

    // To preserve insertion order.
    private List entries;

    private Map deviceNameToEntryMap;

    public HierarchyEntry getRootEntry() {
        initialise();

        return rootEntry;
    }

    public HierarchyEntry find(String deviceName) {
        initialise();

        return (HierarchyEntry) deviceNameToEntryMap.get(deviceName);
    }

    public Iterator entries() {
        initialise();

        return entries.iterator();
    }

    private void initialise() {

        if (deviceNameToEntryMap == null) {
            deviceNameToEntryMap = new HashMap();
            entries = new ArrayList();
            initialiseEntry(rootEntry);
        }
    }

    private void initialiseEntry(HierarchyEntry entry) {

        // Add the entry to the map for easy searching.
        deviceNameToEntryMap.put(entry.getDeviceName(), entry);
        // Add the entry to the list to preserve insertion order.
        entries.add(entry);
        Iterator children = entry.children();
        while (children.hasNext()) {
            HierarchyEntry child = (HierarchyEntry) children.next();
            // Create back links from entries to their parents.
            child.setParent(entry);
            initialiseEntry(child);
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
