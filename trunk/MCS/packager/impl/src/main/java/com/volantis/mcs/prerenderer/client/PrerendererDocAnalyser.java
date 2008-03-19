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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Class for parsing application descriptor for list of pages to prerendering process,
 * parse HTTP response with XML in body and also create xml document with list of pages to post to control prerenderer server   
 */
public class PrerendererDocAnalyser {
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(PrerendererDocAnalyser.class);        
    
    
    private SAXBuilder builder = new SAXBuilder();

    /** 
     * prefix URL from application descriptor file
     * This is prefix from top sets of pages defined in descriptor.
     * 
     * TODO - in next realise of prerenderer prefix should be supported for each pages section form decsriptor
     * not only for top level as so far
     */ 
    private String prefix;    
    
    /**
     * Parse application descriptor file and return list of page's path to request
     * Used in PrerendererApp or in PrerendererAnt task 
     * 
     * @param inputFile application descriptor file
     * @param validate if descriptor should be validate by schema
     * @return Collection of pages  
     * @throws PackagerPrerendererException 
     */
    public List preparePagesListCollection(File inputFile, boolean validate) throws PackagerPrerendererException {
        
        List pagesCollection = new ArrayList(); 
        Document inputDoc = null;

        if(validate) {
            builder.setValidation(true);
            builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            builder.setFeature("http://apache.org/xml/features/validation/schema",  true);                            
        }        
                
        try {
            inputDoc = builder.build(inputFile);                                    
        } catch (JDOMException e) {
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-descriptor-parse-error", new Object[]{inputFile.getName(), e.getMessage()}), e);            
        } catch (IOException e) {
            throw new PackagerPrerendererException(exceptionLocalizer.format("packager-descriptor-cannot-access", new Object[]{inputFile.getName()}), e);
        }
           
        // indicate parser not to validate next XML source 
        builder.setValidation(false);
        builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
        builder.setFeature("http://apache.org/xml/features/validation/schema",  false);                                                        
        
        Element inputRoot = inputDoc.getRootElement();
        List pagesList = inputRoot.getChildren("pages");
        Iterator pagesIt = pagesList.iterator(); 
        
        while(pagesIt.hasNext()) {
            Element pages = (Element) pagesIt.next();
            prefix = pages.getAttributeValue("context");   
            List pageList = pages.getChildren();
            Iterator pageIt = pageList.iterator();            
            while(pageIt.hasNext()) {
                String url = prefix + ((Element) pageIt.next()).getText(); 
                pagesCollection.add(url);
            }
        }                        
        return pagesCollection;        
    }

    /**
     * Create Document with list of pages from descriptor
     * Content of that document will be send to prerenderer control server  
     * @param baseURL baseURL
     * @param pagesList list of full URL to pages to prerendering process from 
     * aplication descriptor
     * @param prefixPath prefix to pages from collection
     * @return Return list pages element with URL inside as string in Document object
     * @throws PackagerPrerendererException if url for page is not valid and should be correct in application descriptor file 
     */
    public Document preparePagesListDocument(List pagesList, String baseURL, String prefixPath) throws PackagerPrerendererException {

        URL fullURL = null;        
        Document doc = new Document();
        Element root = new Element("pages");        
        
        if(baseURL != null) {
            root.setAttribute("base", baseURL);            
        }        
        
        root.setAttribute("prefix-path", prefixPath);                    
        
        doc.setRootElement(root);
        Iterator pageIt = pagesList.iterator();         
        while(pageIt.hasNext()) {
            Element page = new Element("page");
            String url = (String) pageIt.next();
            try {
                fullURL = new URL(url);
            } catch (MalformedURLException e) {
                throw new PackagerPrerendererException(exceptionLocalizer.format("malformed-url", new Object[]{url}), e);
            }
            String remoteURL = fullURL.getFile();
            page.setText(remoteURL);
            root.addContent(page);
        }        
        return doc;        
    }

    /**
     * Create and return Map of pages with their local and remote URL
     * 
     * @param pagesList list of full URL to pages to prerendering process from 
     * aplication descriptor
     * @param is response from control prerenderer server with mapping (remoteURL, localURL) in xml body 
     * @return map Map of local and remote page's URL
     */
    public Map preparePagesMapping(List pageList, InputStream is) {        
        
        Map map = new Hashtable();
        Document responseDoc = null;
                
        try {
            responseDoc = builder.build(is);
            is.close();
        } catch (JDOMException e) {
            throw new RuntimeException("Error in parse xml response with local pages list received from prerenderer control server", e);
        } catch (IOException e) {
            throw new RuntimeException("Error in parse xml response with local pages list received from prerenderer control server", e);
        }
                      
        Element root = responseDoc.getRootElement();
        List localPageList = root.getChildren("page"); 
        for(int i = 0; i < pageList.size(); i++) {            
            map.put((String) pageList.get(i), ((Element)localPageList.get(i)).getText());
        }
      
        if(pageList.size() != localPageList.size()) {
            throw new IllegalStateException("Count of response pages not equals to sent count pages");            
        }                
        return map;
    }


    /**
     * Create and return Map of resources with their local and remote URL
     * 
     * @param is response from control prerenderer server with mapping (remoteURL, localURL) in xml body
     * @param prefixURL  
     * @return map Map of local and remote page's URL
     */
    public Map prepareResourcesMapping(InputStream is, URL prefixURL) {
        
        Map map = new Hashtable();        
        Document responseDoc = null;
        
        try {            
            responseDoc = builder.build(is);
            is.close();
        } catch (JDOMException e) {
            throw new RuntimeException("Error in parse xml response with local pages list received from prerenderer control server", e);
        } catch (IOException e) {
            throw new RuntimeException("Error in parse xml response with local pages list received from prerenderer control server", e);
        }

        Element root = responseDoc.getRootElement();
        List localResourceList = root.getChildren("resource"); 
        Iterator resourceIt = localResourceList.iterator(); 
        while(resourceIt.hasNext()) {
          Element resource = (Element) resourceIt.next();
          URI prefixURI;
          try {
            prefixURI = new URI(prefixURL.toString());
          } catch (URISyntaxException e) {
              throw new RuntimeException("remote URL for resource is not valid URI", e);
          }
          URI remoteURI = prefixURI.resolve(resource.getChild("remote").getText());          
          map.put(remoteURI.toString(), resource.getChild("local").getText());            
        }        
        return map;
    }

    /**
     * Return prefix for files read from file description
     * TODO - it only return prefix from top section pages, 
     * if in the future descriptor will be allow to has more sections of pages
     * the implementation of prefix should be changed  
     * 
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

      
}
