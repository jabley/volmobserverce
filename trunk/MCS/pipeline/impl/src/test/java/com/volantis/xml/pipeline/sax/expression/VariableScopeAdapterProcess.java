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
package com.volantis.xml.pipeline.sax.expression;

import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import com.volantis.xml.expression.ExpressionContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * AdapterProcess that allows a variable scopes to be declared and undeclared.
 */ 
public class VariableScopeAdapterProcess extends AbstractAdapterProcess {

    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {        
    }

    // javadoc inherited
    public void startProcess() throws SAXException {
        // declare a variable scope
        ExpressionContext context = 
                getPipelineContext().getExpressionContext();
        context.pushStackFrame();
    }

    // javadoc inherited
    public void stopProcess() throws SAXException {
        // undeclare a variable scope
        ExpressionContext context = 
                getPipelineContext().getExpressionContext();
        context.popStackFrame();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
