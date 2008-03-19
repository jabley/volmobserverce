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

package com.volantis.xml.pipeline.sax;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.net.MalformedURLException;
import java.net.URL;

import com.volantis.xml.sax.ExtendedSAXException;

/**
 * Class which resolves all entities which have a system id set and no public
 * id set as resources on the class path.
 * <p>
 * This class looks for entities using the following class loaders in
 * order:
 * <ol>
 *  <li>The thread context class loader.
 *  <li>The current class's class loader.
 *  <li>The system class loader.
 * </ol>
 */
public class TestEntityResolver
        implements EntityResolver {

    private XMLPipelineContext pipelineContext;

    public TestEntityResolver(XMLPipelineContext context) {
        this.pipelineContext = context;
    }

    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException {

        // If the public id is not null then return.
        if (publicId != null || systemId == null) {
            return null;
        }

        // Resolve relative URLs to absolute ones.
        URL base = pipelineContext.getCurrentBaseURI();
        URL resolved = null;

        if (base == null) {
            String resource = systemId;
            ClassLoader cl;

            // Use the context class loader first if any.
            cl = Thread.currentThread().getContextClassLoader();
            if (cl != null) {
                resolved = cl.getResource(resource);
            }

            // If that did not work then use the current class's class loader.
            if (resolved == null) {
                cl = getClass().getClassLoader();
                resolved = cl.getResource(resource);
            }

            // If that did not work then use the system class loader.
            if (resolved == null) {
                cl = ClassLoader.getSystemClassLoader();
                resolved = cl.getResource(resource);
            }

        } else {
            try {
                resolved = new URL(base, systemId);
            } catch (MalformedURLException e) {
                throw new ExtendedSAXException(e);
            }
        }

        // If none of the above worked then return null.
        if (resolved == null) {
            return null;
        }

        systemId = resolved.toExternalForm();

        //InputStream stream = null;

        System.out.println("Resolved is " + systemId);

        InputSource source = new InputSource(systemId);
        //source.setSystemId (systemId);

        return source;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 ===========================================================================
*/
