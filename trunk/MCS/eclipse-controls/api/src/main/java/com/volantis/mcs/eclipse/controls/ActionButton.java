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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * A Button that is associated with an IAction such that it adopts the
 * text, toolip and enablement of the action.
 */
public class ActionButton extends Composite {

    /**
     * The underlying Button.
     */
    private Button button;

    /**
     * Construct a new ActionButton to use the specified Action.
     * @param parent The parent Composite.
     * @param style The SWT button style - supports the same styles as
     * org.eclipse.swt.Button.
     * @param action The IAction.
     */
    public ActionButton(Composite parent, int style, final IAction action) {
        super(parent, style);

        button = new Button(this, style);

        button.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                action.run();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });
        action.addPropertyChangeListener(new IPropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getProperty().equals(Action.ENABLED)) {
                    Boolean bool = (Boolean) event.getNewValue();
                    button.setEnabled(bool.booleanValue());
                }
            }
        });
        button.setText(action.getText());
        button.setToolTipText(action.getToolTipText());
        button.setEnabled(action.isEnabled());

        FillLayout layout = new FillLayout();
        setLayout(layout);

        initAccessible();
    }

    /**
     * Add a SelectionListener to the underlying button.
     * @param listener The SelectionListener.
     */
    public void addSelectionListener(SelectionListener listener) {
        button.addSelectionListener(listener);
    }

    /**
     * Remove a SelectionListener from the underlying button.
     * @param listener The SelectionListener.
     */
    public void removeSelectionListener(SelectionListener listener) {
        button.removeSelectionListener(listener);
    }

    /**
     * Get the underlying Button.
     * @return The underlying Button.
     */
    public Button getButton() {
        return button;
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        AccessibleControlListener acl =
                new SingleComponentACL(this, ACC.ROLE_PUSHBUTTON);
        getAccessible().addAccessibleControlListener(acl);

        StandardAccessibleListener al = new StandardAccessibleListener() {
            public void getName(AccessibleEvent ae) {
                ae.result = button.getText();
            }
        };
        al.setControl(this);
        getAccessible().addAccessibleListener(al);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	6121/2	adrianj	VBM:2004102602 Accessibility support for custom controls

 05-Feb-04	2853/1	byron	VBM:2004012303 Chart Editor should not allow user to add more than 1 asset

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 ===========================================================================
*/
