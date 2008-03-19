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
package com.volantis.xml.utilties.sax;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.utilities.sax.XMLReaderFactory;
import com.volantis.xml.utilities.sax.DocumentFragmentFilter;
import com.volantis.xml.utilities.sax.NamespaceFilter;
import com.volantis.xml.xerces.parsers.SAXParser;
import org.xml.sax.XMLReader;
import org.xml.sax.XMLFilter;

/**
 * TestCase for the XMLReaderFactory class
 */ 
public class XMLReaderFactoryTestCase extends TestCaseAbstract {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * Creates a new XMLReaderFactoryTestCase
     * @param name
     */ 
    public XMLReaderFactoryTestCase(String name) {
        super(name);
    }

    /**
     * Test that the <code>createXMLReader(boolean)<code> method can 
     * create a DocumentFragmentFilter
     * @throws Exception if an error occurs
     */ 
    public void testCreateXMLFragmentReader() throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader(true);
        assertEquals("XMLReader returned should be a DocumentFragmentFilter",
                     DocumentFragmentFilter.class, reader.getClass());
        
        XMLReader parent = ((XMLFilter)reader).getParent();
        
        assertNotNull("DocumentFragmentFilter parent should not be null",
                      parent);       
    }
    
    /**
     * Test that the <code>createXMLReader(boolean)<code> method can 
     * create a standard reader
     * @throws Exception if an error occurs
     */ 
    public void testCreateXMLDocumentReader() throws Exception {
        XMLReader reader = XMLReaderFactory.createXMLReader(false);
        assertEquals("XMLReader returned should be a Standard XMLReader",
                      SAXParser.class, reader.getClass());       
    }
    
    /**
     * Test that the <code>createXMLReader(boolean, String, String)<code> 
     * method can creates a NamespaceFilter with a Fragment parser as the 
     * parent
     * @throws Exception if an error occurs
     */ 
    public void testCreateXMLNamespaceFragmentReader() throws Exception {
        
        XMLReader reader = XMLReaderFactory.createXMLReader(false, 
                                                            "http://foo.com",
                                                            "p");
        
        // should be a NamespaceFilter
        assertEquals("XMLReader returned should be a NamespaceFilter",
                     NamespaceFilter.class, reader.getClass());
        
        // should have a standard reader as the parent
        XMLReader parent = ((XMLFilter)reader).getParent();
        
        assertEquals("NamespaceFilter parent should be a Standard XMLReader",
                      SAXParser.class, parent.getClass());
                
    }
    
    /**
     * Test that the <code>createXMLReader(boolean, String, String)<code> 
     * method can creates a NamespaceFilter with a Document parser as the 
     * parent
     * @throws Exception if an error occurs
     */ 
    public void testCreateXMLNamespaceDocumentReader() throws Exception {
        
        XMLReader reader = XMLReaderFactory.createXMLReader(true, 
                                                            "http://foo.com",
                                                            "p");
        
        assertEquals("XMLReader returned should be a NamespaceFilter",
                     NamespaceFilter.class, reader.getClass());
        
        XMLReader parent = ((XMLFilter)reader).getParent();
        
        assertEquals(
                "NamespaceFilter parent should be a DocumentFragmentFilter",
                DocumentFragmentFilter.class, parent.getClass());
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
