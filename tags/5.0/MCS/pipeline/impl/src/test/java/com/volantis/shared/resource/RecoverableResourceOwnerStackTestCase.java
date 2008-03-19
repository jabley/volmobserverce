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
 
package com.volantis.shared.resource;

import com.volantis.shared.recovery.AbstractRecoverableTransaction;
import com.volantis.shared.recovery.RecoverableTransactionStackTestCase;
import com.volantis.xml.pipeline.sax.ReleasableResourceOwner;
import com.volantis.xml.pipeline.sax.ResourceOwnerMock;

import java.util.Stack;

/**
 * This class tests RecoverableResourceOwnerStack
 */
public class RecoverableResourceOwnerStackTestCase 
        extends RecoverableTransactionStackTestCase {

    // javadoc inherited from superclass
    public AbstractRecoverableTransaction getRecoverableTransaction() {
        return new RecoverableResourceOwnerStack();
    }

    // javadoc inherited from superclass
    public void configureForCloneTest(AbstractRecoverableTransaction clone) {
        super.configureForCloneTest(clone);
        RecoverableResourceOwnerStack rros = 
                (RecoverableResourceOwnerStack) clone;
        rros.poppedResourceOwners.push(STACKABLE);
        rros.poppedResourceOwners.push(STACKABLE);
        rros.poppedResourceOwners.push(STACKABLE);
        rros.pushedResourceOwners.push(STACKABLE);
        rros.pushedResourceOwners.push(STACKABLE);
    }

    // javadoc inherited from superclass
    public void doTestClone(AbstractRecoverableTransaction clone)
            throws Exception {
        super.doTestClone(clone);
        RecoverableResourceOwnerStack rros =
                (RecoverableResourceOwnerStack) clone;
        assertTrue("Expected pushed delta stack to be the same.",
                rros.pushedResourceOwners.size() == 2);
        assertTrue("Expected popped delta stack to be the same.",
                rros.poppedResourceOwners.size() == 3);        
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a non ResourceOwner object onto the stack outside a transaction
     * and request that release is suppressed     
     */ 
    public void testPushNonResourceOwnerNoTransactionSuppressRelease() 
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();        
        final Object toPush = new Object();
        
        trros.push(toPush, false);
        
        assertSame("Unexpected object on stack.",
                toPush, trros.getStack().peek());
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a non ResourceOwner object onto the stack outside a transaction
     * and request release.     
     */
    public void testPushNonResourceOwnerNoTransactionRequestRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final Object toPush = new Object();

        trros.push(toPush, true);

        assertSame("Unexpected object on stack.",
                toPush, trros.getStack().peek());
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a non ResourceOwner object onto the stack inside a transaction 
     * and request that release is suppressed.     
     */
    public void testPushNonResourceOwnerInTransactionSuppressRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        trros.startTransaction();

        final Object toPush = new Object();

        trros.push(toPush, false);

        assertSame("Unexpected object on stack.",
                toPush, trros.getStack().peek());
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a non ResourceOwner object onto the stack inside a transaction
     * and request that release is called     
     */
    public void testPushNonResourceOwnerInTransactionRequestRelease() 
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        trros.startTransaction();
        
        final Object toPush = new Object();

        trros.push(toPush, true);

        assertSame("Unexpected object on stack.",
                toPush, trros.getStack().peek());
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a ResourceOwner Object onto the stack outside a transaction and
     * request that release is not called.
     */
    public void testPushResourceOwnerNoTransactionSuppressRelease() 
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPushMock =
                 new ResourceOwnerMock("toPushMock", expectations);

        trros.push(toPushMock, false);

        assertSame("Unexpected object on stack.",
                toPushMock, trros.getStack().peek());
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a ResourceOwner Object onto the stack outside a transaction and
     * request that release is called.
     */
    public void testPushResourceOwnerNoTransactionRequestRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPushMock =
                 new ResourceOwnerMock("toPushMock", expectations);

        trros.push(toPushMock, true);

        Object onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.",
                toPushMock, ((ReleasableResourceOwner)onStack).getResourceOwner());
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());        
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a ResourceOwner Object onto the stack inside a transaction and
     * request that release is called.
     */
    public void testPushResourceOwnerInTransactionSuppressRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPushMock =
                 new ResourceOwnerMock("toPushMock", expectations);

        trros.startTransaction();
        trros.push(toPushMock, false);

        Object onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.", toPushMock, onStack);
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());  
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a ResourceOwner Object onto the stack inside a transaction and
     * request that release is called.
     */
    public void testPushResourceOwnerInTransactionRequestRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPushMock =
                 new ResourceOwnerMock("toPushMock", expectations);

        trros.startTransaction();
        trros.push(toPushMock, true);

        Object onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.", toPushMock,
                ((ReleasableResourceOwner) onStack).getResourceOwner());
        Object onPushed = trros.pushedResourceOwners.peek();
        assertSame("Unexpected object on pushed stack.", toPushMock,
                ((ReleasableResourceOwner) onPushed).getResourceOwner());        
    }    
    
    /**
     * Test the push(Object, boolean) method.
     * Push a ResourceOwner Object onto the stack inside a transaction and
     * request that release is called.
     */
    public void testPushResourceOwnerNestedTransactionRequestRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPushMock =
                 new ResourceOwnerMock("toPushMock", expectations);

        trros.startTransaction();
        trros.startTransaction();
        trros.push(toPushMock, true);

        Object onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.", toPushMock,
                ((ReleasableResourceOwner) onStack).getResourceOwner());
        Object onPushed = trros.pushedResourceOwners.peek();
        assertSame("Unexpected object on pushed stack.", toPushMock,
                ((ReleasableResourceOwner) onPushed).getResourceOwner());   
    }
    
    /**
     * Test the push(Object, boolean) method.
     * Push a ResourceOwner Object onto the stack inside a transaction and
     * request that release is called.
     */
    public void testPushResourceOwnerNestedTransaction2PushesRequestRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPushMock =
                 new ResourceOwnerMock("toPushMock", expectations);

        trros.startTransaction();
        trros.push(toPushMock, true);
        trros.startTransaction();
        trros.push(toPushMock, true);

        Object onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.", toPushMock, onStack);
        assertTrue("Nothing should be on pushedResourceOwner stack",
                trros.pushedResourceOwners.isEmpty());
        
        TestRecoverableResourceOwnerStack clone = 
                (TestRecoverableResourceOwnerStack)trros.getClones().peek();
        Object onPushed = clone.pushedResourceOwners.peek();
        assertSame("Unexpected object on parent pushed stack.", toPushMock,
                ((ReleasableResourceOwner) onPushed).getResourceOwner());
    }

    /**
     * Test the method peek()     
     */ 
    public void testPeek() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        
        final Object object = new Object();
        trros.push(object, false);
        assertSame("Unexpected object on stack.", object, trros.peek());
        
        final ResourceOwnerMock resource1Mock =
                 new ResourceOwnerMock("resource1Mock", expectations);
        trros.push(resource1Mock, true);
        Object onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.", resource1Mock,
                ((ReleasableResourceOwner) onStack).getResourceOwner());

        final ResourceOwnerMock resource2Mock =
                 new ResourceOwnerMock("resource2Mock", expectations);
        trros.push(resource2Mock, false);
        onStack = trros.getStack().peek();
        assertSame("Unexpected object on stack.", resource2Mock, onStack);
    }

    /**
     * Test the method pop() with a non ResourceOwner object.     
     */ 
    public void testPopNonResourceOwner() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final Object toPop = new Object();
        trros.push(toPop, false);
        
        Object popped = trros.pop();
        assertSame("Unexpected object popped.", toPop, popped);
        assertTrue("Expected popped stack to be empty.", 
                trros.poppedResourceOwners.isEmpty());
    }
    
    /**
     * Test the method pop() with a ResourceOwner object with release requested     
     */ 
    public void testPopResourceOwnerNoTransactionRequestRelease() 
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPopMock =
                 new ResourceOwnerMock("toPopMock", expectations);
        trros.push(toPopMock, true);

        toPopMock.expects.release();

        Object popped = trros.pop();
        assertSame("Unexpected object popped.", toPopMock, popped);
        assertTrue("Expected popped stack to be empty.",
                trros.poppedResourceOwners.isEmpty());
    }
    
    /**
     * Test the method pop() with a ResourceOwner object with release requested     
     */
    public void testPopResourceOwnerNoTransactionSuppressRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPopMock =
                 new ResourceOwnerMock("toPopMock", expectations);
        trros.push(toPopMock, false);

        Object popped = trros.pop();
        assertSame("Unexpected object popped.", toPopMock, popped);
        assertTrue("Expected popped stack to be empty.",
                trros.poppedResourceOwners.isEmpty());
    }
    
    /**
     * Test the method pop() with a ResourceOwner object with release requested
     * in a transaction.     
     */
    public void testPopResourceOwnerInTransactionRequestRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPopMock =
                 new ResourceOwnerMock("toPopMock", expectations);
        trros.startTransaction();
        trros.push(toPopMock, true);

        Object popped = trros.pop();
        assertSame("Unexpected object popped.", toPopMock, popped);
        Object onPopped = trros.poppedResourceOwners.peek();
        assertSame("Expected object on popped stack.", toPopMock,
                ((ReleasableResourceOwner) onPopped).getResourceOwner());
    }
    
    /**
     * Test the method pop() with a ResourceOwner object with release 
     * suppressed in a transaction.
     */
    public void testPopResourceOwnerInTransactionSuppressRelease()
            throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPopMock =
                 new ResourceOwnerMock("toPopMock", expectations);
        trros.startTransaction();
        trros.push(toPopMock, false);

        Object popped = trros.pop();
        assertSame("Unexpected object popped.", toPopMock, popped);
        assertTrue("Expected popped stack to be empty.",
                trros.poppedResourceOwners.isEmpty());
    }

    /**
     * Test the method startTransaction()     
     */ 
    public void testStartTransactionImpl() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        trros.pushedResourceOwners.push(STACKABLE);
        trros.poppedResourceOwners.push(STACKABLE);
        
        assertFalse("Expected items on stack.", 
                trros.pushedResourceOwners.isEmpty());
        assertFalse("Expected items on stack.",
                trros.poppedResourceOwners.isEmpty());
        
        trros.startTransactionImpl();
        assertTrue("Expected stack to be cleared.",
                trros.pushedResourceOwners.isEmpty());
        assertTrue("Expected stack to be cleared.",
                trros.poppedResourceOwners.isEmpty());
    }

    /**
     * Tests the method commitTransaction(AbstractRecoverableTransaction)
     * with an unnested transaction.    
     */ 
    public void testCommitTransactionImplUnNested() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        final ResourceOwnerMock toPopMock =
                 new ResourceOwnerMock("toPopMock", expectations);
        trros.startTransaction();
        trros.push(toPopMock, true);

        Object popped = trros.pop();
        assertSame("Unexpected object popped.", toPopMock, popped);
        Object onPopped = trros.poppedResourceOwners.peek();
        assertSame("Expected object on popped stack.", toPopMock,
                ((ReleasableResourceOwner) onPopped).getResourceOwner());

        toPopMock.expects.release();

        trros.commitTransaction();
    }
    
    /**
     * Tests the method commitTransaction(AbstractRecoverableTransaction)
     * with an unnested transaction.    
     */
    public void testCommitTransactionImplNested() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();
        
        final Object onStack = new Object();
        trros.push(onStack, false);
        
        final ResourceOwnerMock toPopMock =
                 new ResourceOwnerMock("toPopMock", expectations);
        trros.startTransaction();
        trros.push(toPopMock, true);
        Object popped = trros.pop();
        
        assertSame("Unexpected object popped.", toPopMock, popped);
        Object onPopped = trros.poppedResourceOwners.peek();
        assertSame("Expected object on popped stack.", toPopMock,
                ((ReleasableResourceOwner) onPopped).getResourceOwner());

        // Nest another transaction
        final ResourceOwnerMock nestedMock =
                 new ResourceOwnerMock("nestedMock", expectations);
        trros.startTransaction();
        trros.push(nestedMock, true);
        Object nestedPopped = trros.pop();        
        assertSame("Unexpected object popped.", nestedMock, nestedPopped);
        onPopped = trros.poppedResourceOwners.peek();
        assertSame("Expected object on popped stack.", nestedMock,
                ((ReleasableResourceOwner) onPopped).getResourceOwner());
        assertTrue("Only object from this transaction should be on the " +
                "popped object stack", trros.poppedResourceOwners.size() == 1);

        trros.commitTransaction();

        onPopped = trros.poppedResourceOwners.get(0);
        assertSame("Expected object on popped stack.", toPopMock,
                ((ReleasableResourceOwner) onPopped).getResourceOwner());
        onPopped = trros.poppedResourceOwners.get(1);
        assertSame("Expected object on popped stack.", nestedMock,
                ((ReleasableResourceOwner) onPopped).getResourceOwner());
        
        Object onPushed = trros.pushedResourceOwners.get(0);
        assertSame("Expected object on pushed stack.", toPopMock,
                ((ReleasableResourceOwner) onPushed).getResourceOwner());
        onPushed = trros.pushedResourceOwners.get(1);
        assertSame("Expected object on pushed stack.", nestedMock,
                ((ReleasableResourceOwner) onPushed).getResourceOwner());

        nestedMock.expects.release();
        toPopMock.expects.release();

        trros.commitTransaction();

        assertTrue("Expected pushed stack to be empty.",
                trros.pushedResourceOwners.isEmpty());
        assertTrue("Expected popped stack to be empty.",
                trros.poppedResourceOwners.isEmpty());    
        
        assertEquals("Expected original object to still be on stack",
                onStack, trros.getStack().peek());
    }

    /**
     * Tests the method rollbackTransaction(AbstractRecoverableTransaction)
     * with an unnested transaction.    
     */
    public void testRollbackTransactionImpl() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();

        final Object onStack = new Object();
        trros.push(onStack, false);

        final ResourceOwnerMock unnestedMock =
                 new ResourceOwnerMock("unnestedMock", expectations);
        trros.startTransaction();
        trros.push(unnestedMock, true);

        Object pushedObject = trros.pushedResourceOwners.peek();
        assertSame("Expected object on popped stack.", unnestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());

        // Nest another transaction
        final ResourceOwnerMock nestedMock =
                 new ResourceOwnerMock("nestedMock", expectations);
        trros.startTransaction();
        trros.push(nestedMock, true);
        pushedObject = trros.pushedResourceOwners.peek();
        assertSame("Expected object on popped stack.", nestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());

        trros.commitTransaction();

        pushedObject = trros.getStack().get(1);
        assertSame("Expected object on stack.", unnestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());
        pushedObject = trros.getStack().get(2);
        assertSame("Expected object on pushed stack.", nestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());
        
        pushedObject = trros.pushedResourceOwners.get(0);
        assertSame("Expected object on pushed stack.", unnestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());
        pushedObject = trros.pushedResourceOwners.get(1);
        assertSame("Expected object on pushed stack.", nestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());

        nestedMock.expects.release();
        unnestedMock.expects.release();

        trros.rollbackTransaction();

        assertTrue("Expected pushed stack to be empty.",
                trros.pushedResourceOwners.isEmpty());
        assertTrue("Expected popped stack to be empty.",
                trros.poppedResourceOwners.isEmpty());

        assertEquals("Expected original object to still be on stack",
                onStack, trros.getStack().peek()); 
        assertTrue("Expected stack to be back to original size.",
                trros.getStack().size() == 1);
    }

    /**
     * Test the method release()     
     */ 
    public void testRelease() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();

        final ResourceOwnerMock resourceOwnerMock =
                 new ResourceOwnerMock("resourceOwnerMock", expectations);
        trros.push(resourceOwnerMock, false);
        
        final ResourceOwnerMock releasableMock =
                 new ResourceOwnerMock("releasableMock", expectations);
        trros.push(releasableMock, true);

        final ResourceOwnerMock unnestedMock =
                 new ResourceOwnerMock("unnestedMock", expectations);
        trros.startTransaction();
        trros.push(unnestedMock, true);

        Object pushedObject = trros.pushedResourceOwners.peek();
        assertSame("Expected object on pushed stack.", unnestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());

        // Nest another transaction
        final ResourceOwnerMock nestedMock =
                 new ResourceOwnerMock("nestedMock", expectations);
        trros.startTransaction();
        trros.push(nestedMock, true);
        pushedObject = trros.pushedResourceOwners.peek();
        assertSame("Expected object on pushed stack.", nestedMock,
                ((ReleasableResourceOwner) pushedObject).getResourceOwner());
        
        trros.pop();
        Object poppedObject = trros.poppedResourceOwners.peek();
        assertSame("Expected object on popped stack.", nestedMock,
                ((ReleasableResourceOwner) poppedObject).getResourceOwner());

        // The following are released many times but I am not sure that is
        // correct but I will leave it for now.
        releasableMock.expects.release().atLeast(1);
        unnestedMock.expects.release().atLeast(1);
        nestedMock.expects.release().atLeast(1);

        trros.release();
    }

    /**
     * Test the method addStack(Stack)
     */ 
    public void testAddStack() throws Exception {
        Stack target = new Stack();    
        final Object object1 = new Object();
        target.push(object1);
        
        Stack toAdd = new Stack();
        final Object object2 = new Object();        
        toAdd.push(object2);
        final Object object3 = new Object();
        toAdd.push(object3);
        
        assertTrue("Expected one item on stack", target.size() == 1);
        assertTrue("Expected two items on stack", toAdd.size() == 2);
        
        
        RecoverableResourceOwnerStack.addStack(target, toAdd);        
        assertTrue("Expected stack to be empty.", toAdd.size() == 0);
        assertTrue("Expected three items on stack", target.size() == 3);
        
        assertSame("Unexpected item on stack.", object1, target.get(0));
        assertSame("Unexpected item on stack.", object2, target.get(1));
        assertSame("Unexpected item on stack.", object3, target.get(2));
    }

    /**
     * Test the method releaseStack(Stack)     
     */ 
    public void testReleaseStack() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();

        final ResourceOwnerMock unreleasableMock =
                 new ResourceOwnerMock("unreleasableMock", expectations);
        trros.push(unreleasableMock, false);

        final ResourceOwnerMock releasable1Mock =
                 new ResourceOwnerMock("releasable1Mock", expectations);
        trros.push(releasable1Mock, true);

        final ResourceOwnerMock releasable2Mock =
                 new ResourceOwnerMock("releasable2Mock", expectations);
        trros.push(releasable2Mock, true);
        
        Stack stack = trros.getStack();
        assertTrue("Expected three items on stack", stack.size() == 3);
        
        releasable1Mock.expects.release();
        releasable2Mock.expects.release();

        RecoverableResourceOwnerStack.releaseStack(stack);
        assertTrue("Expected zero items on stack", stack.isEmpty());
    }

    /**
     * Test the method contains(Stack, ResourceOwner)     
     */ 
    public void testContains() throws Exception {
        TestRecoverableResourceOwnerStack trros =
                new TestRecoverableResourceOwnerStack();

        final ResourceOwnerMock unreleasableMock =
                 new ResourceOwnerMock("unreleasableMock", expectations);
        trros.push(unreleasableMock, false);

        final ResourceOwnerMock releasable1Mock =
                 new ResourceOwnerMock("releasable1Mock", expectations);
        trros.push(releasable1Mock, true);

        final ResourceOwnerMock releasable2Mock =
                 new ResourceOwnerMock("releasable2Mock", expectations);
        trros.push(releasable2Mock, true);

        final ResourceOwnerMock notInStackMock =
                 new ResourceOwnerMock("notInStackMock", expectations);
        
        Stack stack = trros.getStack();
        assertTrue("Expected three items on stack", stack.size() == 3);
        
        boolean contains = 
                RecoverableResourceOwnerStack.contains(stack, unreleasableMock);
        assertTrue("Stack should contain the item.", contains);
        
        contains = RecoverableResourceOwnerStack.contains(stack, releasable1Mock);
        assertTrue("Stack should contain the item.", contains);
        
        contains = RecoverableResourceOwnerStack.contains(stack, releasable2Mock);
        assertTrue("Stack should contain the item.", contains);
        
        contains = RecoverableResourceOwnerStack.contains(stack, notInStackMock);
        assertFalse("Stack should not contain the item.", contains);
    }


    /**
     * This Test version of RecoverableResourceOwnerStack is used so that we
     * can get hold of the protected stack member field in the super class
     * which is in a different package.
     */ 
    public static class TestRecoverableResourceOwnerStack 
            extends RecoverableResourceOwnerStack {
        /**
         * Get the protected stack member field.
         * @return the protected stack member field.
         */ 
        public Stack getStack() {
            return stack;
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
