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

package com.volantis.xml.expression.functions.pipeline;

import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import org.xml.sax.SAXParseException;

/**
 * Retrieve the id of the operation that was the source of the error,
 * i.e. the value of the getErrorSourceID() method of the XMLPipelineException instance.
 */
public class ErrorSourceIDFunction extends AbstractErrorFunction {

    public Value invoke(ExpressionContext context, Value[] arguments) throws ExpressionException {
        assertArgumentCount(arguments, 0);
        SAXParseException x = getException(context);
        ExpressionFactory factory = context.getFactory();
        Value result = Sequence.EMPTY;

        if (x instanceof XMLPipelineException) {
            XMLPipelineException pipelineException = (XMLPipelineException) x;
            String sourceID = pipelineException.getErrorSourceID();
            if (sourceID != null) {
                result = factory.createStringValue(sourceID);
            }
        }

        return result;
    }

    protected String getFunctionName() {
        return "errorSourceID";
    }
}
