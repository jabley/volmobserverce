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

import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Test for FormSection.
 */
public class FormSectionTest extends ControlsTestAbstract {

    /**
     * Construct a new FormSectionTest.
     * @param title The shell title.
     */
    public FormSectionTest(String title) {
        super(title);
    }

    // javadoc inherited
    public void createControl() {
        Composite parent = new Composite(getShell(), SWT.NONE);
        GridLayout layout = new GridLayout();
        parent.setLayout(layout);

        FormSection formSection = new FormSection(parent, SWT.NONE) {
        };

        final Section section =
                SectionFactory.createSection(formSection, "The Title",
                        "A message that should wrap if the size of the " +
                "display is not wide enough.");
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        String message;
        message = "\nShould display a frame with a the text \"The Title\"" +
                " in bold.\n\nDirectly beneath the title there should be a" +
                " rule that stretches the width of the frame and resizes " +
                " horizontally with the frame.\n\nThe background of the frame" +
                " should be white, the text black and the rule #98AACB.\n\n" +
                "Finally there should be a message string beneath the rule.";
        return message;
    }


    /**
     * The main method must be implemented by the test class writer.
     * @param args Unused.
     */
    public static void main(String[] args) {
        new FormSectionTest("FormSection Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 04-Dec-03	2102/1	allan	VBM:2003112101 Create the AlertsActionsSection.

 03-Dec-03	2093/1	allan	VBM:2003110402 Created FormSection.

 ===========================================================================
*/
