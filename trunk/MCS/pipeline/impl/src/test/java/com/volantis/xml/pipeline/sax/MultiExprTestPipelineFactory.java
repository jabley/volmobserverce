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

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcessConfiguration;
import com.volantis.xml.pipeline.sax.expression.MultiExprDynamicProcess;
import org.xml.sax.SAXException;

/**
 * Implementation of the PipelineFactory interface for use in test cases with
 * multiple expression declarations.
 */ 
public class MultiExprTestPipelineFactory extends TestPipelineFactory {

    // javadoc inherited
    public DynamicProcess createDynamicProcess(
            DynamicProcessConfiguration configuration) {

        return new MultiExprDynamicProcess(configuration);
    }

    // Javadoc inherited.
    public XMLPipeline createDynamicPipeline(XMLPipelineContext context) {
        try {
            return new MultiExprDynamicProcess(context);
        } catch (SAXException e) {
            throw new ExtendedRuntimeException(
                    "Could not create a pipeline", e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-05	8751/1	schaloner	VBM:2005060711 ExpressionProcess and PipelineExpressionHelper can now support multiple expression declaration markup

 ===========================================================================
*/
