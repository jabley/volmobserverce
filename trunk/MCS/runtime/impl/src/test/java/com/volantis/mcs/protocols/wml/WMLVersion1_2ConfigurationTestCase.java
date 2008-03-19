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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLVersion1_2ConfigurationTestCase.java,v 1.2 2003/03/17 11:21:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Mar-03    Phil W-S        VBM:2003031110 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ProtocolConfigurationImpl;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class WMLVersion1_2ConfigurationTestCase
    extends WMLRootConfigurationTestCase {

    public void testIsPermittedCardChild() {
        String[] permittedChildren =
            {"onevent", "timer", "do", WMLConstants.BLOCH_ELEMENT, "pre"};
        String[] illegalChildren =
            {"table"};

        doTest("isPermittedCardChild", permittedChildren, illegalChildren,
               new ConfigBooleanCommand() {
                   public boolean execute(String value,
                                          WMLRootConfiguration config) {
                       return config.isPermittedCardChild(value);
                   }
               });
    }

    // Javadoc inherited.
    protected ProtocolConfigurationImpl createProtocolConfiguration() {
        return new WMLVersion1_2Configuration() {
            // make the abstract class instantiable.
        };
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/2	ibush	VBM:2005091502 no_save on images

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
