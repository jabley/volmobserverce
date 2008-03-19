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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMUndoRedoGUIDemarcator;
import com.volantis.mcs.eclipse.ab.editors.dom.ProxyElement;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutODOMEditorContext;
import com.volantis.mcs.eclipse.ab.editors.layout.LayoutEditorContext;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionListener;
import com.volantis.mcs.eclipse.builder.common.BuilderSelectionEvent;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.interaction.event.InteractionEventListenerAdapter;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.BeanProxy;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;
import org.jdom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This view displays attributes using controls defined in the associated XML
 * configuration file. This page will use a proxy element configured to handle
 * Format selections directly.<p>
 *
 * It will parse the XML Configuration, storing the details in various
 * AttributesDetails implementation instances, and construct a number of
 * wrappered AttributesComposites, one per section.<p>
 *
 * The page's tab text will be set using the proxy element name. This will be
 * updated each time the proxy element's name changes. Note that the text will
 * be obtained from a resource bundle and should contain values for 'undefined'
 * and null.<p>
 *
 * Note that special handling is required for the nextLinkText and
 * previousLinkText attributes. When used in a DissectingPane these may only
 * contain literal text, while within a Fragment they may contain literal text or
 * component references. Essentially, when a dissecting pane is selected the
 * controls representing these values must be told to disable the component
 * reference function. In all other cases, the controls must be told that they
 * can enable the component reference function. This handling must be performed
 * in an ODOMElementSelectionListener that looks for dissecting pane
 * selections.<p>
 *
 * The wrappered page will register a property change listener on each
 * AttributesComposite, and will use the property change events to update the
 * proxy element's attributes.<p>
 *
 * This page will also set up an ODOM change listener and will use the ODOM
 * change events to drive value updates down into the attributes composites'
 * controls. Each change will have to be passed to all (wrappered) attributes
 * composites - only the attributes composite containing a control for the given
 * attribute will actually do anything. Attribute update events should be sent
 * to the wrapping composites which will delegate to the contained attribute
 * composite.<p>
 *
 * These updates include changing control visibility based on the creation and
 * destruction of attributes on the proxy element.<p>
 *
 * Both these listeners need to be disposed in the dispose method. <p>
 */
public class FormatAttributesViewPage extends Page {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(FormatAttributesViewPage.class);


   /**
     * Store the state of the MAIN xml configuration so that we don't read the
     * configuration more than once in a session.
     */
    private static List mainSectionDetails = null;

    /**
     * Store the state of the ROW xml configuration so that we don't read the
     * configuration more than once in a session.
     */
    private static List rowSectionDetails = null;

    /**
     * Store the state of the COLUMN xml configuration so that we don't read the
     * configuration more than once in a session.
     */
    private static List columnSectionDetails = null;

    /**
     * The resource prefix for this class.
     */
    private static final String RESOURCE_PREFIX = "FormatAttributesViewPage.";

