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

/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008. 
 * ---------------------------------------------------------------------------
*/

package com.volantis.mcs.xdime.xhtml2.meta.property;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.DataType;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.StringContentProcessor;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MetaData;
import com.volantis.mcs.protocols.MetaAttributes;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;

/**
 * A property handler for meta tags used only by search engines like Google, Yahoo, etc.
 * It renders description, keywords and author meta tags in head 
 */
public class SearchEngineMetaPropertyHandler extends AbstractMetaPropertyHandler {

    public static final String AUTHOR_HTML_META = "author";    


    protected void checkContent(Object content, XDIMEContextInternal context) throws XDIMEException {
        // check the right type
        checkContentType(content, String.class);
    }

    // javadoc inherited
    public DataType getDefaultDataType() {
        return StringContentProcessor.STRING_TYPE;
    }

    // javadoc inherited
    protected boolean hasPageScope() {
        return true;
    }

    // javadoc inherited
    public void process(Object content, XDIMEContextInternal context,
                        String id, String propertyName) throws XDIMEException {

        super.process(content, context, id, propertyName);

        MarinerPageContext ctx = ContextInternals.getMarinerPageContext(
            context.getInitialRequestContext());

        MetaData metaData = context.getPageMetaData();
        MetaAttributes attributes = new MetaAttributes();

        //add all search engine meta tags i.e. description, keywords, author        
        if(MetaPropertyHandlerFactory.DESCRIPTION.equals(propertyName)
                &&  DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE
                .equals(ctx.getDevicePolicyValue(DevicePolicyConstants.X_ELEMENT_SUPPORTS_META_DESCRIPTION))) {

            attributes.setName(MetaPropertyHandlerFactory.DESCRIPTION);
            attributes.setContent((String) metaData.getPropertyValue(MetaPropertyHandlerFactory.DESCRIPTION));
            ctx.getProtocol().writeMeta(attributes);            
        }    

        if(MetaPropertyHandlerFactory.KEYWORDS.equals(propertyName)
                &&  DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE
                .equals(ctx.getDevicePolicyValue(DevicePolicyConstants.X_ELEMENT_SUPPORTS_META_KEYWORDS))) {

            attributes.setName(MetaPropertyHandlerFactory.KEYWORDS);
            String value = null;
            if(metaData.getPropertyValue(MetaPropertyHandlerFactory.KEYWORDS) != null) {
                value = normalize((String) metaData.getPropertyValue(MetaPropertyHandlerFactory.KEYWORDS));
            }
            attributes.setContent(value);
            ctx.getProtocol().writeMeta(attributes);
        }            

        if(MetaPropertyHandlerFactory.AUTHOR.equals(propertyName)
                &&  DevicePolicyConstants.FULL_SUPPORT_POLICY_VALUE
                .equals(ctx.getDevicePolicyValue(DevicePolicyConstants.X_ELEMENT_SUPPORTS_META_AUTHOR))) {
            
            attributes.setName(AUTHOR_HTML_META);
            attributes.setContent((String) metaData.getPropertyValue(MetaPropertyHandlerFactory.AUTHOR));
            ctx.getProtocol().writeMeta(attributes);
        }
    }

    /**
     * Normalize keywords value, i.e. remove not needed whitespaces 
     * @param input String before normalization
     * @return normalized string
     */
    private String normalize(String input) {

        StringBuffer normalized = new StringBuffer();
        if (input != null || !input.equalsIgnoreCase("")){
            String[] splittArray = input.split("\\,");            
            int i = 0;
            boolean firstKeyword = true;
            while(i < splittArray.length) {
                splittArray[i] = splittArray[i].replaceAll("\\s+"," ");                
                if(!firstKeyword) {
                    normalized.append(",");
                }
                normalized.append(splittArray[i].trim());
                firstKeyword = false;
                i++;
            }
        }
        return normalized.toString();
    }
}
