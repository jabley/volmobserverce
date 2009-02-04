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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.util.ListenerList;
import com.volantis.mcs.eclipse.builder.editors.themes.PolicySelectorBrowseAction;
import com.volantis.mcs.eclipse.builder.editors.EditorMessages;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.objects.FileExtension;

/**
 * An editor for policy references.
 */
public class PolicyReferenceEditor extends Composite {
    /**
     * The resource prefix for this class.
     */
    private static final String RESOURCE_PREFIX = "PolicyReferenceEditor.";

    /**
     * The label for the browse button.
     */
    private static final String BROWSE_BUTTON_LABEL =
            EditorMessages.getString(RESOURCE_PREFIX + "browse.label");

    /**
     * The text field in which the policy reference is displayed/edited.
     */
    private Text textEntry;

    /**
     * The browse button for selecting an existing policy.
     */
    private Button browseButton;

    /**
     * The type of policy edited by this reference editor.
     */
    private PolicyType policyType;

    /**
     * The context in which this policy is being edited.
     */
    private EditorContext context;

    /**
     * A list of modify listeners registered against this editor.
     */
    private ListenerList listeners = new ListenerList();

    /**
     * Create a new policy reference editor for the specified policy type and
     * editor context.
     *
     * @param composite The parent composite
     * @param style The SWT style flags
     * @param policyType The type of policy to edit
     * @param context The editor context
     */
    public PolicyReferenceEditor(Composite composite, int style,
                                 PolicyType policyType,
                                 EditorContext context) {
        super(composite, style);
        this.policyType = policyType;
        this.context = context;
        createDisplay();
    }

    /**
     * Create the graphical components used by this editor.
     */
    private void createDisplay() {
        GridLayout layout = new GridLayout(2, false);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        setLayout(layout);

        textEntry = new Text(this, SWT.BORDER);

        GridData data = new GridData(
                GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL);
        textEntry.setLayoutData(data);

        textEntry.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent event) {
                valueChanged();
            }
        });

        if (policyType != null) {
            browseButton = new Button(this, SWT.NONE);
            final PolicySelectorBrowseAction psba =
                    new PolicySelectorBrowseAction(
                            FileExtension.getFileExtensionForPolicyType(
                                    policyType));

            data = new GridData(GridData.HORIZONTAL_ALIGN_END);
            browseButton.setLayoutData(data);
            browseButton.setText(BROWSE_BUTTON_LABEL);
            browseButton.addSelectionListener(new SelectionListener() {
                private void handleButtonPress() {
                    String newTextValue = psba.doBrowse(textEntry.getText(),
                            textEntry.getParent(), context);
                    if (!textEntry.getText().equals(newTextValue)) {
                        textEntry.setText(newTextValue);
                    }
                }

                public void widgetSelected(SelectionEvent event) {
                    handleButtonPress();
                }

                public void widgetDefaultSelected(SelectionEvent event) {
                    handleButtonPress();
                }
            });
        }
    }

    // Javadoc not required
    public PolicyReference getValue() {
        if ("".equals(textEntry.getText())) {
            return null;
        }
        return PolicyFactory.getDefaultInstance().createPolicyReference(
                textEntry.getText(), policyType);
    }

    // Javadoc not required
    public void setValue(PolicyReference newValue) {
        if (newValue == null) {
            textEntry.setText("");
        } else {
            textEntry.setText(newValue.getName());
        }
    }

    /**
     * Handle changes to the value of the policy reference represented by this
     * editor, by firing events to any registered listeners.
     */
    private void valueChanged() {
        Event event = new Event();
        event.widget = this;
        String value = textEntry.getText();
        event.data = value;
        event.text = value;
        ModifyEvent modifyEvent = new ModifyEvent(event);
        Object interested[] = listeners.getListeners();
        for (int i = 0; i < interested.length; i++)
        if (interested[i] != null) {
            ((ModifyListener) interested[i]).modifyText(modifyEvent);
        }
    }

    // Javadoc not required
    public void addModifyListener(ModifyListener listener) {
        listeners.add(listener);
    }

    // Javadoc not required
    public void removeModifyListener(ModifyListener listener) {
        listeners.remove(listener);
    }

    // Javadoc inherited
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textEntry.setEnabled(enabled);
        if (browseButton != null) {
            browseButton.setEnabled(enabled);
        }
    }

    // Javadoc inherited
    public void setBackground(Color color) {
        super.setBackground(color);
        textEntry.setBackground(color);
    }
}
