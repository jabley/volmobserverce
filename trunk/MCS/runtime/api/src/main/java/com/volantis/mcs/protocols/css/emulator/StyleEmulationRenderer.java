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
package com.volantis.mcs.protocols.css.emulator;

import com.volantis.mcs.dom.Element;

/**
 * This interface is responsible for abstracting the rendering of stylistic
 * markup to emulate style properties, for protocols such as HTML3.2 and WML
 * which do not support CSS at all.
 * <p>
 * It provides a simple-to-use "facade" interface for the protocols to use.
 */
public interface StyleEmulationRenderer {

    /**
     * Write stylistic markup to emulate the style provided.
     *
     * @param element the output buffer to write to.
     */
    void applyStyleToElement(Element element);

    /**
     * Tell the renderer that the specified element should not have
     * styling adding to it.
     */
    void exclude(String element);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9184/3	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6183/1	tom	VBM:2004101801 Changed PAPI to use the StyleEngine from the DeviceTheme for styling

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Jul-04	4897/1	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 12-Jul-04	4783/4	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 ===========================================================================
*/
