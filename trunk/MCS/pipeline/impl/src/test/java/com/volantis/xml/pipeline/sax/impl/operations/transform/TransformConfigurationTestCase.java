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
package com.volantis.xml.pipeline.sax.impl.operations.transform;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.cache.GenericCache;
import com.volantis.xml.pipeline.sax.operations.transform.TransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;

/**
 * This class tests that the default value of TransformConfigurationAdapter is
 * correctly set.
 */
public class TransformConfigurationTestCase extends TestCaseAbstract {

    /**
     * The instance of the class being tested
     */
    protected TransformConfiguration config;

    /**
     * Default JUnit constructor
     * @param name
     */
    public TransformConfigurationTestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        config = createTestable();
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        super.tearDown();
        config = null;
    }

    /**
     * Factory method to create the class being tested
     * @return TransformConfigurationAdapter
     */
    protected TransformConfiguration createTestable() {
        return new DefaultTransformConfiguration();
    }

    /**
     * Tests that the default response is not to use compiled xslt.
     */
    public void testGetCompile() {
        assertTrue("Default should use xslt", 
                   !config.isTemplateCompilationRequired());
                                                
        config.setTemplateCompilationRequired(true);
        assertTrue("Should now use xsltc", 
                   config.isTemplateCompilationRequired());
    }

    /**
     * Tests that the default response is not to use a cache.  Also tests
     * the creation of a valid cache, and it's removal once it is no
     * longer necessary.
     */
    public void testGetCache() {
        assertTrue("Default should be uncached",
                !config.isTemplateCacheRequired());

        config.setTemplateCacheRequired(true);
        assertTrue("Should now use a cache", config.isTemplateCacheRequired());

        GenericCache cache = (GenericCache)
                ((DefaultTransformConfiguration)config).getTemplateCache();
        assertNotNull("Should have a cache", cache);
        String strategy = cache.getStrategy();
        assertNull("No strategy specified", strategy);
        // GenericCache sets this to 0 even though our cache construction
        // specified -1
        int max = cache.getMaxEntries();
        assertEquals("No limit on entries", 0, max);
        int timeout = cache.getTimeout();
        assertEquals("No timeout on entries", -1, timeout);

        // Test repeated requests do not kill an existing cache
        GenericCache secondCache = (GenericCache)
                ((DefaultTransformConfiguration)config).getTemplateCache();
        assertNotNull("Should have a cache", secondCache);
        assertEquals("Caches should be the same", cache, secondCache);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 23-Jan-04	545/3	claire	VBM:2004012202 Updated TransformConfiguration and related implementations and testcases

 22-Jan-04	545/1	claire	VBM:2004012202 transform configuration to support new template cache property

 07-Aug-03	268/4	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	268/1	chrisw	VBM:2003072905 implemented compilable attribute on transform

 ===========================================================================
*/
