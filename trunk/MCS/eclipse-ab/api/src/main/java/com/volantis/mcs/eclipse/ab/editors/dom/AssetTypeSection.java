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
/*
----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 *
----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.PresentableItem;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ComboViewer;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.ControlUtils;
import com.volantis.mcs.eclipse.controls.SingleComponentACL;
import com.volantis.mcs.eclipse.controls.StandardAccessibleListener;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import com.volantis.mcs.objects.PropertyValueLookUp;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.Section;

import java.util.List;


/**
 * Creates an AssetTypeSection for use in component editors.
 */
public class AssetTypeSection extends FormSection implements XPathFocusable {

    /**
     * The resource prefix for this control.
     */
    private static final String RESOURCE_PREFIX =
                "AssetTypeSection."; //$NON-NLS-1$

    /**
     * The label text.
     */
    private static final String LABEL_TEXT = EditorMessages.
            getString(RESOURCE_PREFIX +
            "label.text"); //$NON-NLS-1$

    /**
     * The name of the combo box.
     */
    private static final String DROP_DOWN_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX + "dropDown");

    /**
     * The display area control.
     */
    private Composite displayArea;

    /**
     * The ODOMElement used by the AssetTypeSection.
     */
    private ODOMElement odomElement;

    /**
     * The change listener for the ODOM element.
     */
    private ODOMChangeListener odomChangeListener;

    /**
     * The ModifyListener for the ComboViewer.
     */
    private ModifyListener modifyListener;

    /**
     * The ComboViewer used by the AssetTypeSection.
     */
    private ComboViewer assetTypeCombo;

    /**
     * The name of the element which the AssetTypeSection uses.
     */
    private String elementName;

    /**
     * Creats an AssetTypeSection form for use in component editors.
     * @param parent the parent Composite
     * @param style the style for the parent
     * @param elementName the name of the element used by the form
     * @param odomElement the ODOM element used by the form
     * @param title the title for the form
     * @param message the message for the form
     */
    public AssetTypeSection(Composite parent, int style,
                            String elementName,
                            ODOMElement odomElement,
                            String title,
                            String message) {
        super(parent, style);
        if (odomElement == null) {
            throw new IllegalArgumentException(
                        "Cannot be null: odomElement."); //$NON-NLS-1$
        }
        if (elementName == null || elementName.length() == 0) {
            throw new IllegalArgumentException(
                        "Cannot be null or empty: " + //$NON-NLS-1$
                        "elementName."); //$NON-NLS-1$
        }
        this.elementName = elementName;
        this.odomElement = odomElement;
        createDisplayArea(title, message);
        initAccessible();
    }

    /**
     * Creates the display area of the form.
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, SWT.NONE, title, message);

        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);
        final Color white = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
        this.setBackground(white);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        final int horizontalSpacing = Integer.parseInt(EditorMessages.
                getString(RESOURCE_PREFIX + "horizontalSpacing"));
        gridLayout.horizontalSpacing = horizontalSpacing;
        displayArea.setLayout(gridLayout);
        displayArea.setBackground(white);
        GridData displayAreaData = new GridData(GridData.FILL_HORIZONTAL);
        displayArea.setLayoutData(displayAreaData);

        Label assetLabel = new Label(displayArea, SWT.NONE);
        assetLabel.setBackground(white);
        assetLabel.setText(LABEL_TEXT);
        addCombo();
        assetTypeCombo.setBackground(white);
        odomChangeListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node, ODOMChangeEvent event) {
                updateDisplayArea(event);
            }
        };
        odomElement.addChangeListener(odomChangeListener);

        if (odomElement.getName().equals(ODOMElement.NULL_ELEMENT_NAME)) {
            ControlUtils.setEnabledHierarchical(displayArea, false);
        }
    }

    /**
     * Creates and adds a ComboViewer.
     */
    private void addCombo() {
        List elements = PropertyValueLookUp.getDependentElements(elementName);
        PresentableItem[] items = new PresentableItem[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            String assetName = (String) elements.get(i);
            String resourceName = EclipseCommonMessages.getString(
                        "PolicyName." + assetName); //$NON-NLS-1$
            items[i] = new PresentableItem(assetName, resourceName);
        }
        assetTypeCombo = new ComboViewer(displayArea, SWT.READ_ONLY |
                SWT.DROP_DOWN, items);
        GridData comboData = new GridData(GridData.FILL_HORIZONTAL);
        assetTypeCombo.setLayoutData(comboData);
        String name = odomElement.getName();
        // On Windows it is necessary to deselect to set to something not
        // in the Combo.
        assetTypeCombo.getCombo().deselectAll();
        if (name.equals(ODOMElement.UNDEFINED_ELEMENT_NAME)) {
            assetTypeCombo.setValue("");
        } else {
            assetTypeCombo.setValue(name);
        }
        modifyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                updateODOMElement();
            }
        };
        assetTypeCombo.addModifyListener(modifyListener);
    }

    /**
     * Handles selection events from the ComboViewer, by updating
     * the name of the ODOM element.
     */
    private void updateODOMElement() {
        String name = (String) assetTypeCombo.getValue();
        if (name != null && name.length() > 0) {
            odomElement.removeChangeListener(odomChangeListener);
            odomElement.setName(name);
            odomElement.addChangeListener(odomChangeListener);
        }
    }

    /**
     * Updates the display area of the form after an ODOM element has
     * changed. If the ODOMElement was removed, clear the ComboViewer's
     * text field and disable the form. Otherwise, enable the form and update
     * the ComboViewer with the new display name for the new name of
     * the ODOM element.
     * @param event the ODOMChangeEvent
     */
    private void updateDisplayArea(ODOMChangeEvent event) {
        assetTypeCombo.removeModifyListener(modifyListener);
        if (event.getChangeQualifier() == ChangeQualifier.NAME) {
            // On Windows it is necessary to deselect to set to something not
            // in the Combo.
            assetTypeCombo.getCombo().deselectAll();
            if(ODOMElement.NULL_ELEMENT_NAME.equals(odomElement.getName())) {
                assetTypeCombo.setValue("");
                ControlUtils.setEnabledHierarchical(displayArea, false);
            } else if(ODOMElement.UNDEFINED_ELEMENT_NAME.
                    equals(odomElement.getName())) {
                assetTypeCombo.setValue("");
                ControlUtils.setEnabledHierarchical(displayArea, true);
            } else {
                assetTypeCombo.setValue(odomElement.getName());
                ControlUtils.setEnabledHierarchical(displayArea, true);
            }
        }
        assetTypeCombo.addModifyListener(modifyListener);
    }

    //javadoc inherited
    protected Control getDisplayArea() {
        return displayArea;
    }

    // javadoc
    public boolean setFocus(XPath path) {
        // todo implement this method
        return false;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL() {
            public void getValue(AccessibleControlEvent ae) {
                ae.result = (String)assetTypeCombo.getValue();
            }
        };
        acl.setControl(this);
        acl.setRole(ACC.ROLE_COMBOBOX);
        getAccessible().addAccessibleControlListener(acl);

        StandardAccessibleListener al = new StandardAccessibleListener(assetTypeCombo) {
            public void getName(AccessibleEvent event) {
                event.result = DROP_DOWN_TEXT;
            }
        };
        assetTypeCombo.getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-May-05	8452/3	matthew	VBM:2005011006 Add accesibility name to ComboViewer

 24-May-05	8452/1	matthew	VBM:2005011006 Add accesibility name to ComboViewer

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 27-Sep-04	5652/1	adrianj	VBM:2004090813 Correct enable/disable of sections when editing image assets

 07-Sep-04	5429/1	allan	VBM:2004081624 Ensure type field updates properly on Windows

 06-Sep-04	5423/1	allan	VBM:2004081624 Ensure type field updates properly on Windows

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 05-Jan-04	2393/1	pcameron	VBM:2004010204 ComboViewer now also uses ModifyListeners

 15-Dec-03	2213/3	allan	VBM:2003121401 Commit to make available to Doug

 14-Dec-03	2208/5	allan	VBM:2003121201 Move PresentableItem to eclipse.common

 13-Dec-03	2208/3	allan	VBM:2003121201 Use PresentableItems for presenting attribute values.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 09-Dec-03	2170/1	pcameron	VBM:2003102103 Added AssetTypeSection

 ===========================================================================
*/
