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

import java.util.Stack;

/**
 * This class ...
 */
public class RecoverableTransactionStackTestCase 
        extends AbstractRecoverableTransactionTestAbstract {
        
    /**
     * Object to put on a stack.
     */ 
    protected final static Object STACKABLE = new Object(); 

    // javadoc inherited from superclass
    public void doTestClone(AbstractRecoverableTransaction clone)
            throws Exception {
        RecoverableTransactionStack rts = (RecoverableTransactionStack) clone;
        assertTrue("Unexpected value on stack", rts.stack.peek() == STACKABLE);
        assertTrue("Only expected one object.", rts.stack.size() == 1);
    }

    // javadoc inherited from superclass
    public AbstractRecoverableTransaction getRecoverableTransaction() {
        return new RecoverableTransactionStack();
    }

    // javadoc inherited from superclass
    public void configureForCloneTest(AbstractRecoverableTransaction clone) {
        RecoverableTransactionStack rts = (RecoverableTransactionStack) clone;
        rts.stack.push(STACKABLE); 
    }

    /**
     * Test the push(Object) method.     
     */ 
    public void testPush() throws Exception {
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        rts.push(STACKABLE);
        
        assertSame("Unexpected value on stack.", STACKABLE, rts.stack.peek());
    }

    /**
     * Test the method pop(Object)     
     */ 
    public void testPop() throws Exception {
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        rts.push(STACKABLE);
        
        Object popped = rts.pop();
        assertSame("Unexpected value popped.", STACKABLE, popped);
        assertTrue("Stack should be empty", rts.stack.isEmpty());
    }

    /**
     * Test the method peek()     
     */ 
    public void testPeek() throws Exception {
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        rts.push(STACKABLE);

        assertSame("Unexpected value on stack.", STACKABLE, rts.stack.peek());
    }

    /**
     * Test the method empty()
     */ 
    public void testEmpty() throws Exception {
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        rts.push(STACKABLE);        
        assertFalse("Stack is not empty.", rts.empty());        
        rts.pop();        
        assertTrue("Stack should be empty.", rts.empty());
    }

    /**
     * Test the method size()     
     */ 
    public void testSize() throws Exception {
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        rts.push(STACKABLE);
        rts.push(STACKABLE);
        rts.push(STACKABLE);
        assertTrue("Size should be 3", rts.size() == 3);
        
        rts.pop();
        rts.pop();
        assertTrue("Size should be 1", rts.size() == 1);
        
        rts.pop();
        assertTrue("Size should be 0", rts.size() == 0);        
    }

    /**
     * Tests the method find(Class)     
     */
    public void testFind() throws Exception {
        Integer i = new Integer(2);
        Integer j = new Integer(2);
        
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        // push the objects onto the stack
        rts.push(i);
        rts.push(new Object());
        rts.push(j);

        // ensure the find method can locate an object that exists
        assertEquals("find should return the first match",
                j, rts.find(Integer.class));
        
        // ensure the find method returns null for items that are not present
        assertNull("find should return null if object is not found",
                rts.find(Boolean.class));
    }

    /**
     * Test the method rollbackTransactionImpl     
     */ 
    public void testRollbackTransactionImpl() throws Exception {
        RecoverableTransactionStack rts = new RecoverableTransactionStack();
        rts.push(STACKABLE);
        rts.push(STACKABLE);
        
        rts.startTransaction();
        
        Stack clonedStack = 
                ((RecoverableTransactionStack)rts.clones.peek()).stack;
        assertEquals("Expected stacks to be equal.", rts.stack, clonedStack);
        assertNotSame("The stacks should be different.", 
                rts.stack, clonedStack);
        
        rts.push(STACKABLE);
        assertNotEquals(rts.stack, clonedStack);
        
        rts.rollbackTransaction();        
        assertSame("Expected stacks to be equal.", clonedStack, rts.stack);
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
