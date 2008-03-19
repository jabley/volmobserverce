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

package com.volantis.mcs.cli;

import com.volantis.mcs.prof.migrate30.cli.MarinerMigrate;
import com.volantis.mcs.migrate.api.config.ConfigFactory;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.ResourceMigrator;
import com.volantis.mcs.migrate.api.notification.NotificationReporter;
import com.volantis.mcs.migrate.api.notification.NotificationType;
import com.volantis.mcs.migrate.notification.NotificationFactory;
import com.volantis.mcs.migrate.set.ResourceSetMigrator;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Application class for CLI migration.
 */
public class Migrate {

    /**
     * The properties bundle for the UpdateClientCLI.
     */
    private static final String BUNDLE_NAME =
        "com.volantis.mcs.cli.MigrateMessages";

    /**
     * The ResourceBundle for properties associated with this class
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
        ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * The input argument identifier.
     */
    private static final String INPUT_ARG = "input";

    /**
     * The output argument identifier.
     */
    private static final String OUTPUT_ARG = "output";

    /**
     * The verbose identifier.
     */
    private static final String VERBOSE_ARG = "verbose";

    /**
     * The input29 argument identifier.
     */
    private static final String INPUT_29_ARG = "input29";


    private NotificationFactory notificationFactory =
        NotificationFactory.getDefaultInstance();

    private ConfigFactory configFactory = ConfigFactory.getDefaultInstance();

    private com.volantis.mcs.migrate.set.ResourceSetFactory resourceSetFactory =
        com.volantis.mcs.migrate.set.ResourceSetFactory.getDefaultInstance();

    /**
     * The command-line options.
     */
    private Options globalOptions;

    /**
     * Create the commons-cli Options object which defines the command line
     * options that can be processed.
     *
     * @return the Options object created.
     */
    private Options createOptions() {
        /*
          -input (required) -
              The input source for the migration process (a file or directory).
          -output (required) -
              The location to which the input should be migrated.
         */

        Option inputSpec = OptionBuilder.isRequired().hasArg().
            withArgName(getOptionArgumentName(INPUT_ARG)).
            withDescription(getOptionDescription(INPUT_ARG)).
            create(INPUT_ARG);
        Option outputSpec = OptionBuilder.isRequired().hasArg().
            withArgName(getOptionArgumentName(OUTPUT_ARG)).
            withDescription(getOptionDescription(OUTPUT_ARG)).
            create(OUTPUT_ARG);
        Option verboseErrorsSpec = OptionBuilder.
            withArgName(getOptionArgumentName(VERBOSE_ARG)).
            withDescription(getOptionDescription(VERBOSE_ARG)).
            create(VERBOSE_ARG);
        Option input29Spec = OptionBuilder.hasOptionalArg().
            withArgName(getOptionArgumentName(INPUT_29_ARG)).
            withDescription(getOptionDescription(INPUT_29_ARG)).
            create(INPUT_29_ARG);

        globalOptions = new Options();
        globalOptions.addOption(inputSpec);
        globalOptions.addOption(outputSpec);
        globalOptions.addOption(verboseErrorsSpec);
        globalOptions.addOption(input29Spec);
        return globalOptions;
    }

    /**
     * Retrieves the string for an options description from the CLI bundle.
     *
     * @param option the name of the option
     * @return the description
     */
    private static String getOptionDescription(String option) {
        String propertyName = "MigrateCLI." + option + ".description";
        return RESOURCE_BUNDLE.getString(propertyName);
    }

    /**
     * Retrieves the string for an options argument from the CLI bundle.
     *
     * @param option the name of the
     * @return the description
     */
    private static String getOptionArgumentName(String option) {
        String propertyName = "MigrateCLI." + option + ".argumentName";
        return RESOURCE_BUNDLE.getString(propertyName);
    }

