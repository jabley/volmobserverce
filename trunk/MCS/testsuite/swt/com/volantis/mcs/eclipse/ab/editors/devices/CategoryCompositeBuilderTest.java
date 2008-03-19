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
import java.io.IOException;

import com.volantis.mcs.eclipse.ab.core.DeviceRepositoryAccessorManager;
import com.volantis.mcs.eclipse.ab.editors.devices.odom.DeviceODOMElementFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoInfo;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMemento;
import com.volantis.mcs.eclipse.common.odom.undo.UndoRedoMementoOriginator;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.mcs.xml.jaxp.JAXPTransformerMetaFactory;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import com.volantis.testtools.mocks.MockFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * A manual GUI test for CategoryCompositeBuilder.
 */
public class CategoryCompositeBuilderTest extends ControlsTestAbstract {

    /**
     * A Combo widget with a selection of devices.
     */
    private Combo deviceCombo;

    /**
     * A Combo widget with a selection of categories.
     */
    private Combo categoryCombo;

    /**
     * The DeviceRepositoryAccessorManager used by the CategoryCompositeBuilder.
     */
    private DeviceRepositoryAccessorManager deviceRAM;

    /**
     * The ODOMSelectionManager to use for the tests.
     */
    private ODOMSelectionManager manager;

    /**
     * The currently selected category.
     */
    private String currentCategory;

    /**
     * The Composite container for the category compoiste. This is updated with
     * a new category composite whenever a different category is selected.
     */
    private ScrolledComposite scroller;

    /**
     * The category composite created by the builder.
     */
    private Composite categoryComposite;

    /**
     * The device repository file, this will be cleaned up automagically.
     */
    private File deviceRepositoryFile;

    // javadoc inherited
    public CategoryCompositeBuilderTest(String title,
            File deviceRepositoryFile) {

        super(title);
        this.deviceRepositoryFile = deviceRepositoryFile;
    }

