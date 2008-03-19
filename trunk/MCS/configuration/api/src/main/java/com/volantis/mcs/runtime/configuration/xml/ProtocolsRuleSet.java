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

import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;

import our.apache.commons.digester.Digester;

/**
 * Digester rule set for reading the protocols configuration   
 */
public class ProtocolsRuleSet extends PrefixRuleSet {

    /**
     * Create a new Protocols element rule set
     * @param the tag pattern prefix to use 
     */
    public ProtocolsRuleSet(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Add instances to map protocols elements to the ProtocolsConfiguration
     * class when configuration is read.
     * @param digester the digester for the mariner configuration file.
     */
    public void addRuleInstances(Digester digester) {
        // <protocols>
        final String pattern = prefix + "/protocols";
        final String wml = pattern + "/wml";

        digester.addObjectCreate(pattern, ProtocolsConfiguration.class);
        digester.addSetNext(pattern, "setProtocols");
        
        digester.addSetProperties(wml,
            new String[] {"preferred-output-format"},
            new String[] {"wmlPreferredOutputFormat"} );
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 25-Mar-04	3386/2	steve	VBM:2004030901 Supermerged and merged back with Proteus

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 22-Jan-04	2685/1	steve	VBM:2003121104 Allow WMLC and special character encoding to be turned off in Mariner Config

 ===========================================================================
*/
