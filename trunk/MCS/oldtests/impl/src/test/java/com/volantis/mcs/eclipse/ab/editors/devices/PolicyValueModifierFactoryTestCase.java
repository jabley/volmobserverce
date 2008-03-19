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

import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.input.DefaultJDOMFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Test case for PolicyValueModifierFactory.
 */
public class PolicyValueModifierFactoryTestCase extends TestCaseAbstract {

    /**
     * A map of policy name to the PolicyValueModifier subclass that is used
     * to modify the values of the policy.
     */
    private static final Map policyNameToPVMClass = new HashMap(7);

    /**
     * Populate the map with test data.
     */
    static {
        // boolean type
        policyNameToPVMClass.put("beep", BooleanPolicyValueModifier.class);
        // int type
        policyNameToPVMClass.put("pixelsx", TextPolicyValueModifier.class);
        // ordered set type
        policyNameToPVMClass.put("ssversion", ListPolicyValueModifier.class);
        // range type
        policyNameToPVMClass.put("localsec", TextPolicyValueModifier.class);
        // selection type
        policyNameToPVMClass.put("java", ComboPolicyValueModifier.class);
        // text type
        policyNameToPVMClass.put("UAProf.MexeSpec",
                TextPolicyValueModifier.class);
        // unordered list
        policyNameToPVMClass.put("UAProf.MexeClassmarks",
                ListPolicyValueModifier.class);
    }

    /**
     * Tests the class of the factory-created PolicyValueModifier is correct
     * for the policy name.
     */
    public void testPolicyValueModifierFactory() throws Exception {

        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DefaultJDOMFactory(), false);

                PolicyValueModifierFactory pvmodFactory =
                        new PolicyValueModifierFactory(deviceRAM);
                Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);

                Iterator it = policyNameToPVMClass.keySet().iterator();

                // Iterate over the policy names, checking that the class of the
                // factory-created PolicyValueModifier is the expected class.
                while (it.hasNext()) {
                    String policyName = (String) it.next();
                    PolicyValueModifier pvMod =
                            pvmodFactory.createPolicyValueModifier(
                                    shell,
                                    SWT.NONE,
                                    policyName);

                    assertEquals(policyNameToPVMClass.get(policyName),
                            pvMod.getClass());
                }
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 17-Nov-04	6012/1	allan	VBM:2004051307 Remove standard elements in admin mode.

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 20-Apr-04	3909/2	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 19-Apr-04	3904/1	allan	VBM:2004020903 Support localized device policy categories

 08-Apr-04	3806/1	doug	VBM:2004040810 Paramaterized the DeviceRepositoryAccessorManager and the XMLDeviceRepositoryAccessor contstructors with a JDOMFactory

 23-Mar-04	3523/3	pcameron	VBM:2004030802 JDOM's getChildren() doesn't return null

 23-Mar-04	3523/1	pcameron	VBM:2004030802 Added PolicyValueModifierFactory

 ===========================================================================
*/
