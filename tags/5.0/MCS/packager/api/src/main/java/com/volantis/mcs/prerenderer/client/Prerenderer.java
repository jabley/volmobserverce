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
import java.net.URL;
import java.util.Collection;

/**
 * Interace for prerenderer module of installer packager. 
 * 
 * Main prerenderer class should implement those public API methods  
 */

public interface Prerenderer {

    /**
     * Sets URL to prerenderer control server 
     * 
     * @param serverURL The server URL to set.
     */
    public void setServer(URL serverURL);
    
    /**
     * Sets URL to prerenderer control server
     * 
     * @param serverStr The server URI to set.
     */
    public void setServer(String serverStr);
    
    /**
     * Sets the URL to the production server, to be used when creating absolute links by prerenderer
     * 
     * @param baseStr The baseURI to set.
     */
    public void setBaseURL(String baseStr);

    /**
     * Sets the URL to the production server, to be used when creating absolute links by prerenderer
     * 
     * @param URI baseURI The baseURI to set.
     */
    public void setBaseURL(URL baseURL);
        
    /**
     * Sets the target device name
     *  
     * @param deviceName The deviceName to set.
     */
    public void setDeviceName(String deviceName); 

    /**
     * Sets prerenderer output directory. Prerenderer will will create a subdirectory for the target device in this dir and save the prerendered pages and resources in this subdirectory.
     * 
     * @param outputDir The outputDir to set.
     */
    public void setOutputDir(File destDir);

    /**
     * Adds a collection of pages with specified prefix to the list of pages to prerender
     * 
     * @param prefix URL prefix to the list of pages   
     * @param pagesList collection of pages to prerenderer
     */
    public void addAllPages(URL prefix, Collection pagesList);

    /**
     * Adds a collection of pages with specified prefix to the list of pages to prerender
     * 
     * @param prefix URL prefix to the list of pages   
     * @param pagesList collection of pages to prerenderer
     */
    public void addAllPages(String prefix, Collection pagesList);
        
    /**
     * Adds a page with specified prefix to the list of pages to prerender
     * 
     * @param prefix URL prefix to page URL   
     * @param pagePath path page to request
     */
    public void addPage(URL prefix, String pagePath);

    /**
     * Add page to request
     * 
     * @param prefix URL prefix to page URL   
     * @param pagePath path to added page
     */
    public void addPage(String prefix, String pagePath);
    
    /**
     * Clear whole list of pages to request
     *
     */
    public void clearPages();  

    /**
     * Run prerenderer
     * @throws PackagerPrerendererException 
     *
     */
    public void run() throws PackagerPrerendererException;
    
}
