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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/packagers/mime/MultipartApplicationContextTestCase.java,v 1.4 2003/02/24 13:27:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Feb-03    Byron           VBM:2003020305 - Created.
 * 18-Feb-03    Byron           VBM:2003020305 - Modified test case to work
 *                              correctly (testAddEncodedURLCandidate).
 * 21-Feb-03    Phil W-S        VBM:2003022006 - Added test for
 *                              initializeEncodedURLs.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.packagers.mime;

import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.runtime.packagers.PackageResources;
import junit.framework.TestCase;

/**
 * This class does this
 * @author Byron Wild
 */
public class MultipartApplicationContextTestCase extends TestCase {
    /**
     * The constructor for this test case
     */
    public MultipartApplicationContextTestCase(String name) {
        super(name);
    }

    /**
     * Test the adding of encoded urls.
     *
     * @throws java.lang.Exception if an exception occurs.
     */
    public void testAddEncodedURLCandidate() throws Exception {
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        MultipartApplicationContext context = new MultipartApplicationContext(requestContext);
        assertNull("Before add, urls should be null", context.getEncodedURLs());
        context.addEncodedURLCandidate(null);
        assertNotNull("After add, we should have urls", context.getEncodedURLs());

        PackageResources.Asset asset = new PackageResources.Asset("Name", false);

        final String ENCODED = "Test";
        context.addAssetURLMapping(ENCODED, asset);
        context.addEncodedURLCandidate(ENCODED);
        assertTrue("Have added one candidate",
                   context.getEncodedURLs().size() == 1);

        context.addEncodedURLCandidate(ENCODED);
        assertTrue("Add the same candidate again, still only have 1 candidate",
                   context.getEncodedURLs().size() == 1);

        context.addEncodedURLCandidate("Test not in List");
        assertTrue("Add value not in list, still only have 1 candidate",
                   context.getEncodedURLs().size() == 1);

        // Check the value of the item in the list.
        Object o = context.getEncodedURLs().get(0);
        assertTrue(o instanceof String);
        assertTrue(ENCODED.equals(o));
    }

    public void testInitializeEncodedURLs() throws Exception {
        TestMarinerRequestContext requestContext = new TestMarinerRequestContext();
        MultipartApplicationContext context = new MultipartApplicationContext(requestContext);
        assertNull("Before initialize, urls should be null",
                   context.getEncodedURLs());
        context.initializeEncodedURLs();
        assertNotNull("After initialize urls should be non-null",
                      context.getEncodedURLs());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
