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

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.EndElementAction;
import com.volantis.xml.pipeline.sax.impl.template.AbstractTemplateRule;
import com.volantis.xml.pipeline.sax.impl.template.Complexity;
import com.volantis.xml.pipeline.sax.impl.template.ParameterBlock;
import com.volantis.xml.pipeline.sax.impl.template.TValue;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import com.volantis.xml.pipeline.sax.impl.template.TemplateValueSetter;
import com.volantis.xml.pipeline.sax.impl.template.Use;
import com.volantis.xml.pipeline.sax.impl.template.TemplateSchema;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for the template:parameter element.
 */
public class ParameterRule
        extends AbstractTemplateRule {

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        final TemplateModel model = getModel(dynamicProcess);

        model.startElement(TemplateSchema.PARAMETER);

        final String name = attributes.getValue("name");

        // Convert the type attribute value into an enumeration.
        String type = attributes.getValue("type");
        Complexity complexity;
        if (type == null) {
            complexity = Complexity.SIMPLE;
        } else {
            complexity = Complexity.literal(type);
            if (complexity == null) {
                throw forwardFatalError(dynamicProcess,
                        "Unknown type '" + type + "'");
            }
        }

        // Convert the use attribute value into an enumeration.
        String useAsString = attributes.getValue("use");
        Use use;
        if (useAsString == null) {
            use = Use.REQUIRED;
        } else {
            use = Use.literal(useAsString);
            if (use == null) {
                throw forwardFatalError(dynamicProcess,
                        "Unknown use '" + useAsString + "'");
            }
        }

        String defaultValue = attributes.getValue("default");

        String variable = attributes.getValue("expressionVariable");

        EndElementAction action;

        if (name == null) {
            throw forwardFatalError(dynamicProcess,
                    "A name must be provided for a parameter");
        } else if (use == Use.REQUIRED && defaultValue != null) {
            throw forwardFatalError(dynamicProcess,
                    "Default values cannot be specified for " +
                    "mandatory parameter '" + name + "'");
        } else {
            ParameterBlock parameters = model.getParameterBlock();
            TValue boundValue = parameters.query(name);

            if (boundValue != null) {
                if (boundValue.getComplexity() == Complexity.COMPLEX &&
                        complexity == Complexity.SIMPLE) {

                    throw forwardFatalError(dynamicProcess,
                            "A complex binding has been provided for the " +
                            "simple parameter " + name);
                } else {

                    model.transition(TemplateSchema.VALUE_START);

                    dynamicProcess.getPipelineContext()
                            .getFlowControlManager().exitCurrentElement();

                    // Record the fact that the existing binding value is
                    // "good"
                    boundValue.verify();

                    model.transition(TemplateSchema.VALUE_END);

                    action = new EndParameterAction(model);
                }
            } else if (use == Use.REQUIRED) {
                throw forwardFatalError(dynamicProcess,
                        "No binding has been provided for required " +
                        "parameter " + name);
            } else {
                // The parameter is optional and has no existing binding
                if (defaultValue != null) {

                    TValue value = model.createSimpleValue(defaultValue);
                    model.addDefaultValue(name, value);

                    action = new EndParameterAction(model);

                } else {
                    model.setValueSetter(new TemplateValueSetter() {

                        public void setTemplateValue(TValue value)
                                throws SAXException {
                            model.addDefaultValue(name, value);
                        }
                    });
                    action = new EndParameterAction(model);
                }
            }
        }

        if (variable == null) {
            // No variable has to be created so simply end the parameter.
            action = new EndParameterAction(model);
        } else {
            // Create a variable after processing the body of the element.
            action = new CreateVariableAction(model, variable, name);
        }

        return action;
    }

    // Javadoc inherited.
    protected void endElementImpl(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        EndElementAction action = (EndElementAction) object;
        action.doAction(dynamicProcess);
    }

}
