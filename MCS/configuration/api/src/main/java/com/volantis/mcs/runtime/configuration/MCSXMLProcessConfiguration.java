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
 * $Header: $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who         Description
 * -----------  ----------- -------------------------------------------------
 * 22-Apr-2003  Sumit       VBM:2003041502 - Interface that all XML process 
 *                          configurations must implement
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.runtime.configuration;

import com.volantis.xml.pipeline.sax.config.Configuration;

/**
 * Interface that all XML process configurations must implement
 */
public interface MCSXMLProcessConfiguration {
    
    /**
     * All implementors of this interface must return a process configuration 
     * or null if it is not required by the XML process
     * @return the Configuration
     */
    public Configuration getProcessConfiguration();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 04-Aug-03	933/1	doug	VBM:2003080402 Renamed XMLProcessConfiguration interface to Configuration

 ===========================================================================
*/
