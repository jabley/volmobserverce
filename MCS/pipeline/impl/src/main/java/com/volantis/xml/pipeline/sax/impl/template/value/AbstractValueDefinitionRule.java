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

package com.volantis.xml.pipeline.sax.impl.template.value;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.impl.template.AbstractTemplateRule;
import com.volantis.xml.pipeline.sax.impl.template.Complexity;
import com.volantis.xml.pipeline.sax.impl.template.EvaluationMode;
import com.volantis.xml.pipeline.sax.impl.template.TemplateModel;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base for rules related to definitions of values, either in bindings, or
 * parameters.
 */
public class AbstractValueDefinitionRule
        extends AbstractTemplateRule {

    private final Element element;

    /**
     * The complexity of the value.
     *
     * <p>This is what the resulting complexity is after it has been evaluated,
     * not the complexity of the content that generates the value.</p>
     */
    private final Complexity complexity;

    /**
     * Initialise.
     *
     * @param element
     * @param complexity The complexity of the resulting value.

     */
    protected AbstractValueDefinitionRule(
            Element element, Complexity complexity) {
        this.element = element;
        this.complexity = complexity;
    }

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        TemplateModel model = getModel(dynamicProcess);

        String evaluationMode = attributes.getValue("evaluationMode");
        EvaluationMode mode;
        if (evaluationMode == null) {
            mode = EvaluationMode.IMMEDIATE;
        } else {
            mode = EvaluationMode.literal(evaluationMode);
            if (mode == null) {
                throw forwardFatalError(dynamicProcess,
                        "Unknown evaluation mode '" + evaluationMode + "'");
            }
        }

        if (mode != EvaluationMode.IMMEDIATE) {
            dynamicProcess.passThroughElementContents();
        }

        model.startValueDefinition(this.element, mode);
        return model;
    }

    // Javadoc inherited.
    protected void endElementImpl(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        TemplateModel model = (TemplateModel) object;
        model.endValueDefinition(this.element, complexity);
    }
}
