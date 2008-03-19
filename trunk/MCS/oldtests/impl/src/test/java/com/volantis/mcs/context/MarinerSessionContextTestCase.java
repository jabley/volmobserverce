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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/context/MarinerSessionContextTestCase.java,v 1.2 2003/02/18 12:12:38 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Phil W-S        VBM:2003021302 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class MarinerSessionContextTestCase extends TestCaseAbstract {
    protected static class Entry {

        public String asset;
        public boolean cacheState;

        public Entry(String asset, boolean cacheState) {
            this.asset = asset;
            this.cacheState = cacheState;
        }
    }

    public MarinerSessionContextTestCase(String name) {
        super(name);
    }

    public void testIsAssetCachedNoCache() throws Exception {
        MarinerSessionContext sc = new MarinerSessionContext();

        assertTrue("unexpectedly stated qwerty as cached",
                   !sc.isAssetCached("qwerty"));
        assertTrue("unexpectedly stated qwerty as cached",
                   !sc.isAssetCached("qwerty"));
    }

    public void testIsAssetCached() throws Exception {
        MarinerSessionContext sc = new MarinerSessionContext();
        Entry[] twoItemCache =
            {new Entry("qwerty", false),
             new Entry("qwerty", true),
             new Entry("uiop", false),
             new Entry("uiop", true),
             new Entry("asdf", false),
             new Entry("asdf", true),
             new Entry("uiop", true),
             new Entry("qwerty", false),
             new Entry("asdf", false),
             new Entry("uiop", false)};
        Entry[] threeItemCache =
            {new Entry("uiop", false),
             new Entry("asdf", false),
             new Entry("qwerty", false),
             new Entry("qwerty", true),
             new Entry("asdf", true),
             new Entry("uiop", true),
             new Entry("ghjkl", false),
             new Entry("ghjkl", true),
             new Entry("qwerty", false),
             new Entry("uiop", true),
             new Entry("asdf", false),
             new Entry("ghjkl", false)};

        doIsAssetCachedTest(sc, "two item cache", twoItemCache, 2);
        doIsAssetCachedTest(sc, "three item cache", threeItemCache, 3);
    }

    protected void doIsAssetCachedTest(MarinerSessionContext sc,
                                       String message,
                                       Entry[] cache,
                                       int size) throws Exception {
        sc.setDeviceAssetCacheMaxSize(size);

        for (int i = 0;
             i < cache.length;
             i++) {
            assertEquals(message + " step " + i + " " +
                         cache[i].asset + " caching not as",
                         cache[i].cacheState,
                         sc.isAssetCached(cache[i].asset));
        }
    }

    /**
     * A utility method to return a project for use in the tests.
     *
     * @param source The policy source to use for the project.  This may be
     *               null as long as the test code is not then going to rely
     *               on that policy source having values.
     *
     * @return An initialized instance of a RuntimeProject
     */
    protected RuntimeProject createTestProject(PolicySource source) {
        final RuntimeProjectMock runtimeProjectMock =
                new RuntimeProjectMock("runtimeProjectMock", expectations);
        return runtimeProjectMock;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 15-Feb-05	6974/1	emma	VBM:2005021411 Allowing relative paths to devices.mdpr and xml repository

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	6010/1	claire	VBM:2004102701 mergevbm: External stylesheet handling with different projects in portals

 28-Oct-04	5995/1	claire	VBM:2004102701 External stylesheet handling with different projects in portals

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
