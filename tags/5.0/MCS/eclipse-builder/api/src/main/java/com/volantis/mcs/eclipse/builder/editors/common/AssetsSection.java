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
import com.volantis.mcs.eclipse.builder.common.DiagnosticsToolTipper;
import com.volantis.mcs.eclipse.builder.common.InteractionFocussable;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.policies.PolicyEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.controls.ActionButton;
import com.volantis.mcs.eclipse.controls.TableItemContainer;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.model.path.IndexedStep;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PropertyStep;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.themes.PropertyValue;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Form section for displaying and adding to a list of variants.
 */
public class AssetsSection extends FormSection implements InteractionFocussable {
    private static final String RESOURCE_PREFIX = "AssetsSection.";

    /**
     * The maximum number of buttons to be displayed on a single line.
     */
    private static final int BUTTON_COUNT = 10;

    private EditorContext context;

    private Action newAction;
    private Action addAction;
    private Action deleteAction;
    private Action cutAction;
    private Action copyAction;
    private Action pasteAction;
    private Action selectAllAction;

    private TableViewer viewer;
    
    public AssetsSection(Composite composite, int i, EditorContext context) {
        super(composite, i);
        this.context = context;
        createDisplayArea();
    }

    private BuilderSelectionListener variantSelectionListener =
            new BuilderSelectionListener() {
                public void selectionMade(BuilderSelectionEvent event) {
                    boolean noChange = false;
                    IStructuredSelection selection =
                            (IStructuredSelection) viewer.getSelection();
                    if (selection.size() == 1) {
                        BeanProxy currentSelection =
                                (BeanProxy) selection.getFirstElement();
                        Proxy newSelection = (Proxy) event.getSelection();

                        if (currentSelection.equals(newSelection)) {
                            noChange = true;
                        }
                    } else if (selection.size() == 0) {
                        if (event.getSelection() == null) {
                            noChange = true;
                        }
                    }

                    if (!noChange) {
                        IStructuredSelection newSelection = null;
                        if (event.getSelection() == null) {
                            newSelection = StructuredSelection.EMPTY;
                        } else {
                            newSelection = new StructuredSelection(
                                    event.getSelection());
                        }
                        viewer.setSelection(newSelection);
                    }
                }
            };

    protected ILabelProvider createLabelProvider(
            ILabelProvider originalProvider, ILabelDecorator decorator) {
        DecoratingLabelProvider provider =
                new DecoratingLabelProvider(originalProvider, decorator);

        return provider;
    }

    protected EditorContext getContext() {
        return context;
    }

    private void createDisplayArea() {
        GridLayout layout = new GridLayout(1, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        String description = EditorMessages.getString(RESOURCE_PREFIX +
                "subheading");

        Section section =
                createSection(this, EditorMessages.getString(RESOURCE_PREFIX +
                "title"), description, Section.EXPANDED);
        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);

        Composite displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);
        GridLayout displayAreaLayout = new GridLayout(1, false);
        displayAreaLayout.marginHeight = 0;
        displayAreaLayout.marginWidth = 0;
        displayArea.setLayout(displayAreaLayout);
        data = new GridData(GridData.FILL_BOTH);
        displayArea.setLayoutData(data);
        setDefaultColour(displayArea);

