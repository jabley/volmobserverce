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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jdom.Document;   
import org.jdom.output.XMLOutputter;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
/**
 * Main class in Prerenderer module, do whole prerendering process  
 */        
public class PrerendererCore implements Prerenderer {
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(PrerendererCore.class);        
    
    // default count of concurent run threads 
    private static int THREAD_COUNT = 8;
    
    // Helper class for parsing XML files and prepare some collections or fragment documents to send to control server
    private PrerendererDocAnalyser docAnalyser; 
        
    // apache http client 
    private HttpClient httpClient;  
    
    private File outputDir;
    private URL server;
    private String deviceName;    
    private URL baseURL;
    private URL prefixURL;
    
    /**
     * prefix passed to control server in argument prefix-path with list of pages 
     */
    private String prefixPath;
    
    /**
     * List of full URL to pages 
     */
    private List pageList;
    
    /**
     * indicate if prerenderer tools (docAnalyser, httpClient) are created and initialized  
     */
    private boolean initialized;
    
    PrerendererCore() {
        pageList = new ArrayList();
        initialized = false;
    }
    
    /**
     * create and initialize httpClient, create output directory, get docAnalyser instance
     * @param servletUrl 
     * @throws PackagerPrerendererException 
     */    
    private void initialize() throws PackagerPrerendererException {
        
        // set default logger in HttpClient to error level        
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "error");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "error");       
        
        DocAnalyserFactory docAnalyserFactory = DocAnalyserFactory.getDefaultInstance(); 
        docAnalyser = docAnalyserFactory.createPrerendererDocAnalyser();
                
        httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        httpClient.getHttpConnectionManager().
            getParams().setConnectionTimeout(30000);
        httpClient.getHttpConnectionManager().
            getParams().setDefaultMaxConnectionsPerHost(THREAD_COUNT);
                        
        // create directory if not exist for output files
        if(outputDir != null) {
        	// if directory not exist
            boolean success = false;
        	if(!outputDir.exists()) {
        		success = outputDir.mkdirs();
            	if (! success ) {
                    throw new PackagerPrerendererException(exceptionLocalizer.format("cannot-create-dir", new Object[]{outputDir.getAbsolutePath()}));                    
            	}        	
        	}
        }                
        initialized = true;
    }

    /**
     * Run prerenderer process
     * @throws PackagerPrerendererException 
     */
    public void run() throws PackagerPrerendererException {
        
        // initialized only at first running method run 
        if(!initialized) {
            initialize();            
        }
        
        // run control request (post list of pages to prerendering process)            
        Map map;        
        map = postPagesList(baseURL.toString(), prefixPath);
    
        // create directory named like deviceName
        File deviceFile = null;
        deviceFile = createDeviceDir();
        
        // download pages and save in destDir/deviceName
        Fetcher downloader = new Fetcher(map, httpClient, deviceFile, deviceName);            
        downloader.fetch();
        
        // load and save resources
        do {
            map = getResourcesList();
            if(map.isEmpty()) {
                break;
            }
            downloader = new Fetcher(map, httpClient, deviceFile, deviceName);            
            downloader.fetch();   
            
        } while(true);            
    }
    
    
    /**
     * Create device directory inside output directory
     * @throws PackagerPrerendererException 
     */
    private File createDeviceDir() throws PackagerPrerendererException {
        // create directory if not exist for output files
        boolean success = false;
        File deviceDir = new File(outputDir, deviceName);
        if(!deviceDir.exists()) {
            success = deviceDir.mkdir();
            if (! success ) {
              throw new PackagerPrerendererException(exceptionLocalizer.format("packager-cannot-create-device-dir", new Object[]{deviceName, outputDir.getAbsolutePath()}));
            }                
        }    
        return deviceDir;
    }

    /**
     * Send list of pages to prerendering process to prerenderer control server
     * 
     * @param baseURI base URL for rewtiting absolute links
     * @param prefixPath prefix path for pages
     * @return Mapping - collection of Pairs consist of local and remote URL 
     * @throws PackagerPrerendererException if is problem with communitation to control server
     */
    private Map postPagesList(String baseURL, String prefixPath) throws PackagerPrerendererException {

        // prepare list of pages in Document XML for request
        Document pagesDocument = null;
        if(pageList.size() != 0) {
            pagesDocument = docAnalyser.preparePagesListDocument(pageList, baseURL, prefixPath);
        }    
                        
        PostMethod post = new PostMethod(server.toString());                
        String bodyContent = new XMLOutputter().outputString(pagesDocument);              
        post.setRequestBody(bodyContent);
            
        // post request to control prerenderer server        
        int iPostResultCode = 0;
        try {
            iPostResultCode = httpClient.executeMethod(post);
        } catch(Exception e) {
            post.releaseConnection();
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-control-server-io-exception", new Object[]{server.toString()}), e);
        }
                
        if(iPostResultCode != 200) {
            String errorMessage = "";
            try {
                errorMessage = post.getResponseBodyAsString();
            } catch (IOException e) {
                errorMessage = "Error message is impossible to read from response!";
            } 
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-control-server-invalid-response", new Object[]{"" + iPostResultCode, errorMessage}));
        }
        
        Map map;
        try {
            map = docAnalyser.preparePagesMapping(pageList, post.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-control-server-invalid-response", new Object[]{"" + iPostResultCode, e.getLocalizedMessage()}));
        }        
        return map;                                       
    }
    
    /**
     * Get list of resources from prerenderer control server
     * @return map consist of remote URL and local URL for resource file
     * @throws PackagerPrerendererException
     */
    private Map getResourcesList() throws PackagerPrerendererException {

        GetMethod get = new GetMethod(server.toString());
        get.setFollowRedirects(true);

        int iGetResultCode = 0;
        try {
            iGetResultCode = httpClient.executeMethod(get);
        } catch (Exception e) {  
            get.releaseConnection();            
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-control-server-io-exception", new Object[]{server.toString()}), e);
        }
                
        if(iGetResultCode != 200) {
            String errorMessage = "";
            try {
                errorMessage = get.getResponseBodyAsString();
            } catch (IOException e) {
                errorMessage = "Error message is impossible to read from response!";
            }
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-control-server-invalid-response", new Object[]{"" + iGetResultCode, errorMessage}));            
        }
        Map map;
        try {
            map = docAnalyser.prepareResourcesMapping(get.getResponseBodyAsStream(), prefixURL);
        } catch (IOException e) {
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-control-server-invalid-response", new Object[]{"" + iGetResultCode, e.getLocalizedMessage()}));
        }        
        return map;        
    }          
        
    // Javadoc inherited
    public void setOutputDir(File destDir) {
        this.outputDir = destDir;
    }
    
    // Javadoc inherited
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
       
    // Javadoc inherited
    public void setServer(URL server) {
        this.server = server;
    }

    // Javadoc inherited
    public void setServer(String serverURL) {
        try {
            this.server = new URL(serverURL);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(exceptionLocalizer.format("malformed-url", new Object[]{serverURL}));            
        }
    }
    
    // Javadoc inherited
    public void addPage(URL prefix, String pageStr) {
        pageList.add(pageStr);
        prefixURL = prefix;        
    }

    // Javadoc inherited
    public void addPage(String prefix, String pageURL) {
        pageList.add(pageURL);
        try {
            prefixURL = new URL(prefix);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(exceptionLocalizer.format("malformed-url", new Object[]{prefix}));            
        }
        validatePrefix();        
    }
    
    // Javadoc inherited
    public void addAllPages(String prefix, final Collection pages) {
        pageList.addAll(pages);
        try {
            prefixURL = new URL(prefix);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(exceptionLocalizer.format("malformed-url", new Object[]{prefix}));                        
        }
        validatePrefix();                
    }    

    // Javadoc inherited    
    public void addAllPages(URL prefix, final Collection pages) {
        pageList.addAll(pages);
        prefixURL = prefix;
        
        validatePrefix();                
    }    
    
    // Javadoc inherited    
    public void clearPages() {
        pageList.clear();
    }

    // Javadoc inherited
    public void setBaseURL(String base) {
        try {
            baseURL = new URL(base);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(exceptionLocalizer.format("malformed-url", new Object[]{base}));                        
        }        
        validateBase();
    }

    // Javadoc inherited
    public void setBaseURL(URL base) {        
        baseURL = base;
        validateBase();        
    }
    
    /**
     * Validate prefix URL
     */
    private void validatePrefix() {
        
        if(! prefixURL.getProtocol().equals("http")) {
            throw new IllegalArgumentException(exceptionLocalizer.format("url-http-only-supported"));            
        }
        
        if(prefixURL.getHost() == null) {
            throw new IllegalArgumentException(exceptionLocalizer.format("url-unknown-host", new Object[]{baseURL.toString()}));                                
        }

        if(prefixURL.getQuery() != null) {
            throw new IllegalArgumentException(exceptionLocalizer.format("url-query-not-permitted"));                    
        }        
        
        prefixPath = prefixURL.getPath();         
                       
        if(prefixPath == null || !prefixPath.endsWith("/")) {
            throw new IllegalArgumentException(exceptionLocalizer.format("url-path-mandatory", new Object[]{"context inside application descriptor"}));            
        }        
    }
    
    /**
     * Validate base URL
     */
    private void validateBase() {
        
        if(baseURL.getHost() == null) {
            throw new IllegalArgumentException(exceptionLocalizer.format("packager-unknown-host-in-url", new Object[]{baseURL.toString()}));                    
        }

        String path = baseURL.getPath();  
                
        if(path == null || ! path.endsWith("/")) {
            throw new IllegalArgumentException(exceptionLocalizer.format("url-path-mandatory", new Object[]{"base URL parameter"}));            
        }
        
    }
    
}
