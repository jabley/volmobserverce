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
package com.volantis.mcs.xml.validation;

/**
 * This interface provides add and remove methods for supplementary
 * validation registration management
 */
public interface DOMSupplementaryValidationProvider {

    /**
     * Register a {@link DOMSupplementaryValidator} for a given element name and
     * namespace
     * @param namespaceURI the namespaceURI. Must not be null.
     * @param elementName the name of the element. Must not be null.
     * @param supplementaryValidator the supplementary validator
     */
    void addSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator);

    /**
     * Unregister a {@link DOMSupplementaryValidator} for a given element name
     * and namespace
     * @param namespaceURI the namespaceURI. Must not be null.
     * @param elementName the name of the element. Must not be null.
     * @param supplementaryValidator the supplementary validator
     */
    void removeSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Dec-03	2057/5	doug	VBM:2003112803 Added SAX XSD Validation mechanism

 28-Nov-03	2055/1	doug	VBM:2003112802 Added ODOM validation interfaces

 ===========================================================================
*/
