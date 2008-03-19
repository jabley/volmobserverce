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
 * $Header: /src/voyager/com/volantis/mcs/gui/policyobject/PolicyObjectChooser.java,v 1.1 2002/05/23 14:16:20 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-May-03    Adrian          VBM:2003030509 - Created this junit testcase 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.cache;

import junit.framework.TestCase;
import com.volantis.shared.time.Period;

/**
 * This class tests CacheControl.
 */
public class CacheControlTestCase extends TestCase {

    /**
     * Construct a new instance of CacheControlTestCase
     */
    public CacheControlTestCase(String name) {
        super(name);
    }

    /**
     * Test setTimeToLive with a valid values.
     */
    public void testSetTimeToLivePositive() {
        CacheControl control = new CacheControl();

        control.setTimeToLive(CacheControlRule.calculateTimeToLive("5"));
        assertEquals(Period.inSeconds(5), control.getTimeToLive());

        control.setTimeToLive(Period.INDEFINITELY);
        assertEquals(Period.INDEFINITELY, control.getTimeToLive());
    }

    /**
     * Test setTimeToLive with invalid values
     */
    public void testTimeToLiveNegative() {
        CacheControl control = new CacheControl();

        try {
            control.setTimeToLive(CacheControlRule.calculateTimeToLive("-5"));
            fail("Expected IllegalArgumentException. -5 is not valid for " +
                    "timeToLive");
        } catch(IllegalArgumentException e) {
            // Success.
        }

        try {
            control.setTimeToLive(
                    CacheControlRule.calculateTimeToLive("not an integer"));
            fail("Expected NumberFormatException. \"not an integer\" is not " +
                    "an integer");
        } catch(IllegalArgumentException e) {
            // Success.
        }
    }
    /**
     * Test the equals(Object) method.     
     */
    public void testEquals() throws Exception {
        CacheControl control1 = new CacheControl();
        CacheControl control2 = new CacheControl();

        assertEquals("CacheControls should be equal.", control1, control2);

        CacheControl control3 = new CacheControl();
        control3.setTimeToLive(CacheControlRule.calculateTimeToLive("4"));
        assertTrue("CacheControls should not be equal.",
                !control1.equals(control3));

        CacheControl control5 = new CacheControl();
        control5.setTimeToLive(CacheControlRule.calculateTimeToLive("4"));
        assertTrue("CacheControls should be equal.",
                control3.equals(control5));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jun-04	761/1	claire	VBM:2004060803 Updated constants to remove dependency on GenericCache constants

 07-Aug-03	316/1	allan	VBM:2003080501 Redesigned CacheControl and added timeToLive

 ===========================================================================
*/
