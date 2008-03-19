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
package com.volantis.mcs.eclipse.ab.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.controls.ControlsMessages;
import com.volantis.mcs.eclipse.controls.ImageDropDown;
import com.volantis.mcs.eclipse.controls.ImageDropDownItem;
import com.volantis.mcs.eclipse.controls.SecondaryControl;
import com.volantis.mcs.eclipse.controls.SelectionDialogDetails;
import com.volantis.mcs.eclipse.controls.StandardAccessibleListener;
import com.volantis.mcs.eclipse.validation.PolicyNameValidator;
import com.volantis.mcs.objects.FileExtension;


/**
 * A control which allows a user to select literal text or a
 * pre-existing text component.
 */
public class TextDefinition extends Composite implements SecondaryControl {

    /**
     * The index of the TextComponent in the ImageDropDown.
     */
    private static final int TEXT_COMPONENT = 1;

    /**
     * The accessible name for this control.
     */
    private String accessibleName;

    /**
     * The browse button widget.
     */
    private Button browseButton;

    /**
     * The ImageDropDown widget,
     */
    private ImageDropDown imageDropDown;

    /**
     * The Text widget.
     */
    private Text textField;

    /**
     * The current project.
     */
    private ProjectProvider projectProvider;

    /**
     * The PolicyTreeSelectionDialog for browsing
     * text components.
     */
    private PolicyTreeSelectionDialog policyTreeSelectionDialog;

    /**
     * The resource prefix for this control.
     */
    private static final String RESOURCE_PREFIX =
            "TextDefinition.";

    /**
     * The image indicating literal text.
     */
    private static Image literalTextImage;

    /**
     * The image indicating text components.
     */
    private static Image componentTextImage;

