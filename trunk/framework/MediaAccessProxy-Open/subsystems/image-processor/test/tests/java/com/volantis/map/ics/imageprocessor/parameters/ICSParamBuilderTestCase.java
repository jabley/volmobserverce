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
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.parameters;

import com.volantis.map.common.CommonFactory;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URI;

/**
 * Test for ICSParamBuilder
 */
public class ICSParamBuilderTestCase  extends TestCaseAbstract {

    private static final CommonFactory COMMON_FACTORY = CommonFactory.getInstance();

    public void testBuildFromAbsoluteURL() throws Exception {

        ICSParamBuilder builder = new ICSParamBuilder();
        MutableParameters params = COMMON_FACTORY.createMutableParameters();

        // Typical simple URL
        URI external = new URI("http://myownhost/images/cj24/fluffy.jpg?v.width=320");        
        builder.build(external, params);

        // Check if all the source url componentys were preserved
        assertEquals("http", params.getParameterValue(ParameterNames.SOURCE_PROTOCOL));
        assertEquals("myownhost", params.getParameterValue(ParameterNames.SOURCE_HOST));
        assertEquals("images/fluffy.jpg", params.getParameterValue(ParameterNames.SOURCE_PATH));

        // Check if ICS info was properly extracted
        assertEquals("cj24", params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE));
        assertEquals(320, params.getInteger(ParameterNames.IMAGE_WIDTH));

        // Complex URL
        external = new URI("https://john:secret@myhost:8000/images/cj24/get?imageId=me%2C%20myself%20and%20I&v.width=320#whatever");
        builder.build(external, params);

        // Check if all the source url componentys were preserved
        assertEquals("https", params.getParameterValue(ParameterNames.SOURCE_PROTOCOL));
        assertEquals("john:secret", params.getParameterValue(ParameterNames.SOURCE_USER_INFO));
        assertEquals("myhost", params.getParameterValue(ParameterNames.SOURCE_HOST));
        assertEquals(8000, params.getInteger(ParameterNames.SOURCE_PORT));
        assertEquals("images/get", params.getParameterValue(ParameterNames.SOURCE_PATH));
        assertEquals("imageId=me, myself and I", params.getParameterValue(ParameterNames.SOURCE_QUERY));
        assertEquals("whatever", params.getParameterValue(ParameterNames.SOURCE_FRAGMENT));

        // Check if ICS info was properly extracted
        assertEquals("cj24", params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE));
        assertEquals(320, params.getInteger(ParameterNames.IMAGE_WIDTH));
    }

    public void testBuildFromRelativeURL() throws Exception {

        ICSParamBuilder builder = new ICSParamBuilder();
        MutableParameters params = COMMON_FACTORY.createMutableParameters();
        URI external = new URI("images/gp8/get?imageId=me%2C+myself+and+I" +
                "&v.width=96" +
                "&v.imgHost=myotherhost" +
                "&v.imgProtocol=http" +
                "#whatever");

        builder.build(external, params);

        // Check if all the source url componentys were preserved
        assertEquals("http", params.getParameterValue(ParameterNames.SOURCE_PROTOCOL));
        assertFalse(params.containsName(ParameterNames.SOURCE_USER_INFO));
        assertEquals("myotherhost", params.getParameterValue(ParameterNames.SOURCE_HOST));
        assertFalse(params.containsName(ParameterNames.SOURCE_PORT));
        assertEquals("images/get", params.getParameterValue(ParameterNames.SOURCE_PATH));
        assertEquals("imageId=me, myself and I", params.getParameterValue(ParameterNames.SOURCE_QUERY));
        assertEquals("whatever", params.getParameterValue(ParameterNames.SOURCE_FRAGMENT));

        // Check if ICS info was properly extracted
        assertEquals("gp8", params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE));
        assertEquals(96, params.getInteger(ParameterNames.IMAGE_WIDTH));
    }

    public void testBuildFromRelativeURLAndNoImgHost() throws Exception {

        ICSParamBuilder builder = new ICSParamBuilder();
        MutableParameters params = COMMON_FACTORY.createMutableParameters();
        URI external = new URI("images/cj8/get?imageId=me%2C+myself+and+I" +
                "&v.width=128" +
                "#whatever");

        builder.build(external, params);

        // Check if all the source url componentys were preserved
        assertFalse(params.containsName(ParameterNames.SOURCE_PROTOCOL));
        assertFalse(params.containsName(ParameterNames.SOURCE_USER_INFO));
        assertFalse(params.containsName(ParameterNames.SOURCE_HOST));
        assertFalse(params.containsName(ParameterNames.SOURCE_PORT));
        assertEquals("images/get", params.getParameterValue(ParameterNames.SOURCE_PATH));
        assertEquals("imageId=me, myself and I", params.getParameterValue(ParameterNames.SOURCE_QUERY));
        assertEquals("whatever", params.getParameterValue(ParameterNames.SOURCE_FRAGMENT));

        // Check if ICS info was properly extracted
        assertEquals("cj8", params.getParameterValue(ParameterNames.DESTINATION_FORMAT_RULE));
        assertEquals(128, params.getInteger(ParameterNames.IMAGE_WIDTH));
    }
}
