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
package com.volantis.mcs.runtime.configuration.xml.pipeline;

import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import com.volantis.xml.pipeline.sax.operations.transform.DefaultTransformConfiguration;
import our.apache.commons.digester.Digester;

/**
 * Provide a rule set definition for reading in the TransformConfigurationAdapter
 * configuration objects.
 *
 * An example of the configuration may look like: 
 *
 * <pipeline-configuration>
 *     <transform compile="true"/>
 * </pipeline-configuration>
 */
public class TransformConfigurationRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Initializes a <code>TransformConfigurationRuleSet</code> instance.
     * @param prefix 
     */
    public TransformConfigurationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // Javadoc inherited.
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/transform";
        digester.addObjectCreate(pattern, DefaultTransformConfiguration.class);
        digester.addSetNext(pattern, "setTransformConfiguration");
        digester.addSetProperties(pattern,
            new String[] {"compile", "cache"},
            new String[] {"templateCompilationRequired",
                          "templateCacheRequired"});
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/3	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 23-Jan-04	2714/2	claire	VBM:2004012203 Updated transform configuration to support template cache attribute

 07-Aug-03	906/4	chrisw	VBM:2003072905 Public API changed for transform configuration

 05-Aug-03	906/1	chrisw	VBM:2003072905 implemented compilable attribute on transform

 ===========================================================================
*/
