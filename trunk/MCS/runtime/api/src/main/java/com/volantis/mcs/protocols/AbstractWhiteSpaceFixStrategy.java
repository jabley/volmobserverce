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

import com.volantis.mcs.dom.Text;

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
public abstract class AbstractWhiteSpaceFixStrategy
        implements WhiteSpaceFixStrategy {

    /**
     * Constant for the whitespace character.
     */
    protected static final String SINGLE_WHITESPACE = " ";

    /**
     * Constant for the entity nbsp, which represents non blocking
     * space.
     */
    protected static final String NON_BREAKING_SPACE = VolantisProtocol.NBSP;

    /**
     * Prepend the given String to the Text text passed in.
     * @param text the Text text that the String will be appended to
     * @param toPrepend the String to prePend
     */
    protected void prependText(Text text, String toPrepend) {
        char[] contents = text.getContents();
        int length = text.getLength();
        text.clearContents();
        text.append(toPrepend);
        text.append(contents, 0, length);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10803/2	pabbott	VBM:2005120901 Fix white space problem on LG-C1100

 08-Dec-05	10675/3	pduffin	VBM:2005110905 Ported forward some white space fixes from 3.2.3

 03-Aug-05	9139/3	doug	VBM:2005071403 Fixed whitespace issues

 02-Aug-05	9139/1	doug	VBM:2005071403 Finished off whitespace fixes

 22-Jul-05	9108/1	rgreenall	VBM:2005071403 Partial implementation.

 ===========================================================================
*/
