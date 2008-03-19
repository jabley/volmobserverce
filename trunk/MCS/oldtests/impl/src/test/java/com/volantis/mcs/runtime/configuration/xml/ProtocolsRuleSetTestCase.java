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

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.configuration.WMLOutputPreference;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link ProtocolsRuleSet}. 
 */ 
public class ProtocolsRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public ProtocolsRuleSetTestCase(String s) {
        super(s);
    }
    
    public void testNull() throws ConfigurationException {
        checkProtocols(null);
    }
    
    public void testEmpty() throws ConfigurationException {
        ProtocolsValue value = new ProtocolsValue();
        // default values from the DTD
        value.output = WMLOutputPreference.WMLC;
        checkProtocols(value);
    }

    public void testFull() throws ConfigurationException {
        ProtocolsValue value = new ProtocolsValue();

        value.output = WMLOutputPreference.WML;
        checkProtocols(value);
        value.output = WMLOutputPreference.WMLC;
        checkProtocols(value);
    }
    
    /**
     * Create a subset of the mariner-config XML document from the values 
     * supplied, parse it into a {@link ProtocolsConfiguration} object, 
     * and ensure that the values supplied match those found.
     * 
     * @param value
     * @throws ConfigurationException
     */ 
    private void checkProtocols(ProtocolsValue value) 
        throws ConfigurationException {
        String doc = "  <local-repository/> \n"
            + "<devices>\n"
            + "  <standard>\n"
            + "    <file-repository location=\"devices.mdpr\"/>\n"
            + "  </standard>\n"
            + "  <logging>\n"
            + "     <log-file>/tmp/devices-log.log</log-file>\n"
            + "     <e-mail>\n"
            + "       <e-mail-sending>disable</e-mail-sending>\n"
            + "     </e-mail>\n"
            + "  </logging>\n"
            + "</devices>\n"
            + "<projects>\n"
            + "  <default>\n"
            + "    <xml-policies directory=\"test\"/>\n"
            + "  </default>\n"
            + "</projects>\n";       

        if (value != null) {
            doc += "<protocols><wml ";
            if (value.output != null) {
                doc += "preferred-output-format=\"" + value.output.toString() 
                        + "\" ";
            }
            doc += "/></protocols>";
        }
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        
        ProtocolsConfiguration conf = config.getProtocols();
        if (value != null) {
            assertNotNull("ProtocolsConfiguration", conf);
            assertEquals(value.output, conf.getPreferredOutputFormat());
        } else {
            assertNull("ProtocolsConfiguration", conf);
        }
    }

    /**
     * A private Value Object class for holding protocols values.
     */ 
    private static class ProtocolsValue {
        WMLOutputPreference output;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3386/2	steve	VBM:2004030901 Supermerged and merged back with Proteus

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
