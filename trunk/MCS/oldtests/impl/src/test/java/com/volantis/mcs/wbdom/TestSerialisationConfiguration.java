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
package com.volantis.mcs.wbdom;

import com.volantis.mcs.wbdom.io.SerialisationConfiguration;
import com.volantis.mcs.wbdom.EmptyElementType;

import java.util.ArrayList;

/**
 * A test implementation of {@link SerialisationConfiguration} to allow the
 * registration and checking of url attributes and empty elements.
 */ 
public class TestSerialisationConfiguration 
        implements SerialisationConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private class UrlAttribute {
        private String element;
        private String attribute;

        public UrlAttribute(String element, String attribute) {
            this.element = element;
            this.attribute = attribute;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof UrlAttribute)) {
                return false;
            }
            UrlAttribute other = (UrlAttribute) obj;
            return element.equals(other.element) && 
                    attribute.equals(other.attribute);
        }

        public int hashCode() {
            int result = 17;
            result = 37 * result + element.hashCode();  
            result = 37 * result + attribute.hashCode();
            return result;
        }
    }

    private ArrayList emptyTokens;

    private ArrayList emptyNames;

    private ArrayList urlAttributes;
    
    public TestSerialisationConfiguration() {
        emptyTokens = new ArrayList();
        emptyNames = new ArrayList();
        urlAttributes = new ArrayList();
    }

    public void registerEmptyElement(int token, String name) {
        emptyTokens.add(new Integer(token));
        emptyNames.add(name);
    }
    
    public EmptyElementType getEmptyElementType(int token) {
        if (emptyTokens.contains(new Integer(token))) {
            return EmptyElementType.EmptyTag;
        } else {
            return EmptyElementType.StartAndEndTag;
        }
    }

    public EmptyElementType getEmptyElementType(String name) {
        if (emptyNames.contains(name)) {
            return EmptyElementType.EmptyTag;
        } else {
            return EmptyElementType.StartAndEndTag;
        }
    }

    public void registerUrlAttribute(String element, String attribute) {
        urlAttributes.add(new UrlAttribute(element, attribute));
    }
    
    public boolean isURLAttribute(String element, String attribute) {
        return urlAttributes.contains(new UrlAttribute(element, attribute));
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-03	804/1	geoff	VBM:2003070405 merge from metis with manual renames and copies

 15-Jul-03	798/1	geoff	VBM:2003070405 commit the clean up, athough I am not finished yet

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/3	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 04-Jul-03	733/2	geoff	VBM:2003070403 port from mimas and fix renames manually

 04-Jul-03	724/5	geoff	VBM:2003070403 first take at cleanup

 30-Jun-03	559/7	geoff	VBM:2003060607 changes to test atomic elements

 27-Jun-03	559/5	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
