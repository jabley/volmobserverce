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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * ControlsTestAbstract is extended by clients to allow them to
 * display widgets and successful test criteria for manual
 * testing.
 */
public abstract class ControlsTestAbstract {

    /**
     * The display to use for the widget.
     */
    private static Display display;

    /**
     * The shell of the display to which the widget
     * for testing is added by the client.
     */
    private static Shell shell;

    /**
     * The title for the shell window.
     */
    private static String title;

    /**
     * A description of the widget and how to test it. For example,
     * it should describe what the widget looks like and how it should
     * behave. In particular, the criteria for successfully passing the
     * test MUST be described so the manual tester knows what to do.
     * It may also refer to an external resource, such as a requirements
     * document which contains information pertinent to the correct
     * operation of the widget.
     */
    private static String successCriteria;

    /**
     * Constructor for a ControlsTestAbstract that provides a
     * framework for clients to display widgets.
     * @param title the title for the shell window.
     */
    public ControlsTestAbstract(String title) {
        this.title = title;
        init();
        createControl();
    }

    /**
     * Clients must implement this method to create and add a
     * widget to the shell. Clients must use the shell as a
     * parent of their widget, otherwise it will not be displayed.
     * For example:
     * <code>
     * public void addControl() {
     *     new ColorSelector(getShell(), SWT.BORDER, NamedColor.getAllColors());
     * }
     * </code>
     *
     * Clients must also add a main method to their subclass which creates the
     * subclass object and then displays it with its inherited display method.
     * For example:
     *
     * <code>
     * public static void main(String[] args) {
     *     new ColorSelectorTest("ColorSelector Test").display();
     * }
     * </code>
     */
    public abstract void createControl();

    /**
     * Gets the success criteria for a successful manual test.
     * @return the success criteria
     */
    public abstract String getSuccessCriteria();

    /**
     * Get the shell which clients add their controls to.
     * @return the shell
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * Initialises the class.
     */
    private void init() {
        display = Display.getDefault();
        shell = new Shell(display, SWT.SHELL_TRIM);
        successCriteria = getSuccessCriteria();
    }

    /**
     * Prints out the success criteria for the test and displays
     * a client widget. This is used by the client in a main method.
     * See #addControl.
     */
    public static void display() {
        System.out.println(successCriteria);
        shell.setLayout(new FillLayout());
        shell.setText(title);
        shell.setVisible(true);
        shell.pack();
        shell.layout();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
        display.dispose();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 21-Nov-03	1634/1	pcameron	VBM:2003102205 Added ColorSelector, NamedColor and supporting classes

 ===========================================================================
*/
