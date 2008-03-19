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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLVersion1_2TestCase.java,v 1.3 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-03    Phil W-S        VBM:2003031110 - Created.
 * 24-Apr-03    Chris W         VBM:2003030404 - Inner class TestWMLVersion1_2
 *                              now calls TestDOMOutputBufferFactory instead of
 *                              DOMOutputBufferFactory to prevent
 *                              NullPointerException.
 * 27-May-03    Byron           VBM:2003051904 - Added templateTestDoMenu().
 *                              Updated TestWMLVersion1_2 in order to get/set
 *                              the style.
 * 23-May-03    Mat             VBM:2003042907 - Changed testGetXMLOutputter()
 *                              to testGetDocumentOutputter()
 * 27-May-03    Byron           VBM:2003051904 - Added templateTestDoMenu().
 *                              Updated TestWMLVersion1_2 in order to get/set
 *                              the style.
 * 30-May-03    Geoff           VBM:2003042905 - Fix merge problems between
 *                              Mat's and Allan's code. 
 * 30-May-03    Mat             VBM:2003042911 - Removed getDoucmentOutputter()
 *                              test as this method has been removed from 
 *                              the protocol.
 *                              Mat's and Allan's code.
 * 30-May-03    Chris W         VBM:2003052702 - getExpectedDissectingPaneMarkup
 *                              overridden so to make tests pass.
 * 30-May-03    Mat             VBM:2003042906 - Removed getDissector()
 *                              test case.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.FragmentLinkRenderer;
import com.volantis.mcs.wbsax.PublicIdCode;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 * 
 * NOTE: this is abstract because it's related protocol is also abstract.
 */
public abstract class WMLVersion1_2TestAbstract 
    extends WMLVersion1_1AccessTestCaseAbstract {
    
    /**
     * Defined here just to make the local name a bit shorter.
     */ 
    private static final String ACCESSKEY_ANNOTATION_NAME = 
            AccesskeyConstants.ACCESSKEY_ANNOTATION_ELEMENT;

    public WMLVersion1_2TestAbstract(String name) {
        super(name);
    }

    /**
     * WML v1.2 should pick up WML v1.1's name, dtd and code.
     */
    public void testProtocolHasCorrectDTD() throws Exception {

        final PublicIdCode publicIdCode = ((WMLRootConfiguration)protocol.
            getProtocolConfiguration()).publicIdCode;

        assertEquals("DTD should match",
                     "http://www.wapforum.org/DTD/wml_1.1.xml",
                     publicIdCode.getDtd());

        assertEquals("Name should match",
                     "-//WAPFORUM//DTD WML 1.1//EN",
                     publicIdCode.getName());

        assertEquals("Code should match",
                     0x04,
                     publicIdCode.getInteger());
    }


    protected String getExpectedNumericShortcutMenuOutput(
            boolean accesskeynumdisplay) {
        String contentPrefix = "";
        if (!accesskeynumdisplay) {
            contentPrefix = "x ";
        }
        return 
                "<" + ACCESSKEY_ANNOTATION_NAME + ">" +
                    "<a accesskey=\"x\" href=\"http://www.volantis.com:8080/volantis/sports.jsp\">" + 
                        contentPrefix + "Sports News" +
                    "</a>" +
                "</" + ACCESSKEY_ANNOTATION_NAME + ">" +
                "<br/>" +
                "<" + ACCESSKEY_ANNOTATION_NAME + ">" +
                    "<a accesskey=\"x\" href=\"http://www.volantis.com:8080/volantis/astrology.jsp\">" + 
                        contentPrefix + "Astrology" +
                    "</a>" +
                "</" + ACCESSKEY_ANNOTATION_NAME + ">" +
                "<br/>" +
                "<" + ACCESSKEY_ANNOTATION_NAME + ">" +
                    "<a accesskey=\"x\" href=\"http://www.volantis.com:8080/volantis/games.jsp\">" +
                        contentPrefix + "Fun and Games" +
                    "</a>" +
                "</" + ACCESSKEY_ANNOTATION_NAME + ">" +
                "<br/>";
    }
    
    // Inherit Javadoc.
    protected void checkNumericShortcutFragmentLinkRenderer(
            FragmentLinkRenderer renderer) {
        
        assertTrue("Numeric-shortcut renderer should be WML", renderer 
                instanceof WMLNumericShortcutFragmentLinkRenderer);
        
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Mar-05	7243/6	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 02-Mar-05	7120/1	geoff	VBM:2005022309 Illegal state exception for WML 1.3 devices.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 31-Mar-04	3662/1	steve	VBM:2004032907 Incorrect DTD for WML 1.1

 06-Oct-03	1469/6	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols (fix rework stuff from phil)

 02-Oct-03	1469/4	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 25-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 17-Aug-03	1052/1	allan	VBM:2003073101 Support styles on menu and menuitems

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 17-Jun-03	427/1	mat	VBM:2003061607 Changes to testcases

 13-Jun-03	399/1	mat	VBM:2003060503 Handle new special dissection tags

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 06-Jun-03	277/3	chrisw	VBM:2003052702 Merged changes from Metis to Mimas

 05-Jun-03	285/5	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
