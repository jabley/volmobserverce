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

package com.volantis.mcs.prerenderer.client;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A command line tool for invoking {@link Prerenderer}.
 * <p>
 * This takes the following arguments:
 * <ul>
 * <li>-d, --descriptor (required) - Specifies the path to the application descriptor
 * <li>-t, --target (required) - Specifies the device name for prerendering pages
 * <li>-o, --output (required) - Specifies the output directory in which prerendered
 *      files will be saved.
 * <li>-s, --server (required) - Specifies the URL to the prerenderer server.
 * <li>-b, --base (required) - Specifies the base URL to be used for abolute links.
 * </ul>
 */

public class PrerendererCLI {

    /**
     * The global options 
     */
    private static Options globalOptions;
    
    /**
     * The properties bundle for the PrerendererCLI.
     */
    private static final String BUNDLE_NAME =
        "com.volantis.mcs.prerenderer.client.PrerendererCLIMessages";
    
    /**
     * The ResourceBundle for properties associated with this class
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
        ResourceBundle.getBundle(BUNDLE_NAME);
               
    private static final String DESCRIPTOR_ARG = "d";
    private static final String SERVER_ARG = "s";
    private static final String OUTPUTDIR_ARG = "o";
    private static final String DEVICENAME_ARG = "t";
    private static final String BASEURI_ARG = "b";
       
    
    public static void main(String[] args) {
        
        String strDescriptor = null;
        String strServer = null;
        String strOutputDir = null;
        String strDeviceName = null;
        String strBase = null;
        
        CommandLine cmd = null;        
        try {            
            CommandLineParser parser = new BasicParser();
            Options options = createOptions();
            
            // parse command line passed by user
            cmd = parser.parse(options, args, true);                                    
        } catch (ParseException e) {
            reportError("commandLineFailed",
                    new String[] {e.getLocalizedMessage()});
            printUsage();
            System.exit(1);
        }
        
        strDescriptor = cmd.getOptionValue(DESCRIPTOR_ARG);           
        strServer = cmd.getOptionValue(SERVER_ARG);
        strOutputDir = cmd.getOptionValue(OUTPUTDIR_ARG);
        strDeviceName = cmd.getOptionValue(DEVICENAME_ARG);
        strBase = cmd.getOptionValue(BASEURI_ARG);
                                    
        // get prerenderer factory
        PrerendererFactory prerendererFactory = PrerendererFactory.getDefaultInstance();
        // create Prerenderer instance
        Prerenderer prerenderer = prerendererFactory.createPrerenderer();        
        
        DocAnalyserFactory docAnalyserFactory = DocAnalyserFactory.getDefaultInstance();  
        PrerendererDocAnalyser docAnalyser = docAnalyserFactory.createPrerendererDocAnalyser(); 
        
        try {
            // get pages list from file and convert it to List collection
            List pagesList = null;
            
            File inputFile = new File(strDescriptor);
            pagesList = docAnalyser.preparePagesListCollection(inputFile, true);
    
            // add collection list pages to prerenderer instance 
            prerenderer.addAllPages(docAnalyser.getPrefix(), pagesList);             
    
            // set URL to prerenderer control server
            prerenderer.setServer(strServer);    
    
            // set output dir path 
            File destDirFile = new File(strOutputDir);               
            if(destDirFile != null) {
                prerenderer.setOutputDir(destDirFile);                
            }
            
            // set device name for prerendering 
            prerenderer.setDeviceName(strDeviceName);
            
            // set base URL for absolute links
            prerenderer.setBaseURL(strBase);
        
            // start working prerenderer 
            prerenderer.run();
            
            // Prerenderer has succeeded. Print out a message
            String successMessage = RESOURCE_BUNDLE.getString(
                        "PrerendererCLI.prerendererSucceeded.message");
            printMessage(successMessage);            
            
        } catch (Exception e) {
            reportPrerendererFalied(e);
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
                .withArgName(getArgumentName(OUTPUTDIR_ARG))
                .withDescription(getOptionDescription(OUTPUTDIR_ARG))
                .withLongOpt(getOptionLong(OUTPUTDIR_ARG))
                .create(OUTPUTDIR_ARG));        
                
        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(SERVER_ARG))
                .withDescription(getOptionDescription(SERVER_ARG))
                .withLongOpt(getOptionLong(SERVER_ARG))
                .create(SERVER_ARG));        
                
        globalOptions.addOption(OptionBuilder
                .hasArg().isRequired(true)
                .withArgName(getArgumentName(BASEURI_ARG))
                .withDescription(getOptionDescription(BASEURI_ARG))
                .withLongOpt(getOptionLong(BASEURI_ARG))
                .create(BASEURI_ARG));
        
        return globalOptions;
    }    
    
    /**
     * Retrieves the string for an options description from the CLI bundle.
     *
     * @param option the name of the option
     * @return the description
     */
    private static String getOptionDescription(String option) {
        String propertyName = "PrerendererCLI." + option + ".description";
        return RESOURCE_BUNDLE.getString(propertyName);
    }

    /**
     * Retrieves the string for an options long name from the CLI bundle.
     *
     * @param option the name of the option
     * @return the long name of parameter
     */
    private static String getOptionLong(String option) {
        String propertyName = "PrerendererCLI." + option + ".long";
        return RESOURCE_BUNDLE.getString(propertyName);
    }
    
    /**
     * Retrieves the string for an agrument name from the CLI bundle.
     *
     * @param option the name of the option
     * @return the argument name of parameter
     */
    private static String getArgumentName(String option) {
        String propertyName = "PrerendererCLI." + option + ".argumentName";
        return RESOURCE_BUNDLE.getString(propertyName);
    }
    
    
    /**
     * Prints out the usage statement
     */
    private static void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
            RESOURCE_BUNDLE.getString("PrerendererCLI.usage.syntax"), 
            RESOURCE_BUNDLE.getString("PrerendererCLI.usage.header"),
            globalOptions,
            RESOURCE_BUNDLE.getString("PrerendererCLI.usage.footer"));
    }
    
    /**
     * Outputs a message saying the prerenderer failed to std err
     * @param cause the cause of the error
     */
    private static void reportPrerendererFalied(Exception cause) {
        reportError("prerendererFailed",
                    new String[] {cause.getLocalizedMessage()});
    }

    /**
     * Outputs an error message that is retrieved from the cli resource bundle
     * based on the given type.
     * @param key the key into the bundle
     * @param args the format args
     */
    private static void reportError(String key, String[] args) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("PrerendererCLI.")
              .append(key)
              .append(".message");
        System.err.println(MessageFormat.format(
                    RESOURCE_BUNDLE.getString(buffer.toString()),
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
