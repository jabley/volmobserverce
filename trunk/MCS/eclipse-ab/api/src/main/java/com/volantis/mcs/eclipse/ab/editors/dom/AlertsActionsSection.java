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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.ab.editors.EditorMessages;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.ValidationListener;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.controls.forms.FormSection;
import com.volantis.mcs.eclipse.controls.forms.SectionFactory;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.markers.MarkerViewUtil;
import org.eclipse.ui.views.tasklist.TaskList;

import java.text.MessageFormat;

/**
 * The Alerts and Actions form section. This form section displays information
 * regarding the problems, warnings and task associated with a ODOM based
 * resource.
 */
public class AlertsActionsSection extends FormSection {

    /**
     * Prefix for resources associated with this class.
     */
    private static final String RESOURCE_PREFIX =
            "AlertsActionsSection."; //$NON-NLS-1$

    /**
     * The display area control for this FormSection.
     */
    private Composite displayArea;

    /**
     * The hyperlink.
     */
    private Hyperlink hyperlink;

    /**
     * The inactive foreground colour of the hyperlink.
     */
    private Color inactiveLinkForeground;

    /**
     * The active foreground colour of the hyperlink.
     */
    private Color activeLinkForeground;

    /**
     * The message label.
     */
    private Label messageLabel;

    /**
     * The link text.
     */
    private String linkText;

    /**
     * The localized policy type name.
     */
    private String localizedPolicyType;

    /**
     * The layout for displayArea.
     */
    private StackLayout displayAreaLayout;

    /**
     * The ODOMEditorContext associated with this AlertsActionsSection.
     */
    private final ODOMEditorContext context;

    /**
     * The last found array of problem markers.
     */
    private IMarker[] currentProblemMarkers;

    /**
     * The last found array of task markers that we are interested in.
     */
    private IMarker[] currentTaskMarkers;

    /**
     * The ValidationListener associated with this AlertsActionsSection.
     */
    private final ValidationListener validationListener;

    /**
     * Construct a new AlertsActionsSection.
     * @param parent The parent Composite.
     * @param style The style - this is currently unused.
     * @param context The ODOMEditorContext
     * @throws IllegalArgumentException If either resource or policyType
     * are null.
     */
    public AlertsActionsSection(Composite parent, int style,
                                final ODOMEditorContext context) {
        super(parent, style);

        if (context == null) {
            throw new IllegalArgumentException(
                    "Cannot be null: context."); //$NON-NLS-1$
        }

        this.context = context;
        createDisplayArea();

        // Create a ValidationListener and add it to the XMLValidator
        // associated with the ODOMEditorContext so that we are notified
        // when validation occurs allowing us to refresh and if necessary
        // redecorate images that indicate or should not now indecate errors.
        validationListener = new ValidationListener() {
            public void validated() {
                refresh();
            }
        };
        context.addValidationsListener(validationListener);

    }

    /**
     * Override setFocus() to set focus to the message hyperlink.
     */
    public boolean setFocus() {
        return hyperlink.setFocus();
    }

    /**
     * Dispose of the HyperlinkHandler.
     */
    private void disposeResources() {
        context.removeValidationsListener(validationListener);
    }

    /**
     * Refresh this AlertsActionsSection.
     */
    public void refresh() {
        displayArea.getDisplay().asyncExec(new Runnable() {
            public void run() {
                updateDisplayArea();
            }
        });
    }

    /**
     * Provides the control that contains the message.
     * @return displayArea
     */
    protected Control getDisplayArea() {
        if (displayArea == null) {
            createDisplayArea();
        }
        return displayArea;
    }

