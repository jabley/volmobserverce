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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.ManagementConfiguration;

/**
 * Rule set used to parse Management informtaion from the mcs-config.xml
 * configuration file.
 */
public class ManagementRuleSet extends PrefixRuleSet {


    /**
     * Standard constructor.
     * @param prefix the path to the element which will act as a parent to
     * this.
     */
    public ManagementRuleSet(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Add the parse rules for management sub elements.
     * @param digester
     */
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/management";
        digester.addObjectCreate(pattern,
                ManagementConfiguration.class);
        digester.addSetNext(pattern, "setManagementConfiguration");

        // add page-tracking
        final PageTrackingRuleSet pageTrackingRuleSet =
                new PageTrackingRuleSet(pattern);
        pageTrackingRuleSet.addRuleInstances(digester);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
