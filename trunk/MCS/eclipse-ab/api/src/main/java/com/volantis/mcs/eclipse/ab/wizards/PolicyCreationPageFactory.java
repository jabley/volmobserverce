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
package com.volantis.mcs.eclipse.ab.wizards;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.VariablePolicyType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * A PolicyPageFactory that factors a GenericPolicyCreationPage.
 */
public class PolicyCreationPageFactory {

    /**
     * The ResourceBundle associated with Wizard pages.
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
            WizardMessages.getResourceBundle();

    /**
     * The prefix for resources for this factories pages.
     */
    private static final String RESOURCE_PREFIX = "GenericPolicyCreationPage.";

    // The properties for the page to create.
    private String elementName;
    private String policyExtension;
    private String titleKey;
    private String messageKey;
    private String descriptionKey;
    private String icon;

    /**
     * Construct a new PolicyCreationPageFactory.
     * @param elementName The element name representing the policy to create.
     * attribute.
     * @param icon The icon associated with the policy page.
     */
    public PolicyCreationPageFactory(String elementName,
                                     String icon) {
        this.elementName = elementName;
        this.icon = icon;
        this.policyExtension =
                FileExtension.getFileExtensionForPolicyType(elementName).getExtension();
        this.titleKey = RESOURCE_PREFIX + "title";
        this.messageKey = RESOURCE_PREFIX + "message";
        this.descriptionKey = RESOURCE_PREFIX + "description";
    }

    // javadoc inherited
    public GenericPolicyCreationPage
            createPolicyCreationPage(IStructuredSelection selection) {

        String policyTypeName =
                EclipseCommonMessages.getLocalizedPolicyName(elementName);

        GenericPolicyCreationPage page =
                new GenericPolicyCreationPage(policyTypeName,
                        policyExtension, selection);

        MessageFormat format;
        String args [] = {policyTypeName};
        format = new MessageFormat(RESOURCE_BUNDLE.getString(titleKey));
        String title = format.format(args);

        format = new MessageFormat(RESOURCE_BUNDLE.getString(messageKey));
        String message = format.format(args);

        format = new MessageFormat(RESOURCE_BUNDLE.getString(descriptionKey));
        String description = format.format(args);

        page.setTitle(title);
        page.setMessage(message);
        page.setDescription(description);

        ImageDescriptor id = ABPlugin.getImageDescriptor(icon);
        page.setImageDescriptor(id);

        return page;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	6213/1	adrianj	VBM:2004101908 Changed name label in new policy wizards

 14-Apr-04	3862/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 14-Apr-04	3847/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 16-Nov-03	1909/3	allan	VBM:2003102405 All policy wizards.

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 ===========================================================================
*/
