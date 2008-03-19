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

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.controls.ComboViewer;
import com.volantis.mcs.eclipse.validation.CharacterSetValidator;
import com.volantis.mcs.eclipse.validation.CharacterValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.text.MessageFormat;
import java.util.HashMap;

import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page that allows you to specify the name, composition and type of a
 * policy. These can be retrieved with {@link #getPolicyName},
 * {@link #getPolicyTypeComposition} and {@link #getPolicyType} respectively.
 * Policy names are validated according to the schema constraints for policy
 * names in devices-core.xsd, and also checked that the policy name does not
 * exist already. 
 */
public class NewDevicePolicyWizardPage extends WizardPage {

    /**
     * Resource prefix for the NewDevicePolicyWizardPage.
     */
    private final static String RESOURCE_PREFIX = "NewDevicePolicyWizardPage.";

    /**
     * The path to the icon that this page uses.
     */
    private static final String PAGE_ICON =
            DevicesMessages.getString(RESOURCE_PREFIX + "icon");

    /**
     * The title for this page.
     */
    private static final String PAGE_TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The description for this page.
     */
    private static final String PAGE_DESCRIPTION =
            DevicesMessages.getString(RESOURCE_PREFIX + "description");

    /**
     * The text for the policy label.
     */
    private static final String POLICY_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX + "text.policy");

    /**
     * The text for the composition label.
     */
    private static final String COMPOSITION_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX + "text.composition");

    /**
     * The text for the type label.
     */
    private static final String TYPE_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX + "text.type");

    /**
     * The horizontal spacing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "horizontalSpacing").
            intValue();

    /**
     * The vertical spacing between widgets.
     */
    private static final int VERTICAL_SPACING =
            DevicesMessages.getInteger(RESOURCE_PREFIX + "verticalSpacing").
            intValue();

    /**
     * The MessageFormat for duplicate policy names.
     */
    private static final MessageFormat DUPLICATE_POLICY_FORMAT =
            new MessageFormat(DevicesMessages.getString(RESOURCE_PREFIX +
            "duplicatePolicyName"));

    /**
     * Mapping between fault types understood by this page and
     * message keys in the this Wizard's properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    /**
     * The regular expression for pattern matching valid/invalid
     * policy names.
     */
    private static final RE POLICY_NAME_REG_EXP;

    /**
     * The CharacterValidator that will use pattern matching to determine
     * the validity of characters in a candidate device policy name.
     */
    private static final CharacterValidator CHARACTER_VALIDATOR;

    /**
     * Array of supplementary arguments for the validation message builder.
     */
    private static final Object[] SUPPLEMENTARY_ARGS = new Object[1];

    /**
     * The CharacterSetValidator for device policy names.
     */
    private static final CharacterSetValidator POLICY_NAME_VALIDATOR;

    /**
     * The maximum length of policy names, as defined by devices-core.xsd.
     */
    private static final int POLICY_NAME_MAX_LENGTH = 200;

    // Initialise the validator and message mappings.
    static {

        // Create the regular expression object, as defined by
        // devices-core.xsd, with some characters escaped. The regular
        // expression from the schema is: [A-Za-z0-9_.\-]+
        try {
            POLICY_NAME_REG_EXP = new RE("[A-Za-z0-9\\-_.]+");
        } catch (RESyntaxException e) {
            throw new UndeclaredThrowableException(e);
        }

        CHARACTER_VALIDATOR =
                new CharacterValidator() {
                    public boolean isValidChar(char c) {
                        String charString = new Character(c).toString();
                        return POLICY_NAME_REG_EXP.match(charString);
                    }
                };

        POLICY_NAME_VALIDATOR =
                new CharacterSetValidator(CHARACTER_VALIDATOR);

        // The min and max lengths are specified in devices-core.xsd.
        POLICY_NAME_VALIDATOR.setMinChars(1);
        POLICY_NAME_VALIDATOR.setMaxChars(POLICY_NAME_MAX_LENGTH);
        SUPPLEMENTARY_ARGS[0] = new Integer(POLICY_NAME_MAX_LENGTH);

        // Initialize message key mappings
        MESSAGE_KEY_MAPPINGS = new HashMap();
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_CHARACTER,
                RESOURCE_PREFIX + "invalidNameCharacter");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                RESOURCE_PREFIX + "nameTooLong");
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.CANNOT_BE_NULL,
                RESOURCE_PREFIX + "emptyName");
    }

    /**
     * The ValidationMessageBuilder for this page.
     */
    private ValidationMessageBuilder messageBuilder;

    /**
     * The manager to use for checking for duplicate policy names.
     */
    private final DeviceRepositoryAccessorManager deviceRAM;

    /**
     * The text field for entering short policy names.
     */
    private Text policyText;

    /**
     * The Combo for choosing the policy's composition.
     */
    private ComboViewer compositionCombo;

    /**
     * The Combo for choosing the policy's type.
     */
    private ComboViewer typeCombo;


    /**
     * Creates a NewDevicePolicyWizardPage.
     * @param deviceRAM the manager to use for checking for duplicate policy
     * names. Cannot be null.
     * @throws IllegalArgumentException if deviceRAM is null.
     */
    public NewDevicePolicyWizardPage(DeviceRepositoryAccessorManager deviceRAM) {
        super("NewDevicePolicyWizardPage");

        if (deviceRAM == null) {
            throw new IllegalArgumentException("Cannot be null: deviceRAM.");
        }

        this.deviceRAM = deviceRAM;

        messageBuilder = new ValidationMessageBuilder(
                DevicesMessages.getResourceBundle(),
                MESSAGE_KEY_MAPPINGS, SUPPLEMENTARY_ARGS);

        // set the page title
        setTitle(PAGE_TITLE);

        // set the description
        setMessage(PAGE_DESCRIPTION);

        // set the page icon
        setImageDescriptor(ABPlugin.getImageDescriptor(PAGE_ICON));
    }

    // javadoc inherited
    public void createControl(Composite parent) {
        // create the wizard page
        Composite topLevel = new Composite(parent, SWT.NONE);

        GridLayout layoutGrid = new GridLayout(2, false);
        layoutGrid.horizontalSpacing = HORIZONTAL_SPACING;
        layoutGrid.verticalSpacing = VERTICAL_SPACING;
        layoutGrid.marginHeight = 0;
        layoutGrid.marginWidth = 0;
        topLevel.setLayout(layoutGrid);

        // create the widgets
        addControls(topLevel);
        topLevel.layout();
        setControl(topLevel);

        // validate this wizard page
        validatePage();

        // set the page complete status
        setPageComplete(isPageComplete());
    }

    /**
     * Creates and adds the widgets to the page.
     * @param container the parent Composite for the widgets
     */
    private void addControls(Composite container) {
        Label policyLabel = new Label(container, SWT.NONE);
        policyLabel.setText(POLICY_TEXT);

        policyText = new Text(container, SWT.BORDER);
        policyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Add a listener that reponds to text changes by performing validation.
        policyText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                boolean valid = validatePage();
                NewDevicePolicyWizardPage.this.setPageComplete(valid);
            }
        });

        Label compositionLabel = new Label(container, SWT.NONE);
        compositionLabel.setText(COMPOSITION_TEXT);

        PresentableItem[] compItems = DevicesMessages.getLocalizedCompositions();

        compositionCombo = new ComboViewer(container, SWT.READ_ONLY, compItems);
        compositionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Add a listener which responds to composition selections by
        // populating the type Combo with the appropriate PolicyTypes.
        compositionCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                populateTypeCombo();
            }
        });

        Label typeLabel = new Label(container, SWT.NONE);
        typeLabel.setText(TYPE_TEXT);

        PresentableItem[] typeItems =
                DevicesMessages.getLocalizedPolicyTypes(
                        (PolicyTypeComposition) compItems[0].realValue);

        typeCombo = new ComboViewer(container, SWT.READ_ONLY, typeItems);
        typeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Set the initial composition selection and populate the type Combo.
        compositionCombo.getCombo().select(0);
        populateTypeCombo();
    }

    /**
     * Populates the type Combo with all PolicyTypes of the currently selected
     * composition.
     */
    private void populateTypeCombo() {
        PresentableItem[] typeItems =
                DevicesMessages.getLocalizedPolicyTypes(
                        (PolicyTypeComposition) compositionCombo.getValue());

        typeCombo.setItems(typeItems);
        // Always select the first item.
        typeCombo.getCombo().select(0);
    }

    /**
     * Validates this wizard page by validating the policy name. An error
     * message is displayed if the policy name is invalid.
     * @return true if the page is valid; false otherwise
     */
    private boolean validatePage() {
        boolean isOK = true;
        final String policyName = policyText.getText();
        ValidationStatus status = POLICY_NAME_VALIDATOR.validate(
                policyName, messageBuilder);

        if (status.isOK()) {
            // Policy name has passed the schema constraints so now check for
            // duplicates.
            if (deviceRAM.policyExists(policyName)) {
                String errorMsg =
                        DUPLICATE_POLICY_FORMAT.format(new Object[]{policyName});
                setErrorMessage(errorMsg);
                isOK = false;
            } else {
                // Clear the error message
                setErrorMessage(null);
                // set the description back to the default
                setMessage(PAGE_DESCRIPTION);
            }
        } else if (status.getSeverity() == ValidationStatus.INFO) {
            setMessage(status.getMessage());
            isOK = false;
        } else if (status.getSeverity() == ValidationStatus.ERROR) {
            // Set the error message
            setErrorMessage(status.getMessage());
            isOK = false;
        }

        return isOK;
    }

    // javadoc inherited
    public boolean isPageComplete() {
        return (getPolicyType() != null) && validatePage();
    }

    /**
     * Gets the policy name specified by this wizard.
     * @return the policy name
     */
    public String getPolicyName() {
        return policyText.getText();
    }

    /**
     * Gets the policy type composition specified by this wizard.
     * @return the {@link PolicyTypeComposition}
     */
    public PolicyTypeComposition getPolicyTypeComposition() {
        return (PolicyTypeComposition) compositionCombo.getValue();
    }

    /**
     * Gets the policy type specified by this wizard.
     * @return the {@link PolicyType}
     */
    public PolicyType getPolicyType() {
        return (PolicyType) typeCombo.getValue();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4397/2	byron	VBM:2004051305 Device Editor, New Custom entry, no default value displayed causes exception

 14-May-04	4384/4	pcameron	VBM:2004050703 NewDevicePolicyWizard checks for duplicate policy names

 11-May-04	4161/1	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 04-May-04	4121/5	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 04-May-04	4121/3	pcameron	VBM:2004042910 Localised the policy type and composition names, and completed test cases

 29-Apr-04	4103/2	allan	VBM:2004042812 Redesign PolicyType & PolicyTypeComposition.

 29-Apr-04	4035/3	byron	VBM:2004032403 Create the NewDeviceWizard class - review issues addressed

 27-Apr-04	4035/1	byron	VBM:2004032403 Create the NewDeviceWizard class

 26-Apr-04	4001/7	pcameron	VBM:2004032104 Refactored the device policy wizard and page so that no policy element is created or returned

 26-Apr-04	4001/5	pcameron	VBM:2004032104 Added NewDevicePolicyWizard and NewDevicePolicyWizardPage

 ===========================================================================
*/
