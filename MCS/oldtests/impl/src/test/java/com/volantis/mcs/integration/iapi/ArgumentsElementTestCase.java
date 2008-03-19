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
 
package com.volantis.mcs.integration.iapi;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.BooleanWrapper;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.integration.iapi.InvokeElement;
import com.volantis.mcs.integration.iapi.ArgumentsElement;
import com.volantis.mcs.integration.iapi.IAPIConstants;

import java.util.Map;
import java.util.HashMap;

/**
 * This class tests ArgumentsElement
 */
public class ArgumentsElementTestCase extends TestCaseAbstract {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a new instance of ArgumentsElementTestCase
     * @param name The name of the testcase
     */ 
    public ArgumentsElementTestCase(String name) {
        super(name);
    }

    /**
     * Test the method elementStart
     * 
     * Although we could attempt to check the value of the private parent
     * field we know by implication that it will have been set if we
     * recieve the expected return value.
     * 
     * Also the parent field will be implicitly tested by testElementEnd.     
     */ 
    public void testElementStart() throws Exception {
        // Set up the contexts
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        
        InvokeElement invoke = new InvokeElement();
        pageContext.pushIAPIElement(invoke);
        
        ArgumentsElement element = new ArgumentsElement();
        int result = element.elementStart(requestContext, null);
        
        assertEquals("Unexpected result from elementStart.", 
                IAPIConstants.PROCESS_ELEMENT_BODY, result);
    }

    /**
     * Test the method elementEnd.     
     */ 
    public void testElementEnd() throws Exception {
        // Set up the contexts
        TestMarinerRequestContext requestContext =
                new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        ContextInternals.setMarinerPageContext(requestContext, pageContext);

        final String argName = new String("arg-name");
        final String argValue = new String("arg-value");
        final Map argsMap = new HashMap(1);
        argsMap.put(argName, argValue);

        final BooleanWrapper calledSetArgs = new BooleanWrapper(false);
        InvokeElement invoke = new InvokeElement() {
            void setArguments(Map arguments) {
                calledSetArgs.setValue(true);
                String value = (String) arguments.get(argName);
                assertEquals("Unexpected argument value.", argValue, value);
            }
        };
        pageContext.pushIAPIElement(invoke);

        ArgumentsElement element = new ArgumentsElement();        
        // call elementStart to ensure we have our parent.
        element.elementStart(requestContext, null);
        element.addArgument(argName, argValue);
        int result = element.elementEnd(requestContext, null);
        assertEquals("Unexpected result from elementEnd.",
                IAPIConstants.CONTINUE_PROCESSING, result);
        
        assertTrue("InvokeElement.setArguments should have been invoked.",
                calledSetArgs.getValue());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
