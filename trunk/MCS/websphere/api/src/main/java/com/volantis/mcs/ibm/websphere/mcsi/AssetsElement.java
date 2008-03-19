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
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;
import com.volantis.mcs.runtime.configuration.project.AssetConfiguration;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The AssetsElement MCSIElement
 */
public class AssetsElement extends AbstractPortletContextChildElement {

    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    /**
     * The name of the element in an array for use in error messages.
     */
    private static final Object[] elementName = new Object[] {"assets"};

    /**
     * The AssetConfiguration.
     */
    private AssetsConfiguration assetsConfiguration;

    /*
     * Parent PortletContextElement
     */
    private PortletContextElement parent;

    // Javadoc inherited from MCSIElement interface
    public int elementStart(MarinerRequestContext context,
                            PAPIAttributes mcsiAttributes)
            throws PAPIException {

        AssetsAttributes attrs = (AssetsAttributes) mcsiAttributes;
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);

        assetsConfiguration = initialiseAssetConfiguration(attrs);

        parent = findParent(pageContext, elementName);

        pageContext.pushMCSIElement(this);

        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from MCSIElement interface
    public int elementEnd(MarinerRequestContext context,
                          PAPIAttributes mcsiAttributes)
            throws PAPIException {
        if (parent != null) {
            parent.setAssetsConfiguration(assetsConfiguration);
            // only pop ourselves of the stack if there was a parent.  if there
            // wasn't we would not have pushed ourselves onto the stack.
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

    /**
     * Creates a new AssetsConfiguration with the base URL from the supplied
     * asset attributes, containing assets whose prefix URL is '/'.
     * @param attrs the asset attributes
     * @return the new AssetsConfiguration
     */
    private AssetsConfiguration initialiseAssetConfiguration(
            AssetsAttributes attrs) {
        // Create a new AssetsConfiguration and add all the asset types to it.
        final AssetsConfiguration config = new AssetsConfiguration();
        config.setAudioAssets(new AssetConfiguration());
        config.setDynamicVisualAssets(new AssetConfiguration());
        config.setImageAssets(new AssetConfiguration());
        config.setScriptAssets(new AssetConfiguration());
        config.setTextAssets(new AssetConfiguration());

        // Copy the base URL.
        config.setBaseUrl(attrs.getBaseUrl());

        // Create a List of the asset configurations to simplify processing.
        final List assetConfigsList = new ArrayList(5);
        assetConfigsList.add(config.getAudioAssets());
        assetConfigsList.add(config.getDynamicVisualAssets());
        assetConfigsList.add(config.getImageAssets());
        assetConfigsList.add(config.getScriptAssets());
        assetConfigsList.add(config.getTextAssets());

        // Process each asset configuration, inserting a leading slash if
        // necessary.
        for (Iterator it = assetConfigsList.iterator(); it.hasNext();) {
            AssetConfiguration assetConfig = (AssetConfiguration) it.next();
            assetConfig.setPrefixUrl("/");
        }

        return config;
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8312/4	pcameron	VBM:2005051617 MCSI projects ensure asset value has leading slash

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/1	tony	VBM:2004012601 update localisation services

 06-Feb-04	2828/3	ianw	VBM:2004011922 corrected logging issues

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
