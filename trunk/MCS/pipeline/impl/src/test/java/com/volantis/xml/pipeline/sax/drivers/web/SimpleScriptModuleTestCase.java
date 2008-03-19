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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.xml.sax.XMLFilter;
import org.xml.sax.helpers.XMLFilterImpl;
import junitx.util.PrivateAccessor;

import java.util.Map;

/**
 * Test the SimpleScriptModule.
 */
public class SimpleScriptModuleTestCase extends TestCaseAbstract {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";


    /**
     * The module to use for the testing.
     */
    private SimpleScriptModule module;

    public SimpleScriptModuleTestCase(String s) {
        super(s);
    }

    // javadoc inherited.
    protected void setUp() throws Exception {
        module = new SimpleScriptModule();
    }

    // javadoc inherited.
    protected void tearDown() throws Exception {
        module = null;
    }

    /**
     * Test the putting of values into the SimpleScriptModule.
     */
    public void testSelectScriptFilter() throws Exception {
        assertNull("Should not be found",
                   module.selectScriptFilter(null));

        assertNull("Should not be found",
                   module.selectScriptFilter("not there"));

        String contentType = "contentType";
        ScriptFilter filter = createScriptFilter(
            contentType,
            "org.xml.sax.helpers.XMLFilterImpl");

        module.putScriptFilter(filter);
        final XMLFilter xmlFilter = module.selectScriptFilter(contentType);
        assertNotNull("Should be found", xmlFilter);
        assertTrue("Value should match", xmlFilter instanceof XMLFilterImpl);
    }

    /**
     * Test the putting of values into the SimpleScriptModule�
     */
    public void testPutScriptFilter() throws Exception {
        ScriptFilter filter = null;
        final String contentType = "contentType";
        final String classNameNotXMLFilter =
            "com.volantis.xml.pipeline.sax.drivers.web.SimpleScriptModuleTestCase";
        final String classNameXMLFilter =
            "org.xml.sax.helpers.XMLFilterImpl";

        // Test the creation with a null content type and class name.
        try {
            filter = createScriptFilter(null, null);
            fail("Null class should not be found");
        } catch (NullPointerException e) {
            // ignore
        }
        assertTrue("Content type should not be in the list",
                   !isFilterInContainer(filter));

        ScriptFilter result = null;
        // Test the creation with a null content type a class name.
        filter = createScriptFilter(null, classNameNotXMLFilter);
        try {
            result = module.putScriptFilter(filter);
            fail("Expected and IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // ignore
        }
        assertTrue("Content type should not be in the list",
                   !isFilterInContainer(filter));
        assertNull("XMLFilter result should be null.", result);

        // Test the creation with a null content type and class name.
        filter = createScriptFilter(contentType, classNameNotXMLFilter);
        try {
            result = module.putScriptFilter(filter);
            fail("Expected and IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // ignore
        }
        assertTrue("Content type should not be in the list",
                   !isFilterInContainer(filter));
        assertNull("XMLFilter result should be null.", result);

        // Test the creation with a null content type and class name.
        // The script filter should be put in the hashmap.
        filter = createScriptFilter(contentType, classNameXMLFilter);
        result = module.putScriptFilter(filter);

        assertNull("XMLFilter result should be null.", result);
        assertTrue("Retrieved result should match",
                   isFilterInContainer(filter));

        result = module.putScriptFilter(filter);
        assertNotNull("XMLFilter result should be null.", result);
        assertTrue("Retrieved result should match",
                   isFilterInContainer(filter));

        String contentType2 = "contentType2";
        filter = createScriptFilter(contentType2, classNameXMLFilter);
        result = module.putScriptFilter(filter);
        assertNull("Result should not be null.", result);
        assertTrue("Retrieved result should match",
                   isFilterInContainer(filter));
    }

    /**
     * Helper method.
     *
     * @param  contentType the content type.
     * @param  className   the class name.
     * @return             the newly created scriptFilter (always non-null).
     */
    private ScriptFilter createScriptFilter(String contentType, String className)
        throws Exception {

        ScriptFilter filter = new ScriptFilter();
        filter.setContentType(contentType);
        filter.setScriptClass(Class.forName(className));
        return filter;
    }

    /**
     * Helper method.
     *
     * @param  filter the content type.
     * @return        true if the filter is in the container, false otherwise.
     */
    private boolean isFilterInContainer(ScriptFilter filter) throws Exception {
        if (filter != null) {
            Map map = (Map) PrivateAccessor.getField(module, "scriptFilters");
            if (map != null) {
                return map.containsKey(filter.getContentType());
            }
        }
        return false;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 06-Aug-03	310/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 ===========================================================================
*/
