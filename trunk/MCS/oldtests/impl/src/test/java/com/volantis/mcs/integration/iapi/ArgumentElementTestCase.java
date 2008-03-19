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
import com.volantis.mcs.integration.iapi.ArgumentElement;
import com.volantis.mcs.integration.iapi.IAPIConstants;
import com.volantis.mcs.integration.iapi.ArgumentsElement;
import com.volantis.mcs.integration.iapi.ArgumentAttributes;

/**
 * This class tests ArgumentElement
 */
public class ArgumentElementTestCase extends TestCaseAbstract {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";

    /**
     * Create a new instance of ArgumentElementTestCase.
     * @param name The name of the test case.
     */ 
    public ArgumentElementTestCase(String name) {
        super(name);
    }

    /**
     * Test the method elementEnd     
     */ 
    public void testElementEnd() throws Exception {
        ArgumentElement element = new ArgumentElement();
        // this method doesn't do anything so we can pass in two null params.
        int result = element.elementEnd(null, null);
        assertEquals("Unexpected result from elementEnd.",
                IAPIConstants.CONTINUE_PROCESSING, result);
    }

    /**
     * Test the method elementStart     
     */ 
    public void testElementStart() throws Exception {
        // Set up the contexts
        TestMarinerRequestContext requestContext = 
                new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();        
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        
        // Create a parent ArgumentsElement and push it onto the stack
        final String argName = "arg-name";
        final String argValue = "arg-value";
        final BooleanWrapper calledAdd = new BooleanWrapper(false);
        ArgumentsElement arguments = new ArgumentsElement() {
            void addArgument(String name, String value) {
                calledAdd.setValue(true);
                assertEquals("Unexpected value for name param.", 
                        argName, name);
                assertEquals("Unexpected value for value param.",
                        argValue, value);
            }
        };        
        pageContext.pushIAPIElement(arguments);
        
        // Create the ArgumentElement and test the elementStart method.
        ArgumentElement element = new ArgumentElement();
        ArgumentAttributes attrs = new ArgumentAttributes();
        attrs.setName(argName);
        attrs.setValue(argValue);
        
        int result = element.elementStart(requestContext, attrs);
        assertEquals("Unexpected result from elementStart.",
                IAPIConstants.SKIP_ELEMENT_BODY, result);
        
        assertTrue("ArgumentsElement.addArgument should have been called",
                calledAdd.getValue());
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
