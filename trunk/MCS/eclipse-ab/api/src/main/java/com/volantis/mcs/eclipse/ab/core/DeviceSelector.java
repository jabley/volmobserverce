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

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.DeviceHierarchyLabelProvider;
import com.volantis.mcs.eclipse.ab.editors.dom.ElementChildrenTreeContentProvider;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import com.volantis.mcs.eclipse.controls.ControlsPlugin;
import com.volantis.mcs.eclipse.controls.TextButton;
import com.volantis.mcs.eclipse.validation.SelectionValidator;
import com.volantis.mcs.eclipse.validation.Validated;
import com.volantis.mcs.eclipse.validation.ValidationMessageBuilder;
import com.volantis.mcs.eclipse.validation.ValidationStatus;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ProjectDeviceRepositoryProvider;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.utilities.FaultTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.jdom.Element;

/**
 * A control that allows the user to select a device. This controller provides
 * a browser from which to select a specific device from a hierarchy of devices.
 */
public class DeviceSelector
        extends TextButton
        implements Validated {

    /**
     * The resource key prefix.
     */
    private static final String SESSION_PROPERTY_PREFIX = "DeviceSelector."; //$NON-NLS-1$
    private static final String KEY_PREFIX_MESSAGE = "DeviceSelector.message."; //$NON-NLS-1$
    private static final String KEY_PREFIX_EMPTY_LIST = "DeviceSelector.emptyList."; //$NON-NLS-1$
    private static final String KEY_PREFIX_TITLE = "DeviceSelector.title."; //$NON-NLS-1$

    /**
     * The project provider for this DeviceSelector.
     */
    private final ProjectProvider projectProvider;

    /**
     * The device tree content provider.
     */
    private final ITreeContentProvider contentProvider =
            new ElementChildrenTreeContentProvider();

    /**
     * Label provider that provides the view of each item in the tree.
     */
    ILabelProvider labelProvider = new DeviceHierarchyLabelProvider();

    /**
     * The supplementary format args array for the validator.
     */
    private String supplementaryArgs [] = new String[1];

    /**
     * Mapping between fault types understood by this page and message keys in
     * the Wizards properties.
     */
    private static final HashMap MESSAGE_KEY_MAPPINGS;

    /**
     * The element tree selection dialog.
     */
    private ElementTreeSelectionDialog dialog;

    /**
     * Indicates whether the dialog allows multiple selections or only single
     * selections.
     */
    private boolean allowMultiple;

    static {
        MESSAGE_KEY_MAPPINGS = new HashMap(1);
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.NOT_IN_SELECTION,
                "DeviceSelector.invalidDeviceName"); //$NON-NLS-1$
        MESSAGE_KEY_MAPPINGS.put(FaultTypes.CANNOT_BE_NULL,
                "DeviceSelector.valueIsMandatory"); //$NON-NLS-1$
    }

    /**
     * Construct this object with the parent and specified style.
     *
     * @param parent      the parent composite object.
     * @param style       the style. To ensure that only single selections are
     *                    permitted the style should include {@link
     *                    SWT#SINGLE}.
     * @param project     the project used to store the intitial selection.
     * @throws com.volantis.mcs.repository.RepositoryException if the content provider could not be
     *                             created.
     */
    public DeviceSelector(Composite parent,
                          int style,
                          final IProject project)
            throws RepositoryException {
        this(parent, style, new ProjectProvider() {
            public IProject getProject() {
                return project;
            }
        });

    }

    /**
     * Construct this object with the parent and specified style.
     *
     * @param parent The parent composite object.
     * @param style  The style. To ensure that only single selections are
     * permitted the style should include {@link org.eclipse.swt.SWT#SINGLE}.
     * @param projectProvider The projectProvider used to get the project that
     * is used to store the intitial selection.
     * @throws com.volantis.mcs.repository.RepositoryException If the content provider could not be
     * created.
     */
    public DeviceSelector(Composite parent,
                          int style,
                          ProjectProvider projectProvider)
            throws RepositoryException {
        super(parent, style);

        allowMultiple = ((style & SWT.SINGLE) == 0);

        this.projectProvider = projectProvider;

        getButton().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                int result = doSelectionDialog();
                if (result == ElementTreeSelectionDialog.OK) {
                    updateSelection();
                }
            }
        });
    }

    /**
     * Create the validator associated with this selector.
     */
    private SelectionValidator createValidator() {

        List selection = new ArrayList();
        try {
            DeviceRepositoryAccessorManager dram =
                    ProjectDeviceRepositoryProvider.getSingleton().
                    getDeviceRepositoryAccessorManager(projectProvider.
                    getProject());
            final String parentNode = dram.
                    getDeviceHierarchyDocument().getRootElement().getName();
            buildSelectionList(selection, contentProvider, parentNode);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        return new SelectionValidator(selection);
    }

    /**
     * Recursively build the selection list.
     *
     * @param list       the list to add the nodes too.
     * @param provider   the content provider.
     * @param parentNode the node to use as the parent
     */
    private void buildSelectionList(List list,
                                    ITreeContentProvider provider,
                                    String parentNode) {
        Object[] children = provider.getChildren(parentNode);
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                String child = (String) children[i];
                list.add(child);
                buildSelectionList(list, provider, child);
            }
        }
    }

    /**
     * Get the resource identified by the prefix.
     *
     * @param prefix the prefix to identify the resource.
     * @return the resource identified by the prefix.
     */
    protected String getResource(final String prefix) {
        return ControlsMessages.getString(prefix + "device"); //$NON-NLS-1$
    }

    /**
     * Update the text control with the device just selected.
     */
    private void updateSelection() {
        Element element = (Element) dialog.getFirstResult();
        if (element != null) {
            getText().setText(element.getAttributeValue(
                    XMLAccessorConstants.DEVICE_NAME_ATTRIBUTE));
            try {
                IProject project = projectProvider.getProject();

                project.setSessionProperty(createQualifiedName(),
                        element); //$NON-NLS-1$
            } catch (CoreException e) {
                EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
            }
        }
    }

    /**
     * Do the selection using a selection dialog.
     */
    private int doSelectionDialog() {

        dialog = new ElementTreeSelectionDialog(
                this.getShell(), labelProvider, contentProvider);

        try {
            DeviceRepositoryAccessorManager dram =
                    ProjectDeviceRepositoryProvider.getSingleton().
                    getDeviceRepositoryAccessorManager(projectProvider.
                    getProject());
            dialog.setInput(dram.getDeviceHierarchyDocument().getRootElement());
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        dialog.setMessage(getResource(KEY_PREFIX_MESSAGE));
        dialog.setTitle(getResource(KEY_PREFIX_TITLE));
        dialog.setEmptyListMessage(getResource(KEY_PREFIX_EMPTY_LIST));
        dialog.setAllowMultiple(allowMultiple);
        updateInitialSelection();
        dialog.setBlockOnOpen(true);
        
        return dialog.open();
    }

    /**
     * Update the initial selection with the last selected item or the item
     * in the text box (the latter taking preference if it is valid).
     */
    private void updateInitialSelection() {
        Element element = null;
        String deviceName = getText().getText();

        try {
            // get the device from the text box.
            DeviceRepositoryAccessorManager dram =
                    ProjectDeviceRepositoryProvider.getSingleton().
                    getDeviceRepositoryAccessorManager(projectProvider.
                    getProject());
            if ((deviceName != null) && (!deviceName.equals("") &&
                    dram.deviceExists(deviceName))) {
                element = dram.getHierarchyDeviceElement(deviceName);
                if (element != null) {
                    dialog.setInitialSelection(element);
                }
            } else {   // try the previous selection
                IProject project = projectProvider.getProject();
                Object initialSelection = project.getSessionProperty(
                        createQualifiedName());
                if (initialSelection != null) {
                    dialog.setInitialSelection(initialSelection);
                }
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (RepositoryException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

    }

    /**
     * Create a QualifiedName that will be uniquely associated with this
     * class.
     * @return A QualifiedName for this class.
     */
    private QualifiedName createQualifiedName() {
        return new QualifiedName(getClass().toString(),
                SESSION_PROPERTY_PREFIX);
    }

    // javadoc inherited
    public ValidationStatus validate() {
        SelectionValidator validator = createValidator();
        supplementaryArgs[0] = getText().getText();
        return validator.validate(getValue(),
                new ValidationMessageBuilder(
                        ControlsMessages.getResourceBundle(),
                        MESSAGE_KEY_MAPPINGS,
                        supplementaryArgs));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 14-Jan-05	6681/1	allan	VBM:2004081607 Allow device selectors and browser to see project device repository changes

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 01-Sep-04	5363/1	allan	VBM:2004081705 Fix initial selection in DeviceSelector - port from 3.2.2

 01-Sep-04	5351/3	allan	VBM:2004081705 Fix initial selection in DeviceSelector

 01-Sep-04	5351/1	allan	VBM:2004081705 Fix initial selection in DeviceSelector

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 24-May-04	4231/6	tom	VBM:2004042704 Fixedup the 2004032606 change

 18-May-04	4455/4	matthew	VBM:2004051708 change DeviceSelector to use Elements rather then Strings

 17-May-04	4455/2	matthew	VBM:2004051708 change DeviceSelector to use Elements rather then Strings

 29-Apr-04	4072/3	matthew	VBM:2004042601 Sorting of device hierarchy views removed

 29-Apr-04	4072/1	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 09-Feb-04	2913/1	philws	VBM:2004020801 Change device selection box to single selection

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Dec-03	2237/1	byron	VBM:2003112804 Provide an MCS project builder

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 15-Nov-03	1825/2	byron	VBM:2003092601 Create generic policy property composite

 05-Nov-03	1803/1	byron	VBM:2003102901 Device Asset creation wizard page

 03-Nov-03	1587/10	byron	VBM:2003101503 Create the Device Selector Tree View - fixed merge issues

 31-Oct-03	1587/8	byron	VBM:2003101503 Create the Device Selector Tree View

 23-Oct-03	1587/4	byron	VBM:2003101503 Create the Device Selector Tree View

 ===========================================================================
*/
