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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;

/**
 * This interface is responsible for defining the interface of
 * stategies that will allow whitespace and/or non blocking space
 * to be added before and/or after an element.
 * <p>
 * This behaviour is required as some devices do not honour spacing within
 * inline styling elements and the anchor element.
 * </p>
 * <p>
 * e.g, A small number of devices render,
 * This is &lt;strong&gt;strong&lt;/strong&gt; and here is some more text. as
 * <br>
 * This isstrongand here is some more text.
 * </p>
 * <p>
 * Please refer to Mantis VBM:2005071403 for more examples of the behavior
 * that is being addressed.
 * </p>
 */
public interface WhiteSpaceFixStrategy {
    /**
     * Adds the required space (whitespace or non blocking) before the
     * supplied element.
     *
     * @param element the element requiring space to be added.
     * @param isOpen indicates if this element is an opening element.
     */
    public void fixUpSpace(Element element, boolean isOpen);

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10675/2	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 ===========================================================================
*/
