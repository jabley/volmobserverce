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
package com.volantis.xml.namespace;

import junit.framework.TestCase;

import java.util.EmptyStackException;


/**
 * TestCase for the <code>DefaultNamespaceManagerTestCase</code> class
 */ 
public class DefaultNamespaceTrackerTestCase extends TestCase {
    
    /**
     * Instance of the class that is being tested
     */ 
    private DefaultNamespacePrefixTracker manager;

    // javsadoc inherited from super class
    protected void setUp() throws Exception {
        super.setUp();
        manager = new DefaultNamespacePrefixTracker();
    }

    // javsadoc inherited from super class
    protected void tearDown() throws Exception {
        super.tearDown();
        manager = null;
    }
     /**
     * Created a new DefaultNamespaceTrackerTestCase instance
     * @param name the name
     */ 
    public DefaultNamespaceTrackerTestCase(String name) {
        super(name);
    }

    /**
     * Test the getPrefixes method returns all prefixes currently in force
     * @throws Exception
     */
    public void testGetPrefixes() throws Exception {

        // NOTE: The tracker always contains a prefix of "xml" so the number of
        // prefixes returned by it is always 1 larger then the number of
        // prefixes expected
        manager.startPrefixMapping("", "http://default");
        assertEquals("should be two prefix", 2,
                     manager.getPrefixes().length);
        // add a new prefix mapping
        manager.startPrefixMapping("a", "http://firstNameSpace");
        assertEquals("should be three prefixes", 3,
                     manager.getPrefixes().length);
        // remap the prefix
        manager.startPrefixMapping("a", "http://overwriteFirstNameSpace");
        assertEquals("should be three prefixes", 3,
                     manager.getPrefixes().length);

        // remove the remapped prefix and we should get the original mapping
        // of "a"
        manager.endPrefixMapping("a");
        assertEquals("should be three prefixes", 3,
                     manager.getPrefixes().length);
        // remove the original mapping of "a"
        manager.endPrefixMapping("a");
        assertEquals("should be two prefixes", 2,
                     manager.getPrefixes().length);
    }

    /**
     * Tests the startPrefixMapping() method.
     * @throws Exception if an error occurs
     */ 
    public void testStartPrefixMapping() throws Exception {
        String prefix = "p";
        String namespace = "http://foo.com";
        
        manager.startPrefixMapping(prefix, namespace);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     namespace,
                     manager.namespaceSupport.
                     getURI(prefix));
                     
        String namespace2 = "http://bar.com";
        manager.startPrefixMapping(prefix, namespace2);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the new mapping
        assertEquals("prefix mapping was not registered",
                     namespace2,
                     manager.namespaceSupport.getURI(prefix));
        
        String namespace3 = "http://foobar.com";
        String prefix2 = "n";
        
        manager.startPrefixMapping(prefix2, namespace3);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the new mapping
        assertEquals("prefix mapping was not registered",
                     namespace3,
                     manager.namespaceSupport.getURI(prefix2));        
        
        // ensure that we can still access the last mapping
        // ensure that underlying NamespaceSupport has been 
        // pased the new mapping
        assertEquals("prefix mapping was not registered",
                     namespace2,
                     manager.namespaceSupport.getURI(prefix));
        
        String invalidPrefix = "xml";
        IllegalArgumentException e = null;
        try {
            manager.startPrefixMapping(invalidPrefix, namespace3);            
        } catch (IllegalArgumentException ie) {
            e = ie;
        }
        assertNotNull("IllegalArgumetException should be thrown for invalid" +
                      " prefixes", e);
    }
    
    /**
     * Tests the encPrefixMapping() method.
     * @throws Exception if an error occurs.
     */ 
    public void testEndPrefixMapping() throws Exception {
        
        String prefix = "p";
        String namespace = "http://foo.com";
        
        // declare a prefix to namespace mapping
        manager.startPrefixMapping(prefix, namespace);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     namespace,
                     manager.namespaceSupport.getURI(prefix));
                     
        String namespace2 = "http://bar.com";
        manager.startPrefixMapping(prefix, namespace2);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the new mapping
        assertEquals("prefix mapping was not registered",
                     namespace2,
                     manager.namespaceSupport.getURI(prefix));
        
        // ok lets start undeclaring a the prefixes
        
        manager.endPrefixMapping(prefix);
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     namespace,
                     manager.namespaceSupport.getURI(prefix));
        
        manager.endPrefixMapping(prefix);
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     null,
                     manager.namespaceSupport.getURI(prefix));
        
        EmptyStackException e = null;
        try {
            manager.endPrefixMapping(prefix);
            
        } catch (EmptyStackException ese) {
            e = ese;
        }
        
        assertNotNull("EmptyStackException should be thrown if this method " +
                      "is invoked without the prefix being declared", e);           
    }
    
    /**
     * Tests the getNamespaceURI() method.
     * @throws Exception if an error occurs.
     */ 
    public void testGetNamespaceURI() throws Exception {
        String prefix = "p";
        String namespace = "http://foo.com";
        
        // declare a prefix to namespace mapping
        manager.startPrefixMapping(prefix, namespace);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     namespace,
                     manager.getNamespaceURI(prefix));
        
        String undeclaredPrefix = "a";
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     null,
                     manager.getNamespaceURI(undeclaredPrefix));
        
                
    }
    
    /**
     * Tests the getPrefix() method.
     * @throws Exception if an error occurs.
     */ 
    public void testGetPrefix() throws Exception {
        String prefix = "p";
        String prefix2 = "q";            
        String namespace = "http://foo.com";
        
        // declare one of the prefix to namespace mappings
        manager.startPrefixMapping(prefix, namespace);
        
        // ensure getPrefix() returns the prefix
        assertEquals("prefix should have been declared",
                     prefix,
                     manager.getNamespacePrefix(namespace));
        
        // declare the other prefix to namespace mappings        
        manager.startPrefixMapping(prefix2, namespace);
            
        // ensure getPrefix() returns one of the prefixes
        String prfx = manager.getNamespacePrefix(namespace);
        assertTrue("prefixes should have been declared",
                    (prfx == prefix || prfx == prefix2));                                        
                        
    }
    
    /**
     * Ensures that if a namespaceURI has not been declared the
     * <code>getPrefix</code> method returns null.
     * @throws Exception if an error occurs.
     */ 
    public void testGetPrefixWithUndeclaredURI() throws Exception {
        
        // a namespace URI that has not been declared 
        String undeclaredNamespaceURI = "http://foo.com";
        
        assertEquals("prefix mapping was registered but should not have been",
                     null,
                     manager.getNamespacePrefix(undeclaredNamespaceURI));        
    }
    /**
     * Test the getQName() method.
     * @throws Exception if an error occurs
     */ 
    public void testGetQName() throws Exception {
        String prefix = "p";
        String namespace = "http://foo.com";
        
        // declare a prefix to namespace mapping
        manager.startPrefixMapping(prefix, namespace);
        
        // ensure that underlying NamespaceSupport has been 
        // pased the mapping
        assertEquals("prefix mapping was not registered",
                     namespace,
                     manager.getNamespaceURI(prefix));        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 25-Jul-03	242/1	steve	VBM:2003072310 Implement namespace package and refactor exitsting code to fit it

 27-Jun-03	127/1	doug	VBM:2003062306 Column Conditioner Modifications

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
