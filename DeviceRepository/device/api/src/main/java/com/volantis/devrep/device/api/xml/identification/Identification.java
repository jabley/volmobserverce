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
package com.volantis.devrep.device.api.xml.identification;

import com.volantis.shared.iteration.ReadOnlyCollectionIterator;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Identification {

    private List entries;

    private Map deviceNameToEntryMap;

    public Iterator entries() {

        return new ReadOnlyCollectionIterator(entries);
    }

    public IdentificationEntry find(String deviceName) {

        if (deviceNameToEntryMap == null) {
            deviceNameToEntryMap = new HashMap();
            Iterator entries = entries();
            while (entries.hasNext()) {
                IdentificationEntry entry = (IdentificationEntry)
                        entries.next();
                deviceNameToEntryMap.put(entry.getDeviceName(), entry);
            }
        }
        return (IdentificationEntry) deviceNameToEntryMap.get(deviceName);
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
