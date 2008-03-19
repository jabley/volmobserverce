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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.ab.core.AttributesComposite;
import com.volantis.mcs.eclipse.ab.core.AttributesCompositeBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.resources.IProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Test the building of attribute composite controls.
 */
public class AttributesCompositeBuilderTestCase
        extends TestCaseAbstract {


    /**
     * Simple test of building the composite using a PolicyAttributesDetails.
     */
    public void testBuildingComposite() throws Exception {
        final Shell shell = new Shell(SWT.NONE);
        AttributesComposite parent = new AttributesComposite(shell, SWT.NONE);

        AttributesDetails attributesDetails =
                new PolicyAttributesDetails("assetGroup", false);

        AttributesComposite result = AttributesCompositeBuilder.getSingleton().
                buildAttributesComposite(parent, attributesDetails,
                        (IProject) null, null);

        assertNotNull("Should not be null", result);
        String [] attributeNames = result.getAttributeNames();
        assertEquals("Size should match: " + result, 2,
                attributeNames.length);

        List properties = new ArrayList();
        properties.add("prefixURL");        
        properties.add("locationType");

        for (int i=0; i<attributeNames.length;i++) {
            String name = attributeNames[i];
            assertTrue("Value should be in collection: " + name,
                    properties.contains(name));
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-May-04	4231/2	tom	VBM:2004042704 Fixedup the 2004032606 change

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 13-Feb-04	2985/1	allan	VBM:2004012803 Fix for null project in ProjectProviders

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 27-Nov-03	2013/4	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 24-Nov-03	1825/11	byron	VBM:2003092601 Updated testcase - and fixed minor resource text width bug

 24-Nov-03	1825/9	byron	VBM:2003092601 Updated testcase

 15-Nov-03	1825/4	byron	VBM:2003092601 Create generic policy property composite

 15-Nov-03	1825/1	byron	VBM:2003092601 Create generic policy property composite

 ===========================================================================
*/
