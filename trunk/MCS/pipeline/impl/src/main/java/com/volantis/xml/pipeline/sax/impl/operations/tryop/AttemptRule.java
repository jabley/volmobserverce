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
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This DynamicElementRule starts and end a block of recoverable markup.
 */
public abstract class AttemptRule
        extends DynamicElementRuleImpl {

    private final Element element;

    protected AttemptRule(Element element) {
        this.element = element;
    }

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess,
            ExpandedName elementName,
            Attributes attributes)
            throws SAXException {

        XMLPipelineContext context = dynamicProcess.getPipelineContext();

        TryModel model = (TryModel) context.findObject(TryProcess.class);
        model.startBlock(element);

        dynamicProcess.addErrorRecoveryPoint();

        return model;
    }

    // javadoc inherited
    public void endElement(
            DynamicProcess dynamicProcess,
            ExpandedName element,
            Object object)
            throws SAXException {
        // retrieve the pipeline context so that we can determine if
        // the pipeline is in error recovery mode.
        XMLPipelineContext context = dynamicProcess.getPipelineContext();

        // determine if we are in error recovery mode before the superclasses
        // endElement is invoked as the error recovery mode is reset whenever
        // the attempt operation process is removed from the pipeline.
        boolean inErrorRecoveryMode = context.inErrorRecoveryMode();

        TryModel model = (TryModel) object;
        model.endBlock(this.element);

        // if attempt succeeded (ie the pipeline was not in error recovery
        // mode) then exit the current element, skipping any alternative
        // operations.
        if (!inErrorRecoveryMode) {
            context.getFlowControlManager().exitCurrentElement();
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

 02-Jan-04	493/3	doug	VBM:2003120808 Fixed flow control problem within the AttemptProcess

 13-Aug-03	331/1	adrian	VBM:2003081001 implemented try operation

 ===========================================================================
*/
