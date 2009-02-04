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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/xml/MarinerRuleSet.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Mar-03    Geoff           VBM:2002112102 - Created; adds digester rules
 *                              for the mariner-config tag.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration.xml;

import our.apache.commons.digester.Digester;
import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.xml.digester.MarinerDigester;

/**
 * Adds digester rules for the mcs-config tag.
 */
public class MarinerRuleSet extends PrefixRuleSet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public MarinerRuleSet(String prefix) {
        this.prefix = prefix;
    }

    public void addRuleInstances(Digester digester) {

        String basePath = prefix + "mcs-config";

        digester.addObjectCreate(basePath,
                MarinerConfiguration.class);

        // Simple properties
        digester.addSetProperties(
                basePath + "/secondary-repository/inline-metadata",
                "enabled", "imdRepositoryEnabled");
        digester.addSetProperties(
                basePath + "/page-messages",
                "heading", "pageMessageHeading");
        digester.addSetProperties(
                basePath + "/log4j",
                "xml-configuration-file", "log4jConfigFile");
        digester.addSetProperties(
                basePath + "/chartimages",
                "base", "chartImagesBase");
        digester.addSetProperties(
                basePath + "/modesets",
                "base", "modeSetsBase");
        digester.addSetProperties(
                basePath + "/scripts",
                "base", "scriptsBase");
        digester.addSetProperties(
                basePath + "/style-sheets",
                "max-age", "stylesheetMaxAge");
        digester.addSetProperties(
                basePath + "/plugins",
                new String[] {"url-rewriter",
                              "page-url-rewriter",
                              "asset-url-rewriter",
                              "asset-transcoder"},
                new String[] {"urlRewriterPluginClass",
                              "pageURLRewriterPluginClass",
                              "assetURLRewriterPluginClass",
                              "assetTranscoderPluginClass"});
        digester.addSetProperties(
                basePath + "/page-packaging/mime-packaging",
                "enabled", "pagePackagingMimeEnabled");
        digester.addSetProperties(
                basePath + "/session-proxy/map-cookies",
                "enabled", "sessionProxyCookieMappingEnabled");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Jul-04	4702/4	matthew	VBM:2004061402 merge problems

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 18-Dec-03	2246/1	geoff	VBM:2003121715 debrand config file

 15-Oct-03	1517/6	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 ===========================================================================
*/
