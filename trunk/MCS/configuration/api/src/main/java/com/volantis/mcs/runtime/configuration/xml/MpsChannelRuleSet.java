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

import com.volantis.mcs.runtime.configuration.MpsChannelConfiguration;

import our.apache.commons.digester.Digester;


/**
 * Adds digester rules for the mps tag.  
 * 
 * @todo this should be moved into MPS repository once the plugin 
 * configuration API is made available to MPS. Note that this implies that the 
 * EnabledDigester needs to be made available to MPS as well.
 */ 
public class MpsChannelRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Construct an instance of this class, using the prefix provided.
     * 
     * @param prefix the prefix to add the rules to the digester under.
     */ 
    public MpsChannelRuleSet(String prefix) {
        this.prefix = prefix;
    }
    
    /**
     * Add the set of Rule instances defined in this RuleSet to the specified 
     * Digester instance, associating them with our namespace URI (if any). 
     * This method should only be called by a Digester instance.
     * @param digester Digester instance to which the new Rule instances should
     *  be added.
     */ 
    public void addRuleInstances(Digester digester) {
        String pattern = prefix + "/channel";
        digester.addObjectCreate(pattern, MpsChannelConfiguration.class);
        digester.addSetNext(pattern, "addChannel");
        digester.addSetProperties(
                pattern, 
                new String[] {"name", "class"}, 
                new String[] {"name", "className"});

        ArgumentRuleSet argumentRuleSet =
                new ArgumentRuleSet(pattern);
        argumentRuleSet.addRuleInstances(digester);        
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Jun-04	4619/5	ianw	VBM:2004060111 Fixed Javadoc issues

 09-Jun-04	4619/3	ianw	VBM:2004060111 Fixed up testcase for MPS configuration

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 ===========================================================================
*/
