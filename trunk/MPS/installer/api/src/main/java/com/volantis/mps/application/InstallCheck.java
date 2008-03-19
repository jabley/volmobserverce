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
 * $Header: /src/mps/installer/InstallCheck.java,v 1.5 2002/12/02 10:05:13 buildusr Exp $
 *
 * (c) Volantis Systems Ltd 2000. 
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02/12/02     Mike Jones      Added
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.application;

import java.io.*;
import java.lang.*;

/**
 * This class provides a means of checking the MCS version required when
 * installing MPS.  This code is executed from the installer, for example
 * InstallAnywhere.
 */
public class InstallCheck {

    /**
     * The code entry point.  No command line parameters are required, and
     * any supplied will be ignored.
     *
     * @param args Command line arguments.  Not required.
     *
     * @throws IOException If there is a problem handling any of the files
     *                     used during the execution of the version checking
     */
    public static void main(String args[]) throws IOException {

        System.out.println( "InstallCheck started..." );

        // The files used during the installation
        String installCheckOKFile = "install-check-ok.txt";
        String installCheckFailFile = "install-check.txt";

        // Ensure the files do not exist
        removeFile(installCheckOKFile);
        removeFile(installCheckFailFile);

        System.out.println( "Now Checking MCS and MPS version numbers..." );

        String installCheckResultFile = installCheckFailFile;
        String installMessage = null;
        String checkVersionResult = null;

        boolean validVersion = false;

        try {
            // Grab the version information from MPS
            validVersion = MPSVersion.isMCSValidVersion();
            checkVersionResult = MPSVersion.checkVersionMessage();
        } catch (NoClassDefFoundError ex) {
            // Ooops, the MCS jar(s) cannot be found
            installMessage = "The install check has failed!\n\n" +
                    "Could not find MCS common JAR.\n\n" + ex;
        } finally {
            // Having determined whether there is a valid version or not
            // communicate this information appropriately
            if (validVersion) {
                installCheckResultFile = installCheckOKFile;
                installMessage = "Your install has passed the install check.";
            } else {
                if (installMessage == null) {
                    installMessage = "The install check has failed.";
                }
            }

            // Output the resultant message and version
            System.out.println(installMessage);
            System.out.println(checkVersionResult);

            // And generate the appropriate file for use with the installer
            // (e.g. InstallAnywhere)
            writeFile(installCheckResultFile,
                      installMessage + "\n\n" + checkVersionResult);
        }
    }

    /**
     * Removes the file specified from the file system.  No auxilliary paths
     * are used in locating the file so all necessary path information must
     * be provided to this method.  If there is a problem in deleting a file
     * that exists (e.g. Unix permissions are wrong) then an
     * <code>IllegalStateException</code> will be thrown.  If a file does not
     * exist the code will not cause any exceptions.
     *
     * @param filePath The file to delete, including all necessary path
     *                 information.
     */
    private static void removeFile(String filePath) {
        System.out.println("Checking if " + filePath + " exists...");
        File removableFile = new File(filePath);
        boolean removableFileExists = removableFile.exists();

        // The file exists so try to remove it
        if (removableFileExists) {
            System.out.println("Removing existing " + filePath);
            boolean removableFileDeleted = removableFile.delete();
            if (!removableFileDeleted) {
                // Unable to remove the file so report this problem
                System.out.println("Problem encountered deleting " +
                                   filePath);
                throw new IllegalStateException("Exception deleting file " +
                                                filePath);
            }
        } else {
            System.out.println( "File " + filePath + " does not exist");
        }
    }

    /**
     * Given a filename (including the path where necessary) and some text,
     * create that file (if necessary) and write the provided text to that
     * file.
     *
     * @param filePath  The file to write the text to.  Will be created if
     *                  it does not exist, or overwritten if it exists.
     *                  Should have all the necessary path information in
     *                  the string
     * @param writeText The text to output to the file
     *
     * @throws IOException If there is a problem in creating, accessing, or
     *                     writing to the file specified.
     */
    private static void writeFile(String filePath,
                                  String writeText) throws IOException {
        File logFile = new File(filePath);
        // create file if it doesn't exist
        boolean createLogFile = logFile.createNewFile();
        if (createLogFile) {
            System.out.println("File " + filePath + " created");
        }

        // output the text to the file
        FileWriter writeLogFile = new FileWriter(logFile);
        writeLogFile.write(writeText);
        writeLogFile.close();

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Sep-04	182/4	claire	VBM:2004070709 MPS Installer not building

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
