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
 * $Header: /src/voyager/com/volantis/testtools/ConsoleOutputManager.java,v 1.1 2003/02/25 14:24:54 geoff Exp $
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

import com.volantis.synergetics.testtools.Executor;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * A class to manage the collection of console output (i.e. System.out and
 * System.err). 
 * <p>
 * Useful for system initialisation test cases which throw errors before 
 * logging is up and running. Not to be used for much else!
 * <p>
 * Uses the Command pattern via {@link com.volantis.synergetics.testtools.Executor}. 
 */ 
public class ConsoleOutputManager {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002. ";

    private ByteArrayOutputStream outBuffer;
    
    private ByteArrayOutputStream errBuffer;
    
    /**
     * Execute a code block whilst collecting System.out and System.err, 
     * ensuring that the state of those variables is restored afterwards.
     * 
     * @param executor
     * @throws Exception
     */ 
    public void useCollectionWith(Executor executor) throws Exception {
        PrintStream out = System.out;
        outBuffer = new ByteArrayOutputStream();
        TeeOutputStream outTee = new TeeOutputStream(outBuffer, out); 
        System.setOut(new PrintStream(outTee));
        
        PrintStream err = System.err;
        errBuffer = new ByteArrayOutputStream();
        TeeOutputStream errTee = new TeeOutputStream(errBuffer, err); 
        System.setErr(new PrintStream(errTee));
        
        try {
            executor.execute();
        } finally {
            System.setOut(out);
            System.setErr(err);
        }
    }

    /**
     * Returns the collected System.out output.
     * 
     * @return the collected System.out output.
     */ 
    public String getOut() {
        return outBuffer.toString();
    }

    /**
     * Returns the collected System.err output.
     * 
     * @return the collected System.err output.
     */ 
    public String getErr() {
        return errBuffer.toString();
    }

    /**
     * Simplistic "tee" output stream ala Unix "tee" command. 
     * <p>
     * Not fast, and not particularly reliable :-).
     */ 
    private class TeeOutputStream extends OutputStream {

        private OutputStream original;
        private OutputStream copy;
        
        private TeeOutputStream(OutputStream original, OutputStream copy) {
            this.original = original;
            this.copy = copy;
        }

        public void write(int b) throws IOException {
            original.write(b);
            copy.write(b);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 10-Jun-03	356/1	allan	VBM:2003060907 Moved some common testtools into Synergetics

 ===========================================================================
*/
