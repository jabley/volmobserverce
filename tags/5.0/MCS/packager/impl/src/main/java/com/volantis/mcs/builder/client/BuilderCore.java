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
import java.net.URL;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Main class in Builder module, do whole prerendering process  
 */        
public class BuilderCore implements Builder {
    
    private static String PLATFORM_POLICY_NAME = "preferred.installer.format";   
    private static String PLATFORM_POLICY_UID = "platform.uid";
    private static String PRODUCT_POLICY_UID = "product.uid";  
    private static String BROWSER_POLICY_UID = "browser.uid";     
       
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(BuilderCore.class);    
        
    
    /**
     * Indicator if initialize method was invoked
     */
    boolean initialized = false;
    
    private File inputDir;
    private File outputDir;
    private String deviceName;
    private String drwsURL;
    private File descriptor;
    
    /**
     * path to dir where build.xml for device dependent target is installed
     */  
    private File baseDir;

    /**
     * Web service client which get values of needed policies
     */
    DeviceRepository dr;

    /**
     * Instance of class which invoke ant target from external ant project 
     */
    AntTargetExecutor ate;
    
    /**
     * Instance of PropertiesGenerator which generate all needed property for ant target from descriptor and DRWS response 
     */
    PropertiesGenerator propertiesGenerator;
        
    /**
     * Initialize builder before run method, invoked only once
     * @throws PackagerBuilderException 
     */ 
    private void initialize() throws PackagerBuilderException{
        
        // set default logger in HttpClient to error level
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "error");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "error");               
        
        // create DRWS client 
        dr = new DeviceRepository();
        dr.setDrwsURL(drwsURL);
        
        // instantiate properties generator         
        propertiesGenerator = new PropertiesGenerator();
        
        // set common source directory path (inputDir)        
        propertiesGenerator.setInputDir(inputDir);
        
        // set common source directory path (outputDir)         
        propertiesGenerator.setOutputDir(outputDir.getAbsolutePath());
        
        // parse descriptor and store parameters inside propertiesGenerator object
        propertiesGenerator.prepareParam(descriptor);        
                
        //create tagret executor
        ate = new AntTargetExecutor();
        ate.setBaseDir(baseDir);
                
        initialized = true;
    }
    
    // Javadoc inherited
    public void run() throws PackagerBuilderException {
            
        if(! initialized) {
            initialize();
        }            

        String platform = dr.getPolicyValue(deviceName, PLATFORM_POLICY_NAME);              
        String platformUID = dr.getPolicyValue(deviceName, PLATFORM_POLICY_UID);              
        String productUID = dr.getPolicyValue(deviceName, PRODUCT_POLICY_UID);
        String browserUID = dr.getPolicyValue(deviceName, BROWSER_POLICY_UID);                        
        
        if(platform == null || platform.equals("none")) {
            throw new PackagerBuilderException(exceptionLocalizer.format("packager-policy-incorrect", new Object[]{"preffered.installer.platform", deviceName}));
        }            

        if(platformUID == null || platformUID.equals("none")) {
            throw new PackagerBuilderException(exceptionLocalizer.format("packager-policy-incorrect", new Object[]{"platform.uid", deviceName}));
        }            

        if(productUID == null || productUID.equals("none")) {
            throw new PackagerBuilderException(exceptionLocalizer.format("packager-policy-incorrect", new Object[]{"product.uid", deviceName}));
        }                    
        
        propertiesGenerator.setPlatformUID(platformUID);
        propertiesGenerator.setProductUID(productUID);
        propertiesGenerator.setBrowserUID(browserUID);
        propertiesGenerator.setDeviceName(deviceName);
        propertiesGenerator.setBaseDir(baseDir);
      
        // generate file with list of needed properties for external third-party tools
        propertiesGenerator.generate(platform);                
        
        // run build tool by ant target
        ate.runTarget(platform);                        
    }
    
    // Javadoc inherited    
    public void setDRWSUrl(String url) {
        this.drwsURL = url;
    }

    // Javadoc inherited    
    public void setDRWSUrl(URL url) {
        this.drwsURL = url.toString();        
    }
    
    // Javadoc inherited    
    public void setInputDir(File inputDir) {
        this.inputDir = inputDir;         
    }

    // Javadoc inherited    
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;        
    }

    // Javadoc inherited    
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;        
    }

    // Javadoc inherited    
    public void setDescriptor(File descriptor) {
        this.descriptor = descriptor;        
    }

    // Javadoc inherited    
    public void setBaseDir(String base) {
        baseDir = new File(base);
        if(! baseDir.exists()) {
            throw new IllegalArgumentException(exceptionLocalizer.format("directory-non-existant", new Object[]{base}));
        }        
    }

    // Javadoc inherited    
    public void setBaseDir(File base) {
        if(! base.exists()) {
            throw new IllegalArgumentException(exceptionLocalizer.format("directory-non-existant", new Object[]{base.getAbsolutePath()}));
        }
        baseDir = base;                
    }
    
}
