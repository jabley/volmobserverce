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
 
package com.volantis.mcs.integration;

import com.volantis.mcs.context.MarinerRequestContext;

import java.util.Map;

/**
 * A test-only implementation of MarkupPlugin.  Provides a public Map of
 * arguments that can be retrieved in testcases.
 */
public class TestMarkupPlugin implements MarkupPlugin {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * A public Map of arguments that we can retrieve in test cases.
     */ 
    public Map initializeArgs;
    
    /**
     * A public Map of arguments that we can retrieve in test cases.
     */
    public Map processArgs;

    // javadoc inherited.
    public void initialize(Map arguments) {
        initializeArgs = arguments;
    }
    
    // javadoc inherited.
    public void process(MarinerRequestContext context, Map arguments) {
        processArgs = arguments;
    }

    // javadoc inherited.
    public void release() {
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
