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
package com.volantis.xml.pipeline.sax.cache;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for cacheKey element
 */
public class CacheKeyRule extends DynamicElementRuleImpl {

    //  javadoc inherited from interface
    public Object startElement(DynamicProcess dynamicProcess,
            ExpandedName element, Attributes attributes) throws SAXException {
        
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        
        CacheProperties properties = 
            (CacheProperties) context.findObject(CacheProperties.class);

        if (properties == null) {
            forwardError(dynamicProcess, "Cache properties can not be found. "
                        + "Unexpected cacheKey element");
        } else {
            String key = attributes.getValue("value");
            properties.addCacheKey(key);
        }        
        return null;    
    }

    //  javadoc inherited from interface
    public void endElement(DynamicProcess dynamicProcess, 
            ExpandedName element, Object object) throws SAXException {
        // nothing to do
    }
}
