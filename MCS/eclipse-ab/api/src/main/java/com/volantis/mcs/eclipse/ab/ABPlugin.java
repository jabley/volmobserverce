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
package com.volantis.mcs.eclipse.ab;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.osgi.framework.BundleContext;
import org.apache.log4j.Logger;

import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;

import com.volantis.mcs.bundles.BundleUtilities;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.common.EclipseConstants;

/**
 * The main plugin class to be used in the desktop.
 */
public class ABPlugin extends AbstractUIPlugin {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ABPlugin.class);

    /**
     * The nature ID MCS projects.
     */
    public static final String NATURE_ID =
            EclipseConstants.NATURE_ID;

    /**
     * The name of the dialog settings file.
     */
    private static final String DIALOG_SETTINGS_NAME = "ABDialogSettings.xml";

    /**
     * The shared instance of ABPlugin (an Eclipse convention).
     */
	private static ABPlugin plugin;

	/**
     * The ResourceBundle associated with this instance of ABPlugin.
     */
	private ResourceBundle resourceBundle;

    /**
     * The DialogSettings for AB dialogs.
     */
    private DialogSettings dialogSettings;

	/**
	 * The constructor.
	 */
	public ABPlugin() {
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle(
                        "com.volantis.mcs.eclipse" + //$NON-NLS-1$
                        ".ab.ABPluginResources"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}


	/**
	 * The constructor.
	 */
	public ABPlugin(IPluginDescriptor descriptor) {
        super(descriptor);
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle(
                        "com.volantis.mcs.eclipse" + //$NON-NLS-1$
                        ".ab.ABPluginResources"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

    /**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static ABPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle= ABPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

    /**
     * Log a plugin specific error to the Workbench log.
     * @param cls The class that originated the error.
     * @param t The Throwable to log.
     */
    public static void logError(Class cls, Throwable t) {
        EclipseCommonPlugin.handleError(plugin, t);
    }

    /**
     * Get the ImageDescriptor for a plugin image.
     * @param imageFileName The file name relative to the plugin directory
     * of the image.
     * @return The ImageDescriptor associated with the specified file.
     */
    public static ImageDescriptor getImageDescriptor(String imageFileName) {
        return EclipseCommonPlugin.getImageDescriptor(ABPlugin.getDefault(),
                imageFileName);
    }

	/**
     * Get the IDialogSettings for a specified section in the IDialogSettings
     * associated with this plugin. If the section does not exist in the
     * IDialogSettings associated with this plugin then it will be created.
     * @param sectionName the name of the section
     * @return the IDialogSettings for the named section
	 */
	public IDialogSettings getDialogSettings(String sectionName) {
        IDialogSettings settings = getDialogSettings();
        IDialogSettings section = settings.getSection(sectionName);
        if(section==null) {
            section = settings.addNewSection(sectionName);
        }

        return section;
	}
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 17-Aug-04	5107/1	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 27-Feb-04	3200/1	allan	VBM:2004022410 Basic Update Client Wizard.

 03-Feb-04	2820/2	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Jan-04	2414/1	byron	VBM:2003121803 Misleading package id in MCSProjectNature

 17-Oct-03	1502/3	allan	VBM:2003092202 Set the selected policy in the Text properly. Improved error handling.

 17-Oct-03	1502/1	allan	VBM:2003092202 Component selection dialog with filtering and error handling

 13-Oct-03	1549/1	allan	VBM:2003101302 Eclipse Common plugin

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 18-Sep-03	1398/1	allan	VBM:2003091001 Build environment for Application Builder Eclipse plugin

 ===========================================================================
*/
