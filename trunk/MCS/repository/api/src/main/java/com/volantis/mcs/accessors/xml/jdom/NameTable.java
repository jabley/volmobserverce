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
 * This interface is to be implemented by concrete classes that act as the
 * source of two way name translations.
 * <p>
 * Implementing classes define a registerNames method that calls
 * registerNameTranslation on the caller supplied NameTranslationRegistrar.
 * NameTranslationRegistrar is an interface implemented by the abstract class
 * NameTranslator and then further refined by ReadNameTranslator and
 * WriteNameTranslator.  This allows users of this group of class to supply
 * either a left-to-right ('ReadNameTranslator') or a right-to-left
 * ('WriteNameTranslator')  translator object to the registerNames method.
 * <p>
 * This pattern is employed in several places in import/export code to provide
 * translations between version three schema names and version 2.9 database/dtd
 * names.
 * @see com.volantis.mcs.accessors.xml.jdom.NameTranslationRegistrar
 * @see com.volantis.mcs.accessors.xml.jdom.NameTranslator
 * @see com.volantis.mcs.accessors.xml.jdom.ReadNameTranslator
 * @see com.volantis.mcs.accessors.xml.jdom.WriteNameTranslator
 * @see com.volantis.mcs.accessors.xml.jdom.AggregatingNameTable
 *
 *
 */
public interface NameTable {

    /**
     * Implement this method to populate a NameTranslationRegistrar with
     * translations by calling NameTranslationRegistrar.registerNameTranslation()
     * for each translation.
     * @param registrar The object to which translations will be registered.
     * @see NameTranslationRegistrar#registerNameTranslation(String from, String to)
     */
    void registerNames(NameTranslationRegistrar registrar);
    
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
