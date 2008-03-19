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
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class TestDocumentDetails {

    private DissectableDocument document;

    private Map commonStrings;

    private List commonStringList;

    public TestDocumentDetails() {
        commonStrings = new HashMap();
        commonStringList = new ArrayList();
    }

    public void initialise() {
        commonStrings.clear();
        commonStringList.clear();
    }

    public void setDocument(DissectableDocument document) {
        this.document = document;
    }

    public DissectableDocument getDocument() {
        return document;
    }

    public int getCommonStringIndex(String name)
        throws SAXException {

        Integer index = (Integer) commonStrings.get(name);
        if (index == null) {
            return -1;
        }

        return index.intValue();
    }

    public void addCommonString(String name, String value, int index)
        throws SAXException {

        commonStringList.add (null);
        commonStringList.set(index, name);

        Integer entry = (Integer) commonStrings.get(name);
        if (entry == null) {
            entry = new Integer(index);
            commonStrings.put(name, entry);
        }
    }

    public String getCommonStringName(int index) {
        return (String) commonStringList.get(index);
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

 12-Jun-03	385/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 12-Jun-03	368/1	geoff	VBM:2003061006 Enhance WBDOM to support string references

 09-Jun-03	309/1	pduffin	VBM:2003060302 Added some testcases, fixed some problems with keeptogether, slightly improved the documentation

 ===========================================================================
*/
