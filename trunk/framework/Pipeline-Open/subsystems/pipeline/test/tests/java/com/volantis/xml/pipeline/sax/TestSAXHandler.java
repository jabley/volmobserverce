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
package com.volantis.xml.pipeline.sax;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import junit.framework.Assert;

/**
 * Default Handler for testing.
 */ 
public class TestSAXHandler extends DefaultHandler {
    
    /**
     * Flag that indicates whether the startElement method was invoked;
     */ 
    boolean wasStartElementInvoked = false;
    
    /**
     * Flag that indicates whether the endElement method was invoked;
     */ 
    boolean wasEndElementInvoked = false;
    
    /**
     * Flag that indicates whether the startPrefixMapping method was invoked;
     */
    boolean wasStartPrefixMappingInvoked = false;
    
    /**
     * Flag that indicates whether the endPrefixMapping method was invoked;
     */
    boolean wasEndPrefixMappingInvoked = false;
        
    /**
     * Used to store the namespaceURI that was passed into a method
     */ 
    String actualElementNamespaceURI = null;
    
    /**
     * Used to store the namespaceURI that was passed into a method
     */ 
    String actualNamespaceURI = null;
    
    /**
     * Used to store the localName that was passed into a method
     */
    String actualLocalName = null;
    
    /**
     * Used to store the prefixedName that was passed into a method
     */
    String actualPrefixedName = null;
    
    /**
     * Used to store the attributes that was passed into a method
     */
    Attributes actualAttributes = null;
    
    /**
     * Used to store the prefix that was passed into a method
     */     
    String actualPrefix = null;
    
    /**
     * Reset the state of this object back to the default
     */ 
    public void reset() {
        wasStartElementInvoked = false;
        wasEndElementInvoked = false;
        wasStartPrefixMappingInvoked = false;
        wasEndPrefixMappingInvoked = false;
        
        actualNamespaceURI = null;
        actualElementNamespaceURI = null;
        actualLocalName = null;        
        actualAttributes = null;
        actualPrefix = null;
        actualPrefixedName = null;
        
    }

    // javadoc inherited
    public void startPrefixMapping(String prefix, 
                                   String namespaceURI) throws SAXException {
        wasStartPrefixMappingInvoked = true;
        actualPrefix = prefix;
        actualNamespaceURI = namespaceURI;
    }

    /**
     * Test to see whether the <code>startPrefixMapping</code> was invoked
     * @param expectedPrefix the prefix parameter that should have been used 
     * when invoking the method.
     * @param expectedNamespace the namespace parameter that should have been 
     * used when invoking the method.
     * @throws Exception if an error occurs
     */ 
    public void assertStartPrefixMappingInvoked(String expectedPrefix,
                                                String expectedNamespace) 
        throws Exception {
        
            Assert.assertTrue(
                    "The startPrefixMapping() method was not invoked", 
                    wasStartPrefixMappingInvoked);
            Assert.assertEquals(
                    "unexpected prefix passed to startPrefixMapping", 
                    expectedPrefix, actualPrefix);
            Assert.assertEquals(
                    "unexpected namespace passed to startPrefixMapping", 
                    expectedNamespace, actualNamespaceURI);
        
    }
    
    /**
     * Test that ensures that the <code>startPrefixMapping</code> was not
     * invoked
     */
    public void assertStartPrefixMappingNotInvoked() 
        throws Exception {
        
            Assert.assertTrue(
                    "The startPrefixMapping() method was invoked", 
                    !wasStartPrefixMappingInvoked);                    
    }
    
    // javadoc inherited
    public void endPrefixMapping(String prefix) throws SAXException {
        wasEndPrefixMappingInvoked = true;
        actualPrefix = prefix;
    }

    /**
     * Test to see whether the <code>endPrefixMapping</code> was invoked
     * @param expectedPrefix the prefix parameter that should have been used 
     * when invoking the method.
     * @throws Exception if an error occurs
     */ 
    public void assertEndPrefixMappingInvoked(String expectedPrefix) 
        throws Exception {
        
            Assert.assertTrue(
                    "The endPrefixMapping() method was not invoked", 
                    wasEndPrefixMappingInvoked);
            Assert.assertEquals(
                    "unexpected prefix passed to endPrefixMapping", 
                    expectedPrefix, actualPrefix);        
    }
    
