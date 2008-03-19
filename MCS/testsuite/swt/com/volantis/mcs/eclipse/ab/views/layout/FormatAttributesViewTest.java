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

package com.volantis.mcs.eclipse.ab.views.layout;

import com.volantis.mcs.eclipse.ab.editors.dom.ProxyElement;
import com.volantis.mcs.eclipse.ab.core.MCSProjectNature;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.mcs.layouts.LayoutSchemaType;
import com.volantis.testtools.stubs.ProjectStub;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Test the Format Attributes View.
 * Note that this test relies on the fact that the volantis-mcs-ab.jar contains
 * the format-attributes-view.xsd and format-attributes-view.xml. If these
 * files occur in the classpath BEFORE volantis-mcs-ab.jar then this test
 * will not work. It is advised to remove the xsd from the build classes
 * directory.
 */
public class FormatAttributesViewTest extends ControlsTestAbstract {

    private static List sections = new ArrayList();
    private IProject project = null;
    private boolean initializeComplete = false;

    static boolean visible = false;
    private List sectionComposites;

    private static List scrollers = new ArrayList(1);

    private static final int MARGIN_HEIGHT = 5;
    private static final int MARGIN_WIDTH = 5;
    private static final int VERTICAL_SPACING = 5;
    private static final int VERTICAL_SCROLL_INCREMENT = 20;
    private static final int HORIZONTAL_SCROLL_INCREMENT = 20;

    /**
     * Default constructor.
     * @param title the test's title.
     */
    public FormatAttributesViewTest(String title) {
        super(title);
    }


    private void initializeAttributeList() {
        try {
            String name = "format-attributes-view.xml";

            XMLReader xmlReader = createValidatingReader(sections);
            Class aClass = getResourceClass();
            System.out.println("xmlReader = " + aClass.getResource(name));
            xmlReader.parse(new InputSource(aClass.getResourceAsStream(name)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method.
     */
    private Class getResourceClass() {
        return ConfigurationHandler.class;
    }

    /**
     * Helper method for creating the XMLReader with the validation enabled.
     * @param theSections the sections list
     * @return the newly created XMLReader.
     */
    private XMLReader createValidatingReader(List theSections)
            throws SAXNotRecognizedException, SAXNotSupportedException {

        XMLReader reader = new com.volantis.xml.xerces.parsers.SAXParser();

        reader.setContentHandler(new ConfigurationHandler(theSections));
        reader.setErrorHandler(new ConfigurationErrorHandler());

        reader.setFeature("http://xml.org/sax/features/namespaces", true);
        reader.setFeature("http://xml.org/sax/features/validation", true);
        reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);

        URL url = getResourceClass().getResource("format-attributes-view.xsd");
        System.out.println("url = " + url.toExternalForm());
        reader.setProperty("http://apache.org/xml/properties/schema/" +
                "external-noNamespaceSchemaLocation",
                url.toExternalForm());

        return reader;
    }

    // javadoc inherited
    public void createControl() {
        final MCSProjectNature nature = new MCSProjectNature() {
            public IPath getPolicySourcePath() {
                return new Path("/tmp");
            }
        };

        project = new ProjectStub() {
            public IProjectNature getNature(String id) {
                return nature;
            }
        };
        nature.setProject(project);

        if (!initializeComplete) {
            initializeAttributeList ();
            initializeComplete = true;
        }


        final Shell shell = getShell();
        shell.setLocation(0,0);
        shell.setSize(500, 500);
        final Composite parent = new Composite(shell, SWT.NONE);
        parent.setLayout(new GridLayout(2, false));
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite listComposite = new Composite(parent, SWT.NONE);

        final TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        tabFolder.setLayout(new GridLayout(1, false));
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        createAttributesTab(tabFolder, "Test Label");


        listComposite.setLayout(new GridLayout(2, false));

        final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
                listComposite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        list.setSize(100, 200);

        // Get all the attribute names and add them to a List Widget.
        Iterator iterator = sections.iterator();
        List allAttributes = new ArrayList();
        while (iterator.hasNext()) {
            SectionDetails details = (SectionDetails)iterator.next();
            allAttributes.addAll(Arrays.asList(details.getDetails().getAttributes()));
        }

        String[] attrs = new String[allAttributes.size()];
        attrs = (String[]) allAttributes.toArray(attrs);
        for (int i = 0; i < attrs.length; i++) {
            list.add(attrs[i]);
        }

        // Allow the user to show the selected attributes on demand.
        Button show = new Button(listComposite, SWT.NONE);

        show.setText("Show selection");
        show.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                Iterator iterator = sectionComposites.iterator();
                while (iterator.hasNext()) {
                    SectionDetailsComposite sdc =
                            (SectionDetailsComposite) iterator.next();
                    sdc.setVisible(list.getSelection());
                }
//                tabFolder.layout();
                iterator = scrollers.iterator();
                while (iterator.hasNext()) {
                    ScrolledComposite scroller = (ScrolledComposite)iterator.next();
                    refresh(scroller);
//                    scroller.layout();
//                    ((Composite)scroller.getContent()).layout();
                }
            }
        });

/*
        updateSectionCompositesFirstColumnWidth(sectionComposites.iterator(),
                determineMaxWidth(sectionComposites.iterator()));
*/

        shell.layout();
        shell.pack();
    }

