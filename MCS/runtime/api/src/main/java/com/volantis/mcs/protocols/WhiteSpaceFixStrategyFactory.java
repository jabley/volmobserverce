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

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * This class is responsible for dispensing instances of
 * {@link AbstractWhiteSpaceFixStrategy}.
 */
public final class WhiteSpaceFixStrategyFactory {

    /**
     * The logger
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(
            WhiteSpaceFixStrategyFactory.class);

    /**
     * The sole instance of this class.
     */
    private static final WhiteSpaceFixStrategyFactory strategyFactory =
            new WhiteSpaceFixStrategyFactory();

    /**
     * Ensure that only one instance of this class is created as per
     * Singleton pattern.
     */
    private WhiteSpaceFixStrategyFactory() {
    }

    /**
     * Returns the singleton instance of
     * <code>WhiteSpaceFixStrategyFactory</code>
     *
     * @return the singleton instance of
     * <code>WhiteSpaceFixStrategyFactory</code>
     */
    public static WhiteSpaceFixStrategyFactory getInstance() {
        return strategyFactory;
    }

    /**
     * Returns a {@link AbstractWhiteSpaceFixStrategy} based on the supplied
     * whitespaceFixRule.
     *
     * @param whitespaceFixRule specifies which strategy is to be returned.
     * <p>Should use one of:</p>
     *
     * <ul>
     *  <li> {@link DevicePolicyConstants#WHITESPACE_INSIDE_AND_OUTSIDE} </li>
     *  <li> {@link DevicePolicyConstants#WHITESPACE_INSIDE} </li>
     *  <li> {@link DevicePolicyConstants#NON_BREAKING_SPACE_OUTSIDE} </li>
     * </ul>
     * Any other value will result in an IllegalArgumentException.
     *
     *
     * @return a {@link AbstractWhiteSpaceFixStrategy} which corresponds to the
     * supplied rule.
     *
     * @throws IllegalArgumentException is an incorrect rule is supplied.
     */
    public AbstractWhiteSpaceFixStrategy getWhitespaceFixStrategy(
            String whitespaceFixRule) {

        AbstractWhiteSpaceFixStrategy whitespaceFixStrategy = null;
        if (DevicePolicyConstants.WHITESPACE_INSIDE.equals(whitespaceFixRule)) {
            whitespaceFixStrategy = new WhiteSpaceInsideStrategy();
        } else if (DevicePolicyConstants.WHITESPACE_INSIDE_AND_OUTSIDE.equals(
                whitespaceFixRule)) {
            whitespaceFixStrategy = new WhiteSpaceInsideAndOutStrategy();
        } else if (DevicePolicyConstants.NON_BREAKING_SPACE_OUTSIDE.equals(
                whitespaceFixRule)) {
            whitespaceFixStrategy = new NonBreakingSpaceOutsideStrategy();
        } else if (DevicePolicyConstants.SPACE_AND_NON_BREAKING_SPACES_OUTSIDE.equals(
                whitespaceFixRule)) {
            whitespaceFixStrategy = new SpaceAndNonBreakingSpacesOutsideStrategy();
        } else if (DevicePolicyConstants.NO_WHITESPACE_FIXING.equals(
                whitespaceFixRule)) {
            whitespaceFixStrategy = null;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Incorrect strategy rule supplied: " +
                             whitespaceFixRule);
            }
        }
        return whitespaceFixStrategy;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/3	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 03-Aug-05	9139/3	doug	VBM:2005071403 Fixed whitespace issues

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 22-Jul-05	9108/1	rgreenall	VBM:2005071403 Partial implementation.

 ===========================================================================
*/
