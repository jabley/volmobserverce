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

package com.volantis.xml.pipeline.sax.impl.operations.tryop;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This DynamicElementRule creates a {@link TryProcess}
 *
 * <p>It provides a {@link ErrorListener} to the {@link TryProcess} as the
 * registered error listener. If the {@link TryProcess} detects an error and
 * enters error recovery mode then this {@link ErrorListener} will be informed
 * and will cause the DynamicProcess to skip to the next error recovery
 *  point.</p>
 */
public class TryRule
        extends DynamicElementRuleImpl {

    // Javadoc inherited.
    public Object startElement(
            final DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        TryProcess process = new TryProcess(new ErrorListener() {
            public void startErrorRecovery() {
                dynamicProcess.skipToErrorRecoveryPoint();
            }
        });
        dynamicProcess.addProcess(process);

        TryModel model = process;
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        context.pushObject(model, false);

        model.startTry();

        return process;
    }

    // Javadoc inherited.
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {

        TryProcess process = (TryProcess) object;
        TryModel model = process;

        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        if (!context.inErrorRecoveryMode()) {
            model.endTry();

            context.popObject(model);
        }

        dynamicProcess.removeProcess(process);

        if (!context.inErrorRecoveryMode()) {
            SAXParseException exception = model.getException(context);
            if (exception != null) {
                getTargetProcess(dynamicProcess).error(exception);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 ===========================================================================
*/