    /**
     * Create the display area and along with its constituent controls and
     * behaviour.
     *
     * NOTE: This method uses the internal Eclipse classes HyperlinkHandler
     * and SelectableFormLabel. Both of these are in v2.1.0 of the
     * org.eclipse.update.ui.forms plugin and may in future need to be
     * ported to Volantis code to ensure compatibility with newer versions
     * of Eclipse. Version 2.1.2 of Eclipse is the current stable release
     * and contains v2.1.0 of the update.ui.forms plugin. However, v3 of
     * Eclipse does not include this plugin.
     * @todo later check Eclipse v3 compatibilty and update if necessary
     */
    private void createDisplayArea() {

        Section section =
                SectionFactory.createSection(this,
                        EditorMessages.getString(RESOURCE_PREFIX + "title"),
                        null);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        section.setLayoutData(data);

        displayArea = new Composite(section, SWT.NONE);
        displayAreaLayout = new StackLayout();
        displayArea.setLayout(displayAreaLayout);

        messageLabel = new Label(displayArea, SWT.LEFT);
        messageLabel.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        messageLabel.setText(EditorMessages.getString(RESOURCE_PREFIX +
                "label.message")); //$NON-NLS-1$

        hyperlink = new Hyperlink(displayArea, SWT.NONE);
        hyperlink.setBackground(getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        activeLinkForeground =
                JFaceColors.getActiveHyperlinkText(getDisplay());
        inactiveLinkForeground = JFaceColors.getHyperlinkText(getDisplay());

        hyperlink.setForeground(inactiveLinkForeground);
        hyperlink.setUnderlined(true);
        
        IHyperlinkListener hyperlinkListener = new IHyperlinkListener() {
            public void linkEntered(HyperlinkEvent event) {
                hyperlink.setForeground(activeLinkForeground);
            }

            public void linkExited(HyperlinkEvent event) {
                hyperlink.setForeground(inactiveLinkForeground);
            }

            public void linkActivated(HyperlinkEvent event) {
                try {
                    // Show the views associated with the markers for
                    // the resource associated with this section.
                    final IWorkbenchPage activePage =
                            PlatformUI.getWorkbench().
                            getActiveWorkbenchWindow().
                            getActivePage();

                    // The task view
                    final TaskList taskList =
                            currentTaskMarkers.length > 0 ? (TaskList)
                            activePage.showView(MarkerViewUtil.
                            getViewId(currentTaskMarkers[0])) : null;


                    Display.getCurrent().asyncExec(new Runnable() {
                        public void run() {
                            if (taskList != null) {
                                taskList.setSelection(new
                                        StructuredSelection(currentTaskMarkers),
                                        true);
                            }
                            if (currentProblemMarkers.length>0) {
                                MarkerViewUtil.showMarker(activePage,
                                        currentProblemMarkers[0], true);
                            }
                        }
                    });
                } catch (PartInitException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                } catch (CoreException e) {
                    EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
                }
            }
        };
        hyperlink.addHyperlinkListener(hyperlinkListener);

        linkText = EditorMessages.getString(RESOURCE_PREFIX +
                "link.message"); //$NON-NLS-1$
        localizedPolicyType = EclipseCommonMessages.
                getLocalizedPolicyName(context.getPolicyType());

        // Set the Hyperlink text since it must be non-null.
        updateHyperlinkText(0, 0, 0);

        updateDisplayArea();

        data = new GridData(GridData.FILL_HORIZONTAL);
        displayArea.setLayoutData(data);
        section.setClient(displayArea);

        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent event) {
                AlertsActionsSection.this.disposeResources();
            }
        });
    }

    /**
     * Look for any alert/action markers associated with the resource this
     * AlertsActionsSection reports on. If there are are any, update the
     * link message text to reflect the summary details and set the
     * link as the top control in the layout. If there are none
     * set the message label as the top control in the layout.
     */
    private void updateDisplayArea() {

        try {
            // ensure the widgets are still there.
            if (!displayArea.isDisposed()) {
                int errorCount = countProblemMarkers(IMarker.SEVERITY_ERROR);
                int warningCount = countProblemMarkers(IMarker.SEVERITY_WARNING);
                int taskCount = countTaskMarkers();
                if (errorCount == 0 && warningCount == 0 && taskCount == 0) {
                    displayAreaLayout.topControl = messageLabel;
                } else {
                    updateHyperlinkText(errorCount, warningCount, taskCount);

                    displayAreaLayout.topControl = hyperlink;
                }
                // call layout() on the display so that it redraws itself

                displayArea.layout();
                // The display area must then be packed to ensure the label
                // widget resizes and updates correctly.
                displayArea.pack();
            }
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ABPlugin.getDefault(), e);
        }
    }

    /**
     * Update the hyperlink text. The text details the errors, warnings
     * and tasks associated with the current policy.
     * @param errorCount the error count
     * @param warningCount the warning count
     * @param taskCount the task count
     */
    private void updateHyperlinkText(int errorCount, int warningCount,
                                     int taskCount) {
        String args [] = new String[4];
        args[0] = String.valueOf(errorCount);
        args[1] = String.valueOf(warningCount);
        args[2] = String.valueOf(taskCount);
        args[3] = localizedPolicyType;

        MessageFormat format = new MessageFormat(linkText);
        hyperlink.setText(format.format(args));
    }

    /**
     * Count all the problem markers of a particular severity.
     * @param severity The severity of problem marker to count.
     * @return The number of problem makers with the specified severity.
     * @throws CoreException If there is a problem accessing markers.
     */
    private int countProblemMarkers(int severity)
            throws CoreException {
        int result = 0;
        IResource resource = context.getPolicyResource();

        if (resource.exists()) {
            currentProblemMarkers = resource.findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);
            int notSeverity = ~severity;
            for (int i = 0; i < currentProblemMarkers.length; i++) {
                if (currentProblemMarkers[i].getAttribute(IMarker.SEVERITY,
                        notSeverity)
                        == severity) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * Count all the markers in a given set of markers of a particular type
     * @return The number of markers on the resource of the specified type.
     * @throws CoreException If there is a problem accessing markers.
     */
    private int countTaskMarkers() throws CoreException {
        IResource resource = context.getPolicyResource();
        if (resource.exists()) {
            currentTaskMarkers = resource.findMarkers(IMarker.TASK, true,
                    IResource.DEPTH_INFINITE);
        }
        return currentTaskMarkers.length;
    }

    /**
     * Add all the current markers together and return the resulting array.
     * @return All the current markers.
     */
    private IMarker[] provideAllMarkers() {
        updateDisplayArea();
        IMarker all [] = new IMarker[currentProblemMarkers.length +
                currentTaskMarkers.length];
        int i;
        for (i = 0; i < currentProblemMarkers.length; i++) {
            all[i] = currentProblemMarkers[i];
        }
        for (int i2 = i; i2 - i < currentTaskMarkers.length; i2++) {
            all[i2] = currentTaskMarkers[i2 - i];
        }

        return all;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 12-May-04	4281/1	matthew	VBM:2004051103 ODOMOutlinePage and AlertsActionsSection modified to stop Widget is disposed errors due to multithreading and callbacks

 10-May-04	4239/2	allan	VBM:2004042207 SaveAs on DeviceEditor.

 05-May-04	4115/5	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 05-May-04	4115/3	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Jan-04	2562/1	allan	VBM:2003112010 Handle outline view showing and closing.

 08-Jan-04	2431/6	allan	VBM:2004010404 Fix validation and display update issues.

 08-Jan-04	2431/4	allan	VBM:2004010404 Fix validation and display update issues.

 05-Jan-04	2376/1	pcameron	VBM:2003121718 Fixed error message truncation on overview pages

 17-Dec-03	2219/2	doug	VBM:2003121502 Added dom validation to the eclipse editors

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 04-Dec-03	2102/2	allan	VBM:2003112101 Create the AlertsActionsSection.

 ===========================================================================
*/
