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

import com.volantis.shared.recovery.RecoverableTransactionMapTestCase;
import junitx.util.PrivateAccessor;

import java.util.Map;
import java.util.Stack;

/**
 * This class tests RecoverableComplexPropertyContainer
 */
public class RecoverableComplexPropertyContainerTestCase
        extends RecoverableTransactionMapTestCase {
    private ResourceOwnerMock resourceOwnerMock1;
    private ResourceOwnerMock resourceOwnerMock2;
    private ResourceOwnerMock resourceOwnerMock3;
    private ResourceOwnerMock resourceOwnerMock4;

    protected void setUp() throws Exception {
        super.setUp();

        resourceOwnerMock1 = new ResourceOwnerMock(
                "resourceOwnerMock1", expectations);

        resourceOwnerMock2 = new ResourceOwnerMock(
                "resourceOwnerMock2", expectations);

        resourceOwnerMock3 = new ResourceOwnerMock(
                "resourceOwnerMock3", expectations);

        resourceOwnerMock4 = new ResourceOwnerMock(
                "resourceOwnerMock4", expectations);
    }

    /**
     * Test the method setProperty(Object, Object, boolean)
     */ 
    public void testSetProperty() throws Exception {
        TestRCPC rcpc = new TestRCPC();

        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);
        assertEquals("unexpected value in map.", 
                resourceOwnerMock1, rcpc.getMap().get(key1));
        
        String key2 = "key2";
        rcpc.setProperty(key2, resourceOwnerMock2, true);
        Object inMap = rcpc.getMap().get(key2);
        assertEquals("unexpected value in map.", resourceOwnerMock2,
                ((ReleasableResourceOwner)inMap).getResourceOwner());
        
        // Test that if the same object is added under the same key and it
        // it releasable it isnt released...
        rcpc.setProperty(key2, resourceOwnerMock2, true);
        inMap = rcpc.getMap().get(key2);
        assertEquals("unexpected value in map.", resourceOwnerMock2,
                ((ReleasableResourceOwner) inMap).getResourceOwner());

        expectations.verify();

        // Check that if a different object is added under the key that the
        // old one is released.
        resourceOwnerMock2.expects.release();
        rcpc.setProperty(key2, resourceOwnerMock3, true);
        inMap = rcpc.getMap().get(key2);
        assertEquals("unexpected value in map.", resourceOwnerMock3,
                ((ReleasableResourceOwner) inMap).getResourceOwner());
    }

    /**
     * Test the method getProperty(Object)
     */ 
    public void testGetProperty() throws Exception {
        TestRCPC rcpc = new TestRCPC();

        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);
        
        assertEquals("Unexpected value retrieved.", resourceOwnerMock1,
                rcpc.getProperty(key1));
        
        String key2 = "key2";
        rcpc.setProperty(key2, resourceOwnerMock2, true);

        assertEquals("Unexpected value retrieved.", resourceOwnerMock2,
                rcpc.getProperty(key2));
    }

    /**
     * Test the method remove property     
     */ 
    public void testRemoveProperty() throws Exception {
        TestRCPC rcpc = new TestRCPC();

        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);
        rcpc.removeProperty(key1);

        expectations.verify();

        String key2 = "key2";
        rcpc.setProperty(key2, resourceOwnerMock2, true);

        resourceOwnerMock2.expects.release();
        rcpc.removeProperty(key2);

        expectations.verify();

        String key3 = "key3";
        rcpc.setProperty(key3, resourceOwnerMock3, true);
                        
        rcpc.startTransaction();
        // check that a REMOVED is added into the map in a transaction.
        rcpc.removeProperty(key3);        
        Object REMOVE_ON_MERGE =
                PrivateAccessor.getField(rcpc, "REMOVE_ON_MERGE");
        assertSame("Expected removed flag to be added to map.",
                REMOVE_ON_MERGE, rcpc.get(key3));
        
        String key4 = "key4";
        rcpc.setProperty(key4, resourceOwnerMock4, true);

        resourceOwnerMock4.expects.release();
        rcpc.removeProperty(key4);

        expectations.verify();

        resourceOwnerMock3.expects.release();
        rcpc.commitTransaction();
    }

    /**
     * Test the method release()     
     */ 
    public void testRelease() throws Exception {
        TestRCPC rcpc = new TestRCPC();
        
        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);
        
        String key2 = "key2";
        rcpc.setProperty(key2, resourceOwnerMock2, true);
        
        rcpc.startTransaction();
                
        String key3 = "key3";
        rcpc.setProperty(key3, resourceOwnerMock3, true);

        resourceOwnerMock2.expects.release();
        resourceOwnerMock3.expects.release();

        rcpc.release();

        assertTrue("Expected map to be empty.", rcpc.getMap().isEmpty());
    }

    /**
     * Test the method startTransactionImpl()     
     */ 
    public void testStartTransactionImpl() throws Exception {
        TestRCPC rcpc = new TestRCPC();

        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);
        
        assertFalse("Expected map to have values.", rcpc.getMap().isEmpty());
        rcpc.startTransaction();        
        assertTrue("Expected map to be cleared.", rcpc.getMap().isEmpty());
    }

    /**
     * Test the method commitTransactionImpl(AbstractRecoverableTransaction)     
     */ 
    public void testCommitTransactionImpl() throws Exception {
        TestRCPC rcpc = new TestRCPC();

        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);
        
        String key2 = "key2";
        rcpc.setProperty(key2, resourceOwnerMock2, true);
        
        assertTrue("Expected two map entries.", rcpc.getMap().size() == 2);        
        rcpc.startTransaction();
        assertTrue("Expected no entries.", rcpc.getMap().size() == 0);
        
        String key3 = "key3";
        rcpc.setProperty(key3, resourceOwnerMock3, true);
        
        String key4 = "key4";
        rcpc.setProperty(key4, resourceOwnerMock4, true);
        
        // remove a property.
        // this has been added within the transaction so should be released
        // immediately.
        resourceOwnerMock4.expects.release();
        rcpc.removeProperty(key4);

        expectations.verify();

        assertTrue("Expected one map entry.", rcpc.getMap().size() == 1);
        
        // start another transaction and remove a property
        // this is in the parent so we will have a marker put into the map
        // so we know to release it at the end of the transaction.
        
        rcpc.startTransaction();
        assertTrue("Expected no map entries.", rcpc.getMap().size() == 0);   
        rcpc.removeProperty(key2);
        assertTrue("Expected one map entry.", rcpc.getMap().size() == 1);
        
        rcpc.commitTransaction();
        // the remove marker will move up
        assertTrue("Expected two map entries.", rcpc.getMap().size() == 2);

        resourceOwnerMock2.expects.release();
        rcpc.commitTransaction();
        assertTrue("Expected two map entries.", rcpc.getMap().size() == 2);

        expectations.verify();

        // hopefully we are left with objects 1 and 3...
        assertSame("Unexpected value retrieved.", resourceOwnerMock1,
                rcpc.getProperty(key1));
        assertSame("Unexpected value retrieved.", resourceOwnerMock3,
                rcpc.getProperty(key3));
    }

    /**
     * Test the method rollbackTransactionImpl(AbstractRecoverableTransaction)     
     */ 
    public void testRollbackTransactionImpl() throws Exception {
        TestRCPC rcpc = new TestRCPC();

        String key1 = "key1";
        rcpc.setProperty(key1, resourceOwnerMock1, false);

        String key2 = "key2";
        rcpc.setProperty(key2, resourceOwnerMock2, true);

        assertTrue("Expected two map entries.", rcpc.getMap().size() == 2);
        rcpc.startTransaction();
        assertTrue("Expected no entries.", rcpc.getMap().size() == 0);

        String key3 = "key3";
        rcpc.setProperty(key3, resourceOwnerMock3, false);

        String key4 = "key4";
        rcpc.setProperty(key4, resourceOwnerMock4, true);
        
        assertTrue("Expected two map entries.", rcpc.getMap().size() == 2);
        
        resourceOwnerMock4.expects.release();

        // rollback the transaction
        rcpc.rollbackTransaction();
        
        expectations.verify();

        assertTrue("Expected two map entries.", rcpc.getMap().size() == 2);
        assertEquals("Unexpected value retrieved.", resourceOwnerMock1,
                rcpc.getProperty(key1));
        assertEquals("Unexpected value retrieved.", resourceOwnerMock2,
                rcpc.getProperty(key2));                
    }


    /**
     * This Test version of RecoverableComplexPropertyContainer is used so that 
     * we can get hold of the protected map member field in the super class
     * which is in a different package.
     */ 
    public static class TestRCPC extends RecoverableComplexPropertyContainer {
        
        /**
         * Get the protected map member field.
         * @return the protected map member field.
         */ 
        public Map getMap() {
            return map;
        }
        
        /**
         * Get the protected clones member field.
         * @return the protected clones member field.
         */ 
        public Stack getClones() {
            return clones;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
