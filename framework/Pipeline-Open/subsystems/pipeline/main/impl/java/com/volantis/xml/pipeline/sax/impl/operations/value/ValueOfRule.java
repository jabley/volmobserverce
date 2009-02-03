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

package com.volantis.xml.pipeline.sax.impl.operations.value;

import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionParser;
import com.volantis.xml.expression.Value;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for the pipeline:value-of and the diselect:value operation.
 */
public class ValueOfRule
        extends DynamicElementRuleImpl {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new ValueOfRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        XMLPipelineContext pipelineContext = dynamicProcess.getPipelineContext();
        ExpressionContext context = pipelineContext.getExpressionContext();

        // Get the expr attribute.
        String expr = attributes.getValue("expr");
        if (expr == null) {
            forwardError(dynamicProcess,
                    "'expr' attribute must be specified on " + element);
        } else {
            ExpressionFactory factory = ExpressionFactory.getDefaultInstance();
            ExpressionParser parser = factory.createExpressionParser();
            try {
                // Parse, and evaluate the expression.
                Expression expression = parser.parse(expr);
                Value value = expression.evaluate(context);

                // Stream the value's contents into the process following the
                // dynamic process. Don't do it into the dynamic process as
                // that will cause any markup to get reevaluated.
                value.streamContents(getTargetProcess(dynamicProcess));

            } catch (ExpressionException e) {
                dynamicProcess.fatalError(new XMLPipelineException(
                        "Error parsing '" + expr +
                        "' from expr attribute on " + element,
                        pipelineContext.getCurrentLocator(), e));
            }
        }

        return null;
    }

    // Javadoc inherited.
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
    }
}
