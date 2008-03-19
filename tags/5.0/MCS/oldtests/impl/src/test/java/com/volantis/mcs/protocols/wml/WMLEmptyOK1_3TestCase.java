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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;

/**
 * Test the WMLEmptyOK1_3 protocol.
 */
public class WMLEmptyOK1_3TestCase
        extends WMLVersion1_3TestCase {

    /**
     * Construct this object.
     */
    public WMLEmptyOK1_3TestCase(String name) {
        super(name);
    }

    // javadoc inherited
    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = builder.build(
                new TestProtocolRegistry.TestWMLEmptyOKFactory(),
                internalDevice);
        return protocol;
    }

    /**
     * Tests the addition of the emptyok attribute to an input type if the the
     * input field validation text beings with lower case
     */
    public void testAddTextInputValidation() {

        // If no validation is specified then expect the emptyok attribute to
        // be set due to issues with the device.
        checkTextInputValidation(null, "true", null);

        // If the validation allows empty field then expect emptyok attribute
        // to be set and the format attribute to be set.
        checkTextInputValidation("n:####", "true", "NNNN");

        // If the validation does not allow empty fields then expect emptyok
        // attribute to be set anyway and the format attribute to be set.
        //
        // Not sure if this is strictly necessary, it may be that this
        // is going too far but it is what it used to do. What is probably
        // needed is simply to add empty ok if validation has not been
        // specified as tested in the above test. The forcing of empty ok was
        // done before support was added for using lower case starting letters
        // to indicate that the value could be empty.
        checkTextInputValidation("N:####", "true", "NNNN");
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/2	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 16-Feb-05	6129/5	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/3	matthew	VBM:2004102019 yet another supermerge

 23-Nov-04	6129/1	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 25-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (sigh, rework as per dougs request)

 04-Sep-03	1331/1	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 ===========================================================================
*/
