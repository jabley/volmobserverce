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

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.dom.ElementChildrenTreeContentProvider;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.FilteringTreeContentProvider;
import com.volantis.mcs.eclipse.common.ObservableProperties;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.jdom.Element;
import org.jdom.Namespace;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide a Tree showing device categories and optionally their policies.
 */
public class CategoriesComposite extends Composite {
    /**
     * The basic content provider that will provide all the children of the
     * root definitions element but no further descendents. In order words
     * no policy elements.
     */
    private static final ITreeContentProvider FIRST_LEVEL_CONTENT_PROVIDER =
            new ElementChildrenTreeContentProvider(new String[]{"types"},
                    new String[]{"category", "types"}, false, false);

    /**
     * Unfortunately it is not possible to use skip and stop elements to
     *  avoid the types element because it is an immeadiate child of the
     * root element. So, we use a FilteringTreeContentProvider for this.
     * This content provider will only provide category elements.
     */
    private static final ITreeContentProvider CATEGORY_CONTENT_PROVIDER =
            new FilteringTreeContentProvider(
                    FIRST_LEVEL_CONTENT_PROVIDER,
                    new FilteringTreeContentProvider.NodeFilter() {
                        // To store the filtered nodes
                        List filtered = new ArrayList();

                        // javadoc inherited
                        public Object[] filter(Object[] nodes) {
                            filtered.clear();
                            // filter out the selected device if
                            // it is in the list of nodes
                            for (int i = 0; i < nodes.length; i++) {
                                if (!DeviceRepositorySchemaConstants.
                                        POLICY_DEFINITION_TYPES_ELEMENT_NAME.
                                        equals(((Element) nodes[i]).getName())) {
                                    filtered.add(nodes[i]);
                                }
                            }
                            return filtered.toArray();
                        }
                    });


    /**
     * The basic content provider that will provide all the children of the
     * root definitions element and their children but no further descendents.
     * In order words category elements, policy elements and the types element.
     */
    private static final ITreeContentProvider SECOND_LEVEL_CONTENT_PROVIDER =
            new ElementChildrenTreeContentProvider(new String[]{"types"},
                    new String[]{"policy", "types"}, false, false);

    /**
     * Unfortunately it is not possible to use skip and stop elements to
     *  avoid the types element because it is an immeadiate child of the
     * root element. So, we use a FilteringTreeContentProvider for this.
     * This content provider will only provide category elements.
     * This content provider will provide category elements and policy
     * elements.
     */
    private static final ITreeContentProvider POLICIES_CONTENT_PROVIDER =
            new FilteringTreeContentProvider(
                    SECOND_LEVEL_CONTENT_PROVIDER,
                    new FilteringTreeContentProvider.NodeFilter() {
                        // To store the filtered nodes
                        List filtered = new ArrayList();

                        // javadoc inherited
                        public Object[] filter(Object[] nodes) {
                            filtered.clear();
                            // filter out the selected device if
                            // it is in the list of nodes
                            for (int i = 0; i < nodes.length; i++) {
                                if (!DeviceRepositorySchemaConstants.
                                        POLICY_DEFINITION_TYPES_ELEMENT_NAME.
                                        equals(((Element) nodes[i]).getName())) {
                                    filtered.add(nodes[i]);
                                }
                            }
                            return filtered.toArray();
                        }
                    });
    /**
     * The TreeViewer for viewing categoryTree.
     */
    private TreeViewer viewer;

    /**
     * Style constant indicating that only categories should be shown in the
     * CategoriesComposite. (Actual value -100).
     */
    public static int CATEGORIES = -100;

    /**
     * Style constant indicating that only categories and policies should be shown
     * in the CategoriesComposite. (Actual value -101).
     */
    public static int POLICIES = -101;

