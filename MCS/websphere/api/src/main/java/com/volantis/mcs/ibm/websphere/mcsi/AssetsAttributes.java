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
package com.volantis.mcs.ibm.websphere.mcsi;

/**
 * Attribute values for the assets IAPI element.
 */
public class AssetsAttributes extends MCSIAttributes {


    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    /**
     * The baseUrl attribute
     */ 
    private String baseUrl;
    
    /**
     * Get the baseUrl attribute
     * @return the baseUrl attribute
     */ 
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set the baseUrl attribute
     * @param baseUrl the baseUrl attribute
     */ 
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    // Javadoc inherited
    public void reset() {
        baseUrl = null;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/1	ianw	VBM:2004090605 New Build system

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/2	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
