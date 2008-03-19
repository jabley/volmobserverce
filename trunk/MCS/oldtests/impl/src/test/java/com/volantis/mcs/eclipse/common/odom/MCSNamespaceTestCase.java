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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.repository.xml.XMLRepositoryConstants;
import com.volantis.mcs.repository.xml.PolicySchemas;
import org.jdom.Namespace;

/**
 * Test case for the {@link com.volantis.mcs.eclipse.common.odom.MCSNamespaceTestCase}
 */
public class MCSNamespaceTestCase extends TestCaseAbstract {

    /**
     * Test that the LPDM namespace has the correct prefix and URI.
     */
    public void testLPDMNamespace() {
        Namespace ns = MCSNamespace.LPDM;
        // check that the prefix is correct
        assertEquals("prefix not as ", "lpdm", ns.getPrefix());
        // check that the namespace URI is correct
        assertEquals("namespaceURI not as ",
                PolicySchemas.MARLIN_LPDM_2006_02.getNamespaceURL(),
                     ns.getURI());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/5	allan	VBM:2004081008 Remove invalid plugin dependencies

 19-Aug-04	5264/3	allan	VBM:2004081008 Remove invalid plugin dependencies

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 15-Dec-03	2160/1	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 ===========================================================================
*/
