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
import com.volantis.mcs.eclipse.ab.editors.devices.validation.ContentPresentElementValidator;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorPart;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMOutlinePage;
import com.volantis.mcs.eclipse.common.odom.MCSNamespace;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeSupport;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilterConfiguration;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidatorDetails;
import com.volantis.mcs.xml.xpath.XPath;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.jdom.Document;

/**
 * The overview page for the device editor. This consists of four
 * form sections. An "alerts and actions" section, a section that displays the
 * device hierarchy in a tree format and two that allow the identification
 * document to be updated (for the selected device)
 */
public class DeviceOverviewPart extends ODOMEditorPart {

    /**
     * The form that contains the FormSections that the overview page
     * requires
     */
    private DeviceOverviewForm deviceOverviewForm;

    /**
     * Initialises a <code>DeviceOverviewPart</code> instance.
     *
     * @param context the {@link DeviceEditorContext} for this overview
     */
    public DeviceOverviewPart(DeviceEditorContext context) {
        // the root element is the hierarchy element
        super(DeviceRepositorySchemaConstants.HIERARCHY_ELEMENT_NAME,
                context);

        context.addSupplementaryValidator(new DOMSupplementaryValidatorDetails(
                MCSNamespace.DEVICE_IDENTIFICATION.getURI(),
                DeviceRepositorySchemaConstants.REGULAR_EXPRESSION_ELEMENT_NAME,
                new ContentPresentElementValidator(
                        DeviceRepositorySchemaConstants.
                                REGULAR_EXPRESSION_ELEMENT_NAME,
                        MCSNamespace.DEVICE_IDENTIFICATION)));

    }

    /**
     * Override getAdapter() to provide the ODOMOutlinePage to callers.
     */
    // rest of javadoc inherited
    public Object getAdapter(Class adapterClass) {
        Object adapter;
        if (IContentOutlinePage.class.equals(adapterClass)) {
            // create the filter
            ODOMSelectionFilter deviceFilter = new ODOMSelectionFilter(null,
                    new String[]{
                            DeviceRepositorySchemaConstants.DEVICE_ELEMENT_NAME},
                    new ODOMSelectionFilterConfiguration(true, true));
            // Note that the outline page is not cached because the page
            // is disposed if the user closes the view and if re-opened
            // must be recreated. There is no dispose listener interface
            // for view parts.
            DeviceEditorContext context =
                    (DeviceEditorContext) getODOMEditorContext();
            DeviceRepositoryAccessorManager dram =
                    context.getDeviceRepositoryAccessorManager();
            // create the OutlinePage
            ODOMOutlinePage outlinePage = new ODOMOutlinePage(
                    getODOMEditorContext(),
                    (ODOMElement) dram.getDeviceHierarchyDocument().
                            getRootElement(),
                    deviceFilter,
                    null, // no skip elements
                    null, // no stop elements
                    false, // attributes are not children
                    new DeviceHierarchyLabelProvider(),
                    null, // no menu manager
                    new DeviceProblemMarkerFinder(dram));

            adapter = outlinePage;
        } else {
            adapter = super.getAdapter(adapterClass);
        }
        return adapter;
    }

    // javadoc inherited
    protected void createPartControlImpl(final Composite parent) {
        // cast the context to a device editor context
        final DeviceEditorContext deviceEditorContext =
                (DeviceEditorContext) getODOMEditorContext();

        ODOMChangeSupport.ChangeSupportDisabledCommand command =
                new ODOMChangeSupport.ChangeSupportDisabledCommand() {
                    public Object execute() {
                        deviceOverviewForm = new DeviceOverviewForm(parent,
                                SWT.NONE,
                                deviceEditorContext);
                        return null;
                    }
                };

        ODOMChangeSupport.executeWithoutChangeSupport(command);
    }

    // javadoc inherited
    protected XPathFocusable[] getXPathFocusableControls() {
        return new XPathFocusable[]{
                deviceOverviewForm
        };
    }

    // javadoc inherited
    public void setFocus() {
        // set the focus to the device overview form
        deviceOverviewForm.setFocus();
    }

    // javadoc inherited
    protected Document getRootElementDocument() {
        return ((DeviceEditorContext) getODOMEditorContext()).
                getDeviceRepositoryAccessorManager().
                getDeviceHierarchyDocument();
    }

    // todo implement this
    // javadoc inherited
    protected void setFocus(XPath xpath) {
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

 16-Nov-04	4394/4	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/4	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 13-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 10-Sep-04	5488/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/3	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Sep-04	5432/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 02-Sep-04	5369/3	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 13-May-04	4321/1	doug	VBM:2004051202 Added label decorating to the device hierarchy tree

 05-May-04	4115/3	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 04-May-04	4007/1	doug	VBM:2004032304 Added a PrimaryPatterns form section

 22-Apr-04	3878/1	doug	VBM:2004032405 Created a basic DeviceEditor and overview page

 ===========================================================================
*/
