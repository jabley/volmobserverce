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
import junitx.util.PrivateAccessor;

/**
 * This tests the utility class TransactionTracker
 */
public class TransactionTrackerTestCase extends TestCaseAbstract {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a new instance of TransactionTrackerTestCase
     * @param name the name of the testcase.
     */ 
    public TransactionTrackerTestCase(String name) {
        super(name);
    }
    
    /**
     * This method uses the PrivateAccessor class to get the transactionDepth
     * field of the specified TransactionTracker.
     * @param tracker The TransactionTracker from which we want the 
     * transactionDepth field.
     * @return the transactionDepth field value from the TransactionTracker.
     */ 
    private int getDepth(TransactionTracker tracker) throws Exception {
        Integer integer = (Integer)
                PrivateAccessor.getField(tracker, "transactionDepth");
        return integer.intValue();
    }
    
    /**
     * Test the method startTransaction()     
     */ 
    public void testStartTransaction() throws Exception {
        TransactionTracker tracker = new TransactionTracker();        
        assertTrue("Transaction depth should be 0.", getDepth(tracker) == 0);
        
        tracker.startTransaction();
        assertTrue("Transaction depth should be 1.", getDepth(tracker) == 1);
        tracker.startTransaction();
        assertTrue("Transaction depth should be 2.", getDepth(tracker) == 2);
        tracker.startTransaction();
        tracker.startTransaction();
        tracker.startTransaction();
        assertTrue("Transaction depth should be 5.", getDepth(tracker) == 5);
        
    }

    /**
     * Test the method commitTransaction()     
     */ 
    public void testCommitTransaction() throws Exception {
        TransactionTracker tracker = new TransactionTracker();
        PrivateAccessor.setField(tracker, "transactionDepth", new Integer(5));
        assertTrue("Transaction depth should be 5.", getDepth(tracker) == 5);
        
        tracker.commitTransaction();
        assertTrue("Transaction depth should be 4.", getDepth(tracker) == 4);
        tracker.commitTransaction();
        assertTrue("Transaction depth should be 3.", getDepth(tracker) == 3);
        tracker.commitTransaction();
        tracker.commitTransaction();
        tracker.commitTransaction();
        assertTrue("Transaction depth should be 0.", getDepth(tracker) == 0);
        
        try {
            tracker.commitTransaction();
            fail("Expected an exception trying to commit when transaction " +
                    "depth was already at zero minimum value.");
        } catch (IllegalStateException ise) {            
        }
    }

    /**
     * Test the method rollbackTransaction()     
     */ 
    public void testRollbackTransaction() throws Exception {
        TransactionTracker tracker = new TransactionTracker();
        PrivateAccessor.setField(tracker, "transactionDepth", new Integer(5));
        assertTrue("Transaction depth should be 5.", getDepth(tracker) == 5);

        tracker.rollbackTransaction();
        assertTrue("Transaction depth should be 4.", getDepth(tracker) == 4);
        tracker.rollbackTransaction();
        assertTrue("Transaction depth should be 3.", getDepth(tracker) == 3);
        tracker.rollbackTransaction();
        tracker.rollbackTransaction();
        tracker.rollbackTransaction();
        assertTrue("Transaction depth should be 0.", getDepth(tracker) == 0);

        try {
            tracker.rollbackTransaction();
            fail("Expected an exception trying to rollback when transaction " +
                    "depth was already at zero minimum value.");
        } catch (IllegalStateException ise) {
        }
    }

    /**
     * Test the method inTransaction()     
     */ 
    public void testInTransaction() throws Exception {
        TransactionTracker tracker = new TransactionTracker();
        assertFalse("Expected not to be in transaction as depth was 0.", 
                tracker.inTransaction());
        
        PrivateAccessor.setField(tracker, "transactionDepth", new Integer(1));
        assertTrue("Expected to be in transaction as depth was 1.",
                tracker.inTransaction());
        
        PrivateAccessor.setField(tracker, "transactionDepth", new Integer(7));
        assertTrue("Expected to be in transaction as depth was 7.",
                tracker.inTransaction());        
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