    // javadoc inherited
    public void createControl() {
        Composite topLevel = new Composite(getShell(), SWT.NONE);
        topLevel.setLayout(new GridLayout(2, false));

        Composite comboContainer = new Composite(topLevel, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        comboContainer.setLayout(gridLayout);
        comboContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

        scroller = new ScrolledComposite(topLevel,
                SWT.H_SCROLL | SWT.V_SCROLL);
        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);
        scroller.setAlwaysShowScrollBars(false);
        scroller.setLayoutData(new GridData(GridData.FILL_BOTH));

        try {
            deviceRAM = new DeviceRepositoryAccessorManager(
                    deviceRepositoryFile.getPath(),
                    new JAXPTransformerMetaFactory(),
                    new DeviceODOMElementFactory(), false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }

        manager = new ODOMSelectionManager(null);

        // Create the widgets for the test
        createDeviceCombo(comboContainer);
        createCategoryCombo(comboContainer);
        createCategoryComposite(currentCategory = categoryCombo.getText());

        handleDeviceChange();
    }

    /**
     * Creates a Combo widget with device names.
     * @param parent the parent Composite.
     */
    private void createDeviceCombo(Composite parent) {
        Label deviceLabel = new Label(parent, SWT.NONE);
        deviceLabel.setText("Choose device:");
        final String[] deviceNames = new String[]{
            "TV", "Mobile", "PC", "Voice", "Clamshell", "Handset",
            "Pixo-Handset", "SMS-Handset", "WAP-Handset", "Sony-WAP",
            "Sony-CMD-Z5-WML", "PC", "PC-UNIX", "PC-Win32", "PC-Unix-Mozilla",
            "PC-UNIX-Nautilus", "PC-UNIX-Konqueror", "PC-UNIX-Konqueror-2",
            "PC-UNIX-Konqueror-3", "PC-Win32-IE", "PC-Win32-Mozilla",
            "Web-Box", "Microsoft-WebTV", "Master"
        };
        deviceCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < deviceNames.length; i++) {
            deviceCombo.add(deviceNames[i]);
        }
        deviceCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleDeviceChange();
            }
        });
        deviceCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Creates a Combo widget with category names.
     * @param parent the parent Composite.
     */
    private void createCategoryCombo(Composite parent) {
        Label categoryLabel = new Label(parent, SWT.NONE);
        categoryLabel.setText("Choose category:");
        final String[] categoryNames = new String[]{
            "system", "misc", "output", "network", "image", "audio",
            "browser", "identification", "ergonomics", "dynvis", "input",
            "security", "location", "rules", "protocol", "message"
        };
        categoryCombo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < categoryNames.length; i++) {
            categoryCombo.add(categoryNames[i]);
        }
        categoryCombo.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleCategoryChange();
            }
        });
        categoryCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    }

    /**
     * Handles selecting a device from the device Combo by retrieving the
     * device element and simulating an ODOMSelectionEvent for the
     * ODOMSelectionManager.
     */
    private void handleDeviceChange() {
        final ODOMElement deviceElement = (ODOMElement)
                deviceRAM.retrieveDeviceElement(deviceCombo.getText());

        final StructuredSelection structuredSelection =
                new StructuredSelection(deviceElement);

        // An empty implementation as the selection provider isn't currently
        // used by the {@link ODOMSelectionManager#selectionChanged} method.
        ISelectionProvider sp = new ISelectionProvider() {
            public void addSelectionChangedListener(
                    ISelectionChangedListener iSelectionChangedListener) {
            }

            public ISelection getSelection() {
                return null;
            }

            public void removeSelectionChangedListener(
                    ISelectionChangedListener iSelectionChangedListener) {
            }

            public void setSelection(ISelection iSelection) {
            }
        };

        // Create the selection event and give it to the manager.
        SelectionChangedEvent event =
                new SelectionChangedEvent(sp, structuredSelection);
        manager.selectionChanged(event);
    }

    /**
     * Handles selecting a policy from the policy Combo. Action is taken only
     * if the policy changes. When the policy changes, a new PolicyController
     * is created.
     */
    private void handleCategoryChange() {
        String categoryName = categoryCombo.getText();
        if (!categoryName.equals(currentCategory)) {
            currentCategory = categoryName;
            createCategoryComposite(categoryName);
        }
    }

    /**
     * Creates the category composite for the named category and refreshes the
     * scrolling display.
     * @param categoryName the name of the category
     */
    private void createCategoryComposite(String categoryName) {

        UndoRedoMementoOriginator orig = new UndoRedoMementoOriginator() {
            public UndoRedoMemento takeSnapshot() {
                return null;
            }
            public void restoreSnapshot(UndoRedoInfo undoRedoInfo) {
            }
        };

        DeviceEditorContext context =
                DeviceEditorContext.createDeviceEditorContext(
                        new MockFile("name"),
                        orig,
                        deviceRAM);

        categoryComposite = CategoryCompositeBuilder.
                buildCategoryComposite(scroller, SWT.NONE, categoryName,
                        "Master", context);
        scroller.setContent(categoryComposite);
        refresh();
    }

    /**
     * Performs a "refresh" of the scrolled area, so that the display's size
     * is recomputed, and the scroll bars adjust accordingly.
     */
    private void refresh() {
        categoryComposite.setSize(
                categoryComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        categoryComposite.layout();
        categoryComposite.pack();
        scroller.setMinSize(
                categoryComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        return "";
    }

    // javadoc inherited
    public static void main(String[] args) {
        TemporaryFileManager manager = new TemporaryFileManager(
                new TestDeviceRepositoryCreator());
        try {
            manager.executeWith(new TemporaryFileExecutor() {
                public void execute(File repositoryFile) throws Exception {
                    new CategoryCompositeBuilderTest(
                            "CategoryCompositeBuilderTest", repositoryFile).
                            display();
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

 16-Nov-04	4394/8	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 13-Sep-04	5315/6	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 13-Sep-04	5315/4	geoff	VBM:2004082404 Improve testsuite device repository test speed. (merge conflicts)

 27-Aug-04	5315/1	geoff	VBM:2004082404 Improve testsuite device repository test speed.

 03-Sep-04	5405/1	allan	VBM:2004082009 Ensure PolicyControllers have a device name when they need it

 03-Sep-04	5347/1	allan	VBM:2004082009 Ensure PolicyControllers have a device name when they need it

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 20-Apr-04	3909/1	pcameron	VBM:2004031004 Added CategoryCompositeBuilder

 ===========================================================================
*/
