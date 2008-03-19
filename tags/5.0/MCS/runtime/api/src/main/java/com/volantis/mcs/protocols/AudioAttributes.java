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
 * $Header: /src/voyager/com/volantis/mcs/protocols/AudioAttributes.java,v 1.3 2003/02/04 12:43:43 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 11-Nov-02    Chris W         VBM:2002102403 - Created
 * 11-Dec-02    Mat             VBM:2002112212 - Added URLAssetSuffix
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;


/**
 * This class encapsulates the properties needed by the MMS SMIL audio tag
 */
public class AudioAttributes
        extends MCSAttributes {

    private String src;
    /**
     * The assetURLsuffix attribute of the audio element
     */
    private String assetURLsuffix;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public AudioAttributes() {
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
        setTagName("audio");

        src = null;
    }

    /**
     * Returns the src.
     *
     * @return String
     */
    public String getSrc() {
        return src;
    }

    /**
     * Sets the src.
     *
     * @param src The src to set
     */
    public void setSrc(String src) {
        this.src = src;
    }

    /**
     * Getter for property assetURLsuffix.
     *
     * @return Value of property assetURLsuffix.
     */
    public String getAssetURLSuffix() {
        return assetURLsuffix;
    }

    /**
     * Setter for property assetURLsuffix.
     *
     * @param assetURLsuffix New value of property assetURLsuffix.
     */
    public void setAssetURLSuffix(String assetURLsuffix) {
        this.assetURLsuffix = assetURLsuffix;
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

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 ===========================================================================
*/
