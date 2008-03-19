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
package com.volantis.xml.pipeline.sax.impl.template.definition;

import com.volantis.xml.pipeline.sax.impl.template.StateUpdatingRule;
import com.volantis.xml.pipeline.sax.impl.template.TemplateSchema;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.expression.ExpressionContext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule to process template body elements.
 */
public class BodyRule extends StateUpdatingRule {

    /**
     * Initialise.
     */
    public BodyRule() {
        super(TemplateSchema.BODY);
    }

    // Javadoc inherited.
    public Object startElement(final DynamicProcess dynamicProcess,
                               final ExpandedName element,
                               final Attributes attributes) throws SAXException {

        // create a new stack frame and execute the pending variable
        // declarations 
        final ExpressionContext expressionContext =
            dynamicProcess.getPipelineContext().getExpressionContext();
        expressionContext.pushStackFrame();
        final TemplateModel model = getModel(dynamicProcess);
        model.executePendingVariableDeclarations();
        return super.startElement(dynamicProcess, element, attributes);
    }

    // Javadoc inherited.
    protected void endElementImpl(final DynamicProcess dynamicProcess,
                                  final ExpandedName element,
                                  final Object object) throws SAXException {
        super.endElementImpl(dynamicProcess, element, object);
        // remove the stack frame created for the body of the template
        final ExpressionContext expressionContext =
            dynamicProcess.getPipelineContext().getExpressionContext();
        expressionContext.popStackFrame();
    }
}
