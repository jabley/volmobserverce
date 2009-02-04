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

package com.volantis.mcs.xdime.cp.samples;

import com.volantis.mcs.xdime.cp.xslt.XDIMECPConstants;
import com.volantis.mcs.xdime.cp.xslt.XSLTTestAbstract;

/**
 * Test case for the acme-xdime-cp.xsl stylesheet.
 */
public class AcmeHomePageTestCase extends XSLTTestAbstract {

    /**
     * Tests that a sample page with custom stylings is transformed according
     * to AN062.
     * @throws Exception
     */
    public void testMyPage() throws Exception {
        doTransform("myPage.xml transformation failed",
                getInputSourceForClassResource("acme-xdime-cp.xsl"),
                getInputSourceForClassResource("myPage.xml"),
                getInputSourceForClassResource("actual.xml"));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Jun-04	4645/1	pcameron	VBM:2004060306 Committed for integration

 ===========================================================================
*/