    /**
     * Main entry point for the migration CLI tool.
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {

        // Setup log4j logging, sending output to stdout
        BasicConfigurator.configure(
            new ConsoleAppender(new PatternLayout()));

        Migrate mig = new Migrate();
        try {
            mig.migrate(args);
        } catch (ResourceMigrationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Migrates one resource (file or directory) to another, based on the
     * command line arguments.
     *
     * @param args The command line arguments
     */
    public void migrate(String[] args) throws ResourceMigrationException {
        NotificationReporter reporter =
            notificationFactory.createCLIReporter();
        try {
            // parse the global options;
            CommandLineParser parser = new BasicParser();
            Options options = createOptions();
            CommandLine commandLine = parser.parse(options, args, true);

            configureLogging(commandLine);

            final String input = commandLine.getOptionValue(INPUT_ARG);

            // Perform "special" migration from 2.9 to 3.0.
            //
            // NOTE: This facility was added especially for Three and is *not*
            // recommended for general use.
            //
            // If the user specified a base file for a 2.9 multi file
            // repository,
            if (commandLine.hasOption(INPUT_29_ARG)) {

                final String input29 = commandLine.getOptionValue(INPUT_29_ARG);
                migrate29to30(input29, input, commandLine, reporter);
            }

            // Perform "normal" migration from 3.0 to latest.
            final String output = commandLine.getOptionValue(OUTPUT_ARG);
            // Do the migration.
            migrate30toLatest(input, output, reporter);

        } catch (MissingOptionException e) {
            // a required option was missing
            printUsage();
        } catch (MissingArgumentException e) {
            // an options argument was missing
            printUsage();
        } catch (UnrecognizedOptionException e) {
            // an illegal options was encountered
            printUsage();
        } catch (ParseException e) {
            // catch all
            printUsage();
        }
    }

    /**
     * Migrate a repository from 2.9 to 3.0.
     * <p>
     * NOTE: This was required for a single customer, Three, so we have
     * implemented it in the simplest possible way. They are one of the last
     * customers using 2.9 so there is little point making this more clever or
     * general than they need.
     * <p>
     * This is not recommended from general use anyway because when migrating
     * from 2.9 to 3.0 we made the schema much more restrictive so that you
     * typically get a lot of errors which need to be fixed. Doing the
     * migration in stages makes it easier to understand and deal with those
     * errors.  
     *
     * @param inputBaseFile The base file of a 2.9 multi file repository.
     * @param outputDirectory The directory which will contain the 3.0
     *      repository created. This must not exist.
     * @param commandLine
     * @param reporter
     */
    private void migrate29to30(String inputBaseFile, String outputDirectory,
            CommandLine commandLine, NotificationReporter reporter) {

        // Report that we are doing the special 2.9 to 3.0 migration.
        reporter.reportNotification(
                notificationFactory.createLocalizedNotification(
                        NotificationType.INFO, "migrate-29-to-30"));

        // Create the argument list for the 2.9 migrator.
        ArrayList argumentList = new ArrayList();
        argumentList.add("-all");
        argumentList.add("-srcfile");
        argumentList.add(inputBaseFile);
        argumentList.add("-destdir");
        argumentList.add(outputDirectory);
        if (commandLine.hasOption(VERBOSE_ARG)) {
            argumentList.add("-verbose");
        }

        // Run the 2.9 migrator with the arguments we created above.
        final String[] argumentArray = (String[]) argumentList.toArray(
                new String[argumentList.size()]);
        MarinerMigrate.main(argumentArray);
    }

    /**
     * Given input and output paths, recursively migrate30 the contents of those
     * paths.
     *
     * @param inputPath The input to migrate30 from
     * @param outputPath The location of the migrated file
     * @param reporter
     */
    private void migrate30toLatest(String inputPath, String outputPath,
        NotificationReporter reporter)
        throws ResourceMigrationException {

        // Report that we are doing the migration from 3.0 to latest.
        reporter.reportNotification(
                notificationFactory.createLocalizedNotification(
                        NotificationType.INFO, "migrate-30-to-latest"));

        File inputDir = new File(inputPath);
        File outputDir = new File(outputPath);

        ResourceMigrator migrator =
            configFactory.createDefaultResourceMigrator(reporter, false);
        ResourceSetMigrator rsm =
            resourceSetFactory.createFileResourceSetMigrator(inputDir,
                outputDir, reporter);
        rsm.migrate(migrator);
    }

    /**
     * Configure the Log4j logging system. The default logging level is
     * "info" which will result in informational and error messages being
     * generated. If the CommandLine object contains the option "verbose"
     * then the logging level is raised to "all". This will result in all
     * logging messages being generated; info, error and debug.
     *
     * @param globalCommandLine contains user specified parameters
     */
    private void configureLogging(CommandLine globalCommandLine) {
        Level loggingLevel = Level.INFO;
        if (globalCommandLine.hasOption(VERBOSE_ARG)) {
            loggingLevel = Level.ALL;
        }
        Logger.getRoot().setLevel(loggingLevel);
    }

    /**
     * Prints out the usage statement
     */
    private void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter writer = new PrintWriter(System.err);
        writer.write("\n");
        int width = 120;
        formatter.printWrapped(writer, width, 4, RESOURCE_BUNDLE.getString(
                "MigrateCLI.usage"));
        formatter.printOptions(writer, width, globalOptions, 1, 1);
        writer.close();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP migration CLI

 18-May-05	8181/1	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 ===========================================================================
*/
