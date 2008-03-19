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
 * $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.utilities.sax;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.InputStream;

/**
 * Class which resolves entities which are identified as resources on the
 * class path.
 * <p>
 * This class looks for entities whose system id is of the form
 * resource://<path> and resolves them using the following class loaders in
 * order:
 * <ol>
 *  <li>The thread context class loader.
 *  <li>The current class's class loader.
 *  <li>The system class loader.
 * </ol>
 * <p>
 * Entities which do not match cause this class to return null.
 */
public class ResourceEntityResolver
        implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId) {
        // If the system id does not match then return.
        if (!systemId.startsWith("resource://")) {
            return null;
        }

        String resource = systemId.substring(11);
        InputStream stream = null;
        ClassLoader cl;

        // Use the context class loader first if any.
        cl = Thread.currentThread().getContextClassLoader();
        if (cl != null) {
            stream = cl.getResourceAsStream(resource);
        }

        // If that did not work then use the current class's class loader.
        if (stream == null) {
            cl = getClass().getClassLoader();
            stream = cl.getResourceAsStream(resource);
        }

        // If that did not work then use the system class loader.
        if (stream == null) {
            cl = ClassLoader.getSystemClassLoader();
            stream = cl.getResourceAsStream(resource);
        }

        // If none of the above worked then return null.
        if (stream == null) {
            return null;
        }

        InputSource source = new InputSource(stream);
        source.setSystemId(systemId);

        return source;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
