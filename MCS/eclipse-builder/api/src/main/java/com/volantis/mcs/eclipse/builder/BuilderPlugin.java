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
package com.volantis.mcs.eclipse.builder;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.builder.preference.MCSPreferencePage;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The main plugin class to be used in the desktop.
 */
public class BuilderPlugin extends AbstractUIPlugin {

    /**
     * The nature ID MCS projects.
     */
    public static final String NATURE_ID =
            "com.volantis.mcs.eclipse.ab.MCSProjectNature";

    /**
     * The shared instance of BuilderPlugin (an Eclipse convention).
     */
    private static BuilderPlugin plugin;

    /**
     * The ResourceBundle associated with this instance of BuilderPlugin.
     */
    private ResourceBundle resourceBundle;

    /**
     * The constructor.
     */
    public BuilderPlugin() {
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "com.volantis.mcs.eclipse" +
                            ".builder.BuilderPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }


    /**
     * The constructor.
     */
    public BuilderPlugin(IPluginDescriptor descriptor) {
        super(descriptor);
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle(
                    "com.volantis.mcs.eclipse" +
                            ".builder.BuilderPluginResources");
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
    public static BuilderPlugin getDefault() {
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
        ResourceBundle bundle = BuilderPlugin.getDefault().getResourceBundle();
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
     *
     * @param cls The class that originated the error.
     * @param t   The Throwable to log.
     */
    public static void logError(Class cls, Throwable t) {
        EclipseCommonPlugin.handleError(plugin, t);
    }

    /**
     * Get the ImageDescriptor for a plugin image.
     *
     * @param imageFileName The file name relative to the plugin directory
     *                      of the image.
     * @return The ImageDescriptor associated with the specified file.
     */
    public static ImageDescriptor getImageDescriptor(String imageFileName) {
        return EclipseCommonPlugin
                .getImageDescriptor(BuilderPlugin.getDefault(),
                        imageFileName);
    }

    /**
     * Get the IDialogSettings for a specified section in the IDialogSettings
     * associated with this plugin. If the section does not exist in the
     * IDialogSettings associated with this plugin then it will be created.
     *
     * @param sectionName the name of the section
     * @return the IDialogSettings for the named section
     */
    public IDialogSettings getDialogSettings(String sectionName) {
        IDialogSettings settings = getDialogSettings();
        IDialogSettings section = settings.getSection(sectionName);
        if (section == null) {
            section = settings.addNewSection(sectionName);
        }

        return section;
    }

    // javadoc inherited
    protected void initializeDefaultPluginPreferences() {
        final IPreferenceStore preferenceStore = getPreferenceStore();
        preferenceStore.setDefault(
                MCSPreferencePage.PREF_ORACLE_DRIVER_LOCATION, "");
        preferenceStore.setDefault(
                MCSPreferencePage.PREF_FASTER_VALIDATION, true);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

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
