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
package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.common.TargetTransfer;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyModelSet;
import com.volantis.mcs.eclipse.controls.ActionButton;
import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.image.GenericImageSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.CategoryReference;
import com.volantis.mcs.policies.variants.selection.DefaultSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.DeviceReference;
import com.volantis.mcs.policies.variants.selection.EncodingSelectionBuilder;
import com.volantis.mcs.policies.variants.selection.InternalTarget;
import com.volantis.mcs.policies.variants.selection.SelectionBuilder;
import com.volantis.mcs.policies.variants.selection.TargetedSelectionBuilder;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Section containing the selection criteria. This displays the selection
 * criteria (if there are multiple values allowed) and the targets.
 */
public class AssetSelectionCriteriaSection extends AssetEditorSection {
    /**
     * Prefix for property resources associated with this GUI component.
     */
    private static final String RESOURCE_PREFIX = "AssetSelectionCriteriaSection.";

    /**
     * Factory for creating policy objects.
     */
    private static final PolicyFactory POLICY_FACTORY =
            PolicyFactory.getDefaultInstance();

    /**
     * A content provider for retrieving content from a collection.
     */
    private static final IStructuredContentProvider LIST_CONTENT_PROVIDER = new ArrayContentProvider();

    /**
     * Editor context for the policy being modified.
     */
    private PolicyEditorContext context;

    /**
     * A container for the targets that enforces a logical order on them for
     * display purposes.
     */
    private Set targets = new TreeSet(new TargetComparator());

    /**
     * Viewer for the targets.
     */
    private TableViewer targetViewer;

    /**
     * Flag to indicate whether events are being handled.
     */
    private boolean handlingEvents = true;

    private ComboViewer criteriaCombo;

    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action deleteAction;
    private Action selectAllAction;

    /**
     * Flag indicating whether the section suports targeted selection.
     *
     * @todo In theory all variant types should support targeted selection so this is unnecessary. However, at the moment chart still does not so until it does this will have to stay.
     */
    private boolean supportsTargetedSelection;

    /**
     * Flag indicating whether the section suports generic image selection.
     */
    private boolean supportsGenericImageSelection;

    /**
     * Array of supported selection types.
     */
    private Class[] supportedSelectionTypes;

    /**
     * A list of the controls that need to be modified when the enabled state
     * is changed.
     */
    private List enableableControls = new ArrayList();

    /**
     * A list of the controls that are only relevant for targeted selection.
     */
    private List targetControls = new ArrayList();

