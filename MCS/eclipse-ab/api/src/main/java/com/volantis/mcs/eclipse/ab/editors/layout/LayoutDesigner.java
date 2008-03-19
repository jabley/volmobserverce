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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMEditorContext;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionFilter;
import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.eclipse.controls.ControlUtils;
import com.volantis.mcs.layouts.LayoutSchemaType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.jdom.Element;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The main display area for the Layout editor design page.
 */
public class LayoutDesigner extends Composite {

    /**
     * The prefix for resources associated with ThemeDesignForm.
     */
    private static final String RESOURCE_PREFIX = "LayoutDesigner."; //$NON-NLS-1$

    /**
     * The message for when no device theme is available to edit.
     */
    private static final String NO_DEVICE_LAYOUT_MESSAGE =
            LayoutMessages.getString(RESOURCE_PREFIX + "noDeviceLayout"); //$NON-NLS-1$


    /**
     * The resource message for the deisgn label message format.
     */
    private static final String DESIGN_MESSAGE_FORMAT_STRING =
            LayoutMessages.getString(RESOURCE_PREFIX +
            "designLabel.text"); //$NON-NLS-1$

    /**
     * The MessageFormat that formats the design label.
     */
    private static final MessageFormat DESIGN_MESSAGE_FORMAT =
            new MessageFormat(DESIGN_MESSAGE_FORMAT_STRING);

    /**
     * Constant declaration for the device name.
     */
    private static final String DEVICE_NAME = "deviceName";

    /**
     * The ODOMEditorContext associated with this page.
     */
    private final ODOMEditorContext context;

    /**
     * The displayArea composite containing the page.
     */
    private Composite displayArea;

    /**
     * The layout for the displayArea.
     */
    private StackLayout displayAreaLayout;

    /**
     * The message page.
     */
    private Composite messagePage;

    /**
     * The label showing what device the layout being designed is for.
     */
    private Label designLabel;

    /**
     * A cache of previously built device layout design pages.
     * todo cleanup this cache when device layouts are deleted
     */
    private Map designPages = new HashMap();

    /**
     * The FormatCompositeBuilder used by this LayoutDesigner.
     */
    private FormatCompositeBuilder builder;

    /**
     * Store the device layout so that we can de-register ODOM listeners
     * if/when event selection changes.
     */
    private ODOMElement deviceLayout;

    /**
     * The odom change listener that is used to update the device label
     * if/when the device name changes.
     */
    private ODOMChangeListener odomChangeListener;

    /**
     * Construct a new LayoutDesigner
     * @param parent The parent Composite.
     * @param style The style of this ThemeDesignForm. Supports SWT.NONE.
     * @param context The ODOMEditorContext associated with this LayoutDesigner.
     */
    public LayoutDesigner(Composite parent, int style,
                          ODOMEditorContext context) {
        super(parent, style);

        this.context = context;

        builder = new FormatCompositeBuilder(context);

        setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        setLayout(new FillLayout());

        createDisplayArea();
    }


    /**
     * Update the display with the given layout. If layout is null
     * then the message page will be displayed asking the user to select
     * a single device layout. Otherwise the design page will shown to allow
     * the user to design the given layout.
     */
    public void updateDesigner(final ODOMElement deviceLayout) {
        // For some strange reason the LayoutDesign can appear to not be
        // disposed when some or all of its children are. This means that
        // we have to provide defensive tests for widget disposal in here
        // rather than in any callers that are listeners.
        if (!displayArea.isDisposed()) {
            if (deviceLayout == null) {
                messagePage.layout();
                displayAreaLayout.topControl = messagePage;
            } else if ((designLabel == null) || !designLabel.isDisposed()) {
                Composite designPage = (Composite)
                        designPages.get(deviceLayout);
                if (designPage == null) {
                    designPage = createDesignPage(displayArea, deviceLayout);
                    designPages.put(deviceLayout, designPage);
                }

                designLabel.setText("Layout Design");
                designLabel.pack();

                // Comparion using the handle is deliberate.
                if (this.deviceLayout != deviceLayout) {
                    // Remove the old listener, if there was one.
                    if (this.deviceLayout != null) {
                        this.deviceLayout.removeChangeListener(
                                odomChangeListener,
                                ChangeQualifier.ATTRIBUTE_VALUE);
                    }

                    // Update the device layout member variable.
                    this.deviceLayout = deviceLayout;
                }

                if (odomChangeListener == null) {
                    // lazily create the odom change listener.
                    odomChangeListener = new ODOMChangeListener() {
                        // javadoc inherited
                        public void changed(ODOMObservable node,
                                            ODOMChangeEvent event) {
                            final ODOMObservable source = event.getSource();

                            if (source.getParent() == node) {
                                if (DEVICE_NAME.equals(source.getName())) {
                                    designLabel.setText(getDeviceLayoutName(
                                            (String) event.getNewValue()));
                                    designLabel.pack();
                                }
                            }
                        }
                    };
                }

                this.deviceLayout.addChangeListener(odomChangeListener,
                        ChangeQualifier.ATTRIBUTE_VALUE);

                if (designPage != null) {
                    displayAreaLayout.topControl = designPage;
                }
            }

            displayArea.layout();
        }
    }

