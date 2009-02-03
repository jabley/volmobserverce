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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.impl.drivers.webservice.wsif.soap;

import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.ser.SimpleSerializer;
import org.apache.axis.utils.Messages;
import org.xml.sax.Attributes;

import javax.xml.namespace.QName;
import java.io.IOException;

public class PipelineAXISStringSerializer extends SimpleSerializer {

    public PipelineAXISStringSerializer(Class javaType, QName xmlType) {
        super(javaType, xmlType);
    }

    public PipelineAXISStringSerializer(Class javaType, QName xmlType,
                                        TypeDesc typeDesc) {
        super(javaType, xmlType, typeDesc);
    }

    /**
     * Override serialize to call writeString instead of writeSafeString.
     * @param name
     * @param attributes
     * @param value
     * @param context
     * @throws IOException
     */
    public void serialize(QName name, Attributes attributes,
                          Object value, SerializationContext context)
            throws IOException {

        if (value != null && value.getClass() == java.lang.Object.class) {
            throw new IOException(Messages.getMessage("cantSerialize02"));
        }

        context.startElement(name, attributes);
        if (value != null) {
            context.writeString(getValueAsString(value, context));
        }
        context.endElement();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 29-Jun-03	98/1	allan	VBM:2003022822 Added some tests for RequestOperationProcess - could do with more though. Added some possibly final touches

 ===========================================================================
*/
