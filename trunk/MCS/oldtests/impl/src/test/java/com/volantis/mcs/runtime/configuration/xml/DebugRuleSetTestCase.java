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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/DebugRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              DebugRuleSet. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.DebugConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link DebugRuleSet}. 
 */ 
public class DebugRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public DebugRuleSetTestCase(String s) {
        super(s);
    }
    
    public void testNull() throws ConfigurationException {
        checkDebug(null);
    }
    
    public void testEmpty() throws ConfigurationException {
        DebugValue value = new DebugValue();
        // default values
        value.comments = Boolean.TRUE;
        value.logPageOutput = Boolean.TRUE;
        checkDebug(value);
    }

    public void testFull() throws ConfigurationException {
        DebugValue value = new DebugValue();
        // default values
        value.comments = Boolean.TRUE;
        value.logPageOutput = Boolean.TRUE;
        // optional
        value.comments = Boolean.FALSE;
        value.logPageOutput = Boolean.FALSE;
        checkDebug(value);
    }
    
    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link DebugConfiguration} object, 
     * and ensure that the values supplied are match those found.
     * 
     * @param value
     * @throws ConfigurationException
     */ 
    private void checkDebug(DebugValue value) throws ConfigurationException {
        String doc = ""; 
        if (value != null) {
            doc += "  <debug \n";
            if (value.comments != null) {
                doc += "    comments=\"" + value.comments + "\" \n";
            }
            if (value.logPageOutput != null) {
                doc += "    logPageOutput=\"" + value.logPageOutput  + "\" \n";
            }
            doc += "  /> \n";
        }
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull("marinerConfiguration", config);
        
        DebugConfiguration debug = config.getDebug();
        if (value != null) {
            assertNotNull("debugConfiguration", debug);
            assertEquals(value.comments, debug.getComments());
            assertEquals(value.logPageOutput, debug.getLogPageOutput());
        } else {
            assertNull("debugConfiguration", debug);
        }
    }

    /**
     * A private Value Object class for holding debug values.
     */ 
    private static class DebugValue {
        Boolean comments;
        Boolean logPageOutput;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
