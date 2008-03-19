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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics.osgi.impl.framework.watcher;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Watches a specified directory, polling it at a specified interval to see
 * whether any packages have been installed, removed or changed and performs
 * the appropriate action.
 */
public class Watcher
        implements Runnable {

    /**
     * The context within which the watcher is running.
     */
    private final BundleContext context;

    /**
     * The directory to watch.
     */
    private final File dir;

    /**
     * The poll internal in milliseconds.
     */
    private final int intervalMS;

    /**
     * The URL to the directory as a string.
     */
    private final String dirURL;

    /**
     * Flag to determine whether the watcher is active.
     *
     * <p>If set to false then it means that the watcher is shutting down.</p>
     */
    private boolean active;

    /**
     * A map from location (URL represented as a string) to a Long which
     * represents the time at which the previous action on a bundle failed.
     * This is used to detect when the bundle has changed so the action can
     * be retried. Otherwise the action will keep failing and generating a
     * flood of errors.
     */
    private final Map failedActions;

    /**
     * A flag that is set the first time that the watcher runs to make sure
     * that any bundles that have not changed since the last time that the
     * framework was running are started.
     */
    private boolean startingUp;

    /**
     * Initialise.
     *
     * @param context  The context within which this is running.
     * @param dir      The directory to watch.
     * @param interval The poll interval.
     * @throws IOException If there was a problem converting the file into a
     *                     URL.
     */
    public Watcher(BundleContext context, File dir, int interval)
            throws IOException {

        this.context = context;
        this.intervalMS = interval * 1000;
        this.dir = dir.getCanonicalFile();
        dirURL = this.dir.toURL().toExternalForm();
        active = true;
        failedActions = new HashMap();
    }

    // Javadoc inherited.
    public void run() {
        startingUp = true;
        do {
            refresh();

            synchronized (this) {
                try {
                    wait(intervalMS);
                    if (!active) {
                        return;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        } while (true);
    }

    /**
     * Refresh the set of installed packages if necessary.
     */
    private void refresh() {

        // Get a map from location to bundle of all the bundles that were
        // installed from the watched directory.
        Map installedBundles = getInstalledBundles(context);

        // Get the set of JAR files in the directory being watched.
        File[] bundleFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });

        // If the directory does not exist then the chances are neither does
        // the OSGi install and config directories so do nothing.
        if (bundleFiles == null) {
            return;
        }

        // Canonicalize the paths to the bundles and add them to a set.
        Map watched = new HashMap();
        for (int i = 0; i < bundleFiles.length; i++) {
            File file = bundleFiles[i];
            try {
                File canonicalFile = file.getCanonicalFile();
                watched.put(canonicalFile.toURL().toExternalForm(),
                        canonicalFile);
            } catch (IOException e) {
                IllegalStateException exception = new IllegalStateException(
                        "Cannot get canonical file for '" + file + "'");
                exception.initCause(e);
                exception.printStackTrace(System.err);
            }
        }

        // Create an uber set of all the bundle locations, that includes
        // bundles in the watched directory, not yet installed as well as
        // bundles installed that have been removed from the watched
        // directory.
        Set locations = new HashSet();
        locations.addAll(watched.keySet());
        locations.addAll(installedBundles.keySet());

        // The list of bundles that need removing.
        Collection toUninstall = new HashSet();

        // The list of bundles that have need installed and need starting.
        Collection toInstall = new HashSet();

        // The list of bundles to update.
        Collection toUpdate = new HashSet();

        // The list of bundles to start.
        Collection toStart = new HashSet();

        int changeCount = 0;

        // Iterate over all the bundles.
        for (Iterator i = locations.iterator(); i.hasNext();) {
            String location = (String) i.next();

            // Check to see whether the bundle exists.
            File file = (File) watched.get(location);

            // Check to see whether the bundle has been installed.
            Bundle installed = (Bundle) installedBundles.get(location);


            if (file == null) {
                // The bundle must be installed and needs removing.
                if (installed == null) {
                    throw new IllegalStateException("'" + location +
                            "' is in uber set but not in either watched " +
                            "or installed");
                }

                URL url;
                try {
                    url = new URL(location);
                    File timeStampFile = getTimestampFile(url);

                    toUninstall.add(new UninstallAction(installed,
                            timeStampFile));
                    changeCount += 1;

                } catch (MalformedURLException e) {
                }

            } else {
                File timeStampFile = getTimestampFile(file);

                // Get the last modified date for the file.
                long watchedLastModified = file.lastModified();

                // Check to see whether the previous attempt to install /
                // update the bundle resulted in an error. If it did then see
                // whether the watched file has changed since the error was
                // recorded.
                Long l = (Long) failedActions.get(location);
                boolean previousActionFailed = false;
                if (l != null) {
                    long errorTimeStamp = l.longValue();
                    previousActionFailed = true;
                    if (watchedLastModified > errorTimeStamp) {
                        changeCount += 1;
                    }
                }

                if (installed == null) {

                    if (!previousActionFailed) {
                        changeCount += 1;
                    }

                    toInstall.add(new InstallAction(context,
                            location, watchedLastModified, timeStampFile));

                } else {

                    // The bundle is in the directory and installed so may
                    // need updating.
                    long bundleLastUpdated = timeStampFile.lastModified();

                    // If the bundle in the watched directory has been modified
                    // since the bundle was last updated then update the bundle.
                    if (watchedLastModified > bundleLastUpdated) {

                        if (!previousActionFailed) {
                            changeCount += 1;
                        }

                        toUpdate.add(new UpdateAction(installed,
                                watchedLastModified, timeStampFile));
                    } else if (startingUp) {

                        // If starting up and not updating then make sure that
                        // the bundle is started.
                        toStart.add(new StartAction(installed,
                                watchedLastModified));
                        changeCount += 1;
                    } else if (installed.getState() == Bundle.INSTALLED) {

                        // The bundle is installed but is not active so it must
                        // have failed to start last time so add this to the
                        // list to try and start. This will only happen if
                        // some other change has occurred, otherwise we would
                        // generate a flood of errors.
                        toStart.add(new StartAction(installed,
                                watchedLastModified));
                    }
                }

            }
        }

        // Uninstall bundles first, just in case a new bundle has been added
        // with a different location but the same symbolic name and version.
        for (Iterator i = toUninstall.iterator(); i.hasNext();) {
            Action action = (Action) i.next();
            action.takeAction(failedActions);
        }

        if (changeCount > 0) {

            // Install bundles next, they will be started later.
            for (Iterator i = toInstall.iterator(); i.hasNext();) {
                InstallAction action = (InstallAction) i.next();
                Bundle bundle = action.takeAction(failedActions);
                if (bundle != null) {
                    toStart.add(new StartAction(bundle,
                            action.getWatchedLastModified()));
                }
            }

            // Update existing bundles next.
            for (Iterator i = toUpdate.iterator(); i.hasNext();) {
                UpdateAction action = (UpdateAction) i.next();
                Bundle bundle = action.takeAction(failedActions);
                if (bundle != null) {
                    toStart.add(new StartAction(bundle,
                            action.getWatchedLastModified()));
                }
            }

            // Finally start any remaining bundles.
            for (Iterator i = toStart.iterator(); i.hasNext();) {
                Action action = (Action) i.next();
                Bundle bundle = action.takeAction(failedActions);
                if (bundle != null) {
                    failedActions.remove(action.getLocation());
                }
            }

            // Refresh all the packages when they change.
            ServiceReference reference = context.getServiceReference(
                    PackageAdmin.class.getName());
            if (reference != null) {
                PackageAdmin packageAdmin = (PackageAdmin) context.getService(
                        reference);
                packageAdmin.refreshPackages(null);

                // Get the list of installed bundles again as they may have
                // changed.
                installedBundles = getInstalledBundles(context);
                for (Iterator i = installedBundles.entrySet().iterator();
                     i.hasNext();) {

                    Map.Entry entry = (Map.Entry) i.next();
                    String location = (String) entry.getKey();
                    Bundle bundle = (Bundle) entry.getValue();
                    String message = location + " " +
                            stateAsString(bundle.getState());
                    Reporter.report(message);
                }
            }

            startingUp = false;
        }
    }

    private String stateAsString(int state) {
        switch(state) {
            case Bundle.ACTIVE: return "ACTIVE";
            case Bundle.INSTALLED: return "INSTALLED";
            case Bundle.RESOLVED: return "RESOLVED";
            case Bundle.STARTING: return "STARTING";
            case Bundle.STOPPING: return "STOPPING";
            case Bundle.UNINSTALLED: return "UNINSTALLED";
        }
        return "unknown";
    }

    private File getTimestampFile(File file) {
        File dir = file.getParentFile();
        String name = file.getName();
        return new File(dir, "." + name + ".wts");
    }

    private File getTimestampFile(URL url) {
        File file = new File(url.getPath());
        return getTimestampFile(file);
    }

    /**
     * Get a map of installed bundles from the context.
     * @param context The bundle context within which this is running.
     * @return A map from location (URL as a string) to bundle.
     */
    private Map getInstalledBundles(BundleContext context) {
        Bundle[] bundles = context.getBundles();
        Map map = new TreeMap();
        for (int i = 0; i < bundles.length; i++) {
            Bundle bundle = bundles[i];
            String location = bundle.getLocation();
            if (location.startsWith(dirURL)) {
                map.put(location, bundle);
            }
        }

        return map;
    }

    /**
     * Stops the watcher.
     */
    public void stop() {
        synchronized (this) {
            active = false;
            notifyAll();
        }
    }
}
