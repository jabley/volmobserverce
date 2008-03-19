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
import com.volantis.mcs.project.PolicySource;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;
import com.volantis.mcs.runtime.configuration.project.GeneratedResourcesConfiguration;
/**
 * The PortletContextElement MCSIElement
 */
public class PortletContextElement extends MCSIElement {

    /**
     * The AssetConfiguration for the project..
     */ 
    private AssetsConfiguration assetsConfiguration;

    /**
     * The "fake" configuration object created for the generated-resources
     * element.
     */
    private GeneratedResourcesConfiguration generatedResourcesConfiguration;

    /**
     * The AssetConfiguration for the project..
     */ 
    private PolicySource policySource;


    // Javadoc inherited from MCSIElement interface
    public int elementStart(MarinerRequestContext context,
                            PAPIAttributes mcsiAttributes)
            throws PAPIException {
        // Push this element onto the MCSI stack.
        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(context);
        pageContext.pushMCSIElement(this);
        
        return PROCESS_ELEMENT_BODY;
    }

    // Javadoc inherited from MCSIElement interface
    public int elementEnd(MarinerRequestContext context,
                          PAPIAttributes mcsiAttributes)
            throws PAPIException {
        MarinerPageContext pageContext = 
                ContextInternals.getMarinerPageContext(context);
        
        // pop this element off the MCSI stack.
        pageContext.popMCSIElement();
        
        return CONTINUE_PROCESSING;
    }
    
        
    // Javadoc inherited from MCSIElement interface
    public void elementReset(MarinerRequestContext context) {
        assetsConfiguration = null;
        policySource = null;
    }

    /**
     * Set the AssetConfiguration. This method will typically be called by
     * AssetsElement.
     * @param assetsConfiguration the assetConfiguration for the project.
     */ 
    void setAssetsConfiguration(AssetsConfiguration assetsConfiguration) {
        this.assetsConfiguration = assetsConfiguration;
    }

    /**
     * Set the GeneratedResourcesConfiguration.
     * <p>
     * This method will typically be called by GeneratedResourcesElement.
     *
     * @param generatedResourcesConfiguration the
     *      GeneratedResourcesConfiguration for the project.
     */
    public void setGeneratedResourcesConfiguration(
            GeneratedResourcesConfiguration generatedResourcesConfiguration) {
        this.generatedResourcesConfiguration = generatedResourcesConfiguration;
    }

    /**
     * Set the PolicySource. This method will typically be called by
     * JdbcPoliciesElement or XmlPoliciessElement.
     * @param policySource the PolicySource for the project.
     */ 
    void setPolicySource(PolicySource policySource) {
        this.policySource = policySource;
    }

    /**
     * Get the AssetConfiguration. This method will typically be called by
     * AssetsElement.
     * @return AssetConfiguration The AssetConfiguration for the project.
     */
    AssetsConfiguration getAssetsConfiguration() {
        return assetsConfiguration;
    }

    /**
     * Get the GeneratedResourcesConfiguration.
     * <p>
     * This method will typically be called by GeneratedResourcesElement.
     *
     * @return AssetConfiguration The AssetConfiguration for the project.
     */
    public GeneratedResourcesConfiguration getGeneratedResourcesConfiguration() {
        return generatedResourcesConfiguration;
    }

    /**
     * Get the PolicySource. This method will typically be called by
     * JdbcPoliciesElement or XmlPoliciessElement.
     * @return PolicySource The PolicySource for the project.
     */
    PolicySource getPolicySource() {
        return policySource;
    }

}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 06-Feb-04	2828/3	ianw	VBM:2004011922 corrected logging issues

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
