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
package com.volantis.mcs.xdime.xhtml2.meta.datatype;

import com.volantis.xml.namespace.ImmutableExpandedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory to create content processors based on the qualified name of the
 * requested data type.
 */
public class MetaContentProcessorFactory {

    private static final Map CONTENT_PROCESSORS;

    static {
        CONTENT_PROCESSORS = new HashMap();
        CONTENT_PROCESSORS.put(DOMContentProcessor.EXPANDED_NAME_DOM,
                               DOMContentProcessor.class);
        CONTENT_PROCESSORS.put(StringContentProcessor.EXPANDED_NAME_STRING,
                               StringContentProcessor.class);
        CONTENT_PROCESSORS.put(IntegerContentProcessor.EXPANDED_NAME_INT,
                               IntegerContentProcessor.class);
        CONTENT_PROCESSORS.put(DateTimeContentProcessor.EXPANDED_NAME_DATE_TIME,
                               DateTimeContentProcessor.class);
        CONTENT_PROCESSORS.put(PeriodContentProcessor.EXPANDED_NAME_PERIOD,
                               PeriodContentProcessor.class);
        CONTENT_PROCESSORS.put(RefreshContentProcessor.EXPANDED_NAME_REFRESH,
                               RefreshContentProcessor.class);
    }

    private MetaContentProcessorFactory() {
        // hide it
    }

    /**
     * Returns an instance of the content processor registered for the specified
     * qualified name.
     *
     * Returns null if no content processor is registered for the qname.
     *
     * @param name the expanded name of the data type
     *
     * @return the content processor or null
     */
    public static MetaContentProcessor getProcessor(
            final ImmutableExpandedName name) {

        final Class clazz = (Class) CONTENT_PROCESSORS.get(name);
        final MetaContentProcessor processor;
        if (clazz != null) {
            try {
                processor = (MetaContentProcessor) clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(
                    "Cannot instantiate class: " + clazz.getName());
            }
        } else {
            processor = null;
        }
        return processor;
    }
}
