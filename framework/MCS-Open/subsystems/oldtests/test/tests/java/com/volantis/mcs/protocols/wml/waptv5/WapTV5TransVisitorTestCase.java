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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/waptv5/WapTV5TransVisitorTestCase.java,v 1.2 2003/04/07 09:34:37 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 04-Apr-03    Adrian          VBM:2003031701 - Added this testcase to test 
 *                              the whitespace handling when preprocessing a 
 *                              WapTV DOM 
 * 04-Jun-03    Byron           VBM:2003042204 - Modified test methods to use
 *                              generic test method in superclass. Added
 *                              getProtocolSpecificFactory() and removed
 *                              createTransVisitor.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml.waptv5;

import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.trans.TransFactory;
import com.volantis.mcs.protocols.trans.TransVisitorTestAbstract;
import com.volantis.mcs.devices.InternalDevice;

/**
 * This class tests the WapTV5TransVisitor class using the hierarchical test
 * framework.
 */
public class WapTV5TransVisitorTestCase extends TransVisitorTestAbstract {    

    // javadoc inherited from superclass
    protected TransFactory getProtocolSpecificFactory() {
        return new WapTV5TransFactory(null);
    }

    // javadoc inherited from superclass
    protected DOMProtocol createDOMProtocol(InternalDevice internalDevice) {

        ProtocolBuilder builder = new ProtocolBuilder();
        DOMProtocol protocol = (DOMProtocol) builder.build(
                new TestProtocolRegistry.TestWapTV5_WMLVersion1_3Factory(),
                internalDevice);
        return protocol;
    }
    
    /**
     * test the whitespace handling where a table has only whitespace siblings     
     */ 
    public void testWhitespaceHandling() throws Exception {
        String original =
                "<wml>" +
                  "<card>" +
                    "<p>" +
                      "<table>" +
                        "<tr>" +
                          "<td>" +
                            // The whitespace following the font tag should
                            // disappear in the processed dom 
                            "<font> " +
                              "<table>" +
                                "<tr>" +
                                  "<td>" +
                                    "test" +
                                  "</td>" +
                                "</tr>" +
                              "</table> " +
                            "</font>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</p>" +
                  "</card>" +
                "</wml>";

        String expected =
                "<wml>" +
                  "<card>" +
                    "<p>" +
                      "<table columns=\"1\">" +
                        "<tr>" +
                          "<td>" +
                            "<font>" +
                              "<table columns=\"1\">" +
                                "<tr>" +
                                  "<td>" +
                                    "test" +
                                  "</td>" +
                                "</tr>" +
                              "</table>" +
                            "</font>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</p>" +
                  "</card>" +
                "</wml>";


        doProcessingTest(original, expected, false);
    }
    
    /**
     * Test whitespace handling where table has more than just whitespace
     * siblings.
     * @throws Exception
     */ 
    public void testWhitespaceHandling2() throws Exception {
        String original =
                "<wml>" +
                  "<card>" +
                    "<p>" +
                      "<table>" +
                        "<tr>" +
                          "<td>" +
                            "some content" +
                            "<b>inside bold</b>" +
                            "       " +
                            "<table>" +
                              "<tr>" +
                                "<td>" +
                                  "some content" +
                                "</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</p>" +
                  "</card>" +
                "</wml>";

        String expected =
                "<wml>" +
                  "<card>" +
                    "<p>" +
                      "<table columns=\"1\">" +
                        "<tr>" +
                          "<td>" +
                            "some content" +
                            "<b>inside bold</b>" +
                          "</td>" +
                        "</tr>" +
                        "<tr>" +
                          "<td>" +
                            "<table columns=\"1\">" +
                              "<tr>" +
                                "<td>" +
                                  "some content" + 
                                "</td>" +
                              "</tr>" +
                            "</table>" +
                          "</td>" +
                        "</tr>" +
                      "</table>" +
                    "</p>" +
                  "</card>" +
                "</wml>";


        doProcessingTest(original, expected, false);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 19-Aug-05	9289/3	emma	VBM:2005081602 Refactoring UnabridgedTransformers so they're not singletons

 17-Aug-05	9289/1	emma	VBM:2005081602 Refactoring TransMapper and TransFactory so that they're not singletons

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
