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
package com.volantis.xml.pipeline.sax.operations.evaluate;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipeline;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.config.XMLPipelineConfiguration;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule that adds a new {@link DynamicProcess} to the pipeline.
 */
public class EvaluateRule
        extends AbstractAddProcessRule {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new EvaluateRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    // Javadoc inherited.
    protected XMLProcess createProcess(
            DynamicProcess dynamicProcess, ExpandedName elementName,
            Attributes attributes) throws SAXException {

        XMLPipeline pipeline = dynamicProcess.getPipeline();

        // obain the pipeline configuration
        XMLPipelineContext context = pipeline.getPipelineContext();
        XMLPipelineConfiguration configuration =
                context.getPipelineConfiguration();

        // retrieve an XMLPipelineFactory
        XMLPipelineFactory factory = context.getPipelineFactory();

        // Create a DynamicProcess to be added to the pipeline.
        return factory.createDynamicProcess(configuration);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Oct-04	906/1	doug	VBM:2004101313 Added an Evaluate pipeline process

 ===========================================================================
*/
