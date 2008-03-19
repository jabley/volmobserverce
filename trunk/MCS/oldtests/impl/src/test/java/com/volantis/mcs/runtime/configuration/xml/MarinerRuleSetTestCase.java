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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/configuration/xml/MarinerRuleSetTestCase.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; test case for 
 *                              MarinerRuleSet. 
 * 03-Jun-03    Allan           VBM:2003060301 - TestCaseAbstract moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.ConfigurationException;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for {@link MarinerRuleSet}. 
 */ 
public class MarinerRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public MarinerRuleSetTestCase(String s) {
        super(s);
    }
    
    public void testNull() throws ConfigurationException {
        checkMariner(null);
    }

    /**
     * Test the minimal valid set of values.
     * 
     * @throws ConfigurationException
     */ 
    public void testEmpty() throws ConfigurationException {
        MarinerValue value = new MarinerValue();
        // mandatory
        checkMariner(value);
    }
    
    /**
     * Test all the values that can be done easily in one go.
     * 
     * @throws ConfigurationException
     */ 
    public void testShared() throws ConfigurationException {
        MarinerValue value = new MarinerValue();
        // mandatory
        // optional
        value.assetUrlRewriterPluginClass = "aurpc";
        value.assetTranscoderPluginClass = "atpc";
        value.chartImagesBase = "cib";
        value.imdRepositoryEnabled = Boolean.TRUE;
        value.modesetsBase = "mb";
        value.pageMessageHeading = "pmh";
        value.scriptsBase = "sb";
        value.stylesheetMaxAge = new Integer(99);
        value.urlRewriterPluginClass = "urpc";
        value.pagePackagingMimeEnabled = Boolean.TRUE;
        value.sessionProxyCookieMappingEnabled = Boolean.TRUE;
        checkMariner(value);
    }

    public void testURLRewriters() throws Exception {
        MarinerValue value = new MarinerValue();
        value.pageURLRewriterPluginClass = "purpc";
        checkMariner(value);
    }

    /**
     * Create a subset of the mcs-config XML document from the values 
     * supplied, parse it into a {@link MarinerConfiguration} object, and 
     * ensure that the values supplied are match those found.
     * 
     * @param value the example values to use.
     * @throws ConfigurationException if the was a problem.
     */ 
    private void checkMariner(MarinerValue value) 
            throws ConfigurationException {
        String doc = ""; 
        if (value != null) {
            if (value.chartImagesBase != null) {
                doc += "  <chartimages base=\"" + value.chartImagesBase + 
                        "\"/> \n";
            }
            if (value.modesetsBase != null) {
                doc += "  <modesets base=\"" + value.modesetsBase + "\"/> \n";
            }
            if (value.pageMessageHeading != null) {
                doc += "  <page-messages heading=\"" + 
                        value.pageMessageHeading + "\"/> \n";
            }
            doc += "  <plugins ";
            if (value.urlRewriterPluginClass != null) {
                doc +=      "url-rewriter=\"" + value.urlRewriterPluginClass + 
                        "\" ";
            }
            if (value.pageURLRewriterPluginClass != null) {
                doc +=      "page-url-rewriter=\"" +
                        value.pageURLRewriterPluginClass +
                        "\" ";
            }
            if (value.assetUrlRewriterPluginClass != null) {
                doc +=      "asset-url-rewriter=\"" + 
                        value.assetUrlRewriterPluginClass + "\" ";
            }
            if (value.assetTranscoderPluginClass != null) {
                doc += "asset-transcoder=\"" +
                    value.assetTranscoderPluginClass + "\"";
            }
            doc += "/> \n";
            if (value.scriptsBase != null) {
                doc += "  <scripts base=\"" + value.scriptsBase + "\"/> \n";
            }
            if (value.imdRepositoryEnabled != null) {
                doc += "  <secondary-repository> \n"; 
                doc += "    <inline-metadata enabled=\"" + 
                        value.imdRepositoryEnabled + "\"/> \n";
                doc += "  </secondary-repository> \n"; 
            }
            if (value.stylesheetMaxAge != null) {
                doc += "  <style-sheets "; 
                doc +=      "max-age=\"" + value.stylesheetMaxAge + "\"";
                doc +=      "/> \n";
            }
            if (value.pagePackagingMimeEnabled != null) {
                doc += "  <page-packaging> \n"; 
                doc += "    <mime-packaging enabled=\"" + 
                        value.pagePackagingMimeEnabled + "\"/> \n";
                doc += "  </page-packaging> \n"; 
            }
            if (value.sessionProxyCookieMappingEnabled != null) {
                doc += "  <session-proxy> \n"; 
                doc += "    <map-cookies enabled=\"" + 
                        value.sessionProxyCookieMappingEnabled + "\"/> \n";
                doc += "  </session-proxy> \n"; 
            }
        } else {
            doc += "  <local-repository/> \n" +
                "<devices>\n" +
                "  <standard>\n" +
                "    <file-repository location=\"devices.mdpr\"/>\n" +
                "  </standard>\n" +
                "  <logging>\n" +
                "     <log-file>/tmp/devices-log.log</log-file>\n" +
                "     <e-mail>\n" +
                "       <e-mail-sending>disable</e-mail-sending>\n" +
                "     </e-mail>\n" +
                "  </logging>\n" +
                "</devices>\n" +
                "<projects>\n" +
                "  <default>\n" +
                "    <xml-policies directory=\"test\"/>\n" +
                "  </default>\n" +
                "</projects>\n"; 
        }

        TestXmlConfigurationBuilder configBuilder = 
                new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration mariner = configBuilder.buildConfiguration();
        assertNotNull(mariner);
        
        if (value == null) {
            value = new MarinerValue();
        }
        assertEquals("assetUrlRewriterPluginClass", 
                value.assetUrlRewriterPluginClass, 
                mariner.getAssetURLRewriterPluginClass());
        assertEquals("assetTranscoderPluginClass",
                     value.assetTranscoderPluginClass,
                     mariner.getAssetTranscoderPluginClass());
        assertEquals("chartImagesBase", value.chartImagesBase,
                mariner.getChartImagesBase());
        assertEquals("imdRepositoryEnabled", value.imdRepositoryEnabled,
                mariner.getImdRepositoryEnabled());
        assertEquals("modesetsBase", value.modesetsBase, 
                mariner.getModeSetsBase());
        assertEquals("pageMessageHeading", value.pageMessageHeading, 
                mariner.getPageMessageHeading());
        assertEquals("scriptsBase", value.scriptsBase, 
                mariner.getScriptsBase());
        assertEquals("styleSheet.maxAge", value.stylesheetMaxAge, 
                mariner.getStylesheetMaxAge());
        assertEquals("urlRewriterPluginClass", value.urlRewriterPluginClass, 
                mariner.getUrlRewriterPluginClass());
        assertEquals("pageURLRewriterPluginClass", value.pageURLRewriterPluginClass,
                mariner.getPageURLRewriterPluginClass());
        assertEquals("pagePackagingMime", value.pagePackagingMimeEnabled, 
                mariner.getPagePackagingMimeEnabled());
        assertEquals("cookieMapping", value.sessionProxyCookieMappingEnabled, 
                mariner.getSessionProxyCookieMappingEnabled());
    }
    
    /**
     * A private Value Object class for holding mariner config values.
     */ 
    private static class MarinerValue {
        String assetUrlRewriterPluginClass;
        String assetTranscoderPluginClass;
        String chartImagesBase;
        Boolean imdRepositoryEnabled;
        String modesetsBase;
        String pageMessageHeading;
        String scriptsBase;
        Integer stylesheetMaxAge;
        String urlRewriterPluginClass;
        String pageURLRewriterPluginClass;
        Boolean pagePackagingMimeEnabled;
        Boolean sessionProxyCookieMappingEnabled;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	7890/2	pduffin	VBM:2005042705 Committing results of supermerge

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 26-Apr-05	7759/1	pcameron	VBM:2005040505 Logging initialisation changed

 21-Apr-05	7665/1	pcameron	VBM:2005040505 Logging initialisation changed

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 09-Mar-04	2867/1	ianw	VBM:2004012603 Rationalised data source configuration and refactored code to cope with validated config schema

 06-Jan-04	2271/2	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 15-Oct-03	1517/6	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 ===========================================================================
*/