    /**
     * Helper method for getting the layout device name in the correct format
     * given a value.
     *
     * @param value the value to use whilst formatting the message.
     * @return the formatted device layout name.
     */
    private String getDeviceLayoutName(String value) {
        return DESIGN_MESSAGE_FORMAT.format(new String[]{value});
    }

    /**
     * Create the displayArea composite. This is parent of the two 'pages'
     * that can appear in this form. Using a StackLayout, the a FormatComposite
     * is shown or a label notifying the user that a single device layout should
     * be selected.
     */
    private void createDisplayArea() {
        displayArea = new Composite(this, SWT.NONE);
        displayAreaLayout = new StackLayout();
        displayArea.setLayout(displayAreaLayout);
        displayArea.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        createMessagePage(displayArea);

        displayAreaLayout.topControl = messagePage;

        displayArea.layout();
    }


    /**
     * Create a designPage for this LayoutDesigner.
     */
    private Composite createDesignPage(Composite parent,
                                       ODOMElement deviceLayout) {

        Composite container = new Composite(parent, SWT.NONE);

        container.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        int marginHeight = EditorMessages.getInteger("Editor." + //$NON-NLS-1$
                "marginHeight").intValue(); //$NON-NLS-1$
        int marginWidth = EditorMessages.getInteger("Editor." + //$NON-NLS-1$
                "marginWidth").intValue(); //$NON-NLS-1$
        int verticalSpacing = EditorMessages.getInteger("Editor." + //$NON-NLS-1$
                "verticalSpacing").intValue(); //$NON-NLS-1$

        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = marginHeight;
        gridLayout.marginWidth = marginWidth;
        gridLayout.verticalSpacing = verticalSpacing;
        container.setLayout(gridLayout);
        GridData data = new GridData(GridData.FILL_BOTH);
        container.setLayoutData(data);

        designLabel = new Label(container, SWT.NONE);
        Font font = designLabel.getFont();
        FontData fontData [] = font.getFontData();
        int designLabelHeight =
                LayoutMessages.getInteger(RESOURCE_PREFIX +
                "designLabel.height").intValue(); //$NON-NLS-1$
        fontData[0].setHeight(designLabelHeight);
        fontData[0].setStyle(SWT.BOLD);
        final Font newFont = new Font(designLabel.getDisplay(), fontData[0]);
        designLabel.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                newFont.dispose();
            }
        });

        designLabel.setFont(newFont);
        designLabel.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        final String labelText = "Layout Design";

        designLabel.setText(labelText);
        designLabel.pack();

        final ScrolledComposite deviceLayoutComposite =
                new ScrolledComposite(container, SWT.H_SCROLL | SWT.V_SCROLL);
        deviceLayoutComposite.setExpandHorizontal(true);
        deviceLayoutComposite.setExpandVertical(true);
        deviceLayoutComposite.setAlwaysShowScrollBars(true);
        deviceLayoutComposite.setLayout(new GridLayout());
        deviceLayoutComposite.setSize(container.getClientArea().width,
                container.getClientArea().height);
        data = new GridData(GridData.FILL_BOTH);
        deviceLayoutComposite.setLayoutData(data);

        deviceLayoutComposite.addControlListener(new ControlListener() {
            public void controlMoved(ControlEvent event) {
            }

            public void controlResized(ControlEvent event) {
                // Updating the size here will make the scrollbar update
                // properly. However, there is some strange behaviour at
                // least on linux where the sizes of some children seem to
                // shrink and grow irrationally. Adding a pack prevents this
                // weird behaviour but rendering is slightly less smooth.
                deviceLayoutComposite.setMinSize(
                        deviceLayoutComposite.computeSize(SWT.DEFAULT,
                                SWT.DEFAULT));
                deviceLayoutComposite.getChildren()[0].pack();
            }
        });

        initializeODOMChangeListener(deviceLayout, deviceLayoutComposite);

        initializeRootFormatComposite(deviceLayout, deviceLayoutComposite);

        return container;
    }

    /**
     * Initialize the root FormatComposite as the only child of a
     * ScrolledComposite.
     * @param deviceLayout the ODOMElement representing the device layout
     * containing the formats that make up the root FormatComposite and
     * its descendents.
     * @param deviceLayoutComposite the ScrolledComposite that will contain
     * the root FormatComposite as its only child.
     */
    private void
            initializeRootFormatComposite(final ODOMElement deviceLayout,
                                          final ScrolledComposite deviceLayoutComposite) {
        ODOMElement rootFormat = (ODOMElement) deviceLayout.getChildren().get(0);

        // Build a new FormatComposite for the given layout.
        final FormatComposite formatComposite = builder.build(deviceLayoutComposite,
                rootFormat, null);

        deviceLayoutComposite.setContent(formatComposite);
        deviceLayoutComposite.setSize(formatComposite.getSize().x,
                formatComposite.getSize().y);
        deviceLayoutComposite.layout(true);

        // Ensure that the ODOMSelectionManager is informed of FormatComposite
        // selection changes.
        formatComposite.
                addSelectionChangedListener(context.getODOMSelectionManager());

        // Ensure that FormatComposites are informaed of ODOMSelectionManager
        // selection changes.
        final ODOMElementSelectionListener listener =
                new ODOMElementSelectionListener() {
                    public void selectionChanged(ODOMElementSelectionEvent event) {
                        formatComposite.setSelection(event.getSelection());
                    }
                };

        final ODOMSelectionFilter filter =
                new ODOMSelectionFilter(null,
                        new String[]{
                            context.getRootElement().getName(),
                            LayoutSchemaType.CANVAS_LAYOUT.getName(),
                            LayoutSchemaType.MONTAGE_LAYOUT.getName()
                        });

        final ODOMSelectionManager manager = context.getODOMSelectionManager();

        manager.addSelectionListener(listener, filter);

        formatComposite.addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                manager.removeSelectionListener(listener, filter);
                // Set the selection to the device layout element otherwise
                // nothing will be selected.
                ArrayList selection = new ArrayList();
                selection.add(deviceLayout);
                manager.setSelection(selection);
            }
        });
    }

    /**
     * Initialize the ODOMChangeListener that will handle changes to a
     * layout element.
     * @param deviceLayout the ODOMElement whose changes to listen for.
     * @param deviceLayoutComposite the ScrolledComposite containing the
     * view of the specified layout.
     */
    private void
            initializeODOMChangeListener(final ODOMElement deviceLayout,
                                         final ScrolledComposite deviceLayoutComposite) {
        ODOMChangeListener listener = new ODOMChangeListener() {
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                if (event.getSource() instanceof Element) {
                    FormatComposite root = null;
                    if (event.getOldValue() == deviceLayout &&
                            event.getNewValue() == null) {
                        root =
                                (FormatComposite) deviceLayoutComposite.
                                getChildren()[0];
                        // The root format element been deleted
                        root.dispose();
                    } else if (event.getOldValue() == null &&
                            event.getNewValue() == deviceLayout) {
                        // A new root element has been created
                        Control children [] =
                                deviceLayoutComposite.getChildren();
                        if (children.length > 0) {
                            String message = "Attempted to create a new" +
                                    " root FormatComposite for a device " +
                                    "layout  when " +
                                    " there are existing child controls.";
                            throw new IllegalStateException(message);
                        }
                        initializeRootFormatComposite(deviceLayout,
                                deviceLayoutComposite);
                        root =
                                (FormatComposite) deviceLayoutComposite.
                                getChildren()[0];
                    }

                    if (root == null || !root.isDisposed()) {
                        deviceLayoutComposite.setMinSize(
                                deviceLayoutComposite.computeSize(SWT.DEFAULT,
                                        SWT.DEFAULT));
                        deviceLayoutComposite.layout();
                    }
                }
            }
        };
        deviceLayout.addChangeListener(listener, ChangeQualifier.HIERARCHY);
    }

    /**
     * Create a control that is just an empty page with a message notifying
     * the user that a device theme should be selected to edit.
     */
    private void createMessagePage(Composite parent) {
        messagePage = ControlUtils.createMessageComposite(parent, SWT.CENTER,
                new String[]{NO_DEVICE_LAYOUT_MESSAGE});
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Apr-05	7861/1	philws	VBM:2005042508 Port exception fixes from 3.3

 26-Apr-05	7846/1	philws	VBM:2005042508 Prevent erroneous updates causing exceptions during Layout Editor closure

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 04-Aug-04	4902/14	allan	VBM:2004071504 Rework issues

 03-Aug-04	4902/12	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 27-Apr-04	4050/1	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 24-Feb-04	3167/3	byron	VBM:2004021602 Layout Editor: Rename Device Type in Outline mode, not refreshing Design View - fixed javadoc

 23-Feb-04	3167/1	byron	VBM:2004021602 Layout Editor: Rename Device Type in Outline mode, not refreshing Design View

 05-Feb-04	2843/4	pcameron	VBM:2004020209 Added row and column insertion, plus scrolling hack

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 30-Jan-04	2549/14	pcameron	VBM:2004011201 FormatComposite structures and graphical layout editor

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 23-Jan-04	2726/5	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2726/3	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2726/1	pcameron	VBM:2004012301 Committed for integration into layout editor

 23-Jan-04	2720/3	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 22-Jan-04	2549/9	pcameron	VBM:2004011201 Committed for integration into layout editor

 ===========================================================================
*/
