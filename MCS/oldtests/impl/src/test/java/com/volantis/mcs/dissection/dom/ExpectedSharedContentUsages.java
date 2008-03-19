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
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection.dom;

import org.xml.sax.SAXException;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import com.volantis.mcs.dissection.shared.SharedContentUsagesImpl;
import com.volantis.mcs.dissection.impl.DissectionHelper;
import com.volantis.mcs.dissection.SharedContentUsages;

/**
 * Encapsulates expected shared content.
 */
public class ExpectedSharedContentUsages {

    private Set entityNames;

    private SharedContentUsages expectedUsages;

    public ExpectedSharedContentUsages() {
        entityNames = new HashSet();
    }

    /**
     * Add an entity for shared element content.
     * 
     * @param name
     */ 
    public void addEntity(String name) {
        // NOTE: for historical reasons, shared element content does not 
        // have an identifying prefix (i.e., to add one, I would have had to
        // modify all the existing tests ...) 
        entityNames.add(name);
    }

    /**
     * Add an entity for a shared element name.
     * 
     * @param name
     */ 
    public void addElementEntity(String name) {
        entityNames.add(DissectableContentHandler.COMMON_ELEMENT_PREFIX + name);
    }
    
    public SharedContentUsages getExpectedUsages(TestDocumentDetails details)
        throws SAXException {
        if (expectedUsages == null) {
            DissectableDocument document = details.getDocument();
            expectedUsages = DissectionHelper.createSharedContentUsages(document);
            for (Iterator i = entityNames.iterator(); i.hasNext();) {
                String name = (String) i.next();
                int index = details.getCommonStringIndex(name);
                if (index != -1) {
                    expectedUsages.addSharedContentUsage(index);
                }
            }
        }
        return expectedUsages;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Sep-03	1336/1	geoff	VBM:2003090301 Provide support for StringLiteral in WMLC

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
