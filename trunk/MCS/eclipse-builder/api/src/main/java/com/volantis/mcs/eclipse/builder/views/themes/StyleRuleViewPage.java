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
package com.volantis.mcs.eclipse.builder.views.themes;

import com.volantis.mcs.css.renderer.CSSStyleSheetRenderer;
import com.volantis.mcs.css.renderer.RendererContext;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.common.DiagnosticsToolTipper;
import com.volantis.mcs.eclipse.builder.common.InteractionChangeAdapter;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.eclipse.builder.editors.themes.ThemeEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.controls.TableItemContainer;
import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.ParentProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.event.ProxyEvent;
import com.volantis.mcs.interaction.event.RemovedFromListEvent;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.styling.properties.StyleProperty;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.Page;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class StyleRuleViewPage extends Page {
    private static final int COLUMN_PROPERTY = 0;
    private static final int COLUMN_VALUE = 1;
    private static final int COLUMN_IMPORTANT = 2;
    private static final int COLUMN_ERROR = 3;

    private static final Image ERROR_IMAGE =
            EclipseCommonMessages.getImage(StyleRuleViewPage.class, "images/error.gif");
    private static final Image IMPORTANT_IMAGE =
            EclipseCommonMessages.getImage(StyleRuleViewPage.class, "images/important.gif");

    private static final String RESOURCE_PREFIX = "StyleRuleViewPage.";

    private static final String NO_THEME_MESSAGE =
            EditorMessages.getString(RESOURCE_PREFIX + "noDeviceTheme");

    private ThemeEditorContext editorContext;

    private TableTreeViewer treeViewer;

    private RuleTreeContentProvider contentProvider = new RuleTreeContentProvider();

    private Composite displayArea;

    private StackLayout displayAreaLayout;

    private Composite messagePage;

    private TableTree tree;
    
    private boolean removalListenerAdded = false;

    private BuilderSelectionListener selectionListener =
            new BuilderSelectionListener() {
                // Javadoc inherited
                public void selectionMade(BuilderSelectionEvent event) {
                    if (event.getSelection() instanceof Proxy && !event.getSelection().equals(treeViewer.getInput())) {
                        setInput((Proxy) event.getSelection());
                    }
                }
            };

    // Javadoc inherited
    public void dispose() {
        super.dispose();
        editorContext.removeSelectedVariantListener(selectionListener);
    }

    public StyleRuleViewPage(ThemeEditorContext context) {
        this.editorContext = context;
        editorContext.addSelectedVariantListener(selectionListener);
    }

    // Javadoc inherited
    public void createControl(Composite composite) {
        displayArea = new Composite(composite, SWT.NONE);
        displayAreaLayout = new StackLayout();
        displayArea.setLayout(displayAreaLayout);
        displayArea.setBackground(composite.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        createMessagePage(displayArea);
        createStyleView(displayArea);

        setInput(editorContext.getSelectedVariant());
    }

    private void createMessagePage(Composite parent) {
        messagePage = new Composite(parent, SWT.NONE);
        messagePage.setLayout(new GridLayout(1, false));
        Label label = new Label(messagePage, SWT.NONE);
        label.setText(NO_THEME_MESSAGE);
        GridData data = new GridData(GridData.FILL_BOTH);
        label.setLayoutData(data);
    }

    private void createStyleView(Composite composite) {
        tree = new TableTree(composite, SWT.SINGLE | SWT.BORDER);

        // We need to explicitly set the bounds of the display area to that of
        // its parent, otherwise we get a stack overflow error when running on
        // windows. See vbm 2006051721 on mantis for more details.
        composite.setBounds(composite.getParent().getClientArea());

        final Table table = tree.getTable();
        table.setHeaderVisible(true);

        // This is a remarkably nasty hack to work around the broken nature of
        // SWT tables. All images are forced to the same size in a table, and
        // the tree expansion widget is an image. To get the error icons at the
        // right size we need to ensure that an error icon is inserted into the
        // table before any content creates the expansion widget. This has the
        // side effect of making the expansion widget a more consistent size
        // as well. Once the icon has been added it can be removed immediately.
        TableItem bogus = new TableItem(table, SWT.NONE);
        bogus.setImage(ERROR_IMAGE);
        bogus.dispose();

        final TableColumn property = new TableColumn(table, SWT.NONE);
        final TableColumn value = new TableColumn(table, SWT.NONE);
        final TableColumn important = new TableColumn(table, SWT.NONE);
        final TableColumn errorMarker = new TableColumn(table, SWT.NONE);

        property.setText("Property");
        value.setText("Value");
        value.setResizable(false);
        errorMarker.setWidth(36);
        errorMarker.setResizable(false);
        errorMarker.setText(" ");
        errorMarker.setAlignment(SWT.CENTER);
        important.setWidth(18);
        important.setResizable(false);
        important.setText("!");
        important.setAlignment(SWT.CENTER);
        final TableLayout tableLayout = new TableLayout() {
            // Javadoc inherited
            public void layout(Composite c, boolean flush) {
                super.layout(c, flush);

                // Ensure we are laying out a table.
                if (c instanceof Table) {
                    int tableWidth = c.getClientArea().width;
                    // The image columns are fixed at 18, leaving the rest of
                    // the space to be shared between the (resizable) property
                    // column and the (static) value column. We should try to
                    // honour the size set for the property column where we
                    // can.
                    int accountedWidth = 36 + property.getWidth();
                    // If the final column fits in the available space, trim it
                    // to fit the table. Otherwise give it a small fixed size.
                    if ((accountedWidth + 18) < tableWidth) {
                        value.setWidth(tableWidth - accountedWidth);
                    } else {
                        value.setWidth(18);
                        int propertyWidth = tableWidth - (3 * 18);
                        property.setWidth((propertyWidth > 18) ? propertyWidth : 18);
                    }
                }
            }
        };
        tableLayout.addColumnData(new ColumnWeightData(1, 18, true));
        tableLayout.addColumnData(new ColumnWeightData(1, 18, false));
        tableLayout.addColumnData(new ColumnWeightData(0, 18, false));
        tableLayout.addColumnData(new ColumnWeightData(0, 18, false));

        ControlListener columnResizeListener = new ControlAdapter() {
            // Javadoc inherited
            public void controlResized(ControlEvent event) {
                tableLayout.layout(table, true);
            }
        };

        TreeListener treeExpansionListener = new TreeListener() {
            private void treeChanged() {
                tableLayout.layout(table, true);
            }

            // Javadoc inherited
            public void treeCollapsed(TreeEvent event) {
                treeChanged();
            }

            // Javadoc inherited
            public void treeExpanded(TreeEvent event) {
                treeChanged();
            }
        };

        treeViewer = new TableTreeViewer(tree);
        treeViewer.setLabelProvider(new RuleTreeLabelProvider());
        treeViewer.setContentProvider(contentProvider);

        new DiagnosticsToolTipper(new TableItemContainer(table),
                new DiagnosticsToolTipper.ItemMapper() {
                    public Proxy dataFromItem(Item item) {
                        Proxy proxy = null;
                        if (item instanceof TableItem) {
                            Object tableTreeItem = item.getData("TableTreeItemID");
                            if (tableTreeItem instanceof Item) {
                                Object proxyObject = ((Item) tableTreeItem).getData();
                                if (proxyObject instanceof Proxy && ((Proxy) proxyObject).getModelObject() instanceof PropertyValue) {
                                    proxy = (Proxy) proxyObject;
                                }
                            }
                        }
                        return proxy;
                    }
                });

        table.setLayout(tableLayout);
        property.addControlListener(columnResizeListener);
        tree.addTreeListener(treeExpansionListener);
        tableLayout.layout(table, true);

        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                Proxy proxy = (Proxy) selection.getFirstElement();
                if (proxy != null && proxy.getModelObject() instanceof PropertyValue) {
                    editorContext.getThemeEditor().setFocus(proxy.getPathFromRoot());
                    // Another slightly nasty hack - we can't set the focus to
                    // the appropriate point in the other tab without losing
                    // focus in this view, so we pull it back after setting the
                    // focus on the relevant control
                    getControl().setFocus();
                }
            }
        });
    }

    // Javadoc inherited
    public Control getControl() {
        return displayArea;
    }

    // Javadoc inherited
    public void setFocus() {
    }

    private InteractionChangeAdapter modelChangeListener = new InteractionChangeAdapter() {
        // Javadoc inherited
        public void interactionChangeEvent(ProxyEvent event) {
            contentProvider.populateParentMap();
            treeViewer.refresh();
        }
    };
    
    private InteractionChangeAdapter variantRemovalListener = new InteractionChangeAdapter() {
        // Javadoc inherited
        public void interactionChangeEvent(ProxyEvent event) {
            if (event instanceof RemovedFromListEvent) {
                setInput(null);                
            } 
        }        
    };

    private void setInput(Proxy newInput) {
        if (treeViewer.getInput() != null) {
            Proxy input = (Proxy) treeViewer.getInput();
            input.removeListener(modelChangeListener);
            input.removeDiagnosticListener(modelChangeListener);
        }
        treeViewer.setInput(newInput);

        if (newInput != null && newInput.getModelObject() instanceof VariantBuilder) {
            newInput.addListener(modelChangeListener, true);
            newInput.addDiagnosticListener(modelChangeListener);
                        
            final ParentProxy parent = newInput.getParentProxy();
            if (parent instanceof ListProxy && !removalListenerAdded) {
                // allows to refresh Style Rules window when a Variant is removed from list.                
                parent.addListener(variantRemovalListener, false);
                // Need to know if listener was added because only 1 instance of
                // given listener can be added to ListProxy
                removalListenerAdded  = true;
            }
            
            displayAreaLayout.topControl = tree;
            
        } else {
            displayAreaLayout.topControl = messagePage;
        }
        displayArea.layout();
    }

    /**
     * A label provider for tables consisting of rules and properties.
     */
    private class RuleTreeLabelProvider implements ITableLabelProvider {
        /**
         * The CSS suffix for important properties - this needs to be stripped
         * from rendered property values as the important status is displayed
         * in a separate column.
         */
        private static final String IMPORTANT_SUFFIX = " !important";

        // Javadoc inherited
        public void addListener(ILabelProviderListener iLabelProviderListener) {
        }

        // Javadoc inherited
        public void dispose() {
        }

        // Javadoc inherited
        public boolean isLabelProperty(Object o, String s) {
            return true;
        }

        // Javadoc inherited
        public void removeListener(ILabelProviderListener iLabelProviderListener) {
        }

        /**
         * Converts a property value to its string equivalent.
         *
         * @param propertyValue The property value to render
         * @return The string representation of the specified value
         */
        private String propertyValueToString(PropertyValue propertyValue) {
            StyleValue value = propertyValue.getValue();
            StyleProperty property = propertyValue.getProperty();

            StringWriter writer = new StringWriter();
            String rendered = "";
            RendererContext context = new RendererContext(
                    writer, CSSStyleSheetRenderer.getSingleton());
            StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
            try {
                MutableStyleProperties properties =
                    ThemeFactory.getDefaultInstance().createMutableStyleProperties();
                properties.setStyleValue(property, value);
                renderer.renderStyleProperties(properties, context);
                context.flushStyleSheet();
                rendered = writer.toString();
                int firstColon = rendered.indexOf(':');
                if (firstColon > 0) {
                    rendered = rendered.substring(firstColon + 1);
                }
                if (rendered.endsWith(IMPORTANT_SUFFIX)) {
                    rendered = rendered.substring(0, rendered.length() - IMPORTANT_SUFFIX.length());
                }
            } catch (IOException ioe) {
            }
            return rendered;
        }

        /**
         * Converts a selector list to its string equivalent.
         *
         * @param selectors A list of selectors to render as a string
         * @return The string representation of the selectors
         */
        private String selectorToString(List selectors) {
            StringWriter writer = new StringWriter();
            String rendered = "";
            RendererContext context = new RendererContext(
                    writer, CSSStyleSheetRenderer.getSingleton());
            StyleSheetRenderer renderer = CSSStyleSheetRenderer.getSingleton();
            try {
                renderer.renderStyleSelectors(selectors, context);
                context.flushStyleSheet();
                rendered = writer.toString();
            } catch (IOException ioe) {
            }
            return rendered;
        }

        // Javadoc inherited
        public Image getColumnImage(Object o, int i) {
            Image image = null;
            if (o instanceof Proxy) {
                Proxy proxy = (Proxy) o;
                if (proxy.getModelObject() instanceof PropertyValue) {
                    image = getColumnImageProperty(proxy, i);
                }
            }
            return image;
        }

        // Javadoc inherited
        public String getColumnText(Object o, int i) {
            String string = "";
            if (o instanceof Proxy) {
                Proxy proxy = (Proxy) o;
                if (proxy.getModelObject() instanceof Rule) {
                    string = getColumnTextRule((BeanProxy) proxy, i);
                } else if (proxy.getModelObject() instanceof PropertyValue) {
                    string = getColumnTextProperty(proxy, i);
                }
            }
            return string;
        }

        /**
         * Returns the image for a column for a given property.
         *
         * @param property The property for which to return the text
         * @param i The index of the column being processed
         * @return The image to display in the specified column
         */
        private Image getColumnImageProperty(Proxy property, int i) {
            Image imageValue = null;
            if (i == COLUMN_ERROR) {
                List diagnostics = property.getDiagnostics();
                if (diagnostics != null && !diagnostics.isEmpty()) {
                    imageValue = ERROR_IMAGE;
                }
            } else if (i == COLUMN_IMPORTANT) {
                PropertyValue value = (PropertyValue) property.getModelObject();
                if (value != null && value.getPriority() == Priority.IMPORTANT) {
                    imageValue = IMPORTANT_IMAGE;
                }
            }
            return imageValue;
        }

        /**
         * Returns the text for a column for a given rule.
         *
         * @param rule The rule for which to return the text
         * @param i The index of the column being processed
         * @return The text to display in the specified column
         */
        private String getColumnTextRule(BeanProxy rule, int i) {
            String stringValue = "";
            if (i == COLUMN_PROPERTY) {
                ListProxy proxy = (ListProxy) rule.getPropertyProxy(Rule.SELECTORS);
                List list = (List) proxy.getModelObject();
                stringValue = selectorToString(list);
            }
            return stringValue;
        }

        /**
         * Returns the text for a column for a given property.
         *
         * @param property The property for which to return the text
         * @param i The index of the column being processed
         * @return The text to display in the specified column
         */
        private String getColumnTextProperty(Proxy property, int i) {
            String stringValue = "";

            if (i == COLUMN_PROPERTY) {
                BeanProxy parent = (BeanProxy) property.getParentProxy();
                PropertyIdentifier identifier = parent.getPropertyForProxy(property);
                StyleProperty styleProperty = ThemeModel.getStylePropertyForPropertyIdentifier(identifier);
                stringValue = styleProperty.getName();
                stringValue = EclipseCommonMessages.getString("PolicyName." + stringValue);
            } else if (i == COLUMN_VALUE) {
                PropertyValue propertyValue = (PropertyValue) property.getModelObject();
                stringValue = propertyValueToString(propertyValue);
            }
            return stringValue;
        }
    }

    /**
     * A tree content provider for displaying the rules in a device variant with
     * their style properties as nodes under the rules.
     */
    private class RuleTreeContentProvider implements ITreeContentProvider {
        /**
         * A map associating style properties with their parent rules.
         */
        private Map parents;

        /**
         * The proxy for the device variant being displayed
         */
        private BeanProxy variant;

        private void populateParentMap() {
            populateParentMap(variant);
        }

        /**
         * Populates the map associating style property proxies with their
         * parent rule.
         *
         * @param variant The proxy for the variant to process
         */
        private synchronized void populateParentMap(BeanProxy variant) {
            parents = new HashMap();
            
            
             ListProxy rules = getRulesFromVariant(variant);
             if (rules != null) {
                 for (int i = 0; i < rules.size(); i++) {
                     BeanProxy rule = (BeanProxy) rules.getItemProxy(i);
                     Object[] styleProperties = getStylePropertyProxies(
                             (BeanProxy) rule.getPropertyProxy(
                                     Rule.STYLE_PROPERTIES));
                     for (int j = 0; j < styleProperties.length; j++) {
                         parents.put(styleProperties[j], rule);
                     }
                 }
             }            
        }

        /**
         * Gets the list proxy of rules from a bean proxy representing a variant
         *
         * @param variant The variant for which to retrieve the rules
         * @return A list proxy representing the rules, or null if none can be
         *         found.
         */
        private ListProxy getRulesFromVariant(BeanProxy variant) {
            ListProxy rules = null;

            BaseProxy content = (BaseProxy)
                    variant.getPropertyProxy(PolicyModel.CONTENT);
            BeanProxy themeContent = (BeanProxy) content.getConcreteProxy();
            if (themeContent != null) {
                BeanProxy stylesheet = (BeanProxy)
                        themeContent.getPropertyProxy(ThemeModel.STYLE_SHEET);
                rules = (ListProxy) stylesheet.
                        getPropertyProxy(ThemeModel.RULES);
            }

            return rules;
        }

        /**
         * Retrieves the individual style property proxies for a proxy
         * corresponding to a style properties object.
         *
         * @param styleProperties The proxy for a set of style properties
         * @return An array of proxies representing the individual properties
         */
        private Object[] getStylePropertyProxies(BeanProxy styleProperties) {
            List stylePropertyProxies = new ArrayList();
            BeanClassDescriptor descriptor = styleProperties.getBeanClassDescriptor();
            List propertyDescriptors = descriptor.getPropertyDescriptors();
            Iterator it = propertyDescriptors.iterator();
            while (it.hasNext()) {
                PropertyDescriptor propertyDescriptor = (PropertyDescriptor) it.next();
                Proxy property = styleProperties.getPropertyProxy(propertyDescriptor.getIdentifier());
                if (property.getModelObject() != null) {
                    stylePropertyProxies.add(property);
                }
            }
            return stylePropertyProxies.toArray();
        }

        // Javadoc inherited
        public Object[] getChildren(Object o) {
            Object[] returnValue = null;
            BeanProxy proxy = (BeanProxy) o;
            if (proxy.getModelObject() instanceof Rule) {
                returnValue = getStylePropertyProxies((BeanProxy) proxy.getPropertyProxy(Rule.STYLE_PROPERTIES));
            } else {
                returnValue = new Object[0];
            }
            return returnValue;
        }

        // Javadoc inherited
        public Object getParent(Object o) {
            return parents.get(o);
        }

        // Javadoc inherited
        public boolean hasChildren(Object o) {
            Proxy proxy = (Proxy) o;
            return proxy.getModelObject() instanceof Rule;
        }

        // Javadoc inherited
        public Object[] getElements(Object o) {
            ListProxy rules = getRulesFromVariant((BeanProxy) o);
            Object[] elements = null;
            if (rules == null) {
                elements = new Object[0];
            } else {
                elements = new Object[rules.size()];
                for (int i = 0; i < rules.size(); i++) {
                    elements[i] = rules.getItemProxy(i);
                }
            }
            return elements;
        }

        // Javadoc inherited
        public void dispose() {
        }

        // Javadoc inherited
        public void inputChanged(Viewer viewer, Object oldVal, Object newVal) {
            if (newVal instanceof BeanProxy) {
                this.variant = (BeanProxy) newVal;
                populateParentMap(variant);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10345/1	adrianj	VBM:2005111601 Add style rule view

 ===========================================================================
*/
