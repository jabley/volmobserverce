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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.devices.validation.DeviceLocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.ElementChildrenTreeContentProvider;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMLabelDecorator;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetails;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.LocationDetailsRegistry;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.MarkerGeneratingErrorReporter;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.ValidationListener;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.FilteringTreeContentProvider;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.eclipse.controls.ActionButton;
import com.volantis.mcs.eclipse.controls.NodeSelectionDialog;
import com.volantis.mcs.eclipse.controls.TreeItemContainer;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.validation.CharacterSetValidator;
import com.volantis.mcs.eclipse.validation.CharacterValidator;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import org.apache.regexp.RE;
import org.apache.regexp.RESyntaxException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.output.XMLOutputter;
import org.xml.sax.SAXException;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A form section that displays the device hierarchy in a tree format, allowing
 * devices to be created, deleted and moved.
 */
public class DeviceHierarchySection
        extends FormSection implements XPathFocusable {

    /**
     * The prefix for resource messages associated with this class.
     */
    private static final String RESOURCE_PREFIX =
            "DeviceHierarchySection.";

    /**
     * The default minimum width for device hierarchy sections.
     */
    private static final int DEFAULT_MIN_WIDTH = DevicesMessages.getInteger(
            RESOURCE_PREFIX + "minWidth").intValue();

    /**
     * Constant used as the title for the Move Device dialog
     */
    private static final String SELECT_DEVICE_TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "selectDevice.title");

    /**
     * Constant used as the prompt in the Move Device dialog
     */
    private static final String SELECT_DEVICE_MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "selectDevice.message");

    /**
     * Constant used as the title in the delete device message dialog
     */
    private static final String DELETE_DEVICE_TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "deleteDevice.title");

    /**
     * Constant used as the message in the delete device message dialog
     */
    private static final String DELETE_DEVICE_MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "deleteDevice.message");
    
    /**
     * Constant used as the title in the rename device input dialog
     */
    private static final String RENAME_DEVICE_TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX + "renameDevice.title");

    /**
     * Constant used as the message in the rename device input dialog
     */
    private static final String RENAME_DEVICE_MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "renameDevice.message");

    /**
     * Constant used for the error message indicating that the new name for a
     * renamed device is empty.
     */
    private static final String RENAME_DEVICE_ERROR_EMPTY =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "renameDevice.error.empty");

    /**
     * Constant used for the error message indicating that the new name for a
     * renamed device is unchanged.
     */
    private static final String RENAME_DEVICE_ERROR_UNCHANGED =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "renameDevice.error.unchanged");

    /**
     * Constant used for the error message indicating that the new name for a
     * renamed device already exists in the repository.
     */
    private static final String RENAME_DEVICE_ERROR_EXISTS =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "renameDevice.error.exists");

    /**
     * Constant used for the error message indicating that the new name for a
     * renamed device contains illegal characters.
     */
    private static final String RENAME_DEVICE_ERROR_INVALID =
            DevicesMessages.getString(RESOURCE_PREFIX +
            "renameDevice.error.invalid");

    /**
     * Constant used for the error message indicating that the new name for a
     * renamed device is too long.
     */
    private static final String RENAME_DEVICE_ERROR_LENGTH =
            MessageFormat.format(DevicesMessages.getString(RESOURCE_PREFIX +
            "renameDevice.error.length"), new Object[] {
                String.valueOf(DeviceConstants.TEXT_MAX_LENGTH) });

    /**
     * The CharacterSetValidator for the device name.
     */
    private static CharacterSetValidator DEVICE_NAME_VALIDATOR;

    /**
     * The maximum length for text values.
     */
    private static final int TEXT_MAX_LENGTH = 20;

    /**
     * Initialize the regular expression.
     */
    static {
        // Create and initialize the regular expression.
        try {
            final RE DEVICE_NAME_REGULAR_EXPRESSION = new RE("[A-Za-z0-9\\-_@.]+");

            // Create and initialize the character validator.
            CharacterValidator CHARACTER_VALIDATOR = new CharacterValidator() {
                public boolean isValidChar(char c) {
                    String charString = (new Character(c)).toString();
                    return DEVICE_NAME_REGULAR_EXPRESSION.match(charString);
                }
            };

            // Create and initialize the character set validator.
            DEVICE_NAME_VALIDATOR = new CharacterSetValidator(CHARACTER_VALIDATOR);
            DEVICE_NAME_VALIDATOR.setMaxChars(TEXT_MAX_LENGTH);
            DEVICE_NAME_VALIDATOR.setMinChars(1);
        } catch (RESyntaxException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

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
     * The hierarchy trees label provider
     */
    private final ILabelProvider labelProvider =
            new DeviceHierarchyLabelProvider();

    /**
     * The content provider that the hierarchy tree uses.
     */
    private static final ITreeContentProvider CONTENT_PROVIDER =
            new ElementChildrenTreeContentProvider();

    /**
     * Create a filter so that only device elements are included in
     * the selection.
     */
    private static final ODOMSelectionFilter DEVICE_FILTER =
            new ODOMSelectionFilter(null, new String[]{
                DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                    new ODOMSelectionFilterConfiguration(true, true));

    /**
     * The error location message format.
     */
    private static final MessageFormat ERROR_LOCATION_FORMAT =
            new MessageFormat(DevicesMessages.getString(RESOURCE_PREFIX +
            "locationDetails"));

    /**
     * The original delete action associated with the editors global menu.
     * This will be replaced with this section delete action whislt this
     * section is active
     */
    private IAction originalDelete;

    /**
     * Action that displays a wizard to allow a new device to be created
     */
    private Action newAction;

    /**
     * Action that removes the currently selected device
     */
    private Action deleteAction;

    /**
     * Action that allows a device to be moved to a different location in the
     * hierarchy
     */
    private Action moveAction;

    /**
     * Action that allows a device to be renamed.
     */
    private Action renameAction;

    /**
     * Action that will open a device xml document in a viewer. For
     * admin mode only.
     */
    private Action viewDeviceXMLAction;

    /**
     * Action that will open the identification xml document in a viewer. For
     * admin mode only.
     */
    private Action viewIdentificationXMLAction;

    /**
     * Action that will open the definitions xml document in a viewer. For
     * admin mode only.
     */
    private Action viewDefinitionsXMLAction;

    /**
     * Action that will open the tac xml document in a viewer. For
     * admin mode only.
     */
    private Action viewTACXMLAction;

    /**
     * The display area for this form section
     */
    private Composite displayArea;

    /**
     * The <code>DeviceEditorContext</code> required by this editor part
     */
    private final DeviceEditorContext context;

    /**
     * Used to display the device hierarchy tree.
     */
    private TreeViewer treeViewer;

    /**
     * The MenuManager that is associated with the hierarchys context
     * sensitive menu
     */
    private MenuManager menuManager;

    /**
     * Initializes a <code>DeviceHierarchySection</code> with the given
     * arguments
     * @param parent the parent composite
     * @param style style bitset
     * @param context the device editor context
     */
    public DeviceHierarchySection(Composite parent,
                                  int style,
                                  final DeviceEditorContext context) {
        super(parent, style);
        setMinWidth(DEFAULT_MIN_WIDTH);
        this.context = context;

        // ensure the context has the correct root element
        Document hierarchyDocument =
                context.getDeviceRepositoryAccessorManager().
                getDeviceHierarchyDocument();

        try {
            ODOMElement hierarchyRootElement =
                    (ODOMElement) hierarchyDocument.getRootElement();

            context.addRootElement(hierarchyRootElement,
                    DeviceRepositorySchemaConstants.HIERARCHY_ELEMENT_NAME);

            MarkerGeneratingErrorReporter errorReporter =
                    context.getErrorReporter(hierarchyRootElement);

            LocationDetailsRegistry registry =
                    errorReporter.getLocationDetailsRegistry();
            if (registry == null) {
                registry = new LocationDetailsRegistry();
                errorReporter.setLocationDetailsRegistry(registry);
            }

            DefaultJDOMFactory factory = new DefaultJDOMFactory();

            Element element = factory.element(DeviceRepositorySchemaConstants.
                    DEVICE_ELEMENT_NAME,
                    MCSNamespace.DEVICE_HIERARCHY);

            LocationDetails details =
                    new DeviceLocationDetails(ERROR_LOCATION_FORMAT,
                            MCSNamespace.DEVICE_HIERARCHY);

            registry.registerLocationDetails(element, details);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (ParserErrorException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        // create this sections display area
        createDisplayArea(TITLE, MESSAGE);

        final ODOMSelectionManager selectionManager =
                context.getODOMSelectionManager();

        // create a listener that responds to ODOMElement selections
        ODOMElementSelectionListener deviceSelectionListener =
                new ODOMElementSelectionListener() {
                    // javadoc inherited
                    public void selectionChanged(
                            ODOMElementSelectionEvent event) {
                        treeViewer.removeSelectionChangedListener(
                                selectionManager);
                        treeViewer.setSelection(event.getSelection(), true);
                        treeViewer.getTree().showSelection();
                        treeViewer.addSelectionChangedListener(
                                selectionManager);

                        ODOMElement selected = getSelectedDevice();
                        boolean enable = (selected != null &&
                                !isRootDevice(selected) &&
                                (context.isAdminProject() ||
                                !context.getDeviceRepositoryAccessorManager().
                                isStandardDevice(getDeviceName(selected))));

                        // Disable the delete and move actions if there
                        // is no selection or the master device is selected or
                        // if the device is a standard device and the project
                        // is not in admin mode.
                        deleteAction.setEnabled(enable);
                        moveAction.setEnabled(enable);
                        renameAction.setEnabled(enable);

                        // The new action is always enabled if there is a
                        // selection.
                        newAction.setEnabled(selected != null);
                        viewDeviceXMLAction.setEnabled(selected != null);

                    }
                };
        // register the ODOM selection listener with the contexts selection
        // manager
        selectionManager.addSelectionListener(deviceSelectionListener,
                DEVICE_FILTER);

        // we want to update the tree whenever a hierarchy element is modified
        ODOMChangeListener rootElementListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                // simply refresh the tree to reflect any change
                treeViewer.refresh();
            }
        };
        // register the change listener with the context so that we are
        // notified whenever any of the contexts root elements are updated
        context.addChangeListener(rootElementListener);

        // Also want to update the tree whenever validation occurs as a
        // node may require decorating
        ValidationListener validationListener = new ValidationListener() {
            public void validated() {
                if (!treeViewer.getControl().isDisposed()) {
                    treeViewer.getControl().getDisplay().asyncExec(new Runnable() {
                        public void run() {
                            // ensure that the widget still exists when this
                            // runnable is run
                            if (!treeViewer.getControl().isDisposed()) {
                                treeViewer.refresh();
                            }
                        }
                    });
                }
            }
        };
        context.addValidationsListener(validationListener);
    }

    /**
     * Creates the display area for this form seciton
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, SWT.NONE, title, message);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);

        GridLayout layout = new GridLayout();
        data = new GridData(GridData.FILL_BOTH);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);
        displayArea.setLayoutData(data);

        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // the order in which the display areas components are created is
        // important. DO NOT change the order in which these methods are
        // invoked
        createHierarchyTree();
        createActions();
        createContextMenu();
        createButtons();
    }

    /**
     * Create the tree that allows navigation of the device hierarchy.
     */
    private void createHierarchyTree() {
        Tree tree = new Tree(displayArea,
                SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL |
                SWT.BORDER);

        // ensure that the tree has a white background
        tree.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));
        // add a focus listener to the tree that updates the active editors
        // global actions
        tree.addFocusListener(new FocusListener() {
            // javadoc inherited
            public void focusGained(FocusEvent event) {
                setupGlobalActions();
            }

            // javadoc inherited
            public void focusLost(FocusEvent event) {
                restoreGlobalActions();
            }
        });

        // create the viewer for the tree
        treeViewer = new TreeViewer(tree);
        treeViewer.setContentProvider(CONTENT_PROVIDER);

        ODOMLabelDecorator labelDecorator = new ODOMLabelDecorator(
                context.getPolicyResource(),
                new DeviceProblemMarkerFinder(
                        context.getDeviceRepositoryAccessorManager()),
                new TreeItemContainer(tree));

        treeViewer.setLabelProvider(new DecoratingLabelProvider(
                labelProvider, labelDecorator));

        treeViewer.setInput(context.getDeviceRepositoryAccessorManager().
                getDeviceHierarchyDocument().getRootElement());
        treeViewer.expandToLevel(2);

        // hook up the contexts ODOMSelectionManager with the tree so that
        // other editor parts or components can obtain the selected device
        treeViewer.addSelectionChangedListener(
                context.getODOMSelectionManager());
    }

    /**
     * An ApplicationWindow for displaying the XML for the selected device.
     */
    class XMLWindow extends ApplicationWindow {

        /**
         * The XMLOutputter for formatting and viewing the XML.
         */
        private XMLOutputter outputter;

        /**
         * The Element whose XML to show in the window.
         */
        private Element element;

        /**
         * The title of the window.
         */
        private String title;

        /**
         * Construct a new XMLWindow.
         * @param shell the parent shell.
         */
        XMLWindow(Shell shell, Element element, String title) {
            super(shell);
            outputter = new XMLOutputter("    ");
            outputter.setNewlines(true);
            outputter.setTrimAllWhite(true);
            this.element = element;
            this.title = title;
        }

        // javadoc inherited
        protected void configureShell(Shell shell) {
            shell.setText(title);
            shell.setLayout(new FillLayout());
        }

        // javadoc inherited
        protected Control createContents(Composite parent) {
            Composite container = new Composite(parent, SWT.NULL);
            container.setLayout(new FillLayout());
            container.setBackground(getShell().getDisplay().
                    getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            Text text = new Text(container, SWT.MULTI | SWT.READ_ONLY |
                    SWT.H_SCROLL | SWT.V_SCROLL);
            String xml = outputter.outputString(element);
            text.setText(xml);

            return container;
        }

        // javadoc inherited
        public void create() {
            super.create();
            int y = getShell().getSize().y;
            y = y > 500 ? 500 : y;
            getShell().setSize(500, y);
        }
    }

    /**
     * Creates the actions needed by this form
     */
    private void createActions() {
        // create the view device xml action
        viewDeviceXMLAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "viewDevice.") {

            protected void runImpl() {
            DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
            String selectedDeviceName = getDeviceName(getSelectedDevice());
                XMLWindow window = new XMLWindow(getShell(),
                        dram.retrieveDeviceElement(selectedDeviceName),
                        selectedDeviceName + ".xml");
                window.open();
            }
        };

        // create the view identification xml action
        viewIdentificationXMLAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "viewIdentification.") {

            protected void runImpl() {
            DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
                XMLWindow window = new XMLWindow(getShell(),
                        dram.getDeviceIdentificationDocument().
                        getRootElement(), "identification.xml");
                window.open();
            }
        };


        // create the view definitions xml action
        viewDefinitionsXMLAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "viewDefinitions.") {

            protected void runImpl() {
            DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
                XMLWindow window = new XMLWindow(getShell(),
                        dram.getDeviceDefinitionsDocument().
                        getRootElement(), "definitions.xml (combined)");
                window.open();
            }
        };

        // create the view definitions xml action
        viewTACXMLAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "viewTAC.") {

            protected void runImpl() {
            DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
                XMLWindow window = new XMLWindow(getShell(),
                        dram.getDeviceTACIdentificationDocument().
                        getRootElement(), "tac.xml");
                window.open();
            }
        };

        // create the new action
        newAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "new.") {
            // javadoc inherited
            protected void runImpl() {
                final DeviceRepositoryAccessorManager dram = context.
                        getDeviceRepositoryAccessorManager();
                NewDeviceWizard wizard = new NewDeviceWizard(
                        getShell(),
                        CONTENT_PROVIDER,
                        treeViewer.getSelection(),
                        dram.getDeviceHierarchyDocument().getRootElement(),
                        context);

                int result = wizard.open();
                if (result == Window.OK) {
                    String fallbackDevice = wizard.getFallbackDevice();
                    String newDeviceName = wizard.getDevice();

                    if ((fallbackDevice != null) && (newDeviceName != null)) {
                        try {
                            // Create the new device element.
                            dram.createDevice(fallbackDevice, newDeviceName);

                            // Create the XPath for selecting the new device
                            // element just created from the hierarchy
                            // document.
                            final StringBuffer newDeviceXPathBuffer =
                                    new StringBuffer();
                            newDeviceXPathBuffer.append("//").
                                    append(MCSNamespace.DEVICE_HIERARCHY.
                                    getPrefix()).
                                    append(':').
                                    append(DeviceRepositorySchemaConstants.
                                    DEVICE_ELEMENT_NAME).
                                    append("[@").
                                    append(DeviceRepositorySchemaConstants.
                                    DEVICE_NAME_ATTRIBUTE).
                                    append("=\"").append(newDeviceName).
                                    append("\"]");
                            final XPath newDeviceXPath =
                                    new XPath(newDeviceXPathBuffer.toString(),
                                            new Namespace[]{MCSNamespace.
                                    DEVICE_HIERARCHY});

                            Element newDeviceElement = null;
                            try {
                                // Get the hierarchy root for the XPath search.
                                final Element hierarchyRoot = dram.
                                        getDeviceHierarchyDocument().
                                        getRootElement();

                                // Retrieve the new device element.
                                newDeviceElement = newDeviceXPath.
                                        selectSingleElement(hierarchyRoot);
                            } catch (XPathException e) {
                                EclipseCommonPlugin.
                                        handleError(ABPlugin.getDefault(), e);
                            }

                            // Expand the tree to the new device element's level
                            // and select it.
                            treeViewer.expandToLevel(newDeviceElement, 1);
                            treeViewer.setSelection(
                                    new StructuredSelection(newDeviceElement),
                                    true);
                        } catch (RepositoryException e) {
                            EclipseCommonPlugin.handleError(ABPlugin.getDefault(),
                                    e);
                            throw new UndeclaredThrowableException(e);
                        }
                    }
                }
            }
        };

        renameAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "rename.") {
            // javadoc inherited
            protected void runImpl() {
                final ODOMElement selected = getSelectedDevice();
                String oldName = getDeviceName(selected);

                DeviceRepositoryAccessorManager dram =
                        context.getDeviceRepositoryAccessorManager();
                String newName = getNewName(oldName, dram, false);
                if (newName != null) {
                    UndoRedoManager undoRedoManager =
                            context.getUndoRedoManager();
                    try {
                        // we don't allow device renames to be undoable
                        undoRedoManager.enable(false);
                        dram.renameDevice(oldName, newName);
                    } catch (RepositoryException re) {
                        EclipseCommonPlugin.handleError(ABPlugin.getDefault(),
                                re);
                        throw new UndeclaredThrowableException(re);
                    } finally {
                        // re-enable the undo manager
                        undoRedoManager.enable(true);
                    }
                }
            }

            /**
             * Prompts for a new name for the device.
             *
             * @param oldName The old name of the device
             * @param dram The device repository accessor manager for the device
             *             repository being edited
             * @param hasAdminRights True if the user has admin rights, false
             *                       otherwise
             * @return The new name for the device, or null if the request was
             *         cancelled.
             */
            private String getNewName(final String oldName,
                                   final DeviceRepositoryAccessorManager dram,
                                   final boolean hasAdminRights) {
                final RE deviceNameMatch =
                        new RE(DeviceConstants.DEVICE_NAME_REGEXP_STRING);
                IInputValidator validator = new IInputValidator() {
                    public String isValid(String string) {
                        if (!hasAdminRights && !string.startsWith("_")) {
                            string = "_" + string;
                        }

                        if (string == null || "".equals(string)) {
                            return RENAME_DEVICE_ERROR_EMPTY;
                        } else if (string.length() >
                                DeviceConstants.TEXT_MAX_LENGTH) {
                            return RENAME_DEVICE_ERROR_LENGTH;
                        } else if (oldName.equals(string)) {
                            return RENAME_DEVICE_ERROR_UNCHANGED;
                        } else if (dram.deviceExists(string)) {
                            return RENAME_DEVICE_ERROR_EXISTS;
                        } else if (deviceNameMatch.match(string)) {
                            if (deviceNameMatch.getParenLength(0) !=
                                    string.length()) {
                                return RENAME_DEVICE_ERROR_INVALID;
                            }
                        } else {
                            return RENAME_DEVICE_ERROR_INVALID;
                        }

                        return null;
                    }
                };

                InputDialog dialog = new InputDialog(getShell(),
                        RENAME_DEVICE_TITLE, RENAME_DEVICE_MESSAGE,
                        oldName, validator);
                dialog.setBlockOnOpen(true);
                int result = dialog.open();

                // Return the entered name if the dialog was completed
                // successfully.
                String newName = null;
                if (result == Dialog.OK) {
                    newName = dialog.getValue();
                    // Add a leading _ if necessary for non-admin users
                    if (!hasAdminRights && !newName.startsWith("_")) {
                        newName = "_" + newName;
                    }
                }
                return newName;
            }
        };

        // create the move action
        moveAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "move.") {
            // javadoc inherited
            protected void runImpl() {
                // get hold of the node that is to be moved
                final ODOMElement selected = getSelectedDevice();
                // create the dialog that allows the user to select the
                // destination for the "move" device action

                // we need to filter out the selected node from the
                // NodeSelectionDialog as it does not make sense to move the
                // selected device to itself or a descendent of itself.
                // We can do this by wrapping the hierarchy content provider
                // in a FilteringTreeContentProvider that filters out the
                // selected device.
                ITreeContentProvider filteringContentProvider =
                        new FilteringTreeContentProvider(
                                CONTENT_PROVIDER,
                                new FilteringTreeContentProvider.NodeFilter() {
                                    // To store the filtered nodes
                                    List filtered = new ArrayList();

                                    // javadoc inherited
                                    public Object[] filter(Object[] nodes) {
                                        filtered.clear();
                                        // filter out the selected device if
                                        // it is in the list of nodes
                                        for (int i = 0; i < nodes.length; i++) {
                                            if (nodes[i] != selected) {
                                                filtered.add(nodes[i]);
                                            }
                                        }
                                        return filtered.toArray();
                                    }
                                });
                // create the dialog that allows the user to select the new
                // position in the device hierarchy.
                NodeSelectionDialog dialog = new NodeSelectionDialog(
                        displayArea.getShell(),
                        SELECT_DEVICE_MESSAGE,
                        context.getDeviceRepositoryAccessorManager().
                        getDeviceHierarchyDocument().getRootElement(),
                        filteringContentProvider,
                        new DeviceHierarchyLabelProvider());

                // set the title for the dialog
                dialog.setTitle(SELECT_DEVICE_TITLE);
                // display the dialog
                dialog.open();
                // retrieve the selected device
                Object[] result = dialog.getResult();
                // if the result is null then the user pressed the cancel
                // button. However, if it is non null then a selection will
                // have been made and the result array should contain 1
                // ODOMElement - the device that was selected.
                if (result != null) {
                    // check that the result array contains a single ODOMElement
                    if (result.length != 1) {
                        throw new IllegalStateException(
                                "Expected a single device to be selected" +
                                " but got " + result.length);
                    }
                    // cast to an ODOMelement
                    ODOMElement parent = (ODOMElement) result[0];
                    // finally move the selected device to the new location
                    DeviceRepositoryAccessorManager dram =
                            context.getDeviceRepositoryAccessorManager();
                    try {
                        dram.moveDevice(getDeviceName(selected),
                                getDeviceName(parent));
                    } catch (RepositoryException e) {
                        EclipseCommonPlugin.handleError(ABPlugin.getDefault(),
                                e);
                        throw new UndeclaredThrowableException(e);
                    }
                    // ensure that device that has been moved is selected
                    treeViewer.setSelection(new StructuredSelection(selected));
                }
            }
        };

        // create the remove action
        deleteAction = context.new DemarcatingResourceAction(
                DevicesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "delete.") {
            // javadoc inherited
            protected void runImpl() {
                // get hold of the selected device
                ODOMElement selected = getSelectedDevice();
                String message = MessageFormat.format(
                        DELETE_DEVICE_MESSAGE,
                        new String[]{getDeviceName(selected)});

                if (MessageDialog.openQuestion(getShell(),
                        DELETE_DEVICE_TITLE,
                        message)) {
                    DeviceRepositoryAccessorManager dram =
                            context.getDeviceRepositoryAccessorManager();
                    UndoRedoManager undoRedoManager =
                            context.getUndoRedoManager();
                    try {
                        // we don't allow device deletions to be undoable
                        undoRedoManager.enable(false);
                        dram.removeDevice(getDeviceName(selected));
                    } catch (RepositoryException e) {
                        EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                    } finally {
                        // ensure the undo redo manager is enabled.
                        undoRedoManager.enable(true);
                    }

                }
            }
        };

        // Initial enablement settings for actions.
        newAction.setEnabled(false);
        deleteAction.setEnabled(false);
        moveAction.setEnabled(false);
        renameAction.setEnabled(false);
        viewDeviceXMLAction.setEnabled(false);
        viewIdentificationXMLAction.setEnabled(true);
        viewDefinitionsXMLAction.setEnabled(true);
        viewTACXMLAction.setEnabled(true);

    }

    /**
     * Replace some of the editors actions with those specific to this section.
     */
    private void setupGlobalActions() {
        IActionBars actionBars = context.getActionBars();
        // store away the current delete action
        originalDelete = actionBars.getGlobalActionHandler(
                IWorkbenchActionConstants.DELETE);
        // add the delete action for this section
        actionBars.setGlobalActionHandler(IWorkbenchActionConstants.DELETE,
                deleteAction);
        actionBars.updateActionBars();
    }

    /**
     * Restore the original global actions that were replaced in the
     * {@link #setupGlobalActions} method
     */
    private void restoreGlobalActions() {
        IActionBars actionBars = context.getActionBars();
        actionBars.setGlobalActionHandler(IWorkbenchActionConstants.DELETE,
                originalDelete);
        actionBars.updateActionBars();
    }

    /**
     * Creates the buttons needed by this form
     */
    private void createButtons() {
        // create the container that the buttons will belong to
        Composite buttonContainer = new Composite(displayArea, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        buttonContainer.setLayout(layout);
        buttonContainer.setLayoutData(data);

        // set the background color for the container to white
        buttonContainer.setBackground(getDisplay().getSystemColor(
                SWT.COLOR_LIST_BACKGROUND));

        // create the new button
        ActionButton newButton = new ActionButton(buttonContainer,
                SWT.PUSH,
                newAction);
        newButton.setLayoutData(
                new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

        // create the remove button
        ActionButton deleteButton = new ActionButton(buttonContainer,
                SWT.PUSH,
                deleteAction);
        deleteButton.setLayoutData(
                new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
    }

    /**
     * Creates the context menu that is associated with the tree.
     * <p><strong>The {@link #createActions} method must have been invoked
     * prior to this method</strong></p>
     */
    private void createContextMenu() {
        // Create menu manager.Dev
        menuManager = new MenuManager();
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener() {
            // javadoc inherited
            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });
        // Create the menu.
        Menu menu = menuManager.createContextMenu(treeViewer.getControl());
        treeViewer.getTree().setMenu(menu);
    }

    /**
     * Fill the context menu with actions.
     */
    private void fillContextMenu(IMenuManager manager) {
        manager.add(newAction);
        manager.add(new Separator());
        manager.add(deleteAction);
        manager.add(new Separator());
        manager.add(moveAction);
        manager.add(renameAction);
        if (context.isAdminProject()) {
            manager.add(new Separator());
            manager.add(viewDefinitionsXMLAction);
            manager.add(viewDeviceXMLAction);
            manager.add(viewIdentificationXMLAction);
            manager.add(viewTACXMLAction);
        }
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // todo implement this
        return false;
    }

    /**
     * Returns the currently selected device
     * @return the ODOMElement for the currently slected device or null if
     * no device is selected
     */
    private ODOMElement getSelectedDevice() {
        IStructuredSelection selection =
                (IStructuredSelection) treeViewer.getSelection();
        return (selection.isEmpty())
                ? null : (ODOMElement) selection.getFirstElement();
    }

    /**
     * Helper method that takes a device element and returns it's name.
     * Note the element is assumed to be a non null "device" element, no
     * checks are performed to ensure this.
     * @param deviceElement the device ODOMElement
     * @return the name of the device
     */
    private String getDeviceName(ODOMElement deviceElement) {
        // assumes this is a device element with a name attribute.
        return deviceElement.getAttributeValue(
                DeviceRepositorySchemaConstants.DEVICE_NAME_ATTRIBUTE);
    }

    /**
     * Helper method that determines whether the given device element is the
     * root device
     * Note the element is assumed to be a non null "device" element, no
     * checks are performed to ensure this.
     * @param deviceElement the device ODOMElement
     * @return true if and only if the element is the root device element
     */
    private boolean isRootDevice(ODOMElement deviceElement) {
        String rootDeviceName = context.getDeviceRepositoryAccessorManager().
                retrieveRootDeviceName();
        return rootDeviceName.equals(getDeviceName(deviceElement));
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

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	6218/3	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 16-Nov-04	4394/9	allan	VBM:2004051018 Undo/Redo in device editor.

 17-May-04	4394/5	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4394/2	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/4	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 14-May-04	4413/1	doug	VBM:2004051412 Fixed PolicyValueModifier labelling issue
 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 28-Sep-04	5676/1	allan	VBM:2004092302 Fixes to update client ported from v3.2.2

 28-Sep-04	5615/1	allan	VBM:2004092302 UpdateClient fixes and custom device distinction

 08-Sep-04	5449/1	claire	VBM:2004090809 New Build Mechanism: Remove the use of utilities.UndeclaredThrowableException

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 17-May-04	4401/13	pcameron	VBM:2004051405 Automatic selection of newly-created devices and policies

 17-May-04	4401/11	pcameron	VBM:2004051405 Automatic selection of newly-created devices and policies

 17-May-04	4401/8	pcameron	VBM:2004051405 Automatic selection of newly-created devices and policies

 14-May-04	4369/2	allan	VBM:2004051311 Override fixed, widget dispose fix, new button fix.

 13-May-04	4321/4	doug	VBM:2004051202 Added label decorating to the device hierarchy tree

 11-May-04	4272/4	allan	VBM:2004050503 Fixed merge conflict.

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 11-May-04	4161/3	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 05-May-04	4115/5	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/5	doug	VBM:2004032304 Added a PrimaryPatterns form section

 04-May-04	4113/3	doug	VBM:2004042906 Fixed migration problem with the device repository

 29-Apr-04	4072/5	matthew	VBM:2004042601 Sorting of device hierarchy views removed

 29-Apr-04	4072/3	matthew	VBM:2004042601 Improved performance of device hierarchy viewers

 27-Apr-04	4035/1	byron	VBM:2004032403 Create the NewDeviceWizard class

 22-Apr-04	3878/4	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 22-Apr-04	3878/2	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
