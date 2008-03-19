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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Request MCS for XDIME page or their resource, save prerendered response in file 
 */
public class FetcherThread extends Thread {
    
    // result, contain exception
    private Exception result;    
    private Queue queue;
    private HttpClient httpClient;
    
    private String deviceName;
    private File deviceFile;
    
    FetcherThread(Queue queue, HttpClient client, File deviceFile, String device) {
        this.queue = queue;
        this.httpClient = client;
        this.deviceName = device;
        this.deviceFile = deviceFile;        
    }

    /**
     * Request and save response in file
     */
    public void run() {
        
        Pair p = null;
        try {
            while((p = queue.pop()) != null) {
                
                GetMethod get = new GetMethod(p.getRemoteUri());
                try {                    
                    get.setFollowRedirects(true);                                          
                    get.setRequestHeader("Mariner-Application", "prerenderer");
                    get.setRequestHeader("Mariner-DeviceName", deviceName);
                    
                    int iGetResultCode = httpClient.executeMethod(get);
                    if(iGetResultCode != 200) {
                        // TODO: should be localized
                        throw new IOException("Got response code " + iGetResultCode + " for a request for "+ p.getRemoteUri()); 
                    }
                    
                    InputStream is = get.getResponseBodyAsStream();
                    
                    File localFile = new File(deviceFile, p.getLocalUri());
                    localFile.getParentFile().mkdirs();
                    OutputStream os = new FileOutputStream(localFile);
                    
                    IOUtils.copy(is, os);                              
                    os.close();
                } finally {
                    get.releaseConnection();
                }                
            }            
        } 
        catch (Exception ex) {
            result = ex;
        } 
    }

    /**
     * @return Returns the result.
     */
    public Exception getResult() {
        return result;
    }
}
