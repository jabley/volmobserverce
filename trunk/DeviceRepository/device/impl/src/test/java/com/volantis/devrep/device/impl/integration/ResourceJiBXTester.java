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

import com.volantis.synergetics.io.IOUtils;
import org.jibx.runtime.JiBXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceJiBXTester extends JiBXTester {

    private Class resourceClass;

    public ResourceJiBXTester(Class resourceClass) {
        this.resourceClass = resourceClass;
    }

    public Object unmarshall(Class aClass,
            String resourceName) throws JiBXException {

        InputStream resourceStream =
                resourceClass.getResourceAsStream(resourceName);
        InputStreamReader reader = new InputStreamReader(resourceStream);

        return unmarshall(aClass, reader);
    }

    public String getResource(String resourceName)
            throws IOException {

        InputStream resourceStream =
                resourceClass.getResourceAsStream(resourceName);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
        IOUtils.copyAndClose(resourceStream, baos);
        String expected = new String(baos.toByteArray());
        return expected;
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
