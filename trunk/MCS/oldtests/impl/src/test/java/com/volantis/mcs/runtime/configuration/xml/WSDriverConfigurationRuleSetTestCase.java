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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLEntry;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.ClasspathResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.ServletContextResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.ServletContextResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.URIResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.URIResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLEntry;

import junitx.util.PrivateAccessor;

import java.util.List;

/**
 * Test the reading of WSDriver Configuration values.
 */
public class WSDriverConfigurationRuleSetTestCase extends TestCaseAbstract {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    public WSDriverConfigurationRuleSetTestCase(String s) {
        super(s);
    }

    /**
     * Test the reading of the configuration.
     */
    public void testConfigurationReading() throws Exception {
        String wsdlURI = "url";
        String classPath = "class/path";
        String servletPath = "servlet/path";
        String uriResource = "uri/at/there";

        String input =
            "<pipeline-configuration>\n" +
            "  <web-services-driver>\n" +
            "    <wsdl-catalog>\n" +
            "      <wsdl-entry uri=\"" + wsdlURI + "\">\n" +
            "        <class-resource path=\"" + classPath + "\"/>\n" +
            "        <servlet-resource path=\"" + servletPath + "\"/>\n" +
            "        <uri-resource uri=\"" + uriResource + "\"/>\n" +
            "      </wsdl-entry>\n" +
            "    </wsdl-catalog>\n" +
            "  </web-services-driver>\n" +
            "</pipeline-configuration>\n";

        TestXmlConfigurationBuilder configBuilder =
            new TestXmlConfigurationBuilder(input);

        MarinerConfiguration actualCfg = configBuilder.buildConfiguration();
        assertNotNull("Main configuration", actualCfg);

        final WSDriverConfiguration config =
                    actualCfg.getPipelineConfiguration().
                        getWsDriverConfiguration();
        assertNotNull("WSDLCatalog", config.getWSDLCatalog());

        WSDLEntry entry = config.getWSDLCatalog().retrieveWSDLEntry(wsdlURI);
        assertNotNull("WSDLEntry", entry);

        assertEquals("URI should match", wsdlURI, entry.getURI());

        List list = (List) PrivateAccessor.getField(entry,
                                                    "wsdlResources");
        assertNotNull("List should not be null.", list);

        assertEquals("List size should match", 3, list.size());

        WSDLResource wsdlResource;
        wsdlResource = (WSDLResource) list.get(0);
        assertTrue("Type should match",
                   wsdlResource instanceof ClasspathResource);
        assertEquals("Value should match",
                     classPath,
                     ((ClasspathResource) wsdlResource).getPath());

        wsdlResource = (WSDLResource) list.get(1);
        assertTrue("Type should match",
                   wsdlResource instanceof ServletContextResource);
        assertEquals("Value should match",
                     servletPath,
                     ((ServletContextResource) wsdlResource).getPath());

        wsdlResource = (WSDLResource) list.get(2);
        assertTrue("Type should match",
                   wsdlResource instanceof URIResource);
        assertEquals("Value should match",
                     uriResource,
                     ((URIResource) wsdlResource).getURI());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/2	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 30-Jun-03	492/3	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector - part 2

 25-Jun-03	492/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 ===========================================================================
*/
