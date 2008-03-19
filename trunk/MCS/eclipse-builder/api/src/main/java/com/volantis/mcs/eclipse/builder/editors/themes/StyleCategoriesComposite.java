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
package com.volantis.mcs.eclipse.builder.editors.themes;

import com.volantis.mcs.eclipse.builder.common.ProxyProvider;
import com.volantis.mcs.eclipse.builder.common.ProxyReceiver;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.model.ThemeModel;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.xml.xerces.parsers.SAXParser;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.texteditor.ResourceAction;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.util.Iterator;
import java.util.List;

/**
 * Composite that displays a tree of style categories, each of which may
 * have child style categories
 */
public class StyleCategoriesComposite extends Composite {
    /**
     * The prefix for resource messages associated with StyleCategoriesComposite.
     */
    private static final String RESOURCE_PREFIX =
                "StyleCategoriesComposite.";

    /**
     * The message key for the minumum width of the style categories tree.
     */
    private static final int MIN_TREE_WIDTH =
            ThemesMessages.getInteger(RESOURCE_PREFIX +
                                      "tree.width").intValue();

   /**
    * The location of the style-categories.xml file in the ab jar.
    * This xml file is parsed in order to populate the categories tree.
    */
    private static final String STYLE_CATEGORIES_XML_LOCATION =
        "com/volantis/mcs/eclipse/builder/editors/themes/" +
        "style-categories.xml";

    /**
     * This can be an arbitrary string as it is a purely internal
     * system ID in this case
     */
    private static final String STYLE_CATEGORIES_XML_URI =
        "http://www.volantis.com/style-categories.xml";

    /**
     * The location of the style-categories.xsd file in the ab jar.
     * This schema will be used to validate the style-categories.xml that
     * is used to populate the categories tree.
     */
     private static final String STYLE_CATEGORIES_SCHEMA_LOCATION =
         "com/volantis/mcs/eclipse/builder/editors/themes/" +
         "style-categories.xsd";

     /**
      * This is the entity string that the style-categories.xml file uses to
      * reference the style-categories.xsd
      */
     private static final String STYLE_CATEGORIES_SCHEMA_URI =
         "http://www.volantis.com/style-categories.xsd";

    /**
     * Factory for creating theme elements.
     */
    private static final StyleValueFactory THEME_FACTORY =
            StyleValueFactory.getDefaultInstance();

    /**
     * Array of StyleCategory objects that are at the top level of the tree:
     * this is shared by all instances so is static
     */
    private static StyleCategory[] categories;

    /**
     * TreeViewer for the StyleCategorys
     */
    private final TreeViewer categoryTreeViewer;

    /**
     * Will listen for nodes being collapsed in the tree
     */
    private final ITreeViewerListener collapseListener;

    /**
     * The action for setting all values in a category to inherit.
     */
    private Action inheritAction;

    /**
     * The action for setting all priorities in a category to important.
     */
    private Action priorityAction;
    
    /**
     * A provider to make available the current style properties being edited.
     */
    private final ProxyProvider stylePropertiesProxyProvider;

    /**
     * A receiver to allow modified style properties to be sent back to the
     * parent of this composite.
     */
    private final ProxyReceiver stylePropertiesProxyReceiver;

