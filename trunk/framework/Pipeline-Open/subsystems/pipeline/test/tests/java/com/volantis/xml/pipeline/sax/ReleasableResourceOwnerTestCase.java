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
 
package com.volantis.xml.pipeline.sax;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * This class test ReleasableResourceOwner.
 */
public class ReleasableResourceOwnerTestCase
        extends TestCaseAbstract {
    
    /**
     * Test the method release()     
     */ 
    public void testRelease() throws Exception {


        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final ResourceOwnerMock resourceOwnerMock =
                new ResourceOwnerMock("resourceOwnerMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        resourceOwnerMock.expects.release();

        // =====================================================================
        //   Test Expectations
        // =====================================================================


        ReleasableResourceOwner releasable =
                new ReleasableResourceOwner(resourceOwnerMock);
        
        releasable.release();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
