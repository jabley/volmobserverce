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
package com.volantis.xml.pipeline.sax.content;

import com.volantis.xml.pipeline.sax.config.Configuration;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;

/**
 * Class that encapsulates the Configuration required for the
 * Content XMLProcess.
 */
public class ContentConfiguration implements Configuration {

    /**
     * An XMLPipelineFactory instance
     */
    private XMLPipelineFactory factory;

    /**
     * Creates a new ContentConfiguration instance
     * @param factory an XMLPipelineFactory instance.
     * This XMLPipelineFactory <strong>must</strong> create a instance of
     * <code>XMLPreprocessingPipelineProcess</code> when the
     * <code>createPipelineProcess</code> method is called.
     */
    public ContentConfiguration(XMLPipelineFactory factory) {
        this.factory = factory;
    }

    /**
     * Returns an XMLPipelineFactory instance
     * @return an XMLPipelineFactory instance.
     */
    public XMLPipelineFactory getPipelineFactory() {
        return factory;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	285/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 12-Jun-03	53/1	doug	VBM:2003050603 JSP ContentTag refactoring

 ===========================================================================
*/
