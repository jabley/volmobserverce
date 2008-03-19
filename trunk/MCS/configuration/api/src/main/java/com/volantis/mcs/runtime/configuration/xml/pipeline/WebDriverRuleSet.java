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

import our.apache.commons.digester.Digester;

import com.volantis.mcs.runtime.configuration.xml.PrefixRuleSet;
import com.volantis.xml.pipeline.sax.drivers.web.ScriptFilter;
import com.volantis.xml.pipeline.sax.drivers.web.SimpleScriptModule;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfigurationImpl;
import com.volantis.xml.pipeline.sax.proxy.DefaultProxy;

/**
 * <mcs-config>
 *   :
 *  <pipeline-configuration>
 *   <web-driver>
 *     <proxy id='unique_proxy_identity'
 *            host='proxy_host'
 *            port='proxy_port'/>
 *     <proxy id='another_unique_proxy_identity'
 *           host='proxy_host'
 *           port='proxy_port'/>
 *
 *     <script>
 *        <module id='id_of_this_module'>
 *            <filter content-type="content_type_of_parent_document"
 *                class='class_implementing_script_filter'/>
 *            <filter content-type='content_type_of_parent_document'>
 *                class='another_class_implementing_script_filter'/>
 *        </module>
 *        <module id='id_of_another_module'>
 *            <filter content-type="content_type_of_parent_document">
 *                class='class_implementing_scripconfigt_filter'/>
 *            <filter content-type='content_type_of_parent_document'>
 *                class='another_class_implementing_script_filter'/>
 *        </module>
 *     </script>
 *   </web-driver>
 *  </pipeline-configuration>
 *   :
 * </mcs-config>
 *
 */
public class WebDriverRuleSet extends PrefixRuleSet {
    /**
     *  Volantis copyright mark.
     */
    private static String mark
        = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Constructor for intializing <code>WebDriverRuleSet</code> instances
     * @param prefix the prefix
     */
    public WebDriverRuleSet(String prefix) {
        this.prefix = prefix;
    }

    // javadoc inherited
    public void addRuleInstances(Digester digester) {
        final String pattern = prefix + "/web-driver";
        digester.addObjectCreate(pattern, WebDriverConfigurationImpl.class);
        digester.addSetNext(pattern, "setWebDriverConfiguration");
        digester.addSetProperties(pattern, "connection-timeout", "timeout");

        String newPattern = pattern + "/proxy";
        digester.addObjectCreate(newPattern, DefaultProxy.class);
        digester.addSetNext(newPattern, "putProxy");
        digester.addSetProperties(
            newPattern,
            new String[] {"id", "host", "port"},
            new String[] {"id", "host", "port"}
        );

        newPattern = pattern + "/script/module";
        digester.addObjectCreate(newPattern, SimpleScriptModule.class);
        digester.addSetNext(newPattern, "putScriptModule");
        digester.addSetProperties(newPattern, "id", "id");

        newPattern = newPattern + "/filter";
        digester.addObjectCreate(newPattern, ScriptFilter.class);
        digester.addSetNext(newPattern, "putScriptFilter");
        digester.addSetProperties(
            newPattern,
            new String[]{"content-type", "class"},
            new String[]{"contentType", "scriptClass"}
        );
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9649/2	matthew	VBM:2005092809 Allow proxy configuration via system properties

 01-Jun-05	8627/1	tom	VBM:2005052502 Added connection-timeout to mcs-properties.xml

 01-Apr-05	6798/3	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Jul-04	4707/1	doug	VBM:2004061405 Refactored WEB Driver so that all requests are performed via a generic interface allowing different HTTP frameworks to be plugged in

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 06-Aug-03	921/3	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy - rework issue

 05-Aug-03	921/1	byron	VBM:2003080102 Provide MCS configuration reading for script/proxy

 ===========================================================================
*/
