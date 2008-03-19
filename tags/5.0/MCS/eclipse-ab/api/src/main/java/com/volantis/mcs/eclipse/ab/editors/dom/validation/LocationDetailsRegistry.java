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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import org.jdom.Element;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Register of LocationDetails.
 */
public class LocationDetailsRegistry {
    /**
     * A Set for the register of LocationDetails.
     */
    private Set register = new HashSet();

    /**
     * Register a LocationDetails
     * @param element the Element that is associated with the LocationDetails
     * @param details the LocationDetails that describe the location of the
     * given Element in a manner appropriate to the context within which
     * this LocationDetailsRegistry is used.
     * @throws IllegalArgumentException if there is already a LocationDetails
     * registered against the given Element.
     */
    public void registerLocationDetails(Element element,
                                        LocationDetails details) {

        RegistryEntry entry = new RegistryEntry(element, details);
        if(!register.add(entry)) {
            throw new IllegalArgumentException("Already registered element: " +
                    element.getName() + " with namespace " +
                    element.getNamespace());
        }
    }

    /**
     * Get the LocationDetails for the specified Element
     * @param element the element
     * @return the LocationDetails for the given Element or null if no
     * LocationDetails have been registered for the given Element.
     */
    public LocationDetails getLocationDetails(Element element) {
        RegistryEntry entry = new RegistryEntry(element, null);
        LocationDetails details = null;
        Iterator entries = register.iterator();
        while(details==null && entries.hasNext()) {
            RegistryEntry next = (RegistryEntry) entries.next();
            details = entry.equals(next) ? next.locationDetails : null;
        }

        return details;
    }

    /**
     * A registry entry that is identified using an Element name and
     * namespace.
     */
    private static class RegistryEntry {
        private final Element element;
        private final LocationDetails locationDetails;

        /**
         * Construct a new RegistryEntry
         * @param element the Element
         * @param locationDetails the LocationDetails
         */
        private RegistryEntry(Element element,
                              LocationDetails locationDetails) {
            this.element = element;
            this.locationDetails = locationDetails;
        }

        // javadoc inherited
        public boolean equals(Object o) {
            boolean equals = o!=null && o.getClass().equals(getClass());
            if(equals) {
                RegistryEntry entry = (RegistryEntry) o;
                equals = element.getName().equals(entry.element.getName()) &&
                        element.getNamespace().equals(entry.element.
                        getNamespace());
            }

            return equals;
        }

        // javadoc inherited
        public int hashCode() {
            return getClass().hashCode() + element.hashCode();
        }
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 ===========================================================================
*/
