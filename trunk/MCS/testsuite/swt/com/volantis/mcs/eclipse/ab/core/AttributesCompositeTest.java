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
package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.common.Filter;
import com.volantis.mcs.eclipse.common.PolicyAttributesDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.eclipse.controls.ControlsTestAbstract;
import com.volantis.testtools.stubs.ProjectStub;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * Testcase for AttributeComposite. This simply uses an imageComponent child
 * attributes AttributesComposite to demonstrate the building of an
 * AttributesComposite and showing and hiding of both individual attribute
 * controls and the whole of the AttributesComposite.
 */
public class AttributesCompositeTest extends ControlsTestAbstract {
    public void createControl() {
        final MCSProjectNature nature = new MCSProjectNature() {
            public IPath getPolicySourcePath() {
                return new Path("/tmp");
            }
        };

        ProjectStub project = new ProjectStub() {
            public IProjectNature getNature(String id) {
                return nature;
            }
        };
        nature.setProject(project);

        GridLayout layout = new GridLayout(2, false);
        final Composite container = new Composite(getShell(), SWT.DEFAULT);
        container.setLayout(layout);

        String[] filter = {"deviceName"};
        PolicyAttributesDetails attrDetails =
                new PolicyAttributesDetails("imageComponent",
                        new Filter(filter, Filter.EXCLUDE), true);
        final List list = new List(container, SWT.MULTI);
        final String[] attrs = attrDetails.getAttributes();
        for (int i = 0; i < attrs.length; i++) {
            list.add(attrs[i]);
        }

        final Composite fillerComposite = new Composite(container, SWT.NONE);
        fillerComposite.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_CYAN));

        fillerComposite.setLayout(new GridLayout(1, false));
        fillerComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
                GridData.VERTICAL_ALIGN_BEGINNING));


        AttributesCompositeBuilder acb = AttributesCompositeBuilder.
                getSingleton();
        final AttributesComposite ac = acb.buildAttributesComposite(fillerComposite,
                attrDetails, project, null);

        Button select = new Button(container, SWT.PUSH);
        select.setText("Select");

        Button showHide = new Button(container, SWT.PUSH);
        showHide.setText("Show/Hide");


        Button focus = new Button(container, SWT.PUSH);
        focus.setText("Set XPath Focus");

        Composite valueSetter = new Composite(container, SWT.PUSH);
        GridData valueSetterData = new GridData(GridData.FILL_HORIZONTAL);
        valueSetter.setLayoutData(valueSetterData);

        valueSetter.setLayout(new GridLayout(2, false));


        GridData textData = new GridData(GridData.FILL_HORIZONTAL);
        final Text text = new Text(valueSetter, SWT.BORDER | SWT.SINGLE);
        text.setLayoutData(textData);

        Button setValue = new Button(valueSetter, SWT.PUSH);
        setValue.setText("Set Value");
        setValue.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                String[] selection = list.getSelection();
                if (selection != null && selection.length > 0) {
                    ac.setAttributeValue(selection[0], text.getText());
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        focus.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                String[] selection = list.getSelection();
                if (selection != null && selection.length > 0) {
                    XPath path = new XPath("/@" + selection[0]);
                    System.out.println("XPath focus success = " +
                            ac.setFocus(path));
                }
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        showHide.addSelectionListener(new SelectionListener() {
            boolean acVisible = true;

            public void widgetSelected(SelectionEvent event) {
                acVisible = !acVisible;
                ac.setVisible(acVisible);
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });

        select.addSelectionListener(new SelectionListener() {
            public void widgetSelected(SelectionEvent event) {
                ac.setVisible(list.getSelection());
                fillerComposite.layout(true);
                fillerComposite.pack();
            }

            public void widgetDefaultSelected(SelectionEvent event) {
                widgetSelected(event);
            }
        });
        Label label = new Label(fillerComposite, SWT.BORDER);
        label.setText("This label should move up and/or down when controls become visible/invisible.");
        label.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_YELLOW));
        ac.setEnabled(false);
    }

    // javadoc inherited
    public String getSuccessCriteria() {
        String message = "Select attributes on the left whose controls to " +
                "show in the AttributesComposite on the right.\n\n";
        message += "The AttributesComposite should contain all the controls " +
                "for setting all generic image asset attributes.\n\n";
        message += "Clicking the select button show cause only the selected " +
                "attributes to be shown in the AttributesComposite and for " +
                "the AttributesComposite controls and labels to be layed out " +
                "as if they are the only controls in the AttributesComposite " +
                "(i.e. no gaps where previously displayed controls were).\n\n";
        message += "Clicking the Show/Hide button should show or hide the" +
                "AttributesComposite. This action should not change the list" +
                "of visible controls/labels (i.e. a hide followed by a show" +
                "should result in the same visible controls as before the " +
                "hide.\n\n";
        message += "Clicking the XPath Focus button should set the focus to" +
                "the control corresponding to the first selected attribute " +
                "in the list. This also outputs the success status of the " +
                "focus call to stdout.\n\n";
        message += "Clicking the Set Value button should set the contents " +
                "of the adjacent text field into the first control of the " +
                "first selected attribute.\n";
        return message;
    }

    /**
     * Construct a new AttributesCompositeTest.
     * @param title The test title.
     */
    public AttributesCompositeTest(String title) {
        super(title);
    }


    /**
     * The main method must be implemented by the test class writer.
     * @param args the ColorSelector does not require input arguments.
     */
    public static void main(String[] args) {
        new AttributesCompositeTest("AttributesComposite Test").display();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 17-May-04	4231/2	tom	VBM:2004042704 Fixedup the 2004032606 change

 23-Feb-04	3057/1	byron	VBM:2004021105 Accelerator keys Ctrl+c and Ctrl+x , Ctrl+v do not work within editors

 16-Feb-04	2891/5	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management - scrollbar fixed

 13-Feb-04	2891/3	byron	VBM:2003121508 Eclipse PM Layout Editor: Format Attributes View: Real Estate Management

 19-Dec-03	2237/3	byron	VBM:2003112804 Provide an MCS project builder

 17-Dec-03	2213/3	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 16-Dec-03	2213/1	allan	VBM:2003121401 More editors and fixes for presentable values.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 28-Nov-03	2013/5	allan	VBM:2003112501 Test setAttributeValue.

 28-Nov-03	2013/3	allan	VBM:2003112501 Support multi-attribute controls and XPathFocusable.

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 ===========================================================================
*/
