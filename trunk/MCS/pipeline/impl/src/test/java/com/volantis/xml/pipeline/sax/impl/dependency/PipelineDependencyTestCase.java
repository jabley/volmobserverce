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

package com.volantis.xml.pipeline.sax.impl.dependency;

import com.volantis.xml.pipeline.sax.PipelineTestAbstract;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;

public class PipelineDependencyTestCase
        extends PipelineTestAbstract {

    protected void configureDynamicProcess(
            DynamicProcessConfiguration configuration) {
        super.configureDynamicProcess(configuration);

        DependencyTestRuleConfigurator.getDefaultInstance()
                .configure(configuration);
    }

    public void testSimple()
            throws Exception {

        doTest(XMLPipelineFactory.getDefaultInstance(),
                "xml/simple-input.xml", "xml/simple-expected.xml");
    }

    public void testComplex()
            throws Exception {

        doTest(XMLPipelineFactory.getDefaultInstance(),
                "xml/complex-input.xml", "xml/complex-expected.xml");
    }

    public void testIgnoring()
            throws Exception {

        doTest(XMLPipelineFactory.getDefaultInstance(),
                "xml/ignoring-input.xml", "xml/ignoring-expected.xml");
    }
}
