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

package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ExcludeDeviceConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Digester rule set for the exclude-device children of the servlet-filter
 * element.
 */
public class ExcludeDeviceRuleSet extends PrefixRuleSet {

    /**
     * Initialise an instance of this class using the given prefix.
     * @param prefix the prefix to use
     */
    public ExcludeDeviceRuleSet(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Add instances to map exclude-device elements to the
     * ServletFilterConfiguration class.
     * @param digester the digester for the mariner configuration file.
     */
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/exclude-device";

        digester.addObjectCreate(pattern,
                ExcludeDeviceConfiguration.class);
        digester.addSetNext(pattern, "addExcludedDevice");

        digester.addSetProperties(pattern, "name", "name");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jan-05	6413/1	pcameron	VBM:2004120702 Servlet filter integration for XDIME

 ===========================================================================
*/
