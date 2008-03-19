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
package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.eclipse.ab.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.testtools.io.IOUtils;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.XMLOutputter;
import org.jdom.input.JDOMFactory;
import org.jdom.input.DefaultJDOMFactory;

import java.io.File;
import java.io.IOException;

/**
 * Tests the <code>StructurePolicyValueModifier</code> class
 */
public class StructurePolicyValueModifierTest extends ControlsTestAbstract {

    /**
     * JDOMFactory for creating JDOM related objects
     */
    private static JDOMFactory jdomFactory = new DefaultJDOMFactory();

    /**
     * device repository Namespace
     */
    private static Namespace ns = Namespace.getNamespace(
                "ns",
                "http://www.volantis.com/xmlns/device-repository/device");

    /**
     * An XMLOutputter instance
     */
    private static XMLOutputter xmlOutputter = new XMLOutputter("  ", true);

    /**
     * The device repository file, this will be cleaned up automagically.
     */
    private File deviceRepositoryFile;

    /**
     * Intializes a <code>StructurePolicyValueModifierTest</code> with the
     * given arguments
     * @param title the title of the test
     */
    public StructurePolicyValueModifierTest(String title,
            File deviceRepositoryFile) {

        super(title);
        this.deviceRepositoryFile = deviceRepositoryFile;
    }

    /**
     * The device repository access manager.
     */
    private DeviceRepositoryAccessorManager repositoryManager;

    // javadoc inherited
    public void createControl() {
        Composite container = new Composite(getShell(), SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 10;
        container.setLayout(gridLayout);

        try {
            // get the file for the repository that is in the same jar as
            // this class
            repositoryManager = new DeviceRepositoryAccessorManager(
                    deviceRepositoryFile.getPath(), new JAXPTransformerMetaFactory(),
                    new DefaultJDOMFactory(), false);

            // create the StructurePolicyValueModifier
            StructurePolicyValueModifier modifier =
                        new StructurePolicyValueModifier(
                                    container,
                                    SWT.NONE,
                                    "protocol.wml.emulate.bigTag",
                                    repositoryManager);
            final Element policyElement = createPolicyElement();
            modifier.addModifyListener(new ModifyListener() {
                // javadoc inherited
                public void modifyText(ModifyEvent event) {
                    try {
                        System.out.println("Policy Modified");
                        xmlOutputter.output(policyElement, System.out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            modifier.setPolicy(policyElement);
            modifier.getControl().setLayoutData(
                        new GridData(GridData.FILL_HORIZONTAL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Factory method for creating a "policy" element that specifies a
     * structure value
     * @return an Element
     */
    private Element createPolicyElement() {

        // create the policy element
        Element policy = jdomFactory.element("policy", ns);
        policy.setAttribute("name", "protocol.wml.emulate.bigTag");

        // create the enable field
        policy.addContent(createFieldElement("enable", "false"));
        policy.addContent(createFieldElement("prefix", "MyPrefix"));
        policy.addContent(createFieldElement("suffix", "MySuffix"));
        policy.addContent(createFieldElement("altTag", "MyAltTag"));
        return policy;
    }

    /**
     * Factory method for creating a "field" element with a specific name
     * and value
     * @param name the name of the field
     * @param value the value of the field
     * @return an element
     */
    private Element createFieldElement(String name, String value) {
        Element field = jdomFactory.element("field", ns);
        field.setAttribute(jdomFactory.attribute("name", name));
        field.setAttribute(jdomFactory.attribute("value", value));
        return field;
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "You should see a structure control with  4 fields\n" +
                    "Editing a field results in the policy element being updated\n" +
                    "Every the policy element is update its structure wil be " +
                    "printed out";
    }

    // javadoc inherited
    public static void main(String[] args) {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        try {
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repositoryFile) throws Exception {
                    new StructurePolicyValueModifierTest(
                            "StructurePolicyValueModifierTest test",
                            repositoryFile).display();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 13-Sep-04	5315/3	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 01-Apr-04	3602/1	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 ===========================================================================
*/