    /**
     * Construct a new CategoriesSection.
     *
     * Styles of CategoriesComposite.CATEGORIES and CategoriesComposite.POLICIES
     * are valid and mutually exclusive with one another.
     *
     * @param parent the Composite
     * @param style the style - CategoriesComposite.CATEGORIES or
     * CategoriesComposite.POLICIES. If style is something other than these
     * two alternatives then the style will default to
     * CategoriesComposite.CATEGORIES.
     * @param dram the DeviceRepositoryAccessorManager from which to obtain
     * the categories and policies.
     */
    public CategoriesComposite(Composite parent, int style,
                               final DeviceRepositoryAccessorManager dram) {

        super(parent, SWT.NONE);

        setLayout(new FillLayout());
        setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        LabelProvider labelProvider =
                new PolicyDefinitionsLabelProvider(dram);

        Composite displayArea = new Composite(this, SWT.BORDER);
        displayArea.setLayout(new FillLayout());
        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        Tree tree = new Tree(displayArea, SWT.SINGLE | SWT.H_SCROLL |
                SWT.V_SCROLL);
        tree.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        viewer = new TreeViewer(tree);

        ITreeContentProvider contentProvider = CATEGORY_CONTENT_PROVIDER;
        if (style == POLICIES) {
            contentProvider = POLICIES_CONTENT_PROVIDER;
        }

        // We need to be notified of changes to the properties file associated
        // with the device repository so that we can refresh the viewer. So,
        // we use an ObservableProperties to provide this facility.
        ObservableProperties op = dram.getProperties();
        op.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                viewer.refresh();
            }
        });

        // We need to be notified of changes to the definitions document
        // associated with the device repository so we can refresh the viewer.
        ODOMElement definitions = (ODOMElement)
                dram.getDeviceDefinitionsDocument().getRootElement();
        definitions.addChangeListener(new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                viewer.refresh();
            }
        });

        viewer.setContentProvider(contentProvider);
        viewer.setLabelProvider(labelProvider);
        viewer.setSorter(new LocalisedCategoriesSorter(labelProvider));
        viewer.setInput(dram.getDeviceDefinitionsDocument().getRootElement());
    }

    /**
     * Provide access to the underlying TreeViewer.
     * @return the TreeViewer that this composite is based around.
     */
    public TreeViewer getTreeViewer() {
        return viewer;
    }

    /**
     * Add a SelectionChange listend that is notified when category selection
     * changes.
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        viewer.addSelectionChangedListener(listener);
    }

    /**
     * Remove a SelectionChange listend that is notified when category selection
     * changes.
     */
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        viewer.removeSelectionChangedListener(listener);
    }

    /**
     * Get the selected category element if any from the categories composite.
     * If a policy is selected then this method will be resolve the
     * category element.
     * @return the selected category element or null if no category or policy
     * element is selected.
     */
    public Element getSelectedCategoryElement() {
        Element categoryElement = null;

        IStructuredSelection categoriesCompositeSelection =
                (IStructuredSelection) getTreeViewer().getSelection();

        if (!categoriesCompositeSelection.isEmpty()) {
            Element selectedElement = (Element)
                    categoriesCompositeSelection.getFirstElement();

            if (selectedElement.getName().
                    equals(DeviceRepositorySchemaConstants.
                    POLICY_ELEMENT_NAME)) {

                StringBuffer xPathBuffer = new StringBuffer();
                xPathBuffer.append("ancestor::").//$NON-NLS-1$
                        append(MCSNamespace.DEVICE_DEFINITIONS.getPrefix()).
                        append(':').
                        append(DeviceRepositorySchemaConstants.
                        CATEGORY_ELEMENT_NAME);
                XPath categoryXPath = new XPath(xPathBuffer.toString(),
                        new Namespace[]{MCSNamespace.DEVICE_DEFINITIONS});
                try {
                    categoryElement =
                            categoryXPath.selectSingleElement(selectedElement);
                } catch (XPathException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                }
            } else if (selectedElement.getName().
                    equals(DeviceRepositorySchemaConstants.
                    CATEGORY_ELEMENT_NAME)) {
                categoryElement = selectedElement;
            }
        }
        return categoryElement;
    }

    /**
     * Get the selected policy element if any from the categories composite.
     * @return the selected policy element or null if no policy element is
     * selected.
     */
    public Element getSelectedPolicyElement() {
        Element selectedPolicyElement = null;
        IStructuredSelection categoriesCompositeSelection =
                (IStructuredSelection) getTreeViewer().getSelection();

        if (!categoriesCompositeSelection.isEmpty()) {
            Element selectedElement = (Element)
                    categoriesCompositeSelection.getFirstElement();

            if (selectedElement.getName().
                    equals(DeviceRepositorySchemaConstants.
                    POLICY_ELEMENT_NAME)) {
                selectedPolicyElement = selectedElement;
            }
        }
        return selectedPolicyElement;
    }

    /**
     * A sorter which sorts category names according to their localised names
     * and the default locale.
     */
    private class LocalisedCategoriesSorter extends ViewerSorter {

        /**
         * The LabelProvider used to retrieve localised category names.
         */
        private final LabelProvider labelProvider;

        /**
         * The default collator which provides the sorting rules for the
         * current locale.
         */
        private final Collator collator = Collator.getInstance();

        /**
         * Creates a new LocalisedCategoriesSorter.
         * @param labelProvider the label provider to use for retrieving the
         * localised names being displayed.
         * @throws IllegalArgumentException if labelProvider is null
         */
        public LocalisedCategoriesSorter(LabelProvider labelProvider) {
            if (labelProvider == null) {
                throw new IllegalArgumentException("Cannot be null: " +
                        "labelProvider.");
            }
            this.labelProvider = labelProvider;
        }

        /**
         * Overridden to retrieve and sort on localised category names.
         */
        public int compare(Viewer viewer, Object o1, Object o2) {

            // Sort according to the locale-specific collation order.
            return collator.compare(labelProvider.getText(o1),
                    labelProvider.getText(o2));
        }
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

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 11-May-04	4161/5	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 11-May-04	4161/2	doug	VBM:2004031604 Added the PolicyDefinitionSection composite

 11-May-04	4284/1	byron	VBM:2004051105 Device editor permits creating policies for child elements

 10-May-04	4068/3	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/1	allan	VBM:2004032103 Structure page policies section.

 ===========================================================================
*/
