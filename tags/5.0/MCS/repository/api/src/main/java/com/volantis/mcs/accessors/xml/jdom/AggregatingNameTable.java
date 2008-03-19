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
package com.volantis.mcs.accessors.xml.jdom;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This is an exension of NameTable which can be used to create sets of
 * translations as aggregations of other sets of Translations.
 * <p>
 * As with the parent class, implementors of this class should override the
 * registerNames method to provide the concrete translations.  This can
 * conveniently be acheived using anonymous classes like this:
 * <p>
 * <code>
 * <br>AggregatingNameTable myNameTable = new AggregatingNameTable() {
 * <br>  public void registerNames(NameTranslationRegistrar r) {
 * <br>        r.registerNameTranslation("key1","val1");
 * <br>        r.registerNameTranslation("key2","val2");
 * <br>        }
 * <br>  myNameTable.addAll ( myOtherNameTable );
 * <br>  // ...
 * </code>
 * <p>
 *  @see NameTable
 */
public class AggregatingNameTable implements NameTable {
    final ArrayList additions = new ArrayList();

    /**
     * Implementors should override this to add new translations of their own
     * and/or call addAll() to add translations from other AggregatingNameTables
     * <p>
     * Note: This method should not be called to create the aggregated set of
     * translations instead use registerAll();
     * @param registrar The recipient of the translations
     */
    public void  registerNames(NameTranslationRegistrar registrar) {}

    /**
     * Use this method to create a complete, aggregated set of translations.
     * @param registrar The recipient of the translations.
     */
    public void registerAll(NameTranslationRegistrar registrar) {
        this.registerNames(registrar);
        for (Iterator it = additions.iterator(); it.hasNext(); ) {
            AggregatingNameTable table = ( AggregatingNameTable ) it.next();
            table.registerAll(registrar);
        }
    }

    /**
     * Add all the translations from anothe aggregating name table.
     * @param source The other AggregatingNameTable whose translations are to
     * be added to this one.
     */
    public void addAll (AggregatingNameTable source) {
        additions.add(source);
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

 ===========================================================================
*/
