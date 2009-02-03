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
package com.volantis.mcs.accessors.xml.jdom;

import java.util.Map;
import java.util.HashMap;

/**
 * This class implements NameTranslationRegistrar providing an internal Map
 * of translations and appropriate accessors.  It is still abstract -
 * concrete implementing classes should implement registerNameTranslation(String
 * from, String to)
 * <p>
 * For more details and links to various cooperating classes see NameTable.
 * @see NameTable
 *
 */
public abstract class NameTranslator implements NameTranslationRegistrar {

    /**
     * Holds the actual translations
     */
    protected final Map translationMap = new HashMap();

    /**
     * Once this object has been populated (by passing it to the registerNames()
     * method of the appropriate NameTable) this method can be called to
     * perform an actual translation.
     * @param from The source string for the translation.
     * @return The result string of the translation.
     */
    public String translate(String from) {
        return (String) translationMap.get(from);
    }

    /**
     * Return the internal map containing the translations that have been
     * registered onto this object.
     * @return The map of translations.
     */
    public Map getMap () {
        return translationMap;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jan-04	2754/1	tony	VBM:2004011205 javadocs for NameTable and assoicated classes, move methods into JDOMXMLDeviceLayoutAccessor

 12-Jan-04	2304/1	tony	VBM:2003121708 fixed several accessor bugs and integrated migrate30/accessor translations

 02-Jan-04	2343/1	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 ===========================================================================
*/