    /**
     * The menu item text for literal text.
     */
    private static final String LITERAL_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX +
            "literal.text");

    /**
     * The menu item text for text components.
     */
    private static final String COMPONENT_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX +
            "component.text");

    /**
     * The text for the browse button.
     */
    private static final String BROWSE_TEXT =
            ControlsMessages.getString(RESOURCE_PREFIX +
            "browse");

    /**
     * The horizontal spcing between widgets.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "horizontalSpacing").intValue();

    /**
     * The minimum width for the Text widget.
     */
    private static final int MIN_TEXT_WIDTH =
            ControlsMessages.getInteger(RESOURCE_PREFIX +
            "text.minWidth").intValue();

    /**
     * Store the text item so that we may set the selection to it later.
     */
    private ImageDropDownItem textItem;

    /**
     * Store the component item so that we may set the selection to it later.
     */
    private ImageDropDownItem componentItem;

    /**
     * Constructs a TextDefinition control.
     * @param parent the parent Composite
     * @param style the style for the TextDefinition
     * @param project the project that the TextDefinition uses to access
     * text components
     */
    public TextDefinition(Composite parent, int style, final IProject project) {

        this(parent, style, new ProjectProvider() {
            public IProject getProject() {
                return project;
            }
        });
    }

    /**
     * Constructs a TextDefinition control.
     * @param parent The parent Composite
     * @param style The style for the TextDefinition
     * @param projectProvider The projectProvider that provider the project
     * that the TextDefinition uses to access text components.
     */
    public TextDefinition(Composite parent, int style,
                          ProjectProvider projectProvider) {
        super(parent, style);
        this.projectProvider = projectProvider;
        createControl();
        initAccessible();
    }

    /**
     * Creates and adds the widgets to the TextDefinition control.
     */
    private void createControl() {
        GridLayout layout = new GridLayout(3, false);
        layout.horizontalSpacing = HORIZONTAL_SPACING;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        this.setLayout(layout);
        
        // create and setup the ImageDropDown
        if (literalTextImage == null || literalTextImage.isDisposed()) {
            literalTextImage = ControlsMessages.getImage(RESOURCE_PREFIX + "literal.icon");
        }
        if (componentTextImage == null || componentTextImage.isDisposed()) {
            componentTextImage = ControlsMessages.getImage(RESOURCE_PREFIX + "component.icon");
        }
        //Menu item text and tooltip text are the same for each item.
        textItem = new ImageDropDownItem(literalTextImage, LITERAL_TEXT, LITERAL_TEXT);
        componentItem = new ImageDropDownItem(componentTextImage, COMPONENT_TEXT, COMPONENT_TEXT);

        ImageDropDownItem[] items = new ImageDropDownItem[]{
            textItem, componentItem
        };
        imageDropDown = new ImageDropDown(this, SWT.NONE, items);
        imageDropDown.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                updateBrowseButtonEnabledState();
            }
        });

        // set up the text field
        addText();

        // create and setup the browse button
        browseButton = new Button(this, SWT.PUSH);
        browseButton.setText(BROWSE_TEXT);
        browseButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent selectionEvent) {
                handleBrowseButtonSelection();
            }
        });

        // set the initial state of the browseButton
        updateBrowseButtonEnabledState();

        this.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                TextDefinition.this.disposeResources();
            }
        });

        setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
    }

    /**
     * Updates the enabled state of the browse button. This method exists to centralise the
     * button enable logic.
     */
    private void updateBrowseButtonEnabledState() {
        browseButton.setEnabled(imageDropDown.getItem() == TEXT_COMPONENT);
    }

    /**
     * Creates and adds the Text widget.
     */
    private void addText() {
        textField = new Text(this, SWT.BORDER);
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.widthHint = MIN_TEXT_WIDTH;
        textField.setLayoutData(textGridData);
    }

    /**
     * Handles the browse button selection by creating and displaying
     * a dialog for choosing text components.
     */
    private void handleBrowseButtonSelection() {
        if (policyTreeSelectionDialog == null) {
            policyTreeSelectionDialog = createPolicyTreeSelectionDialog();
        }
        if (policyTreeSelectionDialog.doSelectionDialog() ==
                ElementTreeSelectionDialog.OK) {
            textField.setText(policyTreeSelectionDialog.getPolicyReference());
        }
    }

    /**
     * Creates and returns a PolicyTreeSelectionDialog for text components.
     * @return the selection dialog for text components
     */
    private PolicyTreeSelectionDialog createPolicyTreeSelectionDialog() {
        final String componentName = "textComponent";
        final String message =
                ControlsMessages.getString("PolicySelector.message." + componentName);
        final String title =
                ControlsMessages.getString("PolicySelector.title." + componentName);
        final String emptyListMessage =
                ControlsMessages.getString("PolicySelector.emptyList." + componentName);

        FileExtension[] fileExtensions = new FileExtension[]
        {FileExtension.getFileExtensionForPolicyType(componentName)};

        SelectionDialogDetails selectionDialogDetails = new SelectionDialogDetails
                (componentName, title, message, emptyListMessage, fileExtensions);

        PolicyNameValidator policyNameValidator =
                new PolicyNameValidator(null, fileExtensions, false);

        PolicyTreeSelectionDialog dialog = new PolicyTreeSelectionDialog(
                this.getShell(),
                selectionDialogDetails,
                new QualifiedName(getClass().toString(),
                        RESOURCE_PREFIX + selectionDialogDetails.entityId),
                projectProvider.getProject(),
                policyNameValidator);
        return dialog;
    }

    /**
     * Dispose of the image resources used for the menu items.
     */
    private void disposeResources() {
        if (literalTextImage != null && !literalTextImage.isDisposed()) {
            literalTextImage.dispose();
            literalTextImage = null;
        }
        if (componentTextImage != null && !componentTextImage.isDisposed()) {
            componentTextImage.dispose();
            componentTextImage = null;
        }
    }

    /**
     * Gets the selected text of the TextDefinition. If the
     * selection is a text component, the returned text is
     * enclosed within { and }.
     * @return the selected text in the text field
     */
    public String getText() {
        String text = textField.getText();
        if (imageDropDown.getItem() == TEXT_COMPONENT) {
            text = "{" + text + "}"; //$NON-NLS-2$
        }
        return text;
    }

    /**
     * Set the value of the text control to that passed in.
     * @param value The value of the text to be set.
     */
    public void setText(String value) {
        if (value != null && value.startsWith("{") && value.endsWith("}")) { //$NON-NLS-2$
	        // this is text component so ensure the selection is automatically
            // set to text component. We also strip off the { and } from the
            // beginning and end of the string.
            imageDropDown.setItem(componentItem);
            textField.setText(value.substring(1, value.length() - 1));
        } else {
            imageDropDown.setItem(textItem);
            textField.setText(value);
        }
        // modify the state of the broseButton if necessary.
        updateBrowseButtonEnabledState();

    }

    /**
     * Adds a ModifyListener.
     * @param listener the listener to add
     */
    public void addModifyListener(ModifyListener listener) {
        textField.addModifyListener(listener);
    }

    /**
     * Removes a ModifyListener.
     * @param listener the listener to remove
     */
    public void removeModifyListener(ModifyListener listener) {
        textField.removeModifyListener(listener);
    }

    // javadoc inherited
    public void setSecondaryEnabled(boolean enabled) {
        if (!enabled) {
            imageDropDown.setItem(textItem);
        }
        imageDropDown.setEnabled(enabled);
    }

    /**
     * Create a grid data object with the specified width hint value.
     *
     * @param widthHint the width hint value.
     * @return the newly created GridData object with the specified width hint.
     */
    private GridData createGridData(int widthHint) {
        GridData data = new GridData();
        data.widthHint = widthHint;
        return data;
    }

    // javadoc inherited
    public void setSecondaryVisible(boolean visible) {
        super.setVisible(visible);
        browseButton.setVisible(visible);
        imageDropDown.setVisible(visible);
        GridLayout layout = (GridLayout)getLayout();
        if (visible) {
            // Re-instate horizontal spacing and default width width layout data
            // on 'visible' controls.
            layout.horizontalSpacing = HORIZONTAL_SPACING;
            browseButton.setLayoutData(createGridData(browseButton.computeSize(
                    SWT.DEFAULT, SWT.DEFAULT, true).x));
            imageDropDown.setLayoutData(createGridData(imageDropDown.computeSize(
                    SWT.DEFAULT, SWT.DEFAULT, true).x));
        } else {
            // Remove horizontal spacing and force 0 width layout data on
            // 'invisible' controls.
            layout.horizontalSpacing = 0;
            browseButton.setLayoutData(createGridData(0));
            imageDropDown.setLayoutData(createGridData(0));
        }
        layout(true);
    }

    // javadoc inherited
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textField.setEnabled(enabled);
        updateBrowseButtonEnabledState();
        imageDropDown.setEnabled(enabled);
    }

    /**
     * Getter for the text field control.
     * @return the text field control.
     */
    public Text getTextField() {
        return textField;
    }

    /**
     * Sets the accessible name for this control.
     * @param name The accessible name for this control
     */
    public void setAccessibleName(String name) {
        accessibleName = name;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener textFieldListener =
                new StandardAccessibleListener(textField) {
                    public void getName(AccessibleEvent ae) {
                        ae.result = accessibleName;
                    }
                };

        StandardAccessibleListener imageDropDownListener =
                new StandardAccessibleListener(imageDropDown) {
                    public void getName(AccessibleEvent ae) {
                        ae.result = accessibleName;
                    }
                };

        imageDropDown.getAccessible().addAccessibleListener(
                imageDropDownListener);
        textField.getAccessible().addAccessibleListener(textFieldListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jan-05	6603/1	adrianj	VBM:2004120801 Added names for accessible components

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/2	adrianj	VBM:2004102602 Accessibility support for custom controls

 19-Aug-04	5264/2	allan	VBM:2004081008 Remove invalid plugin dependencies

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 22-Apr-04	3964/11	matthew	VBM:2004032601 change return type of updateBrowseButtonEnabled from boolean to void

 22-Apr-04	3964/9	matthew	VBM:2004032601 setBrowseButtonEnabled() changed to updateBrowseButtonEnabled(). Comment corrected.

 22-Apr-04	3964/7	matthew	VBM:2004032601 reworked code to set the browseButton enabled state. When setItem is called on an ImageDropDown no SelectionEvent results.

 22-Apr-04	3964/5	matthew	VBM:2004032601 rework comments

 22-Apr-04	3964/3	matthew	VBM:2004032601 removed unused and missleading jdoc and inline comments. Added TEXT_COMPONENT.

 21-Apr-04	3964/1	matthew	VBM:2004032601 2004032601 problem setText corrected. Minor refactoring to remove secondary storage of state and to move initialisation of browseButton enabled state to a more appropriate location

 22-Mar-04	3509/3	byron	VBM:2004031610 Link Text and Style Class boxes in dissecting pane attributes remove unused buttons

 23-Feb-04	3057/2	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 18-Feb-04	3068/3	allan	VBM:2004021115 Rework issues.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 02-Feb-04	2707/1	byron	VBM:2003121506 Eclipse PM Layout Editor: hide units control if element is a grid

 28-Jan-04	2752/4	byron	VBM:2004012602 Addressed xxxlinkText enablement for dissecting panes and other bug fixes

 28-Jan-04	2752/2	byron	VBM:2004012602 Address issues from review

 22-Jan-04	2540/2	byron	VBM:2003121505 Added main formats attribute page

 05-Dec-03	2128/9	pcameron	VBM:2003112105 Tweaks to TextDefinition

 ===========================================================================
*/
