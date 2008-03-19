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

import com.volantis.mcs.eclipse.validation.ValidationStatus;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.SelectionDialog;

import java.util.StringTokenizer;

/**
 * A TextButton in which the user can enter a space(s)-separated list
 * of times, and whose selection brings up a TimeSelectionDialog whose
 * retrieved value is then used to reset the text of the TextButton.
 */
public class TimeSelector extends TextButton {

    /**
     * Constructor
     * @param parent The parent Composite
     * @param style The required style
     */
    public TimeSelector(Composite parent, int style) {
        super(parent, style);

        // Add a listener to the button such that when the button is
        // pressed the appropriate dialog is displayed
        getButton().addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                doSelectionDialog();
            }
        });
    }

    /**
     * Inherited validation method, which in this case is not supported.
     * @throws UnsupportedOperationException because this method is not supported.
     */
    public ValidationStatus validate() {
        throw new UnsupportedOperationException("No TimeSelector.validate()");
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
        CustomisableListSelectionDialog dialog = ListSelectionDialogFactory.
                createTimeListSelectionDialog(getButton().getShell(), tokens);

        // Display the dialog, and only proceed if user okayed it
        if (dialog.open() == SelectionDialog.OK) {

            // Concatenate the times together (NB they cannot be null as
            // the dialog's initial population was non-null)
            final String[] times = dialog.getSelection();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < times.length; i++) {
                buffer.append(times[i]);
                if (i < times.length - 1) {
                    buffer.append(' ');
                }
            }

            // Put the concatenated value into the TextButton's text
            setValue(buffer.toString());
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/2	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 09-Mar-05	7073/3	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 08-Mar-05	7073/1	matthew	VBM:2005022203 refactor ColorListSelectionDialog and TimeSelectionDialog into a single customisable class that is configured in the ListSelectionDialogFactory

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 13-Oct-04	5771/1	byron	VBM:2004100806 Support style classes on grids and spatial format iterators: GUI

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Jan-04	2604/1	richardc	VBM:2003112406 Added TimeSelector

 ===========================================================================
*/
