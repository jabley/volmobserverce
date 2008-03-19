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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.InitialContextConfiguration;
import com.volantis.mcs.runtime.configuration.ParameterConfiguration;

/**
 * The Rule set defining the manner in which to read in <jndi-configuration>
 * values.
 */
public class InitialContextRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    public InitialContextRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/initial-context";

        digester.addObjectCreate(pattern,  InitialContextConfiguration.class);
        digester.addSetNext(pattern, "addInitialContext");

        digester.addSetProperties(
            pattern,
            new String[] {"name"},
            new String[] {"name"}
        );

        final String patternParameter = pattern + "/parameter";
        digester.addObjectCreate(patternParameter, ParameterConfiguration.class);
        digester.addSetNext(patternParameter, "addParameter");
        digester.addSetProperties(
            patternParameter,
            new String[] {"name", "value"},
            new String[] {"name", "value"}
        );
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/2	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 12-Jun-03	316/3	byron	VBM:2003060403 Read cache and sql connector information from xml file

 ===========================================================================
*/
