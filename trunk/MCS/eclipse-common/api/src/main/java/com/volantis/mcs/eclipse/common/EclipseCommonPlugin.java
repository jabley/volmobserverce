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
package com.volantis.mcs.eclipse.common;

import com.volantis.synergetics.log.DefaultConfigurator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;

import org.apache.log4j.Priority;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class EclipseCommonPlugin extends Plugin {
    /**
     * The id for the Eclipse Common plugin.
     * @todo remove When/if getDefault() works within junit.
     */
    public static final String PLUGIN_ID = "com.volantis.mcs.eclipse.common";

    /**
     * Generic error title. This is hardcoded in English as a fallback
     * in case access to the EclipseCommonMessages bundle fails.
     */
    private static final String FALLBACK_GENERIC_ERROR_TITLE =
            "Internal Error";


    /**
     * Generic error message. This is hardcoded in English as a fallback
     * in case access to the EclipseCommonMessages bundle fails.
     */
    private static final String FALLBACK_GENERIC_ERROR_MESSAGE =
            "An unsupported error has occurred.";

    /**
     * The prefix for the error title message key.
     */
    private static final String ERROR_TITLE_KEY_PREFIX =
            "EclipseCommonPlugin.errorTitle.";

    /**
     * The prefix for the error message message key.
     */
    private static final String ERROR_MESSAGE_KEY_PREFIX =
            "EclipseCommonPlugin.errorMessage.";

    /**
     * The singleton DND clipboard.
     */
    private static Clipboard CLIPBOARD;

    /**
     * The singleton ColorRegistry
     */
    private static ColorRegistry COLOR_REGISTRY;

    /**
     * An appender that can be used with Log4J to ensure that Log4J errors
     * and fatal messages are sent to the Eclipse log.
     */
    private static EclipseLogAppender appender = new EclipseLogAppender();

    /**
     * A reference to the last EclipseCommonPlugin instance. (NOTE: this
     * method of accessing a/the plugin instance copies the convention used
     * by Eclipse plugins such as JDT. I can't find any documentation
     * discussing this approach so I simply assume that it works.)
     */
    private static EclipseCommonPlugin plugin;

    static {
        boolean log = Boolean.getBoolean("volantis.logging");

        // Ensure that we log only error and fatal messages
        appender.setThreshold(Priority.ERROR);

        // Configure Log4J to use this appender and optionally log everything
        // to the console
        DefaultConfigurator.configure(log, appender);
    }

    /**
     * Construct a new EclipseCommonPlugin with the specified descriptor.
     *
     * @param descriptor The plugin descriptor.
     */
    public EclipseCommonPlugin(IPluginDescriptor descriptor) {
        super(descriptor);
        plugin = this;
        appender.setLog(plugin.getLog());
    }

    /**
	 * This method is called upon plug-in activation
	 */
//	public void start(BundleContext context) throws Exception {
//		super.start(context);
//	}

	/**
	 * This method is called when the plug-in is stopped
	 */
//	public void stop(BundleContext context) throws Exception {
        // Perform shutdown specific to this class
//        if (CLIPBOARD != null) {
//           CLIPBOARD.dispose();
//        }
//		super.stop(context);
//	}

    /**
     * Overriding of the shutdown method. Clients must never call this
     * method. This method first performs shutdown specific to this class
     * and then delegates to the superclass.
     */
    public void shutdown() throws CoreException {

        // Perform system-specific shutdown
        super.shutdown();

        if (this == plugin) {
            DefaultConfigurator.shutdown();
        }
    }

    /**
     * Get the default instance of this plugin. (NOTE: this
     * method of accessing a/the plugin instance copies the convention used
     * by Eclipse plugins such as JDT. I can't find any documentation
     * discussing this approach so I simply assume that it works. I did find
     * a reference in a newsgroup from someone at OTI - part of IBM heavilly
     * involved with Eclipse development - who beleives that not being able to
     * get this information is a hole in the architecture:
     * http://dev.eclipse.org/newslists/news.eclipse.platform/msg03508.html)
     * @return The default instance of EclipseCommonPlugin.
     */
    public static EclipseCommonPlugin getDefault() {
        return plugin;
    }

    /**
     * Log a plugin-agnostic error to the Workbench log.
     */
    public static void handleError(Throwable t) {
        // TODO later This is a nasty hack to get things working, logging all errors against eclipse-common.
        handleError(getDefault(), t);
    }

    /**
     * Log a plugin specific error to the Workbench log.
     * @param caller The instance of the plugin that this method is being
     * called from.
     * @param t The Throwable to log.
     * @throws UndeclaredThrowableException throws the given Throwable so as
     * to ensure the flow of execution does not just continue.
     */
    public static void handleError(Plugin caller, Throwable t) {
        try {
        String id = caller.getDescriptor().getUniqueIdentifier();
        Status status = new Status(Status.ERROR, id, Status.ERROR,
                createStringStackTrace(t), t);
        IWorkbenchWindow activeWindow =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow();

        String title = null;
        String message = null;

        Status multi [] = new Status[1];
        multi[0] = status;

        message = t.getLocalizedMessage();
        if(message==null) {
            message = t.getMessage() == null ? "" : t.getMessage(); //$NON-NLS-1$
        }

        MultiStatus multiStatus =
                new MultiStatus(EclipseCommonPlugin.PLUGIN_ID,
                        Status.ERROR, multi, message, t);

        try {
            title = getErrorTitle(t);
        } catch (Throwable anotherT) {
            multiStatus.add(new Status(Status.ERROR,
                    EclipseCommonPlugin.PLUGIN_ID, Status.ERROR,
                    createStringStackTrace(anotherT), anotherT));
            title = FALLBACK_GENERIC_ERROR_TITLE;
        }

        try {
            message = getErrorMessage(t);
        } catch (Throwable anotherT) {
            multiStatus.add(new Status(Status.ERROR,
                    EclipseCommonPlugin.PLUGIN_ID, Status.ERROR,
                    createStringStackTrace(anotherT), anotherT));
            message = FALLBACK_GENERIC_ERROR_MESSAGE;
        }

        Shell parentShell = activeWindow.getShell();


        ErrorDialog.openError(parentShell, title, message,
                multiStatus, Status.ERROR);
        } catch (Throwable t2) {
            // Failsafe to ensure we get the original failure even if there
            // is an exception in this method.
            System.err.println("Error Occurred within error handler:"); //$NON-NLS-1$
            t2.printStackTrace();

            System.err.println("Original failure:"); //$NON-NLS-1$
            t.printStackTrace();

        } finally {
            throw new UndeclaredThrowableException(t);
        }
    }

    /**
     * Log a plugin specific error to the Workbench log.
     *
     * @param caller The instance of the plugin that this method is being
     *               called from.
     * @param cls    The class that originated the error.
     * @param t      The Throwable to log.
     */
    public static void logError(Plugin caller, Class cls, Throwable t) {
        String className = cls.getName();
        String id = caller.getBundle().getSymbolicName();
        Status status = new Status(Status.ERROR, id, Status.ERROR,
                "Class " + className, t);
        caller.getLog().log(status);
    }

    /**
     * Convert the stack trace from a Throwable into a String and return
     * the String.
     * @param t the Throwable
     */
    public static String createStringStackTrace(Throwable t) {
        StringWriter sWriter = new StringWriter();
        PrintWriter pWriter = new PrintWriter(sWriter);
        t.printStackTrace(pWriter);

        return sWriter.toString();
    }

    /**
     * Provide the title for an error message dialog using.
     * @param t The throwable that describes the problem. This could be
     * used for providing a more context specific title.
     * @return The title for an error message dialog.
     * @todo later support context specific titles
     */
    private static String getErrorTitle(Throwable t)
            throws MissingResourceException {
        return EclipseCommonMessages.RESOURCE_BUNDLE.
                getString(ERROR_TITLE_KEY_PREFIX + "genericError"); //$NON-NLS-1$
    }


    /**
     * Provide the message for an error message dialog using.
     * @param t The throwable that describes the problem. This could be
     * used for providing a more context specific message.
     * @return The title for an error message dialog.
     * @todo later support context specific messages
     */
    private static String getErrorMessage(Throwable t)
            throws MissingResourceException {
        return EclipseCommonMessages.RESOURCE_BUNDLE.
                getString(ERROR_MESSAGE_KEY_PREFIX + "genericError"); //$NON-NLS-1$
    }

    /**
     * Return a singleton Clipboard for use by all plugins.
     * @param display The Display object which will be used to create the
     * singleton Clipboard. The design ignores the possibility of multiple
     * Display objects within a plug-in, and the singleton Clipboard is created
     * with respect to the first Display object passed in; the same singleton
     * is always returned regardless of the value of Display objects passed in.
     * This is justified because it is expected that there will only be one
     * Display object, and all Clipboard objects probably delegate to a
     * single master system clipboard anyway. The dispose disposition of the
     * Display object parameter is not tested in the code; this is a caller
     * responsibility.
     * @return The singleton Clipboard.
     */
    public static synchronized Clipboard getClipboard (Display display) {
        if (CLIPBOARD == null) {
            CLIPBOARD = new Clipboard(display);
        }
        return CLIPBOARD;
    }

    /**
     * Return a singleton {@link ColorRegistry} for use by all plugins.
     * @return The singleton {@link ColorRegistry}.
     */
    public static synchronized ColorRegistry getColorRegistry() {
        if (COLOR_REGISTRY == null) {
            // This design ignores the possibility of multiple Display objects
            // within a plug-in.  The singleton is created with the current
            // Display object.
            COLOR_REGISTRY = new ColorRegistry();
        }
        return COLOR_REGISTRY;
    }

    /**
     * Get the ImageDescriptor for a plugin image.
     * @param plugin The plugin whose descriptor will be used to establish
     * the URL to the image file.
     * @param imageFileName The file name relative to the plugin directory
     * of the image.
     * @return The ImageDescriptor associated with the specified file.
     */
    public static ImageDescriptor getImageDescriptor(Plugin plugin,
                                                     String imageFileName) {
        ImageDescriptor id = null;
        try {
            URL installURL = plugin.getDescriptor().getInstallURL();
            URL imgURL = new URL(installURL, imageFileName);
            id = ImageDescriptor.createFromURL(imgURL);
        } catch (MalformedURLException e) {
            handleError(plugin, e);
        }

        return id;
    }

    /**
     * Check that a given device repository is valid
     * @param deviceRepositoryName the name of the repository
     * @return true if the repository is valid or false otherwise
     */
    public static boolean checkDeviceRepositoryValid(
            String deviceRepositoryName) {
        boolean result = false;
        if (deviceRepositoryName != null) {
            /*&& new File(deviceRepositoryName).exists()*/
            result = true;
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10539/1	adrianj	VBM:2005111712 Added 'New' button for device themes

 01-Dec-05	10520/1	adrianj	VBM:2005111712 Implement New... button for device themes

 26-Apr-05	7759/4	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	7665/1	pcameron	VBM:2005040505 Logging initialisation changed

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Nov-04	6272/1	philws	VBM:2004111803 Output error and fatal Log4J messages to the Eclipse Log

 08-Oct-04	5557/1	allan	VBM:2004070608 Device search

 28-Sep-04	5663/1	tom	VBM:2004081003 Replaced ColorRegistry with Eclipse V3.0.0 Version

 08-Sep-04	5449/1	claire	VBM:2004090809 New Build Mechanism: Remove the use of utilities.UndeclaredThrowableException

 17-Aug-04	5107/2	allan	VBM:2004080408 Basic port to use Eclipse v3.0.0

 03-Aug-04	4902/3	allan	VBM:2004071504 Rewrite layout designer and provide it with a context menu.

 22-Jul-04	4905/2	adrian	VBM:2004071507 Refactored Color support in layout formats

 18-May-04	4231/4	tom	VBM:2004042704 rework for 2004042704

 05-May-04	4115/1	allan	VBM:2004042907 Multiple root elements in ODOMEditorContext.

 15-Apr-04	3881/1	steve	VBM:2004032606 Allow assignment of MCS nature to imported projects

 27-Feb-04	3200/3	allan	VBM:2004022410 UpdateClient https to update.volantis.set

 27-Feb-04	3200/1	allan	VBM:2004022410 Basic Update Client Wizard.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 12-Dec-03	2123/2	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 16-Nov-03	1909/1	allan	VBM:2003102405 Done Image Component Wizard.

 12-Nov-03	1855/1	richardc	VBM:2003110608 2003110608

 17-Oct-03	1502/3	allan	VBM:2003092202 Set the selected policy in the Text properly. Improved error handling.

 17-Oct-03	1502/1	allan	VBM:2003092202 Component selection dialog with filtering and error handling

 13-Oct-03	1549/1	allan	VBM:2003101302 Eclipse Common plugin

 ===========================================================================
*/
