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
/*
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/FragmentationStateTestCase.java,v 1.1 2003/04/29 11:42:43 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Apr-2003  Chris W         VBM:2003040311 - Tests InclusionState.equals()
 * ----------------------------------------------------------------------------

 */
package com.volantis.mcs.runtime;

import junit.framework.TestCase;

/**
 * This is the unit test class for FragmentationState. Currently it only tests
 * the equals method of the inner class InclusionState.
 */
public class FragmentationStateTestCase extends TestCase {

    /**
     * Standard junit constructor
     * @param name
     */
    public FragmentationStateTestCase(String name) {
        super(name);
    }
    
    /**
     * Tests the equals method of InclusionState inner class is an
     * equivalence relation.
     * @see java.lang.Object#equals(Object) for an explanation of why we need
     * all these tests.
     */
    public void testInclusionStateEquivalence() {
        FragmentationState.InclusionState iState 
            = new FragmentationState.InclusionState();

        FragmentationState.InclusionState iState2 
            = new FragmentationState.InclusionState();

        FragmentationState.InclusionState iState3 
            = new FragmentationState.InclusionState();
            
        iState.setFragmentName("fragment");
        iState.setShardIndex("dissectingPane", 0);

        iState2.setFragmentName("fragment");
        iState2.setShardIndex("dissectingPane", 0);

        iState3.setFragmentName("fragment");
        iState3.setShardIndex("dissectingPane", 0);
        
        // Test of consistency done through repeated tests.
        // Test reflexive 
        assertTrue("not reflexive", iState.equals(iState));
        
        // Test symmetric
        assertTrue("not symmetric", iState.equals(iState2));
        assertTrue("not symmetric", iState2.equals(iState));
        
        // Test transitive
        assertTrue("not transitive", iState.equals(iState2));
        assertTrue("not transitive", iState2.equals(iState3));                
        assertTrue("not transitive", iState.equals(iState3));
        
        // Test null
        assertTrue("null test fails", !iState.equals(null));
    }

    /**
     * Test the equals method of InclusionState inner class
     * We check that changing the various InclusionState properties causes
     * equals() to return false.
     */
    public void testInclusionStateEquals() {
        FragmentationState.InclusionState iState 
            = new FragmentationState.InclusionState();

        FragmentationState.InclusionState iState2 
            = new FragmentationState.InclusionState();
        
        iState.setFragmentName("fragment");
        iState.setShardIndex("dissectingPane", 0);

        // Test different fragment names
        iState2.setFragmentName("fragment2");
        iState2.setShardIndex("dissectingPane", 0);        
        assertTrue("InclusionStates equal when they are not", 
            !iState.equals(iState2));
            
        // Test different dissecting pane names            
        iState2.setFragmentName("fragment");
        iState2.setShardIndex("dissectingPane2", 0);        
        assertTrue("InclusionStates equal when they are not", 
            !iState.equals(iState2));

        // Test different shard indexes.
        iState2.setFragmentName("fragment");
        iState2.setShardIndex("dissectingPane", 1);        
        assertTrue("InclusionStates equal when they are not", 
            !iState.equals(iState2));            
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
