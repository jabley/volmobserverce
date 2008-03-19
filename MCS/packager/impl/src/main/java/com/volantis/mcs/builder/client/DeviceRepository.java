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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Web service client which get policy values from DRWS service
 * It works only in http protocol (not in https) 
 */
public class DeviceRepository {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(DeviceRepository.class);        
    
    /**
     * prefix URL to DRWS (without request_query)
     * It has to match to http://[username]:[password]@[hostname]:[portnumber]/[context_root]
     */
    private String drwsURL;
    
    /**
     * apache http client
     */  
    private HttpClient httpClient;
    
    /**
     * docAnalyser contains all needed properties from application descriptor
     */  
    private BuilderDocAnalyser docAnalyser; 
        
    DeviceRepository() {
        // create http client 
        httpClient = new HttpClient();
        
        DocAnalyserFactory docAnalyserFactory = DocAnalyserFactory.getDefaultInstance();
        docAnalyser = docAnalyserFactory.createBuilderDocAnalyser();        
    }
    
    /**
     * Get policy value from DRWS for specified deviceName and specified policy name
     * @param deviceName  
     * @return policy value
     * @throws HttpException
     * @throws IOException
     */ 
    public String getPolicyValue(String deviceName, String policy) 
        throws PackagerBuilderException {
                
        String strUrl = drwsURL + "/device/policy/value/" + deviceName + "/" + policy;
                
        GetMethod get = new GetMethod(strUrl);
        get.setFollowRedirects(true);

        // set credentials
        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            // TODO: IllegalStateException does not seem to be the right exception here.
            // also the message should be localized
            throw new IllegalStateException("Invalid URL to Device Repository web service");
        }
        
        // http protocol
        if(url.getProtocol().equals("http")) {
            String strAuth = url.getUserInfo();
            if(strAuth != null) {
                String [] loginPassword = strAuth.split("\\:");
                if(loginPassword.length == 2) {
                    Credentials defaultcreds = new UsernamePasswordCredentials(loginPassword[0], loginPassword[1]);
                    httpClient.getState().setCredentials(AuthScope.ANY, defaultcreds);
                } else {
                    throw new PackagerBuilderException(exceptionLocalizer.format("packager-drws-autorization-incorrect"));
                }
            }                
        } else {
            throw new IllegalStateException("In packager only http protocol is supported in DRWS service");                
        }
        
        int iGetResultCode = 0;
        
        try {
            iGetResultCode = httpClient.executeMethod(get);
        } catch (IOException e) {
            // TODO: IllegalStateException does not seem to be the right exception here.
            // also the message should be localized
            throw new IllegalStateException("Failed to execute request to " + url.toString());
        }
        
        if(iGetResultCode != 200) {
            // TODO: IllegalStateException does not seem to be the right exception here.
            // also the message should be localized
            throw new IllegalStateException("Got response code " + iGetResultCode + " for a request for " + url);
        }
        
        // input stream for response content
        InputStream is = null;
        try {
            is = get.getResponseBodyAsStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // get and return policy value for xml response from DRWS    
        return docAnalyser.getPolicyValue(is);                
    }

    /**
     * @return Returns the drwsURL.
     */
    public String getDrwsURL() {
        return drwsURL;
    }

    /**
     * @param drwsURL The drwsURL to set.
     */
    public void setDrwsURL(String drwsURL) {
        this.drwsURL = drwsURL;
    }        
}
