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
package com.volantis.devrep.device.impl.integration;

import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class JiBXTester {

    public void marshall(Object object, Writer writer)
            throws JiBXException {
        IBindingFactory identificationBindingFactory =
                BindingDirectory.getFactory(object.getClass());

        IMarshallingContext marshallingContext =
                identificationBindingFactory.createMarshallingContext();

        marshallingContext.setIndent(4);
        marshallingContext.marshalDocument(object, "UTF-8", null, writer);
    }

    public String marshall(Object object)
            throws JiBXException {

        StringWriter writer = new StringWriter();
        marshall(object, writer);
        return writer.toString();
    }

    public Object unmarshall(Class aClass, Reader reader)
            throws JiBXException {

        IBindingFactory identificationBindingFactory =
                BindingDirectory.getFactory(aClass);

        IUnmarshallingContext unmarshallingContext =
                identificationBindingFactory.createUnmarshallingContext();

        Object unmarshalled = unmarshallingContext.unmarshalDocument(reader,
                null);
        return unmarshalled;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
