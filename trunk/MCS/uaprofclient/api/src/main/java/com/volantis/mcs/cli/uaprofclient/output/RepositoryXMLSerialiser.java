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
package com.volantis.mcs.cli.uaprofclient.output;

import com.volantis.mcs.repository.xml.XMLRepositoryConstants;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * A profile serialiser which writes to MCS Repository XML format.
 * <p>
 * NOTE: This doesn't bother with any quoting or anything like that so the
 * input values better be plain text ;-).
 */ 
public class RepositoryXMLSerialiser implements ProfileSerialiser {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The JDK 1.4 Logger object to use.
     */ 
    private final static Logger logger = Logger.getLogger(
            "com.volantis.mcs.cli.uaprofclient.RepositoryXMLSerialiser");

    private static final int MAX_VALUE_LENGTH = 1024;
    
    private static final String DEFAULT_REVISION = "-1";

    private String device;
    
    private String fallbackDevice;

    private PrintWriter out;

    public RepositoryXMLSerialiser(String device, String fallbackDevice, 
            PrintWriter out) {
        this.device = device;
        this.fallbackDevice = fallbackDevice;
        this.out = out;
    }

    public void serialise(Map outputMap) {
        
        List keys = new ArrayList(outputMap.keySet());

        // Sort alphabetically.
        // Jonesy wanted this so we separate the UAProf.* attributes from the
        // existing mariner attributes.
        Collections.sort(keys);

        // Then dump out the sorted attributes in MCS XML format.
        String buf = "";

        // First dump the fixed start.
        buf +=
                "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<!DOCTYPE mariner " +
                        "PUBLIC '" + XMLRepositoryConstants.
                            TRITON_DTD_PUBLIC_ID + "' " +
                        "'" + XMLRepositoryConstants.TRITON_DTD_SYSTEM_ID + 
                    "'>\n" + 
                "<!-- Created by Mariner UAProf Client -->\n" +
                "<mariner>\n" +
                "  <devices>\n" +
                "    <device name='" + quoteAttribute(device) + "' fallback='" + 
                                quoteAttribute(fallbackDevice) + "'>\n";

        // Add the fixed things we always need.
        buf += 
                "      <attribute name='fallback' value='" + 
                                quoteAttribute(fallbackDevice) + "' " + 
                            "revision='" + DEFAULT_REVISION + "'/>\n" +
                "      <attribute name='entrytype' value='real_device' " +
                            "revision='" + DEFAULT_REVISION + "'/>\n" + 
                "\n";

        // Then all the attributes.
        for (Iterator attributes = keys.iterator();
             attributes.hasNext();) {

            // todo: figure out and check the max name length.
            String name = (String) attributes.next();
            name = quoteAttribute(name);
            
            String value = (String) outputMap.get(name);
            if (value.length() > MAX_VALUE_LENGTH) {
                logger.warning("MCS Attribute " + name + " has a length > " + 
                        MAX_VALUE_LENGTH + " (" + value + ")");
            }
            value = quoteAttribute(value);
            
            buf +=              
                "      <attribute name='" + name + "' " +
                                    "value='" + value + "' " +
                                    "revision='" + DEFAULT_REVISION + "'/>\n";
        }
        
        // First dump the fixed end.
        buf +=
                "    </device>\n" +
                "  </devices>\n" +
                "</mariner>\n";
        
        out.write(buf);
        out.flush();
    }

    private String quoteAttribute(String value) {
        value = value.replaceAll("'", "&apos;");
        return value;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Jun-04	4640/4	geoff	VBM:2004060402 UA Prof tool : cannot build uaprof tool

 09-Oct-03	1461/9	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (review comments)

 08-Oct-03	1461/7	geoff	VBM:2003092501 Create CLI tool to translate UAProf data files into MCS xml import file (final version)

 ===========================================================================
*/
