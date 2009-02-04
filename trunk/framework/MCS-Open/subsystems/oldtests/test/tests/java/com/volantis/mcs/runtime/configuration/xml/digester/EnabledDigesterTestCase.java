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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/digester/EnabledDigesterTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              EnabledDigester. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.Digester;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.volantis.mcs.runtime.configuration.xml.digester.MarinerDigester;
import com.volantis.xml.xerces.parsers.SAXParser;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link MarinerDigester}.
 */ 
public class EnabledDigesterTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public EnabledDigesterTestCase(String s) {
        super(s);
    }

    /**
     * Ensure that an Enabled object is created properly if it's enabled 
     * attribute has value "true".
     * 
     * @throws SAXException
     * @throws IOException
     */ 
    public void testEnabled() throws SAXException, IOException {
        // Parse the document
        String document = 
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<parent>" +
                  "<enabled enabled=\"true\" property=\"a property\"/>" +
                "</parent>";
        ParentConf parent = parse(document);
        // Check the results
        assertNotNull("parent should have enabled", parent.getEnabled());
        assertEquals("enabled should have property",
                "a property", parent.getEnabled().getProperty());
    }

    /**
     * Ensure that an Enabled object is not created properly if it's enabled 
     * attribute has value != "true".
     * 
     * @throws SAXException
     * @throws IOException
     */ 
    public void testDisabled() throws SAXException, IOException {
        // Parse the document
        String document = 
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<parent>" +
                  "<enabled enabled=\"false\" property=\"a property\"/>" +
                "</parent>";
        ParentConf parent = parse(document);
        // Check the results
        assertNull("parent should not have enabled", parent.getEnabled());
    }

    /**
     * Ensure that an Enabled object is not created properly if it's enabled 
     * attribute does not have any value - i.e. it is the empty string.
     * 
     * @throws SAXException
     * @throws IOException
     */ 
    public void testEmpty() throws SAXException, IOException {
        // Parse the document
        String document = 
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<parent>" +
                  "<enabled enabled=\"\" property=\"a property\"/>" +
                "</parent>";
        ParentConf parent = parse(document);
        // Check the results
        assertNull("parent should not have enabled", parent.getEnabled());
    }
    
    /**
     * Ensure that an Enabled object is not created properly if does not have 
     * an enabled attribute. 
     * 
     * @throws SAXException
     * @throws IOException
     */ 
    public void testNone() throws SAXException, IOException {
        // Parse the document
        String document = 
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<parent>" +
                  "<enabled property=\"a property\"/>" +
                "</parent>";
        ParentConf parent = parse(document);
        // Check the results
        assertNull("parent should not have enabled", parent.getEnabled());
    }

    /**
     * Parse the XML document supplied using a digester, returning the topmost 
     * object created by the digester.
     * 
     * @param document the document to parse.
     * @return the topmost object created by the digester.
     * @throws IOException
     * @throws SAXException
     */ 
    private ParentConf parse(String document) 
            throws IOException, SAXException {
        // Create the digester
        // @todo ought this digester use our repackaged parser?
        // I have used our repackaged parser to get it to work for now...
        // This will probably have to be changed as part of VBM:2003022702.
        Digester digester = new MarinerDigester(new SAXParser());
        digester.addObjectCreate("parent", ParentConf.class);
        digester.addObjectCreate("parent/enabled", EnabledConf.class);
        digester.addSetProperties("parent/enabled", "property", "property");
        digester.addSetNext("parent/enabled", "setEnabled");
        
        // Parse the document using the digester.
        ByteArrayInputStream stream = new ByteArrayInputStream(
                document.getBytes());
        InputSource source = new InputSource(stream);
        ParentConf parent = (ParentConf) digester.parse(source);
        
        assertNotNull(parent);
        return parent;
    }

    /**
     * An example parent object.
     */ 
    public static class ParentConf {
        private EnabledConf enabled;

        public EnabledConf getEnabled() {
            return enabled;
        }

        public void setEnabled(EnabledConf enabled) {
            this.enabled = enabled;
        }
    }
    
    /**
     * An example Enabled object.
     * 
     * Notice that the enabled property is *not* defined in this class; 
     * merely implementing the marker interface is enough.
     */ 
    public static class EnabledConf implements Enabled {
        private String property;

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
