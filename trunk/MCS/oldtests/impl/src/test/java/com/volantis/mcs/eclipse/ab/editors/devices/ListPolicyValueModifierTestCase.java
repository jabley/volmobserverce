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
import java.util.List;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
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
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

/**
 * A test case for the ListPolicyValueModifier.
 */
public class ListPolicyValueModifierTestCase extends TestCaseAbstract {

    /**
     * Tests the private ListPolicyValueModifier#updatePolicyElement method.
     * @throws Throwable
     */
    public void testUpdatePolicyElement() throws Throwable {

        TemporaryFileManager tempFileMgr = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        tempFileMgr.executeWith(new TemporaryFileExecutor() {
            public void execute(File repository) throws Exception {

                // Create the manager from the repository file, giving it an
                // ODOMFactory,
                DeviceRepositoryAccessorManager deviceRAM =
                        new DeviceRepositoryAccessorManager(
                                repository.getPath(),
                                new TestTransformerMetaFactory(),
                                new DeviceODOMElementFactory(), false);

                final Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);

                final PolicyOriginSelector selector =
                        new PolicyOriginSelector(shell, SWT.NONE, deviceRAM,
                                false);
                final PolicyValueModifierFactory factory =
                        new PolicyValueModifierFactory(deviceRAM);

                final String policyName = "UAProf.CcppAccept";
                final String deviceName = "Master";

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

                // Create the PolicyValueModifier and controller for
                // UAProf.CcppAccept.
                ListPolicyValueModifier listModifier = (ListPolicyValueModifier)
                        factory.createPolicyValueModifier(shell, SWT.NONE,
                                policyName);
                final PolicyController controller =
                        new PolicyController(policyName, selector,
                                listModifier, null, context);

                controller.setDeviceName(deviceName);

                Element originalPolicy = listModifier.getPolicy();
                final Namespace ns = originalPolicy.getNamespace();
                final ElementFilter elementFilter = new ElementFilter(ns);

                Element standardElement =
                        originalPolicy.getChild(DeviceRepositorySchemaConstants.
                        STANDARD_ELEMENT_NAME, ns);

                // There should be no pre-existing standard element.
                assertNull(standardElement);

                // These are the first set of update values. These should appear in the
                // given order in the updated element, and there should be a standard
                // element appearing after these values.
                String[] firstUpdates =
                        new String[]{"up1", "up2", "up3", "up4", "up5"};
                final Class[] types = {firstUpdates.getClass()};

                // Perform the first update by calling the PolicyValueModifier's
                // private updatePolicyElement method.
                try {
                    PrivateAccessor.invoke(listModifier,
                            "updatePolicyElement", types,
                            new Object[]{firstUpdates});
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                // Retrieve the updated policy.
                Element firstUpdatedPolicy = listModifier.getPolicy();

                // Retrieve the newly-created standard element.
                Element standardElementFirstUpdate =
                        firstUpdatedPolicy.getChild(DeviceRepositorySchemaConstants.
                        STANDARD_ELEMENT_NAME, ns);

                // There should now be a standard element.
                assertNotNull(standardElementFirstUpdate);

                List firstUpdateContent = firstUpdatedPolicy.getContent(elementFilter);

                // The standard element should be the last child element after the
                // update.
                assertEquals(firstUpdateContent.indexOf(standardElementFirstUpdate),
                        firstUpdateContent.size() - 1);

                // There should now be 6 child elements: 5 value and 1 standard.
                assertEquals(firstUpdateContent.size(), 6);

                // Check that the updated values have been inserted in the given order.
                for (int i = 0; i < firstUpdates.length; i++) {
                    Element valueElement = (Element) firstUpdateContent.get(i);
                    String value = valueElement.getText();
                    assertEquals("Names should be inserted in order",
                            value, firstUpdates[i]);
                }

                String[] secondUpdates = new String[]{"new1", "new2", "new3"};

                try {
                    PrivateAccessor.invoke(listModifier,
                            "updatePolicyElement", types,
                            new Object[]{secondUpdates});
                } catch (Throwable throwable) {
                    throw new Exception(throwable);
                }

                Element secondUpdatedPolicy = listModifier.getPolicy();

                // Retrieve the newly-created standard element.
                Element standardElementSecondUpdate =
                        secondUpdatedPolicy.getChild(DeviceRepositorySchemaConstants.
                        STANDARD_ELEMENT_NAME, ns);

                // There should now be a standard element.
                assertNotNull(standardElementSecondUpdate);

                List secondUpdateContent =
                        secondUpdatedPolicy.getContent(elementFilter);

                // The standard element should be the last child element after the
                // update.
                assertEquals(secondUpdateContent.indexOf(standardElementSecondUpdate),
                        secondUpdateContent.size() - 1);

                // There should now be 4 child elements: 3 value and 1 standard.
                assertEquals(secondUpdateContent.size(), 4);

                // Check that the updated values have been inserted in the given order.
                for (int i = 0; i < secondUpdates.length; i++) {
                    Element valueElement = (Element) secondUpdateContent.get(i);
                    String value = valueElement.getText();
                    assertEquals("Names should be inserted in order",
                            value, secondUpdates[i]);
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

 17-Nov-04	6012/3	allan	VBM:2004051307 Remove standard elements in admin mode.

 16-Nov-04	4394/10	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/4	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4301/1	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 27-Aug-04	5315/2	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 07-May-04	4172/3	pcameron	VBM:2004032305 Added SecondaryPatternsSection and refactored ListValueBuilder

 30-Apr-04	4081/1	pcameron	VBM:2004031007 Added PoliciesSection

 22-Apr-04	3980/1	pcameron	VBM:2004040510 Added a unit test for the ListPolicyValueModifier

 ===========================================================================
*/
