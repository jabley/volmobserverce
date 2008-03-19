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
 * $Header: /src/voyager/com/volantis/mcs/runtime/StyleSheetConfiguration.java,v 1.4 2002/12/18 11:22:44 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Mar-02    Paul            VBM:2002030607 - Created to encapsulate the
 *                              current configuration of the style sheet
 *                              generation to make it easy to access from the
 *                              different parts of the code which need to.
 * 13-Mar-02    Paul            VBM:2002030607 - Stopped paramString from
 *                              throwing a null pointer exception if the
 *                              baseURL has not been set.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 16-Dec-02    Phil W-S        VBM:2002121001 - Add attribute to hold the CSS
 *                              max-age cache control header configuration
 *                              value.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.utilities.MarinerURL;

/**
 * Encapsulates the current configuration of the style sheet generation
 * to make it easy to access from the different parts of the code which need
 * to.
 */
public class StyleSheetConfiguration {

    /**
     * This value indicates that the preferred location for styles is inline
     * in the page.
     */
    public static final int INLINE = 0;

    /**
     * This value indicates that the preferred location for styles is in an
     * external style sheet.
     */
    public static final int EXTERNAL = 1;

    /**
     * This value indicates that the cache control max age value has not been
     * set.
     */
    public static final int NO_MAX_AGE = -1;

    /**
     * The base URL which is used to identify the location of the servlet which
     * serves external style sheets.
     */
    private MarinerURL baseURL;

    /**
     * The preferred location for the styles, must be one of
     * <ul>
     * <li>INLINE</li>
     * <li>EXTERNAL</li>
     * </ul>
     * Defaults to INLINE if not set.
     */
    private int preferredLocation;

    /**
     * Flag which indicates whether it is possible to generate external
     * style sheets.
     */
    private boolean supportsExternal;

    /**
     * If set to a non-zero (positive) value, this indicates the number of
     * seconds for which a CSS file should be cached, by the device retrieving
     * the stylesheet, before an attempt should be made to fetch it again.  A
     * value of -1 indicates an unlimited expiry on cache entries.
     */
    private int pageLevelMaxAge = NO_MAX_AGE;

    /**
     * Constant value that indicates that the session id must be part of the URL.
     */
    public static final String INCLUDE_SESSION_ID = "include-id-in-url";

    /**
     * The css session type. Supported values are:
     * <ul>
     *   <li>include-id-in-url</li>
     * </ul>
     *
     * If this value is set to 'include-id-in-url' then the url will be
     * rewritten to include the sesssion id, otherwise it won't.
     */
    private String cssSessionType = null;

    /**
     * Set the value of the base URL property.
     * @param baseURL The new value of the base URL property.
     */
    public void setBaseURL(MarinerURL baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Get the value of the base URL property.
     * @return The value of the base URL property.
     */
    public MarinerURL getBaseURL() {
        return baseURL;
    }

    /**
     * Set the value of the supports external property.
     * @param supportsExternal The new value of the supports external property.
     */
    public void setSupportsExternal(boolean supportsExternal) {
        this.supportsExternal = supportsExternal;
    }

    /**
     * Get the value of the supports external property.
     * @return The value of the supports external property.
     */
    public boolean getSupportsExternal() {
        return supportsExternal;
    }

    /**
     * Set the value of the preferred location property.
     * @param preferredLocation The new value of the preferred location property.
     */
    public void setPreferredLocation(int preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    /**
     * Get the value of the preferred location property.
     * @return The value of the preferred location property.
     */
    public int getPreferredLocation() {
        return preferredLocation;
    }    

    /**
     * Set the named attribute's value. A value of NO_MAX_AGE indicates "unset".
     *
     * @param pageLevelMaxAge the new value for the attribute.
     */
    public void setPageLevelMaxAge(int pageLevelMaxAge) {
        this.pageLevelMaxAge = pageLevelMaxAge;
    }

    /**
     * Return the value of the named attribute. A value of NO_MAX_AGE indicates
     * "unset".
     *
     * @return the value of the named attribute.
     */
    public int getPageLevelMaxAge() {
        return pageLevelMaxAge;
    }

    // Javadoc inherited from super class.
    public String toString() {
        return getClass().getName()
                + "@" + Integer.toHexString(System.identityHashCode(this))
                + " [" + paramString() + "]";
    }

    /**
     * Return a String representation of the state of the object.
     * @return The String representation of the state of the object.
     */
    protected String paramString() {
        return "baseURL="
                + (baseURL == null ? null : baseURL.getExternalForm())
                + ", supportsExternal=" + supportsExternal
                + ", pageLevelMaxAge=" + pageLevelMaxAge
                + ", cssSessionType=" + cssSessionType;
    }


    // javadoc unecessary
    public String getCssSessionType() {
        return cssSessionType;
    }

    // javadoc unecessary
    public void setCssSessionType(String cssSessionType) {
        this.cssSessionType = cssSessionType;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Jul-04	4702/1	matthew	VBM:2004061402 merge problems

 24-Jun-04	4726/1	claire	VBM:2004060803 Implementation of internal style sheet caching

 ===========================================================================
*/
