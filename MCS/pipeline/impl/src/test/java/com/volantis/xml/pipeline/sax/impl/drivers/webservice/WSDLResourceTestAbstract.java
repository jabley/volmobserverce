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

package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

import com.volantis.shared.net.url.URLContentManager;
import com.volantis.shared.net.url.URLContentManagerConfiguration;
import com.volantis.shared.time.Period;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineContextMock;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfigurationMock;
import com.volantis.xml.pipeline.sax.url.PipelineURLContentManager;
import org.custommonkey.xmlunit.XMLUnit;

public abstract class WSDLResourceTestAbstract
        extends TestCaseAbstract {

    protected XMLPipelineContextMock contextMock;
    private XMLPipelineConfigurationMock configurationMock;

    protected void setUp() throws Exception {
        super.setUp();

        contextMock = new XMLPipelineContextMock("contextMock", expectations);

        configurationMock = new XMLPipelineConfigurationMock(
                "configurationMock", expectations);

        contextMock.expects.getPipelineConfiguration()
                .returns(configurationMock).any();

        final URLContentManagerConfiguration config =
            new URLContentManagerConfiguration();
        config.setDefaultTimeout(Period.INDEFINITELY);
        PipelineURLContentManager manager =
            new PipelineURLContentManager(config);
        configurationMock.expects
                .retrieveConfiguration(URLContentManager.class)
                .returns(manager).any();
    }
}
