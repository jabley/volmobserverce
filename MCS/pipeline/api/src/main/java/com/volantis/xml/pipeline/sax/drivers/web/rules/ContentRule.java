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

package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.drivers.web.Content;
import com.volantis.xml.pipeline.sax.drivers.web.ContentAction;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.DynamicElementRuleImpl;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;

/**
 * Rule for webd:content element.
 */
public class ContentRule
        extends DynamicElementRuleImpl {

    // Javadoc inherited.
    public Object startElement(
            DynamicProcess dynamicProcess, ExpandedName element,
            Attributes attributes) throws SAXException {

        String action = attributes.getValue("action");
        String type = attributes.getValue("type");

        Map map = null;
        if ((action != null) && (type != null)) {

            // Add the content to the map in the context.
            XMLPipelineContext context = dynamicProcess.getPipelineContext();
            Content content = new Content(type,
                    ContentAction.getContentAction(action));

            map = getContentMap(context);
            map.put(type, content);

        } else {
            forwardError(dynamicProcess,
                    "Content tag should have 'action' and 'type' " +
                    "parameter set. Values are: action='" + action +
                    "', type='" + type + "'");
        }

        return map;
    }

    // Javadoc inherited.
    public void endElement(
            DynamicProcess dynamicProcess, ExpandedName element, Object object)
            throws SAXException {
        // Nothing to do.
    }

    /**
     * Get the content map from the context, creating it if necessary.
     *
     * @param context The context.
     * @return The map, which may have been created.
     */
    private Map getContentMap(XMLPipelineContext context) {
        final Class key = Content.class;
        Map map = (Map) context.getProperty(key);
        if (map == null) {
            map = new HashMap();
            context.setProperty(key, map, false);
        }
        return map;
    }
}