        // Create a Composite just to get a black border around the table.
        final Composite tableComposite = new Composite(displayArea, SWT.NONE);
        GridLayout tableCompositeLayout = new GridLayout(1, false);
        tableCompositeLayout.marginHeight = 1;
        tableCompositeLayout.marginWidth = 1;
        tableComposite.setLayout(tableCompositeLayout);
        data = new GridData(GridData.FILL_BOTH);
        tableComposite.setLayoutData(data);
        tableComposite.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_BLACK));

        Table table = new Table(tableComposite, SWT.SINGLE | SWT.H_SCROLL |
                SWT.V_SCROLL);
        data = new GridData(GridData.FILL_BOTH);
        table.setLayoutData(data);
        int tableMinHeight = EditorMessages.getInteger(RESOURCE_PREFIX +
                "tableMinHeight").intValue();
        table.setSize(table.getSize().x, tableMinHeight);

        viewer = new TableViewer(table);
        viewer.setContentProvider(new ListProxyContentProvider());

        VariantType defaultVariantType =
                ((PolicyEditorContext) context).getDefaultVariantType();

        VariantProxyLabelProvider provider = new VariantProxyLabelProvider(
                defaultVariantType);
        ProxyLabelDecorator decorator = new ProxyLabelDecorator();
        ILabelProvider dlp = createLabelProvider(provider, decorator);

        viewer.setLabelProvider(dlp);
        BeanProxy policy = (BeanProxy) context.getInteractionModel();

        if (policy != null) {
            ListProxy deviceThemes = (ListProxy)
                    policy.getPropertyProxy(PolicyModel.VARIANTS);
            viewer.setInput(deviceThemes);
        }

        createActions();
        createButtons(displayArea);
        createDiagnosticToolTipper();

        // Refresh the viewer when the model changes - this is deep because the
        // labels for the assets can be changed by any change within the asset.
        // Also refreshes the viewer if the read-only state changes, as this
        // will require changing of the lock icon in collaborative mode.
        context.getInteractionModel().addListener(
                new InteractionEventListenerAdapter() {
                    protected void interactionEvent(InteractionEvent event) {
                        if (event.isOriginator ()) {
                            viewer.refresh();
                        }
                    }

                    public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                        if (event.isOriginator()) {
                            viewer.refresh();
                        }
                    }
                }, true);

        viewer.getControl().setMenu(createMenu());

        // Notify the context when a new selection is made
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                if (context instanceof PolicyEditorContext) {
                    IStructuredSelection selection =
                            (IStructuredSelection) event.getSelection();
                    ((PolicyEditorContext) context).setSelectedVariant(
                            (BeanProxy) selection.getFirstElement());
                    readWriteStateChanged();
                }
            }
        });

        // Successful lock/unlock is one of the reasons the read only state
        // could change - update the lock buttons when this happens
        new SelectedVariantMonitor((PolicyEditorContext) context) {
            // Javadoc inherited
            public void readOnlyStateChanged(ReadOnlyStateChangedEvent event) {
                if (event.isOriginator ()) {
                    readWriteStateChanged();
                }
            }
        };
        readWriteStateChanged();

        ((PolicyEditorContext) context).addSelectedVariantListener(
                variantSelectionListener);
    }

    /**
     * Creates a tooltip provider to allow errors to be displayed as tooltips.
     */
    private void createDiagnosticToolTipper() {
        new DiagnosticsToolTipper(new TableItemContainer(viewer.getTable()),
                new DiagnosticsToolTipper.ItemMapper() {
                    public Proxy dataFromItem(Item item) {
                        Proxy proxy = null;
                        if (item instanceof TableItem) {
                            Object proxyObject = item.getData();
                            if (proxyObject instanceof Proxy &&
                                    ((Proxy) proxyObject).getModelObject()
                                    instanceof PropertyValue) {
                                proxy = (Proxy) proxyObject;
                            }
                        }
                        return proxy;
                    }
                });
    }

    /**
     * Constructs the popup menu for this section.
     * @return
     */
    private Menu createMenu() {
        Menu menu = new Menu(getShell(), SWT.POP_UP);
        addMenuItem(menu, newAction);
        addMenuItem(menu, addAction);
        new MenuItem(menu, SWT.SEPARATOR);
        addMenuItem(menu, cutAction);
        addMenuItem(menu, copyAction);
        addMenuItem(menu, pasteAction);
        new MenuItem(menu, SWT.SEPARATOR);
        addMenuItem(menu, deleteAction);
        // We do not currently support multiple selection, so the select all
        // action was having no effect. This needs to be added in the future,
        // so the code for adding the action and the action itself are kept in
        // the code.
        // TODO later Add select all menu item when multiple selection is supported
        // new MenuItem(menu, SWT.SEPARATOR);
        // addMenuItem(menu, selectAllAction);
        return menu;
    }

    /**
     * Add an action to a menu, using the action's display text as the menu
     * text and triggering the action on selection of the menu item.
     *
     * @param menu The menu to modify
     * @param action The action to add to the menu
     * @return The created menu item
     */
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

    public void refresh() {
        viewer.refresh();
    }

    /**
     * Creates the actions that are used by this form section.
     */
    protected void createActions() {
        ActionFactory actionFactory = ActionFactory.getDefaultInstance();
        addAction = actionFactory.createAddVariantAction(context);
        deleteAction = actionFactory.createDeleteVariantAction(context, viewer, getShell());
        newAction = actionFactory.createNewVariantAction(context, this);
        copyAction = actionFactory.createCopyVariantAction(context, this, viewer);
        cutAction = actionFactory.createCutVariantAction(context, copyAction, deleteAction);
        pasteAction = actionFactory.createPasteVariantAction(context, this);
        selectAllAction = actionFactory.createSelectAllAction(viewer);
    }

    private void createButtons(Composite parent) {
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(BUTTON_COUNT, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing =
                EditorMessages.getInteger(RESOURCE_PREFIX +
                "horizontalSpacing").intValue();
        buttonComposite.setLayout(layout);
        setDefaultColour(buttonComposite);
        createActionButtons(buttonComposite);
    }

    protected void createActionButtons(Composite buttonComposite) {
        // There is only a Browse button if a new variant can be created
        if (newAction != null) {
            new ActionButton(buttonComposite, SWT.PUSH, newAction);
        }

        // There may be no addAction.
        if (addAction != null) {
            new ActionButton(buttonComposite, SWT.PUSH, addAction);
        }

        if (deleteAction != null) {
            new ActionButton(buttonComposite, SWT.PUSH, deleteAction);
        }
    }

    // Javadoc inherited
    public void setFocus(Path path) {
        boolean seekingVariants = true;
        int stepCount = path.getStepCount();
        for (int i = 0; i < stepCount && seekingVariants; i++) {
            Step step = path.getStep(i);
            if (step instanceof PropertyStep) {
                PropertyStep property = (PropertyStep) step;
                if (PolicyModel.VARIANTS.getName().equals(
                        property.getProperty())) {
                    if ((i + 1) < stepCount) {
                        Step variantIndexStep = path.getStep(i + 1);
                        if (variantIndexStep instanceof IndexedStep) {
                            int variantIndex =
                                    ((IndexedStep) variantIndexStep).getIndex();
                            ISelection newSelection = new StructuredSelection(
                                    viewer.getElementAt(variantIndex));
                            viewer.setSelection(newSelection, true);
                        }
                    }
                    seekingVariants = false;
                }
            }
        }
    }

    /**
     * Carry out any updates required because of a change to the read/write
     * state.
     */
    public void readWriteStateChanged() {
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/3	adrianj	VBM:2005111601 Add style rule view

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/3	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/2	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10290/1	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 11-Nov-05	10266/1	adrianj	VBM:2005101806 Fix for ObjectDialogListBuilder under Windows

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/1	pduffin	VBM:2005101811 Committing restructuring

 31-Oct-05	9886/3	adrianj	VBM:2005101811 New themes GUI

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
