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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.drivers.webservice;

import java.util.HashMap;

/**
 * A catalog of WSDLEntrys' keyed on the uri for the WSDLEntry.
 */
public class WSDLCatalog {

    /**
     * A Map of WSDLEntry objects.
     */
    private HashMap wsdlEntries;

    /**
     * Add a WSDLEntry to this WSDLCatalog.
     * @param wsdlEntry The WSDLEntry to add.
     * @return The WSDLEntry that was replaced by wsdlEntry or null if there
     * was no other wsdlEntry with the same uri already in this WSDLCatalog.
     */
    public WSDLEntry addWSDLEntry(WSDLEntry wsdlEntry) {
        if (wsdlEntries == null) {
            wsdlEntries = new HashMap();
        }

        return (WSDLEntry)wsdlEntries.put(wsdlEntry.getURI(), wsdlEntry);
    }

   /**
     * Remove a WSDLEntry from this WSDLCatalog based on the uri of the
     * WSDLEntry.
     * @param uri The uri of the WSDLEntry to remove.
     * @return The removed WSDLEntry or null if none was removed.
     */
    public WSDLEntry removeWSDLEntry(String uri) {
        WSDLEntry entry = null;
        if (wsdlEntries != null) {
            entry = (WSDLEntry) wsdlEntries.remove(uri);
        }
        return entry;
    }

    /**
     * Retrieve a WSDLEntry from this WSDLCatalog based on the uri of the
     * WSDLEntry.
     * @param uri The uri of the WSDLEntry to retrieve.
     * @return The retrieved WSDLEntry or null a WSDLEntry for the given uri
     * was not found.
     */
    public WSDLEntry retrieveWSDLEntry(String uri) {
        WSDLEntry entry = null;
        if(wsdlEntries != null) {
            entry = (WSDLEntry) wsdlEntries.get(uri);
        }
        return entry;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-04	6062/1	tom	VBM:2004102704 Fixed null pointer exception for retrieve and remove methods

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 19-Jun-03	98/4	allan	VBM:2003022822 WS Connector renamed to WS Driver

 18-Jun-03	98/1	allan	VBM:2003022822 Promote prelimary classes so that other can access them

 ===========================================================================
*/
