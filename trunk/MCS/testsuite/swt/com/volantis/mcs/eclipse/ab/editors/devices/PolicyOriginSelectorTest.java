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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.ab.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.xml.jaxp.JAXPTransformerMetaFactory;

import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jdom.input.DefaultJDOMFactory;

/**
 * A test for the PolicyOriginSelector control.
 */
public class PolicyOriginSelectorTest extends ControlsTestAbstract {

    /**
     * The PolicyOriginSelector under test.
     */
    private PolicyOriginSelector pvos;

    /**
     * The details for the PolicyOriginSelector.
     */
    private PolicyOriginSelectorDetails details;

    /**
     * A Combo widget with device names.
     */
    private Combo deviceCombo;

    /**
     * A Combo widget with short policy names.
     */
    private Combo policyCombo;

    /**
     * Creates a PolicyOriginSelector test.
     * @param title the title of the test
     */
    public PolicyOriginSelectorTest(String title) {
        super(title);
    }

    /**
     * Creates two Combo controls and the PolicyOriginSelector.
     */
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = 10;
        container.setLayout(gridLayout);

        createDeviceCombo(container);
        createPolicyList(container);
        createPolicyOriginSelector(container);

        // Sets the details for the PolicyOriginSelector
        details = new PolicyOriginSelectorDetails(
                deviceCombo.getText(),
                policyCombo.getText());

        pvos.setDetails(details);
    }

    /**
     * Creates a Combo widget with device names.
     * @param parent the parent Composite.
     */
    private void createDeviceCombo(Composite parent) {
        Label deviceLabel = new Label(parent, SWT.NONE);
        deviceLabel.setText("Choose device:");
        final String[] deviceNames = new String[]{
            "Master", "Mobile", "PC", "Voice", "Clamshell", "Handset",
            "Pixo-Handset", "SMS-Handset", "WAP-Handset", "Sony-WAP",
            "Sony-CMD-Z5-WML", "PC", "PC-UNIX", "PC-Win32", "PC-Unix-Mozilla",
            "PC-UNIX-Nautilus", "PC-UNIX-Konqueror", "PC-UNIX-Konqueror-2",
            "PC-UNIX-Konqueror-3", "PC-Win32-IE", "PC-Win32-Mozilla",
            "Web-Box", "Microsoft-WebTV", "TV"
        };
        deviceCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < deviceNames.length; i++) {
            deviceCombo.add(deviceNames[i]);
        }
        deviceCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String deviceName = deviceCombo.getText().trim();
                String policyName = policyCombo.getText().trim();
                details = new PolicyOriginSelectorDetails(deviceName,
                        policyName);
                pvos.setDetails(details);
            }
        });
        deviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates a Combo widget with short policy names.
     * @param parent the parent Composite.
     */
    private void createPolicyList(Composite parent) {
        Label policyLabel = new Label(parent, SWT.NONE);
        policyLabel.setText("Choose policy:");
        final String[] policyNames = new String[]{
            "beep", "realaudioinpage", "baudioinpage", "bookmarks",
            "activesky", "aggregation", "brwsrname", "entrytype",
            "download", "firmversion", "disptech", "gifinpage",
            "brwsrvers", "pixeldepth", "pagedown", "pageup",
            "pixelsx", "pixelsy", "fallback"
        };
        policyCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < policyNames.length; i++) {
            policyCombo.add(policyNames[i]);
        }
        policyCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                String deviceName = deviceCombo.getText().trim();
                String policyName = policyCombo.getText().trim();
                details = new PolicyOriginSelectorDetails(deviceName,
                        policyName);
                pvos.setDetails(details);
            }
        });
        policyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates the PolicyOriginSelector control.
     * @param parent the parent Composite.
     */
    private void createPolicyOriginSelector(Composite parent) {
        String filename = "/home/pcameron/repository1902.mdpr";
        DeviceRepositoryAccessorManager dram = null;
        try {
            dram = new DeviceRepositoryAccessorManager(
                    filename, new JAXPTransformerMetaFactory(),
                    new DefaultJDOMFactory(), false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        Label policyOriginLabel = new Label(parent, SWT.NONE);
        policyOriginLabel.setText("PolcyOriginSelector:");
        pvos = new PolicyOriginSelector(parent, SWT.NONE, dram, false);
        String deviceName = deviceCombo.getText().trim();
        String policyName = policyCombo.getText().trim();
        details = new PolicyOriginSelectorDetails(deviceName,
                policyName);
        pvos.setDetails(details);
    }


    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    // javadoc inherited
    public static void main(String[] args) {
        new PolicyOriginSelectorTest("PolicyOriginSelectorTest").
                display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-04	6012/3	allan	VBM:2004051307 Remove standard elements in admin mode.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 11-Mar-04	3398/2	pcameron	VBM:2004030906 Renamed PolicyValueOriginSelector and associated classes and added method to PolicyValueModifier interface

 04-Mar-04	3284/5	pcameron	VBM:2004022007 Rework issues with TextPolicyValueModifier

 01-Mar-04	3197/12	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 01-Mar-04	3197/10	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 ===========================================================================
*/
