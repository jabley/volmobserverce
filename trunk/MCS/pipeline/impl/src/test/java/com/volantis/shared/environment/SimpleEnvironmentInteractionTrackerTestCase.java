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
package com.volantis.shared.environment;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.servlet.ServletEnvironmentInteractionImpl;
import com.volantis.shared.recovery.RecoverableTransactionStack;

import java.util.Stack;

import junitx.util.PrivateAccessor;

/**
 * Test Case for the SimpleEnvironmentInteractionTracker class
 */ 
public class SimpleEnvironmentInteractionTrackerTestCase
        extends TestCaseAbstract {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * Instance of the class being tested
     */ 
    private SimpleEnvironmentInteractionTracker tracker;
    
    /**
     * Reference to the internal stack that the traker manages 
     */    
    private RecoverableTransactionStack trackersStack;
    
    /**
     * An EnvironmentInteraction that represents the root interaction
     */ 
    private EnvironmentInteraction rootInteraction;
    
    /**
     * Creates a new SimpleEnvironmentInteractionTrackerTestCase instance
     * @param name the name of the test
     */ 
    public SimpleEnvironmentInteractionTrackerTestCase(String name) {
        super(name);
    }
    
    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        rootInteraction = createInteraction();    
        tracker = new SimpleEnvironmentInteractionTracker(rootInteraction);
        trackersStack = (RecoverableTransactionStack)
                PrivateAccessor.getField(tracker, "environmentInteractions");
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the pushEnvironmentInteraction method
     * @throws Exception if an error occurs
     */ 
    public void testPushEnvironmentInteraction() throws Exception {
        // create some environment interactions
        EnvironmentInteraction e1 = createInteraction();
        EnvironmentInteraction e2 = createInteraction();
        
        tracker.pushEnvironmentInteraction(e1);
        tracker.pushEnvironmentInteraction(e2);
        
        assertEquals("interaction e2 should be pushed at the top of the " +
                     "stack", 1, trackersStack.search(e2));        
        
        assertEquals("interaction e1 should be pushed at the bottom of the " +
                     "stack", 2, trackersStack.search(e1));
    }
    
    /**
     * Tests the popEnvironmentInteraction method
     * @throws Exception if an error occurs
     */ 
    public void testPopEnvironmentInteraction() throws Exception {
        // create some environment interactions
        EnvironmentInteraction e1 = createInteraction();
        EnvironmentInteraction e2 = createInteraction();
        
        trackersStack.push(e1);
        trackersStack.push(e2);
        
        // ensure the items are popped of in the correct order
        assertEquals("interaction e2 should be popped", e2, 
                     tracker.popEnvironmentInteraction());
        assertEquals("interaction e1 should be popped", e1, 
                     tracker.popEnvironmentInteraction());                     
    }
    
    /**
     * Tests the popEnvironmentInteraction method when no 
     * interactions have been pushed
     * @throws Exception if an error occurs
     */ 
    public void testPopEnvironmentInteractionEmptyStack() throws Exception {
        tracker = new SimpleEnvironmentInteractionTracker();
        IllegalStateException ise = null;
        try {
            tracker.popEnvironmentInteraction();    
        } catch (IllegalStateException e) {
            ise = e;
        }
        assertNotNull("pop should throw an IllegalStateException if stack " +
                      "is empty", ise);                             
    }
    
    
    /**
     * Tests the getCurrentEnvironmentInteraction method
     * @throws Exception if an error occurs
     */ 
    public void testGetCurrentEnvironmentInteraction() throws Exception {
        // create an environment interactions
        EnvironmentInteraction e1 = createInteraction();
        
        trackersStack.push(e1);
        
        // ensure the items are popped of in the correct order
        assertEquals("interaction e1 should be current", e1, 
                     tracker.getCurrentEnvironmentInteraction());                     
    }
    
    /**
     * Tests the getCurrentEnvironmentInteraction method when no interactions
     * have been pushed.
     * @throws Exception if an error occurs
     */ 
    public void testGetCurrentEnvironmentInteractionEmptyStack() 
                throws Exception {
            
         tracker = new SimpleEnvironmentInteractionTracker();
        
        // ensure null is returned as the current interaction
        assertNull("current interaction should be null",
                     tracker.getCurrentEnvironmentInteraction());                     
    }
    
    /**
     * Tests the getRootEnvironmentInteraction method
     * @throws Exception if an error occurs
     */ 
    public void testGetRootEnvironmentInteraction() throws Exception {
        
        // ensure the root interaction is returned
        assertEquals("root should be interaction provided at construction",
                     rootInteraction, tracker.getRootEnvironmentInteraction());
        
        // create an environment interactions
        EnvironmentInteraction e1 = createInteraction();                
        trackersStack.push(e1);
        
        // ensure the root interaction is returned
        assertEquals("root should return the interaction at the bottom of " + 
                     "the stack", 
                     rootInteraction,
                     tracker.getRootEnvironmentInteraction());                             
    }
     
        /**
     * Tests the getRootEnvironmentInteraction method
     * @throws Exception if an error occurs
     */ 
    public void testGetRootEnvironmentInteractionDefaultConstructor() 
                throws Exception {
        
        tracker = new SimpleEnvironmentInteractionTracker();
            
        // ensure the root interaction is returned
        assertEquals("root should be interaction provided at construction",
                     null, tracker.getRootEnvironmentInteraction());
        
        // create an environment interactions
        EnvironmentInteraction e1 = createInteraction();                
        tracker.pushEnvironmentInteraction(e1);
        
        // ensure the root interaction is returned
        assertEquals("root should return the interaction at the bottom of " + 
                     "the stack",
                     e1,
                     tracker.getRootEnvironmentInteraction());                             
    }
    /**
     * Factory method for creating EnvironmentInteraction instances
     * @return an EnvironmentInteraction instance
     */ 
    private EnvironmentInteraction createInteraction() {
        return new ServletEnvironmentInteractionImpl(null,
                                                     null,
                                                     null,
                                                     null,
                                                     null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 24-Jul-03	252/1	doug	VBM:2003072403 Implemented the EnvironmentInteractionTracker interface

 ===========================================================================
*/
