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

package com.volantis.xml.pipeline.sax;

import org.xml.sax.XMLReader;

/**
 * A pipeline that is encapsulated inside an XMLReader.
 *
 * <p><strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels.</strong></p>
 *
 * <p>Instances of this class can be used to parse an XML InputSource,
 * process the resulting events through the pipeline and then forward the
 * events onto attached SAX handlers.</p>
 * <p>The events received from the parser are passed through a context updating
 * process that then forwards them down the pipeline and events from the
 * pipeline are passed through a context annotating process that then forwards
 * them to the attached handlers.</p>
 * @see <a href="package-summary.html#contextual-information">Contextual Information</a>
 * @see XMLPipelineFactory#createContextUpdatingProcess
 * @see XMLPipelineFactory#createContextAnnotatingProcess
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface XMLPipelineReader
        extends XMLReader, XMLPipeline {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 20-Jan-04	529/1	adrian	VBM:2004011904 Pipeline API updates in preparation for fully integrating ContextUpdating/Annotating processes

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
