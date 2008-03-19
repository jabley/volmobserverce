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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.schema;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.InputSource;

/**
 * Test case for JarFileEntityResolver.
 */
public class JarFileEntityResolverTestCase extends TestCaseAbstract {

    /**
     * Test resolveEntity using a relative URL.
     * @throws Exception if an error occurs
     */
    public void testResolveEntityRelative() throws Exception {
        JarFileEntityResolver resolver = new JarFileEntityResolver(this);

        InputSource result =
                resolver.resolveEntity(null,
                        "JarFileEntityResolverTestCase");
        assertNull("Managed to resolve an entity that is unknown.", result);

        resolver.addSystemIdMapping("com.volantis.mcs.utilities." +
                "JarFileEntityResolverTestCase",
                "com/volantis/xml/schema/JarFileEntityResolverTestCase.class");

        result = resolver.resolveEntity(null, "JarFileEntityResolverTestCase");

        assertNotNull("Expected a non null result.", result);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/3	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5577/1	adrianj	VBM:2004081004 Workaround for corrupted bundleresource URIs

 13-May-04	4333/1	allan	VBM:2004051015 Handle relative urls in JarFileEntityResolver.

 ===========================================================================
*/
