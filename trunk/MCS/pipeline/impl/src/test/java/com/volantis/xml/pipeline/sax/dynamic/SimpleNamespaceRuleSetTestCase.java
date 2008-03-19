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
package com.volantis.xml.pipeline.sax.dynamic;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet;
import com.volantis.xml.namespace.ExpandedName;

import java.util.Map;

import junitx.util.PrivateAccessor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Test case for the {@link com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet} class
 */ 
public class SimpleNamespaceRuleSetTestCase extends TestCaseAbstract {

    /**
     * Instance of the class being tested
     */ 
    private SimpleNamespaceRuleSet ruleSet;
   
    /**
     * String default namespace URI 
     */ 
    private static String URI = "http://foo.com";
    
    /**
     * Reference to the internal map that the SimpleNamespaceRuleSet that
     * is being tested manages.
     */ 
    private Map internalRuleMap;
    
    /**
     * A DynamicElementRule to use when performing tests
     */ 
    private DynamicElementRule rule;
    
    /**
     * Creates a new <code>SimpleNamespaceRuleSetTestCase</code> instance
     * @param name the name of the test
     */ 
    public SimpleNamespaceRuleSetTestCase(String name) {
        super(name);        
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        ruleSet = new SimpleNamespaceRuleSet(URI);
        internalRuleMap = (Map) PrivateAccessor.getField(ruleSet, "rules");
        rule = new AbstractAddProcessRule() {
            // javadoc inherited            
            protected XMLProcess createProcess(DynamicProcess dynamicProcess,
                                               ExpandedName elementName,
                                               Attributes attributes)
                    throws SAXException {
                return null;
            }
        };

    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        ruleSet = null;
    }
    
    /**
     * Test that ensures an <code>IllegalArguementException<code> is thrown
     * if a null namespace URI is passed in on construction 
     * @throws Exception if an error occurs
     */ 
    public void testConstructorWithNullURI() throws Exception {
        try {
            new SimpleNamespaceRuleSet(null);
            fail("Constructor should throw an IllegalArgument Exception " +
                 "if a null namespace URI is provided");
        } catch (IllegalArgumentException e) {            
        }
        
    }
    
    /**
     * Test that the {@link com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet#getNamespaceURI} returns
     * the correct URI
     * @throws Exception if an error occurs
     */ 
    public void testGetNamespaceURI() throws Exception {
        assertEquals("getNamespaceURI did not return the expected uri",
                     URI, ruleSet.getNamespaceURI());        
    }
    
    /**
     * Test that ensures an <code>IllegalArguementException<code> is thrown
     * if {@link SimpleNamespaceRuleSet#getRule} is called with a null
     * localName argument 
     * @throws Exception if an error occurs
     */
    public void testGetRuleWithNullLocalName() throws Exception {
        try {
            ruleSet.getRule(null);
            fail("getRule should throw an IllegalArgument Exception " +
                 "if a null localName is provided");            
        } catch (IllegalArgumentException e) {            
        }
    }
    
    /**
     * Test that ensures an <code>IllegalArguementException<code> is thrown
     * if {@link SimpleNamespaceRuleSet#getRule} is called with an empty 
     * string localName argument 
     * @throws Exception if an error occurs
     */
    public void testGetRuleWithEmptyLocalName() throws Exception {
        try {
            ruleSet.getRule("");
            fail("getRule should throw an IllegalArgument Exception " +
                 "if an empty localName is provided");            
        } catch (IllegalArgumentException e) {            
        }
    }
    
    /**
     * Tests the {@link com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet#getRule} method
     * @throws Exception if an error occurs
     */    
    public void testGetRule() throws Exception {            
        // add the rule to the internal map
        internalRuleMap.put("test", rule);
        
        // ensure we can retrieve the rule that was added above
        assertEquals("getRule did not return the correct rule", 
                     rule, ruleSet.getRule("test"));
        
        // ensure null is returned if no rule has been registered for 
        // a given local name 
        assertNull("getRule should return null if the rule does not exist",
                   ruleSet.getRule("tester"));
    }
    
    /**
     * Test that ensures an <code>IllegalArguementException<code> is thrown
     * if {@link SimpleNamespaceRuleSet#getRule} is called with a null
     * localName argument 
     * @throws Exception if an error occurs
     */
    public void testAddRuleWithNullLocalName() throws Exception {
        try {
            ruleSet.addRule(null, rule);
            fail("addRule should throw an IllegalArgument Exception " +
                 "if a null localName is provided");            
        } catch (IllegalArgumentException e) {            
        }
    }
    
    /**
     * Test that ensures an <code>IllegalArguementException<code> is thrown
     * if {@link SimpleNamespaceRuleSet#addRule} is called with an empty 
     * string localName argument 
     * @throws Exception if an error occurs
     */
    public void testAddRuleWithEmptyLocalName() throws Exception {
        try {
            ruleSet.addRule("", rule);
            fail("addRule should throw an IllegalArgument Exception " +
                 "if an empty localName is provided");            
        } catch (IllegalArgumentException e) {            
        }
    }        
    
    /**
     * Test that ensures an <code>IllegalArguementException<code> is thrown
     * if {@link com.volantis.xml.pipeline.sax.impl.dynamic.SimpleNamespaceRuleSet#addRule} is called with a null
     * localName argument 
     * @throws Exception if an error occurs
     */
    public void testAddRuleWithNullRule() throws Exception {
        try {
            ruleSet.addRule("test", null);
            fail("addRule should throw an IllegalArgument Exception " +
                 "if a null rule is provided");            
        } catch (IllegalArgumentException e) {            
        }
    }

    /**
     * Tests the {@link SimpleNamespaceRuleSet#addRule} method  
     * @throws Exception if an error occurs
     */    
    public void testAddRule() throws Exception {            
        // add the rule 
        ruleSet.addRule("test", rule);
        
        // ensure that the rule was added to the internal map
        assertEquals("addRule should add the rule to the internal map", 
                     rule, internalRuleMap.get("test"));
    }    

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	280/1	doug	VBM:2003080108 Provided a default implementation of the NamespaceRuleSet interface

 ===========================================================================
*/
