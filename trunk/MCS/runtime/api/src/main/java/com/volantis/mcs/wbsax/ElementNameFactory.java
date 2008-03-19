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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-May-03    Geoff           VBM:2003042904 - Created; a factory for 
 *                              creating ElementCodes.
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory for creating {@link ElementNameCode}s.
 * <p>
 * Utilises/implements the Flyweight pattern to avoid excess garbage.
 */ 
public class ElementNameFactory implements ElementRegistrar {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Map of {@link ElementNameCode}s, by name.
     */ 
    private Map elementsByName = new HashMap();

    public ElementNameFactory() {
    }

    /**
     * Create an {@link ElementNameCode} from an element name, or return
     * <code>null</code> if there was no name code registered for this element. 
     * 
     * @param name the name of the element code.
     * @return the created element code, or null.
     */ 
    public ElementNameCode create(String name) {
        ElementNameCode tag = (ElementNameCode) elementsByName.get(name);
        return tag;
    }

    // Inherit Javadoc.
    public void registerElement(int token, String name) {
        ElementNameCode tag = new ElementNameCode(token, name);
        elementsByName.put(name, tag);
    }

    // There should really be byte[] versions of the above methods as well?

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Sep-03	1301/6	byron	VBM:2003082107 Support Openwave GUI Browser extensions - single select controls handled as type=radio

 10-Sep-03	1301/3	byron	VBM:2003082107 Support Openwave GUI Browser extensions

 12-Sep-03	1295/9	geoff	VBM:2003082109 Build all jars and run the junit testsuite with IBM JDK 1.4.1 (build script changes)

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 14-Jul-03	790/2	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/1	geoff	VBM:2003070404 clean up WBSAX

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 ===========================================================================
*/
