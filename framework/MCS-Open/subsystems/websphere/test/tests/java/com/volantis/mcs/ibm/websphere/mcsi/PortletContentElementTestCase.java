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

import com.volantis.mcs.project.PolicySourceMock;
import com.volantis.mcs.runtime.DynamicProjectKey;
import com.volantis.mcs.runtime.RuntimeProjectMock;
import com.volantis.mcs.runtime.policies.cache.CacheControlConstraintsMap;
import com.volantis.mcs.runtime.policies.cache.SeparateCacheControlConstraintsMap;
import com.volantis.mcs.runtime.project.ProjectManager;
import com.volantis.mcs.runtime.project.ProjectManagerMock;
import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;

/**
 * This class tests ArgumentElement
 */
public class PortletContentElementTestCase
        extends PortletContextChildElementTestAbstract {

    /**
     * The baseUrl
     */
    private static String BASE_URL = "ardvark";
    private PolicySourceMock policySourceMock;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        policySourceMock = new PolicySourceMock("policySourceMock", expectations);


    }

    /**
     * Test the element
     */ 
    public void testElement() throws Exception {

        PortletContentElement element = new PortletContentElement();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final RuntimeProjectMock projectMock =
                new RuntimeProjectMock("projectMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pageContextMock.expects.pushMCSIElement(element);
        pageContextMock.expects.popMCSIElement().returns(element);

        // Set the AssetsConfiguration
        AssetsConfiguration assetsConfiguration = new AssetsConfiguration();
        assetsConfiguration.setBaseUrl(BASE_URL);
        parent.setAssetsConfiguration(assetsConfiguration);

        // Set the PolicySource
        parent.setPolicySource(policySourceMock);

        SeparateCacheControlConstraintsMap cacheConstraintsMap = new SeparateCacheControlConstraintsMap();

        DynamicProjectKey key = new DynamicProjectKey(policySourceMock,
                assetsConfiguration, null, cacheConstraintsMap);
        
        ProjectManagerMock pmMock = new ProjectManagerMock("pmMock", expectations);
        pmMock.expects.getDefaultProject().returns(projectMock);
        projectMock.expects.getCacheControlConstraintsMap().returns(cacheConstraintsMap);
        volantisMock.expects.getProjectManager().returns(pmMock);
        
        volantisMock.expects.getDynamicProject(key).returns(projectMock);

        requestContextMock.expects.pushProject(projectMock);
        requestContextMock.expects.popProject(projectMock)
                .returns(projectMock);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        element.elementStart(requestContextMock, null);
        
        
        int result = element.elementEnd(requestContextMock, null);
        assertEquals("Unexpected result from elementEnd.",
                MCSIConstants.CONTINUE_PROCESSING, result);  
                                 
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
