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
 * $Header: /src/voyager/com/volantis/mcs/protocols/PhoneNumberAttributes.java,v 1.1 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Mar-03    Phil W-S        VBM:2002111502 - Created. Required attributes
 *                              class to support the new PhoneNumber PAPI
 *                              element.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * Encapsulate the attributes associated with a phone number link.
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class PhoneNumberAttributes extends AnchorBaseAttributes {

    private TextAssetReference fullNumber = null;

    private String qualifiedFullNumber = null;

    private String defaultContents;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public PhoneNumberAttributes() {
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
        setTagName("a");

        fullNumber = null;
        qualifiedFullNumber = null;
    }

    /**
     * Return the fullNumber property.
     *
     * @return the fullNumber
     */
    public TextAssetReference getFullNumber() {
        return fullNumber;
    }

    /**
     * Set or reset the fullNumber property.
     *
     * @param fullNumber the new value for the fullNumber property
     */
    public void setFullNumber(TextAssetReference fullNumber) {
        this.fullNumber = fullNumber;
    }

    /**
     * Return the qualifiedFullNumber property.
     *
     * @return the qualifiedFullNumber
     */
    public String getQualifiedFullNumber() {
        return qualifiedFullNumber;
    }

    /**
     * Set or reset the qualifiedFullNumber property.
     *
     * @param qualifiedFullNumber the new value for the qualifiedFullNumber
     *                            property
     */
    public void setQualifiedFullNumber(String qualifiedFullNumber) {
        this.qualifiedFullNumber = qualifiedFullNumber;
    }

    public void setDefaultContents(String defaultContents) {
        this.defaultContents = defaultContents;
    }

    public String getDefaultContents() {
        return defaultContents;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
