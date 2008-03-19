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
package com.volantis.mcs.eclipse.ab.core;

import org.eclipse.swt.events.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.eclipse.common.ProjectProvider;
import com.volantis.mcs.eclipse.ab.core.TextDefinition;

/**
 * Simple test that allow the TextDefinition control to be displayed and manipulated.
 */
public class TextDefinitionTest extends ControlsTestAbstract {

    private TextDefinition ts3;

    // javadoc inherited
    public static void main(String[] args) {

        new TextDefinitionTest("Text Definition").display();
    }

     /**
     * Construct a TextDefinitionTest
     * @param title the title of the test
     */
    public TextDefinitionTest(String title) {
        super(title);
    }

    /**
     * Creates three instances of TextDefinition. And tests the default instanciation and
     * the effects of calling setText with an empty String and a String representing a
     * component path.
     */
    public void createControl() {
        TextDefinition ts = new TextDefinition(getShell(), SWT.DEFAULT, (ProjectProvider) null);
        TextDefinition ts2 = new TextDefinition(getShell(), SWT.DEFAULT,(ProjectProvider)null);
        /*TextDefinition*/ ts3 = new TextDefinition(getShell(), SWT.DEFAULT,(ProjectProvider)null);
        ts2.setText(new String());
        ts3.setText(new String("{Text/component}"));

        Button b = new Button(getShell(),SWT.DEFAULT);
        b.addSelectionListener(new SelectionAdapter() {
            boolean literal = true;
            public void widgetSelected(SelectionEvent e){
                ts3.setText((literal)?new String():new String("{Text/component}"));
                literal = !literal;
            }
        });

    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "You should see three TextDefinition components. Each consists of an ImageDropDown,\n"
                +"a text field, and a browse button. The first instance shows the default behavior \n"
                +"when the component is constructed (should default to Literal with browse button disabled).\n"
                +"The second shows the effect of calling setText with an empty String. This should show the\n"
                +"same results as the first instance. The third instance shows the effects of calling setText with\n"
                +"a Text component style path. This instance should show the textComponent icon in the dropdown and\n"
                +"have the browse button enabled. A button to the right of the TextDefinitions calls setText() on the 3rd TextDefinition to\n"
                +"toggle it between Text_Component and Literal";
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Aug-04	5264/3	allan	VBM:2004081008 Remove invalid plugin dependencies

 22-Apr-04	3964/7	matthew	VBM:2004032601 setBrowseButtonEnabled() changed to updateBrowseButtonEnabled(). Comment corrected.

 22-Apr-04	3964/5	matthew	VBM:2004032601 reworked code to set the browseButton enabled state. When setItem is called on an ImageDropDown no SelectionEvent results.

 22-Apr-04	3964/3	matthew	VBM:2004032601 removed unused and missleading jdoc and inline comments. Added DEFAULT_SELECTION.

 21-Apr-04	3964/1	matthew	VBM:2004032601 2004032601 problem setText corrected. Minor refactoring to remove secondary storage of state and to move initialisation of browseButton enabled state to a more appropriate location

 ===========================================================================
*/