    private InteractionEventListener readOnlyListener =
            new InteractionEventListenerAdapter() {
                public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                    if (event.isOriginator ()) {
                        setEnabled(!event.isReadOnly());
                    }
                }
            };

    private PropertiesComposite composite;

    public AssetSelectionCriteriaSection(Composite parent, int style,
                                         EditorContext context) {
        super(parent, style, RESOURCE_PREFIX);
        this.context = (PolicyEditorContext) context;

        PolicyModelSet modelSet = PolicyModelSet.getModelSet(this.context.getPolicyType());
        supportedSelectionTypes = modelSet.getSelectionClasses();
        supportsTargetedSelection = false;
        supportsGenericImageSelection = false;
        for (int i = 0; i < supportedSelectionTypes.length; i++) {
            if (TargetedSelectionBuilder.class == supportedSelectionTypes[i]) {
                supportsTargetedSelection = true;
            } else if (GenericImageSelectionBuilder.class == supportedSelectionTypes[i]) {
                supportsGenericImageSelection = true;
            }
        }

        createDisplayArea();

        this.context.addSelectedVariantListener(new BuilderSelectionListener() {
            public void selectionMade(BuilderSelectionEvent event) {
                selectedVariantChanged(event);
            }
        });

        // Initialise the metadata section with the currently selected variant
        selectedVariantChanged(null);
    }

    private void createDisplayArea() {
        final Composite displayArea = createDefaultDisplayArea();

        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);

        // Create the PropertiesComposite.
        composite = new PropertiesComposite(displayArea, SWT.NORMAL, context);

        GridData data = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(data);

        addCriteriaCombo(composite);

        if (supportsTargetedSelection) {
            addTargetsList(composite);
        }

        if (supportsGenericImageSelection) {
            addGenericImageSelectionComposite(composite);
        }

        composite.addPropertiesCompositeChangeListener(
                new PropertiesCompositeChangeListener() {
                    public void propertyChanged(PropertiesComposite composite,
                                                PropertyDescriptor property,
                                                Object newValue) {
                        selectionPropertyChanged(property, newValue);
                    }
                });

        // Set the color and enabled state of the properties composite after
        // all children have been added.
        setDefaultColour(composite);

        // Eclipse v3.1.x and later require the composite to be enabled (and
        // therefore in the list of enableable controls) in order for the child
        // controls to be considered to be enabled.
        enableableControls.add(composite);
    }

    private static final String SELECTION_TARGETED =
            EditorMessages.getString(RESOURCE_PREFIX + "selection.targeted");
    
    private static final String SELECTION_DEFAULT =
            EditorMessages.getString(RESOURCE_PREFIX + "selection.default");

    private static final String SELECTION_GENERIC =
            EditorMessages.getString(RESOURCE_PREFIX + "selection.generic");

    private static final String SELECTION_ENCODING =
            EditorMessages.getString(RESOURCE_PREFIX + "selection.encoding");

    private static final LabelProvider SELECTION_LABEL_PROVIDER =
            new LabelProvider() {
                public String getText(Object o) {
                    String label = "";
                    if (o == TargetedSelectionBuilder.class) {
                        label = SELECTION_TARGETED;
                    } else if (o == DefaultSelectionBuilder.class) {
                        label = SELECTION_DEFAULT;
                    } else if (o == GenericImageSelectionBuilder.class) {
                        label = SELECTION_GENERIC;
                    } else if (o == EncodingSelectionBuilder.class) {
                        label = SELECTION_ENCODING;
                    }
                    return label;
                }
            };
    
    private void addCriteriaCombo(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(EditorMessages.getString(
                RESOURCE_PREFIX + "criteria.label"));
        setDefaultColour(label);
        enableableControls.add(label);

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        criteriaCombo = new ComboViewer(parent, SWT.BORDER | SWT.READ_ONLY);
        enableableControls.add(criteriaCombo.getCombo());

        data = new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        criteriaCombo.getControl().setLayoutData(data);
        setDefaultColour(criteriaCombo.getCombo());
        
        criteriaCombo.setContentProvider(new ArrayContentProvider());

        criteriaCombo.setLabelProvider(SELECTION_LABEL_PROVIDER);

        PolicyModelSet modelSet = PolicyModelSet.getModelSet(context.getPolicyType());
        Class[] selectionClasses = modelSet.getSelectionClasses();
        criteriaCombo.setInput(selectionClasses);
        criteriaCombo.setSelection(StructuredSelection.EMPTY);

        criteriaCombo.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if (handlingEvents) {
                    setModelSelection();
                }
            }
        });
    }

    /**
     * Add the list of selected targets to the GUI.
     *
     * @param parent The parent component for the list. Must have a 2-column
     *               grid layout.
     */
    private void addTargetsList(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "targets.label"));
        setDefaultColour(label);
        enableableControls.add(label);
        targetControls.add(label);

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        label.setLayoutData(data);

        Composite targetsComposite = new Composite(parent, SWT.NONE);
        data = new GridData(GridData.FILL_BOTH);
        targetsComposite.setLayoutData(data);
        setDefaultColour(targetsComposite);

        GridLayout layout = new GridLayout(2, false);
        layout.makeColumnsEqualWidth = false;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        targetsComposite.setLayout(layout);

        Table table = new Table(targetsComposite, SWT.BORDER | SWT.MULTI);
        targetViewer = new TableViewer(table);
        targetViewer.setLabelProvider(new TargetLabelProvider());
        targetViewer.setContentProvider(LIST_CONTENT_PROVIDER);
        targetViewer.setInput(targets);
        enableableControls.add(table);
        targetControls.add(table);

        data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        data.heightHint = 100;
        table.setLayoutData(data);

        Composite buttonsComposite = new Composite(targetsComposite, SWT.NONE);
        setDefaultColour(buttonsComposite);
        layout = new GridLayout(1, false);
        buttonsComposite.setLayout(layout);

        data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.VERTICAL_ALIGN_BEGINNING);
        buttonsComposite.setLayoutData(data);

        Button browseButton = new Button(buttonsComposite, SWT.NONE);
        browseButton.setText(EditorMessages.getString(
                RESOURCE_PREFIX + "browse.label"));
        enableableControls.add(browseButton);
        targetControls.add(browseButton);

        data = new GridData(GridData.FILL_HORIZONTAL);
        browseButton.setLayoutData(data);

        createActions();

        // Start with the remove button disabled
        deleteAction.setEnabled(false);
        ActionButton removeButton = new ActionButton(buttonsComposite, SWT.NONE, deleteAction);
        enableableControls.add(removeButton);

        data = new GridData(GridData.FILL_HORIZONTAL);
        removeButton.setLayoutData(data);

        browseButton.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                browseTargets();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                browseTargets();
            }
        });

        targetViewer.getTable().setMenu(createMenu());
    }

    /**
     * Add the fields for the generic image selection criteria.
     *
     * @param propertiesComposite The containing
     */
    private void addGenericImageSelectionComposite(
            PropertiesComposite propertiesComposite) {

        BeanClassDescriptor typeDescriptor = (BeanClassDescriptor)
                PolicyModel.MODEL_DESCRIPTOR.
                getTypeDescriptorStrict(GenericImageSelectionBuilder.class);

        List propertyList = typeDescriptor.getPropertyDescriptors();
        PropertyDescriptor[] properties = new PropertyDescriptor[propertyList.size()];
        for (int i = 0; i < properties.length; i++) {
            properties[i] = (PropertyDescriptor) propertyList.get(i);
        }

        propertiesComposite.addProperties(properties, false, null, null);
    }

    /**
     * Handle changes in selection property.
     *
     * @param descriptor The descriptor of the property that has changed
     * @param newValue The new value of the property
     */
    private void selectionPropertyChanged(PropertyDescriptor descriptor,
                                          Object newValue) {
        if (handlingEvents) {
            PropertyIdentifier identifier = descriptor.getIdentifier();
            Proxy selectionProxy = getSelectionProxy();
            if (selectionProxy instanceof BeanProxy) {
                BeanProxy selectionBeanProxy = (BeanProxy) selectionProxy;
                if (selectionBeanProxy != null &&
                        selectionBeanProxy.getBeanClassDescriptor().
                        getPropertyDescriptor(identifier) != null) {
                    Proxy propertyProxy =
                            selectionBeanProxy.getPropertyProxy(identifier);
                    Operation setValue = propertyProxy.
                            prepareSetModelObjectOperation(newValue);
                    context.executeOperation(setValue);
                }
            }
        }
    }

    /**
     * Display the target selection dialog.
     */
    private void browseTargets() {
        TargetSelectionDialog.editTargetSelection(targets, getShell(), context);
        targetViewer.refresh();
        setPolicyTargets();
    }

    // Javadoc inherited
    public void setFocus(Path path) {
    }

    /**
     * Respond to changes in the selected variant.
     */
    private void selectedVariantChanged(BuilderSelectionEvent event) {
        handlingEvents = false;
        try {
            BeanProxy oldVariant = (BeanProxy) ((event == null) ?
                    null : event.getOldSelection());
            BeanProxy newVariant = context.getSelectedVariant();

            if (oldVariant != null) {
                oldVariant.removeListener(readOnlyListener);
            }

            if (newVariant == null) {
                setEnabledClearIfDisabled(false);
            } else {
                Proxy selection = ((BaseProxy)
                        newVariant.getPropertyProxy(PolicyModel.SELECTION))
                        .getConcreteProxy();

                SelectionBuilder builder = null;
                if (selection == null) {
                    composite.clear();
                } else {
                    builder = (SelectionBuilder) selection.getModelObject();
                    if (builder instanceof GenericImageSelectionBuilder) {
                        composite.updateFromProxy((BeanProxy) selection);
                    }
                }

                // Enable/disable all controls
                setEnabled(!newVariant.isReadOnly());

                // Set the selection and alter the enabled state of the target
                setDisplaySelection(builder);

                newVariant.addListener(readOnlyListener, false);
            }
        } finally {
            handlingEvents = true;
        }
    }

    // Javadoc inherited
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        Iterator it = enableableControls.iterator();
        while (it.hasNext()) {
            Control control = (Control) it.next();
            control.setEnabled(enabled);
        }
    }

    /**
     * Set the enabled state of this composite.
     *
     * <p>If this composite is disabled then all the controls contained within
     * will be cleared.</p>
     *
     * @param enabled The enabled state.
     */
    private void setEnabledClearIfDisabled(boolean enabled) {
        setEnabled(enabled);
        if (!enabled) {
            criteriaCombo.setSelection(StructuredSelection.EMPTY);
            clearTargetedSelection();
            composite.clear();
        }
    }

    /**
     * Gets the selection proxy for the currently selected variant.
     *
     * @return The selection proxy, or null if none exists
     */
    private Proxy getSelectionProxy() {
        Proxy selectionProxy = null;
        BeanProxy selectedVariant = context.getSelectedVariant();
        if (selectedVariant != null) {
            BaseProxy selectionBase = (BaseProxy) selectedVariant.getPropertyProxy(PolicyModel.SELECTION);
            if (selectionBase != null) {
                selectionProxy = selectionBase.getConcreteProxy();
                // If we have no concrete metadata, create an empty one of the
                // appropriate type
                if (selectionProxy == null) {
                    SelectionBuilder selection = getDefaultSelectionBuilder();
                    selectionBase.setModelObject(selection);

                    // Now that we've set a concrete model object, we should
                    // have a concrete proxy.
                    selectionProxy = selectionBase.getConcreteProxy();
                }
            }
        }
        return selectionProxy;
    }

    /**
     * Returns an empty instance of the default selection type for this policy.
     *
     * @return An empty instance of the default selection type for this policy,
     *         or null if one could not be created
     */
    private SelectionBuilder getDefaultSelectionBuilder() {
        SelectionBuilder selectionBuilder = null;
        selectionBuilder = POLICY_FACTORY.createTargetedSelectionBuilder();

        return selectionBuilder;
    }

    /**
     * Sets the policy targets from those defined in the target list.
     */
    private void setPolicyTargets() {
        List categoryTargets = new ArrayList();
        List deviceTargets = new ArrayList();

        // Split the targets into device/category
        Iterator it = targets.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof DeviceReference) {
                deviceTargets.add(next);
            } else if (next instanceof CategoryReference) {
                categoryTargets.add(next);
            }
        }

        BeanProxy targetedSelection = (BeanProxy) getSelectionProxy();
        ListProxy categories = (ListProxy) targetedSelection.getPropertyProxy(PolicyModel.CATEGORY_REFERENCES);
        Operation setCategories = categories.prepareSetModelObjectOperation(categoryTargets);
        ListProxy devices = (ListProxy) targetedSelection.getPropertyProxy(PolicyModel.DEVICE_REFERENCES);
        Operation setDevices = devices.prepareSetModelObjectOperation(deviceTargets);

        // TODO later Should be merged into a compound operation for future undo implementation
        context.executeOperation(setCategories);
        context.executeOperation(setDevices);
    }

    private void createActions() {
        deleteAction = new Action() {
            public void run() {
                IStructuredSelection selection = (IStructuredSelection) targetViewer.getSelection();
                Object[] selected = selection.toArray();
                for (int i = 0; i < selected.length; i++) {
                    targets.remove(selected[i]);
                }
                targetViewer.refresh();
                setPolicyTargets();
            }
        };
        deleteAction.setText("Delete");

        cutAction = new Action() {
            /**
             * A cut is a combination of copy and paste - carry out the cut
             * by invoking the copy and paste actions.
             */
            public void run() {
                copyAction.run();
                pasteAction.run();
            }
        };
        cutAction.setText("Cut");

        copyAction = new Action() {
            public void run() {
                Clipboard clipboard = new Clipboard(getDisplay());
                TargetTransfer transfer = TargetTransfer.getInstance();
                IStructuredSelection selection = (IStructuredSelection) targetViewer.getSelection();
                InternalTarget[] modelArray = new InternalTarget[selection.size()];
                modelArray = (InternalTarget[]) selection.toList().toArray(modelArray);
                clipboard.setContents(new Object[]{modelArray},
                        new Transfer[]{transfer});
                clipboard.dispose();
            }
        };
        copyAction.setText("Copy");

        pasteAction = new Action() {
            public void run() {
                TargetTransfer transfer = TargetTransfer.getInstance();
                Clipboard clipboard = new Clipboard(getDisplay());

                try {
                    Object[] contents = (Object[]) clipboard.getContents(transfer);

                    if (contents != null && contents.length > 0) {
                        for (int i = 0; i < contents.length; i++) {
                            Object newTarget = contents[i];
                            if (newTarget instanceof InternalTarget) {
                                targets.add(newTarget);
                            }
                        }
                        setPolicyTargets();
                        targetViewer.refresh();
                    }
                } finally {
                    clipboard.dispose();
                }
            }
        };
        pasteAction.setText("Paste");

        selectAllAction = new Action() {
            public void run() {
                targetViewer.getTable().selectAll();
            }
        };
        selectAllAction.setText("Select All");
    }

    private Menu createMenu() {
        Menu menu = new Menu(getShell(), SWT.POP_UP);
        addMenuItem(menu, cutAction);
        addMenuItem(menu, copyAction);
        addMenuItem(menu, pasteAction);
        new MenuItem(menu, SWT.SEPARATOR);
        addMenuItem(menu, deleteAction);
        new MenuItem(menu, SWT.SEPARATOR);
        addMenuItem(menu, selectAllAction);
        return menu;
    }

    private MenuItem addMenuItem(Menu menu, final Action action) {
        MenuItem item = new MenuItem(menu, SWT.PUSH);
        item.setText(action.getText());
        item.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event e) {
                action.run();
            }
        });
        return item;
    }

    private void setDisplaySelection(SelectionBuilder selection) {

        if (selection == null) {
            setTargetSectionEnabled(false);
            setGenericSectionEnabled(false);
            criteriaCombo.setSelection(StructuredSelection.EMPTY);
        } else if (supportsTargetedSelection &&
                selection instanceof TargetedSelectionBuilder) {
            criteriaCombo.setSelection(new StructuredSelection(TargetedSelectionBuilder.class));
            TargetedSelectionBuilder targetted = (TargetedSelectionBuilder) selection;
            targets.clear();
            List categoryReferences = targetted.getModifiableCategoryReferences();
            List deviceReferences = targetted.getModifiableDeviceReferences();
            targets.addAll(categoryReferences);
            targets.addAll(deviceReferences);
            targetViewer.refresh();
            setTargetSectionEnabled(true);
            setGenericSectionEnabled(false);
        } else if (supportsGenericImageSelection &&
                selection instanceof GenericImageSelectionBuilder) {
            criteriaCombo.setSelection(new StructuredSelection(GenericImageSelectionBuilder.class));
            setTargetSectionEnabled(false);
            setGenericSectionEnabled(true);
        } else {
            setTargetSectionEnabled(false);
            setGenericSectionEnabled(false);

            Class selectionClass = null;
            for (int i = 0; selectionClass == null &&
                    i < supportedSelectionTypes.length; i++) {

                if (supportedSelectionTypes[i].isInstance(selection)) {
                    selectionClass = supportedSelectionTypes[i];
                }
            }

            if (selectionClass == null) {
                criteriaCombo.setSelection(StructuredSelection.EMPTY);
            } else {
                criteriaCombo.setSelection(new StructuredSelection(selectionClass));
            }
        }
    }

    private void setModelSelection() {
        Class selectionType = (Class)
                ((IStructuredSelection) criteriaCombo.getSelection()).
                getFirstElement();
        Object model = null;
        if (selectionType == DefaultSelectionBuilder.class) {
            model = POLICY_FACTORY.createDefaultSelectionBuilder();
        } else if (selectionType == GenericImageSelectionBuilder.class) {
            model = POLICY_FACTORY.createGenericImageSelectionBuilder();
        } else if (selectionType == TargetedSelectionBuilder.class) {
            model = POLICY_FACTORY.createTargetedSelectionBuilder();
        } else if (selectionType == EncodingSelectionBuilder.class) {
            model = POLICY_FACTORY.createEncodingSelectionBuilder();
        }

        context.getSelectedVariant().getPropertyProxy(PolicyModel.SELECTION).
                setModelObject(model);

        if (selectionType == TargetedSelectionBuilder.class) {
            setTargetSectionEnabled(true);
            setGenericSectionEnabled(false);
            setPolicyTargets();
        } else if (selectionType == GenericImageSelectionBuilder.class) {
            setTargetSectionEnabled(false);
            setGenericSectionEnabled(true);
        } else {
            setTargetSectionEnabled(false);
            setGenericSectionEnabled(false);
        }
    }

    private void clearTargetedSelection() {
        if (supportsTargetedSelection) {
            targets.clear();
            targetViewer.refresh();
        }
    }

    /**
     * Enable or disable all target controls.
     *
     * <p>If target controls are disabled then they are cleared.</p>
     *
     * @param enabled / disabled flag
     */
    private void setTargetSectionEnabled(boolean enabled) {
        if (supportsTargetedSelection) {
            Iterator it = targetControls.iterator();
            while (it.hasNext()) {
                Control control = (Control) it.next();
                control.setEnabled(enabled);
            }

            // Enable/disable the delete button via its action
            deleteAction.setEnabled(enabled);

            if (!enabled) {
                clearTargetedSelection();
            }
        }
    }

    /**
     * Enable of disable all generic controls.
     *
     * <p>If generic controls are disabled then they are cleared.</p>
     *
     * @param enabled / disable flag.
     */
    private void setGenericSectionEnabled(boolean enabled) {
        if (supportsGenericImageSelection) {
            composite.setPropertyEnabled(PolicyModel.WIDTH_HINT, enabled);
        }
    }
}
