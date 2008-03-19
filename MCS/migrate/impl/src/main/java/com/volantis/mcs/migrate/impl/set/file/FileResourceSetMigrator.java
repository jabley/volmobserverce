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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.migrate.impl.set.file;

import com.volantis.mcs.migrate.api.framework.FrameworkFactory;
import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.OutputCreator;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.notification.Notification;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.migrate.set.ResourceSetMigrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * An implementation of {@link com.volantis.mcs.migrate.set.ResourceSetMigrator} that migrates between
 * files in a single standard filesystem, where the structure and naming of
 * the target migrated directory is the same as that of the original structure.
 */
public class FileResourceSetMigrator implements ResourceSetMigrator {

    NotificationFactory notificationFactory =
            NotificationFactory.getDefaultInstance();

    FrameworkFactory frameworkFactory = FrameworkFactory.getDefaultInstance();

    /**
     * The top-level directory or file to migrate30.
     */
    private File originalInput;

    /**
     * The top-level directory or file in the migration target.
     */
    private File originalOutput;

    /**
     * The notification reporter to use to log any notifications.
     */
    private NotificationReporter reporter;

    /**
     * Constructs a file-based resource set migrator representing a specified
     * input and output location.
     *
     * @param input The input file or directory to migrate30
     * @param output The location to which migration should take place
     * @param reporter The notification reporter to use for providing
     *                 information to the user
     */
    public FileResourceSetMigrator(File input, File output,
                                   NotificationReporter reporter) {
        originalInput = input;
        originalOutput = output;
        this.reporter = reporter;
    }

    /**
     * Carry out the migration process specified in the provided resource
     * migrator across the resources represented by this resource set migrator.
     *
     * @param migrator The migrator to use to migrate30 individual resources
     */
    public void migrate(ResourceMigrator migrator) {
        try {
            migrateImpl(originalInput, originalOutput,
                    originalInput.getName(), migrator);
        } catch (IOException ioe) {
            reporter.reportNotification(
                    notificationFactory.createThrowableNotification(
                            NotificationType.ERROR, ioe));
        }
    }

    /**
     * Recurse through a directory structure, migrating files and copying and
     * recursing through directories.
     *
     * @param inputFile The file/directory to migrate30
     * @param outputFile The corresponding output file
     * @param path A string representation of the path to this resource,
     *             relative to the original resource to be migrated
     * @param migrator The resource migrator to use to migrate30 files
     * @throws java.io.IOException if an unexpected I/O error occurs
     */
    private void migrateImpl(File inputFile, File outputFile, String path,
                              ResourceMigrator migrator) throws IOException {
        if (inputFile.exists()) {
            if (inputFile.isDirectory()) {
                if (outputFile.mkdir()) {
                    String[] children = inputFile.list();
                    for (int i = 0; i < children.length; i++) {
                        String fileName = children[i];
                        File inputChild = new File(inputFile, fileName);
                        File outputChild = new File(outputFile, fileName);
                        migrateImpl(inputChild, outputChild,
                                path + "/" + fileName, migrator);
                    }
                } else {
                    Notification note = notificationFactory.
                            createLocalizedNotification(
                                    NotificationType.ERROR, "cannot-create-dir",
                                    outputFile.getAbsolutePath());
                    reporter.reportNotification(note);
                }
            } else {
                InputStream in = new FileInputStream(inputFile);
                OutputCreator out = new FileOutputCreator(outputFile);
                try {
                    InputMetadata inputMeta =
                            frameworkFactory.createInputMetadata(path, true);
                    migrator.migrate(inputMeta, in, out);
                } catch (ResourceMigrationException me) {
                    reporter.reportNotification(
                            notificationFactory.createThrowableNotification(
                                    NotificationType.ERROR, me));
                }
            }
        } else {
            Notification note = notificationFactory.createLocalizedNotification(
                    NotificationType.ERROR, "file-missing",
                    inputFile.getAbsolutePath());
            reporter.reportNotification(note);
        }
    }

    // Javadoc inherited
    public String toString() {
        return "[File-based resource set migrator from '" + originalInput +
                "' to '" + originalOutput + "']";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 18-May-05	8181/4	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
