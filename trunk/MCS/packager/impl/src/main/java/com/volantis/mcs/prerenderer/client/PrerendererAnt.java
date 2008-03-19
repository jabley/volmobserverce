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
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 *  Class implement ant task for prerenderer. It get all needed parameters, set for prerenderer module
 *  and start prerenderer   
 */

public class PrerendererAnt extends Task {
    
    private String descriptor;
    private String serverurl;
    private String outputdir;
    private String devicename;
    private String baseurl;
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(PrerendererAnt.class);
    
    
    public void execute() throws BuildException { 
        
        if (descriptor==null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "descriptor"));
        }      

        if (serverurl==null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "serverurl"));
        }      

        if (outputdir==null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "outputdir"));
        }      

        if (devicename==null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "devicename"));
        }      

        if (baseurl==null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "baseurl"));
        }      
                
        // get prerenderer factory
        PrerendererFactory prerendererFactory = PrerendererFactory.getDefaultInstance();
        // create Prerenderer instance
        Prerenderer prerenderer = prerendererFactory.createPrerenderer();
         
        // get PrerendererDocAnalyser factory
        DocAnalyserFactory docAnalyserFactory = DocAnalyserFactory.getDefaultInstance(); 
        PrerendererDocAnalyser docAnalyser = docAnalyserFactory.createPrerendererDocAnalyser();
        
        try {
        
            // get pages list from file and convert it to List collection
            File inputFile = new File(descriptor);
            if (!(inputFile.exists())) {
                throw new PackagerPrerendererException(exceptionLocalizer.format("file-missing", "descriptor"));            
            }               

            List pagesList = docAnalyser.preparePagesListCollection(inputFile, true);
            
            // add collection of pages to  prerenderer module
            prerenderer.addAllPages(docAnalyser.getPrefix(), pagesList);             
    
            // set control server URL
            prerenderer.setServer(serverurl);    
            
            // set destination directory path
            File destDirFile = new File(outputdir);               
            if(destDirFile != null) {
                prerenderer.setOutputDir(destDirFile);                
            }
            
            // set device name for prerendering pages
            prerenderer.setDeviceName(devicename);
            
            // set base URL to prerenderer
            prerenderer.setBaseURL(baseurl);
        
            // start working for prerenderer 
            prerenderer.run();
            
        } catch (Exception e) {
            throw new BuildException(exceptionLocalizer.format("packager-prerenderer-failed", e.getLocalizedMessage()));
        }        
    }     
    
    /**
     * @param baseURI The baseURI to set.
     */
    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    /**
     * @param descriptor The descriptor to set.
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    /**
     * @param deviceName The deviceName to set.
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }

    /**
     * @param outputDir The outputDir to set.
     */
    public void setOutputdir(String dir) {
        this.outputdir = dir;
    }

    /**
     * @param serverURI The serverURI to set.
     */
    public void setServerurl(String server) {
        this.serverurl = server;
    }
    
}
