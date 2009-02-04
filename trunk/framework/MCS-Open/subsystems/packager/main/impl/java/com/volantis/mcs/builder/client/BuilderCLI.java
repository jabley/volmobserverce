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

package com.volantis.mcs.builder.client;

import java.io.File;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A command line tool for invoking {@link Builder}.
 * <p>
 * This takes the following arguments:
 * <ul>
 * <li>-b, --base (required) - Specifies the path to directory where Builder's build.xml is installed
 * <li>-i, --input (required) - Specifies the path to input directory
 * <li>-d, --descriptor (required) - Specifies the path to the application descriptor
 * <li>-t, --targetdevice (required) - Specifies the target device
 * <li>-r, --drws (required) - Specifies the URL of Device Repository web service.
 * <li>-o, --output (required) - Specifies the path to output directory.
 * </ul>
 */
public class BuilderCLI {

    /**
     * The global options 
     */
    private static Options globalOptions;
    
    /**
     * The properties bundle for the BuilderCLI.
     */
    private static final String BUNDLE_NAME =
        "com.volantis.mcs.builder.client.BuilderCLIMessages";
    
    /**
     * The ResourceBundle for properties associated with this class
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
        ResourceBundle.getBundle(BUNDLE_NAME);

    private static final String DESCRIPTOR_ARG = "d";
    private static final String INPUTDIR_ARG = "i";
    private static final String OUTPUTDIR_ARG = "o";
    private static final String DRWS_ARG = "r";
    private static final String DEVICENAME_ARG = "t";        
    private static final String BASEDIR_ARG = "b";        
       
    public static void main(String[] args) {

        String strDescriptor = null;
        String strInputDir = null;
        String strOutputDir = null;
        String strDeviceName = null;
        String strDRWS = null;        
        String strBaseDir = null;        
                
        CommandLineParser parser = new BasicParser();
        Options options = createOptions();
        
        CommandLine cmd = null;
        
        // parse command line passed by user            
        try {
            cmd = parser.parse(options, args, true);
        } catch (ParseException e) {
            reportError("commandLineFailed",
                    new String[] {e.getLocalizedMessage()});
            printUsage();
            System.exit(1);
        }
            
        strDescriptor = cmd.getOptionValue(DESCRIPTOR_ARG);
        strInputDir = cmd.getOptionValue(INPUTDIR_ARG);
        strOutputDir = cmd.getOptionValue(OUTPUTDIR_ARG);
        strDeviceName = cmd.getOptionValue(DEVICENAME_ARG);
        strDRWS = cmd.getOptionValue(DRWS_ARG);
        strBaseDir = cmd.getOptionValue(BASEDIR_ARG);
        
        //set builder parameters
        BuilderFactory builderFactory = BuilderFactory.getDefaultInstance(); 
        Builder builder = builderFactory.createBuilder();
        
        try {
            
            File inputDir = new File(strInputDir);            
            // set input directory with pages to build in installer
            builder.setInputDir(inputDir);
    
            File outputDir = new File(strOutputDir);
       
            // set output directory
            builder.setOutputDir(outputDir);
            
            // set DRWS URL
            builder.setDRWSUrl(strDRWS);
    
            // det device name
            builder.setDeviceName(strDeviceName);
            
            File descriptorFile = new File(strDescriptor);
            
            // set application descriptor 
            builder.setDescriptor(descriptorFile);
    
            //set base dir for build.xml builder file
            File baseDirFile = new File(strBaseDir);                        
            builder.setBaseDir(baseDirFile);
        
            // run builder               
            builder.run();
            
            // Builder has succeeded. Print out a message
            String successMessage = RESOURCE_BUNDLE.getString(
                        "BuilderCLI.builderSucceeded.message");
            printMessage(successMessage);                        
            
        } catch (Exception e) {
            reportBuilderFalied(e);
            System.exit(1);            
        }        
    }
    
    /**
     * Create the commons-cli Options object which defines the command line
     * options that can be processed.
     *
     * @return the Options object created.
     */
    private static Options createOptions() {
        
        globalOptions = new Options();
        
        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(BASEDIR_ARG))
                .withDescription(getOptionDescription(BASEDIR_ARG))
                .withLongOpt(getOptionLong(BASEDIR_ARG))
                .create(BASEDIR_ARG));        
                
        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(INPUTDIR_ARG))
                .withDescription(getOptionDescription(INPUTDIR_ARG))
                .withLongOpt(getOptionLong(INPUTDIR_ARG))
                .create(INPUTDIR_ARG));                

        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(DESCRIPTOR_ARG))
                .withDescription(getOptionDescription(DESCRIPTOR_ARG))
                .withLongOpt(getOptionLong(DESCRIPTOR_ARG))
                .create(DESCRIPTOR_ARG));        
                        
        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(DEVICENAME_ARG))
                .withDescription(getOptionDescription(DEVICENAME_ARG))
                .withLongOpt(getOptionLong(DEVICENAME_ARG))
                .create(DEVICENAME_ARG));        

        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(DRWS_ARG))
                .withDescription(getOptionDescription(DRWS_ARG))
                .withLongOpt(getOptionLong(DRWS_ARG))
                .create(DRWS_ARG));        

        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(OUTPUTDIR_ARG))
                .withDescription(getOptionDescription(OUTPUTDIR_ARG))
                .withLongOpt(getOptionLong(OUTPUTDIR_ARG))
                .create(OUTPUTDIR_ARG));        
        
        return globalOptions;
    }
    
    /**
     * Retrieves the string for an options description from the CLI bundle.
     *
     * @param option the name of the option
     * @return the description
     */
    private static String getOptionDescription(String option) {
        String propertyName = "BuilderCLI." + option + ".description";
        return RESOURCE_BUNDLE.getString(propertyName);
    }

    /**
     * Retrieves the string for an options long argument name from the CLI bundle.
     *
     * @param option the name of the option
     * @return the long argument name
     */
    private static String getOptionLong(String option) {
        String propertyName = "BuilderCLI." + option + ".long";
        return RESOURCE_BUNDLE.getString(propertyName);
    }
    
    /**
     * Retrieves the string for an agrument name from the CLI bundle.
     *
     * @param option the name of the option
     * @return the argument name of parameter
     */
    private static String getArgumentName(String option) {
        String propertyName = "BuilderCLI." + option + ".argumentName";
        return RESOURCE_BUNDLE.getString(propertyName);
    }    
    
    /**
     * Prints out the usage statement
     */
    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            RESOURCE_BUNDLE.getString("BuilderCLI.usage.syntax"), 
            RESOURCE_BUNDLE.getString("BuilderCLI.usage.header"),
            globalOptions,
            RESOURCE_BUNDLE.getString("BuilderCLI.usage.footer"));
    }
    
    /**
     * Outputs a message saying the prerenderer failed to std err
     * @param cause the cause of the error
     */
    private static void reportBuilderFalied(Exception cause) {
        reportError("builderFailed",
                    new String[] {cause.getLocalizedMessage()});
    }

    /**
     * Outputs an error message that is retrieved from the cli resource bundle
     * based on the given type.
     * @param key the key into the bundle
     * @param args the format args
     */
    private static void reportError(String key, String[] args) {
        System.err.println(
                MessageFormat.format(
                    RESOURCE_BUNDLE.getString("BuilderCLI." + key + ".message"),
                    args));
    }    
    
    /**
     * Prints out a generic mesage to std out
     * @param message the message to print
     */
    private static void printMessage(String message) {
        System.out.println(message);
    }
        

}
