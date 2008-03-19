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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.marshaller;

import com.volantis.synergetics.reporting.config.DatasourceConfiguration;

import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

/**
 * Default datasource configuration marshaller.
 */
public class DefaultDatasourceMarshaller extends DatasourceMarshaller {

    /**
     * Name tag.
     */
    private static final String NAME_TAG_NAME = "name";
    
    // javadoc unnecessary
    public DefaultDatasourceMarshaller() {
        super();
    }
    
    // javadoc unnecessary
    public DefaultDatasourceMarshaller(String uri, int index, 
            String name) {
        super(uri, index, name);
    }    
    
    // javadoc inherited
    protected void marshalName(int index, MarshallingContext context,
            String name) throws JiBXException {
        context.startTag(index, NAME_TAG_NAME);
        context.content(name);
        context.endTag(index, NAME_TAG_NAME);        
    }
    
    // javadoc inherited
    protected void unmarshalName(UnmarshallingContext context,
            DatasourceConfiguration datasource) throws JiBXException {
        context.toStart();
        context.parsePastStartTag(context.getNamespace(), NAME_TAG_NAME);
        datasource.setName(context.getText().trim());
        context.toEnd();
        context.parsePastEndTag(context.getNamespace(), NAME_TAG_NAME);        
    }
}