    /**
     * Test that ensures that the <code>endPrefixMapping</code> was not
     * invoked
     */
    public void assertEndPrefixMappingNotInvoked() 
        throws Exception {
        
            Assert.assertTrue(
                    "The endPrefixMapping() method was invoked", 
                    !wasEndPrefixMappingInvoked);                    
    }
    
    // javadoc inherited
    public void startElement(String namespaceURI, 
                             String localName, 
                             String prefixedName, 
                             Attributes attributes) throws SAXException {
        wasStartElementInvoked = true;
        actualElementNamespaceURI = namespaceURI;
        actualLocalName = localName;
        actualPrefixedName = prefixedName;
        actualAttributes = attributes;
    }

    /**
     * Test to see whether the <code>startElement</code> was invoked
     * @param expectedNamespaceURI the namespaceURI parameter that should have
     *  been used when invoking the method.
     * @param expectedLocalName the localName parameter that should have
     *  been used when invoking the method.
     * @param expectedPrefixedName the prefixedName parameter that should have 
     * been used when invoking the method.
     * @param expectedAttributes the Attributes parameter that should have
     *  been used when invoking the method.
     * @throws Exception if an error occurs
     */ 
    public void assertStartElementInvoked(String expectedNamespaceURI,
                                          String expectedLocalName,
                                          String expectedPrefixedName,
                                          Attributes expectedAttributes) 
        throws Exception {
        
            Assert.assertTrue(
                    "The startElement() method was not invoked", 
                    wasStartElementInvoked);
            Assert.assertEquals(
                    "unexpected namespaceURI passed to startElement", 
                    expectedNamespaceURI, actualElementNamespaceURI);
            Assert.assertEquals(
                    "unexpected localName passed to startElement", 
                    expectedLocalName, actualLocalName);            
            Assert.assertEquals(
                    "unexpected prefixedName passed to startElement", 
                    expectedPrefixedName, actualPrefixedName);
            Assert.assertEquals(
                    "unexpected Attributes passed to startElement", 
                    expectedAttributes, actualAttributes);
    }
    
    /**
     * Test that ensures that the <code>startElement</code> was not
     * invoked
     */
    public void assertStartElementNotInvoked() 
        throws Exception {
        
            Assert.assertTrue(
                    "The startElement() method was invoked", 
                    !wasStartElementInvoked);            
    }
    
    // javadoc inherited
    public void endElement(String namespaceURI, 
                           String localName, 
                           String prefixedName) throws SAXException {
        wasEndElementInvoked = true;
        actualElementNamespaceURI = namespaceURI;
        actualLocalName = localName;
        actualPrefixedName = prefixedName;       
    }
    
    /**
     * Test to see whether the <code>endElement</code> was invoked
     * @param expectedNamespaceURI the namespaceURI parameter that should have
     *  been used when invoking the method.
     * @param expectedLocalName the localName parameter that should have
     *  been used when invoking the method.
     * @param expectedPrefixedName the prefixedName parameter that should have 
     * been used when invoking the method.
     * @throws Exception if an error occurs
     */ 
    public void assertEndElementInvoked(String expectedNamespaceURI,
                                        String expectedLocalName,
                                        String expectedPrefixedName) 
        throws Exception {
        
            Assert.assertTrue(
                    "The endElement() method was not invoked", 
                    wasEndElementInvoked);
            Assert.assertEquals(
                    "unexpected namespaceURI passed to endElement", 
                    expectedNamespaceURI, actualElementNamespaceURI);
            Assert.assertEquals(
                    "unexpected localName passed to endElement", 
                    expectedLocalName, actualLocalName);            
            Assert.assertEquals(
                    "unexpected prefixedName passed to endElement", 
                    expectedPrefixedName, actualPrefixedName);
    }
    
    /**
     * Test that ensures that the <code>endElement</code> was not
     * invoked
     */
    public void assertEndElementNotInvoked() 
        throws Exception {
        
            Assert.assertTrue(
                    "The endElement() method was invoked", 
                    !wasEndElementInvoked);            
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
