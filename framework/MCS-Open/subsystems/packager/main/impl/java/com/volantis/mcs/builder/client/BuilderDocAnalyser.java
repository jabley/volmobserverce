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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
 * Parse application descriptor in order to get property values 
 * and also parse xml body response from DRWS  
 */
public class BuilderDocAnalyser {

    private SAXBuilder builder = new SAXBuilder();
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(BuilderDocAnalyser.class);    
    
    /**
     * Map consists of set of individual parameters set in application description
     */ 
    private Map properties;    
        
    /**
     * Analyse xml response from DRWS. Return policy value after response in request for one policy value.
     * 
     * @param stream response from DRWS in request for one policy value
     * @return policy value
     */
    public String getPolicyValue(InputStream stream) {
        
        Document doc = null;
        try {
            doc = builder.build(stream);
            stream.close();
        } catch (JDOMException e) {
            throw new RuntimeException("Error in parse xml response received from DRWS web service! Response incorect!", e);
        } catch (IOException e) {
            throw new RuntimeException("Error in parse xml response received from DRWS web service! Response incorect!", e);
        }
            
        Element root = doc.getRootElement();        
        Element policy = root.getChild("policy", root.getNamespace());        
        return policy.getChildText("string", root.getNamespace());
    }


    /**
     * Parse application descriptor file, 
     * fill internal Map with individual parameters  
     * 
     * @param descriptor path to file descriptor 
     * @param validate if application descriptor should be validate by schema
     * @throws PackagerBuilderException 
     */
    public void prepareDescriptorParams(File descriptor, boolean validate) throws PackagerBuilderException {
        
        Document doc = null;
        if(validate) {
            builder.setValidation(true);
            builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            builder.setFeature("http://apache.org/xml/features/validation/schema",  true);                            
        }        
        
        properties = new HashMap();
        
        try {
            doc = builder.build(descriptor);                        
        } catch (JDOMException e) {
            throw new PackagerBuilderException(exceptionLocalizer.format("packager-descriptor-parse-error"), e);            
        } catch (IOException e) {
            throw new PackagerBuilderException(exceptionLocalizer.format("packager-descriptor-cannot-access"), e);
        }
                               
        // indicate parser not to validate next XML source 
        builder.setValidation(false);
        builder.setFeature("http://apache.org/xml/features/validation/schema-full-checking", false);
        builder.setFeature("http://apache.org/xml/features/validation/schema",  false);                                                

        Element root = doc.getRootElement();
        
        properties.put("display-name", root.getChildText("display-name"));
        properties.put("description", root.getChildText("description"));
        properties.put("label", root.getChild("shortcut").getChildText("label"));
        properties.put("icon", root.getChild("shortcut").getChildText("icon"));
        properties.put("major", root.getChild("version").getChildText("major"));
        properties.put("minor", root.getChild("version").getChildText("minor"));
        properties.put("revision", root.getChild("version").getChildText("revision"));

        Element uids = root.getChild("uids");
        List uidList = uids.getChildren();
        
        Iterator it = uidList.iterator();
        while(it.hasNext()) {
            Element uid = (Element) it.next();
            properties.put(uid.getChildText("format"), uid.getChildText("value"));            
        }            
    }

    /**
     * Get application name defined in descriptor
     * @return application name
     */
    public String getAppName() {
        return (String) properties.get("display-name");
    }

    /**
     * Get major version defined in descriptor
     * @return major version
     */
    public String getMajor() {
        return (String) properties.get("major");
    }

    /**
     * Get minor version defined in descriptor
     * @return minor version
     */
    public String getMinor() {
        return (String) properties.get("minor");
    }

    /**
     * Get revision version defined in descriptor
     * @return revision version
     */
    public String getRevision() {
        return (String) properties.get("revision");
    }

    /**
     * Get application description defined in descriptor
     * @return application description
     */
    public String getAppDescription() {
        return (String) properties.get("description");
    }
    
    /**
     * Get application label defined in descriptor
     * @return label
     */
    public String getLabel() {
        return (String) properties.get("label");
    }

    /**
     * Get icon defined in descriptor
     * @return icon
     */
    public String getIcon() {
        return (String) properties.get("icon");
    }
    
    /**
     * Get application UID defined in descriptor
     * @return application UID
     */
    public String getAppUID(String platform) {
        return (String) properties.get(platform);
    }

    
    
}
