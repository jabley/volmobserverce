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

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Stack;

/**
 * This class tests AbstractRecoverableTransaction
 */
public abstract class AbstractRecoverableTransactionTestAbstract 
        extends TestCaseAbstract {
    
    /**
     * Test the method clone()     
     */ 
    public void testClone() throws Exception {
        AbstractRecoverableTransaction transaction = 
                getRecoverableTransaction();
        configureForCloneTest(transaction);
        
        // set the value of the inTransaction flag.
        transaction.inTransaction = true;
        // put an arbitrary value on the clones Stack.
        final Object onStack = new Object();
        transaction.clones.push(onStack);
        
        AbstractRecoverableTransaction clone = 
                (AbstractRecoverableTransaction)transaction.clone();
        
        assertTrue("Expected clone to have true inTransaction flag", 
                clone.inTransaction);
        
        assertEquals("Expected clones stack to be same.", transaction.clones, 
                clone.clones);
        
        assertSame("Expected value to be on stack.", 
                onStack, clone.clones.peek());
        
        doTestClone(clone);
    }
    
    /**
     * Get a RecoverableTransaction instance
     * @return an instance of RecoverableTransaction.
     */ 
    public abstract AbstractRecoverableTransaction getRecoverableTransaction();
    
    /**
     * Set the properties of the specified AbstractRecoverableTransaction
     * required to test the clone method.
     * @param clone the AbstractRecoverableTransaction to configure
     */ 
    public abstract void 
            configureForCloneTest(AbstractRecoverableTransaction clone);
    
    /**
     * Implementation method for testClone()     
     */ 
    public abstract void doTestClone(AbstractRecoverableTransaction clone) 
            throws Exception;

    /**
     * Test the method startTransaction()     
     */ 
    public void testStartTransaction() throws Exception {
        TestRecoverableTransaction transaction =
                new TestRecoverableTransaction();
                
        // put an arbitrary value on the clones Stack.
        final Object onStack = new Object();
        transaction.clones.push(onStack);
        
        transaction.startTransaction();

        assertTrue("Expected to have true inTransaction flag",
                transaction.inTransaction);
        
        assertSame("Expected value to be on stack.",
                onStack, transaction.clones.get(0));
        
        transaction.assertStartTransactionInvoked();
        
        TestRecoverableTransaction clone =
                (TestRecoverableTransaction) transaction.clones.peek();        

        assertTrue("Expected clone to have false inTransaction flag",
                !clone.inTransaction);

        assertSame("Expected value to be on stack.",
                onStack, clone.clones.get(0));
    }

    /**
     * Test the method commitTransaction()     
     */ 
    public void testCommitTransaction() throws Exception {
        TestRecoverableTransaction transaction =
                new TestRecoverableTransaction();
        
        try {
            transaction.commitTransaction();
            fail("Should not be able to commit when not in a transaction.");
        } catch (IllegalStateException e) { }
        
        transaction.startTransaction();
        transaction.commitTransaction();
        
        transaction.assertCommitTransactionInvoked();
    }

    /**
     * Test the method rollbackTransaction()     
     */ 
    public void testRollbackTransaction() throws Exception {
        TestRecoverableTransaction transaction =
                new TestRecoverableTransaction();

        try {
            transaction.commitTransaction();
            fail("Should not be able to rollback when not in a transaction.");
        } catch (IllegalStateException e) { }
               
        // although we are not in a transaction set the flag to show it is
        // correctly restored on rollback.
        transaction.inTransaction = true;
        Stack stack = transaction.clones;
        
        transaction.startTransaction();
        
        Stack clonedStack = ((AbstractRecoverableTransaction)
                transaction.clones.peek()).clones;
        
        assertNotSame("Should be different objects.", stack, clonedStack);
        
        transaction.rollbackTransaction();
        stack = transaction.clones;
        assertSame("The stack should now be the same as the cloned stack",
                clonedStack, stack);
        assertTrue("Expected inTransaction flag to be true.",
                transaction.inTransaction);
        
        transaction.assertRollbackTransactionInvoked();
    }


    /**
     * Test concrete implementation of AbstractRecoverableTransaction.
     */ 
    public static class TestRecoverableTransaction 
            extends AbstractRecoverableTransaction {

        /**
         * Default constructor to facilitate cloning.
         */ 
        public TestRecoverableTransaction() {
        }
        
        /**
         * This flag is used to determine whether {@link #startTransaction}
         * has been invoked.
         */ 
        private boolean startInvoked = false;
        
        /**
         * This flag is used to determine whether {@link #commitTransaction}
         * has been invoked.
         */
        private boolean commitInvoked = false;
        
        /**
         * This flag is used to determine whether {@link #rollbackTransaction}
         * has been invoked.
         */
        private boolean rollbackInvoked = false;                
        
        // javadoc inherited from superclass
        protected void startTransactionImpl() {
            startInvoked = true;
        }
        
        /**
         * Assert that the startTransaction method has been invoked.
         */ 
        public void assertStartTransactionInvoked() {
            assertTrue("Expected startTransaction to have been invoked.", 
                    startInvoked);
        }

        // javadoc inherited from superclass
        protected void commitTransactionImpl(
                AbstractRecoverableTransaction poppedState) {
            commitInvoked = true;
        }
        
        /**
         * Assert that the commitTransaction method has been invoked.
         */ 
        public void assertCommitTransactionInvoked() {
            assertTrue("Expected commitTransaction to have been invoked.",
                    commitInvoked);
        }

        // javadoc inherited from superclass
        protected void rollbackTransactionImpl(
                AbstractRecoverableTransaction poppedState) {
            rollbackInvoked = true;
        }
        
        /**
         * Assert that the rollbackTransaction method has been invoked.
         */ 
        public void assertRollbackTransactionInvoked() {
            assertTrue("Expected rollbackTransaction to have been invoked.",
                    rollbackInvoked);
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
