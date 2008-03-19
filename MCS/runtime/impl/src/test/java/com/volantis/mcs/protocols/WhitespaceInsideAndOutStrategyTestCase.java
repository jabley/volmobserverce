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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

/**
 * This class is responsible for testing the behaviour of tge
 * {@link WhiteSpaceInsideAndOutStrategy} class
 */
public class WhitespaceInsideAndOutStrategyTestCase
        extends AbstractWhitespaceFixStrategyTestAbstract {

    // javadoc inherted
    protected WhiteSpaceFixStrategy createStrategy() {
        return new WhiteSpaceInsideAndOutStrategy();
    }

    // javadoc inherited
    protected String getExpectedOpenElementSingleSpaceResult() {
        return "<body>This is  <strong> some strong text</strong>" +
                " with text after</body>";
    }

    // javadoc inherited
    protected String getExpectedCloseElementSingleSpaceResult() {
        return "<body>This is <strong>some strong text </strong>" +
               "  with text after</body>";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10675/6	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 ===========================================================================
*/
