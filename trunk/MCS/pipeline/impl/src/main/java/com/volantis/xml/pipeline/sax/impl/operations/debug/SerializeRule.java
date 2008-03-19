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
package com.volantis.xml.pipeline.sax.impl.operations.debug;

import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicElementRule;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Rule for the {@link SerializeProcess}
 */
public class SerializeRule extends AbstractAddProcessRule {

    /**
     * The default instance.
     */
    private static final DynamicElementRule DEFAULT_INSTANCE =
            new SerializeRule();

    /**
     * Get the default instance.
     *
     * @return The default instance.
     */
    public static DynamicElementRule getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }


    /**
     * Constant for the serialize elements "active" attribute
     */
    private static final String ACTIVE_ATTRIBUTE = "active";

    /**
     * Constant for the serialize elements "fileSuffix" attribute
     */
    private static final String FILE_SUFFIX_ATTRIBUTE = "fileSuffix";

    // javadoc inherited
    protected XMLProcess createProcess(DynamicProcess dynamicProcess,
                                       ExpandedName elementName,
                                       Attributes attributes)
                throws SAXException {
        XMLProcess process = null;
        // we only add a process if the "active" attribute is set to true
        // and a DebugOutputFilesPrefix has been set on the pipeline context.
        XMLPipelineContext context =
                    dynamicProcess.getPipeline().getPipelineContext();
        String suffix = attributes.getValue(FILE_SUFFIX_ATTRIBUTE);
        String logFilePath =
                SerializeRule.calculateLogFilePath(context, suffix);
        boolean isActive = Boolean.valueOf(attributes.getValue(
                    ACTIVE_ATTRIBUTE)).booleanValue();
        
        // only add a serialize process if the active attribute is set to
        // "true" and the
        if(isActive && logFilePath != null) {
            SerializeProcess serializer = new SerializeProcess();
            serializer.setDebugOutputFilePath(logFilePath);
            process = new TryCatchFinallyManagerProcess(serializer);
        }
        return process;
    }

    /**
     * Calculates the location that the SerializeProcess will write to.
     * @param context the pipeline context
     * @param suffix the suffix to use for the debug file.
     *
     * @return the location as a String or null if the process should not write
     * out it's output.
     */
    private static String calculateLogFilePath(XMLPipelineContext context,
                                                 String suffix) {
        String prefix = context.getDebugOutputFilesPrefix();
        // if the prefix or suffix is null then debugging should be disabled
        String logFilePath = null;
        if (prefix != null && suffix != null) {
            // no point using a StringBuffer as only appending two Strings
            logFilePath = prefix + suffix;
        }
        return logFilePath;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-May-05	8151/1	matthew	VBM:2005051002 add JSP Tag support for Serialize pipeline operation

 01-Apr-05	6798/1	doug	VBM:2005012605 Added SerializeProcess to the Pipeline

 ===========================================================================
*/
