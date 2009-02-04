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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;


/**
 * A MessageAreaSelectionDialog that provides an area on a dialog for
 * displaying an error message with an icon.
 */
public class MessageAreaSelectionDialog extends SelectionDialog {

    /**
     * The error message for the message area.
     */
    private String errorMessage;

    /**
     * The label used for the error icon.
     */
    private Label imageLabel;

    /**
     * The label used for the error message text.
     */
    private Label messageLabel;

    /**
     * The Composite containing the two labels.
     */
    private Composite labelComposite;

    /**
     * The image icon for error messages.
     */
    private Image errorImage;

    /**
     * The color of the message background.
     * This color is specified by the RGB
     * triple below.
     */
    private Color labelBackground;

    /**
     * The RGB representation for the message background. This value
     * was determined by examining the color that Eclipse uses on
     * Linux and Windows for error message background.
     */
    private static final RGB LABEL_BACKGROUND_RGB = new RGB(230, 226, 221);

    /**
     * The default label background.
     */
    private Color originalBackground;

    /**
     * Resource prefix for the dialog.
     */
    private static final String MESSAGE_AREA_SELECTION_DIALOG_PREFIX = "MessageAreaSelectionDialog."; //$NON-NLS-1$


    /**
     * The horizontal spacing between the two labels.
     */
    private static final int HORIZONTAL_SPACING =
            ControlsMessages.getInteger(MESSAGE_AREA_SELECTION_DIALOG_PREFIX + "horizontalSpacing").intValue(); //$NON-NLS-1$

    /**
     * Constructor for a MessageAreaSelectionDialog.
     * @param parent the parent shell of the dialog
     */
    public MessageAreaSelectionDialog(Shell parent) {
        super(parent);
    }

    /**
     * Sets the error message for the message area and also
     * displays an error icon.
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        if (messageLabel != null) {
            if (errorMessage == null || errorMessage.length() == 0) {
                messageLabel.setText(""); //$NON-NLS-1$
                messageLabel.setToolTipText("");
                imageLabel.setImage(null);
                imageLabel.setBackground(originalBackground);
                messageLabel.setBackground(originalBackground);
            } else {
                if (errorImage == null) {
                    errorImage = ControlsMessages.getImage(MESSAGE_AREA_SELECTION_DIALOG_PREFIX + "images.error"); //$NON-NLS-1$
                    errorImage.setBackground(originalBackground);
                }
                if (labelBackground == null) {
                    labelBackground = new Color(messageLabel.getDisplay(), LABEL_BACKGROUND_RGB);
                }
                errorImage.setBackground(labelBackground);
                imageLabel.setImage(errorImage);
                imageLabel.setBackground(labelBackground);
                messageLabel.setBackground(labelBackground);
                messageLabel.setText(errorMessage);
                messageLabel.setToolTipText(errorMessage);
            }
            labelComposite.layout();
        }
    }

    /**
     * Gets the error message.
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Creates the error message area for the dialog.
     * @param parent the Composite parent for the message area
     * @return the message area control
     */
    protected Control createErrorMessageArea(Composite parent) {
        labelComposite = new Composite(parent, SWT.NONE);
        GridLayout grid = new GridLayout();
        grid.numColumns = 2;
        grid.marginHeight = 0;
        grid.marginWidth = 0;
        grid.horizontalSpacing = HORIZONTAL_SPACING;
        labelComposite.setLayout(grid);
        imageLabel = new Label(labelComposite, SWT.NONE);
        originalBackground = labelComposite.getBackground();
        GridData data2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL |
                                      GridData.VERTICAL_ALIGN_BEGINNING);
        imageLabel.setLayoutData(data2);
        messageLabel = new Label(labelComposite, SWT.WRAP);
        GridData data = new GridData(GridData.FILL_BOTH);
        messageLabel.setLayoutData(data);
        return labelComposite;
    }


    /**
     * Closes the dialog and disposes of any resources it was using,
     * such as colors and images.
     * @return true if dialog closed, false otherwise
     */
    public final boolean close() {
        boolean closed = super.close();
        if (closed) {
            //Must set all to null because if the dialog
            //is cancelled and brought up again without
            //being disposed, null values are expected.
            if (labelBackground != null) {
                labelBackground.dispose();
                labelBackground = null;
            }
            if (originalBackground != null) {
                originalBackground.dispose();
                originalBackground = null;
            }
            if (errorImage != null) {
                errorImage.dispose();
                errorImage = null;
            }
        }
        return closed;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 27-Nov-03	2024/3	pcameron	VBM:2003111704 Refactored ColorListSelectionDialog

 27-Nov-03	2024/1	pcameron	VBM:2003111704 Added ColorListSelectionDialog

 ===========================================================================
*/
