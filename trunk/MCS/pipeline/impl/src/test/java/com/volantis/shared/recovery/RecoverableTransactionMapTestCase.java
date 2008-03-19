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
 
package com.volantis.shared.recovery;

/**
 * This class test RecoverableTransactionMap
 */
public class RecoverableTransactionMapTestCase 
        extends AbstractRecoverableTransactionTestAbstract {

    /**
     * Object to store in the map
     */
    protected final static Object VALUE = new Object();
    
    /**
     * An Object to use as a key into a map.
     */ 
    protected final static Object KEY = new Object();
    
    // javadoc inherited from superclass.
    public void doTestClone(AbstractRecoverableTransaction clone)
            throws Exception {
        RecoverableTransactionMap rtm = (RecoverableTransactionMap) clone;
        assertEquals("Unexpected value in clone.", VALUE, rtm.get(KEY));
    }

    // javadoc inherited from superclass.
    public AbstractRecoverableTransaction getRecoverableTransaction() {
        return new RecoverableTransactionMap();
    }

    // javadoc inherited from superclass.
    public void configureForCloneTest(AbstractRecoverableTransaction clone) {
        RecoverableTransactionMap rtm = (RecoverableTransactionMap) clone;
        rtm.put(KEY, VALUE);
    }

    /**
     * Test the method put(Object, Object)     
     */ 
    public void testPut() throws Exception {
        RecoverableTransactionMap rtm = new RecoverableTransactionMap();
        rtm.put(KEY, VALUE);
        
        assertSame("Unexpected value in map.", VALUE, rtm.map.get(KEY));
    }

    /**
     * Test the method get(Object)     
     */ 
    public void testGet() throws Exception {
        RecoverableTransactionMap rtm = new RecoverableTransactionMap();
        rtm.map.put(KEY, VALUE);
        assertSame("Unexpected value in map.", VALUE, rtm.get(KEY));
    }

    /**
     * Test the method remove(Object)
     */ 
    public void testRemove() throws Exception {
        RecoverableTransactionMap rtm = new RecoverableTransactionMap();
        rtm.map.put(KEY, VALUE);
        rtm.remove(KEY);
        assertNull("Value should be null.", rtm.get(KEY));
    }

    /**
     * Test the method rollbackTransaction(AbstractRecoverableTransaction)     
     */ 
    public void testRollbackTransactionImpl() throws Exception {
        RecoverableTransactionMap rtm = new RecoverableTransactionMap();
        rtm.put(KEY, VALUE);
        
        rtm.startTransaction();
        
        rtm.put("key1", "value1");
        rtm.put("key2", "value2");
        
        assertTrue("Should be three values in map.", rtm.map.size() == 3);
        
        rtm.rollbackTransaction();
        assertTrue("Should be one value in map.", rtm.map.size() == 1);
        assertSame("Unexpected value in map.", VALUE, rtm.get(KEY));        
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
