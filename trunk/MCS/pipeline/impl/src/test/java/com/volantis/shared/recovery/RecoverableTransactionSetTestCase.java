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

import java.util.Set;

/**
 * This class tests RecoverableTransactionSet
 */
public class RecoverableTransactionSetTestCase 
        extends AbstractRecoverableTransactionTestAbstract {
        
    /**
     * An Object to store in the set
     */
    protected final static Object VALUE_A = new Object();
    
    /**
     * An Object to store in the set
     */
    protected final static Object VALUE_B = new Object();

    // javadoc inherited from superclass
    public AbstractRecoverableTransaction getRecoverableTransaction() {
        return new RecoverableTransactionSet();
    }

    // javadoc inherited from superclass
    public void configureForCloneTest(AbstractRecoverableTransaction clone) {
        RecoverableTransactionSet set = (RecoverableTransactionSet) clone;
        set.set.add(VALUE_A);
        set.set.add(VALUE_B);
    }

    // javadoc inherited from superclass
    public void doTestClone(AbstractRecoverableTransaction clone)
            throws Exception {
        RecoverableTransactionSet transactionSet = 
                (RecoverableTransactionSet) clone;
        Set set = transactionSet.getSet();
        Object[] values = set.toArray();
        
        boolean inArray = (values[0] == VALUE_A) || (values[1] == VALUE_A);
        assertTrue("Expected value to be in set.", inArray);
        
        inArray = (values[0] == VALUE_B) || (values[1] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);
        
        assertTrue("Expected two values in set", set.size() == 2);        
    }

    /**
     * Test the method add(Object)     
     */ 
    public void testAdd() throws Exception {
        RecoverableTransactionSet transactionSet = 
                new RecoverableTransactionSet();
        transactionSet.add(VALUE_A);
        transactionSet.add(VALUE_B);
        
        Set set = transactionSet.getSet();
        Object[] values = set.toArray();

        boolean inArray = (values[0] == VALUE_A) || (values[1] == VALUE_A);
        assertTrue("Expected value to be in set.", inArray);

        inArray = (values[0] == VALUE_B) || (values[1] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);

        assertTrue("Expected two values in set", set.size() == 2); 
    }

    /**
     * Test the method remove(Object)     
     */ 
    public void testRemove() throws Exception {
        RecoverableTransactionSet transactionSet =
                new RecoverableTransactionSet();
        transactionSet.add(VALUE_A);
        transactionSet.add(VALUE_B);

        Set set = transactionSet.getSet();
        Object[] values = set.toArray();

        boolean inArray = (values[0] == VALUE_A) || (values[1] == VALUE_A);
        assertTrue("Expected value to be in set.", inArray);

        inArray = (values[0] == VALUE_B) || (values[1] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);

        assertTrue("Expected two values in set", set.size() == 2);
        
        // remove one of the objects...
        transactionSet.remove(VALUE_A);
        
        set = transactionSet.getSet();
        values = set.toArray();

        inArray = (values[0] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);

        assertTrue("Expected one value in set", set.size() == 1);
    }

    /**
     * Test the method rollbackTransactionImpl(AbstractRecoverableTransaction)     
     */ 
    public void testRollbackTransactionImpl() throws Exception {
        RecoverableTransactionSet transactionSet =
                new RecoverableTransactionSet();
        transactionSet.add(VALUE_A);
        transactionSet.add(VALUE_B);

        Set set = transactionSet.getSet();
        Object[] values = set.toArray();

        boolean inArray = (values[0] == VALUE_A) || (values[1] == VALUE_A);
        assertTrue("Expected value to be in set.", inArray);

        inArray = (values[0] == VALUE_B) || (values[1] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);

        assertTrue("Expected two values in set", set.size() == 2);
        
        // start a transaction...
        transactionSet.startTransaction();
        
        // remove one of the objects...
        transactionSet.remove(VALUE_A);

        set = transactionSet.getSet();
        values = set.toArray();

        inArray = (values[0] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);

        assertTrue("Expected one value in set", set.size() == 1);
        
        // rollback the transaction...
        transactionSet.rollbackTransaction();
        
        set = transactionSet.getSet();
        values = set.toArray();

        inArray = (values[0] == VALUE_A) || (values[1] == VALUE_A);
        assertTrue("Expected value to be in set.", inArray);

        inArray = (values[0] == VALUE_B) || (values[1] == VALUE_B);
        assertTrue("Expected value to be in set.", inArray);

        assertTrue("Expected two values in set", set.size() == 2);
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
