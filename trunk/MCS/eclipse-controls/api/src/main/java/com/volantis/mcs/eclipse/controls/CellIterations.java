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
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;


/**
 * The CellIterations widget.
 * <p>This control consists of a read-only dropdown-list style combo and a
 * single row text box. The combo holds two items denoting whether the count is
 * variable or fixed. The text control allows entry of the number of iterations
 * required.</p>
 * <p>The control is <b>NOT</b> validated, so the user is free to enter numbers
 * or text into the text control. Whatever is entered is returned by the
 * getIterations() method.</p>
 * <p>The getIterationsQualifier() method will return a String representation
 * of the item selected in the combo which is either "fixed" or "variable".
 * The actual text displayed in the combo for these items is read from
 * ControlsMessages.properties</p>
 */
public class CellIterations extends Composite {
    /**
     * The qualifier returned when 'Up to' is selected in the control
     */
    public static String QUALIFIER_VARIABLE = "variable";

    /**
     * The qualifier returned when 'Exactly' is selected in the control
     */
    public static String QUALIFIER_FIXED = "fixed";

    /**
     * Name text for the iterations entry control.
     */
    private static final String TEXT_ITERATIONS =
            ControlsMessages.getString("CellIterations.iterations");

    /**
     * Name text for the iteration type entry control.
     */
    private static final String TEXT_ITERATION_TYPE =
            ControlsMessages.getString("CellIterations.iterationType");

    /**
     * The wrapped Combo control
     */
    private Combo comboControl;

    /**
     * The wrapped text control
     */
    private Text textControl;

    /**
     * An array of text representations of each qualifier. There will be
     * one qualifier mapping to the index of each entry in the combo.
     */
    private String[] qualifiers;

    /**
     * An event object to use when the control is modified. The contents
     * of the event are set up by notifyListeners().
     */
    private Event event = new Event();

    /**
     * Creates a new CellIterations widget
     *
     * @param parent a composite widget to add this widget to.
     */
    public CellIterations(Composite parent) {
        super(parent, SWT.NONE);

        comboControl = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        textControl = new Text(this, SWT.BORDER | SWT.SINGLE);

        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        // Assume the same text width as defined for the text button control
        GridData textGridData = new GridData(GridData.FILL_HORIZONTAL);
        textGridData.widthHint =
            ControlsMessages.getInteger("Text.textWidth").intValue();
        textControl.setLayoutData(textGridData);

        setLayout(layout);

        // Set up the combo and qualifiers
        qualifiers = new String[2];
        qualifiers[0] = QUALIFIER_VARIABLE;
        qualifiers[1] = QUALIFIER_FIXED;

        comboControl.add(ControlsMessages.getString("CellIterations.variable"));
        comboControl.add(ControlsMessages.getString("CellIterations.fixed"));
        comboControl.select(0);

        // Convert combo selections to modifications
        comboControl.addSelectionListener(new SelectionListener() {
                public void widgetSelected(SelectionEvent evt) {
                    modified();
                }

                public void widgetDefaultSelected(SelectionEvent evt) {
                    modified();
                }
            });

        // Catch and convert text modifications
        textControl.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent evt) {
                    modified();
                }
            });

        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        initAccessible();
    }

    /**
     * Get the string representation of the qualifier currently selected
     * in the combo. This will be either "variable" or "fixed" depending on
     * which of the two qualifiers is selected.
     * @return the string representation of the selected qualifier
     */
    public String getIterationsQualifier() {
        String result = null;
        int sel = comboControl.getSelectionIndex();
        if (sel != -1) {
            result = qualifiers[sel];
        }
        return result;
    }

    /**
     * Set the iteration qualifier selection for this control.
     *
     * @param qualifier the qualifier (either 'fixed' or 'variables').
     */
    public void setIterationQualifier(String qualifier) {
    	if (qualifier.equals("")){
    		comboControl.setText("");
    	} else {
	        boolean found = false;
	        for (int i = 0; !found && i < qualifiers.length; i++) {
	            if (qualifiers[i].equals(qualifier)) {
	                comboControl.select(i);
	                found = true;
	            }
	        }
	        if (!found) {
	            throw new IllegalArgumentException ("The value supplied (" +
	                    qualifier + ") is not valid for this iteration qualifier.");
	        }
    	}
    }

    /**
     * Return the number of iterations entered into the text box. This
     * simply returns the contents of the text box with no validation. The
     * contents can therefore be anything.
     * @return the contents of the text control.
     */
    public String getIterations() {
        return textControl.getText();
    }

    /**
     * Return the child Combo control.
     * @return the <code>Combo</code> control.
     */
    public Combo getCombo() {
        return comboControl;
    }

    /**
     * Return the child text control.
     * @return the <code>Text</code> control.
     */
    public Text getText() {
        return textControl;
    }

    /**
     * The combo box has been selected or the text box has been modified
     * so report that our control has been modified. <code>notifyListeners</code>
     * populates the event for you using 'this' as the event source. 
     * This as it happens, is exactly what we want.
     * @see org.eclipse.swt.widgets.Widget#isListening
     * @see org.eclipse.swt.widgets.Widget#notifyListeners
     */
    private void modified() {
        if (isListening(SWT.Modify)) {
            notifyListeners(SWT.Modify, event);
        }
    }

    /**
     * Adds a listener that is called when the contents of the text control
     * is modified or a selection occurs in the combo.
     * @param listener for modifications
     */
    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            addListener(SWT.Modify, new TypedListener(listener));
        }
    }
    
    /**
     * Adds a selection listener to the comboControl
     * @param listener for modifications
     */
    public void addSelectionListener(SelectionListener listener) {
        if (listener != null) {
            comboControl.addSelectionListener(listener);
        }
    }
    
    /**
     * Removes a control modification listener
     * @param listener for modifications
     */
    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            removeListener(SWT.Modify, new TypedListener(listener));
        }
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        StandardAccessibleListener comboListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = TEXT_ITERATION_TYPE;
                    }
                };
        comboListener.setControl(comboControl);
        comboControl.getAccessible().addAccessibleListener(comboListener);

        StandardAccessibleListener textListener =
                new StandardAccessibleListener() {
                    public void getName(AccessibleEvent ae) {
                        ae.result = TEXT_ITERATIONS;
                    }
                };
        textListener.setControl(textControl);
        textControl.getAccessible().addAccessibleListener(textListener);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/1	adrianj	VBM:2004102602 Accessibility support for custom controls

 22-Mar-04	3502/1	byron	VBM:2004022610 Defect :Changing Spatial BGColor also changes Pane BGColor

 06-Feb-04	2870/2	pwells	VBM:2004020216 layout: Error when adding a pane to a spatial iterator

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 23-Jan-04	2728/1	byron	VBM:2004012301 Miscellaneous bug fixes cell iterations/listeners/enablement/etc

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 18-Nov-03	1921/1	steve	VBM:2003110902 CellIterations control

 ===========================================================================
*/
