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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.helper.ProjectHelperImpl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Class which invoke device specific third-party tools to create installer package
 * It execute ant target saved in build.xml inside daseDir path   
 */
public class AntTargetExecutor {
        
    AntTargetExecutor() {
        BasicConfigurator.configure();
        Category.getRoot().setLevel(Level.ERROR);
    }    

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(AntTargetExecutor.class);
        
    /**
     * Absolute path to directory in which build.xml exist for run device dependent target tool  
     */
    private File baseDir;
    
    /**
     * Run specific pratform target
     * @param platform name of project to run in ant target 
     */
    public void runTarget(String platform) {

        Project antProject = new Project();
        antProject.init();        
                
        ProjectHelper helper = new ProjectHelperImpl();
        File buildPath = new File(baseDir, "build.xml");                
        helper.parse(antProject, buildPath);
                                
        try {
            antProject.executeTarget(platform);            
        } catch(Exception e) {
            if(antProject.getProperty("redirector.err") != null) {
                logger.error("packager-externals-tools-error", antProject.getProperty("redirector.err"));                                    
            }
            throw new BuildException(e.getMessage());
        }
    }

    /**
     * Set path to directory consist of build.xml file for ant target device specific tools 
     * @param base
     */
    public void setBaseDir(File base) {
        baseDir = base;
    }

}