    /**
     * The constant for margin width.
     */
    private static final int MARGIN_WIDTH =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
                                      "marginWidth").intValue();

    /**
     * The constant for margin height.
     */
    private static final int MARGIN_HEIGHT =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
                                      "marginHeight").intValue();

    /**
     * The constant for vertical spacing.
     */
    private static final int VERTICAL_SPACING =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
                                      "verticalSpacing").intValue();

    /**
     * Vertical scroll increment.
     */
    private static final int VERTICAL_SCROLL_INCREMENT =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
                                      "verticalScrollIncrement").intValue();

    /**
     * Horizontal scroll increment.
     */
    private static final int HORIZONTAL_SCROLL_INCREMENT =
            LayoutMessages.getInteger(RESOURCE_PREFIX +
                                      "horizontalScrollIncrement").intValue(); 

    /**
     * Store the control that may be returned via the getControl method.
     */
    private Composite viewer = null;

    /**
     * The odom editor context.
     */
    private ODOMEditorContext odomEditorContext;

    /**
     * List of tab details.
     */
    private TabDetails mainTabDetails;
    private TabDetails rowTabDetails;
    private TabDetails columnTabDetails;
    private TabFolder tabFolder;

    private List scrollers = new ArrayList(3);

    /**
     * helper that executes UndoRedo unit of work demarcation
     */
    private ODOMUndoRedoGUIDemarcator odomUndoRedoGUIDemarcator;

    /**
     * state flag used to allow clients to known they
     * hold on to a disposed instance
     */
    private boolean disposed = false;


    /**
     * Inner class that encapsulates each tab and its contained information as
     * well as some tab specific behaviour.
     */
    private class TabDetails {
        /**
         * The tab item.
         */
        private TabItem item;

        /**
         * The odom change listener.
         */
        private ODOMChangeListener odomChangeListener;

        /**
         * The odom element.
         */
        private ProxyElement odomElement;

        /**
         * Store the filter so that the listener may be removed in the dispose
         * method.
         */
        private ODOMSelectionFilter odomSelectionFilter;

        /**
         * The list of section details composites.
         */
        private List sectionDetailsComposites;

        /**
         * The layout proxy element details.
         */
        private LayoutProxyElementDetails layoutDetails;


        /**
         * Create the Tab Details item with the relevant parameters.
         *
         * @param item                     the new created <code>TabItem</code>.
         * @param odomChangeListener       the odom change listener.
         * @param odomElement              the odom element.
         * @param odomSelectionFilter      the odom selection filter.
         * @param sectionDetailsComposites the section details composites list.
         * @param layoutDetails            the layout proxy element details.
         */
        public TabDetails(TabItem item,
                          ODOMChangeListener odomChangeListener,
                          ProxyElement odomElement,
                          ODOMSelectionFilter odomSelectionFilter,
                          List sectionDetailsComposites,
                          LayoutProxyElementDetails layoutDetails) {
            this.item = item;
            this.odomChangeListener = odomChangeListener;
            this.odomElement = odomElement;
            this.odomSelectionFilter = odomSelectionFilter;
            this.sectionDetailsComposites = sectionDetailsComposites;
            this.layoutDetails = layoutDetails;
        }

        /**
         * Getter for the item field.
         * @return the item field.
         */
        public TabItem getItem() {
            return item;
        }

        /**
         * Setter for the item field.
         * @param item the item field.
         */
        public void setItem(TabItem item) {
            this.item = item;
        }

        /**
         * Get the odom change listener.
         * @return the odom change listener.
         */
        public ODOMChangeListener getOdomChangeListener() {
            return odomChangeListener;
        }

        /**
         * Set the odom change listener.
         * @param odomChangeListener the odom change listener.
         */
        public void setOdomChangeListener(ODOMChangeListener odomChangeListener) {
            this.odomChangeListener = odomChangeListener;
        }

        /**
         * The odom element.
         * @return the odom element.
         */
        public ProxyElement getOdomElement() {
            return odomElement;
        }

        /**
         * Set the odom element.
         * @param odomElement the odom element.
         */
        public void setOdomElement(ProxyElement odomElement) {
            this.odomElement = odomElement;
        }

        /**
         * The odom selection filter.
         * @return the odom selection filter.
         */
        public ODOMSelectionFilter getOdomSelectionFilter() {
            return odomSelectionFilter;
        }

        /**
         * Set the the odom selection filter.
         * @param odomSelectionFilter the odom selection filter.
         */
        public void setOdomSelectionFilter(ODOMSelectionFilter odomSelectionFilter) {
            this.odomSelectionFilter = odomSelectionFilter;
        }

        /**
         * The section details composite.
         * @return the section details composite.
         */
        public List getSectionDetailsComposites() {
            return sectionDetailsComposites;
        }

        /**
         * Get the layout details.
         * @return the layout details.
         */
        public LayoutProxyElementDetails getLayoutDetails() {
            return layoutDetails;
        }

        /**
         * Set the layout details.
         * @param layoutDetails the layout details.
         */
        public void setLayoutDetails(LayoutProxyElementDetails layoutDetails) {
            this.layoutDetails = layoutDetails;
        }

        /**
         * Dispose of the elements contained in this object and remove
         * the appropriate listeners too.
         */
        public void dispose() {
            // Remove teh property change listeners from each section details
            // composite.
            if (sectionDetailsComposites != null) {
                Iterator iterator = sectionDetailsComposites.iterator();
                while (iterator.hasNext()) {
                    SectionDetailsComposite wac =
                            (SectionDetailsComposite) iterator.next();
                    wac.dispose();
                }
                sectionDetailsComposites.clear();
                sectionDetailsComposites = null;
            }

            // Remove the selection listener on the main element.
            odomElement.removeChangeListener(odomChangeListener);

            odomEditorContext.getODOMSelectionManager().removeSelectionListener(
                    odomElement, odomSelectionFilter);
        }


        /**
         * Add the property change listeners for each of the section details
         * composite objects.
         */
        public void addPropertyChangeListeners() {
            Iterator wacIterator = getSectionDetailsComposites().iterator();
            while (wacIterator.hasNext()) {
                SectionDetailsComposite wac =
                        (SectionDetailsComposite) wacIterator.next();
                // Create the property change listener that will propogate any
                // changes to the ProxyODOMElement.
                wac.addPropertyChangeListener(new IPropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent changeEvent) {
                        updateODOMAttributes(changeEvent);
                    }
                });
            }
        }

        /**
         * Updates the ODOMElement's attributes after a PropertyChangeEvent from
         * the form.
         *
         * @param changeEvent the PropertyChangeEvent.
         */
        private void updateODOMAttributes(PropertyChangeEvent changeEvent) {
            String name = changeEvent.getProperty();
            Object newValue = changeEvent.getNewValue();
            if (newValue != null) {
                ProxyElement odomElement = getOdomElement();
                ODOMChangeListener odomChangeListener = getOdomChangeListener();

                odomElement.removeChangeListener(odomChangeListener);
                odomElement.setAttribute(name, newValue.toString());
                odomElement.addChangeListener(odomChangeListener);
            }
        }

        /**
         * Update the visibility of all the controls in all the section details
         * composites. This is done by obtaining a list of attribute
         * names to display from the layout details instance and asking each
         * section details composite to update the visibility accordingly.
         */
        protected void updateControlVisibility() {
            final String[] attributeNames = getLayoutDetails().getAttributeNames();

            if (getSectionDetailsComposites() != null) {
                Iterator iterator = getSectionDetailsComposites().iterator();
                while (iterator.hasNext()) {
                    final SectionDetailsComposite sdc =
                            ((SectionDetailsComposite) iterator.next());
                    sdc.setVisible(attributeNames);
                }
                iterator = scrollers.iterator();
                while (iterator.hasNext()) {
                    ScrolledComposite scroller = (ScrolledComposite)iterator.next();
                    refresh(scroller);
                }
            }
        }

        /**
         * Update the display area (contents of the visible controls) for each
         * section details composite in the list.
         *
         * @param event     the ODOM change event event.
         */
        private void updateDisplayArea(ODOMChangeEvent event) {
            if (getSectionDetailsComposites() != null) {
                Iterator iterator = getSectionDetailsComposites().iterator();
                while (iterator.hasNext()) {
                    final SectionDetailsComposite sectionDetailsComposite =
                        (SectionDetailsComposite) iterator.next();
                    sectionDetailsComposite.updateDisplayArea(event);
                    final LayoutEditorContext layoutEditorContext =
                        ((LayoutODOMEditorContext) odomEditorContext).
                            getLayoutEditorContext();
                    sectionDetailsComposite.setEnabled(
                        !layoutEditorContext.isSelectedVariantReadOnly());
                }
            }
        }

        /**
         * Updates the enablement of the section details composites based on
         * the read onlyness of the currently selected variant
         */
        private void updateEnablement() {
            if (getSectionDetailsComposites() != null) {
                final LayoutODOMEditorContext layoutODOMEditorContext =
                    ((LayoutODOMEditorContext) odomEditorContext);
                final LayoutEditorContext layoutEditorContext =
                    layoutODOMEditorContext.getLayoutEditorContext();
                final boolean enablement =
                    !layoutEditorContext.isSelectedVariantReadOnly();
                final Iterator iterator =
                    getSectionDetailsComposites().iterator();
                while (iterator.hasNext()) {
                    final SectionDetailsComposite sectionDetailsComposite =
                        (SectionDetailsComposite) iterator.next();
                    sectionDetailsComposite.setEnabled(enablement);
                }
            }
        }
    }

    /**
     * Construct this object with an ODOMEditorContext.
     *
     * @param odomEditorContext the ODOM editor context.
     * @throws IllegalArgumentException if the odom edtior context is null.
     */
    public FormatAttributesViewPage(ODOMEditorContext odomEditorContext)
        throws IllegalArgumentException {
        if (odomEditorContext == null) {
            throw new IllegalArgumentException(
                    "The ODOM editor context parameter may not be null."); //$NON-NLS-1$
        }
        this.odomEditorContext = odomEditorContext;
        final LayoutEditorContext layoutEditorContext =
            ((LayoutODOMEditorContext) odomEditorContext).getLayoutEditorContext();
        final InteractionEventListenerAdapter readOnlyListener =
            new InteractionEventListenerAdapter() {
                public void readOnlyStateChanged(
                    final ReadOnlyStateChangedEvent event) {
                    if (event.isOriginator ()) {
                        mainTabDetails.updateEnablement();
                        rowTabDetails.updateEnablement();
                        columnTabDetails.updateEnablement();
                    }
                }
            };
        layoutEditorContext.addSelectedVariantListener(new BuilderSelectionListener() {
            public void selectionMade(final BuilderSelectionEvent event) {
                if (event.getOldSelection() != null) {
                    ((Proxy) event.getOldSelection()).removeListener(readOnlyListener);
                }

                if (event.getSelection() != null) {
                    BeanProxy selectedVariant = (BeanProxy) event.getSelection();
                    selectedVariant.addListener(readOnlyListener, false);
                }
            }
        });

    }

    /**
     * Initialize the main tab details instance.
     *
     * @param odomEditorContext the odom editor context.
     * @param project           the project resource.
     * @param tabFolder         the <code>TabFolder</code> object.
     */
    private void intializeMainTabDetails(ODOMEditorContext odomEditorContext,
                                            IProject project,
                                            TabFolder tabFolder) {

        LayoutProxyElementDetails layoutDetails = new LayoutProxyElementDetails();
        ProxyElement element = new ProxyElement(layoutDetails,
                odomEditorContext.getODOMFactory());

        ODOMChangeListener changeListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                mainElementChanged(event);
            }
        };
        element.addChangeListener(changeListener);

            // Create the main tab selection filter.
        ODOMSelectionFilter odomSelectionFilter = new ODOMSelectionFilter(
                null, new String[] {
                    LayoutSchemaType.LAYOUT.getName(),
                    LayoutSchemaType.CANVAS_LAYOUT.getName(),
                    LayoutSchemaType.MONTAGE_LAYOUT.getName()
                });


        ArrayList sectionDetailsComposites = new ArrayList();
        TabItem tab = createMainTabItem(tabFolder, mainSectionDetails, project,
                element, odomSelectionFilter, sectionDetailsComposites);

        mainTabDetails = new TabDetails(tab, changeListener, element, odomSelectionFilter,
                sectionDetailsComposites, layoutDetails);

        odomEditorContext.getODOMSelectionManager().addSelectionListener(
                element, odomSelectionFilter);
    }


    /**
     * Initialize the row tab details.
     *
     * @param odomEditorContext the odom editor context.
     * @param project           the project resource.
     * @param tabFolder         the <code>TabFolder</code> object.
     */
    private void initializeRowTabDetails(ODOMEditorContext odomEditorContext,
                                           IProject project,
                                           TabFolder tabFolder) {

        LayoutProxyElementDetails layoutDetails = new LayoutProxyElementDetails();
        ProxyElement element = new ProxyElement(layoutDetails,
                odomEditorContext.getODOMFactory());

        ODOMChangeListener changeListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                tabElementChanged(event, rowTabDetails);
            }
        };
        element.addChangeListener(changeListener);

        // Create the row specific selection filter.
        RowODOMSelectionFilter selectionFilter = new RowODOMSelectionFilter();

        ArrayList sectionDetailsComposites = new ArrayList();
        TabItem tab = createRowTabItem(tabFolder, rowSectionDetails, project,
                element, selectionFilter, sectionDetailsComposites);

        rowTabDetails = new TabDetails(tab, changeListener, element, selectionFilter,
                sectionDetailsComposites, layoutDetails);

        odomEditorContext.getODOMSelectionManager().addSelectionListener(
                element, selectionFilter);
    }

    /**
     * Initialize the column tab details.
     *
     * @param odomEditorContext the odom editor context.
     * @param project           the project resource.
     * @param tabFolder         the <code>TabFolder</code> object.
     */
    private void initializeColumnTabDetails(ODOMEditorContext odomEditorContext,
                                           IProject project,
                                           TabFolder tabFolder) {

        LayoutProxyElementDetails layoutDetails = new LayoutProxyElementDetails();
        ProxyElement element = new ProxyElement(layoutDetails,
                odomEditorContext.getODOMFactory());

        ODOMChangeListener changeListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                tabElementChanged(event, columnTabDetails);
            }
        };
        element.addChangeListener(changeListener);

        // Create the column specific selection filter.
        ColumnODOMSelectionFilter selectionFilter =
                new ColumnODOMSelectionFilter();

        ArrayList sectionDetailsComposites = new ArrayList();
        TabItem tab = createColumnTabItem(tabFolder, columnSectionDetails, project,
                element, selectionFilter, sectionDetailsComposites);

        columnTabDetails = new TabDetails(tab, changeListener, element, selectionFilter,
                sectionDetailsComposites, layoutDetails);

        odomEditorContext.getODOMSelectionManager().addSelectionListener(
                element, selectionFilter);
    }

    // javadoc inherited
    public void createControl(Composite parent) {
        if (mainSectionDetails == null) {
            mainSectionDetails = initializeAttributeList(
                    "format-attributes-view.xml"); //$NON-NLS-1$
        }
        if (rowSectionDetails == null) {
            rowSectionDetails = initializeAttributeList(
                    "format-row-attributes-view.xml"); //$NON-NLS-1$
        }
        if (columnSectionDetails == null) {
            columnSectionDetails = initializeAttributeList(
                    "format-column-attributes-view.xml"); //$NON-NLS-1$
        }
        viewer = new Composite(parent, SWT.NONE);

        tabFolder = new TabFolder(viewer, SWT.NONE);
        tabFolder.setLayout(new GridLayout(1, true));
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

        IProject project = odomEditorContext.getPolicyResource().getProject();

        intializeMainTabDetails(odomEditorContext, project, tabFolder);
        initializeRowTabDetails(odomEditorContext, project, tabFolder);
        initializeColumnTabDetails(odomEditorContext, project, tabFolder);

        viewer.setLayout(new GridLayout(1, true));
        viewer.setLayoutData(new GridData(GridData.FILL_BOTH));

        // Add the property change listeners to each tab detail's sections
        // details composites. We do this here because we need to pass in
        // the created TabDetail instance to the updateODOMAttributes method.
        mainTabDetails.addPropertyChangeListeners();
        rowTabDetails.addPropertyChangeListeners();
        columnTabDetails.addPropertyChangeListeners();

        mainTabDetails.updateControlVisibility();
        rowTabDetails.updateControlVisibility();
        columnTabDetails.updateControlVisibility();

        odomUndoRedoGUIDemarcator = new ODOMUndoRedoGUIDemarcator(
                odomEditorContext.getUndoRedoManager());
        odomUndoRedoGUIDemarcator.addFocusDrivenUndoRedoDemarcatorFor(parent);
    }

    // javadoc inherited
    public void dispose() {
        mainTabDetails.dispose();
        rowTabDetails.dispose();
        columnTabDetails.dispose();
        disposed = true;
    }


    /**
     * Query accessor that clients can use to check whether they are
     * holding on to a disposed instance
     * @return true if this page has been disposed, false otherwise
     */
    public boolean isDisposed() {
        return disposed;
    }


    // javadoc inherited
    public Control getControl() {
        return viewer;
    }

    /**
     * Helper method to return a resource as a string.
     *
     * @param key the key to be used to extract the resource string.
     * @return the resource string, or null if not found.
     */
    private String getResourceString(String key) {
        StringBuffer buffer = new StringBuffer(RESOURCE_PREFIX);
        buffer.append(key).append(".text"); //$NON-NLS-1$
        return LayoutMessages.getString(buffer.toString());
    }

    /**
     * Helper method to return the policy resource as a string.
     *
     * @param key the key to be used to extract the resource string.
     * @return the resource string, or null if not found.
     */
    private String getPolicyResourceString(String key) {
        return EclipseCommonMessages.getLocalizedPolicyName(key);
    }


   /**
     * Create the main tab item (the name of this tab changes according to the
     * current selection)...
     *
     * @param tabFolder the tab folder to create the tabs in.
     * @param sections  the list of sections.
     * @param project   the project resource.
     * @param element   the odom element to proxy on.
     * @param filter    the odom selection filter.
     * @param sectionDetailsComposites
     *                  the list of section details composites (initially this
     *                  list should be empty so that all the newly created
     *                  <code>SectionDetailsComposite</code> may be added to
     *                  this list).
     * @return a newly created TabItem used for the display controls in the main
     *         tab.
     */
    private TabItem createMainTabItem(TabFolder tabFolder,
                                      List sections,
                                      IProject project,
                                      ProxyElement element,
                                      ODOMSelectionFilter filter,
                                      List sectionDetailsComposites) {
        TabItem tab = new TabItem(tabFolder, SWT.NONE);
        tab.setText(getPolicyResourceString(element.getName()));

        createScrollableTabControl(tabFolder, tab, element, sections,
                project, filter, sectionDetailsComposites);

        return tab;
    }

    /**
     * This tab can use a proxy element that will address the grid row
     * containing each selection's Format. However, because the page should only
     * be displayed when every selected Format is a "grid cell" (i.e. an
     * immediate child of the (segment) grid format row), bespoke selection
     * processing is required.<p>
     *
     * The attributes to be handled are:
     *
     * <ul> <li>height (for normal and segment grids)</li>
     *
     * <li>heightUnits (for segment grids only)</li> </ul>
     *
     * The proxy will provide intersections of available attributes for the
     * selected (segment) grid rows if all are non-null, and will return an
     * empty set of attributes if there is one or more null selected elements.
     * If the heightUnits attribute isn't available in the proxy, the height
     * editing control must be told not to handle the height units. If, on the
     * other hand, the heightUnits attribute is available, the height editing
     * control must be told to handle it.<p>
     *
     * The page will register as a property change listener on the single
     * attributes composite it contains and will use property change events to
     * update the selected Row heights. It will also register as an ODOM change
     * listener on the proxy and will use the ODOM change events to update the
     * attributes composite as needed.<p>
     *
     * The page's title will be a fixed value obtained from the LayoutMessages
     * helper class.
     *
     * @param tabFolder the tab folder to create the tabs in.
     * @param sections  the list of sections.
     * @param project   the project resource.
     * @param element   the odom element to proxy on.
     * @param filter    the odom selection filter.
     * @param sectionDetailsComposites
     *                  the list of section details composites (initially this
     *                  list should be empty so that all the newly created
     *                  <code>SectionDetailsComposite</code> may be added to
     *                  this list).
     * @return a newly created TabItem used for the display controls in the row
     *         tab.
     */
    private TabItem createRowTabItem(TabFolder tabFolder,
                                     List sections,
                                     IProject project,
                                     ProxyElement element,
                                     ODOMSelectionFilter filter,
                                     List sectionDetailsComposites) {
        TabItem tab = new TabItem(tabFolder, SWT.NONE);
        tab.setText(getResourceString("row")); //$NON-NLS-1$
        createScrollableTabControl(tabFolder, tab, element, sections,
                project, filter, sectionDetailsComposites);
        return tab;
    }

    /**
     * This tab must use a proxy element that will address the grid column
     * containing each selection's Format. However, because there's no easy way
     * of mapping from the selected formats to the required column elements and
     * to ensure that the page is only displayed when every selected Format is a
     * "grid cell", bespoke selection processing is required.<p>
     *
     * The unique set of column elements will then be given as the selections to
     * the proxy element.<p>
     *
     * The attributes to be handled are:
     *
     * <ul> <li>width (for both normal and segment grids)</li>
     *
     * <li>widthUnits (for both normal and segment grids)</li> </ul>
     *
     * The page will register as a property change listener on the single
     * attributes composite its contains and will use property change events to
     * update the selected Row heights. It will also register as an ODOM change
     * listener on the proxy and will use the ODOM change events to update the
     * attributes composite as needed.<p>
     *
     * The page's title will be a fixed value obtained from the LayoutMessages
     * helper class.<p>
     *
     * @param tabFolder the tab folder to create the tabs in.
     * @param sections  the list of sections.
     * @param project   the project resource.
     * @param element   the odom element to proxy on.
     * @param filter    the odom selection filter.
     * @param sectionDetailsComposites
     *                  the list of section details composites (initially this
     *                  list should be empty so that all the newly created
     *                  <code>SectionDetailsComposite</code> may be added to
     *                  this list).
     * @return a newly created TabItem used for the display controls in the
     *         column tab.
     */
    private TabItem createColumnTabItem(TabFolder tabFolder,
                                        List sections,
                                        IProject project,
                                        ProxyElement element,
                                        ODOMSelectionFilter filter,
                                        List sectionDetailsComposites) {
        TabItem tab = new TabItem(tabFolder, SWT.NONE);
        tab.setText(getResourceString("column")); //$NON-NLS-1$
        createScrollableTabControl(tabFolder, tab, element, sections,
                project, filter, sectionDetailsComposites);
        return tab;
    }

    /**
     * Create a scrollable tab composite that contains the contents of all the
     * sections attributes composite controls. The newly created attributes
     * composite objects are added to the section details composites list passed
     * in as a parameter.
     *
     * @param tabFolder the tabFolder (parent control).
     * @param tabItem   the tabItem that will contain this scrollable control.
     * @param element   the element that is required to create an attributes
     *                  composite instance.
     * @param sections  the list of SectionDetails objects used to create an
     *                  attributes composite instance.
     * @param project   the project used to create an attributes composite
     *                  instance.
     * @param filter    the odom selection filter.
     * @param sectionDetailsComposites
     *                  the section dteails composites.
     */
    private void createScrollableTabControl(TabFolder tabFolder,
                                            TabItem tabItem,
                                            ProxyElement element,
                                            List sections,
                                            IProject project,
                                            ODOMSelectionFilter filter,
                                            List sectionDetailsComposites) {

        final ScrolledComposite scroller = new ScrolledComposite(tabFolder,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scroller.getVerticalBar().setIncrement(VERTICAL_SCROLL_INCREMENT);
        scroller.getHorizontalBar().setIncrement(HORIZONTAL_SCROLL_INCREMENT);

        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);

        final Composite scrollable = new Composite(scroller, SWT.NONE);
        scroller.setLayoutData(new GridData(GridData.FILL_BOTH));
        scroller.setContent(scrollable);
        scrollers.add(scroller);

        GridLayout layout = new GridLayout();
        layout.marginHeight = MARGIN_HEIGHT;
        layout.marginWidth = MARGIN_WIDTH;
        layout.verticalSpacing = VERTICAL_SPACING;
        scrollable.setLayout(layout);

        tabItem.setControl(scroller);

        // Iterate over the sections and create a SectionDetailsComposite
        // object object whilst ensuring appropriate listeners have been
        // added.
        Iterator iterator = sections.iterator();
        while (iterator.hasNext()) {
            SectionDetails section = (SectionDetails) iterator.next();
            // Create the section detail first so we have something to add the
            // attributes composite controls into.
            SectionDetailsComposite sdc = new SectionDetailsComposite(
                    scrollable, SWT.NONE, section, element,
                    project,
                    odomEditorContext.getODOMSelectionManager(),
                    filter,
                    odomEditorContext.getHandler());
            sectionDetailsComposites.add(sdc);
        }

        updateSectionCompositesFirstColumnWidth(sectionDetailsComposites.iterator(),
                determineMaxWidth(sectionDetailsComposites.iterator()));

        refresh(scroller);
    }

    private void refresh(ScrolledComposite scroller) {
        Composite scrollable = (Composite)scroller.getContent();
        scrollable.setSize(scrollable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrollable.layout();
        scroller.setMinSize(scroller.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    /**
     * Update the attributes composite's 2 column layout with the specified
     * minimum width for the first column.
     *
     * @param iterator the iterator of section details composites.
     * @param width    the minimum width for the first column.
     */
    private void updateSectionCompositesFirstColumnWidth(Iterator iterator,
                                                         int width) {
        while (iterator.hasNext()) {
            SectionDetailsComposite sdc =
                    (SectionDetailsComposite) iterator.next();
            sdc.setFirstColumnWidth(width);
        }
    }

    /**
     * Determine the maximum width of the first column across all section
     * details components.
     *
     * @param iterator the iterator of section details composites.
     * @return the maximum width of the first column or 0 if no maximum was
     *         determinable.
     */
    private int determineMaxWidth(Iterator iterator) {
        int maxWidth = 0;
        while (iterator.hasNext()) {
            SectionDetailsComposite sdc =
                    (SectionDetailsComposite) iterator.next();
            maxWidth = Math.max(sdc.getFirstColumnWidth(), maxWidth);
        }
        return maxWidth;
    }


    /**
     * Initialize the attribute list from the XML configuration file. Parse the
     * XML document and return the array of SectionDetails objects in a List.
     *
     * @param xmlFilename the file name to read the xml data from.
     * @return a list of SectionDetails objects.
     */
    private List initializeAttributeList(String xmlFilename) {
        List sections = new ArrayList();
        try {
            XMLReader xmlReader = createValidatingReader(sections);
            xmlReader.parse(new InputSource(getClass().getResourceAsStream(
                    xmlFilename)));
        } catch (IOException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
        return sections;
    }

    /**
     * Helper method for creating the XMLReader with the validation enabled.
     *
     * @param sections the sections list
     * @return the newly created XMLReader.
     * @throws org.xml.sax.SAXNotRecognizedException if SAX is not recognized.
     * @throws org.xml.sax.SAXNotSupportedException  if SAX is not supported.
     */
    private XMLReader createValidatingReader(List sections)
            throws SAXNotRecognizedException, SAXNotSupportedException {

        XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();

        reader.setContentHandler(new ConfigurationHandler(sections));
        reader.setErrorHandler(new ConfigurationErrorHandler());

        reader.setFeature("http://xml.org/sax/features/namespaces", true); //$NON-NLS-1$
        reader.setFeature("http://xml.org/sax/features/validation", true); //$NON-NLS-1$
        reader.setFeature("http://apache.org/xml/features/validation/schema", true); //$NON-NLS-1$
        reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true); //$NON-NLS-1$

        URL url = getClass().getResource("format-attributes-view.xsd"); //$NON-NLS-1$
        reader.setProperty("http://apache.org/xml/properties/schema/" + //$NON-NLS-1$
                "external-noNamespaceSchemaLocation", url.toExternalForm()); //$NON-NLS-1$

        return reader;
    }

    /**
     * Method invoked whenever an ODOM change event occurs that the main
     * tab needs to react to.
     *
     * @param event the event itself.
     */
    private void mainElementChanged(ODOMChangeEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("ODOMChangeEvent received."); //$NON-NLS-1$
        }
        final ChangeQualifier changeQualifier = event.getChangeQualifier();
        if (changeQualifier == ChangeQualifier.NAME &&
                event.getSource() instanceof Element) {
            // Update the tab title.
            final String key = (String) event.getNewValue();
            if (mainTabDetails != null && mainTabDetails.getItem() != null) {
                mainTabDetails.getItem().setText(getPolicyResourceString(key));
                mainTabDetails.updateControlVisibility();
            }
        }
        mainTabDetails.updateDisplayArea(event);
        mainTabDetails.updateEnablement();
    }

    /**
     * Method invoked whenever an ODOM change event occurs that the row/column
     * tab needs to react to.
     *
     * @param event      the event itself.
     * @param tabDetails the tab details instance.
     */
    private void tabElementChanged(ODOMChangeEvent event,
                                   final TabDetails tabDetails) {
        if (tabDetails != null) {
            tabDetails.updateControlVisibility();
            tabDetails.updateDisplayArea(event);
            tabDetails.updateEnablement();
        }
    }


    // javadoc inherited
    public void setFocus() {
        if (viewer != null) {
            viewer.setFocus();
        }
    }

    // javadoc inherited
    public void init(IPageSite pageSite) {
        super.init(pageSite);

        odomEditorContext.updatePageSiteActions(pageSite);
    }


    public XPathFocusable[] getXPathFocusableControls() {
        List arrayList = new ArrayList(this.mainTabDetails.getSectionDetailsComposites());
        arrayList.addAll(this.columnTabDetails.getSectionDetailsComposites());
        arrayList.addAll(this.rowTabDetails.getSectionDetailsComposites());

        XPathFocusable[] result =
                new XPathFocusable[arrayList.size()];
        arrayList.toArray(result);
        return result;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 03-Aug-04	4902/1	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 24-Feb-04	3021/6	pcameron	VBM:2004020211 Added StyledGroup and background colours

 20-Feb-04	3021/3	pcameron	VBM:2004020211 Undid addition of layoutFormat key to some resources

 19-Feb-04	3021/1	pcameron	VBM:2004020211 Committed for integration

 23-Feb-04	3057/3	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 17-Feb-04	2988/4	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 16-Feb-04	2891/3	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - added comments and renamed methods

 16-Feb-04	2891/1	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - updated FormatAttributesView hierarchy as per rework item

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 ===========================================================================
*/
