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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.operations.foreach;

import com.volantis.xml.expression.InternalExpressionContext;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import org.xml.sax.SAXException;

/**
 * An action that pops a block scope.
 */
class PopBlockScopeAction
        implements EndElementAction {

    // Javadoc inherited.
    public void doAction(DynamicProcess dynamicProcess)
            throws SAXException {

        XMLPipelineContext pipelineContext =
                dynamicProcess.getPipeline().getPipelineContext();
        InternalExpressionContext expressionContext =
                (InternalExpressionContext)
                pipelineContext.getExpressionContext();

        expressionContext.popBlockScope();
    }
}
