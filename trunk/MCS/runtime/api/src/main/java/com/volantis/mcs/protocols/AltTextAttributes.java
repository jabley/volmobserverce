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
 * $Header: /src/voyager/com/volantis/mcs/protocols/AltTextAttributes.java,v 1.3 2003/04/25 12:18:46 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Nov-02    Geoff           VBM:2002111504 - Created. 
 * 15-Apr-03    Byron           VBM:2003040302 - Added a default public
 *                              constructor.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.papi.BlockAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * Attributes for the {@link VolantisProtocol#writeAltText} method. These
 * are basically the style info of the original tag, along with the alt text
 * and the id of the component to fall back from to find text.
 */
public class AltTextAttributes
        extends MCSAttributes {

    /**
     * The alt text of the tag we are to write.
     */
    private TextAssetReference altText;

    /**
     * Default public constructor required for integration testing (reflection)
     */
    public AltTextAttributes() {
    }

    /**
     * Create an instance from a PAPI attributes object. This copies (some of)
     * the required attributes from the PAPI attribute to the instance.
     *
     * @param papiAttributes the PAPI tag attributes to copy.
     */
    public AltTextAttributes(BlockAttributes papiAttributes) {
        // We copy the tag name so that the style of the fallback is
        // faithful to the original - yes it's a hack!
        setTagName(papiAttributes.getElementName());
        // Not sure why we need these, but the old code did this... :-)
        setId(papiAttributes.getId());
        setTitle(papiAttributes.getTitle());
        // @TODO: this should have the ability to get the alt text direct from
        // the the PAPI attrs, but there is currently no common interface on 
        // the PAPI attrs for obtaining Alt Text, so it requires a non-public 
        // change to the marlin schema - Paul fell at the last hurdle :-).
    }

    /**
     * Returns the alt text, or null if there was none.
     *
     * @return alt text, or null if there was none
     */
    public TextAssetReference getAltText() {
        return altText;
    }

    /**
     * Sets the alt text.
     *
     * @param altText the alt text
     */
    public void setAltText(TextAssetReference altText) {
        this.altText = altText;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/1	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03	1751/1	philws	VBM:2003031710 Permit image alt text to be component reference

 ===========================================================================
*/
