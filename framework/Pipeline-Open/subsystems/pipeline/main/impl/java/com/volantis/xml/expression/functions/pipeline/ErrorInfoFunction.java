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

import java.util.Map;

import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import org.xml.sax.SAXParseException;

/**
 * Retrieve the value of an error property,
 * i.e. an entry from the properties map returned by the getErrorProperties()
 * method of the XMLPipelineException  instance.
 */
public class ErrorInfoFunction extends AbstractErrorFunction {

    public Value invoke(ExpressionContext context, Value[] arguments) throws ExpressionException {
        assertArgumentCount(arguments, 1);
        StringValue arg = (StringValue) arguments[0];
        SAXParseException x = getException(context);
        ExpressionFactory factory = context.getFactory();
        Value result = Sequence.EMPTY;

        if (x instanceof XMLPipelineException) {
            XMLPipelineException pipelineException = (XMLPipelineException) x;
            Map errorProperties = pipelineException.getErrorProperties();
            if (errorProperties != null) {
                Object value = errorProperties.get(arg.asJavaString());
                if (value != null) {
                    result = factory.createStringValue(value.toString());
                }
            }
        }

        return result;
    }

    protected String getFunctionName() {
        return "errorInfo";
    }
}
