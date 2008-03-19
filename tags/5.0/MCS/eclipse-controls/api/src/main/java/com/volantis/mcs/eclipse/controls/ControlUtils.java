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
package com.volantis.mcs.eclipse.controls;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;

/**
 * General utility class for Controls.
 */
public class ControlUtils {
    /**
     * Key constant for naming controls using Control.setData(key, name).
     */
    public static final String CONTROL_NAME_KEY = "controlName";

    /**
     * Create a Composite that takes up all the available space in the parent
     * Composite, has a white background and a sequence of messages from the
     * top downwards.
     *
     * @param parent   the parent Composite
     * @param style    the style for the messages. This can be used to set the
     * alignment of the text. Currently supported styles are:
     * {@link SWT#SEPARATOR}, {@link SWT#HORIZONTAL}, {@link SWT#VERTICAL},
     * {@link SWT#SHADOW_IN}, {@link SWT#SHADOW_OUT}, {@link SWT#SHADOW_NONE},
     * {@link SWT#CENTER}, {@link SWT#LEFT}, {@link SWT#RIGHT},
     * {@link SWT#WRAP}
     *
     *  Note that some of these styles are mutually exclusive.
     * 
     * @param messages an array of messages to display, one per line. Null
     *                 messages are ignored. Empty messages are displayed.
     * @return a Composite containing the message.
     */
    public static Composite createMessageComposite(Composite parent,
                                                   int style,
                                                   String[] messages) {
        Composite messagePage = new Composite(parent, SWT.NONE);
        messagePage.setBackground(parent.getDisplay().
                getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        messagePage.setLayout(new GridLayout());

        for (int i = 0; i < messages.length; i++) {
            if (messages[i] != null) {
                GridData labelGridData = new GridData(GridData.FILL_HORIZONTAL);
                Label messageLabel = new Label(messagePage, style);
                messageLabel.setLayoutData(labelGridData);
                messageLabel.setText(messages[i]);
                messageLabel.setBackground(parent.getDisplay().
                        getSystemColor(SWT.COLOR_LIST_BACKGROUND));
            }
        }

        messagePage.pack();

        return messagePage;
    }

    /**
     * Enable/disable a the descendents of a Composite.
     *
     * @param composite the Composite
     * @param enabled   true to enable the descendents of the Composite;
     *                  false to disable the descendents of the Composite.
     */
    public static void setEnabledHierarchical(Composite composite,
                                              boolean enabled) {
        Control children [] = composite.getChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Composite) {
                setEnabledHierarchical((Composite) children[i], enabled);
            }
            children[i].setEnabled(enabled);
        }
    }

    /**
     * Search for a named control and if found call setFocus on this control.
     * The search is hierarchical in that it will search all the descendent
     * controls of the given composite until it finds the named control or
     * all controls have been searched.
     * <p/>
     * The name of the control is obtained using Contro.getData(String, Object)
     * where the String (i.e. the key) is Control.CONTROL_NAME_KEY.
     *
     * @param composite   the Composite the search
     * @param controlName the name of the Control on which to set focus.
     * @return true if the named Control was found and setFocus() was called
     *         on this Control; false otherwise
     */
    public static boolean setFocusHierarchical(Composite composite,
                                               String controlName) {
        boolean focused = false;

        Control children [] = composite.getChildren();
        for (int i = 0; i < children.length && !focused; i++) {
            Object childControlName = children[i].getData(CONTROL_NAME_KEY);
            if (childControlName != null &&
                    childControlName.equals(controlName)) {
                children[i].setFocus();
                focused = true;
            }
            if (!focused && children[i] instanceof Composite) {
                focused = setFocusHierarchical((Composite) children[i],
                        controlName);
            }
        }

        return focused;
    }

    /**
     * Get a session property as a boolean.
     *
     * @param qName the QualifiedName
     * @return the sessionProperty associated with the given QualifiedName as
     *         a boolean. If the property does not exist false is returned otherwise
     *         the value of the property converted to a boolean is returned. It is
     *         assumed that the requested property has been set as a Boolean.
     */
    public static boolean getPersistentPropertyAsBoolean(QualifiedName qName) {
        String result = getPersistentProperty(qName);
        boolean returnValue = result == null ? false :
                Boolean.valueOf(result).booleanValue();
        return returnValue;
    }

    /**
     * Get a session property keyed on a QualifiedName.
     *
     * @param qName the QualifiedName
     * @return the persistent property associated with the given QualifiedName
     *         or an empty String if the property has not been set.
     */
    public static String getPersistentProperty(QualifiedName qName) {
        String result = null;
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            result = root.getPersistentProperty(qName);
            result = result == null ? "" : result;
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
        return result;
    }

    /**
     * Set a persistent property keyed on a QualifiedName.
     *
     * @param qName the QualifiedName
     * @param value the Object representing the value of the session property
     */
    public static void setPersistentProperty(QualifiedName qName, String value) {
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            root.setPersistentProperty(qName, value);
        } catch (CoreException e) {
            EclipseCommonPlugin.handleError(ControlsPlugin.getDefault(), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-May-05	8260/1	allan	VBM:2005042107 Implement and fix setFocus(XPath) for theme design

 16-May-05	8201/3	allan	VBM:2005042107 Implement and fix setFocus(XPath) for theme design

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6244/1	allan	VBM:2004111802 Stop using SWT.COLOR_WHITE for backgrounds

 16-Aug-04	5206/1	allan	VBM:2004081201 Auto-migration of mdpr with dialog.

 13-Aug-04	5202/1	allan	VBM:2004072803 Fix enablement of ListValueBuilder.

 27-Apr-04	4050/5	pcameron	VBM:2004040701 Added a device Information page and augmented DeviceRepositoryBrowser's title

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 07-Apr-04	3740/1	allan	VBM:2004040508 Make update client available for testing.

 ===========================================================================
*/
