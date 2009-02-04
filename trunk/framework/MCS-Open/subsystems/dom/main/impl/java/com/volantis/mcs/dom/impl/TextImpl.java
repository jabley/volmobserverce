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
 * $Header: /src/voyager/com/volantis/mcs/dom/Text.java,v 1.3 2002/03/28 19:14:49 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 28-Mar-02    Allan           VBM:2002022007 - Added char[], int, int version
 *                              of append().
 * 30-Apr-03    Steve           VBM:2003041606 - Added char version of append
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom.impl;

import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.DOMVisitor;
import com.volantis.mcs.dom.Text;
import com.volantis.synergetics.cornerstone.utilities.ReusableStringBuffer;

/**
 * This class represents a block of text in the dom.
 * <p>
 * For efficiency reasons sometimes it is useful to be able to store contents
 * which contain encoded entities.
 * </p>
 */
public class TextImpl
        extends CharacterNodeImpl
        implements Text {


    public TextImpl(DOMFactory factory) {
        super(factory);

        contents = new ReusableStringBuffer();
    }

    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    public boolean isEncoded() {
        return encoded;
    }


    // Javadoc inherited.
    public void accept(DOMVisitor visitor) {
        visitor.visit(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-05	10701/1	pduffin	VBM:2005110905 Porting forward changes from 3.5

 08-Dec-05	10675/1	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 22-Jun-05	8856/3	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 ===========================================================================
*/
