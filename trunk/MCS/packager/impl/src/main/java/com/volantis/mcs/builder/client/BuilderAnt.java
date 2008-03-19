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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import com.volantis.mcs.localization.LocalizationFactory;

/**
 *  Class implement ant task for builder. It get all needed parameters, set for builder module
 *  and start builder   
 */
public class BuilderAnt extends Task {

    private String descriptor;
    private String inputdir;
    private String outputdir;
    private String drwsurl;
    private String devicename;
    private String basedir;
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(BuilderAnt.class);
    
    
    public void execute() throws BuildException { 
        
        if (descriptor == null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "descriptor"));
        }      

        if (inputdir == null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "inputdir"));
        }      

        if (outputdir == null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "outpputdir"));
        }      

        if (devicename == null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "devicename"));
        }      

        if (drwsurl == null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "drwsurl"));
        }      

        if (basedir == null) {
            throw new BuildException(exceptionLocalizer.format("packager-missing-mandatory-parameter", "basedir"));
        }      
           
        // get factory for builder
        BuilderFactory builderFactory = BuilderFactory.getDefaultInstance();
        // get builder instance
        Builder builder = builderFactory.createBuilder();         
        
        //set destination directory path
        File destDirFile = new File(outputdir);               
        if(destDirFile != null) {
            builder.setOutputDir(destDirFile);                
        }

        try { 

            //set source directory path
            File sourceDirFile = new File(inputdir);               
            if(sourceDirFile != null) {
                builder.setInputDir(sourceDirFile);                
            }
            
            // set device name for build installer 
            builder.setDeviceName(devicename);
            
            // set DRWS URL
            builder.setDRWSUrl(drwsurl);
           
            // set base dir in which build.xml exist for device dependent target 
            File baseDirFile = new File(basedir);               
            if(baseDirFile != null) {
                builder.setBaseDir(baseDirFile);                
            }  
            
            // set descriptor application  
            File descriptorFile = new File(descriptor);
            if (!(descriptorFile.exists())) {
                throw new PackagerBuilderException(exceptionLocalizer.format("file-missing", "descriptor"));            
            }               
            builder.setDescriptor(descriptorFile);                        
        
            // start working builder 
            builder.run();
            
        } catch (PackagerBuilderException e) {
            throw new BuildException(exceptionLocalizer.format("packager-builder-failed", e.getLocalizedMessage()));            
        }        
    } 
    
    /**
     * Set descriptor path
     * 
     * @param descriptor The descriptor to set.
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    
    /**
     * Set device name 
     * 
     * @param devicename The devicename to set.
     */
    public void setDevicename(String devicename) {
        this.devicename = devicename;
    }
    
    /**
     * Set DRWS URL
     * 
     * @param drwsurl The drwsurl to set.
     */
    public void setDrwsurl(String drwsurl) {
        this.drwsurl = drwsurl;
    }
    
    /**
     * Set input dir
     * 
     * @param inputdir The inputdir to set.
     */
    public void setInputdir(String inputdir) {
        this.inputdir = inputdir;
    }
    
    /**
     * Set output dir
     * 
     * @param outputdir The outputdir to set.
     */
    public void setOutputdir(String outputdir) {
        this.outputdir = outputdir;
    }

    /**
     * Set base dir
     * 
     * @param basedir The basedir to set.
     */
    public void setBasedir(String basedir) {
        this.basedir = basedir;
    }
    
}
