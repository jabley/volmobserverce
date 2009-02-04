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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ScriptAttributes.java,v 1.10 2002/03/18 12:41:17 ianw Exp $
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
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.ScriptAssetReference;

import java.io.File;

public class ScriptAttributes
        extends MCSAttributes {

    private String charSet;
    private String defer;
    private String language;
    private ScriptAssetReference scriptReference;
    private String type;
    private boolean embeddable = false;
    private File scriptFilePath;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public ScriptAttributes() {
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
        setTagName("script");

        charSet = null;
        defer = null;
        language = null;
        scriptReference = null;
        type = null;
        embeddable = false;
    }

    /**
     * Set the charSet property.
     *
     * @param charSet The new value of the charSet property.
     */
    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    /**
     * Get the value of the charSet property.
     *
     * @return The value of the charSet property.
     */
    public String getCharSet() {
        return charSet;
    }

    /**
     * Set the defer property.
     *
     * @param defer The new value of the defer property.
     */
    public void setDefer(String defer) {
        this.defer = defer;
    }

    /**
     * Get the value of the defer property.
     *
     * @return The value of the defer property.
     */
    public String getDefer() {
        return defer;
    }

    /**
     * Set the language property.
     *
     * @param language The new value of the language property.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Get the value of the language property.
     *
     * @return The value of the language property.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the scriptReference property.
     *
     * @param scriptReference The new value of the scriptReference property.
     */
    public void setScriptReference(final ScriptAssetReference scriptReference) {
        this.scriptReference = scriptReference;
    }

    /**
     * Get the value of the src property.
     *
     * @return The value of the src property.
     */
    public ScriptAssetReference getScriptReference() {
        return scriptReference;
    }

    /**
     * Set the type property.
     *
     * @param type The new value of the type property.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the value of the type property.
     *
     * @return The value of the type property.
     */
    public String getType() {
        return type;
    }

    public void setEmbeddable(boolean embeddable) {
        this.embeddable = embeddable;
    }

    public boolean isEmbeddable() {
        return embeddable;
    }

    public void setScriptFilePath(File scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }

    public File getScriptFilePath() {
        return scriptFilePath;
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
