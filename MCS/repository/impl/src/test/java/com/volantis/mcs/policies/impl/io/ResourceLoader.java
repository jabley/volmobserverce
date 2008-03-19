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
package com.volantis.mcs.policies.impl.io;

import com.volantis.synergetics.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

/**
 * @todo rename to TestResourceLoader to avoid confusion with runtime 
 * ResourceLoader interface
 */
public class ResourceLoader {

    private Class aClass;

    public ResourceLoader(Class aClass) {
        this.aClass = aClass;
    }

    public String getResourceAsString(String name) throws IOException {
        String content = null;
        InputStream stream = aClass.getResourceAsStream(name);
        if (stream != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            IOUtils.copyAndClose(stream, buffer);
            content = buffer.toString();
        } else {
            Assert.fail("Resource not available: " + name);
        }
        return content;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 ===========================================================================
*/
