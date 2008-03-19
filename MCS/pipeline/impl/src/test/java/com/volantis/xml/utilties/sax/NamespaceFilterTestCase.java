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

import com.volantis.xml.utilities.sax.NamespaceFilter;
import com.volantis.xml.pipeline.sax.TestSAXHandler;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

/**
 * TestCase for the NamespaceFilter class
 */ 
public class NamespaceFilterTestCase extends TestCaseAbstract {
        
    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * Default namespace uri
     */ 
    private static String NAMESPACE = "http://foo.com";
    
    /**
     * Default namespace prefic
     */ 
    private static String PREFIX = "px";
    
    /**
     * The NamespaceFilter class that is being tested
     */ 
    private NamespaceFilter filter;
    
    /**
     * Test ContentHandler
     */ 
    private TestSAXHandler handler;
    
    /**
     * Creates a NamespaceFilterTestCase instance
     * @param name the name
     */ 
    public NamespaceFilterTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        filter = new NamespaceFilter(PREFIX, NAMESPACE);
        handler = new TestSAXHandler();
        filter.setContentHandler(handler);
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        filter = null;
    }
    
    /**
     * Tests the <code>startElement</code> method
     * @throws Exception if an error occurs
     */
    public void testStartElement() throws Exception {
        
        Attributes atts = new AttributesImpl();
        
        // fire an event with a namespace mapping
        filter.startElement("http://bar.com", "a", "p:a", atts);
        
        // check that the correct event was forwarded to the content handler
        handler.assertStartElementInvoked("http://bar.com", "a", "p:a", atts);
        handler.assertStartPrefixMappingNotInvoked();
        
        handler.reset();
        // fire an event with no namespace
        filter.startElement("", "b", "b", atts);
        
        // ensure that a startPrefixMapping event was generated
        handler.assertStartPrefixMappingInvoked(PREFIX, NAMESPACE);
        handler.assertStartElementInvoked(NAMESPACE, "b", PREFIX + ":b", atts);
    }
    
    /**
     * Tests the <code>endElement</code> method
     * @throws Exception if an error occurs
     */ 
    public void testEndElement() throws Exception {
        
        Attributes atts = new AttributesImpl();
        
        // fire an event with a namespace mapping
        filter.startElement("http://bar.com", "a", "p:a", atts);
        // fire an event with no namespace
        filter.startElement("", "b", "b", atts);        

        // fire an event with a namespace mapping
        filter.startElement("http://foobar.com", "c", "p:c", atts);
        
        // reset the state on the test Handler 
        handler.reset();
        
         
        // fire an endElement event
        filter.endElement("http://foobar.com", "c", "p:c");
        // check that the correct event was forwarded to the content handler
        handler.assertEndElementInvoked("http://foobar.com", "c", "p:c");        
        // ensure that an endPrefixMapping event was generated
        handler.assertEndPrefixMappingNotInvoked();
        
        // reset the state on the test Handler 
        handler.reset();
        
        // fire an endElement event
        filter.endElement("", "b", "b");
        // check that the correct event was forwarded to the content handler
        handler.assertEndElementInvoked(NAMESPACE, "b", PREFIX + ":b");        
        // ensure that an endPrefixMapping event was generated
        handler.assertEndPrefixMappingInvoked(PREFIX);
        
        // reset the state of the test handler.
        handler.reset(); 
        
        // fire an endElement event
        filter.endElement("http://bar.com", "a", "p:a");
        // check that the correct event was forwarded to the content handler
        handler.assertEndElementInvoked("http://bar.com", "a", "p:a");        
        // ensure that an endPrefixMapping event was generated
        handler.assertEndPrefixMappingNotInvoked();
                            
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
