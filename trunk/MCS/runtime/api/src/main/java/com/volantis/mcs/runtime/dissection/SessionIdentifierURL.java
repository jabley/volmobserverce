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

package com.volantis.mcs.runtime.dissection;

/**
 * This class contains a url that has been split up into three parts
 * - The prefix before the jsessionid
 * - The jsessionid
 * - The suffix after the jsessionid
 * This class is used by the OptimiserFilter to store jsessionid's in the
 * string table when we generate wmlc.
 */
public class SessionIdentifierURL {

    /**
     * The part of the url before the jsessionid
     */
    private String prefix;

    /**
     * The jsessionid part of the url
     */    
    private String jsessionid;
    
    /**
     * The part of the url after the jsessionid
     */
    private String suffix;

    /**
     * Default constructor 
     */
    public SessionIdentifierURL() {
    }

    /**
     * Returns the jsessionid part of the url
     * @return String
     */
    public String getJsessionid() {
        return jsessionid;
    }

    /**
     * Sets the jsessionid part of the url
     * @param jsessionid
     */
    void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    }

    /**
     * Returns true if we have a jsessionid
     * @return boolean
     */
    public boolean hasJsessionid() {
        return jsessionid != null;
    }

    /**
     * Returns the part of the url before the jsessionid
     * @return String
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the part of the url before the jsessionid
     * @param prefix
     */
    void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the part of the url after the jsessionid
     * @return String
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the part of the url after the jsessionid
     * @param suffix
     */
    void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Returns true if we have a suffix i.e. there is more url after the
     * jsessionid
     * @return boolean
     */
    public boolean hasSuffix() {
        return suffix != null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
