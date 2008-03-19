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

/**
 * This is a concrete NameTranslator which maps in reverse order (right-left).
 * This is used for the 'read' part of import/export accessors, that is reading
 * from an xml repository where new (v3.0 schema-) names are being read in and
 * translated to old (v2.9 repository/dtd ) names.  A full description of this
 * class' collaborators is in NameTable.
 * @see NameTable
 *
 */
public class ReadNameTranslator extends NameTranslator {

    /**
     * Add a translation to this object.  This method is typically called
     * by an implementor of NameTable when an instance of this class is passed
     * to it
     * @param from The right-hand (value) side of the translation to add -
     * because this class is for right-left translations
     * @param to The left-hand (key) side of the translation to add -
     * because this class is for right-left translations
     * @see NameTable#registerNames(NameTranslationRegistrar registrar)
     */
    public void registerNameTranslation(String from, String to) {
        // Add in REVERSE order.
        translationMap.put(to, from);
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

 02-Jan-04	2343/1	geoff	VBM:2003121708 change xml layout accessors to write mostly in new format structure, at least for grids

 ===========================================================================
*/