    /**
     *
     * @param iterator
     * @param width
     */
    private void updateSectionCompositesFirstColumnWidth(Iterator iterator,
                                                         int width) {
        while (iterator.hasNext()) {
            SectionDetailsComposite sdc =
                    (SectionDetailsComposite) iterator.next();
            sdc.setFirstColumnWidth(width);
        }
    }

    /**
     *
     * @param iterator
     * @return the maximum width
     */
    private int determineMaxWidth(Iterator iterator) {
        int maxWidth = 0;
        while (iterator.hasNext()) {
            SectionDetailsComposite sdc =
                    (SectionDetailsComposite) iterator.next();
            maxWidth = Math.max(sdc.getFirstColumnWidth(), maxWidth);
        }
        return maxWidth;
    }

    /**
     * Create the attributes tab control.
     * @param tabFolder
     * @param label
     */
    private void createAttributesTab(TabFolder tabFolder, String label) {
        // Create a tab tabItem.
        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        tabItem.setText(label);

        final ProxyElement element = new ProxyElement(new LayoutProxyElementDetails(),
                new ODOMFactory());

        final ODOMSelectionFilter filter = new ODOMSelectionFilter(null,
                new String[] {
                    "rootElementName",
                    LayoutSchemaType.DEVICE_LAYOUT_CANVAS_FORMAT.getName(),
                    LayoutSchemaType.DEVICE_LAYOUT_MONTAGE_FORMAT.getName()
                 });

        sectionComposites = new ArrayList();
        createScrollableTabControl(tabFolder, tabItem, element, sections, project,
                filter, sectionComposites);

    }


    /**
     * Helper method
     */
    private void createScrollableTabControl(TabFolder tabFolder,
                                            TabItem tabItem,
                                            ProxyElement element,
                                            List sections,
                                            IProject project,
                                            ODOMSelectionFilter filter,
                                            List sectionDetailsComposites) {

        final ScrolledComposite scroller = new ScrolledComposite(tabFolder,
                SWT.H_SCROLL | SWT.V_SCROLL);

        scroller.getVerticalBar().setIncrement(VERTICAL_SCROLL_INCREMENT);
        scroller.getHorizontalBar().setIncrement(HORIZONTAL_SCROLL_INCREMENT);

        scroller.setExpandHorizontal(true);
        scroller.setExpandVertical(true);

        final Composite scrollable = new Composite(scroller, SWT.NONE);
        scroller.setLayoutData(new GridData(GridData.FILL_BOTH));
        scroller.setContent(scrollable);
        scrollers.add(scroller);

        GridLayout layout = new GridLayout();
        layout.marginHeight = MARGIN_HEIGHT;
        layout.marginWidth = MARGIN_WIDTH;
        layout.verticalSpacing = VERTICAL_SPACING;
        scrollable.setLayout(layout);


        tabItem.setControl(scroller);

        Color[] colors = {
            scrollable.getDisplay().getSystemColor(SWT.COLOR_CYAN),
            scrollable.getDisplay().getSystemColor(SWT.COLOR_BLUE),
            scrollable.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN),
            scrollable.getDisplay().getSystemColor(SWT.COLOR_YELLOW),
            scrollable.getDisplay().getSystemColor(SWT.COLOR_RED)
        };
        int colourIndex = 0;

        // Iterate over the sections and create a SectionDetailsComposite
        // object object whilst ensuring appropriate listeners have been
        // added.
        Iterator iterator = sections.iterator();
        while (iterator.hasNext()) {
            SectionDetails section = (SectionDetails) iterator.next();
            // Create the section detail first so we have something to add the
            // attributes composite controls into.
            SectionDetailsComposite sdc = new SectionDetailsComposite(
                    scrollable, SWT.NONE, section, element,
                    project,
                    new ODOMSelectionManager(null),
                    filter,
                    null);
            sdc.setBackground(colors[colourIndex%colors.length]);
            ++colourIndex;
            sectionDetailsComposites.add(sdc);
        }

        updateSectionCompositesFirstColumnWidth(sectionDetailsComposites.iterator(),
                determineMaxWidth(sectionDetailsComposites.iterator()));

        refresh(scroller);
    }


    /**
     * Refresh the scroll bar area.
     * @param scroller the scroller.
     */
    private void refresh(ScrolledComposite scroller) {
        Composite scrollable = (Composite)scroller.getContent();
        scrollable.setSize(scrollable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrollable.layout();
        scroller.setMinSize(scroller.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }

    public String getSuccessCriteria() {
        return "Success";
    }

    /**
     * The main method must be implemented by the test class writer.
     * @param args the FontSelector does not require input arguments.
     */
    public static void main(String[] args) {
        FormatAttributesViewTest formatAttributesViewTest =
                new FormatAttributesViewTest("FormatAttributesView Test");
        formatAttributesViewTest.display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 17-May-04	4231/1	tom	VBM:2004042704 Fixedup the 2004032606 change

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 16-Feb-04	2891/6	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - scrollbar fixed

 13-Feb-04	2891/4	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - take 2

 13-Feb-04	2891/2	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 02-Feb-04	2707/2	byron	VBM:2003121506 Eclipse PM Layout Editor: Format Attributes View: Row Page

 29-Jan-04	2752/1	byron	VBM:2004012602 Fixed test case and reduced garbage creation

 22-Jan-04	2540/1	byron	VBM:2003121505 Added main formats attribute page

 ===========================================================================
*/
