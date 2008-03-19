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
package com.volantis.mcs.accessors.xml;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.devrep.repository.api.accessors.xml.EclipseEntityResolver;
import org.xml.sax.InputSource;

public class EclipseEntityResolverTestCase extends TestCaseAbstract {

    /**
     * Test resolveEntity with mangled bundleresource:// URIs as generated
     * by Eclipse 3.0. These should be treated as normal bundleresource://
     * URIs.
     * @throws Exception if an error occurs
     */
    public void testResolveEntityBundleresource() throws Exception {
        
        EclipseEntityResolver resolver = new EclipseEntityResolver();
        String bundleResourceURI = "bundleresource://104/test";
        String badBundleResourceURI = "file://" + bundleResourceURI;

        // Attempt to resolve a valid bundleresource URI
        InputSource result =
                resolver.resolveEntity(null, bundleResourceURI);
        assertNull(bundleResourceURI + " should resolve using the default " +
                "mechanism (ie. resolveEntity should return null)", result);

        // Attempt to resolve the equivalent mangled bundleresource URI
        result = resolver.resolveEntity(null, badBundleResourceURI);
        assertNotNull(badBundleResourceURI + " should resolve within " +
                "EclipseEntityResolver", result);
        assertEquals(badBundleResourceURI + " should be resolved as " +
                bundleResourceURI, bundleResourceURI, result.getSystemId());
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 13-Dec-05	10345/2	adrianj	VBM:2005111601 Add style rule view

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 ===========================================================================
*/
