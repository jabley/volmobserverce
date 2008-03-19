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
 * This tests the class ResourceOwnerHelper.
 */
public class ResourceOwnerHelperTestCase extends TestCaseAbstract {
    private ResourceOwnerMock resourceOwnerMock1;
    private ResourceOwnerMock resourceOwnerMock2;

    protected void setUp() throws Exception {
        super.setUp();

        resourceOwnerMock1 = new ResourceOwnerMock("resourceOwnerMock1",
                expectations);

        resourceOwnerMock2 = new ResourceOwnerMock("resourceOwnerMock2",
                expectations);
    }

    /**
     * Test the method sameResourceOwner(Object, Object)     
     */     
    public void testSameResourceOwner() throws Exception {

        ReleasableResourceOwner releasable1 =
                new ReleasableResourceOwner(resourceOwnerMock1);
        ReleasableResourceOwner releasable2 =
                new ReleasableResourceOwner(resourceOwnerMock1);
        ReleasableResourceOwner releasable3 =
                new ReleasableResourceOwner(resourceOwnerMock2);
        
        boolean result = ResourceOwnerHelper.
                sameResourceOwner(resourceOwnerMock1, resourceOwnerMock1);
        assertTrue("The resource owners were not the same.", result);
        
        result = ResourceOwnerHelper.
                sameResourceOwner(resourceOwnerMock1, resourceOwnerMock2);
        assertFalse("The resource owners were the same.", result);
        
        result = ResourceOwnerHelper.
                sameResourceOwner(resourceOwnerMock1, releasable1);
        assertTrue("The resource owners were not the same.", result);
        
        result = ResourceOwnerHelper.
                sameResourceOwner(resourceOwnerMock1, releasable3);
        assertFalse("The resource owners were the same.", result);
        
        result = ResourceOwnerHelper.
                sameResourceOwner(releasable1, releasable2);
        assertTrue("The resource owners were not the same.", result);
        
        result = ResourceOwnerHelper.
                sameResourceOwner(releasable1, releasable3);
        assertFalse("The resource owners were the same.", result);
    }

    /**
     * Test the method getAsResourceOwner(Object)     
     */ 
    public void testGetAsResourceOwner() throws Exception {
        Object object1 = resourceOwnerMock1;
        Object object2 = new ReleasableResourceOwner((ResourceOwner)object1);
        Object object3 = new Object();
        
        ResourceOwner resourceOwner1 = 
                ResourceOwnerHelper.getAsResourceOwner(object1);
        assertSame("Unexpected value retrieved.", object1, resourceOwner1);
        
        ResourceOwner resourceOwner2 =
                ResourceOwnerHelper.getAsResourceOwner(object2);
        assertSame("Unexpected value retrieved.", object1, resourceOwner2);
        
        assertNull("Expected null from non ResourceOwner.", 
                ResourceOwnerHelper.getAsResourceOwner(object3));        
    }

    /**
     * Test the method releaseIfReleasableResourceOwner(Object)     
     */ 
    public void testReleaseIfReleasableResourceOwner() throws Exception {

        Object object = new Object();
        ReleasableResourceOwner releasable =
                new ReleasableResourceOwner(resourceOwnerMock1);
        
        // just to test that an arbitrary object doesn't break anything.
        ResourceOwnerHelper.releaseIfReleasableResourceOwner(object);
        
        // shouldn't release an unwrapped ResourceOwner.
        ResourceOwnerHelper.releaseIfReleasableResourceOwner(resourceOwnerMock1);

        // should release when wrapped in ReleasableResourceOwner
        resourceOwnerMock1.expects.release();
        ResourceOwnerHelper.releaseIfReleasableResourceOwner(releasable);
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
