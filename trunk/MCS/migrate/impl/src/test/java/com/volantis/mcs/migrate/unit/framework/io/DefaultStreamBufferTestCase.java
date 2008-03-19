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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.migrate.unit.framework.io;

import com.volantis.mcs.migrate.impl.framework.io.DefaultStreamBuffer;
import com.volantis.mcs.migrate.impl.framework.io.StreamBuffer;
import com.volantis.synergetics.testtools.ArrayObject;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A test case for {@link DefaultStreamBuffer}.
 */
public class DefaultStreamBufferTestCase extends TestCaseAbstract {

    /**
     * Create a stream buffer, write to its output and then read its input.
     * <p>
     * This should succeed as we are following the allowed state transitions of
     * the stream buffer properly.
     *
     * @throws IOException
     */
    public void testSuccess() throws IOException {

        StreamBuffer streamBuffer = new DefaultStreamBuffer();
        OutputStream output = streamBuffer.getOutput();
        output.write(new byte[]{1, 2, 3});
        output.close();
        InputStream input = streamBuffer.getInput();
        byte[] buffer = new byte[3];
        assertEquals(input.read(buffer), 3);
        assertEquals(new ArrayObject(buffer),
                     new ArrayObject(new byte[]{1, 2, 3}));
        assertEquals(input.read(buffer), -1);
        input.close();
    }

    /**
     * Ensure that if we write to the output after it is closed we fail.
     *
     * @throws IOException
     */
    public void testFailureWriteOutputAfterClose() throws IOException {

        StreamBuffer streamBuffer = new DefaultStreamBuffer();
        OutputStream output = streamBuffer.getOutput();
        output.write(new byte[]{1, 2, 3, 4, 5});
        output.close();
        try {
            output.write(new byte[]{1, 2, 3, 4, 5});
            fail("Should not be able to write output after close");
        } catch (Exception e) {
            // success
        }
    }

    /**
     * Ensure if we attempt to get the input before we close the output we fail.
     */
    public void testFailureGetInputAfterWriteOutput() {

        StreamBuffer streamBuffer = new DefaultStreamBuffer();
        try {
            OutputStream output = streamBuffer.getOutput();
            output.write(new byte[]{1, 2, 3, 4, 5});
            streamBuffer.getInput();
            fail("Input should not be available till output completes");
        } catch (Exception e) {
            // success
        }
    }

    /**
     * Ensure that if we try to read the input after it is closed we fail.
     *
     * @throws IOException
     */
    public void testFailureReadInputAfterCloseInput() throws IOException {

        StreamBuffer streamBuffer = new DefaultStreamBuffer();
        OutputStream output = streamBuffer.getOutput();
        output.write(new byte[]{1, 2, 3});
        output.close();
        InputStream input = streamBuffer.getInput();
        byte[] buffer = new byte[3];
        assertEquals(input.read(buffer), 3);
        assertEquals(new ArrayObject(buffer),
                     new ArrayObject(new byte[]{1, 2, 3}));
        assertEquals(input.read(buffer), -1);
        input.close();
        try {
            input.read(new byte[4]);
            fail("cannot read input once closed");
        } catch (Exception e) {
            // success
        }
    }

    /**
     * Ensure that if we try to get the input after it is closed we fail.
     *
     * @throws IOException
     */
    public void testFailureGetInputAfterCloseInput() throws IOException {

        StreamBuffer streamBuffer = new DefaultStreamBuffer();
        OutputStream output = streamBuffer.getOutput();
        output.write(new byte[]{1, 2, 3});
        output.close();
        InputStream input = streamBuffer.getInput();
        byte[] buffer = new byte[3];
        assertEquals(input.read(buffer), 3);
        assertEquals(new ArrayObject(buffer),
                     new ArrayObject(new byte[]{1, 2, 3}));
        assertEquals(input.read(buffer), -1);
        input.close();
        try {
            streamBuffer.getInput();
            fail("cannot get input once closed");
        } catch (Exception e) {
            // success
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 19-May-05	8036/3	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
