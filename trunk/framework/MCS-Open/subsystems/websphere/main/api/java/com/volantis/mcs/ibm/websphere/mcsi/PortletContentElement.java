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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.DynamicProjectKey;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * The PortletContentElement MCSIElement
 */
public class PortletContentElement extends AbstractPortletContextChildElement {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(PortletContentElement.class);

    /**
     * The name of the element in an array for use in error messages.
     */
    private static final Object[] elementName = new Object[] {"portlet-content"};

    /**
     * The AssetConfiguration for the project.
     */
    private AssetsConfiguration assetsConfiguration;

    /**
     * The PolicySource for the Project.
     */
    private PolicySource policySource;

    /**
     * The Project.
     */
    private RuntimeProject project;

    /*
     * Parent PortletContextElement
     */
    private PortletContextElement parent;

    // Javadoc inherited from MCSIElement interface
    public int elementStart(MarinerRequestContext context,
                            PAPIAttributes mcsiAttributes)
            throws PAPIException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        parent = findParent(pageContext, elementName);

        AssetsConfiguration assetsConfiguration = parent.getAssetsConfiguration();
        String generatedResourceBaseDir = null;
        GeneratedResourcesConfiguration generatedResourcesConfiguration =
                parent.getGeneratedResourcesConfiguration();
        if (generatedResourcesConfiguration != null) {
            generatedResourceBaseDir =
                    generatedResourcesConfiguration.getBaseDir();
        }
        policySource = parent.getPolicySource();
        CacheControlConstraintsMap cacheControlConstraintsMap =
                pageContext.getVolantisBean().getProjectManager().getDefaultProject().getCacheControlConstraintsMap();
        DynamicProjectKey key = new DynamicProjectKey(policySource,
                assetsConfiguration, generatedResourceBaseDir, cacheControlConstraintsMap);

        project = pageContext.getVolantisBean().getDynamicProject(key);
        if (logger.isDebugEnabled()) {
            logger.debug("Setting project to : PolicySource [" + 
                ((policySource != null) ? policySource.toString() : "null") + 
                "," + 
                ((assetsConfiguration != null)
			 ? assetsConfiguration.getBaseUrl() : "null") + 
                "," +		      
                ((generatedResourcesConfiguration != null) 
			 ? generatedResourcesConfiguration.getBaseDir()
			 : "null") + 
                "]");
        }
        pageContext.pushMCSIElement(this);
        context.pushProject(project);
        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from MCSIElement interface
    public int elementEnd(MarinerRequestContext context,
                          PAPIAttributes mcsiAttributes)
            throws PAPIException {
        if (parent != null) {
            // only pop ourselves of the stack if there was a parent.  if there
            // wasn't we would not have pushed ourselves onto the stack.
            context.popProject(project);
            MarinerPageContext pageContext =
                    ContextInternals.getMarinerPageContext(context);
            pageContext.popMCSIElement();
        }

        return CONTINUE_PROCESSING;
    }


    // Javadoc inherited from MCSIElement interface
    public void elementReset(MarinerRequestContext context) {
        parent = null;
        assetsConfiguration = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-04	6460/1	doug	VBM:2004121008 Fixed NullPointerException in debug statement construction

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/5	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 14-Apr-04	3862/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 14-Apr-04	3847/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 19-Feb-04	2789/6	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/4	tony	VBM:2004012601 localisation services update

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 18-Feb-04	3090/1	ianw	VBM:2004021716 Added extra debugging and removed error masking for IBM projects problem

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
