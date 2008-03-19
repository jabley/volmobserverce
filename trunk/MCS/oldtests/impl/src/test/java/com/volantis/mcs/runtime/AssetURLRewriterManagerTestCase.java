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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/AssetURLRewriterManagerTestCase.java,v 1.2 2002/11/20 13:55:45 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Nov-02    Phil W-S        VBM:2002111816 - Created. Tests the
 *                              AssetURLRewriterManager.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import junit.framework.*;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.repository.RepositoryException;

/**
 * Unit test for the {@link AssetURLRewriterManager}.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class AssetURLRewriterManagerTestCase extends TestCase {
    /**
     * Used to represent the default asset URL rewriter within the unit
     * test environment (minimize the setup required).
     */
    protected static class LocalDefaultAssetURLRewriter
        implements AssetURLRewriter {
        public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                          Asset asset,
                                          AssetGroup assetGroup,
                                          MarinerURL marinerURL)
            throws RepositoryException {
            marinerURL.setParameterValue("default", "default");
            marinerURL.setParameterValue("override", "override");
            return marinerURL;
        }
    }

    /**
     * Used to represent the custom asset URL rewriter within the unit
     * test environment.
     */
    protected static class LocalCustomAssetURLRewriter
        implements AssetURLRewriter {
        public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                          Asset asset,
                                          AssetGroup assetGroup,
                                          MarinerURL marinerURL)
            throws RepositoryException {
            marinerURL.setParameterValue("custom", "custom");
            marinerURL.setParameterValue("override", "overridden");
            return marinerURL;
        }
    }

    /**
     * Simple specialization for test purposes to ensure that unit test
     * environment setup is as simple as possible.
     */
    protected static class LocalAssetURLRewriterManager
        extends AssetURLRewriterManager {
        public LocalAssetURLRewriterManager(String customAssetURLRewriterConfig)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
            super(customAssetURLRewriterConfig);
        }

        protected AssetURLRewriter createDefaultRewriter() {
            return new LocalDefaultAssetURLRewriter();
        }
    }

    public AssetURLRewriterManagerTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the constructors.
     */
    public void testConstructors() throws Exception {
        AssetURLRewriterManager aurm = new AssetURLRewriterManager(null);

        assertEquals("rewriters (null) array wrong size",
                     1,
                     aurm.rewriters.size());
        assertEquals("default rewriter wrong class",
                     DefaultAssetURLRewriter.class.getName(),
                     aurm.rewriters.get(0).getClass().getName());

        aurm = new AssetURLRewriterManager(LocalCustomAssetURLRewriter.class.
                                           getName());

        assertEquals("rewriters (custom) array wrong size",
                     2,
                     aurm.rewriters.size());
        assertEquals("custom rewriter wrong class",
                     LocalCustomAssetURLRewriter.class.getName(),
                     aurm.rewriters.get(1).getClass().getName());
    }

    /**
     * This method tests the method public MarinerURL
     * rewriteAssetURL(MarinerRequestContext,Asset,AssetGroup,MarinerURL).
     */
    public void testRewriteAssetURL()
        throws Exception {
        AssetURLRewriterManager aurm =
            new LocalAssetURLRewriterManager(LocalCustomAssetURLRewriter.class.
                                             getName());
        MarinerURL originalURL;
        MarinerURL expectedURL;
        MarinerURL actualURL;

        originalURL = new MarinerURL("http://www.volantis.com/example.jsp;" +
                                     "jsessionid=1234?" +
                                     "inherit=inherit&override=original");
        expectedURL = new MarinerURL(originalURL);
        expectedURL.setParameterValue("default", "default");
        expectedURL.setParameterValue("override", "override");
        expectedURL.setParameterValue("custom", "custom");
        expectedURL.setParameterValue("override", "overridden");

        actualURL = aurm.rewriteAssetURL(null, null, null, originalURL);

        assertEquals("rewritten asset URL not as",
                     expectedURL.getExternalForm(),
                     actualURL.getExternalForm());
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
