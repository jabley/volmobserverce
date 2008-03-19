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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/wml/WMLRootConfigurationTestCase.java,v 1.2 2003/03/17 11:21:44 philws Exp $
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
import com.volantis.mcs.protocols.ProtocolConfigurationTestCase;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class WMLRootConfigurationTestCase
        extends ProtocolConfigurationTestCase {

    protected static interface ConfigBooleanCommand {
        public boolean execute(String value,
                               WMLRootConfiguration config);
    }

    public void testIsElementAtomic() throws Exception {
        String[] atomics =
            {"a", "anchor", "do"};
        String[] nonAtomics =
            {"table", "p"};

        doTest("isElementAtomic", atomics, nonAtomics,
               new ConfigBooleanCommand() {
                   public boolean execute(String value,
                                          WMLRootConfiguration config) {
                       return config.isElementAtomic(value);
                   }
               });
    }

    public void testIsElementAlwaysEmpty() throws Exception {
        String[] empties =
            {"access", "br", "setvar", "img", "input", "meta", "noop",
             "postfield", "timer"};
        String[] nonEmpties =
            {"table", "onevent", "p"};

        doTest("isElementAlwaysEmpty", empties, nonEmpties,
               new ConfigBooleanCommand() {
                   public boolean execute(String value,
                                          WMLRootConfiguration config) {
                       return config.isElementAlwaysEmpty(value);
                   }
               });
    }

    /**
     * @todo later implement this test
     * @throws Exception
     */
    public void testAddCandidateElementAssetURLs() throws Exception {
    }

    public void testIsPermittedCardChild() {
        String[] permittedChildren =
            {"onevent", "timer", "do", WMLConstants.BLOCH_ELEMENT};
        String[] illegalChildren =
            {"pre", "table"};

        doTest("isPermittedCardChild", permittedChildren, illegalChildren,
               new ConfigBooleanCommand() {
                   public boolean execute(String value,
                                          WMLRootConfiguration config) {
                       return config.isPermittedCardChild(value);
                   }
               });
    }

    protected void doTest(String description,
                          String[] trueValues,
                          String[] falseValues,
                          ConfigBooleanCommand command) {
        for (int i = 0;
             i < trueValues.length;
             i++) {
            assertTrue(description + " should be true for " + trueValues[i],
                       command.execute(trueValues[i],
                                       (WMLRootConfiguration)config));
        }

        for (int i = 0;
             i < falseValues.length;
             i++) {
            assertTrue(description + " should be false for " + falseValues[i],
                       !command.execute(falseValues[i],
                                        (WMLRootConfiguration)config));
        }
    }

    // Javadoc inherited.
    protected ProtocolConfigurationImpl createProtocolConfiguration() {
        return new WMLRootConfiguration();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/2	ibush	VBM:2005091502 no_save on images

 29-Sep-05	9600/1	geoff	VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
