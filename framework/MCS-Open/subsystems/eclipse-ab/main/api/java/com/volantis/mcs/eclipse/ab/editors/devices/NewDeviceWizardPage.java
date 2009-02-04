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
import com.volantis.mcs.eclipse.validation.CharacterSetValidator;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.utilities.FaultTypes;
import com.volantis.xml.jdom.SimpleVisitor;
import com.volantis.xml.jdom.Walker;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Element;

import java.text.MessageFormat;
import java.util.HashMap;

/**
 * New device wizard page.
 */
public class NewDeviceWizardPage extends WizardPage {
    /**
     * Constant for resource messages associated with the page.
     */
    private static final String RESOURCE_PREFIX = "NewDeviceWizardPage.";

    /**
     * The page title resource defintion.
     */
    private static final String PAGE_TITLE = DevicesMessages.getString(
            RESOURCE_PREFIX + "title");

    /**
     * The page description string constant.
     */
    private static final String PAGE_DESCRIPTION = DevicesMessages.getString(
            RESOURCE_PREFIX + "description");

    /**
     * The icon resource location constant.
     */
    private static final String ICON = DevicesMessages.getString(
            RESOURCE_PREFIX + "icon");

    /**
     * The horizontal spacing constant.
     */
    private static final int HORIZONTAL_SPACING = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "horizontalSpacing").intValue();

    /**
     * The vertical spacing constant.
     */
    private static final int VERTICAL_SPACING = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "verticalSpacing").intValue();

    /**
     * The margin height constant.
     */
    private static final int MARGIN_HEIGHT = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "marginHeight").intValue();

    /**
     * The margin width constant.
     */
    private static final int MARGIN_WIDTH = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "marginWidth").intValue();

    /**
     * Valid status constant.
     */
    private static final ValidationStatus VALID_STATUS =
            new ValidationStatus(Status.OK, "");

    /**
     * The message key mappings constant map.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    /**
     * The CharacterSetValidator for the device name.
     */
    private static final CharacterSetValidator DEVICE_NAME_VALIDATOR =
            DeviceConstants.DEVICE_NAME_VALIDATOR;

    /**
     * Initialize the regular expression.
     */
    static {
        // Create and initialize the message key mappings.
        MESSAGE_KEY_MAPPINGS = new HashMap(1);
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.CANNOT_BE_NULL,
                RESOURCE_PREFIX + "error.valueRequired");

        MESSAGE_KEY_MAPPINGS.put(FaultTypes.INVALID_CHARACTER,
                RESOURCE_PREFIX + "error.invalidCharacter");

        MESSAGE_KEY_MAPPINGS.put(FaultTypes.TOO_MANY_CHARACTERS,
                RESOURCE_PREFIX + "error.tooManyCharacters");

        MESSAGE_KEY_MAPPINGS.put(FaultTypes.DUPLICATE_NAME,
                RESOURCE_PREFIX + "error.duplicateDeviceName");
    }

    /**
     * The validation message builder.
     */
    private ValidationMessageBuilder validationMessageBuilder;

    /**
     * The label provider.
     */
    private final ILabelProvider labelProvider;

    /**
     * The content provider.
     */
    private final IContentProvider contentProvider;

    /**
     * The initial selection.
     */
    private final ISelection initialSelection;

    /**
     * The root element.
     */
    private final Element root;

    /**
     * The DeviceEditorContext currently in use.
     */
    private final DeviceEditorContext context;

    /**
     * The tree viewer control.
     */
    private TreeViewer treeViewer;

    /**
     * The device name text control.
     */
    private Text deviceName;

    /**
     * The device fallback tree.
     */
    private Tree deviceFallbackTree;

    /**
     * The fallback device name.
     */
    private String fallbackDevice;

    /**
     * The device name.
     */
    private String device;

    /**
     * The duplicate name visitor.
     */
    private DuplicateDeviceNameVisitor visitor;

    /**
     * The walker used for finding duplicate device names during validation.
     */
    private final Walker walker = new Walker();

    /**
     * The height hint for this wizard.
     */
    private static final int HEIGHT_HINT = 300;


    /**
     * Inner class that is used find duplicate device names.
     */
    private class DuplicateDeviceNameVisitor extends SimpleVisitor {

        /**
         * True if a duplicate is found, false otherwise.
         */
        private boolean duplicate = false;

        // javadoc inherited
        public Action visit(Element element) {
            if ((getDevice() != null) && (element != null) &&
                    getDevice().equals(element.getAttributeValue("name"))) {
                duplicate = true;
                return com.volantis.xml.jdom.Visitor.Action.STOP;
            } else {
                return com.volantis.xml.jdom.Visitor.Action.CONTINUE;
            }
        }

        /**
         * Return true if the visitor found a duplicate, false otherwise.
         * @return true if the visitor found a duplicate, false otherwise.
         */
        public boolean isDuplicate() {
            return duplicate;
        }

        /**
         * Reset the status of the visitor.
         */
        public void reset() {
            duplicate = false;
        }
    }

    /**
     * Create a new instance of the device wizard page.
     *
     * @param pageName        the page's name.
     * @param labelProvider   the label provider
     * @param contentProvider the content provider.
     * @param selection       the initial selection which may be null.
     * @param root            the root element.
     * @param context the DeviceEditorContext within which this wizard page
     * is used
     * @throws IllegalArgumentException if the labelProvider, contentProvider
     * or root element is null.
     */
    public NewDeviceWizardPage(String pageName,
                               ILabelProvider labelProvider,
                               IContentProvider contentProvider,
                               ISelection selection,
                               Element root,
                               DeviceEditorContext context)
            throws IllegalArgumentException {
        super(pageName);

        if (labelProvider == null) {
            throw new IllegalArgumentException(
                    "LabelProvider cannot be null.");
        }
        if (contentProvider == null) {
            throw new IllegalArgumentException(
                    "ContentProvider cannot be null.");
        }

        if (root == null) {
            throw new IllegalArgumentException(
                    "Root cannot be null.");
        }



        validationMessageBuilder = new ValidationMessageBuilder(
                DevicesMessages.getResourceBundle(), MESSAGE_KEY_MAPPINGS, null);

        visitor = new DuplicateDeviceNameVisitor();
        this.labelProvider = labelProvider;
        this.contentProvider = contentProvider;
        this.root = root;
        this.initialSelection = selection;
        this.context = context;

        setTitle(PAGE_TITLE);
        setMessage(PAGE_DESCRIPTION);
        setImageDescriptor(ABPlugin.getImageDescriptor(ICON));
    }

    // javadoc inherited.
    public void createControl(Composite parent) {
        Composite topLevel = new Composite(parent, SWT.NONE);
        GridLayout layoutGrid = new GridLayout(1, false);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = HEIGHT_HINT;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;

        topLevel.setLayoutData(data);
        layoutGrid.horizontalSpacing = HORIZONTAL_SPACING;
        layoutGrid.verticalSpacing = VERTICAL_SPACING;
        layoutGrid.marginHeight = MARGIN_HEIGHT;
        layoutGrid.marginWidth = MARGIN_WIDTH;

        topLevel.setLayout(layoutGrid);
        addFallbackComposite(topLevel);
        addNewDeviceNameComposite(topLevel);
        topLevel.layout();
        setControl(topLevel);
        validatePage();
        setPageComplete(isPageComplete());
    }

    /**
     * Add the device name composite to the top level composite.
     * @param topLevel the top level composite.
     */
    private void addNewDeviceNameComposite(Composite topLevel) {
        GridLayout layout = new GridLayout(2, false);
        Composite composite = new Composite(topLevel, 0);
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        composite.setLayout(layout);
        Label label = new Label(composite, SWT.NONE);

        label.setText(DevicesMessages.getString(RESOURCE_PREFIX + "newDevice.label"));
        deviceName = new Text(composite, SWT.SINGLE | SWT.BORDER);

        deviceName.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                getWizard().getContainer().updateButtons();
            }

        });
        deviceName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Add the fallback composite to the top level composite.
     * @param topLevel the top level composite.
     */
    private void addFallbackComposite(Composite topLevel) {
        Label label = new Label(topLevel, SWT.NONE);
        label.setText(DevicesMessages.getString(RESOURCE_PREFIX +
                "deviceTree.label"));

        Composite treeContainer = new Composite(topLevel, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 1;
        layout.marginWidth = 1;
        treeContainer.setLayout(layout);
        treeContainer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        Display display = treeContainer.getDisplay();
        treeContainer.setBackground(display.getSystemColor(SWT.COLOR_BLACK));

        deviceFallbackTree = new Tree(treeContainer, SWT.SINGLE | SWT.H_SCROLL |
                SWT.V_SCROLL);
        deviceFallbackTree.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.heightHint = HEIGHT_HINT - 20;

        deviceFallbackTree.setLayoutData(data);
        treeViewer = new TreeViewer(deviceFallbackTree);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(labelProvider);
        treeViewer.setInput(root);
        treeViewer.expandToLevel(2);
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                getWizard().getContainer().updateButtons();
            }
        });
        if (initialSelection != null) {
            treeViewer.setSelection(initialSelection, true);
        }
    }

    // javadoc inherited
    public boolean isPageComplete() {
        boolean result = true;
        if ((deviceName != null) && (treeViewer != null)) {
            result = validatePage();
        }
        return result;
    }

    /**
     * Validate the page and return true if the page's content is valid, false
     * otherwise.
     *
     * @return true if the page's content is valid, false otherwise.
     */
    private boolean validatePage() {
        boolean valid = true;

        // Update the device and fallback device names.
        device = null;
        fallbackDevice = null;
        updateValues();

        // Default status is valid.
        ValidationStatus status = VALID_STATUS;
        String supplementaryArgs[] = {
            DevicesMessages.getString(RESOURCE_PREFIX + "newDevice.text")
        };
        validationMessageBuilder.setSupplementaryFormatArgs(supplementaryArgs);

        status = DEVICE_NAME_VALIDATOR.validate(getDevice(),
                validationMessageBuilder);

        // Ensure the fallback device has been selected.
        if ((status.getSeverity() == Status.OK) &&
                deviceFallbackTree.getSelectionCount() <= 0) {
            String message = DevicesMessages.getString(RESOURCE_PREFIX +
                    "error.fallbackDeviceIsMandatory");
            status = new ValidationStatus(4, message);
        }

        // If the status is OK then check to see if we have any duplicate names.
        if (status.getSeverity() == Status.OK) {
            visitor.reset();
            walker.visit(root, visitor);
            if (visitor.isDuplicate()) {
                String message = DevicesMessages.getString(RESOURCE_PREFIX +
                        "error.duplicateDeviceName");
                String args[] = {
                    getDevice()
                };
                MessageFormat format = new MessageFormat(message);
                message = format.format(args);
                status = new ValidationStatus(Status.ERROR, message);
            }
        }

        if (status.getSeverity() == Status.ERROR) {
            setErrorMessage(status.getMessage());
        } else if (status.getSeverity() == Status.WARNING) {
            setErrorMessage(null);
            setMessage(status.getMessage());
        } else {
            setErrorMessage(null);
            if(context.isAdminProject()) {
                setMessage(DevicesMessages.getString(RESOURCE_PREFIX +
                            "info.admin"));
            } else {
                setMessage(DevicesMessages.getString(RESOURCE_PREFIX +
                            "info.custom"));
            }
        }
        valid = status.getSeverity() == Status.OK;
        return valid;
    }

    /**
     * Helper method to update the device and fallback device variables. This is
     * necessary since we cannot obtain the values from disposed widgets.
     */
    private void updateValues() {
        if (!deviceName.isDisposed()) {
            device = deviceName.getText();
            if(!context.isAdminProject()) {
                // Custom device names are given a prefix
                device = context.getDeviceRepositoryAccessorManager().
                        getCustomDeviceNamePrefix() +
                        device;
            }
        }
        if (!deviceFallbackTree.isDisposed() &&
                (deviceFallbackTree.getSelectionCount() == 1)) {
            fallbackDevice = deviceFallbackTree.getSelection()[0].getText();
        }
    }

    /**
     * Get the new device name.
     * @return the new device name.
     */
    public String getDevice() {
        return device;
    }

    /**
     * Get the fallback device name.
     * @return the fallback device name.
     */
    public String getFallbackDevice() {
        return fallbackDevice;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Apr-05	7572/1	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 29-Apr-04	4035/3	byron	VBM:2004032403 Create the NewDeviceWizard class - review issues addressed

 27-Apr-04	4035/1	byron	VBM:2004032403 Create the NewDeviceWizard class

 ===========================================================================
*/
