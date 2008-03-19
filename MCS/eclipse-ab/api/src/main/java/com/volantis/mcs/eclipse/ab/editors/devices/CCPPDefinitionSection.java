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

import java.util.Iterator;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoManager;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ComboActionable;
import com.volantis.mcs.eclipse.controls.TextActionable;
import com.volantis.mcs.eclipse.controls.XPathFocusable;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.jdom.Element;

/**
 * Form section that can be use to view and edit a CCPP definitions<p>
 *
 * Repository XML example: <pre>
 *     &lt;policy name="UAProf.WapPushMsgSize" ccppVocabulary="UAProf"&gt;
 *       &lt;type&gt;
 *           &lt;int/&gt;
 *       &lt;/type&gt;
 *       &lt;UAProf attribute="WapPushMsgSize"/&gt;
 *    &lt;/policy&gt;
 * </pre>
 */
public class CCPPDefinitionSection
        extends FormSection implements XPathFocusable {

    /**
     * The prefix for resource messages associated with this class.
     */
    private static final String RESOURCE_PREFIX =
            "CCPPDefinitionSection."; //$NON-NLS-1$

    /**
     * The title for this form section.
     */
    private static final String TITLE =
            DevicesMessages.getString(RESOURCE_PREFIX + "title");

    /**
     * The message for this form section.
     */
    private static final String MESSAGE =
            DevicesMessages.getString(RESOURCE_PREFIX + "message");

    /**
     * The display item used in the combo box.
     */
    private static final String UAPROF_DISPLAY_NAME = "UAProf";

    /**
     * The composite that this section uses to display its controls.
     */
    private Composite displayArea;

    /**
     * The control for handling vocabulary values.
     */
    private Combo vocabulary;

    /**
     * The listener for the vocabulary control.
     */
    private ModifyListener vocabularyListener;

    /**
     * The control for handling UAProf name values.
     */
    private Text uaProfName;

    /**
     * The listener for the UAProf name control.
     */
    private ModifyListener uaProfNameListener;

    /**
     * The label control for UAProf.
     */
    private Label uaProfLabel;

    /**
     * The label control for Vocabulary.
     */
    private Label vocabularyLabel;

    /**
     * The currently selected policy element.
     */
    private ODOMElement selectedElement;

    /**
     * Listen to changes on the selected element in order to propogate undo and
     * redo operations to the display.
     */
    private ODOMChangeListener selectedElementListener;

    /**
     * The {@link DeviceEditorContext} used for undo/redo actions.
     */
    private DeviceEditorContext context;

    /**
     * Initializes a <code>CCPPDefinitionSection</code> with the specified
     * parameters.
     *
     * @param parent  the parent composite
     * @param style   the style bitset
     * @param context the <code>DeviceEditorContext</code> context.
     */
    public CCPPDefinitionSection(Composite parent,
                                 int style,
                                 DeviceEditorContext context) {
        super(parent, style);
        this.context = context;

        createDisplayArea(TITLE, MESSAGE);
        createListeners();
    }

    // javadoc inherited
    public boolean setFocus(XPath path) {
        // todo this needs implementing
        return false;
    }

    /**
     * Creates the display area for this composite
     */
    private void createDisplayArea(String title, String message) {
        Section section =
                SectionFactory.createSection(this, title, message);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        section.setClient(displayArea);

        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        displayArea.setLayout(layout);
        displayArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        displayArea.setBackground(getColour(SWT.COLOR_LIST_BACKGROUND));

        // add the list value builder to the display area
        vocabularyLabel = new Label(displayArea, SWT.NONE);
        vocabularyLabel.setBackground(getColour(SWT.COLOR_LIST_BACKGROUND));
        vocabularyLabel.setText(DevicesMessages.getString(RESOURCE_PREFIX +
                "vocabulary.label"));

        vocabulary = new Combo(displayArea, SWT.READ_ONLY);
        context.getHandler().addControl(new ComboActionable(vocabulary));
        vocabulary.add("");
        vocabulary.add(UAPROF_DISPLAY_NAME);
        vocabulary.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        uaProfLabel = new Label(displayArea, SWT.NONE);
        uaProfLabel.setBackground(getColour(SWT.COLOR_LIST_BACKGROUND));
        uaProfLabel.setText(DevicesMessages.getString(RESOURCE_PREFIX +
                "uaprofName.label"));

        uaProfName = new Text(displayArea, SWT.SINGLE | SWT.BORDER);
        context.getHandler().addControl(new TextActionable(uaProfName));
        uaProfName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        setEnabled(true);
    }

    /**
     * Helper method for obtaining the SWT colour.
     *
     * @param colour the colour as an integer (e.g. SWT.WHITE)
     * @return the SWT sytem colour.
     */
    private Color getColour(int colour) {
        return getDisplay().getSystemColor(colour);
    }

    /**
     * Add the vocabulary listener.
     */
    private void addVocabularyListener() {
        if ((vocabulary != null) && !vocabulary.isDisposed()) {
            vocabulary.addModifyListener(vocabularyListener);
        }
    }

    /**
     * Add the UAProf listener.
     */
    private void addUAProfListener() {
        if ((uaProfName != null) && !uaProfName.isDisposed()) {
            uaProfName.addModifyListener(uaProfNameListener);
        }
    }

    /**
     * Remove the vocabulary listener.
     */
    private void removeVocabularyListener() {
        if ((vocabulary != null) && !vocabulary.isDisposed()) {
            vocabulary.removeModifyListener(vocabularyListener);
        }
    }

    /**
     * Remove the UAProf listener.
     */
    private void removeUAProfListener() {
        if ((uaProfName != null) && !uaProfName.isDisposed()) {
            uaProfName.removeModifyListener(uaProfNameListener);
        }
    }

    // javadoc inherited.
    public void dispose() {
        removeVocabularyListener();
        removeUAProfListener();
        if (selectedElement != null) {
            selectedElement.removeChangeListener(selectedElementListener);
        }
        super.dispose();
    }

    // javadoc inherited.
    public void setEnabled(boolean enabled) {
        boolean isUAPROFVocabulary = false;

        if (vocabulary != null) {
            vocabulary.setEnabled(enabled);
            int selctedIndex = vocabulary.getSelectionIndex();
            if (selctedIndex != -1) {
                isUAPROFVocabulary = UAPROF_DISPLAY_NAME.equals(
                        vocabulary.getItem(selctedIndex));
            }
        }

        if (vocabularyLabel != null) {
            vocabularyLabel.setEnabled(enabled);
        }
        // UAProf controls are disabled is the vocabulary is not UAProf
        if (uaProfLabel != null) {
            uaProfLabel.setEnabled(enabled && isUAPROFVocabulary);
        }

        if (uaProfName != null) {
            uaProfName.setEnabled(enabled && isUAPROFVocabulary);
        }
    }

    /**
     * Return the vocabulary value extracted from the selected element.
     * @return the vocabulary value extracted from the selected element.
     */
    private String getVocabulary() {
        String result = null;
        if (selectedElement != null) {
            result = selectedElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.VOCABULARY_ATTRIBUTE_NAME);
        }
        return result;
    }

    /**
     * Return the UAProf value extracted from the selected element.
     * @return the UAProf value extracted from the selected element.
     */
    private String getUAProfName() {
        String result = null;
        Element uaProfElement = getUAProfElement();
        if (uaProfElement != null) {
            result = uaProfElement.getAttributeValue(
                    DeviceRepositorySchemaConstants.UAPROF_ATTRIBUTE_NAME);
        }
        return result;
    }

    /**
     * Helper method for locating and returning the child UAProf element.
     *
     * @return he child UAProf element or null if not found.
     */
    private Element getUAProfElement() {
        Element result = null;
        if (selectedElement != null) {
            Iterator children = selectedElement.getChildren().iterator();
            boolean found = false;
            while (children.hasNext() && !found) {
                Element e = (Element) children.next();
                if (e.getName().equals(
                        DeviceRepositorySchemaConstants.UAPROF_ELEMENT_NAME)) {
                    found = true;
                    result = e;
                }
            }
        }
        return result;
    }

    /**
     * Initializes the various listeners that are required by this section.
     */
    private void createListeners() {
        // UAProf name modify listener.
        uaProfNameListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                updateUAProfName(uaProfName.getText());
            }
        };
        uaProfName.addModifyListener(uaProfNameListener);

        // Vocabulary combo box listener.
        vocabularyListener = new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                String newValue = vocabulary.getItem(vocabulary.getSelectionIndex());
                boolean enabled = UAPROF_DISPLAY_NAME.equals(newValue);
                uaProfName.setEnabled(enabled);
                uaProfLabel.setEnabled(enabled);
                updateVocabulary(newValue);
                if (!enabled) {
                    // Blank out the UAProf name.
                    setUAProfName("");
                }
            }
        };
        vocabulary.addModifyListener(vocabularyListener);

        selectedElementListener = new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                setVocabulary(getVocabulary());
                setUAProfName(getUAProfName());
            }
        };
    }

    /**
     * Update the element with the new value(s)
     *
     * @param vocabulary the new value for the vocabulary.
     */
    private void updateVocabulary(String vocabulary) {
        selectedElement.removeChangeListener(selectedElementListener);
        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();
        try {
            if (vocabulary != null && vocabulary.length() > 0) {
                selectedElement.setAttribute(
                        DeviceRepositorySchemaConstants.VOCABULARY_ATTRIBUTE_NAME,
                        vocabulary);
            } else {
                selectedElement.removeAttribute(
                        DeviceRepositorySchemaConstants.VOCABULARY_ATTRIBUTE_NAME);
                updateUAProfName(null);
            }
        } finally {
            selectedElement.removeChangeListener(selectedElementListener);
            undoRedoManager.demarcateUOW();
        }
    }

    /**
     * Update the element with the new value(s)
     * @param uaProfName the UAProf name.
     */
    private void updateUAProfName(String uaProfName) {
        selectedElement.removeChangeListener(selectedElementListener);

        UndoRedoManager undoRedoManager = context.getUndoRedoManager();
        undoRedoManager.demarcateUOW();

        try {
            Element uaProfElement = getUAProfElement();
            if ((uaProfName == null) || (uaProfName.trim().length() == 0)) {
                if (uaProfElement != null) {
                    uaProfElement.detach();
                }
            } else {
                if (uaProfElement == null) {
                    // create a new element
                    uaProfElement = context.getODOMFactory().element(
                            DeviceRepositorySchemaConstants.UAPROF_ELEMENT_NAME,
                            selectedElement.getNamespace());
                    selectedElement.addContent(uaProfElement);
                }
                uaProfElement.setAttribute(
                        DeviceRepositorySchemaConstants.UAPROF_ATTRIBUTE_NAME,
                        uaProfName);
            }
        } finally {
            selectedElement.addChangeListener(selectedElementListener);
            undoRedoManager.demarcateUOW();
        }
    }

    /**
     * Update the control with the specified value.
     * @param value the vocabulary control value (an item in the combo box). If
     * the item isn't found it is ignored.
     */
    public void setVocabulary(String value) {
        boolean found = false;

        int index = 0;
        if (value == null) {
            value = "";
        }

        String items[] = vocabulary.getItems();
        for (index = 0; !found && (index < items.length); index++) {
            if (value.equals(items[index])) {
                found = true;
            }
        }
        if (found) {
            try {
                removeVocabularyListener();
                vocabulary.select(index - 1);
            } finally {
                addVocabularyListener();
            }
        }
    }

    /**
     * Update the UAProf name control with the specified value.
     * @param value the new name value for UAProf.
     */
    public void setUAProfName(String value) {
        removeUAProfListener();
        try {
            if (value == null) {
                uaProfName.setText("");
            } else {
                uaProfName.setText(value);
            }
        } finally {
            addUAProfListener();
        }
    }

    /**
     * Called when the policy selection has changed.
     * @param policyElement the selected element.
     */
    public void setElement(ODOMElement policyElement) {
        if (policyElement != selectedElement) {
            selectedElement = policyElement;
            selectedElement.removeChangeListener(selectedElementListener);
            setVocabulary(getVocabulary());
            setUAProfName(getUAProfName());
            selectedElement.addChangeListener(selectedElementListener);
        }
        setEnabled(true);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jan-05	6643/1	allan	VBM:2004090913 Expand all Sections at creation time.

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Nov-04	4394/3	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/4	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 12-May-04	4288/4	doug	VBM:2004051107 Ensured RHS of Structure page is disabled for non admin user

 10-May-04	4237/3	byron	VBM:2004031601 Provide the CCPP form section - update

 10-May-04	4237/1	byron	VBM:2004031601 Provide the CCPP form section

 ===========================================================================
*/
