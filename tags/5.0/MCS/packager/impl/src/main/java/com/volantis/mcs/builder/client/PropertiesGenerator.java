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
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Generator list of properties for third-party tools and ant device dependent target  
 * This class generate properties into tremporary file and 
 * save name and path to file in installer.properties java property 
 */
public class PropertiesGenerator {
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(PropertiesGenerator.class);    
    
    
    // path to directory in which sis file should be saved
    private String outputDir;
    
    // path to source directory which contains all application files for installer (without device name subdirectory)
    private File inputDir;
    
    private String deviceName;
    
    // Platform UID
    private String platformUID;

    // Device UID
    private String deviceUID;

    // browser UID
    private String browserUID;     
    
    // baseDir from builder agruments
    private File baseDir;
        
    /**
     * Instance contains properties from application descriptor 
     * and parse XML response returned by DRWS returns policy value  
     */
    private BuilderDocAnalyser docAnalyser;
    
    PropertiesGenerator() {            
        DocAnalyserFactory docAnalyserFactory = DocAnalyserFactory.getDefaultInstance(); 
        docAnalyser = docAnalyserFactory.createBuilderDocAnalyser();
    }
    
    /**
     * Generate properties list to temporary file and save path 
     * to installer.properties java property
     * It also create temporary directory and save it in tmp.working.dir java propperty 
     *  
     * @param platform installer platform name like sis-dp3
     * @throws PackagerBuilderException 
     */
    public void generate(String platform) throws PackagerBuilderException {
        
        String appName = docAnalyser.getAppName(); 
        String appLabel = docAnalyser.getLabel();
        String verMajor = docAnalyser.getMajor();
        String verMinor = docAnalyser.getMinor();
        String verRevision = docAnalyser.getRevision();        
        String appUID = docAnalyser.getAppUID(platform);
        if(appUID == null) {
            throw new PackagerBuilderException(exceptionLocalizer.format("packager-uid-not-set", new Object[]{"application", platform}));
        }
        
        // set path to generated file in property
        File propertiesFile;
        try {
            propertiesFile = File.createTempFile("installer", "properties");
            System.setProperty("installer.properties", propertiesFile.getAbsolutePath());
            
            // create temporary dir and save it in property tmp.working.dir 
            File tempFile = File.createTempFile("temp", "dir");        
            
            tempFile.delete();
            tempFile.mkdir();
            System.setProperty("tmp.working.dir", tempFile.getAbsolutePath());
            
        } catch (Exception e) {
            throw new PackagerBuilderException(exceptionLocalizer.format("cannot-create-temp-file"), e);                        
        }
                                
        try {
            OutputStream fos = new FileOutputStream(propertiesFile);
            PrintStream pstream = new PrintStream(fos);
            
            // propertirs needed for PKG file
            pstream.println("application.name = " + appName);
            pstream.println("application.UID = " + appUID);
            pstream.println("application.short.UID = " + appUID.substring(2));
            pstream.println("application.label = " + appLabel);
            pstream.println("version.major = " + verMajor);
            pstream.println("version.minor = " + verMinor);
            pstream.println("version.revision = " + verRevision);
            pstream.println("platformUID = " + platformUID);
            pstream.println("productUID = " + deviceUID);
                        
            // generate all instalable application files into installer.properties
            generateFileList(pstream, appUID, platform);            
            
            // others useful properties
            pstream.println("");
            pstream.println("installer.platform = " + platform);
            pstream.println("sis.outputDir = " + outputDir.replaceAll("\\\\", "\\\\\\\\"));
            pstream.println("files.inputdir = " + inputDir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"));            
            pstream.println("deviceName = " + deviceName);            
            pstream.println("browser.UID = " + browserUID);
            
            //TODO - start page should be passed from prerenderer to builder by parameter (setter method), now we assume that it is always index.html
            pstream.println("page.startup = \\\\\\\\System\\\\\\\\Data\\\\\\\\installer\\\\\\\\" + appUID.substring(2) + "\\\\\\\\index.html");            
            pstream.println("baseDir = " + baseDir.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\"));
            
        } catch (Exception e) {
            throw new PackagerBuilderException(exceptionLocalizer.format("cannot-write-to-file", "temporary created file"), e);
        }        
    }
    
    /**
     * Get descriptor file and pass it to docAnalyser
     * @param descriptor application description
     * @throws PackagerBuilderException 
     */
    public void prepareParam(File descriptor) throws PackagerBuilderException {
        docAnalyser.prepareDescriptorParams(descriptor, true);
    }

    /**
     * Set platform UID retrieved from DRWS web service
     * @param UID platfotm UID
     */
    public void setPlatformUID(String UID) {
         this.platformUID = UID;       
    }
    
    /**
     * Wtite files list to temporary file installer.properties
     * 
     * @param pstream PrintStream for writing file
     * @param appUID application.UID
     * @throws PackagerBuilderException 
     */
    private void generateFileList(PrintStream pstream, String appUID, String platform) throws PackagerBuilderException  {
       
        List files;
        files = getFileListing(new File(inputDir, File.separator + deviceName), "\\");
        
        pstream.print("files.list = ");                 
        Iterator It = files.iterator();
        while (It.hasNext()) {
            File file = (File)(It.next());
            pstream.print(file.getName());
            if(It.hasNext()) {
                pstream.print(";");    
            }
        }                        
    }    
    
    /**
     * Return Map of files. This class parse catalog structure in inputDir directory 
     * and get list of all files to build in installer package  
     * 
     * @param aStartingDir inputDir
     * @return
     * @throws PackagerBuilderException 
     */
    private List getFileListing( File startingDir, String parentPath ) throws PackagerBuilderException {

        validateDirectory(startingDir);

        File[] files = startingDir.listFiles();
        List filesList = Arrays.asList(files);
        return filesList;
    }
    
    /**
    * Directory is valid if it exists, does not represent a file, and can be read.
    * 
    * @param dir input directory with list of files to build
     * @throws PackagerBuilderException 
    */
    private void validateDirectory (File dir) throws PackagerBuilderException  {
               
        if (!dir.exists()) {
            throw new PackagerBuilderException(exceptionLocalizer.format("directory-non-existant", dir.getName()));
        }
        
        if (!dir.isDirectory()) {
            throw new PackagerBuilderException(exceptionLocalizer.format("directory-is-file", dir.getName()));
        }
        
        if (!dir.canRead()) {
          throw new PackagerBuilderException(exceptionLocalizer.format("directory-cannot-be-read", dir.getAbsolutePath()));
        }
    }

    /**
     * Set input directory
     * @param inputDir input directory for builder 
     */
    public void setInputDir(File inputDir) {
        this.inputDir = inputDir;        
    }

    /**
     * @param deviceName The deviceName to set.
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * Set product UID
     * @param productUID
     */
    public void setProductUID(String productUID) {
        this.deviceUID = productUID;
        
    }

    public void setOutputDir(String absolutePath) {
        this.outputDir = absolutePath;        
    }

    public void setBrowserUID(String browserUID) {
        this.browserUID = browserUID;        
    }

    /**
     * @param baseDir The baseDir to set.
     */
    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;
    }
}
