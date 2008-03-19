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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/TestDOMOutputBuffer.java,v 1.5 2003/04/23 09:44:20 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-03    Geoff           VBM:2003012101 - Created.
 * 06-Mar-03    Sumit           VBM:2003022605 - Removed extra imports
 * 28-Mar-03    Geoff           VBM:2003031711 - Update deprecation comments
 *                              to try and explain the different ways that
 *                              DOM output is being collected.
 * 07-Apr-03    Geoff           VBM:2003040305 - Update comments to add another
 *                              way of dealing with DOM content comparison.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Element;

import java.io.StringWriter;
import java.io.Writer;

/**
 * A DOMOutputBuffer which is "designed" for use with test cases.
 * <p>
 * Note that the original "design" for this was simply to collect together all
 * the existing usages demonstrated in the various inner classes which had 
 * previously extended DOMOutputBuffer before the existance of this class,
 * so the code is not necessarily the best at the moment. However, with 
 * continued use and more refactoring, this should evolve into something which 
 * is useful for all test cases.
 * <p> 
 * Make sure you run ALL the test cases if you modify this class! 
 * <p>
 * NOTE: several of the methods of this class are to do with collecting output
 * from the output buffer after it is finished. There are two basic strategies
 * for this, the first is to collect the entire output as a string, the second
 * is to inspect the DOM elements created. Of these two basic strategies, there
 * are quite a few variations used in the existing test cases, as they were all
 * implemented separately. Neither of the two here are optimal in my opinion,
 * which is why I have deprecated them. The variations I have discovered so far
 * are:
 * <ul>
 *   <li>
 *      Use {@link #getCurrentElement} to access DOM nodes, 
 *      this seems reasonable and I have added 
 *      {@link DOMProtocolTestAbstract#checkElementEquals}, etc based on this.
 *   </li>
 *   <li>
 *      Use {@link #popElement} to access DOM nodes, this seems like a 
 *      duplication of the above so I have deprecated it.
 *   </li>
 *   <li>
 *      Use {@link com.volantis.mcs.protocols.html.XHTMLBasicTestCase#bufferToString}
 *      (which is based on {@link com.volantis.mcs.dom.DOMUtilities#toString})
 *      to get the output as a String, this seems reasonable.
 *   </li>
 *   <li>
 *      Use {@link #getWriter}, and cast it to a {@link StringWriter} to get
 *      the output as a String, seems like a duplication of the above so I have
 *      deprecated it. 
 *   </li>
 *   <li>
 *      Use {@link DOMProtocolTestAbstract.MyPageOutputBuffer#getWriter} 
 *      which is a variation on this class, it has a to do to that effect. 
 *   </li>
 * </ul>
 * <p> 
 * Note that I have also discovered 
 * {@link com.volantis.mcs.protocols.wml.WMLRootTestCase#transformMarkup} to 
 * normalise expected markup strings, this should be combined with whatever 
 * approach you choose above.  
 */ 
public class TestDOMOutputBuffer extends DOMOutputBuffer {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * This constructor leaves the class in the same state as if you had
     * created a normal DOMOutputBuffer and then called initialise(protocol).
     * <p>
     * If you do call initialise(protocol), however, it will behave just like
     * the normal DOMOutputBuffer and use a the protocol's real DOMFactory.
     */ 
    public TestDOMOutputBuffer() {
    }

    /**
     * @deprecated You can achieve the same result using better alternatives; 
     *      see the class comment. 
     * 
     * @return the popped Element
     */ 
    public Element popElement() {
        return super.popElement();
    }
    
    // these methods are useful 
    
    StringWriter writer = new StringWriter();

    /**
     * @deprecated You can achieve the same result using better alternatives; 
     *      see the class comment.
     *  
     * @return the Writer
     */ 
    public Writer getWriter() {
        return writer;
    }

    public boolean isEmpty() {
        return false;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 09-Aug-05	9211/1	pabbott	VBM:2005080902 End to End CSS emulation test

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
