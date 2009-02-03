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
 
package com.volantis.xml.pipeline.sax.tryop;

import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.namespace.ExpandedName;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class generates a fatal error
 */
public class GenerateErrorRule extends DynamicElementRuleImpl {

    protected XMLProcess createProcess(DynamicProcess dynamicProcess,
                                       ExpandedName elementName,
                                       Attributes attributes)
            throws SAXException {
        return null;
    }

    // todo javadoc this
    public Object startElement(DynamicProcess dynamicProcess,
                               ExpandedName elementName,
                               Attributes attributes)
            throws SAXException {

        XMLPipelineException exception = new XMLPipelineException(
                "Deliberately generated exception.", 
                dynamicProcess.getPipeline().getPipelineContext().
                getCurrentLocator());

        dynamicProcess.fatalError(exception);
        
        return null;
    }

    public void endElement(DynamicProcess dynamicProcess, ExpandedName element, Object object) throws SAXException {
        // no-op
    }
}

