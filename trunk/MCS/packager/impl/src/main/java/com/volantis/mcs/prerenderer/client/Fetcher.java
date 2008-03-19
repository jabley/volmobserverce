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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;

/**
 * Create new requests to MCS server in new threads  
 */

public class Fetcher {

    // Default count of concurent run threads
    // TODO: It seems, that there's a bug in MCS.
    // In case of pages which contain forms, MCS fails
    // when asked for such pages is multiple threads.
    // That's why this constant is set to 1.
    // As soon as it's fixed, this constant may be reverted to 8.
    //private static int THREAD_COUNT = 8; 
    private static int THREAD_COUNT = 1; 
    
    private HttpClient httpClient;
    private Map map;
    private File deviceFile;
    private String deviceName;    
                
    Fetcher(Map map, HttpClient client, File deviceFile, String deviceName){
        this.httpClient = client;
        this.map = map;
        this.deviceName = deviceName;
        this.deviceFile = deviceFile;
    }   
    
    // download pages or resources from MCS
    public void fetch() {
        Queue queue = new Queue(map); 
        ArrayList threads = new ArrayList();
        for(int i= 0; i < THREAD_COUNT; i++) {
            FetcherThread t = new FetcherThread(queue, httpClient, deviceFile, deviceName);
            t.setDaemon(true);
            t.start();            
            threads.add(t);            
        }        
        Exception threadsEx;
        List threadsList = new ArrayList();           
        for(int i= 0; i < THREAD_COUNT; i++) {
            
            try {
                ((FetcherThread)threads.get(i)).join();
                threadsEx = ((FetcherThread)threads.get(i)).getResult();
            } catch (InterruptedException e) {
                threadsEx = e;                
            }
            if(threadsEx != null) {
                threadsList.add(threadsEx);
            }    
        }
        if(! threadsList.isEmpty()) {
            throw new RuntimeException((Exception)threadsList.get(0));
        }        
    }    
}
