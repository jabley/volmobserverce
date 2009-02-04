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
package com.volantis.mcs.eclipse.ab.editors.devices;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jdom.Element;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

/**
 * A LabelProvider for device policy categories.
 */
public class PolicyDefinitionsLabelProvider extends LabelProvider {

    private static final String mark = "(c) Volantis Systems Ltd 2004."; //$NON-NLS-1$

    /**
     * The name of the policy icon that is returned by the {@link #getImage}
     * method when the element is a category.
     */
    private static final String CATEGORY = "category"; //$NON-NLS-1$

    /**
     * The name of the policy icon that is returned by the {@link #getImage}
     * method when the element is a policy.
     */
    private static final String POLICY = "devicePolicy"; //$NON-NLS-1$

    /**
     * The categoryImage that is returned by the {@link #getImage} method for
     * category elements.
     */
    private Image categoryImage = null;

    /**
     * The policyImage that is returned by the {@link #getImage} method for
     * policy elements.
     */
    private Image policyImage = null;

    /**
     * The DeviceRepositoryAccessorManager associated with this LabelProvider.
     */
    private final DeviceRepositoryAccessorManager dram;

    /**
     * Construct a new PolicyDefinitionsLabelProvider.
     * @param dram the DeviceRepositoryAccessorManager used to retrieve the
     * localized names for policy cateogries. Must not be null.
     * @throws IllegalArgumentException if dram is null.
     */
    public PolicyDefinitionsLabelProvider(DeviceRepositoryAccessorManager dram) {
        if (dram == null) {
            throw new IllegalArgumentException("Cannot be null: dram");
        }

        this.dram = dram;
    }

    // javadoc inherited
    public Image getImage(Object element) {
        if (!(element instanceof Element)) {
            throw new IllegalArgumentException("Expected an org.jdom.Element " +
                    "but was an in intance of " + element.getClass().getName());
        }

        Image image = null;
        Element e = (Element) element;

        if (e.getName().
                equals(DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME)) {

            if (categoryImage == null) {
                    categoryImage = EclipseCommonMessages.getPolicyIcon(CATEGORY);

            }
            image = categoryImage;
        } else if (e.getName().equals(DeviceRepositorySchemaConstants.
                POLICY_ELEMENT_NAME)) {
            if (policyImage == null) {
                    policyImage = EclipseCommonMessages.getPolicyIcon(POLICY);
            }
            image = policyImage;
        } else {
            throw new IllegalArgumentException("Expected a " +
                    DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME +
                    " or a " +
                    DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME +
                    " but was " + e.getName());
        }

        return image;
    }

    // javadoc inherited
    public String getText(Object element) {

        if (!(element instanceof Element)) {
            throw new IllegalArgumentException("Expected an org.jdom.Element " +
                    "but was an in intance of " + element.getClass().getName());
        }

        String text = null;
        Element e = (Element) element;

        if (e.getName().
                equals(DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME)) {
            String categoryName =
                    e.getAttributeValue(DeviceRepositorySchemaConstants.
                    CATEGORY_NAME_ATTRIBUTE);

            text = dram.getLocalizedPolicyCategory(categoryName);
        } else if (e.getName().equals(DeviceRepositorySchemaConstants.
                POLICY_ELEMENT_NAME)) {
            String policyName =
                    e.getAttributeValue(DeviceRepositorySchemaConstants.
                    POLICY_NAME_ATTRIBUTE);

            text = dram.getLocalizedPolicyName(policyName);
        } else {
            throw new IllegalArgumentException("Expected a " +
                    DeviceRepositorySchemaConstants.CATEGORY_ELEMENT_NAME +
                    " or a " +
                    DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME +
                    " but was " + e.getName());
        }

        return text;
    }

    // javadoc inherited
    public void dispose() {
        super.dispose();
        if (categoryImage != null) {
            categoryImage.dispose();
            categoryImage = null;
        }
        if (policyImage != null) {
            policyImage.dispose();
            policyImage = null;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 ===========================================================================
*/
