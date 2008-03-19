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

/**
 * Interace for builder module of installer packager. 
 * 
 * Main builder class should implement those public API methods  
 */

public interface Builder {

    /**
     * Sets directory where Builder should look for prerendered content. Should be the same as prerenderer output directory.
     *
     * @param inputDir File to input directory.
     */
    void setInputDir(File inputDir);
    
    /**
     * Set directory where instalers will be placed
     * If builder make packages for more devices in this directory will be created some child directories named as device name
     * 
     * @param outputDir File to output directory
     */
    void setOutputDir(File outputDir);

    /**
     * Set device name for which builder create installer
     * The device name is browser dependend e.g. user should set Nokia-N80-Opera8 or Nokia-N80-NetFront3.4 instead Nokia-N80
     * 
     * @param deviceName The device name from device repository
     */
    void setDeviceName(String deviceName);
    
    /**
     * Execute the building of installer for one specified device 
     * @throws PackagerBuilderException 
     * 
     */
    void run() throws PackagerBuilderException;
    
    /**
     * Set URL for DRWS web service. It is full URL with authentication information withouth request_path
     * e.g. http://[username]:[password]@[hostname]:[portnumber]/[context_root]
     * DRWS service should be configure only in http protocol (not https) 
     * 
     * @param url URL as String to DRWS service  
     */
    void setDRWSUrl(String url);

    /**
     * Set URL for DRWS web service. It is full URL with authentication information withouth request_path
     * e.g. http://[username]:[password]@[hostname]:[portnumber]/[context_root]
     * DRWS service should be configure only in http protocol (not https) 
     * 
     * @param url URL to DRWS service  
     */
    void setDRWSUrl(URL url);
    
    /**
     * Set path to application descriptor 
     * 
     * @param descriptor File path to descriptor
     */
    void setDescriptor(File descriptor);    
    
    /**
     * Set path to directory where Builder's build.xml is installed
     * 
     * @param base File path to builder's build.xml
     */
    void setBaseDir(File base);

    /**
     * Set path to directory where Builder's build.xml is installed
     * 
     * @param base String path to builder's build.xml
     */
    void setBaseDir(String base);
    
}
