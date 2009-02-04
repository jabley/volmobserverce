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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/html/XHTMLBasic_Netfront3ConfigurationTestCase.java,v 1.2 2002/12/12 12:25:50 adrian Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Dec-02    Adrian          VBM:2002121116 - Created this to exercise 
 *                              changes made to constructor 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.html;

import junit.framework.TestCase;

/**
 * This class unit test the XHTMLBasic_Netfront3Configurationclass.
 */
public class XHTMLBasic_Netfront3ConfigurationTestCase 
    extends TestCase {

    /**
     * This method tests the constructors for
     * the com.volantis.mcs.protocols.html.XHTMLBasic_Netfront3Configuration class.
     */
    public void testConstructors() {
    }

    /**
     * This method tests the method public XHTMLBasic_Netfront3Configuration getXHTMLBasic_Netfront3Configuration ( )
     * for the com.volantis.mcs.protocols.html.XHTMLBasic_Netfront3Configuration class.
     */
    public void notestGetXHTMLBasic_Netfront3Configuration()
        throws Exception {
        //
        // Test public XHTMLBasic_Netfront3Configuration getXHTMLBasic_Netfront3Configuration ( ) method.
        //
        // Assert.fail("public XHTMLBasic_Netfront3Configuration getXHTMLBasic_Netfront3Configuration ( ) not tested.");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 12-Jul-05	9011/1	pduffin	VBM:2005071214 Refactored StyleValueFactory to change static methods to non static

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5354/3	tom	VBM:2004082008 Optimized imports and defuncted StandardStyleProperties

 02-Sep-04	5354/1	tom	VBM:2004082008 started device theme normalization

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 ===========================================================================
*/
