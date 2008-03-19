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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/protocols/TestDOMProtocol.java,v 1.1 2003/03/14 16:37:28 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Mar-03    Doug            VBM:2003030409 - Created. A Testable 
 *                              DOMProtocol
 * 27-May-03    Byron           VBM:2003051904 - Updated TestDOMProtocol in
 *                              order to get/set the style.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.protocols.layouts.ContainerInstance;

/**
 * Implement the testable methods to create a testable Protocol.
 * All the javadoc for these methods is defined in the interfaces.
 */
public class TestDOMProtocol extends DOMProtocol
        implements DOMProtocolTestable {

    /**
     * The document 
     */ 
    protected Document document;

    public TestDOMProtocol(
            ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    // Javadoc inherited from superclass
    public void setPageHead(PageHead value) {
        this.pageHead = value; 
    }

    // Javadoc inherited from superclass
    public void setPageBuffer(DOMOutputBuffer pageBuffer) {
        this.pageBuffer = pageBuffer;
    }

    // Javadoc inherited from superclass
    public void setStyleSheetRenderer(StyleSheetRenderer renderer) {
        this.styleSheetRenderer = renderer;
    }

    // Javadoc inherited.
    public void setCurrentBuffer(ContainerInstance containerInstance,
            DOMOutputBuffer buffer) {
    }

    // Javadoc inherited from superclass
    public Document getDocument() {
        return document;
    }
    
    // Javadoc inherited from superclass
    public void setDocument(Document document) {
        this.document = document;
    }

    // javadoc inherited
    public String defaultMimeType() {
        return "";
    }

    public void setSupportsNestedTables(boolean value) {
        supportsNestedTables = value;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 23-Jun-05	8833/1	pduffin	VBM:2005042901 Addressing review comments

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 12-Nov-04	6135/1	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 06-Apr-04	3429/3	philws	VBM:2004031502 MenuLabelElement implementation

 25-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 07-Jul-03	728/1	adrian	VBM:2003052001 fixed pane attribute generation

 04-Jul-03	680/1	adrian	VBM:2003052001 Fixed bug in pane attribute and styleclass rendering

 ===========================================================================
*/
