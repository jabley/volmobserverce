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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/MpsPluginRuleSet.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 18-Mar-03    Geoff           VBM:2002112102 - Created; adds digester rules 
 *                              for the mps tag.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.MpsPluginConfiguration;


/**
 * Adds digester rules for the mps tag.  
 * 
 * @todo this should be moved into MPS repository once the plugin 
 * configuration API is made available to MPS. Note that this implies that the 
 * EnabledDigester needs to be made available to MPS as well.
 */ 
public class MpsPluginRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // Inherit javadoc.
    public void addRuleInstances(Digester digester) {
        String pattern = prefix + "/mps";
        digester.addObjectCreate(pattern, MpsPluginConfiguration.class);
        digester.addSetNext(pattern, "addApplicationPlugin");
        digester.addSetProperties(
                pattern, 
                new String[] {"internal-base-url", "message-recipient-info"}, 
                new String[] {"internalBaseUrl", "messageRecipientInfo"});

        MpsChannelRuleSet mpsChannelRuleSet =
                new MpsChannelRuleSet(pattern + "/channels");
        mpsChannelRuleSet.addRuleInstances(digester);   
    }     
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 ===========================================================================
*/
