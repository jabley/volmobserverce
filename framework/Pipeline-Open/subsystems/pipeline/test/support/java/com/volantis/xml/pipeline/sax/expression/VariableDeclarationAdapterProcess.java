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

import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.pipeline.sax.adapter.AbstractAdapterProcess;
import com.volantis.xml.namespace.ImmutableQName;
import com.volantis.xml.sax.ExtendedSAXParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.Locator;

/**
 * AdapterProcess that allows variables to be declared
 */ 
public class VariableDeclarationAdapterProcess extends AbstractAdapterProcess {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";
         
    /**
     * The name of the variable being declared
     */ 
    private String variableName;
    
    /**
     * The value of the variable being declared
     */
    private String variableValue;
    
    // javadoc inherited
    public void processAttributes(Attributes attributes) throws SAXException {
        variableName = attributes.getValue("name");
        variableValue = attributes.getValue("value");
        if(variableName == null || variableValue == null) {
            Locator l = getPipelineContext().getCurrentLocator(); 
            fatalError(new ExtendedSAXParseException(
                    "name and value attributes are compulsory", l));                                             
        }
    }

    // javadoc inherited
    public void startProcess() throws SAXException {

        // declare the variabe in the current ExpressionContext.
        ExpressionContext expressionContext
                = getPipelineContext().getExpressionContext();

        // The given name could be a prefixed name, so go through these hoops
        // to ensure that we register the variable against the correct
        // namespace (the namespace for no prefix is "no namespace" according
        // to http://www.w3.org/TR/xpath20/#id-variables - this is modelled
        // using the "default namespace" for variables)
        expressionContext.getCurrentScope().declareVariable(
            expressionContext.getNamespacePrefixTracker().
            resolveQName(new ImmutableQName(variableName), ""),
            expressionContext.getFactory().
            createStringValue(variableValue));
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Mar-05	7294/1	geoff	VBM:2005022311 Remote Repository Exceptions

 04-Mar-05	7247/1	geoff	VBM:2005022311 Remote Repository Exceptions

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Jul-03	222/2	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
