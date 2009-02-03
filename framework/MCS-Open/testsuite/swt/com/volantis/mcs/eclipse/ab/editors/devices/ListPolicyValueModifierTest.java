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
import com.volantis.mcs.eclipse.controls.ListValueBuilder;
import com.volantis.mcs.eclipse.controls.TimeSelectionDialog;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.xml.jaxp.JAXPTransformerMetaFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.jdom.Element;
import org.jdom.input.DefaultJDOMFactory;
import org.jdom.input.JDOMFactory;

/**
 * A test for the ListPolicyValueModifier.
 */
public class ListPolicyValueModifierTest extends ControlsTestAbstract {

    /**
     * The ListPolicyValueModifier under test, with dialog and ordering.
     */
    private ListPolicyValueModifier lpvm;

    /**
     * A Combo widget with device names.
     */
    private Combo deviceCombo;

    /**
     * A Combo widget with short policy names.
     */
    private Combo policyCombo;

    /**
     * The current selected device.
     */
    private String selectedDevice;

    /**
     * The current selected policy.
     */
    private String selectedPolicy;

    /**
     * The device repository access manager.
     */
    private DeviceRepositoryAccessorManager deviceRAM;

    /**
     * Factory for creating JDOM objects. Note that devices do not use
     * the LPDM namespace.
     */
    private static final JDOMFactory factory = new DefaultJDOMFactory();

    /**
     * Creates a ListPolicyValueModifier test.
     * @param title the title of the test
     */
    public ListPolicyValueModifierTest(String title) {
        super(title);
    }

    /**
     * Creates the controls for this test.
     */
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 10;
        container.setLayout(gridLayout);

        String filename = "/home/pcameron/repository1902.mdpr";
        try {
            deviceRAM = new DeviceRepositoryAccessorManager(
                    filename, new JAXPTransformerMetaFactory(),
                    new DefaultJDOMFactory(), false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        createDeviceCombo(container);
        createPolicyValueCombo(container);

        TimeSelectionDialog selectionDialog =
                new TimeSelectionDialog(getShell(), null);
        ListValueBuilder listBuilder1 = createListBuilder(true, selectionDialog);
        lpvm = createLPVM(listBuilder1);

        selectedDevice = deviceCombo.getText();
        selectedPolicy = policyCombo.getText();

        setPolicyElementOnModifier(lpvm);
    }

    /**
     * Creates the ListValueBuilder used by the ListPolicyValueModifier.
     * @param ordered whether or not the ListValueBuilder is ordered
     * @param dialog the SelectionDialog to use, if any. Can be null.
     * @return the ListValueBuilder
     */
    private ListValueBuilder createListBuilder(boolean ordered,
                                          SelectionDialog dialog) {
        ListValueBuilder listBuilder = new ListValueBuilder(getShell(),
                ordered, dialog);
        return listBuilder;
    }

    /**
     * Creates a ListPolicyValueModifier using the specified ListValueBuilder.
     * @param listBuilder the ListValueBuilder to use
     */
    private ListPolicyValueModifier createLPVM(ListValueBuilder listBuilder) {
        final ListPolicyValueModifier lpvm =
                new ListPolicyValueModifier(listBuilder, "name", null);

        lpvm.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                Element policyElement = lpvm.getPolicy();
                List children = policyElement.getChildren();
                if (children != null && children.size() > 0) {
                    Iterator it = children.iterator();
                    System.out.println("You chose the following values:");
                    while (it.hasNext()) {
                        Element child = (Element) it.next();
                        System.out.println("Value is " + child.getText());
                    }
                } else {
                    System.out.println("There are no values.");
                }
            }
        });
        return lpvm;
    }

    /**
     * Creates a Combo widget with device names.
     * @param parent the parent Composite.
     */
    private void createDeviceCombo(Composite parent) {
        Label deviceLabel = new Label(parent, SWT.NONE);
        deviceLabel.setText("Choose device:");
        final String[] deviceNames = new String[]{
            "WAP-Handset",
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

        // Adds a SelectionListener which sets the policy element on the
        // controls.
        deviceCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (!deviceCombo.getText().equals(selectedDevice)) {
                    selectedDevice = deviceCombo.getText();
                    setPolicyElementOnModifier(lpvm);
                }
            }
        });
        deviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates a Combo widget with short policy names.
     * @param parent the parent Composite.
     */
    private void createPolicyValueCombo(Composite parent) {
        Label policyLabel = new Label(parent, SWT.NONE);
        policyLabel.setText("Choose policy value name:");
        final String[] policyNames = new String[]{
            "UAProf.CcppAccept",
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

        // Adds a SelectionListener which sets the policy element on the
        // controls.
        policyCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                if (!policyCombo.getText().equals(selectedPolicy)) {
                    selectedPolicy = policyCombo.getText();
                    setPolicyElementOnModifier(lpvm);
                }
            }
        });
        policyCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Sets the policy element for the current selections on the specified
     * PolicyValueModifier. The policy element is for the originating device of
     * the selected device and selected policy. This ensures that a non-null
     * element is retrieved. This element is found each time the Combo
     * selections change.
     */
    private void setPolicyElementOnModifier(PolicyValueModifier policyModifier) {
        String originatingDevice =
                deviceRAM.getOriginatingDevice(selectedDevice, selectedPolicy);
        Element policyElement =
                deviceRAM.retrievePolicy(originatingDevice, selectedPolicy);
        policyModifier.setPolicy(policyElement);
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    // javadoc inherited
    public static void main(String[] args) {
        new ListPolicyValueModifierTest(
                "ListPolicyValueModifier test").
                display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 15-Nov-04	6199/1	adrianj	VBM:2004102512 Added accessibility support to PolicyValueModifiers

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 18-Mar-04	3416/7	pcameron	VBM:2004022309 Added ListBuilder and ListPolicyValueModifier with tests

 ===========================================================================
*/
