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
 * $Header: /src/voyager/com/volantis/testtools/config/ConfigValue.java,v 1.3 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Mar-03    Geoff           VBM:2003010904 - Created; a simple ValueObject
 *                              to contain the data require to configure 
 *                              Mariner.
 * 10-Mar-03    Steve           VBM:2003021101 - Added Boolean value for 
 *                              mapping cookies
 * 11-Mar-03    Geoff           VBM:2002112102 - Refactor value property names
 *                              for consistency.
 * 30-Apr-03    Chris W         VBM:2003041503 - Add JSP properties to handle
 *                              character references.
 * ----------------------------------------------------------------------------
 */
package com.volantis.testtools.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple ValueObject which contains the data needed to configure Mariner.
 * <p>
 * This is designed to not have any dependencies on how the data is used, e.g.
 * currently used to create the config XML file, in future will also be used
 * to create the new config objects as well.
 * <p>
 * One interesting thing about this class is the lack of accessors and mutators
 * for the majority of the data, this is done for readability and reflects the
 * "data only" purpose of this class. Consider this a pedant trap :-).  
 */ 
public class ConfigValue {
    // Page Message Heading
    public String pageMessageHeading;
    // Base directories
    public String chartImagesBase;
    public String modesetsBase;
    public String scriptsBase;
    // Local repository
    public String repositoryType;
    public String repositoryUser;
    public String repositoryPassword;
    public String repositoryVendor;
    public String repositorySource;
    public String repositoryHost;
    public Integer repositoryPort;
    public Integer repositoryDbPoolMax;
    public Boolean repositoryKeepConnectionsAlive;
    public Integer repositoryConnectionPollInterval;

    // Device related
    public String standardFileDeviceRepositoryLocation;
    public String standardJDBCDeviceRepositoryProject;

    // Projects
    // Currently we have only implemented the policies tag for default projects
    // This will need to be extended as we add the rest of multiple project 
    // support.
    public ConfigProjectPoliciesValue defaultProjectPolicies;
    // Default asset url prefixes
    public String audioUrlPrefix;
    public String dynvisUrlPrefix;
    public String imageUrlPrefix;
    public String scriptUrlPrefix;
    public String textUrlPrefix;
    // App server related
    public String internalUrl;
    public String baseUrl;
    public String pageBase;

    public String styleBaseUrl;
    // Debug related
    public Boolean debugComments;
    public Boolean debugLogPageOutput;
    // Log4j
    public String log4jXmlConfigFile;
    // Plugins
    public String assetUrlRewriterPluginClass;
    public String assetTranscoderPluginClass;
    
    // Page Packaging
    public Boolean pagePackagingMimeEnabled;
    // JSP support
    public Boolean jspSupportRequired;
    public Boolean jspWriteDirect;
    public Boolean jspResolveCharacterReferences;
    public Boolean jspEvaluatePageAfterCanvas;
    // Proxy Server Cookie Mapping
    public Boolean sessionProxyCookieMappingEnabled;
    // Policy Caches
    public Map policyCaches = new HashMap();
    // Remote Repositories
    public Integer remoteRepositoryTimeout;
    public ConfigValueRemotePolicy remotePolicyCaches; // schema ordering.
    public List remotePolicyQuotaList; // schema ordering.
    // Protocols
    public String wmlPreferredOutputFormat;
    
    // MCS Plugins
    //   Markup plugins
    public List markupPlugins;
    
    // List of plugin ConfigValues used to build configs for application plugins.
    public List pluginConfigValues = new ArrayList();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Mar-04	3386/4	steve	VBM:2004030901 Supermerged and merged back with Proteus

 25-Mar-04	3386/1	steve	VBM:2004030901 Supermerged and merged back with Proteus

 11-Mar-04	3370/1	steve	VBM:2004030901 Null exception if protocols element is missing in config

 24-Mar-04	3482/1	geoff	VBM:2004030205 The runtime needs to support the jdbc-repository device repository configuration

 03-Mar-04	3277/1	claire	VBM:2004021606 Added devices to configuration and cli options

 30-Jan-04	2807/1	geoff	VBM:2003121709 Import/Export: JDBC Accessors: Add support for the default jdbc project

 29-Jan-04	2749/1	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 06-Jan-04	2271/1	geoff	VBM:2003121716 Import/Export: Schemify configuration file: Enable schema validation

 31-Oct-03	1593/1	mat	VBM:2003101502 Adding pluginconfigvalue

 23-Oct-03	1585/3	mat	VBM:2003101502 Add plugin config builders to ConfigFileBuilder

 15-Oct-03	1517/7	pcameron	VBM:2003100706 Further changes associated with license removal

 13-Oct-03	1517/1	pcameron	VBM:2003100706 Removed all traces of licensing from MCS

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 10-Jul-03	761/1	adrian	VBM:2003070801 Added integration test to Volantis testcase to test markup plugin configuration

 30-Jun-03	623/1	mat	VBM:2003063005 Add app server config values

 25-Jun-03	544/2	geoff	VBM:2003061007 Allow JSPs to create binary output

 25-Jun-03	540/1	geoff	VBM:2003061709 remove mariner config debug enabled attribute

 ===========================================================================
*/
