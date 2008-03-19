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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/gui/validation/parsers/javacc/TimerParserTestCase.java,v 1.2 2002/11/15 11:45:08 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-02    Allan           VBM:2002111110 - Test case for TimerParser.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.validation.parsers.javacc;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.StringReader;

import com.volantis.mcs.eclipse.validation.parsers.javacc.TimerParser;

/**
 * This class unit test the TimerParserclass.
 */
public class TimerParserTestCase 
    extends TestCase {

    public TimerParserTestCase(String name) {
        super(name);
    }

    /**
     * This method tests the method public void parse ( ).
     * All these tests succeed if the strings are parsed.
     */
    public void testParsePositive()
        throws Exception {

        String [] tests = {"1", "1h", "1min", "1s", "1ms",
                           "10:00", "10:00", "10:00.5", "10:40.123",
                           "1:00:00", "10:00:00", "100:00:00",
                           "1:00:00.9", "1:00:00.999", "10, 10", "10,10",
                           "1:00:00, 10min, 5, 6ms, 08:00, 67h, 9999.9ms"
        };

        int i=0;
        try {
            for(; i<tests.length; i++) {
                StringReader sr = new StringReader(tests[i]);
                TimerParser tp = new TimerParser(sr);
                tp.parse();
            }
        }
        catch(Throwable e) {
            fail("Could not parse \"" + tests[i] +
                    "\" with the following exception:\n" + e.getMessage());
        }
    }

    /**
     * This method tests the method public void parse ( ).
     * All these tests succeed if the strings are not parsed.
     */
    public void testParseNegative()
        throws Exception {

        String [] tests = {"a", "m", "min", "1mi", "10:10s",
                           "10:78", ",10", "10,", "10:10:78",
                           "10:10:10.", ".9", "60:10", "111:10"
        };

        int i=0;
        for(; i<tests.length; i++) {
            StringReader sr = new StringReader(tests[i]);
            TimerParser tp = new TimerParser(sr);
            try {
                tp.parse(); // this should always throw an exception
                fail("Managed to parse \"" + tests[i] + "\"" +
                        " even though this is not a legal sequence.");
            }
            catch(Throwable e) {
            }
        }
    }
    /**
     * This method tests the constructors for
     * the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestConstructors() {
        //
        // Test public TimerParser ( TimerParserTokenManager ) constructor
        //
        Assert.fail("public TimerParser ( TimerParserTokenManager ) not tested.");
        //
        // Test public TimerParser ( Reader ) constructor
        //
        Assert.fail("public TimerParser ( Reader ) not tested.");
        //
        // Test public TimerParser ( InputStream ) constructor
        //
        Assert.fail("public TimerParser ( InputStream ) not tested.");
    }

    /**
     * This method tests the method public void main ( [Ljava.lang.String; )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestMain()
        throws Exception {
        //
        // Test public void main ( [Ljava.lang.String; ) method.
        //
        Assert.fail("public void main ( [Ljava.lang.String; ) not tested.");
    }

    /**
     * This method tests the method public Token parseClockValues ( )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestParseClockValues()
        throws Exception {
        //
        // Test public Token parseClockValues ( ) method.
        //
        Assert.fail("public Token parseClockValues ( ) not tested.");
    }

    /**
     * This method tests the method public void ReInit ( InputStream )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestReInit()
        throws Exception {
        //
        // Test public void ReInit ( InputStream ) method.
        //
        Assert.fail("public void ReInit ( InputStream ) not tested.");
        //
        // Test public void ReInit ( InputStream ) method.
        //
        Assert.fail("public void ReInit ( InputStream ) not tested.");
        //
        // Test public void ReInit ( InputStream ) method.
        //
        Assert.fail("public void ReInit ( InputStream ) not tested.");
    }

    /**
     * This method tests the method public Token getNextToken ( )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestGetNextToken()
        throws Exception {
        //
        // Test public Token getNextToken ( ) method.
        //
        Assert.fail("public Token getNextToken ( ) not tested.");
    }

    /**
     * This method tests the method public Token getToken ( int )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestGetToken()
        throws Exception {
        //
        // Test public Token getToken ( int ) method.
        //
        Assert.fail("public Token getToken ( int ) not tested.");
    }

    /**
     * This method tests the method public ParseException generateParseException ( )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestGenerateParseException()
        throws Exception {
        //
        // Test public ParseException generateParseException ( ) method.
        //
        Assert.fail("public ParseException generateParseException ( ) not tested.");
    }

    /**
     * This method tests the method public void enable_tracing ( )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestEnable_tracing()
        throws Exception {
        //
        // Test public void enable_tracing ( ) method.
        //
        Assert.fail("public void enable_tracing ( ) not tested.");
    }

    /**
     * This method tests the method public void disable_tracing ( )
     * for the com.volantis.mcs.gui.validation.parsers.javacc.TimerParser class.
     */
    public void notestDisable_tracing()
        throws Exception {
        //
        // Test public void disable_tracing ( ) method.
        //
        Assert.fail("public void disable_tracing ( ) not tested.");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
