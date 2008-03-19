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
package com.volantis.mcs.cli.uaprofclient;

import com.sun.ccpp.DescriptionManager;
import com.sun.ccpp.ProfileFactoryImpl;
import com.sun.ccpp.ProfileFragmentFactoryImpl;
import com.volantis.mcs.cli.uaprofclient.output.RepositoryXMLSerialiser;
import com.volantis.mcs.cli.uaprofclient.output.ProfileSerialiser;

import javax.ccpp.ProfileFactory;
import javax.ccpp.ProfileFragmentFactory;
import javax.ccpp.ValidationMode;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The main class for the Mariner UAProf client.
 * <p>
 * This reads in a CCPP UAProf RDF Device definition and spits out a MCS 
 * device definition. 
 * <p>
 * NOTE: This code was developed as a prototyping exercise without the 
 * involvement of architecture. As such, there are no test cases and the code
 * quality is not what it could be, as speed of development was "paramount". 
 * <p> 
 * NOTE: This requires JDK1.4.1. This is because the CCPP RI requires it.
 * <p>
 * NOTE: CCPP RI uses the JDK 1.4 Logging API. This client code therefore also 
 * uses the JDK 1.4 Logging API, for consistency. It is very similar to log4j.
 */ 
public class MarinerUAProf {

    /**
     * The JDK 1.4 Logger object to use.
     * <p>
     * NOTE: we use 1.4 logging here since the majority of the interesting
     * logging actually occurs inside the RI and that uses JDK 1.4 logging so
     * we may as well use it to be consistent.
     * <p>
     * Take a look in in the [java-dir]/jre/lib/logging.properties file for 
     * how you can configure this at runtime.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.MarinerUAProf");

    private static final String PACKAGE_DIR = 
            "com/volantis/mcs/cli/uaprofclient/";
    
    private static final String VOCABULARY_PACKAGE_DIR = 
            PACKAGE_DIR + "vocabulary/";
    
    private static final String NAME_TRANSLATION_PROPERTIES_FILENAME = 
            "MarinerUAProf.properties";

    public static void main(String[] args) {

        // Process arguments.
        if (args == null || args.length < 4) {
            System.out.println("Usage: MarinerUAProf <url> <deviceName> " +
                    "<fallbackDeviceName> <warnOnCharacters> " +
                    "[<output file>]");
            System.out.println("(Uses name translation properties from " +
                    "./MarinerUAProf.properties or falls back to jar version.)");
            return;
        }
        // For now, hardcode the Validation Mode to "weak". This reports errors
        // but still allows us to get the results of the parse.
        int mode = ValidationMode.VALIDATIONMODE_WEAK;
        // Collect the URL parameter.
        String url = args[0];
        String deviceName = args[1];
        String fallbackDeviceName = args[2];
        char[] warnOnCharacters = args[3].toCharArray();
        String outputFileName = null;
        if (args.length >= 5) {
            outputFileName = args[4];
        }
        
        try {
            
            // Find and load the name translation properties.
            // These will be used to translate the UAProf attribute names into
            // MCS attribute names. 
            // Having all the valid attribute names declared also allows us to 
            // filter out any names in the RDF which MCS does not understand.
            Properties nameTranslationProps = loadNameTranslationProperties();

            // Initialise the CCPP API factory singleton instances that the 
            // ProfileParser requires in order to be able to parse the RDF 
            // files. Yep, I love singletons.
            initialiseCCPPFactories();

            // Initialise our output stream to point to the appropriate place.
            PrintWriter out = null;
            if (outputFileName != null) {
                out = new PrintWriter(new FileWriter(outputFileName));
            } else {
                out = new PrintWriter(System.out);
            }
            
            // Process the Profile to generate a simple map of Attributes.
            ProfileParser parser = new ProfileParser(mode);
            Map inputMap = parser.processURL(url);

            // Translate the map of CCPP UAProf attributes into a map of 
            // MCS attributes.
            ProfileTranslator translator = new ProfileTranslator(
                    nameTranslationProps, warnOnCharacters);
            Map outputMap = translator.translate(inputMap);
            
            // Dump the MCS format attributes out in MCS Repository XML.
            ProfileSerialiser serialiser = new RepositoryXMLSerialiser(
                    deviceName, fallbackDeviceName, out);
            serialiser.serialise(outputMap);
            
            // Close our output stream.
            out.close();
            
        } catch (Exception e) {
            
            e.printStackTrace();
            
        }
        
    }

    private static void initialiseCCPPFactories() throws Exception {
        
        // Tell the RI to use the following as it's logger name.
        System.setProperty("com.sun.ccpp.logger", 
                "com.volantis.mcs.cli.uaprofclient.ccpp-ri");

        // The CCPP RI (Jena I think) seems to use the org.xml.sax.driver
        // system property to find a SAX parser (rather than JAXP), and it
        // seems to default to looking for xerces if that is not set - it 
        // normally operates in Tomcat 4.1 which provides xerces anyway.
        // So, we can either run with jar/internal/xerces.jar or we can 
        // register our renamed jar/redist/xercesImpl.jar into the property
        // and use that. Note also that IBM JDK provides xerces as it's
        // "native" XML parser, whereas Sun uses Crimson.
        //System.out.println(System.getProperty("org.xml.sax.driver"));
        System.setProperty("org.xml.sax.driver", 
                "com.volantis.xml.xerces.parsers.SAXParser");
        
        // Set up the javax.ccpp factories required by the rest of the app.
        // Currently we use the JSR-188 RI implementation, but in future 
        // it might be better to use a "real" implementation if one is 
        // available.
        // NOTE: This initialisation code is mostly copied from the RI 
        // 20020925 RI example init.jsp.
        logger.fine("Initialising JSR-188 RI CC/PP library");

        // Create and initialise the RI Profile Factory.
        ProfileFactoryImpl factory = (ProfileFactoryImpl)
                ProfileFactoryImpl.getInstance();
        factory.setCacheEnabled(false);
        
        // Create and initialise the RI Profile Fragment Factory.
        ProfileFragmentFactoryImpl fragmentFactory =
                (ProfileFragmentFactoryImpl) 
                    ProfileFragmentFactoryImpl.getInstance();

        // Create and initialise the RI Description Manager.
        DescriptionManager.setSchema(getVocabularyStream("vocabulary.xsd"));
        DescriptionManager dm = DescriptionManager.getInstance();
        
        // Add the "normal" vocabulary files provided with the RI example.
        // NOTE: it would be better if these were configurable.
        dm.addVocabulary(getVocabularyStream("universalvocab.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-19991014.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20000405.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20000405a.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20000405b.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010111.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010330.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010330a.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010330b.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010430.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010430a.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20010430b.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20020710.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20020710a.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20020710b.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20021212.xml"));
        dm.addVocabulary(getVocabularyStream("ccppschema-20030226.xml"));

        // OK, we have set up the CC/PP RI, so save away the factories
        // for our CC/PP client code to find later. Oh, I love singletons.
        ProfileFactory.setInstance(factory);
        ProfileFragmentFactory.setInstance(fragmentFactory);
            
        // NOTE: using RI 20030925: When you call factory.newProfile(), 
        // the RI potentially generates a load of errors to stdout. These 
        // are split into two areas. The first area is Jena errors which 
        // are dumped direct to stdout. They look like so:
        // 
        //  [169:30]: Unknown or out of context RDF description element li
        //  [170:30]: Only one element allowed inside a property element
        //  [171:30]: Only one element allowed inside a property element
        //  java.lang.NullPointerException
        // 
        // Notice how Jena doesn't print stack traces! Argh!
        // 
        // Next comes the errors from the RI itself. They look like so:
        // 
        //  03-Oct-2003 16:54:47 com.sun.ccpp.FragmentProcessor processComponent
        //  WARNING: Attribute not defined in component.
        //  03-Oct-2003 16:54:47 com.sun.ccpp.FragmentProcessor processComponent
        //  INFO: http://www.wapforum.org/UAPROF/ccppschema-20000405#CcppAccept-Charset
        //  ...
        //
        // Notice how single logical events are split into two. Sigh.
        // Currently the only way to capture all of this would be to 
        // redirect Stdout. You can redirect the jdk1.4 logging as per the
        // example JSP page, but only the RI itself uses jdk1.4 logging, 
        // so that only captures the second lot of errors.
    }

    private static Properties loadNameTranslationProperties() 
            throws Exception {
        
        logger.fine("Loading name translation properties");
        InputStream is = null;
        // First try a local file in the current directory.
        File nameTranslationFile = new File(
                NAME_TRANSLATION_PROPERTIES_FILENAME);
        if (nameTranslationFile.exists()) {
            is = new FileInputStream(nameTranslationFile);
        }
        // If that couldn't be found,
        if (is == null) {
            // Then fall back to the one provided in the jar file.
            String resource = PACKAGE_DIR + 
                    NAME_TRANSLATION_PROPERTIES_FILENAME;
            logger.fine("Using name translation resource: " + resource);
            is = getResourceStream(resource);
        }
        Properties nameTranslationProps = new Properties();
        nameTranslationProps.load(is);
        return nameTranslationProps;
    }

    private static InputStream getVocabularyStream(String name) 
            throws Exception {
        
        String resource = VOCABULARY_PACKAGE_DIR + name;
        logger.fine("Using vocabulary resource: " + resource);
        return getResourceStream(resource);
    }
    
    private static InputStream getResourceStream(String resource) 
            throws Exception {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL nameTranslationUrl = cl.getResource(resource);
        if (nameTranslationUrl == null) {
            throw new Exception("Cannot load resource: " + resource);
        }
        return nameTranslationUrl.openStream();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Oct-03	1461/14	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/12	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 06-Oct-03	1461/5	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (can dump text out only - for testing, take two, forgot the jars)

 06-Oct-03	1461/3	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (can dump text out only - for testing)

 ===========================================================================
*/
