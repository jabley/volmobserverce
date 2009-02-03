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

package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.SAXException;

/**
 * Base for most of the template related rules.
 */
public abstract class AbstractTemplateRule
        extends DynamicElementRuleImpl {

    /**
     * Get the template model from the process.
     *
     * @param dynamicProcess The dynamic process that invoked the rule.
     * @return The template model.
     * @throws SAXException If the model could not be retrieved.
     */
    protected TemplateModel getModel(DynamicProcess dynamicProcess)
            throws SAXException {
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        TemplateModel model = (TemplateModel) context.findObject(
                TemplateModel.class);

        if (model == null) {
            throw forwardFatalError(dynamicProcess,
                    "Missing template:apply operation");
        }

        return model;
    }

    /**
     * Checks whether in error recovery mode and only calls
     * {@link #endElementImpl} if it is not.
     *
     * @param dynamicProcess The DynamicProcess that has invoked this rule.
     * @param element        The name of the element for which the rule was
     *                       invoked.
     * @param object         The object that was returned by the matching
     *                       {@link #startElement} method.
     */
    public final void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        if (!dynamicProcess.getPipelineContext().inErrorRecoveryMode()) {
            endElementImpl(dynamicProcess, element, object);
        }
    }

    /**
     * Only called when not in error recovery mode.
     *
     * @param dynamicProcess The DynamicProcess that has invoked this rule.
     * @param element        The name of the element for which the rule was
     *                       invoked.
     * @param object         The object that was returned by the matching
     *                       {@link #startElement} method.
     */
    protected abstract void endElementImpl(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException;
}
