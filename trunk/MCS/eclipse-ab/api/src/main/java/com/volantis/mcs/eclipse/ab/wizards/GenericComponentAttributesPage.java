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

import com.volantis.mcs.eclipse.ab.core.AttributesComposite;
import com.volantis.mcs.eclipse.ab.core.AttributesCompositeBuilder;
import com.volantis.mcs.eclipse.common.AttributesDetails;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.eclipse.common.ProjectProviderReceiver;
import com.volantis.mcs.eclipse.common.ProjectReceiver;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.validation.Validator;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.jdom.Element;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * This is a wizard page that allows the user to enter the values for
 * policy properties.
 *
 * This page makes use of AttributesCompositeBuilder to build the
 * Composite that contain the policy properties that are available for
 * the policy being created.
 */
public class GenericComponentAttributesPage extends WizardPage
        implements PolicyWizardPage, ProjectReceiver {

    /**
     * The resource prefix used for extracting string resource names.
     */
    private static final String RESOURCE_PREFIX =
            "GenericComponentAttributesPage."; //$NON-NLS-1$

    /**
     * The ResourceBundle associated with Wizard pages.
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
            WizardMessages.getResourceBundle();

    /**
     * Mapping between fault types understood by this page and
     * message keys in the Wizards properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    static {
        // Initialize message key mappings
        MESSAGE_KEY_MAPPINGS = new HashMap();
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_CHARACTER,
                RESOURCE_PREFIX + "invalidNameCharacter"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_FIRST_CHARACTER,
                RESOURCE_PREFIX + "invalidFirstCharacter"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                RESOURCE_PREFIX + "nameTooLong"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_EXTENSION,
                RESOURCE_PREFIX + "invalidExtension"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.MUST_START_WITH,
                RESOURCE_PREFIX + "mustStartWith"); //$NON-NLS-1$
    }

    /**
     * The element name representing the policy whose attributes are available
     * from this page.
     */
    private String elementName;

    /**
     * The AttributesComposite for this page.
     */
    private AttributesComposite attrsComposite;

    /**
     * The ProjectProvider for this page.
     */
    private ProjectProviderReceiver projectProvider;

    /**
     * Holds the validation state of the Page.
     */
    private boolean isValidated = false;

    /**
     * The propertyChangeListener used to respond to user input and validate
     * the page
     */
    private IPropertyChangeListener propChangeListener =
            new IPropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent event) {
                    // a change has been made so invalidate the cache to force
                    // revalidation
                    isValidated = false;
                    setPageComplete(validatePage());
                }
            };

    /**
     * Construct a new GenericComponentAttributesPage.
     * @param elementName The name of the element that represents the policy
     * whose attributes are available on this page.
     * @param projectProvider The ProjectProvider for this page. Can be null
     * and if it is this page will assume the previous page is a ProjectProvider
     * and use it instead.
     */
    public GenericComponentAttributesPage(String elementName,
                                          ProjectProviderReceiver projectProvider,
                                          ImageDescriptor bannerImage) {
        super(elementName);
        this.elementName = elementName;

        if (elementName == null) {
            throw new IllegalArgumentException("Cannot be null: elementName"); //$NON-NLS-1$
        }

        this.projectProvider = projectProvider;

        String args [] =
                {EclipseCommonMessages.getLocalizedPolicyName(elementName)};
        MessageFormat format;

        format = new MessageFormat(RESOURCE_BUNDLE.
                getString(RESOURCE_PREFIX + "title")); //$NON-NLS-1$
        String title = format.format(args);
        setTitle(title);

        format = new MessageFormat(RESOURCE_BUNDLE.
                getString(RESOURCE_PREFIX + "description")); //$NON-NLS-1$
        String description = format.format(args);
        setDescription(description);
        setImageDescriptor(bannerImage);
        setPageComplete(true);
    }

    // javadoc inherited
    public void createControl(Composite composite) {
        AttributesCompositeBuilder builder =
                AttributesCompositeBuilder.getSingleton();

        AttributesDetails attributesDetails =
                new PolicyAttributesDetails(elementName, false);

        // No context available for to obtain the ActionableHandler. OK to pass
        // through a null value.
        attrsComposite =
                builder.buildAttributesComposite(composite,
                        attributesDetails, projectProvider, null);

        attrsComposite.addPropertyChangeListener(propChangeListener);

        setErrorMessage(null);
        setMessage(null);
        setControl(attrsComposite);
    }

    // javadoc inherited
    public boolean isPageComplete() {
        return validatePage();
    }

    /**
     * Returns whether this page's controls currently all contain valid
     * values.
     *
     * @return <code>true</code> if all controls are valid, and
     *   <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
        // check cached variable
        if (!isValidated) {
            String[] attributeNames = attrsComposite.getAttributeNames();
            ValidationStatus status = null;
            boolean valid = true;
            for (int i = 0; i < attributeNames.length && valid; i++) {
                String attrName = attributeNames[i];
                String value = attrsComposite.getAttributeValue(attrName);
                Validator validator =
                        attrsComposite.getAttributeValidator(attrName);
                if (value != null && validator != null) {
                    status = validateAttribute(attrName, value, validator);
                    valid = status.getSeverity() == IStatus.OK;
                }
            }

            if (valid) {
                setErrorMessage(null);
                setMessage(null);
            } else {
                // status should be IStatus.ERROR since there should never be
                // another type if we are here i.e. INFOs are not expected.
                setErrorMessage(status.getMessage());
            }
            isValidated = valid;
        }
        return isValidated;
    }

    /**
     * Validate that a particular value is valid for a particular attribute.
     *
     * @param attrName The name of the attribute to validate. This is only used
     * for the message builder since all attributes are treated as policy
     * names.
     * @param value The value to validate.
     * @param validator The validator to do the validation with.
     * @return the ValidationStatus resulting from the validation.
     */
    private ValidationStatus validateAttribute(String attrName, String value,
                                               Validator validator) {
        String supplementaryFormatArgs [] = new String[1];
        String localName = EclipseCommonMessages.
                getLocalizedPolicyName(elementName, attrName);
        supplementaryFormatArgs[0] = localName;
        ValidationMessageBuilder messageBuilder =
                new ValidationMessageBuilder(WizardMessages.
                getResourceBundle(), MESSAGE_KEY_MAPPINGS,
                        supplementaryFormatArgs);
        ValidationStatus status = validator.validate(value, messageBuilder);

        return status;
    }

    /**
     * Add the attributes set in this page to the given element.
     * @param element The element.
     */
    public void performFinish(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element"); //$NON-NLS-1$
        }
        if (validatePage()) {
            String[] attrs = attrsComposite.getAttributeNames();
            for (int i = 0; i < attrs.length; i++) {
                String attr = attrs[i];
                String value = attrsComposite.getAttributeValue(attr);
                if (value != null && value.length() > 0) {
                    element.setAttribute(attr, value);
                }
            }
        }
    }

    // TODO later implement this
    public void performFinish(PolicyBuilder[] policyBuilderHolder) {
    }

    /**
     * Set the project for this page. What this method really does is
     * set the project in the ProjectProvider for this page.
     */
    // rest of javadoc inherited
    public void setProject(final IProject project) {
        projectProvider.setProject(project);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-May-04	4434/6	matthew	VBM:2004021715 merged

 26-May-04	4434/4	matthew	VBM:2004021715 merged

 17-May-04	4434/1	matthew	VBM:2004021715 make the Finish button in Component wizards enabled or disabled to reflect current validation state

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 14-Apr-04	3862/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 14-Apr-04	3847/1	byron	VBM:2004041303 Translated NLV values modified to lowercase

 19-Mar-04	3471/1	byron	VBM:2004030504 Component Wizard does not add / to front of fallback components

 23-Feb-04	3057/2	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 13-Feb-04	2985/4	allan	VBM:2004012803 Fix for null project in ProjectProviders

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 13-Dec-03	2208/1	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 17-Nov-03	1909/10	allan	VBM:2003102405 Rework issues.

 16-Nov-03	1909/8	allan	VBM:2003102405 All policy wizards.

 16-Nov-03	1909/3	allan	VBM:2003102405 Done Image Component Wizard.

 ===========================================================================
*/
