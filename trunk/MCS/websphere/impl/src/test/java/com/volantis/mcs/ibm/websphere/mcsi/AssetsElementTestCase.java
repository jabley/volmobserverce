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

import com.volantis.mcs.runtime.configuration.project.AssetsConfiguration;


/**
 * This class tests ArgumentElement
 */
public class AssetsElementTestCase
        extends PortletContextChildElementTestAbstract {

    /**
     * The baseUrl
     */
    private static String BASE_URL = "flobnobs"; 

    /**
     * Test the element
     */ 
    public void testElement() throws Exception {

        // Create the ArgumentElement and test the elementStart method.
        AssetsElement element = new AssetsElement();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pageContextMock.expects.pushMCSIElement(element);
        pageContextMock.expects.popMCSIElement().returns(element);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        AssetsAttributes attrs = new AssetsAttributes();
        attrs.setBaseUrl(BASE_URL);
        
        int result;
        result = element.elementStart(requestContextMock, attrs);
        assertEquals("Unexpected result from elementStart.",
                MCSIConstants.PROCESS_ELEMENT_BODY, result);

        result = element.elementEnd(requestContextMock, null);
        assertEquals("Unexpected result from elementEnd.",
                MCSIConstants.CONTINUE_PROCESSING, result);

        AssetsConfiguration assetsConfiguration =
                parent.getAssetsConfiguration();
        String baseUrl= assetsConfiguration.getBaseUrl();
        assertEquals("BaseUrl has unexpected value",baseUrl,BASE_URL);

        // Finally, check that the asset URL prefixes are as expected: there is
        // a leading slash on each.
        assertEquals("Audio asset prefix URL should be /",
                "/",
                assetsConfiguration.getAudioAssets().getPrefixUrl());
        assertEquals("Dynamic visual asset prefix URL should be /",
                "/",
                assetsConfiguration.getDynamicVisualAssets().getPrefixUrl());
        assertEquals("Image asset prefix URL should be /",
                "/",
                assetsConfiguration.getImageAssets().getPrefixUrl());
        assertEquals("Script asset prefix URL should be /",
                "/",
                assetsConfiguration.getScriptAssets().getPrefixUrl());
        assertEquals("Text asset prefix URL should be /",
                "/",
                assetsConfiguration.getTextAssets().getPrefixUrl());
    }
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8312/4	pcameron	VBM:2005051617 MCSI projects ensure asset value has leading slash

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
