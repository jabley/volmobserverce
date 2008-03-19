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
import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.xml.validation.sax.ParserErrorException;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;
import org.xml.sax.SAXException;

/**
 * The FormSection for showing and allowing selection of device policy
 * categories.
 */
public class CategoriesSection extends FormSection {
    /**
     * The prefix for property resources associated with this class.
     */
    private static final String RESOURCE_PREFIX = "CategoriesSection.";

    /**
     * The default minimum width for categories sections.
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
    public CategoriesSection(Composite parent, int style,
                             DeviceEditorContext context) {
        super(parent, style);

        setMinWidth(DEFAULT_MIN_WIDTH);
        this.context = context;
        createDisplayArea(TITLE, MESSAGE);
    }

    /**
     * Create the categoriesComposite for this section.
     */
    private void createDisplayArea(String title, String message) {
        DeviceRepositoryAccessorManager dram =
                context.getDeviceRepositoryAccessorManager();
        try {
            context.addRootElement((ODOMElement) dram.
                    getDeviceDefinitionsDocument().getRootElement(),
                    DeviceRepositorySchemaConstants.DEFINITIONS_ELEMENT_NAME);
        } catch (SAXException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        } catch (ParserErrorException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }

        Section section =
                SectionFactory.createSection(this, SWT.NONE, title, message);

        GridData data = new GridData(GridData.FILL_BOTH);
        section.setLayoutData(data);
        categoriesComposite = new CategoriesComposite(section,
                CategoriesComposite.CATEGORIES, dram);
        section.setClient(categoriesComposite);

        data = new GridData(GridData.FILL_BOTH);
        categoriesComposite.setLayoutData(data);

        Tree tree = categoriesComposite.getTreeViewer().getTree();
        TreeItem items [] = tree.getItems();
        tree.setSelection(new TreeItem[]{items[0]});
    }

    /**
     * Get the currently selected category.
     * @return the currently selected category or null if
     * none are selected.
     */
    public String getSelectedCategory() {
        String selectedCategory = null;
        Element categoryElement = categoriesComposite.
                getSelectedCategoryElement();
        if (categoryElement != null) {
            selectedCategory = categoryElement.
                    getAttributeValue(DeviceRepositorySchemaConstants.
                    CATEGORY_NAME_ATTRIBUTE);
        }

        return selectedCategory;
    }

    /**
     * Add a SelectionChange listener that is notified when category selection
     * changes.
     */
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        categoriesComposite.addSelectionChangedListener(listener);
    }

    /**
     * Remove a SelectionChange listend that is notified when category selection
     * changes.
     */
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        categoriesComposite.removeSelectionChangedListener(listener);
    }

    /**
     * Override setFocus() to set both the focus to
     * the CategoriesCamposite.
     */
    public boolean setFocus() {
        return categoriesComposite.getTreeViewer().getControl().setFocus();
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

 16-Nov-04	6218/1	adrianj	VBM:2004102021 Enhanced sizing for FormSections

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 11-May-04	4272/1	allan	VBM:2004050503 Unique problem markers fix for device editor.

 10-May-04	4068/5	allan	VBM:2004032103 Added actions to DeviceDefinitionsPoliciesSection.

 06-May-04	4068/3	allan	VBM:2004032103 Structure page policies section.

 29-Apr-04	4109/2	pcameron	VBM:2004042701 Added sorter for localised category names

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 ===========================================================================
*/
