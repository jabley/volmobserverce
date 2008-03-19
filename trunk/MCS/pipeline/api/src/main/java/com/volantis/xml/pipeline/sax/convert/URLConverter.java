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
package com.volantis.xml.pipeline.sax.convert;

import com.volantis.xml.pipeline.sax.XMLPipelineContext;

/**
 * Permits the URLToURLC process to defer to an external object to generate
 * the required URLC. Implementations of this must be thread-safe as the
 * configuration (and thus this converter) may be shared across multiple
 * threads.
 */
public interface URLConverter {
    /**
     * The given <tt>imageURL</tt> is converted, using the other given
     * parameters, into a URLC form.
     *
     * @param pipelineContext the pipeline context within which the conversion
     *                        is happening
     * @param imageURL        the original image URL to be converted
     * @param serverURL       address of the DMS server in the form of a URL
     * @return a URLC version of the original image URL
     * @throws URLConversionException if there is a problem converting the
     *                                given URL into a URLC
     */
    String toURLC(XMLPipelineContext pipelineContext, String imageURL,
                  String serverURL) throws URLConversionException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 07-Aug-03	299/7	philws	VBM:2003080504 Update javadoc

 07-Aug-03	299/5	philws	VBM:2003080504 Provide the pipeline context to the URLConverter on URLC conversion

 07-Aug-03	299/3	philws	VBM:2003080504 Remove the relativeWidth and maxFileSize attributes from the URL to URLC converter following architectural change

 06-Aug-03	299/1	philws	VBM:2003080504 Pipeline work for the DSB convertImageURLToDMS process

 ===========================================================================
*/
