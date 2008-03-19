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
import com.volantis.mps.bms.impl.DefaultFailures;
import com.volantis.mps.bms.impl.DefaultSendRequest;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.InputStream;
import java.io.OutputStream;

public class JiBXModelParser implements ModelParser {

    // javadoc inherited
    public SendRequest readSendRequest(InputStream in) {
        try {
            IBindingFactory factory = BindingDirectory.getFactory(DefaultSendRequest.class);
            IUnmarshallingContext context = factory.createUnmarshallingContext();
            return (SendRequest) context.unmarshalDocument(in, null);

        } catch (JiBXException e) {
            throw new RuntimeException(e);
        }
    }

    // javadoc inherited
    public void write(SendRequest sendRequest, OutputStream out) {
        try {
            IBindingFactory factory = BindingDirectory.getFactory(DefaultSendRequest.class);
            IMarshallingContext context = factory.createMarshallingContext();

            // @todo any indentation, etc?
            context.marshalDocument(sendRequest, "UTF-8", null, out);
        } catch (JiBXException e) {
            throw new RuntimeException(e);
        }
    }

    // javadoc inherited
    public Failures readFailures(InputStream in) {
        try {
            IBindingFactory factory = BindingDirectory.getFactory(DefaultFailures.class);
            IUnmarshallingContext context = factory.createUnmarshallingContext();
            return (Failures) context.unmarshalDocument(in, null);

        } catch (JiBXException e) {
            throw new RuntimeException(e);
        }
    }

    // javadoc inherited
    public void write(Failures failures, OutputStream out) {
        try {
            IBindingFactory factory = BindingDirectory.getFactory(DefaultFailures.class);
            IMarshallingContext context = factory.createMarshallingContext();

            // @todo any indentation, etc?
            context.marshalDocument(failures, "UTF-8", null, out);
        } catch (JiBXException e) {
            throw new RuntimeException(e);
        }
    }
}
