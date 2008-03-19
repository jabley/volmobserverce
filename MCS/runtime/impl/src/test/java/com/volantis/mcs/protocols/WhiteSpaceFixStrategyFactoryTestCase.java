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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;

/**
 * This class is responsible for testing the behaviour of
 * {@link WhiteSpaceFixStrategyFactory}
 */
public class WhiteSpaceFixStrategyFactoryTestCase extends TestCaseAbstract {

    public void testGetWhiteSpaceInsideStrategy() {

        testFactory(DevicePolicyConstants.WHITESPACE_INSIDE,
                    WhiteSpaceInsideStrategy.class);
    }

    public void testGetWhiteSpaceInsideAndOutStrategy() {
        testFactory(DevicePolicyConstants.WHITESPACE_INSIDE_AND_OUTSIDE,
                    WhiteSpaceInsideAndOutStrategy.class);
    }

    public void testGetNonBlockingSpaceStrategy() {
        testFactory(DevicePolicyConstants.NON_BREAKING_SPACE_OUTSIDE,
                    NonBreakingSpaceOutsideStrategy.class);
    }

    public void testGetSpaceAndNonBlockingSpacesStrategy() {
        testFactory(DevicePolicyConstants.SPACE_AND_NON_BREAKING_SPACES_OUTSIDE,
                    SpaceAndNonBreakingSpacesOutsideStrategy.class);
    }

    private void testFactory(String strategyRule, Class expectedStrategyClass) {

        AbstractWhiteSpaceFixStrategy whiteSpaceInsideStrategy =
                getWhiteSpaceFixStrategy(strategyRule);

        boolean strategyExpectedType = false;

        if (whiteSpaceInsideStrategy.getClass().
                equals(expectedStrategyClass)) {
            strategyExpectedType = true;
        }

        assertTrue("Incorrect strategy type returned.", strategyExpectedType);
    }

    /**
     * Helper method to obtain an AbstractWhiteSpaceFixStrategy.
     *
     * @param strategyRule the rule specifiying which strategy is required.
     *
     * @return the AbstractWhiteSpaceFixStrategy corresponding to the supplied
     * rule.
     */
    private AbstractWhiteSpaceFixStrategy getWhiteSpaceFixStrategy(
            String strategyRule) {
        WhiteSpaceFixStrategyFactory strategyFactory =
                WhiteSpaceFixStrategyFactory.getInstance();

        AbstractWhiteSpaceFixStrategy whiteSpaceInsideStrategy =
                strategyFactory.getWhitespaceFixStrategy(strategyRule);

        return whiteSpaceInsideStrategy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 8-Dec-05	10675/6	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 03-Aug-05	9139/3	doug	VBM:2005071403 Fixed whitespace issues

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 22-Jul-05	9108/1	rgreenall	VBM:2005071403 Partial implementation.

 ===========================================================================
*/
