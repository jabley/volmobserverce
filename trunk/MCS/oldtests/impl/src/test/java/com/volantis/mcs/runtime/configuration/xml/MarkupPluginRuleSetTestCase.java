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

import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;

import org.xml.sax.SAXParseException;
import org.xml.sax.SAXException;

/**
 * Test case for {@link com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration}.
 */ 
public class MarkupPluginRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public MarkupPluginRuleSetTestCase(String s) {
        super(s);
    }    
   
    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link com.volantis.mcs.runtime.configuration.MarkupPluginConfiguration} object.
     */ 
    public void doMarkupPluginTest(MarkupPluginConfiguration values)
            throws Exception {
        StringBuffer doc = new StringBuffer();
        doc.append("  <mcs-plugins> \n");
        if (values != null) {
            doc.append("    <markup-plugin ");
            if (values.getName() != null) {
                doc.append("        name=\"" + values.getName() + "\" ");
            }
            if (values.getClassName() != null) {
                doc.append("        class=\"" + values.getClassName() + "\" ");
            }
            if (values.getScope() != null) {
                doc.append("        scope=\"" + values.getScope() + "\"");
            }
            doc.append("> \n");
            doc.append("      <initialize> \n");
            doc.append("        <argument name=\"x\" value=\"y\"/> \n");
            doc.append("      </initialize> \n");
            doc.append("    </markup-plugin> \n");
        }
        doc.append("  </mcs-plugins> \n");
        
        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc.toString());
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        Iterator plugins = config.getMarkupPluginsListIterator();
        if (plugins.hasNext()) {
            MarkupPluginConfiguration mpc =
                    (MarkupPluginConfiguration) plugins.next();
            assertEquals("Name should be the same as specified.", 
                    values.getName(), mpc.getName());
            assertEquals("Class name should be the same as specified.",
                    values.getClassName(), mpc.getClassName());
            if (values.getScope() != null) {
                assertEquals("Scope should be the same as specified.",
                        values.getScope(), mpc.getScope());
            } else {
                assertEquals("Scope should be the default - canvas.",
                        "canvas", mpc.getScope());    
            }
        } else {
            if (values != null) {
                fail("Should have been a MarkupPluginConfiguration created.");
            }
        }
    }

    public void testNull() throws Exception {
        doMarkupPluginTest(null);
    }

    public void testEmpty() throws Exception {
        try {
            MarkupPluginConfiguration values = new MarkupPluginConfiguration();
            values.setName("");
            values.setClassName("x");
            doMarkupPluginTest(values);

            fail("Expected exception to be thrown");
        } catch (ConfigurationException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof SAXParseException) {
                SAXParseException pe = (SAXParseException) cause;
                assertEquals("Exception message not correct",
                             pe.getMessage(),
                             "cvc-datatype-valid.1.2.1: '' is not a valid value for 'NCName'.");
            } else {
                fail("Expected SAXParseException as root cause");
            }
        }
    }

    public void testFull() throws Exception {
        MarkupPluginConfiguration values = new MarkupPluginConfiguration();
        values.setClassName("com.volantis.MyClass");
        values.setName("myPlugin");
        values.setScope("application");
        doMarkupPluginTest(values);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Jan-05	6712/3	pduffin	VBM:2005011713 Updated build scripts to pick up the changes to the configuration schema

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 24-Jul-03	853/1	adrian	VBM:2003072307 added default value of canvas for plugin scope

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 ===========================================================================
*/
