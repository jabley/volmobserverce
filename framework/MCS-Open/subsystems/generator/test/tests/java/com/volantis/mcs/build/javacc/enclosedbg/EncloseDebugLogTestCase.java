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
/**
 * ----------------------------------------------------------------------------
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/build/javacc/enclosedbg/EncloseDebugLogTestCase.java,v 1.2 2003/03/24 09:51:10 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * -----------  --------------- -----------------------------------------------
 * 18-Mar-2003  sumit           VBM:2003022826 - Tests the debug log encloser
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.javacc.enclosedbg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

/**
 * This class tests the functionality of the debug log encloser
 * It tests if the javacc generate compiler wrapps lonely 
 * logger.debug statements in an if block e.g:
 * {
 * .
 * logger.debug(...);
 * .
 * }
 * becomes 
 * {
 * .
 * if(logger.isDebugEnabled()) {
 *     logger.debug(...);
 * }
 * .
 * }
 */
public class EncloseDebugLogTestCase extends TestCase{

    public EncloseDebugLogTestCase(String name) {
        super(name);
    }
    
    /**
     * This method tests that the two logger.debug statements are wrapped in
     * an if
     *
     */
    public void testEncloseDebug(){
        String testString = "logger.debug(\"hello\");\nlogger.debug(\"hello\");\nend";
        String expectedString = "if(logger.isDebugEnabled()) {\n" +            "    logger.debug(\"hello\");\n" +            "    logger.debug(\"hello\");\n" +            "}\n" +            "end";
        ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOut));
        System.setIn(new ByteArrayInputStream(testString.getBytes()));
        String[] args ={};
        try {
            EncloseDebugLog.main(args);
        } catch (ParseException pse) {
            fail("Test failed, invalid source string");
        }
        assertTrue("Enclosed debug log output is different from expected: "
            +systemOut.toString(), 
            expectedString.equals(systemOut.toString()));        
    }
    
    /**
     * This method tests that an already wrapped set of logger.debug
     * statements do not get wrapped again
     */
    
    public void testAlreadyEnclosedDebug(){
        String testString = "if (logger.logger.isDebugEnabled()) {\n" +
            "    logger.debug(\"hello\");\n" +
            "    logger.debug(\"hello\");" +
            "}";
        ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(systemOut));
        System.setIn(new ByteArrayInputStream(testString.getBytes()));
        String[] args ={};
        try {
            EncloseDebugLog.main(args);
        } catch (ParseException pse) {
            fail("Test failed, invalid source string");
        }
        assertTrue("Enclosed debug log output is different from expected: "
                    +systemOut.toString(), testString.equals(systemOut.toString()));        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 ===========================================================================
*/
