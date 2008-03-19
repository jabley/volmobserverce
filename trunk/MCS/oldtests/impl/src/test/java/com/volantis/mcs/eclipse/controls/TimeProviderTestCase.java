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

import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import junitx.util.PrivateAccessor;

/**
 * JUnit testcase class for TimeProvider
 */
public class TimeProviderTestCase extends TestCaseAbstract {

    /**
     * Test the method getValue
     */
    public void testGetValue() throws Exception {
        Display display = Display.getDefault();
        Shell shell = new Shell(display, SWT.SHELL_TRIM);
        TimeProvider provider = new TimeProvider(shell, SWT.NONE);

        Text hourField = (Text)
                PrivateAccessor.getField(provider, "hoursTextField");
        Text minField = (Text)
                PrivateAccessor.getField(provider, "minutesTextField");
        Text secField = (Text)
                PrivateAccessor.getField(provider, "secondsTextField");

        hourField.setText("10");
        minField.setText("15");
        secField.setText("59");
        assertEquals("Unexpected time value", "10:15:59", provider.getValue());

        hourField.setText("");
        minField.setText("");
        secField.setText("5");
        assertEquals("Unexpected time value", "5s", provider.getValue());

        hourField.setText("");
        minField.setText("2");
        secField.setText("");
        assertEquals("Unexpected time value", "2min", provider.getValue());

        hourField.setText("1");
        minField.setText("");
        secField.setText("");
        assertEquals("Unexpected time value", "1h", provider.getValue());

        hourField.setText("1");
        minField.setText("");
        secField.setText("1");
        assertEquals("Unexpected time value", "01:00:01", provider.getValue());

        hourField.setText("");
        minField.setText("");
        secField.setText(".1234");
        assertEquals("Unexpected time value", "0.1234s", provider.getValue());

        hourField.setText("2");
        minField.setText("");
        secField.setText(".1234");
        assertEquals("Unexpected time value", "02:00:00.1234", provider.getValue());

        hourField.setText("");
        minField.setText("2");
        secField.setText("5");
        assertEquals("Unexpected time value", "00:02:05", provider.getValue());

        hourField.setText("3");
        minField.setText("");
        secField.setText("5");
        assertEquals("Unexpected time value", "03:00:05", provider.getValue());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 17-Aug-04	5227/1	adrian	VBM:2004070809 Fixed bug whereby times in seconds were appended with an extra zero

 ===========================================================================
*/
