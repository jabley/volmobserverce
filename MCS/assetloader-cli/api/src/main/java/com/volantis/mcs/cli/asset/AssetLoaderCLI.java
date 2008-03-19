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
package com.volantis.mcs.cli.asset;

import com.volantis.mcs.devices.DeviceRepository;
import com.volantis.mcs.devices.DeviceRepositoryFactory;
import com.volantis.mcs.devices.DeviceRepositoryException;
import com.volantis.mcs.devices.xml.XMLDeviceRepositoryConfiguration;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.project.ProjectConfiguration;
import com.volantis.mcs.project.ProjectFactory;
import com.volantis.mcs.repository.LocalRepository;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.repository.xml.XMLRepositoryFactory;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * A command line tool for invoking {@link AssetLoader}.
 * <p>
 * As per A898, this takes the following arguments:
 * <ul>
 * <li>-srcdir (required) - Specifies the root of the directory structure
 *      (defined in A898).
 * <li>-repository (required) - Specifies the root of the XML Repository in
 *      which to import the images.
 * <li>-devicerepository (required) - Specifies the mdpr file containing device
 *      information.
 * <li>-assetgroup (optional) - Specifies the asset group to assign to all
 *      image assets.
 * <li>-folder (optional) - Specifies the folder name to pre-append to the
 *      components.
 * <li>-replace (optional) - If specified will cause the target component and
 *      all of its assets to be deleted before being reimported.
 * <li>-widthhint (optional) - If specified defines the widthhint attribute for
 *      a generic image asset. The default if not specified is 100.
 * </ul>
 * It also takes the following additional arguments:
 * <ul>
 * <li>-verbose (optional) - Sets the logging level to debug so all logging
 *      information is output.
 * </ul>
 */
