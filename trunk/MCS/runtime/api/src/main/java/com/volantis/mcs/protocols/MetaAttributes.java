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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MetaAttributes.java,v 1.8 2002/03/18 12:41:17 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Kula            VBM:2001083120 - forua attribute added, which
 *                              is a WML related attribute use to control
 *                              whether the meta data intended to reach the
 *                              user agent.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

/**
 * MetaAttributes.
 */
public class MetaAttributes
        extends MCSAttributes {

    private String content;
    private String httpEquiv;
    private String lang;
    private String name;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public MetaAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {

        // Set the default tag name, this is the name of the tag which makes
        // the most use of this class.
        setTagName("meta");

        content = null;
        httpEquiv = null;
        lang = null;
        name = null;

    }

    /**
     * Set the content property.
     *
     * @param content The new value of the content property.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the value of the content property.
     *
     * @return The value of the content property.
     */
    public String getContent() {
        return content;
    }

    /**
     * Set the httpEquiv property.
     *
     * @param httpEquiv The new value of the httpEquiv property.
     */
    public void setHttpEquiv(String httpEquiv) {
        this.httpEquiv = httpEquiv;
    }

    /**
     * Get the value of the httpEquiv property.
     *
     * @return The value of the httpEquiv property.
     */
    public String getHttpEquiv() {
        return httpEquiv;
    }

    /**
     * Set the lang property.
     *
     * @param lang The new value of the lang property.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Get the value of the lang property.
     *
     * @return The value of the lang property.
     */
    public String getLang() {
        return lang;
    }

    /**
     * Set the name property.
     *
     * @param name The new value of the name property.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of the name property.
     *
     * @return The value of the name property.
     */
    public String getName() {
        return name;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
