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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.servlet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import mock.javax.servlet.FilterConfigMock;
import mock.javax.servlet.ServletContextMock;

import com.volantis.mcs.runtime.GenericURLRemapper;
import com.volantis.testtools.mock.test.MockTestCaseAbstract;

/**
 * This class is responsible for testing the behaviour of
 * {@link GenericURLRemapper}.
 */
public abstract class GenericURLRemapperTestAbstract extends MockTestCaseAbstract {
    
    /**
     * Proper URL.
     */
    private static final String REMOTE_HOST_OK = "http://remotehost:8080";
    
    /**
     * Incorrect URL
     */
    private static final String REMOTE_HOST_BAD = "incorrectURL";
    
    /**
     * Name of the remote project with properly defined URL.
     */
    private static final String REMOTE_PROJECT_OK = "remoteOK";
    
    /**
     * Name of the remote project with improperly defined URL.
     */
    private static final String REMOTE_PROJECT_BAD = "remoteBad";
    
    /**
     * Name of the properties file which contains the project mappings. 
     * The file will be created in temp directory. Must be the same as
     * GenericURLRemapper.PROPERTIES_FILE
     */
    private static final String PROPERTIES_FILE_NAME = "projects.properties";
    
    /** 
     * System temp dir.
     */
    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir"); 
    
    /**
     * Name of directory in which projects.properties file will be created
     */
    private String propertiesDirectoryName;
    
    /**
     * projects.properties file.
     */
    private File propertiesFile;
    
    
    private FilterConfigMock filterConfigMock;
    
    private ServletContextMock servletContextMock;
    
    GenericURLRemapper remapper;
    
//     Javadoc inherited.
    protected void setUp() throws Exception {
        super.setUp();

        createProjectsPropertiesFile();
  
        filterConfigMock = new FilterConfigMock("filterConfigMock", expectations);
        servletContextMock = new ServletContextMock("servletContextMock", expectations);        
        
        filterConfigMock.expects.getServletContext().returns(servletContextMock).fixed(1);
        servletContextMock.expects.getRealPath("/").returns(propertiesDirectoryName).fixed(1);
        
        remapper = getURLRemapper();
        remapper.initialise(filterConfigMock);
    }

//  Javadoc inherited.
    protected void tearDown() {
        deleteProjectsPropertiesFile();
    }
    

    
    /**
     * remapURL should remain URL unchanged if it does not relate
     * to any remote projects
     */
    public void testRemapURLLocalOK() {

        String unmappedURL = 
            new String("http://localhost:8080/volantis/index.xdime");
        
        String servletPath = new String("/index.xdime");
        
        String mappedURL = remapper.remapURL(unmappedURL, servletPath);
        
        assertEquals(unmappedURL, mappedURL);
    }

    /**
     * If URL relates to remote project with properly defined host URL,
     * then URL must be remapped to remote host
     */
    public void testRemapURLWithProperRemoteProjectURL() {

        String unmappedURL = 
            new String("http://localhost:8080/volantis/" + 
                    REMOTE_PROJECT_OK + "/index.xdime");
        
        String servletPath = 
            new String("/" + REMOTE_PROJECT_OK + "/index.xdime");
        
        String expectedURL = REMOTE_HOST_OK + "/index.xdime";
        
        String mappedURL = remapper.remapURL(unmappedURL, servletPath);
        
        assertEquals(mappedURL, expectedURL);
    } 

    /**
     * If URL relates to remote project with improperly defined host URL,
     * then URL should remain unchanged
     */
    public void testRemapURLWithIncorrectRemoteProjectURL() {

        String unmappedURL = 
            new String("http://localhost:8080/volantis/" + 
                    REMOTE_PROJECT_BAD + "/index.xdime");
         
        String servletPath = 
            new String("/" + REMOTE_PROJECT_BAD + "/index.xdime");
        
        String mappedURL = remapper.remapURL(unmappedURL, servletPath);

        assertEquals(unmappedURL, mappedURL);
    }
    
    /**
     * Creates temporary projects.properties file in system temp dir,
     * and writes there properties used later for tests.
     */
    private void createProjectsPropertiesFile() {
        Random r = new Random();
    	
        propertiesDirectoryName = TEMP_DIR + System.getProperty("file.separator") 
            + "tmp" + r.nextLong();
        (new File(propertiesDirectoryName)).mkdir();
        propertiesFile = new File(propertiesDirectoryName, PROPERTIES_FILE_NAME);
        try {
            BufferedWriter out = new BufferedWriter(
                    new FileWriter(propertiesFile));
            out.write(REMOTE_PROJECT_OK + "=" + REMOTE_HOST_OK + "\n");
            out.write(REMOTE_PROJECT_BAD + "=" + REMOTE_HOST_BAD);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Deletes temporary projects.properties file
     * after tests finish.
     */
    private void deleteProjectsPropertiesFile() {
        propertiesFile.delete();
        (new File(propertiesDirectoryName)).delete();
    }
    
    /**
     * Returns instance of GenericURLRemapper to test.
     */
    public abstract GenericURLRemapper getURLRemapper();
}
