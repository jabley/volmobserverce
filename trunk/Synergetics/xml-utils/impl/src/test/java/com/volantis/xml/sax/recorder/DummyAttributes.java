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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.sax.recorder;

import org.xml.sax.Attributes;

public class DummyAttributes implements Attributes {

    public int getLength() {
        return 0;  // @todo implement this
    }

    public String getLocalName(int index) {
        return null;  // @todo implement this
    }

    public String getQName(int index) {
        return null;  // @todo implement this
    }

    public String getType(int index) {
        return null;  // @todo implement this
    }

    public String getURI(int index) {
        return null;  // @todo implement this
    }

    public String getValue(int index) {
        return null;  // @todo implement this
    }

    public int getIndex(String qName) {
        return 0;  // @todo implement this
    }

    public String getType(String qName) {
        return null;  // @todo implement this
    }

    public String getValue(String qName) {
        return null;  // @todo implement this
    }

    public int getIndex(String uri, String localName) {
        return 0;  // @todo implement this
    }

    public String getType(String uri, String localName) {
        return null;  // @todo implement this
    }

    public String getValue(String uri, String localName) {
        return null;  // @todo implement this
    }
}
