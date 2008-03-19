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

import com.volantis.devrep.repository.accessors.EclipseDeviceRepository;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.actions.ODOMAction;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionCommand;
import com.volantis.mcs.eclipse.ab.actions.ODOMActionDetails;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyType;
import com.volantis.mcs.eclipse.ab.editors.devices.types.PolicyTypeComposition;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.controls.ActionButton;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;
import org.jdom.Namespace;

import java.text.MessageFormat;

/**
 * The form section that displays the available policies for devices and
 * allows users to add and remove policies. This section affects the
 * device definitions document, the master device document when a new policy
 * is created and when a policy removed every device that has a setting for
 * that policy must also be modified.
 */
public class DeviceDefinitionPoliciesSection extends FormSection
        implements XPathFocusable {
    /**
     * The prefix for property resources associated with this class.
     */
    private static final String RESOURCE_PREFIX =
            "DeviceDefinitionPoliciesSection.";

    /**
     * The default minimum width for device definition policies sections.
     */
    private static final int DEFAULT_MIN_WIDTH = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "minWidth").intValue();

    /**
     * Constant used as the title for this form section
     */
    private static final String TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * Constant used as the message for this form section
     */
    private static final String MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "message");

    /**
     * The message for the dialog for policy deletion confirmation.
     */
    private static final MessageFormat POLICY_DELETION_DIALOG_MESSAGE_FORMAT =
            new MessageFormat(DevicesMessages.getString(
                    RESOURCE_PREFIX + "deletePolicyDialog.message"));

    /**
     * The title for the dialog for policy deletion confirmation.
     */
    private static final String POLICY_DELETION_DIALOG_TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "deletePolicyDialog.title");

    /**
     * The text for the Yes button of the dialog for policy deletion confirmation.
     */
    private static final String POLICY_DELETION_DIALOG_YES_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "deletePolicyDialog.yes");

    /**
     * The text for the No button of the dialog for policy deletion confirmation.
     */
    private static final String POLICY_DELETION_DIALOG_NO_TEXT =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "deletePolicyDialog.no");

    /**
     * The original delete actions that will be restored when
     * this section loses focus.
     */
    private IAction origDelete;

    /**
     * The Delete Policy action.
     */
    private Action deletePolicyAction;

    /**
     * The new policy action.
     */
    private Action newPolicyAction;

    /**
     * The categoriesComposite control.
     */
    private CategoriesComposite categoriesComposite;

    /**
     * The DeviceEditorContext associated with this section.
     */
    private final DeviceEditorContext context;


    /**
     * Construct a new CategoriesSection.
     */
    // rest of javadoc inherited
    public DeviceDefinitionPoliciesSection(Composite parent, int style,
                                           DeviceEditorContext context) {
        super(parent, style);

        setMinWidth(DEFAULT_MIN_WIDTH);
        this.context = context;
        DeviceRepositoryAccessorManager dram =
                context.getDeviceRepositoryAccessorManager();

        Section section =
                SectionFactory.createSection(this, SWT.NONE, TITLE, MESSAGE);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        categoriesComposite = new CategoriesComposite(section,
                CategoriesComposite.POLICIES, dram);
        section.setClient(categoriesComposite);

        // Set up a focus listener for maintaining global actions.
        categoriesComposite.getTreeViewer().getControl().
                addFocusListener(new FocusListener() {
                    public void focusGained(FocusEvent event) {
                        IActionBars actionBars = DeviceDefinitionPoliciesSection.this.
                                context.getActionBars();
                        origDelete = actionBars.
                                getGlobalActionHandler(IWorkbenchActionConstants.DELETE);
                        actionBars.setGlobalActionHandler(IWorkbenchActionConstants.DELETE,
                                deletePolicyAction);
                        actionBars.updateActionBars();
                    }

                    public void focusLost(FocusEvent event) {
                        IActionBars actionBars = DeviceDefinitionPoliciesSection.this.
                                context.getActionBars();
                        actionBars.setGlobalActionHandler(IWorkbenchActionConstants.DELETE,
                                origDelete);
                        actionBars.updateActionBars();
                    }
                });

        // We use the selection manager for event handling because it allows
        // use to use filters which we need for resolving category elements
        // for use with ODOMAction enablement.
        categoriesComposite.addSelectionChangedListener(context.
                getODOMSelectionManager());

        data = new GridData(GridData.FILL_BOTH);
        categoriesComposite.setLayoutData(data);

        createActions();
        createContextMenu();

        createButtons(this, SWT.NONE);
    }

    /**
     * Create the buttons that allow users to invoke actions on this
     * section.
     */
    private void createButtons(Composite parent, int style) {
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        buttonComposite.setLayout(layout);
        buttonComposite.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        new ActionButton(buttonComposite, style, newPolicyAction);
        new ActionButton(buttonComposite, style, deletePolicyAction);
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // todo implement this method
        return false;
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        // New policy
        ODOMActionCommand command = new ODOMActionCommand() {
            public boolean enable(ODOMActionDetails details) {
                Element selectedCategory = categoriesComposite.
                        getSelectedCategoryElement();
                boolean enabled = selectedCategory != null;
                if (enabled) {
                    // New policy is always enabled in admin mode when there
                    // is a selection.
                    enabled = context.isAdminProject();
                    if (!enabled) {
                        String categoryName = selectedCategory.
                                getAttributeValue(DeviceRepositorySchemaConstants.
                                CATEGORY_NAME_ATTRIBUTE);
                        enabled = categoryName.equals(
                                DeviceRepositorySchemaConstants.CUSTOM_CATEGORY_NAME);
                    }
                }
                return enabled;
            }

            public void run(ODOMActionDetails details) {
                NewDevicePolicyWizard wizard =
                        new NewDevicePolicyWizard(getShell(),
                                context.getDeviceRepositoryAccessorManager());
                wizard.open();
                String policyName = wizard.getPolicyName();
                // If the custom category is selected then prefix the policy
                // name with the custom prefix to enable it to be identified
                // as a custom policy.
                if (categoriesComposite.
                        getSelectedCategoryElement().
                        getAttributeValue(DeviceRepositorySchemaConstants.
                        CATEGORY_NAME_ATTRIBUTE).
                        equals(DeviceRepositorySchemaConstants.
                        CUSTOM_CATEGORY_NAME)) {
                    StringBuffer buffer =
                            new StringBuffer(EclipseDeviceRepository.
                            getCustomPolicyNamePrefix());
                    buffer.append(policyName);
                    policyName = buffer.toString();
                }
                PolicyTypeComposition composition =
                        wizard.getPolicyTypeComposition();
                PolicyType type = wizard.getPolicyType();
                if (policyName != null && composition != null && type != null) {
                    // we don't allow policy creation to
                    // be undoable
                    try {
                        context.getUndoRedoManager().enable(false);
                        addNewPolicyToRepository(policyName, composition, type);
                    } finally {
                        context.getUndoRedoManager().enable(true);
                    }
                    selectNewPolicy(policyName);
                }
            }
        };

        newPolicyAction = new ODOMAction(command,
                context,
                null,
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "newPolicy.");

        // Delete policy
        command = new ODOMActionCommand() {
            public boolean enable(ODOMActionDetails details) {
                boolean enabled = categoriesComposite.getSelectedPolicyElement()
                        != null;

                if (enabled) {
                    // New policy is always enabled in admin mode when there
                    // is a selection.
                    enabled = context.isAdminProject();
                    if (!enabled) {
                        String categoryName = categoriesComposite.
                                getSelectedCategoryElement().
                                getAttributeValue(DeviceRepositorySchemaConstants.
                                CATEGORY_NAME_ATTRIBUTE);
                        enabled = categoryName.equals(
                                DeviceRepositorySchemaConstants.CUSTOM_CATEGORY_NAME);
                    }
                }
                return enabled;
            }

            public void run(ODOMActionDetails details) {
                final String selectedPolicy = details.getElement(0).
                        getAttributeValue(DeviceRepositorySchemaConstants.
                        POLICY_NAME_ATTRIBUTE);
                final String dialogMessage =
                        POLICY_DELETION_DIALOG_MESSAGE_FORMAT.format(
                                new Object[]{selectedPolicy});

                // Get the shell for the dialog.
                final Shell shell =
                        categoriesComposite.getShell();

                // Create the confirmation dialog for the policy deletion.
                // The Yes button is at index 0 and has focus. There is no
                // image.
                final MessageDialog dialog = new MessageDialog(
                        shell,
                        POLICY_DELETION_DIALOG_TITLE,
                        null,
                        dialogMessage,
                        MessageDialog.INFORMATION, new String[]{
                            POLICY_DELETION_DIALOG_YES_TEXT,
                            POLICY_DELETION_DIALOG_NO_TEXT},
                        0);
                // Open the dialog.
                dialog.open();

                // Only delete the policy if the user has confirmed the action
                // by pressing the Yes button at index 0.
                if (dialog.getReturnCode() == 0) {
                    // Delete takes a while so use a BusyIndicator.
                    BusyIndicator.showWhile(shell.getDisplay(),
                            new Runnable() {
                                public void run() {
                                    // BusyIndicator should display a busy
                                    // cursor automatically, but for some
                                    // reason does not do so here (it works
                                    // elsewhere within MCS). Experimentation
                                    // showed that manually setting a busy
                                    // cursor with Display.asyncExec or
                                    // Display.syncExec still did not produce
                                    // a busy cursor. Strangely, the only
                                    // combination that did work was manually
                                    // setting the busy cursor with
                                    // BusyIndicator.showWhile!
                                    // Also, NOT using any of Display.syncExec,
                                    // Display.asyncExec and BusyIndicator,
                                    // but running the code "as is" with manual
                                    // setting of the cursor also did NOT work.
                                    // @todo This is odd. BusyIndicator is broken?
                                    final Cursor busyCursor =
                                            new Cursor(shell.getDisplay(),
                                                    SWT.CURSOR_WAIT);

                                    shell.setCursor(busyCursor);
                                    try {
                                        // we don't allow policy deletions to
                                        // be undoable
                                        context.getUndoRedoManager().enable(false);
                                        String policyName = categoriesComposite.
                                                getSelectedPolicyElement().
                                                getAttributeValue(
                                                        DeviceRepositorySchemaConstants.
                                                POLICY_NAME_ATTRIBUTE);
                                        context.getDeviceRepositoryAccessorManager().
                                                cleansePolicy(policyName);
                                    } catch (RepositoryException e) {
                                        EclipseCommonPlugin.handleError(ABPlugin.
                                                getDefault(), e);
                                    } finally {
                                        // ensure the undo redo manager is enabled.
                                        context.getUndoRedoManager().enable(true);

                                        // Restore the shell's default cursor
                                        // and dispose of the busy cursor
                                        // resources.
                                        shell.setCursor(null);
                                    }
                                }
                            });
                }
            }
        };

        deletePolicyAction = new ODOMAction(command,
                context,
                null,
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "deletePolicy.");
    }

    /**
     * Creates the context menu that is associated with the tree.
     * <p><strong>The {@link #createActions} method must have been invoked
     * prior to this method</strong></p>
     */
    private void createContextMenu() {
        // Create menu manager.
        MenuManager menuManager = new MenuManager();
        menuManager.add(newPolicyAction);
        menuManager.add(new Separator());
        menuManager.add(deletePolicyAction);

        // Create the menu and add it to the tree.
        final Tree tree = categoriesComposite.getTreeViewer().getTree();
        Menu menu = menuManager.createContextMenu(tree);
        tree.setMenu(menu);
    }

    /**
     * Add a new policy to the device repository in use. This will add the
     * policy to the definitions document and to the master device file.
     * @param policyName the name of the new policy. Cannot be null.
     * @param composition the PolicyTypeComposition of the new policy
     * @param type the PolicyType of the new policy
     * @throws IllegalArgumentException if the named policy already exists or
     * if any of the arguments are null.
     */
    private void addNewPolicyToRepository(String policyName,
                                          PolicyTypeComposition composition,
                                          PolicyType type) {
        if (policyName == null) {
            throw new IllegalArgumentException("Cannot be null: " + policyName);
        }
        if (composition == null) {
            throw new IllegalArgumentException("Cannot be null: " + composition);
        }
        if (type == null) {
            throw new IllegalArgumentException("Cannot be null: " + type);
        }
        String masterDeviceName = context.
                getDeviceRepositoryAccessorManager().retrieveRootDeviceName();
        boolean policyExists = context.getDeviceRepositoryAccessorManager().
                retrievePolicy(masterDeviceName, policyName) != null;

        if (policyExists) {
            throw new IllegalArgumentException("Policy " + policyName +
                    " already exists. Aborting new policy.");
        }

        // Add the policy to the definitions document
        Element category = categoriesComposite.getSelectedCategoryElement();
        Element policy = context.getODOMFactory().
                element(DeviceRepositorySchemaConstants.
                POLICY_ELEMENT_NAME, category.getNamespace());
        policy.setAttribute(DeviceRepositorySchemaConstants.
                POLICY_NAME_ATTRIBUTE,
                policyName);
        category.addContent(policy);
        composition.addTypeElement(policy, type,
                context.getODOMFactory());

        // Add the policy to the master device
        Element masterDevice = context.
                getDeviceRepositoryAccessorManager().
                retrieveDeviceElement(masterDeviceName);

        StringBuffer xPathBuffer = new StringBuffer();
        xPathBuffer.append("//").//$NON-NLS-1$
                append(MCSNamespace.DEVICE.getPrefix()).
                append(':').
                append(DeviceRepositorySchemaConstants.
                POLICIES_ELEMENT_NAME);
        XPath policiesXPath = new XPath(xPathBuffer.toString(),
                new Namespace[]{MCSNamespace.DEVICE});

        try {
            Element policies = policiesXPath.
                    selectSingleElement(masterDevice);
            composition.addDefaultPolicyValue(policies, policyName, type,
                    context.getODOMFactory(),
                    context.getDeviceRepositoryAccessorManager());
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(),
                    e);
        }
    }

    /**
     * Get the name of the selected policy, if any, from this section.
     * @return the selected policy name or null if no policy is selected.
     */
    public Element getSelectedPolicyElement() {
        return categoriesComposite.getSelectedPolicyElement();
    }

    /**
     * Get the name of the selected category. If a policy is selected then
     * this method will return the name of the category that the policy
     * belongs to.
     * @return the selected category name or null if no category or policy
     * is selected.
     */
    public String getSelectedCategoryName() {
        Element category = categoriesComposite.getSelectedCategoryElement();
        return (category == null)
                ? null : category.getAttributeValue(
                        DeviceRepositorySchemaConstants.
                CATEGORY_NAME_ATTRIBUTE);
    }

    /**
     * Get the name of the selected policy, if any, from this section.
     * @return the selected policy name or null if no policy is selected.
     */
    public String getSelectedPolicy() {
        Element element = categoriesComposite.getSelectedPolicyElement();
        return element == null ? null : element.getAttributeValue(
                DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE);
    }

    /**
     * Add a SelectionChange listener that is notified when policy selection
     * changes.
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        categoriesComposite.addSelectionChangedListener(listener);
    }

    /**
     * Remove a SelectionChange listener that is notified when policy selection
     * changes.
     */
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        categoriesComposite.removeSelectionChangedListener(listener);
    }

    /**
     * Select's the new named policy element in the tree.
     * @param newPolicyName the name of the new policy to select.
     */
    private void selectNewPolicy(final String newPolicyName) {
        // Create the XPath for selecting the new policy element just created
        // from the definitions document.
        final StringBuffer newPolicyXPathBuffer = new StringBuffer();
        newPolicyXPathBuffer.append("//").
                append(MCSNamespace.DEVICE_DEFINITIONS.getPrefix()).
                append(':').
                append(DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME).
                append("[@").
                append(DeviceRepositorySchemaConstants.POLICY_NAME_ATTRIBUTE).
                append("=\"").append(newPolicyName).
                append("\"]");
        final XPath newPolicyXPath = new XPath(newPolicyXPathBuffer.toString(),
                new Namespace[]{MCSNamespace.DEVICE_DEFINITIONS});

        Element newPolicyElement = null;
        try {
            // Get the definitions root for the XPath search.
            final Element definitionsRoot = context.
                    getDeviceRepositoryAccessorManager().
                    getDeviceDefinitionsDocument().getRootElement();
            // Retrieve the new policy element.
            newPolicyElement =
                    newPolicyXPath.selectSingleElement(definitionsRoot);
        } catch (XPathException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        TreeViewer treeViewer = categoriesComposite.getTreeViewer();

        // Expand the tree to the new policy element's level and select it.
        treeViewer.expandToLevel(newPolicyElement, 1);
        treeViewer.setSelection(
                new StructuredSelection(newPolicyElement), true);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/2	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 16-Nov-04	4394/4	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 29-Oct-04	6046/1	allan	VBM:2004102804 Prevent UpdateClient from clobbering custom policies

 29-Oct-04	6029/1	allan	VBM:2004102804 Prevent UpdateClient from clobbering custom policies

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 26-May-04	4401/14	pcameron	VBM:2004051405 Fixed merge problems

 26-May-04	4401/11	pcameron	VBM:2004051405 Fixing merge issues

 17-May-04	4401/4	pcameron	VBM:2004051405 Automatic selection of newly-created devices and policies

 17-May-04	4401/2	pcameron	VBM:2004051405 Automatic selection of newly-created devices and policies

 26-May-04	4442/6	pcameron	VBM:2004042608 Fixing merge issues

 17-May-04	4442/3	pcameron	VBM:2004042608 Usages of constants from DeviceRepositorySchemaConstants in XMLDeviceRepositoryAccessor

 19-May-04	4468/12	pcameron	VBM:2004050704 Policy addition and removal context menu and confirmation dialog

 19-May-04	4468/10	pcameron	VBM:2004050704 Policy addition and removal context menu and confirmation dialog

 14-May-04	4384/1	pcameron	VBM:2004050703 NewDevicePolicyWizard checks for duplicate policy names

 12-May-04	4288/1	doug	VBM:2004051107 Ensured RHS of Structure page is disabled for non admin user

 10-May-04	4239/2	allan	VBM:2004042207 SaveAs on DeviceEditor.

 10-May-04	4237/3	byron	VBM:2004031601 Provide the CCPP form section

 10-May-04	4235/3	pcameron	VBM:2004031603 Added MasterValueSection

 10-May-04	4068/3	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 ===========================================================================
*/
