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
import com.volantis.mcs.runtime.configuration.ArgumentConfiguration;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.Iterator;
import java.util.Map;

import org.xml.sax.SAXParseException;

/**
 * Test case for {@link ArgumentRuleSet}.
 */
public class ArgumentRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public ArgumentRuleSetTestCase(String s) {
        super(s);
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link ArgumentConfiguration} object.
     */
    public void doArgumentTest(ArgumentConfiguration values)
            throws Exception {
        String doc = "";
        doc += "  <mcs-plugins> \n";
        doc += "    <markup-plugin ";
        doc += "        name=\"myName\" ";
        doc += "        class=\"com.volantis.myClass\" ";
        doc += "        scope=\"application\"> \n";
        if (values != null) {
            doc += "      <initialize> \n";
            doc += "        <argument ";
            if (values.getName() != null) {
                doc += "name=\"" + values.getName() + "\" ";
            }
            if (values.getValue() != null) {
                doc += "value=\"" + values.getValue() + "\"";
            }
            doc += "/> \n";
            doc += "      </initialize> \n";
        }
        doc += "    </markup-plugin> \n";
        doc += "  </mcs-plugins> \n";

        TestXmlConfigurationBuilder configBuilder =
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        Iterator plugins = config.getMarkupPluginsListIterator();
        IntegrationPluginConfiguration mpc =
                (IntegrationPluginConfiguration) plugins.next();
        if (values != null) {
            Iterator args = mpc.getArguments().entrySet().iterator();
            if (args.hasNext()) {
                Map.Entry entry = (Map.Entry) args.next();
                assertEquals("Name should be the same as specified.",
                             values.getName(), entry.getKey());
                assertEquals("Value name should be the same as specified.",
                             values.getValue(), entry.getValue());
            } else {
                fail("Should have been an ArgumentConfiguration created.");
            }
        }
    }

    public void testNull() throws Exception {
        doArgumentTest(null);
    }

    public void testEmpty() throws Exception {
        ArgumentConfiguration values = new ArgumentConfiguration();
        values.setName("");
        values.setValue("");
        doArgumentTest(values);
    }

    public void testFull() throws Exception {
        ArgumentConfiguration values = new ArgumentConfiguration();
        values.setName("myArg");
        values.setValue("argValue");
        doArgumentTest(values);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 09-Jul-03	761/1	adrian	VBM:2003070801 Added configuration support for mcs plugins

 ===========================================================================
*/
