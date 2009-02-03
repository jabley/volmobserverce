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

import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;

public class TimeSelectorTest extends ControlsTestAbstract {

    public static void main(String[] args) {
        new TimeSelectorTest("TimeSelector").display();
    }

    public TimeSelectorTest(String title) {
        super(title);
    }

    public void createControl() {
        TimeSelector ts = new TimeSelector(getShell(), SWT.DEFAULT);
    }

    public String getSuccessCriteria() {
        return "On hitting the button, a TimeSelectionDialog should appear\n"
            + "and its members should be the space-separated \"words\" in\n"
            + "the text associated with the button. If OK is selected, the\n"
            + "text part of the button should be the concatenation of the\n"
            + "times in the dialog, separated by spaces";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jan-04	2604/3	richardc	VBM:2003112406 Added TimeSelector

 15-Jan-04	2604/1	richardc	VBM:2003112406 Added TimeSelector

 ===========================================================================
*/