    /**
     * Constructor
     *
     * @param parent The parent Composite
     */
    public StyleCategoriesComposite(Composite parent,
                                    ProxyProvider stylePropertiesProvider,
                                    ProxyReceiver stylePropertiesReceiver) {
        // Invoke super constructor
        super(parent, SWT.NONE);

        stylePropertiesProxyProvider = stylePropertiesProvider;
        stylePropertiesProxyReceiver = stylePropertiesReceiver;

        // Attempt to initialise the categories if they are still null;
        // NB IS IT DELIBERATE that this call is not surrounded by a
        // test for categories == null, and that the synchronized method
        // is always called - see:
        // http://www.javaworld.com/javaworld/jw-02-2001/jw-0209-double.html
        initializeCategories();

        // Order of following is important:
        // First set the layout
        GridLayout layout = new GridLayout();
        layout.marginHeight = 1;
        layout.marginWidth = 1;
        setLayout(layout);
        setLayoutData(new GridData(GridData.FILL_VERTICAL));
        setBackground(getDisplay().
                getSystemColor(SWT.COLOR_BLACK));

        // Construct the tree viewer on this, setting single select mode
        categoryTreeViewer = new TreeViewer(this, SWT.SINGLE);
        Tree tree = categoryTreeViewer.getTree();
        // for some reason the verticalAlignment needs to be set at
        // construction time if a widthHint is provided.
        GridData data = new GridData(GridData.FILL_VERTICAL);
        data.widthHint = MIN_TREE_WIDTH;
        tree.setLayoutData(data);


        // Set its content provider and label provider
        final IContentProvider contentProvider =
            new StyleCategoriesContentProvider();
        final ILabelProvider labelProvider = new StyleCategoriesLabelProvider();
        categoryTreeViewer.setContentProvider(contentProvider);
        categoryTreeViewer.setLabelProvider(labelProvider);

        // create the node collapse listener. We need this listener to ensure
        // that a node is always selected. If a node is expanded and one of
        // its children is the selected node the tree will have no nodes
        // selected when the expanded node is collapsed.
        collapseListener = new ITreeViewerListener() {
            // javadoc inherited
            public void treeCollapsed(TreeExpansionEvent event) {
                // If the colapse event has resulted in nothing being selected
                // then ensure that the node that was collapsed is selected
                if (categoryTreeViewer.getSelection().isEmpty()) {
                    categoryTreeViewer.setSelection(
                                new StructuredSelection(event.getElement()));
                }
            }

            // javadoc inherited
            public void treeExpanded(TreeExpansionEvent event) {
                // not interested in expansion events
            }
        };
        // register the listener with the viewer
        categoryTreeViewer.addTreeListener(collapseListener);

        // Now set the input to the top-level categories: in single-select
        // mode, this will automatically cause the first one to be selected
        // which is what we want
        categoryTreeViewer.setInput(categories);

        // Listen for disposal
        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                contentProvider.dispose();
                labelProvider.dispose();
                categoryTreeViewer.removeTreeListener(collapseListener);
            }
        });

        // initialize the actions that the context sensitive menu requires
        initializeActions();
        // initialize the context sensitive menu
        initializeContextMenu();
    }

    /**
     * Creates the actions.
     */
    private void initializeActions() {
        inheritAction = new ResourceAction(ThemesMessages.getResourceBundle(),
                RESOURCE_PREFIX + "allToInherit.") {
            // Javadoc inherited
            public void run() {
                ISelection selection = categoryTreeViewer.getSelection();
                if (selection instanceof IStructuredSelection) {
                    BeanProxy stylePropertiesProxy =
                            (BeanProxy) stylePropertiesProxyProvider.getProxy();
                    IStructuredSelection structuredSelection =
                            (IStructuredSelection) selection;
                    Iterator categories = structuredSelection.iterator();
                    while (categories.hasNext()) {
                        StyleCategory category = (StyleCategory) categories.next();
                        Iterator it = category.getProperties().iterator();
                        while (it.hasNext()) {
                            StyleProperty details = (StyleProperty) it.next();
                            PropertyIdentifier identifier = ThemeModel.
                                getPropertyIdentifierForStyleProperty(details);
                            Proxy propertyProxy =
                                    stylePropertiesProxy.getPropertyProxy(identifier);
                            PropertyValue value =
                                ThemeFactory.getDefaultInstance().
                                    createPropertyValue(
                                        details, THEME_FACTORY.getInherit());
                            propertyProxy.setModelObject(value);
                        }
                    }
                    stylePropertiesProxyReceiver.setProxy(stylePropertiesProxy);
                }
            }
        };

        priorityAction = new ResourceAction(
                    ThemesMessages.getResourceBundle(),
                    RESOURCE_PREFIX + "allToImportant.") {
            // Javadoc inherited
            public void run() {
                ISelection selection = categoryTreeViewer.getSelection();
                if (selection instanceof IStructuredSelection) {
                    BeanProxy stylePropertiesProxy =
                            (BeanProxy) stylePropertiesProxyProvider.getProxy();
                    IStructuredSelection structuredSelection =
                            (IStructuredSelection) selection;
                    Iterator categories = structuredSelection.iterator();
                    while (categories.hasNext()) {
                        StyleCategory category = (StyleCategory) categories.next();
                        Iterator it = category.getProperties().iterator();
                        while (it.hasNext()) {
                            StyleProperty details = (StyleProperty) it.next();
                            PropertyIdentifier identifier = ThemeModel.
                                getPropertyIdentifierForStyleProperty(details);
                            Proxy propertyProxy =
                                    stylePropertiesProxy.getPropertyProxy(identifier);
                            PropertyValue value = (PropertyValue) propertyProxy.getModelObject();
                            if (value != null) {
                                value = ThemeFactory.getDefaultInstance().
                                    createPropertyValue(value.getProperty(),
                                        value.getValue(), Priority.IMPORTANT);
                                propertyProxy.setModelObject(value);
                            }
                        }
                    }
                    stylePropertiesProxyReceiver.setProxy(stylePropertiesProxy);
                }
            }
        };
    }

    /**
     * Initialize the context menu that is associated with the tree control.
     * This method expects all the actions to have been initialized.
     */
    private void initializeContextMenu() {
        // Create menu manager.
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            // javadoc inherited
            public void menuAboutToShow(IMenuManager mgr) {
                fillContextMenu(mgr);
            }
        });

        // Create the menu.
        Menu menu = menuMgr.createContextMenu(categoryTreeViewer.getTree());
        categoryTreeViewer.getTree().setMenu(menu);
    }

    /**
     * Fill the context menu with actions.
     * This method expects all the actions to have been initialized.
     *
     * @param manager The MenuManager.
     */
    private void fillContextMenu(IMenuManager manager) {
        // add the inherit action
        manager.add(inheritAction);
        // add the priority action
        manager.add(priorityAction);
    }

    /**
     * Initialises this class's static array of top-level categories
     * which are used as input to the displayed category tree
     */
    synchronized private void initializeCategories() {
        if (categories == null) {
            try {
                categories = readCategories();
            } catch (Throwable t) {
                EclipseCommonPlugin.handleError(t);
            }
        }
    }

    /**
     * Reads the style categories XML file and uses the contents to populate
     * (and return) an array of the top-level categories
     *
     * @return The top-level categories  (each of which may have child
     * categories)
     * @throws Exception if there was any XML i/o, parsing or schema
     * validation error
     */
    private StyleCategory[] readCategories()
        throws Exception {

        // Get an XML reader
        final XMLReader reader = new SAXParser();

        // Construct an entity resolver that looks in the JAR that
        // contains the same class as this class, and use it to look
        // up the XML file
        final JarFileEntityResolver resolver = new JarFileEntityResolver(this);
        // add the mapping for the style-catorgories.xml file.
        resolver.addSystemIdMapping(
            STYLE_CATEGORIES_XML_URI,
            STYLE_CATEGORIES_XML_LOCATION);
        // add the mapping for the style-catorgories.xsd file.
        resolver.addSystemIdMapping(
            STYLE_CATEGORIES_SCHEMA_URI,
            STYLE_CATEGORIES_SCHEMA_LOCATION);

        final InputSource xmlInputSource =
            resolver.resolveEntity(null, STYLE_CATEGORIES_XML_URI);

        // Create a content handler and attach it to the reader
        final StyleCategoryContentHandler handler =
            new StyleCategoryContentHandler();
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
        reader.setEntityResolver(resolver);

        // Turn on schema validation
        reader.setFeature(
            "http://apache.org/xml/features/validation/schema", true);

        // Parse the XML file
        reader.parse(xmlInputSource);

        // Get the categories from the content handler
        final List catsList = handler.getStyleCategories();
        StyleCategory[] catsArray = new StyleCategory[catsList.size()];
        return (StyleCategory[])
            handler.getStyleCategories().toArray(catsArray);
    }

    /**
     * Returns an array of top level StyleCategory instances
     *
     * @return a StyleCategory array.
     */
    public StyleCategory[] getCategories() {
        return categories;
    }

    /**
     * Allows clients to register for category selection notifications
     *
     * @param listener The listener that will receive the notifications
     */
    public void addCategorySelectionListener(ISelectionChangedListener listener) {
        // Just delegate to the tree
        categoryTreeViewer.addSelectionChangedListener(listener);
    }

    /**
     * Allows a category to be selected
     *
     * @param category the StyleCategory that should be selected.
     */
    public void selectCategory(StyleCategory category) {
        categoryTreeViewer.setSelection(new StructuredSelection(category));
    }

    /**
     * Allows clients to de-register for category selection notifications
     *
     * @param listener The listener that will no longer receive the
     * notifications
     */
    public void removeCategorySelectionListener(ISelectionChangedListener listener) {
        // Just delegate to the tree
        categoryTreeViewer.removeSelectionChangedListener(listener);
    }

}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10617/7	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10617/5	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10617/3	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10617/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 06-Dec-05	10589/1	adrianj	VBM:2005101809 Add context menu for StyleCategoriesComposite

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
