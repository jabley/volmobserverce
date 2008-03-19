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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.url;

import com.volantis.shared.net.url.URLConfiguration;
import com.volantis.shared.net.url.URLContent;
import com.volantis.shared.net.url.URLContentManagerMock;
import com.volantis.shared.net.url.URLContentMock;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.net.URL;

/**
 * Test cases for {@link PipelineURLContentManager}.
 */
public class PipelineURLContentManagerTestCase
        extends TestCaseAbstract {
    private URLContentManagerMock contentManagerMock;
    private URLContentMock urlContentMock;
    private Period period;
    private String urlAsString;
    private PipelineURLContentManager manager;

    protected void setUp() throws Exception {
        super.setUp();

        contentManagerMock = new URLContentManagerMock(
                "contentManagerMock", expectations);

        urlContentMock = new URLContentMock("urlContentMock", expectations);

        period = Period.inSeconds(5);

        urlAsString = "http://blah/xyz";

        manager = new PipelineURLContentManager(contentManagerMock);
    }

    /**
     * Ensure that {@link PipelineURLContentManager#getURLContent(URL, Period, URLConfiguration)}
     * passes values through both ways.
     */
    public void testGetContentWithURL() throws Exception {

        URL url = new URL(urlAsString);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contentManagerMock.expects.getURLContent(url, period, null)
                .returns(urlContentMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        // Make sure that parameters are passed through properly both ways.
        URLContent content = manager.getURLContent(url, period, null);
        assertSame(urlContentMock, content);
    }

    /**
     * Ensure that
     * {@link PipelineURLContentManager#getURLContent(String, Period, URLConfiguration)}
     * passes values through both ways.
     */
    public void testGetContentWithURLAsString() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        contentManagerMock.expects.getURLContent(urlAsString, period, null)
                .returns(urlContentMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        PipelineURLContentManager manager = new PipelineURLContentManager(
                contentManagerMock);
        URLContent content;

        // Make sure that parameters are passed through properly both ways.
        content = manager.getURLContent(urlAsString, period, null);
        assertSame(urlContentMock, content);
    }
}
