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
package com.volantis.mcs.protocols.wml;

/**
 * Helper class used when testing openwave numeric shortcut menu rendering
 */
public class OpenwaveMenuTestCaseHelper {

    /**
     * Helper method that returns the expected Class that Openewave
     * protocols will return from their
     * {@link com.volantis.mcs.protocols.DOMProtocol
     * #createNumericShortcutMenuRenderer} method. The
     * {@link com.volantis.mcs.protocols.DOMProtocolTestAbstract
     * #testCreateNumericShortcutMenuRenderer} test, ensures that protocols
     * return the correct </code>MenuRenderer</code>. Openwave protocols
     * use a <code>OpenwaveNumericShortcutMenuRenderer</code> instance to
     * render menus. Currently the
     * {@link WMLOpenWave1_3TestCase} and {@link WMLPhoneDotComTestCase}
     * tests rely on this method to provide the expected renderer class.
     * @return the OpenwaveNumericShortcutMenuRenderer class.
     */
    public static final Class getExpectedNumericShortcutRendererClass() {
        return OpenwaveNumericShortcutMenuRenderer.class;
    }

    /**
     * Helper method that returns the expected output that Openewave
     * protocols will generate for the
     * {@link com.volantis.mcs.protocols.DOMProtocolTestAbstract
     * #testNumericShortcutMenus} test. Currently the
     * {@link WMLOpenWave1_3TestCase} and {@link WMLPhoneDotComTestCase}
     * tests rely on this method to provide the expected output.
     * @return the expected wml output.
     */
    public static final String
            getExpextedNumericShortcutRendererOutput() {

        return "<p mode=\"nowrap\">" +
                   "<select>" +
                       "<option onpick=\"http://www.volantis.com:8080" +
                                        "/volantis/sports.jsp\">Sports News" +
                        "</option>" +
                        "<option onpick=\"http://www.volantis.com:8080" +
                                         "/volantis/astrology.jsp\">Astrology" +
                        "</option>" +
                        "<option onpick=\"http://www.volantis.com:8080" +
                                         "/volantis/games.jsp\">Fun and Games" +
                        "</option>" +
                    "</select>" +
                "</p>";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Sep-03	1394/1	doug	VBM:2003090902 centralised common openwave menu rendering code

 ===========================================================================
*/
