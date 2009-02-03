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
package com.volantis.xml.pipeline.testtools.stubs.sax;

import org.xml.sax.Attributes;

/**
 * Stub implementation of org.xml.sax.Attributes.
 */
public class AttributesStub implements Attributes {
    public int getLength() {
        return 0;
    }

    public String getURI(int i) {
        return null;
    }

    public String getLocalName(int i) {
        return null;
    }

    public String getQName(int i) {
        return null;
    }

    public String getType(int i) {
        return null;
    }

    public String getValue(int i) {
        return null;
    }

    public int getIndex(String s, String s1) {
        return 0;
    }

    public int getIndex(String s) {
        return 0;
    }

    public String getType(String s, String s1) {
        return null;
    }

    public String getType(String s) {
        return null;
    }

    public String getValue(String s, String s1) {
        return null;
    }

    public String getValue(String s) {
        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 01-Jul-03	165/1	allan	VBM:2003070101 Fix bug in MessageOperationProcess.startElement()

 ===========================================================================
*/
