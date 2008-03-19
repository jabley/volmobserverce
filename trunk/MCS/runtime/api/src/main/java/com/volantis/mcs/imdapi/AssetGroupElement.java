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
 * $Header: /src/voyager/com/volantis/mcs/imdapi/AssetGroupElement.java,v 1.3 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 *
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Mar-02    Mat             VBM:2002022009 - Created
 * 14-Jun-02    Mat             VBM:2002052001 - Made changes in elementStart
 *                              to take account of the new IMDAPI layout. 
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.imdapi;

import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.policies.BaseURLPolicyBuilder;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.repository.imd.IMDPolicyFetcher;
import com.volantis.mcs.runtime.repository.imd.IMDPolicyFetcher;

/**
 * The Asset Group element.
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public class AssetGroupElement extends AbstractIMDAPIElement {

    public AssetGroupElement() {
    }

    /**
     * Called while processing this IMDAPI markup element, this creates an
     * {@link AssetGroup} initialised with the supplied attributes and stores
     * it in the latest canvas associated with the current request.
     *
     * @param mrc   the current request context.
     * @param attrs attributes of the asset to be stored.
     * @throws RepositoryException
     */
    public void elementStart(MarinerRequestContext mrc,
                             AssetGroupAttributes attrs) 
     throws RepositoryException {
         
        MarinerPageContext pageContext
            = ContextInternals.getMarinerPageContext (mrc);
        
        pageContext.pushIMDAPIElement(this);

        // Create the base URL builder.
        PolicyFactory factory = PolicyFactory.getDefaultInstance();
        BaseURLPolicyBuilder builder = factory.createBaseURLPolicyBuilder();
        builder.setName(attrs.getName());

        String locationTypeAsString = attrs.getLocationType();
        BaseLocation baseLocation = getBaseLocation(locationTypeAsString);
        builder.setBaseLocation(baseLocation);
        builder.setBaseURL(attrs.getPrefixURL());

        // Add it to the map.
        IMDPolicyFetcher fetcher = pageContext.getPagePolicyFetcher();
        fetcher.addInlinePolicyBuilder(builder);
    }

    /**
     * Get the {@link BaseLocation} from the integer value of the location
     * type.
     *
     * @param locationTypeAsString The location type as a string.
     * @return The  {@link BaseLocation}.
     * @throws IllegalArgumentException If the location type was not valid.
     */
    private BaseLocation getBaseLocation(String locationTypeAsString) {
        BaseLocation baseLocation;
        int locationType = Integer.parseInt(locationTypeAsString);
        switch (locationType) {
            case AssetGroup.CONTEXT:
                baseLocation = BaseLocation.CONTEXT;
                break;

            case AssetGroup.HOST:
                baseLocation = BaseLocation.HOST;
                break;

            case AssetGroup.ON_DEVICE:
                baseLocation = BaseLocation.DEVICE;
                break;

            case AssetGroup.ON_SERVER:
                baseLocation = BaseLocation.CONTEXT;
                break;

            default:
                throw new IllegalArgumentException("Unknown location type " +
                        locationType);
        }
        return baseLocation;
    }

    /**
     * Called when this IMDAPI element's processing is complete.
     *
     * @param mrc the current request context.
     */
    public void elementEnd(MarinerRequestContext mrc,
                           ScriptAssetAttributes attrs) {
                               
        MarinerPageContext pageContext
            = ContextInternals.getMarinerPageContext (mrc);
        
        pageContext.popIMDAPIElement();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 20-May-05	8410/1	rgreenall	VBM:2005051615 Added Javadoc

 20-May-05	8326/7	rgreenall	VBM:2005051615 Improved Javadoc descriptions.

 20-May-05	8326/5	rgreenall	VBM:2005051615 Added Javadoc.

 19-May-05	8326/1	rgreenall	VBM:2005051615 Added Javadoc

 20-May-05	8365/1	rgreenall	VBM:2005051614 Added Javadoc

 19-May-05	8302/1	rgreenall	VBM:2005051614 Added Javadoc

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 05-Feb-04	2694/3	mat	VBM:2004011917 Rework for finding repositories

 26-Jan-04	2694/1	mat	VBM:2004011917 Improve the way repository connections are located

 ===========================================================================
*/