public class AssetLoaderCLI {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AssetLoaderCLI.class);

    /**
     * The source directory CLI option.
     */
    private static final String SOURCE_DIRECTORY = "srcdir";

    /**
     * The repository CLI option.
     */
    private static final String REPOSITORY = "repository";

    /**
     * The device repository CLI option.
     */
    private static final String DEVICE_REPOSITORY = "devicerepository";

    /**
     * The asset group CLI option.
     */
    private static final String ASSET_GROUP = "assetgroup";

    /**
     * The folder CLI option.
     */
    private static final String FOLDER = "folder";

    /**
     * The replace CLI option.
     */
    private static final String REPLACE = "replace";

    /**
     * The widthhint CLI option.
     */
    private static final String WIDTH_HINT = "widthhint";

    /**
     * The verbose CLI option.
     */
    private static final String VERBOSE = "verbose";

    /**
     * The "command line" for the command to be output in the usage.
     */
    private static final String COMMAND_LINE = "AssetLoaderCLI";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final MessageLocalizer messageLocalizer =
            LocalizationFactory.createMessageLocalizer(AssetLoaderCLI.class);

    /**
     * Process the CLI arguments and then perform the loading of assets based
     * on those arguments.
     *
     * @param args the arguments provided by the user on the command line.
     */
    public void load(String[] args) {

        Options options = createOptions();
        CommandLineParser parser = new BasicParser();
        try {
            boolean optionValuesValid = true;
            CommandLine cmd = parser.parse(options, args);

            // Check verbose first since that affects everything else.
            Level level = Level.INFO;
            if (cmd.hasOption(VERBOSE)) {
                level = Level.DEBUG;
            }
            Category.getRoot().setLevel(level);

            // First, extract the mandatory arguments and translate them
            // into the objects that they correspond to.

            printValue(cmd, SOURCE_DIRECTORY);
            String srcDirString = cmd.getOptionValue(SOURCE_DIRECTORY);
            File srcDir = createSourceDirectory(srcDirString);

            printValue(cmd, REPOSITORY);
            String repositoryString = cmd.getOptionValue(REPOSITORY);
            LocalRepository destinationRepository =
                    createDestinationRepository();

            Project project = createProject(destinationRepository,
                    repositoryString);

            printValue(cmd, DEVICE_REPOSITORY);
            String deviceRepositoryString = cmd.getOptionValue(DEVICE_REPOSITORY);
            DeviceRepository deviceRepository = createDeviceRepository(
                    deviceRepositoryString);

            // Now create the asset loader since we have the minimum
            // requirements.

            AssetLoader loader = new AssetLoader(srcDir,
                    deviceRepository, project);

            // Add in any otional arguments.

            printValue(cmd, ASSET_GROUP);
            if (cmd.hasOption(ASSET_GROUP)) {
                String assetGroupString = cmd.getOptionValue(ASSET_GROUP);
                loader.setAssetGroupName(assetGroupString);
            }

            printValue(cmd, FOLDER);
            // Default the folder to /.
            String folderString = "/";
            if (cmd.hasOption(FOLDER)) {
                folderString = cmd.getOptionValue(FOLDER);
                if (!(folderString.endsWith("/") ||
                        folderString.endsWith(File.separator))) {
                    folderString = folderString + "/";
                }
                if (folderString.charAt(0) != '/') {
                    System.err.println(
                            messageLocalizer.format("invalid-xdime-name",
                                    "-folder: " + folderString));
                    optionValuesValid = false;
                }
            }

            if (optionValuesValid) {
                // Always set the folder, either to the default / or to the
                // user-supplied value from the command line.
                loader.setComponentNamePrefix(folderString);

                printValue(cmd, REPLACE);
                if (cmd.hasOption(REPLACE)) {
                    loader.setReplace(true);
                }

                printValue(cmd, WIDTH_HINT);
                if (cmd.hasOption(WIDTH_HINT)) {
                    String widthHintString = cmd.getOptionValue(WIDTH_HINT);
                    int widthHintInt = Integer.parseInt(widthHintString);
                    loader.setWidthHint(widthHintInt);
                }

                // OK, we are finished parsing the arguments.
                // So kick off the loading!

                loader.load();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(COMMAND_LINE, options);
        } catch (RepositoryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the commons-cli Options object which defines the command line
     * options that can be processed.
     *
     * @return the Options object created.
     */
    private Options createOptions() {

        /*
          -srcdir (required) -
              Specifies the root of the directory structure defined above.
          -repository (required) -
              Specifies the root of the XML Repository in which to import the
              images.
          -devicerepository (required) -
              Specifies the mdpr file containing device information.
          -assetgroup (optional) -
              Specifies the asset group to assign to all image assets.
          -folder (optional) -
              Specifies the folder name to pre-append to the components.
          -replace (optional) -
              If specified will cause the target component and all of its
              assets to be deleted before being reimported.
          -widthhint (optional) -
              If specified defines the 'widthhint' attribute for a generic
              image asset. The default if not specified is 100.
         */

        Option srcDirSpec = OptionBuilder.isRequired().hasArg().
                withArgName("source directory").
                withDescription("Specifies the root of the directory " +
                "structure.").
                create(SOURCE_DIRECTORY);
        Option repositorySpec = OptionBuilder.isRequired().hasArg().
                withArgName(REPOSITORY).
                withDescription("Specifies the root of " +
                "the XML Repository in which to import the images.").
                create(REPOSITORY);
        Option deviceRepositorySpec = OptionBuilder.isRequired().hasArg().
                withArgName("device repository").
                withDescription("Specifies the mdpr file " +
                "containing device information.").
                create(DEVICE_REPOSITORY);
        Option assetGroupSpec = OptionBuilder.hasArg().
                withArgName("asset group").
                withDescription("(optional) Specifies the asset group to assign " +
                "to all image assets.").
                create(ASSET_GROUP);
        Option folderSpec = OptionBuilder.hasArg().
                withArgName(FOLDER).
                withDescription("(optional) Specifies the folder name to " +
                "pre-append to the components.").
                create(FOLDER);
        Option replaceSpec = OptionBuilder.
                withDescription("(optional) If specified will cause the target " +
                "component and all of its assets to be deleted before being " +
                "reimported.").
                create(REPLACE);
        Option widthHintSpec = OptionBuilder.hasArg().
                withArgName("width hint").
                withDescription("(optional) If specified defines the 'widthhint' " +
                "attribute for a generic image asset. The default if not " +
                "specified is 100.").
                create("widthhint");
        Option verboseSpec = OptionBuilder.
                withDescription("(optional) If specified will verbose logging " +
                "to be output.").
                create("verbose");

        Options options = new Options();
        // Architected options.
        options.addOption(srcDirSpec);
        options.addOption(repositorySpec);
        options.addOption(deviceRepositorySpec);
        options.addOption(assetGroupSpec);
        options.addOption(folderSpec);
        options.addOption(replaceSpec);
        options.addOption(widthHintSpec);
        // Other options.
        options.addOption(verboseSpec);
        return options;
    }

    /**
     * Creates a File object which represents the source directory.
     *
     * @param srcDirName the name of the source directory.
     * @return the directory corresponding to the name provided, if it exists.
     * @throws IOException if the file did not exist or was not a directory.
     */
    private File createSourceDirectory(String srcDirName)
            throws IOException {

        File srcDir = new File(srcDirName);
        if (!srcDir.exists() || !srcDir.isDirectory()) {
            throw new IOException("Source directory invalid: " + srcDirName);
        }
        return srcDir;
    }

    /**
     * Create a (n XML) repository to act as the destination for the components
     * and assets created by the asset loader.
     *
     * @return the created repository.
     * @throws RepositoryException if there was a problem creating the
     *      repository.
     */
    private LocalRepository createDestinationRepository()
            throws RepositoryException {

        XMLRepositoryFactory factory =
                XMLRepositoryFactory.getDefaultInstance();

        return factory.createXMLRepository(null);
    }

    /**
     * Create a project in the XML repository into which the policies will be
     * added.
     *
     * @param destinationRepository
     * @param policyRoot the root of the XML policies.
     * @return the created project.
     */
    private Project createProject(
            LocalRepository destinationRepository, String policyRoot) {

        ProjectFactory factory = ProjectFactory.getDefaultInstance();
        ProjectConfiguration configuration = factory.createProjectConfiguration();
        configuration.setRepository(destinationRepository);
        configuration.setPolicyLocation(new File(policyRoot).getAbsolutePath());

        return factory.createProject(configuration);
    }

    /**
     * Create a device repository to be used to validate the device directories
     * in the input directory structure.
     *
     * @param deviceRepositoryFileName the name of the device repository file.
     * @return the created device repository.
     * @throws RepositoryException of tjere was a problem creating the device
     *      repository.
     */
    private DeviceRepository createDeviceRepository(
            String deviceRepositoryFileName) throws RepositoryException {

        // Create a device repository using the accessor and repository.
        // This is a bit easier to work with than the accessor.
        final DeviceRepositoryFactory repositoryFactory =
            DeviceRepositoryFactory.getDefaultInstance();
        final XMLDeviceRepositoryConfiguration configuration =
            repositoryFactory.createXMLDeviceRepositoryConfiguration();
        try {
            configuration.setRepositoryURL(
                new File(deviceRepositoryFileName).toURL());
            final DeviceRepository deviceRepository =
                repositoryFactory.createDeviceRepository(configuration);
            return deviceRepository;
        } catch (MalformedURLException e) {
            throw new RepositoryException(e);
        } catch (DeviceRepositoryException e) {
            throw new RepositoryException(e);
        }
    }

    /**
     * Print out the value of a command option to the debug log.
     *
     * @param cmd the processed command line.
     * @param name the name of the option.
     */
    private void printValue(CommandLine cmd, String name) {
        if (logger.isDebugEnabled()) {
            if (cmd.hasOption(name)) {
                logger.debug("The " + name + " option was found with value: "
                        + cmd.getOptionValue(name));
            } else {
                logger.debug("The " + name + " option was not found");
            }
        }
    }

    /**
     * The entry point for this CLI application.
     *
     * @param args the user arguments.
     */
    public static void main(String[] args) {
        BasicConfigurator.configure();

        AssetLoaderCLI cli = new AssetLoaderCLI();
        cli.load(args);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Nov-05	10404/1	geoff	VBM:2005112301 Implement meta data for JiBX device repository accessor

 13-Nov-05	9896/1	geoff	VBM:2005101906 Avoid using JDOM in MCS at runtime: rewrite runtime XML device repository

 27-May-05	8542/1	pcameron	VBM:2005051701 Folder option's value defaults to / and must begin with /

 11-Mar-05	6842/1	emma	VBM:2005020302 Making file references in config files relative to those files

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 16-Aug-04	5177/1	geoff	VBM:2004081014 Provide a bulk image loading CLI

 ===========================================================================
*/
