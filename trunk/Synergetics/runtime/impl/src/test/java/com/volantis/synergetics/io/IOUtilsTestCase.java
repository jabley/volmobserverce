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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.io;

import junit.framework.TestCase;

import java.io.Writer;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTestCase extends TestCase {

    public void testNullWriterIsOK()  throws Exception {
        IOUtils.closeQuietly((Writer) null);
    }

    public void testNullInputStreamIsOK()  throws Exception {
        IOUtils.closeQuietly((InputStream) null);
    }

    public void testNullOutputStreamIsOK()  throws Exception {
        IOUtils.closeQuietly((OutputStream) null);
    }

    public void testNullReaderIsOK()  throws Exception {
        IOUtils.closeQuietly((Writer) null);
    }
}
