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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

/**
 * Holds the configuration information for management functionality
 */
public class ManagementConfiguration {

    private PageTrackingConfiguration pageTrackingConfiguration;

    /**
     * Set the page traker configuration
     * @param pageTrackingConfiguration
     */
    public void setPageTrackingConfiguration(
            PageTrackingConfiguration pageTrackingConfiguration) {
        this.pageTrackingConfiguration = pageTrackingConfiguration;
    }

    /**
     * Return the PageTrackingConfiguration object
     */
    public PageTrackingConfiguration getPageTrackingConfiguration() {
        return this.pageTrackingConfiguration;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 16-Jun-04	4702/1	matthew	VBM:2004061402 management functionality added

 ===========================================================================
*/
