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

package com.volantis.mps.bms.impl.ser;

import com.volantis.mps.bms.Failures;
import com.volantis.mps.bms.SendRequest;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>Interface defining behaviour for serializing and deserializing the
 * objects.</p>
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation. User implementations of this interface are highly likely to
 * be incompatible with future releases of the product at both binary and
 * source levels.</strong></p>
 */
public interface ModelParser {

    /**
     * Read a SendRequest from the provided XML InputStream.
     *
     * @param in an InputStream - not null.
     * @return a SendRequest
     */
    public SendRequest readSendRequest(InputStream in);

    /**
     * Write a SendRequest to the provided OutputStream, in an XML form.
     *
     * @param sendRequest a SendRequest - not null.
     * @param out         an OutputStream - not null.
     */
    public void write(SendRequest sendRequest, OutputStream out);

    /**
     * Read a Failures from the provided XML InputStream.
     *
     * @param in an InputStream - not null.
     * @return a Failures
     */
    public Failures readFailures(InputStream in);

    /**
     * Write a Failures to the provided OutputStream, in an XML form.
     *
     * @param failures a Failures - not null.
     * @param out      an OutputStream - not null.
     */
    public void write(Failures failures, OutputStream out);
}
