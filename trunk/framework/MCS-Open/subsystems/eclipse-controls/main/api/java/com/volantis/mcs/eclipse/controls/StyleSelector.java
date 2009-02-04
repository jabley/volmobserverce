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
package com.volantis.mcs.eclipse.controls;

import com.volantis.mcs.eclipse.validation.ValidationStatus;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;

import java.util.StringTokenizer;

/**
 * A TextButton in which the user can enter a space(s)-separated list of style
 * classes, and whose selection brings up a StyleSelectionDialog whose
 * retrieved value is then used to reset the text of the TextButton.
 */
public class StyleSelector extends TextButton {
    /**
     * Constructor.
     *
     * @param parent The parent Composite.
     * @param style The required style.
     */
    public StyleSelector(Composite parent, int style) {
        super(parent, style);

        getButton().setText(ControlsMessages.getString(
                "StyleSelector.button.label"));
        // Add a listener to the button such that when the button is
        // pressed the appropriate dialog is displayed
        getButton().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                doSelectionDialog();
            }
        });
    }

    /**
     * Performs all processing associated with populating, constructing,
     * displaying and interrogating the dialog
     */
    private void doSelectionDialog() {
        // Tokenize the text from the TextButton
        StringTokenizer tokenizer = new StringTokenizer(getValue());
        String[] tokens = new String[tokenizer.countTokens()];
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokenizer.nextToken();
        }

        // Construct a dialog based on the above tokens
        StyleSelectionDialog dialog =
                new StyleSelectionDialog(getButton().getShell(), tokens);

        // Display the dialog, and only proceed if user okayed it
        if (dialog.open() == StyleSelectionDialog.OK) {

            // Put the concatenated value into the TextButton's text
            setValue(dialog.getStyles());
        }
    }

    // javadoc inherited.
    public ValidationStatus validate() {
        throw new UnsupportedOperationException("StyleSelector.validate()");
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

 ===========================================================================
*/
