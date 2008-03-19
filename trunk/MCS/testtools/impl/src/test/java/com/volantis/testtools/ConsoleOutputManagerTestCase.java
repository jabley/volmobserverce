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
 * $Header: /src/voyager/testsuite/unit/com/volantis/testtools/ConsoleOutputManagerTestCase.java,v 1.1 2003/02/25 14:24:54 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 25-Feb-03    Geoff           VBM:2003022506 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools;

import junit.framework.TestCase;

import java.io.PrintStream;

import com.volantis.synergetics.testtools.Executor;

/**
 * A test case to test the ConsoleOutputManager.
 */ 
public class ConsoleOutputManagerTestCase extends TestCase {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002. ";

    public ConsoleOutputManagerTestCase(String s) {
        super(s);
    }

    /**
     * Test that output collection works.
     *  
     * @throws Exception
     */ 
    public void testOutput() throws Exception {
        final String value = "some output";
        doOutput(new Executor() {
            public void execute() throws Exception {
                System.out.print(value);
            }
        }, value);
    }

    /**
     * Test that output collection works ok when there is no output.
     * 
     * @throws Exception
     */ 
    public void testOutputEmpty() throws Exception {
        doOutput(Executor.Null, "");
    }

    /**
     * Ensure System.out content is collected properly and that System.out
     * state is restored properly.
     *
     * @param executor executor to write some err content
     * @param value value to check was written  
     * @throws Exception
     */ 
    private void doOutput(Executor executor, String value) throws Exception {
        PrintStream out = System.out;
        ConsoleOutputManager mgr = new ConsoleOutputManager();
        mgr.useCollectionWith(executor);
        String output = mgr.getOut();
        
        assertNotNull(output);
        assertEquals(value, output);
        assertEquals(out, System.out);
    }
    
    /**
     * Test that error collection works.
     *  
     * @throws Exception
     */ 
    public void testError() throws Exception {
        final String value = "an error";
        doError(new Executor() {
            public void execute() throws Exception {
                System.err.print(value);
            }
        }, value);
    }

    /**
     * Test that error collection works ok when there is no output.
     * 
     * @throws Exception
     */ 
    public void testErrorEmpty() throws Exception {
        doError(Executor.Null, "");
    }

    /**
     * Ensure System.err content is collected properly and that System.err
     * state is restored properly.
     *
     * @param executor executor to write some err content
     * @param value value to check was written  
     * @throws Exception
     */ 
    private void doError(Executor executor, String value) throws Exception {
        PrintStream err = System.err;
        ConsoleOutputManager mgr = new ConsoleOutputManager();
        mgr.useCollectionWith(executor);
        String error = mgr.getErr();
        
        assertNotNull(error);
        assertEquals(value, error);
        assertEquals(err, System.err);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/
