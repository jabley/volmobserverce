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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/digester/BogusCallParamLiteralRule.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a bogus Rule 
 *                              implementation that saves a parameter from a 
 *                              literal value. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml.digester;

import our.apache.commons.digester.Rule;

/**
 * A bogus Rule implementation that saves a parameter from a literal value, 
 * to be used in a call generated by a surrounding CallMethodRule rule.
 * <p>
 * This is considered to be bogus because it subverts the overall design of 
 * the Digester, and is required to support something in our config file that 
 * is hard to understand and should never have been implemented, namely 
 * allowing the mere presence of an empty element to change the behaviour of
 * the configuration.
 * 
 * @deprecated Do not use this for new functionality; it is strictly to 
 * support the bogus existing format of mcs-config.dtd. 
 */
public class BogusCallParamLiteralRule extends Rule {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * The zero-relative index of the parameter we are saving.
     */
    protected int paramIndex = 0;
    
    protected String value;

    /**
     * Construct a "call parameter" rule that will save the value as the 
     * parameter value.
     *
     * @param paramIndex The zero-relative parameter number
     * @param value The value to save
     */
    public BogusCallParamLiteralRule(int paramIndex,
                         String value) {

        this.paramIndex = paramIndex;
        this.value = value;

    }
    

    // --------------------------------------------------------- Public Methods

    /**
     * Process the end of this element.
     */
    public void end() throws Exception {

        String parameters[] = (String[]) digester.peekParams();
        parameters[paramIndex] = value;

    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
