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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/TestXmlConfigurationBuilder.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; a "testing" XML 
 *                              configuration builder that builds 
 *                              configurations from a XML document provided as 
 *                              a String, without the ?xml PI or Doctype. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationBuilder;
import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import org.apache.log4j.Category;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;

/**
 * A "testing" XML configuration builder that builds configurations from a XML 
 * document provided as a String, without the ?xml PI or !doctype.
 */ 
public class TestXmlConfigurationBuilder implements ConfigurationBuilder {

    /**
     * The logger
     */
    private static Category logger = Category.getInstance(
            "com.volantis.mcs.runtime.configuration.xml.TestXmlConfigurationBuilder");

    /**
     * The XML snippet to be turned into a complete document.
     */ 
    private String snippet;

    /**
     * The configuration builder used to turn the XML document into a
     * configuration.
     */ 
    private XMLConfigurationBuilder configBuilder;
    
    /**
     * Controls whether a default local-repository tag will be added in if 
     * none is found in the snippet.
     */ 
    private boolean addDefaultLocalRepository;
    
    /**
     * Controls whether a default devices tag will be added in if none is 
     * found in the snippet.
     */ 
    private boolean addDefaultDevices;
    
    /**
     * Controls whether a default projects tag will be added in if none is 
     * found in the snippet.
     */ 
    private boolean addDefaultProjects;

    /**
     * Construct an instance of this class using the XML snippet provided.
     * <p>
     * The XML snippet will be automagically transformed into a complete XML
     * document including the appropriate XML PI, root element and schema 
     * information and will have any mandatory elements which are not already 
     * present added by default.
     * 
     * @param snippet an xml snippet of the secion of the config file under
     *      test. 
     */ 
    public TestXmlConfigurationBuilder(String snippet) 
            throws ConfigurationException {

        // We add default elements by default. These may be turned off 
        // individually to allow testing of the mandatory elements. 
        addDefaultLocalRepository = true;
        addDefaultDevices = true;
        addDefaultProjects = true;
        
        this.snippet = snippet; 
        
//        // Find the voyager dir.
//        // NOTE: this is cut and pasted from ConfigFileBuilder, so...
//        // TODO: factor out a class which knows how to find the root directory
//        // of the change.
//        File workspaceDir = new File(System.getProperty("user.dir"));
//        // Hack to make it work if run in parent dir of change to support
//        // typical IDEA config.
//        File webapp = new File(workspaceDir, "webapp");
//        if (!webapp.exists()) {
//            workspaceDir = new File(workspaceDir, "voyager");
//        }
//        File schemaFile = new File(workspaceDir, ConfigFileBuilder.SCHEMA_FILE);
//
//        if (schemaFile.exists()) {
//            // Construct a URL to the schema file.
//            try {
//                xsdLocation = schemaFile.toURL().toExternalForm();
//            } catch (MalformedURLException e) {
//                throw new ConfigurationException("Unable to construct url for " +
//                        schemaFile, e);
//            }
//        } else {
//            xsdLocation = getClass().getClassLoader().getResource(
//                    ConfigFileBuilder.SCHEMA_RESOURCE).toExternalForm();
//        }

        this.configBuilder = new XMLConfigurationBuilder();
    }

    /**
     * Controls whether a default local-repository tag will be added in if
     * none is found in the snippet.
     */ 
    public void setAddDefaultLocalRepository(boolean addDefaultLocalRepository) {
        this.addDefaultLocalRepository = addDefaultLocalRepository;
    }

    /**
     * Controls whether a default devices tag will be added in if none is 
     * found in the snippet.
     */ 
    public void setAddDefaultDevices(boolean addDefaultDevices) {
        this.addDefaultDevices = addDefaultDevices;
    }

    /**
     * Controls whether a default projects tag will be added in if none is 
     * found in the snippet.
     */ 
    public void setAddDefaultProjects(boolean addDefaultProjects) {
        this.addDefaultProjects = addDefaultProjects;
    }

    public void addApplicationPluginRuleSet(PrefixRuleSet ruleSet) {
        configBuilder.addApplicationPluginRuleSet(ruleSet);
    }
    
    // Inherit javadoc.
    public MarinerConfiguration buildConfiguration() 
            throws ConfigurationException {

        // Constuct the full xml document to parse.
        
        // First, add on the XML PI and Schema information in the root element
        String doc = "";
        doc += 
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n";
        doc += 
                "<mcs-config " + 
                "  xmlns=\"" + XMLConfigurationBuilder.SCHEMA_NAMESPACE + "\"" +
                ">";
        
        doc += snippet;
        
        // Then, add default versions of mandatory elements. 
        if (addDefaultLocalRepository &&
                doc.indexOf("<local-repository") == -1) {
            doc +=
                    "<!-- automatically added for validity --> \n" +
                    "<local-repository/> \n";
        }
        if (addDefaultDevices &&
                doc.indexOf("<devices") == -1) {
            doc +=
                    "<!-- automatically added for validity --> \n" +
                    "<devices>\n" +
                    "  <standard>\n" +
                    "    <file-repository location=\"devices.mdpr\"/>\n" +
                    "  </standard>\n" +
                    "  <logging>\n" +
                    "    <log-file>/tmp/devices-log.log</log-file>\n" +
                    "    <e-mail>\n" +
                    "      <e-mail-sending>disable</e-mail-sending>\n" +
                    "    </e-mail>\n" +
                    "  </logging>\n" +
                    "</devices>\n";
        }
        if (addDefaultProjects && 
                doc.indexOf("<projects") == -1) {
            doc +=
                    "<!-- automatically added for validity --> \n" +
                    "<projects>\n" +
                    "  <default>\n" +
                    "    <xml-policies directory=\"test\"/>\n" +
                    "  </default>\n" +
                    "</projects>\n";
        }
        
        // And then, end the root element.
        doc += "</mcs-config>";
        
        // Once we have constructed the document, we attempt to build a
        // configuration from it.
        ByteArrayInputStream stream = new ByteArrayInputStream(doc.getBytes());
        InputSource source = new InputSource(stream);
        configBuilder.setInputSource(source);
        try {
            return configBuilder.buildConfiguration();
        } catch (ConfigurationException e) {
            if (logger.isDebugEnabled()) {
                logger.debug(doc);
            }
            throw e;
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 23-Nov-04	6241/1	philws	VBM:2004111209 Enable resource-based access to the MCS configuration schema in the ConfigFileBuilder

 28-Jun-04	4726/1	claire	VBM:2004060803 Refined implementation of internal style sheet caching and added extra style sheet tests

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 06-Jan-04	2271/4	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/2	geoff	VBM:2003121715 debrand config file

 ===========================================================================
*/
