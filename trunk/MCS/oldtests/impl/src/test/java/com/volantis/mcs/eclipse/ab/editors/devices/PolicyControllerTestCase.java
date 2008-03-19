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
package com.volantis.mcs.eclipse.ab.editors.devices;

import java.io.File;

import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.core.ResolvedDevicePolicy;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.testtools.mocks.MockFile;
import junitx.util.PrivateAccessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * A test case for the PolicyController.
 */
public class PolicyControllerTestCase extends TestCaseAbstract {

    /**
     * The PolicyController under test.
     */
    private PolicyController controller;

    /**
     * Tests the private PolicyController#handleOverrideSelection method.
     * @throws Throwable
     */
    public void testHandleOverrideSelection() throws Throwable {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DeviceODOMElementFactory(), false);
                Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
                PolicyOriginSelector selector = new PolicyOriginSelector(
                        shell, SWT.NONE, deviceRAM, false);
                PolicyValueModifierFactory factory =
                        new PolicyValueModifierFactory(deviceRAM);

                final String policyName = "pixelsx";
                final String deviceName = "Web-Box";

                UndoRedoMementoOriginator orig =
                        new UndoRedoMementoOriginator() {
                    public UndoRedoMemento takeSnapshot() {
                        return null;
                    }

                    public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
                    }
                };

                DeviceEditorContext context =
                        DeviceEditorContext.createDeviceEditorContext(
                                new MockFile("name"),
                                orig, deviceRAM);
                final PolicyValueModifier modifier =
                        factory.createPolicyValueModifier(shell, SWT.NONE,
                                policyName);
                controller = new PolicyController(policyName, selector,
                        modifier, null, context);

                final ResolvedDevicePolicy rdpOriginal =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // WebBox falls back to pixelsx from TV.
                assertEquals("WebBox does not inherit pixelsx from TV",
                        "TV", rdpOriginal.deviceName);

                controller.setDeviceName(deviceName);

                // Simulate changing from fallback to override by invoking the private
                // PolicyController#handleOverrideSelection method.
                try {
                    PrivateAccessor.invoke(controller,
                            "handleOverrideSelection", null, null);
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                // Retrieve the updated policy.
                final ResolvedDevicePolicy rdpOverride =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // pixelsx is now overridden by Web-Box.
                assertEquals("WebBox does not override pixelsx from TV",
                        deviceName, rdpOverride.deviceName);
            }
        });
    }

    /**
     * Tests the private PolicyController#handleFallbackSelection method.
     * @throws Throwable
     */
    public void testHandleFallbackSelection() throws Throwable {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DeviceODOMElementFactory(), false);
                Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
                PolicyOriginSelector selector = new PolicyOriginSelector(
                        shell, SWT.NONE, deviceRAM, false);
                PolicyValueModifierFactory factory =
                        new PolicyValueModifierFactory(deviceRAM);

                final String policyName = "pixelsx";
                final String deviceName = "Microsoft-WebTV";

                UndoRedoMementoOriginator orig =
                        new UndoRedoMementoOriginator() {
                    public UndoRedoMemento takeSnapshot() {
                        return null;
                    }

                    public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
                    }
                };

                DeviceEditorContext context =
                        DeviceEditorContext.createDeviceEditorContext(
                                new MockFile("name"),
                                orig, deviceRAM);

                final PolicyValueModifier modifier =
                        factory.createPolicyValueModifier(shell, SWT.NONE, policyName);
                controller = new PolicyController(policyName, selector,
                        modifier, null, context);

                final ResolvedDevicePolicy rdpOriginal =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // pixelsx is overridden by Microsoft-WebTV.
                assertEquals("Microsoft-WebTV does not override pixelsx",
                        deviceName, rdpOriginal.deviceName);

                controller.setDeviceName(deviceName);

                // Simulate changing from override to fallback by invoking the private
                // PolicyController#handleFallbackSelection method.
                try {
                    PrivateAccessor.invoke(controller,
                            "handleFallbackSelection", null, null);
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                // Retrieve the updated policy.
                final ResolvedDevicePolicy rdpFallback =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // pixelsx was overridden by Microsoft-WebTV but now falls back to TV.
                assertEquals("Microsoft-WebTV does not inherit pixelsx from TV",
                        "TV", rdpFallback.deviceName);
            }
        });
    }

    /**
     * Tests the private PolicyController#handleRestoreSelection method.
     */
    public void testHandleRestoreSelection() throws Throwable {
        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DeviceODOMElementFactory(), false);
                Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
                PolicyOriginSelector selector = new PolicyOriginSelector(
                        shell, SWT.NONE, deviceRAM, false);
                PolicyValueModifierFactory factory =
                        new PolicyValueModifierFactory(deviceRAM);

                final String policyName = "pixelsx";
                final String deviceName = "Microsoft-WebTV";

                UndoRedoMementoOriginator orig =
                        new UndoRedoMementoOriginator() {
                    public UndoRedoMemento takeSnapshot() {
                        return null;
                    }

                    public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
                    }
                };

                DeviceEditorContext context =
                        DeviceEditorContext.createDeviceEditorContext(
                                new MockFile("name"),
                                orig, deviceRAM);

                final PolicyValueModifier modifier =
                        factory.createPolicyValueModifier(shell, SWT.NONE, policyName);
                controller = new PolicyController(policyName, selector,
                        modifier, null, context);

                final ResolvedDevicePolicy rdpOriginal =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // pixelsx is overridden by Microsoft-WebTV.
                assertEquals("Microsoft-WebTV does not override pixelsx",
                        deviceName, rdpOriginal.deviceName);

                controller.setDeviceName(deviceName);

                // Simulate changing from override to fallback by invoking the private
                // PolicyController#handleFallbackSelection method.
                try {
                    PrivateAccessor.invoke(controller,
                            "handleFallbackSelection", null, null);
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                // Retrieve the updated policy.
                final ResolvedDevicePolicy rdpFallback =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // pixelsx was overridden by Microsoft-WebTV but now falls back to TV.
                assertEquals("Microsoft-WebTV does not inherit pixelsx from TV",
                        "TV", rdpFallback.deviceName);

                // Simulate a Restore by invoking the private
                // PolicyController#handleRestoreSelection method.
                try {
                    PrivateAccessor.invoke(controller,
                            "handleRestoreSelection", null, null);
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                // Retrieve the updated policy.
                final ResolvedDevicePolicy rdpRestore =
                        deviceRAM.resolvePolicy(deviceName, policyName);

                // pixelsx has now been restored and should be overridden by
                // Microsoft-WebTV.
                assertEquals("Microsoft-WebTV does not override pixelsx",
                        deviceName, rdpRestore.deviceName);
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

 17-Nov-04	6012/3	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/12	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/10	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/6	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 30-Apr-04	4081/1	pcameron	VBM:2004031007 Added PoliciesSection

 14-Apr-04	3683/3	pcameron	VBM:2004030401 Some further tweaks

 14-Apr-04	3683/1	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 ===========================================================================
*/
