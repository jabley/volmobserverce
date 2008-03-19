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
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDriverConfiguration;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLCatalog;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLEntry;
import com.volantis.xml.pipeline.sax.drivers.webservice.ClasspathResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.ServletContextResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.URIResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.URIResource;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLCatalog;
import com.volantis.xml.pipeline.sax.drivers.webservice.WSDLEntry;
import our.apache.commons.digester.Digester;

/**
 * Provide a rule set definition for reading in the WSDriver configuration
 * objects.
 *
 * An example of the web services configuration may look like: 
 *
 * <pipeline-configuration>
 *   <web-services-driver>
 *     <wsdl-catalog>
 *       <wsdl-entry uri="url">
 *         <class-resource path="class/path"/>
 *         <servlet-resource path="servlet/path"/>
 *         <uri-resource uri="uri/at/here"/>
 *       </wsdl-entry>
 *     </wsdl-catalog>
 *   </web-services-driver>
 * </pipeline-configuration>
 */
public class WSDriverConfigurationRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Initializes a <code>WSDriverConfigurationRuleSet</code> instance
     * @param prefix the prefix
     */
    public WSDriverConfigurationRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        // <web-services-driver>
        final String pattern = prefix + "/web-services-driver";
        digester.addObjectCreate(pattern, WSDriverConfiguration.class);
        digester.addSetNext(pattern, "setWsDriverConfiguration");

        // <web-services-driver><wsdl-catalog>
        final String wsdlCatalog = pattern + "/wsdl-catalog";
        digester.addObjectCreate(wsdlCatalog, WSDLCatalog.class);
        digester.addSetNext(wsdlCatalog, "setWsdlCatalog");

        // <web-services-driver><wsdl-catalog><wsdl-entry>
        final String wsdlEntry = wsdlCatalog + "/wsdl-entry";
        digester.addObjectCreate(wsdlEntry,
                                 WSDLEntry.class);
        digester.addSetNext(wsdlEntry, "addWSDLEntry");

        digester.addSetProperties(
            wsdlEntry,
            new String[] {"uri"},
            new String[] {"URI"}
        );

        // <web-services-driver><wsdl-catalog><wsdl-entry><class-resource>
        final String classResource = wsdlEntry + "/class-resource";
        digester.addObjectCreate(classResource, ClasspathResource.class);
        digester.addSetNext(classResource, "addWSDLResource");

        digester.addSetProperties(
            classResource,
            new String[] {"path"},
            new String[] {"path"}
        );
        // <web-services-driver><wsdl-catalog><wsdl-entry><servlet-resource>
        final String servletResource = wsdlEntry + "/servlet-resource";
        digester.addObjectCreate(servletResource, ServletContextResource.class);
        digester.addSetNext(servletResource, "addWSDLResource");

        digester.addSetProperties(
            servletResource,
            new String[] {"path"},
            new String[] {"path"}
        );
        // <web-services-driver><wsdl-catalog><wsdl-entry><uri-resource>
        final String uriResource = wsdlEntry + "/uri-resource";
        digester.addObjectCreate(uriResource, URIResource.class);
        digester.addSetNext(uriResource, "addWSDLResource");

        digester.addSetProperties(
            uriResource,
            new String[] {"uri"},
            new String[] {"URI"}
        );
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Apr-05	6798/6	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 30-Jun-03	492/3	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector - part 2

 25-Jun-03	492/1	byron	VBM:2003061808 Allow MCS to configure the pipeline WebService Connector

 ===========================================================================
*/
