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

package com.volantis.xml.pipeline.sax.impl.template.parameter;

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.template.TValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateExpressionValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;

import org.xml.sax.SAXException;

/**
 * Action that will create a variable after processing the body of the
 * parameter element.
 */
public class CreateVariableAction
        extends EndParameterAction {

    /**
     * The name of the variable.
     */
    private final String variable;

    /**
     * The name of the parameter for which the variable is to be created.
     */
    private final String parameter;

    /**
     * Initialise.
     *
     * @param model     The template model.
     * @param variable  The name of the variable.
     * @param parameter The name of the parameter for which the variable is to
     *                  be created.
     */
    public CreateVariableAction(
            TemplateModel model, String variable,
            String parameter) {
        super(model);
        this.variable = variable;
        this.parameter = parameter;
    }

    // Javadoc inherited.
    public void doAction(DynamicProcess dynamicProcess) throws SAXException {

        TValue value = model.getParameterBlock().query(parameter);

        XMLPipelineContext pipelineContext = dynamicProcess.getPipelineContext();

        ExpressionContext context = pipelineContext.getExpressionContext();

        // The given name could be a prefixed name, so go through these
        // hoops to ensure that we register the variable against the
        // correct namespace (the namespace for no prefix is "no namespace"
        // according to http://www.w3.org/TR/xpath20/#id-variables - this
        // is modelled using the "default namespace" for variables)
        NamespacePrefixTracker namespacePrefixTracker =
                context.getNamespacePrefixTracker();
        ExpandedName name = namespacePrefixTracker.resolveQName(
                new ImmutableQName(variable), "");

        // Create a new TemplateExpressionValue
        TemplateExpressionValue initialValue = new TemplateExpressionValue(
                parameter, value, pipelineContext);

        model.addPendingVariableDeclaration(name, initialValue);

        super.doAction(dynamicProcess);
    }
}
