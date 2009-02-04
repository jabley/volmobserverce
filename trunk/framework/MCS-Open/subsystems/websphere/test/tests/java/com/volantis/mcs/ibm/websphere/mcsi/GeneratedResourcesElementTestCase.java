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



/**
 * This class tests GeneratedResourcesElement
 *
 * todo: later factor together with similar portlet content child tests
 */
public class GeneratedResourcesElementTestCase
        extends PortletContextChildElementTestAbstract {

    /**
     * The base directory
     */
    private static String BASE_DIR = "/flobnobs";

    /**
     * Test the element
     */
    public void testElement() throws Exception {

        // Create the Element and test the elementStart method.
        GeneratedResourcesElement element = new GeneratedResourcesElement();

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        pageContextMock.expects.pushMCSIElement(element);
        pageContextMock.expects.popMCSIElement().returns(element);

        GeneratedResourcesAttributes attrs = new GeneratedResourcesAttributes();
        attrs.setBaseDir(BASE_DIR);

        int result;
        result = element.elementStart(requestContextMock, attrs);
        assertEquals("Unexpected result from elementStart.",
                MCSIConstants.PROCESS_ELEMENT_BODY, result);

        result = element.elementEnd(requestContextMock, null);
        assertEquals("Unexpected result from elementEnd.",
                MCSIConstants.CONTINUE_PROCESSING, result);

        String baseDir = parent.getGeneratedResourcesConfiguration()
                .getBaseDir();
        assertEquals("BaseDir has unexpected value", baseDir, BASE_DIR);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 ===========================================================================
*/
